package com.atet.tvmarket.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;



/**
 * 
 * @ClassName:  CloseAcceTextView   
 * @Description:TODO(关闭硬件加速的textview)   
 * @author wenfuqiang
 * @date:   2014-11-29 上午11:00:58
 */
public class CloseAcceTextView extends TextView{

	public CloseAcceTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		CloseHardwareAcceleration();
	}

	public CloseAcceTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		CloseHardwareAcceleration();
	}

	public CloseAcceTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		CloseHardwareAcceleration();
	}
	
	/**
	 * 
	 * @Title: CloseHardwareAcceleration   
	 * @Description: TODO(关闭硬件加速)   
	 * @param:       
	 * @return: void      
	 * @throws
	 */
	private void CloseHardwareAcceleration(){
		setLayerType(View.LAYER_TYPE_HARDWARE, null);
	}

}
