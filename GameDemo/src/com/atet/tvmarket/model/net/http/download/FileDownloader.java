package com.atet.tvmarket.model.net.http.download;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.atet.tvmarket.R;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.app.UrlConstant;
import com.atet.tvmarket.entity.ConfigInfo;
import com.atet.tvmarket.entity.DownloadAddrInfo;
import com.atet.tvmarket.entity.GameConfigInfo;
import com.atet.tvmarket.entity.Group;
import com.atet.tvmarket.entity.KeyConfigInfo;
import com.atet.tvmarket.entity.ReqGetConfiigInfo;
import com.atet.tvmarket.entity.RespGetConfigInfo;
import com.atet.tvmarket.entity.RespGetConfigInfo.AddrInfo;
import com.atet.tvmarket.entity.TrueDownAddressInfo;
import com.atet.tvmarket.model.DataHelper;
import com.atet.tvmarket.model.database.PersistentSynUtils;
import com.atet.tvmarket.model.entity.MyGameInfo;
import com.atet.tvmarket.model.netroid.Exception.DownloadErrorException;
import com.atet.tvmarket.model.netroid.Exception.NetWorkNotFoundException;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.DeviceTool;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.StreamTool;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


/**
 * @ClassName: FileDownloader
 * @Description: 文件下载类，负责管理单个文件中的下载线程
 * @author: Liuqin
 * @date 2012-12-10 上午11:19:58
 * 
 */
public class FileDownloader {
	private static final String TAG = "FileDownloader";
	private Context context;
	// private FileService fileService;
	/* 已下载文件长度，即各线程下载数据长度的总和 */
	private int downloadSize = 0;
	/* 线程数 */
	private DownloadThread[] threads;
	/* 本地保存文件 */
	private File saveFileTmp;
	/* 每条线程下载的长度 */
	private int block;

	// 是否停止下载标志
	private volatile boolean isStopDownload = false;
	private FileDownInfo fileDownInfo = null;
	private FileDownDAO fileDownDAO;
	/* 缓存各线程下载的长度 */
	private Map<Integer, Integer> threadInfos = new ConcurrentHashMap<Integer, Integer>();
	private DownloadListenner listenner;

	// for test download speed
	private static final boolean isTestSpeed = false;
	private long startDownloadSize = 0;
	private long startDownloadTimes = 0;
	private List<String> urlList;

	/**
	 * 获取线程数
	 */
	public int getThreadSize() {
		return threads.length;
	}

	/**
	 * 获取文件大小
	 * 
	 * @return
	 */
	public int getFileSize() {
		return fileDownInfo == null ? -1 : fileDownInfo.getFileSize();
	}

	/**
	 * @Title: getDownLen
	 * @Description: 获取已下载的文件长度
	 * @return
	 * @throws
	 */
	public int getDownLen() {
		return fileDownInfo == null ? -1 : fileDownInfo.getDownLen();
	}

	/**
	 * @Title: update
	 * @Description: 更新下载大小
	 * @param threadId
	 *            线程id
	 * @param downLen
	 *            该线程下载的总长度
	 * @param offset
	 *            该线程最后下载的位置
	 * @throws
	 */
	// protected synchronized void update(int threadId, int downLen ,int offset)
	// {
	protected void update(int threadId, int downLen, int offset) {
		this.threadInfos.put(threadId, downLen);
		downloadSize += offset;
		fileDownInfo.setDownLen(downloadSize);
	}

	/**
	 * @Title: updateProgressDB
	 * @Description: 更新线程下载信息到数据库
	 * @throws
	 */
	protected void updateProgressDB() {
		fileDownDAO.updateThreadInfos(fileDownInfo.getFileId(), threadInfos);
	}

	public FileDownloader(Context context) {
		this.context = context;
	}

