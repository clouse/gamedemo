package com.atet.tvmarket.control.promotion.holder;

import java.util.List;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.atet.tvmarket.R;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.home.inf.OnRecyItemClickListener;
import com.atet.tvmarket.control.home.inf.OnkeyChangeListener;
import com.atet.tvmarket.control.promotion.panel.PromotionFirstPanel;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.DataConfig.UpdateInterface;
import com.atet.tvmarket.utils.UIUtils;


/**
 * @description: 游戏中心的第一部分的holder
 *
 * @author: Lijie
 * @date: 2015年6月12日 上午9:43:51 
 */
public class PromotionFirstHolder extends ViewHolder implements 
		OnFocusChangeListener, OnClickListener, OnKeyListener{
	
	private OnRecyItemClickListener mListener;
	public PromotionFirstPanel panel ;
	private RecyclerView recyclerView;
	private ImageFetcher imageFetcher;
	private Bitmap reflectBitmap;
	private OnkeyChangeListener keyListener;
	
	/*private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			panel.getGame_recomment_reflect().setImageBitmap(reflectBitmap);
		};
	}; */
	
	public PromotionFirstHolder(View itemView,RecyclerView recyclerView,
			OnRecyItemClickListener listener) {
		super(itemView);
		panel = (PromotionFirstPanel) itemView;
		this.mListener = listener;
		this.recyclerView = recyclerView;
		initView();
		initEvent();
	}
	
	private void initView() {
		imageFetcher = new ImageFetcher();
		imageFetcher.loadLocalImage(R.drawable.promotion_area_icon, panel.getPromotion_area_iv(), R.drawable.promotion_area_icon);
		imageFetcher.loadLocalImage(R.drawable.integral_exchange_icon, panel.getIntegral_exchange_iv(), R.drawable.integral_exchange_icon);
	}
	
	public void checkUpdate(List<String> interfaceList) {
		
		if(interfaceList.contains(UpdateInterface.GAME_ACTIVITY)){
			//活动有更新
			panel.getPromotion_area_new().setVisibility(View.VISIBLE);
			//点击后移除本地记录
		}
		
		if(interfaceList.contains(UpdateInterface.GAME_GOODS)){
			//积分兑换有更新
			panel.getIntegral_exchange_new().setVisibility(View.VISIBLE);
			//点击后移除本地记录
		
		}
		
	}
	

	private void initEvent(){
		
		panel.getPromotion_area().setOnFocusChangeListener(this);
		panel.getPromotion_area().setOnClickListener(this);
		
		panel.getIntegral_exchange().setOnFocusChangeListener(this);
		panel.getIntegral_exchange().setOnClickListener(this);
		panel.getIntegral_exchange().setOnKeyListener(this);;
		
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus){
			
			if(v.getId() == panel.getPromotion_area().getId()){
				recyclerView.smoothScrollBy(-v.getWidth() * 2, 0);
				panel.getPromotion_area_shadow().setBackgroundResource(R.drawable.white_focus);
				panel.getPromotion_area_content().setScaleX(1.11f);
				panel.getPromotion_area_content().setScaleY(1.11f);
				panel.getPromotion_area().setNextFocusUpId(R.id.tab_promotion);
				panel.getPromotion_area().setNextFocusRightId(R.id.promotion_carousel_game);
			}
			if(v.getId() == panel.getIntegral_exchange().getId()){
				panel.getIntegral_exchange_shadow().setBackgroundResource(R.drawable.white_focus);
				panel.getIntegral_exchange_content().setScaleX(1.11f);
				panel.getIntegral_exchange_content().setScaleY(1.11f);
				panel.getIntegral_exchange().setNextFocusRightId(R.id.promotion_carousel_game);
				panel.getIntegral_exchange().setNextFocusDownId(panel.getIntegral_exchange().getId());
			}
			
			if(v.isInTouchMode()){
				v.performClick();
			}
		}
		else{
			if(v.getId() == panel.getPromotion_area().getId()){
				panel.getPromotion_area_shadow().setBackgroundResource(android.R.color.transparent);
				panel.getPromotion_area_content().setScaleX(1.0f);
				panel.getPromotion_area_content().setScaleY(1.0f);
			}
			if(v.getId() == panel.getIntegral_exchange().getId()){
				panel.getIntegral_exchange_shadow().setBackgroundResource(android.R.color.transparent);
				panel.getIntegral_exchange_content().setScaleX(1.0f);
				panel.getIntegral_exchange_content().setScaleY(1.0f);
			}
		}
	}
	
	@Override
	public void onClick(View view) {
		if(mListener != null){
			mListener.onItemClick(view, getPosition());
		}
	}

	@Override
	public boolean onKey(View view, int keyCode, KeyEvent event) {
		if(event.getAction() == KeyEvent.ACTION_DOWN){
			if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
				
				return true;
			}
		}
		return false;
	}

	
}
