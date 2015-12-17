package com.atet.api.pay.ui.service;

import java.util.HashMap;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.atet.api.pay.ui.service.IAtetPayService.Stub;
import com.atet.common.logging.ALog;

public class AtetPayService extends Service{
    private static final String TAG = "AtetPayService";
    public static final String KEY_PAY_RESULT="PayResult";
    public static final String KEY_LOGIN_RESULT="LoginResult";
    private Object mLock=new Object();
    
    public static boolean mIsRunning;
    
    private HashMap<Integer, PayInstanse> mTaskMap=new HashMap<Integer, PayInstanse>();
    private int mId=0;
    
    ALog alog = ALog.getLogger(AtetPayService.class);
    
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mIsRunning=true;
        alog.debug("");
    };
    
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mIsRunning=false;
        alog.debug("");
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        alog.debug("");
        if(intent!=null){
            int id=intent.getIntExtra(AtetPayService.KEY.PayId, 0);
            if(id>0){
                PayInstanse payInstanse=mTaskMap.remove(id);
                if(payInstanse!=null){
                    String result=intent.getStringExtra(KEY_PAY_RESULT);
                    if(result==null){
                        result=intent.getStringExtra(KEY_LOGIN_RESULT);
                    }
                    payInstanse.setResult(result);
                    payInstanse.releaseLock();
                }
                
                if (mTaskMap.size() <= 0) {
//                    TaskQueue.recycle();
                    stopSelf();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return mBinder;
    }
    
    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        return super.onUnbind(intent);
    }

    private IAtetPayService.Stub mBinder = new Stub() {
        
        @Override
        public String startPay(String params, IRemoteServiceCallback callback) throws RemoteException {
            // TODO Auto-generated method stub
        	alog.debug("params:"+params);
            if(TextUtils.isEmpty(params) || callback==null){
                return null;
            }
            
            PayInstanse payInstanse=null;
            synchronized (mLock) {
                int id=getId();
                payInstanse=new PayInstanse(id);
                mTaskMap.put(id, payInstanse);
            }
            
            return payInstanse.startPay(params, callback);
        }

        @Override
        public String login(String params, IRemoteServiceCallback callback) throws RemoteException {
            // TODO Auto-generated method stub
        	alog.debug("params:"+params);
            if(TextUtils.isEmpty(params) || callback==null){
                return null;
            }
            
            PayInstanse payInstanse=null;
            synchronized (mLock) {
                int id=getId();
                payInstanse=new PayInstanse(id);
                mTaskMap.put(id, payInstanse);
            }
            
            return payInstanse.startLogin(params, callback);
        }
    };
    
    private synchronized int getId(){
        return ++mId;
    }
    
    class PayInstanse{
        private ConditionVariable cv=new ConditionVariable();
        private String result=null;
        private int id;

        public PayInstanse(int id) {
            // TODO Auto-generated constructor stub
            this.id=id;
        }
        
        public String startPay(String params, IRemoteServiceCallback mCallback){
            try {
                Bundle bundle=new Bundle();
                bundle.putString(AtetPayService.KEY.PayParams, params);
                bundle.putInt(AtetPayService.KEY.PayId, id);
                mCallback.startActivity(AtetPayService.this.getPackageName(), InitActivity.class.getName(), -1, bundle);
                cv.block();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            } finally{
                cv.close();
                cv=null;
            }
            return result;
        }
        
        public String startLogin(String params, IRemoteServiceCallback mCallback){
            try {
                Bundle bundle=new Bundle();
                bundle.putString(AtetPayService.KEY.LoginParams, params);
                bundle.putInt(AtetPayService.KEY.PayId, id);
                mCallback.startActivity(AtetPayService.this.getPackageName(), InitActivity.class.getName(), -1, bundle);
                cv.block();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            } finally{
                cv.close();
                cv=null;
            }
            
            return result;
        }
                
        public ConditionVariable getCv() {
            return cv;
        }

        public void setCv(ConditionVariable cv) {
            this.cv = cv;
        }
        
        public void releaseLock(){
            cv.open();
        }

        public void setResult(String result) {
            this.result = result;
        }
    }
    
    public static class KEY {
        public static final String PayParams="PayParams";
        public static final String LoginParams="LoginParams";
        public static final String PayId="PayId";
        
        public static final String LoginInfo="LoginInfo";
     }
}
