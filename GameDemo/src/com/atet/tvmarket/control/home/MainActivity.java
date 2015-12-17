package com.atet.tvmarket.control.home;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atet.common.logging.ALog;
import com.atet.statistics.utils.StatisticsHelper;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.classify.GameClassifyFragment;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.control.common.BaseFragment;
import com.atet.tvmarket.control.home.activity.LuckyDrawActivity;
import com.atet.tvmarket.control.home.fragments.GameCenterFragment;
import com.atet.tvmarket.control.home.view.GameCenterSecondPanel;
import com.atet.tvmarket.control.home.view.GameCenterThirdPanel;
import com.atet.tvmarket.control.home.view.GameCenterfifthPanel;
import com.atet.tvmarket.control.home.view.GameCenterfourthPanel;
import com.atet.tvmarket.control.mine.MineFragment;
import com.atet.tvmarket.control.mygame.UpdateGameService;
import com.atet.tvmarket.control.promotion.fragment.PromotionFragment;
import com.atet.tvmarket.control.promotion.panel.PromotionFourthPanel;
import com.atet.tvmarket.entity.AppConfig;
import com.atet.tvmarket.entity.RewardInfoResp;
import com.atet.tvmarket.entity.UserInfo;
import com.atet.tvmarket.entity.dao.NoticeInfo;
import com.atet.tvmarket.model.DataConfig;
import com.atet.tvmarket.model.DataConfig.UpdateInterface;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.DataHelper;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.dataupdate.DataUpdateService;
import com.atet.tvmarket.model.net.http.download.DownloadTask;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.CheckMarketVersionUtil1;
import com.atet.tvmarket.utils.FileUtils;
import com.atet.tvmarket.utils.GamepadTool;
import com.atet.tvmarket.utils.IOUtils;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UIUtils;
import com.atet.tvmarket.utils.UmengUtils;
import com.atet.tvmarket.utils.VersionUpdate;
import com.atet.tvmarket.view.CommonProgressDialog;
import com.atet.tvmarket.view.FooterView;
import com.atet.tvmarket.view.NewToast;

/**
 * @description: 程序的入口
 * 
 * @author: LiJie
 * @date: 2015年5月28日 下午5:54:29
 */
public class MainActivity extends BaseActivity {

	private ALog alog = ALog.getLogger(MainActivity.class);
	public int CUR_TAB = 2;// 当前的TAB号
	public static final int TAB_ID_GAMECENTER = 2;// 游戏中心的ID编号
	public static final int TAB_ID_CLASSIFY = 3;// 分类的ID编号
	public static final int TAB_ID_PROMOTION = 4;// 活动的ID编号
	public static final int TAB_ID_MY = 5;// 我的的ID编号
	// public static final int TAB_ID_SETTING = 6;// 排行的ID编号

	// fragment
	private PromotionFragment fg_promotion;
	private GameClassifyFragment fg_classify;
	private GameCenterFragment fg_game_center;
	private MineFragment fg_my;
	// private SetupFragment fg_setting;
	private FragmentManager FManager;
	private Fragment mCurrentFragment;
	// 水平跑马灯效果
	private HorizontalScrollView horScroll;
	private TextView mainPagerNotice;
	// 导航条效果
	private RadioButton rb_gamecenter;
	private RadioButton rb_classify;
	private RadioButton rb_promotion;
	private RadioButton rb_my;
	
	private ImageView update_gamecenter;
	private ImageView update_classify;
	private ImageView update_promotion;
	private ImageView update_my;
	// private RadioButton rb_setting;
	private TabClickListener mClickListener;
	private TabFocusListener mFocusListener;
	private int action;
	private FragmentTransaction mTransaction;
	private LinkedList<RadioButton> rb_group;
	// 翻页动画效果
	private ArrayList<String> txtTipArray;
	private TextView tvMainTip;
	private Animation bootChoiceAnim;
	private TimerTask task;
	private int i = 0;
	private Timer timer;

	private View lastFcousView;
	private View gameCenterFocusView;
	private View promotionFocusView;
	private View mineView;
	private View setupView;

	private RelativeLayout mhomeContainer;
	private ImageView ivAd;
	private WindowManager wm;
	private static final int VALUE_AD_TIME = 8000;
	private static final int SPASH_OVER = 0;// 节日动画结束标志
	private static final int HOME_LOAD_OVER = 1;// 主页内容加载完毕标志
	private static final int REMOVE_AD_IV = 2; // 移除广告图片
	private static final int SCROLL_TEXT_TIP = 3; // 开始滚动问题提示框
	private static final int SCROLL_HORIZENTOL_TEXT = 4; // 开始横向滚动条
	private List<BaseFragment> fragments = new ArrayList<BaseFragment>();
	private int capability;
	private RewardInfoResp rewardInfo;// 奖品信息
	private long clickXTime = 0;
	// private CommonProgressDialog mDialog;
	private SharedPreferences sp;
	private AppConfig appConfig;

	private FooterView footerView1, footerView2;
	//水平滚动条的数据
	private String noticeText = "";

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SPASH_OVER:
				// 如果节日动画播放完毕，就让游戏中心获取焦点，并显示桌面
				rb_gamecenter.requestFocusFromTouch();
				showHome();
				break;

			case HOME_LOAD_OVER:
				break;

			case SCROLL_HORIZENTOL_TEXT:
				initScollText(noticeText);
				break;

			case REMOVE_AD_IV:
				isLuckDraw();
				// showHome();
				wm.removeView(ivAd);
				break;

			case SCROLL_TEXT_TIP:

				i++;

