package com.atet.tvmarket.entity;

import java.io.Serializable;

/**
 * @ClassName: AppInfo
 * @Description: 应用信息实体
 * @author: Liuqin
 * @date 2013-1-7 上午9:29:32
 * 
 */
/**
 * @description: 
 *
 * @author: LiuQin
 * @date: 2015年7月29日 上午11:28:13 
 */
public class NewVersionInfoResp implements Serializable, AutoType {
    private static final long serialVersionUID = 1L;

    // 版本号
    private int versionCode;
    // 版本名
    private String versionName;
    // 描述内容
    private String remark;
    // 低于此版本的必须升级
    private int minVersion;
    private String downloadURL;
	private int code;
	// 是否存在新版本
	private boolean isNewVersionExist;
	// 是否强制升级才能使用
	private boolean isForce;
	private boolean isSilent;

    public NewVersionInfoResp() {
        super();
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getMinVersion() {
        return minVersion;
    }

    public void setMinVersion(int minVersion) {
        this.minVersion = minVersion;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }
    
	public boolean isNewVersionExist() {
		return isNewVersionExist;
	}

	public void setNewVersionExist(boolean isNewVersionExist) {
		this.isNewVersionExist = isNewVersionExist;
	}

	public boolean isForce() {
		return isForce;
	}

	public void setForce(boolean isForce) {
		this.isForce = isForce;
	}

	public boolean isSilent() {
		return isSilent;
	}

	public void setSilent(boolean isSilent) {
		this.isSilent = isSilent;
	}

	@Override
	public String toString() {
		return "NewVersionInfoResp [versionCode=" + versionCode
				+ ", versionName=" + versionName + ", remark=" + remark
				+ ", minVersion=" + minVersion + ", downloadURL=" + downloadURL
				+ ", code=" + code + ", isNewVersionExist=" + isNewVersionExist
				+ ", isForce=" + isForce + ", isSilent=" + isSilent + "]";
	}
}
