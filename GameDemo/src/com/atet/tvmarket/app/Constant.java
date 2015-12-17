package com.atet.tvmarket.app;

import android.os.Environment;

/**
 * @description: 常量
 * 
 * @author: LiuQin
 * @date: 2015年5月27日 上午9:25:42
 */
public class Constant {
	public static final int PIC_REFLECT_HEIGHT = 50;
	public static final int VIEWHOLDER_TYPE_0 = 0;
	public static final int VIEWHOLDER_TYPE_1 = 1;
	public static final int VIEWHOLDER_TYPE_2 = 2;
	public static final int VIEWHOLDER_TYPE_3 = 3;
	public static final int VIEWHOLDER_TYPE_4 = 4;

	public static int DIALOG_MESSAGE_FONT_SIZE = 18;
	public static int DIALOG_BACKGROUND_ALPHA = 180;

	
	public static final String USERGAMEGIFTINFO = "userGameGiftInfo";
	public static final String GAMEGIFTINFO = "gameGiftInfo";
	public static final String USERGIFTINFO = "userGiftInfo";
	public static final String ACTINFO = "actInfo";
	public static final String GOODSINFO = "goodsInfo";
	public static final String GIFTCODE = "giftCode";
	public static final String GIFTNAME = "giftName";
	public static final String QRCODE = "QRcode";
	public static final String GAMECENTER = "GAMECENTER";
	public static final String REWARDINFORESP = "RewardInfoResp";
	public static final String TIME = "Time";
	public static final String LUCKYTIME = "luckyTime";
	
	public static final boolean is_login_success = false;

	public static final String ACTION_TO_JUMP = "ACTION_TO_JUMP";
	public static final int ACTION_TO_MAIN = 6001;
	public static final int ACTION_TO_SETTING = 6002;
	public static final int ACTION_TO_MYGAME = 6003;
	public static final int ACTION_TO_GAME_CLASSIFY = 6004;
	public static final int ACTION_TO_ACTIVITIES = 6011;

	public static final int ACTION_TO_UNICOM = 6006;
	public static final int ACTION_TO_GAME_DETAIL = 6007;
	public static final int ACTION_TO_GAMEPAD_SHOP = 6008;
	public static final int ACTION_TO_VIDEO_GUIDE = 6009;
	public static final int ACTION_TO_GIFT_PACKAGE = 6010;
	// 我的游戏状态
	public static final int GAME_STATE_NOT_DOWNLOAD = 0;
	public static final int GAME_STATE_NOT_INSTALLED = 1;
	public static final int GAME_STATE_INSTALLED = 2;
	public static final int GAME_STATE_INSTALLED_FAIL = 3;
	public static final int GAME_STATE_DOWNLOAD_ERROR = 4;
	public static final int GAME_STATE_NEED_UPDATE = 6;
	// 系统更新了应用
	public static final int HANDLER_PKG_INSTALLED_REMOVED = 5;
	// 在SD卡中的apk状态
	public static final int GAME_STATE_NOT_INSTALLED_APK = 7;
	public static final int GAME_STATE_INSTALLED_SYSTEM = 0x100 | GAME_STATE_INSTALLED;
	public static final int GAME_STATE_INSTALLED_USER = 0x200 | GAME_STATE_INSTALLED;
	public static String SDCARD_ROOT = Environment.getExternalStorageState()
			.equalsIgnoreCase(Environment.MEDIA_MOUNTED) ? Environment
			.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard";
	// 如果sd卡不可用，是否使用data/data中的目录
	public static boolean IS_USE_CACHE_PATH_TO_SAVE = true;
	// 游戏包下载相对位置
	public static final String GAME_RELATIVE_DIR = "/atet/tvmarket/game/";
	// 游戏下载保存目录
	public static String GAME_DOWNLOAD_LOCAL_DIR = SDCARD_ROOT
			+ GAME_RELATIVE_DIR;
	// 游戏数据包存放目录
	public static String GAME_ZIP_DATA_LOCAL_DIR = SDCARD_ROOT + "/";
	// 默认的apk存储路径
	public static final String APK_FOLDER_PATH = "/sdcard/atet/tvmarket/internal/";
	// 视频教程缓存地址
    public static String VIDEO_DATA_LOCAL_DIR = SDCARD_ROOT
					+ "/atet/tvmarket/video_cache/";
	// 游戏的下载方式
	public static int DOWN_FROM_THIRD = 2; // 从网络获取真实下载地址
	public static int DOWN_FROM_LOCAL = 1; // 从本地下载
	public static int WIDTH;
	public static int HEIGHT;
	public static int DENSITY;
	public static String RESOLUTION;
	/** 预装游戏 * */
	public static final int TASKKEY_PREGAME = 4004;
	/** 需要更新升级的游戏 **/
	public static final int TASKKEY_NEEDUPDATEGAME = 4005;
	/** 无网络连接 */
	public static final int NETWORK_TYPE_NONE = -1;
	/** 通过WIFI连接网络 */
	public static final int NETWORK_TYPE_NET = 0;
	/** 通过GPRS连接网络 */
	public static final int NETWORK_TYPE_WAP = 1;
	public static final int CODE_SYS_SUCCESS = 0;
	public static final int CODE_SYS_INVALID_OPTION = 1;
	public static final int CODE_SYS_INNER_ERROR = 2;
	public static final int CODE_SYS_JSON_PARSE_ERROR = 3;
	public static final int CODE_SYS_REQUEST_PARAMS_ERROR = 4;
	public static final int CODE_NETWORK_ERROR = -100;
	public static final int CODE_NO_RESPONDING_DATA = 1201;

