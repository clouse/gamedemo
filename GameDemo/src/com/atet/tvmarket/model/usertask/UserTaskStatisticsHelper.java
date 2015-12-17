package com.atet.tvmarket.model.usertask;

import com.atet.tvmarket.model.TimeHelper;

import android.content.Context;

/**
 * @description: 用户任务数据收集
 *
 * @author: LiuQin
 * @date: 2015年8月25日 下午1:25:04 
 */
public class UserTaskStatisticsHelper {
	private static final boolean LIMIT_LOGIN = true;

	/**
	 * @description: 统计微信首次登录
	 *
	 * @param context
	 * @param userId
	 * @author: LiuQin
	 * @date: 2015年8月25日 下午12:55:03
	 */
	public static void saveWeChatFirstLogin(Context context, int userId){
		if(LIMIT_LOGIN && userId <= 0){
			return;
		}
		long curtime = TimeHelper.getCurTime();
		UserTaskDaoHelper.saveWeChatFirstLogin(context, userId, curtime);
	}
	
	/**
	 * @description: 统计首次充值
	 *
	 * @param context
	 * @param userId 
	 * @author: LiuQin
	 * @date: 2015年8月25日 下午12:56:14
	 */
	public static void saveFirstRecharge(Context context, int userId){
		if(LIMIT_LOGIN && userId <= 0){
			return;
		}
		long curtime = TimeHelper.getCurTime();
		UserTaskDaoHelper.saveFirstRecharge(context, userId, curtime);
	}
	
	/**
	 * @description: 统计累计充值
	 *
	 * @param context
	 * @param userId
	 * @param quantity 
	 * @author: LiuQin
	 * @date: 2015年8月25日 下午12:57:38
	 */
	public static void saveRechargeSum(Context context, int userId, int quantity){
		if(LIMIT_LOGIN && userId <= 0){
			return;
		}
		long curtime = TimeHelper.getCurTime();
		UserTaskDaoHelper.saveRechargeSum(context, userId, curtime, quantity);
	}
	
	/**
	 * @description: 统计累计兑换
	 *
	 * @param context
	 * @param userId
	 * @param quantity 
	 * @author: LiuQin
	 * @date: 2015年8月25日 下午12:57:47
	 */
	public static void saveExchangeGoods(Context context, int userId, int quantity){
		if(LIMIT_LOGIN && userId <= 0){
			return;
		}
		long curtime = TimeHelper.getCurTime();
		UserTaskDaoHelper.saveExchangeGoods(context, userId, curtime, quantity);
	}
	
	/**
	 * @description: 统计游戏下载
	 *
	 * @param context
	 * @param gameId
	 * @param userId 
	 * @author: LiuQin
	 * @date: 2015年8月25日 下午12:59:07
	 */
	public static void saveGameDownload(Context context, String gameId, int userId){
		if(LIMIT_LOGIN && userId <= 0){
			return;
		}
		long startTime = 0;
		long endTime = 0;
		UserTaskDaoHelper.saveGameDownload(context, gameId, userId, startTime, endTime);
	}
	
	/**
	 * @description: 统计游戏搜索
	 *
	 * @param context
	 * @param gameId
	 * @param userId 
	 * @author: LiuQin
	 * @date: 2015年8月25日 下午12:59:17
	 */
	public static void saveGameSearchRecord(Context context, String gameId, int userId){
		if(LIMIT_LOGIN && userId <= 0){
			return;
		}
		long startTime = 0;
		long endTime = 0;
		UserTaskDaoHelper.saveGameSearchRecord(context, gameId, userId, startTime, endTime);
	}
	
	/**
	 * @description: 统计大厅在线时长
	 *
	 * @param context
	 * @param userId
	 * @param timeSum 
	 * @author: LiuQin
	 * @date: 2015年8月25日 下午1:00:05
	 */
	public static void saveMarketOnlineRecord(Context context, int userId, int timeSum){
		if(LIMIT_LOGIN && userId <= 0){
			return;
		}
		long startTime = 0;
		long endTime = 0;
		UserTaskDaoHelper.saveMarketOnlineRecord(context, userId, timeSum, startTime, endTime);
	}
	
	/**
	 * @description: 统计游戏在线时长
	 *
	 * @param context
	 * @param userId
	 * @param gameId
	 * @param timeSum 
	 * @author: LiuQin
	 * @date: 2015年8月25日 下午1:01:49
	 */
	public static void saveGameOnlineRecord(Context context, int userId, String gameId, int timeSum){
		if(LIMIT_LOGIN && userId <= 0){
			return;
		}
		long startTime = 0;
		long endTime = 0;
		UserTaskDaoHelper.saveGameOnlineRecord(context, userId, gameId, timeSum, startTime, endTime);
	}
	
	/**
	 * @description: 统计用户在游戏中登录
	 *
	 * @param context
	 * @param gameId
	 * @param userId 
	 * @author: LiuQin
	 * @date: 2015年8月25日 下午1:01:24
	 */
	public static void saveGameLoginRecord(Context context, String gameId, int userId){
		if(LIMIT_LOGIN && userId <= 0){
			return;
		}
		long startTime = 0;
		long endTime = 0;
		UserTaskDaoHelper.saveGameLoginRecord(context, gameId, userId, startTime, endTime);
	}
}
