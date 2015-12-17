package com.atet.tvmarket.model.database;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 * @ClassName: BaseModelTool.java
 * @Description: 模型处理工具
 * @author 吴绍东
 * @date 2012-12-12 下午12:47:55
 */
public class BaseModelTool {
	/**
	 * 检查模型数据是否有效(在编辑状态下)
	 * 
	 */
	public static boolean isValidForEditable(BaseModel model) {
		String id = model.getAutoIncrementId();
		String name = getTableName(model.getClass());
		return (name != null && id != null && !id.equals(""));
	}

	/**
	 * 检查模型数据是否有效(查询列表情况下)
	 * 
	 */
	public static boolean isValidForSearchable(BaseModel model) {

		String name = getTableName(model.getClass());
		return (name != null && !name.equals(""));
	}

	/**
	 * 获取字段名称的连接字符串
	 * 
	 * @return
	 */
	public static String[] getNamesForField(Class<?> modelType) {

		int index = 0;
		List<Field> list = getFieldList(modelType);
		if (list == null || list.size() == 0) {
			return null;
		}

		String[] result = new String[list.size()];
		for (Field item : list) {
			result[index++] = item.getName().toLowerCase();
		}
		return result;
	}

	/**
	 * 通过模型获取ContentValues
	 * 
	 * @return ContentValues支持数据库插入
	 */
	public static ContentValues getContentValues(BaseModel model) {
		Object obj = null;
		ContentValues values = new ContentValues();
		List<Field> fieldInfos = getFieldList(model.getClass());

		for (Field item : fieldInfos) {
			// DebugTool.info("item name is ="+item.getName());
			try {
				obj = item.get(model);

				IgnoreField field = item.getAnnotation(IgnoreField.class);
				if (field != null) {
					continue;
				}
			} catch (IllegalArgumentException e) {
//				DebugTool.error("获取模型数据出错。", e);
			} catch (IllegalAccessException e) {
//				DebugTool.error("获取模型数据出错。", e);
			}

			if (obj == null) {
				// DebugTool.info(item.getName() + " is NULL.");
				continue;
			}

			// 针对不同的数据类型进行处理
			if (isString(item)) {
				values.put(item.getName(), obj.toString());
			} else if (isLong(item)) {
				values.put(item.getName(), Long.valueOf(obj.toString()));
			} else if (isDouble(item)) {
				values.put(item.getName(), Double.valueOf(obj.toString()));
			} else if (isInteger(item)) {
				values.put(item.getName(), Integer.valueOf(obj.toString()));
			}

			else {
//				DebugTool.error(" 未知的数据类型,请联系负责人。", null);
			}
		}
		return values;
	}

	/**
	 * 获取所有的字段信息
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<Field> getFieldList(Class<?> modelType) {
		List<Field> fieldInfos = new ArrayList<Field>();

		for (Class cls = modelType; !cls.equals(Object.class); cls = cls
				.getSuperclass()) {

			for (Field item : cls.getDeclaredFields()) {
				if (item.getName().equals("serialVersionUID")) {
					continue;
				}
				IgnoreField field = item.getAnnotation(IgnoreField.class);
				if (field != null) {
					continue;
				}
				item.setAccessible(true);
				fieldInfos.add(item);
			}
		}
		return fieldInfos;
	}

	/**
	 * 获取数据模型的数据库创建表语句
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getCreateTableSql(Class<?> modelType) {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE " + getTableName(modelType) + "(");
		for (Class cls = modelType; !cls.equals(Object.class); cls = cls
				.getSuperclass()) {

			for (Field item : cls.getDeclaredFields()) {
				if (item.getName().equals("serialVersionUID")) {
					continue;
				}
				IgnoreField field = item.getAnnotation(IgnoreField.class);
				if (field != null) {
					continue;
				}
				if (item.getName().equals("autoIncrementId")) {
					sb.append("autoIncrementId INTEGER PRIMARY KEY AUTOINCREMENT,");
				} else {
					sb.append(getFieldValue(item));
				}
			}
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(");");
//		DebugTool.debug("BaseModelTool", "" + sb.toString());
		return sb.toString();
	}

	/**
	 * 修改数据库的表名
	 * */
	public static String alterTableNameSql(Class<?> modelType) {
		StringBuilder sb = new StringBuilder();
		sb.append("ALTER TABLE " + getTableName(modelType) + " RENAME TO "
				+ getTableName(modelType) + "_temp");
		return sb.toString();
	}

