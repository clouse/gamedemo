package com.atet.tvmarket.control.classify.special.detail;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atet.tvmarket.R;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.classify.detail.ThirdGameDetailActivity;
import com.atet.tvmarket.control.classify.special.GameSpecialActivity;
import com.atet.tvmarket.control.mygame.activity.MyGameActivity;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.entity.dao.TopicToGame;
import com.atet.tvmarket.model.DataConfig;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.ScreenShot;
import com.atet.tvmarket.view.recyclerview.BaseAdapter;
import com.atet.tvmarket.view.recyclerview.BaseViewHolder;

public class GameSpecialDetailAdapter extends BaseAdapter {

	private GameSpecialActivity context;
	private RecyclerView mRecyclerView;
	private ImageFetcher mImageFetcher;
	private View lastFocusView=null;
	
	List<TopicToGame> games = new ArrayList<TopicToGame>();
	List<TopicToGame> myGames = new ArrayList<TopicToGame>();
	
	public GameSpecialDetailAdapter(RecyclerView recyclerView,GameSpecialActivity context,ImageFetcher mImageFetcher){
		this.mRecyclerView = recyclerView;
		this.context = context;
		this.mImageFetcher = mImageFetcher;
	}
	
	public void setLastFocusView(View lastFocusView) {
		this.lastFocusView = lastFocusView;
	}

	public void setData(List<TopicToGame> games) {
		clearData();
		this.games.addAll(games);
		notifyDataSetChanged();
	}

	public void clearData(){
		this.games.clear();
		myGames.clear();
	}
	
	public void addData(TopicToGame game){
		myGames.add(game);
		notifyItemInserted(getItemCount());
		
		if(this.games.size()>6 && myGames.size()==6){
			for(int i=6;i<games.size();i++){
				myGames.add(games.get(i));
			}
			notifyItemInserted(getItemCount());
		}
		
	}
	
	@Override
	public int getItemCount() {
		return myGames.size();
	}

	@Override
	public void onBindViewHolder(BaseViewHolder holder, int position) {
		TopicToGame gameInfo = (TopicToGame)myGames.get(position);
		ItemViewHolder viewHolder = (ItemViewHolder) holder;
		viewHolder.getLayout().setTag(position);
		if(gameInfo!=null){
			if(gameInfo.getType() == DataConfig.GAME_TYPE_COPYRIGHT){
				viewHolder.setGameName(gameInfo.getGameInfo().getGameName());
				if(gameInfo.getGameInfo().getGameDownCount()==null){
					viewHolder.setDownCount(0);
				}
				else{
					viewHolder.setDownCount(gameInfo.getGameInfo().getGameDownCount());
				}
				if(gameInfo.getGameInfo().getStartLevel()==null){
					viewHolder.setCommentScore(0f);
				}
				else{
					viewHolder.setCommentScore(Float.valueOf(gameInfo.getGameInfo().getStartLevel().toString()));
				}
				
				viewHolder.setGameIcon(gameInfo.getGameInfo().getMiddlePhoto());
				viewHolder.setGameSize(gameInfo.getGameInfo().getGameSize()/1024/1024);
				viewHolder.setGameId(gameInfo.getGameInfo().getGameId());
				viewHolder.setType(gameInfo.getType());
				viewHolder.setGameInfo(gameInfo.getGameInfo());
			}
			else{
				viewHolder.setGameName(gameInfo.getThirdGameInfo().getGameName());
				if(gameInfo.getThirdGameInfo().getDownloadCount()==null){
					viewHolder.setDownCount(0);
				}
				else{
					viewHolder.setDownCount(gameInfo.getThirdGameInfo().getDownloadCount());
				}
				if(gameInfo.getThirdGameInfo().getStartLevel()==null){
					viewHolder.setCommentScore(0f);
				}
				else{
					viewHolder.setCommentScore(Float.valueOf(gameInfo.getThirdGameInfo().getStartLevel().toString()));
				}
				viewHolder.setGameIcon(gameInfo.getThirdGameInfo().getMinPhoto());
				viewHolder.setGameSize(gameInfo.getThirdGameInfo().getGameSize()/1024/1024);
				viewHolder.setGameId(gameInfo.getThirdGameInfo().getGameId());
				viewHolder.setType(gameInfo.getType());
			}
		}
		if(position==0){
			if(lastFocusView==null){
				viewHolder.getLayout().requestFocus();
			}
		}
	}

