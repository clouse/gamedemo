package com.atet.tvmarket.control.mygame.utils;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

import com.atet.tvmarket.R;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.app.UrlConstant;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.mygame.activity.GameDetailActivity;
import com.atet.tvmarket.control.mygame.activity.MyGameActivity;
import com.atet.tvmarket.control.mygame.task.TaskResult;
import com.atet.tvmarket.control.mygame.update.HttpApi;
import com.atet.tvmarket.control.mygame.update.HttpReqParams;
import com.atet.tvmarket.entity.Group;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.model.DataHelper;
import com.atet.tvmarket.model.database.PersistentSynUtils;
import com.atet.tvmarket.model.entity.MyGameInfo;
import com.atet.tvmarket.model.net.http.download.DownloadTask;
import com.atet.tvmarket.model.net.http.download.FileDownInfo;
import com.atet.tvmarket.utils.AppUtil;
import com.atet.tvmarket.utils.DeviceTool;
import com.atet.tvmarket.utils.InstallExceptionUtils;
import com.atet.tvmarket.utils.StringTool;
import com.atet.tvmarket.utils.ZipUtils;
import com.atet.tvmarket.view.CommonProgressDialog;
import com.atet.tvmarket.view.NewToast;

/**
 * 安装
 * 卸载
 * 更新
 * 下载
 * @author chenqingwen
 *
 */
public class DownloadingUtil {
	private CommonProgressDialog mDialog;
	private Context context;
	private Handler mHandler; 
    private PackageManager mPm;
    private DownloadTask mDownTask;// 下载实现的对象
//	private static boolean mIsUnzipping = false;
 // 安装包名，安装中设置，安装完成后，清空。以此属性判断是否有游戏正在安装
	private static String mInstallingPackageName;  
	private static final int HANDLER_GAME_START_UPDATE = 151; // 更新开始
	private static final int HANDLER_GAME_UPDATED_FAILED = 152;// 更新失败
	private static final int HANDLER_GAME_UPDATED_SUCCESS = 153;// 更新成功
	 // 记录对应游戏新版本版本号的newVersioncodeSharedPreferences
	private SharedPreferences newVersioncodeSharedPreferences;
	private SharedPreferences.Editor newVersioncodeSPEditor;
	private static final boolean IS_ONLY_ZIP = true;
	public static final String ZIP_EXT = ".zip";
	private ImageFetcher imageFetcher;	
	
