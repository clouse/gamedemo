package com.atet.tvmarket.entity.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.atet.tvmarket.entity.dao.NoticeInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table NOTICE_INFO.
*/
public class NoticeInfoDao extends AbstractDao<NoticeInfo, String> {

    public static final String TABLENAME = "NOTICE_INFO";

    /**
     * Properties of entity NoticeInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property NoticeId = new Property(0, String.class, "noticeId", true, "NOTICE_ID");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property Icon = new Property(2, String.class, "icon", false, "ICON");
        public final static Property Content = new Property(3, String.class, "content", false, "CONTENT");
        public final static Property Url = new Property(4, String.class, "url", false, "URL");
        public final static Property NoticeType = new Property(5, Integer.class, "noticeType", false, "NOTICE_TYPE");
        public final static Property StartTime = new Property(6, Long.class, "startTime", false, "START_TIME");
        public final static Property EndTime = new Property(7, Long.class, "endTime", false, "END_TIME");
        public final static Property CreateTime = new Property(8, Long.class, "createTime", false, "CREATE_TIME");
    };


    public NoticeInfoDao(DaoConfig config) {
        super(config);
    }
    
    public NoticeInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'NOTICE_INFO' (" + //
                "'NOTICE_ID' TEXT PRIMARY KEY NOT NULL ," + // 0: noticeId
                "'TITLE' TEXT," + // 1: title
                "'ICON' TEXT," + // 2: icon
                "'CONTENT' TEXT," + // 3: content
                "'URL' TEXT," + // 4: url
                "'NOTICE_TYPE' INTEGER," + // 5: noticeType
                "'START_TIME' INTEGER," + // 6: startTime
                "'END_TIME' INTEGER," + // 7: endTime
                "'CREATE_TIME' INTEGER);"); // 8: createTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'NOTICE_INFO'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, NoticeInfo entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getNoticeId());
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        String icon = entity.getIcon();
        if (icon != null) {
            stmt.bindString(3, icon);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(4, content);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(5, url);
        }
 
        Integer noticeType = entity.getNoticeType();
        if (noticeType != null) {
            stmt.bindLong(6, noticeType);
        }
 
        Long startTime = entity.getStartTime();
        if (startTime != null) {
            stmt.bindLong(7, startTime);
        }
 
        Long endTime = entity.getEndTime();
        if (endTime != null) {
            stmt.bindLong(8, endTime);
        }
 
        Long createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindLong(9, createTime);
        }
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public NoticeInfo readEntity(Cursor cursor, int offset) {
        NoticeInfo entity = new NoticeInfo( //
            cursor.getString(offset + 0), // noticeId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // title
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // icon
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // content
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // url
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // noticeType
            cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6), // startTime
            cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7), // endTime
            cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8) // createTime
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, NoticeInfo entity, int offset) {
        entity.setNoticeId(cursor.getString(offset + 0));
        entity.setTitle(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setIcon(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setContent(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setUrl(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setNoticeType(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setStartTime(cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6));
        entity.setEndTime(cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7));
        entity.setCreateTime(cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(NoticeInfo entity, long rowId) {
        return entity.getNoticeId();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(NoticeInfo entity) {
        if(entity != null) {
            return entity.getNoticeId();
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
