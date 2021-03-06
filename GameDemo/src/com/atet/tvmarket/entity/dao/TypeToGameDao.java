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

import com.atet.tvmarket.entity.dao.TypeToGame;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table TYPE_TO_GAME.
*/
public class TypeToGameDao extends AbstractDao<TypeToGame, Long> {

    public static final String TABLENAME = "TYPE_TO_GAME";

    /**
     * Properties of entity TypeToGame.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property ReturnOrder = new Property(1, Integer.class, "returnOrder", false, "RETURN_ORDER");
        public final static Property TypeId = new Property(2, String.class, "typeId", false, "TYPE_ID");
        public final static Property GameId = new Property(3, String.class, "gameId", false, "GAME_ID");
    };

    private DaoSession daoSession;

    private Query<TypeToGame> gameTypeInfo_TypeToGameListQuery;

    public TypeToGameDao(DaoConfig config) {
        super(config);
    }
    
    public TypeToGameDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'TYPE_TO_GAME' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'RETURN_ORDER' INTEGER," + // 1: returnOrder
                "'TYPE_ID' TEXT NOT NULL ," + // 2: typeId
                "'GAME_ID' TEXT NOT NULL );"); // 3: gameId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'TYPE_TO_GAME'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, TypeToGame entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer returnOrder = entity.getReturnOrder();
        if (returnOrder != null) {
            stmt.bindLong(2, returnOrder);
        }
        stmt.bindString(3, entity.getTypeId());
        stmt.bindString(4, entity.getGameId());
    }

    @Override
    protected void attachEntity(TypeToGame entity) {
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
    public TypeToGame readEntity(Cursor cursor, int offset) {
        TypeToGame entity = new TypeToGame( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // returnOrder
            cursor.getString(offset + 2), // typeId
            cursor.getString(offset + 3) // gameId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, TypeToGame entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setReturnOrder(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setTypeId(cursor.getString(offset + 2));
        entity.setGameId(cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(TypeToGame entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(TypeToGame entity) {
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
    
    /** Internal query to resolve the "typeToGameList" to-many relationship of GameTypeInfo. */
    public List<TypeToGame> _queryGameTypeInfo_TypeToGameList(String typeId) {
        synchronized (this) {
            if (gameTypeInfo_TypeToGameListQuery == null) {
                QueryBuilder<TypeToGame> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.TypeId.eq(null));
                queryBuilder.orderRaw("RETURN_ORDER ASC");
                gameTypeInfo_TypeToGameListQuery = queryBuilder.build();
            }
        }
        Query<TypeToGame> query = gameTypeInfo_TypeToGameListQuery.forCurrentThread();
        query.setParameter(0, typeId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getGameTypeInfoDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getGameInfoDao().getAllColumns());
            builder.append(" FROM TYPE_TO_GAME T");
            builder.append(" LEFT JOIN GAME_TYPE_INFO T0 ON T.'TYPE_ID'=T0.'TYPE_ID'");
            builder.append(" LEFT JOIN GAME_INFO T1 ON T.'GAME_ID'=T1.'GAME_ID'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected TypeToGame loadCurrentDeep(Cursor cursor, boolean lock) {
        TypeToGame entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        GameTypeInfo gameTypeInfo = loadCurrentOther(daoSession.getGameTypeInfoDao(), cursor, offset);
         if(gameTypeInfo != null) {
            entity.setGameTypeInfo(gameTypeInfo);
        }
        offset += daoSession.getGameTypeInfoDao().getAllColumns().length;

        GameInfo gameInfo = loadCurrentOther(daoSession.getGameInfoDao(), cursor, offset);
         if(gameInfo != null) {
            entity.setGameInfo(gameInfo);
        }

        return entity;    
    }

    public TypeToGame loadDeep(Long key) {
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
    public List<TypeToGame> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<TypeToGame> list = new ArrayList<TypeToGame>(count);
        
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
    
    protected List<TypeToGame> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<TypeToGame> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
