package com.atet.tvmarket.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.atet.api.utils.EncryptUtils;
import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.app.Configuration;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.app.IPPort;
import com.atet.tvmarket.entity.AppConfigResp;
import com.atet.tvmarket.entity.AutoType;
import com.atet.tvmarket.entity.DeviceInfo;
import com.atet.tvmarket.entity.DownStarResp;
import com.atet.tvmarket.entity.DownloadFileReq;
import com.atet.tvmarket.entity.FileDownResultInfo;
import com.atet.tvmarket.entity.GoodsExchangeReq;
import com.atet.tvmarket.entity.GoodsExchangeResp;
import com.atet.tvmarket.entity.GoodsLeftCountResp.GoodsLeftCountInfo;
import com.atet.tvmarket.entity.InterfaceInfo;
import com.atet.tvmarket.entity.NewVersionInfoResp;
import com.atet.tvmarket.entity.ObtainRewardReq;
import com.atet.tvmarket.entity.ObtainRewardResp;
import com.atet.tvmarket.entity.ObtainUserGiftReq;
import com.atet.tvmarket.entity.RateGameReq;
import com.atet.tvmarket.entity.RewardInfoResp;
import com.atet.tvmarket.entity.UpdateInterfaceReq;
import com.atet.tvmarket.entity.UserGetCaptchaReq;
import com.atet.tvmarket.entity.UserGoodsOrderInfo;
import com.atet.tvmarket.entity.UserGoodsOrderInfoResp;
import com.atet.tvmarket.entity.UserInfo;
import com.atet.tvmarket.entity.UserLoginRegisterReq;
import com.atet.tvmarket.entity.UserModifyReq;
import com.atet.tvmarket.entity.UserTaskReq;
import com.atet.tvmarket.entity.UserTaskResp;
import com.atet.tvmarket.entity.UserTaskResp.UserTaskList;
import com.atet.tvmarket.entity.UserTaskResp.UserTaskList.RewardRuleInner;
import com.atet.tvmarket.entity.UserWeChatLoginReq;
import com.atet.tvmarket.entity.dao.ActInfo;
import com.atet.tvmarket.entity.dao.AdInfo;
import com.atet.tvmarket.entity.dao.AdModelInfo;
import com.atet.tvmarket.entity.dao.GameGiftInfo;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.entity.dao.GameSearchPinyinInfo;
import com.atet.tvmarket.entity.dao.GameTopicInfo;
import com.atet.tvmarket.entity.dao.GameTypeInfo;
import com.atet.tvmarket.entity.dao.GoodsInfo;
import com.atet.tvmarket.entity.dao.LocalSubTypeId;
import com.atet.tvmarket.entity.dao.LocalUpdateInfo;
import com.atet.tvmarket.entity.dao.ModelToAd;
import com.atet.tvmarket.entity.dao.NoticeInfo;
import com.atet.tvmarket.entity.dao.SubTypeId;
import com.atet.tvmarket.entity.dao.ThirdGameInfo;
import com.atet.tvmarket.entity.dao.TopicToGame;
import com.atet.tvmarket.entity.dao.TypeToGame;
import com.atet.tvmarket.entity.dao.UserGameGiftInfo;
import com.atet.tvmarket.entity.dao.UserGameToGift;
import com.atet.tvmarket.entity.dao.VideoInfo;
import com.atet.tvmarket.model.DataConfig.UpdateInterface;
import com.atet.tvmarket.model.ReqConfig.ReqCode;
import com.atet.tvmarket.model.ReqConfig.ReqType;
import com.atet.tvmarket.model.netroid.Listener;
import com.atet.tvmarket.model.netroid.Exception.NetroidError;
import com.atet.tvmarket.model.netroid.request.DataTaskRequest;
import com.atet.tvmarket.model.netroid.toolbox.FileDownloader;
import com.atet.tvmarket.model.netroid.toolbox.FileDownloader.DownloadController;
import com.atet.tvmarket.model.task.TaskQueue;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.model.usertask.CheckInTaskInfo;
import com.atet.tvmarket.model.usertask.UserTask;
import com.atet.tvmarket.model.usertask.UserTaskDaoHelper;
import com.atet.tvmarket.model.usertask.UserTaskGenerator;
import com.atet.tvmarket.model.usertask.UserTaskInfo;
import com.atet.tvmarket.model.usertask.UserTaskInfo.UserTaskState;
import com.atet.tvmarket.model.usertask.UserTaskInfo.UserTaskType;
import com.atet.tvmarket.utils.AESCryptoUtils;
import com.atet.tvmarket.utils.AppUtil;
import com.atet.tvmarket.utils.NetUtil;
import com.atetpay.autologin.ATETRegist;
import com.atetpay.autologin.AutoSignIn;
import com.atetpay.autologin.GetAllUserInfo;
import com.atetpay.autologin.GetCode;
import com.atetpay.autologin.GetLocalUserInfo;
import com.atetpay.autologin.SwitchEnvUtils;
import com.atetpay.autologin.UpdateUserInfo;
import com.atetpay.autologin.WechatLogin;
import com.atetpay.autologin.callback.SignListener;
import com.atetpay.autologin.callback.UserInfoCallBack;
import com.atetpay.login.data.LOGIN;
import com.atetpay.pay.data.PAY;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.ConditionVariable;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import de.greenrobot.dao.identityscope.IdentityScope;
import de.greenrobot.dao.identityscope.IdentityScopeObject;

/**
 * @description: 数据获取
 *
 * @author: LiuQin
 * @date: 2015年7月10日 下午2:55:33 
 */
public class DataHelper {
	private static ALog alog = ALog.getLogger(DataHelper.class);
	private static DeviceInfo sDeviceInfo = new DeviceInfo();
	private static UserInfo sAtetUserInfo;
    public static IdentityScope<String, AutoType> sIdentityScope = new IdentityScopeObject();
    public static boolean isInitDeviceInfo = false;
    private static List<String> sNewContentInterface = null;
    
	public static String PUBLICKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC1qSc4idfWls43XQp+HkF4enRu1iDCD3YKfbmIbiD6j257RfxBA3PLVWppWWRLmv1M+mTv+pRKXUzyv9VNWlfQhqEgo7AFxzPjrDKHKNicN9LtZSTRqz/p9rimzxpuJP4z9OB1GfNBR5Bs/hTK0jLEpMyCsMA8q3Su3v15cKOZiQIDAQAB";
    static{
//    	System.loadLibrary("crypto");
//        System.loadLibrary("_All_ATET_Market_Crypto");
        PUBLICKEY=EncryptUtils.mencrypt(PUBLICKEY);
        
        if(IPPort.IS_PAY_RELEASE_IP){
        	SwitchEnvUtils.switchEnv(BaseApplication.getContext(), PAY.ENVIRONMENT.ONLINE);
        } else {
        	SwitchEnvUtils.switchEnv(BaseApplication.getContext(), PAY.ENVIRONMENT.IOS_TEST);
        }
    }
	
	public static String signPostData(String content){
		String result = null;
		result = EncryptUtils.payEncrypt(content, PUBLICKEY);
		return result;
	}
	
	public static boolean checkSign(String content, String sign){
		boolean result = false;
		String contentSign = EncryptUtils.payEncrypt(content, PUBLICKEY);
		if(contentSign.equals(sign)){
			result = true;
		}
		return result;
	}
	
	public static DataRequester makeDataRequester(Context context, ReqConfig reqConfig){
		alog.info("reqCode:"+reqConfig.getReqCode());
		return new DataRequester(reqConfig);
	}
	
	public static DataTaskRequest getGameDataEx(Context context, ReqConfig reqConfig){
		alog.info("reqCode:"+reqConfig.getReqCode());
		context = context.getApplicationContext();
		DataTaskRequest dataTaskRequest = null; 
		switch (reqConfig.getReqCode()) {
		case ReqCode.FETCH_SERVER_ID:
			dataTaskRequest=DataHelper.fetchServerId(context, reqConfig);
			break;
		case ReqCode.FETCH_GAME_TYPE:
			dataTaskRequest=DataHelper.fetchGameType(context, reqConfig);
			break;
		case ReqCode.FETCH_GAME_INFOS_FROM_GAME_TYPE:
			dataTaskRequest=DataHelper.fetchGameInfosFromGameType(context, reqConfig);
			break;
		case ReqCode.FETCH_GAME_INFOS_FROM_GAME_TYPE2:
			dataTaskRequest=DataHelper.fetchGameInfosFromGameType2(context, reqConfig);
			break;
		case ReqCode.FETCH_GAME_INFO_FROM_GAME_ID:
			dataTaskRequest=DataHelper.fetchGameInfoFromGameId(context, reqConfig);
			break;
		case ReqCode.FETCH_GAME_INFOS_FROM_NEW_UPLOAD:
			dataTaskRequest=DataHelper.fetchNewUploadGames(context, reqConfig);
			break;
		case ReqCode.FETCH_GAME_INFOS_FROM_GAME_RANKING:
			dataTaskRequest=DataHelper.fetchGameRanking(context, reqConfig);
			break;
		case ReqCode.FETCH_GAME_TOPIC:
			dataTaskRequest=DataHelper.fetchGameTopic(context, reqConfig);
			break;
		case ReqCode.FETCH_GAME_INFOS_FROM_GAME_TOPIC:
			dataTaskRequest=DataHelper.fetchGameInfosFromGameTopic(context, reqConfig);
			break;
		case ReqCode.FETCH_GAME_INFOS_FROM_GAME_TOPIC2:
			dataTaskRequest=DataHelper.fetchGameInfosFromGameTopic2(context, reqConfig);
			break;
		case ReqCode.FETCH_GAME_INFOS_FROM_GAME_TOPIC_THIRD:
			dataTaskRequest=DataHelper.fetchGameInfosFromGameTopic(context, reqConfig);
			break;
		case ReqCode.FETCH_GAME_SEARCH_PINYIN:
			dataTaskRequest=DataHelper.fetchGameSearchPinyinInfo(context, reqConfig);
			break;
		case ReqCode.FETCH_GAME_SEARCH_PINYIN_BY_PINYIN:
			dataTaskRequest=DataHelper.fetchGameSearchPinyinInfoByPinyin(context, reqConfig);
			break;
		case ReqCode.FETCH_GAME_CENTER_INFO:
//			DataHelper.getGameCenterInfos(context, reqConfig);
			dataTaskRequest=DataHelper.getGameCenterInfos2(context, reqConfig);
			break;
		case ReqCode.FETCH_LAUNCH_AD:
			dataTaskRequest=DataHelper.getLaunchAdInfos(context, reqConfig);
			break;
		case ReqCode.FETCH_ACTIVITY:
			dataTaskRequest=DataHelper.getActInfos(context, reqConfig);
			break;
		case ReqCode.FETCH_GOODS:
			dataTaskRequest=DataHelper.getGoodsInfos(context, reqConfig);
			break;
		case ReqCode.FETCH_GIFT:
			dataTaskRequest=DataHelper.getGameGiftInfos(context, reqConfig);
			break;
		case ReqCode.FETCH_USER_GIFT:
			dataTaskRequest=DataHelper.getUserGiftInfos(context, reqConfig);
			break;
		case ReqCode.OBTAIN_USER_GIFT:
			dataTaskRequest=DataHelper.obtainUserGift(context, reqConfig);
			break;
		case ReqCode.FETCH_NOTICE:
			dataTaskRequest=DataHelper.getNoticeInfos(context, reqConfig);
			break;
		case ReqCode.FETCH_VIDEO:
			dataTaskRequest=DataHelper.getVideoInfos(context, reqConfig);
			break;
		case ReqCode.RATE_GAME:
			dataTaskRequest=DataHelper.rateGame(context, reqConfig);
			break;
		case ReqCode.FETCH_THIRD_GAME_INFO_FROM_GAME_ID:
			dataTaskRequest=DataHelper.fetchThirdGameInfoFromGameId(context, reqConfig);
			break;
		case ReqCode.FETCH_RELATIVE_GAME_INFO_FROM_GAME_ID:
			dataTaskRequest=DataHelper.fetchRelativeGames(context, reqConfig);
			break;
		case ReqCode.FETCH_RECOMMEND_GAME_SEARCH_INFO:
			dataTaskRequest=DataHelper.fetchRecommendGameSearchPinyinInfo(context, reqConfig);
			break;
		case ReqCode.FETCH_USER_TASK:
			dataTaskRequest=DataHelper.fetchUserTask(context, reqConfig);
			break;
		case ReqCode.FETCH_USER_TASK2:
			dataTaskRequest=DataHelper.fetchUserTask2(context, reqConfig);
			break;
		case ReqCode.OBTAIN_USER_TASK_REWARD:
			dataTaskRequest=DataHelper.obtainUserTaskReward(context, reqConfig);
			break;
		case ReqCode.FETCH_NEW_VERSION_INFO:
			dataTaskRequest=DataHelper.fetchNewVersionInfo(context, reqConfig);
			break;
//		case ReqCode.DOWNLOAD_NEW_VERSION_APK:
//			return DataHelper.downloadNewVersionApk(context, reqConfig);
		case ReqCode.USER_GOODS_ORDER:
			dataTaskRequest=DataHelper.fetchUserGoodsOrderInfo(context, reqConfig);
			break;
		case ReqCode.EXCHANGE_GOODS:
			dataTaskRequest=DataHelper.exchangeGoods(context, reqConfig);
			break;
		case ReqCode.UPDATABLE_INTERFACE:
			dataTaskRequest=DataHelper.fetchLocalUpdatableInterface(context, reqConfig);
			break;
		case ReqCode.FETCH_REWARD_INFO:
			dataTaskRequest=DataHelper.fetchRewardInfo(context, reqConfig);
			break;
		case ReqCode.OBTAIN_REWARD:
			dataTaskRequest=DataHelper.obtainReward(context, reqConfig);
			break;
		case ReqCode.DOWN_STAR:
			dataTaskRequest=DataHelper.fetchDownStar(context, reqConfig);
			break;
		case ReqCode.APP_CONFIG:
			dataTaskRequest=DataHelper.fetchAppConfig(context, reqConfig);
			break;
		case ReqCode.NEW_CONTENT_INTERFACE:
			dataTaskRequest=DataHelper.getNewContentInterface(context, reqConfig);
			break;
		case ReqCode.REMOVE_NEW_CONTENT_INTERFACE:
			dataTaskRequest=DataHelper.removeInterface(context, reqConfig);
			break;
		case ReqCode.FETCH_GOODS_LEFT_COUNT:
			dataTaskRequest=DataHelper.fetchGoodsLeftCount(context, reqConfig);
			break;
		case ReqCode.SYNC_TIME:
			dataTaskRequest=DataHelper.syncTime(context, reqConfig);
			break;

		default:
			break;
		}
		
		if(dataTaskRequest != null){
			dataTaskRequest.setReqConfig(reqConfig);
		}
		
		return dataTaskRequest;
	}
	
	public static void getUserDataEx(Context context, ReqConfig reqConfig){
		switch (reqConfig.getReqCode()) {
		case ReqCode.USER_REGISTER:
			DataHelper.userRegister(context, reqConfig);
			break;
		case ReqCode.USER_LOGIN:
			DataHelper.userLogin(context, reqConfig);
			break;
		case ReqCode.USER_AUTO_LOGIN:
			DataHelper.userAutoLogin(context, reqConfig);
			break;
		case ReqCode.USER_AUTO_LOGIN_BY_USERID:
			DataHelper.userAutoLoginByUserId(context, reqConfig);
			break;
		case ReqCode.USER_GET_CAPTCHA:
			DataHelper.userGetCaptcha(context, reqConfig);
			break;
		case ReqCode.USER_WECHAT_LOGIN:
			DataHelper.userWeChatLogin(context, reqConfig);
			break;
		case ReqCode.USER_GET_ALL_LOGINED_USERS:
			DataHelper.userGetAllLoginedUser(context, reqConfig);
			break;
		case ReqCode.USER_GET_LAST_LOGINED_USER:
			DataHelper.userGetLastLoginedUser(context, reqConfig);
			break;
		case ReqCode.USER_UPDATE_PASSWORD:
			DataHelper.userUpdatePassword(context, reqConfig);
			break;
		case ReqCode.USER_UPDATE_PHONE_NUM:
			DataHelper.userUpdatePhoneNum(context, reqConfig);
			break;
		case ReqCode.USER_RESET_PASSWORD:
			DataHelper.resetUpdatePassword(context, reqConfig);
			break;
			
		default:
			break;
		}
	}
	
	public static DownloadTaskRequest getDownloadDataEx(Context context, ReqConfig reqConfig){
		switch (reqConfig.getReqCode()) {
		case ReqCode.DOWNLOAD_NEW_VERSION_APK:
			return DataHelper.downloadNewVersionApk(context, reqConfig);
		case ReqCode.DOWNLOAD_FILE:
			return DataHelper.downloadFile(context, reqConfig);

		default:
			break;
		}
		return null;
	}
	
