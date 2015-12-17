package com.atet.tvmarket.utils;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
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
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.app.Constant.DEVICE_TYPE;
import com.atet.tvmarket.control.home.MainActivity;
import com.atet.tvmarket.entity.FileDownResultInfo;
import com.atet.tvmarket.entity.NewVersionInfoResp;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.DataRequester;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.view.NewToast;

public class CheckMarketVersionUtil {
	ALog alog = ALog.getLogger(CheckMarketVersionUtil.class);
	
	private Context context;
	private NewVersionInfoResp latestVerInfo;
	private AlertDialog updateDialog;
	private ProgressDialog proDialog;
	private int progress = 0;
	private String filePath;
	// 进度条
	private ProgressBar downloadBar;
		// 进度值
	private TextView progressTv;
	private int max=0;
	
	private DataRequester downloadDataRequest;
	
	private static class SingletonHolder {  
		private static final CheckMarketVersionUtil INSTANCE = new CheckMarketVersionUtil();  
	}  
	
	private CheckMarketVersionUtil(){
		
	}
	
	public static final CheckMarketVersionUtil getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	public RelativeLayout addLayout() {
		RelativeLayout relativeLayout = new RelativeLayout(context);
		TextView textView = new TextView(context);
		textView.setId(1);
		textView.setTextSize(15);
		textView.setTextColor(Color.BLACK);
		textView.setText(R.string.update_downloading_msg);
		RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
				RelativeLayout.TRUE);
		layoutParams1.setMargins(10, 10, 0, 5);
		relativeLayout.addView(textView, layoutParams1);

