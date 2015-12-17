package com.atet.tvmarket.control.promotion.holder;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.mine.MineAccountManagerActivity;
import com.atet.tvmarket.control.mygame.utils.QRUtil;
import com.atet.tvmarket.control.promotion.activity.IntegralDetailsActivity;
import com.atet.tvmarket.control.promotion.activity.IntegralExchangeSucActivity;
import com.atet.tvmarket.entity.GoodsExchangeReq;
import com.atet.tvmarket.entity.GoodsExchangeResp;
import com.atet.tvmarket.entity.GoodsLeftCountResp.GoodsLeftCountInfo;
import com.atet.tvmarket.entity.dao.GoodsInfo;
import com.atet.tvmarket.model.DaoHelper;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UIUtils;
import com.atet.tvmarket.utils.UmengUtils;
import com.atet.tvmarket.view.BaseImageView;
import com.atet.tvmarket.view.CommonProgressDialog;
import com.atet.tvmarket.view.NewToast;
import com.google.zxing.WriterException;

/*
 * File：DetailItemfirstHolder.java
 *
 * Copyright (C) 2015 MainActivity Project
 * Date：2015年7月12日 下午6:58:41
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */
public class IntegralfirstHolder extends PromotionBaseHolder implements
		OnFocusChangeListener, OnKeyListener, OnClickListener {
	ALog alog = ALog.getLogger(IntegralfirstHolder.class);
	private static final int SUCCESS = 0;
	private static final int FAILED = 1;
	private static final int UPDATE = 2;
	public Button bt_exchange;
	public Button bt_buy;
	public BaseImageView game_icon;
	public BaseImageView game_code;
	public BaseImageView game_shadow;
	/*
	 * public RelativeLayout rl_vedio; public BaseImageView vedio_icon; public
	 * BaseImageView vedio_shadow;
	 */
	private RelativeLayout rl_detail;
	private BaseImageView rl_detail_shadow;
	private TextView tv_goods;
	private TextView tv_desc;
	private TextView tv_time;
	private TextView tv_integral;
	private TextView tv_count;
	private TextView tv_remain;
	private TextView code_caption;
	private ImageView emptyProgress;
	private AnimationDrawable mDrawable;

	private IntegralDetailsActivity context;
	private GoodsInfo info;
	private RecyclerView item_recyclerView;
	private GoodsExchangeResp goodsExchangeResp;
	private RecyclerView recyclerView;// 最外层的RecyclerView
	private int web_tag = 1; // 网页页面显示
	private int order_tag = 1;// 订单页面显示

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				// Umeng统计积分"兑换"成功
				UmengUtils.setOnEvent(context,UmengUtils.PROMOTION_INTEGRAL_EXCHANGE_SUCCESS);
				Intent intent = new Intent(context,IntegralExchangeSucActivity.class);
				//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra(Constant.QRCODE,getQRCode(goodsExchangeResp, (Integer) msg.obj));
				context.startActivityForResult(intent, 0);
				
				// 更新商品剩余数量的显示
				tv_remain.setText(UIUtils.getString(R.string.surplus)+ info.getSurplus());
				context.setTitleData();
				break;
			case FAILED:
				NewToast.makeToast(UIUtils.getContext(), (String)msg.obj, 0).show();
				break;
				
			case UPDATE:
				tv_remain.setText(UIUtils.getString(R.string.surplus)+ info.getSurplus());
				break;

			default:
				break;
			}
			stopAnima();
		};
	};

	public IntegralfirstHolder(View itemView, IntegralDetailsActivity context,
			RecyclerView item_recyclerView, RecyclerView recyclerView) {
		super(itemView);
		this.recyclerView = recyclerView;
		this.item_recyclerView = item_recyclerView;
		this.context = context;
		initView();
	}

	@Override
	protected void initView() {
		bt_exchange = (Button) itemView.findViewById(R.id.item_download);
		bt_buy = (Button) itemView.findViewById(R.id.item_buy);
		
		game_icon = (BaseImageView) itemView	.findViewById(R.id.detail_item2_icon);
		game_code = (BaseImageView) itemView.findViewById(R.id.detail_item2_code);
		game_shadow = (BaseImageView) itemView.findViewById(R.id.detail_item2_shadow);
		rl_detail = (RelativeLayout) itemView.findViewById(R.id.integral_rl_detail);
		rl_detail_shadow = (BaseImageView) itemView.findViewById(R.id.integral_rl_detail_shadow);
		tv_goods = (TextView) itemView.findViewById(R.id.detail_item2_goods);
		tv_desc = (TextView) itemView.findViewById(R.id.detail_item2_desc);
		tv_time = (TextView) itemView.findViewById(R.id.detail_item2_time);
		tv_integral = (TextView) itemView.findViewById(R.id.detail_item2_integral);
		tv_count = (TextView) itemView.findViewById(R.id.detail_item2_count);
		tv_remain = (TextView) itemView.findViewById(R.id.detail_item2_remain);
		code_caption = (TextView) itemView.findViewById(R.id.detail_item2_code_caption);
		
		emptyProgress = (ImageView) itemView.findViewById(R.id.idetail_item2_emptyProgress);
		mDrawable = (AnimationDrawable) emptyProgress.getDrawable();
		

		bt_exchange.setOnFocusChangeListener(this);
		bt_exchange.setOnKeyListener(this);
		bt_exchange.setOnClickListener(this);
		
		bt_buy.setOnFocusChangeListener(this);
		bt_buy.setOnKeyListener(this);
		bt_buy.setOnClickListener(this);
		
		rl_detail.setOnFocusChangeListener(this);
		rl_detail.setOnKeyListener(this);
	}

	public void setData(GoodsInfo info) {
		this.info = info;
		mImageFetcher.loadImage(info.getErectPhoto(), game_icon,R.drawable.default_vertical);
		String startTime = UIUtils.changeTimeStyle(info.getStartTime());
		String endTime = UIUtils.changeTimeStyle(info.getEndTime());
		tv_time.setText(UIUtils.getString(R.string.integral_exchange_time)+ startTime + "-" + endTime);
		tv_integral.setText(UIUtils.getString(R.string.exchange_integral)+ info.getIntegral());
		
		tv_remain.setText(UIUtils.getString(R.string.surplus)+ info.getSurplus());
		//System.out.println("tv_remain" +  info.getSurplus());
		
		tv_desc.setText(info.getRemark());
		tv_goods.setText(UIUtils.getString(R.string.goods_name)+ info.getName());
		tv_count.setText(UIUtils.getString(R.string.exchange_people));
		code_caption.setText(info.getCaption());
		Bitmap QRBitmap = null;
		try {
			QRBitmap = QRUtil.createQRCode(info.getQrCode(),
					(int) ScaleViewUtils.resetTextSize(400));
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		game_code.setImageBitmap(QRBitmap);
		// tv_rule.setText(info.getRules());
		// tv_reward.setText(info.getPrize());
		updateRemain(info);
	}
	
	/**
	 * 
	 * @description: 更新剩余商品的数量
	 * 
	 * @throws:
	 * @author: LiJie
	 * @date: 2015年7月30日 下午8:46:27
	 */
	private void updateRemain(GoodsInfo info) {
		
		GoodsInfo goodsInfo = info;
		ReqCallback<GoodsLeftCountInfo> reqCallback = new ReqCallback<GoodsLeftCountInfo>() {
			@Override
			public void onResult(TaskResult<GoodsLeftCountInfo> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					mHandler.sendEmptyMessage(UPDATE);
					alog.info("商品id:"+taskResult.getData().getGoodsId());
					alog.info("商品最新剩余数量:"+taskResult.getData().getSurplus());
				}

			}
		};
		DataFetcher.getGoodsLfetCount(UIUtils.getContext(), goodsInfo, reqCallback, false).request(UIUtils.getContext());
		
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		RecyclerView rootRecyclerView = (RecyclerView) item_recyclerView
				.getParent().getParent();
		alog.info("rootRecyclerView" + rootRecyclerView.getChildCount());
		int[] location = new int[2];
		v.getLocationOnScreen(location);

		if (hasFocus) {
			if (v.getId() == bt_exchange.getId()) {
				if (bt_exchange.getLeft() < 150) {
					rootRecyclerView.smoothScrollBy(-200, 0);
					item_recyclerView.smoothScrollBy(	-(bt_exchange.getWidth() * 2), 0);
				}
				//game_shadow.setBackgroundResource(R.drawable.white_focus);
			}else if(v.getId() == bt_buy.getId()){
				
			} else if (v.getId() == rl_detail.getId()) {

				if (location[0] - v.getWidth() * 1.1 < 0) {
					item_recyclerView.smoothScrollBy(-(int) (v.getWidth() * 1.5), 0);
				}
				// rl_vedio.setNextFocusRightId(R.id.detail_item2_rl_top);
				if(info != null){
					if(info.getUrl()!= null && !info.getUrl().isEmpty()){
						rootRecyclerView.smoothScrollToPosition(1);
					}
				}

				/*
				 * if(location[0] + v.getWidth() * 1.1 >
				 * mRecyclerView.getRight()){
				 * 
				 * mRecyclerView.smoothScrollBy(v.getWidth(), 0); }
				 */

				rl_detail_shadow.setBackgroundResource(R.drawable.white_focus);
			}
		} else {
			if (v.getId() == bt_exchange.getId()) {
				game_shadow.setBackgroundResource(android.R.color.transparent);
			} else if (v.getId() == rl_detail.getId()) {
				rl_detail_shadow.setBackgroundResource(android.R.color.transparent);
			}
		}

	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		int action = event.getAction();
		if (action == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_DPAD_UP
					|| keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {

				return true;
			}

			if (v.getId() == bt_exchange.getId()
					&& keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
				bt_buy.requestFocus();
				return true;
			}else if(v.getId() == bt_exchange.getId()&& keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
				if(info != null){
					if(info.getUrl()!= null && !info.getUrl().isEmpty()){
						recyclerView.smoothScrollToPosition(1);
					}
				}
			}else if (v.getId() == rl_detail.getId()) {
				if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
					alog.info("size" + info.getDetailPhotos().size() + "----"
							+ recyclerView.getChildCount());
					if (info.getDetailPhotos().size() == 0) {
						// 如果没有游戏截图，也没有订单页面，点击右键无效
						if (order_tag == 1) {
							context.getThirdHolder().item_recyclerView
									.getChildAt(0)
									.findViewById(R.id.integral_third)
									.requestFocus();
							return true;
						} else {
							return true;
						}
					} else {
						item_recyclerView.getChildAt(1)
								.findViewById(R.id.detail_item2_rl_top)
								.requestFocus();
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// Umeng统计"兑换"点击次数
		UmengUtils.setOnEvent(context,UmengUtils.PROMOTION_INTEGRAL_EXCHANGE_CLICK);
		
		if(NetUtil.isNetworkAvailable(context, true)){
			if(v.getId() == bt_exchange.getId()){
				snapEXchangeDialog();
			}else{
			//	snapBuyDialog();
				NewToast.makeToast(context, R.string.look_forward, 0).show();
			}
		}else{
			UIUtils.makeText(context, UIUtils.getString(R.string.http_no_connect));
		}
	}
	private void stopAnima(){
		if(mDrawable != null){
			mDrawable.stop();
		}
		emptyProgress.setVisibility(View.INVISIBLE);
	}
	
	private void startAnima(){
		emptyProgress.setVisibility(View.VISIBLE);
		mDrawable.start();
	}
	/**
	 * 
	 * @description: 获取二维码数据
	 * 
	 * @throws:
	 * @author: LiJie
	 * @date: 2015年7月30日 下午8:46:27
	 */
	private void loadExchangeData() {

		// 先检测用户是否登录
		if (!DataFetcher.isUserLogin()) {
			// 提示用户未登录
			snapLoginDialog();
			// NewToast.makeToast(context, R.string.gift_please_login, 0).show();
			return;
		}
		startAnima();
		if (BaseApplication.userInfo != null) {
			// BaseApplication.userInfo.getIntegral() >= info.getIntegral()
			if (BaseApplication.userInfo.getIntegral() >= info.getIntegral()) {
				// 用户id
				int userId = DataFetcher.getUserIdInt();
				// 商品id
				String goodsId = info.getGoodsId();
				// 兑换的数量
				final int count = 1;

				// 构造请求参数
				GoodsExchangeReq reqInfo = new GoodsExchangeReq();
				reqInfo.setUserId(userId);
				reqInfo.setGoodsId(goodsId);
				reqInfo.setCount(count);

				ReqCallback<GoodsExchangeResp> reqCallback = new ReqCallback<GoodsExchangeResp>() {
					@Override
					public void onResult(
							final TaskResult<GoodsExchangeResp> taskResult) {
						final int code = taskResult.getCode();
						alog.info("taskResult code:" + code);
						if (code == TaskResult.OK) {
							goodsExchangeResp = taskResult.getData();
							if (info != null) {
								// 商品剩余数量减1
								if(info.getSurplus() >= 1){
									info.setSurplus(info.getSurplus()-1);
									
									GoodsInfo goodsInfo = DaoHelper.getGoodsInfo(BaseApplication.getContext(), info.getGoodsId());
									if(goodsInfo!=null){
										goodsInfo.setSurplus(info.getSurplus());
										goodsInfo.update();
									}
								}
								
								Message msg = Message.obtain();
								msg.what = SUCCESS;
								msg.obj = count;
								mHandler.sendMessage(msg);
							} else {
								sendMessage(taskResult);
								
							}
						} else {
							sendMessage(taskResult);
						}

					}

				};
				DataFetcher.exchangeGoods(context, reqCallback, reqInfo, false)
						.request(context);
			} else {
				NewToast.makeToast(context, R.string.integral_not_enough, 0).show();
				stopAnima();
			}
		}

	}

	private void sendMessage(TaskResult<GoodsExchangeResp> taskResult) {
		Message msg = Message.obtain();
		msg.what = FAILED;
		msg.obj = taskResult.getMsg();
		mHandler.sendMessage(msg);
	}
	
	private void snapLoginDialog() {
		CommonProgressDialog mDialog = new CommonProgressDialog.Builder(context)
				.setMessage(R.string.login_or_out)
				.setPositiveButton(R.string.ok_login,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(context,MineAccountManagerActivity.class);
								context.startActivityForResult(intent, 1);
							}

						}).setNegativeButton(R.string.cancle_login, null)
				.create();
		mDialog.setParams(mDialog);
		mDialog.show();

	}
	
	private void snapEXchangeDialog() {
		CommonProgressDialog mDialog = new CommonProgressDialog.Builder(context)
				.setMessage(R.string.exchange_or_out)
				.setPositiveButton(R.string.ok_login,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								/*Intent intent = new Intent(context,MineAccountManagerActivity.class);
								context.startActivity(intent);*/
								loadExchangeData();
							}

						}).setNegativeButton(R.string.cancle_login, null)
				.create();
		mDialog.setParams(mDialog);
		mDialog.show();

	}

	private String getQRCode(GoodsExchangeResp exchangeInfo, Integer count) {
		String str = "http://61.145.164.151/atetinterface/goodsOrder_toAddAddress_address.action?"
				+ "userId="
				+ exchangeInfo.getUserId()
				+ "&goodsId="
				+ info.getGoodsId()
				+ "&tradeNo="
				+ exchangeInfo.getOrderId()
				+ "&count=" + count + "&tradeModel=" + "1";
		return str;
	}
}
