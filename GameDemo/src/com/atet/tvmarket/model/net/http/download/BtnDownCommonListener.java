package com.atet.tvmarket.model.net.http.download;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.mygame.activity.MyGameActivity;
import com.atet.tvmarket.control.mygame.activity.RelativeGameActivity;
import com.atet.tvmarket.control.mygame.receiver.DownStateReceiver;
import com.atet.tvmarket.control.mygame.utils.DownloadingUtil;
import com.atet.tvmarket.control.promotion.activity.PromotionDetailsActivity;
import com.atet.tvmarket.entity.Group;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.database.PersistentSynUtils;
import com.atet.tvmarket.model.entity.MyGameInfo;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.AppUtil;
import com.atet.tvmarket.utils.StringTool;
import com.atet.tvmarket.utils.UmengUtils;
import com.atet.tvmarket.view.NewToast;

/**
 * 实现的主要功能：button按钮的下载实现
 * 
 * @author: LiuQin
 * @date: 2013-5-29 下午5:23:20 修改记录： 修改者: 修改时间： 修改内容：
 */
public class BtnDownCommonListener {
	private static final int PKG_INSTALLED_REMOVED = 5;
	private Context mContext;
	private DownloadTask mDownTask;
	private Button downTv; // 下载按钮
	private GameInfo mGameInfo;
	private MyGameInfo mMyGameInfo;
	private boolean mIsDownloading = false;
	public boolean sIsVisible = false;
	private DownloadingUtil downloadingUtil;
	private DownStateReceiver downStateReceiver;

	public BtnDownCommonListener(Context context) {
		super();
		this.mContext = context;
	}

	public BtnDownCommonListener() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @description 根据游戏信息初始化安装状态以及点击事件
	 * @param downTextView
	 * @param gameInfo
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:21:12
	 * 
	 */
	public void listen(Button downTextView, GameInfo gameInfo) {
		this.mGameInfo = gameInfo;
		downloadingUtil = new DownloadingUtil(mContext, mHandler);
		downTv = downTextView;
		initDownButton(downTextView);
		downTv.setOnClickListener(downListener);
		sIsVisible = true;
	}

	/**
	 * 
	 * @description 执行点击
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:22:00
	 * 
	 */
	public void performClick() {
		downTv.performClick();
	}