		progressTv = new TextView(context);
		progressTv.setId(2);
		progressTv.setTextSize(15);
		progressTv.setTextColor(Color.BLACK);
		progressTv.setText("0%");
		RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams2.addRule(RelativeLayout.RIGHT_OF, 1);
		layoutParams2.addRule(RelativeLayout.CENTER_HORIZONTAL,
				RelativeLayout.TRUE);
		layoutParams2.setMargins(10, 10, 0, 0);
		relativeLayout.addView(progressTv, layoutParams2);
		LayoutInflater inflater = LayoutInflater.from(context);
		downloadBar = (ProgressBar) inflater.inflate(
				R.layout.dialog_progress, null);
		downloadBar.setId(3);
		downloadBar.setMax(100);
		downloadBar.setProgress(0);
		RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(
				250, LayoutParams.WRAP_CONTENT);
		layoutParams3.addRule(RelativeLayout.CENTER_HORIZONTAL,
				RelativeLayout.TRUE);
		layoutParams3.addRule(RelativeLayout.BELOW, 1);
		layoutParams3.setMargins(10, 5, 0, 2);
		relativeLayout.addView(downloadBar, layoutParams3);
		return relativeLayout;
	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0: //更新版本
				updateDialog = new AlertDialog.Builder(context)
				.setTitle(R.string.update_launcher_version_found_title)
				.setMessage(latestVerInfo.getRemark())
				.setPositiveButton(R.string.update_ok, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						mHandler.sendEmptyMessage(2);
						mHandler.post(new Runnable() {
							
							@Override
							public void run() {
								downloadNewVersionApk(latestVerInfo);
							}
						});
					}
				})
				.setNegativeButton(R.string.update_cancel,
						new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(updateDialog!=null){
							updateDialog.cancel();
							updateDialog=null;
							if(latestVerInfo.isForce()){
								((MainActivity) context).finish();
							}
						}
					}
				}).setCancelable(false).setOnKeyListener(mOnDialogKeyListener).show();
				
				Button PositiveButton=((AlertDialog)updateDialog).getButton(AlertDialog.BUTTON_POSITIVE);
				PositiveButton.requestFocusFromTouch();
				
				AppUtil.setDialogAlpha(updateDialog,180);
				break;
			case 1://不需要更新或没有检测到版本
				if(!((MainActivity)context).isFinishing() && updateDialog!=null && updateDialog.isShowing()){
					updateDialog.dismiss();
				}
				break;
			case 2: //开始下载
				boolean isExistIfCancel = false;
				final boolean isExistIfCancelFinal = isExistIfCancel;
				int btnStr = isExistIfCancelFinal ? R.string.update_cancel_update_and_exit : R.string.update_cancel_update;
				updateDialog = new AlertDialog.Builder(context)
						.setTitle(R.string.update_updating)
						.setView(addLayout())
						.setCancelable(!isExistIfCancelFinal)
						.setNegativeButton(btnStr, new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if(latestVerInfo.isForce()){
									if(downloadDataRequest!=null){
										downloadDataRequest.cancel(context);
									}
									((MainActivity) context).finish();
								}
							}
						})
						.setOnCancelListener(
								new DialogInterface.OnCancelListener() {
									@Override
									public void onCancel(DialogInterface dialog) {
										if(latestVerInfo.isForce()){
											if(downloadDataRequest!=null){
												downloadDataRequest.cancel(context);
											}
											((MainActivity) context).finish();
										}
									}
								}).create();
				updateDialog.setOnKeyListener(mOnDialogKeyListener);
				updateDialog.show();
				AppUtil.setDialogAlpha(updateDialog,180);
				
				/*if(proDialog!=null && proDialog.isShowing()){
					proDialog.dismiss();
				}
				proDialog = new ProgressDialog(context);
				proDialog.setMessage("正在下载，请稍候...");
				if(latestVerInfo.isForce()){
					proDialog.setCancelable(false);
				}
				else{
					proDialog.setCancelable(true);
				}
				proDialog.show();*/
				break;
			case 3://下载成功
				if(!((MainActivity)context).isFinishing() && updateDialog!=null && updateDialog.isShowing()){
					updateDialog.dismiss();
				}
				proDialog = new ProgressDialog(context);
				proDialog.setMessage("正在升级，稍候请重新进入游戏大厅");
				proDialog.setCanceledOnTouchOutside(false);
				if(latestVerInfo.isForce()){
					proDialog.setCancelable(false);
				}
				else{
					proDialog.setCancelable(true);
				}
				proDialog.setIndeterminateDrawable(null);
				proDialog.show();
				mHandler.sendEmptyMessage(8);
				break;
			case 4://下载失败
				String errMsg = (String) msg.obj;
				if (errMsg == null && errMsg.length() <= 0) {
					errMsg = context.getString(R.string.update_failed);
				}
				NewToast.makeToast(context, errMsg, Toast.LENGTH_SHORT).show();
				if (!((MainActivity)context).isFinishing() && updateDialog != null && updateDialog.isShowing()) {
					updateDialog.dismiss();
				}
				if(latestVerInfo.isForce()){
					((MainActivity) context).finish();
				}
				break;
			case 5: //下载进度
				int pro = msg.arg1;
				downloadBar.setProgress(pro);
				progressTv.setText(progress+"%");
				break;
			case 6:// 安装成功
				NewToast.makeToast(context, "升级成功",
						Toast.LENGTH_SHORT).show();
				break;
			case 7:// 安装失败
				NewToast.makeToast(context, "升级失败",
						Toast.LENGTH_SHORT).show();
				if (proDialog != null && proDialog.isShowing()) {
					proDialog.dismiss();
					proDialog = null;
				}
				break;
			case 8://开始安装
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						installApk(context, filePath,
								BaseApplication.getContext().getPackageName());
					}
				});
				break;
				
			case 9:
				int max = (Integer) msg.arg1;
				downloadBar.setMax(max);
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
	
	/**
	 * @description: 下载新版本的apk文件
	 * 
	 * @author: LiuQin
	 * @date: 2015年7月29日 下午2:02:46
	 */
	private void downloadNewVersionApk(NewVersionInfoResp info) {
		ReqCallback<FileDownResultInfo> reqCallback = new ReqCallback<FileDownResultInfo>() {
			@Override
			public void onResult(TaskResult<FileDownResultInfo> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					// 下载成功
					FileDownResultInfo info = taskResult.getData();
					// 获取本地的apk路径
					filePath = info.getPath();
					alog.info("filepath:" + filePath);
					mHandler.sendEmptyMessage(3);
				} else {
					// 下载失败
					Message msg = Message.obtain();
					msg.what = 4;
					msg.obj = taskResult.getMsg();
					mHandler.sendMessage(msg);
				}
			}

			@Override
			public void onProgressChange(long fileSize, long downloadedSize) {
				// 下载进度更新，fileSize,原文件总大小；downloadedSize,已经下载的大小
				/*downloadBar.setMax((int)fileSize);
				*/
				if(max==0){
					max = (int) fileSize;
					Message msg = Message.obtain();
					msg.what = 9;
					msg.arg1 = max;
					mHandler.sendMessage(msg);
				}
				Message msg = Message.obtain();
				msg.what = 5;
				msg.arg1 = (int) downloadedSize;
				progress = (int) (downloadedSize * 100 / fileSize);
				mHandler.sendMessage(msg);
				alog.debug("fileSize:" + fileSize + " downloadedSize:"
						+ downloadedSize + "progress:" + progress);
				
			}
		};
		downloadDataRequest = DataFetcher.downloadNewVersionApk(context,
				reqCallback, info, false);
		// 下载
		downloadDataRequest.request(context);

		// 取消下载
		// dataRequest.cancel(getContext());
	}
	
	public void installApk(final Context context, final String filePath,
			final String packageName) {
		Uri mPackageURI = Uri.fromFile(new File(filePath));
		int installFlags = 0;
		PackageManager mPm = context.getPackageManager();
		try {
			PackageInfo pi = mPm.getPackageInfo(packageName,
					PackageManager.GET_UNINSTALLED_PACKAGES);
			if (pi != null) {
				installFlags |= PackageManager.INSTALL_REPLACE_EXISTING;
			}
		} catch (NameNotFoundException e) {
		} catch (Exception e) {
		}
		if ((installFlags & PackageManager.INSTALL_REPLACE_EXISTING) != 0) {
			alog.warn("[installApk2] Replacing package:" + packageName);
		}
		PackageInstallObserver observer = new PackageInstallObserver();
		observer.setContext(context);
		observer.setmPackageURI(mPackageURI);
		try {
			mPm.installPackage(mPackageURI, observer, installFlags, packageName);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			installApk2(context, mPackageURI);
		}
	}

	public void installApk2(Context context, Uri mPackageURI) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 启动新的activity
			// 设置Uri和类型
			alog.debug("mPackageUri = " + mPackageURI);
			intent.setDataAndType(mPackageURI,
					"application/vnd.android.package-archive");
			// 执行安装
			context.startActivity(intent);
		} catch (Exception f) {
			f.printStackTrace();
			// NewToast.makeToast(context,"升级失败",Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessage(7);
		}
	}

	class PackageInstallObserver extends IPackageInstallObserver.Stub {

		@Override
		public void packageInstalled(String packageName, final int returnCode)
				throws RemoteException {
			if (returnCode == PackageManager.INSTALL_SUCCEEDED) {
				mHandler.sendEmptyMessage(6);
			} else {
				mHandler.sendEmptyMessage(7);
				// installApk2(context,mPackageURI);
			}
			
			InstallExceptionUtils.collectSelfInstallInfoAsync(BaseApplication.getContext(), returnCode);
		}

		private Context context;
		private Uri mPackageURI;

		public void setContext(Context context) {
			this.context = context;
		}

		public void setmPackageURI(Uri mPackageURI) {
			this.mPackageURI = mPackageURI;
		}
	}
	
	private DialogInterface.OnKeyListener mOnDialogKeyListener = new DialogInterface.OnKeyListener() {
		@Override
		public boolean onKey(DialogInterface dialogInterface, int keyCode,
				KeyEvent event) {
			// TODO Auto-generated method stub
			View focusView = ((Dialog) dialogInterface).getCurrentFocus();
			if (focusView == null) {
				return false;
			}
			if (GamepadTool.isButtonB(keyCode) || GamepadTool.isButtonBack(keyCode)) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if(updateDialog!=null){
						updateDialog.cancel();
						updateDialog=null;
						if(latestVerInfo.isForce()){
							((MainActivity)context).finish();
						}
					}
				}
				return true;
			} else if (GamepadTool.isButtonA(keyCode)) {
				KeyEvent keyevent = new KeyEvent(event.getAction(),
						KeyEvent.KEYCODE_ENTER);
				focusView.dispatchKeyEvent(keyevent);
				return true;
			}
			else if(GamepadTool.isButtonL1(keyCode)){
				
				return true;
			}
			if (GamepadTool.isButtonXY(keyCode)) {
				return true;
			}
			return false;
		}
	};
}
