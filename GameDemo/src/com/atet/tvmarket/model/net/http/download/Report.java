package com.atet.tvmarket.model.net.http.download;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.http.client.HttpResponseException;

import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.app.UrlConstant;
import com.atet.tvmarket.control.mygame.update.HttpApi;
import com.atet.tvmarket.control.mygame.update.Response;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.DataHelper;
import com.atet.tvmarket.model.usertask.UserTaskDaoHelper;
import com.atet.tvmarket.utils.CollectDownCountUtil;

import android.content.SharedPreferences;



public class Report {
	private static final String TAG="REPORT";
	private static final int REPORT_MAX_RETRY_TIME=1;
	// 线程池中的活动线程数量
	private static final int FIX_THREADS = 2;
	
	private final Object queueLock=new Object();
	private ThreadPoolExecutor executor = null;
	private volatile Map<String,Object> queue=new HashMap<String,Object>();
	private static Report INSTANSE=null;
	
	private Report() {
	    if(executor==null){
	        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(FIX_THREADS);
	    }
	}

	public synchronized static Report getInstance() {
	    if(INSTANSE==null){
	        INSTANSE=new Report();
	    }
	    return INSTANSE;
	}
	
	private boolean putInQueue(String gameId){
		synchronized (queueLock) {
			if(queue.containsKey(gameId)){
				return false;
			}
			queue.put(gameId, null);
			return true;
		}
	}
	
	private void removeFromQueue(String gameId){
		synchronized (queueLock) {
			queue.remove(gameId);
		}
	}
	
	public void reportToServer(String gameId){
	    if(gameId==null || gameId.length()<1) {
	        return;
	    }
		if(!putInQueue(gameId)){
			return;
		}
		executor.execute(new ReportRunnable(gameId));
	}
	
	private class ReportRunnable implements  Runnable {
		private String gameId;
		
		public ReportRunnable(String gameId) {
			super();
			this.gameId= gameId;
		}

		public void run() {
			try{
			    confirmSuccessRequest(gameId);
				removeFromQueue(gameId);
				if(queue.size()<=0){

				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	private boolean confirmSuccessRequest(final String gameId) {
		boolean result=false;
		for(int i=0;i<REPORT_MAX_RETRY_TIME && !result;i++){
			result=confirmSuccess(gameId);
			if(result){

				return true;
			}
		}
		return result;
	}
	

	private boolean confirmSuccess(final String gameId) {
		    String postData="{gameId:\""+gameId+"\","+"deviceId:\""+DataHelper.getDeviceInfo().getServerId()+"\",userId:\""+DataFetcher.getUserId()+"\"}";
		    CollectDownCountUtil.addCollectDownCount(gameId);
		    if(gameId.trim().length()<10){
		    	try{
		    		confirmAgain(gameId,postData,UrlConstant.HTTP_THIRD_GAME_ADD_DOWNLOAD_COUNT);
		    	}catch(Exception e){
		    		e.printStackTrace();
		    		try{
		    			confirmAgain(gameId,postData,UrlConstant.HTTP_THIRD_GAME_ADD_DOWNLOAD_COUNT2);
		    		}catch (Exception e1){
		    			e1.printStackTrace();
		    			try{
		    				confirmAgain(gameId,postData,UrlConstant.HTTP_THIRD_GAME_ADD_DOWNLOAD_COUNT3);
		    			}catch(Exception e2){
		    				e2.printStackTrace();
		    			}
		    		}
		    	}
		    }else{
		    	try{
		    		confirmAgain(gameId,postData,UrlConstant.HTTP_GAME_ADD_DOWNLOAD_COUNT);
		    		return true;
		    	}catch(Exception e){
		    		e.printStackTrace();
		    		try {
						confirmAgain(gameId,postData,UrlConstant.HTTP_GAME_ADD_DOWNLOAD_COUNT2);
						return true;
					} catch (Exception e1) {
						e1.printStackTrace();
						try {
							confirmAgain(gameId,postData,UrlConstant.HTTP_GAME_ADD_DOWNLOAD_COUNT3);
							return true;
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
		    	}
		    }
		    return false;
	}
	
	
	/**
	 * @throws HttpResponseException 
	 * @throws IOException 
	 * @throws ProtocolException 
	 * @throws MalformedURLException 
	 * 
	 * @Title: confirmAgain   
	 * @Description: TODO(向服务器提交请求)   
	 * @param: @param gameId  游戏id
	 * @param: @param postData 发送的数据
	 * @param: @param url  发送的服务器地址
	 * @param: @return      
	 * @return: void    
	 * @throws
	 * 
	 * @author wenfuqiang
	 */
	public void  confirmAgain(String gameId,String postData,String url) throws MalformedURLException, ProtocolException, IOException, HttpResponseException {
		Response response=HttpApi.getHttpPost1(url, postData.getBytes());
    	if(response!=null){
    		String responseStr;
			try {
				responseStr = response.asString();
		  		if(responseStr!=null && responseStr.indexOf("code\":0")>0){
		  			int userId = DataFetcher.getUserIdInt();
		  			if(userId > 0 ){
		  				UserTaskDaoHelper.saveGameDownload(BaseApplication.getContext(), gameId, userId, 0, 0);
		  			}
	    		}
			} catch (com.atet.tvmarket.control.mygame.update.HttpResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 
    	}
	}
	
	public void recycle(){
	    try {
	        INSTANSE=null;
	        if(executor!=null){
	            executor.shutdown();
	            executor=null;
	        }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
	}
}
