package com.atet.tvmarket.model;

public class DataConfig {
	//游戏中心广告id
	public static final String AD_MODEL_ID_GAME_CENTER = "A0A8A1000";
	//开机广告id
	public static final String AD_MODEL_ID_LAUNCH = "A0A8A2000";
	
	//Test User ID
	public static final int TestUserId = 7003;
	
	// 活动类型：所有
	public static final int ACTIVITY_TYPE_ALL = 0;
	// 活动类型：推荐
	public static final int ACTIVITY_TYPE_RECOMMEND = 1;
	
	// 商品类型：所有
	public static final int GOODS_TYPE_ALL = 0;
	// 商品类型：推荐
	public static final int GOODS_TYPE_RECOMMEND = 1;
	
	// 公告类型：新闻
	public static final int NOTICE_TYPE_NEWS = 0;
	// 公告类型：上下滚动的提示条
	public static final int NOTICE_TYPE_TIPS = 1;
	// 公告类型：紧急公告
	public static final int NOTICE_TYPE_EMERGENCY = 2;
	
	//视频教学
	public static final String VIDEO_TYPE_GUIDE = "005";
	
	//版权游戏
	public static final int GAME_TYPE_COPYRIGHT = 1;
	//第三方游戏
	public static final int GAME_TYPE_THIRD = 0;
	
	//下载排行类型：下载
	public static final int GAME_RANKING_TYPE_DOWNLOAD = 0;
	//下载排行类型：手柄游戏
	public static final int GAME_RANKING_TYPE_GAMEPAD = 1;
	//下载排行类型：遥控器游戏
	public static final int GAME_RANKING_TYPE_REMOTE_CONTROLLER = 2;
	
	//任务标识：获取任务
	public static final int TASK_FLAG_FETCH = 0;
	public static final int TASK_FLAG_OBTAIN = 1;
	
	//大厅升级的appId
	public static final String MARKET_APPID = "10002";
	
	//接口数据状态:用户未查看
	public static final int INTERFACE_STATE_NOTSEE = 1;
	//接口数据状态:用户已查看
	public static final int INTERFACE_STATE_SEE = 0;
	
	
	/**
	 * @description: 可更新的接口
	 *
	 * @author: LiuQin
	 * @date: 2015年8月6日 下午10:24:05 
	 */
	public static class UpdateInterface{
		//游戏中心
		public static final String ADS = "adsbymodel";
		//游戏分类
		public static final String GAME_TYPE = "typelist";
		//某个游戏分类
		public static final String GAME_TYPE_DETAIL = "glist";
		//游戏专题
		public static final String GAME_TOPIC = "topiclist";
		//某个游戏专题
		public static final String GAME_TOPIC_DETAIL = "topicinfo";
		//最近上线
		public static final String GAME_NEW_UPLOADED = "newuploaded";
		//游戏排行
		public static final String GAME_RANKING = "topdown";
		//活动
		public static final String GAME_ACTIVITY = "activitylist";
		//商品
		public static final String GAME_GOODS = "goodslist";
		//礼包
		public static final String GAME_GIFT = "newgamegift";
		//视频教学
		public static final String VIDEO_GUIDE = "videolist";
		
		//公告
		public static final String NOTICE = "notice";
		//查询版本号
		public static final String QUERY_VERSION = "queryversion";
		//拼音搜索
		public static final String PINYIN_SEARCH = "pinyinlist";
		
		//任务
		public static final String USER_TASK = "usertask";
	}
	
	public static final String REQUEST_TASK_TAG_PREFIX = "UI_";
	
	//数据更新服务acton
	public static final String ACTION_DATA_UPDATE = "com.atet.tvmarket.action_data_update";
	public static final long NomalSpaceTime = 6 * 60 * 60 * 1000;
	public static final long ExceptionSpaceTime = 30 * 60 * 1000;
//	public static final long NomalSpaceTime = 10 * 60 * 1000;
//	public static final long ExceptionSpaceTime = 10 * 60 * 1000;
	
	/**
	 * @description: 设备性能等级标识
	 *
	 * @author: LiuQin
	 * @date: 2015年8月21日 下午3:47:14 
	 */
	public static class DeviceCapability {
		//高
		public static final int HIGH = 3;
		//中
		public static final int MEDIUM = 2;
		//低
		public static final int LOW = 1;
	}
}