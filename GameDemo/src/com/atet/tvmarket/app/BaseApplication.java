package com.atet.tvmarket.app;

import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.entity.DeviceInfo;
import com.atet.tvmarket.entity.UserInfo;
import com.atet.tvmarket.model.DataHelper;
import com.atet.tvmarket.model.database.DBAccess;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.ScreenZoomUtils;
import com.atetpay.pay.atet.OnBalanceChangeReceiver;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;



/**
 * @description: app入口
 *
 * @author: LiuQin
 * @date: 2015年5月27日 上午11:07:16 
 */
public class BaseApplication extends Application {
	// 屏幕的宽度
	public static int mScreenWidth;
	// 屏幕的高度
	public static int mScreenHeight;
	// 屏幕密度
	public static int sScreenDensity;
	// 宽度的缩放比例（相对于720p）
	public static float sScreenWZoom;
	// 高度的缩放比例（相对于720p）
	public static float sScreenHZoom;
	//全局的Context
	private static Context context;
	//全局的Handler
	private static Handler mainHandler;
	//主线程
	private static Thread mainThread;
	
	private static long mMainThreasdId;
	
	//public static Typeface typeface;
	
	public static int position = 0;
	private static SharedPreferences preferences;
	private static DeviceInfo deviceInfo;
	/** 数据库访问 */
	private static DBAccess m_sqlAccess = null;
	public static UserInfo userInfo;
	
	/**gpu型号*/
	public static String gpuInfo;
	private OnBalanceChangeReceiver mBalanceChangeReceiver;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		initialize();
	}
	
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		if(mBalanceChangeReceiver != null){
			mBalanceChangeReceiver.unRegisterReceiver();
		}
	}

	/**
	 * @description: app初始化处理
	 * 
	 * @author: LiuQin
	 * @date: 2015年5月27日 上午9:50:18
	 */
	private void initialize() {
		ScaleViewUtils.init(this);
		context = this;
		mainHandler = new Handler();
		mainThread = Thread.currentThread();
		mMainThreasdId = android.os.Process.myTid();//主线程ID
		//typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/huawen.ttf");
		initCrashHandler();
		initStrictMode();
		initLog();
		initScreenParams(context);
		initImageLoader(context);
		registerBalanceChange();
	}
	
	public static Context getContext() {
		return context;
	}
	
	public static long getMainThreasdId() {
		return mMainThreasdId;
	}

	public static void setmMainThreasdId(long mMainThreasdId) {
		BaseApplication.mMainThreasdId = mMainThreasdId;
	}

	public static Handler getMainHandler(){
		return mainHandler;
	}
	
	public static Thread getMainThread(){
		return mainThread;
	}
	
	/*public static Typeface getTypeFace(){
		return typeface;
	}*/

	/**
	 * @description: 初始化日志配置
	 * 
	 * @author: LiuQin
	 * @date: 2015年5月26日 下午6:29:36
	 */
	private static void initLog() {
		ALog.init(Configuration.ENABLE_LOG_PRINT,
				Configuration.ENABLE_LOG_FILE, null,
				Configuration.LOG_MAX_FILE_SIZE);
	}

	/**
	 * @description: 初始化Strict Mode设定
	 * 
	 * @author: LiuQin
	 * @date: 2015年5月26日 下午7:33:31
	 */
	private static void initStrictMode() {
		if (Configuration.ENABLE_STRICT_MODE) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectAll().penaltyLog().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectAll().penaltyLog().build());
		}
	}
	
	private static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(Configuration.DISK_CACHE_SIZE)
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.threadPoolSize(1)
				// .writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	/**
	 * @description: 获取屏幕相关参数
	 *
	 * @param context
	 * @author: LiuQin
	 * @date: 2015年5月27日 上午11:03:28
	 */
	@SuppressLint("NewApi")
	private static void initScreenParams(Context context) {
		int width = 0;
		int height = 0;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		int sdkInt = Build.VERSION.SDK_INT;

		try {
			if (sdkInt >= 17) {
				Point outSize = new Point();
				display.getRealSize(outSize);
				width = outSize.x;
				height = outSize.y;
			} else if (sdkInt >= 13 && sdkInt <= 16) {
				Method mGetRawW;
				Method mGetRawH;
				mGetRawW = Display.class.getMethod("getRawWidth");
				mGetRawH = Display.class.getMethod("getRawHeight");
				width = (Integer) mGetRawW.invoke(display);
				height = (Integer) mGetRawH.invoke(display);
			} else {
				width = dm.widthPixels;
				height = dm.heightPixels;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			width = dm.widthPixels;
			height = dm.heightPixels;
		}

		BaseApplication.mScreenWidth = width;
		BaseApplication.mScreenHeight = height;
		BaseApplication.sScreenDensity = dm.densityDpi;
		BaseApplication.sScreenWZoom = ScreenZoomUtils.getScreenWZoom_1280(mScreenWidth);
		BaseApplication.sScreenHZoom = ScreenZoomUtils.getSceennHZoom_720(mScreenHeight);
		Constant.WIDTH = width;
		Constant.HEIGHT = height;
		Constant.DENSITY = dm.densityDpi;
	}
	
	
	/**
	 * @author wsd
	 * @Description:获取数据库
	 * @date 2012-12-12 下午12:52:36
	 */
	public static DBAccess getSqlInstance() {
		if (m_sqlAccess == null) {
			m_sqlAccess = DBAccess.getInstance(context);
		}
		return m_sqlAccess;
	}	
	
	/**
	 * @description: 初始化全局异常处理
	 * 
	 * @author: LiuQin
	 * @date: 2015年8月15日
	 */
	private void initCrashHandler(){
		if(Configuration.CATCH_CRASH_EXCEPTION_LOG){
			CrashHandler crashHandler = CrashHandler.getInstance();  
			crashHandler.init(getApplicationContext());  		
		}
	}
	
	/**
	 * @description: 监听支付sdk中的积分变化
	 * 
	 * @author: LiuQin
	 * @date: 2015年10月9日 下午10:15:21
	 */
	private void registerBalanceChange() {
		mBalanceChangeReceiver = new OnBalanceChangeReceiver(this,
				new com.atetpay.pay.atet.OnBalanceChangeListener() {

			public void onChange(int userId, int balance) {
				UserInfo userInfo = BaseApplication.userInfo;
				if(userInfo!=null && userInfo.getUserId() == userId){
					userInfo.setBalance(balance);
					DataHelper.updateUserInfo(userInfo);
				}
			}
		});
	}

}
