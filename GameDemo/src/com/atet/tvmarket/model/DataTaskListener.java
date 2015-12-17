package com.atet.tvmarket.model;

import java.util.List;

import android.content.Context;

import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.entity.InterfaceInfo;
import com.atet.tvmarket.entity.UpdateInterfaceReq;
import com.atet.tvmarket.entity.dao.LocalUpdateInfo;
import com.atet.tvmarket.entity.dao.UpdateInterfaceInfo;
import com.atet.tvmarket.model.ReqConfig.ReqType;
import com.atet.tvmarket.model.task.TaskResult;

public abstract class DataTaskListener<T, Q> extends TaskListener<T> {
	private ReqConfig mReqConfig;
	
	public TaskResult<T> doTaskInBackground() {
		if(!TimeHelper.isServerTimeSyn() && mReqConfig.getReqCode() != ReqConfig.ReqCode.SYNC_TIME){
			syncTime(mReqConfig);
		} else {
			doWork();
		}
		return null;
	}
	
	private void doWork(){
		if(mReqConfig.getReqType() == ReqType.REFRESH){
			doRefresh();
		} else if(mReqConfig.getReqType() == ReqType.UPDATE){
			doUpdate();
		} else {
			doRequest();
		}
		recycle();
	}
	
	private void doRefresh(){
		boolean done = onFetchUpdateInterface();
		if(!done){
			doRequest();
		}
	}
	
	private void doUpdate(){
		ReqConfig reqConfig= mReqConfig;
		InterfaceInfo info = DataHelper.getInterfaceInfo(reqConfig);
		if(info != null){
			LocalUpdateInfo localUpdateInfo = DaoHelper.getLocalUpdateInfo(BaseApplication.getContext(), info.getUniqueId());
			if(localUpdateInfo!=null){
				info.setUpdateTime(localUpdateInfo.getUpdateInterfaceInfo().getUpdateTime());
			}
			reqConfig.setInterfaceInfo(info);
		}
		onFetchHttpData();
	}
	
	private void doRequest(){
		T cacheData = onGetCacheData();
		boolean exist = isCacheDataExist(cacheData);
		onGetCacheDataCallback(exist);
		if (exist) {
			Q q = onCacheDataProcess(cacheData);
			onResultCallback(null, q);
		} else {
			if(!onFetchUpdateInterface()){
				onFetchHttpData();
			}
		}
	}

	@Override
	public void onSuccess(T response) {
	}

	/**
	 * @description: 获取缓存数据
	 *
	 * @return
	 * @author: LiuQin
	 * @date: 2015年7月9日 下午1:55:12
	 */
	public abstract T onGetCacheData();

	/**
	 * @description: 缓存数据是否存在
	 *
	 * @param cacheData
	 * @return
	 * @author: LiuQin
	 * @date: 2015年7月9日 下午1:55:22
	 */
	public abstract boolean isCacheDataExist(T cacheData);

	/**
	 * @description: 获取网络数据
	 * 
	 * @author: LiuQin
	 * @date: 2015年7月9日 下午6:12:35
	 */
	public abstract void onFetchHttpData();

	/**
	 * @description: 对缓存数据进一步处理
	 *
	 * @param context
	 * @param reqConfig
	 * @param infos
	 * @author: LiuQin
	 * @date: 2015年7月9日 下午1:55:47
	 */
	public abstract Q onCacheDataProcess(T infos);

	public abstract void onResultCallback(TaskResult<Q> taskResult, Q result);

	public HttpReqCallback<T> onGetHttpReqCallback() {
		return new HttpReqCallback<T>() {
			@Override
			public void onResult(Context context, ReqConfig reqConfig,
					TaskResult<T> taskResult) {
				if (taskResult.getCode() == TaskResult.OK) {
					onResultCallback(null,
							onCacheDataProcess(taskResult.getData()));
				} else {
					Q q = null;
					onResultCallback(TaskResult.makeFailTaskResult(
							taskResult.getCode(), taskResult.getMsg(), q), q);
				}
			}
		};
	}

