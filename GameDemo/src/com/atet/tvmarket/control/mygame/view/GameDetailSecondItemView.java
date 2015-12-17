package com.atet.tvmarket.control.mygame.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.atet.tvmarket.R;
import com.atet.tvmarket.app.UrlConstant;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.control.mygame.activity.RelativeGameActivity;
import com.atet.tvmarket.control.mygame.utils.QRUtil;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.ScreenShot;
import com.atet.tvmarket.utils.StringTool;
import com.atet.tvmarket.utils.UmengUtils;
import com.atet.tvmarket.view.NewToast;
import com.google.zxing.WriterException;

/**
 * 游戏详情第二个Item
 * 
 * @author chenqingwen
 * 
 */
public class GameDetailSecondItemView extends LinearLayout implements
		OnFocusChangeListener {

	private FrameLayout frameLayout;
	private View firstView; // 第一个View
	private View secondView;// 第二个View
	private View threeView;// 第三个View
	private RadioGroup group;
	private RadioButton rbtOne;
	private RadioButton rbtTwo;
	private RadioButton rbtThree;
	private LayoutParams layoutParams;
	private View threeView0, threeView1, threeView2, threeView3, threeView4,
			threeView5; // 更多精彩五个Item （最多5个）
	private ArrayList<View> moreGameView = new ArrayList<View>();
	private List<GameInfo> moreGames = new ArrayList<GameInfo>();
	private Button commitBtn; // 评分提交按钮
	private TextView gameDetailName; // 游戏名
	private RatingBar gameScore; // 游戏评分
	private TextView gameDownCount; // 游戏下载次数
	private TextView gameSize; // 游戏大小
	private TextView gameVersion;// 游戏版本
	private TextView gameDes;// 游戏描述
	private TextView gameControl;// 游戏操控方式
	private ImageView iv_qrcode; // 游戏二维码imageview
	private ScoreRatingBar scoreRatingBar; // 评分功能中的评分条
	private TextView ratingTv;
	private int twoCount; // 缩放次数 避免多次缩放
	private int threeCount; // 缩放次数
	private Context context;
	private String gameId;
	private ImageFetcher mImageFetcher;
	private static final int NOTICE = 16,// 预告游戏
			IMITATE = 1, // 模拟模式
			GAMEPAD = 2, // gamepad模式
			KEYBOARD = 4, // 键盘
			CONTROL = 8, // 遥控
			IMITATE_GAMEPAD = 3,// 模拟模式+gamepad模式
			IMITATE_KEYBOARD = 5,// 模拟模式+键盘
			IMITATE_CONTROL = 9,// 模拟模式+遥控；
			GAMEPAD_KEYBOARD = 6,// gamepad模式+键盘
			GAMEPAD_CONTROL = 10,// gamepad+遥控
			KEYBOARD_CONTROL = 12,// 键盘+遥控
			IMITATE_GAMEPAD_KEYBOARD = 7,// 模拟模式+gamepad+键盘
			IMITATE_GAMEPAD_CONTROL = 11,// 模拟模式+gamepad+遥控
			GAMEPAD_KEYBOARD_CONTROL = 14,// gamepad+键盘+遥控
			IMITATE_KEYBOARD_CONTROL = 13,// 模拟模式+键盘+遥控;
			IMITATE_GAMEPAD_KEYBOARD_CONTROL = 15;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0: // 评分提交成功
				rbtTwo.requestFocus();
				rbtTwo.setNextFocusDownId(R.id.game_detail_rbt_two);
				commitBtn.setVisibility(View.INVISIBLE);
				ratingTv.setVisibility(View.INVISIBLE);
				scoreRatingBar.setVisibility(View.INVISIBLE);
				TextView textView = (TextView) secondView
						.findViewById(R.id.score_success_tv);
				textView.setVisibility(View.VISIBLE);
				break;
			case 1: // 获取到数据，显示更多精彩游戏
				for (int i = 0; i < moreGames.size(); i++) {
					MoreGameItemListener moreGameItemListener = new MoreGameItemListener(
							i);
					moreGameView.get(i)
							.setOnClickListener(moreGameItemListener);
					moreGameView.get(i)
							.setOnTouchListener(moreGameItemListener);
					setMoreGameData(moreGameView.get(i), moreGames.get(i));
				}
				break;
			case 2: // 评分提交失败
				commitBtn.setClickable(true);
				commitBtn.setText("提交");
				int isNetExcepton = msg.arg1;
				if (isNetExcepton == 1) {
					NewToast.makeToast(context, R.string.commit_score_fail,
							Toast.LENGTH_LONG).show();
				} else {
					NewToast.makeToast(context, R.string.commit_score_fail_net,
							Toast.LENGTH_LONG).show();
				}
				break;

			}
		}

	};

	public GameDetailSecondItemView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public GameDetailSecondItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public GameDetailSecondItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	/**
	 * 
	 * @description 初始化界面
	 * @param context
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:04:15
	 * 
	 */
	private void init(Context context) {
		this.context = context;
		mImageFetcher = ((BaseActivity) context).getmImageFetcher();
		View view = LayoutInflater.from(context).inflate(
				R.layout.game_detail_remark_item, this, true);
		layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		frameLayout = (FrameLayout) view
				.findViewById(R.id.game_detail_scrollview);
		firstView = LayoutInflater.from(context).inflate(
				R.layout.game_detail_ramark_first_view, null);
		secondView = LayoutInflater.from(context).inflate(
				R.layout.game_detail_remark_second_view, null);
		threeView = LayoutInflater.from(context).inflate(
				R.layout.game_detail_remark_threee_view, null);
		gameDetailName = (TextView) firstView
				.findViewById(R.id.game_detail_name);
		gameScore = (RatingBar) firstView
				.findViewById(R.id.game_detail_ratingbar);
		gameDownCount = (TextView) firstView
				.findViewById(R.id.game_detail_downcounts);
		gameSize = (TextView) firstView.findViewById(R.id.game_detail_size);
		gameVersion = (TextView) firstView
				.findViewById(R.id.game_detail_version);
		gameControl = (TextView) firstView
				.findViewById(R.id.game_detail_control);
		gameDes = (TextView) firstView.findViewById(R.id.game_detail_remark_tv);
		ratingTv = (TextView) secondView
				.findViewById(R.id.game_detail_score_tv);
		commitBtn = (Button) secondView
				.findViewById(R.id.game_detail_score_btn);
		scoreRatingBar = (ScoreRatingBar) secondView
				.findViewById(R.id.game_detail_ratingbar);
		iv_qrcode = (ImageView) firstView.findViewById(R.id.qr_code_iv_one);
		group = (RadioGroup) view.findViewById(R.id.game_detail_radiogroup);
		rbtOne = (RadioButton) view.findViewById(R.id.game_detail_rbt_one);
		rbtTwo = (RadioButton) view.findViewById(R.id.game_detail_rbt_two);
		rbtThree = (RadioButton) view.findViewById(R.id.game_detail_rbt_three);
		rbtOne.setOnClickListener(onClickListener); // 设置顶部radioButton点击事件
		rbtTwo.setOnClickListener(onClickListener);
		rbtThree.setOnClickListener(onClickListener);
		rbtTwo.setOnKeyListener(mOnKeyListener);
		commitBtn.setOnKeyListener(mOnKeyListener);
		rbtOne.setChecked(true);
		scoreRatingBar.setFocusable(false);
		scoreRatingBar.setFocusableInTouchMode(false);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@SuppressLint("ResourceAsColor")
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.game_detail_rbt_one) {
					frameLayout.removeAllViews();
					frameLayout.addView(firstView, layoutParams);
					// Umeng统计查看"游戏简介"次数
					UmengUtils.setOnEvent(getContext(),
							UmengUtils.GAMEDETAIL_RECOMMON);
				} else if (checkedId == R.id.game_detail_rbt_two) {
					// Umeng统计查看"精彩评分"的次数
					UmengUtils.setOnEvent(getContext(),
							UmengUtils.GAMEDETAIL_MARK);
					frameLayout.removeAllViews();
					if (twoCount == 0) {
						ScaleViewUtils.scaleView((ViewGroup) secondView);
						twoCount++;
					}
					frameLayout.addView(secondView, layoutParams);
				} else if (checkedId == R.id.game_detail_rbt_three) {
					// Umeng统计查看"更多精彩"的次数
					UmengUtils.setOnEvent(getContext(),
							UmengUtils.GAMEDETAIL_MORE);
					frameLayout.removeAllViews();
					if (threeCount == 0) {
						ScaleViewUtils.scaleView((ViewGroup) threeView);
						threeCount++;
					}
					frameLayout.addView(threeView, layoutParams);
				}
			}
		});
		// 评分按钮点击事件
		commitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub 提交评分
				commitRateGame(scoreRatingBar.getScore());
				UmengUtils.setOnEvent(getContext(),
						UmengUtils.GAMEDETAIL_SUBMIT);
			}
		});
		commitBtn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (MotionEvent.ACTION_UP == event.getAction()) {
					v.requestFocusFromTouch();
					v.performClick();
				}
				return true;
			}
		});
		frameLayout.addView(firstView, layoutParams);
		setFocus(); // 设置焦点
		// addView(view);
	}

	public FrameLayout getScrollView() {
		return frameLayout;
	}

	public void setScrollView(FrameLayout frameLayout) {
		this.frameLayout = frameLayout;
	}

	public View getFirstView() {
		return firstView;
	}

	public void setFirstView(View firstView) {
		this.firstView = firstView;
	}

	public View getSecondView() {
		return secondView;
	}

	public void setSecondView(View secondView) {
		this.secondView = secondView;
	}

	public View getThreeView() {
		return threeView;
	}

	public void setThreeView(View threeView) {
		this.threeView = threeView;
	}

	public RadioGroup getGroup() {
		return group;
	}

	public void setGroup(RadioGroup group) {
		this.group = group;
	}

	public RadioButton getRbtOne() {
		return rbtOne;
	}

	public void setRbtOne(RadioButton rbtOne) {
		this.rbtOne = rbtOne;
	}

	public RadioButton getRbtTwo() {
		return rbtTwo;
	}

	public void setRbtTwo(RadioButton rbtTwo) {
		this.rbtTwo = rbtTwo;
	}

	public RadioButton getRbtThree() {
		return rbtThree;
	}

	public void setRbtThree(RadioButton rbtThree) {
		this.rbtThree = rbtThree;
	}

	public View getThreeView0() {
		return threeView0;
	}

	public void setThreeView0(View threeView0) {
		this.threeView0 = threeView0;
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			RadioButton rbtn = (RadioButton) v;
			rbtn.setChecked(true);
		}
	};

	/**
	 * 
	 * @description 设置焦点
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:04:31
	 * 
	 */
	private void setFocus() {
		ScrollView scrollView = (ScrollView) firstView
				.findViewById(R.id.game_detail_remark_scroll);
		scrollView.setNextFocusRightId(R.id.game_detail_rbt_two);
		scrollView.setNextFocusUpId(R.id.game_detail_rbt_one);
		rbtOne.setNextFocusDownId(R.id.game_detail_rbt_one);
		commitBtn.setNextFocusLeftId(R.id.game_detail_score_btn);
		commitBtn.setNextFocusRightId(R.id.game_detail_score_btn);
		commitBtn.setNextFocusDownId(R.id.game_detail_score_btn);
		threeView0 = threeView.findViewById(R.id.game_detail_more_item1);
		threeView1 = threeView.findViewById(R.id.game_detail_more_item2);
		threeView2 = threeView.findViewById(R.id.game_detail_more_item3);
		threeView3 = threeView.findViewById(R.id.game_detail_more_item4);
		threeView4 = threeView.findViewById(R.id.game_detail_more_item5);
		threeView5 = threeView.findViewById(R.id.game_detail_more_item6);
		threeView0.setNextFocusUpId(R.id.game_detail_rbt_three);
		threeView1.setNextFocusUpId(R.id.game_detail_rbt_three);
		threeView2.setNextFocusUpId(R.id.game_detail_rbt_three);
		threeView3.setNextFocusDownId(R.id.game_detail_more_item4);
		threeView4.setNextFocusDownId(R.id.game_detail_more_item5);
		threeView5.setNextFocusDownId(R.id.game_detail_more_item6);
		threeView0.setNextFocusLeftId(R.id.game_detail_rbt_two);
		threeView3.setNextFocusLeftId(R.id.game_detail_rbt_two);
		threeView0.setOnFocusChangeListener(this);
		threeView1.setOnFocusChangeListener(this);
		threeView2.setOnFocusChangeListener(this);
		threeView3.setOnFocusChangeListener(this);
		threeView4.setOnFocusChangeListener(this);
		threeView5.setOnFocusChangeListener(this);
		moreGameView.add(threeView0);
		moreGameView.add(threeView1);
		moreGameView.add(threeView2);
		moreGameView.add(threeView3);
		moreGameView.add(threeView4);
		moreGameView.add(threeView5);
		commitBtn.setNextFocusLeftId(R.id.game_detail_rbt_one);
		commitBtn.setNextFocusRightId(R.id.game_detail_rbt_three);

	}

	/**
	 * // 更多精彩 游戏点击事件类
	 * 
	 * @author chenqingwen
	 * 
	 */
	class MoreGameItemListener implements OnClickListener, OnTouchListener {
		private int index;

		public MoreGameItemListener(int index) {
			this.index = index;
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(context, RelativeGameActivity.class);
			intent.putExtra("gameInfo", moreGames.get(index));
			context.startActivity(intent);
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (MotionEvent.ACTION_UP == event.getAction()) {
				v.requestFocusFromTouch();
				onClick(v);
			}
			return true;
		}
	}

	@Override
	public void onFocusChange(View arg0, boolean arg1) {
		// TODO Auto-generated method stub
		ImageView imageView = (ImageView) arg0
				.findViewById(R.id.game_more_shadow);
		if (arg1) {
			arg0.setScaleX(1.2f);
			arg0.setScaleY(1.2f);
			imageView.setBackgroundResource(R.drawable.game_more_shadow);
		} else {
			arg0.setScaleX(1.0f);
			arg0.setScaleY(1.0f);
			imageView.setBackgroundResource(R.color.transparent);
		}
	}

	/**
	 * 
	 * @description 显示第二个Item数据
	 * @param info
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:05:11
	 * 
	 */
	public void setData(GameInfo info) {
		gameId = info.getGameId();
		gameDetailName.setText(info.getGameName());
		gameDownCount.setText("下载次数：" + info.getGameDownCount());
		gameSize.setText("大小："
				+ StringTool.StringToFloat(info.getGameSize() + "") + "MB");
		gameVersion.setText("版本：" + info.getVersionName());
		gameDes.setText("    " + info.getRemark());
		Double rating = info.getStartLevel();
		if (rating == null) {
			gameScore.setRating(0);
		} else {
			gameScore.setRating((float) (double) rating);
		}

		DecideAdapter(info.getHandleType(), gameControl);
		getRelativeGameInfosFromGameId(info.getGameId());
		loadQrCode(info);
		// 先检测用户是否登录
		if (DataFetcher.isUserLogin()) {
			// 用户id
			String userId = DataFetcher.getUserId();
			// 判断该游戏是否已评分
			boolean rated = DataFetcher.isGameRated(getContext(),
					info.getGameId(), userId);
			if (rated) { // 已评分
				rbtTwo.setNextFocusDownId(R.id.game_detail_rbt_two);
				scoreRatingBar.setVisibility(View.INVISIBLE);
				commitBtn.setVisibility(View.INVISIBLE);
				ratingTv.setVisibility(View.INVISIBLE);
				TextView textView = (TextView) secondView
						.findViewById(R.id.score_success_tv);
				textView.setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * 
	 * @description 提交游戏评分
	 * @param score
	 * @return
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:05:26
	 * 
	 */
	public boolean commitRateGame(int score) {
		// 先检测用户是否登录
		if (!DataFetcher.isUserLogin()) {
			NewToast.makeToast(context, R.string.please_login, Toast.LENGTH_LONG)
					.show();
			return false;
		}

		// 检测是否网络异常
		if (!NetUtil.isNetworkAvailable(getContext(), true)) {
			NewToast.makeToast(context, R.string.commit_score_fail_net,
					Toast.LENGTH_LONG).show();
			return false;
		}
		commitBtn.setClickable(false);
		commitBtn.setText("正在提交");
		// 用户id
		String userId = DataFetcher.getUserId();

		ReqCallback<String> reqCallback = new ReqCallback<String>() {
			@Override
			public void onResult(TaskResult<String> taskResult) {
				int code = taskResult.getCode();
				if (code == TaskResult.OK) { // 提交成功
					Message message = handler.obtainMessage();
					message.what = 0;
					handler.sendMessage(message);
				} else { // 提交失败
					if (NetUtil.isNetworkAvailable(context, true)) {
						Message message = handler.obtainMessage();
						message.what = 2;
						message.arg1 = 1;
						handler.sendMessage(message);
					} else {
						Message message = handler.obtainMessage();
						message.what = 2;
						message.arg1 = 0;
						handler.sendMessage(message);
					}
				}

			}

			@Override
			public void onUpdate(TaskResult<String> taskResult) {

			}
		};
		DataFetcher.rateGame(getContext(), userId, gameId, score, reqCallback,
				false).request(getContext());

		return true;
	}

	/**
	 * 
	 * @description 加载二维码图片下载地址
	 * @param gameInfo
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:05:46
	 * 
	 */
	private void loadQrCode(final GameInfo gameInfo) {
		final String downAdress = gameInfo.getFile();
		if (null != downAdress) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					final Bitmap bitmap;
					try {
						bitmap = QRUtil.createQRCode(
								UrlConstant.HTTP_GAME_DETAIL_QRCODE
										+ gameInfo.getGameId(), 100);
						handler.post(new Runnable() {

							@Override
							public void run() {
								iv_qrcode.setImageBitmap(bitmap);
							}
						});
					} catch (WriterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	/***
	 * 
	 * @description 判断适配的类型
	 * @param handType
	 * @param mTvAdapter
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:05:59
	 * 
	 */
	private void DecideAdapter(Integer handType, TextView mTvAdapter) {
		switch (handType) {
		case IMITATE:
			mTvAdapter.setText(getResources().getString(
					R.string.land_detail_adapter)
					+ getResources().getString(R.string.land_detail_hand));
			break;

		case GAMEPAD:
			mTvAdapter.setText(getResources().getString(
					R.string.land_detail_adapter)
					+ getResources().getString(R.string.land_detail_hand));
			break;

		case KEYBOARD:
			mTvAdapter.setText(getResources().getString(
					R.string.land_detail_adapter)
					+ getResources().getString(R.string.land_detail_keyboard));
			break;

		case CONTROL:
			mTvAdapter.setText(getResources().getString(
					R.string.land_detail_adapter)
					+ getResources().getString(
							R.string.land_detail_remote_controlled));
			break;

		case IMITATE_GAMEPAD:
			mTvAdapter.setText(getResources().getString(
					R.string.land_detail_adapter)
					+ getResources().getString(R.string.land_detail_hand));
			break;

		case IMITATE_KEYBOARD:
			mTvAdapter.setText(getResources().getString(
					R.string.land_detail_adapter)
					+ getResources().getString(R.string.land_detail_hand)
					+ ";"
					+ getResources().getString(R.string.land_detail_keyboard));
			break;

		case IMITATE_CONTROL:
			mTvAdapter.setText(getResources().getString(
					R.string.land_detail_adapter)
					+ getResources().getString(R.string.land_detail_hand)
					+ ";"
					+ getResources().getString(
							R.string.land_detail_remote_controlled));
			break;

		case GAMEPAD_KEYBOARD:
			mTvAdapter.setText(getResources().getString(
					R.string.land_detail_adapter)
					+ getResources().getString(R.string.land_detail_hand)
					+ ";"
					+ getResources().getString(R.string.land_detail_keyboard));
			break;

		case GAMEPAD_CONTROL:
			mTvAdapter.setText(getResources().getString(
					R.string.land_detail_adapter)
					+ getResources().getString(R.string.land_detail_hand)
					+ ";"
					+ getResources().getString(
							R.string.land_detail_remote_controlled));
			break;

		case KEYBOARD_CONTROL:
			mTvAdapter.setText(getResources().getString(
					R.string.land_detail_adapter)
					+ getResources().getString(R.string.land_detail_keyboard)
					+ ";"
					+ getResources().getString(
							R.string.land_detail_remote_controlled));
			break;

		case IMITATE_GAMEPAD_KEYBOARD:
			mTvAdapter.setText(getResources().getString(
					R.string.land_detail_adapter)
					+ getResources().getString(R.string.land_detail_hand)
					+ ";"
					+ getResources().getString(R.string.land_detail_keyboard));
			break;

		case IMITATE_GAMEPAD_CONTROL:
			mTvAdapter.setText(getResources().getString(
					R.string.land_detail_adapter)
					+ getResources().getString(R.string.land_detail_hand)
					+ ";"
					+ getResources().getString(
							R.string.land_detail_remote_controlled));
			break;

		case GAMEPAD_KEYBOARD_CONTROL:
			mTvAdapter.setText(getResources().getString(
					R.string.land_detail_adapter)
					+ getResources().getString(R.string.land_detail_hand)
					+ ";"
					+ getResources().getString(
							R.string.land_detail_remote_controlled)
					+ ";"
					+ getResources().getString(R.string.land_detail_keyboard));
			break;

		case IMITATE_KEYBOARD_CONTROL:
			mTvAdapter.setText(getResources().getString(
					R.string.land_detail_adapter)
					+ getResources().getString(R.string.land_detail_hand)
					+ ";"
					+ getResources().getString(
							R.string.land_detail_remote_controlled)
					+ ";"
					+ getResources().getString(R.string.land_detail_keyboard));
			break;

		case IMITATE_GAMEPAD_KEYBOARD_CONTROL:
			mTvAdapter.setText(getResources().getString(
					R.string.land_detail_adapter)
					+ getResources().getString(R.string.land_detail_hand)
					+ ";"
					+ getResources().getString(
							R.string.land_detail_remote_controlled)
					+ ";"
					+ getResources().getString(R.string.land_detail_keyboard));
			break;
		}
	}

	/**
	 * @description: 获取与gameId同分类下的其它游戏
	 * 
	 * @param context
	 * @param gameId
	 * @return
	 * @author: LiuQin
	 * @date: 2015年7月14日 上午11:57:05
	 */
	private void getRelativeGameInfosFromGameId(String gameId) {
		ReqCallback<List<GameInfo>> reqCallback = new ReqCallback<List<GameInfo>>() {
			@Override
			public void onResult(TaskResult<List<GameInfo>> taskResult) {
				int code = taskResult.getCode();
				if (code == TaskResult.OK) {
					moreGames = taskResult.getData();
					if (moreGames != null && moreGames.size() > 0) {
						Message message = handler.obtainMessage();
						message.what = 1;
						handler.sendMessage(message);
					}
				}

			}

			@Override
			public void onUpdate(TaskResult<List<GameInfo>> taskResult) {

			}
		};
		DataFetcher
				.getRelativeGameInfosFromGameId(getContext(), gameId,
						reqCallback, false).registerUpdateListener(reqCallback)
				.request(getContext());
	}

	/**
	 * 
	 * @description 设置更多精彩游戏
	 * @param view
	 * @param gameInfo
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:06:22
	 * 
	 */
	private void setMoreGameData(View view, GameInfo gameInfo) {
		view.setVisibility(View.VISIBLE);
		ImageView imageView = (ImageView) view.findViewById(R.id.game_more_iv);
		TextView textView = (TextView) view.findViewById(R.id.game_more_tv);
		textView.setText(gameInfo.getGameName());
		mImageFetcher.loadLocalImage(R.drawable.game_detail_more_bg, imageView,
				R.drawable.gameranking_item_icon);
		mImageFetcher.loadImage(gameInfo.getMinPhoto(), imageView, 15.0f, 0);
	}

	private OnKeyListener mOnKeyListener = new OnKeyListener() {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {

			if (v == rbtTwo && KeyEvent.KEYCODE_DPAD_DOWN == keyCode) {
				// 请求焦点
				scoreRatingBar.requestRatingBarFocus();
				return true;
			} else if (v == commitBtn && KeyEvent.KEYCODE_DPAD_UP == keyCode) {
				// 请求焦点
				scoreRatingBar.requestRatingBarFocus();
				return true;
			}
			return false;
		}
	};
}
