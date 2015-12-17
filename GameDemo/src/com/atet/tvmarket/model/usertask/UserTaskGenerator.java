package com.atet.tvmarket.model.usertask;

import android.content.Context;

import com.atet.tvmarket.model.SpHelper;
import com.atet.tvmarket.model.usertask.UserTaskInfo.UserTaskState;
import com.atet.tvmarket.model.usertask.UserTaskInfo.UserTaskType;

public class UserTaskGenerator {
	private static final boolean TELL_FROM_SERVER = true;
	
	/**
	 * @description: 首次注册任务信息
	 *
	 * @param taskId
	 * @param taskName
	 * @param taskRemark
	 * @param taskStateRemark
	 * @param taskState
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月19日 下午8:54:21
	 */
	public static UserTaskInfo makeFirstRegisterTask(Context context, int userId, String taskId, String taskName, 
			String taskRemark, String taskStateRemark, UserTaskState taskState){
		
		if(taskState==UserTaskState.TASK_STATE_NOT_FINISH && userId > 0){
			// 服务器记录未完成，检查本地是否已完成。如果全部由服务器记录，则不需要本地判断
//			boolean isFinish = UserTaskDaoHelper.isFinishFirstRegister(context, userId);
//			if(isFinish){
//				taskState = UserTaskState.TASK_STATE_STANDBY;
//			}
			taskState = UserTaskState.TASK_STATE_STANDBY;
		}

		UserTaskType taskType = UserTaskType.TASK_TYPE_FIRST_REGEDISTER;
		UserTaskInfo info = UserTaskInfo.makeUserTaskInfo(taskType, taskId, taskName, taskRemark, 
				taskState, taskStateRemark, userId);
		
		return info;
	}
	
	
	/**
	 * @description: 微信首次登录任务
	 *
	 * @param context
	 * @param userId
	 * @param taskId
	 * @param taskName
	 * @param taskRemark
	 * @param taskStateRemark
	 * @param taskState
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月20日 下午8:25:34
	 */
	public static UserTaskInfo makeWeChatFirstLoginTask(Context context, int userId, String taskId, String taskName, 
			String taskRemark, String taskStateRemark, UserTaskState taskState){
		
//		if(!TELL_FROM_SERVER){
			if(taskState==UserTaskState.TASK_STATE_NOT_FINISH && userId > 0){
				// 服务器记录未完成，检查本地是否已完成。如果全部由服务器记录，则不需要本地判断
				boolean isFinish = UserTaskDaoHelper.isFinishWeChatFirstLogin(context, userId);
				if(isFinish){
					taskState = UserTaskState.TASK_STATE_STANDBY;
				}
			}
//		}
		
		UserTaskType taskType = UserTaskType.TASK_TYPE_WECHAT_FIRST_LOGIN;
		UserTaskInfo info = UserTaskInfo.makeUserTaskInfo(taskType, taskId, taskName, taskRemark, 
				taskState, taskStateRemark, userId);
		
		return info;
	}
	
	/**
	 * @description: 首次充值任务
	 *
	 * @param context
	 * @param userId
	 * @param taskId
	 * @param taskName
	 * @param taskRemark
	 * @param taskStateRemark
	 * @param taskState
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月20日 下午8:28:19
	 */
	public static UserTaskInfo makeFirstRechargeTask(Context context, int userId, String taskId, String taskName, 
			String taskRemark, String taskStateRemark, UserTaskState taskState){
		
		if(!TELL_FROM_SERVER){
			if(taskState==UserTaskState.TASK_STATE_NOT_FINISH && userId > 0){
				// 服务器记录未完成，检查本地是否已完成。如果全部由服务器记录，则不需要本地判断
				boolean isFinish = UserTaskDaoHelper.isFinishFirstRecharge(context, userId);
				if(isFinish){
					taskState = UserTaskState.TASK_STATE_STANDBY;
				}
			}
		}
		
		UserTaskType taskType = UserTaskType.TASK_TYPE_FIRST_RECHARGE;
		UserTaskInfo info = UserTaskInfo.makeUserTaskInfo(taskType, taskId, taskName, taskRemark, 
				taskState, taskStateRemark, userId);
		
		return info;
	}
	