	public DownloadingUtil(Context context, Handler handler) {
		this.context = context;
		mHandler = handler;
		mPm = context.getPackageManager();
		mDownTask = DownloadTask.getInstance(context);
	}

  
	public void setImageFetcher(ImageFetcher imageFetcher) {
		this.imageFetcher = imageFetcher;
	}



	
	/**
	 * 
	 * @description  长按显示删除对话框
	 * @param myGameInfo
	 * @param index 对应的位置
	 * @param mDownloadTask
	 * @param textView 显示提示信息的TextView
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:12:43
	 *
	 */
	public void showDeleteDialog(final MyGameInfo myGameInfo,final int index, final DownloadTask mDownloadTask, final TextView textView) {
		final int state = myGameInfo.getState(); // 状态：1已下载但未安装，2已安装，0未下载
		int titleId;
        // dialog 内容赋值
		if (state == Constant.GAME_STATE_INSTALLED_SYSTEM) {
			titleId = R.string.manage_uninstall_icon;
		} else {
			titleId = R.string.manage_uninstall_game;
		}
		
		CommonProgressDialog.Builder builder = new CommonProgressDialog.Builder(context)
		        .setIcon(myGameInfo.getIconUrl())
				.setMessage(context.getResources().getString(titleId, myGameInfo.getName()))
				.setPositiveButton(R.string.ok_delete,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								try {

									boolean apkInstalled = false;
									// 系统预装游戏
									if ((state == Constant.GAME_STATE_INSTALLED_SYSTEM && AppUtil.getPkgInfoByName(
											context,
											myGameInfo.getPackageName()) != null)) {
										NewToast.makeToast(
												context,
												context.getResources().getString(R.string.mygame_tip_cantdelete),
												Toast.LENGTH_SHORT).show();
										return;
									}
									// 游戏已安装
									if ((state & 0xff) == Constant.GAME_STATE_INSTALLED
											|| state == Constant.GAME_STATE_NEED_UPDATE) {
										// 获取对应PackageInfo
										PackageInfo packageInfo = AppUtil
												.getPkgInfoByName(
														context,
														myGameInfo
																.getPackageName());
										if (packageInfo != null) {
											apkInstalled = true; // 此apk已经被安装		
											uninstallApk2(
													context,
													myGameInfo.getPackageName(),
													myGameInfo.getName(), index, textView);
											return;
										}
									}
									// apk未安装
									if (!apkInstalled) {
										new Thread(new Runnable() {
											@Override
											public void run() {
												try {
													// 更新或者删除操作，必须重新获取一次游戏信息，需要automentId
													Group<MyGameInfo> myGameInfos = PersistentSynUtils
															.getModelList(MyGameInfo.class, " packageName='"
																	+ myGameInfo.getPackageName() + "'");

													// 查看游戏是否安装
													PackageInfo packageInfo = AppUtil.getPkgInfoByName(context, myGameInfo.getPackageName());

													if (packageInfo != null) {
														// 卸载游戏
														uninstallApk2(
																context,
																myGameInfo.getPackageName(),
																myGameInfo.getName(), index, textView);
													}

													// 删除对应游戏信息
													if (myGameInfos != null && myGameInfos.size() > 0) {
														PersistentSynUtils
																.delete(myGameInfos.get(0));
													}
													// 删除压缩包以及apk文件
													if (state == Constant.GAME_STATE_NOT_INSTALLED
															|| state == Constant.GAME_STATE_INSTALLED) {
														// 删除压缩包
														File apkFile = new File(
																myGameInfo
																		.getLocalDir(),
																myGameInfo
																		.getLocalFilename());
														if (apkFile.exists()) {
															apkFile.delete();
														}

														if (myGameInfo
																.getLocalFilename()
																.toLowerCase()
																.endsWith(
																		MyGameActivity.ZIP_EXT)) {
															// 删除解压出来的apk文件
															File file = new File(
																	MyGameActivity
																			.getLocalUnApkPath(myGameInfo));
															if (file != null
																	&& file.exists()) {
																file.delete();
															}
														}
													} else if (state == Constant.GAME_STATE_INSTALLED_SYSTEM
															|| state == Constant.GAME_STATE_INSTALLED_USER) {
													} else { // 未下载完成，则删除对应的临时文件
														mDownloadTask
																.cancelDownload(new FileDownInfo(
																		myGameInfo
																				.getGameId(),
																		null,
																		myGameInfo
																				.getLocalDir(),
																		myGameInfo
																				.getLocalFilename()));
													}

													// 发送删除游戏成功的广播
													Intent in = new Intent(
															DownloadTask.ACTION_ON_APP_INSTALLED);
													Bundle bundle = new Bundle();
													bundle.putInt(
															"opType",
															DownloadTask.OP_UNINSTALL);
													bundle.putSerializable(
															"myGameInfo",
															myGameInfo);
													in.putExtra("data", bundle);
													context.sendBroadcast(in);
												} catch (Exception e) {
													// TODO: handle
													// exception
													e.printStackTrace();
												}
											}
										}).start();

									}
								} catch (Exception e) {
									// TODO
									e.printStackTrace();
								}
							}
						}).setNegativeButton(R.string.cancle_delete, null);
		mDialog =  builder.create(imageFetcher);
		mDialog.setParams(mDialog);
		mDialog.show();
	}

	private void tryUninstallPackage(MyGameInfo myGameInfo) {




	}

	/**
	 *
	 * @description 显示游戏更新对话框
	 * @param myGameInfo
	 * @param index
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:13:35
	 *
	 */
	public void showUpdateDialog(final MyGameInfo myGameInfo, final int index) {
		CommonProgressDialog.Builder builder = new CommonProgressDialog.Builder(context)
				.setTitle(R.string.down_title_tip)
				.setMessage(
						context.getResources()
								.getString(R.string.gameupdate_dialog_msg))
				.setNegativeButton(R.string.gameupdate_dialog_cancle,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								// 取消
							}
						})
				.setPositiveButton(R.string.gameupdate_dialog_update,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 处理更新
								handleUpdate(myGameInfo);
							}
						});
		mDialog =  builder.create(); 
		mDialog.setParams(mDialog);
		mDialog.show();
	}
		
	

	/**
	 * 
	 * @description 显示安装提示对话框
	 * @param myGameInfo
	 * @param textView
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:13:50
	 *
	 */
	public void showInstallApkDialog(final MyGameInfo myGameInfo, final TextView textView) {
		CommonProgressDialog.Builder builder = new CommonProgressDialog.Builder(context)
				.setTitle(
						context.getResources().getString(
								R.string.apkinstall_dialog_title))
				.setMessage(
						context.getResources()
								.getString(R.string.apkinstall_dialog_msg))
				.setPositiveButton(
						context.getResources().getString(
								R.string.apkinstall_dialog_sure),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								// apk安装提示对话框
								apkInstallAndShowProDialog(myGameInfo, textView);
							}
						})
				.setNegativeButton(
						context.getResources().getString(
								R.string.apkinstall_dialog_cancle), null);
		mDialog =  builder.create(); 
		mDialog.setParams(mDialog);
		mDialog.show();
	}
	

	/**
	 * 
	 * @description 重新安装
	 * @param myGameInfo
	 * @param textView
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:14:02
	 *
	 */
	public void showReInstallDialog(final MyGameInfo myGameInfo, final TextView textView) {
		CommonProgressDialog.Builder builder = new CommonProgressDialog.Builder(context)
				.setTitle(R.string.down_title_tip)
				.setMessage(
						myGameInfo.getName()
								+ context.getText(R.string.down_msg_already_uninstall))
				.setPositiveButton(R.string.down_btn_reinstall,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								// 重新安装
								if (! StringTool.isEmpty(mInstallingPackageName)) {
									NewToast.makeToast(context, R.string.exist_game_installing, Toast.LENGTH_LONG).show();
									return;
								}
								myGameInfo
										.setState(Constant.GAME_STATE_NOT_INSTALLED);
								PersistentSynUtils.update(myGameInfo);
								// 界面没变化，无需更新
								// mMyGameAdapter.notifyDataSetChanged();

								mInstallingPackageName = myGameInfo.getPackageName();
								final File apkFile = new File(myGameInfo
										.getLocalDir(), myGameInfo
										.getLocalFilename());  //游戏压缩包路径
								final String unApkPath = MyGameActivity
										.getLocalUnApkPath(myGameInfo); // 解压后的文件路径
								final String packageName = myGameInfo
										.getPackageName();
								final String name = myGameInfo.getName();
								new Thread(new Runnable() {
									@Override
									public void run() {
										// TODO Auto-generated method stub
										unZipApk(
												apkFile.getAbsolutePath(),
												Constant.GAME_ZIP_DATA_LOCAL_DIR,
												unApkPath, packageName, name, textView);
									}
								}).start();
							}
						})
				.setNegativeButton(R.string.cancle_delete,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								// 取消
							}
						});
		mDialog =  builder.create(); 
		mDialog.setParams(mDialog);
		mDialog.show();
	}
	

	/**
	 * 
	 * @description 显示重新下载对话框
	 * @param myGameInfo
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:14:14
	 *
	 */
	public void showReDownloadDialog(final MyGameInfo myGameInfo) {
		CommonProgressDialog.Builder builder = new CommonProgressDialog.Builder(context)
				.setTitle(R.string.down_title_tip)
				.setMessage(myGameInfo.getName()
						+ context.getText(R.string.down_msg_already_uninstall))
				.setPositiveButton(R.string.down_btn_redownload,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								// 重新下载
								myGameInfo
										.setState(Constant.GAME_STATE_NOT_DOWNLOAD);
								// 更新游戏状态
								PersistentSynUtils.update(myGameInfo);
								notDownloadHanlde(myGameInfo);
							}
						})
				.setNegativeButton(R.string.cancle_delete,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								// 取消
							}
						});
		mDialog =  builder.create(); 
		mDialog.setParams(mDialog);
		mDialog.show();
		
	}

	
	
	/**
	 * 
	 * @description 处理更新
	 * @param myGameInfo
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:14:25
	 *
	 */
	private void handleUpdate(final MyGameInfo myGameInfo) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@SuppressWarnings("unused")
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg_start_update = mHandler.obtainMessage(
						HANDLER_GAME_START_UPDATE, myGameInfo.getGameId());
				mHandler.sendMessage(msg_start_update);
				// 获取最新的游戏信息
				GameInfo gameInfo = getLatestMyGameInfo(myGameInfo);
				if (gameInfo == null) {  // 更新失败
					Message msg_update_failed = mHandler.obtainMessage(
							HANDLER_GAME_UPDATED_FAILED, myGameInfo.getGameId());
					mHandler.sendMessage(msg_update_failed);
					return;
				}
				myGameInfo.setDownUrl(gameInfo.getFile());
				myGameInfo.setIconUrl(gameInfo.getMinPhoto());
				// 修改游戏状态未下载
				myGameInfo.setState(Constant.GAME_STATE_NOT_DOWNLOAD);
			    newVersioncodeSharedPreferences =  // 获取对应游戏更新的版本号
			    		context.getSharedPreferences(
			    				"newVersionCode", Context.MODE_PRIVATE);
			    newVersioncodeSPEditor = newVersioncodeSharedPreferences.edit();
				int newVersionCode = newVersioncodeSharedPreferences.getInt(
						myGameInfo.getGameId(), 0);
				newVersioncodeSPEditor.remove(myGameInfo.getGameId()).commit();
				myGameInfo.setVersionCode(newVersionCode);

				if (IS_ONLY_ZIP
						|| gameInfo.getFile().toLowerCase().endsWith(ZIP_EXT)) {
					myGameInfo.setLocalFilename(MyGameActivity
							.getLocalZipName(gameInfo));
				} else {
					myGameInfo.setLocalFilename(MyGameActivity
							.getLocalApkName(gameInfo));
				}
				// 更新成功
				Message msg_update_success = mHandler.obtainMessage(
						HANDLER_GAME_UPDATED_SUCCESS, myGameInfo);
				mHandler.sendMessage(msg_update_success);

				PersistentSynUtils.update(myGameInfo);
				if (myGameInfo.getDownToken() == Constant.DOWN_FROM_THIRD) {
//					Intent intent = new Intent(context,
//							LandSearchResultActivity.class);
//					intent.putExtra(Constant.SEARCH_RESULT_GAMEID,
//							myGameInfo.getGameId());
//					startActivity(intent);
				} else {
					// 开始下载
					notDownloadHanlde(myGameInfo);
				}
			}

		}).start();
	   }
		
	
	

	/**
	 * 
	 * @description 安装apk
	 * @param myGameInfo
	 * @param textView
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:14:43
	 *
	 */
	private void apkInstallAndShowProDialog(MyGameInfo myGameInfo, TextView textView) {
		// 当前有游戏正在安装
		if (! StringTool.isEmpty(mInstallingPackageName)) {
			NewToast.makeToast(context, R.string.exist_game_installing, Toast.LENGTH_SHORT).show();
			return;
		}
		
		try {  // 安装
			installApk2(context,
					myGameInfo.getLocalDir() + myGameInfo.getLocalFilename(),
					myGameInfo.getPackageName(), myGameInfo.getName(),
					textView);
		} catch (Exception e) {
			// TODO: handle exception
		     NewToast.makeToast(context, context.getResources().getString(
						R.string.apkinstall_toast_error), Toast.LENGTH_SHORT).show();
		}
	}
	
	
	
	/**
	 * 
	 * @description 获取最新的myGameInfo
	 * @param myGameInfo
	 * @return
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:15:12
	 *
	 */
	private GameInfo getLatestMyGameInfo(MyGameInfo myGameInfo) {
		// TODO Auto-generated method stub
		HttpReqParams params = new HttpReqParams();
		params.setDeviceId(DataHelper.getDeviceInfo().getDeviceUniqueId());
		params.setGameId(myGameInfo.getGameId());
		TaskResult<Group<GameInfo>> resultGameInfos = HttpApi.getList(
				UrlConstant.HTTP_GAME_BOX_GAMEDETAIL,
				UrlConstant.HTTP_GAME_BOX_GAMEDETAIL2,
				UrlConstant.HTTP_GAME_BOX_GAMEDETAIL3, GameInfo.class,
				params.toJsonParam());
		if (resultGameInfos != null
				&& resultGameInfos.getCode() == TaskResult.OK
				&& resultGameInfos.getData() != null) {
			return resultGameInfos.getData().get(0);
		} else {
			return null;
		}
	}

	

	/**
	 * 
	 * @description 卸载游戏
	 * @param context
	 * @param packageName 包名
	 * @param name 游戏名
	 * @param index 
	 * @param textView
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:15:30
	 *
	 */
	public void uninstallApk2(final Context context, final String packageName,
			final String name, final int index, final TextView textView) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					textView.setText("卸载中");
					PackageDeleteObserver observer = new PackageDeleteObserver();
					observer.setName(name);
					observer.setTextView(textView);
					observer.setLocation(index);
					mPm.deletePackage(packageName, observer, 0);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					textView.setText("");
					uninstallApk3(context, packageName, name, textView);
				}
			}
		});
	}
	
	

	/**
	 * 
	 * @description 调用系统的卸载功能
	 * @param context 
	 * @param packageName
	 * @param name
	 * @param textView
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:16:03
	 *
	 */
	private void uninstallApk3(final Context context, final String packageName,
			final String name, TextView textView) {
		try {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_DELETE);
			intent.setData(Uri.parse("package:" + packageName));
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    /**
     * 
     * @description 安装apk
     * @param context
     * @param filePath
     * @param packageName
     * @param name
     * @param textView
     * @throws
     * @author：陈庆文
     * @date 2015-8-28上午11:16:22
     *
     */
	public void installApk2(final Context context, final String filePath,
			final String packageName, final String name,
			final TextView textView) {
		Uri mPackageURI = Uri.fromFile(new File(filePath));
		int installFlags = 0;

		try {
			PackageInfo pi = mPm.getPackageInfo(packageName,
					PackageManager.GET_UNINSTALLED_PACKAGES);
			if (pi != null) {
				installFlags |= PackageManager.INSTALL_REPLACE_EXISTING;
			}
		} catch (NameNotFoundException e) {
			// 发送安装失败的广播
//			setInstallText(textView);
//			sendInstallfailBroadcast(packageName);
		} catch (Exception e) {
			// 发送安装失败的广播
			setInstallText(textView);
			sendInstallfailBroadcast(packageName);
		}
		if ((installFlags & PackageManager.INSTALL_REPLACE_EXISTING) != 0) {
		}

		PackageInstallObserver observer = new PackageInstallObserver();
		observer.setName(name);
		observer.setProDialog(textView);
		mInstallingPackageName = packageName.toString();
		try {
			if (textView != null) {		
				textView
						.setText(context.getText(R.string.apkinstall_dialog_msg_installing)
								);
			}
			mPm.installPackage(mPackageURI, observer, installFlags, packageName);
		} catch (Exception e) {
			// 调用系统安装，显示信息还原
			setInstallText(textView);
			sendInstallfailBroadcast(packageName);
			e.printStackTrace();
			// 抛异常则調用系統安裝器安装
			try {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 启动新的activity
				// 设置Uri和类型
				intent.setDataAndType(mPackageURI,
						"application/vnd.android.package-archive");
				// 执行安装
				context.startActivity(intent);
			} catch (Exception f) {
				// TODO Auto-generated catch block
				f.printStackTrace();
				NewToast.makeToast(context,
						name + context.getText(R.string.install_failed),
						Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	

	/**
	 * 
	 * @description  解压zip格式的APK
	 * @param zipfilePath
	 * @param destDir
	 * @param apkDestPath
	 * @param packageName
	 * @param name
	 * @param textView
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:16:36
	 *
	 */
	public void unZipApk(final String zipfilePath, String destDir,
			final String apkDestPath, final String packageName,
			final String name, final TextView textView) {
//		if (mInstallingPackageName != null
//				&& mInstallingPackageName.equals(packageName)) {
//			mHandler.post(new Runnable() {
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
////					showToastMsg(getString(R.string.manage_zip_installing));
//					mInstallingPackageName = "";
//				}
//			});
//			return;
//		}

		final ZipUtils zip = new ZipUtils();
		zip.unZipApk(context, zipfilePath, destDir, apkDestPath, packageName,
				new ZipUtils.OnZipListener() {
					private CharSequence mMsg;
					private boolean isCancel = false;

					@Override
					public void onStart() {
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									if(textView != null){
									mMsg = context
											.getText(R.string.manage_zip_progress);
									textView.setText(mMsg + "0%");
									}
								} catch (Exception e) {
									// TODO: handle exception
									isCancel = true;
									zip.terminal();
									// 发送安装失败的广播
									sendInstallfailBroadcast(packageName);
									// 界面恢复安装时的文字显示
									setInstallText(textView);
									NewToast.makeToast(context,
						context.getString(R.string.manage_zip_init_error), Toast.LENGTH_SHORT).show();
								}
							}
						});
					}

					@Override
					public void onProcess(final int progress) {
						// TODO Auto-generated method stub
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (textView != null) {
									textView.setText(mMsg + "" + progress
											+ "%");
								}
							}
						});
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
//						DebugTool.debug(TAG, "zip onFinish()");
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (!isCancel
										&& DeviceTool.getCallState(context) == TelephonyManager.CALL_STATE_IDLE) {
									installApk2(context, apkDestPath,
											packageName, name, textView);
								} else {
									// 界面恢复安装时的文字显示
									setInstallText(textView);
									  //发送失败广播
									sendInstallfailBroadcast(packageName);
								}
							}
						});
					}

					@Override
					public void onError(Exception e, final int errId) {
						// TODO Auto-generated method stub
						mHandler.post(new Runnable() {
							@Override
							public void run() {

								int strId = errId;
								if (errId == R.string.manage_zip_file_format_error
										|| errId == R.string.manage_zip_error) {
									strId = R.string.manage_zip_file_error_delete;
								}
								NewToast.makeToast(context, strId, Toast.LENGTH_SHORT).show();
								setInstallText(textView);
								sendInstallfailBroadcast(packageName);
							}
						});
					}
				});
	}
	
	/**
	 * 
	 * @ClassName: PackageInstallObserver
	 * @Description: TODO 观察者观察应用包安装
	 * @author A18ccms a18ccms_gmail_com
	 * @date 2013-12-26 下午4:12:00
	 * 
	 */
	class PackageInstallObserver extends IPackageInstallObserver.Stub {
		public void packageInstalled(final String packageName, final int returnCode) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub				
					if (returnCode == PackageManager.INSTALL_SUCCEEDED) {
						if(context instanceof GameDetailActivity){
							   proDialog.setText(R.string.down_btn_installed);
							}else{
								if(proDialog!= null)
								 proDialog.setText("");
							}
						// 更新数据
						installUpdateData(packageName);
						NewToast.makeToast(context,
								name + context.getText(R.string.install_successed),
								Toast.LENGTH_SHORT).show();
					} else {
						// 发送安装失败的广播
						sendInstallfailBroadcast(packageName);
						setInstallText(proDialog);
						NewToast.makeToast(context,
								name + context.getText(R.string.install_failed),
								Toast.LENGTH_SHORT).show();
					}
					mInstallingPackageName = null;
				}
			}, 500);
			
			InstallExceptionUtils.collectGameInstallFailedInfoAsync(BaseApplication.getContext(), packageName, name, "unknown", returnCode);
		}

		/**
		 * 安装并更新数据库的数据(为了解决广播接收太慢的问题)
		 * @param packageName
		 */
		public void installUpdateData(String packageName) {

			Group<MyGameInfo> myGameInfos = PersistentSynUtils
					.getModelList(MyGameInfo.class, " packageName='" + packageName + "'");

			if (myGameInfos != null && myGameInfos.size() > 0) {
				MyGameInfo myGameInfo = myGameInfos.get(0);

				myGameInfo.setState(Constant.GAME_STATE_INSTALLED);
				List<ResolveInfo> resolveInfos = AppUtil
						.queryAppInfo(context, packageName);
				if (resolveInfos != null && resolveInfos.size() > 0) {
					myGameInfo.setLaunchAct(resolveInfos.get(0).activityInfo.name);
				}

				// 更新状态
				PersistentSynUtils.update(myGameInfo);

				Intent in = new Intent(
						DownloadTask.ACTION_ON_APP_INSTALLED);
				Bundle bundle = new Bundle();
				bundle.putInt("opType", DownloadTask.OP_INSTALL);
				// bundle.putString("packageName", packageName);
				// bundle.putString("gameId",
				// myGameInfo.getGameId());
				bundle.putSerializable("myGameInfo", myGameInfo);
				in.putExtra("data", bundle);
				context.sendBroadcast(in);
				DownloadingUtil.setmInstallingPackageName(null);
			}
		}

		private String name;
		private TextView proDialog;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public TextView getProDialog() {
			return proDialog;
		}

		public void setProDialog(TextView proDialog) {
			this.proDialog = proDialog;
		}
	}
	
	
	/**
	 * 
	 * @ClassName: PackageDeleteObserver
	 * @Description: TODO 观察应用包的删除
	 * @author A18ccms a18ccms_gmail_com
	 * @date 2013-12-26 下午4:08:20
	 * 
	 */
	class PackageDeleteObserver extends IPackageDeleteObserver.Stub {
		@Override
		public void packageDeleted(final String packageName,
				final int returnCode) throws RemoteException {
			// TODO Auto-generated method stub
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (returnCode == PackageManager.INSTALL_SUCCEEDED) {
						NewToast.makeToast(context,
								name + context.getText(R.string.uninstall_successed),
								Toast.LENGTH_SHORT).show();
						
					} else {
						NewToast.makeToast(context,
								name + context.getText(R.string.uninstall_failed),
								Toast.LENGTH_SHORT).show();
					}
					textView.setText("");
				}
			});
		}

		private String name;
		private TextView textView;
        private int location;
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		

		public TextView getTextView() {
			return textView;
		}

		public void setTextView(TextView textView) {
			this.textView = textView;
		}

		public int getLocation() {
			return location;
		}

		public void setLocation(int location) {
			this.location = location;
		}
		
	}
	
	
	/**
	 * 
	 * @description 处理游戏下载或者暂停
	 * @param myGameInfo
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:17:04
	 *
	 */
	public void notDownloadHanlde(MyGameInfo myGameInfo) {
		// 如果游戏不是处于下载状态(不处于下载队列中)
		if (!mDownTask.isDownloading(myGameInfo.getGameId())) {
			// 如果游戏状态为下载错误状态
			if (myGameInfo.getState() == Constant.GAME_STATE_DOWNLOAD_ERROR) {
				// 将状态改为没有下载
				myGameInfo.setState(Constant.GAME_STATE_NOT_DOWNLOAD);
				// 更新我的游戏信息
//				PersistentSynUtils.update(myGameInfo);
			}
			// 开始下载当前游戏
			startDownload(mDownTask, myGameInfo);
		} else { // 如果正处于下载中，则暂停
			mDownTask.setDownloadStop(myGameInfo.getGameId());
		}
	}
	
	

	/**
	 * 
	 * @description 开始下载
	 * @param downTask
	 * @param myGameInfo
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:17:27
	 *
	 */
	private void startDownload(DownloadTask downTask, MyGameInfo myGameInfo) {
		FileDownInfo fileDownInfo = new FileDownInfo(myGameInfo.getGameId(),
				myGameInfo.getDownUrl(), myGameInfo.getLocalDir(),
				myGameInfo.getLocalFilename());
		fileDownInfo.setExtraData(myGameInfo.getName());
		fileDownInfo.setObject(myGameInfo);
		// add by wenfuqiang
		if (myGameInfo.getDownToken() == Constant.DOWN_FROM_THIRD) {
			// 每次下载都获取一次下载地址

		} else {
			fileDownInfo.setDownUrl(myGameInfo.getDownUrl());
		}
//		if (myGameInfo.getAutoIncrementId() != null) {
//			myGameInfo.setDbId(myGameInfo.getAutoIncrementId());
//		}
		downTask.download(fileDownInfo, myGameInfo.getDownToken());
	}


	public static String getmInstallingPackageName() {
		return mInstallingPackageName;
	}


	public static void setmInstallingPackageName(String mInstallingPackageName) {
		DownloadingUtil.mInstallingPackageName = mInstallingPackageName;
	}
	
	/**
	 * 
	 * @description  发送安装失败的广播
	 * @param packageName
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:17:39
	 *
	 */
	private void sendInstallfailBroadcast(String packageName){
		Intent in = new Intent(
				DownloadTask.ACTION_ON_APP_INSTALLED_FAIL);
		Bundle bundle = new Bundle();
		bundle.putString(
				"packageName",
				packageName);
		in.putExtra("data", bundle);
		context.sendBroadcast(in);
	}
	
	/**
	 * 
	 * @description 设置安装状态文字显示
	 * @param textView
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:18:08
	 *
	 */
	private void setInstallText(TextView textView){
		mInstallingPackageName="";
		if(context instanceof GameDetailActivity){ // 游戏详情界面，则显示安装
			   textView.setText(R.string.down_btn_not_install);
			}else{ // 快速开始则不显示
				if(textView != null)
				textView.setText("");
			}
	}
}
