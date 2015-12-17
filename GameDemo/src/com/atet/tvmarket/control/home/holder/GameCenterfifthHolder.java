package com.atet.tvmarket.control.home.holder;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;

import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.home.MainActivity;
import com.atet.tvmarket.control.home.view.GameCenterfifthPanel;
import com.atet.tvmarket.control.mygame.activity.GameDetailActivity;
import com.atet.tvmarket.control.video.PlayVideoActivity;
import com.atet.tvmarket.entity.dao.AdInfo;
import com.atet.tvmarket.utils.GameAdapterTypeUtil;
import com.atet.tvmarket.utils.UIUtils;
import com.atet.tvmarket.utils.UmengUtils;

/**
 * @description: 游戏中心的第5部分的holder
 * 
 * @author: Lijie
 * @date: 2015年6月12日 上午9:43:51
 */
public class GameCenterfifthHolder extends ViewHolder implements
		OnFocusChangeListener, OnClickListener, OnKeyListener {
	public GameCenterfifthPanel panel;
	private RecyclerView recyclerView;
	private ImageFetcher imageFetcher;
	private Context context;
	private List<AdInfo> infos = new ArrayList<AdInfo>();
	private int position;
	private AdInfo topInfo;
	private AdInfo bellowInfo;
	private int tag = 1; // 进入游戏详情

	public GameCenterfifthHolder(View itemView, RecyclerView recyclerView,
			Context context) {
		super(itemView);
		this.context = context;
		this.recyclerView = recyclerView;
		initView();
		initEvent();
	}

	private void initView() {
		panel = (GameCenterfifthPanel) itemView;
		imageFetcher = new ImageFetcher();
	}

	private void initEvent() {

		panel.getItem5_top().setOnFocusChangeListener(this);
		panel.getItem5_top().setOnClickListener(this);
		panel.getItem5_top().setOnKeyListener(this);

		panel.getItem5_bellow().setOnFocusChangeListener(this);
		panel.getItem5_bellow().setOnClickListener(this);

	}

	public void setData(List<AdInfo> adInfos, int position) {
		this.position = position;

		if (adInfos != null && adInfos.size() != 0) {
			infos.clear();
			infos.addAll(adInfos);
		}

		if (infos.size() != 0) {
			topInfo = infos.get(position - 2);
			imageFetcher.loadImage(topInfo.getCornerMark(),panel.getItem5_top_released(),R.drawable.translation);
			UIUtils.setOnline(panel.getItem5_top_game_online(),
					topInfo.getOnline());
			GameAdapterTypeUtil.decideAdapter(topInfo.getHandleType(),
					panel.getItem5_top_handle_icon(),
					panel.getItem5_top_control_icon());
			imageFetcher.loadImage(topInfo.getUrl(), panel.getItem5_top_iv(),
					R.drawable.default_square);
			if (infos.size() > position - 1) {
				bellowInfo = infos.get(position - 1);
				imageFetcher.loadImage(bellowInfo.getCornerMark(),panel.getItem5_bellow_released(),R.drawable.translation);
				
				UIUtils.setOnline(panel.getItem5_bellow_game_online(),
						bellowInfo.getOnline());
				GameAdapterTypeUtil.decideAdapter(bellowInfo.getHandleType(),
						panel.getItem5_bellow_handle_icon(),
						panel.getItem5_bellow_control_icon());
				imageFetcher.loadImage(bellowInfo.getUrl(),
						panel.getItem5_bellow_iv(), R.drawable.default_square);
			} else {
				panel.getItem5_bellow().setClickable(false);
				panel.getItem5_bellow().setFocusable(false);
				panel.getItem5_bellow().setFocusableInTouchMode(false);
			}
		}

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			int[] location = new int[2];
			v.getLocationOnScreen(location);

			/*
			 * if(location[0] - v.getWidth() * 2 > recyclerView.getLeft()){
			 * recyclerView.smoothScrollBy(-v.getWidth(), 0); }
			 */
			if (location[0] + v.getWidth() * 2 > recyclerView.getRight()) {
				recyclerView.smoothScrollBy(300, 0);
			}

			if (v.getId() == panel.getItem5_top().getId()) {
				panel.getItem5_top_shadow().setBackgroundResource(
						R.drawable.white_focus);
				panel.getItem5_top_content().setScaleX(1.11f);
				panel.getItem5_top_content().setScaleY(1.11f);
				if (infos.size() != 0 && topInfo != null) {
					if (topInfo.getVideoUrl() != null
							&& !topInfo.getVideoUrl().isEmpty()) {

						panel.getItem5_top_video().setVisibility(View.VISIBLE);
					}
				}
			}
			if (v.getId() == panel.getItem5_bellow().getId()) {
				panel.getItem5_bellow_shadow().setBackgroundResource(
						R.drawable.white_focus);
				panel.getItem5_bellow_content().setScaleX(1.11f);
				panel.getItem5_bellow_content().setScaleY(1.11f);
				panel.getItem5_bellow().setNextFocusDownId(
						panel.getItem5_bellow().getId());

				if (infos.size() != 0 && bellowInfo != null) {
					if (bellowInfo.getVideoUrl() != null
							&& !bellowInfo.getVideoUrl().isEmpty()) {
						panel.getItem5_bellow_video().setVisibility(
								View.VISIBLE);
					}
				}
			}

			if (v.isInTouchMode()) {
				v.performClick();
			}
		} else {
			if (v.getId() == panel.getItem5_top().getId()) {
				panel.getItem5_top_shadow().setBackgroundResource(
						android.R.color.transparent);
				panel.getItem5_top_content().setScaleX(1.0f);
				panel.getItem5_top_content().setScaleY(1.0f);
				panel.getItem5_top_video().setVisibility(View.INVISIBLE);
			}
			if (v.getId() == panel.getItem5_bellow().getId()) {
				if (infos.size() > position - 1) {
					panel.getItem5_bellow_shadow().setBackgroundResource(
							android.R.color.transparent);
					panel.getItem5_bellow_content().setScaleX(1.0f);
					panel.getItem5_bellow_content().setScaleY(1.0f);
					panel.getItem5_bellow_video().setVisibility(View.INVISIBLE);
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		// Umeng统计所有交互
		UmengUtils.setOnEvent(context, UmengUtils.GAMECENTER_INTERACTION);
		v.requestFocus();
		AdInfo adInfo = null;
		Intent intent;
		View view = null;
		if (infos != null && infos.size() != 0) {
			if (v.getId() == panel.getItem5_top().getId()) {
				adInfo = infos.get(getPosition() - 2);
				view = panel.getItem5_top_iv();
				if (getPosition() == 4) {
					// Umeng统计"单项4"
					UmengUtils.setOnEvent(context,
							UmengUtils.GAMECENTER_SINGLE4);
				} else if (getPosition() == 6) {
					// Umeng统计"单项7"
					UmengUtils.setOnEvent(context,
							UmengUtils.GAMECENTER_SINGLE7);
				}
			} else if (infos.size() > getPosition() - 1
					&& v.getId() == panel.getItem5_bellow().getId()) {
				adInfo = infos.get(getPosition() - 1);
				view = panel.getItem5_bellow_iv();
				if (getPosition() == 4) {
					// Umeng统计"单项5"
					UmengUtils.setOnEvent(context,
							UmengUtils.GAMECENTER_SINGLE5);
				} else if (getPosition() == 6) {
					// Umeng统计"单项8"
					UmengUtils.setOnEvent(context,
							UmengUtils.GAMECENTER_SINGLE8);
				}
			} else {
				return;
			}

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
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		int action = event.getAction();
		if (v.getId() == panel.getItem5_top().getId()) {
			if (action == KeyEvent.ACTION_DOWN)
				if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
					((MainActivity) context).forceFocusTab(
							MainActivity.TAB_ID_GAMECENTER, 0);
					((MainActivity) context).setGameCenterFcous(recyclerView
							.getChildAt(0));
					return true;
				} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
					if (panel.getItem5_bellow().isShown()
							&& panel.getItem5_bellow().isFocusable()) {
						panel.getItem5_bellow().requestFocus();
						return true;
					}
					return true;
				}
		} else {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {

					return true;
				}
			}
		}
		return false;
	}
}
