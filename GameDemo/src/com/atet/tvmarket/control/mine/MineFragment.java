package com.atet.tvmarket.control.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atet.tvmarket.R;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.control.common.BaseFragment;
import com.atet.tvmarket.control.home.MainActivity;
import com.atet.tvmarket.control.setup.SetupChildlockActivity;
import com.atet.tvmarket.control.setup.SetupHandlelinkActivity;
import com.atet.tvmarket.control.setup.SetupUpdateActivity;
import com.atet.tvmarket.control.setup.SetupVideoActivity;
import com.atet.tvmarket.entity.DeviceInfo;
import com.atet.tvmarket.entity.UserInfo;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.DataHelper;
import com.atet.tvmarket.utils.GAnimationUtils;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UmengUtils;
import com.atet.tvmarket.view.NewToast;
import com.atetpay.PayInfo;
import com.atetpay.pay.data.PAY;
import com.atetpay.recharge.InpourPriceActivity;
import com.atetpay.recharge.data.RECHARGE;

@SuppressLint("NewApi")
public class MineFragment extends BaseFragment {

	private View rootView;
	private ImageView ivAvatar;
	private TextView tvNickname, tvMoney, tvPoint;
	private RelativeLayout mLayoutAccount, mLayoutRecharge, mLayoutAccountSafe,
			mLayoutAccountChange, mLayoutAbout, mLayoutChildlock, mLayoutVideo,
			mLayoutHandle, mLayoutUpdate;
	private RelativeLayout mLayoutLogined, mLayoutUnLogin;
	private MainActivity context;

	public MineFragment newInstance() {
		MineFragment mineFragment = new MineFragment();
		Bundle bundle = new Bundle();
		mineFragment.setArguments(bundle);
		return mineFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) { 
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		System.out.println("MineFragment onCreate()");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("MineFragment onCreateView()");
		rootView = inflater.inflate(R.layout.fragment_mine_main, container,
				false);
		ScaleViewUtils.scaleView(rootView);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		System.out.println("MineFragment onActivityCreated");
		context = (MainActivity) getActivity();
		initView();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
		// Umeng页面统计
		UmengUtils.onPageStart(UmengUtils.MINE_FRAGMENT);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// Umeng页面统计
		UmengUtils.onPageEnd(UmengUtils.MINE_FRAGMENT);
	}

