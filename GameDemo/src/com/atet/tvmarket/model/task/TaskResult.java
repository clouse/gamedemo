package com.atet.tvmarket.model.task;

import java.io.Serializable;

import android.content.Context;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.model.netroid.Exception.NetroidError;
import com.atet.tvmarket.model.netroid.Exception.NoConnectionError;
import com.atet.tvmarket.model.netroid.Exception.ServerError;
import com.atet.tvmarket.model.netroid.Exception.SignVerifyError;
import com.atet.tvmarket.model.netroid.Exception.TimeoutError;
import com.atet.tvmarket.model.netroid.Exception.UnknownHostError;
import com.atet.tvmarket.model.netroid.request.TaskRequest;

public class TaskResult<T> implements Serializable {
	private static ALog alog = ALog.getLogger(TaskResult.class);
	
	public static final int OK = 0;
	public static final int FAILED = -1;
	// 没有数据
	public static final int NO_DATA = 1;
	// 刷新没有数据
	public static final int REFRESH_NO_NEW_DATA = 2;
	// 无效操作
	public static final int INVALID_OPERATION = 11;
	// 服务器内部错误
	public static final int INTERNAL_ERROR = 12;
	// 服务器json解析错误
	public static final int JSON_PARSE_ERROR = 13;
	// 请求参数错误
	public static final int INVALID_REQUEST_DATA = 14;
	// 无效token
	public static final int INVALID_TOKEN = 15;
	// 重复请求礼包
	public static final int REPEAT_GIFT = 1802;
	// 没有网络权限
	public static final int HTTP_NO_PERMISSION = 20;
	// 连接超时
	public static final int HTTP_CONNECT_TIME_OUT = 21;
	// 服务器内部错误
	public static final int HTTP_SERVER_ERROR = 22;
	// 找不到主机
	public static final int HTTP_UNKNOWN_HOST = 23;
	// NoConnection(Network not reachable)
	public static final int HTTP_NO_CONNECTION = 24;
	// 校验签名失败
	public static final int SIGN_INVALIDATE = 100;

	private int code = FAILED;

	private T data = null;

	private String msg = "";

	private Exception exception;
	
	private String requestTag;
	
	private String taskId;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public static <T> TaskResult<T> makeSuccessTaskResult(T data) {
		TaskResult<T> taskResult = new TaskResult<T>();
		taskResult.setCode(TaskResult.OK);
		taskResult.setData(data);
		return taskResult;
	}
	
	public static <T> TaskResult<T> makeTaskResult(Context context, T data) {
		TaskResult<T> taskResult = new TaskResult<T>();
		if(data != null){
			taskResult.setCode(TaskResult.OK);
			taskResult.setData(data);
		} else {
			taskResult.setCode(TaskResult.NO_DATA);
			taskResult.setMsg(getErrorMsg(context, TaskResult.NO_DATA));
		}
		return taskResult;
	}
	
	public static <T> TaskResult<T> makeFailTaskResult(int failCode, String msg, T t) {
		TaskResult<T> taskResult = new TaskResult<T>();
		taskResult.setCode(failCode);
		taskResult.setMsg(msg);
		return taskResult;
	}

	public static int getErrorId(NetroidError error) {
		int errId = TaskResult.FAILED;
		if (error instanceof TimeoutError) {
			errId = TaskResult.HTTP_CONNECT_TIME_OUT;
		} else if (error instanceof ServerError) {
			errId = TaskResult.HTTP_SERVER_ERROR;
		} else if (error instanceof UnknownHostError) {
			errId = TaskResult.HTTP_UNKNOWN_HOST;
		} else if (error.getCause() instanceof SecurityException){
			errId = TaskResult.HTTP_NO_PERMISSION;
		} else if (error instanceof NoConnectionError){
			errId = TaskResult.HTTP_NO_CONNECTION;
		} else if (error instanceof SignVerifyError){
			errId = TaskResult.SIGN_INVALIDATE;
		}

		alog.warn("Error id:"+errId);
		return errId;
	}

	public static String getErrorMsg(Context context, int errId) {
		if(true){
			return "";
		}
		int resId = 0;
		switch (errId) {
		case TaskResult.HTTP_CONNECT_TIME_OUT:
			break;
		case TaskResult.HTTP_SERVER_ERROR:
			break;
		case TaskResult.HTTP_UNKNOWN_HOST:
			break;

		case TaskResult.NO_DATA:
			// 没有数据
			break;
		case TaskResult.INVALID_OPERATION:
			// 无效操作
			break;
		case TaskResult.INTERNAL_ERROR:
			// 服务器内部错误
			break;
		case TaskResult.JSON_PARSE_ERROR:
			// 服务器json解析错误
			break;
		case TaskResult.INVALID_REQUEST_DATA:
			// 请求参数错误
			break;
		case TaskResult.INVALID_TOKEN:
			// 无效token
			break;
		case TaskResult.REPEAT_GIFT:
			// 重复请求礼包
			break;

		default:
			break;
		}

		return context.getString(resId);
	}

	public String getRequestTag() {
		return requestTag;
	}

	public void setRequestTag(String requestTag) {
		this.requestTag = requestTag;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	public static boolean isNetworkException(int code){
		switch (code) {
		case HTTP_CONNECT_TIME_OUT:
		case HTTP_NO_CONNECTION:
		case HTTP_UNKNOWN_HOST:
			return true;
		}
		return false;
	}
}
