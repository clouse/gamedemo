package com.atet.tvmarket.model.usertask;

import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.atet.tvmarket.entity.dao.UserTaskRecord;
import com.atet.tvmarket.entity.dao.UserTaskRecordDao;
import com.atet.tvmarket.model.DaoHelper;
import com.atet.tvmarket.model.TimeHelper;

import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;


public class UserTaskDaoHelper {

	private static void insertUserTaskRecord(Context context, UserTaskRecord userTaskRecord){
		UserTaskRecordDao dao = DaoHelper.getInstanse(context).getUserTaskRecordDao();
		dao.insert(userTaskRecord);
	}

	private static void updateUserTaskRecord(Context context, UserTaskRecord userTaskRecord){
		UserTaskRecordDao dao = DaoHelper.getInstanse(context).getUserTaskRecordDao();
		dao.update(userTaskRecord);
	}

	private static boolean isUserTaskRecordExist(Context context, String taskType, int userId){
		UserTaskRecordDao dao = DaoHelper.getInstanse(context).getUserTaskRecordDao();
		long count = dao.queryBuilder()
				.where(UserTaskRecordDao.Properties.Type.eq(taskType), UserTaskRecordDao.Properties.UserId.eq(userId))
				.buildCount().count();
		return count > 0;
	}

	private static boolean isUserTaskRecordExist(Context context, String taskType, int userId,
			int targetTimes, long startTime, long endTime, String gameId){
		return isUserTaskRecordExist(context, taskType, userId, targetTimes, startTime, endTime, gameId, null);
	}

	private static boolean isUserTaskRecordExist(Context context, String taskType, int userId,
			int targetTimes, long startTime, long endTime, String gameId, String str1){
		if(startTime == 0 || endTime == 0){
			long curtime;
			if(startTime > 0){
				curtime = startTime;
			} else {
				curtime = TimeHelper.getCurTime();
			}
			startTime = TimeHelper.getTodayStartMills(curtime); 
			endTime = TimeHelper.getTodayEndMills(startTime);
		}

		UserTaskRecordDao dao = DaoHelper.getInstanse(context).getUserTaskRecordDao();
		QueryBuilder<UserTaskRecord> qb;
		qb = dao.queryBuilder()
				.where(UserTaskRecordDao.Properties.Type.eq(taskType))
				.whereOr(UserTaskRecordDao.Properties.UserId.eq(userId), UserTaskRecordDao.Properties.UserId.eq(0))
				.where(UserTaskRecordDao.Properties.Time.ge(startTime), UserTaskRecordDao.Properties.Time.le(endTime));
		if(!TextUtils.isEmpty(gameId)){
			qb = qb.where(UserTaskRecordDao.Properties.GameId.eq(gameId));
		}
		if(!TextUtils.isEmpty(str1)){
			qb = qb.where(UserTaskRecordDao.Properties.Str1.eq(str1));
		}
		long count = qb.buildCount().count();
		return count >= targetTimes;
	}

	private static void delUserTaskRecord(Context context, String taskType, int userId){
		UserTaskRecordDao dao = DaoHelper.getInstanse(context).getUserTaskRecordDao();
		dao.queryBuilder()
		.where(UserTaskRecordDao.Properties.Type.eq(taskType), UserTaskRecordDao.Properties.UserId.eq(userId))
		.buildDelete().executeDeleteWithoutDetachingEntities();
	}

	private static void delUserTaskRecord(Context context, String taskType, int userId, long startTime, long endTime){
		delUserTaskRecord(context, taskType, userId, startTime, endTime, null);
	}

	private static void delUserTaskRecord(Context context, String taskType, int userId, long startTime, long endTime, String gameId){
		if(startTime == 0 || endTime == 0){
			long curtime;
			if(startTime > 0){
				curtime = startTime;
			} else {
				curtime = TimeHelper.getCurTime();
			}
			startTime = TimeHelper.getTodayStartMills(curtime); 
			endTime = TimeHelper.getTodayEndMills(startTime);
		}

		UserTaskRecordDao dao = DaoHelper.getInstanse(context).getUserTaskRecordDao();
		QueryBuilder<UserTaskRecord> qb= dao.queryBuilder()
				.where(UserTaskRecordDao.Properties.Type.eq(taskType))
				.whereOr(UserTaskRecordDao.Properties.UserId.eq(userId), UserTaskRecordDao.Properties.UserId.eq(0))
				.where(UserTaskRecordDao.Properties.Time.ge(startTime), UserTaskRecordDao.Properties.Time.le(endTime));
		if(!TextUtils.isEmpty(gameId)){
			qb=qb.where(UserTaskRecordDao.Properties.GameId.eq(gameId));
		}
		qb.buildDelete().executeDeleteWithoutDetachingEntities();
	}

