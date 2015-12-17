package com.atet.tvmarket.control.classify;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atet.tvmarket.R;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.home.view.MarqueeTextView;
import com.atet.tvmarket.control.mygame.activity.GameDetailActivity;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.view.recyclerview.BaseAdapter;
import com.atet.tvmarket.view.recyclerview.BaseViewHolder;

public class GameClassifyRankingPanelAdapter extends BaseAdapter {

	private Context context;
	private ImageFetcher mImageFetcher;
	private RecyclerView recyclerView;
	List<GameInfo> games = new ArrayList<GameInfo>();
	public GameClassifyRankingPanelAdapter(Context context,RecyclerView recyclerView,ImageFetcher mImageFetcher){
		this.recyclerView = recyclerView;
		this.context = context;
		this.mImageFetcher = mImageFetcher;
	}
	
	public void setData(List<GameInfo> games) {
		this.games.clear();
		this.games.addAll(games);
		notifyDataSetChanged();
	}
	
	@Override
	public int getItemCount() {
		if(games.size()==0){
			return 6;
		}
		return games.size();
	}

	@Override
	public void onBindViewHolder(BaseViewHolder holder, int postion) {
		GameHolder gameHolder = (GameHolder) holder;
		if(games.size()>0){
			GameInfo gameInfo = (GameInfo) games.get(postion);
			gameHolder.itemView.setTag(postion);
			gameHolder.setCover(gameInfo.getMinPhoto());
			gameHolder.setGameName(postion+1, gameInfo.getGameName());
			gameHolder.setGameId(gameInfo.getGameId());
			if(postion==0||postion==1){
				gameHolder.itemView.setNextFocusUpId(R.id.tab_categroy);
			}
		}
		else{
			if(postion==0||postion==1){
				gameHolder.itemView.setNextFocusUpId(R.id.tab_categroy);
			}
			gameHolder.setCover("");
			gameHolder.setGameName(-1, "大话西游");
		}
	}

	@Override
	public BaseViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(container.getContext());
		View root = inflater.inflate(R.layout.gameclassify_rankingpanel_item, container, false);
		return new GameHolder(root);
	}
	
	class GameHolder extends BaseViewHolder implements OnFocusChangeListener,OnClickListener,OnKeyListener{

		private ImageView border;
		private ImageView cover;
		private TextView name;
		private String gameId;
		public GameHolder(View itemView) {
			super(itemView);
			ScaleViewUtils.scaleView(itemView);
			border = (ImageView)itemView.findViewById(R.id.iv_border);
			cover = (ImageView) itemView.findViewById(R.id.iv_game_cover);
			name = (TextView)itemView.findViewById(R.id.tv_game_name);
			itemView.setOnFocusChangeListener(this);
			itemView.setOnClickListener(this);
			itemView.setOnKeyListener(this);
		}

		public void setCover(String coverUrl){
			if(mImageFetcher!=null){
				mImageFetcher.loadImage(coverUrl, cover, R.drawable.gameranking_item_icon);
			}
		}
		
		public void setGameName(int order,String gameName){
			if(order==-1){
				name.setText("");
			}
			else{
				name.setText(order+"."+gameName);
			}
			
		}
		
		public void setGameId(String gameId){
			this.gameId = gameId;
		}
		
		@Override
		public void onClick(View v) {
			if(gameId!=null && gameId!=""){
				Intent intent = new Intent(context, GameDetailActivity.class);
				intent.putExtra("gameId", gameId);
				context.startActivity(intent);
			}
		}

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus){
				v.requestLayout();
				border.setImageResource(R.drawable.game_detail_focus);
				//name.requestFocus();
				cover.setScaleX(1.1f);
				cover.setScaleY(1.1f);
				if(v.isInTouchMode()){
					v.performClick();
				}
			}
			else{
				v.requestLayout();
				border.setImageResource(R.color.transparent);
				cover.setScaleX(1.0f);
				cover.setScaleY(1.0f);
			}
		}

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if(event.getAction() == KeyEvent.ACTION_DOWN){
				int pos = recyclerView.getChildAdapterPosition(v);
				GameClassifyRankingPanel panel = (GameClassifyRankingPanel) v.getParent().getParent().getParent().getParent();
				RecyclerView superRecyclerView= (RecyclerView) panel.getParent();
				int panelPos = superRecyclerView.getChildAdapterPosition(panel);
				if((pos==4||pos==5)&&keyCode==KeyEvent.KEYCODE_DPAD_DOWN){
					panel.getMore().requestFocus();
					return true;
				}else if(pos%2==1&&keyCode==KeyEvent.KEYCODE_DPAD_RIGHT){
					GameClassifySinglePanel singlePanel = (GameClassifySinglePanel) superRecyclerView.getLayoutManager().findViewByPosition(panelPos+1);
					singlePanel.getLayout().requestFocus();
					return true;
				}
				
			}
			return false;
		}
	}
}