	public ReqConfig getReqConfig() {
		return mReqConfig;
	}

	public void setReqConfig(ReqConfig reqConfig) {
		this.mReqConfig = reqConfig;
	}
	
	private void onGetCacheDataCallback(boolean exist){
		if(mReqConfig != null){
			DataHelper.onGetCacheDataCallback(null, mReqConfig, exist);
		}
	}
	
	private void recycle(){
		mReqConfig = null;
	}
	
	/**
	 * @description: 获取更新接口
	 *
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月9日 下午9:51:33
	 */
	private boolean onFetchUpdateInterface(){
		ReqConfig reqConfig= mReqConfig;
		InterfaceInfo info = DataHelper.getInterfaceInfo(reqConfig);
		if(info == null){
			return false;
		}
		reqConfig.setInterfaceInfo(info);
		
		Long lastTime = 0l;
		lastTime = (Long)SpHelper.get(BaseApplication.getContext(), SpHelper.KEY_UPDATE_INTERFACE, lastTime);
		UpdateInterfaceReq reqInfo = new UpdateInterfaceReq();
		reqInfo.setLastTime(lastTime);
		HttpReqCallback<List<LocalUpdateInfo>> httpReqCallback =  new HttpReqCallback<List<LocalUpdateInfo>>() {
			@Override
			public void onResult(Context context, ReqConfig reqConfig,
					TaskResult<List<LocalUpdateInfo>> taskResult) {
				if (taskResult.getCode() == TaskResult.OK) {
					List<LocalUpdateInfo> localUpdateInterfaceList=taskResult.getData();
					long updateTime = DataHelper.hasNewDataToUpdate(localUpdateInterfaceList, reqConfig.getInterfaceInfo().getUniqueId());
					if(updateTime > 0){
						reqConfig.getInterfaceInfo().setUpdateTime(updateTime);
						onFetchHttpData();
					} else if(reqConfig.getReqType() == ReqType.REQUEST){
						reqConfig.setInterfaceInfo(null);
						onFetchHttpData();
					} else {
						reqConfig.setInterfaceInfo(null);
						
						//取缓存数据返回
						T cacheData = onGetCacheData();
						boolean exist = isCacheDataExist(cacheData);
						onGetCacheDataCallback(exist);
						if (exist) {
							Q q = onCacheDataProcess(cacheData);
							onResultCallback(null, q);
						} else {
							//回调没有新数据的状态码
//							Q q = null;
//							TaskResult<Q> returnTaskResult = new TaskResult<Q>();
//							returnTaskResult.setCode(TaskResult.REFRESH_NO_NEW_DATA);
//							onResultCallback(returnTaskResult,q);
							onFetchHttpData();
						}
					}
				} else {
					if(reqConfig.getReqType() == ReqType.REQUEST){
						reqConfig.setInterfaceInfo(null);
						onFetchHttpData();
					} else {
						reqConfig.setInterfaceInfo(null);
						//取缓存数据返回
						T cacheData = onGetCacheData();
						boolean exist = isCacheDataExist(cacheData);
						onGetCacheDataCallback(exist);
						if (exist) {
							Q q = onCacheDataProcess(cacheData);
							onResultCallback(null, q);
						} else {
//							Q q = null;
//							onResultCallback(TaskResult.makeFailTaskResult(
//									taskResult.getCode(), taskResult.getMsg(), q), q);
							onFetchHttpData();
						}
					}
				}
			}
		};

		HttpHelper.fetchLocalUpdatableInterface(BaseApplication.getContext(), reqConfig, reqInfo, httpReqCallback);
		return true;
	}
	
	private void syncTime(ReqConfig reqConfig){
		HttpReqCallback<Long> httpReqCallback = new HttpReqCallback<Long>() {
			@Override
			public void onResult(Context context, ReqConfig reqConfig, TaskResult<Long> taskResult) {
				doWork();
			}
		};
		HttpHelper.syncTime(BaseApplication.getContext(), reqConfig, httpReqCallback);
	}
}