	private void initView() {
		GAnimationUtils.init(getActivity());
		FocusChangeListener onFocusChangeListener = new FocusChangeListener();
		ClickListener onClickListener = new ClickListener();
		TouchListener onTouchListener = new TouchListener();

		mLayoutAccount = (RelativeLayout) rootView	.findViewById(R.id.mine_user_layout_bg);
		mLayoutAccount.setNextFocusUpId(R.id.tab_my);
		mLayoutAccount.setNextFocusRightId(R.id.mine_recharge_layout_bg);
		mLayoutAccount.setNextFocusDownId(mLayoutAccount.getId());
		mLayoutAccount.setOnClickListener(onClickListener);
		mLayoutAccount.setOnTouchListener(onTouchListener);
		mLayoutAccount.setOnFocusChangeListener(onFocusChangeListener);
		mLayoutAccount.setNextFocusLeftId(mLayoutAccount.getId());
		mLayoutRecharge = (RelativeLayout) rootView.findViewById(R.id.mine_recharge_layout_bg);
		mLayoutRecharge.setNextFocusUpId(R.id.tab_my);
		mLayoutRecharge.setOnClickListener(onClickListener);
		mLayoutRecharge.setOnTouchListener(onTouchListener);
		mLayoutRecharge.setOnFocusChangeListener(onFocusChangeListener);
		mLayoutAccountSafe = (RelativeLayout) rootView.findViewById(R.id.mine_accountsafe_layout_bg);
		mLayoutAccountSafe.setNextFocusUpId(R.id.tab_my);
		mLayoutAccountSafe.setOnClickListener(onClickListener);
		mLayoutAccountSafe.setOnTouchListener(onTouchListener);
		mLayoutAccountSafe.setOnFocusChangeListener(onFocusChangeListener);
		mLayoutAccountChange = (RelativeLayout) rootView.findViewById(R.id.mine_accountchange_layout_bg);
		mLayoutAccountChange.setOnClickListener(onClickListener);
		mLayoutAccountChange.setOnTouchListener(onTouchListener);
		mLayoutAccountChange.setOnFocusChangeListener(onFocusChangeListener);
		mLayoutAccountChange.setNextFocusDownId(mLayoutAccountChange.getId());
		mLayoutAbout = (RelativeLayout) rootView.findViewById(R.id.mine_about_layout_bg);
		mLayoutAbout.setOnClickListener(onClickListener);
		mLayoutAbout.setOnTouchListener(onTouchListener);
		mLayoutAbout.setOnFocusChangeListener(onFocusChangeListener);
		mLayoutAbout.setNextFocusDownId(mLayoutAbout.getId());
		mLayoutLogined = (RelativeLayout) rootView	.findViewById(R.id.layout_logined);
		mLayoutUnLogin = (RelativeLayout) rootView	.findViewById(R.id.layout_unlogin);

		ivAvatar = (ImageView) rootView.findViewById(R.id.mine_user_avatar_logined_iv);
		tvNickname = (TextView) rootView.findViewById(R.id.mine_user_nickname_logined_tv);
		tvMoney = (TextView) rootView	.findViewById(R.id.mine_user_money_logined_tv);
		tvPoint = (TextView) rootView	.findViewById(R.id.mine_user_point_logined_tv);

		mLayoutChildlock = (RelativeLayout) rootView.findViewById(R.id.setup_childlock_layout_bg);
		mLayoutChildlock.setOnClickListener(onClickListener);
		mLayoutChildlock.setOnTouchListener(onTouchListener);
		mLayoutChildlock.setOnFocusChangeListener(onFocusChangeListener);
		mLayoutChildlock.setNextFocusUpId(R.id.tab_my);
		mLayoutVideo = (RelativeLayout) rootView.findViewById(R.id.setup_video_layout_bg);
		mLayoutVideo.setOnClickListener(onClickListener);
		mLayoutVideo.setOnTouchListener(onTouchListener);
		mLayoutVideo.setOnFocusChangeListener(onFocusChangeListener);
		mLayoutVideo.setNextFocusUpId(R.id.tab_my);
		mLayoutHandle = (RelativeLayout) rootView.findViewById(R.id.setup_handle_layout_bg);
		mLayoutHandle.setOnClickListener(onClickListener);
		mLayoutHandle.setOnTouchListener(onTouchListener);
		mLayoutHandle.setOnFocusChangeListener(onFocusChangeListener);
		mLayoutHandle.setNextFocusDownId(mLayoutHandle.getId());
		mLayoutUpdate = (RelativeLayout) rootView.findViewById(R.id.setup_update_layout_bg);

		mLayoutUpdate.setOnClickListener(onClickListener);
		mLayoutUpdate.setOnTouchListener(onTouchListener);
		mLayoutUpdate.setOnFocusChangeListener(onFocusChangeListener);
		mLayoutUpdate.setNextFocusRightId(mLayoutUpdate.getId());
		mLayoutUpdate.setNextFocusDownId(mLayoutUpdate.getId());
	}

	private void initData() {
		if (BaseApplication.userInfo != null) {
			mLayoutUnLogin.setVisibility(View.INVISIBLE);
			mLayoutLogined.setVisibility(View.VISIBLE);
			UserInfo userInfo = BaseApplication.userInfo;
			if (userInfo.getIcon() == null) {
				context.mImageFetcher.loadImage("", ivAvatar,
						R.drawable.mine_user_default_avator);
			} else {
				context.mImageFetcher.loadImage(userInfo.getIcon(), ivAvatar,
						R.drawable.mine_user_default_avator);
			}
			tvNickname.setText("账号    " + userInfo.getUserName());
			// tvPhone.setText("手机号:" + userInfo.getPhoneNum());
			tvMoney.setText("A 币    " + userInfo.getBalance()/100f);
			tvPoint.setText("积分    " + userInfo.getIntegral());
			// mLayoutAccount.setOnClickListener(null);
		} else {
			mLayoutUnLogin.setVisibility(View.VISIBLE);
			mLayoutLogined.setVisibility(View.INVISIBLE);
		}
	}

