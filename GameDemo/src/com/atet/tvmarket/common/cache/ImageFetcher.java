/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.atet.tvmarket.common.cache;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.control.mygame.view.CustomClipLoading;
import com.atet.tvmarket.control.mygame.view.GameDetailLoading;
import com.atet.tvmarket.utils.ImageReflectUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * This class wraps up completing some arbitrary long running work when loading
 * a bitmap to an ImageView. It handles things like using a memory and disk
 * cache, running the work in a background thread and setting a placeholder
 * image.
 */
public class ImageFetcher {
	private static final String TAG = "ImageFetcher";
	private int imageId;

	public Handler mHandler = new Handler();
	private static ALog alog = ALog.getLogger(ImageFetcher.class);

	/**
	 * 
	 * running. 获取当前的图片id
	 * 
	 */
	public int getLoadingImage() {
		return imageId;
	}

	/**
	 * Set placeholder bitmap that shows when the the background thread is
	 * running.
	 * 
	 * @param resId
	 */
	public void setLoadingImage(int resId) {
		imageId = resId;
	}

	public void setImageSize(int width, int height) {

	}

	/**
	 * 清除旧版本缓存下来的图片
	 * 
	 * clearOrderImageCache:TODO
	 * 
	 * @author:LiuQin
	 * @date 2014-10-18 下午8:46:09
	 */
	public static void clearOrderImageCache(final Context context) {
		final SharedPreferences sp = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		boolean isClearOrderCache = sp.getBoolean("isClearOrderCache", false);
		if (!isClearOrderCache) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					final String IMAGE_CACHE_DIR = "images";
					try {
						File cache;
						try {
							cache = getExternalCacheDir(context,
									IMAGE_CACHE_DIR);
							if (cache != null) {
								deleteContents(cache);
								alog.info("[clearOrderImageCache] clear external cache end");
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}

						try {
							cache = new File(context.getCacheDir()
									+ File.separator + IMAGE_CACHE_DIR);
							if (cache != null) {
								deleteContents(cache);
								alog.info("[clearOrderImageCache] clear internal cache end");
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}

						sp.edit().putBoolean("isClearOrderCache", true)
								.commit();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	public static void deleteContents(File dir) throws IOException {
		File[] files = dir.listFiles();
		if (files == null) {
			throw new IllegalArgumentException("not a directory: " + dir);
		}
		for (File file : files) {
			if (file.isDirectory()) {
				deleteContents(file);
			}
			if (!file.delete()) {
				throw new IOException("failed to delete file: " + file);
			}
		}
	}

	/**
	 * Get the external app cache directory.
	 * 
	 * @param context
	 *            The context to use
	 * @return The external cache dir
	 */
	public static File getExternalCacheDir(Context context, String uniqueName) {
		if (hasFroyo()) {
			return new File(context.getExternalCacheDir() + File.separator
					+ uniqueName);
		}

		// Before Froyo we need to construct the external cache dir ourselves
		final String cacheDir = "/Android/data/" + context.getPackageName()
				+ "/cache/" + uniqueName;
		return new File(Environment.getExternalStorageDirectory().getPath()
				+ cacheDir);
	}

	public static boolean hasFroyo() {
		// Can use static final constants like FROYO, declared in later versions
		// of the OS since they are inlined at compile time. This is guaranteed
		// behavior.
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	private static boolean isMain() {
		return Looper.getMainLooper().getThread() == Thread.currentThread();
	}

	/**
	 * Load an image specified by the data parameter into an ImageView (override
	 * {@link ImageWorker#processBitmap(Object)} to define the processing
	 * logic). A memory and disk cache will be used if an {@link ImageCache} has
	 * been set using {@link ImageWorker#setImageCache(ImageCache)}. If the
	 * image is found in the memory cache, it is set immediately, otherwise an
	 * {@link AsyncTask} will be created to asynchronously load the bitmap.
	 * 
	 * @param data
	 *            The URL of the image to download.
	 * @param imageView
	 *            The ImageView to bind the downloaded image to.
	 */
	public void loadImage(String data, ImageView imageView, int loadingRes) {
		loadImage(data, imageView, 0, loadingRes);
	}

	/** 加载本地资源图片 */
	public void loadLocalImage(int res, ImageView imageView, int loadingRes) {
		displayImage("drawable://" + res, imageView, 0, null, loadingRes);
	}

	/** 加载本地资源图片 */
	public void loadLocalCornerImage(int res, ImageView imageView, float round, int loadingRes) {
		displayImage("drawable://" + res, imageView, round, null, loadingRes);
	}
	
	/** 加载图片的倒影方法 */
	public void loadReflectImage(String data, ImageView imageView,
			ImageView shadowView, int loadingRes) {
		loadReflectImage(data, imageView, shadowView, 0, loadingRes);
	}

	/** 加载有控件截图倒影的图片 */
	public void loadImage(String data, ImageView imageView,
			ImageView shadowView, View shadowParentView, int loadingRes) {
		loadImage(data, imageView, shadowView, shadowParentView, 0, loadingRes);
	}

	/**
	 * @author wsd
	 * @Description:重载方法，增加圆角处理
	 * @date 2013-2-26 下午4:23:07
	 */
	public void loadImage(final String data, final ImageView imageView,
			final float roundPx, final int loadingRes) {
		alog.info("roundPx:" + roundPx);
		displayImage(data, imageView, roundPx, null, loadingRes);
	}

	/**
	 * @author wsd
	 * @Description:重载方法，增加控件倒影处理
	 * @date 2013-2-26 下午4:23:07
	 */
	private void loadImage(final String data, final ImageView imageView,
			final ImageView shadowView, final View shadowParentView,
			final float roundPx, final int loadingRes) {
		alog.info("loadImage");
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				if (shadowView != null) {
					shadowView.setVisibility(View.INVISIBLE);
				}
			}
		});
		SimpleImageLoadingListener listener = new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(String imageUri, View view,
					Bitmap loadedImage) {
				// TODO Auto-generated method stub
				super.onLoadingComplete(imageUri, view, loadedImage);
				final Bitmap parmBitmap = ImageReflectUtil
						.convertViewToBitmap(shadowParentView);
				if (parmBitmap != null) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							final Bitmap shadowBitmap = ImageReflectUtil
									.createCutReflectedImage(parmBitmap, 0,
											true);
							if (shadowBitmap == null) {
								return;
							}
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									if (shadowView != null) {
										shadowView.setVisibility(View.VISIBLE);
									}
									shadowView.setImageBitmap(shadowBitmap);
								}
							});
						}
					}).start();
				}
			}
		};
		displayImage(data, imageView, roundPx, listener, loadingRes);
	}

	/**
	 * @author wsd
	 * @Description:重载方法，增加图片倒影处理
	 * @date 2013-2-26 下午4:23:07
	 */
	private void loadReflectImage(final String data, final ImageView imageView,
			final ImageView shadowView, final float roundPx,
			final int loadingRes) {
		alog.info("loadReflectImage");
		SimpleImageLoadingListener listener = new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(String imageUri, View view,
					final Bitmap loadedImage) {
				// TODO Auto-generated method stub
				super.onLoadingComplete(imageUri, view, loadedImage);
				if (loadedImage == null) {
					return;
				}
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						final Bitmap shadowBitmap = ImageReflectUtil
								.createCutReflectedImage(loadedImage, 0, false);
						if (shadowBitmap == null) {
							return;
						}
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								shadowView.setImageBitmap(shadowBitmap);
							}
						});
					}
				}).start();
			}
		};
		displayImage(data, imageView, roundPx, listener, loadingRes);
	}

	public void loadImage2(final String data, final ImageView imageView,
			final float roundPx, Context context, String pkgName,
			String actName, final String url, final int loadingRes)
			throws Exception {
		final String uri = "appicon://" + pkgName + "/" + actName;
		if (!TextUtils.isEmpty(url)
				&& (url.startsWith("http://") || url.startsWith("https://"))) {
			SimpleImageLoadingListener listener = new SimpleImageLoadingListener() {
				@Override
				public void onLoadingComplete(String imageUri, View view,
						final Bitmap loadedImage) {
					// TODO Auto-generated method stub
					super.onLoadingComplete(imageUri, view, loadedImage);
					alog.info("load app url icon success");
				}

				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					// TODO Auto-generated method stub
					super.onLoadingFailed(imageUri, view, failReason);
					alog.info("load app url icon failed");
					displayImage(uri, imageView, roundPx, null, loadingRes);
				}
			};
			displayImage(url, imageView, roundPx, listener, loadingRes);
		} else {
			displayImage(uri, imageView, roundPx, null, loadingRes);
		}
	}
	
	
	/**
	 * @author wsd
	 * @Description:重载方法，增加圆角处理
	 * @date 2013-2-26 下午4:23:07
	 */
	public void loadMyGameImage(final String data, final ImageView imageView,
			final float roundPx, final int loadingRes, final int pos
			,final String gameId) {
		alog.info("roundPx:" + roundPx);
		SimpleImageLoadingListener listener = new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(String imageUri, View view,
					final Bitmap loadedImage) {
				// TODO Auto-generated method stub
				super.onLoadingComplete(imageUri, view, loadedImage);
				alog.info("load app url icon success");
//				Message msg = handler.obtainMessage(1, pos, 0);
//				msg.obj = gameId;
//				handler.sendMessage(msg);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				// TODO Auto-generated method stub
				super.onLoadingFailed(imageUri, view, failReason);
				alog.info("load app url icon failed");
//				Message msg = handler.obtainMessage(1, pos, 0);
//				msg.obj = gameId;
//				handler.sendMessage(msg);
			}
		};
		displayImage(data, imageView, roundPx, listener, loadingRes);
	}
	
	public void loadMyGameImage2(final String data, final ImageView imageView,
			final float roundPx, Context context, String pkgName,
			String actName, final String url, final int loadingRes, 
			final int pos, final String gameId)
			throws Exception {
		final String uri = "appicon://" + pkgName + "/" + actName;
		if (!TextUtils.isEmpty(url)
				&& (url.startsWith("http://") || url.startsWith("https://"))) {
			SimpleImageLoadingListener listener = new SimpleImageLoadingListener() {
				@Override
				public void onLoadingComplete(String imageUri, View view,
						final Bitmap loadedImage) {
					// TODO Auto-generated method stub
					super.onLoadingComplete(imageUri, view, loadedImage);
					alog.info("load app url icon success");
//					Message msg = handler.obtainMessage(1, pos, 0);
//					msg.obj = gameId;
//					handler.sendMessage(msg);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					// TODO Auto-generated method stub
					super.onLoadingFailed(imageUri, view, failReason);
					alog.info("load app url icon failed");
					displayImage(uri, imageView, roundPx, null, loadingRes);
//					Message msg = handler.obtainMessage(1, pos, 0);
//					msg.obj = gameId;
//					handler.sendMessage(msg);
				}
			};
			displayImage(url, imageView, roundPx, listener, loadingRes);
		} else {
			displayImage(uri, imageView, roundPx, null, loadingRes);
//			Message msg = handler.obtainMessage(1, pos, 0);
//			handler.sendMessage(msg);
		}
	}

	public void loadGameDetailImage(final String data,final ImageView imageView, final GameDetailLoading clipLoading, final int pos)
			throws Exception {
		
		SimpleImageLoadingListener listener = new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(String imageUri, View view,
					final Bitmap loadedImage) {
				// TODO Auto-generated method stub
				super.onLoadingComplete(imageUri, view, loadedImage);
				alog.info("load app url icon success");
				clipLoading.setIsShowOnGameDetail(true);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				// TODO Auto-generated method stub
				super.onLoadingFailed(imageUri, view, failReason);
				alog.info("load app url icon failed");
				clipLoading.setIsShowOnGameDetail(true);
			}
		};
		displayImage(data, imageView, 0, listener, 0);
	}
	
	public void loadGiftImage(final String url, final ImageView imageView,
			final int loadingRes, final Handler handler){
			SimpleImageLoadingListener listener = new SimpleImageLoadingListener() {
				@Override
				public void onLoadingComplete(String imageUri, View view,
						final Bitmap loadedImage) {
					// TODO Auto-generated method stub
					super.onLoadingComplete(imageUri, view, loadedImage);
					alog.info("load app url icon success");
					handler.sendEmptyMessage(0);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					// TODO Auto-generated method stub
					super.onLoadingFailed(imageUri, view, failReason);
					
				}
			};
			displayImage(url, imageView,0,listener, loadingRes);
	}
	
	/**
	 * @description: 使用universial image loader加载图片 
	 *
	 * @param data 图片地址
	 * @param imageView 
	 * @param roundPx 圆角
	 * @param listener 回调
	 * @param loadingRes 加载中的图片资源
	 * @throws: 
	 * @author: LiuQin
	 * @date: 2015年5月27日 上午11:53:02
	 */
	public void displayImage(final String data, final ImageView imageView,
			final float roundPx, final ImageLoadingListener listener,
			final int loadingRes) {
		alog.info("uri:" + data);
		if (!isMain()) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					ImageLoader.getInstance().displayImage(
							data,
							imageView,
							UILDisplayOpiton.getDefaultPhotoOption(loadingRes,
									(int) roundPx), listener);
				}
			});
		} else {
			ImageLoader.getInstance().displayImage(
					data,
					imageView,
					UILDisplayOpiton.getDefaultPhotoOption(loadingRes,
							(int) roundPx), listener);
		}
	}

}
