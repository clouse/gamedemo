package com.atet.tvmarket.view;

import java.io.File;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.app.Constant.DEVICE_TYPE;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.entity.FileDownResultInfo;
import com.atet.tvmarket.entity.NewVersionInfoResp;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.DataRequester;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.GamepadTool;
import com.atet.tvmarket.utils.InstallExceptionUtils;
import com.atet.tvmarket.utils.ScaleViewUtils;

public class VersionUpdateProgressDialog extends Dialog{
	private ALog alog = ALog.getLogger(VersionUpdateProgressDialog.class);
	private Context context;
	private boolean isForce;

	public VersionUpdateProgressDialog(Context context) {
		// TODO Auto-generated constructor stub
		 super(context);
		 this.context = context;
		 setOwnerActivity((Activity)context);
		 setOnKeyListener(mOnDialogKeyListener);
	}



	public VersionUpdateProgressDialog(Context context, int theme) {
		 super(context, R.style.Dialog);
		 this.context = context;
		 setOwnerActivity((Activity)context);
		// TODO Auto-generated constructor stub
		 setOnKeyListener(mOnDialogKeyListener);
	}


	public static class Builder {  
		   
	        private Context context;  
	        private String title;  
	        private String message;  
	        private String positiveButtonText;  
	        private String negativeButtonText;
	        private String centerButtonText;
	        private int iconId = -1;
	        private String iconUrl;
	        private View contentView;  
	    	
	        private Button btnUpdate;
	    	private RelativeLayout content, progressLayout;
	    	private FrameLayout fLayout;

	    	private TextView versionName, remark, tip, num;
	    	private ProgressBar progressBar;
	    	private ImageView installProgress;
	    	private AnimationDrawable mAnimationDrawable;
	    	private LoadingView loadingView;
	    	private Dialog mDialog;
	    	private int progress = 0;

	    	private String filePath;
	    	private int mProgress = 0;
	    	private int width = 0;
	    	private float prew = 0f;
	    	private float movelen = 0f;
	    	
	    	private NewVersionInfoResp latestVerInfo;
	        
	        private DialogInterface.OnClickListener   
	                        positiveButtonClickListener,  
	                        negativeButtonClickListener,
	                        centerButtonClickListener;
	        
	        public Builder(Context context) {  
	            this.context = context;  
	        }  
	        /** 
	         * Set the Dialog message from String 
	         * @param title 
	         * @return 
	         */  
	        public Builder setMessage(String message) {  
	            this.message = message;  
	            return this;  
	        }  
	   
	        /** 
	         * Set the Dialog message from resource 
	         * @param title 
	         * @return 
	         */  
	        public Builder setMessage(int message) {  
	            this.message = (String) context.getText(message);  
	            return this;  
	        }  
	   
	        /** 
	         * Set the Dialog title from resource 
	         * @param title 
	         * @return 
	         */  
	        public Builder setTitle(int title) {  
	            this.title = (String) context.getText(title);  
	            return this;  
	        }  
	   
	        /** 
	         * Set the Dialog title from String 
	         * @param title 
	         * @return 
	         */  
	        public Builder setTitle(String title) {  
	            this.title = title;  
	            return this;  
	        }  
	   
	        /** 
	         * Set a custom content view for the Dialog. 
	         * If a message is set, the contentView is not 
	         * added to the Dialog... 
	         * @param v 
	         * @return 
	         */  
	        public Builder setContentView(View v) {  
	            this.contentView = v;  
	            return this;  
	        }  
	   
			public Builder setIcon(int iconId) {
				this.iconId = iconId;
				return this;
			}
			
			public Builder setIcon(String url){
				this.iconUrl = url;
				return this;
			}
			/** 
	         *设置PostiveButton点击事件以及显示文字 
	         * @param positiveButtonText 
	         * @param listener 
	         * @return 
	         */  
	        public Builder setPositiveButton(int positiveButtonText,  
	                DialogInterface.OnClickListener listener) {  
	            this.positiveButtonText = (String) context  
	                    .getText(positiveButtonText);  
	            this.positiveButtonClickListener = listener;  
	            return this;  
	        }  
	   
