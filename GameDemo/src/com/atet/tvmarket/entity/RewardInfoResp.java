package com.atet.tvmarket.entity;

import java.io.Serializable;
import java.util.List;

public class RewardInfoResp implements AutoType,Serializable{
	private int code;
	private String desc;
	private int flag;
	private String rewardId;
	private int userId;
	private List<RewardList> rewardList;

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

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getRewardId() {
		return rewardId;
	}

	public void setRewardId(String rewardId) {
		this.rewardId = rewardId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public List<RewardList> getRewardList() {
		return rewardList;
	}

	public void setRewardList(List<RewardList> rewardList) {
		this.rewardList = rewardList;
	}

	@Override
	public String toString() {
		return "RewardInfoResp [code=" + code + ", desc=" + desc + ", flag="
				+ flag + ", rewardId=" + rewardId + ", userId=" + userId
				+ ", rewardList=" + rewardList + "]";
	}

	public static class RewardList implements AutoType,Serializable{
		private String rewardId;
		private String rewardName;
		private String icon;
		private int sequence;

		public String getRewardId() {
			return rewardId;
		}

		public void setRewardId(String rewardId) {
			this.rewardId = rewardId;
		}

		public String getRewardName() {
			return rewardName;
		}

		public void setRewardName(String rewardName) {
			this.rewardName = rewardName;
		}

		public String getIcon() {
			return icon;
		}

		public void setIcon(String icon) {
			this.icon = icon;
		}

		public int getSequence() {
			return sequence;
		}

		public void setSequence(int sequence) {
			this.sequence = sequence;
		}

		@Override
		public String toString() {
			return "RewardList [rewardId=" + rewardId + ", rewardName="
					+ rewardName + ", icon=" + icon + ", sequence=" + sequence
					+ "]";
		}
	}
}