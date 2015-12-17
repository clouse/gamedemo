package com.atet.tvmarket.control.search;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atet.tvmarket.R;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.entity.dao.GameSearchPinyinInfo;
import com.atet.tvmarket.utils.ScaleViewUtils;

public class SearchResultAdapter extends
		RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

	private List<GameSearchPinyinInfo> gameSearchPinyinInfoList;
	private int gameCount = 0;
	private IGameiconClick gameiconClickInterface = null;
	private RecyclerView mRecyclerView;
	private ImageFetcher imageFetcher;

	public SearchResultAdapter(
			List<GameSearchPinyinInfo> gameSearchPinyinInfoList,
			IGameiconClick gameiconClickInterface, RecyclerView recyclerView) {
		super();
		this.gameSearchPinyinInfoList = gameSearchPinyinInfoList;
		this.gameiconClickInterface = gameiconClickInterface;
		mRecyclerView = recyclerView;
		gameCount = gameSearchPinyinInfoList.size();
		imageFetcher = new ImageFetcher();
	}

	/**
	 * 
	 * @Title: dataChange
	 * @Description: TODO(监听数据变化)
	 * @param @param gameSearchPinyinInfoList 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void dataChange(List<GameSearchPinyinInfo> gameSearchPinyinInfoList) {
		
		if (gameSearchPinyinInfoList == null) {
			this.gameSearchPinyinInfoList = new ArrayList<GameSearchPinyinInfo>();
		} else {
			this.gameSearchPinyinInfoList = gameSearchPinyinInfoList;
			gameCount = gameSearchPinyinInfoList.size();
		}
		notifyDataSetChanged();
	}

	public class ImageClickLisener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Integer position = (Integer) v.getTag();
			gameiconClickInterface.gameIconClick(gameSearchPinyinInfoList.get(position));
		}
	}

	public class RecycleOnTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				RecycleOnFocusChangeListener listener = (RecycleOnFocusChangeListener) v
						.getOnFocusChangeListener();
				if (listener != null
						&& listener instanceof RecycleOnFocusChangeListener) {
					((RecycleOnFocusChangeListener) listener).setView(v);
				}
			}

			return false;
		}
	}

	public class RecycleOnFocusChangeListener implements OnFocusChangeListener {

		private View view;

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub

			if (hasFocus) {
				BaseApplication.position = (Integer) v.getTag();
				int[] location = new int[2];
				v.getLocationOnScreen(location);
				// 处理recycleview的滑动问题
				if (location[1] - v.getHeight() * 1.5 < mRecyclerView.getTop()) {
					mRecyclerView.smoothScrollBy(0, (int) (-v.getHeight() * 3));
				}
				if ((location[1] /* + v.getHeight() */> mRecyclerView
						.getBottom())
				/* && ((Integer) v.getTag()) == (gameCount - 3) */) {
					mRecyclerView
							.smoothScrollBy(0, (int) (v.getHeight() * 1.5));
				}
				v.setScaleX(1.1f);
				v.setScaleY(1.1f);
				if (v.isInTouchMode() && v == view) {
					v.performClick();
				} else {
					view = null;
				}
			} else {
				v.setScaleX(1.0f);
				v.setScaleY(1.0f);
			}
		}

		public View getView() {
			return view;
		}

		public void setView(View view) {
			this.view = view;
		}
	}

	public interface IGameiconClick {
		public void gameIconClick(GameSearchPinyinInfo gameSearchPinyinInfo);
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public ImageView mIVIcon;
		public TextView mTVgamename;
		public RelativeLayout mLayout;

		public ViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
			mIVIcon = (ImageView) itemView
					.findViewById(R.id.item_search_result_icon_iv);
			mTVgamename = (TextView) itemView
					.findViewById(R.id.item_search_result_gamename_tv);
			mLayout = (RelativeLayout) itemView
					.findViewById(R.id.item_search_result_layout);
		}
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return gameSearchPinyinInfoList.size();
	}

	@SuppressLint("NewApi")
	@Override
	public void onBindViewHolder(ViewHolder viewHodler, int position) {
		// TODO Auto-generated method stub
		viewHodler.mTVgamename.setText(gameSearchPinyinInfoList.get(position)
				.getGameName());
		imageFetcher.loadImage(
				gameSearchPinyinInfoList.get(position).getIcon(),
				viewHodler.mIVIcon, R.drawable.gameranking_item_icon);
		viewHodler.mLayout.setOnClickListener(new ImageClickLisener());
		viewHodler.mLayout.setOnTouchListener(new RecycleOnTouchListener());
		viewHodler.mLayout
				.setOnFocusChangeListener(new RecycleOnFocusChangeListener());
		viewHodler.mLayout.setTag(position);
		if (position == 0 || position == 1) {
			viewHodler.mLayout.setNextFocusUpId(viewHodler.mLayout.getId());
		} else {
			viewHodler.mLayout.setNextFocusUpId(0);
		}
		// 右边的item
		if (position % 2 != 0) {
			/*
			 * viewHodler.mLayout .setNextFocusRightId(R.id.co);
			 */
		}
		// 如果游戏个数为偶数
		if (gameCount % 2 == 0) {
			if (position == gameCount - 1 || position == gameCount - 2) {
				viewHodler.mLayout.setNextFocusDownId(viewHodler.mLayout
						.getId());
			} else {
				viewHodler.mLayout.setNextFocusDownId(0);
			}
		} else {
			if (position == gameCount - 1) {
				viewHodler.mLayout.setNextFocusDownId(viewHodler.mLayout
						.getId());
			} else {
				viewHodler.mLayout.setNextFocusDownId(0);
			}
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int typeStyle) {
		// TODO Auto-generated method stub
		View view = View.inflate(parent.getContext(),
				R.layout.item_search_result, null);
		ScaleViewUtils.scaleView(view);
		ViewHolder holder = new ViewHolder(view);
		return holder;
	}
}