	/**
	 * @description: 执行任务请求
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年8月5日 上午10:27:31
	 */
	public static void doRequest(Context context, ReqConfig reqConfig){
		Task task = null;
		int reqCode = reqConfig.getReqCode();
		if(reqCode <= 100){
			task = new Task(reqConfig, new Requestable() {
				@Override
				public void request(Context context, ReqConfig reqConfig) {
					DataTaskRequest request = DataHelper.getGameDataEx(context, reqConfig);
					if(request.getTag() == null){
						String tag = DataConfig.REQUEST_TASK_TAG_PREFIX + reqConfig.getReqCode();
						reqConfig.setTaskId(tag);
						request.setTag(tag);
					}
					TaskQueue.getInstanse(context).add(request);
				}
			}, new Cancelable() {
				@Override
				public void cancel(Context context, ReqConfig reqConfig) {
					TaskQueue.getInstanse(context).cancelAll(reqConfig.getTaskId());
				}
			});
		} else if(reqCode > 100 && reqCode < 200){
			task = new Task(reqConfig, new Requestable() {
				@Override
				public void request(Context context, ReqConfig reqConfig) {
					DataHelper.getUserDataEx(context, reqConfig);
				}
			}, new Cancelable() {
				@Override
				public void cancel(Context context, ReqConfig reqConfig) {
					Task task = reqConfig.getTask();
					if(task != null){
						Object request = task.getObject();
						if(request != null){
							if(request instanceof ATETRegist){
								((ATETRegist)request).cancel();
							} else  if(request instanceof ATETRegist){
//								((ATETLogin)request).cancel();
							} else  if(request instanceof AutoSignIn){
//								((AutoSignIn)request).cancel();
							} else  if(request instanceof GetCode){
								((GetCode)request).cancel();
							} else  if(request instanceof UpdateUserInfo){
								((UpdateUserInfo)request).cancel();
							} else  if(request instanceof WechatLogin){
//								((WechatLogin)request).cancel();
							}
						}
						task.recycle();
					}
				}
			});
		} else {
			task = new Task(reqConfig, new Requestable() {
				@Override
				public void request(Context context, ReqConfig reqConfig) {
					Task task = reqConfig.getTask();
					if(task != null){
						DownloadTaskRequest request = DataHelper.getDownloadDataEx(context, reqConfig);
						task.setObject(request);
						FileDownloader.DownloadController controller=TaskQueue.getDownloaderInstance(context).add(
								request.getLocalFilePath(), request.getFileUrl(), request.getListener());
						request.setDownloadController(controller);
						controller.resume();
					}
				}
			}, new Cancelable() {
				@Override
				public void cancel(Context context, ReqConfig reqConfig) {
					Task task = reqConfig.getTask();
					if(task != null){
						DownloadTaskRequest request = (DownloadTaskRequest)task.getObject();
						DownloadController controller = request.getDownloadController();
						if(controller!=null){
							controller.discard();
						}
						task.recycle();
					}
				}
			});
		}
		task.request(context);
	}
	
	public static <T> void onCallback(Context context,
			final ReqConfig reqConfig, final TaskResult<T> taskResult) {

		taskResult.setRequestTag(reqConfig.getRequestTag());
		taskResult.setTaskId(reqConfig.getTaskId());
		
		if(taskResult.getCode() == TaskResult.OK && reqConfig.getInterfaceInfo()!=null){
			updateLocalTime(context, reqConfig, reqConfig.getInterfaceInfo());
		}
		
		Task task = reqConfig.getTask();
		if(task != null){
			task.recycle();
		}
				
		if (reqConfig.getCallback() == null) {
			alog.debug("task callback null");
			return;
		}
		if (reqConfig.isMainCallback() && !DataHelper.isUiThread()) {
			alog.debug("post taskResult to UI thread");
			TaskQueue.getInstanse(context).postUiThread(new Runnable() {
				@Override
				public void run() {
					reqConfig.getCallback().onResult(taskResult);
				}
			});
		} else {
			alog.debug("post taskResult to worker thread");
			reqConfig.getCallback().onResult(taskResult);
		}		
	}
	
	public static void  onGetCacheDataCallback(Context context,
			final ReqConfig reqConfig, final boolean result) {
		if (reqConfig.getCallback() == null) {
			alog.debug("task callback null");
			return;
		}
		if (reqConfig.isMainCallback() && !DataHelper.isUiThread()) {
			alog.debug("post onGetCacheData to UI thread");
			TaskQueue.getInstanse(context).postUiThread(new Runnable() {
				@Override
				public void run() {
					reqConfig.getCallback().onGetCacheData(reqConfig.getRequestTag(), result);
				}
			});
		} else {
			alog.debug("post onGetCacheData to worker thread");
			reqConfig.getCallback().onGetCacheData(reqConfig.getRequestTag(), result);
		}
	}
	
	public static <T> void onProgressUpdate(Context context,
			final ReqConfig reqConfig, final long fileSize, final long downloadedSize) {
		if (reqConfig.getCallback() == null) {
			alog.debug("task callback null");
			return;
		}
		if (reqConfig.isMainCallback() && !DataHelper.isUiThread()) {
//			alog.debug("post taskResult to UI thread");
			TaskQueue.getInstanse(context).postUiThread(new Runnable() {
				@Override
				public void run() {
					reqConfig.getCallback().onProgressChange(fileSize, downloadedSize);
					reqConfig.getCallback().onProgressChange(reqConfig.getRequestTag(), fileSize, downloadedSize);
				}
			});
		} else {
//			alog.debug("post taskResult to worker thread");
			reqConfig.getCallback().onProgressChange(fileSize, downloadedSize);
			reqConfig.getCallback().onProgressChange(reqConfig.getRequestTag(), fileSize, downloadedSize);
		}
	}
	
	public static <T> void onUpdateCallback(Context context,
			final ReqConfig reqConfig, final TaskResult<T> taskResult) {
		taskResult.setRequestTag(reqConfig.getRequestTag());
		if (reqConfig.getCallback() == null) {
			alog.debug("task callback null");
			return;
		}
		if (reqConfig.isMainCallback() && !DataHelper.isUiThread()) {
			alog.debug("post taskResult to UI thread");
			TaskQueue.getInstanse(context).postUiThread(new Runnable() {
				@Override
				public void run() {
					reqConfig.getCallback().onUpdate(taskResult);
				}
			});
		} else {
			alog.debug("post taskResult to worker thread");
			reqConfig.getCallback().onUpdate(taskResult);
		}		
	}

	/**
	 * @description: 是否主线程
	 *
	 * @return
	 * @author: LiuQin
	 * @date: 2015年7月3日 上午11:05:45
	 */
	public static boolean isUiThread() {
		return Looper.myLooper() == Looper.getMainLooper();
	}

	/**
	 * @description: 获取设备信息
	 *
	 * @param context 
	 * @author: LiuQin
	 * @date: 2015年7月3日 下午6:58:02
	 */
	public static void initDeviceInfo(Context context) {
		if (TextUtils.isEmpty(DataHelper.getDeviceInfo().getDeviceModel())) {
			String deviceModel = DeviceHelper.getDeviceModel(context);
			String deviceUniqueId = DeviceHelper.getDeviceUniqueId(context);
			alog.debug("get deviceModel:"+deviceModel);
			alog.debug("get deviceUniqueId:"+deviceUniqueId);
			DataHelper.getDeviceInfo().setDeviceModel(deviceModel);
			DataHelper.getDeviceInfo().setDeviceUniqueId(deviceUniqueId);
		}

		if (TextUtils.isEmpty(DataHelper.getDeviceInfo().getServerId())) {
			String serverId = (String) SpHelper.get(context,
					SpHelper.KEY_SERVER_ID, "");
			alog.debug("cache serverId:"+serverId);
			DataHelper.getDeviceInfo().setServerId(serverId);
			if (!TextUtils.isEmpty(serverId)) {
				String channelId = (String) SpHelper.get(context,
						SpHelper.KEY_CHANNEL_ID, "");
				DataHelper.getDeviceInfo().setChannelId(channelId);
			}
		}

		if (TextUtils.isEmpty(DataHelper.getDeviceInfo().getAtetId())
				|| DataHelper.getDeviceInfo().getAtetId().equals("1")) {
			String atetId = (String) SpHelper.get(context,
					SpHelper.KEY_ATET_ID, "");
			alog.debug("cache atetId:"+atetId);
			if(TextUtils.isEmpty(atetId)){
				SharedPreferences preferences = context.getSharedPreferences(
						"deviceInfo", Context.MODE_PRIVATE);
				atetId = preferences.getString("DEVICE_ATET_ID", "1");
				if(!TextUtils.isEmpty(atetId) && !atetId.equals("1")){
					SpHelper.put(context, SpHelper.KEY_ATET_ID, atetId);
				}
			}
			DataHelper.getDeviceInfo().setAtetId(atetId);
		}
		
		DataHelper.isInitDeviceInfo = true;
	}
	
	public static DeviceInfo getDeviceInfo(){
		if(!DataHelper.isInitDeviceInfo){
			DataHelper.isInitDeviceInfo = true;
			DataHelper.initDeviceInfo(BaseApplication.getContext());
		}
		return DataHelper.sDeviceInfo;
	}

