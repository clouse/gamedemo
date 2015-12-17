package com.atet.tvmarket.model.net.http.download;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.atet.tvmarket.utils.NetUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


/**
 * @ClassName: DownloadThread
 * @Description: 下载线程
 * @author: Liuqin
 * @date 2012-12-10 上午11:06:53
 * 
 */
public class DownloadThread extends Thread {
	private static final String TAG = "DownloadThread";
	//网络连接状态下最多尝试次数
	private static final int MAX_RETRY_TIMES=5;
	private File saveFile;
	private String downUrl;
	private int block;
	/* 下载开始位置 */
	private int threadId = -1;
	private int downLength;
	private FileDownloader downloader;
	private volatile boolean isStopDownload = false;
	private volatile int downloadState=0;
	private Context context;
	private SharedPreferences preferences;

	public DownloadThread(Context context,FileDownloader downloader, String downUrl,
			File saveFile, int block, int downLength, int threadId) {
		this.downUrl = downUrl;
		this.saveFile = saveFile;
		this.block = block;
		this.downloader = downloader;
		this.threadId = threadId;
		this.downLength = downLength;
		this.context=context;
		this.preferences=context.getSharedPreferences("marketApp", Context.MODE_PRIVATE);
	}

	@Override
	public void run() {
		int retryTimes=0;
		downloadState=0;
		byte[] buffer = new byte[1024*8];
		while(retryTimes++<MAX_RETRY_TIMES){
			if (downLength < block) {// 未下载完成
			    if (isStopDownload) {
			        return;
			    }
			    
				RandomAccessFile threadfile=null;
				InputStream inStream=null;
				try {
				    boolean isWifiOpen=NetUtil.isWifiOpen(context);
			        if(!isWifiOpen && NetUtil.isNetworkAvailable(context, true)){
			            boolean result=preferences.getBoolean("wifi",false);
			            if(!result){
			                //已禁止非wifi环境下载
			                this.downloadState=-2;
			                return;
			            }
			        }
				    
					int startPos = block * threadId + downLength;// 开始位置
					int endPos = block * (threadId + 1) - 1;// 结束位置
					if(endPos+1>saveFile.length()){
						endPos=(int)saveFile.length()-1;
					}
					
//					threadfile = new RandomAccessFile(this.saveFile, "rwd");
					threadfile = new RandomAccessFile(this.saveFile, "rw");
					threadfile.seek(startPos);
					
					inStream = httpGetInputStream(downUrl,!NetUtil.isWifiOpen(context),startPos,endPos);
//					byte[] buffer = new byte[1024];
					int offset = 0;
					print("Thread " + this.threadId + " start download from position: "+ startPos + " end position:"+endPos);

					while ((offset = inStream.read(buffer, 0, buffer.length)) != -1) {
						if (isStopDownload) {
							// 暂停退出
							return;
						}

						threadfile.write(buffer, 0, offset);
						downLength += offset;// 每条线程已经下载的文件长度
						downloader.update(this.threadId, downLength, offset);
						
						if(downLength>=block){
						    //该线程下载完
						    break;
						}
					}
					
					if(downLength<block){
					    continue;
					}
					
					print("Thread " + this.threadId + " download finish");
					this.downloadState=1;
					return;
				} catch (FileNotFoundException e){
					print("Thread " + this.threadId + ":" + e);
					e.printStackTrace();
					if(threadfile==null){
						this.downloadState=-1;
						return;
					} else {
						//可能是getInputStream返回的错误
					}
				} catch (Exception e) {
					print("Thread " + this.threadId + ":" + e);
					e.printStackTrace();
					if(e.getMessage()!=null){
						if(e.getMessage().equals("write failed: ENOSPC (No space left on device)")){
							this.downloadState = 4;
							return;
						}
					}
				} finally {
					try{
						if(threadfile!=null){
							threadfile.close();
						}
						if(inStream!=null){
							inStream.close();
						}
					} catch(Exception e){
						e.printStackTrace();
					}
				}
			} else {
				//线程已经下载完成
				this.downloadState=1;
				return;
			}
			
			try {
			    int i;
			    for(i=0;i<10;i++){
			        if (isStopDownload) {
			            return;
			        }
			        
			        if(NetUtil.isNetworkAvailable(context, false)){
			            //网络已连接
			            break;
			        }
			        if(i<5){
			            //20秒等待网络连接
//			            Log.i("md", "sleep 4000 i="+i);
			            Thread.sleep(4000); 
			        } else {
			            //20秒后如果有网络正在连接中，再等待30秒，否则退出
			            if(NetUtil.isNetworkAvailable(context, true)){
//			                Log.i("md", "sleep 6000 i="+i);
			                //网络正在连接中,等待连接完成
			                Thread.sleep(6000); 
			            } else {
			                //网络已断开
//			                Log.i("md", "sleep end,no network i="+i);
			                this.downloadState=-2;
			                return;
			            }
			        }
			    }
			    if(i>=10){
			        break;
			    }
			} catch (Exception e) {
			    // TODO: handle exception
			    e.printStackTrace();
			}
		}
		this.downloadState=-2;
	}