	/**
	 * @description: 累计充值任务
	 *
	 * @param context
	 * @param userId
	 * @param taskId
	 * @param taskName
	 * @param taskRemark
	 * @param taskStateRemark
	 * @param taskState
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月20日 下午8:28:19
	 */
	public static UserTaskInfo makeRechargeSumTask(Context context, int userId, String taskId, String taskName, 
			String taskRemark, String taskStateRemark, UserTaskState taskState, int targetSum){
		
		if(!TELL_FROM_SERVER){
			if(taskState==UserTaskState.TASK_STATE_NOT_FINISH && userId > 0){
				// 服务器记录未完成，检查本地是否已完成。如果全部由服务器记录，则不需要本地判断
				boolean isFinish = UserTaskDaoHelper.isFinishRechargeSum(context, userId, targetSum);
				if(isFinish){
					taskState = UserTaskState.TASK_STATE_STANDBY;
				}
			}
		}

		UserTaskType taskType = UserTaskType.TASK_TYPE_RECHARGE_SUM;
		UserTaskInfo info = UserTaskInfo.makeUserTaskInfo(taskType, taskId, taskName, taskRemark, 
				taskState, taskStateRemark, userId);
		
		return info;
	}
	
	/**
	 * @description: 兑换商品
	 *
	 * @param context
	 * @param userId
	 * @param taskId
	 * @param taskName
	 * @param taskRemark
	 * @param taskStateRemark
	 * @param taskState
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月20日 下午8:28:19
	 */
	public static UserTaskInfo makeExchangeGoodsTask(Context context, int userId, String taskId, String taskName, 
			String taskRemark, String taskStateRemark, UserTaskState taskState, int targetSum){
		
		if(!TELL_FROM_SERVER){
			if(taskState==UserTaskState.TASK_STATE_NOT_FINISH && userId > 0){
				// 服务器记录未完成，检查本地是否已完成。如果全部由服务器记录，则不需要本地判断
				boolean isFinish = UserTaskDaoHelper.isFinishRechargeSum(context, userId, targetSum);
				if(isFinish){
					taskState = UserTaskState.TASK_STATE_STANDBY;
				}
			}
		}
		
		UserTaskType taskType = UserTaskType.TASK_TYPE_EXCHANGE_GOODS;
		UserTaskInfo info = UserTaskInfo.makeUserTaskInfo(taskType, taskId, taskName, taskRemark, 
				taskState, taskStateRemark, userId);
		
		return info;
	}
	
	/**
	 * @description: 游戏评分任务
	 *
	 * @param context
	 * @param userId
	 * @param taskId
	 * @param taskName
	 * @param taskRemark
	 * @param taskStateRemark
	 * @param taskState
	 * @param targetTimes 评分次数
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月20日 下午8:29:42
	 */
	public static UserTaskInfo makeGameScoreTask(Context context, int userId, String taskId, String taskName, 
			String taskRemark, String taskStateRemark, UserTaskState taskState, int targetTimes, long startTime, long endTime){
		
		if(taskState==UserTaskState.TASK_STATE_NOT_FINISH){
			boolean isFinish = UserTaskDaoHelper.isFinishGameScoreTask(context, userId, targetTimes, startTime, endTime);
			if(isFinish){
				taskState = UserTaskState.TASK_STATE_STANDBY;
			}
		}
		
		UserTaskType taskType = UserTaskType.TASK_TYPE_GAME_SCORE;
		UserTaskInfo info = UserTaskInfo.makeUserTaskInfo(taskType, taskId, taskName, taskRemark, 
				taskState, taskStateRemark, userId);
		
		return info;
	}
	
	/**
	 * @description: 游戏下载任务
	 *
	 * @param context
	 * @param userId
	 * @param taskId
	 * @param taskName
	 * @param taskRemark
	 * @param taskStateRemark
	 * @param taskState
	 * @param targetTimes
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月20日 下午9:48:51
	 */
	public static UserTaskInfo makeGameDownloadTask(Context context, int userId, String taskId, String taskName, 
			String taskRemark, String taskStateRemark, UserTaskState taskState, int targetTimes, long startTime, long endTime, String gameId){
		
		if(taskState==UserTaskState.TASK_STATE_NOT_FINISH){
			boolean isFinish = UserTaskDaoHelper.isFinishGameDownloadTask(context, userId, targetTimes, startTime, endTime, gameId);
			if(isFinish){
				taskState = UserTaskState.TASK_STATE_STANDBY;
			}
		}
		
		UserTaskType taskType = UserTaskType.TASK_TYPE_GAME_DOWNLOAD;
		UserTaskInfo info = UserTaskInfo.makeUserTaskInfo(taskType, taskId, taskName, taskRemark, 
				taskState, taskStateRemark, userId);
		info.setGameId(gameId);
		
		return info;
	}
	
