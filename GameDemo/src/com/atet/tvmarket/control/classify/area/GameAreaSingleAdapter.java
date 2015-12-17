package com.atet.tvmarket.control.classify.area;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
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
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.mygame.activity.GameDetailActivity;
import com.atet.tvmarket.entity.dao.TypeToGame;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.view.recyclerview.BaseAdapter;
import com.atet.tvmarket.view.recyclerview.BaseViewHolder;

public class GameAreaSingleAdapter extends BaseAdapter {

	private RecyclerView mRecyclerView;
	private GameAreaActivity context;
	private ImageFetcher mImageFetcher;
	private View lastFocusView=null;
	private List<TypeToGame> games = new ArrayList<TypeToGame>();
	public GameAreaSingleAdapter(RecyclerView recyclerView,GameAreaActivity context,ImageFetcher mImageFetcher){
		this.mRecyclerView = recyclerView;
		this.context = context;
		this.mImageFetcher = mImageFetcher;
	}
	
	public void setData(List<TypeToGame> games) {
		
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		
		this.games.clear();
		this.games.addAll(games);
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		return games.size();
	}

	@Override
	public void onBindViewHolder(BaseViewHolder holder, int position) {
		TypeToGame typeToGame = games.get(position);
		ItemViewHolder viewHolder = (ItemViewHolder) holder;
		viewHolder.itemView.setTag(position);
		if(typeToGame!=null){
			viewHolder.setGameName(typeToGame.getGameInfo().getGameName());
			viewHolder.setGameCover(typeToGame.getGameInfo().getErectPhoto());
			viewHolder.setGameId(typeToGame.getGameId());
		}
		if(position==0){
			if(lastFocusView==null){
				viewHolder.itemView.requestFocusFromTouch();
			}
		}
		
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		if(position==getItemCount()-1){
			layoutParams.rightMargin = (int) ScaleViewUtils.resetTextSize(40);
		}
		else{
			layoutParams.rightMargin = (int) ScaleViewUtils.resetTextSize(-81);
		}
		viewHolder.itemView.setLayoutParams(layoutParams);
		
	}

	@Override
	public BaseViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
		View root = LayoutInflater.from(container.getContext()).inflate(R.layout.gameclassify_area_single_item, container,false);
		return new ItemViewHolder(root);
	}

	class ItemViewHolder extends BaseViewHolder implements OnFocusChangeListener,OnClickListener,OnKeyListener,OnTouchListener{
		private RelativeLayout content;
		private ImageView border;
		private ImageView gameCover;
		private TextView gameName;
		private String gameId;
		private TypeToGame typeToGame;
		private View view;
		
		public ItemViewHolder(View itemView) {
			super(itemView);
			ScaleViewUtils.scaleView(itemView);
			
			content = (RelativeLayout)itemView.findViewById(R.id.rl_content);
			border = (ImageView)itemView.findViewById(R.id.iv_border);
			gameCover = (ImageView)itemView.findViewById(R.id.iv_cover);
			gameName = (TextView)itemView.findViewById(R.id.tv_name);
			itemView.setOnFocusChangeListener(this);
			itemView.setOnClickListener(this);
			itemView.setOnKeyListener(this);
			itemView.setOnTouchListener(this);
		}
		
		public void setGameCover(String url){
			mImageFetcher.loadImage(url, gameCover, R.drawable.default_vertical);
		}
		
		public void setGameName(CharSequence name){
			gameName.setText(name);
		}

		public void setGameId(String gameId){
			this.gameId = gameId;
		}
		
		public void setTypeToGame(TypeToGame typeToGame){
			this.typeToGame = typeToGame;
		}
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus){
				v.requestLayout();
				int location[] = new int[2];
				v.getLocationOnScreen(location);
				if(location[0]-v.getWidth()<mRecyclerView.getLeft()){
					mRecyclerView.smoothScrollBy(-v.getWidth(), 0);
				}
				if(location[0]+v.getWidth()>mRecyclerView.getRight()){
					mRecyclerView.smoothScrollBy(v.getWidth(), 0);
				}
				border.setVisibility(View.VISIBLE);
				content.setScaleX(1.1f);
				content.setScaleY(1.1f);
				if(v.isInTouchMode() && v == view){
					v.performClick();
				}
				else
				{
					view = null;
				}
			}
			else{
				v.requestLayout();
				border.setVisibility(View.INVISIBLE);
				content.setScaleX(1.0f);
				content.setScaleY(1.0f);
			}
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(context,GameDetailActivity.class);
			intent.putExtra(Constant.GAMECENTER, 3);
			intent.putExtra("gameId", gameId);
			context.startActivity(intent);
		}

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if(event.getAction()==KeyEvent.ACTION_DOWN){
				int pos = mRecyclerView.getChildAdapterPosition(v);
				Log.i("life", "pos:"+pos);
				if(keyCode==KeyEvent.KEYCODE_DPAD_LEFT){
					if(pos-1>=0){
						mRecyclerView.getLayoutManager().findViewByPosition(pos-1).requestFocus();
						return true;
					}
				}
			}
			return false;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				OnFocusChangeListener listener = v.getOnFocusChangeListener();
				if(listener!=null){
					view = v;
				}
			}
			return false;
		}
		
	}

	public void setLastFocusView(View lastFocusView) {
		this.lastFocusView = lastFocusView;
	}
}
