package com.atet.tvmarket.control.classify.ranking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
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
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.mygame.activity.GameDetailActivity;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UmengUtils;
import com.atet.tvmarket.view.recyclerview.BaseAdapter;
import com.atet.tvmarket.view.recyclerview.BaseViewHolder;

public class GameRankingTypeAdapter extends BaseAdapter {

	private Context context;
	private RecyclerView mRecyclerView;
	private ImageFetcher mImageFetcher;
	private List<GameInfo> gameRankings = new ArrayList<GameInfo>();
	private int type;
	private int lastFocusPos = 0;
	private View lastFocusView = null,lastFFView = null,lastTFView=null;

	public GameRankingTypeAdapter(Context context, RecyclerView recyclerView,
			ImageFetcher mImageFetcher, int type) {
		this.context = context;
		this.mRecyclerView = recyclerView;
		this.mImageFetcher = mImageFetcher;
		this.type = type;
	}

	public void setData(List<GameInfo> gameRankings) {
		Collections.sort(gameRankings, comp);
		this.gameRankings.clear();
		this.gameRankings.addAll(gameRankings);
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {

		return gameRankings.size();
	}

	@Override
	public void onBindViewHolder(BaseViewHolder itemHolder, int position) {
		ItemViewHolder holder = (ItemViewHolder) itemHolder;
		holder.itemView.setTag(position);
		GameInfo gameInfo = (GameInfo) gameRankings.get(position);
		holder.setGameName(gameInfo.getGameName());
		holder.setOrderNum(position + 1);
		holder.setGameId(gameInfo.getGameId());
		holder.setGameIcon(gameInfo.getMinPhoto());

		if (type == 0 && position == 0 && lastFocusView == null) {
			holder.itemView.requestFocus();
		}

		MarginLayoutParams layoutParams = new MarginLayoutParams(
				MarginLayoutParams.WRAP_CONTENT,
				MarginLayoutParams.WRAP_CONTENT);
		if (position == getItemCount() - 1) {
			layoutParams.rightMargin = 0;
		} else {
			layoutParams.rightMargin = (int) ScaleViewUtils.resetTextSize(-93);
		}
		holder.itemView.setLayoutParams(layoutParams);
	}

	@Override
	public BaseViewHolder onCreateViewHolder(ViewGroup container, int viewType) {

		View root = LayoutInflater.from(container.getContext()).inflate(
				R.layout.gameranking_item, container, false);

		return new ItemViewHolder(root);
	}

	class ItemViewHolder extends BaseViewHolder implements
			OnFocusChangeListener, OnClickListener, OnKeyListener,
			OnTouchListener {

		private LinearLayout content;
		private ImageView border;
		private ImageView gameIcon;
		private TextView gameName;
		private TextView gameOrderNum, gameOrderNumS;
		private String gameId;
		private View view;

		public ItemViewHolder(View itemView) {
			super(itemView);
			ScaleViewUtils.scaleView(itemView);
			content = (LinearLayout) itemView.findViewById(R.id.ll_content);
			border = (ImageView) itemView.findViewById(R.id.iv_border);
			gameIcon = (ImageView) itemView.findViewById(R.id.iv_game_icon);
			gameName = (TextView) itemView.findViewById(R.id.tv_game_name);
			gameOrderNum = (TextView) itemView.findViewById(R.id.tv_ordernum);
			gameOrderNumS = (TextView) itemView
					.findViewById(R.id.tv_ordernum_selected);
			itemView.setOnFocusChangeListener(this);
			itemView.setOnClickListener(this);
			itemView.setOnKeyListener(this);
			itemView.setOnTouchListener(this);
		}

		public void setGameIcon(String iconUrl) {
			mImageFetcher.loadImage(iconUrl, gameIcon,
					R.drawable.gameranking_item_icon);
		}

		public void setGameName(CharSequence name) {
			gameName.setText(name);
		}

		public void setOrderNum(int orderNum) {
			gameOrderNum.setText("第" + orderNum + "名");
			gameOrderNumS.setText("第" + orderNum + "名");
		}

		public void setGameId(String gameId) {
			this.gameId = gameId;
		}

		@Override
		public void onClick(View v) {
			// Umeng统计"游戏排行"进入到游戏详情的次数
			UmengUtils.setOnEvent(context,
					UmengUtils.GAMECLASSIFY_RANK_DETAIL_CLICK);
			Intent intent = new Intent(context, GameDetailActivity.class);
			intent.putExtra(Constant.GAMECENTER, 2);
			intent.putExtra("gameId", gameId);
			context.startActivity(intent);
		}

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				v.requestLayout();
				border.setVisibility(View.VISIBLE);
				content.setScaleX(1.31f);
				content.setScaleY(1.3f);
				gameOrderNum.setVisibility(View.INVISIBLE);
				gameOrderNumS.setVisibility(View.VISIBLE);
				gameOrderNumS.setScaleX(1.31f);
				gameOrderNumS.setScaleY(1.3f);
				int pos = mRecyclerView.getChildAdapterPosition(v);
				int [] location = new int[2];
				if(pos > lastFocusPos){
					v.getLocationOnScreen(location);
					if(location[0]>mRecyclerView.getWidth()/2){
						mRecyclerView.smoothScrollBy(location[0]-mRecyclerView.getWidth()/2-v.getWidth()/4, 0);
					}
				}
				if(pos<lastFocusPos){
					v.getLocationOnScreen(location);
					if(location[0]<mRecyclerView.getWidth()/2){
						mRecyclerView.smoothScrollBy(location[0]-mRecyclerView.getWidth()/2-v.getWidth()/4, 0);
					}
				}
				lastFocusView = v;
				if (v.isInTouchMode() && v == view) {
					v.performClick();
				} else {
					view = null;
				}
			} else {
				v.requestLayout();
				border.setVisibility(View.INVISIBLE);
				content.setScaleX(1.0f);
				content.setScaleY(1.0f);
				gameOrderNumS.setVisibility(View.INVISIBLE);
				gameOrderNum.setVisibility(View.VISIBLE);
				gameOrderNumS.setScaleX(1.0f);
				gameOrderNumS.setScaleY(1.0f);
			}
		}

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				int pos = (Integer) v.getTag();
				lastFocusPos = pos;
				Log.i("life", "pos:" + pos);
				if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
					if (pos == getItemCount() - 1) {
						return true;
					}

				} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
					if (pos == 0) {
						return true;
					}
				}
				else if(keyCode == KeyEvent.KEYCODE_DPAD_UP){
					int type = (Integer) mRecyclerView.getTag();
					if(type==0){
						return true;
					}
					else{
						RecyclerView superRecyclerView = (RecyclerView) mRecyclerView.getParent().getParent();
						View view = superRecyclerView.getLayoutManager().findViewByPosition(0);
						if(view!=null){
							RecyclerView tRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
							if(pos<=2){
								tRecyclerView.getChildAt(pos).requestFocus();
							}
							else if(pos==getItemCount()-1){
								if(tRecyclerView.getChildCount()>=6){
									View myv = tRecyclerView.getChildAt(5);
									if(myv!=null){
										myv.requestFocus();
									}
								}
								else if(tRecyclerView.getChildCount()>=5){
									View myv = tRecyclerView.getChildAt(4);
									if(myv!=null){
										myv.requestFocus();
									}
								}
								else{
									tRecyclerView.getChildAt(3).requestFocus();
								}
							}
							else if(pos==getItemCount()-2){
								if(tRecyclerView.getChildCount()>=5){
									View myv = tRecyclerView.getChildAt(4);
									if(myv!=null){
										myv.requestFocus();
									}
								}
								else{
									tRecyclerView.getChildAt(3).requestFocus();
								}
							}
							else{
								tRecyclerView.getChildAt(3).requestFocus();
							}
						}
						
						return true;
					}
				}
				else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
					int type = (Integer) mRecyclerView.getTag();
					if(type==0){
						RecyclerView superRecyclerView = (RecyclerView) mRecyclerView.getParent().getParent();
						View view = superRecyclerView.getLayoutManager().findViewByPosition(1);
						if(view!=null){
							RecyclerView tRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
							if(pos<=2){
								tRecyclerView.getChildAt(pos).requestFocus();
							}
							else if(pos==getItemCount()-1){
								if(tRecyclerView.getChildCount()>=6){
									View myv = tRecyclerView.getChildAt(5);
									if(myv!=null){
										myv.requestFocus();
									}
								}
								else if(tRecyclerView.getChildCount()>=5){
									View myv = tRecyclerView.getChildAt(4);
									if(myv!=null){
										myv.requestFocus();
									}
								}
								else{
									tRecyclerView.getChildAt(3).requestFocus();
								}
							}
							else if(pos==getItemCount()-2){
								if(tRecyclerView.getChildCount()>=5){
									View myv = tRecyclerView.getChildAt(4);
									if(myv!=null){
										myv.requestFocus();
									}
								}
								else{
									tRecyclerView.getChildAt(3).requestFocus();
								}
							}
							else{
								tRecyclerView.getChildAt(3).requestFocus();
							}
						}
						return true;
						
					}else if(type==1){
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
				if (listener != null) {
					view = v;
				}
			}
			return false;
		}
	}

	Comparator<GameInfo> comp = new Comparator<GameInfo>() {

		@Override
		public int compare(GameInfo lhs, GameInfo rhs) {
			return rhs.getGameDownCount().compareTo(lhs.getGameDownCount());
		}
	};
}