	@Override
	public BaseViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
		View root = LayoutInflater.from(container.getContext()).inflate(R.layout.gameclassify_speical_detail_item, container,false);
		return new ItemViewHolder(root);
	}

	class ItemViewHolder extends BaseViewHolder implements OnFocusChangeListener,OnClickListener,OnKeyListener,OnTouchListener{
		private RelativeLayout layout,detail;
		private ImageView gameIcon,border;
		private TextView gameName,downCount,gameSize;
		private RatingBar commentScore;
		private String gameId;
		private int type;
		private GameInfo gameInfo;
		private View view;
		
		public ItemViewHolder(View itemView) {
			super(itemView);
			ScaleViewUtils.scaleView(itemView);
			
			layout = (RelativeLayout)itemView.findViewById(R.id.rl_layout);
			//border = (ImageView)itemView.findViewById(R.id.iv_border);
			gameIcon = (ImageView)itemView.findViewById(R.id.iv_cover);
			gameName = (TextView)itemView.findViewById(R.id.tv_name);
			gameSize = (TextView)itemView.findViewById(R.id.tv_download_size);
			detail = (RelativeLayout)itemView.findViewById(R.id.rl_layout_detail);
			downCount = (TextView)itemView.findViewById(R.id.tv_download_count);
			commentScore = (RatingBar)itemView.findViewById(R.id.rb_comment_score);
			
			layout.setOnFocusChangeListener(this);
			layout.setOnClickListener(this);
			layout.setOnKeyListener(this);
			layout.setOnTouchListener(this);
		}
		
		/*public void setBorder(int visibleVal){
			border.setVisibility(visibleVal);
		}*/
		
		public void setGameIcon(String url){
			mImageFetcher.loadImage(url, gameIcon, R.drawable.default_recommand);
		}
		
		public void setGameName(CharSequence name){
			gameName.setText(name);
		}
		
		public void setDetail(int visibleVal){
			detail.setVisibility(visibleVal);
		}
		
		public void setDownCount(int count){
			downCount.setText(count+"");
		}
		
		public void setGameSize(float size){
			gameSize.setText(size+"M");
		}
		
		public void setCommentScore(float score){
			commentScore.setRating(score);
		}
		
		public RelativeLayout getLayout() {
			return layout;
		}
		
		public void setGameId(String gameId){
			this.gameId = gameId;
		}

		public void setType(int type){
			this.type = type;
		}
		
		public void setGameInfo(GameInfo mGameInfo){
			this.gameInfo = mGameInfo;
		}
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus){
				
				int vlocation[]=new int[2];
				((View) v.getParent()).getLocationInWindow(vlocation);
				Log.i("life", vlocation[0]+"");
				int rlocation[]=new int[2];
				mRecyclerView.getLocationOnScreen(rlocation);
				
				if(vlocation[0]+((View) v.getParent()).getWidth()>rlocation[0]+mRecyclerView.getWidth()){
					mRecyclerView.smoothScrollBy(((View) v.getParent()).getWidth(), 0);
				}else if(vlocation[0]<((View) v.getParent()).getWidth()){
					mRecyclerView.smoothScrollBy(-((View) v.getParent()).getWidth(), 0);
				}
				lastFocusView=v;
				//setBorder(View.VISIBLE);
				layout.setBackgroundResource(R.drawable.special_detail_border);
				setDetail(View.VISIBLE);
				if(v.isInTouchMode() && v == view){
					v.performClick();
				}
				else{
					view = null;
				}
			}
			else{
				//setBorder(View.INVISIBLE);
				layout.setBackgroundResource(android.R.color.transparent);
				setDetail(View.INVISIBLE);
			}
		}

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if(event.getAction()==KeyEvent.ACTION_DOWN){
				if(keyCode==KeyEvent.KEYCODE_DPAD_RIGHT){
					int pos = (Integer) v.getTag();
					if(!context.isFinishing){
						if(pos==4 || pos==5){
							return true;
						}
					}
					
				}else if(keyCode==KeyEvent.KEYCODE_DPAD_LEFT){
					
				}
			}
			return false;
		}

		@Override
		public void onClick(View v) {
			if(type==DataConfig.GAME_TYPE_COPYRIGHT){
				//数据统计来源
				gameInfo.setTypeName("GameTopic");
				context.downloadGameInfo = gameInfo;
				MyGameActivity.chekAgeDownloadGame(context, gameInfo);
				//MyGameActivity.addToMyGameList(context, gameInfo);
			}
			else{
				Intent intent = new Intent(context,ThirdGameDetailActivity.class);
				intent.putExtra("ThirdGameId", gameId);
				context.startActivity(intent);
			}
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			Log.i("life", "touchv:"+v.toString());
			if (event.getAction() == MotionEvent.ACTION_UP) {
				OnFocusChangeListener listener = v.getOnFocusChangeListener();
				if(listener!=null){
					view = v;
				}
			}
			return false;
		}
		
	}
}
