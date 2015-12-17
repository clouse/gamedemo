package com.atet.tvmarket.control.classify.ranking;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atet.tvmarket.R;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.view.recyclerview.BaseAdapter;
import com.atet.tvmarket.view.recyclerview.BaseViewHolder;

public class GameRankingAdapter extends BaseAdapter {

	private Context context;
	private ImageFetcher mImageFetcher;
	private RecyclerView mRecyclerView;
	
	SparseArray<List<GameInfo>> rankingList = new SparseArray<List<GameInfo>>();
	
	public GameRankingAdapter( Context context,ImageFetcher mImageFetcher,RecyclerView mRecyclerView){
		this.context = context;
		this.mImageFetcher = mImageFetcher;
		this.mRecyclerView = mRecyclerView;
	}
	
	public void setData(SparseArray<List<GameInfo>> rankingList) {
		this.rankingList = rankingList;
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		
		return rankingList.size();
	}

	@Override
	public void onBindViewHolder(BaseViewHolder itemHolder, int position) {
		ItemViewHolder holder = (ItemViewHolder) itemHolder;
		holder.itemView.setTag(position);
		if(position==0){
			holder.setIcon(R.drawable.gameranking_handle_icon1);
			holder.setName("手柄排行");
			holder.setRecyclerView(rankingList.get(position),position);
			holder.setType(position);
		}
		if(position==1){
			holder.setIcon(R.drawable.gameranking_control_icon1);
			holder.setName("遥控器排行");
			holder.setRecyclerView(rankingList.get(position),position);
			holder.setType(position);
		}
	}

	@Override
	public BaseViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
		
		View root = LayoutInflater.from(container.getContext()).inflate(R.layout.activity_gameranking_item, container, false);
		
		return new ItemViewHolder(root);
	}

	class ItemViewHolder extends BaseViewHolder{

		private ImageView icon;
		private TextView text;
		private RecyclerView typeRecyclerView;
		public ItemViewHolder(View itemView) {
			super(itemView);
			ScaleViewUtils.scaleView(itemView);
			icon = (ImageView)itemView.findViewById(R.id.iv_icon);
			text = (TextView)itemView.findViewById(R.id.tv_text);
			typeRecyclerView = (RecyclerView)itemView.findViewById(R.id.rv_list);
		}
		
		
		public void setIcon(int resId){
			mImageFetcher.loadImage("", icon, resId);
		}
		
		public void setName(CharSequence name){
			text.setText(name);
		}
		
		public void setRecyclerView(List<GameInfo> mGameInfos,int type){
			LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
			typeRecyclerView.setLayoutManager(layoutManager);
			GameRankingTypeAdapter typeAdapter = new GameRankingTypeAdapter(context,typeRecyclerView,mImageFetcher,type);
			typeRecyclerView.setAdapter(typeAdapter);
			typeAdapter.setData(mGameInfos);
		}
		
		public void setType(int type){
			typeRecyclerView.setTag(type);
		}
		
	}
}
