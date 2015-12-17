package com.atet.tvmarket.control.mygame.utils;

import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.app.UrlConstant;
import com.atet.tvmarket.control.mygame.task.AsynTaskListener;
import com.atet.tvmarket.control.mygame.task.BaseTask;
import com.atet.tvmarket.control.mygame.task.TaskManager;
import com.atet.tvmarket.control.mygame.task.TaskResult;
import com.atet.tvmarket.control.mygame.update.HttpApi;
import com.atet.tvmarket.control.mygame.update.HttpReqParams;
import com.atet.tvmarket.entity.Group;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.model.DataHelper;
import com.atet.tvmarket.model.database.PersistentSynUtils;
import com.atet.tvmarket.model.entity.AppInfo;
import com.atet.tvmarket.model.entity.MyGameInfo;
import com.atet.tvmarket.model.entity.PreGameNetData;
import com.atet.tvmarket.model.net.http.download.FileUtils;
import com.atet.tvmarket.utils.AppUtil;
import com.atet.tvmarket.utils.NetUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;


public class ImplUtils extends Activity {
	public static final String TAG = "ImplUtils";
	private static TaskManager taskManager;
	private static Context mContext;
	private static Handler mHandler;
	public static final String ALARM_INTENT = "com.sxhl.tcltvmarket.action.alarm";
	public static final int INTERVAL = 12 * 60 * 60 * 1000;// 闹铃间隔，这里设为12小时闹一次
	static SharedPreferences mPreGamePreferences;
	static SharedPreferences.Editor mPreGameEditor;
	private static SharedPreferences newVersioncodeSharedPreferences;
	private static SharedPreferences.Editor newVersioncodeSPEditor;
	
	public static boolean handlePreInstallgames(Context context, Handler handler) 
	{
		mContext = context;
		taskManager = new TaskManager(context);
		mHandler = handler;
		return handlePregames();
	}
	
	/**
	 * 
	 * @description 处理预装游戏的问题
	 * @return
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:53:31
	 *
	 */
	public static boolean handlePregames() {
		mPreGamePreferences = mContext.getSharedPreferences("chargefirst",
				Context.MODE_PRIVATE);
		mPreGameEditor = mPreGamePreferences.edit();
		boolean flag = mPreGamePreferences.getBoolean("isLoadPregameFromNet",
				true);
		boolean isNetOk = NetUtil.checkNetWorkStatus(mContext);
		// 如果是true，表示为第一次加载预装信息,并且网络正常，整个大厅只加载一次预装信息
		if (isNetOk) {
			loadPregame();
		} else {
			mHandler.sendEmptyMessage(0x12345);
		}

		return flag;
	}


	/**
	 * 
	 * @description 异步加载预装游戏
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:54:13
	 *
	 */
	public static void loadPregame() {
		taskManager.cancelTask(Constant.TASKKEY_PREGAME);
		taskManager.startTask(mPreAsynListener, Constant.TASKKEY_PREGAME);
	}
	
