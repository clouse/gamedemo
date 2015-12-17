package com.atet.tvmarket.model;

import android.content.Context;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.model.netroid.Listener;
import com.atet.tvmarket.model.netroid.Exception.NetroidError;
import com.atet.tvmarket.model.task.TaskResult;

public abstract class ReqListener<T, Q> extends Listener<T> {
	private Context mContext;
	private ReqConfig mReqConfig;
	private ALog alog = ALog.getLogger(ReqListener.class);
	public TaskResult<Q> mTaskResult = new TaskResult<Q>();
	private HttpReqCallback<Q> mHttpReqCallback;

	private ReqListener() {
	};

	public ReqListener(Context context, ReqConfig reqConfig) {
		mContext = context.getApplicationContext();
		mReqConfig = reqConfig;
	}

	public ReqListener(Context context, ReqConfig reqConfig,
			HttpReqCallback<Q> httpReqCallback) {
		mContext = context.getApplicationContext();
		mReqConfig = reqConfig;
		mHttpReqCallback = httpReqCallback;
	}

	@Override
	public void onError(NetroidError error) {
		// TODO Auto-generated method stub
		super.onError(error);
		alog.warn("error on request", error);
		mTaskResult.setCode(TaskResult.getErrorId(error));
		mTaskResult.setMsg(TaskResult.getErrorMsg(mContext,
				mTaskResult.getCode()));
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		// alog.info("");
		super.onFinish();
		if (mHttpReqCallback != null) {
			mHttpReqCallback.onResult(mContext, mReqConfig, mTaskResult);
		} else {
			DataHelper.onCallback(mContext, mReqConfig, mTaskResult);
		}
		mContext = null;
		mReqConfig = null;
		mTaskResult = null;
		mHttpReqCallback = null;
	}

	public void handleUnsuccessCode(int code) {
		alog.debug(" code:" + code);
		int taskResultCode = TaskResult.FAILED;
		if (code >= 1 && code <= 5) {
			taskResultCode = code + 10;
		} else if (code == 1802) {
			taskResultCode = TaskResult.REPEAT_GIFT;
		} else if (HttpHelper.isNoDataCode(code + "")) {
			taskResultCode = TaskResult.NO_DATA;
		}
		if(taskResultCode!=TaskResult.FAILED){
			mTaskResult.setCode(taskResultCode);
		} else {
			mTaskResult.setCode(code);
		}
		mTaskResult.setMsg(TaskResult.getErrorMsg(mContext,
				mTaskResult.getCode()));
	}

}
