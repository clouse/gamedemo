package com.atet.tvmarket.entity.dao;

import java.util.List;
import com.atet.tvmarket.entity.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table ACT_INFO.
 */
public class ActInfo implements java.io.Serializable {

    /** Not-null value. */
    private String activityId;
    private String title;
    private String remark;
    private Long startTime;
    private Long endTime;
    private String rules;
    private String prize;
    private String photo;
    private String erectPhoto;
    private String squarePhoto;
    private String video;
    private String thumbnail;
    private String url;
    private Integer recommend;
    private String gameId;
    private String packgeName;
    private Integer number;
    private Long createTime;
    private Long updateTime;
    private String qrCode;
    private String caption;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient ActInfoDao myDao;

    private List<ActDetailPhoto> detailPhotos;

    // KEEP FIELDS - put your custom fields here
	private static final long serialVersionUID = 1L;
    // KEEP FIELDS END

    public ActInfo() {
    }

    public ActInfo(String activityId) {
        this.activityId = activityId;
    }

    public ActInfo(String activityId, String title, String remark, Long startTime, Long endTime, String rules, String prize, String photo, String erectPhoto, String squarePhoto, String video, String thumbnail, String url, Integer recommend, String gameId, String packgeName, Integer number, Long createTime, Long updateTime, String qrCode, String caption) {
        this.activityId = activityId;
        this.title = title;
        this.remark = remark;
        this.startTime = startTime;
        this.endTime = endTime;
        this.rules = rules;
        this.prize = prize;
        this.photo = photo;
        this.erectPhoto = erectPhoto;
        this.squarePhoto = squarePhoto;
        this.video = video;
        this.thumbnail = thumbnail;
        this.url = url;
        this.recommend = recommend;
        this.gameId = gameId;
        this.packgeName = packgeName;
        this.number = number;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.qrCode = qrCode;
        this.caption = caption;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getActInfoDao() : null;
    }

    /** Not-null value. */
    public String getActivityId() {
        return activityId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getErectPhoto() {
        return erectPhoto;
    }

    public void setErectPhoto(String erectPhoto) {
        this.erectPhoto = erectPhoto;
    }

    public String getSquarePhoto() {
        return squarePhoto;
    }

    public void setSquarePhoto(String squarePhoto) {
        this.squarePhoto = squarePhoto;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getRecommend() {
        return recommend;
    }

    public void setRecommend(Integer recommend) {
        this.recommend = recommend;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPackgeName() {
        return packgeName;
    }

    public void setPackgeName(String packgeName) {
        this.packgeName = packgeName;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
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

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<ActDetailPhoto> getDetailPhotos() {
        if (detailPhotos == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ActDetailPhotoDao targetDao = daoSession.getActDetailPhotoDao();
            List<ActDetailPhoto> detailPhotosNew = targetDao._queryActInfo_DetailPhotos(activityId);
            synchronized (this) {
                if(detailPhotos == null) {
                    detailPhotos = detailPhotosNew;
                }
            }
        }
        return detailPhotos;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetDetailPhotos() {
        detailPhotos = null;
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
		return "ActInfo [activityId=" + activityId + ", title=" + title
				+ ", remark=" + remark + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", rules=" + rules + ", prize="
				+ prize + ", photo=" + photo + ", erectPhoto=" + erectPhoto
				+ ", squarePhoto=" + squarePhoto + ", video=" + video
				+ ", thumbnail=" + thumbnail + ", url=" + url + ", recommend="
				+ recommend + ", gameId=" + gameId + ", packgeName="
				+ packgeName + ", number=" + number + ", createTime="
				+ createTime + ", updateTime=" + updateTime + ", detailPhotos="
				+ detailPhotos + "]";
	}
    // KEEP METHODS END

}
