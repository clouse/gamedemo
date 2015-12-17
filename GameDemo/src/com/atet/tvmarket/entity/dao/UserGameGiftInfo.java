package com.atet.tvmarket.entity.dao;

import java.util.List;
import com.atet.tvmarket.entity.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table USER_GAME_GIFT_INFO.
 */
public class UserGameGiftInfo implements java.io.Serializable {

    /** Not-null value. */
    private String gameid;
    private String gameName;
    private String minPhoto;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient UserGameGiftInfoDao myDao;

    private List<UserGameToGift> userGameToGiftList;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public UserGameGiftInfo() {
    }

    public UserGameGiftInfo(String gameid) {
        this.gameid = gameid;
    }

    public UserGameGiftInfo(String gameid, String gameName, String minPhoto) {
        this.gameid = gameid;
        this.gameName = gameName;
        this.minPhoto = minPhoto;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserGameGiftInfoDao() : null;
    }

    /** Not-null value. */
    public String getGameid() {
        return gameid;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setGameid(String gameid) {
        this.gameid = gameid;
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

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<UserGameToGift> getUserGameToGiftList() {
        if (userGameToGiftList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserGameToGiftDao targetDao = daoSession.getUserGameToGiftDao();
            List<UserGameToGift> userGameToGiftListNew = targetDao._queryUserGameGiftInfo_UserGameToGiftList(gameid);
            synchronized (this) {
                if(userGameToGiftList == null) {
                    userGameToGiftList = userGameToGiftListNew;
                }
            }
        }
        return userGameToGiftList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetUserGameToGiftList() {
        userGameToGiftList = null;
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
		return "UserGameGiftInfo [gameid=" + gameid + ", gameName=" + gameName
				+ ", minPhoto=" + minPhoto + ", userGameToGiftList="
				+ userGameToGiftList + "]";
	}
    // KEEP METHODS END

}
