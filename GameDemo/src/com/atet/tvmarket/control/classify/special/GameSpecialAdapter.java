package com.atet.tvmarket.control.classify.special;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atet.tvmarket.R;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.entity.dao.GameTopicInfo;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.view.recyclerview.BaseAdapter;
import com.atet.tvmarket.view.recyclerview.BaseViewHolder;

public class GameSpecialAdapter extends BaseAdapter {

	private RecyclerView mRecyclerView;
	private GameSpecialActivity context;
	private ImageFetcher mImageFetcher;
	private View lastFocusView=null;
	private int startPos=0;
	private int endPos=3;
	List<GameTopicInfo> gameTopics = new ArrayList<GameTopicInfo>();
	List<GameTopicInfo> myGameTopics = new ArrayList<GameTopicInfo>(); 
	
	public GameSpecialAdapter(RecyclerView recyclerView,GameSpecialActivity context,ImageFetcher mImageFetcher){
		this.mRecyclerView = recyclerView;
		this.context = context;
		this.mImageFetcher = mImageFetcher;
	}
	
	public void setData(List<GameTopicInfo> gameTopics) {
		this.gameTopics.clear();
		this.gameTopics.addAll(gameTopics);
		myGameTopics.clear();
		startPos=0;
		if(gameTopics.size()>=4){
			for(int i=0;i<4;i++){
				myGameTopics.add(gameTopics.get(i));
			}
			endPos=3;
		}
		else{
			myGameTopics.addAll(gameTopics);
			endPos=gameTopics.size()-1;
		}
		
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		
		if(myGameTopics.size()==0){
			return 0;
		}
		return myGameTopics.size()-1;
	}

	@Override
	public void onBindViewHolder(BaseViewHolder holder, int position) {
		GameTopicInfo topicInfo = (GameTopicInfo)myGameTopics.get(position+1);
		ItemViewHolder viewHolder = (ItemViewHolder) holder;
		if(topicInfo!=null){
			viewHolder.setGameName(topicInfo.getName());
			viewHolder.setGameIcon(topicInfo.getPhoto());
		}
	}

	@Override
	public BaseViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
		View root = LayoutInflater.from(container.getContext()).inflate(R.layout.gameclassify_speical_item, container,false);
		return new ItemViewHolder(root);
	}

	class ItemViewHolder extends BaseViewHolder{
		private RelativeLayout layout;
		private ImageView gameIcon;
		private TextView gameName;
		
		public ItemViewHolder(View itemView) {
			super(itemView);
			ScaleViewUtils.scaleView(itemView);
			
			layout = (RelativeLayout)itemView.findViewById(R.id.rl_layout);
			gameIcon = (ImageView)itemView.findViewById(R.id.iv_cover);
			gameName = (TextView)itemView.findViewById(R.id.tv_name);
			
		}
		public void setGameIcon(String url){
			mImageFetcher.loadImage(url, gameIcon, R.drawable.default_special);
		}
		
		public void setGameName(CharSequence name){
			gameName.setText(name);
		}
		
		public RelativeLayout getLayout() {
			return layout;
		}
	}
	
	private void updatePos(boolean isRight){
		if(isRight){
			if(startPos<gameTopics.size()-1){
				startPos++;
			}
			else{
				startPos=0;
			}
			if(endPos<gameTopics.size()-1){
				endPos++;
			}
			else{
				endPos = 0;
			}
		}
		else{
			if(startPos==0){
				startPos = gameTopics.size()-1;
			}
			else{
				startPos--;
			}
			if(endPos==0){
				endPos=gameTopics.size()-1;
			}
			else{
				endPos--;
			}	
		}
		
		Log.i("life", "pos:"+startPos+","+endPos);
	}
	
	private void execAnimator(final boolean isRight){
		//context.initData((GameTopicInfo)gameTopics.get(startPos));
		//context.coverOutAnimator(isRight);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				//context.coverInAnimator(isRight);
			}
		}, 400);
	}
	
	private void addItem(final boolean isRight){
		GameTopicInfo data=null;
		if(isRight){
			mRecyclerView.setItemAnimator(new DefaultItemAnimator());
			data = (GameTopicInfo) gameTopics.get(endPos);
			myGameTopics.add(data);
			notifyItemInserted(myGameTopics.size());
		}
		else{
			mRecyclerView.setItemAnimator(new DefaultItemAnimator());
			data = (GameTopicInfo) gameTopics.get(startPos);
			myGameTopics.add(0,data);
			notifyItemInserted(0);
		}
	}
	
	private void removeItem(boolean isRight){
		/*mAnimator = new PackageAnimator(null, new ScaleOut());
		mAnimator.setAddDuration(400);*/
		if(isRight){
			mRecyclerView.setItemAnimator(new DefaultItemAnimator());
			if(myGameTopics.size()>0){
				myGameTopics.remove(0);
				notifyItemRemoved(0);
			}
			/*notifyItemMoved(1, 0);
			notifyItemMoved(2, 1);*/
		}
		else{
			mRecyclerView.setItemAnimator(new DefaultItemAnimator());
			if(myGameTopics.size()>0){
				myGameTopics.remove(myGameTopics.size()-1);
				notifyItemRemoved(myGameTopics.size());
			}
		}
		//notifyDataSetChanged();
	}
	
	public int getStartPos() {
		return startPos;
	}

	public void rightMoveDatas(){
		if(gameTopics!=null && gameTopics.size()>0){
			updatePos(true);
			execAnimator(true);
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					//mRecyclerView.setItemAnimator(new RemoveItemTransitionRightOutAnimator());
					removeItem(true);
					addItem(true);
					//notifyItemRangeChanged(0, 4);
				}
			},400);
			
		}
	}
	
	public void leftMoveDatas(){
		if(gameTopics!=null && gameTopics.size()>0){
			updatePos(false);
			execAnimator(false);
			new Handler().post(new Runnable() {
				
				@Override
				public void run() {
					//mRecyclerView.setItemAnimator(new AddItemTransitionLeftInAnimator());
					removeItem(false);
					addItem(false);
					//notifyItemRangeChanged(0, 4);
				}
			});
		}
	}
}
