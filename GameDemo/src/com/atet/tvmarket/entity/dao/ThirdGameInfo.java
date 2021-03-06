package com.atet.tvmarket.entity.dao;

import java.util.List;
import com.atet.tvmarket.entity.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table THIRD_GAME_INFO.
 */
public class ThirdGameInfo implements java.io.Serializable {

    /** Not-null value. */
    private String gameId;
    private String gameName;
    private String minPhoto;
    private Double startLevel;
    private Integer gameSize;
    private String remark;
    private Integer downloadCount;
    private Long createTime;
    private Long updateTime;
    private String packageName;
    private String versionName;
    private Integer versionCode;
    private Integer handleType;
    private Integer interactivity;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient ThirdGameInfoDao myDao;

    private List<ThirdGameDownInfo> downloadInfo;

    // KEEP FIELDS - put your custom fields here
	private static final long serialVersionUID = 1L;
    // KEEP FIELDS END

    public ThirdGameInfo() {
    }

    public ThirdGameInfo(String gameId) {
        this.gameId = gameId;
    }

    public ThirdGameInfo(String gameId, String gameName, String minPhoto, Double startLevel, Integer gameSize, String remark, Integer downloadCount, Long createTime, Long updateTime, String packageName, String versionName, Integer versionCode, Integer handleType, Integer interactivity) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.minPhoto = minPhoto;
        this.startLevel = startLevel;
        this.gameSize = gameSize;
        this.remark = remark;
        this.downloadCount = downloadCount;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.packageName = packageName;
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.handleType = handleType;
        this.interactivity = interactivity;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getThirdGameInfoDao() : null;
    }

    /** Not-null value. */
    public String getGameId() {
        return gameId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getMinPhoto() {
        return minPhoto;
    }

    public void setMinPhoto(String minPhoto) {
        this.minPhoto = minPhoto;
    }

    public Double getStartLevel() {
        return startLevel;
    }

    public void setStartLevel(Double startLevel) {
        this.startLevel = startLevel;
    }

    public Integer getGameSize() {
        return gameSize;
    }

    public void setGameSize(Integer gameSize) {
        this.gameSize = gameSize;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public Integer getHandleType() {
        return handleType;
    }

    public void setHandleType(Integer handleType) {
        this.handleType = handleType;
    }

    public Integer getInteractivity() {
        return interactivity;
    }

    public void setInteractivity(Integer interactivity) {
        this.interactivity = interactivity;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<ThirdGameDownInfo> getDownloadInfo() {
        if (downloadInfo == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ThirdGameDownInfoDao targetDao = daoSession.getThirdGameDownInfoDao();
            List<ThirdGameDownInfo> downloadInfoNew = targetDao._queryThirdGameInfo_DownloadInfo(gameId);
            synchronized (this) {
                if(downloadInfo == null) {
                    downloadInfo = downloadInfoNew;
                }
            }
        }
        return downloadInfo;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetDownloadInfo() {
        downloadInfo = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
	@Override
	public String toString() {
		return "ThirdGameInfo [gameId=" + gameId + ", gameName=" + gameName
				+ ", minPhoto=" + minPhoto + ", startLevel=" + startLevel
				+ ", gameSize=" + gameSize + ", remark=" + remark
				+ ", downloadCount=" + downloadCount + ", createTime="
				+ createTime + ", updateTime=" + updateTime + ", packageName="
				+ packageName + ", versionName=" + versionName
				+ ", versionCode=" + versionCode + ", handleType=" + handleType
				+ ", interactivity=" + interactivity + ", downloadInfo="
				+ downloadInfo + "]";
	}
    // KEEP METHODS END

}
