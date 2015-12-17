package com.atet.tvmarket.model.net.http.download;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.atet.tvmarket.entity.Group;
import com.atet.tvmarket.model.netroid.Exception.DBInvalidException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;




/** 
 * @ClassName: FileDownDAO 
 * @Description: �ļ�������Ϣ��ݿ����
 * @author: Liuqin
 * @date 2012-12-10 ����11:21:51 
 *  
 */  
public class FileDownDAO {
	public static final String FILE_DOWN_TABLE_NAME = "filedownlog";
	public static final String FILE_ID="fileId";
	public static final String DOWN_URL="downUrl";
	public static final String LOCAL_DIR="localDir";
	public static final String LOCAL_FILE_NAME="localFilename";
	public static final String FILE_SIZE="fileSize";
	public static final String THREAD_COUNT="threadCount";
	public static final String THREAD="thread";
	public static final String THREAD1="thread0";
	public static final String THREAD2="thread1";
	public static final String THREAD3="thread2";
	public static final String THREAD4="thread3";
	public static final String THREAD5="thread4";
    public static final String[] FILE_DOWN_ALL_COLUMS = {FILE_ID, DOWN_URL, LOCAL_DIR, LOCAL_FILE_NAME, FILE_SIZE, THREAD_COUNT,
    	THREAD1,THREAD2,THREAD3,THREAD4,THREAD5};
	public static final int MAX_THREAD=5;
		
	
    private SQLiteDatabase db;
    public FileDownDAO(Context context) {
    	db=new DAOHelper(context).getWritableDatabase();
    }
     
    public Map<Integer,Integer> getThreadData(){
    	return null;
    }
    
    /**
     * @Title: getDownloadedLen
     * @Description: ����ļ�id��ȡ�����صĳ���
     * @param fileId
     * @return ���������صĳ��ȣ������ڷ���0
     * @throws
     */
    public int getDownloadedLen(String fileId){
    	FileDownInfo fileDownInfo=findByFileId(fileId);
    	return fileDownInfo.getDownLen();
    }
    