	/* ============================================== */

	/**
	 * @description: 保存首次注册
	 * 
	 * @author: LiuQin
	 * @date: 2015年7月19日 下午8:11:33
	 */
	public static void saveFirstRegister(Context context, int userId, long curtime){
		String taskType = UserTaskInfo.TASK_TYPE_FIRST_REGEDISTER; 

		UserTaskRecord userTaskRecord= new UserTaskRecord();
		userTaskRecord.setType(taskType);
		userTaskRecord.setUserId(userId);
		userTaskRecord.setTime(curtime);

		insertUserTaskRecord(context, userTaskRecord);
	}

	/**
	 * @description: 是否完成首次注册
	 *
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月19日 下午8:32:38
	 */
	public static boolean isFinishFirstRegister(Context context, int userId){
		String taskType = UserTaskInfo.TASK_TYPE_FIRST_REGEDISTER; 
		return isUserTaskRecordExist(context, taskType, userId);
	}

	/**
	 * @description: 删除首次注册
	 *
	 * @param userId 
	 * @author: LiuQin
	 * @date: 2015年7月19日 下午8:44:49
	 */
	public static void delFirstRegister(Context context, int userId){
		String taskType = UserTaskInfo.TASK_TYPE_FIRST_REGEDISTER; 
		delUserTaskRecord(context, taskType, userId);
	}

	/* ============================================== */

	/**
	 * @description: 保存微信首次登录
	 * 
	 * @author: LiuQin
	 * @date: 2015年7月19日 下午8:11:33
	 */
	public static void saveWeChatFirstLogin(Context context, int userId, long curtime){
		String taskType = UserTaskInfo.TASK_TYPE_WECHAT_FIRST_LOGIN; 

		UserTaskRecord userTaskRecord= new UserTaskRecord();
		userTaskRecord.setType(taskType);
		userTaskRecord.setUserId(userId);
		userTaskRecord.setTime(curtime);

		insertUserTaskRecord(context, userTaskRecord);
	}

	/**
	 * @description: 是否完成微信首次登录
	 *
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月19日 下午8:32:38
	 */
	public static boolean isFinishWeChatFirstLogin(Context context, int userId){
		String taskType = UserTaskInfo.TASK_TYPE_WECHAT_FIRST_LOGIN; 
		return isUserTaskRecordExist(context, taskType, userId);
	}

	/**
	 * @description: 删除微信首次登录
	 *
	 * @param userId 
	 * @author: LiuQin
	 * @date: 2015年7月19日 下午8:44:49
	 */
	public static void delWeChatFirstRegister(Context context, int userId){
		String taskType = UserTaskInfo.TASK_TYPE_WECHAT_FIRST_LOGIN; 
		delUserTaskRecord(context, taskType, userId);
	}

	/* ============================================== */

	/**
	 * @description: 保存首次充值
	 * 
	 * @author: LiuQin
	 * @date: 2015年7月19日 下午8:11:33
	 */
	public static void saveFirstRecharge(Context context, int userId, long curtime){
		String taskType = UserTaskInfo.TASK_TYPE_FIRST_RECHARGE; 

		UserTaskRecord userTaskRecord= new UserTaskRecord();
		userTaskRecord.setType(taskType);
		userTaskRecord.setUserId(userId);
		userTaskRecord.setTime(curtime);

		insertUserTaskRecord(context, userTaskRecord);
	}

	/**
	 * @description: 是否完成首次充值
	 *
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月19日 下午8:32:38
	 */
	public static boolean isFinishFirstRecharge(Context context, int userId){
		String taskType = UserTaskInfo.TASK_TYPE_FIRST_RECHARGE; 
		return isUserTaskRecordExist(context, taskType, userId);
	}

	/**
	 * @description: 删除首次充值
	 *
	 * @param userId 
	 * @author: LiuQin
	 * @date: 2015年7月19日 下午8:44:49
	 */
	public static void delFirstRecharge(Context context, int userId){
		String taskType = UserTaskInfo.TASK_TYPE_FIRST_RECHARGE; 
		delUserTaskRecord(context, taskType, userId);
	}

	/* ============================================== */

