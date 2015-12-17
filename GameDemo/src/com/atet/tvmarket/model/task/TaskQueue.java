package com.atet.tvmarket.model.task;

import java.io.File;

import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

import com.atet.tvmarket.model.netroid.Netroid;
import com.atet.tvmarket.model.netroid.Network;
import com.atet.tvmarket.model.netroid.RequestQueue;
import com.atet.tvmarket.model.netroid.cache.DiskCache;
import com.atet.tvmarket.model.netroid.stack.HttpClientStack;
import com.atet.tvmarket.model.netroid.stack.HttpStack;
import com.atet.tvmarket.model.netroid.stack.HurlStack;
import com.atet.tvmarket.model.netroid.toolbox.BasicNetwork;
import com.atet.tvmarket.model.netroid.toolbox.FileDownloader;

public class TaskQueue {
	private static RequestQueue mQueue;
	private static FileDownloader mDownloadQueue;

	public static synchronized void recycle() {
		synchronized (TaskQueue.class) {
			if (mQueue != null) {
				mQueue.stop();
			}
			mQueue = null;
		}
	}

	public static RequestQueue getInstanse(Context context) {
		if (mQueue == null) {
			synchronized (TaskQueue.class) {
				if (mQueue == null) {
					File diskCacheDir = new File(context.getCacheDir(),
							"netroid");
					int diskCacheSize = 50 * 1024 * 1024; // 50MB
					mQueue = Netroid.newRequestQueue(context
							.getApplicationContext(), new DiskCache(
							diskCacheDir, diskCacheSize));
				}
			}
		}
		return mQueue;
	}
	
	public static FileDownloader getDownloaderInstance(Context context){
		if (mDownloadQueue== null) {
			synchronized (TaskQueue.class) {
				if (mDownloadQueue== null) {
					RequestQueue mQueue = newDownloadQueue(context.getApplicationContext()); 
					mDownloadQueue = new FileDownloader(mQueue, 1);
				}
			}
		}
		return mDownloadQueue;
	}
	
    private static RequestQueue newDownloadQueue(Context context) {
		int poolSize = 2;

		HttpStack stack;
		String userAgent = "netroid/0";
		try {
			String packageName = context.getPackageName();
			PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
			userAgent = packageName + "/" + info.versionCode;
		} catch (NameNotFoundException e) {
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			stack = new HurlStack(userAgent, null);
		} else {
			// Prior to Gingerbread, HttpUrlConnection was unreliable.
			// See: http://android-developers.blogspot.com/2011/09/androids-http-clients.html
			stack = new HttpClientStack(userAgent);
		}

		Network network = new BasicNetwork(stack, HTTP.UTF_8);
		RequestQueue queue = new RequestQueue(network, poolSize, null);
		queue.start();

        return queue;
    }
}
