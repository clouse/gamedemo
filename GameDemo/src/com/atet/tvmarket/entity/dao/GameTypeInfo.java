package com.atet.tvmarket.entity.dao;

import java.util.List;
import com.atet.tvmarket.entity.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table GAME_TYPE_INFO.
 */
public class GameTypeInfo implements java.io.Serializable {

    /** Not-null value. */
    private String typeId;
    private String name;
    private String icon;
    private String remark;
    private Integer orderNum;
    private Long createTime;
    private Long updateTime;
    private Integer games;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient GameTypeInfoDao myDao;

    private List<TypeToGame> typeToGameList;

    // KEEP FIELDS - put your custom fields here
	private static final long serialVersionUID = 1L;
    // KEEP FIELDS END

    public GameTypeInfo() {
    }

    public GameTypeInfo(String typeId) {
        this.typeId = typeId;
    }

    public GameTypeInfo(String typeId, String name, String icon, String remark, Integer orderNum, Long createTime, Long updateTime, Integer games) {
        this.typeId = typeId;
        this.name = name;
        this.icon = icon;
        this.remark = remark;
        this.orderNum = orderNum;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.games = games;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGameTypeInfoDao() : null;
    }

    /** Not-null value. */
    public String getTypeId() {
        return typeId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
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

    public Integer getGames() {
        return games;
    }

    public void setGames(Integer games) {
        this.games = games;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<TypeToGame> getTypeToGameList() {
        if (typeToGameList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TypeToGameDao targetDao = daoSession.getTypeToGameDao();
            List<TypeToGame> typeToGameListNew = targetDao._queryGameTypeInfo_TypeToGameList(typeId);
            synchronized (this) {
                if(typeToGameList == null) {
                    typeToGameList = typeToGameListNew;
                }
            }
        }
        return typeToGameList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetTypeToGameList() {
        typeToGameList = null;
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
		return "GameTypeInfo [typeId=" + typeId + ", name="
				+ name + ", icon=" + icon + ", remark=" + remark
				+ ", orderNum=" + orderNum + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + ", games=" + games + "]";
	}
    // KEEP METHODS END

}
