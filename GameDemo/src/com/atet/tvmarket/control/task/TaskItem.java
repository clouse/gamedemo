package com.atet.tvmarket.control.task;

import com.atet.tvmarket.model.usertask.UserTaskInfo;
import com.atet.tvmarket.model.usertask.UserTaskInfo.UserTaskType;

public class TaskItem{
	private int typeId;
	private UserTaskInfo userTaskInfo;
	private UserTaskType userTaskType;
	private String btnText;
	public TaskItem(int typeId, UserTaskInfo userTaskInfo,UserTaskType userTaskType, String btnText) {
		super();
		this.typeId = typeId;
		this.userTaskInfo = userTaskInfo;
		this.userTaskType = userTaskType;
		this.btnText = btnText;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public UserTaskInfo getUserTaskInfo() {
		return userTaskInfo;
	}
	public void setUserTaskInfo(UserTaskInfo userTaskInfo) {
		this.userTaskInfo = userTaskInfo;
	}
	
	public UserTaskType getUserTaskType() {
		return userTaskType;
	}
	public void setUserTaskType(UserTaskType userTaskType) {
		this.userTaskType = userTaskType;
	}
	public String getBtnText() {
		return btnText;
	}
	public void setBtnText(String btnText) {
		this.btnText = btnText;
	}
	
	
}
