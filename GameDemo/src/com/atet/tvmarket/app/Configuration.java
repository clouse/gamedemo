package com.atet.tvmarket.app;

/**
 * @description: 配置类
 * 
 * @author: LiuQin
 * @date: 2015年5月26日 下午5:05:13
 */
public class Configuration {
	
	// 捕获异常信息到文件
	public static final boolean CATCH_CRASH_EXCEPTION_LOG = true;
	// 手柄设置的版本号
	public static final int GAMEPAD_SETTING_VERSION_CODE = 102;
	// 是否允许通过指定文件自定义ip
	public static final boolean ENABLE_DEBUG_IP_FILE = false;
	
	/**
	 * 调试相关
	 */
	// 打印开关
	public static final boolean ENABLE_LOG_PRINT = false;
	// 输出到文件开关
	public static final boolean ENABLE_LOG_FILE = false;
	// 日志文件最大容量：B
	public static final long LOG_MAX_FILE_SIZE = 100 * 1024 * 1024;
	// Strict Mode开关
	public static final boolean ENABLE_STRICT_MODE = false;
	// 旧版本日志开关
	public static final boolean IS_DEBUG_ENABLE = false;
	// 旧版本日志tag
	public static final String DEBUG_TAG = "ATET_MARKET";
	// 统计开关
	public static final boolean ENABLE_STASTIC = true;
	/**
	 * 图片缓存相关
	 */
	// 图片缓存到内存
	public static final boolean ENABLE_IMAGE_CACHE_IN_MEMORY = false;
	// 图片缓存到本地
	public static final boolean ENABLE_IMAGE_CACHE_IN_DISK = true;
	// 本地图片缓存最大容量:B
	public static final int DISK_CACHE_SIZE = 100 * 1024 * 1024;

	// 是否使用公共测试环境IP
	public static final boolean HTTP_PUBLIC_TEST_SERVER_ENABLE = false;

	public static final boolean IS_CUSTOM_TCL_DEVICE_CODE = false;
	public static final boolean IS_CUSTOM_DEVICE_UNIQUE_ID = false;

	//打开角标提示
	public static final boolean IS_OPEN_NEW_CONTENT_TIP = true;
	//测试角标开关
	public static final boolean IS_FILL_TEST_NEW_CONTENT_TIP = false;
}
