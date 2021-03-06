package com.atet.tvmarket.entity.dao;

import com.atet.tvmarket.entity.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table TYPE_TO_GAME.
 */
public class TypeToGame implements java.io.Serializable {

    private Long id;
    private Integer returnOrder;
    /** Not-null value. */
    private String typeId;
    /** Not-null value. */
    private String gameId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient TypeToGameDao myDao;

    private GameTypeInfo gameTypeInfo;
    private String gameTypeInfo__resolvedKey;

    private GameInfo gameInfo;
    private String gameInfo__resolvedKey;


    // KEEP FIELDS - put your custom fields here
	private static final long serialVersionUID = 1L;
    // KEEP FIELDS END

    public TypeToGame() {
    }

    public TypeToGame(Long id) {
        this.id = id;
    }

    public TypeToGame(Long id, Integer returnOrder, String typeId, String gameId) {
        this.id = id;
        this.returnOrder = returnOrder;
        this.typeId = typeId;
        this.gameId = gameId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTypeToGameDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getReturnOrder() {
        return returnOrder;
    }

    public void setReturnOrder(Integer returnOrder) {
        this.returnOrder = returnOrder;
    }

    /** Not-null value. */
    public String getTypeId() {
        return typeId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    /** Not-null value. */
    public String getGameId() {
        return gameId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    /** To-one relationship, resolved on first access. */
    public GameTypeInfo getGameTypeInfo() {
        String __key = this.typeId;
        if (gameTypeInfo__resolvedKey == null || gameTypeInfo__resolvedKey != __key) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GameTypeInfoDao targetDao = daoSession.getGameTypeInfoDao();
            GameTypeInfo gameTypeInfoNew = targetDao.load(__key);
            synchronized (this) {
                gameTypeInfo = gameTypeInfoNew;
            	gameTypeInfo__resolvedKey = __key;
            }
        }
        return gameTypeInfo;
    }

    public void setGameTypeInfo(GameTypeInfo gameTypeInfo) {
        if (gameTypeInfo == null) {
            throw new DaoException("To-one property 'typeId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.gameTypeInfo = gameTypeInfo;
            typeId = gameTypeInfo.getTypeId();
            gameTypeInfo__resolvedKey = typeId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public GameInfo getGameInfo() {
        String __key = this.gameId;
        if (gameInfo__resolvedKey == null || gameInfo__resolvedKey != __key) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GameInfoDao targetDao = daoSession.getGameInfoDao();
            GameInfo gameInfoNew = targetDao.load(__key);
            synchronized (this) {
                gameInfo = gameInfoNew;
            	gameInfo__resolvedKey = __key;
            }
        }
        return gameInfo;
    }

    public void setGameInfo(GameInfo gameInfo) {
        if (gameInfo == null) {
            throw new DaoException("To-one property 'gameId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.gameInfo = gameInfo;
            gameId = gameInfo.getGameId();
            gameInfo__resolvedKey = gameId;
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
		return "TypeToGame [id=" + id + ", typeId=" + typeId + ", gameId="
				+ gameId + ", gameTypeInfo=" + gameTypeInfo + ", gameInfo="
				+ gameInfo + "]";
	}
    // KEEP METHODS END

}
