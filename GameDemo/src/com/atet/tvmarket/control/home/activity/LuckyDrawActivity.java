package com.atet.tvmarket.control.home.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.control.mine.MineAccountManagerActivity;
import com.atet.tvmarket.control.promotion.view.RewardExchangeView;
import com.atet.tvmarket.entity.ObtainRewardReq;
import com.atet.tvmarket.entity.ObtainRewardResp;
import com.atet.tvmarket.entity.RewardInfoResp;
import com.atet.tvmarket.entity.RewardInfoResp.RewardList;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.FileUtils;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UIUtils;
import com.atet.tvmarket.view.CommonProgressDialog;
import com.atet.tvmarket.view.NewToast;

public class LuckyDrawActivity extends BaseActivity {

	private ALog alog = ALog.getLogger(LuckyDrawActivity.class);
	private String path = FileUtils.getDir("lucky") + "lucky.txt";
	private ImageFetcher mImageFetcher = new ImageFetcher();
	// 设置一个时间常量，此常量有两个作用，1.圆灯视图显示与隐藏中间的切换时间；2.指针转一圈所需要的时间，现设置为500毫秒
	private static final long ONE_WHEEL_TIME = 500;
	// 领取奖品是否成功
	private boolean isDrawedSuc = false;
	// 是否可以领取
	// 记录圆灯视图是否显示的布尔常量
	private boolean lightsOn = true;
	// 开始转动时候的角度，初始值为0
	private int startDegree = 0;

	// 增加的度数
	private int increaseDegree;
	private int angle = 0;

	private ImageView pointIv;

	// private ImageView wheelIv;

	// 指针转圈圈数数据源
	private int[] laps = { 30 };
	// 指针所指向的角度数据源，因为有6个选项，所有此处是6个值
	private int[] angles18 = { 18, 54, 90, 126, 162, 198, 234, 270, 306, 342 };
	private int[] angles36 = { 0, 36, 72, 108, 144, 180, 216, 252, 288, 324 };
	/*
	 * private int[] angles_10 = { 0, 60, 240 }; private int[] angles_thanks = {
	 * 0, 180, 300 }; private int[] angles_5 = { 0, 120, 180 };
	 */
	// 转盘内容数组
	/*private String[] lotteryStr = { "200积分", "iphone 6s", "100积分", "手柄",
			"50积分", "谢谢参与", "500积分", "100元话费", "iwatch", "50元话费" };*/
/*	private String[] scrollStr0 = { "200积分", "iphone 6s", "100积分", "手柄",
			"50积分", "500积分", "100元话费", "200积分", "200积分", "200积分", "200积分",
			"200积分", "50积分", "500积分", "100元话费", "iwatch", "50元话费", "200积分",
			"200积分", "200积分", "50积分", "500积分", "100元话费", "50元话费" };*/
	private List<String> scrollStr = new ArrayList<String>();