	/**
	 * @description: 保存某次充值金额
	 * 
	 * @author: LiuQin
	 * @date: 2015年7月19日 下午8:11:33
	 */
	public static void saveRechargeSum(Context context, int userId, long curtime, int quantity){
		String taskType = UserTaskInfo.TASK_TYPE_RECHARGE_SUM; 

		UserTaskRecord userTaskRecord= new UserTaskRecord();
		userTaskRecord.setType(taskType);
		userTaskRecord.setUserId(userId);
		userTaskRecord.setTime(curtime);
		userTaskRecord.setInt1(quantity);

		insertUserTaskRecord(context, userTaskRecord);
	}

	/**
	 * @description: 是否完成累计充值
	 *
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月19日 下午8:32:38
	 */
	public static boolean isFinishRechargeSum(Context context, int userId, int targetSum){
		String taskType = UserTaskInfo.TASK_TYPE_RECHARGE_SUM; 

		UserTaskRecordDao dao = DaoHelper.getInstanse(context).getUserTaskRecordDao();
		Query<UserTaskRecord> qc = dao.queryBuilder()
				.where(UserTaskRecordDao.Properties.Type.eq(taskType),UserTaskRecordDao.Properties.UserId.eq(userId))
				.build();
		List<UserTaskRecord> infos = qc.list();

		int sum = 0;
		if(infos.size()>0){
			for (UserTaskRecord userTaskRecord : infos) {
				sum += userTaskRecord.getInt1();
			}
		}

		return sum >= targetSum;
	}

	//	/**
	//	 * @description: 删除累计充值
	//	 *
	//	 * @param userId 
	//	 * @author: LiuQin
	//	 * @date: 2015年7月19日 下午8:44:49
	//	 */
	//	public static void delRechargeSum(Context context, int userId){
	//		String taskType = UserTaskInfo.TASK_TYPE_RECHARGE_SUM; 
	//		delUserTaskRecord(context, taskType, userId);
	//	}

	/* ============================================== */

	/**
	 * @description: 保存商品兑换数量
	 * 
	 * @author: LiuQin
	 * @date: 2015年7月19日 下午8:11:33
	 */
	public static void saveExchangeGoods(Context context, int userId, long curtime, int quantity){
		String taskType = UserTaskInfo.TASK_TYPE_EXCHANGE_GOODS; 

		UserTaskRecord userTaskRecord= new UserTaskRecord();
		userTaskRecord.setType(taskType);
		userTaskRecord.setUserId(userId);
		userTaskRecord.setTime(curtime);
		userTaskRecord.setInt1(quantity);

		insertUserTaskRecord(context, userTaskRecord);
	}

	/**
	 * @description: 是否完成累计充值
	 *
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月19日 下午8:32:38
	 */
	public static boolean isFinishExchangeGoods(Context context, int userId, int targetSum){
		String taskType = UserTaskInfo.TASK_TYPE_EXCHANGE_GOODS; 

		UserTaskRecordDao dao = DaoHelper.getInstanse(context).getUserTaskRecordDao();
		Query<UserTaskRecord> qc = dao.queryBuilder()
				.where(UserTaskRecordDao.Properties.Type.eq(taskType),UserTaskRecordDao.Properties.UserId.eq(userId))
				.build();
		List<UserTaskRecord> infos = qc.list();

		int sum = 0;
		if(infos.size()>0){
			for (UserTaskRecord userTaskRecord : infos) {
				sum += userTaskRecord.getInt1();
			}
		}

		return sum >= targetSum;
	}

	/* ============================================== */

	/**
	 * @description: 保存游戏评分
	 *
	 * @param gameId
	 * @param userId
	 * @param curtime
	 * @param score 
	 * @author: LiuQin
	 * @date: 2015年7月20日 上午1:18:33
	 */
	public static void saveGameScore(Context context, String gameId, int userId, int score, long startTime, long endTime){
		String taskType = UserTaskInfo.TASK_TYPE_GAME_SCORE; 

		if(isGameSearch(context, taskType, userId, gameId, startTime, endTime)){
			//某段时间内已评分过该游戏，不重复记录
			return;
		}

		long curtime = TimeHelper.getCurTime();
		UserTaskRecord userTaskRecord= new UserTaskRecord();
		userTaskRecord.setType(taskType);
		userTaskRecord.setUserId(userId);
		userTaskRecord.setTime(curtime);
		userTaskRecord.setGameId(gameId);
		userTaskRecord.setInt1(score);

		insertUserTaskRecord(context, userTaskRecord);
	}