	/**
	 * 预装游戏数据异步加载线程
	 */
	private static AsynTaskListener<PreGameNetData> mPreAsynListener = new AsynTaskListener<PreGameNetData>() {

		@Override
		public boolean preExecute(BaseTask<PreGameNetData> task, Integer taskKey) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public void onResult(Integer taskKey, TaskResult<PreGameNetData> result) {
			// TODO Auto-generated method stub
			// 当网络正常时
			if (result.getCode() == TaskResult.OK) {
				// 当能从后台得到的数据不为null时
				if (result.getData() != null) {
					// 从后台得到的预装游戏包是一个实体对象
					final PreGameNetData preGamePkgs = result.getData();

					// 当实体对象中表征包名串的字段data不为null时
					if (preGamePkgs.getData() != null) {
						// 将包名转化为字符串数组形式
						final String[] pkgnames = preGamePkgs.getData().split(",");
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								Group<AppInfo> mPreGameList = AppUtil.getPreInstalledGameList(
										mContext, pkgnames); // 这里就得到了要显示的预装游戏列表
								// 将要显示的预装游戏存储到MyGameInfo中
								AppUtil.savePreInstalledGameToMyGameInfo(mContext,
										mPreGameList);
							}
						}).start();
					}

				}
				// 无论第一次是否加载得到预装游戏，只要本次网络正常，以后就不再加载预装游戏信息，----------------------这个需求有待商议
				// 预装游戏加载完成后，在SharePreferences中改写设置
				mPreGameEditor.putBoolean("isLoadPregameFromNet", false);
				mPreGameEditor.commit();
			}
			mHandler.sendEmptyMessage(0x12345);
		}

		@Override
		public TaskResult<PreGameNetData> doTaskInBackground(Integer taskKey) {
			// TODO Auto-generated method stub
			HttpReqParams params = new HttpReqParams();
			params.setDeviceId(DataHelper.getDeviceInfo().getServerId());
			return HttpApi.getObject(
					UrlConstant.HTTP_LAND_GAMEBOX_PREINSTALLED,UrlConstant.HTTP_LAND_GAMEBOX_PREINSTALLED2,UrlConstant.HTTP_LAND_GAMEBOX_PREINSTALLED3,
					PreGameNetData.class, params.toJsonParam());
		}
	};


	// --------------------------------------------------apk文件的获取----------------------------------------------------

	/**
	 * 
	 * @description 检查指定目录下是否存在apk
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:54:41
	 *
	 */
	public static void checkApks() {
		// TODO Auto-generated method stub
		FileUtils.getApkFileAndSavetoDB(Constant.APK_FOLDER_PATH);
		FileUtils.removeApkInfoFromDB();
	}

	
	/**
	 * 
	 * @description 监测游戏是否有更新
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:55:27
	 *
	 */
	public static void updateGame() {
		gameVersionUpdate();
	}


	/**
	 * 
	 * @description  开启一个新线程来实现游戏版本更新
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:56:02
	 *
	 */
	private static void gameVersionUpdate() {
		newVersioncodeSharedPreferences = mContext.getSharedPreferences(
				"newVersionCode", Context.MODE_PRIVATE);
		newVersioncodeSPEditor = newVersioncodeSharedPreferences.edit();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				startGamesNeedsUpdateAsyn();
			}
		}).start();
	}



	/**
	 * 
	 * @description 从我的游戏表中获取已经安装的游戏包名，包含预装游戏
	 * @return
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:56:14
	 *
	 */
	private static String getPkgNamesOfInstalledFromMygames() {
		Group<MyGameInfo> gameInfos = PersistentSynUtils.getModelList(
				MyGameInfo.class, " state in ("
						+ Constant.GAME_STATE_INSTALLED_SYSTEM + ","
						+ Constant.GAME_STATE_INSTALLED_USER + ","
						+ Constant.GAME_STATE_INSTALLED + ");");
		if (gameInfos == null || gameInfos.size() == 0) {
			return "";
		}
		String pkgsStr = "";
		int gameInfoSize = gameInfos.size();
		for (int i = 0; i < gameInfoSize; i++) {
			pkgsStr = pkgsStr + gameInfos.get(i).getPackageName() + ",";
		}
		int len = pkgsStr.length();
		pkgsStr = pkgsStr.substring(0, len - 1);
		return pkgsStr;
	}




	/**
	 * 
	 * @description 根据游戏包名来获取本地游戏的版本号
	 * @param gamePkgname
	 * @return
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:57:04
	 *
	 */
	private static int getVersionCodeFromLocal2(String gamePkgname) {
		int versionCodeFromLocal = 0;
		Group<MyGameInfo> gameInfos = PersistentSynUtils.getModelList(
				MyGameInfo.class, " packageName ='" + gamePkgname + "';");
		if (gameInfos == null || gameInfos.size() == 0) {
			return 0;
		}
		versionCodeFromLocal = gameInfos.get(0).getVersionCode();
		return versionCodeFromLocal;
	}
	
	
	/**
	 * 
	 * @description
	 * @param packageName  根据游戏包名来获取本地游戏的游戏Id
	 * @return
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:57:14
	 *
	 */
	private static String getLocalGameIdByPackageName(String packageName) {
		// TODO Auto-generated method stub
		String localGameId = "";
		Group<MyGameInfo> myGames = PersistentSynUtils.getModelList(
				MyGameInfo.class, " packageName='" + packageName + "'");
		if (myGames != null && myGames.size() != 0) {
			localGameId = myGames.get(0).getGameId();
		}
		return localGameId;
	}


	/**
	 * 
	 * @description  异步获取需要升级的游戏信息列表
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:58:29
	 *
	 */
	private static void startGamesNeedsUpdateAsyn() {
		taskManager.cancelTask(Constant.TASKKEY_NEEDUPDATEGAME);
		taskManager.startTask(mGamesNeedsUpdateAsynListener,
				Constant.TASKKEY_NEEDUPDATEGAME);
	}

	/**
	 * 获取游戏更新的线程
	 */
	private static AsynTaskListener<Group<GameInfo>> mGamesNeedsUpdateAsynListener = new AsynTaskListener<Group<GameInfo>>() {

		@Override
		public boolean preExecute(BaseTask<Group<GameInfo>> task,
				Integer taskKey) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public void onResult(Integer taskKey, TaskResult<Group<GameInfo>> result) {
			// 如果网络中没有需要更新的游戏，但本地仍然有可能有需要更新的，比如上次提示需要更新，但用户没有手动更新的游戏
			if (result.getCode() == TaskResult.NODATA) {
				
			}
			// 当网络正常时
			if (result.getCode() == TaskResult.OK) {
				// 当能从后台得到的数据不为null时
				if (result.getData() != null) {
					// 后台返回的 当前需要升级的游戏列表
					Group<GameInfo> needUpdateGames = result.getData();
					int needUpdateGamesSize = needUpdateGames.size();
					if (0 == needUpdateGamesSize) {
						return;
					}
					// 比较每一个后台返回的需要更新的游戏，看是否需要升级
					for (int i = 0; i < needUpdateGamesSize; i++) {
						GameInfo gameInfo = needUpdateGames.get(i);
						// 后台服务器获取的游戏信息的gameId

						if(gameInfo == null)
							return;
						String gameIdFromNet = gameInfo.getGameId();
						String gamePkgname = gameInfo.getPackageName();
						String gameIdFromLocal = getLocalGameIdByPackageName(gamePkgname);
						// 后台服务器获取的游戏信息的versionCode
						if (gameIdFromNet.equals(gameIdFromLocal)) {
						int versionCodeFromNet = gameInfo.getVersionCode();
						// 根据gameId来获取数据库中保存的安装游戏versionCode
						// int versionCodeFromLocal =
						// getVersionCodeFromLocal(gameIdFromNet);
						int versionCodeFromLocal = getVersionCodeFromLocal2(gamePkgname);
						// 如果数据库中保存的versioncode为0(由于前期程序的原因，临时设置的值，不代表真实的versioncode)，则获取真实的versioncode
						if (0 == versionCodeFromLocal) {
							versionCodeFromLocal = getRealVersionCodeIfZero(gameInfo
									.getPackageName());
						}
						// 比较versionCode来判断是否需要升级，如果后台给出versioncode比本地的versioncode要大，则表明需要升级
						if (versionCodeFromNet > versionCodeFromLocal) {
							// 更新gameId游戏的state
							// 当检测到需要更新的时候，就修改状态state
							updateGameState(gameInfo.getPackageName());
							updateGameId(gameInfo.getGameId(),
									gameInfo.getPackageName());
							// 同时将新版本信息保存到SP中
							newVersioncodeSPEditor.putInt(gameInfo.getGameId(), versionCodeFromNet);
						}
					}
					}
					newVersioncodeSPEditor.commit();
					// 显示游戏更新提示信息，由本地需要更新的游戏和网络请求要求更新的游戏两部分组成
					// showGameUpdateInfo(needUpdateGamesNumFromNet
					// + stateIsNeedupdateGamesNum);
					// 只有当游戏版本真正检测后才保存更新时间，避免网络异常等原因造成干扰
				}
			}
		}

		@Override
		public TaskResult<Group<GameInfo>> doTaskInBackground(Integer taskKey) {
			// TODO Auto-generated method stub
			HttpReqParams params = new HttpReqParams();
			String pkgNamesStr = getPkgNamesOfInstalledFromMygames();
			params.setDeviceId(DataHelper.getDeviceInfo().getServerId());
			params.setPackageNames(pkgNamesStr);
			return HttpApi.getList(UrlConstant.HTTP_GET_GAMEINFO_INSTALLED,UrlConstant.HTTP_GET_GAMEINFO_INSTALLED2,UrlConstant.HTTP_GET_GAMEINFO_INSTALLED3,
					GameInfo.class, params.toJsonParam());
		}
	};

	
	
	
	private static AlertDialog mGameUpdateDialog;