	/**
	 * 
	 * @description 回收资源
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:22:16
	 * 
	 */
	public void recycle() {
		sIsVisible = false;
		try {
			if (downStateReceiver != null) {
				mDownTask.unregBrocastReceiver(downStateReceiver);
				downStateReceiver = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @description 初始化下载按钮以及注册下载广播
	 * @param downTextView
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:22:38
	 * 
	 */
	private void initDownButton(TextView downTextView) {
		mDownTask = DownloadTask.getInstance(mContext);
		setupDownButton(downTextView);
		if (downStateReceiver == null) {
			downStateReceiver = new DownStateReceiver(mHandler);
			mDownTask.regBrocastReceiver(downStateReceiver);
		}
	}

	/**
	 * 
	 * @description 设置下载按钮
	 * @param downTextView
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:22:50
	 * 
	 */
	private void setupDownButton(TextView downTextView) {
		String id = mGameInfo.getGameId() + "";
		Group<MyGameInfo> infos = PersistentSynUtils.getModelList(
				MyGameInfo.class, " gameId='" + id + "'");
		boolean packageExist = false;
		if (infos != null && infos.size() > 0) {
			packageExist = true;
			mMyGameInfo = infos.get(0);
		}
		boolean isSet = false;
		downTextView.setText(R.string.down_btn_not_download);
		if (mMyGameInfo != null) {
			int state = mMyGameInfo.getState() & 0xff;
			if (state == Constant.GAME_STATE_NOT_INSTALLED) {
				// 已下载
				if (DownloadingUtil.getmInstallingPackageName() != null
						&& DownloadingUtil.getmInstallingPackageName().equals(
								mMyGameInfo.getPackageName())) {
					downTextView.setText(R.string.installing);
				} else {
					downTextView.setText(R.string.down_btn_not_install);
				}
				isSet = true;
			} else if (state == Constant.GAME_STATE_INSTALLED
					|| state == Constant.GAME_STATE_NEED_UPDATE) {
				// 安装
				downTextView.setText(R.string.down_btn_installed);
				isSet = true;
			} else if (state == Constant.GAME_STATE_NOT_DOWNLOAD) {

				if (packageExist && !mDownTask.isDownloading(id)) {
					// 没有在下载中
					downTextView.setText(R.string.down_btn_pause);
					isSet = true;
				}
			}
		}

		if (!isSet) {
			if (mDownTask.isDownloading(id)) {
				// 正在下载中
				int percent = mDownTask.getDownloadingPercentage(mGameInfo
						.getGameId() + "");
				if (percent >= 0) {
					downTextView.setText(R.string.down_btn_ing);
				} else {
					downTextView.setText(R.string.down_btn_wait_to_start);
				}
				mIsDownloading = true;
			}
		}

		if (mMyGameInfo == null
				&& !StringTool.isEmpty(mGameInfo.getPackageName())) {
			mMyGameInfo = MyGameActivity.getMyGameInfoFromGameInfo(mContext,
					mGameInfo);
			if (AppUtil
					.isAlreadyInstall(mContext, mMyGameInfo.getPackageName())) { // 若存在有游戏，已经安装，但未存入数据库中
				mMyGameInfo.setState(Constant.GAME_STATE_INSTALLED);
				downTextView.setText(R.string.down_btn_installed);
				PersistentSynUtils.addModel(mMyGameInfo);
			}
		}
	}

	private OnClickListener downListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			onDownButtonClick(downTv);
			if (mContext instanceof RelativeGameActivity) {
				// Umeng统计"更多精彩"的下载
				UmengUtils.setOnEvent(mContext,
						UmengUtils.GAMEDETAIL_MORE_DOWNLOAD);
			}
		}
	};

	/**
	 * 
	 * @description 处理下载按钮点击事件
	 * @param downloadBtn
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:23:32
	 * 
	 */
	public void onDownButtonClick(TextView downloadBtn) {
		if (mMyGameInfo == null
				|| StringTool.isEmpty(mGameInfo.getPackageName())) {
			getGameInfo(mGameInfo.getGameId());
			return;
		}
		int state = mMyGameInfo.getState() & 0xff;
		if (state == Constant.GAME_STATE_NOT_DOWNLOAD
				|| state == Constant.GAME_STATE_DOWNLOAD_ERROR) {
			// 点击下载
			notDownloadHanlde(downloadBtn);

		} else if (state == Constant.GAME_STATE_NOT_INSTALLED) {
			// 点击安装
			File apkFile = new File(mMyGameInfo.getLocalDir(),
					mMyGameInfo.getLocalFilename());
			if (apkFile.exists()) {
				unZipToInstall(apkFile);
			} else {
				// 文件已丢失
				downloadingUtil.showReDownloadDialog(mMyGameInfo);
			}
		} else if (state == Constant.GAME_STATE_INSTALLED
				|| state == Constant.GAME_STATE_NEED_UPDATE) {
			// 点击运行
			if (!AppUtil.startAppByPkgName(mContext,
					mMyGameInfo.getPackageName())) {
				String downUrl = mMyGameInfo.getDownUrl();
				if (downUrl == null || downUrl.equals("")) {
					// 如果不是从本市场下载的应用
					NewToast.makeToast(mContext, R.string.manage_warn_delete_only,
							Toast.LENGTH_SHORT).show();
				} else {
					// 游戏已卸载
					File zipFile = new File(mMyGameInfo.getLocalDir(),
							mMyGameInfo.getLocalFilename());
					if (zipFile != null && zipFile.exists()) {
						// 提示重新安装
						downloadingUtil.showReInstallDialog(mMyGameInfo, null);
					} else {
						// 提示重新下载
						downloadingUtil.showReDownloadDialog(mMyGameInfo);
					}
				}
			} else {
				// add by zml 2015-01-21 17:38 运行游戏后，关闭游戏详情，游戏退出后回到首页的我的游戏选项
				// Intent intent = new Intent();
				// intent.setAction(CloseGameInfosReceiver.CLOSE_GAMEINFOS_ACTION);
				// downloadBtn.getContext().sendBroadcast(intent);
			}
		}
	}

	/**
	 * 
	 * @description 处理下载或者暂时操作
	 * @param downloadBtn
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:23:49
	 * 
	 */
	private void notDownloadHanlde(TextView downloadBtn) {
		if (!mDownTask.isDownloading(mMyGameInfo.getGameId())) { // 判断是否处于下载队列中

			MyGameActivity.chekAgeDownloadGame(mContext, mGameInfo,
					new MyGameActivity.ChekDownloadGameCallback() {
				@Override
				public void onDownloadGame(Context context, GameInfo gameInfo) {
					// 下载游戏
					startDownloadGame();
				}
			});
		} else {
			// 下载中，暂停
			mDownTask.setDownloadStop(mMyGameInfo.getGameId());
		}
	}

	/**
	 * 
	 * @description 开始下载游戏
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:24:05
	 * 
	 */
	public void startDownloadGame() {
		if (mMyGameInfo.getState() == Constant.GAME_STATE_DOWNLOAD_ERROR) {
			mMyGameInfo.setState(Constant.GAME_STATE_NOT_DOWNLOAD);
			PersistentSynUtils.update(mMyGameInfo); // 游戏下载错误或游戏尚未下载，则更新数据库中的游戏信息
		}

		if (MyGameActivity.addToMyGameList(mContext, mGameInfo)) {
			downTv.setText(R.string.down_btn_wait_to_start);// 等待下载
		}
	}

	/**
	 * 异步传递下载状态信息
	 */
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			FileDownInfo fileDownInfo;
			String gameId = "";

			if (msg.obj == null) return ;

			// 根据不同状态，获取gameId
			if (msg.what == DownloadTask.STATE_ON_DOWNLOAD_FINISH) {
				gameId = ((FileDownInfo) msg.obj).getFileId();
			} else if (msg.what == Constant.HANDLER_PKG_INSTALLED_REMOVED) { // 游戏安装或者卸载成功
				gameId = ((MyGameInfo) msg.obj).getGameId();
			} else {
				gameId = (String) msg.obj;
			}

			if (TextUtils.isEmpty(gameId) || mGameInfo == null) return ;

			if (!gameId.equals(mGameInfo.getGameId())) { // 如果不是当前gameId,则不做处理
				if (msg.what != Constant.GAME_STATE_INSTALLED_FAIL)
					return;
			}

			switch (msg.what) {
			case DownloadTask.STATE_ON_DOWNLOAD_WAIT: // 下载等待
				mIsDownloading = false;
				downTv.setText(R.string.down_btn_wait_to_start);
				break;

			case DownloadTask.STATE_ON_DOWNLOAD_START: // 下载开始
				mIsDownloading = true;
				downTv.setText(R.string.down_btn_ing);
				break;

			case DownloadTask.STATE_ON_DOWNLOAD_STOP: // 下载暂停
				mIsDownloading = false;
				downTv.setText(R.string.down_btn_pause);
				break;

			case Constant.HANDLER_PKG_INSTALLED_REMOVED: // 安装或者卸载成功
				final MyGameInfo myGameInfoOp = (MyGameInfo) msg.obj;
				String pkgName = myGameInfoOp.getPackageName();
				// String pkgName = (String) msg.obj;
				if (mMyGameInfo.getPackageName().equals(pkgName)) {
					if (msg.arg1 == DownloadTask.OP_INSTALL) {
						mMyGameInfo.setState(Constant.GAME_STATE_INSTALLED);
						mMyGameInfo.setLaunchAct(myGameInfoOp.getLaunchAct());
						downTv.setText(R.string.down_btn_installed);
					} else if (msg.arg1 == DownloadTask.OP_UNINSTALL) {
						mMyGameInfo.setState(Constant.GAME_STATE_NOT_DOWNLOAD);
						downTv.setText(R.string.down_btn_not_download);
					}
				}
				break;
			case DownloadTask.STATE_ON_DOWNLOAD_PROGRESS:
				// 更新进度

				if (!mIsDownloading) {
					return;
				}
				break;

			case DownloadTask.STATE_ON_DOWNLOAD_FINISH: // 下载完成
				mIsDownloading = false;
				fileDownInfo = (FileDownInfo) msg.obj;
				MyGameInfo info = (MyGameInfo) fileDownInfo.getObject();
				if (info.getState() == Constant.GAME_STATE_NOT_INSTALLED) {
					downTv.setText(R.string.down_btn_not_install);
					mMyGameInfo.setState(Constant.GAME_STATE_NOT_INSTALLED);
				} else {
					downTv.setText(R.string.down_btn_download_fail);
					mMyGameInfo.setState(Constant.GAME_STATE_DOWNLOAD_ERROR);
					PersistentSynUtils.update(mMyGameInfo);
				}
				break;

			case DownloadTask.STATE_ON_DOWNLOAD_ERROR: // 下载错误
				mIsDownloading = false;
				downTv.setText(R.string.down_btn_download_fail);
				mMyGameInfo.setState(Constant.GAME_STATE_DOWNLOAD_ERROR);
				break;
			case Constant.GAME_STATE_INSTALLED_FAIL: // 安装失败
				String packageName = (String) msg.obj;
				if (!StringTool.isEmpty(packageName)) {
					if (packageName.equals(mGameInfo.getPackageName())) {
						downTv.setText(R.string.down_btn_not_install);
					}
				}
				break;

			default:
				break;
			}
		}
	};

	/**
	 * 
	 * @description 解压安装游戏
	 * @param apkFile
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:24:23
	 * 
	 */
	private void unZipToInstall(final File apkFile) {
		if (apkFile.getName().toLowerCase().endsWith(MyGameActivity.ZIP_EXT)) {
			if (!StringTool
					.isEmpty(DownloadingUtil.getmInstallingPackageName())) {
				NewToast.makeToast(mContext, R.string.exist_game_installing,
						Toast.LENGTH_LONG).show();
				return;
			}
			downTv.setText(R.string.installing);
			DownloadingUtil.setmInstallingPackageName(mGameInfo
					.getPackageName());
			final String unApkPath = MyGameActivity
					.getLocalUnApkPath(mMyGameInfo);
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					downloadingUtil.unZipApk(apkFile.getAbsolutePath(),
							Constant.GAME_ZIP_DATA_LOCAL_DIR, unApkPath,
							mMyGameInfo.getPackageName(),
							mMyGameInfo.getName(), null);
				}
			}).start();
		} else {
			NewToast.makeToast(mContext, R.string.install_not_support_apk_file,
					Toast.LENGTH_SHORT).show();
		}
	}

	private void getGameInfo(String gameId) {

		ReqCallback<List<GameInfo>> reqCallback = new ReqCallback<List<GameInfo>>() {
			@Override
			public void onResult(TaskResult<List<GameInfo>> taskResult) {
				int code = taskResult.getCode();
				List<GameInfo> infos;
				if (code == TaskResult.OK) {
					List<GameInfo> gameInfos = taskResult.getData();
					if (gameInfos != null && gameInfos.size() > 0) {
						mGameInfo = gameInfos.get(0);
						// 被迫这样写的，不然处理不了
						PromotionDetailsActivity.gameInfo = mGameInfo;
						Group<MyGameInfo> myGameinfos = PersistentSynUtils
								.getModelList(MyGameInfo.class, " gameId='"
										+ mGameInfo.getGameId() + "'");
						if (myGameinfos != null && myGameinfos.size() > 0) { // 从数据库中查询是否有对应的游戏信息
							mMyGameInfo = myGameinfos.get(0);
						} else { // 没游戏信息，则由gameInfo转化
							mMyGameInfo = MyGameActivity
									.getMyGameInfoFromGameInfo(mContext,
											mGameInfo);
						}
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								performClick();
							}
						});
					} else {
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								NewToast.makeToast(mContext,
										R.string.manage_down_data_format_error,
										Toast.LENGTH_SHORT).show();
							}
						});
					}
				} else {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							NewToast.makeToast(mContext,
									R.string.manage_down_data_format_error,
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			}

			@Override
			public void onUpdate(TaskResult<List<GameInfo>> taskResult) {

			}
		};
		DataFetcher.getGameInfoFromGameId(mContext, gameId, reqCallback, false)
				.registerUpdateListener(reqCallback).request(mContext);

	}
}
