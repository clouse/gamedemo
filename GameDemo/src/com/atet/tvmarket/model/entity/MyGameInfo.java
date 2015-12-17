package com.atet.tvmarket.model.entity;

import java.io.Serializable;

import com.atet.tvmarket.entity.AutoType;
import com.atet.tvmarket.model.database.BaseModel;
import com.atet.tvmarket.model.database.IgnoreField;
import com.atet.tvmarket.model.database.TableDescription;


/**
 * @ClassName: MyGameInfo
 * @Description: 我的游戏列表中的实体类
 * @author: Liuqin
 * @date 2012-12-14 下午1:44:57
 * 
 */
@TableDescription(name = "myGameInfo")
public class MyGameInfo extends BaseModel implements AutoType, Serializable {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = 1L;

	// 应用ID
	private String gameId;
	// 应用包名
	private String packageName;
	// 启动的activity
	private String launchAct;
	// 应用名称
	private String name;
	//小图
	private String minPhoto;
	//中图
	private String middlePhoto;
	// 版本号
	private int versionCode;
	// 应用图标url
	private String iconUrl;
	// 下载url
	private String downUrl;
	// 本地保存目录
	private String localDir;
	// 本地保存文件名
	private String localFilename;
	// 状态：1已下载但未安装，2已安装，0未下载
	private int state;
	// 运行次数
	private int runCounts;
	//下载方式；1：本地下载；2：网络获取真实下载地址
	private Integer downToken;
	private String midDownAdress;
	// 运行次数
	private Long runTime;
	
	@IgnoreField 
	private String dbId;

	public MyGameInfo() {
		super();
	}

	public MyGameInfo(String gameId, String packageName, String name,
			String iconUrl, String downUrl, String localDir,
			String localFilename, int state, int versionCode, String launchAct,
			Long runTime) {
		super();
		this.gameId = gameId;
		this.packageName = packageName;
		this.name = name;
		this.iconUrl = iconUrl;
		this.downUrl = downUrl;
		this.localDir = localDir;
		this.localFilename = localFilename;
		this.state = state;
		this.versionCode = versionCode;
		this.launchAct = launchAct;
		this.runTime = runTime;
	}
	

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getDownUrl() {
		return downUrl;
	}

	public void setDownUrl(String downUrl) {
		this.downUrl = downUrl;
	}

	public String getLocalDir() {
		return localDir;
	}

	public void setLocalDir(String localDir) {
		this.localDir = localDir;
	}

	public String getLocalFilename() {
		return localFilename;
	}

	public void setLocalFilename(String localFilename) {
		this.localFilename = localFilename;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getLaunchAct() {
		return launchAct;
	}

	public void setLaunchAct(String launchAct) {
		this.launchAct = launchAct;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
	
	public int getRunCounts() {
		return runCounts;
	}

	public void setRunCounts(int runCounts) {
		this.runCounts = runCounts;
	}

	public Integer getDownToken(){
		return downToken;
	}
	
	public void setDownToken(Integer downToken){
		this.downToken=downToken;
	}
	
	public String getMidDownAdress(){
		return midDownAdress;
	}
	
	public void setMidDownAdress(String midDownAdress){
		this.midDownAdress=midDownAdress;
	}
	
	public String getDbId() {
        return dbId;
    }

    public void setDbId(String dbId) {
        this.dbId = dbId;
    }
      

	public String getMinPhoto() {
		return minPhoto;
	}

	public void setMinPhoto(String minPhoto) {
		this.minPhoto = minPhoto;
	}

	public String getMiddlePhoto() {
		return middlePhoto;
	}

	public void setMiddlePhoto(String middlePhoto) {
		this.middlePhoto = middlePhoto;
	}

	
	public Long getRunTime() {
		return runTime;
	}

	public void setRunTime(Long runTime) {
		this.runTime = runTime;
	}

	@Override
	public String toString() {
		return "MyGameInfo [gameId=" + gameId + ", packageName=" + packageName
				+ ", launchAct=" + launchAct + ", name=" + name + ", minPhoto="
				+ minPhoto + ", middlePhoto=" + middlePhoto + ", versionCode="
				+ versionCode + ", iconUrl=" + iconUrl + ", downUrl=" + downUrl
				+ ", localDir=" + localDir + ", localFilename=" + localFilename
				+ ", state=" + state + ", runTime=" + runCounts
				+ ", downToken=" + downToken + ", midDownAdress="
				+ midDownAdress + ", dbId=" + dbId + ", runTime=" + runTime + "]";
	}

}
