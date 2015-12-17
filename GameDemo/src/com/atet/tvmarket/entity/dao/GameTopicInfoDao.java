package com.atet.tvmarket.entity.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.atet.tvmarket.entity.dao.GameTopicInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table GAME_TOPIC_INFO.
*/
public class GameTopicInfoDao extends AbstractDao<GameTopicInfo, String> {

    public static final String TABLENAME = "GAME_TOPIC_INFO";

    /**
     * Properties of entity GameTopicInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property TopicId = new Property(0, String.class, "topicId", true, "TOPIC_ID");
        public final static Property Type = new Property(1, Integer.class, "type", false, "TYPE");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Photo = new Property(3, String.class, "photo", false, "PHOTO");
        public final static Property Remark = new Property(4, String.class, "remark", false, "REMARK");
    };

    private DaoSession daoSession;


    public GameTopicInfoDao(DaoConfig config) {
        super(config);
    }
    
    public GameTopicInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'GAME_TOPIC_INFO' (" + //
                "'TOPIC_ID' TEXT PRIMARY KEY NOT NULL ," + // 0: topicId
                "'TYPE' INTEGER," + // 1: type
                "'NAME' TEXT," + // 2: name
                "'PHOTO' TEXT," + // 3: photo
                "'REMARK' TEXT);"); // 4: remark
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'GAME_TOPIC_INFO'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, GameTopicInfo entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getTopicId());
 
        Integer type = entity.getType();
        if (type != null) {
            stmt.bindLong(2, type);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String photo = entity.getPhoto();
        if (photo != null) {
            stmt.bindString(4, photo);
        }
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(5, remark);
        }
    }

    @Override
    protected void attachEntity(GameTopicInfo entity) {
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
    public GameTopicInfo readEntity(Cursor cursor, int offset) {
        GameTopicInfo entity = new GameTopicInfo( //
            cursor.getString(offset + 0), // topicId
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // type
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // photo
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // remark
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, GameTopicInfo entity, int offset) {
        entity.setTopicId(cursor.getString(offset + 0));
        entity.setType(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPhoto(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setRemark(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(GameTopicInfo entity, long rowId) {
        return entity.getTopicId();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(GameTopicInfo entity) {
        if(entity != null) {
            return entity.getTopicId();
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
