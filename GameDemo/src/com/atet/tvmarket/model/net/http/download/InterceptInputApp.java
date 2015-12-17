package com.atet.tvmarket.model.net.http.download;

import java.io.InputStream;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.gesture.Prediction;
import android.net.Uri;
import android.preference.Preference;

import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.app.UrlConstant;
import com.atet.tvmarket.entity.ReqGetInterceptInfo;
import com.atet.tvmarket.entity.RespGetInterceptInfo;
import com.atet.tvmarket.model.DataHelper;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.StreamTool;
import com.google.gson.Gson;

public class InterceptInputApp {
    private static final String TAG = "InterceptInputApp";
    private static final String SP_NAME_CONFIG="config";
    private static final String SP_KEY_LAST_EXE="IMLastExe";
    private static final String SP_KEY_LAST_UPDATE="IMLastUpdate";
    private static volatile boolean mWorking=false;
    public static final int CODE_NO_RESPONDING_DATA = 1401;
    private static final boolean LIMIT_TIME=false;
    
    
    private static class SingletonHolder {
        static final InterceptInputApp INSTANCE = new InterceptInputApp();
    }

    public static InterceptInputApp getInstance(Context context) {
        return SingletonHolder.INSTANCE;
    }
    
    private InterceptInputApp() {
        // TODO Auto-generated constructor stub
    }
    
    public static void downloadInterceptInputApp(final Context context){
        if(mWorking){
            return;
        }
        
        if(!NetUtil.isNetworkAvailable(context, false)){
            return;
        }
        
        final SharedPreferences preferences = context.getSharedPreferences(SP_NAME_CONFIG, Context.MODE_PRIVATE);
        long lastTime= preferences.getLong(SP_KEY_LAST_EXE, 0);
        long elapseTime=(System.currentTimeMillis()-lastTime)/1000-3600;
        if(LIMIT_TIME && elapseTime<=0){
            return;
        }
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                boolean result=false;
                long lastUpdateTime=preferences.getLong(SP_KEY_LAST_UPDATE, 946656000);
                try {
                    mWorking=true;
                    result=getInterceptInputApp(context,lastUpdateTime);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally{
                    if(result){
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putLong(SP_KEY_LAST_EXE, System.currentTimeMillis());
                        editor.putLong(SP_KEY_LAST_UPDATE, System.currentTimeMillis());
                        editor.commit();
                    }
                    mWorking=false;
                }
            }
        }).start();
        return;
    }
    
    private static boolean getInterceptInputApp(Context context, long lastUpdateTime){
        ReqGetInterceptInfo reqInfo= new ReqGetInterceptInfo();
        reqInfo.setDeviceId(DataHelper.getDeviceInfo().getServerId());
        reqInfo.setMinTime(lastUpdateTime);
        
        InputStream is=null;
        int retryTimes=3;
        String postData;
        
        Gson gson=new Gson();
        postData=gson.toJson(reqInfo);
        while(retryTimes-->0){
            try {
                String result;
                String line;

                if(!mWorking){
                    return false;
                }
                is=DownloadThread.httpPostInputStream(UrlConstant.HTTP_GET_INTERCEPT_INPUT_APP,UrlConstant.HTTP_GET_INTERCEPT_INPUT_APP2,UrlConstant.HTTP_GET_INTERCEPT_INPUT_APP3, !NetUtil.isWifiOpen(context), postData.getBytes());
                result=StreamTool.convertStreamToString(is);
         
                if(result!=null && result.length()>0){
                    RespGetInterceptInfo respInfo=gson.fromJson(result, RespGetInterceptInfo.class);
                    if(respInfo!=null){
                        int code=respInfo.getCode();
                        if(code==Constant.CODE_SYS_SUCCESS){
                            String resultData=respInfo.getData();
                            if(resultData==null){                       
                                return false;
                            }
                            String[] pkgNames=resultData.split(",");
                            if(pkgNames==null || pkgNames.length<=0){
                                return false;
                            }
                            
                            if(!mWorking){
                                return false;
                            }
                            ContentResolver contentResolver = context.getContentResolver();
                            ContentValues[] cvs=new ContentValues[pkgNames.length];
                            
                            for (int i = 0; i < pkgNames.length; i++) {
                                String pkgName = pkgNames[i];
                                cvs[i]=new ContentValues();
                                cvs[i].put(FileDownloader.TABLE_COL_PACKAGE_NAME, pkgName);
                            }
                            
                            try {
                                Uri url=Uri.parse(FileDownloader.REMOTE_INTERCEPT_URI);
                                int insertCount=contentResolver.bulkInsert(url, cvs);                
                            } catch (Exception e) {
                                // TODO: handle exception
                                e.printStackTrace();
                                return false;
                            }
                            return true;
                        } else if(code==CODE_NO_RESPONDING_DATA){
                            return false;
                        } else {
                            return false;
                        }
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } 
        return false;
    }
    
    public static void destroy() {
        InterceptInputApp.mWorking = false;
    }
}