	/**
	* 检查表中某列是否存在
	* @param db
	* @param tableName 表名
	* @param columnName 列名
	* @return
	*/
	public static boolean checkColumnExists(SQLiteDatabase db, Class<?> modelType
	       , String columnName) {
		String tableName = getTableName(modelType);
	    boolean result = false ;
	    Cursor cursor = null ;

	    try{
	        cursor = db.rawQuery( "select * from sqlite_master where name = ? and sql like ?"
	           , new String[]{tableName , "%" + columnName + "%"} );
	        result = null != cursor && cursor.moveToFirst() ;
	    }catch (Exception e){
	    }finally{
	        if(null != cursor && !cursor.isClosed()){
	            cursor.close() ;
	        }
	    }

	    return result ;
	}
	/**
	 * 从拷贝表复制数据至新表中；
	 * */
	public static String insertIntoOldTableSql(Class<?> modelType,
			SQLiteDatabase db) {
		String columns[] = getColumnNames(db, getTableName(modelType) + "_temp");
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO " + getTableName(modelType) + "(");
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].equals("id")) {
				continue;
			}
			sb.append(columns[i] + ",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(") SELECT ");
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].equals("id")) {
				continue;
			}
			sb.append(columns[i] + ",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" FROM " + getTableName(modelType) + "_temp");
		return sb.toString();
	}

	/**
	 * 获取表的列名
	 * */
	public static String[] getColumnNames(SQLiteDatabase db, String tableName) {
		String[] columnNames = null;
		Cursor c = null;

		try {
			c = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
			if (null != c) {
				int columnIndex = c.getColumnIndex("name");
				if (-1 == columnIndex) {
					return null;
				}

				int index = 0;
				columnNames = new String[c.getCount()];
				for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
					columnNames[index] = c.getString(columnIndex);
					index++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			c.close();
		}

		return columnNames;
	}

	/**
	 * 删除数据模型的数据库拷贝表语句
	 * */
	public static String deleteCopyTableSql(Class<?> modelType) {
		return "DROP TABLE IF EXISTS " + getTableName(modelType) + "_temp;";
	}

	/**
	 * 获取数据模型的数据库删除表语句
	 * 
	 * @return
	 */
	public static String getDropTableSql(Class<?> modelType) {

		return "DROP TABLE IF EXISTS " + getTableName(modelType) + ";";
	}

	/**
	 * 添加新列的sql语句
	 * */
	public static String getAddNewColumnSql(Class<?> modelType, String columnName) {
		return "ALTER TABLE " + getTableName(modelType) + " ADD COLUMN " + columnName +" text default '0'"+";";
	}

	/**
	 * sw 加的注释
	 * 
	 * @Title: getFieldValue
	 * @Description: 获取属性的名称
	 * @param @param field
	 * @param @return
	 * @return String
	 * @throws
	 */
	private static String getFieldValue(Field field) {
		if (isInteger(field)) {
			return field.getName() + " INTEGER,";
		} else if (isDouble(field)) {
			return field.getName() + " DOUBLE,";
		} else {
			return field.getName() + " TEXT,";
		}
	}

	/**
	 * 获取注解的表名称
	 * 
	 * @param modelType
	 * @return
	 */
	public static String getTableName(Class<?> modelType) {
		TableDescription table = modelType
				.getAnnotation(TableDescription.class);
		if (table != null) {
			return table.name();
		}
		return "";
	}

	/**
	 * 判断是否是字符串类型
	 * 
	 * @param item
	 * @return
	 */
	public static boolean isString(Field item) {

		if (item.getType().getName().toString().equals("java.lang.String")) {
			return true;
		}
		return false;
	}

	/**
	 * sw 加的注释
	 * 
	 * @Title: isLong
	 * @Description: 判断是否为Long类型数据
	 * @param @param item
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isLong(Field item) {
		if (item.getType().getName().toString().equals("java.lang.Long")) {
			return true;
		}
		return false;
	}

	/**
	 * sw 加的注释
	 * 
	 * @Title: isInteger
	 * @Description: 判断是否是整形
	 * @param @param item
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isInteger(Field item) {
		if (item.getType().getName().toString().equals("java.lang.Integer")
				|| item.getType().getName().toString().equals("int")) {
			return true;
		}
		return false;
	}

	public static boolean isDouble(Field item) {
		if (item.getType().getName().toString().equals("java.lang.Double")
				|| item.getType().getName().toString().equals("double")) {
			return true;
		}
		return false;
	}

	/**
	 * sw 加的注释
	 * 
	 * @Title: getField
	 * @Description: 获取属性
	 * @param @param modelType
	 * @param @param fieldName
	 * @param @return
	 * @return Field
	 * @throws
	 */
	public static Field getField(Class<? extends Object> modelType,
			String fieldName) {

		Field field = null;
		for (Class<?> cls = modelType; !cls.equals(Object.class)
				&& field == null; cls = cls.getSuperclass()) {
			try {

				field = cls.getDeclaredField(fieldName);
				field.setAccessible(true);
			} catch (Exception e) {

			}
		}
		return field;
	}

	public static String getCreateTableSqlNoSuper(Class<?> modelType) {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE " + getTableName(modelType) + "(");

		Class cls = modelType;
		if (!cls.equals(Object.class)) {
			for (Field item : cls.getDeclaredFields()) {
				if (item.getName().equals("serialVersionUID")) {
					continue;
				}
				if (item.getName().equals("id")) {
					sb.append("id INTEGER PRIMARY KEY AUTOINCREMENT,");
				} else {
					sb.append(getFieldValue(item));
				}
			}
		}

		sb.deleteCharAt(sb.length() - 1);
		sb.append(");");
		return sb.toString();
	}

    /**
     * @author wenfuqiang
     * @Title: tabIsExist   
     * @Description: TODO(判断表格是否存在)   
     * @param: @param tabName
     * @param: @param db
     * @param: @return      
     * @return: boolean      
     * @throws
     */
	public static boolean tabIsExist(String tabName, SQLiteDatabase db) {
		boolean result = false;
		if (tabName == null) {
			return false;
		}
		Cursor cursor = null;
		try {

			String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"
					+ tabName.trim() + "' ";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

}