	class TouchListener implements OnTouchListener {
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if ((requestCode == 9991) || (requestCode == 8001)) {
			context.setTitleData();
			initData();
		} else if (requestCode == 9992) {// 支付
			if (resultCode == PAY.CODE.SUCCESS) {
				int price = data.getIntExtra(RECHARGE.PRICE, 0);
				// Log.i("充值成功", "金额为" + price);
				int balance = BaseApplication.userInfo.getBalance()+price;
				BaseApplication.userInfo.setBalance(balance);
				tvMoney.setText("A 币    " + balance/100f);
				NewToast.makeToast(getActivity(), "充值成功,金额为:" + price/100f + "元",
						Toast.LENGTH_SHORT).show();
				DataHelper.updateUserInfo(BaseApplication.userInfo);
				
			} else {
				NewToast.makeToast(getActivity(), "充值失败或取消", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	class ClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = null;
			// Umeng统计交互
			UmengUtils.setOnEvent(getActivity(), UmengUtils.MINE_INTERACTION);
			int id = v.getId();
			if (id == R.id.mine_user_layout_bg) {
				if (BaseApplication.userInfo == null) {
					intent = new Intent(getActivity(),
							MineAccountManagerActivity.class);
					startActivityForResult(intent, 9991);
				}
			} else if (id == R.id.mine_recharge_layout_bg) {
				/*
				 * intent = new Intent(getActivity(),
				 * MineAccountSafetyActivity.class);
				 */
				// NewToast.makeToast(getActivity(), "敬请期待",
				// Toast.LENGTH_SHORT).show();
				OpenRechargeActivity();
			} else if (id == R.id.mine_accountsafe_layout_bg) {
				if (BaseApplication.userInfo != null) {
					intent = new Intent(getActivity(),
							MineAccountSafetyActivity.class);
					startActivityForResult(intent, 9991);
				} else {
					NewToast.makeToast(getActivity(), "请先登录游戏大厅",
							Toast.LENGTH_SHORT).show();
				}
			} else if (id == R.id.mine_accountchange_layout_bg) {
				if (BaseApplication.userInfo != null) {
					intent = new Intent(getActivity(),
							MineAccountChangeActivity.class);
					startActivityForResult(intent, 9991);
				} else {
					NewToast.makeToast(getActivity(), "请先登录游戏大厅",
							Toast.LENGTH_SHORT).show();
				}
			} else if (id == R.id.mine_about_layout_bg) {
				intent = new Intent(getActivity(), MineAboutActivity.class);
				startActivity(intent);
			} else if (id == R.id.setup_childlock_layout_bg) {
				// umeng统计进入"童锁"相关信息
				UmengUtils.setOnEvent(getActivity(),
						UmengUtils.SETUP_CHILDLOCK_ENTER);
				intent = new Intent(getActivity(), SetupChildlockActivity.class);
				startActivity(intent);
			} else if (id == R.id.setup_video_layout_bg) {
				// umeng统计进入"视频导入"相关信息
				UmengUtils.setOnEvent(getActivity(),
						UmengUtils.SETUP_VIDEO_ENTER);
				intent = new Intent(getActivity(), SetupVideoActivity.class);
				startActivity(intent);
			} else if (id == R.id.setup_handle_layout_bg) {
				// umeng统计进入"手柄连接"相关信息
				UmengUtils.setOnEvent(getActivity(),
						UmengUtils.SETUP_HANDLE_ENTER);
				intent = new Intent(getActivity(),
						SetupHandlelinkActivity.class);
				startActivity(intent);
			} else if (id == R.id.setup_update_layout_bg) {
				// umeng统计进入"检查更新"相关信息
				UmengUtils.setOnEvent(getActivity(),
						UmengUtils.SETUP_UPDATE_ENTER);
				intent = new Intent(getActivity(), SetupUpdateActivity.class);
				startActivity(intent);
			}
		}
	}

	private void OpenRechargeActivity() {
		// 先检测用户是否登录
		if (!DataFetcher.isUserLogin()) {
			// 提示用户未登录
			NewToast.makeToast(getActivity(), "请先登录游戏大厅", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		// 用户id
		int userId = DataFetcher.getUserIdInt();
		DeviceInfo deviceInfo = DataHelper.getDeviceInfo();

		PayInfo info = new PayInfo();
		info.addParams(PayInfo.ATETID, deviceInfo.getAtetId());
		info.addParams(PayInfo.DEVICEID, deviceInfo.getServerId());
		info.addParams(PayInfo.PRODUCTID, deviceInfo.getDeviceUniqueId());
		info.addParams(PayInfo.DEVICECODE, deviceInfo.getDeviceModel());
		info.addParams(PayInfo.UESERID, userId);

		Intent intent = new Intent(getActivity(), InpourPriceActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(PAY.PAYINFO, info);
		intent.putExtras(bundle);

		startActivityForResult(intent, 9992);
	}

	class FocusChangeListener implements OnFocusChangeListener {

		private View view;

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				v.bringToFront();
				GAnimationUtils.zoomOut(v);
				if (v.isInTouchMode() && v == view) {
					v.performClick();
				} else {
					view = null;
				}
			} else {
				GAnimationUtils.zoomIn(v);
			}
		}

		public View getView() {
			return view;
		}

		public void setView(View view) {
			this.view = view;
		}
	}
}
