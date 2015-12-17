package com.atet.tvmarket.entity;

import android.text.TextUtils;

import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.model.SpHelper;

/**
 * @description: 从服务器获取的App配置
 *
 * @author: LiuQin
 * @date: 2015年9月22日 下午6:36:13 
 */
public class AppConfig {
	public static final String GAME_CENTER = "GameCenter";
	public static final String GAME_TYPE = "GameType";
	public static final String ACTIVITY = "Activity";
	public static final String MINE = "Mine";
	//新游推荐
	public static final String GAMEPAD_SHOP = "GamepadShop";
	
	private String mModuleNames;
	
	public AppConfig(){
		mModuleNames = (String)SpHelper.get(BaseApplication.getContext(), SpHelper.KEY_APP_MODULE, "");
	}
	
	/**
	 * @description: 游戏中心是否可见
	 *
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年9月22日 下午6:22:45
	 */
	public boolean isGameCenterVisible(){
		if(mModuleNames==null){
			return true;
		}
		return mModuleNames!=null && mModuleNames.contains(GAME_CENTER);
	}
	
	/**
	 * @description: 游戏分类是否可见
	 *
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年9月22日 下午6:28:21
	 */
	public boolean isGameTypeVisible(){
		if(mModuleNames==null){
			return true;
		}
		return mModuleNames!=null && mModuleNames.contains(GAME_TYPE);
	}
	
	/**
	 * @description: 活动是否可见
	 *
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年9月22日 下午6:29:01
	 */
	public boolean isActivityVisible(){
		if(mModuleNames==null){
			return true;
		}
		return mModuleNames!=null && mModuleNames.contains(ACTIVITY);
	}
	
	/**
	 * @description: 我的是否可见
	 *
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年9月22日 下午6:29:35
	 */
	public boolean isMineVisible(){
		if(mModuleNames==null){
			return true;
		}
		return mModuleNames!=null && mModuleNames.contains(MINE);
	}
	
	/**
	 * @description: 新游推荐是否存在
	 *
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年10月9日 下午6:11:12
	 */
	public boolean isGameRecommendExist(){
		if(mModuleNames==null){
			return true;
		}
		return mModuleNames!=null && !mModuleNames.contains(GAMEPAD_SHOP);
	}
	
	/**
	 * @description: 是否开机启动统计服务
	 *
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年9月22日 下午6:31:16
	 */
	public boolean isStatisticsOnBoot(){
		Boolean result = true;
		return (Boolean)SpHelper.get(BaseApplication.getContext(), SpHelper.KEY_STATISTICS_ON_BOOT, result);
	}
	
	public static void saveAppModule(String modelNames){
		SpHelper.put(BaseApplication.getContext(), SpHelper.KEY_APP_MODULE, modelNames);
	}
	
	public static void saveStatisticsOnBootFlag(Boolean on){
		SpHelper.put(BaseApplication.getContext(), SpHelper.KEY_STATISTICS_ON_BOOT, on);
	}
	
	public static boolean isInitAppConfig(){
		String moduleNames = "";
		moduleNames=(String)SpHelper.get(BaseApplication.getContext(), SpHelper.KEY_APP_MODULE, moduleNames);
		return !TextUtils.isEmpty(moduleNames);
	}
}