	        /** 
	         * 设置PostiveButton点击事件以及显示文字 
	         * @param positiveButtonText 
	         * @param listener 
	         * @return 
	         */  
	        public Builder setPositiveButton(String positiveButtonText,  
	                DialogInterface.OnClickListener listener) {  
	            this.positiveButtonText = positiveButtonText;  
	            this.positiveButtonClickListener = listener;  
	            return this;  
	        }  
	   
	        /** 
	         * 设置NegativeButton点击事件以及显示文字 
	         * @param negativeButtonText 
	         * @param listener 
	         * @return 
	         */  
	        public Builder setNegativeButton(int negativeButtonText,  
	                DialogInterface.OnClickListener listener) {  
	            this.negativeButtonText = (String) context  
	                    .getText(negativeButtonText);  
	            this.negativeButtonClickListener = listener;  
	            return this;  
	        }  
	   
	        /** 
	         * 设置NegativeButton点击事件以及显示文字
	         * @param negativeButtonText 
	         * @param listener 
	         * @return 
	         */  
	        public Builder setNegativeButton(String negativeButtonText,  
	                DialogInterface.OnClickListener listener) {  
	            this.negativeButtonText = negativeButtonText;  
	            this.negativeButtonClickListener = listener;  
	            return this;  
	        }  
	        
	        /** 
	         * 设置centerButton点击事件以及显示文字 
	         * @param centerButtonText 
	         * @param listener 
	         * @return 
	         */  
	        public Builder setCenterButton(int centerButtonText,  
	                DialogInterface.OnClickListener listener) {  
	            this.centerButtonText = (String) context  
	                    .getText(centerButtonText);  
	            this.centerButtonClickListener = listener;  
	            return this;  
	        }  
	   
	        /** 
	         * 设置centerButton点击事件以及显示文字
	         * @param negativeButtonText 
	         * @param listener 
	         * @return 
	         */  
	        public Builder setCenterButton(String centerButtonText,  
	                DialogInterface.OnClickListener listener) {  
	            this.centerButtonText = centerButtonText;  
	            this.centerButtonClickListener = listener;  
	            return this;  
	        }
	        
	    	public void setAlapha(VersionUpdateProgressDialog dialog){
	    		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
	    		lp.alpha=0.9f;
	    		dialog.getWindow().setAttributes(lp);               
	    	}
	    	
			/** 
	         * 创建对话框
	         */  
	        public VersionUpdateProgressDialog create(ImageFetcher imageFetcher) {
	            LayoutInflater inflater = (LayoutInflater) context  
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);   
	            final VersionUpdateProgressDialog dialog = new VersionUpdateProgressDialog(context,   
	                    R.style.Dialog);
	            View layout = inflater.inflate(R.layout.version_update_dialog, null);
	        	
	            ScaleViewUtils.scaleView(layout);
	            dialog.setContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	            return dialog;  
	        }
	        