	private static void print(String msg) {
//		Log.i(TAG, msg);
	}

	/**
	 * 已经下载的内容大小
	 * 
	 * @return 如果返回值为-1,代表下载失败
	 */
	public long getDownLength() {
		return downLength;
	}

	/**
	 * 
	 * @Title: setStop
	 * @Description: 结束线程
	 * @param isStop
	 * @throws
	 */
	public void setStop(boolean isStop) {
		this.isStopDownload = isStop;
	}
	
	public int getDownloadState() throws FileNotFoundException,Exception {
		if(downloadState==-1){
			throw new FileNotFoundException();
		} else if(downloadState==-2){
			throw new Exception("Fail to download in Thread");
		} else if(downloadState==4){
			throw new Exception("No space left on device");
		}
		return downloadState;
	}
	
    public static InputStream httpGetInputStream(String url,boolean isNotWifi,int startPos,int endPos) throws Exception{
    	HttpResponse response = httpGetResponse(url,isNotWifi,startPos,endPos);
    	int statusCode=response.getStatusLine().getStatusCode();
    	if(statusCode==HttpStatus.SC_PARTIAL_CONTENT || statusCode==HttpStatus.SC_OK){
    		HttpEntity entity = response.getEntity();
    		if (entity != null) {
    			return response.getEntity().getContent();
    		} else {
    			throw new Exception("Error to getEntity()");
    		}
    	} else {
    		throw new Exception("status code is not 200 -- "+response.getStatusLine().getStatusCode());
    	}
    }
    
