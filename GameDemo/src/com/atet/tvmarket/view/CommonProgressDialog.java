package com.atet.tvmarket.view;

import android.view.*;
import com.atet.tvmarket.R;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.utils.AppUtil;
import com.atet.tvmarket.utils.GamepadTool;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.ScreenShot;
import com.atet.tvmarket.utils.StringTool;
import com.atet.tvmarket.view.blur.BlurringView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CommonProgressDialog extends Dialog{
	private Context context;
	public CommonProgressDialog(Context context) {
		// TODO Auto-generated constructor stub
		 super(context);
		 this.context = context;
		 setOwnerActivity((Activity)context);
		 setOnKeyListener(mOnDialogKeyListener);
	}



	public CommonProgressDialog(Context context, int theme) {
		 super(context, R.style.Dialog);
		 this.context = context;
		 setOwnerActivity((Activity)context);
		// TODO Auto-generated constructor stub
		 setOnKeyListener(mOnDialogKeyListener);
	}


	public static class Builder {
		   
	        private Context context;  
	        private String title;  
	        private String message;  
	        private String positiveButtonText;  
	        private String negativeButtonText;
	        private String centerButtonText;
	        private int iconId = -1;
	        private String iconUrl;
	        private View contentView;  
	    	
	        private DialogInterface.OnClickListener   
	                        positiveButtonClickListener,  
	                        negativeButtonClickListener,
	                        centerButtonClickListener;
	        
	        public Builder(Context context) {  
	            this.context = context;  
	        }  
	        /** 
	         * Set the Dialog message from String 
	         * @param title 
	         * @return 
	         */  
	        public Builder setMessage(String message) {  
	            this.message = message;  
	            return this;  
	        }  
	   
	        /** 
	         * Set the Dialog message from resource 
	         * @param title 
	         * @return 
	         */  
	        public Builder setMessage(int message) {  
	            this.message = (String) context.getText(message);  
	            return this;  
	        }  
	   
	        /** 
	         * Set the Dialog title from resource 
	         * @param title 
	         * @return 
	         */  
	        public Builder setTitle(int title) {  
	            this.title = (String) context.getText(title);  
	            return this;  
	        }  
	   
	        /** 
	         * Set the Dialog title from String 
	         * @param title 
	         * @return 
	         */  
	        public Builder setTitle(String title) {  
	            this.title = title;  
	            return this;  
	        }  
	   
	        /** 
	         * Set a custom content view for the Dialog. 
	         * If a message is set, the contentView is not 
	         * added to the Dialog... 
	         * @param v 
	         * @return 
	         */  
	        public Builder setContentView(View v) {  
	            this.contentView = v;  
	            return this;  
	        }  
	   
			public Builder setIcon(int iconId) {
				this.iconId = iconId;
				return this;
			}
			
			public Builder setIcon(String url){
				this.iconUrl = url;
				return this;
			}
			/** 
	         *设置PostiveButton点击事件以及显示文字 
	         * @param positiveButtonText 
	         * @param listener 
	         * @return 
	         */  
	        public Builder setPositiveButton(int positiveButtonText,  
	                DialogInterface.OnClickListener listener) {  
	            this.positiveButtonText = (String) context  
	                    .getText(positiveButtonText);  
	            this.positiveButtonClickListener = listener;  
	            return this;  
	        }  
	   
	        /** 
	         * 设置PostiveButton点击事件以及显示文字 
	         * @param positiveButtonText 
	         * @param listener 
	         * @return 
	         */  
	        public Builder setPositiveButton(String positiveButtonText,  
	                DialogInterface.OnClickListener listener) {  
	            this.positiveButtonText = positiveButtonText;  
	            this.positiveButtonClickListener = listener;  
	            return this;  
	        }  
	   
	        /** 
	         * 设置NegativeButton点击事件以及显示文字 
	         * @param negativeButtonText 
	         * @param listener 
	         * @return 
	         */  
	        public Builder setNegativeButton(int negativeButtonText,  
	                DialogInterface.OnClickListener listener) {  
	            this.negativeButtonText = (String) context  
	                    .getText(negativeButtonText);  
	            this.negativeButtonClickListener = listener;  
	            return this;  
	        }  
	   
	        /** 
	         * 设置NegativeButton点击事件以及显示文字
	         * @param negativeButtonText 
	         * @param listener 
	         * @return 
	         */  
	        public Builder setNegativeButton(String negativeButtonText,  
	                DialogInterface.OnClickListener listener) {  
	            this.negativeButtonText = negativeButtonText;  
	            this.negativeButtonClickListener = listener;  
	            return this;  
	        }  
	        
	        /** 
	         * 设置centerButton点击事件以及显示文字 
	         * @param centerButtonText 
	         * @param listener 
	         * @return 
	         */  
	        public Builder setCenterButton(int centerButtonText,  
	                DialogInterface.OnClickListener listener) {  
	            this.centerButtonText = (String) context  
	                    .getText(centerButtonText);  
	            this.centerButtonClickListener = listener;  
	            return this;  
	        }  
	   
	        /** 
	         * 设置centerButton点击事件以及显示文字
	         * @param negativeButtonText 
	         * @param listener 
	         * @return 
	         */  
	        public Builder setCenterButton(String centerButtonText,  
	                DialogInterface.OnClickListener listener) {  
	            this.centerButtonText = centerButtonText;  
	            this.centerButtonClickListener = listener;  
	            return this;  
	        }
	        
	    	public void setAlapha(CommonProgressDialog dialog){
	    		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
	    		lp.alpha=0.9f;
	    		dialog.getWindow().setAttributes(lp);               
	    	}
	    	
			/** 
	         * 创建对话框
	         */  
	        public CommonProgressDialog create(ImageFetcher imageFetcher) {
//				Bitmap b = ScreenShot.takeScreenShot((Activity)context);
//				ScreenShot.screenShotBmp = b;
				
	            LayoutInflater inflater = (LayoutInflater) context  
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);   
	            final CommonProgressDialog dialog = new CommonProgressDialog(context,   
	                    R.style.Dialog);
	            View layout = inflater.inflate(R.layout.common_dialog_layout, null);
	        	View backgroundView = layout.findViewById(R.id.dialog_view);
//	    		if(ScreenShot.screenShotBmp!=null){
//	    			backgroundView.setBackgroundDrawable(new BitmapDrawable(ScreenShot.screenShotBmp));
//	    		}
				backgroundView.setBackgroundResource(R.drawable.classify_bg2);
//	    		BlurringView blurringView = (BlurringView)layout.findViewById(R.id.dialog_blurring_view);
//	    		blurringView.setBlurredView(backgroundView);
	            // set the dialog title  
	            ((TextView) layout.findViewById(R.id.title)).setText(title);
	             ImageView imageView = (ImageView) layout.findViewById(R.id.icon);
	            if(iconId == -1){

	            }else{
	            	imageView.setVisibility(View.VISIBLE);
	                imageView.setImageResource(iconId);
	            }
	            if(!StringTool.isEmpty(iconUrl)){
	               	imageView.setVisibility(View.VISIBLE);
	            	imageFetcher.loadImage(iconUrl, imageView, 10, 0);
	            }
	            // set the confirm button  
	            if (positiveButtonText != null) {

	                ((Button) layout.findViewById(R.id.positiveButton)).setText(positiveButtonText);

					// 设置事件监听
					setDialogEventListener(layout.findViewById(R.id.positiveButton),
							new DialogEventListener(dialog, positiveButtonClickListener));
	            } else {  
	                // if no confirm button just set the visibility to GONE  
	                layout.findViewById(R.id.positiveButton).setVisibility(  
	                        View.GONE);  
	            }  
	            // set the cancel button  
	            if (negativeButtonText != null) {

	                ((Button) layout.findViewById(R.id.negativeButton)).setText(negativeButtonText);

					// 设置事件监听
					setDialogEventListener(layout.findViewById(R.id.negativeButton),
							new DialogEventListener(dialog, negativeButtonClickListener));
	            } else {  
	                // if no confirm button just set the visibility to GONE  
	                layout.findViewById(R.id.negativeButton).setVisibility(  
	                        View.GONE);  
	            }  
	            
	            if (centerButtonText != null) {

	                ((Button) layout.findViewById(R.id.centerButton)).setText(centerButtonText);

					// 设置事件监听
					setDialogEventListener(layout.findViewById(R.id.centerButton),
							new DialogEventListener(dialog, centerButtonClickListener));
	            } else {  
	                // if no confirm button just set the visibility to GONE  
	                layout.findViewById(R.id.centerButton).setVisibility(  
	                        View.GONE);  
	            }  
	            
	            // set the content message  
	            if (message != null) {  
	                ((TextView) layout.findViewById(  
	                        R.id.message)).setText(message);  
	            } else if (contentView != null) {  
	            	
	            }
	            ScaleViewUtils.scaleView(layout);
	            dialog.setContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	            return dialog;  
	        }

	        /**
	         * 创建对话框
	         */  
	        public CommonProgressDialog create() {  
	            LayoutInflater inflater = (LayoutInflater) context  
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);   
	            final CommonProgressDialog dialog = new CommonProgressDialog(context,   
	                    R.style.Dialog);
//				Bitmap b = ScreenShot.takeScreenShot((Activity)context);
//				ScreenShot.screenShotBmp = b;
	            View layout = inflater.inflate(R.layout.common_dialog_layout, null);
	        	View backgroundView = layout.findViewById(R.id.dialog_view);
//	    		if(ScreenShot.screenShotBmp!=null){
//	    			backgroundView.setBackgroundDrawable(new BitmapDrawable(ScreenShot.screenShotBmp));
//	    		}
				backgroundView.setBackgroundResource(R.drawable.classify_bg2);
//	    		BlurringView blurringView = (BlurringView)layout.findViewById(R.id.dialog_blurring_view);
//	    		blurringView.setBlurredView(backgroundView);
	            // set the dialog title  
	            ((TextView) layout.findViewById(R.id.title)).setText(title);
	             ImageView imageView = (ImageView) layout.findViewById(R.id.icon);
	            if(iconId == -1){

	            }else{
	               	imageView.setVisibility(View.VISIBLE);
	                imageView.setImageResource(iconId);
	            }
	            if(!StringTool.isEmpty(iconUrl)){
//	            	imageFetcher.loadImage(iconUrl, imageView, 0);
	            }

	            // set the confirm button
	            if (positiveButtonText != null) {

	                ((Button) layout.findViewById(R.id.positiveButton)).setText(positiveButtonText);

					// 设置事件监听
					setDialogEventListener(layout.findViewById(R.id.positiveButton),
							new DialogEventListener(dialog, positiveButtonClickListener));
	            } else {  
	                // if no confirm button just set the visibility to GONE  
	                layout.findViewById(R.id.positiveButton).setVisibility(  
	                        View.GONE);  
	            }  
	            // set the cancel button  
	            if (negativeButtonText != null) {

	                ((Button) layout.findViewById(R.id.negativeButton)).setText(negativeButtonText);

					// 设置事件监听
					setDialogEventListener(layout.findViewById(R.id.negativeButton),
							new DialogEventListener(dialog, negativeButtonClickListener));
	            } else {  
	                // if no confirm button just set the visibility to GONE  
	                layout.findViewById(R.id.negativeButton).setVisibility(  
	                        View.GONE);  
	            }  
	            
	            if (centerButtonText != null) {

	                ((Button) layout.findViewById(R.id.centerButton)).setText(centerButtonText);

					// 设置事件监听
					setDialogEventListener(layout.findViewById(R.id.centerButton),
							new DialogEventListener(dialog, centerButtonClickListener));
	            } else {  
	                // if no confirm button just set the visibility to GONE  
	                layout.findViewById(R.id.centerButton).setVisibility(  
	                        View.GONE);  
	            }  
	            
	            // set the content message  
	            if (message != null) {  
	                ((TextView) layout.findViewById(  
	                        R.id.message)).setText(message);  
	            } else if (contentView != null) {  
	            	
	            }
	            ScaleViewUtils.scaleView(layout);
	            dialog.setContentView(layout);
	            return dialog;  
	        }

			private void setDialogEventListener(View view, DialogEventListener dialogEventListener) {

				if (view == null) return ;

				view.setOnClickListener(dialogEventListener);
				view.setOnTouchListener(dialogEventListener);
			}
	 }

	
	
	/**
	 * Dialog监听器
	 */
	private DialogInterface.OnKeyListener mOnDialogKeyListener = new DialogInterface.OnKeyListener() {
		@Override
		public boolean onKey(DialogInterface dialogInterface, int keyCode,
				KeyEvent event) {
			// TODO Auto-generated method stub
			View focusView = ((Dialog) dialogInterface).getCurrentFocus();
			if (focusView == null) {
				return false;
			}

			if (GamepadTool.isButtonA(keyCode)) {
				KeyEvent keyevent = new KeyEvent(event.getAction(),
						KeyEvent.KEYCODE_ENTER);
				focusView.dispatchKeyEvent(keyevent);
				return true;
			} else if (GamepadTool.isButtonB(keyCode)) {
				Dialog dialog = (Dialog) dialogInterface;
				if (event.getAction() == KeyEvent.ACTION_UP && dialog != null
						&& dialog.isShowing()) {
					dialog.dismiss();
				}
				return true;
			} else if (GamepadTool.isButtonXY(keyCode)) {
				return true;
			}
			return false;
		}
	};
	
	/**
	 * 
	 * @description 设置dialog全屏
	 * @param dialog
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28下午5:27:55
	 *
	 */
	public void setParams(CommonProgressDialog dialog) {
          LayoutParams lay = dialog.getWindow().getAttributes();
		  DisplayMetrics dm = new DisplayMetrics();

		  ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);

		  Rect rect = new Rect();

		  View view = getWindow().getDecorView();

		  view.getWindowVisibleDisplayFrame(rect);

		  lay.height = dm.heightPixels - rect.top;

		  lay.width = dm.widthPixels;

		 }

	private static class DialogEventListener implements View.OnClickListener, View.OnTouchListener {

		private Dialog mDialog;
		private DialogInterface.OnClickListener mOnClickListener;

		public DialogEventListener(Dialog dialog, DialogInterface.OnClickListener onClickListener) {
			mDialog = dialog;
			mOnClickListener = onClickListener;
		}

		@Override
		public void onClick(View v) {

			int id = v.getId();

			if (R.id.positiveButton == id) {
				handlerClick(DialogInterface.BUTTON_POSITIVE);
			} else if (R.id.negativeButton == id) {
				handlerClick(DialogInterface.BUTTON_NEGATIVE);
			} else if (R.id.centerButton == id) {
				handlerClick(DialogInterface.BUTTON_NEUTRAL);
			}
		}

		private void handlerClick(int which) {

			if (mOnClickListener != null) {
				mOnClickListener.onClick(mDialog, which);
			}
			mDialog.dismiss();
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			if (MotionEvent.ACTION_UP == event.getAction()) {

				v.requestFocusFromTouch();
				v.performClick();
				return true;
			}
			return false;
		}
	}
}
