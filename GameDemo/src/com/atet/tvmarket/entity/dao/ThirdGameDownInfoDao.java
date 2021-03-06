package com.atet.tvmarket.entity.dao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import com.atet.tvmarket.entity.dao.ThirdGameDownInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table THIRD_GAME_DOWN_INFO.
*/
public class ThirdGameDownInfoDao extends AbstractDao<ThirdGameDownInfo, Long> {

    public static final String TABLENAME = "THIRD_GAME_DOWN_INFO";

    /**
     * Properties of entity ThirdGameDownInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property GameURLId = new Property(1, String.class, "gameURLId", false, "GAME_URLID");
        public final static Property Url = new Property(2, String.class, "url", false, "URL");
        public final static Property DownToken = new Property(3, Integer.class, "downToken", false, "DOWN_TOKEN");
        public final static Property Remark = new Property(4, String.class, "remark", false, "REMARK");
        public final static Property LogoUrl = new Property(5, String.class, "logoUrl", false, "LOGO_URL");
        public final static Property GameId = new Property(6, String.class, "gameId", false, "GAME_ID");
    };

    private DaoSession daoSession;

    private Query<ThirdGameDownInfo> thirdGameInfo_DownloadInfoQuery;

    public ThirdGameDownInfoDao(DaoConfig config) {
        super(config);
    }
    
    public ThirdGameDownInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'THIRD_GAME_DOWN_INFO' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'GAME_URLID' TEXT," + // 1: gameURLId
                "'URL' TEXT," + // 2: url
                "'DOWN_TOKEN' INTEGER," + // 3: downToken
                "'REMARK' TEXT," + // 4: remark
                "'LOGO_URL' TEXT," + // 5: logoUrl
                "'GAME_ID' TEXT NOT NULL );"); // 6: gameId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'THIRD_GAME_DOWN_INFO'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ThirdGameDownInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String gameURLId = entity.getGameURLId();
        if (gameURLId != null) {
            stmt.bindString(2, gameURLId);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(3, url);
        }
 
        Integer downToken = entity.getDownToken();
        if (downToken != null) {
            stmt.bindLong(4, downToken);
        }
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(5, remark);
        }
 
        String logoUrl = entity.getLogoUrl();
        if (logoUrl != null) {
            stmt.bindString(6, logoUrl);
        }
        stmt.bindString(7, entity.getGameId());
    }

    @Override
    protected void attachEntity(ThirdGameDownInfo entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ThirdGameDownInfo readEntity(Cursor cursor, int offset) {
        ThirdGameDownInfo entity = new ThirdGameDownInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // gameURLId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // url
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // downToken
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // remark
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // logoUrl
            cursor.getString(offset + 6) // gameId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ThirdGameDownInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setGameURLId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUrl(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDownToken(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setRemark(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setLogoUrl(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setGameId(cursor.getString(offset + 6));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ThirdGameDownInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ThirdGameDownInfo entity) {
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
    
    /** Internal query to resolve the "downloadInfo" to-many relationship of ThirdGameInfo. */
    public List<ThirdGameDownInfo> _queryThirdGameInfo_DownloadInfo(String gameId) {
        synchronized (this) {
            if (thirdGameInfo_DownloadInfoQuery == null) {
                QueryBuilder<ThirdGameDownInfo> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.GameId.eq(null));
                thirdGameInfo_DownloadInfoQuery = queryBuilder.build();
            }
        }
        Query<ThirdGameDownInfo> query = thirdGameInfo_DownloadInfoQuery.forCurrentThread();
        query.setParameter(0, gameId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getThirdGameInfoDao().getAllColumns());
            builder.append(" FROM THIRD_GAME_DOWN_INFO T");
            builder.append(" LEFT JOIN THIRD_GAME_INFO T0 ON T.'GAME_ID'=T0.'GAME_ID'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected ThirdGameDownInfo loadCurrentDeep(Cursor cursor, boolean lock) {
        ThirdGameDownInfo entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        ThirdGameInfo thirdGameInfo = loadCurrentOther(daoSession.getThirdGameInfoDao(), cursor, offset);
         if(thirdGameInfo != null) {
            entity.setThirdGameInfo(thirdGameInfo);
        }

        return entity;    
    }

    public ThirdGameDownInfo loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<ThirdGameDownInfo> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<ThirdGameDownInfo> list = new ArrayList<ThirdGameDownInfo>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<ThirdGameDownInfo> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<ThirdGameDownInfo> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
