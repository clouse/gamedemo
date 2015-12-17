
package com.atet.tvmarket.control.promotion.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UIUtils;
import com.atet.tvmarket.view.BaseImageView;
import com.atet.tvmarket.view.CloseAcceTextView;
import com.atet.tvmarket.view.NewToast;

/*
 * File：GifeReceivedSucActivity.java
 *
 * Copyright (C) 2015 MainActivity Project
 * Date：2015年7月2日 下午2:30:39
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */
public class GiftReceivedSucActivity extends BaseActivity implements OnClickListener {
	private BaseImageView suc_icon;
	private Button bt_copy;
	private CloseAcceTextView tv_code;
	private CloseAcceTextView tv_name;
	private ClipboardManager clip;
	private String giftCode;
	private String giftName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		setContentView(R.layout.activity_gift_received_suc);
		ScaleViewUtils.init(this);
		ScaleViewUtils.scaleView(getWindow().getDecorView());
		
		suc_icon = (BaseImageView) findViewById(R.id.gift_received_suc_icon);
		bt_copy = (Button) findViewById(R.id.gift_received_suc_copy);
		tv_code = (CloseAcceTextView) findViewById(R.id.gift_received_suc_code);
		tv_name = (CloseAcceTextView) findViewById(R.id.gift_received_suc_name);
		
		mImageFetcher.loadLocalImage(R.drawable.gift_received_suc_tag, suc_icon, R.drawable.gift_received_suc_tag);
		bt_copy.requestFocus();
		bt_copy.setOnClickListener(this);
		
		giftCode = getIntent().getStringExtra(Constant.GIFTCODE);
		giftName = getIntent().getStringExtra(Constant.GIFTNAME);
		tv_code.setText(UIUtils.getString(R.string.gift_code)+ giftCode);
		tv_name.setText(giftName);
	}

	@Override
	public void onClick(View arg0) {
		clip = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
		clip.setText(giftCode); // 复制
		NewToast.makeToast(getApplicationContext(), UIUtils.getString(R.string.gift_code) + giftCode, 0).show();
	}
}


