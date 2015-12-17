package com.atet.tvmarket.entity;

import java.io.Serializable;

import com.atet.tvmarket.model.database.BaseModel;
import com.atet.tvmarket.model.database.TableDescription;

@TableDescription(name = "collectDownCountInfo")
public class CollectDownCountInfo extends BaseModel implements AutoType,
		Serializable {

	private Integer userId;//用户账号
	private String gameId;
	private String packageName;//游戏包名
	private String gameName;//游戏名称
	private String gameType;//游戏类型
	private String cpId;//CP
	private Integer clickCount; // 广告图片的简要描述
	private Integer downCount;
	private Integer AdClick;
	private Integer copyRight;//版本标志，1、平台游戏　2、第三方游戏　3、其他应用
	private Long recordTime;//记录时间
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getGameType() {
		return gameType;
	}
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}
	public String getCpId() {
		return cpId;
	}
	public void setCpId(String cpId) {
		this.cpId = cpId;
	}
	public Integer getClickCount() {
		return clickCount;
	}
	public void setClickCount(Integer clickCount) {
		this.clickCount = clickCount;
	}
	public Integer getDownCount() {
		return downCount;
	}
	public void setDownCount(Integer downCount) {
		this.downCount = downCount;
	}
	public Integer getAdClick() {
		return AdClick;
	}
	public void setAdClick(Integer adClick) {
		AdClick = adClick;
	}
	public Integer getCopyRight() {
		return copyRight;
	}
	public void setCopyRight(Integer copyRight) {
		this.copyRight = copyRight;
	}
	public Long getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(Long recordTime) {
		this.recordTime = recordTime;
	}
	
}
