package com.atet.tvmarket.entity.dao;

import com.atet.tvmarket.entity.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table TOPIC_TO_GAME.
 */
public class TopicToGame implements java.io.Serializable {

    private Long id;
    private Integer returnOrder;
    private Integer type;
    /** Not-null value. */
    private String topicId;
    /** Not-null value. */
    private String gameId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient TopicToGameDao myDao;

    private GameTopicInfo gameTopicInfo;
    private String gameTopicInfo__resolvedKey;

    private GameInfo gameInfo;
    private String gameInfo__resolvedKey;

    private ThirdGameInfo thirdGameInfo;
    private String thirdGameInfo__resolvedKey;


    // KEEP FIELDS - put your custom fields here
	private static final long serialVersionUID = 1L;
    // KEEP FIELDS END

    public TopicToGame() {
    }

    public TopicToGame(Long id) {
        this.id = id;
    }

    public TopicToGame(Long id, Integer returnOrder, Integer type, String topicId, String gameId) {
        this.id = id;
        this.returnOrder = returnOrder;
        this.type = type;
        this.topicId = topicId;
        this.gameId = gameId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTopicToGameDao() : null;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    /** Not-null value. */
    public String getTopicId() {
        return topicId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setTopicId(String topicId) {
        this.topicId = topicId;
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
    public GameTopicInfo getGameTopicInfo() {
        String __key = this.topicId;
        if (gameTopicInfo__resolvedKey == null || gameTopicInfo__resolvedKey != __key) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GameTopicInfoDao targetDao = daoSession.getGameTopicInfoDao();
            GameTopicInfo gameTopicInfoNew = targetDao.load(__key);
            synchronized (this) {
                gameTopicInfo = gameTopicInfoNew;
            	gameTopicInfo__resolvedKey = __key;
            }
        }
        return gameTopicInfo;
    }

    public void setGameTopicInfo(GameTopicInfo gameTopicInfo) {
        if (gameTopicInfo == null) {
            throw new DaoException("To-one property 'topicId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.gameTopicInfo = gameTopicInfo;
            topicId = gameTopicInfo.getTopicId();
            gameTopicInfo__resolvedKey = topicId;
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

    /** To-one relationship, resolved on first access. */
    public ThirdGameInfo getThirdGameInfo() {
        String __key = this.gameId;
        if (thirdGameInfo__resolvedKey == null || thirdGameInfo__resolvedKey != __key) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ThirdGameInfoDao targetDao = daoSession.getThirdGameInfoDao();
            ThirdGameInfo thirdGameInfoNew = targetDao.load(__key);
            synchronized (this) {
                thirdGameInfo = thirdGameInfoNew;
            	thirdGameInfo__resolvedKey = __key;
            }
        }
        return thirdGameInfo;
    }

    public void setThirdGameInfo(ThirdGameInfo thirdGameInfo) {
        if (thirdGameInfo == null) {
            throw new DaoException("To-one property 'gameId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.thirdGameInfo = thirdGameInfo;
            gameId = thirdGameInfo.getGameId();
            thirdGameInfo__resolvedKey = gameId;
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
		return "TopicToGame [id=" + id + ", returnOrder=" + returnOrder
				+ ", type=" + type + ", topicId=" + topicId + ", gameId="
				+ gameId + ", gameTopicInfo=" + gameTopicInfo + ", gameInfo="
				+ gameInfo + ", thirdGameInfo=" + thirdGameInfo + "]";
	}
    // KEEP METHODS END

}
