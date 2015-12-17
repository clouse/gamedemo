package com.atet.tvmarket.app;


/**
 * @description: http url
 *
 * @author: LiuQin
 * @date: 2015年6月24日 下午5:12:32
 */
public class UrlConstant {
	private static final String GAME_RELATIVE_PATH = "/atetinterface";
	private static final String USER_RELATIVE_PATH = "/myuser";

	// 服务器请求id
	public static final String SERVER_ID = IPPort.BASE_GAME_SERVER + GAME_RELATIVE_PATH + "/deviceid.do";
	// 游戏分类
	public static final String GAME_TYPE = IPPort.BASE_GAME_SERVER + GAME_RELATIVE_PATH + "/typelist.do";
	// 某个分类下的游戏
	public static final String GAME_INFOS_FROM_GAME_TYPE = IPPort.BASE_GAME_SERVER + GAME_RELATIVE_PATH + "/glist.do";
	// 使用game id获取游戏
	public static final String GAME_INFO_FROM_GAME_ID = IPPort.BASE_GAME_SERVER + GAME_RELATIVE_PATH + "/gdetail.do";
	// 新游推荐
	public static final String GAME_INFOS_FROM_NEW_UPLOAD = IPPort.BASE_GAME_SERVER + GAME_RELATIVE_PATH + "/newrecommend.do";
	// 游戏排行
	public static final String GAME_INFOS_FROM_GAME_RANKING = IPPort.BASE_GAME_SERVER + GAME_RELATIVE_PATH + "/topdown.do";
	// 游戏专题
	public static final String GAME_TOPIC = IPPort.BASE_GAME_SERVER + GAME_RELATIVE_PATH + "/topiclist.do";
	// 某个专题下的版权游戏
	public static final String GAME_INFOS_FROM_GAME_TOPIC = IPPort.BASE_GAME_SERVER + GAME_RELATIVE_PATH + "/topicinfo.do";
	// 某个专题下的第三方游戏
	public static final String THIRD_GAME_INFOS_FROM_GAME_TOPIC = IPPort.BASE_GAME_SERVER + GAME_RELATIVE_PATH + "/topicinfo.do";
	// 游戏搜索拼音
	public static final String GAME_SEARCH_PINYIN = IPPort.BASE_GAME_SERVER + GAME_RELATIVE_PATH + "/pinyinlist.do";
	// 广告
	public static final String ADS = IPPort.BASE_GAME_SERVER + GAME_RELATIVE_PATH + "/adsbymodel.do";
	// 公告
	public static final String NOTICE = IPPort.BASE_GAME_SERVER + GAME_RELATIVE_PATH + "/notice.do";
	// 活动
	public static final String ACTIITY = IPPort.BASE_GAME_SERVER + GAME_RELATIVE_PATH + "/activitylist.do";
	// 商品
	public static final String GOODS = IPPort.BASE_GAME_SERVER + GAME_RELATIVE_PATH + "/goodslist.do";
	// 礼包
	public static final String GIFT = IPPort.BASE_GAME_SERVER + GAME_RELATIVE_PATH + "/newgamegift.do";
	// 用户领取的礼包
	public static final String USER_GIFT = IPPort.BASE_GAME_SERVER + GAME_RELATIVE_PATH + "/usergifts.do";
	// 领取礼包
	public static final String OBTAIN_USER_GIFT = IPPort.BASE_GAME_SERVER + GAME_RELATIVE_PATH + "/userreceivegift.do";
	// 视频
	public static final String VIDEO = IPPort.BASE_GAME_SERVER + GAME_RELATIVE_PATH + "/videolist.do";
	// 游戏评分
	public static final String RATE_GAME = IPPort.BASE_GAME_SERVER + GAME_RELATIVE_PATH + "/starscore.do";
	// 使用game id获取第三方游戏
	public static final String THIRD_GAME_INFO_FROM_GAME_ID = IPPort.BASE_GAME_SERVER + GAME_RELATIVE_PATH + "/thirdgamebyids.do";
	// 获取同分类下的其它6个游戏
	public static final String GAME_INFOS_FROM_RELATIVE = IPPort.BASE_GAME_SERVER + GAME_RELATIVE_PATH + "/hotgamelist.do";
	// 获取新版本信息
	public static final String NEW_VERSION_INFO = IPPort.BASE_GAME_SERVER + GAME_RELATIVE_PATH + "/queryversion.do";
	// 获取用户的商品订单信息
	public static final String USER_GOODS_ORDER = IPPort.BASE_GAME_SERVER + GAME_RELATIVE_PATH + "/goodsorderlist.do";
	// 获取用户的商品订单信息
	public static final String EXCHANGE_GOODS = IPPort.BASE_USER_SERVER + USER_RELATIVE_PATH + "/exchange.do";
	// 数据更新接口
	public static final String UPDATE_INTERFACE = IPPort.BASE_GAME_SERVER + GAME_RELATIVE_PATH + "/updateinterface.do";
	// 下载次数和星级
	public static final String DOWN_STAR = IPPort.BASE_GAME_SERVER + GAME_RELATIVE_PATH + "/gcountlevel.do";
	// App配置
	public static final String APP_CONFIG = IPPort.BASE_GAME_SERVER + GAME_RELATIVE_PATH + "/hallstyle.do";
	// 异常信息上传
	public static final String EXCEPTION_UPLOAD = IPPort.BASE_GAME_SERVER + GAME_RELATIVE_PATH + "/hallexception.do";
	// 商品剩余数量
	public static final String GOODS_LEFT_COUNT = IPPort.BASE_GAME_SERVER + GAME_RELATIVE_PATH + "/goodscount.do";
	// 时间同步
	public static final String TIME = IPPort.BASE_GAME_SERVER + GAME_RELATIVE_PATH + "/time.do";
	
