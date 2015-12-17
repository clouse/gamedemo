package com.atet.tvmarket.entity;

import com.atet.tvmarket.model.task.TaskResult;

public class TaskEvent {
	private String tag;
	private TaskResult taskResult;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public TaskResult getTaskResult() {
		return taskResult;
	}

	public void setTaskResult(TaskResult taskResult) {
		this.taskResult = taskResult;
	}

	@Override
	public String toString() {
		return "EventbusEvent [tag=" + tag + ", taskResult=" + taskResult + "]";
	}

}
