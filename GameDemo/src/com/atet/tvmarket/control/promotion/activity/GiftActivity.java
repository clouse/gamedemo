package com.atet.tvmarket.control.promotion.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.control.home.inf.OnRecyItemClickListener;
import com.atet.tvmarket.control.mine.MineAccountManagerActivity;
import com.atet.tvmarket.control.promotion.adapter.GiftAdapter;
import com.atet.tvmarket.control.promotion.decoration.GiftInsetDecoration;
import com.atet.tvmarket.control.promotion.holder.GiftHolder;
import com.atet.tvmarket.entity.dao.GameGiftInfo;
import com.atet.tvmarket.entity.dao.UserGameGiftInfo;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.GamepadTool;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UIUtils;
import com.atet.tvmarket.view.CommonProgressDialog;
import com.atet.tvmarket.view.LoadingView;
import com.atet.tvmarket.view.NewToast;
import com.atet.tvmarket.view.TvRecyclerView;

/*
 * File：GiftActivity.java
 *
 * Copyright (C) 2015 MainActivity Project
 * Date：2015年6月10日 下午6:00:46
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */
public class GiftActivity extends BaseActivity implements
		OnRecyItemClickListener, OnClickListener, OnKeyListener {
	ALog alog = ALog.getLogger(GiftActivity.class);
	private static final int POINT_LEFTMARGIN = 10;
	public static final int POINT_WIDTH = 10;
	public static final int POINT_HEIGHT = 10; //点的大小
	
	private RecyclerView recyclerView;
	private GridLayoutManager manager;
	private GiftAdapter adapter;
	private GiftInsetDecoration insertDecoration;
	public Button box_bt;
	private int position;
	private LinearLayout point_container;
	private boolean isRestart = false;

	private List<GameGiftInfo> infos = new ArrayList<GameGiftInfo>();
	private List<GameGiftInfo> pageGames = new ArrayList<GameGiftInfo>();
	private List<UserGameGiftInfo> receivedGift = new ArrayList<UserGameGiftInfo>();
	private UserGameGiftInfo userGameGiftInfo;
	private GameGiftInfo gameGiftInfo;

	private int pageSize = 0;
	private int currentPage = 0;

	private LoadingView loadingView;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if(isRestart){
				getSameGameInfo();
				if(userGameGiftInfo != null && gameGiftInfo != null){
					GiftHolder holder = (GiftHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(position));
					//NewToast.makeToast(getApplicationContext(), gameGiftInfo.get, duration)
					holder.setData(gameGiftInfo, userGameGiftInfo);
				}
				gameGiftInfo = null;
				userGameGiftInfo = null;
				//changeGiftState();
			}else{
				initData();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		setBlackTitle(false);
		getReceivedGift();
		loadData(false);
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		setTitleData();
	}

	private void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_gift);
		ScaleViewUtils.init(this);
		ScaleViewUtils.scaleView(getWindow().getDecorView());
		
		point_container = (LinearLayout) findViewById(R.id.gift_point);
		box_bt = (Button) findViewById(R.id.gift_Box_bt);
		recyclerView = (RecyclerView) findViewById(R.id.gift_recyclerView);
		loadingView = (LoadingView) findViewById(R.id.gift_contentLoading);
		loadingView.setDataView(recyclerView);
		adapter = new GiftAdapter(recyclerView, this);
		manager = new GridLayoutManager(getApplicationContext(), 2);
		insertDecoration = new GiftInsetDecoration(getApplicationContext());

		box_bt.setOnFocusChangeListener(new FocusChangeListener());
		box_bt.setOnTouchListener(new TouchListener());
		box_bt.setOnClickListener(this);
		box_bt.setOnKeyListener(this);
		adapter.setOnRecyItemClickListener(this);
		manager.setOrientation(LinearLayoutManager.HORIZONTAL);
		// 给recyclerView添加间距
		recyclerView.addItemDecoration(insertDecoration);
		recyclerView.setLayoutManager(manager);
		recyclerView.setAdapter(adapter);
	}

	private void initData() {
		int total = infos.size();
		if (total > 0) {
			if (total % 8 == 0) {
				pageSize = total / 8;
			} else {
				pageSize = total / 8 + 1;
			}
		}
		
		addPoint();
		setAdapters();
	}
	
	private void addPoint(){
		point_container.removeAllViews();
		
		for (int i = 0; i < pageSize; i++) {
			ImageView point = new ImageView(getApplicationContext());
			point.setLayoutParams(new LayoutParams(POINT_WIDTH,POINT_HEIGHT));
			ScaleViewUtils.scaleView(point);
			point.setBackgroundResource(R.drawable.point_default);
			android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			if (i != 0) {
				params.leftMargin = (int)ScaleViewUtils.resetTextSize(POINT_LEFTMARGIN);
			}

			if (i == currentPage) {
				point.setBackgroundResource(R.drawable.point_select);
			}
			
			point_container.addView(point, params);
		}
		
	}
	
	private void setAdapters() {
		pageGames.clear();
		if(infos.size() != 0){
			if ((currentPage + 1) < pageSize) {
				for (int i = currentPage * 8; i < (currentPage + 1) * 8; i++) {
					pageGames.add(infos.get(i));
				}
			} else {
				for (int i = currentPage * 8; i < infos.size(); i++) {
					pageGames.add(infos.get(i));
				}
			}
		}
		
		alog.info("pageGames" + pageGames.size());
		adapter.setData(pageGames,receivedGift);
	}

	public void nextPage() {
		if (pageSize > 1) {
			if (currentPage < pageSize - 1) {
				currentPage++;
			} else {
				currentPage = 0;
			}
			turnPage();
			leftAnimator();
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
			rightAnimator();
		}
	}
	
	private void turnPage(){
		for (int i = 0; i < pageSize; i++) {
			if(i == currentPage){
				point_container.getChildAt(i).setBackgroundResource(R.drawable.point_select);
			}else{
				point_container.getChildAt(i).setBackgroundResource(R.drawable.point_default);
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

	/**
	 * 
	 * @description:进入的游戏中是否有礼包已经领取了
	 * 
	 * @throws: 
	 * @author: LiJie
	 * @date: 2015年8月18日 下午3:01:05
	 */
	private void getSameGameInfo(){
		//pageGames.clear();
		if(pageGames.size() > position){
			gameGiftInfo = pageGames.get(position);
			if(gameGiftInfo != null){
				if(receivedGift.size() != 0){
					for (int i = 0; i < receivedGift.size(); i++) {
						if(gameGiftInfo.getGameid().equals(receivedGift.get(i).getGameid())){
							userGameGiftInfo = receivedGift.get(i);
						}
					}
				}
			}
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
			//NewToast.makeToast(getApplicationContext(), "onActivityResult...", 0).show();
			if(requestCode == 1){
				isRestart = true;
			}else{
				isRestart = false;
			}
			getReceivedGift();
	}
	
	
	public class TouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				OnFocusChangeListener listener = v.getOnFocusChangeListener();
				if (listener != null && listener instanceof FocusChangeListener) {
					((FocusChangeListener) listener).setView(v);
				}
			}
			return false;
		}
	}

	public class FocusChangeListener implements OnFocusChangeListener {

		private View view;

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				if (v.isInTouchMode() && v == view) {
					v.performClick();
				} else {
					view = null;
				}
				box_bt.setBackgroundResource(R.drawable.btn_yellow_selector);

			} else {
				box_bt.setBackgroundColor(UIUtils.getColor(R.color.yellow_bg));
			}
		}

		public View getView() {
			return view;
		}

		public void setView(View view) {
			this.view = view;
		}
	}

	@Override
	public void onItemClick(View view, int position) {
		view.requestFocus();
		this.position = position;
		
		getSameGameInfo();
		
		Intent intent = new Intent(GiftActivity.this, GiftDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Constant.GAMEGIFTINFO, gameGiftInfo);
		bundle.putSerializable(Constant.USERGAMEGIFTINFO, userGameGiftInfo);
		intent.putExtras(bundle);
		startActivityForResult(intent, 1);

	}

	@Override
	public void onClick(final View v) {
		v.requestFocus();
		if (!DataFetcher.isUserLogin()) {
			//NewToast.makeToast(getApplicationContext(), R.string.login_or_out, 0).show();
			snapDialog();
			return;
		}

		Intent intent = new Intent(GiftActivity.this, DepositActivity.class);
		startActivity(intent);
	}
	
	private void snapDialog() {
		CommonProgressDialog mDialog = new CommonProgressDialog.Builder(this)
			.setMessage(R.string.login_or_out)
			.setPositiveButton(R.string.ok_login, new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(GiftActivity.this,MineAccountManagerActivity.class);
					//intent.putExtra(Constant.is_login_success, false);
					startActivityForResult(intent, 0);
				}
				
			})
			.setNegativeButton(R.string.cancle_login, null)
			.create();
		mDialog.setParams(mDialog);
		mDialog.show();
	}


	private void loadData(boolean isRefresh) {
		infos.clear();
		loadingView.getmHandler().sendEmptyMessage(Constant.SHOWLOADING);

		ReqCallback<List<GameGiftInfo>> reqCallback = new ReqCallback<List<GameGiftInfo>>() {
			@Override
			public void onResult(TaskResult<List<GameGiftInfo>> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					infos = taskResult.getData();
					if (infos != null && !infos.isEmpty()) {
						alog.info(infos.toString());
						mHandler.sendEmptyMessage(0);
						loadingView.getmHandler().sendEmptyMessage(Constant.DISMISSLOADING);
					} else {
						if (NetUtil.isNetworkAvailable(GiftActivity.this, true)) {
							alog.info("数据为空0");
							loadingView.getmHandler().sendEmptyMessage(Constant.NULLDATA);
						} else {
							alog.info("网络异常1");
							loadingView.getmHandler().sendEmptyMessage(Constant.EXCEPTION);
						}
					}
				} else {
					alog.info("failed");
					if (NetUtil.isNetworkAvailable(GiftActivity.this, true)) {
						alog.info("数据为空");
						loadingView.getmHandler().sendEmptyMessage(Constant.NULLDATA);
					} else {
						alog.info("出现异常");
						loadingView.getmHandler().sendEmptyMessage(
								Constant.EXCEPTION);
					}
				}
			}

			@Override
			public void onUpdate(TaskResult<List<GameGiftInfo>> taskResult) {

			}
		};
		if(isRefresh){
			DataFetcher.getGameGift(getApplicationContext(), reqCallback, false)
			.registerUpdateListener(reqCallback)
			.refresh(getApplicationContext());
		}else{
			DataFetcher.getGameGift(getApplicationContext(), reqCallback, false)
			.registerUpdateListener(reqCallback)
			.request(getApplicationContext());
		}

	}

	private void getReceivedGift() {
		// 先检测用户是否登录
		if (!DataFetcher.isUserLogin()) {
			return;
		}
		// 用户id
		String userId = DataFetcher.getUserId();
		ReqCallback<List<UserGameGiftInfo>> reqCallback = new ReqCallback<List<UserGameGiftInfo>>() {
			@Override
			public void onResult(TaskResult<List<UserGameGiftInfo>> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					receivedGift = taskResult.getData();
					if (infos != null && !infos.isEmpty()) {
						alog.info(infos.toString());
						mHandler.sendEmptyMessage(0);
					} else {
						alog.info("已领取礼包为空");
					}
				} else {
					alog.info("已领取礼包请求数据失败");
					
				}

			}

			@Override
			public void onUpdate(TaskResult<List<UserGameGiftInfo>> taskResult) {

			}
		};
		DataFetcher.getUserGameGift(getApplicationContext(), userId, reqCallback,
						false).registerUpdateListener(reqCallback)
						.request(getApplicationContext());

	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		int action = event.getAction();
		int childCount = recyclerView.getChildCount();
		Button bt;
		if (recyclerView.getChildCount() != 0) {

			if (action == KeyEvent.ACTION_DOWN
					&& keyCode == KeyEvent.KEYCODE_DPAD_UP) {
				if (infos.size() == 0) {
					return true;
				}
				if (childCount < 4) {
					bt = (Button) recyclerView.getChildAt(childCount - 1)
							.findViewById(R.id.gift_top_bt);
				} else {
					bt = (Button) recyclerView.getChildAt(3).findViewById(
							R.id.gift_top_bt);
				}
				bt.requestFocus();
				return true;
			} else if (action == KeyEvent.ACTION_DOWN
					&& keyCode == KeyEvent.KEYCODE_DPAD_LEFT
					|| keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();
	
		//X键刷新
		if(action == KeyEvent.ACTION_DOWN){
			if(GamepadTool.isButtonX(keyCode) || keyCode == KeyEvent.KEYCODE_MENU){
				loadData(true);
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}
	
}
