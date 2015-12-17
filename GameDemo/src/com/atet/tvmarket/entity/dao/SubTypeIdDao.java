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

import com.atet.tvmarket.entity.dao.SubTypeId;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table SUB_TYPE_ID.
*/
public class SubTypeIdDao extends AbstractDao<SubTypeId, String> {

    public static final String TABLENAME = "SUB_TYPE_ID";

    /**
     * Properties of entity SubTypeId.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property TypeId = new Property(0, String.class, "typeId", true, "TYPE_ID");
        public final static Property UpdateTime = new Property(1, Long.class, "updateTime", false, "UPDATE_TIME");
        public final static Property InterfaceName = new Property(2, String.class, "interfaceName", false, "INTERFACE_NAME");
    };

    private Query<SubTypeId> updateInterfaceInfo_ExtrasQuery;

    public SubTypeIdDao(DaoConfig config) {
        super(config);
    }
    
    public SubTypeIdDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'SUB_TYPE_ID' (" + //
                "'TYPE_ID' TEXT PRIMARY KEY NOT NULL ," + // 0: typeId
                "'UPDATE_TIME' INTEGER," + // 1: updateTime
                "'INTERFACE_NAME' TEXT);"); // 2: interfaceName
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'SUB_TYPE_ID'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, SubTypeId entity) {
        stmt.clearBindings();
 
        String typeId = entity.getTypeId();
        if (typeId != null) {
            stmt.bindString(1, typeId);
        }
 
        Long updateTime = entity.getUpdateTime();
        if (updateTime != null) {
            stmt.bindLong(2, updateTime);
        }
 
        String interfaceName = entity.getInterfaceName();
        if (interfaceName != null) {
            stmt.bindString(3, interfaceName);
        }
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public SubTypeId readEntity(Cursor cursor, int offset) {
        SubTypeId entity = new SubTypeId( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // typeId
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // updateTime
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // interfaceName
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, SubTypeId entity, int offset) {
        entity.setTypeId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setUpdateTime(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setInterfaceName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(SubTypeId entity, long rowId) {
        return entity.getTypeId();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(SubTypeId entity) {
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
    
    /** Internal query to resolve the "extras" to-many relationship of UpdateInterfaceInfo. */
    public List<SubTypeId> _queryUpdateInterfaceInfo_Extras(String interfaceName) {
        synchronized (this) {
            if (updateInterfaceInfo_ExtrasQuery == null) {
                QueryBuilder<SubTypeId> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.InterfaceName.eq(null));
                updateInterfaceInfo_ExtrasQuery = queryBuilder.build();
            }
        }
        Query<SubTypeId> query = updateInterfaceInfo_ExtrasQuery.forCurrentThread();
        query.setParameter(0, interfaceName);
        return query.list();
    }

}