//	private static SharedPreferences updateSharedPreferences;
//	private static final String KEY_LASH_UPDATE = "last_update_time";
	/**
	 * 
	 * @Title: showGameUpdateInfo
	 * @Description: TODO 提示需要升级数目信息的对话框
	 * @param @param needUpdateGamesSize
	 * @return void
	 * @throws
	 */
//	private static void showGameUpdateInfo(int needUpdateGamesSize) {
//		if (0 == needUpdateGamesSize) {
//			return;
//		}
//		mGameUpdateDialog = new AlertDialog.Builder(updateContext)
//				.setTitle(
//						updateContext.getResources().getString(
//								R.string.gameupdate_dialog_updatetip))
//				.setMessage(
//						updateContext.getResources().getString(
//								R.string.gameupdate_dialog_msg1)
//								+ needUpdateGamesSize
//								+ updateContext.getResources().getString(
//										R.string.gameupdate_dialog_msg2))
//				.setPositiveButton(
//						updateContext.getResources().getString(
//								R.string.gameupdate_dialog_sure),
//						new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//								// TODO Auto-generated method stub
//								mGameUpdateDialog.dismiss();
//								mGameUpdateDialog = null;
//							}
//						})
//				.setNegativeButton(
//						updateContext.getResources()
//								.getString(R.string.cancle_delete), null)
//				.setOnKeyListener(mOnDialogKeyListener).show();
//		AppUtil.setDialogAlpha(mGameUpdateDialog,
//				Constant.DIALOG_BACKGROUND_ALPHA);
////		mUpdateTimeEditor = updateSharedPreferences.edit();
////		mUpdateTimeEditor.putLong(KEY_LASH_UPDATE, System.currentTimeMillis())
////				.commit();
//	}

	/**
	 * 对话框的按键事件处理
	 */
	private static DialogInterface.OnKeyListener mOnDialogKeyListener = new DialogInterface.OnKeyListener() {
		@Override
		public boolean onKey(DialogInterface dialogInterface, int keyCode,
				KeyEvent event) {
			// TODO Auto-generated method stub
			View focusView = ((Dialog) dialogInterface).getCurrentFocus();
			if (focusView == null) {
				return false;
			}

			if (keyCode == KeyEvent.KEYCODE_X || keyCode == KeyEvent.KEYCODE_Y
					|| keyCode == KeyEvent.KEYCODE_BUTTON_X
					|| keyCode == KeyEvent.KEYCODE_BUTTON_Y) {
				KeyEvent keyevent = new KeyEvent(event.getAction(),
						KeyEvent.KEYCODE_ENTER);
				focusView.dispatchKeyEvent(keyevent);
				return true;
			} else if (keyCode == KeyEvent.KEYCODE_A
					|| keyCode == KeyEvent.KEYCODE_B
					|| keyCode == KeyEvent.KEYCODE_BUTTON_A
					|| keyCode == KeyEvent.KEYCODE_BUTTON_B) {
				Dialog dialog = (Dialog) dialogInterface;
				if (event.getAction() == KeyEvent.ACTION_UP && dialog != null
						&& dialog.isShowing()) {
					dialog.dismiss();
				}
				return true;
			}
			return false;
		}
	};

	/**
	 * 
	 * @description 更新游戏state
	 * @param packageName
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:59:21
	 *
	 */
	private static void updateGameState(String packageName) {
		String sql = "update myGameInfo set state="
				+ Constant.GAME_STATE_NEED_UPDATE + " where packageName=" + "'"
				+ packageName + "';";
		PersistentSynUtils.execSQL(sql);
	}


	/**
	 * 
	 * @description 更新gameId
	 * @param gameId
	 * @param packageName
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:59:38
	 *
	 */
	private static void updateGameId(String gameId, String packageName) {
		String sql = "update myGameInfo set gameId=" + "'" + gameId + "'"
				+ " where packageName=" + "'" + packageName + "';";
		PersistentSynUtils.execSQL(sql);
	}
	


	/**
	 * 
	 * @description 如果版本号为0(ps:前期开发中,默认的 )，则获取真正的版本号
	 * @param pkgName
	 * @return
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:59:55
	 *
	 */
	private static int getRealVersionCodeIfZero(String pkgName) {
		int realVersionCode = AppUtil.getInstalledAppVersionCode(mContext,
				pkgName);
		if (-1 == realVersionCode) {
			return 0;
		}
		return realVersionCode;
	}
}