	// 获取用户任务
	public static final String USER_TASK = IPPort.BASE_USER_SERVER + USER_RELATIVE_PATH + "/integral.do";
	// 获取抽奖信息
	public static final String REWARD_INFO = IPPort.BASE_USER_SERVER + USER_RELATIVE_PATH + "/rewardInfo.do";
	// 领取抽奖的奖励
	public static final String OBTAIN_REWARD = IPPort.BASE_USER_SERVER + USER_RELATIVE_PATH + "/getReward.do";
	
	
	
	/** 下载地址 */
	public static final String HTTP_GAME_DOWNLOAD_ADDRESS = IPPort.BASE_GAME_SERVER
			+ "/gamebox/downloadServlet.do";
	public static final String HTTP_GAME_DOWNLOAD_ADDRESS2 = IPPort.BASE_GAME_SERVER
			+ "/gamebox/downloadServlet.do";
	public static final String HTTP_GAME_DOWNLOAD_ADDRESS3 = IPPort.BASE_GAME_SERVER
			+ "/gamebox/downloadServlet.do";
	public static final String HTTP_GAME_ADD_DOWNLOAD_COUNT = IPPort.BASE_GAME_SERVER
			+ "/atetinterface/dcount.do";
	public static final String HTTP_GAME_ADD_DOWNLOAD_COUNT2 = IPPort.BASE_GAME_SERVER
			+ "/atetinterface/dcount.do";
	public static final String HTTP_GAME_ADD_DOWNLOAD_COUNT3 = IPPort.BASE_GAME_SERVER
			+ "/atetinterface/dcount.do";
	/** 第三方游戏下载自增接口 */
	public static final String HTTP_THIRD_GAME_ADD_DOWNLOAD_COUNT = IPPort.BASE_GAME_SERVER
			+ "/atetinterface/tgdcount.do";
	public static final String HTTP_THIRD_GAME_ADD_DOWNLOAD_COUNT2 = IPPort.BASE_GAME_SERVER
			+ "/atetinterface/tgdcount.do";
	public static final String HTTP_THIRD_GAME_ADD_DOWNLOAD_COUNT3 = IPPort.BASE_GAME_SERVER
			+ "/atetinterface/tgdcount.do";
	public static final String HTTP_GET_GAMEPAD_CONFIG = IPPort.BASE_GAME_SERVER_RELEASE
			+ "/atetinterface/handconfig.do";
	public static final String HTTP_GET_GAMEPAD_CONFIG2 = IPPort.BASE_GAME_SERVER_RELEASE2
			+ "/atetinterface/handconfig.do";
	public static final String HTTP_GET_GAMEPAD_CONFIG3 = IPPort.BASE_GAME_SERVER_RELEASE
			+ "/atetinterface/handconfig.do";
	public static final String HTTP_GET_INTERCEPT_INPUT_APP = IPPort.BASE_GAME_SERVER
			+ "/atetinterface/screengame.do";
	public static final String HTTP_GET_INTERCEPT_INPUT_APP2 = IPPort.BASE_GAME_SERVER
			+ "/atetinterface/screengame.do";
	public static final String HTTP_GET_INTERCEPT_INPUT_APP3 = IPPort.BASE_GAME_SERVER
			+ "/atetinterface/screengame.do";	
	
	public static final String HTTP_GET_GAMEINFO_INSTALLED = IPPort.BASE_GAME_SERVER
			+ "/atetinterface/gameversion.do";
	public static final String HTTP_GET_GAMEINFO_INSTALLED2 = IPPort.BASE_GAME_SERVER
			+ "/atetinterface/gameversion.do";
	public static final String HTTP_GET_GAMEINFO_INSTALLED3 = IPPort.BASE_GAME_SERVER
			+ "/atetinterface/gameversion.do";
	
	public static final String HTTP_GAME_BOX_GAMEDETAIL = IPPort.BASE_GAME_SERVER
			+ "/atetinterface/gdetail.do";
	public static final String HTTP_GAME_BOX_GAMEDETAIL2 = IPPort.BASE_GAME_SERVER
			+ "/atetinterface/gdetail.do";
	public static final String HTTP_GAME_BOX_GAMEDETAIL3 = IPPort.BASE_GAME_SERVER
			+ "/atetinterface/gdetail.do";
	
	public static final String HTTP_LAND_GAMEBOX_PREINSTALLED = IPPort.BASE_GAME_SERVER
			+ "/atetinterface/installgame.do";
	public static final String HTTP_LAND_GAMEBOX_PREINSTALLED2 = IPPort.BASE_GAME_SERVER
			+ "/atetinterface/installgame.do";
	public static final String HTTP_LAND_GAMEBOX_PREINSTALLED3 = IPPort.BASE_GAME_SERVER
			+ "/atetinterface/installgame.do";


	/**
	 * 游戏详细二维码地址
	 */
	public static final String HTTP_GAME_DETAIL_QRCODE = IPPort.BASE_GAME_SERVER
			+ "/atetinterface/publishedGame_toGamePage_gameDownload.action?gameId=";
}
