package com.atet.tvmarket.control.mygame;

import com.atet.tvmarket.model.database.DBHelper;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;


public class MyGameProvider extends ContentProvider {

	private static final String AUTHORITY = "com.atet.tvmarket.provider";
	private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
	private static final int MYGAME = 1;
	private static final int MYGAMES = 2;
	private static final int APPISGAME = 3;
	
	
	/**已安装游戏的状态*/
	private static final int STATE_INSTALLED = 2;
	/**预转游戏的状态*/
	private static final int STATE_RE_INSTALLED = 514;

	static {
		matcher.addURI(AUTHORITY, "get_mygame/#", MYGAME);
		matcher.addURI(AUTHORITY, "get_mygames", MYGAMES);
		matcher.addURI(AUTHORITY, "appIsGame", APPISGAME);
	}

	private DBHelper dbHelper = null;

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		dbHelper = new DBHelper(this.getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		switch (matcher.match(uri)) {
		case MYGAME:
			long id = ContentUris.parseId(uri);
			String where = "autoIncrementId" + "=" + id;
			if (selection != null && !"".equals(selection)) {
				where = where + "and" + selection;
			}
			return db.query("myGameInfo", projection, selection,
					selectionArgs, null, null, sortOrder);
		case MYGAMES:
			
			//增加条件查询，只获取已经安装的和预装的游戏
			String selectionWhere = "state=" + STATE_INSTALLED+" or state="+STATE_RE_INSTALLED; 
			return db.query("myGameInfo", projection, selectionWhere,
					selectionArgs, null, null, sortOrder);
			
		case APPISGAME:
			return db.query("marketGameInfo", projection, selection,
					selectionArgs, null, null, sortOrder);

		default:
			throw new IllegalArgumentException("未知Uri：" + uri);
		}
	}
	
	// 返回指定uri参数对应的数据的MIME类型
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch (matcher.match(uri)) {
		case MYGAME:
			return "vnd.android.cursor.item/com.sxhl.tcltvmarket";
		case MYGAMES:
			return "vnd.android.cursor.dir/com.sxhl.tcltvmarket";
		default:
			throw new IllegalArgumentException("未知Uri：" + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		switch (matcher.match(uri)) {
		case MYGAMES:
			long rowId = db.insert("myGameInfo", "autoIncrementId",
					values);
			System.out.println("rowId:" + rowId);
			if (rowId > 0) {
				Uri myGameUri = ContentUris.withAppendedId(uri, rowId);
				getContext().getContentResolver().notifyChange(myGameUri, null);
				System.out.println("boUri:" + myGameUri);
				return myGameUri;
			}
			return null;

		default:
			throw new IllegalArgumentException("未知Uri：" + uri);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		int num = 0;
		switch (matcher.match(uri)) {
		case MYGAME:
			long id = ContentUris.parseId(uri);
			String where = "autoIncrementId" + "=" + id;
			if (selection != null && !"".equals(selection)) {
				where = where + "and" + selection;
			}
			num = db.delete("myGameInfo", selection, selectionArgs);
			break;
		case MYGAMES:
			num = db.delete("myGameInfo", selection, selectionArgs);
			break;

		default:
			throw new IllegalArgumentException("未知Uri：" + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return num;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int num = 0;
		switch (matcher.match(uri)) {
		case MYGAME:
			long id = ContentUris.parseId(uri);
			String where = "autoIncrementId" + "=" + id;
			if (selection != null && !"".equals(selection)) {
				where = where + "and" + selection;
			}
			num = db.update("myGameInfo", values, selection,
					selectionArgs);
			break;
		case MYGAMES:
			num = db.update("myGameInfo", values, selection,
					selectionArgs);
			break;

		default:
			throw new IllegalArgumentException("未知Uri：" + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return num;
	}
}
