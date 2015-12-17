package com.atet.tvmarket.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

import com.atet.tvmarket.R;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.view.NewToast;

public class VersionUpdate {
    private static final String TAG = "VersionUpdate";
    public static final String ATETGamepadFileName="ATETGamepad.apk";
    public static final String ATETSettingsFileName="ATETSettings.apk";
	private static final String PATH_TURNON_GAMEPAD = "turnon_gamepad";
	private static final String PATH_TURNOFF_GAMEPAD = "turnoff_gamepad";
	public static final String REMOTE_TURNON_GAMEPAD_URI = "content://com.atet.tvgamepad.provider/" + PATH_TURNON_GAMEPAD;
	public static final String REMOTE_TURNOFF_GAMEPAD_URI = "content://com.atet.tvgamepad.provider/" + PATH_TURNOFF_GAMEPAD;


    public void checkVersionUpdate(final Context context,
            final Handler mHandler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                PackageInfo gamepadPkgInfo = AppUtil.getPkgInfoByName(context,
                        Constant.GAMEPAD_PACKAGE_NAME);
                PackageInfo settingsInfo = AppUtil.getPkgInfoByName(context,
                        Constant.SETTING_PACKAGE_NAME);
                int gamepadInstalledVersionCode = (gamepadPkgInfo==null ? -1 : gamepadPkgInfo.versionCode); 
                int settingsInstalledVersionCode = (settingsInfo==null ? -1 : settingsInfo.versionCode);
                if (gamepadInstalledVersionCode < Constant.GAMEPAD_SETTING_VERSION_CODE
                        || settingsInstalledVersionCode < Constant.SETTINGS_VERSION_CODE) {
                    String localDir = context.getFilesDir().getParentFile().getAbsolutePath() + "/";
                    final File gamepadSettingFile=new File(localDir,ATETGamepadFileName);
                    final File settingsFile=new File(localDir,ATETSettingsFileName);
//                    if(!gamepadSettingFile.getParentFile().exists()){
//                        gamepadSettingFile.getParentFile().mkdirs();
//                    }
                    boolean isGamepadNeedToUpdate=false;
                    boolean isSettingsNeedToUpdate=false;
                    boolean isNeedToSleep=false;
                    if(gamepadInstalledVersionCode<Constant.GAMEPAD_SETTING_VERSION_CODE){
//                        if(!gamepadSettingFile.exists()){
                            isGamepadNeedToUpdate=copyAssetFile(context, ATETGamepadFileName, gamepadSettingFile.getAbsolutePath());
                            if(gamepadSettingFile.exists()){
                                try {
                                    Runtime.getRuntime().exec("chmod 644 "+gamepadSettingFile.getAbsolutePath());
                                    isNeedToSleep=true;
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
//                        } else {
//                            isGamepadNeedToUpdate=true;
//                        }
                    }
                    if(settingsInstalledVersionCode<Constant.SETTINGS_VERSION_CODE){
//                        if(!settingsFile.exists()) {
                            isSettingsNeedToUpdate=copyAssetFile(context, ATETSettingsFileName, settingsFile.getAbsolutePath());
                            if(settingsFile.exists()){
                                try {
                                    Runtime.getRuntime().exec("chmod 644 "+settingsFile.getAbsolutePath());
                                    isNeedToSleep=true;
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
//                        } else {
//                            isSettingsNeedToUpdate=true;
//                        }
                    }
                    
                    if(isNeedToSleep){
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    
                    if(isSettingsNeedToUpdate && settingsFile.exists()){
                        final boolean isGamepadNeedToUpdateFinal=isGamepadNeedToUpdate;
                        OnInstallListener onInstallListener=new OnInstallListener() {
                            @Override
                            public void onInstallFinish(int returnCode, ProgressDialog mProDialog, Handler handler) {
                                // TODO Auto-generated method stub
                                if(isGamepadNeedToUpdateFinal && gamepadSettingFile.exists()){
                                    installApk2(context, gamepadSettingFile.getAbsolutePath(), Constant.GAMEPAD_PACKAGE_NAME, "", mProDialog, handler, null,true);
                                }
                            }
                        };
                        installApk(context, Constant.SETTING_PACKAGE_NAME, settingsFile.getAbsolutePath(), mHandler,onInstallListener,!isGamepadNeedToUpdate);
                    } else if(isGamepadNeedToUpdate && gamepadSettingFile.exists()){
                        installApk(context, Constant.GAMEPAD_PACKAGE_NAME, gamepadSettingFile.getAbsolutePath(), mHandler,null,true);
                    }
                } else {
//                    mHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            // TODO Auto-generated method stub
//                                VersionManager vm = new VersionManager(context, DeviceInfoUtil.getDeviceId(BaseApplication.context));
//                                vm.checkVersion(Constant.MARKET_PACKAGE_NAME,
//                                        Constant.MARKET_APP_ID,
//                                        Constant.UPDATE_INTERVAL, true);
//                        }
//                    });
                }
            }
        }).start();
    }

    public static boolean copyAssetFile(Context context, String assetsFileName,
            String savePath) {
        try {
            InputStream is = context.getResources().getAssets().open(assetsFileName);

            File outFile = new File(savePath);
            if (outFile.exists())
                outFile.delete();
            OutputStream out = new FileOutputStream(outFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            is.close();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    private void installApk(final Context context, final String pkgName, final String absPath, final Handler handler, 
            final OnInstallListener onInstallListener, final boolean isCloseDialog) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                ProgressDialog mProDialog=null;
                mProDialog = new ProgressDialog(context);
                mProDialog.setCanceledOnTouchOutside(false);
                mProDialog.setCancelable(false);
                mProDialog.setIndeterminateDrawable(null);
                mProDialog.setMessage(context.getString(R.string.update_installing_other_app));
                mProDialog.show();
                AppUtil.setProDialogFontSize(mProDialog, Constant.DIALOG_MESSAGE_FONT_SIZE);
                AppUtil.setDialogAlpha(mProDialog, Constant.DIALOG_BACKGROUND_ALPHA);

                installApk2(context, absPath, pkgName, "", mProDialog, handler, onInstallListener,isCloseDialog);
            }
        });
	}
    
    public void installApk2(final Context context, final String filePath,
			final String packageName, final String name,
			final ProgressDialog proDialog, Handler handler, OnInstallListener onInstallListener, boolean isCloseDialog) {
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
			DebugTool.warn(TAG, "[installApk2] Replacing package:" + packageName);
		}

		PackageInstallObserver observer = new PackageInstallObserver();
		observer.setName(name);
		observer.setProDialog(proDialog);
		observer.setContext(context);
		observer.setHandler(handler);
		observer.setOnInstallListener(onInstallListener);
		observer.setCloseDialog(isCloseDialog);
		observer.setPath(filePath);
		try {
			mPm.installPackage(mPackageURI, observer, installFlags, packageName);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			if(isCloseDialog){
			    NewToast.makeToast(context, R.string.update_installing_other_app_filed, Toast.LENGTH_SHORT).show();
			}
			if (proDialog != null && proDialog.isShowing()) {
				proDialog.dismiss();
			}
		}
	}
    
