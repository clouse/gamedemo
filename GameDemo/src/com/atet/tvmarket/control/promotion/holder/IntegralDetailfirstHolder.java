package com.atet.tvmarket.control.promotion.holder;

import android.graphics.Bitmap;
import android.os.Message;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.entity.dao.GoodsInfo;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.view.LoadingView;

/*
 * File：PromotionDetailfirstHolder.java
 *
 * Copyright (C) 2015 MainActivity Project
 * Date：2015年7月8日 上午11:07:29
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn 
 *
 */
public class IntegralDetailfirstHolder extends ViewHolder implements
		OnFocusChangeListener {
	ALog alog = ALog.getLogger(IntegralDetailfirstHolder.class);
	public WebView webView;
	private LoadingView loadingView;
	private Message msg;
	private LinearLayout ll_loading;

	public IntegralDetailfirstHolder(View itemView) {
		super(itemView);
		initView();
	}

	private void initView() {
		ScaleViewUtils.scaleView(itemView);
		webView = (WebView) itemView.findViewById(R.id.integral_detail_webview);
		ll_loading = (LinearLayout) itemView.findViewById(R.id.integral_first_loading);
		loadingView = (LoadingView) itemView.findViewById(R.id.integral_detail_Loading);
		
		//loadingView.setDataView(webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportMultipleWindows(true);
		webView.setOnFocusChangeListener(this);
	}
	
	public void setData(GoodsInfo info) {
		webView.loadUrl(info.getUrl());
		alog.info("url = " + info.getUrl());
	}

	class MyWebViewClient extends WebViewClient {

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			  super.onPageFinished(view, failingUrl);
			//ll_loading.setVisibility(View.INVISIBLE);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			/*msg = Message.obtain();
			msg.what = Constant.SHOWLOADING;
			loadingView.getmHandler().sendMessage(msg);*/
			
			ll_loading.setVisibility(View.VISIBLE);
			webView.setVisibility(View.INVISIBLE);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			/*msg = Message.obtain();
			msg.what = Constant.DISMISSLOADING;
			loadingView.getmHandler().sendMessage(msg);*/
			//ll_loading.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			IntegralDetailSecondHolder.isLeft = true;
		}

	}

}