	/**
	 * @description: 视频观看任务
	 *
	 * @param context
	 * @param userId
	 * @param taskId
	 * @param taskName
	 * @param taskRemark
	 * @param taskStateRemark
	 * @param taskState
	 * @param targetTimes
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月20日 下午10:22:02
	 */
	public static UserTaskInfo makeWatchVideoTask(Context context, int userId, String taskId, String taskName, 
			String taskRemark, String taskStateRemark, UserTaskState taskState, int targetTimes, long startTime, long endTime, String gameId){
		
		if(taskState==UserTaskState.TASK_STATE_NOT_FINISH){
			boolean isFinish = UserTaskDaoHelper.isFinishWatchVideoTask(context, userId, targetTimes, startTime, endTime, gameId);
			if(isFinish){
				taskState = UserTaskState.TASK_STATE_STANDBY;
			}
		}
		
		UserTaskType taskType = UserTaskType.TASK_TYPE_WATCH_VIDEO;
		UserTaskInfo info = UserTaskInfo.makeUserTaskInfo(taskType, taskId, taskName, taskRemark, 
				taskState, taskStateRemark, userId);
		info.setGameId(gameId);
		
		return info;
	}
	
	
	/**
	 * @description: 游戏搜索
	 *
	 * @param context
	 * @param userId
	 * @param taskId
	 * @param taskName
	 * @param taskRemark
	 * @param taskStateRemark
	 * @param taskState
	 * @param targetTimes
	 * @param startTime
	 * @param endTime
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月22日 上午10:40:22
	 */
	public static UserTaskInfo makeGameSearchTask(Context context, int userId, String taskId, String taskName, 
			String taskRemark, String taskStateRemark, UserTaskState taskState, int targetTimes, long startTime, long endTime){
		
		if(taskState==UserTaskState.TASK_STATE_NOT_FINISH){
			boolean isFinish = UserTaskDaoHelper.isFinishGameSearchTask(context, userId, targetTimes, startTime, endTime);
			if(isFinish){
				taskState = UserTaskState.TASK_STATE_STANDBY;
			}
		}
		
		UserTaskType taskType = UserTaskType.TASK_TYPE_GAME_SEARCH;
		UserTaskInfo info = UserTaskInfo.makeUserTaskInfo(taskType, taskId, taskName, taskRemark, 
				taskState, taskStateRemark, userId);
		
		return info;
	}
	
	/**
	 * @description: 大厅在线时长
	 *
	 * @param context
	 * @param userId
	 * @param taskId
	 * @param taskName
	 * @param taskRemark
	 * @param taskStateRemark
	 * @param taskState
	 * @param targetTimeMin
	 * @param startTime
	 * @param endTime
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月22日 上午10:42:10
	 */
	public static UserTaskInfo makeMarketOnlineTask(Context context, int userId, String taskId, String taskName, 
			String taskRemark, String taskStateRemark, UserTaskState taskState, int targetTimeMin, long startTime, long endTime){
		
		if(taskState==UserTaskState.TASK_STATE_NOT_FINISH){
			boolean isFinish = UserTaskDaoHelper.isFinishMarketOnlineTask(context, userId, targetTimeMin, startTime, endTime);
			if(isFinish){
				taskState = UserTaskState.TASK_STATE_STANDBY;
			}
		}
		
		UserTaskType taskType = UserTaskType.TASK_TYPE_GAME_ONLINE;
		UserTaskInfo info = UserTaskInfo.makeUserTaskInfo(taskType, taskId, taskName, taskRemark, 
				taskState, taskStateRemark, userId);
		
		return info;
	}
	