    public static HttpResponse httpGetResponse(String url,boolean isNotWifi,int startPos,int endPos) throws Exception{
    	HttpParams params = new BasicHttpParams();

    	HttpConnectionParams.setConnectionTimeout(params, 30000);
    	HttpConnectionParams.setSoTimeout(params, 30000);
    	HttpConnectionParams.setTcpNoDelay(params, true);

    	HttpClient httpclient = new DefaultHttpClient(params);
    	httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

    	if(isNotWifi){
    		String host = android.net.Proxy.getDefaultHost();
    		int port = android.net.Proxy.getDefaultPort();
    		if (host != null && port != -1) {
    			HttpHost httpHost=new HttpHost(host,port);
    			httpclient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, httpHost);
//    			DebugTool.debug(TAG, "host:"+host+" port:"+port);
    		}
    	} else {
    	}

//    	DebugTool.info("uri encode:", NetUtil.getValidUri(url).toString());
    	HttpGet get = new HttpGet(NetUtil.getValidUri(url));
    	get.setHeader("User-Agent","Mozilla/5.0 ( compatible ) ");
    	get.setHeader("Charset", "UTF-8");
    	get.setHeader("Accept-Language", "zh-CN");
    	if(endPos>0 && endPos>=startPos){
    		get.setHeader("Range", "bytes=" + startPos + "-" + endPos);
    	}
    	get.setHeader("Accept","*/*");
    	get.setHeader("Referer", url);
    	get.setHeader("Connection", "close");	
    	return httpclient.execute(get); 
    }

    public static int httpGetFileLength(String url,boolean isNotWifi) throws Exception{
    	HttpResponse response = httpGetResponse(url, isNotWifi, 0, 0);
        int statusCode=response.getStatusLine().getStatusCode();
        if(statusCode!=HttpStatus.SC_PARTIAL_CONTENT && statusCode!=HttpStatus.SC_OK){
            throw new Exception("status code is not 200 -- "+response.getStatusLine().getStatusCode());
        }
    	Header[] headers=response.getHeaders("Content-Length");
    	return Integer.parseInt(headers[0].getValue());
    }
    
    public static InputStream httpPostInputStream(String url,boolean isNotWifi,byte[] postData) throws Exception{
    	HttpResponse response = httpGetResponse(url,isNotWifi,0,0,false,postData);
    	int statusCode=response.getStatusLine().getStatusCode();
    	if(statusCode==HttpStatus.SC_PARTIAL_CONTENT || statusCode==HttpStatus.SC_OK){
    		HttpEntity entity = response.getEntity();
    		if (entity != null) {
    			return response.getEntity().getContent();
    		} else {
    			throw new Exception("Error to getEntity()");
    		}
    	} else {
    		throw new Exception("status code is not 200 -- "+response.getStatusLine().getStatusCode());
    	}
    }
    
    
    /***
     * @author wenfuqiang
     * @description 
     * @param firstUrl  优先级最高的url
     * @param secUrl    优先级第二高的url
     * @param thirdUrl  优先级第三高的url
     */
    public static InputStream httpPostInputStream(String firstUrl,String secUrl,String thirdUrl,boolean isNotWifi,byte[] postData) throws Exception{
    	HttpResponse response = httpGetResponse(firstUrl,isNotWifi,0,0,false,postData);
    	int statusCode=response.getStatusLine().getStatusCode();
    	if(statusCode==HttpStatus.SC_PARTIAL_CONTENT || statusCode==HttpStatus.SC_OK){
    		HttpEntity entity = response.getEntity();
    		if (entity != null) {
    			return response.getEntity().getContent();
    		} else {
    			throw new Exception("Error to getEntity()");
    		}
    	} else {
    		HttpResponse response2 = httpGetResponse(secUrl,isNotWifi,0,0,false,postData);
	    	int statusCode2=response.getStatusLine().getStatusCode();
	    	if(statusCode2==HttpStatus.SC_PARTIAL_CONTENT || statusCode2==HttpStatus.SC_OK){
	    		HttpEntity entity2 = response2.getEntity();
	    		if (entity2 != null) {
	    			return response2.getEntity().getContent();
	    		} else {
	    			throw new Exception("Error to getEntity()");
	    		}
	    	} else {
	    		HttpResponse response3 = httpGetResponse(thirdUrl,isNotWifi,0,0,false,postData);
    	    	int statusCode3=response3.getStatusLine().getStatusCode();
    	    	if(statusCode3==HttpStatus.SC_PARTIAL_CONTENT || statusCode3==HttpStatus.SC_OK){
    	    		HttpEntity entity3 = response3.getEntity();
    	    		if (entity3 != null) {
    	    			return response3.getEntity().getContent();
    	    		} else {
    	    			throw new Exception("Error to getEntity()");
    	    		}
    	    	} else {
    	    		throw new Exception("status code is not 200 -- "+response.getStatusLine().getStatusCode());
    	    	}
	    	}
    	}
    }
    
    public static HttpResponse httpGetResponse(String url,boolean isNotWifi,int startPos,int endPos,boolean isGet,byte[] postData) throws Exception{
    	HttpParams params = new BasicHttpParams();

    	HttpConnectionParams.setConnectionTimeout(params, 20000);
    	HttpConnectionParams.setSoTimeout(params, 20000);
    	HttpConnectionParams.setTcpNoDelay(params, true);

    	HttpClient httpclient = new DefaultHttpClient(params);
    	httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

    	if(isNotWifi){
    		String host = android.net.Proxy.getDefaultHost();
    		int port = android.net.Proxy.getDefaultPort();
    		if (host != null && port != -1) {
    			HttpHost httpHost=new HttpHost(host,port);
    			httpclient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, httpHost);
//    			DebugTool.debug(TAG, "host:"+host+" port:"+port);
    		}
    	} else {
    	}

//    	DebugTool.info("uri encode:", NetUtil.getValidUri(url).toString());
//    	HttpGet get = new HttpGet(NetUtil.getValidUri(url));
    	HttpRequestBase httpMethod=null;
    	if(isGet){
    	    httpMethod = new HttpGet(url);
    	} else {
    	    httpMethod = new HttpPost(url);
    	    if(postData!=null){
    	        ByteArrayEntity entity = new ByteArrayEntity(postData);
    	        ((HttpPost)httpMethod).setEntity(entity);
    	    }
    	}
    	httpMethod.setHeader("User-Agent","Mozilla/5.0 ( compatible ) ");
    	httpMethod.setHeader("Charset", "UTF-8");
    	httpMethod.setHeader("Accept-Language", "zh-CN");
    	if(endPos>0 && endPos>=startPos){
    		httpMethod.setHeader("Range", "bytes=" + startPos + "-" + endPos);
    	}
    	httpMethod.setHeader("Accept","*/*");
    	httpMethod.setHeader("Referer", url);
    	httpMethod.setHeader("Connection", "close");	
    	return httpclient.execute(httpMethod); 
    }
}
