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
import com.atet.tvmarket.control.classify.special.GameSpecialActivity;
import com.atet.tvmarket.control.home.MainActivity;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.DataConfig.UpdateInterface;
import com.atet.tvmarket.utils.UmengUtils;

public class GameClassifySinglePanel extends RelativeLayout {

	private RelativeLayout layout;
	private ImageView cover, icon, border;
	private ImageView new_content;
	private TextView name;
	private MainActivity context;
	private int flag = 0;
	private String typeId;
	public GameClassifySinglePanel(Context context) {
		this(context, null);
	}

	public GameClassifySinglePanel(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GameClassifySinglePanel(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = (MainActivity) context;
		LayoutInflater.from(context).inflate(R.layout.gameclassify_singlepanel,
				this, true);
		layout = (RelativeLayout) findViewById(R.id.rl_layout);
		border = (ImageView) findViewById(R.id.iv_border);
		icon = (ImageView) findViewById(R.id.iv_icon);
		new_content = (ImageView) findViewById(R.id.iv_new);
		cover = (ImageView) findViewById(R.id.iv_cover);
		name = (TextView) findViewById(R.id.tv_name);
		layout.setOnFocusChangeListener(onFocusChangeListener);
		layout.setOnKeyListener(onKeyListener);
		layout.setOnClickListener(onClickListener);
	}

	OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				GameClassifySinglePanel singlePanel = (GameClassifySinglePanel) v
						.getParent().getParent();
				RecyclerView mRecyclerView = (RecyclerView) singlePanel
						.getParent();
				int panelPos = mRecyclerView
						.getChildAdapterPosition(singlePanel);
				if (panelPos == 1) {
					mRecyclerView.smoothScrollToPosition(0);
				} else {
					int[] location = new int[2];
					v.getLocationOnScreen(location);
					if (location[0] + v.getWidth() > mRecyclerView.getRight()) {
						mRecyclerView.smoothScrollBy(v.getWidth(), 0);
					}
					if (location[0] - v.getWidth() < mRecyclerView.getLeft()) {
						mRecyclerView.smoothScrollBy(-v.getWidth(), 0);
					}
				}
				border.setVisibility(View.VISIBLE);
				v.setScaleX(1.1f);
				v.setScaleY(1.1f);
				if (v.isInTouchMode()) {
					v.performClick();
				}
			} else {
				border.setVisibility(View.INVISIBLE);
				v.setScaleX(1.0f);
				v.setScaleY(1.0f);
			}
		}
	};

	OnKeyListener onKeyListener = new OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				GameClassifySinglePanel singlePanel = (GameClassifySinglePanel) v
						.getParent().getParent();
				RecyclerView mRecyclerView = (RecyclerView) singlePanel
						.getParent();
				int panelPos = mRecyclerView
						.getChildAdapterPosition(singlePanel);
				if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
					if (panelPos == 1) {
						GameClassifyRankingPanel rankingPanel = (GameClassifyRankingPanel) mRecyclerView
								.getLayoutManager().findViewByPosition(0);
						if (rankingPanel != null) {
							rankingPanel.getGameRankingList()
									.getLayoutManager().findViewByPosition(1)
									.requestFocus();
						}
					} else {
						GameClassifyDoublePanel doublePanel = (GameClassifyDoublePanel) mRecyclerView
								.getLayoutManager().findViewByPosition(
										panelPos - 1);
						if (doublePanel != null) {
							doublePanel.getTopPanel().requestFocus();
						}
					}

					return true;
				} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
					if (panelPos < mRecyclerView.getLayoutManager()
							.getItemCount() - 1) {
						GameClassifyDoublePanel doublePanel = (GameClassifyDoublePanel) mRecyclerView
								.getLayoutManager().findViewByPosition(
										panelPos + 1);
						if (doublePanel != null) {
							doublePanel.getTopPanel().requestFocus();
						}
						return true;
					} else {
						return true;
					}
				} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
					mRecyclerView.smoothScrollToPosition(0);
				} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
					return true;
				}
			}
			return false;
		}
	};

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// Umeng统计"交互"统计
			UmengUtils.setOnEvent(getContext(),
					UmengUtils.GAMECLASSIFY_INTERACTION);
			GameClassifySinglePanel singlePanel = (GameClassifySinglePanel) v
					.getParent().getParent();
			RecyclerView mRecyclerView = (RecyclerView) singlePanel.getParent();
			int panelPos = mRecyclerView.getChildAdapterPosition(singlePanel);
			if (panelPos == 1) {
				// Umeng统计"专题游戏"统计
				UmengUtils.setOnEvent(getContext(),
						UmengUtils.GAMECLASSIFY_SPECIAL_CLICK);
			} else if (panelPos == 3) {
				// Umeng统计"单项模块3"统计
				UmengUtils.setOnEvent(getContext(),
						UmengUtils.GAMECLASSIFY_SINGLE3);
			} else if (panelPos == 5) {
				// Umeng统计"单项模块5"统计
				UmengUtils.setOnEvent(getContext(),
						UmengUtils.GAMECLASSIFY_SINGLE5);
			} 
			
			if (panelPos == 1) {
				if(new_content.getVisibility() == View.VISIBLE){
					DataFetcher.removeNewContentInterface(getContext(), UpdateInterface.GAME_TOPIC);
					new_content.setVisibility(View.INVISIBLE);
				}
				Intent intent = new Intent(getContext(),
						GameSpecialActivity.class);
				getContext().startActivity(intent);
			}
			else {
				int pos = 0;
				for (int i = 1; i <= panelPos - 2; i++) {
					if (i % 2 == 1) {
						pos += 2;
					} else {
						pos += 1;
					}
				}

				Log.i("life", "pos:" + pos);
				if(new_content.getVisibility() == View.VISIBLE){
					DataFetcher.removeNewContentInterface(getContext(), UpdateInterface.GAME_TYPE_DETAIL + typeId);
					new_content.setVisibility(View.INVISIBLE);
				}
				Intent intent = new Intent(getContext(), GameAreaActivity.class);
				intent.putExtra("position", pos);
				getContext().startActivity(intent);
			}
		}
	};

	public ImageView getCover() {
		return cover;
	}

	public void setCover(ImageView cover) {
		this.cover = cover;
	}

	public ImageView getIcon() {
		return icon;
	}

	public void setIcon(ImageView icon) {
		this.icon = icon;
	}

	public TextView getName() {
		return name;
	}

	public void setName(TextView name) {
		this.name = name;
	}

	public RelativeLayout getLayout() {
		return layout;
	}

	public ImageView getNew_content() {
		return new_content;
	}
	
	public void setTypeId(String typeId){
		this.typeId = typeId;
	}
	
}