	/**
	 * @description: 指定时间内是否完成游戏评分任务
	 *
	 * @param context
	 * @param userId
	 * @param targetTimes
	 * @param startTime
	 * @param endTime
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月21日 下午6:33:23
	 */
	public static boolean isFinishGameScoreTask(Context context, int userId, int targetTimes, long startTime, long endTime){
		String taskType = UserTaskInfo.TASK_TYPE_GAME_SCORE; 
		return isUserTaskRecordExist(context, taskType, userId, targetTimes, startTime, endTime, null);
	}

	/**
	 * @description: 领取任务后删除对应的游戏评分记录
	 *
	 * @param context
	 * @param userId
	 * @param startTime
	 * @param endTime 
	 * @author: LiuQin
	 * @date: 2015年7月21日 下午6:32:08
	 */
	public static void delGameScoreRecord(Context context, int userId, long startTime, long endTime){
		String taskType = UserTaskInfo.TASK_TYPE_GAME_SCORE; 
		delUserTaskRecord(context, taskType, userId, startTime, endTime);
	}

	/**
	 * @description: 某款游戏一段时间内是否已经评过分
	 *
	 * @param context
	 * @param taskType
	 * @param userId
	 * @param gameId
	 * @param startTime
	 * @param endTime
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月21日 下午6:30:51
	 */
	private static boolean isGameScore(Context context, String taskType, int userId, String gameId, long startTime, long endTime){
		return isUserTaskRecordExist(context, taskType, userId, 1, startTime, endTime, gameId);
	}

	/* ============================================== */

	/**
	 * @description: 游戏下载记录
	 *
	 * @param context
	 * @param gameId
	 * @param userId
	 * @param curtime
	 * @param score 
	 * @author: LiuQin
	 * @date: 2015年7月20日 下午8:36:55
	 */
	public static void saveGameDownload(Context context, String gameId, int userId, long startTime, long endTime){
		String taskType = UserTaskInfo.TASK_TYPE_GAME_DOWNLOAD; 

		if(isGameDownload(context, taskType, userId, gameId, startTime, endTime)){
			//某段时间内已下载过该游戏，不重复记录
			return;
		}

		long curtime = TimeHelper.getCurTime();
		UserTaskRecord userTaskRecord= new UserTaskRecord();
		userTaskRecord.setType(taskType);
		userTaskRecord.setUserId(userId);
		userTaskRecord.setTime(curtime);
		userTaskRecord.setGameId(gameId);

		insertUserTaskRecord(context, userTaskRecord);
	}

	/**
	 * @description: 指定时间内是否完成游戏下载任务
	 *
	 * @param context
	 * @param userId
	 * @param targetTimes
	 * @param startTime
	 * @param endTime
	 * @return 
	 * @throws: 
	 * @author: LiuQin
	 * @date: 2015年7月21日 下午6:23:50
	 */
	public static boolean isFinishGameDownloadTask(Context context, int userId, int targetTimes, long startTime, long endTime, String gameId){
		String taskType = UserTaskInfo.TASK_TYPE_GAME_DOWNLOAD; 
		return isUserTaskRecordExist(context, taskType, userId, targetTimes, startTime, endTime, gameId);
	}

	/**
	 * @description: 领取任务后删除对应的游戏下载记录
	 *
	 * @param context
	 * @param userId
	 * @param startTime
	 * @param endTime 
	 * @throws: 
	 * @author: LiuQin
	 * @date: 2015年7月21日 下午6:22:40
	 */
	public static void delGameDownloadRecord(Context context, int userId, long startTime, long endTime, String gameId){
		String taskType = UserTaskInfo.TASK_TYPE_GAME_DOWNLOAD; 
		delUserTaskRecord(context, taskType, userId, startTime, endTime, gameId);
	}

	/**
	 * @description: 游戏在指定时间内是否下载过
	 *
	 * @param context
	 * @param taskType
	 * @param userId
	 * @param gameId
	 * @param startTime
	 * @param endTime
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月21日 下午6:21:20
	 */
	private static boolean isGameDownload(Context context, String taskType, int userId, String gameId, long startTime, long endTime){
		return isUserTaskRecordExist(context, taskType, userId, 1, startTime, endTime, gameId);
	}

	/* ============================================== */

	public static void saveWatchVideoRecordAsyn(final Context context, final String gameId, final int userId, 
			final String videoId, final long startTime, final long endTime){
		new Thread(new Runnable() {
			@Override
			public void run() {
				saveWatchVideoRecord(context, gameId, userId, videoId, startTime, endTime);
			}
		}).start();
	}

