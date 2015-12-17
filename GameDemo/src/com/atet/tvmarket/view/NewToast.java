package com.atet.tvmarket.view;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.atet.tvmarket.R;
import com.atet.tvmarket.utils.ScaleViewUtils;

public class NewToast extends Toast {
	public NewToast(Context context) {  
        super(context);  
    }  
	
	public static Toast makeToast(Context context, int resId, int duration)
             throws Resources.NotFoundException {
		return makeToast(context, context.getResources().getText(resId), duration);
	}
      
    public static Toast makeToast(Context context,CharSequence text, int duration) {  
        Toast result = new Toast(context);  
          
        //获取LayoutInflater对象  
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);   
        //由layout文件创建一个View对象  
        View layout = inflater.inflate(R.layout.new_toast, null);  
        ScaleViewUtils.scaleView(layout);
          
        //实例化ImageView和TextView对象  
        TextView textView = (TextView) layout.findViewById(R.id.toast_desc);  
         
        //textView.setTextSize(40);
        textView.setText(text);  
          
        result.setView(layout);  
        result.setGravity(Gravity.CENTER, 0, 0);  
        result.setDuration(duration);  
          
        return result;  
    }  
}
