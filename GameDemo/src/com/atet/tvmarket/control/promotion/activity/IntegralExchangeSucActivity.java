package com.atet.tvmarket.control.promotion.activity;

import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.control.home.MainActivity;
import com.atet.tvmarket.control.mygame.utils.QRUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.ScreenShot;
import com.atet.tvmarket.view.BaseImageView;
import com.atet.tvmarket.view.CommonProgressDialog;
import com.atet.tvmarket.view.blur.BlurringView;
import com.google.zxing.WriterException;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

/*
 * File：IntegralExchangeSuccess.java
 *
 * Copyright (C) 2015 DeviceIdActivity Project
 * Date：2015年7月30日 下午8:02:14
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */
public class IntegralExchangeSucActivity extends BaseActivity {
	private static final int QRCODESIZE = 800;// 二维码的宽高
	private BaseImageView code_icon;

	private Bitmap QRBitmap;
	private String str;/*
				 * =
				 * "http://10.1.1.88:8088/atetinterface/goodsOrder_toAddAddress_address.action?"
				 * + "userId=" + 11 + "&goodsId=" + 222 + "&tradeNo=" + 322
				 * +"&count="+ 2 + "&tradeModel=" + "1"
				 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_integral_exchange_suc);
		ScaleViewUtils.init(this);
		ScaleViewUtils.scaleView(getWindow().getDecorView());
		setBlackTitle(false);

		code_icon = (BaseImageView) findViewById(R.id.integral_exchange_suc_code);
		str = getIntent().getStringExtra(Constant.QRCODE);

		try {
			if(str != null && !str.isEmpty() ){
				QRBitmap = QRUtil.createQRCode(str,(int) ScaleViewUtils.resetTextSize(QRCODESIZE));
			}
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		code_icon.setImageBitmap(QRBitmap);

	}

	@Override
	protected void onResume() {
		super.onResume();
		setTitleData();
	}
	
	@Override
	public void onBackPressed() {
		CommonProgressDialog mDialog = new CommonProgressDialog.Builder(this)
				.setMessage(R.string.exchange_goods_warn)
				.setPositiveButton(R.string.ok_login,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								IntegralExchangeSucActivity.this.finish();
							}

						}).setNegativeButton(R.string.cancle_login, null)
				.create();
		mDialog.setParams(mDialog);
		mDialog.show();
	}
	
}