	/**
	 * @description: 保存观看视频记录
	 *
	 * @param context
	 * @param gameId
	 * @param userId
	 * @param curtime
	 * @throws: 
	 * @author: LiuQin
	 * @date: 2015年7月20日 下午10:19:24
	 */
	public static void saveWatchVideoRecord(Context context, String gameId, int userId, String videoId, long startTime, long endTime){
		String taskType = UserTaskInfo.TASK_TYPE_WATCH_VIDEO; 

		if(isVideoWatch(context, taskType, userId, videoId, startTime, endTime)){
			//指定时间内已观看过该视频，不重复记录
			return;
		}

		long curtime = TimeHelper.getCurTime();
		UserTaskRecord userTaskRecord= new UserTaskRecord();
		userTaskRecord.setType(taskType);
		userTaskRecord.setUserId(userId);
		userTaskRecord.setTime(curtime);
		userTaskRecord.setGameId(gameId);
		userTaskRecord.setStr1(videoId);

		insertUserTaskRecord(context, userTaskRecord);
	}

	/**
	 * @description: 指定时间内是否完成视频观看任务
	 *
	 * @param userId
	 * @param curtime
	 * @param targetTimes
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月20日 上午2:03:35
	 */
	public static boolean isFinishWatchVideoTask(Context context, int userId, int targetTimes, long startTime, long endTime, String gameId){
		String taskType = UserTaskInfo.TASK_TYPE_WATCH_VIDEO; 
		return isUserTaskRecordExist(context, taskType, userId, targetTimes, startTime, endTime, gameId);
	}

	/**
	 * @description: 领取任务后删除对应的视频观看记录
	 *
	 * @param userId
	 * @param curtime
	 * @author: LiuQin
	 * @date: 2015年7月20日 上午2:10:10
	 */
	public static void delWatchVideoRecord(Context context, int userId, long startTime, long endTime, String gameId){
		String taskType = UserTaskInfo.TASK_TYPE_WATCH_VIDEO; 
		delUserTaskRecord(context, taskType, userId, startTime, endTime, gameId);
	}

	/**
	 * @description: 指定时间内是否观看过该视频
	 *
	 * @param context
	 * @param taskType
	 * @param userId
	 * @param videoId
	 * @param startTime
	 * @param endTime
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月21日 下午6:12:07
	 */
	private static boolean isVideoWatch(Context context, String taskType, int userId, String videoId, long startTime, long endTime){
		return isUserTaskRecordExist(context, taskType, userId, 1, startTime, endTime, null, videoId);
	}

	/* ============================================== */

	/**
	 * @description: 保存游戏搜索记录
	 *
	 * @param context
	 * @param gameId
	 * @param userId
	 * @param curtime
	 * @author: LiuQin
	 * @date: 2015年7月20日 下午10:23:57
	 */
	public static void saveGameSearchRecord(Context context, String gameId, int userId, long startTime, long endTime){
		String taskType = UserTaskInfo.TASK_TYPE_GAME_SEARCH; 

		if(isGameSearch(context, taskType, userId, gameId, startTime, endTime)){
			//某段时间内已搜索过该游戏，不重复记录
			return;
		}

		long curtime = TimeHelper.getCurTime();
		UserTaskRecord userTaskRecord= new UserTaskRecord();
		userTaskRecord.setType(taskType);
		userTaskRecord.setUserId(userId);
		userTaskRecord.setTime(curtime);
		userTaskRecord.setGameId(gameId);

		insertUserTaskRecord(context, userTaskRecord);
	}

	/**
	 * @description: 指定时间内是否完成游戏搜索任务
	 *
	 * @param context
	 * @param userId 
	 * @param targetimes 搜索次数
	 * @param startTime 搜索开始时间 
	 * @param endTime 搜索结束时间
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月21日 下午4:21:55
	 */
	public static boolean isFinishGameSearchTask(Context context, int userId, int targetTimes, long startTime, long endTime){
		String taskType = UserTaskInfo.TASK_TYPE_GAME_SEARCH; 
		return isUserTaskRecordExist(context, taskType, userId, targetTimes, startTime, endTime, null);
	}

	/**
	 * @description: 领取任务后删除对应的游戏搜索记录
	 *
	 * @param context
	 * @param userId
	 * @param curtime 
	 * @author: LiuQin
	 * @date: 2015年7月21日 下午5:02:08
	 */
	public static void delGameSearchRecord(Context context, int userId, long startTime, long endTime){
		String taskType = UserTaskInfo.TASK_TYPE_GAME_SEARCH; 
		delUserTaskRecord(context, taskType, userId, startTime, endTime);
	}

