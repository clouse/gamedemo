package com.atet.tvmarket.control.promotion.holder;

import java.util.List;

import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.control.mygame.utils.QRUtil;
import com.atet.tvmarket.control.promotion.activity.PromotionDetailsActivity;
import com.atet.tvmarket.entity.dao.ActInfo;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.model.DaoHelper;
import com.atet.tvmarket.model.net.http.download.BtnDownCommonListener;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UIUtils;
import com.atet.tvmarket.view.BaseImageView;
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
public class DetailItemfirstHolder extends PromotionBaseHolder implements
		OnFocusChangeListener, OnKeyListener, OnClickListener {
	ALog alog = ALog.getLogger(DetailItemfirstHolder.class);
	
	public Button bt_download;
	public BaseImageView game_icon;
	public BaseImageView game_shadow;
	public BaseImageView game_code;
	/*public RelativeLayout rl_vedio;
	public BaseImageView vedio_icon;
	public BaseImageView vedio_shadow;*/
	public RelativeLayout detail_item2;
	private BaseImageView detail_item2_shadow;
	private TextView tv_time;
	private TextView tv_rule;
	private TextView tv_reward;
	private TextView code_caption;
	
	private PromotionDetailsActivity context;
	private RecyclerView mRecyclerView;
	private ActInfo info;
	private List<GameInfo> gameInfos;
	private BtnDownCommonListener btnDownCommonListener;
	private int code;
	private int firstClick;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			if (gameInfos != null && gameInfos.size() > 0) {
				
				
			}
			firstClick++;
			
		};
	};

	
	public DetailItemfirstHolder(View itemView,
			PromotionDetailsActivity context, RecyclerView mRecyclerView,
			ActInfo info) {
		super(itemView);
		this.mRecyclerView = mRecyclerView;
		this.context = context;
	//	this.info = info;
		initView();
	}

	@Override
	protected void initView() {
		
		bt_download = (Button) itemView.findViewById(R.id.promotion_detail_item_download);
		game_icon = (BaseImageView) itemView
				.findViewById(R.id.detail_item2_icon);
		game_shadow = (BaseImageView) itemView
				.findViewById(R.id.detail_item2_shadow);
		game_code = (BaseImageView) itemView
				.findViewById(R.id.detail_item2_code);
		/*rl_vedio = (RelativeLayout) itemView
				.findViewById(R.id.detail_item2_vedio);
		vedio_icon = (BaseImageView) itemView
				.findViewById(R.id.detail_item2_vedio_icon);
		vedio_shadow = (BaseImageView) itemView
				.findViewById(R.id.detail_item2_vedio_shadow);*/
		detail_item2 = (RelativeLayout) itemView.findViewById(R.id.promotion_rl_detail);
		detail_item2_shadow = (BaseImageView) itemView.findViewById(R.id.promotion_rl_detail_shadow);
		tv_time = (TextView) itemView.findViewById(R.id.detail_item2_time);
		tv_rule = (TextView) itemView.findViewById(R.id.detail_item2_rule);
		tv_reward = (TextView) itemView.findViewById(R.id.detail_item2_reward);
		code_caption = (TextView) itemView.findViewById(R.id.detail_item2_code_caption);

		//mImageFetcher.loadLocalImage(R.drawable.game_iv_qrcode_bg, game_code,R.drawable.game_iv_qrcode_bg);

		btnDownCommonListener = new BtnDownCommonListener(context);
		bt_download.setOnFocusChangeListener(this);
		bt_download.setOnKeyListener(this);
		bt_download.setOnClickListener(this);
		detail_item2.setOnFocusChangeListener(this);
		detail_item2.setOnKeyListener(this);
		detail_item2.setOnClickListener(this);
	
		//mRecyclerView.setOnFocusChangeListener(this);
		
	}

	public void setData(ActInfo info) {
		this.info = info;
		if (info != null) {
			mImageFetcher.loadImage(info.getErectPhoto(), game_icon,R.drawable.default_vertical);
			String startTime = UIUtils.changeTimeStyle(info.getStartTime());
			String endTime = UIUtils.changeTimeStyle(info.getEndTime());
			tv_time.setText(startTime + "-" + endTime);
			tv_rule.setText(info.getRules());
			tv_reward.setText(info.getPrize());
			code_caption.setText(info.getCaption());
			//getGameInfo(info.getGameId());
			GameInfo gameInfo = new GameInfo();
			gameInfo.setGameId(info.getGameId());
			List<GameInfo> gameInfoList = DaoHelper.getGameInfoFromGameId(BaseApplication.getContext(), info.getGameId());
			if(gameInfoList.size()>0){
				gameInfo = gameInfoList.get(0);
			}
			//数据统计来源
			gameInfo.setTypeName("GameActivity");
			btnDownCommonListener.listen(bt_download, gameInfo);
			
			Bitmap QRBitmap = null;
			try {
				if(info.getQrCode() != null && !info.getQrCode().isEmpty()){
					QRBitmap = QRUtil.createQRCode(info.getQrCode(), (int)ScaleViewUtils.resetTextSize(350));
				}else{
					game_code.setBackgroundResource(R.drawable.game_iv_qrcode_bg);
				}
			} catch (WriterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			game_code.setImageBitmap(QRBitmap);
		}
		
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		RecyclerView rootRecyclerView = (RecyclerView) mRecyclerView.getParent().getParent();

		if (hasFocus) {
			if (v.getId() == bt_download.getId()) {
				//NewToast.makeToast(context, "11111", 0).show();
				
				if (bt_download.getLeft() < 150) {
					rootRecyclerView.smoothScrollBy(-200, 0);
					mRecyclerView.smoothScrollBy(- (bt_download.getWidth()) - 40, 0);
				}
				game_shadow.setBackgroundResource(R.drawable.white_focus);
			} else if (v.getId() == detail_item2.getId()) {

				/*if (location[0] - v.getWidth() * 1.1 < 0) {
					mRecyclerView.smoothScrollBy(-(int)(v.getWidth() * 1.5), 0);
				}*/
				
				detail_item2.setNextFocusRightId(R.id.detail_item2_rl_top);
				rootRecyclerView.smoothScrollToPosition(1);
				detail_item2_shadow.setBackgroundResource(R.drawable.white_focus);
			}
		} else {
			if (v.getId() == bt_download.getId()) {
				game_shadow.setBackgroundResource(android.R.color.transparent);
			} else if (v.getId() == detail_item2.getId()) {
				detail_item2_shadow.setBackgroundResource(android.R.color.transparent);
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

			if (v.getId() == bt_download.getId()
					&& keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
				((RecyclerView) mRecyclerView.getParent().getParent()).smoothScrollToPosition(1);
				detail_item2.requestFocus();
				return true;
			}
		}

		return false;
	}

	@Override
	public void onClick(View v) {
		if (info != null) {
			if(v.getId() == bt_download.getId()){
				if(code == TaskResult.NO_DATA){
					NewToast.makeToast(context,R.string.no_data , 0).show();
				}else if(code == TaskResult.HTTP_NO_CONNECTION){
					NewToast.makeToast(context,R.string.http_no_connect , 0).show();
				}else{
					NewToast.makeToast(context,R.string.net_failed , 0).show();
				}
			}
		}

	}
	
	/**
	 * 
	 * @description: 回收监听
	 *
	 * @return 
	 * @throws: 
	 * @author: LiJie
	 * @date: 2015年8月26日 下午4:06:26
	 */
	public BtnDownCommonListener getListener(){
		return btnDownCommonListener;
	}
	
}
