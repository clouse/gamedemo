package com.atet.tvmarket.control.gamerecommand;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.atet.tvmarket.R;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.view.recyclerview.BaseAdapter;
import com.atet.tvmarket.view.recyclerview.BaseViewHolder;

public class NewGameRecommandAdapter1 extends BaseAdapter {

	private RecyclerView mRecyclerView;
	private NewGameRecommandActivity mContext;
	private ImageFetcher mImageFetcher;
	private List<GameInfo> games = new ArrayList<GameInfo>();
	private boolean isfirst=false;
	//private List<GameInfo> myGames = new ArrayList<GameInfo>();
	
	public NewGameRecommandAdapter1(RecyclerView recyclerView,NewGameRecommandActivity context,ImageFetcher mImageFetcher){
		this.mRecyclerView = recyclerView;
		this.mContext = context;
		this.mImageFetcher = mImageFetcher;
	}

	public void setData(List<GameInfo> games,int flag){
		this.games.clear();
		this.games.addAll(games);
		if(flag==0){
			isfirst = true;
			notifyDataSetChanged();
		}
		else{
			isfirst = false;
			notifyItemRangeChanged(0, getItemCount());
		}
	}
	
	@Override
	public int getItemCount() {
		if(games.size()>4){
			return 4;
		}
		return games.size();
	}

	@Override
	public void onBindViewHolder(BaseViewHolder holder, int position) {
		GameInfo gameInfo = (GameInfo) games.get(position);
		ItemViewHolder viewHolder = (ItemViewHolder) holder;
		viewHolder.itemView.setTag(position);
		viewHolder.setGameCover(gameInfo.getMiddlePhoto());
		
		//LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		MarginLayoutParams params = new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT,MarginLayoutParams.WRAP_CONTENT);
		params.topMargin = (int) ScaleViewUtils.resetTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.top_margin));
		if(position==0){
			params.leftMargin = (int) ScaleViewUtils.resetTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.left_margin_big));
			params.rightMargin  = (int) ScaleViewUtils.resetTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.right_margin_big));
		}
		else{
			params.leftMargin = 0;
			params.rightMargin  = (int) ScaleViewUtils.resetTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.right_margin_small));
		}
		viewHolder.itemView.setLayoutParams(params);
		if(position==0 && isfirst){
			viewHolder.itemView.setScaleX(1.1f);
			viewHolder.itemView.setScaleY(1.1f);
		}
	}

	@Override
	public BaseViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
		View root = LayoutInflater.from(container.getContext()).inflate(R.layout.gamerecommand_item, container, false);
		ScaleViewUtils.scaleView(root);
		return new ItemViewHolder(root);
	}

	class ItemViewHolder extends BaseViewHolder{
		
		private RelativeLayout layout;
		private ImageView cover;
		
		public ItemViewHolder(View itemView) {
			super(itemView);
			layout = (RelativeLayout)itemView.findViewById(R.id.rl_layout);
			cover = (ImageView)itemView.findViewById(R.id.iv_cover);
		}
		
		public RelativeLayout getLayout() {
			return layout;
		}
		
		public void setGameCover(String coverUrl){
			mImageFetcher.loadImage(coverUrl, cover, R.drawable.default_recommand);
		}
	}
}