	/**
	 * @description: 某款游戏一段时间内是否已经搜索过
	 *
	 * @param context
	 * @param taskType
	 * @param userId
	 * @param curtime
	 * @param gameId
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月21日 下午3:52:40
	 */
	private static boolean isGameSearch(Context context, String taskType, int userId, String gameId, long startTime, long endTime){
		return isUserTaskRecordExist(context, taskType, userId, 1, startTime, endTime, gameId);
	}

	/* ============================================== */

	/**
	 * @description: 保存大厅在线时长
	 *
	 * @param context
	 * @param gameId
	 * @param userId
	 * @param curtime
	 * @author: LiuQin
	 * @date: 2015年7月20日 下午10:23:57
	 */
	public static void saveMarketOnlineRecord(Context context, int userId, int timeSum,long startTime, long endTime){
		String taskType = UserTaskInfo.TASK_TYPE_MARKET_ONLINE; 

		boolean isInsert= false;
		UserTaskRecord userTaskRecord=getMarketOnlineUserTaskRecord(context, taskType, userId, startTime, endTime);
		if(userTaskRecord==null){
			userTaskRecord= new UserTaskRecord();
			long curtime = TimeHelper.getCurTime();
			userTaskRecord.setType(taskType);
			userTaskRecord.setUserId(userId);
			userTaskRecord.setTime(curtime);
			userTaskRecord.setInt1(timeSum);
			isInsert = true;
		} else {
			// 更新已有的在线时间
			userTaskRecord.setInt1(userTaskRecord.getInt1() + timeSum);
		}

		if(isInsert){
			insertUserTaskRecord(context, userTaskRecord);
		} else {
			updateUserTaskRecord(context, userTaskRecord);
		}
	}

	/**
	 * @description: 某段时间内是否存在大厅在线时长记录
	 *
	 * @param context
	 * @param taskType
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月22日 上午9:56:49
	 */
	private static UserTaskRecord getMarketOnlineUserTaskRecord(Context context, String taskType, int userId, long startTime, long endTime){
		UserTaskRecordDao dao = DaoHelper.getInstanse(context).getUserTaskRecordDao();
		Query<UserTaskRecord> qc = dao.queryBuilder()
				.where(UserTaskRecordDao.Properties.Type.eq(taskType))
				.whereOr(UserTaskRecordDao.Properties.UserId.eq(0), UserTaskRecordDao.Properties.UserId.eq(userId))
				.where(UserTaskRecordDao.Properties.Time.ge(startTime), UserTaskRecordDao.Properties.Time.le(endTime))
				.build();
		if(qc.list().isEmpty()){
			return null;
		}
		return qc.list().get(0);
	}

	/**
	 * @description: 指定时间内是否完成大厅在线任务
	 *
	 * @param context
	 * @param userId 
	 * @param targetimes 搜索次数
	 * @param startTime 搜索开始时间 
	 * @param endTime 搜索结束时间
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月21日 下午4:21:55
	 */
	public static boolean isFinishMarketOnlineTask(Context context, int userId, int targetTimeMin, long startTime, long endTime){
		boolean result = false;
		String taskType = UserTaskInfo.TASK_TYPE_MARKET_ONLINE; 
		UserTaskRecord userTaskRecord=getMarketOnlineUserTaskRecord(context, taskType, userId, startTime, endTime);
		if(userTaskRecord != null && userTaskRecord.getInt1() >= targetTimeMin){
			result = true;
		}
		return result;
	}

	/**
	 * @description: 领取任务后删除对应的大厅在线记录
	 *
	 * @param context
	 * @param userId
	 * @param curtime 
	 * @author: LiuQin
	 * @date: 2015年7月21日 下午5:02:08
	 */
	public static void delMarketOnlineRecord(Context context, int userId, long startTime, long endTime){
		String taskType = UserTaskInfo.TASK_TYPE_MARKET_ONLINE; 
		delUserTaskRecord(context, taskType, userId, startTime, endTime);
	}

	/* ============================================== */

