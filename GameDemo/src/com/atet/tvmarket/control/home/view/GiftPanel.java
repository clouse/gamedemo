package com.atet.tvmarket.control.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.atet.tvmarket.R;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UIUtils;
import com.atet.tvmarket.view.BaseImageView;
import com.atet.tvmarket.view.CloseAcceTextView;

/*
 * File：GiftPanel.java
 *
 * Copyright (C) 2015 MainActivity Project
 * Date：2015年6月10日 下午8:44:52
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */
public class GiftPanel extends RelativeLayout {
	private RelativeLayout gift_top_content;
	public BaseImageView gift_top_iv;
	private CloseAcceTextView gift_top_name;
	public Button gift_top_bt;
	
	public GiftPanel(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}
	
	public GiftPanel(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		// TODO Auto-generated constructor stub
	}
	

	public GiftPanel(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	private void initView() {
		View view = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.gift_item, this, true);
		ScaleViewUtils.init(UIUtils.getContext());
		ScaleViewUtils.scaleView(view);
		gift_top_content = (RelativeLayout) view.findViewById(R.id.gift_top_content);
		gift_top_iv = (BaseImageView) view.findViewById(R.id.gift_top_iv);
		gift_top_name = (CloseAcceTextView) view.findViewById(R.id.gift_top_name);
		gift_top_bt = (Button) view.findViewById(R.id.gift_top_bt);
		
	}

	
	public RelativeLayout getGift_top_content() {
		return gift_top_content;
	}

	public void setGift_top_content(RelativeLayout gift_top_content) {
		this.gift_top_content = gift_top_content;
	}

	public BaseImageView getGift_top_iv() {
		return gift_top_iv;
	}

	public void setGift_top_iv(BaseImageView gift_top_iv) {
		this.gift_top_iv = gift_top_iv;
	}
	
	public CloseAcceTextView getGift_top_name() {
		return gift_top_name;
	}

	public void setGift_top_name(CloseAcceTextView gift_top_name) {
		this.gift_top_name = gift_top_name;
	}
	
	public Button getGift_top_bt() {
		return gift_top_bt;
	}

	public void setGift_top_bt(Button gift_top_bt) {
		this.gift_top_bt = gift_top_bt;
	}

	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		if(hasFocus()){
			 View child =  getFocusedChild();
			Log.i("life", child.toString());
			if (child != null){
				
				int index = indexOfChild(child);
				/*Log.i("life", "childCount:"+childCount);
				Log.i("life", "i:"+i);
				Log.i("life", "index:"+index);*/
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
}
