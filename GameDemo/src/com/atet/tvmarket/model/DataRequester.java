package com.atet.tvmarket.model;

import android.content.Context;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.common.event.EventBus;
import com.atet.tvmarket.entity.TaskEvent;
import com.atet.tvmarket.model.ReqConfig.ReqType;
import com.atet.tvmarket.model.task.TaskResult;

/**
 * @description: 数据请求类
 *
 * @author: LiuQin
 * @date: 2015年7月11日 下午12:52:14 
 */
public class DataRequester {
	private ReqConfig mReqConfig;
	ALog alog = ALog.getLogger(DataRequester.class);

	public DataRequester(ReqConfig reqConfig) {
		this.mReqConfig = reqConfig;
	}

	/**
	 * @description: 注册数据更新的事件监听，需要时调用，当有数据更新时，会回调到ReqCallback的onUpdate接口
	 * 			          当不再需要监听时调用unregisterUpdateListener注销
	 *
	 * @param reqCallback
	 * @author: LiuQin
	 * @date: 2015年7月11日 上午11:12:03
	 */
	public DataRequester registerUpdateListener(ReqCallback reqCallback) {
//		EventBus.getDefault().register(this);
		return this;	
	}

	/**
	 * @description: 注销数据更新的事件监听
	 * 
	 * @author: LiuQin
	 * @date: 2015年7月11日 上午11:12:14
	 */
	public void unregisterUpdateListener() {
//		EventBus.getDefault().unregister(this);
	}

	/**
	 * @description: 请求数据，当ReqCallback不为空时，会回调到ReqCallback的onResult接口
	 * 
	 * @author: LiuQin
	 * @date: 2015年7月11日 上午11:24:02
	 */
	public boolean request(Context context) {
		return doRequest(context, ReqType.REQUEST);
	}

	/**
	 * @description: 取消调用request方法进行的数据请求
	 * 
	 * @author: LiuQin
	 * @date: 2015年7月11日 上午11:24:12
	 */
	public void cancel(Context context) {
		ReqConfig reqConfig = mReqConfig;
		if(reqConfig != null){
			Task task = reqConfig.getTask();
			if(task != null){
				task.cancel(context);
			}
		}
	}
	
	public void onEvent(TaskEvent taskEvent){
		alog.debug("");
		ReqConfig reqConfig = mReqConfig;
		if(reqConfig != null){
			if(reqConfig.getTaskId().equals(taskEvent.getTag())){
				DataHelper.onUpdateCallback(BaseApplication.getContext(), reqConfig, taskEvent.getTaskResult());
			}
		}
	}
	
	/**
	 * @description: 设置请求码，可根据此码判断是否同一请求
	 *
	 * @param requestCode 
	 * @author: LiuQin
	 * @date: 2015年8月5日 上午11:18:42
	 */
	public void setRequestTag(String requestTag){
		if(mReqConfig != null){
			mReqConfig.setRequestTag(requestTag);
		}
	}
	
	/**
	 * @description: 获取请求码
	 *
	 * @param requestCode 
	 * @author: LiuQin
	 * @date: 2015年8月5日 上午11:19:22
	 */
	public void getRequestTag(String requestTag){
		if(mReqConfig != null){
			mReqConfig.getRequestTag();
		}
	}
	
	/**
	 * @description: 刷新数据
	 *
	 * @param context
	 * @return 
	 * @author: LiuQin
	 */
	public boolean refresh(Context context) {
		return doRequest(context, ReqType.REFRESH);
	}
	
	/**
	 * @description: 更新数据
	 *
	 * @param context
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月12日 上午3:23:30
	 */
	public boolean update(Context context) {
		return doRequest(context, ReqType.UPDATE);
	}
	
	public boolean doRequest(Context context, int reqType) {
		boolean result = false;
		ReqConfig reqConfig = mReqConfig;
		if(reqConfig.getTask() == null){
			reqConfig.setReqType(reqType);
			if(reqConfig != null){
				DataHelper.doRequest(context, reqConfig);
			}
			result = true;
		}
		return result;
	}
}