	/**
	 * @description: 保存游戏在线时长
	 *
	 * @param context
	 * @param gameId
	 * @param userId
	 * @param curtime
	 * @author: LiuQin
	 * @date: 2015年7月20日 下午10:23:57
	 */
	public static void saveGameOnlineRecord(Context context, int userId, String gameId, int timeSum,long startTime, long endTime){
		String taskType = UserTaskInfo.TASK_TYPE_GAME_ONLINE; 

		boolean isInsert= false;
		List<UserTaskRecord> userTaskRecordList=getGameOnlineUserTaskRecord(context, taskType, userId, gameId, startTime, endTime);
		UserTaskRecord userTaskRecord=null;
		if(userTaskRecordList!=null && userTaskRecordList.size()>0){
			userTaskRecord = userTaskRecordList.get(0);
		}
		if(userTaskRecord==null){
			userTaskRecord= new UserTaskRecord();
			long curtime = TimeHelper.getCurTime();
			userTaskRecord.setType(taskType);
			userTaskRecord.setGameId(gameId);
			userTaskRecord.setUserId(userId);
			userTaskRecord.setTime(curtime);
			userTaskRecord.setInt1(timeSum);
			isInsert = true;
		} else {
			// 更新已有的在线时间
			userTaskRecord.setInt1(userTaskRecord.getInt1() + timeSum);
		}

		if(isInsert){
			insertUserTaskRecord(context, userTaskRecord);
		} else {
			updateUserTaskRecord(context, userTaskRecord);
		}
	}

	/**
	 * @description: 某段时间内是否存在游戏在线时长记录
	 *
	 * @param context
	 * @param taskType
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月22日 上午9:56:49
	 */
	private static List<UserTaskRecord> getGameOnlineUserTaskRecord(Context context, String taskType, int userId, String gameId, long startTime, long endTime){
		if(startTime == 0 || endTime == 0){
			long curtime;
			if(startTime > 0){
				curtime = startTime;
			} else {
				curtime = TimeHelper.getCurTime();
			}
			startTime = TimeHelper.getTodayStartMills(curtime); 
			endTime = TimeHelper.getTodayEndMills(startTime);
		}
		
		UserTaskRecordDao dao = DaoHelper.getInstanse(context).getUserTaskRecordDao();
		QueryBuilder<UserTaskRecord> qb= dao.queryBuilder()
				.where(UserTaskRecordDao.Properties.Type.eq(taskType))
				.whereOr(UserTaskRecordDao.Properties.UserId.eq(userId), UserTaskRecordDao.Properties.UserId.eq(0))
				.where(UserTaskRecordDao.Properties.Time.ge(startTime), UserTaskRecordDao.Properties.Time.le(endTime));
		if(!TextUtils.isEmpty(gameId)){
			qb=qb.where(UserTaskRecordDao.Properties.GameId.eq(gameId));
		}
		return qb.build().list();
	}
	
