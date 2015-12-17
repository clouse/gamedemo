package com.atet.api.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;

import com.atet.tvmarket.view.NewToast;

public class DialogUtil {
	private static DialogUtil INSTANCE;
	private ProgressDialog proDialog;
	private AlertDialog alertDialog;
	private Toast toast;
	
	public synchronized static DialogUtil getInstanse(){
		if(INSTANCE==null){
			INSTANCE=new DialogUtil();
		}
		return INSTANCE;
	}
	
	public void showProgressDialog(Context context,String title,String content,boolean isCancelable,final OnCancelListener listener){
		showTVProgressDialog(context,title,content,isCancelable,listener);
	}
	
	private void showTVProgressDialog(Context context,String title,String content,boolean isCancelable,final OnCancelListener listener){
		dismiss();
		proDialog=new ProgressDialog(context,ProgressDialog.THEME_HOLO_DARK);
		
		if(title!=null){
		    proDialog.setTitle(title);
		}
		proDialog.setMessage(content);
		proDialog.setCancelable(isCancelable);
		proDialog.setCanceledOnTouchOutside(false);
		proDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if(listener!=null){
					listener.onCancel(dialog);
				}
			}
		});
		proDialog.show();
	}
	
	public void showMessageDialog(Context context,String title,String content,boolean isCancelable,final OnCancelListener listener,final OnClickListener closeListener){
		dismiss();
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(content);
		builder.setCancelable(isCancelable);
		builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if(listener!=null){
					listener.onCancel(dialog);
				}
			}
		});
		builder.setNegativeButton("关闭", closeListener);
		alertDialog=builder.create();
		alertDialog.show();
	}
	
	public void showMessageDialog(Context context,String title,String content,boolean isCancelable,final OnCancelListener listener){
	    showMessageDialog(context, title, content, isCancelable, listener, null);
	}
	
	public void showToast(Context context,String content,boolean isClear){
	    dismiss();
	    if(isClear && toast!=null){
	        toast.cancel();
	    }
	    toast=NewToast.makeToast(context, content, Toast.LENGTH_SHORT);
	    toast.show();
	}
	
	public void dismiss(){
	    try {
	        if(proDialog!=null && proDialog.isShowing()){
	            proDialog.dismiss();
	            proDialog=null;
	        }
        } catch (Exception e) {
            // TODO: handle exception
        }
	    try {
	        if(alertDialog!=null && alertDialog.isShowing()){
	            alertDialog.dismiss();
	            alertDialog=null;
	        }
        } catch (Exception e) {
            // TODO: handle exception
        }
		if(toast!=null){
		    toast.cancel();
		}
	}
	
	public boolean isShowing(){
	    return (proDialog!=null && proDialog.isShowing()) || (alertDialog!=null && alertDialog.isShowing());
	}

}
