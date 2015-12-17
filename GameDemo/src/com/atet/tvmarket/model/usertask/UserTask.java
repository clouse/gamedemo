package com.atet.tvmarket.model.usertask;

import java.util.HashMap;
import java.util.Map;

import com.atet.tvmarket.model.usertask.UserTaskInfo.UserTaskType;

/**
 * @description: 用户任务
 *
 * @author: LiuQin
 */
public class UserTask {
	private Map<UserTaskType, UserTaskInfo> mUserTaskInfoMap = new HashMap<UserTaskType, UserTaskInfo>();
	
	public UserTaskInfo getUserTaskInfo(UserTaskType userTaskType){
		return mUserTaskInfoMap.get(userTaskType);
	}

	public void putUserTaskInfo(UserTaskType userTaskType, UserTaskInfo userTaskInfo){
		mUserTaskInfoMap.put(userTaskType, userTaskInfo);
	}
	
	public void clear(){
		mUserTaskInfoMap.clear();
	}
	
	public Map<UserTaskType, UserTaskInfo> getUserTaskInfoMap(){
		return mUserTaskInfoMap;
	}
	
	public int size(){
		return mUserTaskInfoMap.size();
	}
}
