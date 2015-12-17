package com.atet.tvmarket.model.net.http.download;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.atet.tvmarket.entity.AutoType;
import com.atet.tvmarket.model.database.BaseModel;
import com.atet.tvmarket.model.database.IgnoreField;


/** 
 * @ClassName: FileDownInfo 
 * @Description: 文件下载信息
 * @author: Liuqin
 * @date 2012-12-10 下午4:52:19 
 *  
 */  
@SuppressWarnings("serial")
public class FileDownInfo extends BaseModel implements AutoType,Cloneable,Serializable {
	//文件唯一ID
	private String fileId;
	//下载url
	private String downUrl;
	//本地保存目录
	private String localDir;
	//本地保存文件名
	private String localFilename;
	//文件的总大小
	private int fileSize;
	//线程数量
	private int threadCount;
	//线程下载信息
	private Map<Integer,Integer> threadsInfo;
	//已下载的文件大小
	private int downLen;
	private String extraData;
	private long threadId;
	private Object object;
//    //下载类型，1：从本地下载；2：从网络获取真实下载地址
//	private Integer downType;
//	
//	private Integer downToken;
	
	//
	@IgnoreField 
	private int fileType;


    public FileDownInfo(){
		
	}
	
	public FileDownInfo(String fileId, String downUrl, String localDir) {
		this.fileId = fileId;
		this.downUrl = downUrl;
		this.localDir = localDir;
		this.localFilename="";
		this.fileSize=0;
		this.threadCount=0;
		threadsInfo=new ConcurrentHashMap<Integer, Integer>();
	}
	
	public FileDownInfo(String fileId, String downUrl, String localDir,
			String localFilename) {
		super();
		this.fileId = fileId;
		this.downUrl = downUrl;
		this.localDir = localDir;
		this.localFilename = localFilename;
		this.fileSize=0;
		this.threadCount=0;
		threadsInfo=new ConcurrentHashMap<Integer, Integer>();
	}

	public FileDownInfo(String fileId, String downUrl, String localDir,
			String localFilename, int fileSize, int downLen, int threadCount) {
		super();
		this.fileId = fileId;
		this.downUrl = downUrl;
		this.localDir = localDir;
		this.localFilename = localFilename;
		this.fileSize = fileSize;
		this.downLen = downLen;
		this.threadCount = threadCount;
		threadsInfo=new ConcurrentHashMap<Integer, Integer>();
	}
	
	public FileDownInfo(String fileId, String downUrl, String localDir,
			String localFilename, int fileSize, int threadCount,
			Map<Integer, Integer> threadsInfo, int downLen, String extraData,
			long threadId) {
		super();
		this.fileId = fileId;
		this.downUrl = downUrl;
		this.localDir = localDir;
		this.localFilename = localFilename;
		this.fileSize = fileSize;
		this.threadCount = threadCount;
		this.threadsInfo = threadsInfo;
		this.downLen = downLen;
		this.extraData = extraData;
		this.threadId = threadId;
	}

	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getDownUrl() {
		return downUrl;
	}
	public void setDownUrl(String downUrl) {
		this.downUrl = downUrl;
	}
	public String getLocalDir() {
		return localDir;
	}
	public void setLocalDir(String localDir) {
		this.localDir = localDir;
	}
	public String getLocalFilename() {
		return localFilename;
	}
	public void setLocalFilename(String localFilename) {
		this.localFilename = localFilename;
	}
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	public int getDownLen() {
		return downLen;
	}
	public void setDownLen(int downLen) {
		this.downLen = downLen;
	}

	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public Map<Integer, Integer> getThreadsInfo() {
		return threadsInfo;
	}

	public void setThreadsInfo(Map<Integer, Integer> threadsInfo) {
		this.threadsInfo = threadsInfo;
	}
//	public void setDownType(Integer downType){
//		this.downType=downType;
//	}
//	
//	public Integer getDownType(){
//		return downType;
//	}
//	
//	public void setDownToken(Integer downToken){
//		this.downToken=downToken;
//	}
//	
//	public Integer getDownToken(){
//		return downToken;
//	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
        FileDownInfo obj = null;  
        try {  
            obj = (FileDownInfo) super.clone();  
        } catch (CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return obj;  
    }

	public String getExtraData() {
		return extraData;
	}

	public void setExtraData(String extraData) {
		this.extraData = extraData;
	}

	public long getThreadId() {
		return threadId;
	}

	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
	
	public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

	@Override
	public String toString() {
		return "FileDownInfo [fileId=" + fileId + ", downUrl=" + downUrl
				+ ", localDir=" + localDir + ", localFilename=" + localFilename
				+ ", fileSize=" + fileSize + ", threadCount=" + threadCount
				+ ", threadsInfo=" + threadsInfo + ", downLen=" + downLen
				+ ", extraData=" + extraData + ", threadId=" + threadId
				+ ", object=" + object + "]";
	}
	
	

}

