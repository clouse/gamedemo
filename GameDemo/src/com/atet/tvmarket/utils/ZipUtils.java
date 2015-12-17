package com.atet.tvmarket.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;


public class ZipUtils {
    private static final String TAG = "ZipUtils";

    public boolean zipTerminal = false;

    public ZipUtils() {
    }

    public void terminal() {
        zipTerminal = true;
    }

    public boolean isTerminal() {
        return zipTerminal;
    }

    /**
     * 功能：解压 zip 文件，只能解压 zip 文件
     */
    public void unZipApk(Context context, String zipfile, String destDir, String apkDestPath, String packageName,
            OnZipListener listener) {
        // destDir = destDir.endsWith("\\") ? destDir : destDir + "\\";
        byte b[] = new byte[4*1024];
        int length;
        byte[] byteTmp1=new byte[10];
        byte[] byteTmp2=new byte[10];
        InputStream inputStream=null;
        OutputStream outputStream=null;

        try {
            zipTerminal = false;
            if (listener != null) {
                listener.onStart();
            }
            if(zipTerminal){
                if(listener!=null){
                    listener.onFinish();
                }
                return;
            }
            
            boolean isSdUsable=true;
            if(Constant.IS_USE_CACHE_PATH_TO_SAVE){
                //如果不能使用内部存储，可以跑到这里，sd存在
                isSdUsable=DeviceTool.isSDCardReadWritable();
            }
            
            ZipFile zipFile;
            long uncompressFileSize = 0;
            long currentUncompressSize = 0;
            long currentEntrySize;
            int progress = 0;
            int lastProgress = 0;
            boolean isUnApk = false;
            String entryName;
            long loadedLen=0;
            boolean isGamepadGuide=false;
//            RandomAccessFile rfile;

            zipFile = new ZipFile(new File(zipfile));

            @SuppressWarnings("rawtypes")
            Enumeration enumeration = zipFile.entries();
            ZipEntry zipEntry = null;
            while (enumeration.hasMoreElements() && !zipTerminal) {
                zipEntry = (ZipEntry) enumeration.nextElement();
                uncompressFileSize += zipEntry.getSize();
            }
//            DebugTool.info("ZipUtils","uncompressSize:" + uncompressFileSize);

            long leftSize=DeviceTool.getSdAvailableSpace();
            // 解压的大小加上5mb大于所剩的大小提示空间不足
            if(uncompressFileSize>0 && leftSize>=0
                    && (uncompressFileSize + (20 * 1024 * 1024))>leftSize){
                if(listener!=null){
                    listener.onError(null,R.string.manage_zip_sd_space_error);
                }
                return;
            }

            enumeration = zipFile.entries();
            while (enumeration.hasMoreElements() && !zipTerminal) {
                zipEntry = (ZipEntry) enumeration.nextElement();
                // String name = destDir + new
                // String(zipEntry.getName().getBytes("gbk"), "utf-8");
                // File loadFile = new File(name);
                isGamepadGuide=false;
                File loadFile = null;
                entryName = zipEntry.getName().toLowerCase();
                if (!isUnApk && entryName.endsWith(".apk")
                        && !entryName.contains("/")) {
                    loadFile = new File(apkDestPath);
                    isUnApk = true;              
                } else {                
                    if(entryName.equals("tvgamepad/guide.jpg")){
                        //手柄说明图片
                        isGamepadGuide=true;
                    } else if(entryName.equals("tvgamepad/")){
                        continue;
                    } else if(entryName.startsWith("sxhldata/")){
                        continue;
                    } 
                    else if(entryName.equals("tvgamepad/thumbs.db")){
                    	continue;
                    } else {
                        loadFile = new File(destDir + zipEntry.getName());
                        if(!isSdUsable){
                            //存在额外数据，但sd卡不存在
                            if(listener!=null){
                                listener.onError(new Exception("has extra data,need sd card"), R.string.manage_zip_error_need_sd);
                            }
                            return;
                        }
                    }
                }
//                 System.out.println(zipEntry.getName()+" size:"+zipEntry.getSize());
                if (zipEntry.isDirectory()) {
                    if(!loadFile.exists()) {
                        loadFile.mkdirs();
                    }
                } else {
                    if(!isGamepadGuide){
                        currentEntrySize = zipEntry.getSize();
                        loadedLen=loadFile.length();
                        if(loadFile.exists() && loadedLen==currentEntrySize && loadedLen>20){
                            //已经解压的文件不再解压
                            inputStream = zipFile.getInputStream(zipEntry);
                            inputStream.read(byteTmp1, 0, byteTmp1.length);
                            try {
                                inputStream.close(); 
                                inputStream=null;
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                            inputStream = new FileInputStream(loadFile);
                            inputStream.read(byteTmp2, 0, byteTmp2.length);
                            try {
                                inputStream.close(); 
                                inputStream=null;
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                            if(Arrays.equals(byteTmp1, byteTmp2)){
                                currentUncompressSize += currentEntrySize;
                                if (listener != null) {
                                    progress = (int) (currentUncompressSize * 100 / uncompressFileSize);
                                    if (progress != lastProgress) {
                                        lastProgress = progress;
                                        listener.onProcess(progress);
                                    }
                                }
                                continue;
                            }
                        }

                        if (!loadFile.getParentFile().exists()) {
                            loadFile.getParentFile().mkdirs();
                        }
                        
                        outputStream = new FileOutputStream(loadFile);
                    } else {
                        outputStream = saveGamepadGuideImage(context, packageName+".jpg", zipEntry.getSize());
                        if(outputStream==null){
                            continue;
                        }
                    }

                    inputStream = zipFile.getInputStream(zipEntry);
//                    rfile=new RandomAccessFile(loadFile, "rw");
//                    if(loadedLen>0){
//                        inputStream.skip(loadedLen);
//                        rfile.skipBytes((int)loadedLen);
//                    }
                    while ((length = inputStream.read(b)) > 0 && (!zipTerminal)) {
                        outputStream.write(b, 0, length);
//                        rfile.write(b, 0, length);
                        
                        currentUncompressSize += length;
                        if (listener != null) {
                            progress = (int) (currentUncompressSize * 100 / uncompressFileSize);
                            if (progress != lastProgress) {
                                lastProgress = progress;
                                listener.onProcess(progress);
                            }
                        }
                    }
                    try {
                        inputStream.close();
                        outputStream.close();
//                        rfile.close();
                        inputStream=null;
                        outputStream=null;
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                }
//                if (zipTerminal){
//                    break;
//                }
//                currentUncompressSize += currentEntrySize;
//                if (listener != null) {
//                    progress = (int) (currentUncompressSize * 100 / uncompressFileSize);
//                    if (progress != lastProgress) {
//                        lastProgress = progress;
//                        listener.onProcess(progress);
//                    }
//                }
            }
            
            try {
                File apkFile=new File(apkDestPath);
                if(apkFile.exists()){
                    Runtime.getRuntime().exec("chmod 644 "+apkDestPath);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            if (listener != null) {
                listener.onFinish();
            }
        } catch (ZipException e){
            e.printStackTrace();
            if(listener!=null){
                listener.onError(e,R.string.manage_zip_file_format_error);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (listener != null) {
            	if(e.getMessage()!=null){
            		if(e.getMessage().equals("write failed: ENOSPC (No space left on device)")){
            			listener.onError(e,R.string.manage_zip_sd_space_error);
            		}else{
            			listener.onError(e,R.string.manage_zip_error);
            		}
            	}else{
            		listener.onError(e,R.string.manage_zip_error);
            	}
            }
        } finally {
            try {
                if(inputStream!=null){
                    inputStream.close();
                    inputStream=null;
                }
                if(outputStream!=null){
                    outputStream.close();
                    outputStream=null;
                }
            } catch (Exception e2) {
                // TODO: handle exception
            }
        }
    }
    
    private FileOutputStream saveGamepadGuideImage(Context context,String fileName,long fileSize){
        try {
            final Context remoteContext = context.createPackageContext("com.atet.tvgamepad",  Context.CONTEXT_IGNORE_SECURITY);
            if(remoteContext!=null){
                File dir=remoteContext.getFilesDir();
                if(dir==null || !dir.exists() || !dir.canRead() || !dir.canWrite()){
                    createGamepadFileDir(context);
                    //need to wait to chmod complete
                    Thread.sleep(1000);
                } else {
//                    DebugTool.info(TAG, "[saveGamepadGuideImage] dir exist:"+dir.getAbsolutePath());
                }

                File file=remoteContext.getFileStreamPath(fileName);
                if(file.exists() && file.length()==fileSize){
//                    DebugTool.info(TAG, "[saveGamepadGuideImage] :"+fileName+" exist");
                    return null;
                } else {
//                    DebugTool.info(TAG, "[saveGamepadGuideImage] :"+fileName+" not exist");
                }

                FileOutputStream os=remoteContext.openFileOutput(fileName, Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
                return os;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
        return null;
    }
    
    public static final String REMOTE_CREATE_FILE_DIR = "content://com.atet.tvgamepad.provider/create_file_dir";
	private boolean createGamepadFileDir(Context context) {
	    try {
	        ContentResolver contentResolver = context.getContentResolver();
	        ContentValues cv = new ContentValues();
	        Uri url;
	        url = Uri.parse(REMOTE_CREATE_FILE_DIR);
	        int res = contentResolver.update(url, cv, null, null);// 更新的行数
	        return res != 0;
	    } catch (Exception e) {
	        // TODO: handle exception
	        e.printStackTrace();
	    }
	    return false;
	}

    public static interface OnZipListener {
        public void onProcess(int progress);

        public void onStart();

        public void onFinish();

        public void onError(Exception e,int errId);
    }
}
