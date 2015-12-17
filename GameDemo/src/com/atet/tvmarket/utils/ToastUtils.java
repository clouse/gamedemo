package com.atet.tvmarket.utils;

import android.content.Context;
import android.widget.Toast;

import com.atet.tvmarket.view.NewToast;

public class ToastUtils {

	public static void ShowToast(Context context, String msg, Toast toast) {
		if (toast == null) {
			toast = NewToast.makeToast(context, msg, Toast.LENGTH_LONG);
		} else {
			toast.setText(msg);
		}
		toast.show();
	}
}
