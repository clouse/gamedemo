package com.atet.tvmarket.entity.dao;

import java.util.List;
import com.atet.tvmarket.entity.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table ACTION_URL.
 */
public class ActionUrl implements java.io.Serializable {

    private String behavior;
    private String action;
    private String packagename;
    private String activityname;
    /** Not-null value. */
    private String adId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient ActionUrlDao myDao;

    private List<Extramap> extramap;

    // KEEP FIELDS - put your custom fields here
	private static final long serialVersionUID = 1L;
    // KEEP FIELDS END

    public ActionUrl() {
    }

    public ActionUrl(String adId) {
        this.adId = adId;
    }

    public ActionUrl(String behavior, String action, String packagename, String activityname, String adId) {
        this.behavior = behavior;
        this.action = action;
        this.packagename = packagename;
        this.activityname = activityname;
        this.adId = adId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getActionUrlDao() : null;
    }

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getActivityname() {
        return activityname;
    }

    public void setActivityname(String activityname) {
        this.activityname = activityname;
    }

    /** Not-null value. */
    public String getAdId() {
        return adId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setAdId(String adId) {
        this.adId = adId;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Extramap> getExtramap() {
        if (extramap == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ExtramapDao targetDao = daoSession.getExtramapDao();
            List<Extramap> extramapNew = targetDao._queryActionUrl_Extramap(adId);
            synchronized (this) {
                if(extramap == null) {
                    extramap = extramapNew;
                }
            }
        }
        return extramap;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetExtramap() {
        extramap = null;
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
		return "ActionUrl [behavior=" + behavior + ", action=" + action
				+ ", packagename=" + packagename + ", activityname="
				+ activityname + ", adId=" + adId + ", extramap=" + extramap
				+ "]";
	}
    // KEEP METHODS END

}