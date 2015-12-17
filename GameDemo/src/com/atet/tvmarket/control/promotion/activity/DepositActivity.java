package com.atet.tvmarket.control.promotion.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.control.home.inf.OnRecyItemClickListener;
import com.atet.tvmarket.control.promotion.adapter.DepositAdapter;
import com.atet.tvmarket.control.promotion.decoration.DepositInsetDecoration;
import com.atet.tvmarket.entity.dao.UserGameGiftInfo;
import com.atet.tvmarket.entity.dao.UserGiftInfo;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.GamepadTool;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.ScreenShot;
import com.atet.tvmarket.view.BaseImageView;
import com.atet.tvmarket.view.CloseAcceTextView;
import com.atet.tvmarket.view.LoadingView;
import com.atet.tvmarket.view.blur.BlurringView;

/*
 * File：DepositActivity.java
 *
 * Copyright (C) 2015 MainActivity Project
 * Date：2015年7月8日 下午8:59:24
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */
public class DepositActivity extends BaseActivity implements
		OnRecyItemClickListener {
	ALog alog = ALog.getLogger(DepositActivity.class);
	private static final int POINT_LEFTMARGIN = 10;
	public static final int POINT_WIDTH = 10;
	public static final int POINT_HEIGHT = 10; // 点的大小

	private RecyclerView recyclerView;
	private GridLayoutManager manager;
	private DepositAdapter adapter;
	private DepositInsetDecoration insetDecoration;
	private LinearLayout point_container;

	private List<UserGameGiftInfo> infos = new ArrayList<UserGameGiftInfo>();
	private List<UserGameGiftInfo> pageGames = new ArrayList<UserGameGiftInfo>();

	private int pageSize = 0;
	private int currentPage = 0;

	private LoadingView loadingView;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			initData();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		setBlackTitle(false);
		loadData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setTitleData();
	}

	private void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_deposit);
		ScaleViewUtils.init(this);
		ScaleViewUtils.scaleView(getWindow().getDecorView());

		recyclerView = (RecyclerView) findViewById(R.id.deposit_recyclerview);
		loadingView = (LoadingView) findViewById(R.id.deposit_contentLoading);
		loadingView.setDataView(recyclerView);
		point_container = (LinearLayout) findViewById(R.id.deposit_point);

		manager = new GridLayoutManager(getApplicationContext(), 3);
		adapter = new DepositAdapter(recyclerView, this);
		insetDecoration = new DepositInsetDecoration(getApplicationContext());

		manager.setOrientation(GridLayoutManager.HORIZONTAL);
		recyclerView.addItemDecoration(insetDecoration);
		recyclerView.setLayoutManager(manager);
		recyclerView.setAdapter(adapter);
		adapter.setOnRecyItemClickListener(this);

	}

	private void loadData() {
		// 先检测用户是否登录
		if (!DataFetcher.isUserLogin()) {
			return;
		}

		loadingView.getmHandler().sendEmptyMessage(Constant.SHOWLOADING);

		// 用户id
		String userId = DataFetcher.getUserId();
		// NewToast.makeToast(this, "userId = " + userId, 0).show();
		ReqCallback<List<UserGameGiftInfo>> reqCallback = new ReqCallback<List<UserGameGiftInfo>>() {
			@Override
			public void onResult(TaskResult<List<UserGameGiftInfo>> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					infos = taskResult.getData();
					if (infos != null && !infos.isEmpty()) {
						alog.info(infos.toString());
						mHandler.sendEmptyMessage(0);
						for (int i = 0; i < infos.size(); i++) {
							// alog.info("gameName = " +
							// infos.get(i).getUserGameToGiftList());
						}
						loadingView.getmHandler().sendEmptyMessage(
								Constant.DISMISSLOADING);
					} else {
						if (NetUtil.isNetworkAvailable(DepositActivity.this,
								true)) {
							loadingView.getmHandler().sendEmptyMessage(
									Constant.NULLDATA);
						} else {
							loadingView.getmHandler().sendEmptyMessage(
									Constant.EXCEPTION);
						}
					}
				} else {
					alog.info("failed");
					if (NetUtil.isNetworkAvailable(DepositActivity.this, true)) {
						alog.info("网络正常");
						loadingView.getmHandler().sendEmptyMessage(
								Constant.NULLDATA);
					} else {
						alog.info("网络异常");
						loadingView.getmHandler().sendEmptyMessage(
								Constant.EXCEPTION);
					}
				}

			}

			@Override
			public void onUpdate(TaskResult<List<UserGameGiftInfo>> taskResult) {

			}
		};
		DataFetcher
				.getUserGameGift(getApplicationContext(), userId, reqCallback,
						false).registerUpdateListener(reqCallback)
				.refresh(getApplicationContext());

	}

	private void initData() {
		int total = infos.size();
		if (total > 0) {
			if (total % 12 == 0) {
				pageSize = total / 12;
			} else {
				pageSize = total / 12 + 1;
			}
		}

		addPoint();
		setAdapters();
	}

	private void addPoint() {
		point_container.removeAllViews();
		for (int i = 0; i < pageSize; i++) {
			ImageView point = new ImageView(getApplicationContext());
			point.setLayoutParams(new LayoutParams(POINT_WIDTH, POINT_HEIGHT));
			ScaleViewUtils.scaleView(point);
			point.setBackgroundResource(R.drawable.point_default);
			android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			if (i != 0) {
				params.leftMargin = (int) ScaleViewUtils
						.resetTextSize(POINT_LEFTMARGIN);
			}

			if (i == currentPage) {
				point.setBackgroundResource(R.drawable.point_select);
			}

			point_container.addView(point, params);
		}

	}

	private void setAdapters() {
		pageGames.clear();
		if ((currentPage + 1) < pageSize) {
			for (int i = currentPage * 12; i < (currentPage + 1) * 12; i++) {
				pageGames.add(infos.get(i));
			}
		} else {
			for (int i = currentPage * 12; i < infos.size(); i++) {
				pageGames.add(infos.get(i));
			}
		}
		alog.info("pageGames = " + pageGames.size());
		adapter.setData(pageGames);
	}

	public void nextPage() {

		if (pageSize > 1) {
			if (currentPage < pageSize - 1) {
				currentPage++;
			} else {
				currentPage = 0;
			}

			turnPage();
			rightAnimator();
		}
	}

	public void previousPage() {
		if (pageSize > 1) {

			if (currentPage > 0) {
				currentPage--;
			} else {
				currentPage = pageSize - 1;
			}
			turnPage();
			leftAnimator();
		}
	}

	private void turnPage() {
		for (int i = 0; i < pageSize; i++) {
			if (i == currentPage) {
				point_container.getChildAt(i).setBackgroundResource(
						R.drawable.point_select);
			} else {
				point_container.getChildAt(i).setBackgroundResource(
						R.drawable.point_default);
			}
		}
		setAdapters();
	}

	public void rightAnimator() {
		ViewCompat.setTranslationX(recyclerView, recyclerView.getWidth());
		ViewCompat.animate(recyclerView).cancel();
		ViewCompat.animate(recyclerView).translationX(0).translationY(0)
				.setDuration(1000);
	}

	public void leftAnimator() {
		ViewCompat.setTranslationX(recyclerView, -recyclerView.getWidth());
		ViewCompat.animate(recyclerView).cancel();
		ViewCompat.animate(recyclerView).translationX(0).translationY(0)
				.setDuration(1000);
	}

	@Override
	public void onItemClick(View view, int position) {
		view.requestFocus();
		Intent intent = new Intent(this, DepositBoxActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();

		// X键刷新
		if (action == KeyEvent.ACTION_DOWN) {
			if (GamepadTool.isButtonX(keyCode)
					|| keyCode == KeyEvent.KEYCODE_MENU) {
				loadData();
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

}