	private RewardExchangeView lucky_scroll;
	private RewardInfoResp rewardInfo;
	private ImageView Lucky_wheel;
	private List<RewardList> rewardList;
	private ObtainRewardResp info;
	private int code;
	// 抽奖失败信息
	private String mFailMsg;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			FileUtils.writeToFile(path, true,String.valueOf(msg.obj));
			isDrawedSuc = true;
		};
	};

	// 监听动画状态的监听器
	private AnimationListener al = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			pointIv.setClickable(false);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			int index = (increaseDegree % 360 - 18) / 36;
			if (code == TaskResult.OK) {
				String msg = null;
				if(rewardList != null && rewardList.size() != 0){
					msg = rewardList.get(index).getRewardName();
				}
				if(index != 5){
					NewToast.makeToast(LuckyDrawActivity.this, "获得了" + msg,Toast.LENGTH_LONG).show();
				}else{
					NewToast.makeToast(LuckyDrawActivity.this, "谢谢参与",Toast.LENGTH_LONG).show();
				}
			} else {
				if (!TextUtils.isEmpty(mFailMsg)) {
					NewToast.makeToast(LuckyDrawActivity.this, mFailMsg,Toast.LENGTH_LONG).show();
				} else if (TaskResult.isNetworkException(code)) {
					NewToast.makeToast(LuckyDrawActivity.this, "网络连接异常，请检查网络连接！",
							Toast.LENGTH_LONG).show();
				} else {
					NewToast.makeToast(LuckyDrawActivity.this, "领取奖品失败！",
							Toast.LENGTH_LONG).show();
				}
			}
			pointIv.setClickable(true);

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
		// flashLights();

	}

	private void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_luckydraw);
		ScaleViewUtils.init(this);
		ScaleViewUtils.scaleView(getWindow().getDecorView());
		// lightIv = (ImageView) findViewById(R.id.light);
		pointIv = (ImageView) findViewById(R.id.point);
		lucky_scroll = (RewardExchangeView) findViewById(R.id.lucky_scroll);
		Lucky_wheel = (ImageView) findViewById(R.id.lucky_wheel_bg);

		lucky_scroll.setDesc(UIUtils.getString(R.string.has_obtain));
	
		pointIv.setOnClickListener(new MyClickListener());
		// wheelIv = (ImageView) findViewById(R.id.main_wheel);
		pointIv.requestFocus();
	}

	private void initData() {
		rewardInfo = (RewardInfoResp) getIntent().getSerializableExtra(Constant.REWARDINFORESP);
		if (rewardInfo != null) {
			rewardList = rewardInfo.getRewardList();
		}
		if (rewardList != null && rewardList.size() != 0) {
			mImageFetcher.loadImage(rewardList.get(0).getIcon(), Lucky_wheel,R.drawable.wheel_bg);
			for (int i = 0; i < rewardList.size(); i++) {
				String rewardName = rewardList.get(i).getRewardName();
				if(!rewardName.equals("谢谢参与")){
					scrollStr.add(rewardName);
				}
			}
			lucky_scroll.setGoods(scrollStr,true);
		}
		// alog.info(rewardInfo.toString());
	}

	private int getRotateDegree() {
		String rewardId = rewardInfo.getRewardId();
		if (rewardList != null) {
			for (int i = 0; i < rewardList.size(); i++) {
				RewardList rewardList2 = rewardList.get(i);
				//System.out.println("rewardList2 == " + rewardList2);
				if (rewardId.equals(rewardList2.getRewardId())) {
					// alog.info(rewardList2.getSequence());
					if (startDegree % 36 == 0) {
						angle = angles18[i];
					} else {
						angle = angles36[i];
					}
				}
			}
		}
		return angle;
	}

	/**
	 * @description: 领取抽奖获取的奖励
	 * 
	 * @author: LiuQin
	 * @date: 2015年9月12日 下午9:00:57
	 */
	public void obtainReward(final int userId) {
		alog.debug("");
		
		// 奖品id
		String rewardId = rewardInfo.getRewardId();

		ObtainRewardReq reqInfo = new ObtainRewardReq();
		reqInfo.setUserId(userId);
		reqInfo.setRewardId(rewardId);
		ReqCallback<ObtainRewardResp> reqCallback = new ReqCallback<ObtainRewardResp>() {

			@Override
			public void onResult(TaskResult<ObtainRewardResp> taskResult) {
				code = taskResult.getCode();
				info = taskResult.getData();
				alog.info("taskResult code:" + code);
				System.out.println("Lucky = " + code );
				if (code == TaskResult.OK) {
					// "领取奖品成功！！！！！！！！！！！");
					Message msg = Message.obtain();
					msg.obj = userId;
					mHandler.sendMessage(msg);
					alog.info(info.toString());
				} else {
					mFailMsg = taskResult.getMsg();
					if (code == TaskResult.HTTP_NO_CONNECTION) {
						// UIUtils.makeText(getApplicationContext(),
						// "网络连接异常，请检查网络连接！");
						isDrawedSuc = false;
					} else if (code == 13002) {
						// UIUtils.makeText(getApplicationContext(),
						// "您今天已经抽过一次了，明天再来吧");
						isDrawedSuc = true;
					} else {
						// UIUtils.makeText(getApplicationContext(), "领取奖品失败！");
						isDrawedSuc = false;
					}
				}
			}
		};
		DataFetcher.obtainReward(getApplicationContext(), reqCallback, reqInfo,
				false).request(getApplicationContext());
	}
	/**
	 * 
	 * @description: 将用户访问的时间存储
	 *
	 * @param userId 
	 * @throws: 
	 * @author: LiJie
	 * @date: 2015年9月29日 下午5:01:34
	 */
	protected void writeToFile(int userId) {
		
	}

	class MyClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			int userId = DataFetcher.getUserIdInt();
			//判断用户是否登陆
			if (!DataFetcher.isUserLogin()) {
				snapDialog();
				return;
			}
			if(!isDrawedSuc){
				if (NetUtil.isNetworkAvailable(getApplicationContext(), true)) {
					
						if(rewardList != null && rewardList.size() > 5)
							if(rewardInfo.getRewardId().equals(rewardList.get(5).getRewardId())){
								//如果是谢谢惠顾不调用接口
								isDrawedSuc = true;
								FileUtils.writeToFile(path, true,String.valueOf(userId));
							}else{
								obtainReward(userId);
							}
					
				} else {
					UIUtils.makeText(getApplicationContext(), "网络连接异常，请检查网络连接！");
				}
				
				int lap = laps[(int) (Math.random() * 1)];
				/*
				 * if(isDrawed){ startDegree = 0; }
				 */
				// 每次转圈角度增量
				// startDegree = startDegree % 360;
				increaseDegree = lap * 360 + getRotateDegree();
				// 初始化旋转动画，后面的四个参数是用来设置以自己的中心点为圆心转圈
				RotateAnimation rotateAnimation = new RotateAnimation(
						startDegree, startDegree + increaseDegree,
						RotateAnimation.RELATIVE_TO_SELF, 0.5f,
						RotateAnimation.RELATIVE_TO_SELF, 0.65f);
				// 将最后的角度赋值给startDegree作为下次转圈的初始角度
				// startDegree += increaseDegree;
				// 计算动画播放总时间
				long time = (lap + angle / 360) * ONE_WHEEL_TIME;
				rotateAnimation.setDuration(time);
				rotateAnimation.setFillAfter(true);
				// 设置动画的加速行为，是先加速后减速
				rotateAnimation.setInterpolator(LuckyDrawActivity.this,
						android.R.anim.accelerate_decelerate_interpolator);
				rotateAnimation.setAnimationListener(al);
				pointIv.startAnimation(rotateAnimation);
				
			}else{
				UIUtils.makeText(getApplicationContext(), "今天已经抽过奖了，明天再来吧！");
			}
		}
	}

	private void snapDialog() {
		CommonProgressDialog mDialog = new CommonProgressDialog.Builder(this)
				.setMessage(R.string.login_or_out)
				.setPositiveButton(R.string.ok_login,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(
										LuckyDrawActivity.this,
										MineAccountManagerActivity.class);
								// intent.putExtra(Constant.is_login_success,
								// false);
								startActivityForResult(intent, 0);
							}

						}).setNegativeButton(R.string.cancle_login, null)
				.create();
		mDialog.setParams(mDialog);
		mDialog.show();
	}
}
