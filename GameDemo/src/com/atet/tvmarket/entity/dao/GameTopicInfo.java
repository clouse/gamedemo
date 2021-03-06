package com.atet.tvmarket.entity.dao;

import java.util.List;
import com.atet.tvmarket.entity.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table GAME_TOPIC_INFO.
 */
public class GameTopicInfo implements java.io.Serializable {

    /** Not-null value. */
    private String topicId;
    private Integer type;
    private String name;
    private String photo;
    private String remark;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient GameTopicInfoDao myDao;

    private List<TopicToGame> topicToGameList;

    // KEEP FIELDS - put your custom fields here
	private static final long serialVersionUID = 1L;
    // KEEP FIELDS END

    public GameTopicInfo() {
    }

    public GameTopicInfo(String topicId) {
        this.topicId = topicId;
    }

    public GameTopicInfo(String topicId, Integer type, String name, String photo, String remark) {
        this.topicId = topicId;
        this.type = type;
        this.name = name;
        this.photo = photo;
        this.remark = remark;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGameTopicInfoDao() : null;
    }

    /** Not-null value. */
    public String getTopicId() {
        return topicId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<TopicToGame> getTopicToGameList() {
        if (topicToGameList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TopicToGameDao targetDao = daoSession.getTopicToGameDao();
            List<TopicToGame> topicToGameListNew = targetDao._queryGameTopicInfo_TopicToGameList(topicId);
            synchronized (this) {
                if(topicToGameList == null) {
                    topicToGameList = topicToGameListNew;
                }
            }
        }
        return topicToGameList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetTopicToGameList() {
        topicToGameList = null;
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
		return "GameTopicInfo [topicId=" + topicId + ", type=" + type
				+ ", name=" + name + ", photo=" + photo + ", remark=" + remark
				+ "]";
	}
    // KEEP METHODS END

}
