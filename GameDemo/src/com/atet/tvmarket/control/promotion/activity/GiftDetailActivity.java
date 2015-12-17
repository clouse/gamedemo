package com.atet.tvmarket.control.promotion.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.control.home.inf.OnRecyItemClickListener;
import com.atet.tvmarket.control.mine.MineAccountManagerActivity;
import com.atet.tvmarket.control.promotion.decoration.DepositBoxInsetDecoration;
import com.atet.tvmarket.control.promotion.holder.GiftDetailHolder;
import com.atet.tvmarket.control.promotion.view.MyButton;
import com.atet.tvmarket.entity.dao.GameGiftInfo;
import com.atet.tvmarket.entity.dao.GiftInfo;
import com.atet.tvmarket.entity.dao.UserGameGiftInfo;
import com.atet.tvmarket.entity.dao.UserGameToGift;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UmengUtils;
import com.atet.tvmarket.view.CloseAcceTextView;
import com.atet.tvmarket.view.CommonProgressDialog;
import com.atet.tvmarket.view.LoadingView;
import com.atet.tvmarket.view.NewToast;
import com.atet.tvmarket.view.TvRecyclerView;

/*
 * File：GiftReceived.java
 *
 * Copyright (C) 2015 MainActivity Project
 * Date：2015年6月11日 上午11:40:57
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */
public class GiftDetailActivity extends BaseActivity implements
		OnRecyItemClickListener {
	ALog alog = ALog.getLogger(GiftDetailActivity.class);
	private static final int FAILED = 0;
	private static final int SUCCESS = 1;
	protected static final int NO_DATA = 2;
	private  static final int REPEATE = 3;
	private static final String ISRECEIVED = "isReceived";
	private boolean allReceived = false;
	private ImageFetcher mImageFetcher;
	private TvRecyclerView recyclerView;
	private LinearLayoutManager manager;
	private GiftReceicedAdapter adapter;
	private CloseAcceTextView gift_received_tag;
	private DepositBoxInsetDecoration insetDecoration;
	private LoadingView loadingView;
	// private Button deposit_box;
	private boolean isFirstFocus = true;
	private GiftActivity giftActivity;
	private MyButton myButton;
	private Integer[] mGrayButton = { R.color.bt_gift_received_color,
			R.drawable.bt_gray_focus, R.drawable.bt_gray_click };
	private int position;

	private GameGiftInfo info;
	private List<GiftInfo> giftinfos = new ArrayList<GiftInfo>();
	private List<UserGameToGift> userGameToGiftList = new ArrayList<UserGameToGift>();
	private UserGameGiftInfo userGameGiftInfo;

	private Message msg;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			loadingView.setVisibility(View.INVISIBLE);
			switch (msg.what) {
			case SUCCESS:
				NewToast.makeToast(getApplicationContext(),R.string.gift_received_success, 0).show();
				Intent intent = new Intent(GiftDetailActivity.this,GiftReceivedSucActivity.class);
				if(giftinfos != null && giftinfos.size() != 0){
					intent.putExtra(Constant.GIFTNAME, giftinfos.get(position).getName());
				}
				intent.putExtra(Constant.GIFTCODE, (String) msg.obj);
				startActivity(intent);
				giftIsReceived(false);
				break;
			case FAILED:
				if ((Integer) msg.obj == TaskResult.HTTP_NO_CONNECTION) {
					NewToast.makeToast(GiftDetailActivity.this,R.string.http_no_connect, 0).show();
				} else {
					NewToast.makeToast(GiftDetailActivity.this,
							R.string.gift_received_failed, 0).show();
				}
				break;
			case NO_DATA:
				NewToast.makeToast(GiftDetailActivity.this,R.string.no_gift_received, 0).show();
				giftIsReceived(true);
			case REPEATE:
				NewToast.makeToast(GiftDetailActivity.this,R.string.gift_repeate, 0).show();
				giftIsReceived(false);
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		setBlackTitle(false);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setTitleData();
	}

	private void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_gift_received);
		ScaleViewUtils.init(this);
		ScaleViewUtils.scaleView(getWindow().getDecorView());
		gift_received_tag = (CloseAcceTextView) findViewById(R.id.gift_received_tag);
		recyclerView = (TvRecyclerView) findViewById(R.id.gift_received_recyclerview);
		loadingView = (LoadingView) findViewById(R.id.gift_received_loading);
		loadingView.setDataView(recyclerView);
		// deposit_box = (Button) findViewById(R.id.gift_received_Box_bt);

		myButton = new MyButton(this);
		giftActivity = new GiftActivity();
		mImageFetcher = new ImageFetcher();
		manager = new LinearLayoutManager(getApplicationContext());
		adapter = new GiftReceicedAdapter();
		insetDecoration = new DepositBoxInsetDecoration(getApplicationContext());

		// deposit_box.setOnFocusChangeListener(new
		// RecycleOnFocusChangeListener());
		// deposit_box.setOnTouchListener(new RecycleOnTouchListener());
		// deposit_box.setOnClickListener(this);
		adapter.setOnRecyItemClickListener(this);

		initData();

		manager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.addItemDecoration(insetDecoration);
		recyclerView.setLayoutManager(manager);
		recyclerView.setAdapter(adapter);
	}

	private void initData() {
		info = (GameGiftInfo) getIntent().getSerializableExtra(
				Constant.GAMEGIFTINFO);
		userGameGiftInfo = (UserGameGiftInfo) getIntent().getSerializableExtra(
				Constant.USERGAMEGIFTINFO);
		if(info != null){
			giftinfos = info.getGiftData();
		}

		if (userGameGiftInfo != null) {
			userGameToGiftList = userGameGiftInfo.getUserGameToGiftList();
		}
		alog.info("size=" + giftinfos.size());
	}

	/**
	 * 
	 * @description: 礼包已领取，判断是否所有礼包全部领取成功
	 * 
	 * @throws:
	 * @author: LiJie
	 * @date: 2015年8月17日 上午10:38:51
	 */
	private void giftIsReceived(boolean noGift) {
		GiftDetailHolder holder = (GiftDetailHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(position));
		if(noGift){
			holder.gift_received_bt.setText(R.string.gift_all_received);
		}else{
			holder.gift_received_bt.setText(R.string.gift_item_received);
		}
		holder.gift_received_bt.setBackgroundDrawable(myButton.setBg(mGrayButton));
		holder.gift_received_bt.setClickable(false);
	}

	class GiftReceicedAdapter extends Adapter<ViewHolder> {
		OnRecyItemClickListener mListener;
		GiftDetailHolder holder;

		@Override
		public int getItemCount() {
			if (giftinfos.size() != 0) {

				return giftinfos.size();
			}
			return 0;
		}

		@Override
		public void onBindViewHolder(ViewHolder viewHolder, int position) {
			holder = (GiftDetailHolder) viewHolder;

			// 解决recyclerview的滑到第二个的时候直接跳到第一个的问题
			if (isFirstFocus) {
				if (position == 0) {
					holder.gift_received_bt.requestFocus();
				}
				isFirstFocus = false;
			}

			if (giftinfos.get(position) != null) {
				holder.setData(giftinfos.get(position), userGameToGiftList);
				mImageFetcher.loadImage(info.getMinPhoto(), holder.game_icon,
						R.drawable.gift_default_bg);
			}

		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
			LayoutInflater inflater = LayoutInflater
					.from(getApplicationContext());
			View root = inflater.inflate(R.layout.gift_received_item,
					viewGroup, false);
			return new GiftDetailHolder(root, mListener, recyclerView,
					GiftDetailActivity.this);
		}

		public void setOnRecyItemClickListener(OnRecyItemClickListener listener) {
			this.mListener = listener;
		}

	}

	@Override
	public void onItemClick(View view, int position) {
		view.requestFocus();
		// Umeng统计点击"领取"事件信息
		UmengUtils.setOnEvent(GiftDetailActivity.this,UmengUtils.PROMOTION_GIFT_RECEIVE_CLICK);
		this.position = position;
		pullGift(giftinfos.get(position));
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
										GiftDetailActivity.this,
										MineAccountManagerActivity.class);
								startActivity(intent);
							}

						}).setNegativeButton(R.string.cancle_login, null)
				.create();
		mDialog.setParams(mDialog);
		mDialog.show();

	}

	private void pullGift(GiftInfo giftInfo) {
		// 先检测用户是否登录
		if (!DataFetcher.isUserLogin()) {
			// NewToast.makeToast(this, R.string.gift_please_login, 0).show();
			snapDialog();
			return;
		}

		loadingView.setVisibility(View.VISIBLE);
		msg = Message.obtain();
		msg.what = Constant.PULLGIFT;
		loadingView.getmHandler().sendMessage(msg);

		// 用户id
		String userId = DataFetcher.getUserId();
		// 礼包id
		String giftPackageid = giftInfo.getGiftPackageid();

		ReqCallback<String> reqCallback = new ReqCallback<String>() {
			@Override
			public void onResult(TaskResult<String> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
			System.out.println("taskResult----" +code );
				if (code == TaskResult.OK) {
					String giftCode = taskResult.getData();
					if (giftCode != null && giftCode != "") {
						UmengUtils.setOnEvent(GiftDetailActivity.this,UmengUtils.PROMOTION_GIFT_RECEIVE_SUCCESS);
						msg = Message.obtain();
						msg.what = SUCCESS;
						msg.obj = giftCode;
						mHandler.sendMessage(msg);
					}else {
						msg = Message.obtain();
						msg.what = FAILED;
						mHandler.sendMessage(msg);
					}
					alog.info("giftCode:" + giftCode);
				} else if (code == TaskResult.NO_DATA){
					msg = Message.obtain();
					msg.what = NO_DATA;
					mHandler.sendMessage(msg);
				} else if(code == 1802){
					mHandler.sendEmptyMessage(REPEATE);
				}else {
					msg = Message.obtain();
					msg.what = FAILED;
					msg.obj = code;
					mHandler.sendMessage(msg);
				}
			}

			@Override
			public void onUpdate(TaskResult<String> taskResult) {

			}
		};
		DataFetcher
				.obtainUserGift(getApplicationContext(), userId, giftPackageid,
						reqCallback, false).registerUpdateListener(reqCallback)
				.request(getApplicationContext());

	}

	/*
	 * @Override public boolean dispatchKeyEvent(KeyEvent event) { int keyCode =
	 * event.getKeyCode(); int action = event.getAction();
	 * if(deposit_box.isFocused()){ if(action == KeyEvent.ACTION_DOWN && keyCode
	 * == KeyEvent.KEYCODE_DPAD_UP){ RelativeLayout rl = (RelativeLayout)
	 * recyclerView.getChildAt(recyclerView.getChildCount() - 1);
	 * rl.findViewById(R.id.gift_received_bt).requestFocus(); } return true; }
	 * return super.dispatchKeyEvent(event); }
	 */
}