	/**
	 * @description: 获取数据请求id
	 *
	 * @param context
	 * @param reqConfig
	 * @author: LiuQin
	 * @date: 2015年7月3日 上午10:03:54
	 */
	public static DataTaskRequest fetchServerId(final Context context, final ReqConfig reqConfig) {
		alog.info("");;
		DataTaskRequest<String> taskRequest = new DataTaskRequest<String>(
				new DataTaskListener<String,String>() {

			@Override
			public String onGetCacheData() {
				alog.debug("get cache info");
				return (String) SpHelper.get(context, SpHelper.KEY_SERVER_ID, "");
			}

			@Override
			public boolean isCacheDataExist(String cacheData) {
//				alog.debug("cacheData:" + cacheData);
				return !TextUtils.isEmpty(cacheData) &&
						!TextUtils.isEmpty((String)SpHelper.get(context, SpHelper.KEY_ATET_ID, ""));
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.fetchServerId(context, reqConfig);
			}
			
			@Override
			public String onCacheDataProcess(String infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<String> taskResult, String result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
		return taskRequest;
	}
	
	/**
	 * @description: 获取分类
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月5日 下午5:18:38
	 */
	public static DataTaskRequest fetchGameType(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		DataTaskRequest<List<GameTypeInfo>> taskRequest = new DataTaskRequest<List<GameTypeInfo>>(
				new DataTaskListener<List<GameTypeInfo>,List<GameTypeInfo>>() {

			@Override
			public List<GameTypeInfo> onGetCacheData() {
				alog.debug("get cache info");
				return DaoHelper.getGameType(context);
			}

			@Override
			public boolean isCacheDataExist(List<GameTypeInfo> cacheData) {
				alog.debug("cache size:" + cacheData.size());
				return !cacheData.isEmpty();
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.fetchGameType(context, reqConfig, onGetHttpReqCallback());
			}
			
			@Override
			public List<GameTypeInfo> onCacheDataProcess(List<GameTypeInfo> infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<List<GameTypeInfo>> taskResult,
					List<GameTypeInfo> result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
//		TaskQueue.getInstanse(context).add(taskRequest);
		return taskRequest;
	}
	
	/**
	 * @description: 获取分类下的游戏
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月5日 下午5:18:52
	 */
	public static DataTaskRequest fetchGameInfosFromGameType(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		final String gameTypeId=((GameTypeInfo)reqConfig.getData()).getTypeId();
		if (TextUtils.isEmpty(gameTypeId)) {
			throw new IllegalArgumentException("Illegal argument");
		}
		DataTaskRequest<List<TypeToGame>> taskRequest = new DataTaskRequest<List<TypeToGame>>(
				new DataTaskListener<List<TypeToGame>,List<TypeToGame>>() {

			@Override
			public List<TypeToGame> onGetCacheData() {
				alog.debug("get cache info");
				return DaoHelper.getTypeToGame(context, gameTypeId);
			}

			@Override
			public boolean isCacheDataExist(List<TypeToGame> cacheData) {
				alog.debug("cache size:" + cacheData.size());
				return !cacheData.isEmpty();
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.fetchGameInfosFromGameType(context, reqConfig, onGetHttpReqCallback());;
			}
			
			@Override
			public List<TypeToGame> onCacheDataProcess(List<TypeToGame> infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<List<TypeToGame>> taskResult,
					List<TypeToGame> result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
//		TaskQueue.getInstanse(context).add(taskRequest);
		return taskRequest;
	}
	
	/**
	 * @description: 获取分类下的游戏
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月5日 下午5:18:52
	 */
	public static DataTaskRequest fetchGameInfosFromGameType2(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		GameTypeInfo gameTypeInfo = (GameTypeInfo)reqConfig.getData();
		if (gameTypeInfo==null) {
			throw new IllegalArgumentException("Illegal argument");
		}
		DataTaskRequest<GameTypeInfo> taskRequest = new DataTaskRequest<GameTypeInfo>(
				new DataTaskListener<GameTypeInfo,GameTypeInfo>() {

			@Override
			public GameTypeInfo onGetCacheData() {
				alog.debug("get cache info");
				return null;
			}

			@Override
			public boolean isCacheDataExist(GameTypeInfo cacheData) {
				alog.debug("cache size:" + 0);
				return false;
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.fetchGameInfosFromGameType2(context, reqConfig, onGetHttpReqCallback());;
			}
			
			@Override
			public GameTypeInfo onCacheDataProcess(GameTypeInfo infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<GameTypeInfo> taskResult,
					GameTypeInfo result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
//		TaskQueue.getInstanse(context).add(taskRequest);
		
		String tag = DataConfig.REQUEST_TASK_TAG_PREFIX  + reqConfig.getReqCode()+"_"+gameTypeInfo.getTypeId();
		taskRequest.setTag(tag);
		reqConfig.setTaskId(tag);
		return taskRequest;
	}
	
	/**
	 * @description: 使用gameid获取游戏
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月5日 下午5:19:04
	 */
	public static DataTaskRequest fetchGameInfoFromGameId(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		final String gameId=(String)reqConfig.getData();
		if (TextUtils.isEmpty(gameId)) {
			throw new IllegalArgumentException("Illegal argument");
		}
		DataTaskRequest<List<GameInfo>> taskRequest = new DataTaskRequest<List<GameInfo>>(
				new DataTaskListener<List<GameInfo>,List<GameInfo>>() {

			@Override
			public List<GameInfo> onGetCacheData() {
				alog.debug("get cache info");
				return DaoHelper.getGameInfoFromGameId(context, gameId);
			}

			@Override
			public boolean isCacheDataExist(List<GameInfo> cacheData) {
				alog.debug("cache size:" + cacheData.size());
				return !cacheData.isEmpty();
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.fetchGameInfoFromGameId(context, reqConfig, onGetHttpReqCallback());
			}
			
			@Override
			public List<GameInfo> onCacheDataProcess(List<GameInfo> infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<List<GameInfo>> taskResult,
					List<GameInfo> result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
//		TaskQueue.getInstanse(context).add(taskRequest);
		return taskRequest;
	}
	
	/**
	 * @description: 获取新游推荐
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月5日 下午7:04:52
	 */
	public static DataTaskRequest fetchNewUploadGames(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		DataTaskRequest<List<GameInfo>> taskRequest = new DataTaskRequest<List<GameInfo>>(
				new DataTaskListener<List<GameInfo>,List<GameInfo>>() {

			@Override
			public List<GameInfo> onGetCacheData() {
				alog.debug("get cache info");
				return DaoHelper.getNewUploadToGame(context);
			}

			@Override
			public boolean isCacheDataExist(List<GameInfo> cacheData) {
				alog.debug("cache size:" + cacheData.size());
				return !cacheData.isEmpty();
			}

			@Override
			public void onFetchHttpData() {
					HttpHelper.fetchNewUploadGames(context, reqConfig, onGetHttpReqCallback());
			}
			
			@Override
			public List<GameInfo> onCacheDataProcess(List<GameInfo> infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<List<GameInfo>> taskResult,
					List<GameInfo> result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
//		TaskQueue.getInstanse(context).add(taskRequest);
		return taskRequest;
	}
	
	/**
	 * @description: 游戏排行
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午9:37:53
	 */
	public static DataTaskRequest fetchGameRanking(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		final Integer type=(Integer)reqConfig.getData();
		if (type==null) {
			throw new IllegalArgumentException("Illegal argument");
		}
		DataTaskRequest<List<GameInfo>> taskRequest = new DataTaskRequest<List<GameInfo>>(
				new DataTaskListener<List<GameInfo>,List<GameInfo>>() {

			@Override
			public List<GameInfo> onGetCacheData() {
				alog.debug("get cache info");
				return DaoHelper.getGameRanking(context, type, 1000000);
			}

			@Override
			public boolean isCacheDataExist(List<GameInfo> cacheData) {
				alog.debug("cache size:" + cacheData.size());
				return !cacheData.isEmpty();
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.fetchGameInfosFromRankingType(context, reqConfig, onGetHttpReqCallback());
			}
			
			@Override
			public List<GameInfo> onCacheDataProcess(List<GameInfo> infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<List<GameInfo>> taskResult,
					List<GameInfo> result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
//		TaskQueue.getInstanse(context).add(taskRequest);
		return taskRequest;
	}
	
	/**
	 * @description: 获取游戏专题
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月6日 下午9:25:25
	 */
	public static DataTaskRequest fetchGameTopic(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		DataTaskRequest<List<GameTopicInfo>> taskRequest = new DataTaskRequest<List<GameTopicInfo>>(
				new DataTaskListener<List<GameTopicInfo>,List<GameTopicInfo>>() {

			@Override
			public List<GameTopicInfo> onGetCacheData() {
				alog.debug("get cache info");
				return DaoHelper.getGameTopic(context);
			}

			@Override
			public boolean isCacheDataExist(List<GameTopicInfo> cacheData) {
				alog.debug("cache size:" + cacheData.size());
				return !cacheData.isEmpty();
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.fetchGameTopic(context, reqConfig, onGetHttpReqCallback());
			}
			
			@Override
			public List<GameTopicInfo> onCacheDataProcess(List<GameTopicInfo> infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<List<GameTopicInfo>> taskResult,
					List<GameTopicInfo> result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
//		TaskQueue.getInstanse(context).add(taskRequest);
		return taskRequest;
	}
	
	/**
	 * @description: 获取专题下的游戏
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月6日 下午9:36:54
	 */
	public static DataTaskRequest fetchGameInfosFromGameTopic(final Context context,
			final ReqConfig reqConfig) {
		GameTopicInfo gameTopicInfo=(GameTopicInfo)reqConfig.getData();
		if (gameTopicInfo==null) {
			throw new IllegalArgumentException("Illegal argument");
		}
		final int type=gameTopicInfo.getType();
		final String topicId=gameTopicInfo.getTopicId();
		alog.debug("");
		DataTaskRequest<List<TopicToGame>> taskRequest = new DataTaskRequest<List<TopicToGame>>(
				new DataTaskListener<List<TopicToGame>,List<TopicToGame>>() {

			@Override
			public List<TopicToGame> onGetCacheData() {
				alog.debug("get cache info");
				List<TopicToGame> topicToGameList=null;
				if(type==DataConfig.GAME_TYPE_COPYRIGHT){
					//版权游戏
					topicToGameList=DaoHelper.getTopicToGame(context, topicId);
				} else if(type==DataConfig.GAME_TYPE_THIRD){
					//第三方游戏
					topicToGameList=DaoHelper.getTopicToGameThird(context, topicId);
				}
				return topicToGameList;
			}

			@Override
			public boolean isCacheDataExist(List<TopicToGame> cacheData) {
				alog.debug("cache size:" + cacheData.size());
				return !cacheData.isEmpty();
			}

			@Override
			public void onFetchHttpData() {
				if(type==DataConfig.GAME_TYPE_COPYRIGHT){
					//版权游戏
					HttpHelper.fetchGameInfosFromGameTopic(context, reqConfig, onGetHttpReqCallback());
				} else if(type==DataConfig.GAME_TYPE_THIRD){
					//第三方游戏
					HttpHelper.fetchGameInfosFromGameTopicThird(context, reqConfig, onGetHttpReqCallback());
				}
			}
			
			@Override
			public List<TopicToGame> onCacheDataProcess(List<TopicToGame> infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<List<TopicToGame>> taskResult,
					List<TopicToGame> result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
//		TaskQueue.getInstanse(context).add(taskRequest);
		return taskRequest;
	}
	
	/**
	 * @description: 获取专题下的游戏
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月6日 下午9:36:54
	 */
	public static DataTaskRequest fetchGameInfosFromGameTopic2(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		GameTopicInfo gameTopicInfo = (GameTopicInfo)reqConfig.getData();
		if (gameTopicInfo==null) {
			throw new IllegalArgumentException("Illegal argument");
		}
		DataTaskRequest<GameTopicInfo> taskRequest = new DataTaskRequest<GameTopicInfo>(
				new DataTaskListener<GameTopicInfo,GameTopicInfo>() {

			@Override
			public GameTopicInfo onGetCacheData() {
				alog.debug("get cache info");
				return null;
			}

			@Override
			public boolean isCacheDataExist(GameTopicInfo cacheData) {
				alog.debug("cache size:" + 0);
				return false;
			}

			@Override
			public void onFetchHttpData() {
				GameTopicInfo gameTopicInfo = (GameTopicInfo)reqConfig.getData();
				int type = gameTopicInfo.getType();
				if(type==DataConfig.GAME_TYPE_COPYRIGHT){
					//版权游戏
					HttpHelper.fetchGameInfosFromGameTopic2(context, reqConfig, onGetHttpReqCallback());;
				} else if(type==DataConfig.GAME_TYPE_THIRD){
					//第三方游戏
					HttpHelper.fetchGameInfosFromGameTopicThird2(context, reqConfig, onGetHttpReqCallback());
				}
			}
			
			@Override
			public GameTopicInfo onCacheDataProcess(GameTopicInfo infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<GameTopicInfo > taskResult,
					GameTopicInfo result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
//		TaskQueue.getInstanse(context).add(taskRequest);
		String tag = DataConfig.REQUEST_TASK_TAG_PREFIX +  reqConfig.getReqCode()+"_"+gameTopicInfo.getTopicId();
		taskRequest.setTag(tag);
		reqConfig.setTaskId(tag);
		return taskRequest;
	}
	
	/**
	 * @description: 游戏拼音搜索
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月7日 下午2:26:58
	 */
	public static DataTaskRequest fetchGameSearchPinyinInfo(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		DataTaskRequest<List<GameSearchPinyinInfo>> taskRequest = new DataTaskRequest<List<GameSearchPinyinInfo>>(
				new DataTaskListener<List<GameSearchPinyinInfo>,List<GameSearchPinyinInfo>>() {

			@Override
			public List<GameSearchPinyinInfo> onGetCacheData() {
				alog.debug("get cache info");
				return DaoHelper.getAllGameSearchInfo(context, 1000000);
			}

			@Override
			public boolean isCacheDataExist(List<GameSearchPinyinInfo> cacheData) {
				alog.debug("cache size:" + cacheData.size());
				return !cacheData.isEmpty();
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.fetchGameSearchPinyinInfo(context, reqConfig, onGetHttpReqCallback());
			}
			
			@Override
			public List<GameSearchPinyinInfo> onCacheDataProcess(List<GameSearchPinyinInfo> infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<List<GameSearchPinyinInfo>> taskResult,
					List<GameSearchPinyinInfo> result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
//		TaskQueue.getInstanse(context).add(taskRequest);
		return taskRequest;
	}
	
	/**
	 * @description: 通过拼音搜索游戏
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月7日 下午2:39:48
	 */
	public static DataTaskRequest fetchGameSearchPinyinInfoByPinyin(final Context context, final ReqConfig reqConfig) {
		final String pinyin = (String) reqConfig.getData();
		if (TextUtils.isEmpty(pinyin)) {
			throw new IllegalArgumentException("modelId null");
		}
		alog.debug("pinyin:"+pinyin);
		DataTaskRequest<List<GameSearchPinyinInfo>> taskRequest = new DataTaskRequest<List<GameSearchPinyinInfo>>(
				new DataTaskListener<List<GameSearchPinyinInfo>,List<GameSearchPinyinInfo>>() {

			@Override
			public List<GameSearchPinyinInfo> onGetCacheData() {
				alog.debug("get cache info");
				return DaoHelper.getGameSearchInfoByPinyin(context, pinyin);
			}

			@Override
			public boolean isCacheDataExist(List<GameSearchPinyinInfo> cacheData) {
				alog.debug("cache size:" + cacheData.size());
				return true;
			}

			@Override
			public void onFetchHttpData() {
			}
			
			@Override
			public List<GameSearchPinyinInfo> onCacheDataProcess(List<GameSearchPinyinInfo> infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<List<GameSearchPinyinInfo>> taskResult,
					List<GameSearchPinyinInfo> result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
//		TaskQueue.getInstanse(context).add(taskRequest);
		return taskRequest;
	}
	
	/**
	 * @description: 获取游戏中心数据
	 *
	 * @param context
	 * @param reqConfig 
	 * @throws: 
	 * @author: LiuQin
	 * @date: 2015年7月9日 上午10:32:49
	 */
//	public static void getGameCenterInfos(final Context context,
//			final ReqConfig reqConfig) {
//		final String modelId = (String) reqConfig.getData();
//		if (TextUtils.isEmpty(modelId)) {
//			throw new IllegalArgumentException("modelId null");
//		}
//		alog.debug("modelId:"+modelId);
//		TaskRequest taskRequest = new TaskRequest(
//				new TaskListener<TaskResult>() {
//					@Override
//					public TaskResult doTaskInBackground() {
//						alog.debug("get cache adModelInfos");
//						List<AdModelInfo> adModelInfos = DaoHelper.getAdModelInfo(context, modelId);
//						alog.debug("cache adModelInfos size:" + adModelInfos.size());
//						if (!adModelInfos.isEmpty()) {
//							onSuccessCallback(context, reqConfig, adModelInfos);
//						} else {
//							HttpHelper.fetchGameCenterInfo(context, reqConfig, new HttpReqCallback<List<AdModelInfo>>() {
//								@Override
//								public void onResult(Context context, ReqConfig reqConfig,
//										TaskResult<List<AdModelInfo>> taskResult) {
//									if(taskResult.getCode() == TaskResult.OK){
//										onSuccessCallback(context, reqConfig, taskResult.getData());
//									} else {
//										DataHelper.onCallback(context, reqConfig, taskResult);
//									}
//								}
//							});
//						}
//						return null;
//					}
//					
//					private void onSuccessCallback(Context context, ReqConfig reqConfig, List<AdModelInfo> infos){
//						DataHelper.onCallback(context, reqConfig,
//								TaskResult.makeSuccessTaskResult(infos.get(0).getAds()));
//					}
//				});
//		TaskQueue.getInstanse(context).add(taskRequest);
////		return taskRequest;
//	}
	
	/**
	 * @description: 获取游戏中心数据
	 *
	 * @param context
	 * @param reqConfig 
	 * @throws: 
	 * @author: LiuQin
	 * @date: 2015年7月9日 上午10:32:49
	 */
	public static DataTaskRequest getGameCenterInfos2(final Context context,
			final ReqConfig reqConfig) {
		final String modelId = (String) reqConfig.getData();
		if (TextUtils.isEmpty(modelId)) {
			throw new IllegalArgumentException("modelId null");
		}
		alog.debug("modelId:"+modelId);
		DataTaskRequest<List<AdModelInfo>> taskRequest = new DataTaskRequest<List<AdModelInfo>>(
				new DataTaskListener<List<AdModelInfo>,List<AdInfo>>() {

			@Override
			public List<AdModelInfo> onGetCacheData() {
				alog.debug("get cache info");
				return DaoHelper.getAdModelInfo(context, modelId);
			}

			@Override
			public boolean isCacheDataExist(List<AdModelInfo> cacheData) {
				alog.debug("cache size:" + cacheData.size());
				return !cacheData.isEmpty();
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.fetchGameCenterInfo(context, reqConfig, onGetHttpReqCallback());
			}
			
			@Override
			public List<AdInfo> onCacheDataProcess(List<AdModelInfo> infos) {
//				return infos.get(infos.size()-1).getAds();
				long curTime = TimeHelper.getCurTime();
				long time = 0;
				long lastTime = Long.MAX_VALUE;
				long lastUpdateTime = -1;
				AdModelInfo lastAdModelInfo = null;
				for (AdModelInfo adModelInfo : infos) {
					if(adModelInfo.getStartTime() <= curTime && adModelInfo.getEndTime() >= curTime){
						time = curTime - adModelInfo.getStartTime();
						if(time < lastTime){
							// startTime较大
							lastAdModelInfo = adModelInfo;
							lastTime = time;
							lastUpdateTime = adModelInfo.getUpdateTime();
						} else if(time == lastTime && adModelInfo.getUpdateTime() > lastUpdateTime){
							// startTime相同,再比较updateTime
							lastAdModelInfo = adModelInfo;
							lastTime = time;
							lastUpdateTime = adModelInfo.getUpdateTime();
						}
					}
				}
				if(lastAdModelInfo == null){
					lastAdModelInfo = infos.get(infos.size()-1);
				}
				
				List<ModelToAd> modelToAdList = lastAdModelInfo.getModelToAdList();
				List<AdInfo> adInfoList = new ArrayList<AdInfo>();
				AdInfo adInfo;
				Integer onlineCount;
				int tempCount;
				if(modelToAdList!=null && modelToAdList.size()>0){
					for (ModelToAd modelToAd : modelToAdList) {
						adInfo = modelToAd.getAdInfo();
						onlineCount = adInfo.getOnline();
						if(onlineCount==null){
							adInfo.setOnline(1000);
						} else {
							tempCount= (int)(onlineCount * 0.1f);
							tempCount = DaoHelper.getRandomInt(tempCount);
							if(SystemClock.uptimeMillis() % 2 == 0){
								tempCount = -tempCount;
							}
							onlineCount += tempCount;
							adInfo.setOnline(onlineCount);
						}
						adInfoList.add(adInfo);
					}
				}
				return adInfoList;
			}

			@Override
			public void onResultCallback(TaskResult<List<AdInfo>> taskResult,
					List<AdInfo> result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
//		TaskQueue.getInstanse(context).add(taskRequest);
		return taskRequest;
	}
	
	/**
	 * @description: 开机广告
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月9日 下午9:27:47
	 */
	public static DataTaskRequest getLaunchAdInfos(final Context context,
			final ReqConfig reqConfig) {
		final String modelId = (String) reqConfig.getData();
		if (TextUtils.isEmpty(modelId)) {
			throw new IllegalArgumentException("modelId null");
		}
		alog.debug("modelId:"+modelId);
		DataTaskRequest<List<AdModelInfo>> taskRequest = new DataTaskRequest<List<AdModelInfo>>(
				new DataTaskListener<List<AdModelInfo>,List<String>>() {

			@Override
			public List<AdModelInfo> onGetCacheData() {
				alog.debug("get cache info");
				return DaoHelper.getAdModelInfo(context, modelId);
			}

			@Override
			public boolean isCacheDataExist(List<AdModelInfo> cacheData) {
				alog.debug("cache size:" + cacheData.size());
				return !cacheData.isEmpty();
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.fetchGameCenterInfo(context, reqConfig, onGetHttpReqCallback());
			}
			
			@Override
			public List<String> onCacheDataProcess(List<AdModelInfo> infos) {
				long curTime = TimeHelper.getCurTime();
				long time = 0;
				long lastTime = Long.MAX_VALUE;
				long lastUpdateTime = -1;
				AdModelInfo lastAdModelInfo = null;
				List<String> urlList = null;
				for (AdModelInfo adModelInfo : infos) {
					if(adModelInfo.getStartTime() <= curTime && adModelInfo.getEndTime() >= curTime){
						time = curTime - adModelInfo.getStartTime();
						if(time < lastTime){
							// startTime较大
							lastAdModelInfo = adModelInfo;
							lastTime = time;
							lastUpdateTime = adModelInfo.getUpdateTime();
						} else if(time == lastTime && adModelInfo.getUpdateTime() > lastUpdateTime){
							// startTime相同,再比较updateTime
							lastAdModelInfo = adModelInfo;
							lastTime = time;
							lastUpdateTime = adModelInfo.getUpdateTime();
						}
					}
				}
				if(lastAdModelInfo!=null){
					urlList = new ArrayList<String>();
					List<ModelToAd> modelToAdList = lastAdModelInfo.getModelToAdList();
					for (ModelToAd modelToAd : modelToAdList) {
						urlList.add(modelToAd.getAdInfo().getUrl());
					}
				}
				return urlList;
			}

			@Override
			public void onResultCallback(TaskResult<List<String>> taskResult,
					List<String> result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}
			
		});
//		TaskQueue.getInstanse(context).add(taskRequest);
		return taskRequest;
	}
	
	/**
	 * @description: 活动
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月9日 下午9:33:39
	 */
	public static DataTaskRequest getActInfos(final Context context,
			final ReqConfig reqConfig) {
		final Integer type = (Integer) reqConfig.getData();
		if (type == null || (type!=DataConfig.ACTIVITY_TYPE_ALL && type!=DataConfig.ACTIVITY_TYPE_RECOMMEND)) {
			throw new IllegalArgumentException("type null");
		}
		alog.debug("type:"+type);
		DataTaskRequest<List<ActInfo>> taskRequest = new DataTaskRequest<List<ActInfo>>(
				new DataTaskListener<List<ActInfo>,List<ActInfo>>() {
 
			@Override
			public List<ActInfo> onGetCacheData() {
				alog.debug("get cache info");
				if(type == DataConfig.ACTIVITY_TYPE_RECOMMEND){
					return DaoHelper.getRecommendActivityInfo(context, type);
				}
				return DaoHelper.getAllActivityInfo(context);
			}

			@Override
			public boolean isCacheDataExist(List<ActInfo> cacheData) {
				alog.debug("cache size:" + cacheData.size());
				boolean result = !cacheData.isEmpty();
				if(!result && type == DataConfig.ACTIVITY_TYPE_RECOMMEND && DaoHelper.getAllActivityInfoCount(context)>0){
					//推荐的活动是空的
					result = true;
				}
				return result;
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.fetchActivityInfos(context, reqConfig, onGetHttpReqCallback());
			}
			
			@Override
			public List<ActInfo> onCacheDataProcess(List<ActInfo> infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<List<ActInfo>> taskResult,
					List<ActInfo> result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
//		TaskQueue.getInstanse(context).add(taskRequest);
		return taskRequest;
	}
	
	/**
	 * @description: 商品
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月9日 下午10:25:30
	 */
	public static DataTaskRequest getGoodsInfos(final Context context,
			final ReqConfig reqConfig) {
		final Integer type = (Integer) reqConfig.getData();
		if (type == null || (type!=DataConfig.GOODS_TYPE_ALL && type!=DataConfig.GOODS_TYPE_RECOMMEND)) {
			throw new IllegalArgumentException("type null");
		}
		alog.debug("type:"+type);
		DataTaskRequest<List<GoodsInfo>> taskRequest = new DataTaskRequest<List<GoodsInfo>>(
				new DataTaskListener<List<GoodsInfo>,List<GoodsInfo>>() {
 
			@Override
			public List<GoodsInfo> onGetCacheData() {
				alog.debug("get cache info");
				if(type == DataConfig.GOODS_TYPE_RECOMMEND){
					return DaoHelper.getRecommendGoodsInfo(context, type);
				}
				return DaoHelper.getAllGoodsInfo(context);
			}

			@Override
			public boolean isCacheDataExist(List<GoodsInfo> cacheData) {
				alog.debug("cache size:" + cacheData.size());
				boolean result = !cacheData.isEmpty();
				if(!result && type == DataConfig.GOODS_TYPE_RECOMMEND && DaoHelper.getAllGoodsInfoCount(context)>0){
					//推荐的商品是空的
					result = true;
				}
				return result;
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.fetchGoodsInfos(context, reqConfig, onGetHttpReqCallback());
			}
			
			@Override
			public List<GoodsInfo> onCacheDataProcess(List<GoodsInfo> infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<List<GoodsInfo>> taskResult,
					List<GoodsInfo> result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
//		TaskQueue.getInstanse(context).add(taskRequest);
		return taskRequest;
	}
	
	/**
	 * @description: 礼包
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月9日 下午10:26:10
	 */
	public static DataTaskRequest getGameGiftInfos(final Context context,
			final ReqConfig reqConfig) {
		alog.debug("");
		DataTaskRequest<List<GameGiftInfo>> taskRequest = new DataTaskRequest<List<GameGiftInfo>>(
				new DataTaskListener<List<GameGiftInfo>,List<GameGiftInfo>>() {
 
			@Override
			public List<GameGiftInfo> onGetCacheData() {
				alog.debug("get cache info");
				return DaoHelper.getGameGiftInfo(context);
			}

			@Override
			public boolean isCacheDataExist(List<GameGiftInfo> cacheData) {
				alog.debug("cache size:" + cacheData.size());
				return !cacheData.isEmpty();
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.fetchGameGiftInfos(context, reqConfig, onGetHttpReqCallback());
			}
			
			@Override
			public List<GameGiftInfo> onCacheDataProcess(List<GameGiftInfo> infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<List<GameGiftInfo>> taskResult,
					List<GameGiftInfo> result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
//		TaskQueue.getInstanse(context).add(taskRequest);
		return taskRequest;
	}
	
	/**
	 * @description: 用户领取的礼包
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月9日 下午10:59:07
	 */
	public static DataTaskRequest getUserGiftInfos(final Context context,
			final ReqConfig reqConfig) {
		alog.debug("");
		final String userId = (String)reqConfig.getData();
		DataTaskRequest<List<UserGameGiftInfo>> taskRequest = new DataTaskRequest<List<UserGameGiftInfo>>(
				new DataTaskListener<List<UserGameGiftInfo>,List<UserGameGiftInfo>>() {
 
			@Override
			public List<UserGameGiftInfo> onGetCacheData() {
				alog.debug("get cache info");
//				return DaoHelper.getUserGameGiftInfo(context);
				return null;
			}

			@Override
			public boolean isCacheDataExist(List<UserGameGiftInfo> cacheData) {
//				alog.debug("cache size:" + cacheData.size());
//				return !cacheData.isEmpty();
				return false;
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.fetchUserGiftInfos(context, reqConfig, onGetHttpReqCallback());
			}
			
			@Override
			public List<UserGameGiftInfo> onCacheDataProcess(List<UserGameGiftInfo> infos) {
				if(infos != null){
					for (UserGameGiftInfo userGameGiftInfo : infos) {
						List<UserGameToGift> userGameToGiftList = userGameGiftInfo.getUserGameToGiftList();
						if(userGameToGiftList != null){
							for (UserGameToGift userGameToGift : userGameToGiftList) {
								userGameToGift.getUserGiftInfo();
							}
						}
					}
				}
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<List<UserGameGiftInfo>> taskResult,
					List<UserGameGiftInfo> result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
//		TaskQueue.getInstanse(context).add(taskRequest);
		return taskRequest;
	}
	
	/**
	 * @description: 领取礼包
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月10日 上午10:40:11
	 */
	public static DataTaskRequest obtainUserGift(final Context context,
			final ReqConfig reqConfig) {
		alog.debug("");
		ObtainUserGiftReq obtainUserGiftReq= (ObtainUserGiftReq)reqConfig.getData();
		if (obtainUserGiftReq==null || TextUtils.isEmpty(obtainUserGiftReq.getUserId()) 
				|| TextUtils.isEmpty(obtainUserGiftReq.getGiftPackageid())) {
			throw new IllegalArgumentException("illegal argument");
		}
		DataTaskRequest<String> taskRequest = new DataTaskRequest<String>(
				new DataTaskListener<String,String>() {
 
			@Override
			public String onGetCacheData() {
				return null;
			}

			@Override
			public boolean isCacheDataExist(String cacheData) {
				return false;
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.obtainUserGift(context, reqConfig, onGetHttpReqCallback());
			}
			
			@Override
			public String onCacheDataProcess(String infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<String> taskResult,
					String result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
//		TaskQueue.getInstanse(context).add(taskRequest);
		return taskRequest;
	}
	
	/**
	 * @description: 公告
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月10日 上午11:46:19
	 */
	public static DataTaskRequest getNoticeInfos(final Context context,
			final ReqConfig reqConfig) {
		alog.debug("");
		final Integer noticeType = (Integer)reqConfig.getData();
		if (noticeType==null || noticeType<DataConfig.NOTICE_TYPE_NEWS || noticeType>DataConfig.NOTICE_TYPE_EMERGENCY) {
			throw new IllegalArgumentException("Illegal argument");
		}
		DataTaskRequest<List<NoticeInfo>> taskRequest = new DataTaskRequest<List<NoticeInfo>>(
				new DataTaskListener<List<NoticeInfo>,List<NoticeInfo>>() {
 
			@Override
			public List<NoticeInfo> onGetCacheData() {
				alog.debug("get cache info");
				return DaoHelper.getNoticeInfo(context, noticeType);
			}

			@Override
			public boolean isCacheDataExist(List<NoticeInfo> cacheData) {
				alog.debug("cache size:" + cacheData.size());
				return !cacheData.isEmpty();
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.fetchNoticeInfos(context, reqConfig, onGetHttpReqCallback());
			}
			
			@Override
			public List<NoticeInfo> onCacheDataProcess(List<NoticeInfo> infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<List<NoticeInfo>> taskResult,
					List<NoticeInfo> result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
//		TaskQueue.getInstanse(context).add(taskRequest);
		return taskRequest;
	}
	
	/**
	 * @description: 获取教学视频
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午1:20:36
	 */
	public static DataTaskRequest getVideoInfos(final Context context,
			final ReqConfig reqConfig) {
		alog.debug("");
		final String videoType = (String)reqConfig.getData();
		if (videoType==null || videoType!=DataConfig.VIDEO_TYPE_GUIDE) {
			throw new IllegalArgumentException("Illegal argument");
		}
		DataTaskRequest<List<VideoInfo>> taskRequest = new DataTaskRequest<List<VideoInfo>>(
				new DataTaskListener<List<VideoInfo>,List<VideoInfo>>() {
 
			@Override
			public List<VideoInfo> onGetCacheData() {
				alog.debug("get cache info");
				return DaoHelper.getVideoInfo(context, videoType);
			}

			@Override
			public boolean isCacheDataExist(List<VideoInfo> cacheData) {
				alog.debug("cache size:" + cacheData.size());
				return !cacheData.isEmpty();
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.fetchVideoInfos(context, reqConfig, onGetHttpReqCallback());
			}
			
			@Override
			public List<VideoInfo> onCacheDataProcess(List<VideoInfo> infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<List<VideoInfo>> taskResult,
					List<VideoInfo> result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
//		TaskQueue.getInstanse(context).add(taskRequest);
		return taskRequest;
	}
	
	/**
	 * @description: 游戏评分
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午2:24:29
	 */
	public static DataTaskRequest rateGame(final Context context,
			final ReqConfig reqConfig) {
		alog.debug("");
		final RateGameReq rateGameReq= (RateGameReq)reqConfig.getData();
		if (rateGameReq==null || TextUtils.isEmpty(rateGameReq.getUserId()) 
				|| TextUtils.isEmpty(rateGameReq.getGameId()) || rateGameReq.getScore()<0 ) {
			throw new IllegalArgumentException("illegal argument");
		}
		DataTaskRequest<String> taskRequest = new DataTaskRequest<String>(
				new DataTaskListener<String,String>() {
 
			@Override
			public String onGetCacheData() {
				return "OK";
			}

			@Override
			public boolean isCacheDataExist(String cacheData) {
				return DaoHelper.isGameRated(context, rateGameReq.getGameId(), rateGameReq.getUserId());
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.rateGame(context, reqConfig, onGetHttpReqCallback());
			}
			
			@Override
			public String onCacheDataProcess(String infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<String> taskResult,
					String result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
//		TaskQueue.getInstanse(context).add(taskRequest);
		return taskRequest;
	}
	
	/**
	 * @description: 使用gameid获取第三方游戏
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月5日 下午5:19:04
	 */
	public static DataTaskRequest fetchThirdGameInfoFromGameId(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		final String gameId=(String)reqConfig.getData();
		if (TextUtils.isEmpty(gameId)) {
			throw new IllegalArgumentException("Illegal argument");
		}
		DataTaskRequest<List<ThirdGameInfo>> taskRequest = new DataTaskRequest<List<ThirdGameInfo>>(
				new DataTaskListener<List<ThirdGameInfo>,List<ThirdGameInfo>>() {

			@Override
			public List<ThirdGameInfo> onGetCacheData() {
				alog.debug("get cache info");
				return DaoHelper.getThirdGameInfoFromGameId(context, gameId);
			}

			@Override
			public boolean isCacheDataExist(List<ThirdGameInfo> cacheData) {
				alog.debug("cache size:" + cacheData.size());
				return !cacheData.isEmpty();
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.fetchThirdGameInfoFromGameId(context, reqConfig, onGetHttpReqCallback());
			}
			
			@Override
			public List<ThirdGameInfo> onCacheDataProcess(List<ThirdGameInfo> infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<List<ThirdGameInfo>> taskResult,
					List<ThirdGameInfo> result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
//		TaskQueue.getInstanse(context).add(taskRequest);
		return taskRequest;
	}
	
	/**
	 * @description: 获取与gameId同分类下的其它游戏
	 *
	 * @param context
	 * @param gameId
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月14日 上午11:57:05
	 */
	public static DataTaskRequest fetchRelativeGames(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		DataTaskRequest<List<GameInfo>> taskRequest = new DataTaskRequest<List<GameInfo>>(
				new DataTaskListener<List<GameInfo>,List<GameInfo>>() {

			@Override
			public List<GameInfo> onGetCacheData() {
				alog.debug("get cache info");
				String gameId = (String)reqConfig.getData();
				return DaoHelper.getRelativeGameInfo(context, gameId);
			}

			@Override
			public boolean isCacheDataExist(List<GameInfo> cacheData) {
				alog.debug("cache size:" + cacheData.size());
				return cacheData.size()>0;
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.fetchRelativeGames(context, reqConfig, onGetHttpReqCallback());
			}
			
			@Override
			public List<GameInfo> onCacheDataProcess(List<GameInfo> infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<List<GameInfo>> taskResult,
					List<GameInfo> result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
		return taskRequest;
	}
	
	/**
	 * @description: 获取推荐的游戏搜索
	 *
	 * @param context
	 * @param reqConfig
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月14日 下午2:33:31
	 */
	public static DataTaskRequest fetchRecommendGameSearchPinyinInfo(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		DataTaskRequest<List<GameSearchPinyinInfo>> taskRequest = new DataTaskRequest<List<GameSearchPinyinInfo>>(
				new DataTaskListener<List<GameSearchPinyinInfo>,List<GameSearchPinyinInfo>>() {

			@Override
			public List<GameSearchPinyinInfo> onGetCacheData() {
				alog.debug("get cache info");
				return DaoHelper.getRecommendGameSearchInfo(context);
			}

			@Override
			public boolean isCacheDataExist(List<GameSearchPinyinInfo> cacheData) {
				alog.debug("cache size:" + cacheData.size());
				return !cacheData.isEmpty();
			}

			@Override
			public void onFetchHttpData() {
				onResultCallback(null, null);
			}
			
			@Override
			public List<GameSearchPinyinInfo> onCacheDataProcess(List<GameSearchPinyinInfo> infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<List<GameSearchPinyinInfo>> taskResult,
					List<GameSearchPinyinInfo> result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
		return taskRequest;
	}
	
	/**
	 * @description: 任务
	 *
	 * @param context
	 * @param reqConfig
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月18日 下午3:29:19
	 */
	public static DataTaskRequest fetchUserTask(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		UserTaskReq reqInfo = (UserTaskReq)reqConfig.getData();
		if (reqInfo == null) {
			throw new IllegalArgumentException("Illegal argument");
		}
		DataTaskRequest<UserTaskResp> taskRequest = new DataTaskRequest<UserTaskResp>(
				new DataTaskListener<UserTaskResp,UserTask>() {

			@Override
			public UserTaskResp onGetCacheData() {
				alog.debug("get cache info");
				return null;
			}

			@Override
			public boolean isCacheDataExist(UserTaskResp cacheData) {
//				alog.debug("cache size:" + cacheData.size());
				return cacheData!=null;
			}

			@Override
			public void onFetchHttpData() {
				UserTaskReq reqInfo = (UserTaskReq)reqConfig.getData();
				HttpHelper.fetchUserTask(context, reqConfig, reqInfo, onGetHttpReqCallback());
			}
			
			@Override
			public UserTask onCacheDataProcess(UserTaskResp info) {
				UserTask userTask = new UserTask();
				UserTaskReq reqInfo = (UserTaskReq)reqConfig.getData();
				List<UserTaskList> taskList = info.getTaskList();
				List<RewardRuleInner> rewardRuleList;
				RewardRuleInner rewardRule = null;
				UserTaskInfo userTaskInfo;
				UserTaskState taskState;
				long startTime = 0;
				long endTime = 0;
				int targetTime;
				String firstGameId;
				boolean isSubTask;
				for (UserTaskList task : taskList) {
					userTaskInfo = null;
					isSubTask = false;
					rewardRuleList = task.getRewardRule();
					if(rewardRuleList!=null && rewardRuleList.size()>0){
						
						if(task.getState() == UserTaskResp.TASK_STATE_FINISH){
							taskState = UserTaskState.TASK_STATE_FINISH;
						} else if(task.getState() == UserTaskResp.TASK_STATE_STANDBY){
							taskState = UserTaskState.TASK_STATE_STANDBY;
						} else {
							taskState = UserTaskState.TASK_STATE_NOT_FINISH;
						}
						
						rewardRule = rewardRuleList.get(0);
						targetTime = rewardRule.getLow();
						firstGameId = getFirstGameId(task);
						
						if(UserTaskInfo.TASK_TYPE_GAME_DOWNLOAD.equals(task.getLogo())){
							if(TextUtils.isEmpty(firstGameId)){
								continue;
							}
							if(taskState == UserTaskState.TASK_STATE_STANDBY){
								taskState = UserTaskState.TASK_STATE_NOT_FINISH;
							}
							//游戏下载
							userTaskInfo = UserTaskGenerator.makeGameDownloadTask(
									context, reqInfo.getUserId(), task.getTaskId(), task.getTaskName(), 
									task.getRemark(), "", taskState, targetTime, startTime, endTime,firstGameId);
							
							isSubTask = true;
							if(userTask.getUserTaskInfo(userTaskInfo.getTaskType()) == null){
								UserTaskInfo rootUserTask = new UserTaskInfo();
								rootUserTask.setTaskType(userTaskInfo.getTaskType());
								rootUserTask.setTaskName(userTaskInfo.getTaskName());
								userTask.putUserTaskInfo(rootUserTask.getTaskType(), rootUserTask);
							}
						} else if(UserTaskInfo.TASK_TYPE_GAME_SCORE.equals(task.getLogo())){
							if(taskState == UserTaskState.TASK_STATE_STANDBY){
								taskState = UserTaskState.TASK_STATE_NOT_FINISH;
							}
							//游戏评分
							userTaskInfo = UserTaskGenerator.makeGameScoreTask(
									context, reqInfo.getUserId(), task.getTaskId(), task.getTaskName(), 
									task.getRemark(), "", taskState, targetTime, startTime, endTime);
						} else if(UserTaskInfo.TASK_TYPE_WATCH_VIDEO.equals(task.getLogo())){
							if(TextUtils.isEmpty(firstGameId)){
								continue;
							}
							if(taskState == UserTaskState.TASK_STATE_STANDBY){
								taskState = UserTaskState.TASK_STATE_NOT_FINISH;
							}
							//观看视频
							userTaskInfo = UserTaskGenerator.makeWatchVideoTask(
									context, reqInfo.getUserId(), task.getTaskId(), task.getTaskName(), 
									task.getRemark(), "", taskState, targetTime, startTime, endTime, firstGameId);
							isSubTask = true;
							if(userTask.getUserTaskInfo(userTaskInfo.getTaskType()) == null){
								UserTaskInfo rootUserTask = new UserTaskInfo();
								rootUserTask.setTaskType(userTaskInfo.getTaskType());
								rootUserTask.setTaskName(userTaskInfo.getTaskName());
								userTask.putUserTaskInfo(rootUserTask.getTaskType(), rootUserTask);
							}
						} else if(UserTaskInfo.TASK_TYPE_GAME_SEARCH.equals(task.getLogo())){
							//游戏搜索
							if(taskState == UserTaskState.TASK_STATE_STANDBY){
								taskState = UserTaskState.TASK_STATE_NOT_FINISH;
							}
							userTaskInfo = UserTaskGenerator.makeGameSearchTask(
									context, reqInfo.getUserId(), task.getTaskId(), task.getTaskName(), 
									task.getRemark(), "", taskState, targetTime, startTime, endTime);
						} else if(UserTaskInfo.TASK_TYPE_FIRST_REGEDISTER.equals(task.getLogo())){
							//首次注册
							userTaskInfo = UserTaskGenerator.makeFirstRegisterTask(
									context, reqInfo.getUserId(), task.getTaskId(), task.getTaskName(), 
									task.getRemark(), "", taskState);
						} else if(UserTaskInfo.TASK_TYPE_WECHAT_FIRST_LOGIN.equals(task.getLogo())){
							//微信首次登录
							if(taskState == UserTaskState.TASK_STATE_STANDBY){
								taskState = UserTaskState.TASK_STATE_NOT_FINISH;
							}
							userTaskInfo = UserTaskGenerator.makeWeChatFirstLoginTask(
									context, reqInfo.getUserId(), task.getTaskId(), task.getTaskName(), 
									task.getRemark(), "", taskState);
						} else if(UserTaskInfo.TASK_TYPE_FIRST_RECHARGE.equals(task.getLogo())
								&& UserTaskInfo.TASK_COMPLETE_WAY_ALLFIRST.equals(task.getCompleteWay())){
							//首次充值
							userTaskInfo = UserTaskGenerator.makeFirstRechargeTask(
									context, reqInfo.getUserId(), task.getTaskId(), task.getTaskName(), 
									task.getRemark(), "", taskState);
						} else if(UserTaskInfo.TASK_TYPE_RECHARGE_SUM.equals(task.getLogo())
								&& UserTaskInfo.TASK_COMPLETE_WAY_ADD.equals(task.getCompleteWay())){
							//累计充值
							userTaskInfo = UserTaskGenerator.makeRechargeSumTask(
									context, reqInfo.getUserId(), task.getTaskId(), task.getTaskName(), 
									task.getRemark(), "", taskState, targetTime);
						} else if(UserTaskInfo.TASK_TYPE_EXCHANGE_GOODS.equals(task.getLogo())){
							//兑换商品
							userTaskInfo = UserTaskGenerator.makeExchangeGoodsTask(
									context, reqInfo.getUserId(), task.getTaskId(), task.getTaskName(), 
									task.getRemark(), "", taskState, targetTime);
						} else if(UserTaskInfo.TASK_TYPE_CHECKIN_DAILY.equals(task.getLogo())){
							//每日签到
							userTaskInfo = UserTaskGenerator.makeCheckInDailyTask(
									context, reqInfo.getUserId(), task.getTaskId(), task.getTaskName(), 
									task.getRemark(), "", taskState);
						} else if(UserTaskInfo.TASK_TYPE_MARKET_LOGIN.equals(task.getLogo())){							
							//连续登录
							List<Integer> integralList = new ArrayList<Integer>();
//							Map<Integer,Integer> integralMap = new HashMap<Integer, Integer>();
							int signValue = 0;
							int ruleSize = rewardRuleList.size();
							boolean isFindFirstRule = false;
							rewardRule = null;
							for (int i = 1; i < 8; i++) {
								if(i <= ruleSize){
									if(UserTaskResp.TASK_RULE_STATE_FINISH == rewardRuleList.get(i-1).getState()) {
										signValue |= (0x1 << (i-1));
									} else {
										if(!isFindFirstRule){
											isFindFirstRule = true;
											rewardRule = rewardRuleList.get(i-1);
										}
									}
									integralList.add(rewardRuleList.get(i-1).getIntegral());
								} else {
									integralList.add(0);
								}
							}
							CheckInTaskInfo checkTaskInfo = UserTaskGenerator.makeCheckInContinuelyTask(
									context, reqInfo.getUserId(), task.getTaskId(), task.getTaskName(), 
									task.getRemark(), "", taskState);
							checkTaskInfo.setSignValue(signValue);
							checkTaskInfo.setIntegralList(integralList);
							userTaskInfo = checkTaskInfo;
						} else if(UserTaskInfo.TASK_TYPE_MARKET_ONLINE.equals(task.getLogo())){
							//大厅在线时长
							userTaskInfo = UserTaskGenerator.makeMarketOnlineTask(
									context, reqInfo.getUserId(), task.getTaskId(), task.getTaskName(), 
									task.getRemark(), "", taskState, targetTime, startTime, endTime);
						} else if(UserTaskInfo.TASK_TYPE_GAME_ONLINE.equals(task.getLogo())){
							//游戏在线时长
							userTaskInfo = UserTaskGenerator.makeGameOnlineTask(
									context, reqInfo.getUserId(), task.getTaskId(), task.getTaskName(), 
									task.getRemark(), "", taskState, targetTime, startTime, endTime, firstGameId);
							isSubTask = true;
							if(userTask.getUserTaskInfo(userTaskInfo.getTaskType()) == null){
								UserTaskInfo rootUserTask = new UserTaskInfo();
								rootUserTask.setTaskType(userTaskInfo.getTaskType());
								rootUserTask.setTaskName(userTaskInfo.getTaskName());
								userTask.putUserTaskInfo(rootUserTask.getTaskType(), rootUserTask);
							}
						} else if(UserTaskInfo.TASK_TYPE_GAME_LOGIN.equals(task.getLogo())){
							//游戏登录
							userTaskInfo = UserTaskGenerator.makeGameLoginTask(
									context, reqInfo.getUserId(), task.getTaskId(), task.getTaskName(), 
									task.getRemark(), "", taskState, targetTime, startTime, endTime, firstGameId);
							isSubTask = true;
							if(userTask.getUserTaskInfo(userTaskInfo.getTaskType()) == null){
								UserTaskInfo rootUserTask = new UserTaskInfo();
								rootUserTask.setTaskType(userTaskInfo.getTaskType());
								rootUserTask.setTaskName(userTaskInfo.getTaskName());
								userTask.putUserTaskInfo(rootUserTask.getTaskType(), rootUserTask);
							}
						}
					}
					
					if(userTaskInfo!=null){
						if(rewardRule!=null){
							userTaskInfo.setRuleId(rewardRule.getRuleId());
							userTaskInfo.setIntegral(rewardRule.getIntegral());
							userTaskInfo.setCoupon(rewardRule.getCoupon());
						}
						
						if(isSubTask){
							UserTaskInfo userTaskInfoInMap = userTask.getUserTaskInfo(userTaskInfo.getTaskType());
							List<UserTaskInfo> subTaskInfos = userTaskInfoInMap.getSubTaskInfos();
							if(subTaskInfos==null){
								subTaskInfos = new ArrayList<UserTaskInfo>();
								userTaskInfoInMap.setSubTaskInfos(subTaskInfos);
							}
							subTaskInfos.add(userTaskInfo);
						} else {
							userTask.putUserTaskInfo(userTaskInfo.getTaskType(), userTaskInfo);
						}
					}
				}
				return userTask;
			}
			
			private String getFirstGameId(UserTaskList task){
				String gameId = null;
				if(task!=null && task.getGameIds()!=null && task.getGameIds().size()>0){
					gameId = task.getGameIds().get(0).getGameId();
				}
				return gameId;
			}
			
			private List<UserTask> sortUserTask(UserTask userTask){
				List<UserTask> userTaskList = new ArrayList<UserTask>();
				UserTask userTask2 = new UserTask();
				UserTaskInfo userTaskInfo;
				UserTaskType userTaskType;
				Map<UserTaskType, UserTaskInfo> userTaskInfoMap = userTask.getUserTaskInfoMap();
				Iterator<Map.Entry<UserTaskType, UserTaskInfo>> entries = userTaskInfoMap.entrySet().iterator();  
				while (entries.hasNext()) {  
					Map.Entry<UserTaskType, UserTaskInfo> entry = entries.next();  
					userTaskType = entry.getKey();
					userTaskInfo = entry.getValue();
					
					if(isNotCommonTask(userTaskType)){
						userTask2.putUserTaskInfo(userTaskType, userTaskInfo);
						entries.remove();
					}
				}
				
				userTaskList.add(userTask);
				userTaskList.add(userTask2);
				
				return userTaskList;
			}
			
			private boolean isNotCommonTask(UserTaskType userTaskType){
				return userTaskType == UserTaskType.TASK_TYPE_FIRST_REGEDISTER
						|| userTaskType == UserTaskType.TASK_TYPE_WECHAT_FIRST_LOGIN
						|| userTaskType == UserTaskType.TASK_TYPE_FIRST_RECHARGE
						|| userTaskType == UserTaskType.TASK_TYPE_RECHARGE_SUM;
			}

			@Override
			public void onResultCallback(TaskResult<UserTask> taskResult,
					UserTask result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
		return taskRequest;
	}
	
	/**
	 * @description: 任务
	 *
	 * @param context
	 * @param reqConfig
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月18日 下午3:29:19
	 */
	public static DataTaskRequest fetchUserTask2(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		UserTaskReq reqInfo = (UserTaskReq)reqConfig.getData();
		if (reqInfo == null) {
			throw new IllegalArgumentException("Illegal argument");
		}
		DataTaskRequest<UserTaskResp> taskRequest = new DataTaskRequest<UserTaskResp>(
				new DataTaskListener<UserTaskResp,List<UserTask>>() {

			@Override
			public UserTaskResp onGetCacheData() {
				alog.debug("get cache info");
				return null;
			}

			@Override
			public boolean isCacheDataExist(UserTaskResp cacheData) {
//				alog.debug("cache size:" + cacheData.size());
				return cacheData!=null;
			}

			@Override
			public void onFetchHttpData() {
				UserTaskReq reqInfo = (UserTaskReq)reqConfig.getData();
				HttpHelper.fetchUserTask(context, reqConfig, reqInfo, onGetHttpReqCallback());
			}
			
			@Override
			public List<UserTask> onCacheDataProcess(UserTaskResp info) {
				UserTask userTask = new UserTask();
				UserTaskReq reqInfo = (UserTaskReq)reqConfig.getData();
				List<UserTaskList> taskList = info.getTaskList();
				List<RewardRuleInner> rewardRuleList;
				RewardRuleInner rewardRule = null;
				UserTaskInfo userTaskInfo;
				UserTaskState taskState;
				long startTime = 0;
				long endTime = 0;
				int targetTime;
				String firstGameId;
				boolean isSubTask;
				for (UserTaskList task : taskList) {
					userTaskInfo = null;
					isSubTask = false;
					rewardRuleList = task.getRewardRule();
					if(rewardRuleList!=null && rewardRuleList.size()>0){
						
						if(task.getState() == UserTaskResp.TASK_STATE_FINISH){
							taskState = UserTaskState.TASK_STATE_FINISH;
						} else if(task.getState() == UserTaskResp.TASK_STATE_STANDBY){
							taskState = UserTaskState.TASK_STATE_STANDBY;
						} else {
							taskState = UserTaskState.TASK_STATE_NOT_FINISH;
						}
						
						rewardRule = rewardRuleList.get(0);
						targetTime = rewardRule.getLow();
						firstGameId = getFirstGameId(task);
						
						if(UserTaskInfo.TASK_TYPE_GAME_DOWNLOAD.equals(task.getLogo())){
							if(TextUtils.isEmpty(firstGameId)){
								continue;
							}
							if(taskState == UserTaskState.TASK_STATE_STANDBY){
								taskState = UserTaskState.TASK_STATE_NOT_FINISH;
							}
							//游戏下载
							userTaskInfo = UserTaskGenerator.makeGameDownloadTask(
									context, reqInfo.getUserId(), task.getTaskId(), task.getTaskName(), 
									task.getRemark(), "", taskState, targetTime, startTime, endTime,firstGameId);
							
							isSubTask = true;
							if(userTask.getUserTaskInfo(userTaskInfo.getTaskType()) == null){
								UserTaskInfo rootUserTask = new UserTaskInfo();
								rootUserTask.setTaskType(userTaskInfo.getTaskType());
								rootUserTask.setTaskName(userTaskInfo.getTaskName());
								userTask.putUserTaskInfo(rootUserTask.getTaskType(), rootUserTask);
							}
						} else if(UserTaskInfo.TASK_TYPE_GAME_SCORE.equals(task.getLogo())){
							if(taskState == UserTaskState.TASK_STATE_STANDBY){
								taskState = UserTaskState.TASK_STATE_NOT_FINISH;
							}
							//游戏评分
							userTaskInfo = UserTaskGenerator.makeGameScoreTask(
									context, reqInfo.getUserId(), task.getTaskId(), task.getTaskName(), 
									task.getRemark(), "", taskState, targetTime, startTime, endTime);
						} else if(UserTaskInfo.TASK_TYPE_WATCH_VIDEO.equals(task.getLogo())){
							if(TextUtils.isEmpty(firstGameId)){
								continue;
							}
							if(taskState == UserTaskState.TASK_STATE_STANDBY){
								taskState = UserTaskState.TASK_STATE_NOT_FINISH;
							}
							//观看视频
							userTaskInfo = UserTaskGenerator.makeWatchVideoTask(
									context, reqInfo.getUserId(), task.getTaskId(), task.getTaskName(), 
									task.getRemark(), "", taskState, targetTime, startTime, endTime, firstGameId);
							isSubTask = true;
							if(userTask.getUserTaskInfo(userTaskInfo.getTaskType()) == null){
								UserTaskInfo rootUserTask = new UserTaskInfo();
								rootUserTask.setTaskType(userTaskInfo.getTaskType());
								rootUserTask.setTaskName(userTaskInfo.getTaskName());
								userTask.putUserTaskInfo(rootUserTask.getTaskType(), rootUserTask);
							}
						} else if(UserTaskInfo.TASK_TYPE_GAME_SEARCH.equals(task.getLogo())){
							//游戏搜索
							if(taskState == UserTaskState.TASK_STATE_STANDBY){
								taskState = UserTaskState.TASK_STATE_NOT_FINISH;
							}
							userTaskInfo = UserTaskGenerator.makeGameSearchTask(
									context, reqInfo.getUserId(), task.getTaskId(), task.getTaskName(), 
									task.getRemark(), "", taskState, targetTime, startTime, endTime);
						} else if(UserTaskInfo.TASK_TYPE_FIRST_REGEDISTER.equals(task.getLogo())){
							//首次注册
							userTaskInfo = UserTaskGenerator.makeFirstRegisterTask(
									context, reqInfo.getUserId(), task.getTaskId(), task.getTaskName(), 
									task.getRemark(), "", taskState);
						} else if(UserTaskInfo.TASK_TYPE_WECHAT_FIRST_LOGIN.equals(task.getLogo())){
							//微信首次登录
							if(taskState == UserTaskState.TASK_STATE_STANDBY){
								taskState = UserTaskState.TASK_STATE_NOT_FINISH;
							}
							userTaskInfo = UserTaskGenerator.makeWeChatFirstLoginTask(
									context, reqInfo.getUserId(), task.getTaskId(), task.getTaskName(), 
									task.getRemark(), "", taskState);
						} else if(UserTaskInfo.TASK_TYPE_FIRST_RECHARGE.equals(task.getLogo())
								&& UserTaskInfo.TASK_COMPLETE_WAY_ALLFIRST.equals(task.getCompleteWay())){
							//首次充值
							if(taskState == UserTaskState.TASK_STATE_STANDBY){
								taskState = UserTaskState.TASK_STATE_NOT_FINISH;
							}
							userTaskInfo = UserTaskGenerator.makeFirstRechargeTask(
									context, reqInfo.getUserId(), task.getTaskId(), task.getTaskName(), 
									task.getRemark(), "", taskState);
						} else if(UserTaskInfo.TASK_TYPE_FIRST_RECHARGE.equals(task.getLogo())
								&& UserTaskInfo.TASK_COMPLETE_WAY_ADD.equals(task.getCompleteWay())){
							//累计充值
							if(taskState == UserTaskState.TASK_STATE_STANDBY){
								taskState = UserTaskState.TASK_STATE_NOT_FINISH;
							}
							userTaskInfo = UserTaskGenerator.makeRechargeSumTask(
									context, reqInfo.getUserId(), task.getTaskId(), task.getTaskName(), 
									task.getRemark(), "", taskState, targetTime);
						} else if(UserTaskInfo.TASK_TYPE_EXCHANGE_GOODS.equals(task.getLogo())){
							//兑换商品
							userTaskInfo = UserTaskGenerator.makeExchangeGoodsTask(
									context, reqInfo.getUserId(), task.getTaskId(), task.getTaskName(), 
									task.getRemark(), "", taskState, targetTime);
						} else if(UserTaskInfo.TASK_TYPE_CHECKIN_DAILY.equals(task.getLogo())){
							//每日签到
							userTaskInfo = UserTaskGenerator.makeCheckInDailyTask(
									context, reqInfo.getUserId(), task.getTaskId(), task.getTaskName(), 
									task.getRemark(), "", taskState);
						} else if(UserTaskInfo.TASK_TYPE_MARKET_LOGIN.equals(task.getLogo())){							
							//连续登录
							List<Integer> integralList = new ArrayList<Integer>();
//							Map<Integer,Integer> integralMap = new HashMap<Integer, Integer>();
							int signValue = 0;
							int ruleSize = rewardRuleList.size();
							boolean isFindFirstRule = false;
							rewardRule = null;
							for (int i = 1; i < 8; i++) {
								if(i <= ruleSize){
									if(UserTaskResp.TASK_RULE_STATE_FINISH == rewardRuleList.get(i-1).getState()) {
										signValue |= (0x1 << (i-1));
									} else {
										if(!isFindFirstRule){
											isFindFirstRule = true;
											rewardRule = rewardRuleList.get(i-1);
										}
									}
									integralList.add(rewardRuleList.get(i-1).getIntegral());
								} else {
									integralList.add(0);
								}
							}
							CheckInTaskInfo checkTaskInfo = UserTaskGenerator.makeCheckInContinuelyTask(
									context, reqInfo.getUserId(), task.getTaskId(), task.getTaskName(), 
									task.getRemark(), "", taskState);
							checkTaskInfo.setSignValue(signValue);
							checkTaskInfo.setIntegralList(integralList);
							userTaskInfo = checkTaskInfo;
						} else if(UserTaskInfo.TASK_TYPE_MARKET_ONLINE.equals(task.getLogo())){
							//大厅在线时长
							userTaskInfo = UserTaskGenerator.makeMarketOnlineTask(
									context, reqInfo.getUserId(), task.getTaskId(), task.getTaskName(), 
									task.getRemark(), "", taskState, targetTime, startTime, endTime);
						} else if(UserTaskInfo.TASK_TYPE_GAME_ONLINE.equals(task.getLogo())){
							//游戏在线时长
							if(taskState == UserTaskState.TASK_STATE_STANDBY){
								taskState = UserTaskState.TASK_STATE_NOT_FINISH;
							}
							userTaskInfo = UserTaskGenerator.makeGameOnlineTask(
									context, reqInfo.getUserId(), task.getTaskId(), task.getTaskName(), 
									task.getRemark(), "", taskState, targetTime, startTime, endTime, firstGameId);
							isSubTask = true;
							if(userTask.getUserTaskInfo(userTaskInfo.getTaskType()) == null){
								UserTaskInfo rootUserTask = new UserTaskInfo();
								rootUserTask.setTaskType(userTaskInfo.getTaskType());
								rootUserTask.setTaskName(userTaskInfo.getTaskName());
								userTask.putUserTaskInfo(rootUserTask.getTaskType(), rootUserTask);
							}
						} else if(UserTaskInfo.TASK_TYPE_GAME_LOGIN.equals(task.getLogo())){
							//游戏登录
							userTaskInfo = UserTaskGenerator.makeGameLoginTask(
									context, reqInfo.getUserId(), task.getTaskId(), task.getTaskName(), 
									task.getRemark(), "", taskState, targetTime, startTime, endTime, firstGameId);
							isSubTask = true;
							if(userTask.getUserTaskInfo(userTaskInfo.getTaskType()) == null){
								UserTaskInfo rootUserTask = new UserTaskInfo();
								rootUserTask.setTaskType(userTaskInfo.getTaskType());
								rootUserTask.setTaskName(userTaskInfo.getTaskName());
								userTask.putUserTaskInfo(rootUserTask.getTaskType(), rootUserTask);
							}
						}
					}
					
					if(userTaskInfo!=null){
						if(rewardRule!=null){
							userTaskInfo.setRuleId(rewardRule.getRuleId());
							userTaskInfo.setIntegral(rewardRule.getIntegral());
							userTaskInfo.setCoupon(rewardRule.getCoupon());
						}
						
						if(isSubTask){
							UserTaskInfo userTaskInfoInMap = userTask.getUserTaskInfo(userTaskInfo.getTaskType());
							List<UserTaskInfo> subTaskInfos = userTaskInfoInMap.getSubTaskInfos();
							if(subTaskInfos==null){
								subTaskInfos = new ArrayList<UserTaskInfo>();
								userTaskInfoInMap.setSubTaskInfos(subTaskInfos);
							}
							subTaskInfos.add(userTaskInfo);
						} else {
							userTask.putUserTaskInfo(userTaskInfo.getTaskType(), userTaskInfo);
						}
					}
				}
				return sortUserTask(userTask);
			}
			
			private String getFirstGameId(UserTaskList task){
				String gameId = null;
				if(task!=null && task.getGameIds()!=null && task.getGameIds().size()>0){
					gameId = task.getGameIds().get(0).getGameId();
				}
				return gameId;
			}
			
			private List<UserTask> sortUserTask(UserTask userTask){
				List<UserTask> userTaskList = new ArrayList<UserTask>();
				UserTask userTask2 = new UserTask();
				UserTaskInfo userTaskInfo;
				UserTaskType userTaskType;
				Map<UserTaskType, UserTaskInfo> userTaskInfoMap = userTask.getUserTaskInfoMap();
				Iterator<Map.Entry<UserTaskType, UserTaskInfo>> entries = userTaskInfoMap.entrySet().iterator();  
				while (entries.hasNext()) {  
					Map.Entry<UserTaskType, UserTaskInfo> entry = entries.next();  
					userTaskType = entry.getKey();
					userTaskInfo = entry.getValue();
					
					if(isNotCommonTask(userTaskType)){
						userTask2.putUserTaskInfo(userTaskType, userTaskInfo);
						entries.remove();
					}
				}
				
				userTaskList.add(userTask);
				userTaskList.add(userTask2);
				
				return userTaskList;
			}
			
			private boolean isNotCommonTask(UserTaskType userTaskType){
				return userTaskType == UserTaskType.TASK_TYPE_FIRST_REGEDISTER
						|| userTaskType == UserTaskType.TASK_TYPE_WECHAT_FIRST_LOGIN
						|| userTaskType == UserTaskType.TASK_TYPE_FIRST_RECHARGE
						|| userTaskType == UserTaskType.TASK_TYPE_RECHARGE_SUM;
			}

			@Override
			public void onResultCallback(TaskResult<List<UserTask>> taskResult,
					List<UserTask> result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
		return taskRequest;
	}
	
	public static DataTaskRequest obtainUserTaskReward(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		UserTaskInfo userTaskInfo = (UserTaskInfo)reqConfig.getData();
		if (userTaskInfo == null) {
			throw new IllegalArgumentException("Illegal argument");
		}
		DataTaskRequest<UserTaskResp> taskRequest = new DataTaskRequest<UserTaskResp>(
				new DataTaskListener<UserTaskResp,UserTaskInfo>() {

			@Override
			public UserTaskResp onGetCacheData() {
				alog.debug("get cache info");
				return null;
			}

			@Override
			public boolean isCacheDataExist(UserTaskResp cacheData) {
//				alog.debug("cache size:" + cacheData.size());
				return cacheData!=null;
			}

			@Override
			public void onFetchHttpData() {
				UserTaskInfo userTaskInfo = (UserTaskInfo)reqConfig.getData();
				UserTaskReq reqInfo = new UserTaskReq();
				reqInfo.setUserId(DataFetcher.getUserIdInt());
				reqInfo.setFlag(DataConfig.TASK_FLAG_OBTAIN);
				reqInfo.setTaskId(userTaskInfo.getTaskId());
				reqInfo.setRuleId(userTaskInfo.getRuleId());
				reqInfo.setGameId(userTaskInfo.getGameId());
				HttpHelper.fetchUserTask(context, reqConfig, reqInfo, onGetHttpReqCallback());
			}
			
			@Override
			public UserTaskInfo onCacheDataProcess(UserTaskResp info) {
				UserInfo userInfo = BaseApplication.userInfo;
				if(userInfo!=null){
					updateUserInfo(userInfo, userInfo.getBalance(), info.getIntegral(), info.getCoupon());
				}
				
				UserTaskInfo userTaskInfo = (UserTaskInfo)reqConfig.getData();
				userTaskInfo.setTaskState(UserTaskState.TASK_STATE_FINISH);
//				if(userTaskInfo instanceof CheckInTaskInfo){
//					((CheckInTaskInfo)userTaskInfo).recordTodaySign(context);
//				}
                UserTaskType taskType = userTaskInfo.getTaskType();
                int userId = userTaskInfo.getUserId();
                long startTime = 0;
                long endTime = 0;
                String gameId = userTaskInfo.getGameId();
				if(UserTaskType.TASK_TYPE_GAME_DOWNLOAD==taskType){
					//游戏下载
					UserTaskDaoHelper.delGameDownloadRecord(context, userId, startTime, endTime, gameId);
				} else if(UserTaskType.TASK_TYPE_GAME_SCORE==taskType){
					//游戏评分
					UserTaskDaoHelper.delGameScoreRecord(context, userId, startTime, endTime);
				} else if(UserTaskType.TASK_TYPE_WATCH_VIDEO==taskType){
					//观看视频
					UserTaskDaoHelper.delWatchVideoRecord(context, userId, startTime, endTime, gameId);
				} else if(UserTaskType.TASK_TYPE_GAME_SEARCH==taskType){
					//游戏搜索
					UserTaskDaoHelper.delGameSearchRecord(context, userId, startTime, endTime);
				} else if(UserTaskType.TASK_TYPE_FIRST_REGEDISTER==taskType){
					//首次注册
				} else if(UserTaskType.TASK_TYPE_WECHAT_FIRST_LOGIN==taskType){
					//微信首次例如
					UserTaskDaoHelper.delWeChatFirstRegister(context, userId);
				} else if(UserTaskType.TASK_TYPE_FIRST_RECHARGE==taskType){
					//首次充值
//					UserTaskDaoHelper.delFirstRecharge(context, userId);
				} else if(UserTaskType.TASK_TYPE_RECHARGE_SUM==taskType){
					//累计充值
				} else if(UserTaskType.TASK_TYPE_EXCHANGE_GOODS==taskType){
					//兑换商品
				} else if(UserTaskType.TASK_TYPE_MARKET_LOGIN==taskType){
					//连续登录
					CheckInTaskInfo checkInTaskInfo = (CheckInTaskInfo)userTaskInfo;
					for (int i = 1; i < 8; i++) {
						if(!checkInTaskInfo.isCheckIn(i)){
							checkInTaskInfo.checkIn(i);
							break;
						}
					}
				} else if(UserTaskType.TASK_TYPE_CHECKIN_DAILY==taskType){
					//每日签到
					((CheckInTaskInfo)userTaskInfo).recordTodaySign(context);
				} else if(UserTaskType.TASK_TYPE_MARKET_ONLINE==taskType){
					//大厅在线时长
					UserTaskDaoHelper.delMarketOnlineRecord(context, userId, startTime, endTime);
				} else if(UserTaskType.TASK_TYPE_GAME_ONLINE==taskType){
					//游戏在线时长
					UserTaskDaoHelper.delGameOnlineRecord(context, userId, gameId, startTime, endTime);
				} else if(UserTaskType.TASK_TYPE_GAME_LOGIN==taskType){
					//游戏登录
					UserTaskDaoHelper.delGameLoginRecord(context, userId, gameId, startTime, endTime);
				}
				
				return userTaskInfo;
			}

			@Override
			public void onResultCallback(TaskResult<UserTaskInfo> taskResult,
					UserTaskInfo result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
		return taskRequest;
	}
	
	/**
	 * @description: 获取新版本信息
	 *
	 * @param context
	 * @param reqConfig
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月29日 上午10:52:22
	 */
	public static DataTaskRequest fetchNewVersionInfo(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		DataTaskRequest<NewVersionInfoResp> taskRequest = new DataTaskRequest<NewVersionInfoResp>(
				new DataTaskListener<NewVersionInfoResp,NewVersionInfoResp>() {

			@Override
			public NewVersionInfoResp onGetCacheData() {
				alog.debug("get cache info");
				NewVersionInfoResp info = null;
				String cacheData=(String)SpHelper.get(context, SpHelper.KEY_NEW_VERSION_INFO, "");
				if(!TextUtils.isEmpty(cacheData)){
					info = MyJsonPaser.fromJson(cacheData, NewVersionInfoResp.class);
					PackageInfo pkgInfo =  AppUtil.getPkgInfoByName(context, context.getPackageName());
					if(pkgInfo!=null && info!=null && pkgInfo.versionCode>=info.getVersionCode()){
						SpHelper.put(context, SpHelper.KEY_NEW_VERSION_INFO, "");
						String localFileName = context.getPackageName()+"_"+info.getVersionCode()+".apk";
						File localFile = new File(context.getFilesDir(), localFileName);
						if(localFile.exists()){
							localFile.delete();
						}
					}
				}
				return info;
			}

			@Override
			public boolean isCacheDataExist(NewVersionInfoResp cacheData) {
				alog.debug("cacheData:" + cacheData);
				boolean result = cacheData != null;
				return result;
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.fetchNewVersionInfo(context, reqConfig, onGetHttpReqCallback());
			}
			
			@Override
			public NewVersionInfoResp onCacheDataProcess(NewVersionInfoResp info) {				
				if (info.getVersionCode() >= 0){
					//服务器部署了版本信息
					int curVersionCode = getVersionCode();
					if(curVersionCode < info.getVersionCode()){
						info.setNewVersionExist(true);
						if(curVersionCode < info.getMinVersion()){
							// 强制升级才能使用
							info.setForce(true);
						}
					}
				}
				return info;
			}

			@Override
			public void onResultCallback(TaskResult<NewVersionInfoResp> taskResult, NewVersionInfoResp result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}
			
			public int getVersionCode() {
				int versionCode = -1;
				try {
					PackageManager manager = context.getPackageManager();
					PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
					versionCode = info.versionCode;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return versionCode;
			}				
		});
		return taskRequest;
	}
	
	/**
	 * @description: 下载新版本apk
	 *
	 * @param context
	 * @param reqConfig
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月29日 下午3:10:05
	 */
	public static DownloadTaskRequest  downloadNewVersionApk(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		
		NewVersionInfoResp info = (NewVersionInfoResp)reqConfig.getData();
		if (info==null) {
			throw new IllegalArgumentException("Illegal argument");
		}
		String localFileName = context.getPackageName()+"_"+info.getVersionCode()+".apk";
		File localFile = new File(context.getFilesDir(), localFileName);
		final String localFilePath = localFile.getAbsolutePath();
		alog.debug("local save path:"+localFilePath);
		
		Listener listener=new Listener<Void>() {
			@Override
			public void onSuccess(Void response) {
				FileDownResultInfo info = new FileDownResultInfo();
				info.setPath(localFilePath);
				TaskResult<FileDownResultInfo> taskResult = TaskResult.makeSuccessTaskResult(info);
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

			@Override
			public void onError(NetroidError error) {
				TaskResult<FileDownResultInfo> taskResult = TaskResult.makeFailTaskResult(TaskResult.FAILED, "", null);
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

			@Override
			public void onProgressChange(long fileSize, long downloadedSize) {
				DataHelper.onProgressUpdate(context, reqConfig, fileSize, downloadedSize);
			}
		};
		
		DownloadTaskRequest downloadTaskRequest = new DownloadTaskRequest();
		downloadTaskRequest.setFileUrl(info.getDownloadURL());
		downloadTaskRequest.setLocalFilePath(localFilePath);
		downloadTaskRequest.setListener(listener);
		return downloadTaskRequest;
	}
	
	/**
	 * @description: 用户商品兑换记录
	 *
	 * @param context
	 * @param reqConfig
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月29日 下午8:09:29
	 */
	public static DataTaskRequest fetchUserGoodsOrderInfo(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		final String userId = (String)reqConfig.getData();
		if (userId==null) {
			throw new IllegalArgumentException("Illegal argument");
		}
		DataTaskRequest<List<UserGoodsOrderInfo>> taskRequest = new DataTaskRequest<List<UserGoodsOrderInfo>>(
				new DataTaskListener<List<UserGoodsOrderInfo>,List<UserGoodsOrderInfo>>() {

			@Override
			public List<UserGoodsOrderInfo> onGetCacheData() {
				alog.debug("get cache info");
				String key = UserGoodsOrderInfo.class.getSimpleName() + "_" + userId;
				UserGoodsOrderInfoResp resp = (UserGoodsOrderInfoResp)DataHelper.sIdentityScope.get(key);
				return resp!=null ? resp.getData() : null;
			}

			@Override
			public boolean isCacheDataExist(List<UserGoodsOrderInfo> cacheData) {
				boolean result = (cacheData!=null && cacheData.size()>0);
				return result;
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.fetchUserGoodsOrderInfo(context, reqConfig, onGetHttpReqCallback());
			}
			
			@Override
			public List<UserGoodsOrderInfo> onCacheDataProcess(List<UserGoodsOrderInfo> infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<List<UserGoodsOrderInfo>> taskResult,
					List<UserGoodsOrderInfo> result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
		return taskRequest;
	}
	
	/**
	 * @description: 兑换商品
	 *
	 * @param context
	 * @param reqConfig
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月30日 下午1:37:38
	 */
	public static DataTaskRequest exchangeGoods(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		final GoodsExchangeReq info = (GoodsExchangeReq)reqConfig.getData();
		if (info==null) {
			throw new IllegalArgumentException("Illegal argument");
		}
		DataTaskRequest<GoodsExchangeResp> taskRequest = new DataTaskRequest<GoodsExchangeResp>(
				new DataTaskListener<GoodsExchangeResp,GoodsExchangeResp>() {

			@Override
			public GoodsExchangeResp onGetCacheData() {
				alog.debug("get cache info");
				return null;
			}

			@Override
			public boolean isCacheDataExist(GoodsExchangeResp cacheData) {
				return false;
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.exchangeGoods(context, reqConfig, onGetHttpReqCallback());
			}
			
			@Override
			public GoodsExchangeResp onCacheDataProcess(GoodsExchangeResp infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<GoodsExchangeResp> taskResult,
					GoodsExchangeResp result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
		return taskRequest;
	}
	
	/**
	 * @description: 注册帐号
	 *
	 * @param context
	 * @param reqConfig
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月2日 下午4:40:16
	 */
	public static void userRegister(Context context, ReqConfig reqConfig) {
		alog.debug("");
		UserLoginRegisterReq info = (UserLoginRegisterReq)reqConfig.getData();
		if (info==null) {
			throw new IllegalArgumentException("Illegal argument");
		}
		
		HttpHelper.userRegister(context, reqConfig);
	}
	
	/**
	 * @description: 登录帐号
	 *
	 * @param context
	 * @param reqConfig
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月2日 下午4:40:39
	 */
	public static void userLogin(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		final UserLoginRegisterReq info = (UserLoginRegisterReq)reqConfig.getData();
		if (info==null) {
			throw new IllegalArgumentException("Illegal argument");
		}
		
		HttpHelper.userLogin(context, reqConfig);
	}
	
	/**
	 * @description: 自动登录最后一个帐号
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年8月2日 下午10:28:57
	 */
	public static void userAutoLogin(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		HttpHelper.userAutoLogin(context, reqConfig);
	}
	
	/**
	 * @description: 自动登录指定的userId帐号
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年8月2日 下午10:28:57
	 */
	public static void userAutoLoginByUserId(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
	
		Integer userId = (Integer)reqConfig.getData();
		if (userId==null) {
			throw new IllegalArgumentException("Illegal argument");
		}
		
		HttpHelper.userAutoLoginByUserId(context, reqConfig);
	}
	
	/**
	 * @description: 获取手机验证码
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年8月3日 下午10:47:55
	 */
	public static void userGetCaptcha(Context context, ReqConfig reqConfig) {
		alog.debug("");
		UserGetCaptchaReq info = (UserGetCaptchaReq)reqConfig.getData();
		if (info==null) {
			throw new IllegalArgumentException("Illegal argument");
		}
		
		HttpHelper.userGetCaptcha(context, reqConfig);
	}
	
	/**
	 * @description: 微信登录
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年8月3日 下午10:48:35
	 */
	public static void userWeChatLogin(Context context, ReqConfig reqConfig) {
		alog.debug("");
		UserWeChatLoginReq info = (UserWeChatLoginReq)reqConfig.getData();
		if (info==null) {
			throw new IllegalArgumentException("Illegal argument");
		}
		
		HttpHelper.userWeChatLogin(context, reqConfig);
	}
	
	/**
	 * @description: 获取所有登录过的用户
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年8月4日 上午10:33:58
	 */
	public static void userGetAllLoginedUser(Context context, final ReqConfig reqConfig) {
		Activity activity = (Activity)context;
		final Context appContext = context.getApplicationContext();
		GetAllUserInfo getUsers = new GetAllUserInfo(activity);
		getUsers.result(new UserInfoCallBack() {
			@Override
			public void result(int code,
					List<com.atetpay.login.data.sqlite.UserInfo> userInfos) {
				TaskResult<List<UserInfo>> taskResult = null;
				UserInfo bean = null;
				List<UserInfo> resultInfoList = new ArrayList<UserInfo>();
				boolean isPhoneNum;
				String phoneNum;
				for (com.atetpay.login.data.sqlite.UserInfo info : userInfos) {
					isPhoneNum = false;
					if(!TextUtils.isEmpty(info.getUsername())){
						isPhoneNum = TextUtils.isDigitsOnly(info.getUsername());
					}
					phoneNum = isPhoneNum ? info.getUsername() : null;
					bean = new UserInfo(info.getUserId(), info.getUsername(), phoneNum, 
							info.getIcon(), info.getIntegral(), info.getBalance(), info.getCoupon());
					resultInfoList.add(bean);
				}
				if (code == LOGIN.SUCCESS) {
					alog.debug(resultInfoList.toString());
					taskResult = TaskResult.makeSuccessTaskResult(resultInfoList);
				} else if (code == LOGIN.NO_LOGIN_USER) {
					String desc = appContext.getString(R.string.fail_msg_no_data, code);
					taskResult = TaskResult.makeFailTaskResult(TaskResult.NO_DATA, desc, null);
				} else {
					String desc = appContext.getString(R.string.fail_msg_unknown_error, code);
					taskResult = TaskResult.makeFailTaskResult(code, desc, null);
				}
				DataHelper.onCallback(appContext, reqConfig, taskResult);
			}
		});
	}
	
	/**
	 * @description: 获取最后登录的用户
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年8月4日 下午3:20:32
	 */
	public static void userGetLastLoginedUser(Context context, final ReqConfig reqConfig) {
//		Activity activity = (Activity)context;
		Context activity = context;
		final Context appContext = context.getApplicationContext();
		
		GetLocalUserInfo localUser = new GetLocalUserInfo(activity);
		localUser.signin(new SignListener() {
			@Override
			public void result(int code, String desc,
					com.atetpay.login.data.sqlite.UserInfo info) {
				TaskResult<UserInfo> taskResult = null;
				if (code == LOGIN.SUCCESS) {
					alog.info(info);
					if(info != null && info.getUserId() != null && info.getUserId()!=0){
						boolean isPhoneNum = false;
						String phoneNum;
						if(!TextUtils.isEmpty(info.getUsername())){
							isPhoneNum = TextUtils.isDigitsOnly(info.getUsername());
						}
						phoneNum = isPhoneNum ? info.getUsername() : null;
						UserInfo bean = new UserInfo(info.getUserId(), info.getUsername(), phoneNum, 
								info.getIcon(), info.getIntegral(), info.getBalance(), info.getCoupon());
						alog.debug(bean.toString());

						String str = "";
						str = (String)SpHelper.get(appContext, SpHelper.KEY_USER_INFO, str);
						if(!TextUtils.isEmpty(str)){
							UserInfo localUserInfo = MyJsonPaser.fromJson(str, UserInfo.class);
							if(localUserInfo!=null 
									&& localUserInfo.getUserId()==bean.getUserId() 
									&& localUserInfo.getUserName().equals(bean.getUserName())){
								if(localUserInfo.getBalance()!=bean.getBalance()){
									bean.setBalance(localUserInfo.getBalance());
								}
								if(localUserInfo.getIntegral()!=bean.getIntegral()){
									bean.setIntegral(localUserInfo.getIntegral());
								}
								if(localUserInfo.getCoupon()!=bean.getCoupon()){
									bean.setCoupon(localUserInfo.getCoupon());
								}
							} else if(localUserInfo!=null){
								DataHelper.updateUserInfo(localUserInfo);
							}
						}

						taskResult = TaskResult.makeSuccessTaskResult(bean);
					} else {
						desc = appContext.getString(R.string.fail_msg_unknown_error, 9527);
						taskResult = TaskResult.makeFailTaskResult(code, desc, null);
					}

					//				} else if (code == LOGIN.NO_LOGIN_USER) {					
					//					String str = "";
					//					str = (String)SpHelper.get(appContext, SpHelper.KEY_USER_INFO, str);
					//					UserInfo bean = null;
					//					if(!TextUtils.isEmpty(str)){
					//						bean = MyJsonPaser.fromJson(str, UserInfo.class);
					//					} else {
					//						String username = DataHelper.getOldVersionUsername(appContext);
					//						int userId = DataHelper.getOldVersionUserId(appContext);
					//						if(!TextUtils.isEmpty(username) && userId > 0){
					//							bean = new UserInfo(userId, username, null, null, 0, 0, 0);
//							DataHelper.updateUserInfo(bean);
//						}
//					}
//					if(bean!=null){
//						taskResult = TaskResult.makeSuccessTaskResult(bean);
//					} else {
//						taskResult = TaskResult.makeFailTaskResult(TaskResult.NO_DATA, desc, null);
//					}
				} else {
					if(TextUtils.isEmpty(desc)){
						desc = appContext.getString(R.string.fail_msg_unknown_error, code);
					}
					taskResult = TaskResult.makeFailTaskResult(code, desc, null);
				}
				DataHelper.onCallback(appContext, reqConfig, taskResult);
			}
		});
	}
	
	/**
	 * @description: 修改密码
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年8月3日 下午10:46:12
	 */
	public static void userUpdatePassword(Context context, ReqConfig reqConfig) {
		alog.debug("");
		UserModifyReq info = (UserModifyReq)reqConfig.getData();
		if (info==null) {
			throw new IllegalArgumentException("Illegal argument");
		}
		
		HttpHelper.userUpdatePassword(context, reqConfig);
	}
	
	/**
	 * @description: 修改手机号
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年8月4日 下午10:41:56
	 */
	public static void userUpdatePhoneNum(Context context, ReqConfig reqConfig) {
		alog.debug("");
		UserModifyReq info = (UserModifyReq)reqConfig.getData();
		if (info==null) {
			throw new IllegalArgumentException("Illegal argument");
		}
		
		HttpHelper.userUpdatePhoneNum(context, reqConfig);
	}
	
	/**
	 * @description: 找回密码
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年8月14日 下午3:26:42
	 */
	public static void resetUpdatePassword(Context context, ReqConfig reqConfig) {
		alog.debug("");
		UserLoginRegisterReq info = (UserLoginRegisterReq)reqConfig.getData();
		if (info==null) {
			throw new IllegalArgumentException("Illegal argument");
		}
		
		HttpHelper.resetPassword(context, reqConfig);
	}
	
	/**
	 * @description: 获取可更新的接口
	 *
	 * @param context
	 * @param reqConfig
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月6日 下午6:28:51
	 */
	public static DataTaskRequest fetchLocalUpdatableInterface(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		final UpdateInterfaceReq info = (UpdateInterfaceReq)reqConfig.getData();
		if (info==null) {
			throw new IllegalArgumentException("Illegal argument");
		}
		DataTaskRequest<List<LocalUpdateInfo>> taskRequest = new DataTaskRequest<List<LocalUpdateInfo>>(
				new DataTaskListener<List<LocalUpdateInfo>, List<LocalUpdateInfo>>() {
					
			@Override
			public List<LocalUpdateInfo> onGetCacheData() {
				alog.debug("get cache info");
				return DaoHelper.getLocalUpdatableInterface(context);
			}

			@Override
			public boolean isCacheDataExist(List<LocalUpdateInfo> cacheData) {
				boolean result = (cacheData != null && cacheData.size()>0);
				if(!result){
					if(cacheData != null){
						//判断数据库是否存在记录，存在说明数据库中记录的接口已更新到最新
						result = DaoHelper.getLocalUpdateInfoSize(context);
					}
				}
				return result;
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.fetchLocalUpdatableInterface(context, reqConfig, null, onGetHttpReqCallback());
			}
			
			@Override
			public List<LocalUpdateInfo> onCacheDataProcess(List<LocalUpdateInfo> infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<List<LocalUpdateInfo>> taskResult,
					List<LocalUpdateInfo> result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
		return taskRequest;
	}
	
	public static boolean isSupportedInterface(String interfaceName){
		boolean result = false;
		if(interfaceName.equals(UpdateInterface.ADS)){
			result = true;
		} else if(interfaceName.equals(UpdateInterface.GAME_ACTIVITY)){
			result = true;
		} else if(interfaceName.equals(UpdateInterface.GAME_GOODS)){
			result = true;
		} else if(interfaceName.equals(UpdateInterface.GAME_GIFT)){
			result = true;
		} else if(interfaceName.equals(UpdateInterface.GAME_NEW_UPLOADED)){
			result = true;
		} else if(interfaceName.equals(UpdateInterface.GAME_RANKING)){
			result = true;
		} else if(interfaceName.equals(UpdateInterface.GAME_TOPIC)){
			result = true;
		} else if(interfaceName.equals(UpdateInterface.GAME_TOPIC_DETAIL)){
			result = true;
		} else if(interfaceName.equals(UpdateInterface.GAME_TYPE)){
			result = true;
		} else if(interfaceName.equals(UpdateInterface.GAME_TYPE_DETAIL)){
			result = true;
		} else if(interfaceName.equals(UpdateInterface.VIDEO_GUIDE)){
			result = true;
		} else if(interfaceName.equals(UpdateInterface.NOTICE)){
			result = true;
		} else if(interfaceName.equals(UpdateInterface.QUERY_VERSION)){
			result = true;
		} else if(interfaceName.equals(UpdateInterface.PINYIN_SEARCH)){
			result = true;
		}  
		
		return result;
	}
	
	public static InterfaceInfo getInterfaceInfo(ReqConfig reqConfig){
		InterfaceInfo info = null;
		Long lastTime = 0l;
		String interfaceName = null;
		String typeId = "";
		int reqCode = reqConfig.getReqCode();
		if(reqCode == ReqCode.FETCH_GAME_CENTER_INFO){
			interfaceName = UpdateInterface.ADS;
			typeId = DataConfig.AD_MODEL_ID_GAME_CENTER;
		} else if(reqCode == ReqCode.FETCH_GAME_TYPE){
			interfaceName = UpdateInterface.GAME_TYPE;
		} else if(reqCode == ReqCode.FETCH_GAME_INFOS_FROM_GAME_TYPE2){
			interfaceName = UpdateInterface.GAME_TYPE_DETAIL;
			GameTypeInfo gameTypeInfo = (GameTypeInfo)reqConfig.getData();
			typeId = gameTypeInfo.getTypeId();
		} else if(reqCode == ReqCode.FETCH_GAME_TOPIC){
			interfaceName = UpdateInterface.GAME_TOPIC;
		} else if(reqCode == ReqCode.FETCH_GAME_INFOS_FROM_GAME_TOPIC2){
			interfaceName = UpdateInterface.GAME_TOPIC_DETAIL;
			GameTopicInfo gameTopicInfo= (GameTopicInfo)reqConfig.getData();
			typeId=gameTopicInfo.getTopicId();
		} else if(reqCode == ReqCode.FETCH_GAME_INFOS_FROM_NEW_UPLOAD){
			interfaceName = UpdateInterface.GAME_NEW_UPLOADED;
		} else if(reqCode == ReqCode.FETCH_GAME_INFOS_FROM_GAME_RANKING){
			interfaceName = UpdateInterface.GAME_RANKING;
			Integer type=(Integer)reqConfig.getData();
			typeId = type+"";
		} else if(reqCode == ReqCode.FETCH_ACTIVITY){
			interfaceName = UpdateInterface.GAME_ACTIVITY;
		} else if(reqCode == ReqCode.FETCH_GOODS){
			interfaceName = UpdateInterface.GAME_GOODS;
		} else if(reqCode == ReqCode.FETCH_GIFT){
			interfaceName = UpdateInterface.GAME_GIFT;
		} else if(reqCode == ReqCode.FETCH_VIDEO){
			interfaceName = UpdateInterface.VIDEO_GUIDE;
			typeId = DataConfig.VIDEO_TYPE_GUIDE;
		} else if(reqCode == ReqCode.FETCH_NOTICE){
			interfaceName = UpdateInterface.NOTICE;
			Integer type = (Integer)reqConfig.getData();
			typeId = type+"";
		} else if(reqCode == ReqCode.FETCH_NEW_VERSION_INFO){
			interfaceName = UpdateInterface.QUERY_VERSION;
			typeId = DataConfig.MARKET_APPID;
		} else if(reqCode == ReqCode.FETCH_GAME_SEARCH_PINYIN){
			interfaceName = UpdateInterface.PINYIN_SEARCH;
		} else if(reqCode == ReqCode.FETCH_LAUNCH_AD){
			interfaceName = UpdateInterface.ADS;
			typeId = DataConfig.AD_MODEL_ID_LAUNCH;
		}
		
		if(interfaceName != null){
			info = new InterfaceInfo();
			info.setName(interfaceName);
			info.setTypeId(typeId);
			info.setUniqueId(interfaceName+typeId);
			
//			lastTime = (Long)SpHelper.get(BaseApplication.getContext(), SpHelper.KEY_UPDATE_INTERFACE, lastTime);
//			info.setLastTime(lastTime);
		}
		
		alog.debug("interfaceName:"+interfaceName+" typeId:"+typeId);
		return info;
	}
	
	public static long hasNewDataToUpdate(List<LocalUpdateInfo> localUpdateInfoList, String uniqueId){
		long updateTime = -1;
		for (LocalUpdateInfo localUpdateInfo : localUpdateInfoList) {
			if(localUpdateInfo.getLocalUniqueId().equals(uniqueId)){
				updateTime = localUpdateInfo.getUpdateInterfaceInfo().getUpdateTime();
				break;
			}
		}
		alog.debug("uniqueId:"+uniqueId+" updateTime:"+updateTime);
//		if(true && updateTime==-1){
//			updateTime = 1;
//		}
		return updateTime;
	}
	
	private static void updateLocalTime(Context context, ReqConfig reqConfig, InterfaceInfo info){
		String uniqueId = info.getUniqueId();
		alog.debug("uniqueId:"+uniqueId);
		LocalUpdateInfo localUpdateInfo = DaoHelper.getLocalUpdateInfo(context, uniqueId);
		if(localUpdateInfo!=null){
			alog.debug("uniqueId:"+uniqueId + " LocalUpdateTime:"+info.getUpdateTime());
			localUpdateInfo.setLocalUpdateTime(info.getUpdateTime());
			
			if(reqConfig.getReqType() == ReqType.UPDATE){
				//后台更新
				alog.debug("update state to not see");
				localUpdateInfo.setLocalState(DataConfig.INTERFACE_STATE_NOTSEE);
			}
			
			localUpdateInfo.update();
		}
	}
	
	/**
	 * @description: 子类型是否可更新
	 *
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月9日 下午3:38:48
	 */
	public static boolean isSubTypeIdUpdatable(SubTypeId subTypeId, List<LocalSubTypeId> localSubTypeIdList){
		boolean result = true;
		if(localSubTypeIdList != null && localSubTypeIdList.size() > 0){
			for (LocalSubTypeId localSubTypeId : localSubTypeIdList) {
				if(subTypeId.equals(localSubTypeId.getLocalname())){
					if(subTypeId.getUpdateTime() <= localSubTypeId.getLocalUpdateTime()){
						result = false;
					}
				}
			}
		}
		alog.debug("isSubTypeIdUpdatable:"+result);
		return result;
	}
	
	/**
	 * @description: 下载文件
	 *
	 * @param context
	 * @param reqConfig
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月21日 下午10:24:48
	 */
	public static DownloadTaskRequest  downloadFile(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		
		DownloadFileReq reqInfo = (DownloadFileReq)reqConfig.getData();
		if (reqInfo==null) {
			throw new IllegalArgumentException("Illegal argument");
		}
		alog.debug("url:"+reqInfo.getUrl());
		alog.debug("local save path:"+reqInfo.getLocalPath());
		
		Listener listener=new Listener<Void>() {
			@Override
			public void onSuccess(Void response) {
				DownloadFileReq reqInfo = (DownloadFileReq)reqConfig.getData();
				FileDownResultInfo info = new FileDownResultInfo();
				info.setPath(reqInfo.getLocalPath());
				TaskResult<FileDownResultInfo> taskResult = TaskResult.makeSuccessTaskResult(info);
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

			@Override
			public void onError(NetroidError error) {
				TaskResult<FileDownResultInfo> taskResult = TaskResult.makeFailTaskResult(TaskResult.FAILED, "", null);
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

			@Override
			public void onProgressChange(long fileSize, long downloadedSize) {
				DataHelper.onProgressUpdate(context, reqConfig, fileSize, downloadedSize);
			}
		};
		
		DownloadTaskRequest downloadTaskRequest = new DownloadTaskRequest();
		downloadTaskRequest.setFileUrl(reqInfo.getUrl());
		downloadTaskRequest.setLocalFilePath(reqInfo.getLocalPath());
		downloadTaskRequest.setListener(listener);
		return downloadTaskRequest;
	}
	
	public static Bitmap getCurrentTimeLaunchAd(final Context context) {
		String modelId = DataConfig.AD_MODEL_ID_LAUNCH;
		List<AdModelInfo> adModelInfoList = DaoHelper.getAdModelInfo(context, modelId);
		List<String> urlList = null;
		Bitmap bitmap = null;
		String url = null;
		if(!adModelInfoList.isEmpty()){
			long curTime = TimeHelper.getCurTime();
			long time = 0;
			long lastTime = Integer.MAX_VALUE;
			AdModelInfo lastAdModelInfo = null;
			for (AdModelInfo adModelInfo : adModelInfoList) {
//				if(adModelInfo.getStartTime() <= curTime && adModelInfo.getEndTime() >= curTime){
					time = curTime - adModelInfo.getStartTime();
					if(time < lastTime){
						lastAdModelInfo = adModelInfo;
						lastTime = time;
					}
//				}
			}
			if(lastAdModelInfo!=null){
				urlList = new ArrayList<String>();
				List<ModelToAd> modelToAdList = lastAdModelInfo.getModelToAdList();
				for (ModelToAd modelToAd : modelToAdList) {
					urlList.add(modelToAd.getAdInfo().getUrl());
				}
			}
			if(urlList!=null && !urlList.isEmpty()){
				url = urlList.get(0);
				String path = getLocalLaunchAdPath(context, url);
				File file = new File(path);
				if(file.exists()){
					bitmap = ImgHelper.loadBitmap(context, path);
				} else {
					downloadImage(context, url, path);
				}
			}
		} else {
			if(!DaoHelper.getLocalUpdateInfoSize(context)){
				fetchLaunchAd(context, url);
			}
		}
		return bitmap;
	}
	
	private static void fetchLaunchAd(final Context context, String url){
		ReqCallback<List<String>> reqCallback = new ReqCallback<List<String>>() {
			@Override
			public void onResult(TaskResult<List<String>> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					List<String> urlList = taskResult.getData();
					alog.info(urlList.toString());
					
					final String url = urlList.get(0);
					BaseApplication.getMainHandler().post(new Runnable() {
						@Override
						public void run() {
							String path = getLocalLaunchAdPath(context, url);
							downloadImage(context, url, path);
						}
					});
				}
			}
		};
		DataFetcher.getLauchAd(context, reqCallback, false).request(context);
	}
	
	private static void downloadImage(Context context, String url, String path){
		DownloadFileReq reqInfo = new DownloadFileReq();
		reqInfo.setUrl(url);
		reqInfo.setLocalPath(path);
		DataRequester dataRequest = DataFetcher.downloadFile(context, null, reqInfo, false);
		//下载
		dataRequest.request(context);
	}

	public static String getLocalLaunchAdPath(Context context, String url){
		String fileName = url.hashCode()+".jpg";
		String path = context.getFilesDir().getPath()+"/launchimg";
		File dir = new File(path);  
		if (!dir.exists()) {  
			dir.mkdirs();  
		}  
		File file = new File(path+"/"+fileName);
		return file.getAbsolutePath();
	}
	
	/**
	 * @description: 抽奖信息
	 *
	 * @param context
	 * @param reqConfig
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年9月12日 下午8:19:19
	 */
	public static DataTaskRequest fetchRewardInfo(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		DataTaskRequest<RewardInfoResp> taskRequest = new DataTaskRequest<RewardInfoResp>(
				new DataTaskListener<RewardInfoResp,RewardInfoResp>() {

			@Override
			public RewardInfoResp onGetCacheData() {
				alog.debug("get cache info");
				return null;
			}

			@Override
			public boolean isCacheDataExist(RewardInfoResp cacheData) {
				Long lastUpdateTime = 0l;
				lastUpdateTime = (Long)SpHelper.get(context, SpHelper.KEY_FETCH_REWARD_TIME, lastUpdateTime);
				if(TimeHelper.isToday(lastUpdateTime)){
					return true;
				}
				return cacheData!=null;
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.fetchRewardInfo(context, reqConfig, onGetHttpReqCallback());
			}
			
			@Override
			public RewardInfoResp onCacheDataProcess(RewardInfoResp infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<RewardInfoResp> taskResult,
					RewardInfoResp result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
		return taskRequest;
	}
	
	
	/**
	 * @description: 领取抽奖的奖励
	 *
	 * @param context
	 * @param reqConfig
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年9月12日 下午8:56:58
	 */
	public static DataTaskRequest obtainReward(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		ObtainRewardReq reqInfo = (ObtainRewardReq)reqConfig.getData();
		if (reqInfo==null) {
			throw new IllegalArgumentException("Illegal argument");
		}
		DataTaskRequest<ObtainRewardResp> taskRequest = new DataTaskRequest<ObtainRewardResp>(
				new DataTaskListener<ObtainRewardResp,ObtainRewardResp>() {

			@Override
			public ObtainRewardResp onGetCacheData() {
				alog.debug("get cache info");
				return null;
			}

			@Override
			public boolean isCacheDataExist(ObtainRewardResp cacheData) {
				return cacheData!=null;
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.obtainReward(context, reqConfig, onGetHttpReqCallback());
			}
			
			@Override
			public ObtainRewardResp onCacheDataProcess(ObtainRewardResp infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<ObtainRewardResp> taskResult,
					ObtainRewardResp result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
		return taskRequest;
	}
	
	/**
	 * @description: 获取所有游戏的下载次数和星级
	 *
	 * @param context
	 * @param reqConfig
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年9月20日 下午11:43:54
	 */
	public static DataTaskRequest fetchDownStar(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		DataTaskRequest<DownStarResp> taskRequest = new DataTaskRequest<DownStarResp>(
				new DataTaskListener<DownStarResp,DownStarResp>() {

			@Override
			public DownStarResp onGetCacheData() {
				alog.debug("get cache info");
				return null;
			}

			@Override
			public boolean isCacheDataExist(DownStarResp  cacheData) {
				return cacheData!=null;
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.fetchDownStarCount(context, reqConfig, onGetHttpReqCallback());
			}
			
			@Override
			public DownStarResp onCacheDataProcess(DownStarResp  infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<DownStarResp > taskResult,
					DownStarResp result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
		return taskRequest;
	}
	
	public static int getOldVersionUserId(Context context) {
		SharedPreferences sp = context.getSharedPreferences(Constant.LOGIN_USER_SP, Context.MODE_PRIVATE);
		return sp.getInt(Constant.LOGIN_USER_ID, 0);
	}
	
	public static String getOldVersionUsername(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				Constant.LOGIN_USER_SP, context.MODE_WORLD_READABLE);
		return sp.getString(Constant.LOGIN_USER_NAME, "");
	}
	
	/**
	 * @description: 获取旧版本的帐号信息
	 *
	 * @param context
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年9月22日 下午3:31:24
	 */
	public static String getOldVersionPassword(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				Constant.LOGIN_USER_SP, context.MODE_WORLD_READABLE);
		String tmpPwd = sp.getString(Constant.LOGIN_PASSWORD, null);
		String msgPwd = "";
		try {
			if (tmpPwd != null)
				msgPwd = AESCryptoUtils.decrypt(Constant.AES_SEED, tmpPwd);
			else
				msgPwd = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msgPwd;
	}
	
	public static void updateUserInfo(UserInfo bean){
		if(bean == null){
			return;
		}
		String str=MyJsonPaser.toJson(bean);
		SpHelper.put(BaseApplication.getContext(), SpHelper.KEY_USER_INFO, str);
	}
	
	public static void updateUserInfo(UserInfo bean, int balance, int integral, int coupon){
		bean.setBalance(balance);
		bean.setIntegral(integral);
		bean.setCoupon(coupon);
		updateUserInfo(bean);
	}
	
	/**
	 * @description: 获取App配置
	 *
	 * @param context
	 * @param reqConfig
	 * @param httpReqCallback 
	 * @author: LiuQin
	 * @date: 2015年9月22日 下午6:38:21
	 */
	public static DataTaskRequest fetchAppConfig(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		DataTaskRequest<AppConfigResp> taskRequest = new DataTaskRequest<AppConfigResp>(
				new DataTaskListener<AppConfigResp,AppConfigResp>() {

			@Override
			public AppConfigResp onGetCacheData() {
				alog.debug("get cache info");
				return null;
			}

			@Override
			public boolean isCacheDataExist(AppConfigResp cacheData) {
				return cacheData!=null;
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.fetchAppConfig(context, reqConfig, onGetHttpReqCallback());
			}
			
			@Override
			public AppConfigResp onCacheDataProcess(AppConfigResp  infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<AppConfigResp> taskResult,
					AppConfigResp result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
		return taskRequest;
	}
	
	/**
	 * @description: 获取存在新内容的接口名称
	 *
	 * @param context
	 * @param reqConfig
	 * @return 
	 * @throws: 
	 * @author: LiuQin
	 * @date: 2015年10月8日 下午10:39:19
	 */
	public static DataTaskRequest getNewContentInterface(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		DataTaskRequest<List<LocalUpdateInfo>> taskRequest = new DataTaskRequest<List<LocalUpdateInfo>>(
				new DataTaskListener<List<LocalUpdateInfo>,List<String>>() {

			@Override
			public List<LocalUpdateInfo> onGetCacheData() {
				alog.debug("get cache info");
				return DaoHelper.getNewContentLocalUpdateInfo(context);
			}

			@Override
			public boolean isCacheDataExist(List<LocalUpdateInfo> cacheData) {
				return true;
			}

			@Override
			public void onFetchHttpData() {
			}
			
			@Override
			public List<String> onCacheDataProcess(List<LocalUpdateInfo> infos) {
				final List<String> result = new ArrayList<String>();
				
				if(!Configuration.IS_OPEN_NEW_CONTENT_TIP){
					return result;
				}
				
				if(Configuration.IS_FILL_TEST_NEW_CONTENT_TIP){
					//测试数据
					result.add(UpdateInterface.GAME_NEW_UPLOADED);
					result.add(UpdateInterface.GAME_ACTIVITY);
					result.add(UpdateInterface.GAME_GOODS);
					result.add(UpdateInterface.USER_TASK);
					result.add(UpdateInterface.GAME_GIFT);
					result.add(UpdateInterface.GAME_TOPIC);
					
					List<GameTypeInfo> gameTypeInfoList =  DaoHelper.getGameType(context);
					if(!gameTypeInfoList.isEmpty()){
						for (GameTypeInfo gameTypeInfo : gameTypeInfoList) {
							result.add(UpdateInterface.GAME_TYPE_DETAIL+gameTypeInfo.getTypeId());
						}
					}
					
					setNewContentInterface(result);
					return result;
				}
				
				if(infos!=null && !infos.isEmpty()){
					boolean isAddGameTopic=false;
					boolean isAddGameType=false;
					for (LocalUpdateInfo localUpdateInfo : infos) {
						if(!isAddGameTopic && UpdateInterface.GAME_TOPIC_DETAIL.equals(localUpdateInfo.getLocalName())){
							isAddGameTopic = true;
							result.add(UpdateInterface.GAME_TOPIC);
						}
						if(!isAddGameType && UpdateInterface.GAME_TYPE_DETAIL.equals(localUpdateInfo.getLocalName())){
							isAddGameType = true;
							result.add(UpdateInterface.GAME_TYPE);
						}
						if(!localUpdateInfo.getLocalName().equals(UpdateInterface.GAME_TOPIC) 
								&& !localUpdateInfo.getLocalName().equals(UpdateInterface.GAME_TYPE)){
							result.add(localUpdateInfo.getLocalUniqueId());
						}
					}
				}
				
				if(NetUtil.isNetworkAvailable(context, false)){
					//获取任务
					resetLock();
					ReqCallback<List<UserTask>> reqCallback = new ReqCallback<List<UserTask>>() {
						@Override
						public void onResult(TaskResult<List<UserTask>> taskResult) {
							int code = taskResult.getCode();
							alog.info("taskResult code:" + code);
							if (code == TaskResult.OK) {
								List<UserTask> userTaskList = taskResult.getData();
								if(userTaskList==null || userTaskList.size()<=0){
									closeLock();
									return;
								}

								for (UserTask userTask : userTaskList) {
									if(userTask.size()<=0){
										continue;
									}

									UserTaskInfo userTaskInfo;
									UserTaskState taskState;

									Map<UserTaskType, UserTaskInfo> userTaskInfoMap = userTask.getUserTaskInfoMap();
									Iterator<Map.Entry<UserTaskType, UserTaskInfo>> entries = userTaskInfoMap.entrySet().iterator();  
									while (entries.hasNext()) {  
										Map.Entry<UserTaskType, UserTaskInfo> entry = entries.next();  
										userTaskInfo = entry.getValue();
										taskState = userTaskInfo.getTaskState();
										if(taskState != UserTaskState.TASK_STATE_INVALID 
												&& taskState != UserTaskState.TASK_STATE_NOT_EXIST){
											if(!userTaskInfo.haveSubTaskInfos()){
												//普通任务
												if(taskState == UserTaskState.TASK_STATE_NOT_FINISH
														|| taskState == UserTaskState.TASK_STATE_STANDBY){
													if(!result.contains(UpdateInterface.USER_TASK)){
														result.add(UpdateInterface.USER_TASK);
													}
													closeLock();
													return;
												}
											} else {
												//具有子任务的任务,遍历子任务
												for (UserTaskInfo info : userTaskInfo.getSubTaskInfos()) {
													taskState = info.getTaskState();
													if(taskState == UserTaskState.TASK_STATE_NOT_FINISH
															|| taskState == UserTaskState.TASK_STATE_STANDBY){
														if(!result.contains(UpdateInterface.USER_TASK)){
															result.add(UpdateInterface.USER_TASK);
														}
														closeLock();
														return;
													}
												}
											}
										}
									}  					
								}
							}
							closeLock();
						}
					};
					DataFetcher.getUserTask2(context, reqCallback, false).request(context);
					block();
				}

				setNewContentInterface(result);
				return result;
			}

			@Override
			public void onResultCallback(TaskResult<List<String>> taskResult,
					List<String> result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}
			
			
			ConditionVariable cv = new ConditionVariable();
			private void resetLock() {
				cv.close();
			}
			private void block() {
				cv.block();
			}
			private void closeLock() {
				cv.open();
			}

		});
		return taskRequest;
	}
	
	public static DataTaskRequest removeInterface(final Context context, final ReqConfig reqConfig) {
		alog.info("");
		final String name=(String)reqConfig.getData();
		if (TextUtils.isEmpty(name)) {
			throw new IllegalArgumentException("Illegal argument");
		}
		DataTaskRequest<Void> taskRequest = new DataTaskRequest<Void>(
				new DataTaskListener<Void,String>() {
			@Override
			public Void onGetCacheData() {
				alog.debug("get cache info");
				
				if(!Configuration.IS_OPEN_NEW_CONTENT_TIP){
					return null;
				}
				
				if(Configuration.IS_FILL_TEST_NEW_CONTENT_TIP){
					//测试数据
					List<String> interfaceList = DataHelper.getNewContentInterface();
					if(interfaceList!=null){
						interfaceList.remove(name);
					}
					return null;
				}


				if (name.equals(UpdateInterface.GAME_TOPIC)) {
					//专题
					List<LocalUpdateInfo> localUpdateInfos = DaoHelper.getNewContentLocalUpdateInfo(context, UpdateInterface.GAME_TOPIC_DETAIL);
					if(!localUpdateInfos.isEmpty()){
						for (LocalUpdateInfo info : localUpdateInfos) {
							info.setLocalState(DataConfig.INTERFACE_STATE_SEE);
							info.update();
						}
					}
				} else if (name.equals(UpdateInterface.USER_TASK)) {
				} else {
					LocalUpdateInfo localUpdateInfo = DaoHelper.getLocalUpdateInfo(context, name);
					if(localUpdateInfo!=null){
						localUpdateInfo.setLocalState(DataConfig.INTERFACE_STATE_SEE);
						localUpdateInfo.update();
					}					
				}
				
				List<String> interfaceList = DataHelper.getNewContentInterface();
				if(interfaceList!=null){
					interfaceList.remove(name);
				}
				return null;
			}

			@Override
			public boolean isCacheDataExist(Void cacheData) {
				return true;
			}

			@Override
			public void onFetchHttpData() {
			}
			
			@Override
			public String onCacheDataProcess(Void infos) {
				return "";
			}

			@Override
			public void onResultCallback(TaskResult<String> taskResult, String result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
		return taskRequest;
	}

	public static List<String> getNewContentInterface() {
		return sNewContentInterface;
	}

	public static void setNewContentInterface(List<String> sNewContentInterface) {
		DataHelper.sNewContentInterface = sNewContentInterface;
	}
	
	/**
	 * @description: 商品的剩余数量
	 *
	 * @param context
	 * @param reqConfig
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年10月18日 下午2:53:38
	 */
//	public static DataTaskRequest fetchGoodsLeftCount(final Context context, final ReqConfig reqConfig) {
//		alog.debug("");
//		String goodsId = (String)reqConfig.getData();
//		if (TextUtils.isEmpty(goodsId)) {
//			throw new IllegalArgumentException("Illegal argument");
//		}
//		DataTaskRequest<GoodsLeftCountInfo> taskRequest = new DataTaskRequest<GoodsLeftCountInfo>(
//				new DataTaskListener<GoodsLeftCountInfo,GoodsLeftCountInfo>() {
//
//			@Override
//			public GoodsLeftCountInfo onGetCacheData() {
//				alog.debug("get cache info");
//				return null;
//			}
//
//			@Override
//			public boolean isCacheDataExist(GoodsLeftCountInfo cacheData) {
//				return cacheData!=null;
//			}
//
//			@Override
//			public void onFetchHttpData() {
//				HttpHelper.fetchGoodsCountSingle(context, reqConfig, onGetHttpReqCallback());
//			}
//			
//			@Override
//			public GoodsLeftCountInfo onCacheDataProcess(GoodsLeftCountInfo  infos) {
//				return infos;
//			}
//
//			@Override
//			public void onResultCallback(TaskResult<GoodsLeftCountInfo> taskResult,
//					GoodsLeftCountInfo result) {
//				if(taskResult==null){
//					taskResult = TaskResult.makeTaskResult(context, result);
//				}
//				DataHelper.onCallback(context, reqConfig, taskResult);
//			}
//
//		});
//		return taskRequest;
//	}
	
	/**
	 * @description: 获取商品剩余数量
	 *
	 * @param context
	 * @param reqConfig
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年10月19日 上午1:32:24
	 */
	public static DataTaskRequest fetchGoodsLeftCount(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		GoodsInfo reqInfo= (GoodsInfo)reqConfig.getData();
		if (reqInfo == null) {
			throw new IllegalArgumentException("Illegal argument");
		}
		DataTaskRequest<GoodsLeftCountInfo> taskRequest = new DataTaskRequest<GoodsLeftCountInfo>(
				new DataTaskListener<GoodsLeftCountInfo,GoodsLeftCountInfo>() {

			@Override
			public GoodsLeftCountInfo onGetCacheData() {
				alog.debug("get cache info");
				return null;
			}

			@Override
			public boolean isCacheDataExist(GoodsLeftCountInfo cacheData) {
				return cacheData!=null;
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.fetchGoodsCountSingle(context, reqConfig, onGetHttpReqCallback());
			}
			
			@Override
			public GoodsLeftCountInfo onCacheDataProcess(GoodsLeftCountInfo  infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<GoodsLeftCountInfo> taskResult,
					GoodsLeftCountInfo result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
		return taskRequest;
	}
	
	/**
	 * @description: 同步时间
	 *
	 * @param context
	 * @param reqConfig
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年10月19日 上午1:49:11
	 */
	public static DataTaskRequest syncTime(final Context context, final ReqConfig reqConfig) {
		alog.debug("");
		DataTaskRequest<Long> taskRequest = new DataTaskRequest<Long>(
				new DataTaskListener<Long,Long>() {

			@Override
			public Long onGetCacheData() {
				alog.debug("get cache info");
				return TimeHelper.getCurTime();
			}

			@Override
			public boolean isCacheDataExist(Long cacheData) {
				return TimeHelper.isServerTimeSyn();
			}

			@Override
			public void onFetchHttpData() {
				HttpHelper.syncTime(context, reqConfig, onGetHttpReqCallback());
			}
			
			@Override
			public Long onCacheDataProcess(Long infos) {
				return infos;
			}

			@Override
			public void onResultCallback(TaskResult<Long> taskResult,
					Long result) {
				if(taskResult==null){
					taskResult = TaskResult.makeTaskResult(context, result);
				}
				DataHelper.onCallback(context, reqConfig, taskResult);
			}

		});
		return taskRequest;
	}
}

