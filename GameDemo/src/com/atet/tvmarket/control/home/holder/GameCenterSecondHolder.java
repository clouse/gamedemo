package com.atet.tvmarket.control.home.holder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.home.inf.OnRecyItemClickListener;
import com.atet.tvmarket.control.home.view.GameCenterSecondPanel;
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
public class GameCenterSecondHolder extends ViewHolder implements
		OnFocusChangeListener, OnClickListener, OnKeyListener {
	ALog alog = ALog.getLogger(GameCenterSecondHolder.class);
	public GameCenterSecondPanel panel;
	private RecyclerView mRecyclerView;
	private OnRecyItemClickListener mlistener;
	private ImageFetcher imageFetcher;
	private Context context;
	private AdInfo adInfo;

	/*
	 * private Handler mHandler = new Handler() { public void
	 * handleMessage(Message msg) {
	 * panel.getMain_push_reflect().setImageBitmap(reflectBitmap); }; };
	 */

	public GameCenterSecondHolder(View itemView, RecyclerView recyclerView,
			OnRecyItemClickListener listener, Context context) {
		super(itemView);
		panel = (GameCenterSecondPanel) itemView;
		this.mlistener = listener;
		this.mRecyclerView = recyclerView;
		this.context = context;
		initView();

	}

	private void initView() {

		imageFetcher = new ImageFetcher();
		
		panel.getMain_push().setOnFocusChangeListener(this);
		panel.getMain_push().setOnClickListener(this);
		panel.getMain_push().setOnKeyListener(this);
		imageFetcher.loadLocalImage(R.drawable.default_oblique,panel.getMain_push_iv(), R.drawable.default_oblique);
	}

	public void setData(AdInfo adInfo) {
		this.adInfo = adInfo;
		UIUtils.setOnline(panel.getGame_online(), adInfo.getOnline());
		imageFetcher.loadImage(adInfo.getCornerMark(), panel.getMain_push_released(), R.drawable.translation);
		GameAdapterTypeUtil.decideAdapter(adInfo.getHandleType(),
				panel.getHandle_icon(), panel.getControl_icon());
		imageFetcher.loadImage(adInfo.getUrl(), panel.getMain_push_iv(),
				R.drawable.default_oblique);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {

			/*
			 * int[] location = new int[2]; v.getLocationOnScreen(location); if
			 * (location[0] - v.getWidth() < mRecyclerView.getLeft()) {
			 * mRecyclerView.smoothScrollBy(-v.getWidth(), 0); }
			 */

			if (v.getId() == R.id.main_push) {

				panel.getMain_push_shadow().setImageResource(
						R.drawable.white_focus);
				panel.getMain_push_content().setScaleX(1.1f);
				panel.getMain_push_content().setScaleY(1.1f);
				// panel.getMain_push_content().setNextFocusUpId(R.id.tab_gamecenter);
				panel.getMain_push().setNextFocusLeftId(
						R.id.game_center_quick_start);
				panel.getMain_push().setNextFocusUpId(R.id.tab_gamecenter);
				panel.getMain_push().setNextFocusDownId(
						panel.getMain_push().getId());
				panel.getMain_push().setNextFocusRightId(
						R.id.game_center_main_game);
				if (adInfo != null) {
					if (adInfo.getVideoUrl() != null
							&& !adInfo.getVideoUrl().isEmpty()) {
						panel.getMain_push_vedio().setVisibility(View.VISIBLE);
					}
				}
			}
			if (v.isInTouchMode()) {
				v.performClick();
			}
		} else {
			if (v.getId() == R.id.main_push) {
				panel.getMain_push_shadow().setImageResource(
						android.R.color.transparent);
				panel.getMain_push_content().setScaleX(1.0f);
				panel.getMain_push_content().setScaleY(1.0f);
				panel.getMain_push_vedio().setVisibility(View.INVISIBLE);
			}
		}
	}

	@Override
	public void onClick(View v) {
		// Umeng统计所有交互
		UmengUtils.setOnEvent(context, UmengUtils.GAMECENTER_INTERACTION);
		// Umeng统计"首页视频"总点击次数
		UmengUtils.setOnEvent(context,
				UmengUtils.GAMECENTER_MAINPAGEVIDEO_CLICK);
		v.requestFocus();
		Intent intent;
		if (adInfo != null) {
			String videoUrl = adInfo.getVideoUrl();
			if (videoUrl == null || videoUrl.isEmpty()) {
				// Umeng统计"首页视频"展位中，非视频的情况
				UmengUtils.setOnEvent(context,
						UmengUtils.GAMECENTER_MAINPAGEVIDEO_UNVIDEO_CLICK);
				intent = new Intent(context, GameDetailActivity.class);
				intent.putExtra(Constant.GAMECENTER, 1);
				intent.putExtra("gameId", adInfo.getGameId());
				context.startActivity(intent);
			} else {
				alog.info("videoUrl = " + videoUrl);
				intent = new Intent(context, PlayVideoActivity.class);
				intent.putExtra(Constant.GAMECENTER, 1);
				intent.putExtra("videoUrl", videoUrl);
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
			}
		}
		return false;
	}
}
