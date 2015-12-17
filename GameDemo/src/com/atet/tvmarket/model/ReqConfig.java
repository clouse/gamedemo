package com.atet.tvmarket.model;

import com.atet.tvmarket.entity.InterfaceInfo;
import com.atet.tvmarket.model.task.TaskResult;

/**
 * @description:
 *
 * @author: LiuQin
 * @date: 2015年7月3日 上午10:06:17
 */
public class ReqConfig {
	// 是否从主线程回调
	private boolean isMainCallback = false;
	private int reqCode = ReqCode.UNKNOWN;
	private ReqCallback callback;
	private Object data = null;
	private Task task = null;
	private String taskId;
	private String requestTag;
	private InterfaceInfo interfaceInfo;
	private int reqType = ReqType.INVALID;
	
	public int getReqCode() {
		return reqCode;
	}

	public void setReqCode(int reqCode) {
		this.reqCode = reqCode;
	}

	public ReqCallback getCallback() {
		return callback;
	}

	public void setCallback(ReqCallback callback) {
		this.callback = callback;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public boolean isMainCallback() {
		return isMainCallback;
	}

	public void setMainCallback(boolean isMainCallback) {
		this.isMainCallback = isMainCallback;
	}

	@Override
	public String toString() {
		return "ReqConfig [isMainCallback=" + isMainCallback + ", reqCode="
				+ reqCode + ", callback=" + callback + ", data=" + data + "]";
	}
	
	
	public static ReqConfig makeReqConfig(int reqCode, ReqCallback reqCallback){
		return makeReqConfig(reqCode, reqCallback, null, false);
	}
	
	public static ReqConfig makeReqConfig(int reqCode, ReqCallback reqCallback, Object data){
		return makeReqConfig(reqCode, reqCallback, data, false);
	}
	
	public static ReqConfig makeReqConfig(int reqCode, ReqCallback reqCallback, Object data, boolean isMainCallback){
		ReqConfig reqConfig =new ReqConfig();
		reqConfig.setReqCode(reqCode);
		reqConfig.setCallback(reqCallback);
		reqConfig.setData(data);
		reqConfig.setMainCallback(isMainCallback);
		return reqConfig;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getRequestTag() {
		return requestTag;
	}

	public void setRequestTag(String requestTag) {
		this.requestTag = requestTag;
	}
	

	public InterfaceInfo getInterfaceInfo() {
		return interfaceInfo;
	}

	public void setInterfaceInfo(InterfaceInfo interfaceInfo) {
		this.interfaceInfo = interfaceInfo;
	}

	public int getReqType() {
		return reqType;
	}

	public void setReqType(int reqType) {
		this.reqType = reqType;
	}


	public static class ReqCode {
		public static final int UNKNOWN = 0;
		// 获取服务器请求ID
		public static final int FETCH_SERVER_ID = 1;
		// 获取游戏分类
		public static final int FETCH_GAME_TYPE = 2;
		// 从分类获取游戏
		public static final int FETCH_GAME_INFOS_FROM_GAME_TYPE = 99;
		// 从分类获取游戏
		public static final int FETCH_GAME_INFOS_FROM_GAME_TYPE2 = 3;
		// 使用GameId获取游戏
		public static final int FETCH_GAME_INFO_FROM_GAME_ID = 4;
		// 新游推荐
		public static final int FETCH_GAME_INFOS_FROM_NEW_UPLOAD = 5;
		// 游戏排行
		public static final int FETCH_GAME_INFOS_FROM_GAME_RANKING = 6;
		// 游戏专题
		public static final int FETCH_GAME_TOPIC = 7;
		// 专题下的游戏（版权游戏）
		public static final int FETCH_GAME_INFOS_FROM_GAME_TOPIC = 98;
		// 专题下的游戏（版权游戏）
		public static final int FETCH_GAME_INFOS_FROM_GAME_TOPIC2 = 8;
		// 专题下的游戏（第三方游戏）
		public static final int FETCH_GAME_INFOS_FROM_GAME_TOPIC_THIRD = 9;
		// 游戏拼音搜索
		public static final int FETCH_GAME_SEARCH_PINYIN = 10;
		// 游戏中心
		public static final int FETCH_GAME_CENTER_INFO = 11;
		// 开机广告
		public static final int FETCH_LAUNCH_AD = 12;
		// 活动
		public static final int FETCH_ACTIVITY = 13;
		// 商品
		public static final int FETCH_GOODS = 14;
		// 礼包
		public static final int FETCH_GIFT = 15;
		// 用户领取的礼包
		public static final int FETCH_USER_GIFT = 16;
		// 领取礼包
		public static final int OBTAIN_USER_GIFT = 17;
		// 公告
		public static final int FETCH_NOTICE = 18;
		// 视频
		public static final int FETCH_VIDEO = 19;
		// 游戏评分
		public static final int RATE_GAME = 20;
		// 通过输入的拼音搜索游戏
		public static final int FETCH_GAME_SEARCH_PINYIN_BY_PINYIN = 21;
		// 使用GameId获取第三方游戏
		public static final int FETCH_THIRD_GAME_INFO_FROM_GAME_ID = 22;
		// 使用GameId获取同分类下的其它游戏
		public static final int FETCH_RELATIVE_GAME_INFO_FROM_GAME_ID = 23;
		// 获取推荐的游戏搜索
		public static final int FETCH_RECOMMEND_GAME_SEARCH_INFO = 24;
		// 获取任务
		public static final int FETCH_USER_TASK = 25;
		// 领取任务奖励
		public static final int OBTAIN_USER_TASK_REWARD = 26;
		// 获取新版本信息
		public static final int FETCH_NEW_VERSION_INFO = 27;
		// 查询用户商品兑换记录
		public static final int USER_GOODS_ORDER = 28;
		// 用户商品兑换
		public static final int EXCHANGE_GOODS = 29;
		// 获取可更新的接口
		public static final int UPDATABLE_INTERFACE = 30;
		// 获取任务2
		public static final int FETCH_USER_TASK2 = 31;
		// 抽奖信息
		public static final int FETCH_REWARD_INFO = 32;
		// 领取抽奖的奖励
		public static final int OBTAIN_REWARD = 33;
		// 所有游戏下载次数和星级
		public static final int DOWN_STAR = 34;
		// 游戏配置
		public static final int APP_CONFIG = 35;
		// 有更新内容的接口
		public static final int NEW_CONTENT_INTERFACE = 36;
		// 去除更新内容的接口
		public static final int REMOVE_NEW_CONTENT_INTERFACE = 37;
		// 商品的剩余数量
		public static final int FETCH_GOODS_LEFT_COUNT = 38;
		// 同步时间
		public static final int SYNC_TIME = 39;
		
		// 注册用户
		public static final int USER_REGISTER = 101;
		// 登录帐号
		public static final int USER_LOGIN = 102;
		// 自动登录帐号
		public static final int USER_AUTO_LOGIN = 103;
		// 获取验证码
		public static final int USER_GET_CAPTCHA = 104;
		// 微信登录
		public static final int USER_WECHAT_LOGIN = 105;
		// 获取所有登录过的用户
		public static final int USER_GET_ALL_LOGINED_USERS = 106;
		// 获取最后登录的用户
		public static final int USER_GET_LAST_LOGINED_USER = 107;
		// 修改密码
		public static final int USER_UPDATE_PASSWORD = 108;
		// 修改手机号
		public static final int USER_UPDATE_PHONE_NUM = 109;
		// 找回密码
		public static final int USER_RESET_PASSWORD = 110;
		// 自动登录指定userId的帐号
		public static final int USER_AUTO_LOGIN_BY_USERID = 111;
		// 下载新版本的apk
		public static final int DOWNLOAD_NEW_VERSION_APK = 201;
		// 下载文件
		public static final int DOWNLOAD_FILE = 202;
		
	}
	
	public static class ReqType{
		public static final int INVALID = 0;
		public static final int REQUEST = 1;
		public static final int REFRESH = 2;
		public static final int UPDATE = 3;
	}
}