	/**
	 * @description: 指定时间内是否完成游戏在线任务
	 *
	 * @param context
	 * @param userId 
	 * @param targetimes 搜索次数
	 * @param startTime 搜索开始时间 
	 * @param endTime 搜索结束时间
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月21日 下午4:21:55
	 */
	public static boolean isFinishGameOnlineTask(Context context, int userId, String gameId, int targetTimeMin, long startTime, long endTime){
		boolean result = false;
		String taskType = UserTaskInfo.TASK_TYPE_GAME_ONLINE; 
		List<UserTaskRecord> userTaskRecordList=getGameOnlineUserTaskRecord(context, taskType, userId, gameId, startTime, endTime);
		if(userTaskRecordList!=null && userTaskRecordList.size()>0){
			int totalMin = 0;
			for (UserTaskRecord userTaskRecord : userTaskRecordList) {
//				if(userTaskRecord.getInt1() >= targetTimeMin){
//					result = true;
//					break;
//				}
				totalMin += userTaskRecord.getInt1();
			}
			if(totalMin >= targetTimeMin){
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * @description: 领取任务后删除对应的游戏在线记录
	 *
	 * @param context
	 * @param userId
	 * @param curtime 
	 * @author: LiuQin
	 * @date: 2015年7月21日 下午5:02:08
	 */
	public static void delGameOnlineRecord(Context context, int userId, String gameId, long startTime, long endTime){
		String taskType = UserTaskInfo.TASK_TYPE_GAME_ONLINE; 
		delUserTaskRecord(context, taskType, userId, startTime, endTime, gameId);
	}
	
	/**
	 * @description: 删除无效的记录
	 *
	 * @param context
	 * @param startTime 
	 * @author: LiuQin
	 * @date: 2015年10月21日 下午6:01:16
	 */
	public static void delGameOnlineInvalid(Context context){
		if(!TimeHelper.isServerTimeSyn()){
			return;
		}
		long startTime = TimeHelper.getTodayStartMills(TimeHelper.getCurTime()) - 1000;
		String taskType = UserTaskInfo.TASK_TYPE_GAME_ONLINE; 

		try {
			UserTaskRecordDao dao = DaoHelper.getInstanse(context).getUserTaskRecordDao();
			QueryBuilder<UserTaskRecord> qb= dao.queryBuilder()
					.where(UserTaskRecordDao.Properties.Type.eq(taskType), UserTaskRecordDao.Properties.Time.lt(startTime));
			qb.buildDelete().executeDeleteWithoutDetachingEntities();
		} catch (Exception e) {
		}
	}
	
	/* ============================================== */
	
	/**
	 * @description: 保存游戏登录记录
	 *
	 * @param context
	 * @param gameId
	 * @param userId
	 * @param curtime
	 * @author: LiuQin
	 * @date: 2015年7月20日 下午10:23:57
	 */
	public static void saveGameLoginRecord(Context context, String gameId, int userId, long startTime, long endTime){
		String taskType = UserTaskInfo.TASK_TYPE_GAME_LOGIN; 
		
//		if(isGameLogin(context, taskType, userId, gameId, startTime, endTime)){
			//某段时间内已登录过该游戏，不重复记录
//			return;
//		}
		
		long curtime = TimeHelper.getCurTime();
		UserTaskRecord userTaskRecord= new UserTaskRecord();
		userTaskRecord.setType(taskType);
		userTaskRecord.setUserId(userId);
		userTaskRecord.setTime(curtime);
		userTaskRecord.setGameId(gameId);
		
		insertUserTaskRecord(context, userTaskRecord);
	}
	
	/**
	 * @description: 指定时间内是否完成游戏登录任务
	 *
	 * @param context
	 * @param userId 
	 * @param targetimes 搜索次数
	 * @param startTime 搜索开始时间 
	 * @param endTime 搜索结束时间
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月21日 下午4:21:55
	 */
	public static boolean isFinishGameLoginTask(Context context, int userId, String gameId, int targetTimes, long startTime, long endTime){
		String taskType = UserTaskInfo.TASK_TYPE_GAME_LOGIN; 
		return isUserTaskRecordExist(context, taskType, userId, targetTimes, startTime, endTime, gameId);
	}
	
	/**
	 * @description: 领取任务后删除对应的游戏搜索记录
	 *
	 * @param context
	 * @param userId
	 * @param curtime 
	 * @author: LiuQin
	 * @date: 2015年7月21日 下午5:02:08
	 */
	public static void delGameLoginRecord(Context context, int userId, String gameId, long startTime, long endTime){
		String taskType = UserTaskInfo.TASK_TYPE_GAME_LOGIN; 
		delUserTaskRecord(context, taskType, userId, startTime, endTime, gameId);
	}
	
	/**
	 * @description: 某款游戏一段时间内是否已经登录过
	 *
	 * @param context
	 * @param taskType
	 * @param userId
	 * @param curtime
	 * @param gameId
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月21日 下午3:52:40
	 */
	private static boolean isGameLogin(Context context, String taskType, int userId, String gameId, long startTime, long endTime){
		return isUserTaskRecordExist(context, taskType, userId, 1, startTime, endTime, gameId);
	}
	
	/**
	 * @description: 获取每日签到记录
	 *
	 * @param context
	 * @param taskType
	 * @param userId
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月25日 上午9:54:19
	 */
	public static UserTaskRecord getCheckInDailyRecord(Context context, int userId){
		String taskType = UserTaskInfo.TASK_TYPE_CHECKIN_DAILY; 
		UserTaskRecordDao dao = DaoHelper.getInstanse(context).getUserTaskRecordDao();
		Query<UserTaskRecord> qc = dao.queryBuilder()
				.where(UserTaskRecordDao.Properties.Type.eq(taskType), UserTaskRecordDao.Properties.UserId.eq(userId))
				.build();
		return qc.unique();
	}
	
	/**
	 * @description: 保存每日签到记录
	 *
	 * @param context
	 * @param userId
	 * @param signValue 
	 * @author: LiuQin
	 * @date: 2015年8月25日 上午10:03:38
	 */
	public static void saveCheckInDailyRecord(Context context, int userId, int signValue, int weekOfYear) {
		UserTaskRecord record = null;
		record = getCheckInDailyRecord(context, userId);
		if(record == null){
			record = new UserTaskRecord();
			record.setType(UserTaskInfo.TASK_TYPE_CHECKIN_DAILY);
			record.setUserId(userId);
			record.setInt1(signValue);
			record.setInt2(weekOfYear);
			insertUserTaskRecord(context, record);
		} else {
			record.setUserId(userId);
			record.setInt1(signValue);
			record.setInt2(weekOfYear);
			updateUserTaskRecord(context, record);
		}
	}
}
