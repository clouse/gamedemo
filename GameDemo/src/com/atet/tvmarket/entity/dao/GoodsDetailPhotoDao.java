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

import com.atet.tvmarket.entity.dao.GoodsDetailPhoto;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table GOODS_DETAIL_PHOTO.
*/
public class GoodsDetailPhotoDao extends AbstractDao<GoodsDetailPhoto, Long> {

    public static final String TABLENAME = "GOODS_DETAIL_PHOTO";

    /**
     * Properties of entity GoodsDetailPhoto.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Picture = new Property(1, String.class, "picture", false, "PICTURE");
        public final static Property GoodsId = new Property(2, String.class, "goodsId", false, "GOODS_ID");
    };

    private Query<GoodsDetailPhoto> goodsInfo_DetailPhotosQuery;

    public GoodsDetailPhotoDao(DaoConfig config) {
        super(config);
    }
    
    public GoodsDetailPhotoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'GOODS_DETAIL_PHOTO' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'PICTURE' TEXT," + // 1: picture
                "'GOODS_ID' TEXT NOT NULL );"); // 2: goodsId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'GOODS_DETAIL_PHOTO'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, GoodsDetailPhoto entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String picture = entity.getPicture();
        if (picture != null) {
            stmt.bindString(2, picture);
        }
        stmt.bindString(3, entity.getGoodsId());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public GoodsDetailPhoto readEntity(Cursor cursor, int offset) {
        GoodsDetailPhoto entity = new GoodsDetailPhoto( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // picture
            cursor.getString(offset + 2) // goodsId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, GoodsDetailPhoto entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPicture(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setGoodsId(cursor.getString(offset + 2));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(GoodsDetailPhoto entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(GoodsDetailPhoto entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "detailPhotos" to-many relationship of GoodsInfo. */
    public List<GoodsDetailPhoto> _queryGoodsInfo_DetailPhotos(String goodsId) {
        synchronized (this) {
            if (goodsInfo_DetailPhotosQuery == null) {
                QueryBuilder<GoodsDetailPhoto> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.GoodsId.eq(null));
                goodsInfo_DetailPhotosQuery = queryBuilder.build();
            }
        }
        Query<GoodsDetailPhoto> query = goodsInfo_DetailPhotosQuery.forCurrentThread();
        query.setParameter(0, goodsId);
        return query.list();
    }

}