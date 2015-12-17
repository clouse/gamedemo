package com.atet.tvmarket.control.mygame.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atet.tvmarket.R;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.control.mygame.adapter.MyGameAdapter;
import com.atet.tvmarket.control.mygame.receiver.BReceiver;
import com.atet.tvmarket.control.mygame.receiver.DownStateReceiver;
import com.atet.tvmarket.control.mygame.view.MyGameItemView;
import com.atet.tvmarket.control.mygame.view.MyGameRecyclerView;
import com.atet.tvmarket.control.setup.SetupChildlockSetPasswordActivity;
import com.atet.tvmarket.entity.Group;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.DataHelper;
import com.atet.tvmarket.model.SpHelper;
import com.atet.tvmarket.model.database.PersistentSynUtils;
import com.atet.tvmarket.model.entity.MyGameInfo;
import com.atet.tvmarket.model.net.http.download.DownloadTask;
import com.atet.tvmarket.model.net.http.download.FileDownInfo;
import com.atet.tvmarket.utils.ChildlockUtils;
import com.atet.tvmarket.utils.CollectDownCountUtil;
import com.atet.tvmarket.utils.DeviceTool;
import com.atet.tvmarket.utils.GamepadTool;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.StringTool;
import com.atet.tvmarket.view.LoadingView;
import com.atet.tvmarket.view.NewToast;

/**
 * 我的游戏
 * 
 * @author chenqingwen
 * 
 */
