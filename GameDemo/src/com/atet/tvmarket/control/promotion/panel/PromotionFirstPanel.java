package com.atet.tvmarket.control.promotion.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.atet.tvmarket.R;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UIUtils;
import com.atet.tvmarket.view.BaseImageView;
import com.atet.tvmarket.view.CloseAcceTextView;

/**
 * @description: 通过容器将布局包裹起来
 *
 * @author: LiJie
 * @date: 2015年6月12日 上午9:51:26 
 */
public class PromotionFirstPanel extends RelativeLayout{
	
	public RelativeLayout promotion_area;
	private RelativeLayout promotion_area_content;
	private BaseImageView promotion_area_iv;
	private BaseImageView promotion_area_shadow;
	private BaseImageView promotion_area_new;
	private CloseAcceTextView promotion_area_name;
	private RelativeLayout integral_exchange;
	private RelativeLayout integral_exchange_content;
	private BaseImageView integral_exchange_iv;
	private BaseImageView integral_exchange_shadow;
	private BaseImageView integral_exchange_new;
	private CloseAcceTextView integral_exchange_name;
	
	
	public PromotionFirstPanel(Context context) {
		this(context,null);
		
	}

	public PromotionFirstPanel(Context context, AttributeSet attrs) {
		this(context,attrs,0);
		
	}

	public PromotionFirstPanel(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setChildrenDrawingOrderEnabled(true);
		View view = LayoutInflater.from(context).inflate(R.layout.promotion_item1,this,true);
		ScaleViewUtils.init(UIUtils.getContext());
		ScaleViewUtils.scaleView(view);
		initView();
	}
	
	private void initView() {
		promotion_area = (RelativeLayout) findViewById(R.id.promotion_area);
		promotion_area_iv = (BaseImageView) findViewById(R.id.promotion_area_iv);
		promotion_area_shadow = (BaseImageView) findViewById(R.id.promotion_area_shadow);
		promotion_area_new = (BaseImageView) findViewById(R.id.promotion_area_new);
		promotion_area_content = (RelativeLayout) findViewById(R.id.promotion_area_content);
		promotion_area_name = (CloseAcceTextView) findViewById(R.id.promotion_area_name);
		
		
		integral_exchange = (RelativeLayout) findViewById(R.id.promotion_integral_exchange);
		integral_exchange_iv = (BaseImageView) findViewById(R.id.promotion_intergral_exchange_iv);
		integral_exchange_shadow = (BaseImageView) findViewById(R.id.promotion_intergral_exchange_shadow);
		integral_exchange_new = (BaseImageView) findViewById(R.id.promotion_intergral_exchange_new);
		integral_exchange_content = (RelativeLayout) findViewById(R.id.promotion_intergral_exchange_content);
		integral_exchange_name = (CloseAcceTextView) findViewById(R.id.promotion_intergral_exchange_name);
		
	}
	
	/**
	 * 处理视图的叠加效果
	 */
	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		if(hasFocus()){
			 View child =  getFocusedChild();
			Log.i("life", child.toString());
			if (child != null){
				
				int index = indexOfChild(child);
				if (index != -1){
					
					if (i < index){
						return i;
					}else {
						return childCount - 1 - i + index;
					}
				}
			}else{
				return i;
			}
		}
		return super.getChildDrawingOrder(childCount, i);
	}

	public RelativeLayout getPromotion_area() {
		return promotion_area;
	}

	public void setPromotion_area(RelativeLayout promotion_area) {
		this.promotion_area = promotion_area;
	}

	public RelativeLayout getPromotion_area_content() {
		return promotion_area_content;
	}

	public void setPromotion_area_content(RelativeLayout promotion_area_content) {
		this.promotion_area_content = promotion_area_content;
	}

	public BaseImageView getPromotion_area_iv() {
		return promotion_area_iv;
	}

	public void setPromotion_area_iv(BaseImageView promotion_area_iv) {
		this.promotion_area_iv = promotion_area_iv;
	}

	public BaseImageView getPromotion_area_shadow() {
		return promotion_area_shadow;
	}

	public void setPromotion_area_shadow(BaseImageView promotion_area_shadow) {
		this.promotion_area_shadow = promotion_area_shadow;
	}

	public RelativeLayout getIntegral_exchange() {
		return integral_exchange;
	}

	public void setIntegral_exchange(RelativeLayout integral_exchange) {
		this.integral_exchange = integral_exchange;
	}

	public RelativeLayout getIntegral_exchange_content() {
		return integral_exchange_content;
	}

	public void setIntegral_exchange_content(
			RelativeLayout integral_exchange_content) {
		this.integral_exchange_content = integral_exchange_content;
	}

	public BaseImageView getIntegral_exchange_iv() {
		return integral_exchange_iv;
	}

	public void setIntegral_exchange_iv(BaseImageView integral_exchange_iv) {
		this.integral_exchange_iv = integral_exchange_iv;
	}

	public BaseImageView getIntegral_exchange_shadow() {
		return integral_exchange_shadow;
	}

	public void setIntegral_exchange_shadow(BaseImageView integral_exchange_shadow) {
		this.integral_exchange_shadow = integral_exchange_shadow;
	}

	public CloseAcceTextView getPromotion_area_name() {
		return promotion_area_name;
	}

	public void setPromotion_area_name(CloseAcceTextView promotion_area_name) {
		this.promotion_area_name = promotion_area_name;
	}

	public CloseAcceTextView getIntegral_exchange_name() {
		return integral_exchange_name;
	}

	public void setIntegral_exchange_name(CloseAcceTextView integral_exchange_name) {
		this.integral_exchange_name = integral_exchange_name;
	}

	public BaseImageView getPromotion_area_new() {
		return promotion_area_new;
	}

	public void setPromotion_area_new(BaseImageView promotion_area_new) {
		this.promotion_area_new = promotion_area_new;
	}

	public BaseImageView getIntegral_exchange_new() {
		return integral_exchange_new;
	}

	public void setIntegral_exchange_new(BaseImageView integral_exchange_new) {
		this.integral_exchange_new = integral_exchange_new;
	}
	
	
	
}
