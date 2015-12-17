package com.atet.tvmarket.model;

import com.atet.tvmarket.model.task.TaskResult;

/**
 * @description: 数据请求回调接口
 *
 * @author: LiuQin
 * @date: 2015年7月11日 下午1:09:03 
 */
public abstract class ReqCallback<T> {
	/**
	 * @description: 调用 DataRequester的request方法后结果会回调到这个接口
	 *
	 * @param taskResult 
	 * @author: LiuQin
	 * @date: 2015年7月11日 下午1:09:17
	 */
	public abstract void onResult(TaskResult<T> taskResult);
	
	
	/**
	 * @description: 调用 DataRequester的registerUpdateListener方法可监听数据更新，当有数据更新时会回调此接口
	 * 				  使用完成后调用unregisterUpdateLister方法注销
	 *
	 * @param taskResult 
	 * @author: LiuQin
	 * @date: 2015年7月11日 下午1:09:49
	 */
	public void onUpdate(TaskResult<T> taskResult) {};
	
	/**
	 * @description: 获取缓存数据是否成功
	 *
	 * @param requestTag 用户自行设置的任务标签,如不设置回调为null
	 * @param result 
	 * @throws: 
	 * @author: LiuQin
	 * @date: 2015年8月5日 上午11:31:59
	 */
	public void onGetCacheData(String requestTag, boolean result){};
	
	/**
	 * @description: 获取缓存数据是否成功
	 *
	 * @param result 
	 * @throws: 
	 * @author: LiuQin
	 * @date: 2015年8月5日 上午11:36:27
	 */
	@Deprecated
	public void onGetCacheData(boolean result){};
	
    /**
     * @description: 下载的进度变化
     *
     * @param fileSize 总文件大小
     * @param downloadedSize 已下载的文件大小
     * @author: LiuQin
     * @date: 2015年7月29日 下午2:07:58
     */
	@Deprecated
    public void onProgressChange(long fileSize, long downloadedSize){};
    
    /**
     * @description: 下载的进度变化
     *
	 * @param requestTag 用户自行设置的任务标签,如不设置回调为null
     * @param fileSize
     * @param downloadedSize 
     * @author: LiuQin
     * @date: 2015年8月5日 上午11:31:42
     */
    public void onProgressChange(String requestTag, long fileSize, long downloadedSize){};
}
