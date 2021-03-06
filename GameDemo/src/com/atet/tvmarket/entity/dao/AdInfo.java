package com.atet.tvmarket.entity.dao;

import com.atet.tvmarket.entity.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table AD_INFO.
 */
public class AdInfo implements java.io.Serializable {

    /** Not-null value. */
    private String adId;
    private String url;
    private String backgroundUrl;
    private String videoUrl;
    private String gameId;
    private String gameName;
    private String packageName;
    private String title;
    private Integer positionIndex;
    private String sizeType;
    private Integer type;
    private String remark;
    private String typeName;
    private Integer newGames;
    private Integer handleType;
    private Integer online;
    private String cornerMark;
    /** Not-null value. */
    private String actionUrlId;
    private long modelKey;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient AdInfoDao myDao;

    private ActionUrl actionUrl;
    private String actionUrl__resolvedKey;


    // KEEP FIELDS - put your custom fields here
	private static final long serialVersionUID = 1L;
    // KEEP FIELDS END

    public AdInfo() {
    }

    public AdInfo(String adId) {
        this.adId = adId;
    }

    public AdInfo(String adId, String url, String backgroundUrl, String videoUrl, String gameId, String gameName, String packageName, String title, Integer positionIndex, String sizeType, Integer type, String remark, String typeName, Integer newGames, Integer handleType, Integer online, String cornerMark, String actionUrlId, long modelKey) {
        this.adId = adId;
        this.url = url;
        this.backgroundUrl = backgroundUrl;
        this.videoUrl = videoUrl;
        this.gameId = gameId;
        this.gameName = gameName;
        this.packageName = packageName;
        this.title = title;
        this.positionIndex = positionIndex;
        this.sizeType = sizeType;
        this.type = type;
        this.remark = remark;
        this.typeName = typeName;
        this.newGames = newGames;
        this.handleType = handleType;
        this.online = online;
        this.cornerMark = cornerMark;
        this.actionUrlId = actionUrlId;
        this.modelKey = modelKey;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAdInfoDao() : null;
    }

    /** Not-null value. */
    public String getAdId() {
        return adId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
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

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPositionIndex() {
        return positionIndex;
    }

    public void setPositionIndex(Integer positionIndex) {
        this.positionIndex = positionIndex;
    }

    public String getSizeType() {
        return sizeType;
    }

    public void setSizeType(String sizeType) {
        this.sizeType = sizeType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getNewGames() {
        return newGames;
    }

    public void setNewGames(Integer newGames) {
        this.newGames = newGames;
    }

    public Integer getHandleType() {
        return handleType;
    }

    public void setHandleType(Integer handleType) {
        this.handleType = handleType;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }

    public String getCornerMark() {
        return cornerMark;
    }

    public void setCornerMark(String cornerMark) {
        this.cornerMark = cornerMark;
    }

    /** Not-null value. */
    public String getActionUrlId() {
        return actionUrlId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setActionUrlId(String actionUrlId) {
        this.actionUrlId = actionUrlId;
    }

    public long getModelKey() {
        return modelKey;
    }

    public void setModelKey(long modelKey) {
        this.modelKey = modelKey;
    }

    /** To-one relationship, resolved on first access. */
    public ActionUrl getActionUrl() {
        String __key = this.actionUrlId;
        if (actionUrl__resolvedKey == null || actionUrl__resolvedKey != __key) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ActionUrlDao targetDao = daoSession.getActionUrlDao();
            ActionUrl actionUrlNew = targetDao.load(__key);
            synchronized (this) {
                actionUrl = actionUrlNew;
            	actionUrl__resolvedKey = __key;
            }
        }
        return actionUrl;
    }

    public void setActionUrl(ActionUrl actionUrl) {
        if (actionUrl == null) {
            throw new DaoException("To-one property 'actionUrlId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.actionUrl = actionUrl;
            actionUrlId = actionUrl.getAdId();
            actionUrl__resolvedKey = actionUrlId;
        }
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
		return "AdInfo [adId=" + adId + ", url=" + url + ", backgroundUrl="
				+ backgroundUrl + ", videoUrl=" + videoUrl + ", gameId="
				+ gameId + ", gameName=" + gameName + ", packageName="
				+ packageName + ", title=" + title + ", positionIndex="
				+ positionIndex + ", sizeType=" + sizeType + ", type=" + type
				+ ", remark=" + remark + ", typeName=" + typeName
				+ ", newGames=" + newGames + ", handleType=" + handleType
				+ ", online=" + online + ", cornerMark=" + cornerMark
				+ ", actionUrlId=" + actionUrlId + ", modelKey=" + modelKey
				+ ", actionUrl=" + actionUrl + ", actionUrl__resolvedKey="
				+ actionUrl__resolvedKey + "]";
	}
    // KEEP METHODS END

}
