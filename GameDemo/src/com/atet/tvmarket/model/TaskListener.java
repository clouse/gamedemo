package com.atet.tvmarket.model;

import com.atet.tvmarket.model.netroid.Listener;
import com.atet.tvmarket.model.task.TaskResult;

public abstract class TaskListener<T> extends Listener<T>{
	public abstract TaskResult doTaskInBackground();

	@Override
	public void onSuccess(T response) {}
}
