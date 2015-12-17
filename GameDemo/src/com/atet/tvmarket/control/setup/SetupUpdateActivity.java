package com.atet.tvmarket.control.setup;

import java.io.File;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.app.Constant.DEVICE_TYPE;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.entity.FileDownResultInfo;
import com.atet.tvmarket.entity.NewVersionInfoResp;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.DataRequester;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.AppUtil;
import com.atet.tvmarket.utils.CommonDialogHelper.Callbacks;
import com.atet.tvmarket.utils.InstallExceptionUtils;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UmengUtils;
import com.atet.tvmarket.view.LoadingView;
import com.atet.tvmarket.view.NewToast;

public class SetupUpdateActivity extends BaseActivity {
	private ALog alog = ALog.getLogger(SetupUpdateActivity.class);
	private Toast toast = null;
	private Intent intent;
	private Button btnUpdate;
	private RelativeLayout content, progressLayout;
	private FrameLayout fLayout;

	private TextView versionName, remark, tip, num;
	private ProgressBar progressBar;
	private ImageView installProgress;
	private AnimationDrawable mAnimationDrawable;
	private LoadingView loadingView;
	private NewVersionInfoResp info;
	private Dialog mDialog;
	private int progress = 0;

	private String filePath = "";
	private int mProgress = 0;
	private int width = 0;
	private float prew = 0f;
	private float movelen = 0f;
	private TextView tvVersionName;// 用于显示当前版本号

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

