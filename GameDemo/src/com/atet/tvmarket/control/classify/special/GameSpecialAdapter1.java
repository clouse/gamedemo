package com.atet.tvmarket.control.classify.special;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
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

public class GameSpecialAdapter1 extends BaseAdapter {

	private RecyclerView mRecyclerView;
	private GameSpecialActivity context;
	private ImageFetcher mImageFetcher;
	List<GameTopicInfo> gameTopics = new ArrayList<GameTopicInfo>();
	public int startPos = 0;
	public long currtime = 0;
	
	public GameSpecialAdapter1(RecyclerView recyclerView,GameSpecialActivity context,ImageFetcher mImageFetcher){
		this.mRecyclerView = recyclerView;
		this.context = context;
		this.mImageFetcher = mImageFetcher;
	}
	
	public void setData(List<GameTopicInfo> gameTopics,int flag) {
		this.gameTopics.clear();
		this.gameTopics.addAll(gameTopics);
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		if(gameTopics.size()==0){
			return 0;
		}
		else{
			return Integer.MAX_VALUE;
		}
	}

	@Override
	public void onBindViewHolder(BaseViewHolder holder, int position) {
		GameTopicInfo topicInfo = gameTopics.get(position%gameTopics.size());
		ItemViewHolder viewHolder = (ItemViewHolder) holder;
		viewHolder.itemView.setTag(position);
		viewHolder.setGameName(topicInfo.getName());
		viewHolder.setGameIcon(topicInfo.getPhoto());
		if(position==0){
			viewHolder.itemView.requestFocus();
		}
	}

	@Override
	public BaseViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
		View root = LayoutInflater.from(container.getContext()).inflate(R.layout.gameclassify_speical_item, container,false);
		return new ItemViewHolder(root);
	}

	class ItemViewHolder extends BaseViewHolder implements OnFocusChangeListener,OnKeyListener{
		private RelativeLayout layout;
		private ImageView gameIcon;
		private TextView gameName;
		
		public ItemViewHolder(View itemView) {
			super(itemView);
			ScaleViewUtils.scaleView(itemView);
			
			layout = (RelativeLayout)itemView.findViewById(R.id.rl_layout);
			gameIcon = (ImageView)itemView.findViewById(R.id.iv_cover);
			gameName = (TextView)itemView.findViewById(R.id.tv_name);
			itemView.setOnFocusChangeListener(this);
			itemView.setOnKeyListener(this);
		}
		public void setGameIcon(String url){
			mImageFetcher.loadImage(url, gameIcon, R.drawable.default_special);
		}
		
		public void setGameName(CharSequence name){
			gameName.setText(name);
		}
		
		public void setGameNameVisible(int visible){
			gameName.setVisibility(visible);
		}
		
		public RelativeLayout getLayout() {
			return layout;
		}
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus){
				context.listFocusView = v;
				int pos = (Integer) v.getTag();
				startPos = pos;
				context.setCurrentGameTopicInfo();
				if(pos==0){
					mRecyclerView.smoothScrollToPosition(0);
				}
				else{
					int []location = new int[2];
					itemView.getLocationOnScreen(location);
					if(location[0]>(int) ScaleViewUtils.resetTextSize(351f)){
						mRecyclerView.smoothScrollBy((int) ScaleViewUtils.resetTextSize(351f), 0);
					}
					if(location[0]<0){
						mRecyclerView.smoothScrollBy((int) ScaleViewUtils.resetTextSize(-351f), 0);
					}
				}
				if(context.isOpenDetail){
					context.isOpenDetail = false;
					gameName.setVisibility(View.INVISIBLE);
					itemView.setScaleX(1.1f);
					itemView.setScaleY(1.1f);
				}
				else{
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							gameName.setVisibility(View.INVISIBLE);
							itemView.setScaleX(1.1f);
							itemView.setScaleY(1.1f);
						}
					}, 300);
				}
			}
			else{
				gameName.setVisibility(View.VISIBLE);
				itemView.setScaleX(1.0f);
				itemView.setScaleY(1.0f);
			}
		}
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			int action = event.getAction();
			if(action == KeyEvent.ACTION_DOWN){
				if(keyCode==KeyEvent.KEYCODE_DPAD_LEFT){
					if(System.currentTimeMillis()-currtime>500){
						currtime = System.currentTimeMillis();
						return false;
					}
					else{
						return true;
					}
					
				}
				else if(keyCode==KeyEvent.KEYCODE_DPAD_RIGHT){
					if(System.currentTimeMillis()-currtime>500){
						currtime = System.currentTimeMillis();
						return false;
					}
					else{
						return true;
					}
				}
			}
			return false;
		}
	}
}