	        /** 
	         * 创建对话框
	         */  
	        public VersionUpdateProgressDialog create(NewVersionInfoResp latestVerInfo) {
	        	this.latestVerInfo = latestVerInfo;
	        	LayoutInflater inflater = (LayoutInflater) context  
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);   
	            final VersionUpdateProgressDialog dialog = new VersionUpdateProgressDialog(context,   
	                    R.style.Dialog);
	            View layout = inflater.inflate(R.layout.version_update_dialog, null);
	            initView(layout);
	            ScaleViewUtils.scaleView(layout);
	            dialog.setContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	            return dialog;  
	        }  
	        
	        private void initView(View view) {

	    		content = (RelativeLayout) view.findViewById(R.id.rl_content);
	    		progressLayout = (RelativeLayout) view.findViewById(R.id.rl_progress);
	    		versionName = (TextView) view.findViewById(R.id.setup_update_dialog_versionname_tv);
	    		remark = (TextView) view.findViewById(R.id.setup_update_dialog_content_tv);
	    		remark.setMovementMethod(ScrollingMovementMethod.getInstance()); 
	    		tip = (TextView) view.findViewById(R.id.tv_tip);
	    		num = (TextView) view.findViewById(R.id.tv_num);
	    		fLayout = (FrameLayout) view.findViewById(R.id.fl_progressbar);
	    		progressBar = (ProgressBar) view.findViewById(R.id.pb_progress);

	    		installProgress = (ImageView) view.findViewById(R.id.iv_install_laoding);
	    		mAnimationDrawable = (AnimationDrawable) installProgress.getDrawable();

	    		btnUpdate = (Button) view.findViewById(R.id.setup_update_dialog_btn);
	    		btnUpdate.requestFocus();
	    		btnUpdate.setOnClickListener(new View.OnClickListener() {
	    			
	    			@Override
	    			public void onClick(View v) {
	    				mHandler.sendEmptyMessage(2);
	    			}
	    		});
	    		
	    		mHandler.sendEmptyMessage(0);
	    		
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
	    				if (code == TaskResult.OK) {
	    					// 下载成功
	    					FileDownResultInfo info = taskResult.getData();
	    					// 获取本地的apk路径
	    					filePath = info.getPath();
	    					mHandler.sendEmptyMessage(3);
	    				} else {
	    					// 下载失败
	    					mHandler.sendEmptyMessage(4);
	    				}
	    			}

	    			@Override
	    			public void onProgressChange(long fileSize, long downloadedSize) {
	    				// 下载进度更新，fileSize,原文件总大小；downloadedSize,已经下载的大小
	    				progress = (int) (downloadedSize * 100 / fileSize);
	    				mHandler.sendEmptyMessage(5);
	    			}
	    		};
	    		DataRequester dataRequest = DataFetcher.downloadNewVersionApk(context,
	    				reqCallback, info, false);

	    		// 下载
	    		dataRequest.request(context);

	    		// 取消下载
	    		// dataRequest.cancel(getContext());
	    	}
	    	
	    	private Handler mHandler = new Handler() {

	    		@Override
	    		public void handleMessage(Message msg) {
	    			switch (msg.what) {
	    			case 0:// 检测版本
	    				versionName.setText("版本  "+latestVerInfo.getVersionName());
	    				remark.setText(latestVerInfo.getRemark());
	    				width = progressBar.getWidth();
	    				prew = (float) width / 100f;
	    				break;
	    			case 1:// 提示版本意识最新
	    				
	    				break;
	    			case 2:// 开始下载
	    				content.setVisibility(View.INVISIBLE);
	    				progressLayout.setVisibility(View.VISIBLE);
	    				downloadNewVersionApk(latestVerInfo);
	    				break;
	    			case 3:// 下载成功
	    				progressBar.setProgress(100);
	    				num.setText(progress + "%");
	    				NewToast.makeToast(context, "下载成功，准备安装",
	    						Toast.LENGTH_SHORT).show();
	    				if (filePath != null) {
	    					mHandler.sendEmptyMessage(8);
	    					// installApk(SetupUpdateActivity.this, filePath,
	    					// DEVICE_TYPE.TV_MARKET_PACKAGE_NAME);
	    				} else {
	    					NewToast.makeToast(context, "没有找到安装路径",
	    							Toast.LENGTH_SHORT).show();
	    					progress = 0;
	    					content.setVisibility(View.VISIBLE);
	    					progressLayout.setVisibility(View.INVISIBLE);
	    				}
	    				break;
	    			case 4:// 下载失败
	    				NewToast.makeToast(context, "下载失败",
	    						Toast.LENGTH_SHORT).show();
	    				progress = 0;
	    				content.setVisibility(View.VISIBLE);
	    				progressLayout.setVisibility(View.INVISIBLE);
	    				break;
	    			case 5:// 更新进度
	    				progressBar.setProgress(progress);
	    				num.setText(progress + "%");
	    				moveView();
	    				break;
	    			case 6:// 安装成功
	    				NewToast.makeToast(context, "升级成功",
	    						Toast.LENGTH_SHORT).show();
	    				mAnimationDrawable.stop();
	    				installProgress.setVisibility(View.INVISIBLE);
	    				content.setVisibility(View.VISIBLE);
	    				progressLayout.setVisibility(View.INVISIBLE);
	    				
	    				break;
	    			case 7:// 安装失败
	    				NewToast.makeToast(context, "升级失败",
	    						Toast.LENGTH_SHORT).show();
	    				mAnimationDrawable.stop();
	    				installProgress.setVisibility(View.INVISIBLE);
	    				content.setVisibility(View.VISIBLE);
	    				progressLayout.setVisibility(View.INVISIBLE);
	    				break;
	    			case 8:// 安装
	    				progress = 0;
	    				tip.setText("正在升级，请稍等...");
	    				fLayout.setVisibility(View.INVISIBLE);
	    				num.setVisibility(View.INVISIBLE);
	    				installProgress.setVisibility(View.VISIBLE);
	    				mAnimationDrawable.start();
	    				mHandler.postDelayed(new Runnable() {

	    					@Override
	    					public void run() {
	    						installApk(context, filePath,
	    								context.getPackageName());
	    					}
	    				}, 1000);
	    				break;
	    			default:
	    				break;
	    			}
	    		}

	    	};
	    	
	    	public void installApk(final Context context, final String filePath,
	    			final String packageName) {
	    		try {
	                File apkFile=new File(filePath);
	                if(apkFile.exists()){
	                    Runtime.getRuntime().exec("chmod 644 "+filePath);
	                }
	            } catch (Exception e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
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

	    	private void moveView() {
	    		ViewCompat.setTranslationX(num, prew * mProgress);
	    		ViewCompat.animate(num).cancel();
	    		movelen = prew * progress;
	    		ViewCompat.animate(num).translationX(movelen).setDuration(0)
	    				.setListener(new ViewPropertyAnimatorListener() {

	    					@Override
	    					public void onAnimationStart(View arg0) {
	    						// TODO Auto-generated method stub

	    					}

	    					@Override
	    					public void onAnimationEnd(View arg0) {
	    						mProgress = progress;
	    					}

	    					@Override
	    					public void onAnimationCancel(View arg0) {

	    					}
	    				}).start();
	    	}
	 }

	
	/**
	 * Dialog监听器
	 */
	private DialogInterface.OnKeyListener mOnDialogKeyListener = new DialogInterface.OnKeyListener() {
		@Override
		public boolean onKey(DialogInterface dialogInterface, int keyCode,
				KeyEvent event) {
			// TODO Auto-generated method stub
			View focusView = ((Dialog) dialogInterface).getCurrentFocus();
			if (focusView == null) {
				return false;
			}

			if (GamepadTool.isButtonA(keyCode)) {
				KeyEvent keyevent = new KeyEvent(event.getAction(),
						KeyEvent.KEYCODE_ENTER);
				focusView.dispatchKeyEvent(keyevent);
				return true;
			} else if (GamepadTool.isButtonB(keyCode) || GamepadTool.isButtonBack(keyCode)) {
				if(isForce()){
					((Activity)context).finish();
					return true;
				}
				Dialog dialog = (Dialog) dialogInterface;
				if (event.getAction() == KeyEvent.ACTION_UP && dialog != null
						&& dialog.isShowing()) {
					dialog.dismiss();
				}
				return true;
			} else if (GamepadTool.isButtonXY(keyCode)) {
				return true;
			}
			return false;
		}
	};
	
	/**
	 * 
	 * @description 设置dialog全屏
	 * @param dialog
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28下午5:27:55
	 *
	 */
	public void setParams(VersionUpdateProgressDialog dialog) {
          LayoutParams lay = dialog.getWindow().getAttributes();
		  DisplayMetrics dm = new DisplayMetrics();

		  ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);

		  Rect rect = new Rect();

		  View view = getWindow().getDecorView();

		  view.getWindowVisibleDisplayFrame(rect);

		  lay.height = dm.heightPixels - rect.top;

		  lay.width = dm.widthPixels;

		 }

	public boolean isForce() {
		return isForce;
	}

	public void setForce(boolean isForce) {
		this.isForce = isForce;
	}
}
