package com.atet.tvmarket.control.classify;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atet.tvmarket.R;
import com.atet.tvmarket.control.classify.area.GameAreaActivity;
import com.atet.tvmarket.control.home.MainActivity;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.DataConfig.UpdateInterface;
import com.atet.tvmarket.utils.UmengUtils;

public class GameClassifyDoublePanel extends RelativeLayout {

	private RelativeLayout topPanel, bottomPanel;
	private ImageView topShadow, bottomShadow, topCover, bottomCover, topBg,
			bottomBg, topBorder, bottomBorder;
	private TextView topName, bottomName, topName1, bottomName1;
	private ImageView top_new;
	private ImageView bottom_new;
	private MainActivity context;
	private int flag = 0;
	private String topTypeId,bottomTypeId;
	public GameClassifyDoublePanel(Context context) {
		this(context, null);
	}

	public GameClassifyDoublePanel(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GameClassifyDoublePanel(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = (MainActivity) context;
		LayoutInflater.from(context).inflate(R.layout.gameclassify_doublepanel,
				this, true);
		topPanel = (RelativeLayout) findViewById(R.id.rl_top);
		top_new = (ImageView) findViewById(R.id.iv_top_new);
		bottom_new = (ImageView) findViewById(R.id.iv_bottom_new);
		bottomPanel = (RelativeLayout) findViewById(R.id.rl_bottom);
		topBorder = (ImageView) findViewById(R.id.iv_top_border);
		bottomBorder = (ImageView) findViewById(R.id.iv_bottom_border);
		topBg = (ImageView) findViewById(R.id.iv_top_bg);
		bottomBg = (ImageView) findViewById(R.id.iv_bottom_bg);
		/*
		 * topShadow = (ImageView)findViewById(R.id.iv_classify_shadow);
		 * bottomShadow = (ImageView)findViewById(R.id.iv_classify_shadow1);
		 */
		topCover = (ImageView) findViewById(R.id.iv_top_cover);
		bottomCover = (ImageView) findViewById(R.id.iv_bottom_cover);
		topName = (TextView) findViewById(R.id.tv_top_classify_name);
		bottomName = (TextView) findViewById(R.id.tv_bottom_classify_name);
		topName1 = (TextView) findViewById(R.id.tv_top_classify_name1);
		bottomName1 = (TextView) findViewById(R.id.tv_bottom_classify_name1);

		setChildrenDrawingOrderEnabled(true);
		topPanel.setOnFocusChangeListener(onFocusChangeListener);
		bottomPanel.setOnFocusChangeListener(onFocusChangeListener);

		topPanel.setOnKeyListener(onKeyListener);
		bottomPanel.setOnKeyListener(onKeyListener);

		topPanel.setOnClickListener(onClickListener);
		bottomPanel.setOnClickListener(onClickListener);
	}

	/**
	 * 改变聚焦的元素在父类容器中的绘制顺序，达到聚焦后的View，放大之后能够层叠在其他子View之上
	 * 
	 * */
	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		if (hasFocus()) {
			View child = getFocusedChild();
			if (child != null) {

				int index = indexOfChild(child);
				if (index != -1) {

					if (i < index) {
						return i;
					} else {
						return childCount - 1 - i + index;
					}
				}
			} else {
				return i;
			}

		}

		return super.getChildDrawingOrder(childCount, i);
	}

	OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				v.requestLayout();
				GameClassifyDoublePanel doublePanel = (GameClassifyDoublePanel) v
						.getParent().getParent();
				RecyclerView mRecyclerView = (RecyclerView) doublePanel
						.getParent();
				int panelPos = mRecyclerView
						.getChildAdapterPosition(doublePanel);
				int[] location = new int[2];
				v.getLocationOnScreen(location);
				if (location[0] + v.getWidth() * 1.5 > mRecyclerView.getRight()) {
					mRecyclerView.smoothScrollBy(v.getWidth(), 0);
				}
				if (location[0] - v.getWidth() < mRecyclerView.getLeft()) {
					mRecyclerView.smoothScrollBy(-v.getWidth(), 0);
				}
				if (v == topPanel) {
					topBorder.setVisibility(View.VISIBLE);
				} else {
					bottomBorder.setVisibility(View.VISIBLE);
				}
				v.setScaleX(1.1f);
				v.setScaleY(1.1f);
				if (v.isInTouchMode()) {
					v.performClick();
				}
			} else {
				v.requestLayout();
				if (v == topPanel) {
					topBorder.setVisibility(View.INVISIBLE);
				} else {
					bottomBorder.setVisibility(View.INVISIBLE);
				}
				v.setScaleX(1.0f);
				v.setScaleY(1.0f);
			}
		}
	};

	OnKeyListener onKeyListener = new OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				GameClassifyDoublePanel doublePanel = (GameClassifyDoublePanel) v
						.getParent().getParent();
				RecyclerView mRecyclerView = (RecyclerView) doublePanel
						.getParent();
				int panelPos = mRecyclerView
						.getChildAdapterPosition(doublePanel);
				if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
				} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
					if (panelPos == mRecyclerView.getLayoutManager()
							.getItemCount() - 1) {
						return true;
					}
				} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP && v == topPanel) {
					mRecyclerView.smoothScrollToPosition(0);
				} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
						&& v == bottomPanel) {
					return true;
				}
			}
			return false;
		}
	};

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			// ScreenShot.savePic(ScreenShot.takeScreenShot(context),
			// Constant.APPPATH+"area.png");

			GameClassifyDoublePanel doublePanel = (GameClassifyDoublePanel) v
					.getParent().getParent();
			RecyclerView mRecyclerView = (RecyclerView) doublePanel.getParent();
			int panelPos = mRecyclerView.getChildAdapterPosition(doublePanel);
			int pos = 0;
			if ((panelPos - 2) > 0) {
				for (int i = 1; i <= panelPos - 2; i++) {
					if (i % 2 == 1) {
						pos += 2;
					} else {
						pos += 1;
					}
				}
			}

			if (v.getId() == doublePanel.getBottomPanel().getId()) {
				pos += 1;
			}
			Log.i("life", "pos:" + pos);

			if (pos == 0) {
				// Umeng统计 "单项模块1"
				UmengUtils.setOnEvent(context, UmengUtils.GAMECLASSIFY_SINGLE1);
			} else if (pos == 1) {
				// Umeng统计 "单项模块2"
				UmengUtils.setOnEvent(context, UmengUtils.GAMECLASSIFY_SINGLE2);
			} else if (pos == 3) {
				// Umeng统计 "单项模块4"
				UmengUtils.setOnEvent(context, UmengUtils.GAMECLASSIFY_SINGLE4);
			} else if (pos == 4) {
				// Umeng统计 "单项模块5"
				UmengUtils.setOnEvent(context, UmengUtils.GAMECLASSIFY_SINGLE5);
			}

			// Umeng统计"交互"统计
			UmengUtils.setOnEvent(getContext(),
					UmengUtils.GAMECLASSIFY_INTERACTION);
			if(v.getId()==topPanel.getId()){
				if(top_new.getVisibility()==View.VISIBLE){
					DataFetcher.removeNewContentInterface(getContext(), UpdateInterface.GAME_TYPE_DETAIL + topTypeId);
					top_new.setVisibility(View.INVISIBLE);
				}
			}
			if(v.getId()==bottomPanel.getId()){
				if(bottom_new.getVisibility()==View.VISIBLE){
					DataFetcher.removeNewContentInterface(getContext(), UpdateInterface.GAME_TYPE_DETAIL + bottomTypeId);
					bottom_new.setVisibility(View.INVISIBLE);
				}
			}
			Intent intent = new Intent(getContext(), GameAreaActivity.class);
			intent.putExtra("position", pos);
			getContext().startActivity(intent);
		}
	};

	public RelativeLayout getTopPanel() {
		return topPanel;
	}

	public RelativeLayout getBottomPanel() {
		return bottomPanel;
	}

	public ImageView getTopBg() {
		return topBg;
	}

	public ImageView getBottomBg() {
		return bottomBg;
	}

	public ImageView getTopShadow() {
		return topShadow;
	}

	public ImageView getBottomShadow() {
		return bottomShadow;
	}

	public ImageView getTopCover() {
		return topCover;
	}

	public ImageView getBottomCover() {
		return bottomCover;
	}

	public TextView getTopName() {
		return topName;
	}

	public TextView getBottomName() {
		return bottomName;
	}

	public TextView getTopName1() {
		return topName1;
	}

	public TextView getBottomName1() {
		return bottomName1;
	}

	public ImageView getTop_new() {
		return top_new;
	}
	
	public ImageView getBottom_new() {
		return bottom_new;
	}
	
	public void setTopTypeId(String topTypeId){
		this.topTypeId = topTypeId;
	}
	
	public void setBottomTypeId(String bottomTypeId){
		this.bottomTypeId = bottomTypeId;
	}
}
