package com.atet.tvmarket.control.promotion.holder;

import android.graphics.Bitmap;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.promotion.activity.GiftActivity;
import com.atet.tvmarket.entity.dao.ActInfo;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UIUtils;
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
public class PromotionDetailfirstHolder extends ViewHolder implements 
			OnFocusChangeListener, OnKeyListener {
	ALog alog = ALog.getLogger(PromotionDetailfirstHolder.class);
	public WebView webView;
	private LoadingView loadingView;
	private RecyclerView root_RecyclerView;
	private Message msg;

	public PromotionDetailfirstHolder(View itemView,RecyclerView root_RecyclerView) {
		super(itemView);
		this.root_RecyclerView = root_RecyclerView;
		initView();
	}

	private void initView() {
		ScaleViewUtils.scaleView(itemView);	
		webView = (WebView) itemView.findViewById(R.id.promotiom_item1_webview);
		loadingView = (LoadingView) itemView.findViewById(R.id.promotion_detail_Loading);
		
		loadingView.setDataView(webView);
		
		webView.getSettings().setJavaScriptEnabled(true);
	
		webView.setOnKeyListener(this);
		webView.setOnFocusChangeListener(this);
		webView.setWebViewClient(new MyWebViewClient());
		
	}
	

	public void setData(ActInfo info) {
		if(info != null){
			webView.loadUrl(info.getUrl());
		}
	}
	
	class MyWebViewClient extends WebViewClient{
		
		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			
		}
		
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			/*msg = Message.obtain();
			msg.what = Constant.SHOWLOADING;
			loadingView.getmHandler().sendMessage(msg);*/
			
		}
		
		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			/*msg = Message.obtain();
			msg.what = Constant.DISMISSLOADING;
			loadingView.getmHandler().sendMessage(msg);
			webView.requestFocus();*/
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus){
			/*root_RecyclerView.setFocusable(false);
			root_RecyclerView.setFocusableInTouchMode(false);*/
			//webView.setNextFocusRightId(R.id.promotion_detail_item_download);
			alog.info("focus.....");
			//NewToast.makeToast(UIUtils.getContext(), "webView", 0).show();
		}else{
			
			alog.info("no focus.....");
			//NewToast.makeToast(UIUtils.getContext(), "webView no focus....", 0).show();
		}
		
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		
		/*if(event.getAction() == KeyEvent.ACTION_DOWN){
			{
				if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
					alog.info("key--------");
					return true;
				}
			}
		}*/
		return false;
	}
			
}
