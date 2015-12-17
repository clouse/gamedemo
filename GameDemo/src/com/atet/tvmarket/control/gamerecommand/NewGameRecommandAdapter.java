package com.atet.tvmarket.control.gamerecommand;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.atet.tvmarket.R;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.view.recyclerview.BaseAdapter;
import com.atet.tvmarket.view.recyclerview.BaseViewHolder;

public class NewGameRecommandAdapter extends BaseAdapter {

	private RecyclerView mRecyclerView;
	private NewGameRecommandActivity mContext;
	private ImageFetcher mImageFetcher;
	private int flag=0;
	private int startPos=0;
	private int endPos=3;
	private List<GameInfo> games = new ArrayList<GameInfo>();
	private List<GameInfo> myGames = new ArrayList<GameInfo>();
	
	public NewGameRecommandAdapter(RecyclerView recyclerView,NewGameRecommandActivity context,ImageFetcher mImageFetcher){
		this.mRecyclerView = recyclerView;
		this.mContext = context;
		this.mImageFetcher = mImageFetcher;
	}

	public void setData(List<GameInfo> games){
		this.games.clear();
		this.games.addAll(games);
		this.myGames.clear();
		startPos=0;
		if(games.size()>=4){
			for(int i=0;i<4;i++){
				myGames.add(games.get(i));
			}
			 endPos=3;
		}
		else{
			myGames.addAll(games);
			 endPos=games.size()-1;
		}
		notifyDataSetChanged();
	}
	
	@Override
	public int getItemCount() {
		return myGames.size()-1;
	}

	@Override
	public void onBindViewHolder(BaseViewHolder holder, int position) {
		GameInfo gameInfo = (GameInfo) myGames.get(position+1);
		ItemViewHolder viewHolder = (ItemViewHolder) holder;
		viewHolder.getLayout().setTag(position);
		viewHolder.setGameCover(gameInfo.getMiddlePhoto());
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
	
	private void updatePos(boolean isRight){
		if(isRight){
			if(startPos<games.size()-1){
				startPos++;
			}
			else{
				startPos=0;
			}
			if(endPos<games.size()-1){
				endPos++;
			}
			else{
				endPos = 0;
			}
		}
		else{
			if(startPos==0){
				startPos = games.size()-1;
			}
			else{
				startPos--;
			}
			if(endPos==0){
				endPos=games.size()-1;
			}
			else{
				endPos--;
			}	
		}
	}
	
	private void execAnimator(final boolean isRight){
		//mContext.coverOutAnimator(isRight);
		mContext.viewAnimator();
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				mContext.visibleView(games.get(startPos));
			//	mContext.coverInAnimator(isRight);
			}
		}, 400);
	}
	
	private void addItem(boolean isRight){
		GameInfo data=null;
		if(isRight){
			mRecyclerView.setItemAnimator(new DefaultItemAnimator());
			data = games.get(endPos);
			myGames.add(data);
			notifyItemInserted(myGames.size());
		}
		else{
			mRecyclerView.setItemAnimator(new DefaultItemAnimator());
			data = games.get(startPos);
			myGames.add(0,data);
			notifyItemInserted(0);
		}
	}
	
	private void removeItem(boolean isRight){
		if(isRight){
			mRecyclerView.setItemAnimator(new DefaultItemAnimator());
			if(myGames.size()>0){
				myGames.remove(0);
				notifyItemRemoved(0);
			}
		}
		else{
			mRecyclerView.setItemAnimator(new DefaultItemAnimator());
			if(myGames.size()>0){
				myGames.remove(myGames.size()-1);
				notifyItemRemoved(myGames.size());
			}
		}
	}
	
	public void rightMoveDatas(){
		if(games!=null && games.size()>0){
			updatePos(true);
			execAnimator(true);
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					removeItem(true);
					addItem(true);
				}
			},400);
			
		}
	}
	
	public void leftMoveDatas(){
		if(games!=null && games.size()>0){
			updatePos(false);
			execAnimator(false);
			new Handler().post(new Runnable() {
				
				@Override
				public void run() {
					removeItem(false);
					addItem(false);
				}
			});
		}
	}
}
