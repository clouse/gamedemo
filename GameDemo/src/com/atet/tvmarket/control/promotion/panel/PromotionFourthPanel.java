package com.atet.tvmarket.control.promotion.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
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
public class PromotionFourthPanel extends RelativeLayout{
	
	private RelativeLayout item4_top;
	private RelativeLayout item4_top_content;
	private BaseImageView item4_top_iv;
	private BaseImageView item4_top_shadow;
	private CloseAcceTextView item4_top_name;
	private RelativeLayout item4_below;
	private RelativeLayout item4_below_content;
	private BaseImageView item4_below_iv;
	private BaseImageView item4_below_shadow;
	private CloseAcceTextView item4_below_name;
	
	
	public PromotionFourthPanel(Context context) {
		this(context,null);
		
	}

	public PromotionFourthPanel(Context context, AttributeSet attrs) {
		this(context,attrs,0);
		
	}

	public PromotionFourthPanel(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setChildrenDrawingOrderEnabled(true);
		View view = LayoutInflater.from(context).inflate(R.layout.promotion_item4,this,true);
		ScaleViewUtils.init(UIUtils.getContext());
		ScaleViewUtils.scaleView(view);
		initView();
	}
	
	private void initView() {
		item4_top = (RelativeLayout) findViewById(R.id.item4_top);
		item4_top_iv = (BaseImageView) findViewById(R.id.item4_top_iv);
		item4_top_shadow = (BaseImageView) findViewById(R.id.item4_top_shadow);
		item4_top_content = (RelativeLayout) findViewById(R.id.item4_top_content);
		
		
		item4_below = (RelativeLayout) findViewById(R.id.item4_below);
		item4_below_iv = (BaseImageView) findViewById(R.id.item4_below_iv);
		item4_below_shadow = (BaseImageView) findViewById(R.id.item4_below_shadow);
		item4_below_content = (RelativeLayout) findViewById(R.id.item4_below_content);
		
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

	public RelativeLayout getItem4_top() {
		return item4_top;
	}

	public void setItem4_top(RelativeLayout item4_top) {
		this.item4_top = item4_top;
	}

	public RelativeLayout getItem4_top_content() {
		return item4_top_content;
	}

	public void setItem4_top_content(RelativeLayout item4_top_content) {
		this.item4_top_content = item4_top_content;
	}

	public BaseImageView getItem4_top_iv() {
		return item4_top_iv;
	}

	public void setItem4_top_iv(BaseImageView item4_top_iv) {
		this.item4_top_iv = item4_top_iv;
	}

	public BaseImageView getItem4_top_shadow() {
		return item4_top_shadow;
	}

	public void setItem4_top_shadow(BaseImageView item4_top_shadow) {
		this.item4_top_shadow = item4_top_shadow;
	}

	public CloseAcceTextView getItem4_top_name() {
		return item4_top_name;
	}

	public void setItem4_top_name(CloseAcceTextView item4_top_name) {
		this.item4_top_name = item4_top_name;
	}

	public RelativeLayout getItem4_below() {
		return item4_below;
	}

	public void setItem4_below(RelativeLayout item4_below) {
		this.item4_below = item4_below;
	}

	public RelativeLayout getItem4_below_content() {
		return item4_below_content;
	}

	public void setItem4_below_content(RelativeLayout item4_below_content) {
		this.item4_below_content = item4_below_content;
	}

	public BaseImageView getItem4_below_iv() {
		return item4_below_iv;
	}

	public void setItem4_below_iv(BaseImageView item4_below_iv) {
		this.item4_below_iv = item4_below_iv;
	}

	public BaseImageView getItem4_below_shadow() {
		return item4_below_shadow;
	}

	public void setItem4_below_shadow(BaseImageView item4_below_shadow) {
		this.item4_below_shadow = item4_below_shadow;
	}

	public CloseAcceTextView getItem4_below_name() {
		return item4_below_name;
	}

	public void setItem4_below_name(CloseAcceTextView item4_below_name) {
		this.item4_below_name = item4_below_name;
	}
	
	

}
