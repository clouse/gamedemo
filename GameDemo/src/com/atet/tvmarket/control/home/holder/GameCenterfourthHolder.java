package com.atet.tvmarket.control.home.holder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;

import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.home.inf.OnRecyItemClickListener;
import com.atet.tvmarket.control.home.inf.OnkeyChangeListener;
import com.atet.tvmarket.control.home.view.GameCenterfifthPanel;
import com.atet.tvmarket.control.home.view.GameCenterfourthPanel;
import com.atet.tvmarket.control.mygame.activity.GameDetailActivity;
import com.atet.tvmarket.control.video.PlayVideoActivity;
import com.atet.tvmarket.entity.dao.AdInfo;
import com.atet.tvmarket.utils.GameAdapterTypeUtil;
import com.atet.tvmarket.utils.UIUtils;
import com.atet.tvmarket.utils.UmengUtils;

/**
 * @description: 游戏中心第二部分布局的holder
 * 
 * @author: LiJie
 * @date: 2015年6月12日 上午9:55:15
 */
public class GameCenterfourthHolder extends ViewHolder implements
		OnFocusChangeListener, OnClickListener, OnKeyListener {
	public GameCenterfourthPanel panel;
	private RecyclerView mRecyclerView;
	private OnRecyItemClickListener mlistener;
	private ImageFetcher imageFetcher;
	private Bitmap reflectBitmap;
	private OnkeyChangeListener keyListener;
	private AdInfo adInfo;
	private Context context;

	/*
	 * private Handler mHandler = new Handler() { public void
	 * handleMessage(Message msg) {
	 * panel.getMain_push_reflect().setImageBitmap(reflectBitmap); }; };
	 */

	public GameCenterfourthHolder(View itemView, RecyclerView recyclerView,
			OnRecyItemClickListener listener, Context context) {
		super(itemView);
		this.context = context;
		this.mlistener = listener;
		this.mRecyclerView = recyclerView;
		initView();

	}

	private void initView() {
		panel = (GameCenterfourthPanel) itemView;
		imageFetcher = new ImageFetcher();
		panel.getMore_game().setOnFocusChangeListener(this);
		panel.getMore_game().setOnClickListener(this);
		panel.getMore_game().setOnKeyListener(this);
	}

	public void setData(AdInfo info) {
		this.adInfo = info;
		imageFetcher.loadImage(info.getCornerMark(), panel.getMore_game_released(),R.drawable.translation);
		UIUtils.setOnline(panel.getGame_online(), info.getOnline());
		GameAdapterTypeUtil.decideAdapter(info.getHandleType(),
				panel.getHandle_icon(), panel.getControl_icon());
		imageFetcher.loadImage(info.getUrl(), panel.getMore_game_iv(),
				R.drawable.default_vertical);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {

			if (v.isInTouchMode()) {
				v.performClick();
			}

			int[] location = new int[2];
			v.getLocationOnScreen(location);

			/*
			 * if (location[0] - v.getWidth() < mRecyclerView.getLeft() ) {
			 * mRecyclerView.smoothScrollBy(- 400, 0); }
			 */

			if (location[0] + (v.getWidth() * 1.3) > mRecyclerView.getRight()) {
				mRecyclerView.smoothScrollBy(300, 0);
			}

			if (v.getId() == R.id.more_game) {
				panel.getMore_game_shadow().setImageResource(
						R.drawable.white_focus);
				panel.getMore_game_content().setScaleX(1.1f);
				panel.getMore_game_content().setScaleY(1.1f);
				panel.getMore_game().setNextFocusUpId(R.id.tab_gamecenter);
				panel.getMore_game().setNextFocusDownId(
						panel.getMore_game().getId());
				if (adInfo != null) {
					if (adInfo.getVideoUrl() != null
							&& !adInfo.getVideoUrl().isEmpty()) {
						panel.getMore_game_video().setVisibility(View.VISIBLE);
					}
				}
			}
		} else {
			if (v.getId() == R.id.more_game) {
				panel.getMore_game_shadow().setImageResource(
						android.R.color.transparent);
				panel.getMore_game_content().setScaleX(1.0f);
				panel.getMore_game_content().setScaleY(1.0f);
				panel.getMore_game_video().setVisibility(View.INVISIBLE);
			}
		}
	}

	@Override
	public void onClick(View v) {
		// Umeng统计所有交互
		UmengUtils.setOnEvent(context, UmengUtils.GAMECENTER_INTERACTION);
		v.requestFocus();
		Intent intent;
		if (getPosition() == 3) {
			// Umeng统计"单项3"
			UmengUtils.setOnEvent(context, UmengUtils.GAMECENTER_SINGLE3);
		} else if (getPosition() == 5) {
			// Umeng统计"单项6"
			UmengUtils.setOnEvent(context, UmengUtils.GAMECENTER_SINGLE6);
		}
		if (adInfo != null) {
			if (adInfo.getVideoUrl() == null || adInfo.getVideoUrl().isEmpty()) {
				intent = new Intent(context, GameDetailActivity.class);
				intent.putExtra(Constant.GAMECENTER, 1);
				intent.putExtra("gameId", adInfo.getGameId());
				context.startActivity(intent);
			} else {
				intent = new Intent(context, PlayVideoActivity.class);
				intent.putExtra(Constant.GAMECENTER, 1);
				intent.putExtra("videoUrl", adInfo.getVideoUrl());
				intent.putExtra("isGameVideo", true);
				intent.putExtra("gameId", adInfo.getGameId());
				context.startActivity(intent);
			}
		}
	}

	@Override
	public boolean onKey(View view, int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {

				return true;
			} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
				GameCenterfifthPanel panel = (GameCenterfifthPanel) mRecyclerView
						.getLayoutManager().findViewByPosition(
								getPosition() + 1);
				panel.getItem5_top().requestFocus();
				return true;
			}
		}
		return false;
	}
}
