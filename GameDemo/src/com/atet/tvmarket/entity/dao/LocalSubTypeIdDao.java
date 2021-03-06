package com.atet.tvmarket.entity.dao;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import com.atet.tvmarket.entity.dao.LocalSubTypeId;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table LOCAL_SUB_TYPE_ID.
*/
public class LocalSubTypeIdDao extends AbstractDao<LocalSubTypeId, String> {

    public static final String TABLENAME = "LOCAL_SUB_TYPE_ID";

    /**
     * Properties of entity LocalSubTypeId.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property TypeId = new Property(0, String.class, "typeId", true, "TYPE_ID");
        public final static Property LocalUpdateTime = new Property(1, Long.class, "localUpdateTime", false, "LOCAL_UPDATE_TIME");
        public final static Property Localname = new Property(2, String.class, "localname", false, "LOCALNAME");
    };

    private Query<LocalSubTypeId> localUpdateInfo_ExtrasQuery;

    public LocalSubTypeIdDao(DaoConfig config) {
        super(config);
    }
    
    public LocalSubTypeIdDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'LOCAL_SUB_TYPE_ID' (" + //
                "'TYPE_ID' TEXT PRIMARY KEY NOT NULL ," + // 0: typeId
                "'LOCAL_UPDATE_TIME' INTEGER," + // 1: localUpdateTime
                "'LOCALNAME' TEXT);"); // 2: localname
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'LOCAL_SUB_TYPE_ID'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, LocalSubTypeId entity) {
        stmt.clearBindings();
 
        String typeId = entity.getTypeId();
        if (typeId != null) {
            stmt.bindString(1, typeId);
        }
 
        Long localUpdateTime = entity.getLocalUpdateTime();
        if (localUpdateTime != null) {
            stmt.bindLong(2, localUpdateTime);
        }
 
        String localname = entity.getLocalname();
        if (localname != null) {
            stmt.bindString(3, localname);
        }
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public LocalSubTypeId readEntity(Cursor cursor, int offset) {
        LocalSubTypeId entity = new LocalSubTypeId( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // typeId
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // localUpdateTime
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // localname
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, LocalSubTypeId entity, int offset) {
        entity.setTypeId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setLocalUpdateTime(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setLocalname(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(LocalSubTypeId entity, long rowId) {
        return entity.getTypeId();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(LocalSubTypeId entity) {
        if(entity != null) {
            return entity.getTypeId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "extras" to-many relationship of LocalUpdateInfo. */
    public List<LocalSubTypeId> _queryLocalUpdateInfo_Extras(String localname) {
        synchronized (this) {
            if (localUpdateInfo_ExtrasQuery == null) {
                QueryBuilder<LocalSubTypeId> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.Localname.eq(null));
                localUpdateInfo_ExtrasQuery = queryBuilder.build();
            }
        }
        Query<LocalSubTypeId> query = localUpdateInfo_ExtrasQuery.forCurrentThread();
        query.setParameter(0, localname);
        return query.list();
    }

}
