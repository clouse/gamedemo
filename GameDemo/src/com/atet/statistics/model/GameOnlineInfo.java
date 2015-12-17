package com.atet.statistics.model;

import java.io.Serializable;

import com.atet.tvmarket.entity.AutoType;
import com.atet.tvmarket.model.database.BaseModel;
import com.atet.tvmarket.model.database.TableDescription;


@TableDescription(name = "GameOnlineInfo")
public class GameOnlineInfo extends BaseModel implements AutoType, Serializable {

	private static final long serialVersionUID = 1L;
	private Integer userId;//用户账号
	private String packageName;//包名
	private String gameType;//游戏类型
	private String gameId;
	private String gameName;
	private String versionCode;//应用的版本号
	private String cpId;//CP
	private Integer copyright;
	private Long startTime; // 
	private Long endTime;
	private Long longTime;
	private String handleMac;
	private String handleProductId;
	private String handleFactoryId;
	private String handleName;
	private Integer isUseHandle;
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getGameType() {
		return gameType;
	}
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getCpId() {
		return cpId;
	}
	public void setCpId(String cpId) {
		this.cpId = cpId;
	}
	public Integer getCopyRight() {
		return copyright;
	}
	public void setCopyRight(Integer copyRight) {
		this.copyright = copyRight;
	}
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	public Long getLongTime() {
		return longTime;
	}
	public void setLongTime(Long longTime) {
		this.longTime = longTime;
	}

	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public Integer getIsUseHandle() {
		return isUseHandle;
	}
	public void setIsUseHandle(Integer isUseHandle) {
		this.isUseHandle = isUseHandle;
	}
	public String getHandleFactoryId() {
		return handleFactoryId;
	}
	public void setHandleFactoryId(String handleFactoryId) {
		this.handleFactoryId = handleFactoryId;
	}
	public String getHandleProductId() {
		return handleProductId;
	}
	public void setHandleProductId(String handleProductId) {
		this.handleProductId = handleProductId;
	}
	public String getHandleMac() {
		return handleMac;
	}
	public void setHandleMac(String handleMac) {
		this.handleMac = handleMac;
	}

	public String getHandleName() {
		return handleName;
	}
	public void setHandleName(String handleName) {
		this.handleName = handleName;
	}
	@Override
	public String toString() {
		return "GameOnlineInfo [userId=" + userId + ", packageName="
				+ packageName + ", gameType=" + gameType + ", gameId=" + gameId
				+ ", gameName=" + gameName + ", versionCode=" + versionCode
				+ ", cpId=" + cpId + ", copyright=" + copyright
				+ ", startTime=" + startTime + ", endTime=" + endTime
				+ ", longTime=" + longTime + ", handleMac=" + handleMac
				+ ", handleProductId=" + handleProductId + ", handleFactoryId="
				+ handleFactoryId + ", handleName=" + handleName
				+ ", isUseHandle=" + isUseHandle + "]";
	}
}
