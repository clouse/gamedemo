package com.atet.tvmarket.model.database;

import java.lang.reflect.Field;

import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.entity.AutoType;
import com.atet.tvmarket.entity.Group;
import com.atet.tvmarket.utils.StringTool;

import android.content.ContentValues;
import android.database.Cursor;



/**
 * @ClassName: PersistentUtils.java
 * @Description: 数据库操作工具
 * @author 吴绍东
 * @date 2012-12-12 下午12:49:35
 */
public class PersistentUtils {
	static DBAccess m_access= BaseApplication.getSqlInstance();
	
	/**
	 * 增加新数据
	 * 
	 * @param model
	 */
	public static long addModel ( BaseModel model ) {
		long ret=-1;
		if ( model != null ) {
     			ret=m_access.insert ( BaseModelTool.getTableName ( model.getClass () ) ,
					BaseModelTool.getContentValues ( model ) );
		}
		return ret;
	}

	/**
	 * 删除记录
	 * 
	 * @param model
	 */
	public static void delete ( BaseModel model ) {

		if ( !BaseModelTool.isValidForEditable ( model ) ) {
			return;
		}
		m_access.delete ( BaseModelTool.getTableName ( model.getClass () ) ,
				model.getAutoIncrementId() );
	}

	/**
	 * 更新数据
	 * 
	 * @param model
	 *            数据模型必须是BaseModel的子类；
	 */
	public static int update ( BaseModel model ) {
		if ( !BaseModelTool.isValidForEditable ( model ) ) {
		}

		return m_access.update (
				BaseModelTool.getTableName ( model.getClass () ) ,
				BaseModelTool.getContentValues ( model ) , " autoIncrementId='" + model.getAutoIncrementId() +"'", null);

	}
	/**
	* @Title: execSQL
	* @Description: (执行sql原生语句)
	* @param @param sql    设定文件
	* @return void    返回类型
	* @throws
	 */
	public static void execSQL(String sql){
		m_access.execSQL(sql);
	}
	
	/**
	 * @Title: execSQL
	 * @Description: (执行删除数据)
	 * @param @param model    设定文件
	 * @param @param where    设定文件
	 * @return void    返回类型
	 * @throws
	 */
	@SuppressWarnings("rawtypes")
	public static void execDeleteData(Class clazz,String where){
		m_access.execSQL("delete from "+BaseModelTool.getTableName(clazz)+" "+ where);
	}

	/**
	 * 更新数据
	 * 
	 * @param model
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public static int update(BaseModel model, ContentValues values,
			String whereClause, String[] whereArgs)

	{
		if (!BaseModelTool.isValidForEditable(model)) {
		}

		return m_access.update(BaseModelTool.getTableName(model.getClass()),
				values, whereClause, whereArgs);

	}
	
	/**
	 * 查询数据表获取数据模型列表； 适用于通用模型IdNameModel，此模型不与具体的数据表关联；
	 * 
	 * @param tableName
	 *            数据库表名称
	 * @param conditon
	 *            查询条件
	 * @return 数据模型列表
	 */
	@SuppressWarnings("unchecked")
	public static < T extends AutoType> Group < T > getModelList ( Class < ? > modelType ,
			String tableName , String conditon ) {

		Group < T > result = new Group < T > ();

		if ( StringTool.isEmpty ( tableName ) ) {
			return result;
		}

		if ( modelType == null ) {
			return result;
		}

		if ( modelType != null ) {

			Cursor cursor = m_access.query ( tableName ,
					BaseModelTool.getNamesForField ( modelType ) , conditon,null,null,null,null );

			if ( cursor == null ) {
			}

			T newInstance = null;
			while ( cursor.moveToNext () ) {
				newInstance = ( T ) getModel ( cursor , modelType );
				result.add ( newInstance );
			}

			cursor.close ();
			cursor = null;
		}
		return result;
	}

	/**
	 * 查询符合条件的获取对象列表
	 * 
	 * @param <T>
	 *            返回数据类型 与modelType对应
	 * @param modelType
	 * @param sql
	 *            SQLit标准SQL语句
	 * @param selectionArgs
	 *            sql对应的参数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static < T extends AutoType> Group< T > getModelListBySQL ( Class < ? > modelType ,
			String sql , String [ ] selectionArgs ) {
		Group < T > result = new Group < T > ();

		if ( modelType == null ) {
	
		}

		if ( modelType != null ) {

			Cursor cursor = m_access.execRawQuery ( sql , selectionArgs );

			if ( cursor == null ) {
		
			}

			while ( cursor.moveToNext () ) {
				T newInstance = ( T ) getModel ( cursor , modelType );
				result.add ( newInstance );
			}

			cursor.close ();
			cursor = null;
		}
		return result;
	}

	/**
	 * 查询符合条件的获取对象列表
	 * 
	 * @param <T>
	 * @param modelType
	 *            模型类的Class
	 * @param conditon
	 *            SQL语句中where查询条件之后的条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static < T extends AutoType> Group< T > getModelList ( Class < ? > modelType ,
			String conditon,String[] selectionArgs,String groupBy,String having, String orderBy) {
		Group < T > result = new Group < T > ();

		if ( modelType == null ) {

		}

		if ( modelType != null ) {

			Cursor cursor = m_access.query (
					BaseModelTool.getTableName ( modelType ) ,
					BaseModelTool.getNamesForField ( modelType ) , conditon, selectionArgs,groupBy,having,orderBy);

			if ( cursor == null ) {
		
			}

			while ( cursor.moveToNext () ) {
				T newInstance = ( T ) getModel ( cursor , modelType );
				result.add ( newInstance );
			}

			cursor.close ();
			cursor = null;
		}
		return result;
	}

	/**
	 * 将cursor中的当前记录按照modelType类型生成对应的对象实例
	 * 
	 * @param cursor
	 *            数据来源，主要取第一行数据
	 * @param modelType
	 *            要生成的对象类型
	 * @return
	 */
	public static Object getModel ( Cursor cursor , Class < ? > modelType ) {

		Object newInstance = createInstance ( modelType );

		for ( Field item : BaseModelTool.getFieldList ( modelType ) ) {
			setFieldValue ( newInstance , item , cursor );
		}

		return newInstance;
	}

