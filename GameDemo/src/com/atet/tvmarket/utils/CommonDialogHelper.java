package com.atet.tvmarket.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.atet.tvmarket.R;

/**
 * @author zhaominglai
 * @description 通用对话框辅助类
 * */
public class CommonDialogHelper{

	private String mtitle;//标题字符串
	private String msg;//提示信息字符串
	private static Button mbtncancle,mbtnok;//取消与确定按钮
	private static TextView mtvTitle,mtvMsg;
	private static Dialog mdialog;
	private static View rootview;
	private static Callbacks mcall;


	/**
	 * 定义按钮点击事件的接口
	 * */
	public interface Callbacks{
		public void clickCancle();
		public void clickOk();
	};
	
	/**
	 * @author zhaominglai
	 * @description 对外接口，用来创建并显示一个Dialog
	 * @param activity表示要在哪个Activity中显示，title为标题信息，msg为提示信息，caller为对话框点击事件回调接口
	 * @return 返回dialog的对象
	 * @date 2014-4-1
	 * */
	public static Dialog openDialog(Activity activity,String title,String msg,Callbacks caller)
	{
		mdialog = new Dialog(activity);//创建一个dialog
		mcall = caller;
	     
		mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//设置dialog的标题为无标题栏
		mdialog.show();
		rootview = activity.getLayoutInflater().inflate(R.layout.dialog_confirm, null);//加载dialog的布局文件
	    mdialog.setContentView(rootview);
	    ScaleViewUtils.scaleView(rootview);
		
	     mbtncancle = (Button) rootview.findViewById(R.id.btn_confirm_cancle);
	     mbtnok = (Button) rootview.findViewById(R.id.btn_confirm_ok);
	     
	     mtvTitle = (TextView) rootview.findViewById(R.id.tv_dialog_title);
	     mtvMsg = (TextView) rootview.findViewById(R.id.tv_desc);
	     
	     mtvTitle.setText(title);
	     mtvMsg.setText(msg);
	     
	     
	     mbtncancle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clickCancle(mcall);//取消事件
			}
		});
	     
	     mbtnok.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					clickOk(mcall);//确定事件
				}
			});
	     mbtncancle.setOnTouchListener(mOnTouchListener);
	     mbtncancle.setOnFocusChangeListener(mOnTouchFocusChangeListener);
	     mbtnok.setOnTouchListener(mOnTouchListener);
	     mbtnok.setOnFocusChangeListener(mOnTouchFocusChangeListener);
		return mdialog;
	}
	
	private static View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				OnFocusChangeListener listener = v.getOnFocusChangeListener();
			    if (listener != null
					&& listener instanceof OnTouchFocusChangeListener) {
					((OnTouchFocusChangeListener) listener).setView(v);
				}
			}
			return false;
		}
	};
	
	
	/** 用于监听焦点变化时的动作 */
	private static OnTouchFocusChangeListener mOnTouchFocusChangeListener = new OnTouchFocusChangeListener();

	public static class OnTouchFocusChangeListener implements View.OnFocusChangeListener {
		private View view;

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					if (v!=null) {
						if(v.isInTouchMode() && v == view){
							v.performClick();
						}
					}
				}
		}

		public View getView() {
			return view;
		}

		public void setView(View view) {
			this.view = view;
		}
		
	}
	
	
	public static Dialog openDialog2(Context activity,String title,String msg,Callbacks caller)
	{
		mdialog = new Dialog(activity);//创建一个dialog
		mcall = caller;
	     
		mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//设置dialog的标题为无标题栏
		mdialog.show();
		rootview = ((Activity) activity).getLayoutInflater().inflate(R.layout.dialog_confirm, null);//加载dialog的布局文件
	    mdialog.setContentView(rootview);
		Window mwindow = mdialog.getWindow();//获取dialog的window对象
		mwindow.setBackgroundDrawable(new BitmapDrawable());//设置dialog的背景为透明
		
		int width = (int) activity.getResources().getDimension(R.dimen.set_reset_confim_width);
		int height = (int) activity.getResources().getDimension(R.dimen.set_reset_confim_height);
		
		WindowManager.LayoutParams lp = mwindow.getAttributes();
	     lp.width = width;  
	     lp.height = height;
	     lp.alpha = 1.0f;
	   /*  lp.gravity = Gravity.LEFT | Gravity.TOP;*/
	     mwindow.setAttributes(lp);
	     
	     mbtncancle = (Button) rootview.findViewById(R.id.btn_confirm_cancle);
	     mbtnok = (Button) rootview.findViewById(R.id.btn_confirm_ok);
	     
	     mtvTitle = (TextView) rootview.findViewById(R.id.tv_dialog_title);
	     mtvMsg = (TextView) rootview.findViewById(R.id.tv_desc);
	     
	     mtvTitle.setText(title);
	     mtvMsg.setText(msg);
	     
	     
	     mbtncancle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clickCancle(mcall);//取消事件
			}
		});
	     
	     mbtnok.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					clickOk(mcall);//确定事件
				}
			});
	    
		return mdialog;
	}
	
	
	/**
	 * @author zhaominglai
	 * @description 对话框的点击取消事件
	 * @param callback为回调接口
	 * @date 2014-4-20
	 * */
	private static void clickCancle(Callbacks callback)
	{
		callback.clickCancle();//调用callback的点击取消事件
		mdialog.dismiss();//隐藏对话框
	}
	
	
	/**
	 * @author zhaominglai
	 * @description 对话框的点击确定事件
	 * @param callback为回调接口
	 * @date 2014-4-20
	 * */
	private static void clickOk(Callbacks callback)
	{
		callback.clickOk();//调用callback的点击确定事件
		mdialog.dismiss();//隐藏对话框
	}
}