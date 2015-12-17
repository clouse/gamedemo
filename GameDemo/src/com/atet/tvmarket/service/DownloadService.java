package com.atet.tvmarket.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.atet.tvmarket.R;
import com.atet.tvmarket.model.entity.MyGameInfo;
import com.atet.tvmarket.model.net.http.download.DownloadTask;
import com.atet.tvmarket.model.net.http.download.FileDownInfo;
import com.atet.tvmarket.view.NewToast;


public class DownloadService extends Service {
    private static final String TAG = "ControllerService";
    private Context mContext;
    private Resources mResource;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
        
    }
     
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Log.i(TAG, "onCreate");
        mContext=this;
        mResource=mContext.getResources();
        regResultBrocastReceiver(downloadReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Log.i(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregResultBrocastReceiver(downloadReceiver);
    }
    
	private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {
	        try {
	            if (intent == null) {
	                return;
	            }
	            String action = intent.getAction();

	            FileDownInfo fileDownInfo = (FileDownInfo) intent.getSerializableExtra(DownloadTask.FILE_DOWN_INFO_KEY);
	            if(fileDownInfo==null){
	                return;
	            }
	            MyGameInfo info = (MyGameInfo) fileDownInfo.getObject();
	            String msg;
	            if (action.equals(DownloadTask.ACTION_ON_DOWNLOAD_FINISH)) {
	                // 下载完成
	                msg=info.getName()+" "+mResource.getText(R.string.service_down_success);
	            } else if (action.equals(DownloadTask.ACTION_ON_DOWNLOAD_ERROR)) {
	                String errMsg=intent.getStringExtra(DownloadTask.ERR_MSG_KEY);
	                if(errMsg!=null && errMsg.length()>0){
	                    msg=errMsg;
	                } else {
	                    msg=info.getName()+" "+mResource.getText(R.string.service_down_fail);
	                }
	            } else {
	                return;
	            }
	            showToast(msg);
	        } catch (Exception e) {
	            // TODO: handle exception
	            e.printStackTrace();
	        }

	    };
	};
	
	public void unregResultBrocastReceiver(BroadcastReceiver receiver){
	    try{
	        mContext.unregisterReceiver(receiver);
	    } catch(Exception e){
	        e.printStackTrace();
	    }
	}
	
	public void regResultBrocastReceiver(BroadcastReceiver receiver){
		IntentFilter filter = new IntentFilter();
		filter.addAction(DownloadTask.ACTION_ON_DOWNLOAD_FINISH);
		filter.addAction(DownloadTask.ACTION_ON_DOWNLOAD_ERROR);
		mContext.registerReceiver(receiver, filter);
	}
	
	private void showToast(final String msg){
	    mHandler.post(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                NewToast.makeToast(mContext, msg, Toast.LENGTH_SHORT).show();
            }
        });
	}
	
	private Handler mHandler=new Handler();
}