public class MyGameActivity extends BaseActivity {
	private boolean isDelState = false; // 判断是否处于删除状态
	private ArrayList<MyGameInfo> myGameInfos = new ArrayList<MyGameInfo>();// 游戏信息
	private ArrayList<MyGameInfo> pageGames = new ArrayList<MyGameInfo>(); // 当前页面游戏信息
	private MyGameRecyclerView recyclerView;
	private TextView mNoDataPro; // 没有游戏的提示
	private LoadingView mLoadingPro;
	private MyGameAdapter gameAdapter; // 我的游戏适配器
	private GridLayoutManager linearLayoutManager; // recyclerView 网格管理器
	private LinearLayout potLayout;
	private ImageFetcher mImageFetcher;
	private int pageSize = 0; // 总页面数
	private int currentPage = 0; // 当前所在页面
	private DownloadTask mDownTask; // 下载任务对象
	private DownStateReceiver downStateReceiver; // 下载广播接收者
	// 系统安装或者卸载了应用
	private static final int HANDLER_PKG_INSTALLED_REMOVED = 5;
	// 游戏开始更新
	private static final int HANDLER_GAME_START_UPDATE = 151;
	// 游戏更新失败
	private static final int HANDLER_GAME_UPDATED_FAILED = 152;
	// 游戏更新成功
	private static final int HANDLER_GAME_UPDATED_SUCCESS = 153;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		setContentView(R.layout.mygame_layout);
		ScaleViewUtils.init(this);
		ScaleViewUtils.scaleView(getWindow().getDecorView());
		init();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (!StringTool.isEmpty(BReceiver.needInstalledGameInfo)) { // 当前有游戏正在安装，返回到该界面时，同步数据
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					myGameInfos.clear();
					myGameInfos = PersistentSynUtils.getModelList(
							MyGameInfo.class, " autoIncrementId>0");
					Collections.sort(myGameInfos, comparator);
					setAdapters();
					BReceiver.needInstalledGameInfo = null;
				}
			});
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		setTitleData();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		BReceiver.needInstalledGameInfo = null;
		recycle();
	}

	/**
	 * 
	 * @description 回收广播接收者
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:33:34
	 * 
	 */
	public void recycle() {
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
	 * @author chenqingwen List的一个比较器，安照游戏的运行次数排序
	 */
	private Comparator<MyGameInfo> comparator = new Comparator<MyGameInfo>() {
		@Override
		public int compare(MyGameInfo lhs, MyGameInfo rhs) {
			// TODO Auto-generated method stub

			return (int) (rhs.getRunTime() - lhs.getRunTime());
		}
	};

	/**
	 * 
	 * @description 初始化界面
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:34:27
	 * 
	 */
	private void init() {
		mImageFetcher = getmImageFetcher();
		// 隐藏加载中提示
		mLoadingPro = (LoadingView) findViewById(R.id.mygame_loading_pro);
		mNoDataPro = (TextView) findViewById(R.id.mygame_nodata_pro);
		recyclerView = (MyGameRecyclerView) findViewById(R.id.mygame_recycler_view);
		potLayout = (LinearLayout) findViewById(R.id.ll_pot);
		mDownTask = DownloadTask.getInstance(this);
		downStateReceiver = new DownStateReceiver(mHandler);
		mDownTask.regBrocastReceiver(downStateReceiver);
		// 数组设置
		gameAdapter = new MyGameAdapter(recyclerView, this, mImageFetcher,
				mHandler, mDownTask);
		linearLayoutManager = new GridLayoutManager(this, 3,
				RecyclerView.HORIZONTAL, false);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setAdapter(gameAdapter);
		setBlackTitle(true);
		new Thread(initDataRunnable).start();
	}

	/**
	 * 下载状态处理
	 */
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			FileDownInfo fileDownInfo;
			MyGameInfo myGameInfo = null;
			try {
				switch (msg.what) {
				// 下载由等待状态转为开始状态
				case DownloadTask.STATE_ON_DOWNLOAD_START:
					tipBarHandle((String) msg.obj,
							DownloadTask.STATE_ON_DOWNLOAD_START, 0);
					break;

				// 下载停止状态
				case DownloadTask.STATE_ON_DOWNLOAD_STOP:
					tipBarHandle((String) msg.obj,
							DownloadTask.STATE_ON_DOWNLOAD_STOP, 0);
					break;

				// 下载进度更新
				case DownloadTask.STATE_ON_DOWNLOAD_PROGRESS:
					tipBarHandle((String) msg.obj,
							DownloadTask.STATE_ON_DOWNLOAD_PROGRESS, msg.arg1);
					break;

				case HANDLER_GAME_START_UPDATE:
					tipBarHandle((String) msg.obj, HANDLER_GAME_START_UPDATE, 0);
					break;

				case HANDLER_GAME_UPDATED_FAILED:
					tipBarHandle((String) msg.obj, HANDLER_GAME_UPDATED_FAILED,
							0);
					break;

				case HANDLER_GAME_UPDATED_SUCCESS:
					myGameInfo = (MyGameInfo) msg.obj;
					// 修改数据
					int updatePos = gameAdapter.getPositionForGameId(myGameInfo
							.getGameId());
					if (updatePos == -1) { // 这个地方好像不对
						myGameInfos.set(updatePos + 12 * pageSize, myGameInfo);
					} else {
						gameAdapter.notifySingleDataChanged(myGameInfo,
								updatePos);
					}
					break;

				// 下载完成
				case DownloadTask.STATE_ON_DOWNLOAD_FINISH:
					fileDownInfo = (FileDownInfo) msg.obj;
					String fileId = fileDownInfo.getFileId();
					if (fileId != null) {
						myGameInfo = (MyGameInfo) fileDownInfo.getObject();
						// 修改数据
						int downPos = gameAdapter
								.getPositionForGameId(myGameInfo.getGameId());
						if (downPos != -1) {
							gameAdapter.notifySingleDataChanged(myGameInfo,
									downPos);
							myGameInfos.set(downPos + 12 * currentPage,
									myGameInfo);
						} else {
							int size = myGameInfos.size();
							for (int i = 0; i < size; i++) {
								if (myGameInfos.get(i).getGameId()
										.equals(myGameInfo.getGameId())) {
									myGameInfos.set(i, myGameInfo);
								}
							}
						}
						if (myGameInfo.getState() == Constant.GAME_STATE_NOT_INSTALLED) {
							tipBarHandle(fileId,
									DownloadTask.STATE_ON_DOWNLOAD_FINISH, 0);
						} else {
							tipBarHandle(fileId,
									DownloadTask.STATE_ON_DOWNLOAD_ERROR, 0);
						}
					}
					break;

				// 下载发生错误
				case DownloadTask.STATE_ON_DOWNLOAD_ERROR:
					// 修改数据
					int pos = gameAdapter
							.getPositionForGameId((String) msg.obj);
					myGameInfo = myGameInfos.get(pos + 12 * currentPage);
					myGameInfo.setState(Constant.GAME_STATE_DOWNLOAD_ERROR);
					if (pos == -1) {
						myGameInfos.set(pos + 12 * currentPage, myGameInfo);
					} else {
						gameAdapter.notifySingleDataChanged(myGameInfo, pos);
					}
					tipBarHandle((String) msg.obj,
							DownloadTask.STATE_ON_DOWNLOAD_ERROR, 0);
					break;

				// 下载等待状态
				case DownloadTask.STATE_ON_DOWNLOAD_WAIT:
					// 修改数据
					tipBarHandle((String) msg.obj,
							DownloadTask.STATE_ON_DOWNLOAD_WAIT, 0);
					break;

				// 系统更新了应用状态
				case HANDLER_PKG_INSTALLED_REMOVED:
					final MyGameInfo myGameInfoOp = (MyGameInfo) msg.obj;
					final String pkgName = myGameInfoOp.getPackageName();
					for (int i = 0; i < myGameInfos.size(); i++) {
						myGameInfo = myGameInfos.get(i);
						if (myGameInfo.getPackageName().equals(pkgName)) {
							// if(myGameInfo.getGameId().equals(gameId)){
							if (msg.arg1 == DownloadTask.OP_INSTALL) { // 安装游戏后
								myGameInfo
										.setState(Constant.GAME_STATE_INSTALLED);
								myGameInfo.setLaunchAct(myGameInfoOp
										.getLaunchAct());
								if (i >= currentPage * 12
										&& i <= (currentPage + 1) * 12) {
									gameAdapter.notifySingleDataChanged(
											myGameInfo, i % 12);
									MyGameItemView item = (MyGameItemView) (recyclerView
											.getChildAt(i % 12));
									item.getTvProgress().setText("");
									item.getIvState()
											.setBackgroundResource(
													R.drawable.game_detail_run_btn_selector);
								}
							} else if (msg.arg1 == DownloadTask.OP_UNINSTALL) { // 卸载游戏后
								if (gameAdapter.getItemCount() == 1) { // 当前页面仅剩一个游戏
									if (currentPage > 0) {
										// currentPage--;
									} else { // 游戏被清空
										isDelState = false;
										gameAdapter.setDeleteState(false);
										mNoDataPro.setVisibility(View.VISIBLE);
									}
								}
								// 删除对应缓存的bitmap
								// CustomClipLoading.removeSingleBitmap(myGameInfos.get(i).getGameId());
								myGameInfos.remove(i);
								int total = myGameInfos.size();
								if (total > 0) {
									if (total % 12 == 0) {
										pageSize = total / 12;
									} else {
										pageSize = total / 12 + 1;
									}
								} else {
									pageSize = 0;
								}
								setPagePotLayout();
								setAdapters();

							}
							break;
						}
						BReceiver.needInstalledGameInfo = null;
					}
					break;
				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * @author chenqingwen 初始化数据的线程
	 */
	private Runnable initDataRunnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 从我的游戏列表读取数据
			myGameInfos = PersistentSynUtils.getModelList(MyGameInfo.class,
					" autoIncrementId>0");
			// 游戏信息排序 以时间排序
			Collections.sort(myGameInfos, comparator);
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					// 更新界面
					initViews();
				}
			});
		}
	};

	/**
	 * 
	 * @description 初始化界面
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:35:25
	 * 
	 */
	private void initViews() {
		int total = myGameInfos.size();
		if (total > 0) {
			Log.e("database", myGameInfos.toString());
			if (total % 12 == 0) {
				pageSize = total / 12;
			} else {
				pageSize = total / 12 + 1;
			}
		}

		// 设置分页控件
		setPagePotLayout();

		if (pageSize > 0) {
			setAdapters();
			if (mLoadingPro != null) {
				mLoadingPro.setVisibility(View.GONE);
			}
		} else {
			mNoDataPro.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 设置分页的控件
	 */
	private void setPagePotLayout() {

		// 移除所有的控件
		potLayout.removeAllViews();

		if (pageSize <= 1)
			return;

		for (int i = 0; i < pageSize; i++) {

			ImageView pot = new ImageView(this);

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = 5;
			layoutParams.rightMargin = 5;
			pot.setLayoutParams(layoutParams);

			potLayout.addView(pot);
		}
	}

	private void setPot() {
		if (potLayout != null) {
			for (int i = 0; i < potLayout.getChildCount(); i++) {
				ImageView imageView = (ImageView) potLayout.getChildAt(i);
				if (i == currentPage) {
					imageView.setImageResource(R.drawable.red_num_bg);
				} else {
					imageView.setImageResource(R.drawable.point_gray);
				}
			}
		}
	}

	/**
	 * 
	 * @description 重新设置界面
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:35:39
	 * 
	 */
	private void setAdapters() { // 重新设置当前页面数据
		pageGames.clear();
		if ((currentPage + 1) < pageSize) { // 当前页面小于总页面数
			for (int i = currentPage * 12; i < (currentPage + 1) * 12; i++) {
				pageGames.add(myGameInfos.get(i));
			}
		} else { // 当前页面大于总页面数，则设置翻页至最后一页
			if (pageSize > 0) {
				currentPage = pageSize - 1;
			}
			for (int i = currentPage * 12; i < myGameInfos.size(); i++) {
				pageGames.add(myGameInfos.get(i));
			}
		}
		gameAdapter.setItem(pageGames);
		recyclerView.setAdapter(gameAdapter);
		setPot();
	}

	/**
	 * 
	 * @description 下一页
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:35:54
	 * 
	 */
	public void nextPage() { // 下一页
		if (currentPage < pageSize - 1) {
			currentPage++;
			setAdapters();
			rightAnimator();
		}
	}

	/**
	 * 
	 * @description 上一页
	 * @param isDelete
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:36:03
	 * 
	 */
	public void previousPage(boolean isDelete) { // 前一页
		if (currentPage > 0) {
			currentPage--;
			setAdapters();
			leftAnimator();
		}
	}

	/**
	 * 
	 * @description 获取当前页面
	 * @return
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:36:19
	 * 
	 */
	public int getCurrentPage() { // 获取当前页面
		return currentPage;
	}

	/**
	 * 
	 * @description 右滑动画
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:36:36
	 * 
	 */
	public void rightAnimator() { // 右滑动画
		ViewCompat.setTranslationX(recyclerView, recyclerView.getWidth());
		ViewCompat.animate(recyclerView).cancel();
		ViewCompat.animate(recyclerView).translationX(0).translationY(0)
				.setDuration(1000);
	}

	/**
	 * 
	 * @description 左滑动画
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:37:12
	 * 
	 */
	public void leftAnimator() { // 左滑动画
		ViewCompat.setTranslationX(recyclerView, -recyclerView.getWidth());
		ViewCompat.animate(recyclerView).cancel();
		ViewCompat.animate(recyclerView).translationX(0).translationY(0)
				.setDuration(1000);
	}

	/**
	 * 
	 * @description 点击游戏运行 数据交换
	 * @param index
	 *            点击的位置
	 * @return
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:37:27
	 * 
	 */
	public boolean swapData(int index) {
		MyGameInfo info = myGameInfos.get(index + currentPage * 12);
		info.setRunTime(System.currentTimeMillis());
		myGameInfos.remove(index + currentPage * 12);
		myGameInfos.add(0, info);
		currentPage = 1;
		previousPage(false);
		updateRunTime(info); // 更新运行时间
		return true;
	}

	/**
	 * 
	 * @description 更新游戏state,
	 * @param gameInfo
	 *            游戏信息
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:38:05
	 * 
	 */
	private void updateRunTime(MyGameInfo gameInfo) {
		String sql = "update myGameInfo set runTime=" + gameInfo.getRunTime()
				+ " where gameId=" + "'" + gameInfo.getGameId() + "';";
		PersistentSynUtils.execSQL(sql);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		int keyCode = event.getKeyCode();
		int action = event.getAction();
		if (GamepadTool.isButtonY(keyCode) || keyCode == KeyEvent.KEYCODE_MENU) {
			if (action == KeyEvent.ACTION_UP) {
				if (!isDelState && gameAdapter.getItemCount() > 0) {
					// 切换到删除状态
					isDelState = !isDelState;
					gameAdapter.setDeleteState(isDelState);
					int chlidCount = recyclerView.getChildCount();
					for (int i = 0; i < chlidCount; i++) { // 设置删除图标可见
						MyGameItemView view = (MyGameItemView) recyclerView
								.getChildAt(i);
						view.getIvDeleteState().setVisibility(View.VISIBLE);
					}
				}
			}
			return true;
		} else if (isDelState
				&& (GamepadTool.isButtonB(keyCode) || GamepadTool
						.isButtonBack(keyCode))) {
			if (action == KeyEvent.ACTION_UP) {
				if (isDelState) {
					// 关闭到删除状态
					isDelState = !isDelState;
					gameAdapter.setDeleteState(isDelState);
					int chlidCount = recyclerView.getChildCount();
					for (int i = 0; i < chlidCount; i++) { // 设置删除图标不可见
						MyGameItemView view = (MyGameItemView) recyclerView
								.getChildAt(i);
						view.getIvDeleteState().setVisibility(View.INVISIBLE);
					}
				}
			}
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	/**
	 * 
	 * @description 更新广播状态，作出相应操作
	 * @param gameId
	 *            游戏Id
	 * @param type
	 *            类型
	 * @param msgArg1
	 *            下载进度
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:38:23
	 * 
	 */
	private void tipBarHandle(String gameId, int type, int msgArg1) {
		MyGameItemView gameItemView = (MyGameItemView) recyclerView
				.getChildAt(gameAdapter.getPositionForGameId(gameId));
		if (gameItemView == null) {
			return;
		}
		switch (type) {
		case DownloadTask.STATE_ON_DOWNLOAD_PROGRESS: // 更新下载进度
			gameItemView.getTvProgress().setText(msgArg1 + "%");
			gameItemView.getGameView().updateMyGameProgress(msgArg1, false);
			break;

		case DownloadTask.STATE_ON_DOWNLOAD_START: // 下载开始
			gameItemView.getTvProgress().setText(msgArg1 + "%");
			gameItemView.getGameView().setGameId(gameId);
			// 更新下载进度
			gameItemView.getGameView().updateMyGameProgress(msgArg1, false);
			gameItemView.getIvState().setBackgroundResource(
					R.drawable.game_detail_downing_btn_selector);
			break;

		case DownloadTask.STATE_ON_DOWNLOAD_STOP: // 下载停止
			gameItemView.getIvState().setBackgroundResource(
					R.drawable.game_detail__downpause_btn_selector);
			// 若显示等待下载，则移除等待下载
			if (!gameItemView.getTvProgress().getText().toString()
					.endsWith("%")) {
				gameItemView.getTvProgress().setText("");
			}
			break;

		case DownloadTask.STATE_ON_DOWNLOAD_FINISH: // 下载完成
			gameItemView.getGameView().removeDrawableOnMyGame();
			gameItemView.getIvState().setBackgroundResource(
					R.drawable.game_detail_install_btn_selector);
			gameItemView.getTvProgress().setText("");
			gameItemView.getIvUpdateState().setVisibility(View.INVISIBLE);
			break;

		case DownloadTask.STATE_ON_DOWNLOAD_ERROR: // 下载错误
			// gameItemView.getGameView().removeDrawableOnMyGame();
			gameItemView.getIvState().setVisibility(View.VISIBLE);
			gameItemView.getIvState().setBackgroundResource(
					R.drawable.mygame_download_error);
			gameItemView.getTvProgress().setText("");
			break;

		case DownloadTask.STATE_ON_DOWNLOAD_WAIT: // 下载等待
			// 下载等待
			gameItemView.getIvState().setVisibility(View.VISIBLE);
			gameItemView.getIvState().setBackgroundResource(
					R.drawable.game_detail_downing_btn_selector);
			gameItemView.getTvProgress().setText(
					R.string.down_btn_wait_to_start);
			break;

		case HANDLER_GAME_START_UPDATE: // 更新开始
			break;

		case HANDLER_GAME_UPDATED_FAILED: // 更新失败
			break;
		}
	}

	private static final boolean IS_ONLY_ZIP = true;
	public static final String ZIP_EXT = ".zip";

	/**
	 * 
	 * @description gameinfo 转MygameInfo
	 * @param context
	 * @param gameInfo
	 *            游戏信息
	 * @return
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:39:36
	 * 
	 */
	public static MyGameInfo getMyGameInfoFromGameInfo(Context context,
			GameInfo gameInfo) {
		MyGameInfo myGameInfo = null;
		String gameId = gameInfo.getGameId() + "";
		String packageName = gameInfo.getPackageName();
		String name = gameInfo.getGameName();
		String iconUrl = gameInfo.getMinPhoto();
		String downUrl = gameInfo.getFile();
		String localDir = getLocalSaveDir(context);
		String localFileName;
		if (MyGameActivity.IS_ONLY_ZIP
				|| downUrl.toLowerCase().endsWith(ZIP_EXT)) {
			localFileName = getLocalZipName(gameInfo);
		} else {
			localFileName = getLocalApkName(gameInfo);
		}
		int versionCode = 0;
		if (gameInfo.getVersionCode() != null) {
			versionCode = gameInfo.getVersionCode().intValue();
		}
		if (!gameId.equals("")) {
			myGameInfo = new MyGameInfo(gameId, packageName, name, iconUrl,
					downUrl, localDir, localFileName,
					Constant.GAME_STATE_NOT_DOWNLOAD, versionCode, "",
					System.currentTimeMillis());
			myGameInfo.setDownToken(Constant.DOWN_FROM_LOCAL);
		}
		myGameInfo.setMinPhoto(gameInfo.getMinPhoto());
		myGameInfo.setMiddlePhoto(gameInfo.getMiddlePhoto());
		return myGameInfo;
	}

	/**
	 * 
	 * @description 获取解压目的路径
	 * @param info
	 * @return
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:40:11
	 * 
	 */
	public static String getLocalUnApkPath(MyGameInfo info) {
		return new File(info.getLocalDir(), getLocalApkName(info))
				.getAbsolutePath();
	}

	/**
	 * 
	 * @description 获取对应游戏apk名
	 * @param info
	 * @return
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:41:23
	 * 
	 */
	public static String getLocalApkName(GameInfo info) {
		return info.getPackageName() + "_" + info.getGameId() + ".apk";
	}

	/**
	 * 
	 * @description 获取对应游戏apk名
	 * @param info
	 * @return
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:42:05
	 * 
	 */
	public static String getLocalApkName(MyGameInfo info) {
		return info.getPackageName() + "_" + info.getGameId() + ".apk";
	}

	/**
	 * 
	 * @description 获取游戏压缩包名
	 * @param info
	 * @return
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:43:37
	 * 
	 */
	public static String getLocalZipName(GameInfo info) {
		return info.getPackageName() + "_" + info.getGameId() + ZIP_EXT;
	}

	/**
	 * 
	 * @description 获取游戏压缩包名
	 * @param info
	 * @return
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:44:03
	 * 
	 */
	public static String getLocalZipNameFromMygameInfo(MyGameInfo info) {
		return info.getPackageName() + "_" + info.getGameId() + ZIP_EXT;
	}

	/**
	 * 获取本地保存目录：sd卡优先，sd卡不可用时使用缓存目录
	 * 
	 * @param context
	 * @return
	 * @throws
	 */
	public static String getLocalSaveDir(Context context) {
		if (!Constant.IS_USE_CACHE_PATH_TO_SAVE) {
			return Constant.GAME_DOWNLOAD_LOCAL_DIR;
		}
		String sdRoot = null;
		try {
			sdRoot = DeviceTool.getSDPath();
			if (StringTool.isEmpty(sdRoot)) {
				// sdRoot=context.getCacheDir().getAbsolutePath()+"/game/";
				sdRoot = context.getFilesDir().getParentFile()
						.getAbsolutePath()
						+ "/";
			} else {
				sdRoot += Constant.GAME_RELATIVE_DIR;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "/";
		}
		return sdRoot;
	}

	/**
	 * 检测年龄，如果年龄没有达到会跳转到输入密码界面(通过onActivityResult方法返回结果来判断是否可以下载)，
	 * 直接下载的方法可调用{@link #addToMyGameList(Context, GameInfo)}来完成
	 * 如果没有年龄限制，直接进行下载
	 * @param context 必须是Activity
	 * @param gameInfo 游戏信息
	 */
	public static void chekAgeDownloadGame(Context context, final GameInfo gameInfo) {
		chekAgeDownloadGame(context, gameInfo, null);
	}

	/**
	 * 检测年龄，如果年龄没有达到会跳转到输入密码界面(通过onActivityResult方法返回结果来判断是否可以下载)，
	 * 直接下载的方法可调用{@link #addToMyGameList(Context, GameInfo)}来完成
	 * 如果没有年龄限制，如果{@link ChekDownloadGameCallback}接口不为null直接调用接口方法返回，如果为null直接进行下载
	 * @param context 必须是Activity
	 * @param gameInfo 游戏信息
	 * @param chekDownloadGameCallback 回调处理的接口
	 */
	public static void chekAgeDownloadGame(Context context, GameInfo gameInfo,
										   ChekDownloadGameCallback chekDownloadGameCallback) {

		if (context == null || gameInfo == null) return ;

		// 查询的我的游戏列表中是否存在,存在就不需要进行年龄判断了
		Group<MyGameInfo> infos = PersistentSynUtils.getModelList(
				MyGameInfo.class, " packageName='" + gameInfo.getPackageName() + "'");

		if ((infos == null || infos.size() <= 0)
				&& !ChildlockUtils.isInAllowedAge(gameInfo.getFitAge(), context)) {

			Intent intent = new Intent();
			intent.setClass(context,
					SetupChildlockSetPasswordActivity.class);
			intent.putExtra(ChildlockUtils.CONTEXT_FLAG,
					ChildlockUtils.CONTEXT_FLAG);
			Activity activity = (Activity) context;
			activity.startActivityForResult(intent, 0);
			return ;
		}

		if (chekDownloadGameCallback != null) {
			// 回调下载(还有内部的逻辑需要处理，不直接添加到列表中)
			chekDownloadGameCallback.onDownloadGame(context, gameInfo);
			return ;
		}

		// 直接添加
		addToMyGameList(context, gameInfo);
	}

	/**
	 * 
	 * @description 添加到我的游戏列表并开始下载
	 * @param context
	 * @param gameInfo
	 * @return
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:44:26
	 * 
	 */
	public static boolean addToMyGameList(Context context,
			final GameInfo gameInfo) {
		if (!NetUtil.isEnableDownload(context, true)) {
			return false;
		}

		MyGameInfo myGameInfo = getMyGameInfoFromGameInfo(context, gameInfo);
		if (myGameInfo == null) {
			NewToast.makeToast(context, R.string.manage_down_data_format_error, // 数据格式有误
					Toast.LENGTH_SHORT).show();
			return false;
		}

		DownloadTask downTask = DownloadTask.getInstance(context);
		boolean isNeedToDownload = true;
		Group<MyGameInfo> infos = PersistentSynUtils.getModelList(
				MyGameInfo.class,
				" packageName='" + gameInfo.getPackageName() + "'");
		if (infos == null || infos.size() < 1) {
			// 获取目录下存储空间
			long leftSize = DeviceTool.getAvailableSpaceByPath(myGameInfo
					.getLocalDir());
			// 获取SD卡路径
			String dataPath = Environment.getDataDirectory()
					.getAbsolutePath();
			if (dataPath == null || dataPath.length() <= 0) { // 无SD卡，则设置为data目录下
				dataPath = "/data";
			}
			long systemSize = DeviceTool.getAvailableSpaceByPath(dataPath);
			long systemAllSize = DeviceTool.getAllSpaceByPath(dataPath);
			long gameSize = 0;
			try {
				gameSize = Long.parseLong("" + gameInfo.getGameSize()); // 游戏大小
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			if (gameSize > 0 && leftSize >= 0 && gameSize > leftSize) { // 存储于SD卡下，但存储空间不足
				boolean isSdUsable = true;
				if (Constant.IS_USE_CACHE_PATH_TO_SAVE) {
					isSdUsable = DeviceTool.isSDCardReadWritable();
				}
				if (isSdUsable) {
					NewToast.makeToast(context,
							R.string.manage_down_error_no_external_space, // 存储空间不足
							Toast.LENGTH_SHORT).show();
				} else {
					NewToast.makeToast(context,
							R.string.manage_down_error_no_internal_space, // 存储空间不足
							Toast.LENGTH_SHORT).show();
				}
				return false;
			} else if ((systemSize - gameSize) < systemAllSize / 10) { // 存储空间少于10%不下载
				if (Constant.IS_USE_CACHE_PATH_TO_SAVE) {
					boolean isSdUsable = DeviceTool.isSDCardReadWritable();
					if (!isSdUsable) {
						NewToast.makeToast(
								context,
								R.string.manage_down_error_no_internal_space,
								Toast.LENGTH_SHORT).show();
						return false;
					}
				}
			}
			final MyGameInfo myGameInfo2 = myGameInfo;// 将数据库操作放置在线程中操作
			new Thread(new Runnable() {

				@Override
				public void run() {
					PersistentSynUtils.addModel(myGameInfo2); // 将获得到的MyGameInfo添加到数据库中

					// 数据统计
					CollectDownCountUtil.addGame(
							BaseApplication.getContext(), gameInfo);
					
					Boolean result = true;
					SpHelper.put(BaseApplication.getContext(), SpHelper.KEY_EXIST_NEW_MY_GAME, result);
				}
			}).start();
		} else {
			if (infos.get(0).getState() == Constant.GAME_STATE_INSTALLED) {
				NewToast.makeToast(
						context,
						myGameInfo.getName()
								+ context
								.getString(R.string.manage_down_installed), // 已经存在于游戏当中
						Toast.LENGTH_SHORT).show();
				isNeedToDownload = false;
			} else if (infos.get(0).getState() == Constant.GAME_STATE_NOT_INSTALLED) {
				NewToast.makeToast(
						context,
						myGameInfo.getName()
								+ context
								.getString(R.string.manage_down_not_install), // 已经存在于游戏当中
						Toast.LENGTH_SHORT).show();
				isNeedToDownload = false;
			}
			myGameInfo = infos.get(0);
		}
		if (isNeedToDownload) {
			if (!downTask.isDownloading(myGameInfo.getGameId())) {
				// 加入下载列表
				FileDownInfo fileDownInfo = new FileDownInfo(
						// 文件下载信息
						myGameInfo.getGameId(), myGameInfo.getDownUrl(),
						myGameInfo.getLocalDir(),
						myGameInfo.getLocalFilename());
				fileDownInfo.setExtraData(myGameInfo.getName());
				fileDownInfo.setObject(myGameInfo);
				// if (myGameInfo.getDownToken() ==
				// Constant.DOWN_FROM_THIRD) {
				// // fileDownInfo.setDownType(Constant.DOWN_FROM_THIRD);
				// // fileDownInfo.setDownToken(myGameInfo.getDownToken());
				// //
				// fileDownInfo.setDownUrl(myGameInfo.getMidDownAdress());
				// } else if (myGameInfo.getDownToken() ==
				// Constant.DOWN_FROM_SKYDRIVE) {
				// // fileDownInfo.setDownType(Constant.DOWN_FROM_SKYDRIVE);
				// // fileDownInfo.setDownToken(myGameInfo.getDownToken());
				// //
				// fileDownInfo.setDownUrl(myGameInfo.getMidDownAdress());
				// } else {
				// // fileDownInfo.setDownType(Constant.DOWN_FROM_LOCAL);
				// }
				downTask.download(fileDownInfo, myGameInfo.getDownToken());
				NewToast.makeToast(
						context,
						myGameInfo.getName()
								+ context
								.getString(R.string.manage_down_addto_list), // 已加入下载列表
						Toast.LENGTH_SHORT).show();
			} else {
				NewToast.makeToast(
						context,
						myGameInfo.getName()
								+ context
								.getString(R.string.manage_down_downloading), // 正在下载中...
						Toast.LENGTH_SHORT).show();
			}
		}
		return true;
	}

	public interface ChekDownloadGameCallback {

		void onDownloadGame(Context context, GameInfo gameInfo);
	}
}
