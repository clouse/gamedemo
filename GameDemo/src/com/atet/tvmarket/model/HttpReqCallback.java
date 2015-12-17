package com.atet.tvmarket.model;

import android.content.Context;

import com.atet.tvmarket.model.task.TaskResult;

public interface HttpReqCallback<T> {
	public void onResult(Context context, ReqConfig reqConfig, TaskResult<T> taskResult);
}
