package com.atet.tvmarket.entity.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.atet.tvmarket.entity.dao.ThirdGameInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table THIRD_GAME_INFO.
*/
public class ThirdGameInfoDao extends AbstractDao<ThirdGameInfo, String> {

    public static final String TABLENAME = "THIRD_GAME_INFO";

    /**
     * Properties of entity ThirdGameInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property GameId = new Property(0, String.class, "gameId", true, "GAME_ID");
        public final static Property GameName = new Property(1, String.class, "gameName", false, "GAME_NAME");
        public final static Property MinPhoto = new Property(2, String.class, "minPhoto", false, "MIN_PHOTO");
        public final static Property StartLevel = new Property(3, Double.class, "startLevel", false, "START_LEVEL");
        public final static Property GameSize = new Property(4, Integer.class, "gameSize", false, "GAME_SIZE");
        public final static Property Remark = new Property(5, String.class, "remark", false, "REMARK");
        public final static Property DownloadCount = new Property(6, Integer.class, "downloadCount", false, "DOWNLOAD_COUNT");
        public final static Property CreateTime = new Property(7, Long.class, "createTime", false, "CREATE_TIME");
        public final static Property UpdateTime = new Property(8, Long.class, "updateTime", false, "UPDATE_TIME");
        public final static Property PackageName = new Property(9, String.class, "packageName", false, "PACKAGE_NAME");
        public final static Property VersionName = new Property(10, String.class, "versionName", false, "VERSION_NAME");
        public final static Property VersionCode = new Property(11, Integer.class, "versionCode", false, "VERSION_CODE");
        public final static Property HandleType = new Property(12, Integer.class, "handleType", false, "HANDLE_TYPE");
        public final static Property Interactivity = new Property(13, Integer.class, "interactivity", false, "INTERACTIVITY");
    };

    private DaoSession daoSession;


    public ThirdGameInfoDao(DaoConfig config) {
        super(config);
    }
    
    public ThirdGameInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'THIRD_GAME_INFO' (" + //
                "'GAME_ID' TEXT PRIMARY KEY NOT NULL ," + // 0: gameId
                "'GAME_NAME' TEXT," + // 1: gameName
                "'MIN_PHOTO' TEXT," + // 2: minPhoto
                "'START_LEVEL' REAL," + // 3: startLevel
                "'GAME_SIZE' INTEGER," + // 4: gameSize
                "'REMARK' TEXT," + // 5: remark
                "'DOWNLOAD_COUNT' INTEGER," + // 6: downloadCount
                "'CREATE_TIME' INTEGER," + // 7: createTime
                "'UPDATE_TIME' INTEGER," + // 8: updateTime
                "'PACKAGE_NAME' TEXT," + // 9: packageName
                "'VERSION_NAME' TEXT," + // 10: versionName
                "'VERSION_CODE' INTEGER," + // 11: versionCode
                "'HANDLE_TYPE' INTEGER," + // 12: handleType
                "'INTERACTIVITY' INTEGER);"); // 13: interactivity
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'THIRD_GAME_INFO'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ThirdGameInfo entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getGameId());
 
        String gameName = entity.getGameName();
        if (gameName != null) {
            stmt.bindString(2, gameName);
        }
 
        String minPhoto = entity.getMinPhoto();
        if (minPhoto != null) {
            stmt.bindString(3, minPhoto);
        }
 
        Double startLevel = entity.getStartLevel();
        if (startLevel != null) {
            stmt.bindDouble(4, startLevel);
        }
 
        Integer gameSize = entity.getGameSize();
        if (gameSize != null) {
            stmt.bindLong(5, gameSize);
        }
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(6, remark);
        }
 
        Integer downloadCount = entity.getDownloadCount();
        if (downloadCount != null) {
            stmt.bindLong(7, downloadCount);
        }
 
        Long createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindLong(8, createTime);
        }
 
        Long updateTime = entity.getUpdateTime();
        if (updateTime != null) {
            stmt.bindLong(9, updateTime);
        }
 
        String packageName = entity.getPackageName();
        if (packageName != null) {
            stmt.bindString(10, packageName);
        }
 
        String versionName = entity.getVersionName();
        if (versionName != null) {
            stmt.bindString(11, versionName);
        }
 
        Integer versionCode = entity.getVersionCode();
        if (versionCode != null) {
            stmt.bindLong(12, versionCode);
        }
 
        Integer handleType = entity.getHandleType();
        if (handleType != null) {
            stmt.bindLong(13, handleType);
        }
 
        Integer interactivity = entity.getInteractivity();
        if (interactivity != null) {
            stmt.bindLong(14, interactivity);
        }
    }

    @Override
    protected void attachEntity(ThirdGameInfo entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ThirdGameInfo readEntity(Cursor cursor, int offset) {
        ThirdGameInfo entity = new ThirdGameInfo( //
            cursor.getString(offset + 0), // gameId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // gameName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // minPhoto
            cursor.isNull(offset + 3) ? null : cursor.getDouble(offset + 3), // startLevel
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // gameSize
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // remark
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // downloadCount
            cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7), // createTime
            cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8), // updateTime
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // packageName
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // versionName
            cursor.isNull(offset + 11) ? null : cursor.getInt(offset + 11), // versionCode
            cursor.isNull(offset + 12) ? null : cursor.getInt(offset + 12), // handleType
            cursor.isNull(offset + 13) ? null : cursor.getInt(offset + 13) // interactivity
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ThirdGameInfo entity, int offset) {
        entity.setGameId(cursor.getString(offset + 0));
        entity.setGameName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setMinPhoto(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setStartLevel(cursor.isNull(offset + 3) ? null : cursor.getDouble(offset + 3));
        entity.setGameSize(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setRemark(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setDownloadCount(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setCreateTime(cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7));
        entity.setUpdateTime(cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8));
        entity.setPackageName(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setVersionName(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setVersionCode(cursor.isNull(offset + 11) ? null : cursor.getInt(offset + 11));
        entity.setHandleType(cursor.isNull(offset + 12) ? null : cursor.getInt(offset + 12));
        entity.setInteractivity(cursor.isNull(offset + 13) ? null : cursor.getInt(offset + 13));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(ThirdGameInfo entity, long rowId) {
        return entity.getGameId();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(ThirdGameInfo entity) {
        if(entity != null) {
            return entity.getGameId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