		setContentView(R.layout.dialog_setup_update);
		ScaleViewUtils.scaleView(this);
		setBlackTitle(false);
		initView();
		getData();

	}

	@Override
	protected void onResume() {
		super.onResume();
		setTitleData();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 
	 * @Title: getData
	 * @Description: TODO(获取最新版本信息)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void getData() {
		loadingView.getmHandler().sendEmptyMessage(Constant.SHOWLOADING);
		ReqCallback<NewVersionInfoResp> reqCallback = new ReqCallback<NewVersionInfoResp>() {

			@Override
			public void onGetCacheData(String requestTag, boolean result) {
				if (!result) {
					// 不存在缓存数据，需要从网络获取，界面根据ui设计是否显示正在加载的进度条
					alog.info("no cache data");
					loadingView.getmHandler().sendEmptyMessage(
							Constant.SHOWLOADING);
				}
			}

			@Override
			public void onResult(TaskResult<NewVersionInfoResp> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					info = taskResult.getData();
					if (info.isNewVersionExist()) {
						// 存在新版本
						alog.info("有新版本可以升级");
						alog.info(info.toString());

						mHandler.sendEmptyMessage(0);
						loadingView.getmHandler().sendEmptyMessage(
								Constant.DISMISSLOADING);
						// 下载apk
						/*
						 * new Handler(Looper.getMainLooper()).post(new
						 * Runnable() {
						 * 
						 * @Override public void run() { // TODO Auto-generated
						 * method stub downloadNewVersionApk(info); } });
						 */
						return;
					} else {
						// 已经是最新版本
						alog.info("大厅已经是最新版本");
						mHandler.sendEmptyMessage(1);
						loadingView.getmHandler().sendEmptyMessage(
								Constant.DISMISSLOADING);
					}
				} else {
					// 获取失败原因
					String errMsg = taskResult.getMsg();

					if (NetUtil.isNetworkAvailable(SetupUpdateActivity.this,
							true)) {
						loadingView.getmHandler().sendEmptyMessage(
								Constant.NULLDATA);
					} else {
						loadingView.getmHandler().sendEmptyMessage(
								Constant.EXCEPTION);
					}

				}

			}
		};
		DataFetcher.getNewVersionInfo(this, reqCallback, false).request(this);
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
					mHandler.sendEmptyMessage(4);
				}
			}

			@Override
			public void onProgressChange(long fileSize, long downloadedSize) {
				// 下载进度更新，fileSize,原文件总大小；downloadedSize,已经下载的大小
				progress = (int) (downloadedSize * 100 / fileSize);
				mHandler.sendEmptyMessage(5);
				alog.debug("fileSize:" + fileSize + " downloadedSize:"
						+ downloadedSize + "progress:" + progress);

			}
		};
		DataRequester dataRequest = DataFetcher.downloadNewVersionApk(this,
				reqCallback, info, false);

		// 下载
		dataRequest.request(this);

		// 取消下载
		// dataRequest.cancel(getContext());
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:// 检测版本
				versionName.setText(SetupUpdateActivity.this
						.getString(R.string.setup_update_text1)
						+ info.getVersionName());
				remark.setText(info.getRemark());
				width = progressBar.getWidth();
				prew = (float) width / 100f;
				/*
				 * mDialog = CommonDialogHelper
				 * .openDialog(SetupUpdateActivity.this
				 * ,getResources().getString(R.string.down_btn_update),
				 * "发现新版本，是否更新?",caller);
				 * mDialog.setOnKeyListener(mOnDialogKeyListener);
				 */

				break;
			case 1:// 提示版本意识最新
				versionName.setText(SetupUpdateActivity.this
						.getString(R.string.setup_update_text1)
						+ "");
				// remark.setText(info.getRemark());
				remark.setText("该版本已经是最新版本");
				NewToast.makeToast(SetupUpdateActivity.this, "已经是最新版本",
						Toast.LENGTH_SHORT).show();
				btnUpdate.setOnClickListener(null);
				btnUpdate.setVisibility(View.INVISIBLE);
				break;
			case 2:// 开始下载
				content.setVisibility(View.INVISIBLE);
				progressLayout.setVisibility(View.VISIBLE);
				downloadNewVersionApk(info);
				break;
			case 3:// 下载成功
				progressBar.setProgress(100);
				num.setText(progress + "%");
				NewToast.makeToast(SetupUpdateActivity.this, "下载成功，准备安装",
						Toast.LENGTH_SHORT).show();
				if (filePath != null) {
					mHandler.sendEmptyMessage(8);
					// installApk(SetupUpdateActivity.this, filePath,
					// DEVICE_TYPE.TV_MARKET_PACKAGE_NAME);
				} else {
					NewToast.makeToast(SetupUpdateActivity.this, "没有找到安装路径",
							Toast.LENGTH_SHORT).show();
					progress = 0;
					content.setVisibility(View.VISIBLE);
					progressLayout.setVisibility(View.INVISIBLE);
				}
				break;
			case 4:// 下载失败
				NewToast.makeToast(SetupUpdateActivity.this, "下载失败",
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
				NewToast.makeToast(SetupUpdateActivity.this, "升级成功",
						Toast.LENGTH_SHORT).show();
				mAnimationDrawable.stop();
				installProgress.setVisibility(View.INVISIBLE);
				content.setVisibility(View.VISIBLE);
				progressLayout.setVisibility(View.INVISIBLE);
				// Umeng统计更新完成信息
				UmengUtils.setOnEvent(SetupUpdateActivity.this,
						UmengUtils.SETUP_UPDATE_COMPLETED);
				break;
			case 7:// 安装失败
				NewToast.makeToast(SetupUpdateActivity.this, "升级失败",
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
						installApk(SetupUpdateActivity.this, filePath,
								getPackageName());
					}
				}, 1000);
				break;
			default:
				break;
			}
		}

	};

	Callbacks caller = new Callbacks() {

		@Override
		public void clickOk() {
			mHandler.sendEmptyMessage(2);
		}

		@Override
		public void clickCancle() {
			mDialog.dismiss();
		}
	};

	/**
	 * 
	 * @Title: installApk
	 * @Description: TODO(安装apk)
	 * @param @param context
	 * @param @param filePath
	 * @param @param packageName 设定文件
	 * @return void 返回类型
	 * @throws
	 */
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

	private void initView() {

		content = (RelativeLayout) findViewById(R.id.rl_content);
		progressLayout = (RelativeLayout) findViewById(R.id.rl_progress);
		loadingView = (LoadingView) findViewById(R.id.contentLoading);
		loadingView.setDataView(content);
		versionName = (TextView) findViewById(R.id.setup_update_dialog_versionname_tv);
		remark = (TextView) findViewById(R.id.setup_update_dialog_content_tv);

		tip = (TextView) findViewById(R.id.tv_tip);
		num = (TextView) findViewById(R.id.tv_num);
		fLayout = (FrameLayout) findViewById(R.id.fl_progressbar);
		progressBar = (ProgressBar) findViewById(R.id.pb_progress);

		installProgress = (ImageView) findViewById(R.id.iv_install_laoding);
		mAnimationDrawable = (AnimationDrawable) installProgress.getDrawable();

		btnUpdate = (Button) findViewById(R.id.setup_update_dialog_btn);
		btnUpdate.requestFocus();
		btnUpdate.setOnClickListener(new CurrentClickListener());
		tvVersionName = (TextView) findViewById(R.id.versionname_tv);
		try {
			tvVersionName.setText("("
					+ this.getString(R.string.setup_update_currentVersion)
					+ AppUtil.getVersionName(this) + ")");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();
		if (action == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_BUTTON_X
					|| keyCode == KeyEvent.KEYCODE_X
					|| keyCode == KeyEvent.KEYCODE_MENU) {
				alog.info("KEYCODE_X");
				getData();
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	class CurrentClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.setup_update_dialog_btn) {
				mHandler.sendEmptyMessage(2);
				// Umeng统计点击更新信息
				UmengUtils.setOnEvent(SetupUpdateActivity.this,
						UmengUtils.SETUP_UPDATE_CLICK);
			} else {
			}
		}
	}
}
