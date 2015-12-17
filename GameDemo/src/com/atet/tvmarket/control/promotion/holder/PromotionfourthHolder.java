package com.atet.tvmarket.control.promotion.holder;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.home.MainActivity;
import com.atet.tvmarket.control.home.inf.OnRecyItemClickListener;
import com.atet.tvmarket.control.home.inf.OnkeyChangeListener;
import com.atet.tvmarket.control.promotion.activity.PromotionDetailsActivity;
import com.atet.tvmarket.control.promotion.panel.PromotionFourthPanel;
import com.atet.tvmarket.entity.dao.ActInfo;
import com.atet.tvmarket.utils.UIUtils;


/**
 * @description: 活动中心的第四部分的holder
 *
 * @author: Lijie
 * @date: 2015年6月12日 上午9:43:51 
 */
public class PromotionfourthHolder extends ViewHolder implements 
		OnFocusChangeListener, OnClickListener, OnKeyListener{
	ALog alog = ALog.getLogger(PromotionfourthHolder.class);
	private OnRecyItemClickListener mListener;
	public PromotionFourthPanel panel ;
	private RecyclerView recyclerView;
	private ImageFetcher imageFetcher;
	private Context context;
	private int position;
	private List<ActInfo> actInfos = new ArrayList<ActInfo>();
	
	public PromotionfourthHolder(View itemView,RecyclerView recyclerView,
			OnRecyItemClickListener listener,Context context) {
		super(itemView);
		this.context = context;
		this.mListener = listener;
		this.recyclerView = recyclerView;
		initView();
		initEvent();
	}
	
	private void initView() {
		panel = (PromotionFourthPanel) itemView;
		imageFetcher = new ImageFetcher();
	}

	public void setData(List<ActInfo> actInfos, int position) {
		this.position = position;
		this.actInfos = actInfos;
		ActInfo topInfo = actInfos.get(position-4);
		imageFetcher.loadImage(topInfo.getSquarePhoto(), panel.getItem4_top_iv(), R.drawable.default_square);
		
		if(actInfos.size() > position-3){
			ActInfo bellowInfo = actInfos.get(position - 3);
			imageFetcher.loadImage(bellowInfo.getSquarePhoto(), panel.getItem4_below_iv(), R.drawable.default_square);
		}else {
			panel.getItem4_below().setClickable(false);
			panel.getItem4_below().setFocusable(false);
			panel.getItem4_below().setFocusableInTouchMode(false);
			//panel.getItem4_below().setVisibility(View.GONE);
		}
		
	}
	
	private void initEvent(){
		
		panel.getItem4_top().setOnFocusChangeListener(this);
		panel.getItem4_top().setOnClickListener(this);
		panel.getItem4_top().setOnKeyListener(this);
		
		panel.getItem4_below().setOnFocusChangeListener(this);
		panel.getItem4_below().setOnClickListener(this);
		panel.getItem4_below().setOnKeyListener(this);
		
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus){
			
			int[] location = new int[2];
			v.getLocationOnScreen(location);

			if (location[0] + v.getWidth() *1.5 > recyclerView.getRight()) {
				recyclerView.smoothScrollBy(v.getWidth(), 0);
			}
			
			if(location[0] - v.getWidth() < recyclerView.getLeft()){
				recyclerView.smoothScrollBy(-v.getWidth(), 0);
			}
			if(v.getId() == panel.getItem4_top().getId()){
				panel.getItem4_top_shadow().setBackgroundResource(R.drawable.white_focus);
				panel.getItem4_top_content().setScaleX(1.1f);
				panel.getItem4_top_content().setScaleY(1.1f);
			}
			if(v.getId() == panel.getItem4_below().getId()){
				if(v.isShown() && v.isFocusable()){
					panel.getItem4_below_shadow().setBackgroundResource(R.drawable.white_focus);
					panel.getItem4_below_content().setScaleX(1.1f);
					panel.getItem4_below_content().setScaleY(1.1f);
					panel.getItem4_below().setNextFocusDownId(panel.getItem4_below().getId());
				}
				
			}
			
			if(v.isInTouchMode()){
				v.performClick();
			}
		}
		else{
			if(v.getId() == panel.getItem4_top().getId()){
				panel.getItem4_top_shadow().setBackgroundResource(android.R.color.transparent);
				panel.getItem4_top_content().setScaleX(1.0f);
				panel.getItem4_top_content().setScaleY(1.0f);
			}
			if(v.getId() == panel.getItem4_below().getId()){
				panel.getItem4_below_shadow().setBackgroundResource(android.R.color.transparent);
				panel.getItem4_below_content().setScaleX(1.0f);
				panel.getItem4_below_content().setScaleY(1.0f);
			}
		}
	}
	
	@Override
	public void onClick(View view) {
		if(view.isShown() && view.isFocusable()){
			Intent intent = new Intent(context,PromotionDetailsActivity.class);
			Bundle bundle = new Bundle();
			if(view.getId() == panel.getItem4_top().getId()){
				bundle.putSerializable(Constant.ACTINFO,actInfos.get(position - 4));
			}else{
				bundle.putSerializable(Constant.ACTINFO,actInfos.get(position - 3));
			}
			intent.putExtras(bundle);
			context.startActivity(intent);
		}
	}
	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		int action = event.getAction();
		if(v.getId() == panel.getItem4_top().getId()){
			if (action == KeyEvent.ACTION_DOWN)
				if(keyCode == KeyEvent.KEYCODE_DPAD_UP){
					((MainActivity) context).forceFocusTab(MainActivity.TAB_ID_PROMOTION,0);
					((MainActivity) context).setPromotionFcous(recyclerView.getChildAt(0));
					alog.info("导航条获取焦点了");
					return true;
				}else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
					if(panel.getItem4_below().isShown() && panel.getItem4_below().isFocusable()){
						panel.getItem4_below().requestFocus();
						return true;
					}
						return true;
				}
		}else{
			if (action == KeyEvent.ACTION_DOWN)
				if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
					return true;
				}
		}
		return false;
	}

}
