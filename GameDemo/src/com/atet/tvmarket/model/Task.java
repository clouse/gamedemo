package com.atet.tvmarket.model;

import android.content.Context;

public class Task implements RequestCancelable {
	private ReqConfig mReqConfig;
	private Requestable mRequestable;
	private Cancelable mCancelable;
	protected Object mObject;

	public Task(ReqConfig reqConfig, Requestable requestable,
			Cancelable cancelable) {
		super();
		if(reqConfig != null){
			reqConfig.setTask(this);
		}
		this.mReqConfig = reqConfig;
		this.mRequestable = requestable;
		this.mCancelable = cancelable;
	}

	@Override
	public void request(Context context) {
		if (mRequestable != null) {
			mRequestable.request(context, mReqConfig);
		}
	}

	@Override
	public void cancel(Context context) {
		// TODO Auto-generated method stub
		if (mCancelable != null) {
			mCancelable.cancel(context, mReqConfig);
		}
	}
	
	public void recycle(){
		mRequestable = null;
		mCancelable = null;
		
		if(mReqConfig != null){
			mReqConfig.setTask(null);;
		}
		mReqConfig = null;
		
		Object object = mObject;
		if(mObject != null && mObject instanceof DownloadTaskRequest){
			DownloadTaskRequest request = (DownloadTaskRequest)object;
			request.setListener(null);
			request.setFileUrl(null);
			request.setLocalFilePath(null);
			request.setDownloadController(null);
		}
		mObject = null;
		
	}

	public ReqConfig getReqConfig() {
		return mReqConfig;
	}

	public void setReqConfig(ReqConfig mReqConfig) {
		this.mReqConfig = mReqConfig;
	}

	public Object getObject() {
		return mObject;
	}

	public void setObject(Object mObject) {
		this.mObject = mObject;
	}

}
