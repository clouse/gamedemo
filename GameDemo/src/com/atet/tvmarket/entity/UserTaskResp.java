package com.atet.tvmarket.entity;

import java.util.List;

public class UserTaskResp implements AutoType {
	public static final int TASK_STATE_FINISH = 2;
	public static final int TASK_STATE_NOT_FINISH = 0;
	public static final int TASK_STATE_STANDBY = 1;
	
	public static final int TASK_RULE_STATE_FINISH = 2;
	public static final int TASK_RULE_STATE_NOT_FINISH = 1;
	
	private int code;
	private String desc;
	private int integral;
	private int coupon;
	private List<UserTaskList> taskList;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public int getCoupon() {
		return coupon;
	}

	public void setCoupon(int coupon) {
		this.coupon = coupon;
	}

	public List<UserTaskList> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<UserTaskList> taskList) {
		this.taskList = taskList;
	}

	@Override
	public String toString() {
		return "UserTaskResp [code=" + code + ", desc=" + desc + ", integral="
				+ integral + ", coupon=" + coupon + ", taskList=" + taskList
				+ "]";
	}

	public static class UserTaskList implements AutoType {
		private String taskId;
		private String taskName;
		private String logo;
		private String getWay;
		private int getMaxCount;
		private String periodLength;
		private String perioGetTimes;
		private String periodCycleMaxnum;
		private String completeWay;
		private String ruleType;
		private String remark;
		private long start_time;
		private long end_time;
		private int state;
		private List<RewardRuleInner> rewardRule;
		private List<GameIdInner> gameIds;
		private String cGameIds;

		public String getTaskId() {
			return taskId;
		}

		public void setTaskId(String taskId) {
			this.taskId = taskId;
		}

		public String getTaskName() {
			return taskName;
		}

		public void setTaskName(String taskName) {
			this.taskName = taskName;
		}

		public String getLogo() {
			return logo;
		}

		public void setLogo(String logo) {
			this.logo = logo;
		}

		public String getGetWay() {
			return getWay;
		}

		public void setGetWay(String getWay) {
			this.getWay = getWay;
		}

		public int getGetMaxCount() {
			return getMaxCount;
		}

		public void setGetMaxCount(int getMaxCount) {
			this.getMaxCount = getMaxCount;
		}

		public String getPeriodLength() {
			return periodLength;
		}

		public void setPeriodLength(String periodLength) {
			this.periodLength = periodLength;
		}

		public String getPerioGetTimes() {
			return perioGetTimes;
		}

		public void setPerioGetTimes(String perioGetTimes) {
			this.perioGetTimes = perioGetTimes;
		}

		public String getPeriodCycleMaxnum() {
			return periodCycleMaxnum;
		}

		public void setPeriodCycleMaxnum(String periodCycleMaxnum) {
			this.periodCycleMaxnum = periodCycleMaxnum;
		}

		public String getCompleteWay() {
			return completeWay;
		}

		public void setCompleteWay(String completeWay) {
			this.completeWay = completeWay;
		}

		public String getRuleType() {
			return ruleType;
		}

		public void setRuleType(String ruleType) {
			this.ruleType = ruleType;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public long getStart_time() {
			return start_time;
		}

		public void setStart_time(long start_time) {
			this.start_time = start_time;
		}

		public long getEnd_time() {
			return end_time;
		}

		public void setEnd_time(long end_time) {
			this.end_time = end_time;
		}

		public int getState() {
			return state;
		}

		public void setState(int state) {
			this.state = state;
		}

		public List<RewardRuleInner> getRewardRule() {
			return rewardRule;
		}

		public void setRewardRule(List<RewardRuleInner> rewardRule) {
			this.rewardRule = rewardRule;
		}

		public List<GameIdInner> getGameIds() {
			return gameIds;
		}

		public void setGameIds(List<GameIdInner> gameIds) {
			this.gameIds = gameIds;
		}

		public String getcGameIds() {
			return cGameIds;
		}

		public void setcGameIds(String cGameIds) {
			this.cGameIds = cGameIds;
		}

		@Override
		public String toString() {
			return "UserTaskList [taskId=" + taskId + ", taskName=" + taskName
					+ ", logo=" + logo + ", getWay=" + getWay
					+ ", getMaxCount=" + getMaxCount + ", periodLength="
					+ periodLength + ", perioGetTimes=" + perioGetTimes
					+ ", periodCycleMaxnum=" + periodCycleMaxnum
					+ ", completeWay=" + completeWay + ", ruleType=" + ruleType
					+ ", remark=" + remark + ", start_time=" + start_time
					+ ", end_time=" + end_time + ", state=" + state
					+ ", rewardRule=" + rewardRule + ", gameIds=" + gameIds
					+ ", cGameIds=" + cGameIds + "]";
		}

		public static class RewardRuleInner {
			private String ruleId;
			private int low;
			private int high;
			private int integral;
			private int coupon;
			private int state;

			public String getRuleId() {
				return ruleId;
			}

			public void setRuleId(String ruleId) {
				this.ruleId = ruleId;
			}

			public int getLow() {
				return low;
			}

			public void setLow(int low) {
				this.low = low;
			}

			public int getHigh() {
				return high;
			}

			public void setHigh(int high) {
				this.high = high;
			}

			public int getIntegral() {
				return integral;
			}

			public void setIntegral(int integral) {
				this.integral = integral;
			}

			public int getCoupon() {
				return coupon;
			}

			public void setCoupon(int coupon) {
				this.coupon = coupon;
			}

			public int getState() {
				return state;
			}

			public void setState(int state) {
				this.state = state;
			}

			@Override
			public String toString() {
				return "RewardRuleInner [ruleId=" + ruleId + ", low=" + low
						+ ", high=" + high + ", integral=" + integral
						+ ", coupon=" + coupon + ", state=" + state + "]";
			}
		}

		public static class GameIdInner {
			private String gameId;
			private String name;

			public String getGameId() {
				return gameId;
			}

			public void setGameId(String gameId) {
				this.gameId = gameId;
			}

			@Override
			public String toString() {
				return "GameIdInner [gameId=" + gameId + "]";
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}
		}
	}
}