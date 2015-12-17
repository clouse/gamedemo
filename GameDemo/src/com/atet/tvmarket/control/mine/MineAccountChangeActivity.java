package com.atet.tvmarket.control.mine;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.entity.UserInfo;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.view.LoadingView;
import com.atet.tvmarket.view.NewToast;

public class MineAccountChangeActivity extends BaseActivity {
	private ALog alog = ALog.getLogger(MineAccountChangeActivity.class);
	private RecyclerView accountRecyclerView;
	private AccountchangeAdapter accountAdapter;
	public static final String INTENT_KEY = "type";
	private List<UserInfo> minfos = new ArrayList<UserInfo>();
	private LoadingView loadingView;
	private ImageView loading;
	private AnimationDrawable mAnimationDrawable;
	private UserInfo changeUserInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_mine_account_change);
		ScaleViewUtils.scaleView(this);
		setBlackTitle(false);
		initView();
		getAllLoginedUsers();
		initRecyclerView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setTitleData();
	}

	private void initView() {
		accountRecyclerView = (RecyclerView) findViewById(R.id.mine_accountchange_recycleview);
		loadingView = (LoadingView) findViewById(R.id.contentLoading);
		loadingView.setDataView(accountRecyclerView);
		loadingView.getmHandler().sendEmptyMessage(Constant.DISMISSLOADING);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		accountRecyclerView.setLayoutManager(layoutManager);

		loading = (ImageView) findViewById(R.id.iv_loading);
		mAnimationDrawable = (AnimationDrawable) loading.getDrawable();
	}

	private void getAllLoginedUsers() {
		ReqCallback<List<UserInfo>> reqCallback = new ReqCallback<List<UserInfo>>() {
			@Override
			public void onGetCacheData(String requestTag, boolean result) {
				loadingView.getmHandler()
						.sendEmptyMessage(Constant.SHOWLOADING);
			}

			@Override
			public void onResult(TaskResult<List<UserInfo>> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					List<UserInfo> infos = taskResult.getData();
					alog.info(infos);
					for (UserInfo userInfo : infos) {
						// alog.info(userInfo.getPhoneNum());
						if (userInfo.getUserId()==BaseApplication.userInfo.getUserId()) {
							infos.remove(userInfo);
							break;
						}
					}
					minfos.clear();
					minfos.addAll(infos);

					mHandler.sendEmptyMessage(0);

					loadingView.getmHandler().sendEmptyMessage(
							Constant.DISMISSLOADING);
				}
				if (code == TaskResult.NO_DATA) {
					// 没有登录过的用户
					loadingView.getmHandler().sendEmptyMessage(
							Constant.DISMISSLOADING);
				} else {
					// 显示失败原因
					alog.error(taskResult.getMsg());
					if (NetUtil.isNetworkAvailable(
							MineAccountChangeActivity.this, true)) {
						loadingView.getmHandler().sendEmptyMessage(
								Constant.DISMISSLOADING);
					} else {
						loadingView.getmHandler().sendEmptyMessage(
								Constant.DISMISSLOADING);
					}
				}
			}
		};
		DataFetcher.userGetAllLoginedUsers(this, reqCallback, false).request(
				this);
	}

	public void autoLogin(int userId) {
		mHandler.sendEmptyMessage(1);
		ReqCallback<UserInfo> reqCallback = new ReqCallback<UserInfo>() {
			@Override
			public void onResult(TaskResult<UserInfo> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					// 登录成功
					initUserInfo();
				} else {
					mHandler.sendEmptyMessage(2);
					// 显示失败原因
					alog.error(taskResult.getMsg());
					Message msg = Message.obtain();
					msg.what = 4;
					msg.obj = taskResult.getMsg();
					mHandler.sendMessage(msg);
				}

			}
		};
		DataFetcher.userAutoLoginByUserId(this, reqCallback, userId, false)
				.request(this);
	}

	private void initUserInfo() {
		ReqCallback<UserInfo> reqCallback = new ReqCallback<UserInfo>() {
			@Override
			public void onResult(TaskResult<UserInfo> taskResult) {
				mHandler.sendEmptyMessage(2);
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					/*
					 * UserInfo info = taskResult.getData();
					 * BaseApplication.userInfo = info; alog.info(info);
					 */
					changeUserInfo = taskResult.getData();
					alog.info(changeUserInfo);
					mHandler.sendEmptyMessage(3);
				} else if (code == TaskResult.NO_DATA) {
					// 没有登录过的用户
					alog.error("用户暂时未登陆");
					Message msg = Message.obtain();
					msg.what = 4;
					msg.obj = "切换账户失败";
					mHandler.sendMessage(msg);
				} else {
					// 显示失败原因
					alog.error(taskResult.getMsg());
					Message msg = Message.obtain();
					msg.what = 4;
					msg.obj = taskResult.getMsg();
					mHandler.sendMessage(msg);
				}
			}
		};
		DataFetcher.userGetLastLoginedUser(this, reqCallback, false).request(
				this);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				initRecyclerView();
				break;
			case 1:
				loading.setVisibility(View.VISIBLE);
				mAnimationDrawable.start();
				break;
			case 2:
				mAnimationDrawable.stop();
				loading.setVisibility(View.INVISIBLE);
				break;
			case 3:
				NewToast.makeToast(MineAccountChangeActivity.this, "切换账户成功",
						Toast.LENGTH_SHORT).show();
				if (changeUserInfo != null) {
					// changeUserInfo = tmpUserInfo;
					BaseApplication.userInfo = changeUserInfo;
				}
				finish();
				break;
			case 4:
				if(NetUtil.isNetworkAvailable(MineAccountChangeActivity.this, true)){
					String errorMsg = msg.obj.toString();
					if (errorMsg == "") {
						errorMsg = "系统异常";
					}
					NewToast.makeToast(MineAccountChangeActivity.this, errorMsg,
							Toast.LENGTH_SHORT).show();
				}
				else{
					NewToast.makeToast(MineAccountChangeActivity.this, "网络未连接",
							Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
			}

		};
	};

	private void initRecyclerView() {
		accountAdapter = new AccountchangeAdapter(minfos,
				new AccountchangeAdapter.IAccountIconClick() {
					@Override
					public void accountIconClick(UserInfo userInfo) {

						if (AccountchangeAdapter.LOGIN.equals(userInfo
								.getUserName())) {
							Intent intent = new Intent(
									MineAccountChangeActivity.this,
									MineAccountManagerActivity.class);
							intent.putExtra(INTENT_KEY,
									AccountchangeAdapter.LOGIN);
							startActivityForResult(intent, 9004);
						} else if (AccountchangeAdapter.REGISTER
								.equals(userInfo.getUserName())) {
							Intent intent = new Intent(
									MineAccountChangeActivity.this,
									MineAccountManagerActivity.class);
							intent.putExtra(INTENT_KEY,
									AccountchangeAdapter.REGISTER);
							startActivityForResult(intent, 9004);
						} else {
							autoLogin(userInfo.getUserId());
						}
					}
				}, accountRecyclerView,mImageFetcher);
		accountRecyclerView.setAdapter(accountAdapter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 9004 && resultCode == 8001) {
			finish();
		}
	}
}