    class PackageInstallObserver extends IPackageInstallObserver.Stub {
		public void packageInstalled(final String packageName, final int returnCode) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (isCloseDialog && proDialog != null && proDialog.isShowing()) {
						proDialog.dismiss();
						proDialog = null;
					}
					if (returnCode == PackageManager.INSTALL_SUCCEEDED) {
						if (packageName.equals(Constant.GAMEPAD_PACKAGE_NAME)) {
							handler.postDelayed(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									toggleGamepadFromRemote(context, true);
								}
							}, 1200);
						}
						if(isCloseDialog){
						    if(isCloseDialog){
						        NewToast.makeToast(context, R.string.update_installing_other_app_success, Toast.LENGTH_SHORT).show();
						    }
						}
						if(path!=null){
						    File file=new File(path);
						    if(file.exists()){
						        file.delete();
						    }
						}
					} else {
					    NewToast.makeToast(context, R.string.update_installing_other_app_filed, Toast.LENGTH_SHORT).show();
					}
					if(onInstallListener!=null){
					    onInstallListener.onInstallFinish(returnCode, proDialog, handler);
					}
				}
			});
			
			InstallExceptionUtils.collectGameInstallFailedInfoAsync(BaseApplication.getContext(), Constant.GAMEPAD_PACKAGE_NAME, 
					"TVGamepad", Constant.GAMEPAD_SETTING_VERSION_CODE+"", returnCode);
		}

		private String name;
		private ProgressDialog proDialog;
		private Handler handler;
		private Context context;
		private OnInstallListener onInstallListener;
		private boolean isCloseDialog;
		private String path;

		public boolean isCloseDialog() {
            return isCloseDialog;
        }

        public void setCloseDialog(boolean isCloseDialog) {
            this.isCloseDialog = isCloseDialog;
        }

        public OnInstallListener getOnInstallListener() {
            return onInstallListener;
        }

        public void setOnInstallListener(OnInstallListener onInstallListener) {
            this.onInstallListener = onInstallListener;
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public Handler getHandler() {
            return handler;
        }

        public void setHandler(Handler handler) {
            this.handler = handler;
        }

        public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public ProgressDialog getProDialog() {
			return proDialog;
		}

		public void setProDialog(ProgressDialog proDialog) {
			this.proDialog = proDialog;
		}

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
	}
    
    private boolean toggleGamepadFromRemote(Context context, boolean isOn) {
		DebugTool.info(TAG, "[toggleGamepadFromRemote] isOn:" + isOn);
		try {
			ContentResolver contentResolver = context.getContentResolver();
			ContentValues cv = new ContentValues();
			Uri url;
			if (isOn) {
				// 打开手柄操控
				url = Uri.parse(REMOTE_TURNON_GAMEPAD_URI);
			} else {
				url = Uri.parse(REMOTE_TURNOFF_GAMEPAD_URI);
			}
			int res = contentResolver.update(url, cv, null, null);
			return res != 0;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
    
    public static interface OnInstallListener{
        public void onInstallFinish(int returnCode, ProgressDialog mProDialog, Handler handler);
    }
}
