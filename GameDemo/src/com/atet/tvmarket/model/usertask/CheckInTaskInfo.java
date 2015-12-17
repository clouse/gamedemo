package com.atet.tvmarket.model.usertask;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.atet.tvmarket.entity.dao.UserTaskRecord;
import com.atet.tvmarket.model.TimeHelper;

/**
 * @description: 每日签到任务
 *
 * @author: LiuQin
 */
public class CheckInTaskInfo extends UserTaskInfo{
	// 周一至周日
	public static final int MON = 1;
	public static final int TUE = 2;
	public static final int WED = 3;
	public static final int THR = 4;
	public static final int FRI = 5;
	public static final int SAT = 6;
	public static final int SUN = 7;

	/**
	 * @description：签到值，记录从周一到周日是否签到
	 * 				按位计算：第1位周一，第7位周日
	 * 				0x1当天已签到，0x0当天没签到
	 */
	private int signValue;
	
	private int weekOfYear;
	
	//连续签到每天的积分
	private List<Integer> integralList;

	public int getSignValue() {
		return signValue;
	}

	public void setSignValue(int signValue) {
		this.signValue = signValue;
	}
	
	public int getWeekOfYear() {
		return weekOfYear;
	}

	public void setWeekOfYear(int weekOfYear) {
		this.weekOfYear = weekOfYear;
	}
	
	/**
	 * @description: 检测某天是否签到，周一传MON，周日传SUN
	 *
	 * @param day
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月19日 下午6:54:02
	 */
	public boolean isCheckIn(int day){
		return ((signValue >> (day - 1)) & 0x1 ) == 0x1;
	}
	
	public void checkIn(int day){
		signValue |= (0x1 << (day - 1));
	}
	
	public static CheckInTaskInfo makeCheckInTaskInfo(UserTaskType taskType, String taskId, String taskName, 
			String taskRemark, UserTaskState taskState, String taskStateRemark, int userId){
		CheckInTaskInfo info = new CheckInTaskInfo();
		info.setTaskType(taskType);
		info.setTaskId(taskId);
		info.setTaskName(taskName);
		info.setTaskRemark(taskRemark);
		info.setTaskStateRemark(taskStateRemark);
		info.setTaskState(taskState);
		info.setUserId(userId);
		return info;
	}
	
	public void initSignValue(Context context){
		long curTime = TimeHelper.getCurTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(curTime);
		int curWeekOfYear=calendar.get(Calendar.WEEK_OF_YEAR);
		
		this.signValue = 0;
		this.weekOfYear = curWeekOfYear;
		UserTaskRecord record = UserTaskDaoHelper.getCheckInDailyRecord(context, getUserId());
		if(record != null && record.getInt2() == curWeekOfYear){
			//同一周内
			this.signValue = record.getInt1();
			this.weekOfYear = record.getInt2();
		}
	}
	
	/**
	 * @description: 记录当天签到至文件
	 *
	 * @param context 
	 * @author: LiuQin
	 * @date: 2015年8月23日 下午8:14:53
	 */
	public void recordTodaySign(Context context){
		long curTime = TimeHelper.getCurTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(curTime);
		int curWeekOfYear=calendar.get(Calendar.WEEK_OF_YEAR);
		int dayOfWeek=calendar.get(Calendar.DAY_OF_WEEK);
		if(dayOfWeek==Calendar.SUNDAY){
			dayOfWeek = SUN;
		} else {
			dayOfWeek -= 1;
		}
		
		if(curWeekOfYear != weekOfYear){
			signValue = 0;
		}
		signValue |= (0x1 << (dayOfWeek -1));
		
		UserTaskDaoHelper.saveCheckInDailyRecord(context, getUserId(), signValue, curWeekOfYear);
	}

	public List<Integer> getIntegralList() {
		return integralList;
	}

	public void setIntegralList(List<Integer> integralList) {
		this.integralList = integralList;
	}

	/**
	 * @description: 获取签到的积分奖励
	 *
	 * @param day
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年9月20日 下午8:06:44
	 */
	public int getIntegral(int day) {
		if(integralList == null){
			return getIntegral();
		}
		int integral = 0;
		day -= 1;
		if (day >= 0 && day <= 7) {
			integral = integralList.get(day);
		}
		return integral;
	}
}
