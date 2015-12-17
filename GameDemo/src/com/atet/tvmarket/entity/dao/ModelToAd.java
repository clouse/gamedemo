package com.atet.tvmarket.entity.dao;

import com.atet.tvmarket.entity.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table MODEL_TO_AD.
 */
public class ModelToAd {

    private Long id;
    /** Not-null value. */
    private String AdId;
    private long modelKey;
    private Integer returnOrder;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient ModelToAdDao myDao;

    private AdInfo adInfo;
    private String adInfo__resolvedKey;

    private AdModelInfo adModelInfo;
    private Long adModelInfo__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public ModelToAd() {
    }

    public ModelToAd(Long id) {
        this.id = id;
    }

    public ModelToAd(Long id, String AdId, long modelKey, Integer returnOrder) {
        this.id = id;
        this.AdId = AdId;
        this.modelKey = modelKey;
        this.returnOrder = returnOrder;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getModelToAdDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getAdId() {
        return AdId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setAdId(String AdId) {
        this.AdId = AdId;
    }

    public long getModelKey() {
        return modelKey;
    }

    public void setModelKey(long modelKey) {
        this.modelKey = modelKey;
    }

    public Integer getReturnOrder() {
        return returnOrder;
    }

    public void setReturnOrder(Integer returnOrder) {
        this.returnOrder = returnOrder;
    }

    /** To-one relationship, resolved on first access. */
    public AdInfo getAdInfo() {
        String __key = this.AdId;
        if (adInfo__resolvedKey == null || adInfo__resolvedKey != __key) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AdInfoDao targetDao = daoSession.getAdInfoDao();
            AdInfo adInfoNew = targetDao.load(__key);
            synchronized (this) {
                adInfo = adInfoNew;
            	adInfo__resolvedKey = __key;
            }
        }
        return adInfo;
    }

    public void setAdInfo(AdInfo adInfo) {
        if (adInfo == null) {
            throw new DaoException("To-one property 'AdId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.adInfo = adInfo;
            AdId = adInfo.getAdId();
            adInfo__resolvedKey = AdId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public AdModelInfo getAdModelInfo() {
        long __key = this.modelKey;
        if (adModelInfo__resolvedKey == null || !adModelInfo__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AdModelInfoDao targetDao = daoSession.getAdModelInfoDao();
            AdModelInfo adModelInfoNew = targetDao.load(__key);
            synchronized (this) {
                adModelInfo = adModelInfoNew;
            	adModelInfo__resolvedKey = __key;
            }
        }
        return adModelInfo;
    }

    public void setAdModelInfo(AdModelInfo adModelInfo) {
        if (adModelInfo == null) {
            throw new DaoException("To-one property 'modelKey' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.adModelInfo = adModelInfo;
            modelKey = adModelInfo.getId();
            adModelInfo__resolvedKey = modelKey;
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
    // KEEP METHODS END

}
