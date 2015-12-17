package com.atet.tvmarket.model;

import android.content.Context;

public interface RequestCancelable {
	public void request(Context context);
	public void cancel(Context context);
}