	/**
	 * 设定数据
	 * 
	 * @param <T>
	 * @param newInstance
	 * @param item
	 * @param cursor
	 */
	public static void setFieldValue ( Object newInstance , Field item ,
			Cursor cursor ) {

		String fieldName = item.getName  ();// .toLowerCase ();
		try {
			if ( BaseModelTool.isString ( item ) ) {

				String tmp = cursor.getString ( cursor
						.getColumnIndex ( fieldName ) );
				//DebugTool.info ( fieldName + " :" + tmp );

				item.set ( newInstance , tmp );
			}
			else if ( BaseModelTool.isLong ( item ) ) {

				item.set ( newInstance ,
						cursor.getLong( cursor.getColumnIndex ( fieldName ) ) );

			}
			else if( BaseModelTool.isInteger(item))
			{
				item.set(newInstance, cursor.getInt( cursor.getColumnIndex ( fieldName ) ));
			}
			else if( BaseModelTool.isDouble(item)){
				item.set(newInstance, cursor.getDouble( cursor.getColumnIndex ( fieldName ) ));
			}

		}
		catch ( IllegalStateException e ) {
		
		}
		catch ( IllegalArgumentException e ) {

		}
		catch ( IllegalAccessException e ) {
	
		}

	}

	/**
	 * 创建类实例
	 * 
	 * @param paramClass
	 * @return
	 */
	private static Object createInstance ( Class < ? > paramClass ) {

		Object obj = null;
		try {

			obj = paramClass.newInstance ();
		}
		catch ( IllegalAccessException e ) {

		}
		catch ( InstantiationException e ) {
		
		}
		return obj;
	}

	/**
	 * 统计记录数
	 * 
	 * @param modelType
	 *            模型类型Class
	 * @param conditon
	 *            查询条件，标准SQL语句中where 之后的条件。
	 * @return
	 */
	public static long getRowCount ( Class < ? > modelType , String conditon ) {
		long count = -1;

		String sql = "select count(*) from "
				+ BaseModelTool.getTableName ( modelType );
		if ( !StringTool.isEmpty ( conditon ) ) {
			sql = sql + " where " + conditon;
		}

		Cursor cursor = m_access.execRawQuery ( sql , null );
		if ( cursor.moveToNext () ) {
			count = cursor.getLong ( 0 );
			cursor.close ();
			cursor = null;
		}

		return count;
	}
	
	/**
	 * 查询符合条件的获取对象列表
	 * 
	 * @param <T>
	 * @param modelType
	 *            模型类的Class
	 * @param conditon
	 *            SQL语句中where查询条件之后的条件
	 * @return
	 */
	public static <T extends AutoType> Group<T> getModelList(
			Class<?> modelType, String conditon) {
		Group<T> result = new Group<T>();

		if (modelType == null) {

		}

		if (modelType != null) {

			Cursor cursor = m_access.query(
					BaseModelTool.getTableName(modelType),
					BaseModelTool.getNamesForField(modelType), conditon);

			if (cursor == null) {

				return result;
			}
            if(!cursor.isClosed()){
            	while (cursor.moveToNext()) {
            		T newInstance = (T) getModel(cursor, modelType);
            		result.add(newInstance);
            	}
            }

			cursor.close();
			cursor = null;
		}
		return result;
	}
	
	
	/**
	 * 查询符合条件的记录数量
	 * 
	 * @param <T>
	 * @param modelType
	 *            模型类的Class
	 * @param conditon
	 *            SQL语句中where查询条件之后的条件
	 * @return
	 */
	public static <T extends AutoType> int getModelListCount(
			Class<?> modelType, String conditon) {
		int count = 0;

		if (modelType == null) {

		}

		if (modelType != null) {
			Cursor cursor = m_access.query(
					BaseModelTool.getTableName(modelType),
					BaseModelTool.getNamesForField(modelType), conditon);

			if (cursor == null) {
	
			} else {
				count = cursor.getCount();
				cursor.close();
				cursor = null;
			}
		}
		return count;
	}

}
