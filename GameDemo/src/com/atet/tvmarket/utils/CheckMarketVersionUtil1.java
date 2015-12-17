package com.atet.tvmarket.utils;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant.DEVICE_TYPE;
import com.atet.tvmarket.control.home.MainActivity;
import com.atet.tvmarket.entity.FileDownResultInfo;
import com.atet.tvmarket.entity.NewVersionInfoResp;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.DataRequester;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.view.CommonProgressDialog;
import com.atet.tvmarket.view.VersionUpdateProgressDialog;

public class CheckMarketVersionUtil1 {
	ALog alog = ALog.getLogger(CheckMarketVersionUtil1.class);
	
	private Context context;
	private NewVersionInfoResp latestVerInfo;
	private VersionUpdateProgressDialog updateDialog;
	private static class SingletonHolder {  
		private static final CheckMarketVersionUtil1 INSTANCE = new CheckMarketVersionUtil1();  
	}  
	
	private CheckMarketVersionUtil1(){
		
	}
	
	public static final CheckMarketVersionUtil1 getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0: //更新版本
				updateDialog = new VersionUpdateProgressDialog.Builder(context).create(latestVerInfo);
				updateDialog.setParams(updateDialog);
				updateDialog.setCanceledOnTouchOutside(false);
				if(latestVerInfo.isForce()){
					updateDialog.setCancelable(false);
					updateDialog.setForce(true);
				}
				else{
					updateDialog.setCancelable(true);
					updateDialog.setForce(false);
				}
				updateDialog.show();
				
				break;
			case 1://不需要更新或没有检测到版本
				if(!((MainActivity)context).isFinishing() && updateDialog!=null && updateDialog.isShowing()){
					updateDialog.dismiss();
				}
				break;
			default:
				break;
			}
		}
	};
	
	public void checkVersion(Context context){
		this.context = context;
		ReqCallback<NewVersionInfoResp> reqCallback = new ReqCallback<NewVersionInfoResp>() {
			@Override
			public void onResult(TaskResult<NewVersionInfoResp> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					final NewVersionInfoResp info = taskResult.getData();
					if(info.isNewVersionExist()){
						// 存在新版本
						alog.info("有新版本可以升级");
						alog.info(info.toString());
						latestVerInfo = info;
						mHandler.sendEmptyMessage(0);
					} else {
						// 已经是最新版本
						alog.info("大厅已经是最新版本");
						mHandler.sendEmptyMessage(1);
					}
				} else {
					// 获取失败原因
					String errMsg = taskResult.getMsg();
					mHandler.sendEmptyMessage(1);
				}

			}
		};
		DataFetcher.getNewVersionInfo(context, reqCallback, false)
			.request(context);

	}
}
