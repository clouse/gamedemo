package com.atet.tvmarket.model.usertask;

import java.util.List;

import android.content.Context;

import com.atet.tvmarket.entity.UserTaskReq;
import com.atet.tvmarket.model.DataConfig;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.ReqCallback;

/**
 * @description: 用户任务信息
 *
 * @author: LiuQin
 */
public class UserTaskInfo {
	// 不存在该任务
	public static final int TASK_STATE_NOT_EXIST = -2;
	// 任务无效或过期
	public static final int TASK_STATE_INVALID = -1;
	// 任务已完成
	public static final int TASK_STATE_COMPLETE = 0;
	// 任务有效但未完成
	public static final int TASK_STATE_STANDBY = 1;
	
	
	// 大厅首次注册
	public static final String TASK_TYPE_FIRST_REGEDISTER = "REGISTER";
	// 微信首次登录
	public static final String TASK_TYPE_WECHAT_FIRST_LOGIN = "WECHATLOGIN";
	// 首次充值
	public static final String TASK_TYPE_FIRST_RECHARGE = "RECHARGE";
	// 累计充值
	public static final String TASK_TYPE_RECHARGE_SUM = "RECHARGE_SUM";
	// 兑换商品
	public static final String TASK_TYPE_EXCHANGE_GOODS = "EXCHANGE";
	
	// 游戏评分
	public static final String TASK_TYPE_GAME_SCORE = "GAMESCORE";
	// 游戏下载
	public static final String TASK_TYPE_GAME_DOWNLOAD = "GAMEDOWN";
	// 游戏搜索
	public static final String TASK_TYPE_GAME_SEARCH = "SEARCH";
	// 观看视频
	public static final String TASK_TYPE_WATCH_VIDEO = "WATCHVIDEO";
	// 大厅在线时长
	public static final String TASK_TYPE_MARKET_ONLINE = "ONLINE";
	
	// 游戏登录
	public static final String TASK_TYPE_GAME_LOGIN = "GAMELOGIN";
	// 游戏在线时长
	public static final String TASK_TYPE_GAME_ONLINE = "GAMEONLINE";
	
	// 每日签到
	public static final String TASK_TYPE_CHECKIN_DAILY = "SIGN";
	// 连接签到
	public static final String TASK_TYPE_CHECKIN_CONTINUELY = "SIGN_CONTINUELY";
	//大厅登录
	public static final String TASK_TYPE_MARKET_LOGIN = "LOGIN";
	
	
	//连续 
	public static final String TASK_COMPLETE_WAY_CONTINUE = "CONTINUE";
	//累计
	public static final String TASK_COMPLETE_WAY_ADD = "ADD";
	//当前首次
	public static final String TASK_COMPLETE_WAY_NOWFIRST = "NOWFIRST";
	//永久首次
	public static final String TASK_COMPLETE_WAY_ALLFIRST = "ALLFIRST";
	
	
	public static enum UserTaskState {
		// 该任务已不存在
		TASK_STATE_NOT_EXIST, 
		// 任务过期
		TASK_STATE_INVALID,
		// 任务已完成
		TASK_STATE_FINISH, 
		// 任务未完成
		TASK_STATE_NOT_FINISH, 
		// 任务已完成但未领取奖励
		TASK_STATE_STANDBY
	}
	
	public static enum UserTaskType{
		// 大厅首次注册
		TASK_TYPE_FIRST_REGEDISTER,
		// 微信首次登录
		TASK_TYPE_WECHAT_FIRST_LOGIN,
		// 首次充值
		TASK_TYPE_FIRST_RECHARGE,
		// 累计充值
		TASK_TYPE_RECHARGE_SUM,
		// 兑换商品
		TASK_TYPE_EXCHANGE_GOODS,
		// 游戏评分
		TASK_TYPE_GAME_SCORE,
		// 游戏下载
		TASK_TYPE_GAME_DOWNLOAD,
		// 游戏搜索
		TASK_TYPE_GAME_SEARCH,
		// 观看视频
		TASK_TYPE_WATCH_VIDEO,
		// 大厅在线时长
		TASK_TYPE_MARKET_ONLINE, 
		// 游戏登录
		TASK_TYPE_GAME_LOGIN,
		// 游戏在线时长
		TASK_TYPE_GAME_ONLINE,
		// 每日签到
		TASK_TYPE_CHECKIN_DAILY,
		// 连接签到
		TASK_TYPE_CHECKIN_CONTINUELY,
		//每日登录
		TASK_TYPE_MARKET_LOGIN 
	}
	
	// 任务类型
	private UserTaskType taskType;
	
	// 任务id
	private String taskId;
	
	// 任务名称
	private String taskName;
	
	// 任务描述
	private String taskRemark;
	
	//任务状态
	private UserTaskState taskState;
	
	// 任务状态描述
	private String taskStateRemark;
	
	//子任务
	private List<UserTaskInfo> subTaskInfos;
	
	//用户id
	private int userId;
	
	//游戏id
	private String gameId;
	
	//规则id
	private String ruleId;
	
	//可以获取到的积分
	private int integral;
	
	//可以获取到的A券
	private int coupon;
	
	/**
	 * @description: 是否存在子任务
	 *
	 * @return 
	 * @author: LiuQin
	 */
	public boolean haveSubTaskInfos(){
		return subTaskInfos != null && subTaskInfos.size() > 0;
	}
	
	public UserTaskType getTaskType() {
		return taskType;
	}

	public void setTaskType(UserTaskType taskType) {
		this.taskType = taskType;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskRemark() {
		return taskRemark;
	}

	public void setTaskRemark(String taskRemark) {
		this.taskRemark = taskRemark;
	}

	public UserTaskState getTaskState() {
		return taskState;
	}

	public void setTaskState(UserTaskState taskState) {
		this.taskState = taskState;
	}

	public String getTaskStateRemark() {
		return taskStateRemark;
	}

	public void setTaskStateRemark(String taskStateRemark) {
		this.taskStateRemark = taskStateRemark;
	}

	public List<UserTaskInfo> getSubTaskInfos() {
		return subTaskInfos;
	}

	public void setSubTaskInfos(List<UserTaskInfo> subTaskInfos) {
		this.subTaskInfos = subTaskInfos;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public static UserTaskInfo makeUserTaskInfo(UserTaskType taskType, String taskId, String taskName, 
			String taskRemark, UserTaskState taskState, String taskStateRemark, int userId){
		UserTaskInfo info = new UserTaskInfo();
		info.setTaskType(taskType);
		info.setTaskId(taskId);
		info.setTaskName(taskName);
		info.setTaskRemark(taskRemark);
		info.setTaskStateRemark(taskStateRemark);
		info.setTaskState(taskState);
		info.setUserId(userId);
		return info;
	}
	
	/**
	 * @description: 获取任务奖励
	 *
	 * @param context
	 * @param reqCallback 回调接口
	 * @param isMain 是否主线程回调
	 * @author: LiuQin
	 * @date: 2015年7月22日 下午5:05:12
	 */
	public void obtainReward(Context context, ReqCallback<UserTaskInfo> reqCallback, boolean isMain){
		DataFetcher.obtainReward(context, reqCallback, this, isMain)
			.request(context);
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public int getCoupon() {
		return coupon;
	}

	public void setCoupon(int coupon) {
		this.coupon = coupon;
	}
}