    public int getDownloadedPercent(String fileId) {
        Cursor cursor = null;
        try {
            cursor = db.query(FILE_DOWN_TABLE_NAME,
                    new String[] { FILE_SIZE, THREAD_COUNT, THREAD1, THREAD2,
                            THREAD3, THREAD4, THREAD5 }, FILE_ID + " = ?",
                    new String[] { fileId }, null, null, null);
            if (cursor != null && cursor.getCount() == 1) {
                if (cursor.moveToFirst()) {
                    try {
                        int fileSize = cursor.getInt(cursor
                                .getColumnIndex(FILE_SIZE));
                        int threadCount = cursor.getInt(cursor
                                .getColumnIndex(THREAD_COUNT));

                        long downLen = 0;
                        for (int i = 0; i < threadCount; i++) {
                            int tmp = cursor.getInt(cursor.getColumnIndex(THREAD + i));
                            downLen += tmp;
                        }
                        if (downLen > 0 && fileSize > 0) {
                            return (int) (downLen * 100 / fileSize);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } finally {
            closeCursor(cursor);
        }
        return 0;
    }

    /**
     * �����ļ�������Ϣ�����������½�һ���¼
     */
	public FileDownInfo save(FileDownInfo fileDownInfo) {
		// TODO Auto-generated method stub
		
		if (fileDownInfo==null) {
			String msg = "Attempting to create a fileDownInfo with an empty fileDownInfo";
			throw new DBInvalidException(msg);
		}		
		
		if(findByFileId(fileDownInfo.getFileId())==null){
			createNewFileDownInfo(db,fileDownInfo);
		} else {
			updateExistingFileDownInfo(db,fileDownInfo);
		}
		return fileDownInfo;
	}
	
	/**
	 * ����ļ�id��ȡ�ļ�������Ϣ
	 */
	/**
	 * @Title: findByFileId
	 * @Description: ����ļ�id��ȡ�ļ�������Ϣ
	 * @param id
	 * @return
	 * @throws
	 */
	public FileDownInfo findByFileId(String id){
        Cursor cursor = null;
        FileDownInfo fileDownInfo=null;
        try {
            cursor = db.query(FILE_DOWN_TABLE_NAME, FILE_DOWN_ALL_COLUMS, FILE_ID+ " = ?", new String[]{id}, null, null, null);
            if (cursor!=null && cursor.getCount() == 1) {
                if (cursor.moveToFirst()) {
                	try{
                		String fileId=cursor.getString(cursor.getColumnIndex(FILE_ID));
                		String downUrl=cursor.getString(cursor.getColumnIndex(DOWN_URL));
                		String localDir=cursor.getString(cursor.getColumnIndex(LOCAL_DIR));
                		String localFilename=cursor.getString(cursor.getColumnIndex(LOCAL_FILE_NAME));
                		int fileSize=cursor.getInt(cursor.getColumnIndex(FILE_SIZE));
                		int threadCount=cursor.getInt(cursor.getColumnIndex(THREAD_COUNT));
                		
                		int downLen=0;
                		Map<Integer,Integer> threadInfo=new ConcurrentHashMap<Integer, Integer>();
                		for(int i=0;i<threadCount;i++){
                			int tmp=cursor.getInt(cursor.getColumnIndex(THREAD+i));
                			threadInfo.put(i, tmp);
                			downLen+=tmp;
                		}
                		fileDownInfo=new FileDownInfo(fileId,downUrl,localDir,localFilename,fileSize,downLen,threadCount);
                		fileDownInfo.setThreadsInfo(threadInfo);
                	} catch (Exception e){
                		e.printStackTrace();
                	}
                }
            }
        } finally {
            closeCursor(cursor);
        }

        return fileDownInfo;
	}
	
	/**
	 * @Title: findAllFieDownInfo
	 * @Description: ���������ļ�������Ϣ
	 * @return
	 * @throws
	 */
	public Group<FileDownInfo> findAllFieDownInfo(){
		Cursor cursor = null;
        Group<FileDownInfo> list=null;
        try {
            cursor = db.query(FILE_DOWN_TABLE_NAME, FILE_DOWN_ALL_COLUMS, null, null, null, null, null);
            if (cursor!=null && cursor.getCount()>0) {
            	cursor.moveToFirst();
            	list=new Group<FileDownInfo>();
            	FileDownInfo fileDownInfo=null;
            	for(int j=0;j<cursor.getCount();j++){
                	try{
                		cursor.moveToPosition(j);
                		String fileId=cursor.getString(cursor.getColumnIndex(FILE_ID));
                		String downUrl=cursor.getString(cursor.getColumnIndex(DOWN_URL));
                		String localDir=cursor.getString(cursor.getColumnIndex(LOCAL_DIR));
                		String localFilename=cursor.getString(cursor.getColumnIndex(LOCAL_FILE_NAME));
                		int fileSize=cursor.getInt(cursor.getColumnIndex(FILE_SIZE));
                		int threadCount=cursor.getInt(cursor.getColumnIndex(THREAD_COUNT));
                		
                		int downLen=0;
                		Map<Integer,Integer> threadInfo=new ConcurrentHashMap<Integer, Integer>();
                		for(int i=0;i<threadCount;i++){
                			int tmp=cursor.getInt(cursor.getColumnIndex(THREAD+i));
                			threadInfo.put(i, tmp);
                			downLen+=tmp;
                		}
                		fileDownInfo=new FileDownInfo(fileId,downUrl,localDir,localFilename,fileSize,downLen,threadCount);
                		fileDownInfo.setThreadsInfo(threadInfo);
                		list.add(fileDownInfo);
                	} catch (Exception e){
                		e.printStackTrace();
                	}
                }
            }
        } finally {
            closeCursor(cursor);
        }
		return list;
	}
	
	/**
	 * @Title: createFileDownInfo
	 * @Description: �½�һ���¼
	 * @param fileDownInfo
	 * @return
	 * @throws
	 */
	public boolean createFileDownInfo(FileDownInfo fileDownInfo){
		return createNewFileDownInfo(db,fileDownInfo);
	}
	
	/**
	 * @Title: updateDownInfo
	 * @Description: ����һ���¼
	 * @param fileDownInfo
	 * @return
	 * @throws
	 */
	public boolean updateDownInfo(FileDownInfo fileDownInfo){
		return updateExistingFileDownInfo(db,fileDownInfo);
	}
	
	/**
	 * @Title: updateThreadInfos
	 * @Description: �����߳�������Ϣ
	 * @param fileId
	 * @param threadInfos
	 * @return
	 * @throws
	 */
	public boolean updateThreadInfos(String fileId,Map<Integer,Integer> threadInfos){
		try{
			String strSQL="update filedownlog set thread0=?,thread1=?,thread2=?,thread3=?,thread4=? where fileId='"+fileId+"';";
			String[] value={"0","0","0","0","0"};
			int len=threadInfos.size()>MAX_THREAD?MAX_THREAD:threadInfos.size();
			for(int i=0;i<len;i++){
				value[i]=threadInfos.get(i).toString();
			}
			db.execSQL(strSQL, value);
		} catch (Exception e){
			e.printStackTrace();
		}finally{
			
		}
//		db.close();
		return false;
	}


	/**
	 * ɾ�����м�¼
	 */
	public void deleteAll() {
		// TODO Auto-generated method stub
	     db.delete(FILE_DOWN_TABLE_NAME, null, null);
	}

	/**
	 * ����ļ�idɾ���¼
	 */
	public void delete(FileDownInfo fileDownInfo) {
		// TODO Auto-generated method stub
		db.delete(FILE_DOWN_TABLE_NAME, FILE_ID + " = ?", new String[]{fileDownInfo.getFileId()});
	}
	
	/**
	 * ����ļ�idɾ���¼
	 */
	public void delete(String fileId) {
		// TODO Auto-generated method stub
		db.delete(FILE_DOWN_TABLE_NAME, FILE_ID + " = ?", new String[]{fileId});
	}
	
	public void deleteByLocalFileName(String localFileName){
		if(localFileName==null || localFileName.length()<=0){
			return;
		}
		db.delete(FILE_DOWN_TABLE_NAME, LOCAL_FILE_NAME + " = ?", new String[]{localFileName});
	}
	
	public void closeDB(){
		if(db!=null){
			db.close();
		}
	}
	
	protected void closeCursor(Cursor cursor) {
		if (cursor != null) {
			cursor.close();
		}
	}
	
	private boolean createNewFileDownInfo(SQLiteDatabase db, FileDownInfo fileDownInfo) {
        ContentValues values = new ContentValues();
        values.put(FILE_ID, fileDownInfo.getFileId());
        values.put(DOWN_URL, fileDownInfo.getDownUrl());
        values.put(LOCAL_DIR, fileDownInfo.getLocalDir());
        values.put(LOCAL_FILE_NAME, fileDownInfo.getLocalFilename());
        values.put(FILE_SIZE, fileDownInfo.getFileSize());
        values.put(THREAD_COUNT, fileDownInfo.getThreadCount());
        Map<Integer,Integer> threadInfos=fileDownInfo.getThreadsInfo();
        if(threadInfos!=null){
        	for(int i=0;i<fileDownInfo.getThreadCount();i++){
        		values.put(THREAD+i, threadInfos.get(i));
        	}
        }
        long id = db.insertOrThrow(FILE_DOWN_TABLE_NAME, null, values);
        return id!=-1 ? true: false;
    }	
	
	private boolean updateExistingFileDownInfo(SQLiteDatabase db, FileDownInfo fileDownInfo) {
        ContentValues values = new ContentValues();
        values.put(FILE_ID, fileDownInfo.getFileId());
        values.put(DOWN_URL, fileDownInfo.getDownUrl());
        values.put(LOCAL_DIR, fileDownInfo.getLocalDir());
        values.put(LOCAL_FILE_NAME, fileDownInfo.getLocalFilename());
        values.put(FILE_SIZE, fileDownInfo.getFileSize());
        values.put(THREAD_COUNT, fileDownInfo.getThreadCount());
        Map<Integer,Integer> threadInfos=fileDownInfo.getThreadsInfo();
        if(threadInfos!=null){
        	for(int i=0;i<fileDownInfo.getThreadCount();i++){
        		values.put(THREAD+i, threadInfos.get(i));
        	}
        }
        long id = db.update(FILE_DOWN_TABLE_NAME, values, FILE_ID + " = ?", new String[]{String.valueOf(fileDownInfo.getFileId())});
        return id>=0 ? true :false;
    }
	
	private class DAOHelper extends SQLiteOpenHelper {
		public static final String DATABASE_NAME="download.db";
		public static final int DATABASE_VERSION=1;
		
		//���������б�SQL���
		private static final String CREATE_FILE_DOWN_TABLE = 
				"CREATE TABLE " + FileDownDAO.FILE_DOWN_TABLE_NAME + " (" 
						+ FileDownDAO.FILE_ID+" TEXT NOT NULL PRIMARY KEY, "
						+ FileDownDAO.DOWN_URL+" TEXT NOT NULL, "
						+ FileDownDAO.LOCAL_DIR+" TEXT NOT NULL, "
						+ FileDownDAO.LOCAL_FILE_NAME+" TEXT NOT NULL, "
						+ FileDownDAO.FILE_SIZE+" INTEGER DEFAULT 0, " 
						+ FileDownDAO.THREAD_COUNT+" INTEGER DEFAULT 0, " 
						+ FileDownDAO.THREAD1+" INTEGER DEFAULT 0, " 
						+ FileDownDAO.THREAD2+" INTEGER DEFAULT 0, " 
						+ FileDownDAO.THREAD3+" INTEGER DEFAULT 0, " 
						+ FileDownDAO.THREAD4+" INTEGER DEFAULT 0, " 
						+ FileDownDAO.THREAD5+" INTEGER DEFAULT 0 " 
						+ ");";
		//ɾ�������б�SQL���
		private static final String DROP_FILE_DOWN_TABLE = "DROP TABLE IF EXISTS "
				+ FileDownDAO.FILE_DOWN_TABLE_NAME;
		
		public DAOHelper(Context context) {
			this(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		public DAOHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_FILE_DOWN_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(DROP_FILE_DOWN_TABLE);
			onCreate(db);
		}
	}

}

