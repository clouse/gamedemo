package com.atet.tvmarket.control.mygame.receiver;

import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.model.entity.MyGameInfo;
import com.atet.tvmarket.model.net.http.download.DownloadTask;
import com.atet.tvmarket.model.net.http.download.FileDownInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class DownStateReceiver extends BroadcastReceiver{

	private Handler mHandler;
	// 系统更新了应用
	private static final int HANDLER_PKG_INSTALLED_REMOVED = 5;
	
	public DownStateReceiver(Handler mHandler){
		this.mHandler = mHandler;
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent == null) {
			return;
		}
		String action = intent.getAction();
		FileDownInfo fileDownInfo = (FileDownInfo) intent
				.getSerializableExtra(DownloadTask.FILE_DOWN_INFO_KEY);
		if (fileDownInfo != null) {
			if (fileDownInfo.getFileType() != DownloadTask.FILE_TYPE_GAME) { // 判断下载类型是否为游戏
				return;
			}
			MyGameInfo myGameInfo = (MyGameInfo) fileDownInfo.getObject();
			if (myGameInfo != null
					&& myGameInfo.getAutoIncrementId() == null) {
				myGameInfo.setAutoIncrementId(myGameInfo.getDbId());
			}
		}
		Message msg = null;

		if (action.equals(DownloadTask.ACTION_ON_DOWNLOAD_PROGRESS)) {
			// 监听下载进度
			int percentage = (int) ((long) fileDownInfo.getDownLen() * 100 / (long) fileDownInfo
					.getFileSize());
			if (percentage > 100 || percentage < 0) {
				percentage = 95;
			}
			msg = mHandler.obtainMessage(
					DownloadTask.STATE_ON_DOWNLOAD_PROGRESS, percentage, 0,
					fileDownInfo.getFileId());
		} else if (action.equals(DownloadTask.ACTION_ON_DOWNLOAD_START)) {
			// 下载开始
			msg = mHandler.obtainMessage(
					DownloadTask.STATE_ON_DOWNLOAD_START,
					fileDownInfo.getFileId());
		} else if (action.equals(DownloadTask.ACTION_ON_DOWNLOAD_FINISH)) {
			// 下载完成
			msg = mHandler.obtainMessage(
					DownloadTask.STATE_ON_DOWNLOAD_FINISH, fileDownInfo);
		} else if (action.equals(DownloadTask.ACTION_ON_DOWNLOAD_ERROR)) {
			// 下载发生错误
			msg = mHandler.obtainMessage(
					DownloadTask.STATE_ON_DOWNLOAD_ERROR,
					fileDownInfo.getFileId());
		} else if (action.equals(DownloadTask.ACTION_ON_DOWNLOAD_STOP)) {
			// 下载停止
			msg = mHandler.obtainMessage(
					DownloadTask.STATE_ON_DOWNLOAD_STOP,
					fileDownInfo.getFileId());
		} else if (action.equals(DownloadTask.ACTION_ON_DOWNLOAD_WAIT)) {  // 下载等待
			msg = mHandler.obtainMessage(
					DownloadTask.STATE_ON_DOWNLOAD_WAIT, fileDownInfo.getFileId());
		} else if (action.equals(DownloadTask.ACTION_ON_APP_INSTALLED)) {    // 游戏安装或者卸载成功
			Bundle b = intent.getBundleExtra("data");
			int fileType = b.getInt(DownloadTask.FILE_TYPE_KEY);
			if (fileType != DownloadTask.FILE_TYPE_GAME) {
				return;
			}
			int opType = b.getInt("opType");  // 类型是安装还是卸载
			MyGameInfo myGameInfo = (MyGameInfo) b
					.getSerializable("myGameInfo");
			if (myGameInfo != null) {
				msg = mHandler.obtainMessage(Constant.HANDLER_PKG_INSTALLED_REMOVED,
						opType, 0, myGameInfo);
			} else {
				return;
			}
		}else if(action.equals(DownloadTask.ACTION_ON_APP_INSTALLED_FAIL)){  // 安装失败
			Bundle b = intent.getBundleExtra("data");
			String packageName = b.getString("packageName");
			msg = mHandler.obtainMessage(Constant.GAME_STATE_INSTALLED_FAIL, packageName);
		}else {
			return;
		}
		mHandler.sendMessage(msg);
	}
	

}