	/**
	 * @description: 游戏在线时长
	 *
	 * @param context
	 * @param userId
	 * @param taskId
	 * @param taskName
	 * @param taskRemark
	 * @param taskStateRemark
	 * @param taskState
	 * @param gameId
	 * @param targetTimeMin
	 * @param startTime
	 * @param endTime
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月22日 上午10:44:14
	 */
	public static UserTaskInfo makeGameOnlineTask(Context context, int userId, String taskId, String taskName, 
			String taskRemark, String taskStateRemark, UserTaskState taskState, int targetTimeMin, long startTime, long endTime, String gameId){
		
		if(taskState==UserTaskState.TASK_STATE_NOT_FINISH){
			boolean isFinish = UserTaskDaoHelper.isFinishGameOnlineTask(context, userId, gameId, targetTimeMin, startTime, endTime);
			if(isFinish){
				taskState = UserTaskState.TASK_STATE_STANDBY;
			}
		}
		
		UserTaskType taskType = UserTaskType.TASK_TYPE_GAME_ONLINE;
		UserTaskInfo info = UserTaskInfo.makeUserTaskInfo(taskType, taskId, taskName, taskRemark, 
				taskState, taskStateRemark, userId);
		info.setGameId(gameId);
		
		return info;
	}
	
	/**
	 * @description: 游戏登录
	 *
	 * @param context
	 * @param userId
	 * @param taskId
	 * @param taskName
	 * @param taskRemark
	 * @param taskStateRemark
	 * @param taskState
	 * @param targetTimes
	 * @param startTime
	 * @param endTime
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月22日 上午10:45:36
	 */
	public static UserTaskInfo makeGameLoginTask(Context context, int userId, String taskId, String taskName, 
			String taskRemark, String taskStateRemark, UserTaskState taskState, int targetTimes, long startTime, long endTime, String gameId){
		
		if(taskState==UserTaskState.TASK_STATE_NOT_FINISH){
			boolean isFinish = UserTaskDaoHelper.isFinishGameLoginTask(context, userId, gameId, targetTimes, startTime, endTime);
			if(isFinish){
				taskState = UserTaskState.TASK_STATE_STANDBY;
			}
		}
		
		UserTaskType taskType = UserTaskType.TASK_TYPE_GAME_LOGIN;
		UserTaskInfo info = UserTaskInfo.makeUserTaskInfo(taskType, taskId, taskName, taskRemark, 
				taskState, taskStateRemark, userId);
		info.setGameId(gameId);
		
		return info;
	}
	
	/**
	 * @description: 连续签到任务
	 *
	 * @param context
	 * @param userId
	 * @param taskId
	 * @param taskName
	 * @param taskRemark
	 * @param taskStateRemark
	 * @param taskState
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月23日 下午6:48:03
	 */
	public static CheckInTaskInfo makeCheckInContinuelyTask(Context context, int userId, String taskId, String taskName, 
			String taskRemark, String taskStateRemark, UserTaskState taskState){
		
		UserTaskType taskType = UserTaskType.TASK_TYPE_MARKET_LOGIN;
//		if(taskState == UserTaskState.TASK_STATE_NOT_FINISH){
//			taskState = UserTaskState.TASK_STATE_STANDBY;
//		}
		CheckInTaskInfo info = CheckInTaskInfo.makeCheckInTaskInfo(taskType, taskId, taskName, taskRemark, 
				taskState, taskStateRemark, userId);
		
		return info;
	}
	
	/**
	 * @description: 每日签到任务
	 *
	 * @param context
	 * @param userId
	 * @param taskId
	 * @param taskName
	 * @param taskRemark
	 * @param taskStateRemark
	 * @param taskState
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月23日 下午6:57:40
	 */
	public static UserTaskInfo makeCheckInDailyTask(Context context, int userId, String taskId, String taskName, 
			String taskRemark, String taskStateRemark, UserTaskState taskState){
		
		UserTaskType taskType = UserTaskType.TASK_TYPE_CHECKIN_DAILY;
//		if(taskState == UserTaskState.TASK_STATE_NOT_FINISH){
//			taskState = UserTaskState.TASK_STATE_STANDBY;
//		}
		CheckInTaskInfo info = CheckInTaskInfo.makeCheckInTaskInfo(taskType, taskId, taskName, taskRemark, 
				taskState, taskStateRemark, userId);
		info.initSignValue(context);
		
		return info;
	}
}