	public static final int SHOWLOADING = 9000;
	public static final int DISMISSLOADING = 9001;
	public static final int NULLDATA = 9002;
	public static final int EXCEPTION = 9003;
	public static final int PULLGIFT = 9004;
	public static final int PULLGIFTFAILED = 9005;
	public static final int PULLGIFTSUCCESS = 9006;
	public static final int WEBVIEWLOADING = 9007;
	
	
	/** 用于存放设备的chanelId */
	public static final String DEVICE_CHANNEL_ID = "DEVICE_CHANNEL_ID";
	/** 用于存放设备的deviceId */
	public static final String DEVICE_DEVICE_ID = "DEVICE_DEVICE_ID";
	/** 用于存放设备的type */
	public static final String DEVICE_TYPE = "DEVICE_TYPE";
	/** 用于存放设备的deviceCode */
	public static final String DEVICE_DEVICE_CODE = "DEVICE_DEVICE_CODE";
	/** 用于存放设备的productId */
	public static final String DEVICE_PRODUCT_ID = "DEVICE_PRODUCT_ID";

	/**
	 * @description: 与服务器定义的设备类型
	 * 
	 * @author: LiuQin
	 * @date: 2015年6月24日 下午6:17:22
	 */
	public static final class DEVICE_TYPE {
		public static final boolean TELL_DEVICE_TYPE_FROM_PACKAGE_NAME = true;
		public static final String TV_MARKET_PACKAGE_NAME = "com.atet.tvmarket";
		public static final String PHONE_MARKET_PACKAGE_NAME = "com.sxhl.market";

		public static final int TYPE_UNKNOWN = -1;
		public static final int TYPE_TV = 1;
		public static final int TYPE_PHONE = 2;
		public static int sCustomDeviceType = TELL_DEVICE_TYPE_FROM_PACKAGE_NAME ? TYPE_UNKNOWN
				: TYPE_TV;
	}

	/**
	 * @description: 服务器返回的状态码
	 * 
	 * @author: LiuQin
	 * @date: 2015年6月24日 下午6:46:20
	 */
	public static final class SERVER_RETURN_CODE {
		public static final int SUCCESS = 0;
		public static final int INVALID_OPERATION = 1;
		public static final int INTERNAL_ERROR = 2;
		public static final int JSON_PARSE_ERROR = 3;
		public static final int INVALID_REQUEST_DATA = 4;
		public static final int INVALID_TOKEN = 5;
		public static final int NO_DATA = 6;

	}

	/**
	 * 与取设备号相关
	 */
	public static final String PACKAGE_NAME = "packageName";
	public static final String GAME_ID = "gameId";
	public static final String EXTERNAL = "external";
	/** 各个界面跳转的action */
	public static final String MARKET_GAME_DETAIL_ACTION = "com.sxhl.tcltvmarket.DETAILGAME";
	public static final String MARKET_GAME_CENTER_ACTION = "com.sxhl.tcltvmarket.MAIN";
	public static final String MARKET_GAME_SETUP_ACTION = "com.sxhl.tcltvmarket.SETUP";
	public static final String MARKET_MY_GAME_ACTION = "com.sxhl.tcltvmarket.MYGAME";
	public static final String MARKET_GAME_CLASSIFY_ACTION = "com.sxhl.tcltvmarket.GAMECLASSIFY";
	public static final String MARKET_GAME_CLASSIFY_UNICOM = "com.sxhl.tcltvmarket.UNICOM";
	public static final String MARKET_GAMEPAD_SHOP = "com.sxhl.tcltvmarket.GAMEPADSHOP";
	public static final String MAREKT_VIDEO_GUIDE = "com.sxhl.tcltvmarket.VIDEOGUIDE";
	public static final String MARKET_GIFT_PACKAGE = "com.sxhl.tcltvmarket.GIFTPACKAGE";
	public static final String MARKET_ACTIVITIES = "com.sxhl.tcltvmarket.ACTIVITIES";
	//public static final String MARKET_ACTIVITIES = "com.sxhl.tcltvmarket.ACTIVITIES";
	/** 用于存放设备信息的SP */
	public static final String DEVICE_INFO_SP = "deviceInfo";
	
	public static final String APPPATH = BaseApplication.getContext().getApplicationContext().getFilesDir().getAbsolutePath() + "/";
	
	//升级相关
	public static final String GAMEPAD_PACKAGE_NAME = "com.atet.tvgamepad";
	public static final String MARKET_PACKAGE_NAME = "com.sxhl.tcltvmarket";
	public static final String SETTING_PACKAGE_NAME = "com.atet.tclsettings";
	// assets\ATETGamepad.apk应用的版本号，如果不存在该文件，设为-100
	public static final int GAMEPAD_SETTING_VERSION_CODE = Configuration.GAMEPAD_SETTING_VERSION_CODE;
	// assets\ATETSettings.apk应用的版本号，如果不存在该文件，设为-100
	public static final int SETTINGS_VERSION_CODE = -100;
	
	//搜索相关
	public static final String ATET_OFFICE_ADDRESS="http://www.at-et.com/weixin/";
	
	/** 用于存放该用户的userID */
	public static final String LOGIN_USER_ID = "LOGIN_USER_ID";
	/** 旧版本用于存放账号信息的SP */
	public static final String LOGIN_USER_SP = "account";
	/** 用于存放登录用户的用户名 */
	public static final String LOGIN_USER_NAME = "LOGIN_USER_NAME";
	/** 用于存放登录用户的密码 */
	public static final String LOGIN_PASSWORD = "LOGIN_PASSWORD";
	/** AES加密算法所需加密种子 */
	public static final String AES_SEED = "ATET";
}
