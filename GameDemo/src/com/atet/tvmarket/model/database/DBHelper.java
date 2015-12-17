package com.atet.tvmarket.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.atet.statistics.model.CollectGameInfo;
import com.atet.statistics.model.GameOnlineInfo;
import com.atet.statistics.model.InitInfo;
import com.atet.tvmarket.entity.CollectDownCountInfo;
import com.atet.tvmarket.model.entity.MyGameInfo;




/**
 * @ClassName: DBHelper.java
 * @Description: 数据库帮助类
 * @author 吴绍东
 * @date 2012-12-12 下午12:48:48
 */
public class DBHelper extends SQLiteOpenHelper {
	public static final String NAME = "box.db";
    public static boolean isCopyData=false;//是否升级后保留之前的数据
	public DBHelper(Context context) {

		super(context, NAME, null, 102);
	}

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {

		super(context, name, factory, version);
	}

	/**
	 * 用户第一次使用软件时调用的操作，用于获取数据库创建语句（SW）,然后创建数据库
	 * @date 2014.04.30 
	 * @change wfq 增加了SearchTypeInfo 表  数据库版本从20升至21；
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(BaseModelTool.getCreateTableSql(MyGameInfo.class));
		
		// 统计服务
		db.execSQL(BaseModelTool.getCreateTableSql(InitInfo.class));
		db.execSQL(BaseModelTool.getCreateTableSql(GameOnlineInfo.class));
		db.execSQL(BaseModelTool.getCreateTableSql(CollectGameInfo.class));
		db.execSQL(BaseModelTool.getCreateTableSql(CollectDownCountInfo.class));

		if (isCopyData) {
			db.execSQL(BaseModelTool
					.insertIntoOldTableSql(MyGameInfo.class, db));
			db.execSQL(BaseModelTool.deleteCopyTableSql(MyGameInfo.class));
		}
	}

	/**
	 * 数据库更新
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String addColumnName = "runTime";
		db.execSQL(BaseModelTool.getDropTableSql(InitInfo.class));
		db.execSQL(BaseModelTool.getDropTableSql(GameOnlineInfo.class));
		db.execSQL(BaseModelTool.getDropTableSql(CollectGameInfo.class));
		db.execSQL(BaseModelTool.getDropTableSql(CollectDownCountInfo.class));
		// 我的游戏信息添加字段
		if(! BaseModelTool.checkColumnExists(db, MyGameInfo.class, addColumnName)){
			db.execSQL(BaseModelTool.getAddNewColumnSql(MyGameInfo.class, addColumnName));	
		}
		// oldVersion=18 时，升级19，修改了MyGameInfo表
		db.execSQL(BaseModelTool.alterTableNameSql(MyGameInfo.class));
		isCopyData = true;
		onCreate(db);
	}

}
