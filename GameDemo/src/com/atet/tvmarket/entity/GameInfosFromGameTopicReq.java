package com.atet.tvmarket.entity;

public class GameInfosFromGameTopicReq implements AutoType {
	private String topicId;
	private int type;

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "GameInfosFromGameTopicReq [topicId=" + topicId + ", type="
				+ type + "]";
	}
}