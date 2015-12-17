package com.atet.tvmarket.entity;

public class InterfaceInfo implements AutoType {
	private String uniqueId;
	private String name;
	private String typeId;
	private long updateTime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	@Override
	public String toString() {
		return "InterfaceInfo [uniqueId=" + uniqueId + ", name=" + name
				+ ", typeId=" + typeId + ", updateTime=" + updateTime + "]";
	}
}