				bootChoiceAnim.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						if (txtTipArray.size() != 0) {
							tvMainTip.setText(txtTipArray.get(i
									% txtTipArray.size()));
						}
					}
				});

				tvMainTip.startAnimation(bootChoiceAnim);
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
		getNewContentInterface();
		setBlackTitle(true);
		getCurrentAction();
		loadFragments();
		initMainTips();
		
		// 开启后台统计服务
		StatisticsHelper.getInstance(getApplicationContext()).startServices();
		// 开启数据更新服务
		DataUpdateService.start(getApplicationContext());
	}

	@Override
	protected void onResume() {
		super.onResume();
		setTitleData();
		// Umeng用于统计时长
		UmengUtils.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// Umeng用于统计时长
		UmengUtils.onPause(this);
	}

	/**
	 * 防止出错后出现重叠
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub

	}

	// 让主页TAB视图可见
	public void showHome() {
		mhomeContainer.setVisibility(View.VISIBLE);
	}

	private void initUserInfo() {
		if(BaseApplication.userInfo != null){
			return;
		}
		ReqCallback<UserInfo> reqCallback = new ReqCallback<UserInfo>() {
			@Override
			public void onResult(TaskResult<UserInfo> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					UserInfo info = taskResult.getData();
					BaseApplication.userInfo = info;
					alog.info(info);
				} else if (code == TaskResult.NO_DATA) {
					// 没有登录过的用户
					alog.error("用户暂时未登陆");
				} else {
					// 显示失败原因
					alog.error(taskResult.getMsg());
				}
			}
		};
		DataFetcher.userGetLastLoginedUser(this, reqCallback, false).request(
				this);
	}

	private void initView() {

		showAd();
		initUserInfo();
		autoLoginLastUser();
		CheckMarketVersionUtil1.getInstance().checkVersion(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		ScaleViewUtils.init(this);
		ScaleViewUtils.scaleView(getWindow().getDecorView());
		// mDialog = new CommonProgressDialog(getApplicationContext());
		sp = getSharedPreferences(Constant.TIME, MODE_PRIVATE);
		mhomeContainer = (RelativeLayout) findViewById(R.id.home_main_container);
		rb_group = new LinkedList<RadioButton>();
		timer = new Timer();
		txtTipArray = new ArrayList<String>();
		
		update_gamecenter = (ImageView) findViewById(R.id.tab_gamecenter_update);
		update_classify = (ImageView) findViewById(R.id.tab_classily_update);
		update_promotion = (ImageView) findViewById(R.id.tab_promotion_update);
		update_my = (ImageView) findViewById(R.id.tab_my_update);

		rb_gamecenter = (RadioButton) findViewById(R.id.tab_gamecenter);

		rb_gamecenter.setTag(TAB_ID_GAMECENTER);
		rb_gamecenter.setNextFocusUpId(R.id.tab_gamecenter);
		rb_gamecenter.setNextFocusLeftId(R.id.tab_my);
		rb_gamecenter.setNextFocusDownId(R.id.game_center_quick_start);
		rb_group.add(rb_gamecenter);
		// UIUtils.setTextType(rb_gamecenter);

		rb_classify = (RadioButton) findViewById(R.id.tab_categroy);
		rb_classify.setTag(TAB_ID_CLASSIFY);

		rb_classify.setNextFocusUpId(R.id.tab_categroy);
		rb_group.add(rb_classify);

		rb_promotion = (RadioButton) findViewById(R.id.tab_promotion);
		rb_promotion.setTag(TAB_ID_PROMOTION);
		rb_promotion.setNextFocusUpId(R.id.tab_promotion);
		rb_promotion.setNextFocusDownId(R.id.promotion_area);
		rb_group.add(rb_promotion);

		// rb_setting = (RadioButton) findViewById(R.id.tab_setting);
		// rb_setting.setTag(TAB_ID_SETTING);
		// rb_setting.setNextFocusUpId(R.id.tab_setting);
		// rb_setting.setNextFocusRightId(R.id.tab_gamecenter);
		// rb_setting.setNextFocusDownId(R.id.setup_childlock_layout_bg);
		// rb_group.add(rb_setting);

		rb_my = (RadioButton) findViewById(R.id.tab_my);
		rb_my.setTag(TAB_ID_MY);
		rb_my.setNextFocusUpId(R.id.tab_my);
		rb_my.setNextFocusRightId(R.id.tab_gamecenter);
		rb_my.setNextFocusDownId(R.id.mine_user_layout_bg);
		rb_group.add(rb_my);

		mClickListener = new TabClickListener();
		mFocusListener = new TabFocusListener();

		rb_gamecenter.setOnClickListener(mClickListener);
		rb_classify.setOnClickListener(mClickListener);
		rb_promotion.setOnClickListener(mClickListener);
		// rb_setting.setOnClickListener(mClickListener);
		rb_my.setOnClickListener(mClickListener);

		rb_gamecenter.setOnFocusChangeListener(mFocusListener);
		rb_classify.setOnFocusChangeListener(mFocusListener);
		rb_promotion.setOnFocusChangeListener(mFocusListener);
		// rb_setting.setOnFocusChangeListener(mFocusListener);
		rb_my.setOnFocusChangeListener(mFocusListener);

		footerView1 = (FooterView) findViewById(R.id.footer110);
		footerView2 = (FooterView) findViewById(R.id.footer2);
		mCurrentFragment = fg_game_center;
		
		loadScrollData();
		loadMainTipData();
		// initScollText("123135465465433333333333333333333333333333333333333333");
	}
	
	/**
	 * 检测是否有新内容
	 */
	public void getNewContentInterface(){
		List<String> interfaceList = DataHelper.getNewContentInterface();
		if(interfaceList==null){
			ReqCallback<List<String>> reqCallback = new ReqCallback<List<String>>() {
				@Override
				public void onResult(TaskResult<List<String>> taskResult) {
					int code = taskResult.getCode();
					alog.info("taskResult code:" + code);
					if (code == TaskResult.OK) {
						checkNewContentInterface(taskResult.getData());
					}
				}
			};
			DataFetcher.getNewContentInterface(getApplicationContext(), reqCallback, true);
		} else {
			checkNewContentInterface(interfaceList);
		}
	}
	/**
	 * 
	 * @description: 检测是否有新内容
	 * 
	 * @throws: 
	 * @author: LiJie
	 * @date: 2015年10月9日 下午3:18:24
	 */
	private void checkNewContentInterface(List<String> interfaceList) {
		if(interfaceList.contains(UpdateInterface.GAME_TOPIC)
				|| interfaceList.contains(UpdateInterface.GAME_TYPE)){
			//分类标题下有更新
			if(rb_classify.isShown() && !rb_classify.isChecked() ){
				update_classify.setVisibility(View.VISIBLE);
			}
		}
		
		if(interfaceList.contains(UpdateInterface.GAME_NEW_UPLOADED)
				|| DataFetcher.existNewDownloadGame()){
			//游戏中心标题下有更新
			if(!rb_gamecenter.isChecked()){
				update_gamecenter.setVisibility(View.VISIBLE);
			}
			
		}
		
		if(interfaceList.contains(UpdateInterface.GAME_ACTIVITY)
				|| interfaceList.contains(UpdateInterface.GAME_GOODS)
				|| interfaceList.contains(UpdateInterface.GAME_GIFT)
				|| interfaceList.contains(UpdateInterface.USER_TASK)){
			//活动有更新
			if(rb_promotion.isShown() && !rb_promotion.isChecked()){
				update_promotion.setVisibility(View.VISIBLE);
			}
		}
	}

	private void getCurrentAction() {

		action = getIntent().getIntExtra(Constant.ACTION_TO_JUMP,
				Constant.ACTION_TO_MAIN);
		//NewToast.makeToast(getApplicationContext(), action, 0).show();
	}

	private void loadFragments() {
		FManager = getSupportFragmentManager();
		// mTransaction = FManager.beginTransaction();

		//GetDeviceCapability();
		getAppConfig();
		getCurrentFragment();

	}

	/**
	 * 
	 * @description: 获取是高设备还是低设备
	 * 
	 * @throws:
	 * @author: LiJie
	 * @date: 2015年8月21日 下午8:47:04
	 */
	/*public void GetDeviceCapability() {
		capability = DataFetcher.getDeviceCapability(this);

		alog.info("capability-------------" + capability);
	}
	*/
	public void getAppConfig(){
		appConfig = DataFetcher.getAppConfig();
		/*if(appConfig.isGameCenterVisible()){
		}
		if(appConfig.isGameTypeVisible()){
			//游戏分类可见
		}
		if(appConfig.isActivityVisible()){
			//活动可见
		}
		if(appConfig.isMineVisible()){
			//我的可见
		}*/
	}


	/**
	 * 
	 * @description: 进入游戏大厅时跳转到哪个Fragment
	 * 
	 * @throws:
	 * @author: LiJie
	 * @date: 2015年8月10日 下午2:23:18
	 */
	private void getCurrentFragment() {

		switch (action) {
		case Constant.ACTION_TO_MAIN:
			rb_gamecenter.requestFocusFromTouch();
			
			break;
		case Constant.ACTION_TO_GAME_CLASSIFY:

			rb_classify.requestFocusFromTouch();
			break;
		case Constant.ACTION_TO_ACTIVITIES:
			rb_promotion.requestFocusFromTouch();

			break;
		case Constant.ACTION_TO_MYGAME:
			rb_my.requestFocusFromTouch();
			break;
		// case Constant.ACTION_TO_SETTING:
		// rb_setting.requestFocusFromTouch();
		//
		// break;

		default:
			break;
		}

	}

	public void snapToTabPage(int tabid) {
		// Fragment tmpFragment = null;
		// CUR_TAB = tabid;
		mTransaction = FManager.beginTransaction();
		// 根据tabid的值，选择实例化fragment的类型
		for (BaseFragment fragment : fragments) {
			mTransaction.hide(fragment);
		}
		footerView1.setVisibility(View.INVISIBLE);
		footerView2.setVisibility(View.VISIBLE);
		switch (tabid) {
		// case TAB_ID_SEARCH:
		// tmpFragment = mtfsearch;
		// break;

		case TAB_ID_GAMECENTER:
			if (fg_game_center == null) {
				fg_game_center = new GameCenterFragment();
				fragments.add(fg_game_center);
				mTransaction.add(R.id.home_fragments_container, fg_game_center)
						.commitAllowingStateLoss();
			} else {
				mTransaction.show(fg_game_center).commitAllowingStateLoss();
			}
			// tmpFragment = FragmentFactory.getFragment(TAB_ID_GAMECENTER);
			// Umeng统计当前页面访问次数
			UmengUtils.setOnEvent(this, UmengUtils.PAGE_GAMECENTER_FRAGMENT);
			break;

		case TAB_ID_CLASSIFY:
			if (fg_classify == null) {
				fg_classify = new GameClassifyFragment().newInstance();
				fragments.add(fg_classify);
				mTransaction.add(R.id.home_fragments_container, fg_classify)
						.commitAllowingStateLoss();
			} else {
				mTransaction.show(fg_classify).commitAllowingStateLoss();
			}
			// tmpFragment = FragmentFactory.getFragment(TAB_ID_CLASSIFY);
			// Umeng统计当前页面访问次数
			UmengUtils.setOnEvent(this, UmengUtils.PAGE_GAMECLASSIFY_FRAGMENT);
			break;

		case TAB_ID_PROMOTION:
			if (fg_promotion == null) {
				fg_promotion = new PromotionFragment();
				fragments.add(fg_promotion);
				mTransaction.add(R.id.home_fragments_container, fg_promotion)
						.commitAllowingStateLoss();
			} else {
				mTransaction.show(fg_promotion).commitAllowingStateLoss();
			}
			// tmpFragment = FragmentFactory.getFragment(TAB_ID_PROMOTION);
			// Umeng统计当前页面访问次数
			UmengUtils.setOnEvent(this, UmengUtils.PAGE_PROMOTION_FRAGMENT);
			break;

		case TAB_ID_MY:
			// tmpFragment = FragmentFactory.getFragment(TAB_ID_MY);
			footerView1.setVisibility(View.VISIBLE);
			footerView2.setVisibility(View.INVISIBLE);
			if (fg_my == null) {
				fg_my = new MineFragment().newInstance();
				fragments.add(fg_my);
				mTransaction.add(R.id.home_fragments_container, fg_my)
						.commitAllowingStateLoss();
			} else {
				mTransaction.show(fg_my).commitAllowingStateLoss();
			}
			// tmpFragment = FragmentFactory.getFragment(TAB_ID_PROMOTION);
			// Umeng统计当前页面信息
			UmengUtils.setOnEvent(this, UmengUtils.PAGE_MINE_FRAGMENT);
			break;

		default:
			break;
		}

	}

	protected void setTabCheck(int id) {
		// TODO Auto-generated method stub

		// 根据id的值选中相应的按钮
		switch (id) {
		// case TAB_ID_SEARCH:
		// tvSearch.setChecked(true);
		// break;

		case TAB_ID_GAMECENTER:
			rb_gamecenter.setChecked(true);
			update_gamecenter.setVisibility(View.INVISIBLE);
			mCurrentFragment = fg_game_center;
			break;

		case TAB_ID_CLASSIFY:
			rb_classify.setChecked(true);
			update_classify.setVisibility(View.INVISIBLE);
			mCurrentFragment = fg_classify;
			break;

		case TAB_ID_PROMOTION:
			rb_promotion.setChecked(true);
			update_promotion.setVisibility(View.INVISIBLE);
			mCurrentFragment = fg_promotion;
			break;

		// case TAB_ID_SETTING:
		// rb_setting.setChecked(true);
		// mCurrentFragment = fg_setting;
		// break;

		case TAB_ID_MY:
			rb_my.setChecked(true);
			mCurrentFragment = fg_my;
			break;
		default:
			break;
		}
		setHomeTabFocus();
	}

	/**
	 * @author zhaominglai
	 * @description 查找被选中的TAB按钮
	 * @param 如果有被选中的按钮则返回该按钮
	 *            ，否则返回null
	 * @date 2014-4-23
	 * */
	private View findCheckedTab() {

		if (rb_gamecenter.isChecked())
			return rb_gamecenter;

		if (rb_classify.isChecked())
			return rb_classify;

		if (rb_promotion.isChecked())
			return rb_promotion;

		if (rb_my.isChecked())
			return rb_my;

		// if (rb_setting.isChecked())
		// return rb_setting;

		return null;

	}

	/**
	 * @author zhaominglai
	 * @description 滑动到当前TAB的前一个TAB内容
	 * @param v表示当前的TAB按钮
	 * @date 2014-3-25
	 * */
	private void snapToPreviesScreen(View v) {
		if (v == rb_gamecenter)
			rb_my.requestFocusFromTouch();
		if (v == rb_classify)
			rb_gamecenter.requestFocusFromTouch();
		if (v == rb_promotion)
			if(rb_classify.isShown()){
				rb_classify.requestFocusFromTouch();
			}else{
				rb_gamecenter.requestFocusFromTouch();
			}
		if (v == rb_my)
			if(rb_promotion.isShown()){
				rb_promotion.requestFocusFromTouch();
			}else if(rb_classify.isShown()){
				rb_classify.requestFocusFromTouch();
			}else{
				rb_gamecenter.requestFocusFromTouch();
			}

		// if (v == rb_setting)
		// rb_my.requestFocusFromTouch();
	}

	/**
	 * 
	 * @description: 滑动到当前TAB的后一个TAB内容
	 * 
	 * @param v
	 * @throws:
	 * @author: LiJie
	 * @date: 2015年8月3日 下午5:25:34
	 */
	private void snapToNextScreen(View v) {

		if (v == rb_gamecenter)
			if(rb_classify.isShown()){
				rb_classify.requestFocusFromTouch();
			}else if(rb_promotion.isShown()){
				rb_promotion.requestFocusFromTouch();
			}else{
				rb_my.requestFocusFromTouch();
			}

		if (v == rb_classify)
			rb_promotion.requestFocusFromTouch();

		if (v == rb_promotion)
			rb_my.requestFocusFromTouch();

		if (v == rb_my)
			rb_gamecenter.requestFocusFromTouch();

	}

	/**
	 * 
	 * @description: 设置导航条获取焦点的样式
	 * 
	 * @throws:
	 * @author: LiJie
	 * @date: 2015年7月6日 下午3:57:24
	 */
	public void setHomeTabFocus() {
		// alog.info("size ====== " + ScaleViewUtils.resetTextSize(50));
		// 修改系统的setTextSize根据像素改变的问题
		setTextSizeScaleDensity();

		for (int i = 0; i < rb_group.size(); i++) {

			if (rb_group.get(i).isChecked()) {

				/*
				 * if (rb_group.get(i).getId() == rb_gamecenter.getId()) {
				 * rb_group.get(i).setBackgroundResource(
				 * R.drawable.home_tab_big_bg); } else {
				 * rb_group.get(i).setBackgroundResource(
				 * R.drawable.home_tab_small_bg); }
				 */

				// float value = dm.scaledDensity;
				// alog.info("value =========" + value);
				// 用setTextSize(18/value)
				rb_group.get(i).setTextSize(
						ScaleViewUtils.resetTextSize(getResources()
								.getDimension(R.dimen.home_tab_focus_size)));
				rb_group.get(i).setTextColor(
						UIUtils.getColor(R.color.home_tab_focus_color));
			} else {
				rb_group.get(i).setTextColor(
						UIUtils.getColor(R.color.home_tab_normal_color));
				rb_group.get(i).setTextSize(
						ScaleViewUtils.resetTextSize(getResources()
								.getDimension(R.dimen.home_tab_normal_size)));

				/*
				 * rb_group.get(i).setBackgroundResource(
				 * android.R.color.transparent);
				 */
			}
		}
		alog.info("textSize------" + rb_classify.getTextSize());
	}

	/**
	 * 
	 * @description: 设置TextSize不根据设备缩放
	 * 
	 * @throws:
	 * @author: LiJie
	 * @date: 2015年8月21日 下午2:59:19
	 */
	private void setTextSizeScaleDensity() {
		DisplayMetrics dm = getResources().getDisplayMetrics();
		try {
			Field declaredField = dm.getClass().getDeclaredField(
					"scaledDensity");
			declaredField.set(dm, 1.0f);
			declaredField.setAccessible(false);

			alog.info("scaledDensity =========" + dm.scaledDensity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @description: 其他Fragment点击向上按钮是对应的标题获取焦点
	 * 
	 * @param tabid
	 * @param direction
	 * @throws:
	 * @author: LiJie
	 * @date: 2015年8月10日 下午2:55:40
	 */
	public void forceFocusTab(int tabid, int direction) {
		int mTabid = tabid + direction;
		switch (mTabid) {

		case TAB_ID_GAMECENTER:
			rb_gamecenter.requestFocusFromTouch();
			break;
		case TAB_ID_PROMOTION:
			rb_promotion.requestFocusFromTouch();
			break;
		case TAB_ID_CLASSIFY:
			rb_classify.requestFocusFromTouch();
			break;
		case TAB_ID_MY:
			rb_my.requestFocusFromTouch();
			break;
		// case TAB_ID_SETTING:
		// rb_setting.requestFocusFromTouch();
		// break;
			
		/*
		 * case 3: tvMyGame.requestFocusFromTouch(); break; // case 4: //
		 * tvMyApp.requestFocusFromTouch(); // break; case 5:
		 * tvSetting.requestFocusFromTouch(); break; case 6:
		 * tvNews.requestFocusFromTouch(); break;
		 */
		default:
			break;
		}
	}

	/**
	 * 
	 * @description: 低端设备只显示游戏中心
	 * 
	 * @throws:
	 * @author: LiJie
	 * @date: 2015年8月21日 下午2:29:20
	 */
	private void setDevicesCapability() {
		setTextSizeScaleDensity();
		boolean gameTypeVisible = appConfig.isGameTypeVisible();
		boolean activityVisible = appConfig.isActivityVisible();
		boolean mineVisible = appConfig.isMineVisible();
		
		rb_gamecenter.setTextSize(ScaleViewUtils.resetTextSize(getResources()
				.getDimension(R.dimen.home_tab_focus_size)));
		rb_gamecenter.setTextColor(UIUtils
				.getColor(R.color.home_tab_focus_color));
		
		if(!gameTypeVisible && !activityVisible && !mineVisible){
			rb_gamecenter.setNextFocusRightId(rb_gamecenter.getId());
		}
		//appConfig.isGameTypeVisible()
		if(!gameTypeVisible){
			//分类不可见
			rb_classify.setVisibility(View.GONE);
			update_classify.setVisibility(View.GONE);
			rb_gamecenter.setNextFocusRightId(rb_promotion.getId());
			rb_promotion.setNextFocusLeftId(rb_gamecenter.getId());
		}
		
		if(!activityVisible){
			//活动不可见
			rb_promotion.setVisibility(View.GONE);
			update_promotion.setVisibility(View.GONE);
			rb_gamecenter.setNextFocusRightId(rb_my.getId());
		}
		if(!mineVisible){
			//我的不可见
			rb_my.setVisibility(View.GONE);
			rb_promotion.setNextFocusRightId(rb_gamecenter.getId());
			rb_gamecenter.setNextFocusLeftId(rb_promotion.getId());
		}
	}

	/**
	 * 
	 * @description: 初始化滚动条动画
	 * 
	 * @param noticeText
	 * @throws:
	 * @author: LiJie
	 * @date: 2015年7月2日 下午8:07:49
	 */
	private void initScollText(String scrollMsg) {
		horScroll = ((HorizontalScrollView) findViewById(R.id.tv_scroll));
		horScroll.setFocusable(false);
		horScroll.setFocusableInTouchMode(false);
		mainPagerNotice = ((TextView) findViewById(R.id.tvMainPageNotice));
		mainPagerNotice.setText(scrollMsg);
		mainPagerNotice.setTextSize(ScaleViewUtils.resetTextSize(40));
		mainPagerNotice.setTextColor(UIUtils.getColor(R.color.home_title_gray));
		mainPagerNotice.setFocusable(false);
		mainPagerNotice.setFocusableInTouchMode(false);
		mainPagerNotice.setSingleLine();
		mainPagerNotice.setEllipsize(TruncateAt.MARQUEE);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mainPagerNotice.setVisibility(View.VISIBLE);

				int screenWid = getResources().getDisplayMetrics().widthPixels;
				int textWid = mainPagerNotice.getWidth();
				TranslateAnimation transAnim = new TranslateAnimation(
						screenWid, 0 - textWid, 0.0F, 0.0F);
				transAnim.setFillEnabled(true);
				transAnim.setFillAfter(true);
				Long duration = (long) (25000 * ((textWid + screenWid) / screenWid));
				transAnim.setDuration(duration);
				transAnim.setRepeatCount(-1);
				transAnim.setInterpolator(new LinearInterpolator());
				mainPagerNotice.startAnimation(transAnim);
			}
		}, 500L);
		horScroll.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				// 按下
				case MotionEvent.ACTION_DOWN:
					return v.onTouchEvent(event);
					// 滑动
				case MotionEvent.ACTION_MOVE:
					break;
				// 离开
				case MotionEvent.ACTION_UP:
					return v.onTouchEvent(event);
				}
				return true;
			}
		});
	}

	/**
	 * 
	 * @description: 加载水平滚动条数据
	 * 
	 * @throws:
	 * @author: LiJie
	 * @date: 2015年7月30日 下午12:04:53
	 */
	private void loadScrollData() {

		ReqCallback<List<NoticeInfo>> reqCallback = new ReqCallback<List<NoticeInfo>>() {
			@Override
			public void onResult(TaskResult<List<NoticeInfo>> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					List<NoticeInfo> infos = taskResult.getData();
					alog.info(infos.toString());
					
					if (infos != null && infos.size() != 0) {
						for (int i = 0; i < infos.size(); i++) {
							NoticeInfo noticeInfo = infos.get(i);
							noticeText = noticeText + noticeInfo.getContent();
						}
						Message msg = Message.obtain();
						msg.what = SCROLL_HORIZENTOL_TEXT;
						mHandler.sendMessage(msg);
					}
				}

			}

			@Override
			public void onUpdate(TaskResult<List<NoticeInfo>> taskResult) {

			}
		};
		DataFetcher
				.getNoticeInfo(getApplicationContext(),
						DataConfig.NOTICE_TYPE_EMERGENCY, reqCallback, false)
				.registerUpdateListener(reqCallback)
				.request(getApplicationContext());
	}

	/**
	 * 
	 * @description: 获取抽奖信息
	 * 
	 * @throws:
	 * @author: LiJie
	 * @date: 2015年9月17日 下午5:35:50
	 */
	public void getRewardInfo() {
		int userId = DataFetcher.getUserIdInt();
		ReqCallback<RewardInfoResp> reqCallback = new ReqCallback<RewardInfoResp>() {
			@Override
			public void onResult(TaskResult<RewardInfoResp> taskResult) {
				final int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					rewardInfo = taskResult.getData();
					alog.info(rewardInfo.toString());
					Intent intent = new Intent(MainActivity.this,LuckyDrawActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable(Constant.REWARDINFORESP, rewardInfo);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		};
		DataFetcher
				.getRewardInfo(MainActivity.this, reqCallback, userId, false)
				.request(MainActivity.this);
	}

	/**
	 * 
	 * @description: 加载竖直滚动条数据
	 * 
	 * @throws:
	 * @author: LiJie
	 * @date: 2015年7月30日 下午12:04:45
	 */
	private void loadMainTipData() {
		ReqCallback<List<NoticeInfo>> reqCallback = new ReqCallback<List<NoticeInfo>>() {
			@Override
			public void onResult(TaskResult<List<NoticeInfo>> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					List<NoticeInfo> infos = taskResult.getData();
					alog.info(infos.toString());
					if (infos != null && infos.size() != 0) {
						txtTipArray.clear();
						for (NoticeInfo noticeInfo : infos) {
							txtTipArray.add(noticeInfo.getContent());
						}
						Message msg = Message.obtain();
						msg.what = SCROLL_TEXT_TIP;
						mHandler.sendMessage(msg);
					}
				}

			}

			@Override
			public void onUpdate(TaskResult<List<NoticeInfo>> taskResult) {

			}
		};
		DataFetcher
				.getNoticeInfo(getApplicationContext(),
						DataConfig.NOTICE_TYPE_TIPS, reqCallback, false)
				.registerUpdateListener(reqCallback)
				.request(getApplicationContext());

	}

	/**
	 * 
	 * @description: 竖直方向的滚动条
	 * 
	 * @throws:
	 * @author: LiJie
	 * @date: 2015年7月2日 下午8:07:35
	 */
	private void initMainTips() {
		tvMainTip = (TextView) findViewById(R.id.tvMainPageTip);
		bootChoiceAnim = AnimationUtils.loadAnimation(MainActivity.this,
				R.anim.mainpage_tip_anim);
		LinearInterpolator lin = new LinearInterpolator();
		bootChoiceAnim.setInterpolator(lin);
		task = new TimerTask() {
			@Override
			public void run() {
				mHandler.sendEmptyMessage(SCROLL_TEXT_TIP);
			}
		};

		timer.schedule(task, 1000, 8000);
		Intent intent = new Intent(this, UpdateGameService.class);
		startService(intent);
	}

	class TabClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			v.requestFocusFromTouch();// 让被点击的按钮聚焦
			// int id = (Integer) v.getTag();
			// snapToTabPage(id);// 显示TAB按钮对应的内容
		}

	}

	class TabFocusListener implements OnFocusChangeListener {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			if (hasFocus) {
				((RadioButton) v).setTextColor(UIUtils
						.getColor(R.color.home_tab_focus_color));
				v.focusSearch(View.FOCUS_DOWN);
				int id = (Integer) v.getTag();
				
				setDevicesCapability();
				snapToTabPage(id);
				setTabCheck(id);
				// CUR_TAB = id;
				lastFcousView = v;

			} else {
				((RadioButton) v).setTextColor(UIUtils.getColor(R.color.home_tab_normal_color));
				// v.setBackgroundResource(android.R.color.transparent);
				// v.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			}
		}
	}

	@Override
	protected void onDestroy(){

		// 彻底销毁Launcher所在的进程,防止程序退出时报Activity has been destroy异常
		timer.cancel();
		// clearTabsFocusable();
		mCurrentFragment = null;
		// Umeng统计
		UmengUtils.onKillProcess(this);
		android.os.Process.killProcess(android.os.Process.myPid());
		super.onDestroy();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();

		if (action == KeyEvent.ACTION_DOWN) {

			// 判断 L2 R2键切换标题
			if (GamepadTool.isButtonL2(keyCode)) {

				snapToPreviesScreen(findCheckedTab());
				return true;
			}
			if (GamepadTool.isButtonR2(keyCode)) {
				snapToNextScreen(findCheckedTab());
				return true;
			}
			// 按X键刷新数据
			if (GamepadTool.isButtonX(keyCode)
					|| keyCode == KeyEvent.KEYCODE_MENU) {
				if (System.currentTimeMillis() - clickXTime >= 1000) {
					clickXTime = System.currentTimeMillis();
					if (mCurrentFragment == fg_game_center) {
						fg_game_center.loadData(true);
						// loadMainTipData();
						// loadScrollData();
						rb_gamecenter.requestFocus();
					} else if (mCurrentFragment == fg_promotion) {
						fg_promotion.loadActData(true);
						fg_promotion.loadRecommendData(true);
						rb_promotion.requestFocus();
					} else if (mCurrentFragment == fg_classify) {
						fg_classify.getRankingData(0, true);
						rb_classify.requestFocus();
					}
				}
				return true;
			}

			/*
			 * //监听B键 if(onKeyDown(keyCode, event)){
			 * NewToast.makeToast(getApplicationContext(), "555555555", 0).show();
			 * showCloseDialog(); return true; }
			 */

			View currentFocus = getCurrentFocus();
			if (currentFocus != null) {

				if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
					if (currentFocus.getId() == rb_gamecenter.getId()) {

						if (gameCenterFocusView != null) {
							gameCenterFocusView.requestFocus();
							gameCenterFocusView = null;
							return true;
						} else {
							// 第一次进入大厅，滑动到第二页的时候焦点的处理
							View firstChild = null;
							RecyclerView root_RecyclerView = fg_game_center.getRecyclerVew();
							if (root_RecyclerView != null) {
								firstChild = root_RecyclerView.getChildAt(0);
							}
							if (firstChild instanceof GameCenterSecondPanel) {
								((GameCenterSecondPanel) firstChild).getMain_push().requestFocus();
								return true;
							} else if (firstChild instanceof GameCenterThirdPanel) {
								((GameCenterThirdPanel) firstChild)
										.getMain_game().requestFocus();
								return true;
							} else if (firstChild instanceof GameCenterfourthPanel) {
								((GameCenterfourthPanel) firstChild)
										.getMore_game().requestFocus();
								return true;
							} else if (firstChild instanceof GameCenterfifthPanel) {
								((GameCenterfifthPanel) firstChild)
										.getItem5_top().requestFocus();
								return true;
							}
						}
					}
					if (lastFcousView != null
							&& lastFcousView.getId() == rb_classify.getId()) {
						if (fg_classify != null) {
							fg_classify.setItemFocus();
						}
						return true;
					}

					if (currentFocus.getId() == rb_promotion.getId()) {
						if (promotionFocusView != null) {
							promotionFocusView.requestFocus();
							promotionFocusView = null;
							return true;
						} else {
							if(fg_promotion != null &&  fg_promotion.getRecyclerView() != null){
								View firstView = fg_promotion.getRecyclerView().getChildAt(0);
								if (firstView.getId() == R.id.promotion_item2) {
									firstView.findViewById(R.id.promotion_carousel_game).requestFocus();
									return true;
								} else if (firstView.getId() == R.id.promotion_item3) {
									firstView.findViewById(R.id.promotion_more_game).requestFocus();
									return true;
								} else if (firstView instanceof PromotionFourthPanel) {
									((PromotionFourthPanel) firstView).getItem4_top().requestFocus();
									return true;
								}
							}else {
								return true;
							}
						}
					}

					if (mineView != null&& currentFocus.getId() == rb_my.getId()) {
						mineView.requestFocus();
						mineView = null;
						return true;
					}

					/*
					 * if (setupView != null && currentFocus.getId() ==
					 * rb_setting.getId()) { setupView.requestFocus(); setupView
					 * = null; return true; }
					 */
				}
			}
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	public void onBackPressed() {
		Set<String> downloadingFileIds = DownloadTask.getInstance(
				getApplicationContext()).getDownloadingFileIds();
		if (downloadingFileIds != null) {
			if (downloadingFileIds.size() != 0) {
				//showDialog(UIUtils.getString(R.string.out_to_home));
				showDialog("有任务正在下载中，确定停止退出吗？");
				return;
			}
		}
		showOutDialog(UIUtils.getString(R.string.out_msg));
	}

	private void showDialog(String warnMsg) {
		CommonProgressDialog mDialog = new CommonProgressDialog.Builder(this)
				.setMessage(warnMsg)
				.setPositiveButton(R.string.ok_login,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								MainActivity.this.finish();
							}

						}).setCenterButton(R.string.btn_home, new DialogInterface.OnClickListener(){
							
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent();
								intent.setAction(Intent.ACTION_MAIN);
								intent.addCategory(Intent.CATEGORY_HOME);           
								startActivity(intent);
							}
							
						}).setNegativeButton(R.string.cancle_login, null).create();
		mDialog.setParams(mDialog);
		mDialog.show();
	};
	
	private void showOutDialog(String warnMsg) {
		CommonProgressDialog mDialog = new CommonProgressDialog.Builder(this)
				.setMessage(warnMsg)
				.setPositiveButton(R.string.ok_login,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								MainActivity.this.finish();
							}

						}).setNegativeButton(R.string.cancle_login, null).create();
		mDialog.setParams(mDialog);
		mDialog.show();
	};

	public void setLastFcousView(View lastFcousView) {
		this.lastFcousView = lastFcousView;
	}

	public void setGameCenterFcous(View gameCenterFocusView) {
		this.gameCenterFocusView = gameCenterFocusView;
	}

	public void setPromotionFcous(View promotionFocusView) {
		this.promotionFocusView = promotionFocusView;
	}

	public void setMineView(View mineView) {
		this.mineView = mineView;
	}

	public void setSetupView(View setupView) {
		this.setupView = setupView;
	}

	private void showAd() {
		ivAd = new ImageView(this);
		ivAd.setScaleType(ScaleType.FIT_XY);
		// ivAd.setImageBitmap(ScreenShot.screenShotBmp);
		/* mAnimationDrawable = (AnimationDrawable) ivAd.getDrawable(); */
		Bitmap imgBitmap = getCurrentTimeImg();
		if (imgBitmap == null) {
			ivAd.setBackgroundResource(R.drawable.start_logo);
		} else {
			ivAd.setImageBitmap(imgBitmap);
		}
		wm = getWindowManager();
		Display display = wm.getDefaultDisplay();
		WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
		wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		wmParams.format = 1;
		wmParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		wmParams.width = display.getWidth();
		wmParams.height = display.getHeight();
		try {
			wm.addView(ivAd, wmParams);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		// 检查手柄输入法版本，如果已安装输入法版本低于大厅附带版本，自动进行安装
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				VersionUpdate versionUpdate = new VersionUpdate();
				versionUpdate.checkVersionUpdate(MainActivity.this, mHandler);
			}
		}, VALUE_AD_TIME / 2);

		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				mHandler.sendEmptyMessage(REMOVE_AD_IV);
			}
		}, VALUE_AD_TIME);
	}

	/**
	 * @return 获取广告图片bitmap
	 */
	private Bitmap getCurrentTimeImg() {
		/*
		 * InitAdInfo currentTimeAd = getCurrentTimeAds();
		 * 
		 * if (currentTimeAd == null) { return null; } try { String imgPath =
		 * currentTimeAd.getLocalDir() + currentTimeAd.getLocalFileName(); File
		 * imgFile = new File(imgPath); if (!imgFile.exists()) { return null;
		 * 
		 * } Bitmap currentBitmp = BitmapFactory.decodeFile(imgPath); return
		 * currentBitmp; } catch (Exception e) { // TODO Auto-generated catch
		 * block return null; }
		 */
		return DataHelper.getCurrentTimeLaunchAd(getApplicationContext());
	}
	
	/**
	 * 
	 * @description: 判断一天抽奖一次
	 * 
	 * @throws: 
	 * @author: LiJie
	 * @date: 2015年9月21日 下午8:23:29
	 */
	private void isLuckDraw(){
		boolean isLogin = true;
		BufferedReader reader = null;
		String path = FileUtils.getDir("lucky") + "lucky.txt";
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			String line = "";
			while((line = reader.readLine()) != null){
				String[] userInfo = line.split(":");
				if(String.valueOf(DataFetcher.getUserIdInt()).equals(userInfo[0])){
					if(Long.valueOf(userInfo[1]) + 24*60*60 *1000 < System.currentTimeMillis()){
						getRewardInfo();
					}else{
						//如果账号存在，而且没有超过一天，不弹出抽奖
					}
					isLogin = true;
					break;
				}else{
					//如果账号不存在，弹出抽奖
					isLogin = false;
				}
			}
			if(!isLogin ){
				getRewardInfo();
			}
		}catch(FileNotFoundException e) {
			//如果文件没有找到，跳转抽奖
			getRewardInfo();
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			IOUtils.close(reader);
		}
			
	}
	
	/**
	 * @description: 自动登录最后一个登录过的用户
	 *
	 * @param userId 
	 * @author: LiuQin
	 * @date: 2015年10月9日 下午9:30:42
	 */
	private void autoLoginLastUser() {
		ReqCallback<UserInfo> reqCallback = new ReqCallback<UserInfo>() {
			@Override
			public void onResult(TaskResult<UserInfo> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					// 登录成功
					UserInfo info = taskResult.getData();
//					BaseApplication.userInfo = info;
				} else {
					// 显示失败原因
					alog.error(taskResult.getMsg());
				}
			}
		};
		DataFetcher.userAutoLogin(getApplicationContext(), reqCallback, false).request(getApplicationContext());
	}
}