	/**
	 * @Title: init
	 * @Description: 初始化下载
	 * @param downInfo
	 * @param threadNum
	 * @return
	 * @throws
	 */
	public boolean init(FileDownInfo downInfo, int threadNum,
			DownloadListenner listenner, int downType) {

		if (!DeviceTool.isSDCardReadWritable()
				&& !downInfo
						.getLocalDir()
						.toLowerCase()
						.startsWith(
								context.getFilesDir().getParentFile()
										.getAbsolutePath())) {

			// &&
			// !downInfo.getLocalDir().toLowerCase().startsWith(context.getCacheDir().getAbsolutePath())){
			// SD卡没有挂载，也不能使用data目录
			throw new DownloadErrorException(context.getResources().getString(
					R.string.manage_down_error_need_to_reinsert_sd));
		}

		try {
			// 获取游戏的手柄配置文件
			MyGameInfo myGameInfo = (MyGameInfo) downInfo.getObject();
			if (myGameInfo != null) {
				if (!getConfigFile(context, myGameInfo.getPackageName())) {
					// if(isStopDownload){
					// return true;
					// }
					// throw new Exception("Error to get gamepad config file");
				}
			}

			this.listenner = listenner;
			// File target=new
			// File(downInfo.getLocalDir(),downInfo.getLocalFilename());
			// if(target.exists()){
			//
			// onDownloadFinish();
			// return;
			// }

			// 获取下载地址
			urlList = new ArrayList<String>();
			String localUrl = null;
			TaskResult<Group<DownloadAddrInfo>> resultList;
			TaskResult<TrueDownAddressInfo> taskResultList;
			for (int i = 0; i < 3; i++) {
				try {
					// if(downType==Constant.DOWN_FROM_LOCAL){
					// resultList=HttpApi.getList(UrlConstant.HTTP_GAME_DOWNLOAD_ADDRESS,
					// DownloadAddrInfo.class, getJson(downInfo.getFileId()));
					// if(resultList!=null &&
					// resultList.getCode()==TaskResult.OK){
					// Group<DownloadAddrInfo> group=resultList.getData();
					// DebugTool.info(TAG, "url group:"+group.toString());
					// for(DownloadAddrInfo downloadAddrInfo:group){
					// DebugTool.debug(TAG,
					// "downloadAddrInfo="+downloadAddrInfo);
					// if(downloadAddrInfo.getType()==1){
					// localUrl=downloadAddrInfo.getDownloadAddress();
					// } else {
					// urlList.add(downloadAddrInfo.getDownloadAddress());
					// }
					// }
					// break;
					// }
					// }else
					if (downType == Constant.DOWN_FROM_LOCAL) {
						// 从网盘直接获取网址下载
						localUrl = downInfo.getDownUrl();
					}
					// else if(downType==Constant.DOWN_FROM_THIRD){
					// //根据中间下载地址获取真实下载地址
					// // HttpReqParams params = new HttpReqParams();
					// // params.setUrl(downInfo.getDownUrl());
					// // params.setDownToken(downInfo.getDownToken());
					// //
					// taskResultList=HttpApi.getObject(UrlConstant.HTTP_GAME_BOX_TRUE_DOWNLOAD_ADDRESS,UrlConstant.HTTP_GAME_BOX_TRUE_DOWNLOAD_ADDRESS2,UrlConstant.HTTP_GAME_BOX_TRUE_DOWNLOAD_ADDRESS3,
					// TrueDownAddressInfo.class, params.toJsonParam());
					// // if(taskResultList!=null &&
					// taskResultList.getCode()==TaskResult.OK){
					// // TrueDownAddressInfo
					// trueDownAddressInfo=taskResultList.getData();
					// // localUrl=trueDownAddressInfo.getUrl();
					// // break;
					// // }
					// localUrl=downInfo.getDownUrl();
					// }
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			if (localUrl != null) {
				urlList.add(localUrl);
			}
			if (urlList.size() <= 0) {
				throw new RuntimeException(
						"url list:0,Error to get real downlaod address");
			}


			fileDownDAO = new FileDownDAO(context);
			FileDownInfo info = fileDownDAO.findByFileId(downInfo.getFileId());
			boolean isExist = false;
			if (info != null && info.getFileId().equals(downInfo.getFileId())) {
				String localFileName = downInfo.getLocalFilename();
				if (localFileName == null || localFileName.length() <= 0) {
					// localFileName = Uri.encode(downInfo.getDownUrl());
					localFileName = Uri.encode(downInfo.getFileId());
					downInfo.setLocalFilename(localFileName);
				}

				if (// info.getDownUrl().equals(downInfo.getDownUrl())&&
				info.getLocalDir().equals(downInfo.getLocalDir())
						&& localFileName.equals(info.getLocalFilename())) {
					isExist = true;

					if (info.getDownLen() > 0) {
						File targetFile = new File(info.getLocalDir(),
								localFileName
										+ DownloadTask.DOWNLOADING_EXT_NAME);
						if (!targetFile.exists()
								|| targetFile.length() != info.getFileSize()) {
							// 文件不存在或长度不一致
							isExist = false;
						}
					}
				}

				if (!isExist) {
					fileDownDAO.delete(info);
				}
			}

			if (isExist) {
				// 数据库存在该文件下载信息
				this.fileDownInfo = info;
				this.fileDownInfo.setExtraData(downInfo.getExtraData());
				this.fileDownInfo.setThreadId(downInfo.getThreadId());
				this.fileDownInfo.setObject(downInfo.getObject());
			} else {
				int size = 0;
				String url;
				for (int i = 0; i < urlList.size(); i++) {
					url = urlList.get(i);
					for (int j = 0; j < 3 && size <= 0; j++) {
						try {
							size = DownloadThread.httpGetFileLength(url,
									!NetUtil.isWifiOpen(context));// 根据响应获取文件大小
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
					if (size <= 0) {
						urlList.remove(i--);
					} else {
						break;
					}
				}
				if (size <= 0) {
					throw new RuntimeException("Unknown file size");
				} else {
					downInfo.setFileSize(size);
					if (size <= 10 * 1024) {
						// 小于10K的文件只用一个线程
						threadNum = 1;
					}
				}
				// if (downInfo.getLocalFilename().length() <= 0) {
				// downInfo.setLocalFilename(Uri.encode(downInfo.getDownUrl()));
				// }
				downInfo.setDownUrl(urlList.get(0));
				downInfo.setDownLen(0);
				downInfo.getThreadsInfo().clear();
				this.fileDownInfo = downInfo;
			}

			this.threadInfos = fileDownInfo.getThreadsInfo();
			File dir = new File(fileDownInfo.getLocalDir());
			if (!dir.exists()) {
				dir.mkdirs();
			}
			this.saveFileTmp = new File(dir, fileDownInfo.getLocalFilename()
					+ DownloadTask.DOWNLOADING_EXT_NAME);

			// 计算每条线程下载的数据长度
			this.block = (fileDownInfo.getFileSize() % threadNum) == 0 ? fileDownInfo
					.getFileSize() / threadNum
					: fileDownInfo.getFileSize() / threadNum + 1;
			this.threads = new DownloadThread[threadNum];
			if (threadInfos.size() != threadNum) {
				threadInfos.clear();
				for (int i = 0; i < threadNum; i++) {
					// 初始化每条线程已经下载的数据长度为0
					threadInfos.put(i, 0);
				}
				// 保存文件下载信息到数据库
				fileDownInfo.setThreadCount(threadNum);
				fileDownInfo.setDownLen(0);
				fileDownDAO.save(fileDownInfo);
			}
			this.downloadSize = fileDownInfo.getDownLen();
			onDownloadSized();
			return true;
		} catch (Exception e) {
			if (fileDownDAO != null) {
				fileDownDAO.closeDB();
			}
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @Title: download
	 * @Description: 开始下载
	 * @param listener
	 *            下载状态监听器
	 * @return
	 * @throws Exception
	 */
	public int download() throws Exception {
		// String tmp=urlList.remove(0);
		// tmp=tmp.replaceAll("https", "http");
		// urlList.add(0,tmp);

		if (isStopDownload) {
			return downloadSize;
		}

		boolean isNeedToContinue = false;
		for (int k = 0; k < urlList.size(); k++) {
			try {
				isNeedToContinue = false;
				// 记录开始位置
				startDownloadSize = downloadSize;
				startDownloadTimes = System.currentTimeMillis();

				if (!saveFileTmp.exists()
						|| saveFileTmp.length() != fileDownInfo.getFileSize()) {
					RandomAccessFile randOut = new RandomAccessFile(
							this.saveFileTmp, "rwd");
					// FileChannel fc = randOut.getChannel();
					// FileLock lock = fc.tryLock();
					// if(lock==null){
					// randOut.close();
					// throw new
					// RuntimeException("Error to get save file lock");
					// }
					if (fileDownInfo.getFileSize() > 0) {
						randOut.setLength(fileDownInfo.getFileSize());
					}
					// lock.release();
					randOut.close();
				}

				if (isStopDownload) {
					return downloadSize;
				}

				// String url=fileDownInfo.getDownUrl();
				String url = urlList.get(k);
				for (int i = 0; i < this.threads.length; i++) {// 开启线程进行下载
					int downLength = threadInfos.get(i);
					if (downLength < this.block
							&& downloadSize < fileDownInfo.getFileSize()) {// 判断线程是否已经完成下载,否则继续下载
						this.threads[i] = new DownloadThread(context, this,
								url, this.saveFileTmp, this.block,
								threadInfos.get(i), i);
						this.threads[i].setPriority(7);
						this.threads[i].start();
					} else {
						this.threads[i] = null;
					}
				}

				FileDownInfo info = new FileDownInfo();
				info.setFileId(fileDownInfo.getFileId());
				info.setFileSize(fileDownInfo.getFileSize());
				info.setLocalFilename(fileDownInfo.getLocalFilename());
				info.setFileType(fileDownInfo.getFileType());
				Intent downProgressIntent = new Intent(
						DownloadTask.ACTION_ON_DOWNLOAD_PROGRESS);

				boolean notFinish = true;// 下载未完成
				final int filesize = fileDownInfo.getFileSize();
				int lastDownPercent = -1;
				int currentDownPercent = 0;
				setThreadId(Thread.currentThread().getId());
				// DebugTool.warn(TAG,
				// "start down,threadId:"+Thread.currentThread().getId());

				while (notFinish) {// 循环判断所有线程是否完成下载
					Thread.sleep(900);
					notFinish = false;// 假定全部线程下载完成
					for (int i = 0; i < threads.length; i++) {
						if (threads[i] != null
								&& threads[i].getDownloadState() != 1) {// 如果发现线程未完成下载
							notFinish = true;// 设置标志为下载没有完成
							break;
						}
					}

					if (isStopDownload) {
						// DebugTool.debug(TAG,
						// "isStopDownload:true,threadId:"+Thread.currentThread().getId());
						for (int i = 0; i < threads.length; i++) {
							if (threads[i] != null) {
								threads[i].setStop(true);
							}
						}
						// onDownloadStop(context,fileDownInfo,listenner);
						// DebugTool.debug(TAG,
						// "return,ThreadId:"+Thread.currentThread().getId());
						return downloadSize;
					}

					currentDownPercent = (int) ((long) downloadSize * 100 / (long) filesize);
					if (currentDownPercent != lastDownPercent) {
						lastDownPercent = currentDownPercent;
						updateProgressDB();
						info.setDownLen(downloadSize);
						onDownloadProgress(info, downProgressIntent);
						// DebugTool.info(TAG,
						// "pro:"+currentDownPercent+" threadId:"+Thread.currentThread().getId());
					}

					if (isTestSpeed) {
						long elapseTime = ((System.currentTimeMillis() - startDownloadTimes) / 1000);
						if (elapseTime > 0) {
						}
					}
				}

				if (downloadSize != filesize) {
					// 下载长度不匹配
					throw new Exception("file size not match");
				}

				// 文件改名
				this.saveFileTmp.renameTo(new File(fileDownInfo.getLocalDir(),
						fileDownInfo.getLocalFilename()));
				fileDownDAO.delete(fileDownInfo);
				onDownloadFinish();
			} catch (FileNotFoundException e) {
				try {
					e.printStackTrace();
					File file = new File(fileDownInfo.getLocalDir(),
							fileDownInfo.getLocalFilename());
					if (file.exists()
							&& file.length() == fileDownInfo.getFileSize()) {
						// 文件已经下载完成
						fileDownDAO.delete(fileDownInfo);
						onDownloadFinish();
					} else {
						throw e;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					throw ex;
				}
			} catch (NetWorkNotFoundException e) {
				throw e;
			} catch (Exception e) {
				e.printStackTrace();
				if (k < urlList.size() - 1) {
					// 可能只是该网盘不能下载，尝试其它网盘
					isNeedToContinue = true;
					continue;
				}
				throw e;
			} finally {
				try {

					if (threads != null) {
						for (int i = 0; i < threads.length; i++) {
							if (threads[i] != null) {
								threads[i].setStop(true);
							}
						}
					}
					if (!isNeedToContinue) {
						fileDownDAO.closeDB();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return this.downloadSize;
	}

	/**
	 * 获取Http响应头字段
	 * 
	 * @param http
	 * @return
	 */
	public static Map<String, String> getHttpResponseHeader(
			HttpURLConnection http) {
		Map<String, String> header = new LinkedHashMap<String, String>();
		for (int i = 0;; i++) {
			String mine = http.getHeaderField(i);
			if (mine == null)
				break;
			header.put(http.getHeaderFieldKey(i), mine);
		}
		return header;
	}

	/**
	 * 打印Http头字段
	 * 
	 * @param http
	 */
	public static void printResponseHeader(HttpURLConnection http) {
		Map<String, String> header = getHttpResponseHeader(http);
		for (Map.Entry<String, String> entry : header.entrySet()) {
			String key = entry.getKey() != null ? entry.getKey() + ":" : "";
			print(key + entry.getValue());
		}
	}

	private static void print(String msg) {
		Log.i(TAG, msg);
	}

	public void setStopDownload(boolean isStopDownload) {
		this.isStopDownload = isStopDownload;
	}

	private void onDownloadFinish() {
		if (listenner != null) {
			listenner.onDownloadFinish(fileDownInfo);
		}
		MyGameInfo info=(MyGameInfo)fileDownInfo.getObject();
		if(info!=null){
		    info.setState(Constant.GAME_STATE_NOT_INSTALLED);
		}
		Group<MyGameInfo> infos = PersistentSynUtils.getModelList(
				MyGameInfo.class, " packageName='" + info.getPackageName()
						+ "'");
		if (infos != null && infos.size() > 0) {
		   MyGameInfo updateInfo = infos.get(0);
		   updateInfo.setState(Constant.GAME_STATE_NOT_INSTALLED);
		   PersistentSynUtils.update(updateInfo); 
		}
		Intent downIntent = new Intent(DownloadTask.ACTION_ON_DOWNLOAD_FINISH);
		downIntent.putExtra(DownloadTask.FILE_DOWN_INFO_KEY, fileDownInfo);
		context.sendBroadcast(downIntent);
		Report.getInstance().reportToServer(fileDownInfo.getFileId());
	}

	private void onDownloadProgress(FileDownInfo info, Intent downProgressIntent) {
		downProgressIntent.putExtra(DownloadTask.FILE_DOWN_INFO_KEY, info);
		context.sendBroadcast(downProgressIntent);

		if (listenner != null) {
			listenner.onDownloadProgress(info);
		}
	}

	private void onDownloadSized() {
		Intent downIntent = new Intent(DownloadTask.ACTION_ON_DOWNLOAD_SIZE);
		downIntent.putExtra(DownloadTask.FILE_DOWN_INFO_KEY, fileDownInfo);
		context.sendBroadcast(downIntent);

		if (listenner != null) {
			listenner.onDownloadSized(fileDownInfo);
		}
	}

	public static void onDownloadStop(Context context,
			FileDownInfo fileDownInfo, DownloadListenner listenner) {
		Intent downIntent = new Intent(DownloadTask.ACTION_ON_DOWNLOAD_STOP);
		downIntent.putExtra(DownloadTask.FILE_DOWN_INFO_KEY, fileDownInfo);
		context.sendBroadcast(downIntent);

		if (listenner != null) {
			listenner.onDownloadStop(fileDownInfo);
		}
	}

	/**
	 * threadId
	 * 
	 * @return the threadId
	 */
	public long getThreadId() {
		return threadId;
	}

	/**
	 * @param threadId
	 *            the threadId to set
	 */
	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}

	private long threadId;

	private byte[] getJson(String gameId) {
		String result = "{id:\"" + gameId + "\"}";
		return result.getBytes();
	}

	/**
	 * 获取游戏的手柄配置文件
	 * 
	 * @param context
	 * @param myGameInfo
	 * @return true获取成功，false失败
	 * @throws
	 */
	public static boolean getConfigFile(Context context, String packageName) {
		if (packageName == null || packageName.length() <= 0) {
			return false;
		}

		if (isConfigFileExistInRemoteDB(context, packageName)) {
			return true;
		}

		ReqGetConfiigInfo mReqInfo = new ReqGetConfiigInfo();
		mReqInfo.setPackageName(packageName);
		if (Constant.WIDTH <= 0) {
			initDevice(context);
		}
		
		int width = Constant.WIDTH;
		int height = Constant.HEIGHT;
		if (width > height) {
			int temp = width;
			width = height;
			height = temp;
		}
		
		mReqInfo.setWide(width);
		mReqInfo.setHeight(height);
		mReqInfo.setDensity(Constant.DENSITY);
		mReqInfo.setDeviceId(DataHelper.getDeviceInfo().getServerId());


		InputStream is = null;
		BufferedReader bufferReader = null;
		int retryTimes = 3;
		String postData;

		Gson gson = new Gson();
		postData = gson.toJson(mReqInfo);
		while (retryTimes-- > 0) {
			try {
				// if(isStopDownload){
				// return false;
				// }
				String result;
				String line;

				is = DownloadThread.httpPostInputStream(
						UrlConstant.HTTP_GET_GAMEPAD_CONFIG,UrlConstant.HTTP_GET_GAMEPAD_CONFIG2,UrlConstant.HTTP_GET_GAMEPAD_CONFIG3,
						!NetUtil.isWifiOpen(context), postData.getBytes());
				result = StreamTool.convertStreamToString(is);
				if (result != null && result.length() > 0) {
					RespGetConfigInfo respInfo = gson.fromJson(result,
							new TypeToken<RespGetConfigInfo>() {
							}.getType());
					if (respInfo != null) {
						int code = respInfo.getCode();
						if (code == Constant.CODE_SYS_SUCCESS) {
							List<AddrInfo> addrInfoList = respInfo.getData();
							if (addrInfoList == null) {
								return false;
							}

							ConfigInfo configInfo = new ConfigInfo();
							gson = new GsonBuilder()
									.excludeFieldsWithoutExposeAnnotation()
									.create();
							for (int j = 0; j < addrInfoList.size(); j++) {
								// if(isStopDownload){
								// return false;
								// }
								int type = addrInfoList.get(j).getHandleType();
								String url = addrInfoList.get(j)
										.getDownloadURL();

								if ((type != ConfigInfo.MODE_EMULATE
										&& type != ConfigInfo.MODE_GAMEPAD && type != ConfigInfo.MODE_KEYBOARD)
										|| (url == null || url.length() <= 0)) {
									continue;
								}

								retryTimes = 2;
								while (retryTimes-- > 0) {
									try {
										is = DownloadThread.httpGetInputStream(
												url,
												!NetUtil.isWifiOpen(context),
												0, 0);

										bufferReader = new BufferedReader(
												new InputStreamReader(is));
										StringBuilder sb = new StringBuilder();
										while ((line = bufferReader.readLine()) != null) {
											// if(!isCheckValid){
											// if(line.contains("")){
											// return null;
											// }
											// }
											sb.append(line);
										}
										result = sb.toString();
										boolean isOk = true;
										if (type == ConfigInfo.MODE_EMULATE) {
											GameConfigInfo info = gson
													.fromJson(
															result,
															GameConfigInfo.class);
											if (info == null) {
												// 解析出错
												
												isOk = false;
											} else {
												if (!info.isEnable(true)
														&& !info.isEnable(false)) {
													// 两个方向的配置文件都不存在
													
													break;
												}
												configInfo
														.setGameConfigInfo(info);
												configInfo
														.setMode(configInfo
																.getMode()
																| ConfigInfo.MODE_EMULATE);

												if (info.getIsfloat() == 1) {
													// 是输入法没响应的游戏
													try {
														ContentValues cv = new ContentValues();
														cv.put(FileDownloader.TABLE_COL_PACKAGE_NAME,
																info.getPkgName());
														Uri uri = Uri
																.parse(FileDownloader.REMOTE_INTERCEPT_URI);
														context.getContentResolver()
																.insert(uri, cv);
													} catch (Exception e) {
														// TODO: handle
														// exception
														e.printStackTrace();
													}
												}
											}
										} else if (type == ConfigInfo.MODE_KEYBOARD) {
											KeyConfigInfo info = gson
													.fromJson(result,
															KeyConfigInfo.class);
											if (info == null) {
												
												isOk = false;
											} else {
												if (info.getMode() == ConfigInfo.MODE_NONE) {
									
													break;
												}
												configInfo
														.setKeyboardConfigInfo(info);
												configInfo
														.setMode(configInfo
																.getMode()
																| ConfigInfo.MODE_KEYBOARD);
											}
										} else if (type == ConfigInfo.MODE_GAMEPAD) {
											KeyConfigInfo info = gson
													.fromJson(result,
															KeyConfigInfo.class);
											if (info == null) {
												
												isOk = false;
											} else {
												if (info.getMode() == ConfigInfo.MODE_NONE) {
												
													break;
												}
												configInfo
														.setGamepadConfigInfo(info);
												configInfo
														.setMode(configInfo
																.getMode()
																| ConfigInfo.MODE_GAMEPAD);
											}
										}
										if (!isOk) {
								
											// taskResult.setCode(Constant.CODE_NO_RESPONDING_DATA);
											// return taskResult;
										}
										break;
									} catch (Exception e) {
										// TODO: handle exception
										if (e != null) {
											
										}
									}
								} // end while
							}
							configInfo.setPkgName(mReqInfo.getPackageName());

							saveConfigFileToRemoteDB(context, configInfo);
							return true;
						} else {
							return false;
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} // end while
		return false;
	}

	private static void saveConfigFileToRemoteDB(Context context,
			ConfigInfo configInfo) {

		try {
			ContentResolver contentResolver = context.getContentResolver();
			ContentValues cv = new ContentValues();
			cv = getContentValuesFromBean(configInfo);
			Uri url = Uri.parse(REMOTE_GAMEPAD_CONFIG_URI);
			if (contentResolver.insert(url, cv) != null) {
				return;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private static boolean isConfigFileExistInRemoteDB(Context context,
			String pkgName) {
		boolean result = false;
		Cursor c = null;
		try {
			ContentResolver contentResolver;
			contentResolver = context.getContentResolver();
			Uri url = Uri.parse(REMOTE_GAMEPAD_CONFIG_URI);

			String condition = TABLE_COL_PACKAGE_NAME + " = \"" + pkgName
					+ "\"";
			c = contentResolver.query(url, null, condition, null, null);
			if (c != null && c.getCount() > 0) {
				result = true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
			}
		}
		return result;
	}

	public static ContentValues getContentValuesFromBean(ConfigInfo bean) {
		ContentValues values = new ContentValues();
		int id = bean.getId();
		if (id != GameConfigInfo.NO_ID) {
			values.put(TABLE_COL_ID, id);
		}
		values.put(TABLE_COL_PACKAGE_NAME, bean.getPkgName());
		values.put(TABLE_COL_MODE, bean.getMode());
		values.put(TABLE_COL_RUNTIMES, bean.getRuntimes());

		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
				.create();
		String json;
		if (bean.isModeEnable(ConfigInfo.MODE_EMULATE)) {
			json = gson.toJson(bean.getGameConfigInfo());
			values.put(TABLE_COL_KEYBOARD, json.getBytes());
		} else if (bean.isModeEnable(ConfigInfo.MODE_KEYBOARD)) {
			json = gson.toJson(bean.getKeyboardConfigInfo());
			values.put(TABLE_COL_KEYBOARD, json.getBytes());
		}
		if (bean.isModeEnable(ConfigInfo.MODE_GAMEPAD)) {
			json = gson.toJson(bean.getGamepadConfigInfo());
			values.put(TABLE_COL_GAMEPAD, json.getBytes());
		}

		return values;
	}

	public static final String TABLE_NAME_REMOTE = "remote";
	public static final String TABLE_NAME_INTERCEPT = "intercept";
	public static final String TABLE_COL_ID = "id";
	public static final String TABLE_COL_PACKAGE_NAME = "pkgName";
	public static final String TABLE_COL_KEYBOARD = "keyboard";
	public static final String TABLE_COL_GAMEPAD = "gamepad";
	public static final String TABLE_COL_RUNTIMES = "runtimes";
	public static final String TABLE_COL_MODE = "mode";
	public static final String REMOTE_GAMEPAD_CONFIG_URI = "content://com.atet.tvgamepad.provider/"
			+ TABLE_NAME_REMOTE;
	public static final String REMOTE_INTERCEPT_URI = "content://com.atet.tvgamepad.provider/"
			+ TABLE_NAME_INTERCEPT;

	@SuppressLint("NewApi")
	public static boolean initDevice(Context context) {
		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		int width = 0;
		int height = 0;

		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		int sdkInt = Build.VERSION.SDK_INT;
		Constant.DENSITY = dm.densityDpi;

		try {
			if (sdkInt >= 17) {
				Point outSize = new Point();
				display.getRealSize(outSize);
				width = outSize.x;
				height = outSize.y;
			} else if (sdkInt >= 13 && sdkInt <= 16) {
				Method mGetRawW;
				Method mGetRawH;
				mGetRawW = Display.class.getMethod("getRawWidth");
				mGetRawH = Display.class.getMethod("getRawHeight");
				width = (Integer) mGetRawW.invoke(display);
				height = (Integer) mGetRawH.invoke(display);
			} else {
				width = dm.widthPixels;
				height = dm.heightPixels;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			width = dm.widthPixels;
			height = dm.heightPixels;
		}

		if (width > height) {
			int temp = width;
			width = height;
			height = temp;
		}
		Constant.WIDTH = width;
		Constant.HEIGHT = height;
		Constant.RESOLUTION = Constant.WIDTH + "x" + Constant.HEIGHT;
		return true;
	}
}
