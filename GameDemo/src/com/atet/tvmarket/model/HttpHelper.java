package com.atet.tvmarket.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.atet.common.logging.ALog;
import com.atet.statistics.utils.DeviceStatisticsUtils;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.app.Constant.SERVER_RETURN_CODE;
import com.atet.tvmarket.app.UrlConstant;
import com.atet.tvmarket.entity.ActivityInfoResp;
import com.atet.tvmarket.entity.AdModelInfoResp;
import com.atet.tvmarket.entity.AppConfig;
import com.atet.tvmarket.entity.AppConfigResp;
import com.atet.tvmarket.entity.AutoType;
import com.atet.tvmarket.entity.DownStarResp;
import com.atet.tvmarket.entity.GameGiftInfoResp;
import com.atet.tvmarket.entity.GameInfoResp;
import com.atet.tvmarket.entity.GameInfosFromGameTopicRespBak;
import com.atet.tvmarket.entity.GameSearchPinyinResp;
import com.atet.tvmarket.entity.GameTopicInfoResp;
import com.atet.tvmarket.entity.GameTypeInfoResp;
import com.atet.tvmarket.entity.GoodsExchangeReq;
import com.atet.tvmarket.entity.GoodsExchangeResp;
import com.atet.tvmarket.entity.GoodsInfoResp;
import com.atet.tvmarket.entity.GoodsLeftCountResp;
import com.atet.tvmarket.entity.GoodsLeftCountResp.GoodsLeftCountInfo;
import com.atet.tvmarket.entity.NewVersionInfoResp;
import com.atet.tvmarket.entity.NoticeInfoResp;
import com.atet.tvmarket.entity.ObtainRewardReq;
import com.atet.tvmarket.entity.ObtainRewardResp;
import com.atet.tvmarket.entity.ObtainUserGiftReq;
import com.atet.tvmarket.entity.ObtainUserGiftResp;
import com.atet.tvmarket.entity.RateGameReq;
import com.atet.tvmarket.entity.RateGameResp;
import com.atet.tvmarket.entity.RewardInfoResp;
import com.atet.tvmarket.entity.ServerIdInfo;
import com.atet.tvmarket.entity.SyncTimeResp;
import com.atet.tvmarket.entity.ThirdGameInfoResp;
import com.atet.tvmarket.entity.ThirdGameInfosFromGameTopicRespBak;
import com.atet.tvmarket.entity.UpdateInterfaceInner;
import com.atet.tvmarket.entity.UpdateInterfaceReq;
import com.atet.tvmarket.entity.UpdateInterfaceResp;
import com.atet.tvmarket.entity.UploadExceptionReq;
import com.atet.tvmarket.entity.UserGetCaptchaReq;
import com.atet.tvmarket.entity.UserGiftInfoResp;
import com.atet.tvmarket.entity.UserGoodsOrderInfo;
import com.atet.tvmarket.entity.UserGoodsOrderInfoResp;
import com.atet.tvmarket.entity.UserInfo;
import com.atet.tvmarket.entity.UserLoginRegisterReq;
import com.atet.tvmarket.entity.UserModifyReq;
import com.atet.tvmarket.entity.UserTaskReq;
import com.atet.tvmarket.entity.UserTaskResp;
import com.atet.tvmarket.entity.UserWeChatLoginReq;
import com.atet.tvmarket.entity.VideoInfoResp;
import com.atet.tvmarket.entity.dao.ActDetailPhoto;
import com.atet.tvmarket.entity.dao.ActInfo;
import com.atet.tvmarket.entity.dao.ActionUrl;
import com.atet.tvmarket.entity.dao.AdInfo;
import com.atet.tvmarket.entity.dao.AdModelInfo;
import com.atet.tvmarket.entity.dao.Extramap;
import com.atet.tvmarket.entity.dao.GameGiftInfo;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.entity.dao.GameRanking;
import com.atet.tvmarket.entity.dao.GameScoreRecordInfo;
import com.atet.tvmarket.entity.dao.GameSearchPinyinInfo;
import com.atet.tvmarket.entity.dao.GameTopicInfo;
import com.atet.tvmarket.entity.dao.GameTypeInfo;
import com.atet.tvmarket.entity.dao.GiftInfo;
import com.atet.tvmarket.entity.dao.GoodsDetailPhoto;
import com.atet.tvmarket.entity.dao.GoodsInfo;
import com.atet.tvmarket.entity.dao.LocalUpdateInfo;
import com.atet.tvmarket.entity.dao.ModelToAd;
import com.atet.tvmarket.entity.dao.NewUploadToGame;
import com.atet.tvmarket.entity.dao.NoticeInfo;
import com.atet.tvmarket.entity.dao.ScreenShotInfo;
import com.atet.tvmarket.entity.dao.ThirdGameDownInfo;
import com.atet.tvmarket.entity.dao.ThirdGameInfo;
import com.atet.tvmarket.entity.dao.TopicToGame;
import com.atet.tvmarket.entity.dao.TypeToGame;
import com.atet.tvmarket.entity.dao.UpdateInterfaceInfo;
import com.atet.tvmarket.entity.dao.UserGameGiftInfo;
import com.atet.tvmarket.entity.dao.UserGameToGift;
import com.atet.tvmarket.entity.dao.UserGiftInfo;
import com.atet.tvmarket.entity.dao.VideoInfo;
import com.atet.tvmarket.model.DataConfig.UpdateInterface;
import com.atet.tvmarket.model.ReqConfig.ReqCode;
import com.atet.tvmarket.model.netroid.request.GsonObjectRequest;
import com.atet.tvmarket.model.netroid.request.GsonObjectVerifyRequest;
import com.atet.tvmarket.model.task.TaskQueue;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.model.usertask.UserTaskDaoHelper;
import com.atetpay.autologin.ATETLogin;
import com.atetpay.autologin.ATETRegist;
import com.atetpay.autologin.ATETResetPassword;
import com.atetpay.autologin.AutoSignIn;
import com.atetpay.autologin.GetCode;
import com.atetpay.autologin.UpdateUserInfo;
import com.atetpay.autologin.WechatLogin;
import com.atetpay.autologin.callback.GetMessCodeCallBack;
import com.atetpay.autologin.callback.ResetPasswordCallBack;
import com.atetpay.autologin.callback.SignCallBack;
import com.atetpay.autologin.callback.UpdateUserInfoCallBack;
import com.atetpay.login.data.LOGIN;

import android.content.Context;
import android.net.Uri;
import android.os.ConditionVariable;
import android.os.SystemClock;
import android.text.TextUtils;

/**
 * @description: Http 数据请求 
 *
 * @author: LiuQin
 * @date: 2015年7月10日 下午2:55:13 
 */
public class HttpHelper {
	private static ALog alog=ALog.getLogger(HttpHelper.class);
	private static final boolean DEBUG_PRINT_HTTP_RESPONSE_DATA = true;
	private static final boolean DEBUG_PRINT_HTTP_POST_DATA = true;
	
	
	private static void printPostData(String data) {
		if (!DEBUG_PRINT_HTTP_POST_DATA) {
			return;
		}
		alog.debug(data);
	}
	
	private static void printRawResponseData(String data) {
		if (!DEBUG_PRINT_HTTP_RESPONSE_DATA) {
			return;
		}
		alog.debug(data);
	}
	
	/**
	 * @description: 是否没有数据的状态码
	 *
	 * @param code
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月3日 下午6:09:10
	 */
	public static boolean isNoDataCode(String code) {
		if(code == null || code.length()<=0){
			return false;
		}
		boolean result = false;
		if(code.equals("6")){
			result = true;
		} else if(code.equals("1802")){
			result = false;
		} else if(code.equals("11901")){
			result = true;
		} else {
			String reg = "^[1-2][0-9]0[1-2]$";
			Pattern pattern = Pattern.compile(reg);
			Matcher m = pattern.matcher(code);
			if (m.matches()) {
				result = true;
			}
		}
		
		return result;
	}

	/**
	 * @description: 每种类型设备先添加至服务器，服务器给每种类型的设备增加一个对应的id，其它接口使用此id去取数据
	 *
	 * @param context
	 * @author: LiuQin
	 * @date: 2015年6月24日 下午6:40:08
	 */
	public static void fetchServerId(final Context context, final ReqConfig reqConfig) {
		alog.info("");
		String url = UrlConstant.SERVER_ID;
		String postData = HttpReqDataHelper.serverIdReqData(context);
		GsonObjectRequest<ServerIdInfo> request = new GsonObjectRequest<ServerIdInfo>(
				url, postData, ServerIdInfo.class,
				new ReqListener<ServerIdInfo,String>(context, reqConfig) {
					@Override
					public void onSuccess(final ServerIdInfo response) {
						alog.debug(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){
							if(!oldUserLogin()){
								//旧帐号登录失败
//								handleUnsuccessCode(6);
//								return;
							}
							
							getServerAppConfig(context, response.getDeviceId());
							
							if(AppConfig.isInitAppConfig()){
								SpHelper.put(context, SpHelper.KEY_SERVER_ID, response.getDeviceId());
								SpHelper.put(context, SpHelper.KEY_CHANNEL_ID, response.getChannelId());
								SpHelper.put(context, SpHelper.KEY_DEVICE_CAPABILITY, response.getCapability());

								DataHelper.initDeviceInfo(context);
								//如果没有存在atetId就去网络上获取。
								String atetId = DataHelper.getDeviceInfo().getAtetId();
								if (atetId.equals("1") || TextUtils.isEmpty(atetId)) {
									DeviceStatisticsUtils.fetchAtetId(context);
								}
								DataHelper.initDeviceInfo(context);

								mTaskResult.setCode(TaskResult.OK);
								mTaskResult.setData(response.getDeviceId());
							} else {
								handleUnsuccessCode(6);
							}
						} else {
							handleUnsuccessCode(code);
						}
					}
					
					public void getServerAppConfig(Context context, String serverId){
						resetLock();
						alog.debug("");
						ReqCallback<AppConfigResp> reqCallback = new ReqCallback<AppConfigResp>() {
							@Override
							public void onResult(TaskResult<AppConfigResp> taskResult) {
								int code = taskResult.getCode();
								alog.info("taskResult code:" + code);
								if (code == TaskResult.OK) {
								}

								closeLock();
							}
						};
						DataFetcher.getServerAppConfig(context, reqCallback, serverId, false).request(context);

						block();
					}
					
					public boolean oldUserLogin() {
						String oldUserName = DataHelper.getOldVersionUsername(context);
						String oldPassword = DataHelper.getOldVersionPassword(context);
						if(TextUtils.isEmpty(oldUserName) || TextUtils.isEmpty(oldPassword)){
							return true;
						}
						
						resetLock();
						alog.debug("");
						//构造请求参数
						final UserLoginRegisterReq reqInfo = new UserLoginRegisterReq();
						reqInfo.setPhoneNum(oldUserName);
						reqInfo.setPassword(oldPassword);

						ReqCallback<UserInfo> reqCallback = new ReqCallback<UserInfo>() {
							@Override
							public void onResult(TaskResult<UserInfo> taskResult) {
								int code = taskResult.getCode();
								alog.info("taskResult code:" + code);
								if (code == TaskResult.OK) {
									UserInfo info = taskResult.getData();
									alog.info(info);
									reqInfo.setPhoneNum(null);
								} else if(code == 11002 || code == 11003){
									reqInfo.setPhoneNum(null);
								} else {
									//显示失败原因
									alog.error(taskResult.getMsg());
								}
								closeLock();
							}
						};
						DataFetcher.userLogin(context, reqCallback, reqInfo, false).request(context);
						block();
						
						return reqInfo.getPhoneNum() == null;
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
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 获取游戏分类
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月4日 下午4:46:34
	 */
	public static void fetchGameType(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<List<GameTypeInfo>> httpReqCallback) {
		alog.info("");
		String url = UrlConstant.GAME_TYPE;
		String postData = HttpReqDataHelper.gameTypeReqData(context);
		GsonObjectRequest<GameTypeInfoResp> request = new GsonObjectRequest<GameTypeInfoResp>(
				url, postData, GameTypeInfoResp.class,
				new ReqListener<GameTypeInfoResp,List<GameTypeInfo>>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(GameTypeInfoResp response) {
						alog.debug(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){
							mTaskResult.setCode(TaskResult.OK);
							mTaskResult.setData(response.getData());
							
							DaoHelper.saveGameType(context, response.getData());
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 获取某个分类下的游戏
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月4日 下午8:16:51
	 */
	public static void fetchGameInfosFromGameType(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<List<TypeToGame>> httpReqCallback) {
		alog.info("");
		String url = UrlConstant.GAME_INFOS_FROM_GAME_TYPE;
		String gameTypeId=((GameTypeInfo)reqConfig.getData()).getTypeId();
		String postData = HttpReqDataHelper.gameInfosFromGameTypeReqData(context, gameTypeId);
		GsonObjectRequest<GameInfoResp> request = new GsonObjectRequest<GameInfoResp>(
				url, postData, GameInfoResp.class,
				new ReqListener<GameInfoResp,List<TypeToGame>>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(GameInfoResp response) {
						alog.debug(response.toString());
						int code=response.getCode();
						List<GameInfo> infos=response.getData();
						if(code == SERVER_RETURN_CODE.SUCCESS){							
							if(infos!=null && infos.size()>0){
								GameTypeInfo gameTypeInfo = (GameTypeInfo)reqConfig.getData();
								List<TypeToGame> typeToGameList = new ArrayList<TypeToGame>();
								List<ScreenShotInfo> screenShotInfos=new ArrayList<ScreenShotInfo>();
								TypeToGame typeToGame;
								int returnOrder=1;
								for (GameInfo gameinfo : infos) {
									for (ScreenShotInfo screenShot : gameinfo.getImgs()) {
										screenShot.setGameId(gameinfo.getGameId());
										screenShotInfos.add(screenShot);
									}
									DaoHelper.delGameScreenShotInfo(context, gameinfo.getGameId());
									
									typeToGame = new TypeToGame();
									typeToGame.setGameId(gameinfo.getGameId());
									typeToGame.setTypeId(gameTypeInfo.getTypeId());
									typeToGame.setGameInfo(gameinfo);
									typeToGame.setGameTypeInfo(gameTypeInfo);
									typeToGame.setReturnOrder(returnOrder);
									typeToGameList.add(typeToGame);
									returnOrder++;
								}
								
								DaoHelper.saveGameScreenShotInfo(context, screenShotInfos);
								DaoHelper.saveGameInfo(context, infos);
								DaoHelper.saveTypeToGame(context, typeToGameList);
								
								mTaskResult.setData(typeToGameList);
							}
							mTaskResult.setCode(TaskResult.OK);
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	
	/**
	 * @description: 获取某个分类下的游戏
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月4日 下午8:16:51
	 */
	public static void fetchGameInfosFromGameType2(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<GameTypeInfo> httpReqCallback) {
		alog.info("");
		String url = UrlConstant.GAME_INFOS_FROM_GAME_TYPE;
		GameTypeInfo gameTypeInfo = (GameTypeInfo)reqConfig.getData();
		String postData = HttpReqDataHelper.gameInfosFromGameTypeReqData(context, gameTypeInfo.getTypeId());
		GsonObjectRequest<GameInfoResp> request = new GsonObjectRequest<GameInfoResp>(
				url, postData, GameInfoResp.class,
				new ReqListener<GameInfoResp,GameTypeInfo>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(GameInfoResp response) {
						alog.debug(response.toString());
						int code=response.getCode();
						List<GameInfo> infos=response.getData();
						if(code == SERVER_RETURN_CODE.SUCCESS){							
							if(infos!=null && infos.size()>0){
								GameTypeInfo gameTypeInfo = (GameTypeInfo)reqConfig.getData();
								List<TypeToGame> typeToGameList = new ArrayList<TypeToGame>();
								List<ScreenShotInfo> screenShotInfos=new ArrayList<ScreenShotInfo>();
								TypeToGame typeToGame;
								int returnOrder=1;
								for (GameInfo gameinfo : infos) {
									for (ScreenShotInfo screenShot : gameinfo.getImgs()) {
										screenShot.setGameId(gameinfo.getGameId());
										screenShotInfos.add(screenShot);
									}
									DaoHelper.delGameScreenShotInfo(context, gameinfo.getGameId());
									
									typeToGame = new TypeToGame();
									typeToGame.setGameId(gameinfo.getGameId());
									typeToGame.setTypeId(gameTypeInfo.getTypeId());
									typeToGame.setGameInfo(gameinfo);
									typeToGame.setGameTypeInfo(gameTypeInfo);
									typeToGame.setReturnOrder(returnOrder);
									typeToGameList.add(typeToGame);
									returnOrder++;
								}
								
								DaoHelper.saveGameScreenShotInfo(context, screenShotInfos);
								DaoHelper.saveGameInfo(context, infos);
								DaoHelper.saveTypeToGame(context, typeToGameList);
								
								gameTypeInfo.resetTypeToGameList();
								gameTypeInfo.getTypeToGameList();
								mTaskResult.setData(gameTypeInfo);
							}
							mTaskResult.setCode(TaskResult.OK);
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 使用game id获取游戏
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月5日 下午7:21:23
	 */
	public static void fetchGameInfoFromGameId(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<List<GameInfo>> httpReqCallback) {
		alog.info("");
		String url = UrlConstant.GAME_INFO_FROM_GAME_ID;
		String gameId=(String)reqConfig.getData();
		String postData = HttpReqDataHelper.gameInfoFromGameId(context, gameId);
		GsonObjectRequest<GameInfoResp> request = new GsonObjectRequest<GameInfoResp>(
				url, postData, GameInfoResp.class,
				new ReqListener<GameInfoResp,List<GameInfo>>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(GameInfoResp response) {
						alog.debug(response.toString());
						int code=response.getCode();
						List<GameInfo> infos=response.getData();
						if(code == SERVER_RETURN_CODE.SUCCESS){							
							if(infos!=null && infos.size()>0){
								List<ScreenShotInfo> screenShotInfos=new ArrayList<ScreenShotInfo>();
								for (GameInfo gameinfo : infos) {
									for (ScreenShotInfo screenShot : gameinfo.getImgs()) {
										screenShot.setGameId(gameinfo.getGameId());
										screenShotInfos.add(screenShot);
									}
									DaoHelper.delGameScreenShotInfo(context, gameinfo.getGameId());
								}
								
								DaoHelper.saveGameScreenShotInfo(context, screenShotInfos);
								DaoHelper.saveGameInfo(context, infos);
								
								mTaskResult.setData(infos);
							}
							mTaskResult.setCode(TaskResult.OK);
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 获取新游推荐游戏
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月5日 下午5:58:07
	 */
	public static void fetchNewUploadGames(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<List<GameInfo>> httpReqCallback) {
		alog.info("");
		String url = UrlConstant.GAME_INFOS_FROM_NEW_UPLOAD;
		String postData = HttpReqDataHelper.gameInfosFromDeviceId(context);
		GsonObjectRequest<GameInfoResp> request = new GsonObjectRequest<GameInfoResp>(
				url, postData, GameInfoResp.class,
				new ReqListener<GameInfoResp,List<GameInfo>>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(GameInfoResp response) {
						alog.debug(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){							
							List<GameInfo> infos=response.getData();
							if(infos!=null && infos.size()>0){
								List<NewUploadToGame> newUploadToGameList = new ArrayList<NewUploadToGame>();
								List<ScreenShotInfo> screenShotInfos=new ArrayList<ScreenShotInfo>();
								NewUploadToGame newUploadToGame;
								int orderCount=1;
								for (GameInfo gameinfo : infos) {
									for (ScreenShotInfo screenShot : gameinfo.getImgs()) {
										screenShot.setGameId(gameinfo.getGameId());
										screenShotInfos.add(screenShot);
									}
									DaoHelper.delGameScreenShotInfo(context, gameinfo.getGameId());
									
									newUploadToGame = new NewUploadToGame();
									newUploadToGame.setGameId(gameinfo.getGameId());
									newUploadToGame.setGameInfo(gameinfo);
									newUploadToGame.setReturnOrder(orderCount);
									newUploadToGameList.add(newUploadToGame);
									orderCount++;
								}
								
								DaoHelper.saveGameScreenShotInfo(context, screenShotInfos);
								DaoHelper.saveGameInfo(context, infos);
								DaoHelper.saveNewUploadToGame(context, newUploadToGameList);
								
								mTaskResult.setData(infos);
							}
							mTaskResult.setCode(TaskResult.OK);
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 获取游戏排行
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月5日 下午8:04:48
	 */
	public static void fetchGameInfosFromRankingType(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<List<GameInfo>> httpReqCallback) {
		alog.info("");
		String url = UrlConstant.GAME_INFOS_FROM_GAME_RANKING;
		Integer type=(Integer)reqConfig.getData();
		String postData = HttpReqDataHelper.gameInfosFromRankingType(context, type);
		GsonObjectRequest<GameInfoResp> request = new GsonObjectRequest<GameInfoResp>(
				url, postData, GameInfoResp.class,
				new ReqListener<GameInfoResp,List<GameInfo>>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(GameInfoResp response) {
						alog.debug(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){							
							List<GameInfo> infos=response.getData();
							if(infos!=null && infos.size()>0){
								Integer type=(Integer)reqConfig.getData();
								List<GameRanking> gameRankingList = new ArrayList<GameRanking>();
								List<ScreenShotInfo> screenShotInfos=new ArrayList<ScreenShotInfo>();
								GameRanking gameRanking;
								int returnOrder=1;
								for (GameInfo gameinfo : infos) {
									for (ScreenShotInfo screenShot : gameinfo.getImgs()) {
										screenShot.setGameId(gameinfo.getGameId());
										screenShotInfos.add(screenShot);
									}
									DaoHelper.delGameScreenShotInfo(context, gameinfo.getGameId());
									
									gameRanking = new GameRanking();
									gameRanking.setGameId(gameinfo.getGameId());
									gameRanking.setType(type);
									gameRanking.setGameInfo(gameinfo);
									gameRanking.setReturnOrder(returnOrder);
									gameRankingList.add(gameRanking);
									returnOrder++;
								}
								
								DaoHelper.saveGameScreenShotInfo(context, screenShotInfos);
								DaoHelper.saveGameInfo(context, infos);
								DaoHelper.saveGameRanking(context, gameRankingList);
								
								mTaskResult.setData(infos);
							}
							mTaskResult.setCode(TaskResult.OK);
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 获取游戏专题
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月4日 下午4:46:34
	 */
	public static void fetchGameTopic(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<List<GameTopicInfo>> httpReqCallback) {
		alog.info("");
		String url = UrlConstant.GAME_TOPIC;
		String postData = HttpReqDataHelper.deviceIdWithMinTime(context, 0);
		printPostData(postData);
		GsonObjectRequest<GameTopicInfoResp> request = new GsonObjectRequest<GameTopicInfoResp>(
				url, postData, GameTopicInfoResp.class,
				new ReqListener<GameTopicInfoResp,List<GameTopicInfo>>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(GameTopicInfoResp response) {
						alog.debug(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){
							mTaskResult.setCode(TaskResult.OK);
							mTaskResult.setData(response.getData());
							
							DaoHelper.saveGameTopic(context, response.getData());
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 获取专题下的游戏（版权游戏）
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月6日 下午8:51:47
	 */
	public static void fetchGameInfosFromGameTopic(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<List<TopicToGame>> httpReqCallback) {
		alog.info("");
		String url = UrlConstant.GAME_INFOS_FROM_GAME_TOPIC;
		GameTopicInfo info = (GameTopicInfo)reqConfig.getData();
		String topicId=info.getTopicId();
		int type=info.getType();
		String postData = HttpReqDataHelper.gameInfosFromGameTopic(context, topicId, type);
		GsonObjectRequest<GameInfosFromGameTopicRespBak> request = new GsonObjectRequest<GameInfosFromGameTopicRespBak>(
				url, postData, GameInfosFromGameTopicRespBak.class,
				new ReqListener<GameInfosFromGameTopicRespBak,List<TopicToGame>>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(GameInfosFromGameTopicRespBak response) {
						alog.debug(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){							
							GameInfosFromGameTopicRespBak.Inner inner=(GameInfosFromGameTopicRespBak.Inner)response.getData();
							if(inner==null){
								handleUnsuccessCode(6);
								return;
							}
							List<GameInfo> infos=inner.getGames();
							
							//temp
//							List<GameInfo> infos = response.getGames();
							
							if(infos!=null && !infos.isEmpty()){
								GameTopicInfo gameTopicInfo = (GameTopicInfo)reqConfig.getData();
								List<TopicToGame> topicToGameList = new ArrayList<TopicToGame>();
								List<ScreenShotInfo> screenShotInfos=new ArrayList<ScreenShotInfo>();
								TopicToGame topicToGame;
								int returnOrder=1;
								for (GameInfo gameinfo : infos) {
									for (ScreenShotInfo screenShot : gameinfo.getImgs()) {
										screenShot.setGameId(gameinfo.getGameId());
										screenShotInfos.add(screenShot);
									}
									DaoHelper.delGameScreenShotInfo(context, gameinfo.getGameId());
									
									topicToGame = new TopicToGame();
									topicToGame.setGameId(gameinfo.getGameId());
									topicToGame.setTopicId(gameTopicInfo.getTopicId());
									topicToGame.setGameInfo(gameinfo);
									topicToGame.setGameTopicInfo(gameTopicInfo);
									topicToGame.setType(gameTopicInfo.getType());
									topicToGame.setReturnOrder(returnOrder);
									topicToGameList.add(topicToGame);
									returnOrder++;
								}
								
								DaoHelper.saveGameScreenShotInfo(context, screenShotInfos);
								DaoHelper.saveGameInfo(context, infos);
								DaoHelper.saveTopicToGame(context, topicToGameList);
								
								mTaskResult.setData(topicToGameList);
							}
							mTaskResult.setCode(TaskResult.OK);
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 获取专题下的游戏（版权游戏）
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月6日 下午8:51:47
	 */
	public static void fetchGameInfosFromGameTopic2(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<GameTopicInfo> httpReqCallback) {
		alog.info("");
		String url = UrlConstant.GAME_INFOS_FROM_GAME_TOPIC;
		GameTopicInfo info = (GameTopicInfo)reqConfig.getData();
		String topicId=info.getTopicId();
		int type=info.getType();
		String postData = HttpReqDataHelper.gameInfosFromGameTopic(context, topicId, type);
		GsonObjectRequest<GameInfosFromGameTopicRespBak> request = new GsonObjectRequest<GameInfosFromGameTopicRespBak>(
				url, postData, GameInfosFromGameTopicRespBak.class,
				new ReqListener<GameInfosFromGameTopicRespBak,GameTopicInfo>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(GameInfosFromGameTopicRespBak response) {
						alog.debug(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){							
							GameInfosFromGameTopicRespBak.Inner inner=(GameInfosFromGameTopicRespBak.Inner)response.getData();
							if(inner==null){
								handleUnsuccessCode(6);
								return;
							}
							List<GameInfo> infos=inner.getGames();
							
							//temp
//							List<GameInfo> infos = response.getGames();
							
							if(infos!=null && !infos.isEmpty()){
								GameTopicInfo gameTopicInfo = (GameTopicInfo)reqConfig.getData();
								List<TopicToGame> topicToGameList = new ArrayList<TopicToGame>();
								List<ScreenShotInfo> screenShotInfos=new ArrayList<ScreenShotInfo>();
								TopicToGame topicToGame;
								int returnOrder=1;
								for (GameInfo gameinfo : infos) {
									for (ScreenShotInfo screenShot : gameinfo.getImgs()) {
										screenShot.setGameId(gameinfo.getGameId());
										screenShotInfos.add(screenShot);
									}
									DaoHelper.delGameScreenShotInfo(context, gameinfo.getGameId());
									
									topicToGame = new TopicToGame();
									topicToGame.setGameId(gameinfo.getGameId());
									topicToGame.setTopicId(gameTopicInfo.getTopicId());
									topicToGame.setGameInfo(gameinfo);
									topicToGame.setGameTopicInfo(gameTopicInfo);
									topicToGame.setType(gameTopicInfo.getType());
									topicToGame.setReturnOrder(returnOrder);
									topicToGameList.add(topicToGame);
									returnOrder++;
								}
								
								DaoHelper.saveGameScreenShotInfo(context, screenShotInfos);
								DaoHelper.saveGameInfo(context, infos);
								DaoHelper.saveTopicToGame(context, topicToGameList);
								
								gameTopicInfo.resetTopicToGameList();
								mTaskResult.setData(gameTopicInfo);
							}
							mTaskResult.setCode(TaskResult.OK);
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 获取专题下的第三方游戏
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月6日 下午9:09:00
	 */
	public static void fetchGameInfosFromGameTopicThird(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<List<TopicToGame>> httpReqCallback) {
		alog.info("");
		String url = UrlConstant.GAME_INFOS_FROM_GAME_TOPIC;
		GameTopicInfo info = (GameTopicInfo)reqConfig.getData();
		String topicId=info.getTopicId();
		int type=info.getType();
		String postData = HttpReqDataHelper.gameInfosFromGameTopic(context, topicId, type);
		GsonObjectRequest<ThirdGameInfosFromGameTopicRespBak> request = new GsonObjectRequest<ThirdGameInfosFromGameTopicRespBak>(
				url, postData, GameInfosFromGameTopicRespBak.class,
				new ReqListener<ThirdGameInfosFromGameTopicRespBak,List<TopicToGame>>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(ThirdGameInfosFromGameTopicRespBak response) {
						alog.debug(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){							
							ThirdGameInfosFromGameTopicRespBak.Inner inner=(ThirdGameInfosFromGameTopicRespBak.Inner)response.getData();
							List<ThirdGameInfo> infos=inner.getGames();
							
							//test
//							List<ThirdGameInfo> infos=response.getGames();
							
							if(infos!=null && !infos.isEmpty()){
								GameTopicInfo gameTopicInfo = (GameTopicInfo)reqConfig.getData();
								List<TopicToGame> topicToGameList = new ArrayList<TopicToGame>();
								List<ThirdGameDownInfo> screenShotInfos=new ArrayList<ThirdGameDownInfo>();
								TopicToGame topicToGame;
								int returnOrder=1;
								for (ThirdGameInfo gameinfo : infos) {
									for (ThirdGameDownInfo screenShot : gameinfo.getDownloadInfo()) {
										screenShot.setGameId(gameinfo.getGameId());
										screenShotInfos.add(screenShot);
									}
									DaoHelper.delThirdGameDownInfo(context, gameinfo.getGameId());
									
									topicToGame = new TopicToGame();
									topicToGame.setGameId(gameinfo.getGameId());
									topicToGame.setTopicId(gameTopicInfo.getTopicId());
									topicToGame.setThirdGameInfo(gameinfo);
									topicToGame.setGameTopicInfo(gameTopicInfo);
									topicToGame.setType(gameTopicInfo.getType());
									topicToGame.setReturnOrder(returnOrder);
									topicToGameList.add(topicToGame);
									returnOrder++;
								}
								
								DaoHelper.saveThirdGameDownInfo(context, screenShotInfos);
								DaoHelper.saveThirdGameInfo(context, infos);
								DaoHelper.saveTopicToGame(context, topicToGameList);
								
								mTaskResult.setData(topicToGameList);
							}
							mTaskResult.setCode(TaskResult.OK);
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 获取专题下的第三方游戏
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月6日 下午9:09:00
	 */
	public static void fetchGameInfosFromGameTopicThird2(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<GameTopicInfo> httpReqCallback) {
		alog.info("");
		String url = UrlConstant.GAME_INFOS_FROM_GAME_TOPIC;
		GameTopicInfo info = (GameTopicInfo)reqConfig.getData();
		String topicId=info.getTopicId();
		int type=info.getType();
		String postData = HttpReqDataHelper.gameInfosFromGameTopic(context, topicId, type);
		GsonObjectRequest<ThirdGameInfosFromGameTopicRespBak> request = new GsonObjectRequest<ThirdGameInfosFromGameTopicRespBak>(
				url, postData, ThirdGameInfosFromGameTopicRespBak.class,
				new ReqListener<ThirdGameInfosFromGameTopicRespBak,GameTopicInfo>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(ThirdGameInfosFromGameTopicRespBak response) {
						alog.debug(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){							
							ThirdGameInfosFromGameTopicRespBak.Inner inner=(ThirdGameInfosFromGameTopicRespBak.Inner)response.getData();
							List<ThirdGameInfo> infos=inner.getGames();
							
							//test
//							List<ThirdGameInfo> infos=response.getGames();
							
							if(infos!=null && !infos.isEmpty()){
								GameTopicInfo gameTopicInfo = (GameTopicInfo)reqConfig.getData();
								List<TopicToGame> topicToGameList = new ArrayList<TopicToGame>();
								List<ThirdGameDownInfo> screenShotInfos=new ArrayList<ThirdGameDownInfo>();
								TopicToGame topicToGame;
								int returnOrder=1;
								for (ThirdGameInfo gameinfo : infos) {
									for (ThirdGameDownInfo screenShot : gameinfo.getDownloadInfo()) {
										screenShot.setGameId(gameinfo.getGameId());
										screenShotInfos.add(screenShot);
									}
									DaoHelper.delThirdGameDownInfo(context, gameinfo.getGameId());
									
									topicToGame = new TopicToGame();
									topicToGame.setGameId(gameinfo.getGameId());
									topicToGame.setTopicId(gameTopicInfo.getTopicId());
									topicToGame.setThirdGameInfo(gameinfo);
									topicToGame.setGameTopicInfo(gameTopicInfo);
									topicToGame.setType(gameTopicInfo.getType());
									topicToGame.setReturnOrder(returnOrder);
									topicToGameList.add(topicToGame);
									returnOrder++;
								}
								
								DaoHelper.saveThirdGameDownInfo(context, screenShotInfos);
								DaoHelper.saveThirdGameInfo(context, infos);
								DaoHelper.saveTopicToGame(context, topicToGameList);
								
								gameTopicInfo.resetTopicToGameList();
								mTaskResult.setData(gameTopicInfo);
							}
							mTaskResult.setCode(TaskResult.OK);
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 游戏搜索
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月7日 下午1:40:10
	 */
	public static void fetchGameSearchPinyinInfo(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<List<GameSearchPinyinInfo>> httpReqCallback) {
		alog.info("");
		String url = UrlConstant.GAME_SEARCH_PINYIN;
		String postData = HttpReqDataHelper.gameSearchPinyin(context);
		GsonObjectRequest<GameSearchPinyinResp> request = new GsonObjectRequest<GameSearchPinyinResp>(
				url, postData, GameSearchPinyinResp.class,
				new ReqListener<GameSearchPinyinResp,List<GameSearchPinyinInfo>>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(GameSearchPinyinResp response) {
						alog.debug(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){
							mTaskResult.setCode(TaskResult.OK);
							mTaskResult.setData(response.getData());
							
							DaoHelper.saveGameSearchInfo(context, response.getData());
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 获取广告信息
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月8日 下午8:45:05
	 */
	public static void fetchGameCenterInfo(final Context context, final ReqConfig reqConfig, 
			final HttpReqCallback<List<AdModelInfo>> httpReqCallback) {
		alog.debug("");
		String url = UrlConstant.ADS;
		final String modelId = (String)reqConfig.getData();
		String postData = HttpReqDataHelper.ads(context, modelId, 0l);
		printPostData(postData);
		GsonObjectRequest<AdModelInfoResp> request = new GsonObjectRequest<AdModelInfoResp>(
				url, postData, AdModelInfoResp.class,
				new ReqListener<AdModelInfoResp,List<AdModelInfo>>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(AdModelInfoResp response) {
						printRawResponseData(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){
							List<AdModelInfo> adModelInfos=response.getData();
							if(adModelInfos==null || adModelInfos.isEmpty()){
								handleUnsuccessCode(SERVER_RETURN_CODE.NO_DATA);
								return;
							}
							
							List<Extramap> extramapList = new ArrayList<Extramap>();
							List<ActionUrl> actionUrlList=new ArrayList<ActionUrl>();
							List<AdInfo> adInfoList=new ArrayList<AdInfo>();
							List<ModelToAd> modelToAdList = new ArrayList<ModelToAd>();
							ModelToAd modelToAd;
							ActionUrl actionUrl=null;
							int returnOrder = 1;
							
							for (AdModelInfo info: adModelInfos) {
								info.setModelId(modelId);
								DaoHelper.saveAdModelInfo(context, info);
								info.resetModelToAdList();
								
								List<AdInfo> ads = info.getAds();
								if(modelId.equals(DataConfig.AD_MODEL_ID_GAME_CENTER)){
									ads = sortCenterAdInfo(ads);
								}
								
								adInfoList.addAll(ads);
//								alog.info(ads.toString());
								
								returnOrder = 1;
								for (AdInfo adInfo : ads) {
									modelToAd = new ModelToAd();
									modelToAd.setAdId(adInfo.getAdId());
									modelToAd.setModelKey(info.getId());
									modelToAd.setReturnOrder(returnOrder++);
									modelToAdList.add(modelToAd);
									
									adInfo.setModelKey(info.getId());
									adInfo.setActionUrlId(adInfo.getAdId());
									
									try {
										actionUrl=adInfo.getActionUrl();
										if(actionUrl!=null){
											actionUrl.setAdId(adInfo.getAdId());
											actionUrlList.add(actionUrl);

											if(actionUrl.getExtramap()!=null){
												DaoHelper.delExtramaWithAdId(context, actionUrl.getAdId());

												for (Extramap extramap : actionUrl.getExtramap()) {
													extramap.setActionUrlId(adInfo.getActionUrlId());
													extramapList.add(extramap);
												}
											}
										}
									} catch (Exception e) {
										// TODO: handle exception
									}

								}
							}

							if(!extramapList.isEmpty()){
								DaoHelper.saveExtramap(context, extramapList);
							}
							if(!actionUrlList.isEmpty()){
								DaoHelper.saveAdsActionUrl(context, actionUrlList);
							}
							if(!adInfoList.isEmpty()){
								DaoHelper.saveAdsInfo(context, adInfoList);
							}
							if(!modelToAdList.isEmpty()){
								DaoHelper.saveModelToAd(context, modelToAdList);
							}

							mTaskResult.setCode(TaskResult.OK);
							mTaskResult.setData(adModelInfos);
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 获取开机广告
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月9日 下午7:08:26
	 */
	public static void fetchLaunchAdsInfo(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<List<AdInfo>> httpReqCallback) {
		alog.debug("");
		String url = UrlConstant.ADS;
		final String modelId = (String)reqConfig.getData();
		String postData = HttpReqDataHelper.ads(context, modelId, 0l);
		printPostData(postData);
		GsonObjectRequest<AdModelInfoResp> request = new GsonObjectRequest<AdModelInfoResp>(
				url, postData, AdModelInfoResp.class,
				new ReqListener<AdModelInfoResp,List<AdInfo>>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(AdModelInfoResp response) {
						printRawResponseData(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){
							List<AdModelInfo> adModelInfos=response.getData();
							if(adModelInfos==null || adModelInfos.isEmpty()){
								handleUnsuccessCode(SERVER_RETURN_CODE.NO_DATA);
								return;
							}
							
							List<Extramap> extramapList = new ArrayList<Extramap>();
							List<ActionUrl> actionUrlList=new ArrayList<ActionUrl>();
							List<AdInfo> adInfoList=new ArrayList<AdInfo>();
							ActionUrl actionUrl=null;
							
							for (AdModelInfo info: adModelInfos) {
								info.setModelId(modelId);
								DaoHelper.saveAdModelInfo(context, info);
								
								List<AdInfo> ads = info.getAds();
								adInfoList.addAll(ads);
//								alog.info(ads.toString());
								for (AdInfo adInfo : ads) {
									adInfo.setModelKey(info.getId());
									adInfo.setActionUrlId(adInfo.getAdId());
									
									try {
										actionUrl=adInfo.getActionUrl();
										if(actionUrl!=null){
											actionUrl.setAdId(adInfo.getAdId());
											actionUrlList.add(actionUrl);

											if(actionUrl.getExtramap()!=null){
												DaoHelper.delExtramaWithAdId(context, actionUrl.getAdId());

												for (Extramap extramap : actionUrl.getExtramap()) {
													extramap.setActionUrlId(adInfo.getActionUrlId());
													extramapList.add(extramap);
												}
											}
										}
									} catch (Exception e) {
										// TODO: handle exception
									}

								}
							}

							if(!extramapList.isEmpty()){
								DaoHelper.saveExtramap(context, extramapList);
							}
							if(!actionUrlList.isEmpty()){
								DaoHelper.saveAdsActionUrl(context, actionUrlList);
							}
							if(!adInfoList.isEmpty()){
								DaoHelper.saveAdsInfo(context, adInfoList);
							}

							mTaskResult.setCode(TaskResult.OK);
							mTaskResult.setData(adModelInfos.get(0).getAds());
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 获取活动详情
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月9日 下午9:27:25
	 */
	public static void fetchActivityInfos(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<List<ActInfo>> httpReqCallback) {
		alog.debug("");
		String url = UrlConstant.ACTIITY;
		final Integer type = (Integer)reqConfig.getData();
		String postData = HttpReqDataHelper.activity(context, 0);
		printPostData(postData);
		GsonObjectRequest<ActivityInfoResp> request = new GsonObjectRequest<ActivityInfoResp>(
				url, postData, ActivityInfoResp.class,
				new ReqListener<ActivityInfoResp,List<ActInfo>>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(ActivityInfoResp response) {
						printRawResponseData(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){
							List<ActInfo> infos=response.getData();
							List<ActDetailPhoto> detailPhotoList = new ArrayList<ActDetailPhoto>();
							for (ActInfo actInfo : infos) {
								List<ActDetailPhoto> detailPhotos = actInfo.getDetailPhotos();
								for (ActDetailPhoto actDetailPhoto : detailPhotos) {
									actDetailPhoto.setActivityId(actInfo.getActivityId());
								}
								detailPhotoList.addAll(detailPhotos);
							}
							if(detailPhotoList.size()>0){
								DaoHelper.saveActivityDetailPictures(context, detailPhotoList);
							}
							DaoHelper.saveActInfo(context, infos);
							
							if(type == DataConfig.ACTIVITY_TYPE_RECOMMEND){
								infos = DaoHelper.getRecommendActivityInfo(context, type);
							}
							mTaskResult.setCode(TaskResult.OK);
							mTaskResult.setData(infos);
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 商品
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月9日 下午9:54:47
	 */
	public static void fetchGoodsInfos(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<List<GoodsInfo>> httpReqCallback) {
		alog.debug("");
		String url = UrlConstant.GOODS;
		final Integer type = (Integer)reqConfig.getData();
		String postData = HttpReqDataHelper.goods(context, 0);
		printPostData(postData);
		GsonObjectRequest<GoodsInfoResp> request = new GsonObjectRequest<GoodsInfoResp>(
				url, postData, GoodsInfoResp.class,
				new ReqListener<GoodsInfoResp,List<GoodsInfo>>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(GoodsInfoResp response) {
						printRawResponseData(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){
							List<GoodsInfo> infos=response.getData();
							List<GoodsDetailPhoto> detailPhotoList = new ArrayList<GoodsDetailPhoto>();
							for (GoodsInfo goodsInfo : infos) {
								List<GoodsDetailPhoto> detailPhotos = goodsInfo.getDetailPhotos();
								for (GoodsDetailPhoto goodsDetailPhoto : detailPhotos) {
									goodsDetailPhoto.setGoodsId(goodsInfo.getGoodsId());
								}
								detailPhotoList.addAll(detailPhotos);
							}
							if(detailPhotoList.size()>0){
								DaoHelper.saveGoodsDetailPictures(context, detailPhotoList);
							}
							
							DaoHelper.saveGoodsInfo(context, infos);
							
							if(type == DataConfig.GOODS_TYPE_RECOMMEND){
								infos = DaoHelper.getRecommendGoodsInfo(context, type);
							}
							mTaskResult.setCode(TaskResult.OK);
							mTaskResult.setData(infos);
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 礼包
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月9日 下午10:35:12
	 */
	public static void fetchGameGiftInfos(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<List<GameGiftInfo>> httpReqCallback) {
		alog.debug("");
		String url = UrlConstant.GIFT;
		String postData = HttpReqDataHelper.gift(context);
		printPostData(postData);
		GsonObjectRequest<GameGiftInfoResp> request = new GsonObjectRequest<GameGiftInfoResp>(
				url, postData, GameGiftInfoResp.class,
				new ReqListener<GameGiftInfoResp,List<GameGiftInfo>>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(GameGiftInfoResp response) {
						printRawResponseData(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){
							List<GameGiftInfo> infos=response.getData();
							List<GiftInfo> giftInfoList = new ArrayList<GiftInfo>();
							for (GameGiftInfo gameGiftInfo : infos) {
								List<GiftInfo> giftInfos = gameGiftInfo.getGiftData();
								
								for (GiftInfo info: giftInfos) {
									info.setGameId(gameGiftInfo.getGameid());
								}
								giftInfoList.addAll(giftInfos);
							}
							
							alog.debug("giftInfoList:"+giftInfoList.toString());
							DaoHelper.saveGiftInfo(context, giftInfoList);
							DaoHelper.saveGameGiftInfo(context, infos);
							
							mTaskResult.setCode(TaskResult.OK);
							mTaskResult.setData(infos);
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 获取用户领取礼包
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月9日 下午10:56:40
	 */
	public static void fetchUserGiftInfos(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<List<UserGameGiftInfo>> httpReqCallback) {
		alog.debug("");
		String url = UrlConstant.USER_GIFT;
		final String userId = (String)reqConfig.getData();
		String postData = HttpReqDataHelper.userGift(context, userId);
		printPostData(postData);
		GsonObjectRequest<UserGiftInfoResp> request = new GsonObjectRequest<UserGiftInfoResp>(
				url, postData, UserGiftInfoResp.class,
				new ReqListener<UserGiftInfoResp,List<UserGameGiftInfo>>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(UserGiftInfoResp response) {
						printRawResponseData(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){
							List<UserGiftInfo> infos=response.getData();
							Map<String, UserGiftInfo> gameIdMap = new HashMap<String, UserGiftInfo>();
							UserGameGiftInfo userGameGiftInfo;
							UserGameToGift userGameToGift;
							List<UserGameToGift> userGameToGiftList = new ArrayList<UserGameToGift>();
							List<UserGameGiftInfo> userGameGiftInfoList = new ArrayList<UserGameGiftInfo>();
							int returnOrder = 1;
							for (UserGiftInfo userGiftInfo : infos) {
								if(!gameIdMap.containsKey(userGiftInfo.getGameId())){
									gameIdMap.put(userGiftInfo.getGameId(), userGiftInfo);
									
									userGameGiftInfo = new UserGameGiftInfo();
									userGameGiftInfo.setGameid(userGiftInfo.getGameId());
									userGameGiftInfo.setGameName(userGiftInfo.getGameName());
									userGameGiftInfo.setMinPhoto(userGiftInfo.getIcon());
									
									userGameGiftInfoList.add(userGameGiftInfo);
								}
								
								userGameToGift = new UserGameToGift();
								userGameToGift.setGameId(userGiftInfo.getGameId());
								userGameToGift.setGiftPackageid(userGiftInfo.getGiftPackageid());
								userGameToGift.setUserId(userId);
								userGameToGift.setReturnOrder(returnOrder++);
								
								userGameToGiftList.add(userGameToGift);
							}
							
							DaoHelper.saveUserGiftInfo(context, infos);
							DaoHelper.saveUserGameGiftInfo(context, userGameGiftInfoList);
							DaoHelper.saveUserGameToGift(context, userGameToGiftList);
							
							mTaskResult.setCode(TaskResult.OK);
							mTaskResult.setData(userGameGiftInfoList);
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 领取礼包
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月10日 上午10:40:11
	 */
	public static void obtainUserGift(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<String> httpReqCallback) {
		alog.debug("");
		String url = UrlConstant.OBTAIN_USER_GIFT;
		final ObtainUserGiftReq obtainUserGiftReq= (ObtainUserGiftReq)reqConfig.getData();
		String postData = HttpReqDataHelper.obtainUserGift(context, obtainUserGiftReq.getUserId(), obtainUserGiftReq.getGiftPackageid());
		printPostData(postData);
		GsonObjectRequest<ObtainUserGiftResp> request = new GsonObjectRequest<ObtainUserGiftResp>(
				url, postData, ObtainUserGiftResp.class,
				new ReqListener<ObtainUserGiftResp,String>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(ObtainUserGiftResp response) {
						printRawResponseData(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){
							mTaskResult.setCode(TaskResult.OK);
							mTaskResult.setData(response.getGiftCode());
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 获取公告
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月10日 上午11:31:19
	 */
	public static void fetchNoticeInfos(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<List<NoticeInfo>> httpReqCallback) {
		alog.debug("");
		String url = UrlConstant.NOTICE;
		final int noticeType = (Integer)reqConfig.getData();
		String postData = HttpReqDataHelper.notice(context, noticeType);
		printPostData(postData);
		GsonObjectRequest<NoticeInfoResp> request = new GsonObjectRequest<NoticeInfoResp>(
				url, postData, NoticeInfoResp.class,
				new ReqListener<NoticeInfoResp,List<NoticeInfo>>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(NoticeInfoResp response) {
						printRawResponseData(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){
							List<NoticeInfo> infos=response.getData();
							DaoHelper.saveNoticeInfo(context, infos);
							
							infos = DaoHelper.getNoticeInfo(context, noticeType);
							if(infos.size()>0){
								mTaskResult.setCode(TaskResult.OK);
								mTaskResult.setData(infos);
							} else {
								handleUnsuccessCode(6);
							}
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 获取视频
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月10日 上午11:31:19
	 */
	public static void fetchVideoInfos(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<List<VideoInfo>> httpReqCallback) {
		alog.debug("");
		String url = UrlConstant.VIDEO;
		final String videoType = (String)reqConfig.getData();
		String postData = HttpReqDataHelper.video(context, videoType);
		printPostData(postData);
		GsonObjectRequest<VideoInfoResp> request = new GsonObjectRequest<VideoInfoResp>(
				url, postData, VideoInfoResp.class,
				new ReqListener<VideoInfoResp,List<VideoInfo>>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(VideoInfoResp response) {
						printRawResponseData(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){
							List<VideoInfo> infos=response.getData();
							for (VideoInfo videoInfo : infos) {
								videoInfo.setVideoType(videoType);
							}
							DaoHelper.saveVideoInfo(context, infos);
							
							mTaskResult.setCode(TaskResult.OK);
							mTaskResult.setData(infos);
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	
	/**
	 * @description: 对游戏进行评分
	 *
	 * @param context
	 * @param reqConfig 
	 * @throws: 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午2:07:24
	 */
	public static void rateGame(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<String> httpReqCallback) {
		alog.debug("");
		String url = UrlConstant.RATE_GAME;
		final RateGameReq rateGameReq= (RateGameReq)reqConfig.getData();
		String postData = HttpReqDataHelper.rateGame(context, rateGameReq.getGameId(), 
				rateGameReq.getUserId(), rateGameReq.getScore());
		printPostData(postData);
		GsonObjectRequest<RateGameResp> request = new GsonObjectRequest<RateGameResp>(
				url, postData, RateGameResp.class,
				new ReqListener<RateGameResp,String>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(RateGameResp response) {
						printRawResponseData(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){
							GameScoreRecordInfo info = new GameScoreRecordInfo();
							info.setGameId(rateGameReq.getGameId());
							info.setUserId(rateGameReq.getUserId());
							info.setScore(rateGameReq.getScore());
							DaoHelper.saveGameScoreRecordInfo(context, info);
							
							//任务的游戏评分记录
							UserTaskDaoHelper.saveGameScore(context, rateGameReq.getGameId(), Integer.parseInt(rateGameReq.getUserId()), rateGameReq.getScore(), 0, 0);
							
							mTaskResult.setCode(TaskResult.OK);
							mTaskResult.setData("OK");
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	
	/**
	 * @description: 使用game id获取第三方游戏
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年7月5日 下午7:21:23
	 */
	public static void fetchThirdGameInfoFromGameId(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<List<ThirdGameInfo>> httpReqCallback) {
		alog.info("");
		String url = UrlConstant.THIRD_GAME_INFO_FROM_GAME_ID;
		String gameId=(String)reqConfig.getData();
		String postData = HttpReqDataHelper.thirdGameInfoFromGameId(context, gameId);
		GsonObjectRequest<ThirdGameInfoResp> request = new GsonObjectRequest<ThirdGameInfoResp>(
				url, postData, ThirdGameInfoResp.class,
				new ReqListener<ThirdGameInfoResp,List<ThirdGameInfo>>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(ThirdGameInfoResp response) {
						alog.debug(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){							
							List<ThirdGameInfo> infos=response.getData();
							if(infos!=null && infos.size()>0){
								List<ThirdGameDownInfo> thirdGameDownInfoInfos=new ArrayList<ThirdGameDownInfo>();
								for (ThirdGameInfo thirdGameInfo : infos) {
									for (ThirdGameDownInfo downInfo : thirdGameInfo.getDownloadInfo()) {
										downInfo.setGameId(thirdGameInfo.getGameId());
										thirdGameDownInfoInfos.add(downInfo);
									}
									DaoHelper.delThirdGameDownInfo(context, thirdGameInfo.getGameId());
								}
								
								DaoHelper.saveThirdGameDownInfo(context, thirdGameDownInfoInfos);
								DaoHelper.saveThirdGameInfo(context, infos);
	
								mTaskResult.setData(infos);
							}
							mTaskResult.setCode(TaskResult.OK);
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 获取版权游戏同分类下的6个游戏
	 *
	 * @param context
	 * @param reqConfig
	 * @param httpReqCallback 
	 * @author: LiuQin
	 * @date: 2015年7月13日 下午9:59:06
	 */
	public static void fetchRelativeGames(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<List<GameInfo>> httpReqCallback) {
		alog.info("");
		String url = UrlConstant.GAME_INFOS_FROM_RELATIVE;
		String gameId = (String)reqConfig.getData();
		String postData = HttpReqDataHelper.relativeGame(context, gameId);
		GsonObjectRequest<GameInfoResp> request = new GsonObjectRequest<GameInfoResp>(
				url, postData, GameInfoResp.class,
				new ReqListener<GameInfoResp,List<GameInfo>>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(GameInfoResp response) {
						alog.debug(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){							
							List<GameInfo> infos=response.getData();
							if(infos!=null && infos.size()>0){
								List<ScreenShotInfo> screenShotInfos=new ArrayList<ScreenShotInfo>();
								for (GameInfo gameinfo : infos) {
									for (ScreenShotInfo screenShot : gameinfo.getImgs()) {
										screenShot.setGameId(gameinfo.getGameId());
										screenShotInfos.add(screenShot);
									}
									DaoHelper.delGameScreenShotInfo(context, gameinfo.getGameId());
								}
								
								DaoHelper.saveGameScreenShotInfo(context, screenShotInfos);
								DaoHelper.saveGameInfo(context, infos);
								
								mTaskResult.setData(infos);
							}
							mTaskResult.setCode(TaskResult.OK);
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 排序游戏中心广告
	 *
	 * @param adInfos
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月16日 上午11:11:26
	 */
	private static List<AdInfo> sortCenterAdInfo(List<AdInfo> adInfos){
		List<AdInfo> shu= new ArrayList<AdInfo>();
		List<AdInfo> fang= new ArrayList<AdInfo>();
		List<AdInfo> heng= new ArrayList<AdInfo>();
		List<AdInfo> yixing= new ArrayList<AdInfo>();
		String sizeType;
		for (AdInfo info: adInfos) {
			alog.warn("adId:"+info.getAdId());
			sizeType = info.getSizeType();
			if(sizeType.equals("shu")){
				shu.add(info);
			} else if(sizeType.equals("fang")){
				fang.add(info);
			} else if(sizeType.equals("heng")){
				heng.add(info);
			} else if(sizeType.equals("yixing")){
				yixing.add(info);
			}
		}
		
		List<AdInfo> result = new ArrayList<AdInfo>();
		result.add(yixing.get(0));
		result.addAll(heng);
		
		while(fang.size()>0){
			result.add(fang.remove(0));
			if(fang.size()>0){
				result.add(fang.remove(0));
			} else {
				break;
			}
			if(shu.size()>0){
				result.add(shu.remove(0));
			}
		}
		
		adInfos.clear();
		adInfos.addAll(result);
		result.clear();
		result=adInfos;
		return result;
	}
	
	/**
	 * @description: 获取任务
	 *
	 * @param context
	 * @param reqConfig
	 * @param httpReqCallback 
	 * @author: LiuQin
	 * @date: 2015年7月18日 下午2:41:43
	 */
	public static void fetchUserTask(final Context context, final ReqConfig reqConfig, final UserTaskReq reqInfo,
			final HttpReqCallback<UserTaskResp> httpReqCallback) {
		alog.info("");
		String url = UrlConstant.USER_TASK;
//		UserTaskReq reqInfo = (UserTaskReq)reqConfig.getData();
		int userId=reqInfo.getUserId();
		int taskFlag=reqInfo.getFlag();
		String gameId=reqInfo.getGameId();
		String taskId=reqInfo.getTaskId();
		String ruleId=reqInfo.getRuleId();
		
//		DataFetcher.initUserInfo(context);
//		userId=DataFetcher.getUserIdInt();
		String postData = HttpReqDataHelper.getAllUserTask(context, userId, taskFlag, gameId, taskId, ruleId);
		
		printPostData(postData);
		GsonObjectVerifyRequest<UserTaskResp> request = new GsonObjectVerifyRequest<UserTaskResp>(
				url, postData, UserTaskResp.class,
				new ReqListener<UserTaskResp,UserTaskResp>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(UserTaskResp response) {
						alog.debug(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){							
							mTaskResult.setCode(TaskResult.OK);
							mTaskResult.setData(response);
						} else {
							handleUnsuccessCode(code);
							mTaskResult.setMsg(response.getDesc());
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 获取新版本信息
	 *
	 * @param context
	 * @param reqConfig
	 * @param httpReqCallback 
	 * @throws: 
	 * @author: LiuQin
	 * @date: 2015年7月28日 下午3:36:56
	 */
	public static void fetchNewVersionInfo(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<NewVersionInfoResp> httpReqCallback) {
		alog.info("");
		String url = UrlConstant.NEW_VERSION_INFO;
		String appId = (String)reqConfig.getData();
		String postData = HttpReqDataHelper.newVersionInfo(context, appId);
		printPostData(postData);
		GsonObjectRequest<NewVersionInfoResp> request = new GsonObjectRequest<NewVersionInfoResp>(
				url, postData, NewVersionInfoResp.class,
				new ReqListener<NewVersionInfoResp,NewVersionInfoResp>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(NewVersionInfoResp response) {
						alog.debug(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS || HttpHelper.isNoDataCode(code+"")){							
							if(code != SERVER_RETURN_CODE.SUCCESS) {
								//没有新版本信息
								response.setVersionCode(-1);
							}
							mTaskResult.setCode(TaskResult.OK);
							mTaskResult.setData(response);
							
							SpHelper.put(context, SpHelper.KEY_NEW_VERSION_INFO, MyJsonPaser.toJson(response));
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 获取用户的商品订单
	 *
	 * @param context
	 * @param reqConfig
	 * @param httpReqCallback 
	 * @author: LiuQin
	 * @date: 2015年7月29日 下午7:03:18
	 */
	public static void fetchUserGoodsOrderInfo(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<List<UserGoodsOrderInfo>> httpReqCallback) {
		alog.info("");
		String url = UrlConstant.USER_GOODS_ORDER;
		final String userId = (String)reqConfig.getData();
		String postData = HttpReqDataHelper.userGoodsOrder(context, userId);
		printPostData(postData);
		GsonObjectRequest<UserGoodsOrderInfoResp> request = new GsonObjectRequest<UserGoodsOrderInfoResp>(
				url, postData, UserGoodsOrderInfoResp.class,
				new ReqListener<UserGoodsOrderInfoResp,List<UserGoodsOrderInfo>>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(UserGoodsOrderInfoResp response) {
						alog.debug(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){							
							mTaskResult.setCode(TaskResult.OK);
							mTaskResult.setData(response.getData());
							
							response.setElapsedRealTime(SystemClock.elapsedRealtime());
							String key = UserGoodsOrderInfo.class.getSimpleName() + "_" + userId;
							DataHelper.sIdentityScope.put(key, response);
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 兑换商品
	 *
	 * @param context
	 * @param reqConfig
	 * @param httpReqCallback 
	 * @author: LiuQin
	 * @date: 2015年7月30日 上午11:12:11
	 */
	public static void exchangeGoods(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<GoodsExchangeResp> httpReqCallback) {
		alog.info("");
		String url = UrlConstant.EXCHANGE_GOODS;
		GoodsExchangeReq info = (GoodsExchangeReq)reqConfig.getData();
		String postData = HttpReqDataHelper.goodsExchange(context, info);
		printPostData(postData);
		GsonObjectVerifyRequest<GoodsExchangeResp> request = new GsonObjectVerifyRequest<GoodsExchangeResp>(
				url, postData, GoodsExchangeResp.class,
				new ReqListener<GoodsExchangeResp,GoodsExchangeResp>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(GoodsExchangeResp response) {
						alog.debug(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){							
							mTaskResult.setCode(TaskResult.OK);
							mTaskResult.setData(response);
							
							UserInfo userInfo = BaseApplication.userInfo;
							if(userInfo!=null){
								userInfo.setIntegral(response.getIntegral());
								
								int coupon = userInfo.getCoupon();
								if(!TextUtils.isEmpty(response.getCoupon()) && TextUtils.isDigitsOnly(response.getCoupon())){
									coupon = Integer.parseInt(response.getCoupon());
								}
								DataHelper.updateUserInfo(userInfo, userInfo.getBalance(), response.getIntegral(), coupon);
							}
						} else {
							handleUnsuccessCode(code);
							mTaskResult.setMsg(response.getDesc());
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 注册用户
	 * 
	 * @author: LiuQin
	 * @date: 2015年8月2日 下午3:43:26
	 */
	public static void userRegister(Context context, final ReqConfig reqConfig) {
		final Context appContext = context.getApplicationContext();
		final UserLoginRegisterReq reqInfo = (UserLoginRegisterReq)reqConfig.getData();
		
		String atetId = DataHelper.getDeviceInfo().getAtetId();
		String channelId = DataHelper.getDeviceInfo().getChannelId();
		String deviceCode = DataHelper.getDeviceInfo().getDeviceModel();
		String deviceId = DataHelper.getDeviceInfo().getServerId();
		
		ATETRegist regist = new ATETRegist(context, reqInfo.getPhoneNum(), reqInfo.getPassword(), reqInfo.getCaptcha(),
				atetId, channelId, deviceCode, deviceId);
		Task task = reqConfig.getTask();
		task.setObject(regist);
		regist.callback(new SignCallBack() {
			public void result(int code, String desc, int userId, String icon,
					int coupon, int integral, int balance) {
				alog.debug("code:"+code + "desc:"+desc);
				TaskResult<UserInfo> taskResult = null;
				UserInfo info = null;
				if (code == LOGIN.SUCCESS) {
					info = new UserInfo(userId, reqInfo.getPhoneNum(), reqInfo.getPhoneNum(), 
							icon, integral, balance, coupon);
					alog.debug(info);
					DataHelper.updateUserInfo(info);
					taskResult = TaskResult.makeSuccessTaskResult(info);
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
	 * @description: 帐号登录
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年8月2日 下午4:35:27
	 */
	public static void userLogin(Context context, final ReqConfig reqConfig) {
		final Context appContext = context.getApplicationContext();
		final UserLoginRegisterReq reqInfo = (UserLoginRegisterReq)reqConfig.getData();
		ATETLogin login = new ATETLogin(context, reqInfo.getPhoneNum(), reqInfo.getPassword());
		Task task = reqConfig.getTask();
		task.setObject(login);
		login.atetLogin(new SignCallBack() {
			public void result(int code, String desc, int userId, String icon,
					int coupon, int integral, int balance) {
				alog.debug("code:"+code + "desc:"+desc);
				TaskResult<UserInfo> taskResult = null;
				UserInfo info = null;
				if (code == LOGIN.SUCCESS) {
					info = new UserInfo(userId, reqInfo.getPhoneNum(), reqInfo.getPhoneNum(), 
							icon, integral, balance, coupon);
					alog.debug(info.toString());
					DataHelper.updateUserInfo(info);
					taskResult = TaskResult.makeSuccessTaskResult(info);
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
	 * @description: 自动登录最后一个帐号
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年8月2日 下午10:28:19
	 */
	public static void userAutoLogin(Context context, final ReqConfig reqConfig) {
		final Context appContext = context.getApplicationContext();
//		final UserLoginRegisterReq reqInfo = (UserLoginRegisterReq)reqConfig.getData();
		AutoSignIn signin = new AutoSignIn(context);
		Task task = reqConfig.getTask();
		if(task!=null){
			task.setObject(signin);
		}
		signin.signIn(new SignCallBack() {
			public void result(int code, String desc, int userId, String icon,
					int coupon, int integral, int balance) {				
				alog.debug("code:"+code + "desc:"+desc);
				TaskResult<UserInfo> taskResult = null;
				UserInfo info = null;
				if (code == LOGIN.SUCCESS) {
					info = new UserInfo(userId, null, null, 
							icon, integral, balance, coupon);
					alog.debug(info.toString());
					UserInfo userInfo = BaseApplication.userInfo;
					if(userInfo!=null && userId == userInfo.getUserId()){
						DataHelper.updateUserInfo(userInfo, balance, integral, coupon);
					}
					taskResult = TaskResult.makeSuccessTaskResult(info);
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
	 * @description: 自动登录指定userid的帐号
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年8月2日 下午10:28:19
	 */
	public static void userAutoLoginByUserId(Context context, final ReqConfig reqConfig) {
		final Context appContext = context.getApplicationContext();
		Integer userId = (Integer)reqConfig.getData();
		AutoSignIn signin = new AutoSignIn(context, userId);
		Task task = reqConfig.getTask();
		task.setObject(signin);
		signin.signIn(new SignCallBack() {
			public void result(int code, String desc, int userId, String icon,
					int coupon, int integral, int balance) {				
				alog.debug("code:"+code + "desc:"+desc);
				TaskResult<UserInfo> taskResult = null;
				UserInfo info = null;
				if (code == LOGIN.SUCCESS) {
					info = new UserInfo(userId, null, null, 
							icon, integral, balance, coupon);
					alog.debug(info.toString());
//					DataHelper.updateUserInfo(info);
					taskResult = TaskResult.makeSuccessTaskResult(info);
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
	 * @description: 获取手机验证码
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年8月3日 下午10:21:57
	 */
	public static void userGetCaptcha(Context context, final ReqConfig reqConfig) {
		final Context appContext = context.getApplicationContext();
		final UserGetCaptchaReq reqInfo = (UserGetCaptchaReq)reqConfig.getData();
		
		GetCode messCode = new GetCode(context, reqInfo.getPhoneNum(), reqInfo.getType());// 注册用户时获取验证码
		Task task = reqConfig.getTask();
		task.setObject(messCode);
		messCode.result(new GetMessCodeCallBack() {
			public void result(int code, String desc) {
				alog.debug("code:"+code + "desc:"+desc);
				TaskResult<Void> taskResult = null;
				if (code == LOGIN.SUCCESS) {
					taskResult = TaskResult.makeSuccessTaskResult(null);
				} else {
					if(TextUtils.isEmpty(desc)){
						desc = appContext.getString(R.string.fail_msg_unknown_error, code);
					}
					taskResult = TaskResult.makeFailTaskResult(code, desc, null);
				}
				DataHelper.onCallback(appContext, reqConfig, taskResult);
			}
		});
		reqConfig.getTask().setObject(messCode);
	}
	
	/**
	 * @description: 微信登录
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年8月3日 下午10:44:44
	 */
	public static void userWeChatLogin(Context context, final ReqConfig reqConfig) {
		final Context appContext = context.getApplicationContext();
		final UserWeChatLoginReq reqInfo = (UserWeChatLoginReq)reqConfig.getData();
		
		WechatLogin wechat = new WechatLogin(context, reqInfo.getUserId(), reqInfo.getUsername(), reqInfo.getToken());
		Task task = reqConfig.getTask();
		task.setObject(wechat);
		wechat.result(new SignCallBack() {
			public void result(int code, String desc, int userId, String icon,
					int coupon, int integral, int balance) {
				TaskResult<UserInfo> taskResult = null;
				UserInfo info = null;
				if (code == LOGIN.SUCCESS) {
					info = new UserInfo(userId, reqInfo.getUsername(), null, 
							icon, integral, balance, coupon);
					alog.debug(info.toString());
					DataHelper.updateUserInfo(info);
					taskResult = TaskResult.makeSuccessTaskResult(info);
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
	 * @description: 更新密码
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年8月4日 下午10:33:04
	 */
	public static void userUpdatePassword(Context context, final ReqConfig reqConfig) {
		final Context appContext = context.getApplicationContext();
		final UserModifyReq reqInfo = (UserModifyReq)reqConfig.getData();
		
		UpdateUserInfo update = new UpdateUserInfo(context, reqInfo.getUserId(),
				reqInfo.getOldPassword(), reqInfo.getNewPassword(), UpdateUserInfo.UPDATE_PASSWORD);
		Task task = reqConfig.getTask();
		task.setObject(update);
		update.result(new UpdateUserInfoCallBack() {
			public void result(int code, String desc) {
				alog.debug("code:"+code + "desc:"+desc);
				TaskResult<UserInfo> taskResult = null;
				if(code == LOGIN.SUCCESS){
					taskResult = TaskResult.makeSuccessTaskResult(null);
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
	 * @description: 更新手机号
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年8月4日 下午10:33:04
	 */
	public static void userUpdatePhoneNum(Context context, final ReqConfig reqConfig) {
		final Context appContext = context.getApplicationContext();
		final UserModifyReq reqInfo = (UserModifyReq)reqConfig.getData();
		
		UpdateUserInfo update = new UpdateUserInfo(context, reqInfo.getUserId(), reqInfo.getCaptcha(),
				reqInfo.getNewPhone(), UpdateUserInfo.UPDATE_PHONE);// 这里最后一个参数改成了UpdateUserInfo.UPDATE_PHONE
		Task task = reqConfig.getTask();
		task.setObject(update);
		update.result(new UpdateUserInfoCallBack() {

			public void result(int code, String desc) {
				alog.debug("code:"+code + "desc:"+desc);
				TaskResult<UserInfo> taskResult = null;
				if(code == LOGIN.SUCCESS){
					taskResult = TaskResult.makeSuccessTaskResult(null);
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
	 * @description: 找回密码
	 *
	 * @param context
	 * @param reqConfig 
	 * @author: LiuQin
	 * @date: 2015年8月14日 下午3:25:19
	 */
	public static void resetPassword(Context context, final ReqConfig reqConfig) {
		final Context appContext = context.getApplicationContext();
		final UserLoginRegisterReq reqInfo = (UserLoginRegisterReq)reqConfig.getData();
		
		ATETResetPassword reset = new ATETResetPassword(context, reqInfo.getPhoneNum(), reqInfo.getCaptcha());
		reset.resetPassword(new ResetPasswordCallBack() {
			public void result(int code, String desc, int userId) {
				alog.debug("code:"+code + "desc:"+desc);
				TaskResult<UserInfo> taskResult = null;
				if(code == LOGIN.SUCCESS){
					taskResult = TaskResult.makeSuccessTaskResult(null);
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
	 * @description: 数据更新接口
	 *
	 * @param context
	 * @param reqConfig
	 * @param httpReqCallback 
	 * @author: LiuQin
	 * @date: 2015年8月6日 下午4:22:06
	 */
	public static void fetchLocalUpdatableInterface(final Context context, final ReqConfig reqConfig, UpdateInterfaceReq reqInfo,
			final HttpReqCallback<List<LocalUpdateInfo>> httpReqCallback) {
		alog.info("");
		String url = UrlConstant.UPDATE_INTERFACE;
		if(reqInfo == null){
			reqInfo = (UpdateInterfaceReq)reqConfig.getData();
		}
		String postData = HttpReqDataHelper.updateInterface(context, reqInfo.getLastTime());
		GsonObjectRequest<UpdateInterfaceResp> request = new GsonObjectRequest<UpdateInterfaceResp>(
				url, postData, UpdateInterfaceResp.class,
				new ReqListener<UpdateInterfaceResp,List<LocalUpdateInfo>>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(UpdateInterfaceResp response) {
						alog.debug(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){							
							List<UpdateInterfaceInfo> infos=response.getData();
							List<LocalUpdateInfo> localUpdateInfoList = new ArrayList<LocalUpdateInfo>();
							if(infos!=null && infos.size()>0){
								List<UpdateInterfaceInfo> updateInterfaceInfoList = new ArrayList<UpdateInterfaceInfo>();
								LocalUpdateInfo localUpdateInfo;
								UpdateInterfaceInfo info = null;
								String interfaceName;
								String uniqueId;
								for (UpdateInterfaceInfo updateInterfaceInfo : infos) {
									interfaceName = updateInterfaceInfo.getName();
									uniqueId = interfaceName;
									if(!DataHelper.isSupportedInterface(interfaceName)){
										continue;
									}
									
									if(updateInterfaceInfo.getTypes()!=null
											&& (interfaceName.equals(UpdateInterface.GAME_TYPE_DETAIL) 
													|| interfaceName.equals(UpdateInterface.GAME_TOPIC_DETAIL)
													|| interfaceName.equals(UpdateInterface.ADS)
													|| interfaceName.equals(UpdateInterface.VIDEO_GUIDE)
													|| interfaceName.equals(UpdateInterface.QUERY_VERSION)
													|| interfaceName.equals(UpdateInterface.NOTICE)
													|| interfaceName.equals(UpdateInterface.GAME_RANKING))){
										for (UpdateInterfaceInner inner : updateInterfaceInfo.getTypes()) {
											if(interfaceName.equals(UpdateInterface.ADS)){
												if(!inner.getKey().equals(DataConfig.AD_MODEL_ID_GAME_CENTER) 
														&& !inner.getKey().equals(DataConfig.AD_MODEL_ID_LAUNCH)){
													continue;
												}
											} else if(interfaceName.equals(UpdateInterface.GAME_RANKING)) {
												if(!inner.getKey().equals(DataConfig.GAME_RANKING_TYPE_GAMEPAD+"") 
														&& !inner.getKey().equals(DataConfig.GAME_RANKING_TYPE_DOWNLOAD+"")
														&& !inner.getKey().equals(DataConfig.GAME_RANKING_TYPE_REMOTE_CONTROLLER+"")){
													continue;
												}
											} else if(interfaceName.equals(UpdateInterface.VIDEO_GUIDE)) {
												if(!inner.getKey().equals(DataConfig.VIDEO_TYPE_GUIDE)) {
													continue;
												}
											} else if(interfaceName.equals(UpdateInterface.QUERY_VERSION)) {
												if(!inner.getKey().equals(DataConfig.MARKET_APPID)) {
													continue;
												}
											} else if(interfaceName.equals(UpdateInterface.NOTICE)) {
												if(!inner.getKey().equals(DataConfig.NOTICE_TYPE_TIPS+"") 
														&& !inner.getKey().equals(DataConfig.NOTICE_TYPE_EMERGENCY+"")){
													continue;
												}
											}
											
											uniqueId = interfaceName+inner.getKey();
											info = new UpdateInterfaceInfo();
											info.setUniqueId(uniqueId);
											info.setName(interfaceName);
											info.setSubId(inner.getKey());
											info.setUpdateTime(inner.getTime());
											
											updateInterfaceInfoList.add(info);
											
											
											if(!DaoHelper.isLocalUpdateInfoExist(context, uniqueId)){
												localUpdateInfo = new LocalUpdateInfo();
												localUpdateInfo.setLocalUniqueId(uniqueId);
												localUpdateInfo.setLocalName(interfaceName);
												localUpdateInfo.setLocalSubId(info.getSubId() == null ? "" : info.getSubId());
												localUpdateInfo.setLocalUpdateTime(0l);
												localUpdateInfo.setLocalState(DataConfig.INTERFACE_STATE_SEE);
												localUpdateInfoList.add(localUpdateInfo);
											}
										}
									} else {
										updateInterfaceInfo.setUniqueId(interfaceName);
										info = updateInterfaceInfo;
										updateInterfaceInfoList.add(info);
									}
									
									if(!DaoHelper.isLocalUpdateInfoExist(context, uniqueId)){
										localUpdateInfo = new LocalUpdateInfo();
										localUpdateInfo.setLocalUniqueId(uniqueId);
										localUpdateInfo.setLocalName(interfaceName);
										localUpdateInfo.setLocalSubId(info.getSubId() == null ? "" : info.getSubId());
										localUpdateInfo.setLocalUpdateTime(0l);
										localUpdateInfo.setLocalState(DataConfig.INTERFACE_STATE_SEE);
										localUpdateInfoList.add(localUpdateInfo);
									}
								}
								
								if(updateInterfaceInfoList.size()>0){
									DaoHelper.saveUpdateInterfaceInfo(context, updateInterfaceInfoList);
								}
								if(localUpdateInfoList.size()>0){
									DaoHelper.saveLocalUpdateInfo(context, localUpdateInfoList);
								}
								SpHelper.put(context, SpHelper.KEY_UPDATE_INTERFACE, response.getLastTime());
																
								localUpdateInfoList.clear();
							}
							localUpdateInfoList = DaoHelper.getLocalUpdatableInterface(context);
							mTaskResult.setData(localUpdateInfoList);
							mTaskResult.setCode(TaskResult.OK);
						} else if(code == SERVER_RETURN_CODE.NO_DATA){
							List<LocalUpdateInfo> localUpdateInfoList = DaoHelper.getLocalUpdatableInterface(context);
							mTaskResult.setData(localUpdateInfoList);
							mTaskResult.setCode(TaskResult.OK);
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	public static void fetchAllUpdateInterface(final Context context, final ReqConfig reqConfig, UpdateInterfaceReq reqInfo,
			final HttpReqCallback<UpdateInterfaceResp> httpReqCallback) {
		alog.info("");
		String url = UrlConstant.UPDATE_INTERFACE;
		String postData = HttpReqDataHelper.updateInterface(context, reqInfo.getLastTime());
		GsonObjectRequest<UpdateInterfaceResp> request = new GsonObjectRequest<UpdateInterfaceResp>(
				url, postData, UpdateInterfaceResp.class,
				new ReqListener<UpdateInterfaceResp,UpdateInterfaceResp>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(UpdateInterfaceResp response) {
						alog.debug(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){							
							mTaskResult.setData(response);
							mTaskResult.setCode(TaskResult.OK);
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 上传错误信息
	 *
	 * @param context
	 * @param reqInfo
	 * @param clazz
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年9月1日 下午3:57:40
	 */
	public static <T extends AutoType> T uploadExceptionLog(Context context, UploadExceptionReq reqInfo, Class<T> clazz) {
		String url = UrlConstant.EXCEPTION_UPLOAD;
		T t = null;
		try {
			MultipartEntity reqEntity = new MultipartEntity();
			if(reqInfo!=null){
				String params = "?deviceId=" + Uri.encode(reqInfo.getDeviceId())
						+ "&productId=" + Uri.encode(reqInfo.getProductId())
						+ "&packageName="+ Uri.encode(reqInfo.getPackageName())
						+ "&appName=" + Uri.encode(reqInfo.getAppName())
						+ "&versionName=" + Uri.encode(reqInfo.getVersionName())
						+ "&exceptionName=" + Uri.encode(reqInfo.getExceptionName());
				url += params;

//				Charset charset = Charset.forName("UTF-8");
//				reqEntity.addPart("deviceId", new StringBody(reqInfo.getDeviceId(), charset));
//				reqEntity.addPart("productId", new StringBody(reqInfo.getProductId(), charset));
//				reqEntity.addPart("packageName", new StringBody(reqInfo.getPackageName(), charset));
//				reqEntity.addPart("appName", new StringBody(reqInfo.getAppName(), charset));
//				reqEntity.addPart("versionName", new StringBody(reqInfo.getVersionName(), charset));
//				reqEntity.addPart("exceptionName", new StringBody(reqInfo.getExceptionName(), charset));

				if(reqInfo.getFile()!=null && reqInfo.getFile().exists()){
					FileBody bin = new FileBody(reqInfo.getFile());
					reqEntity.addPart("file", bin);
				}
			}

			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, 30000);
			HttpConnectionParams.setSoTimeout(params, 30000);
			HttpConnectionParams.setTcpNoDelay(params, true);

			DefaultHttpClient httpclient = new DefaultHttpClient(params);
			HttpPost httppost = new HttpPost(url);
			//		 HttpConnectionParams.setSoTimeout(params, timeout)
			httppost.setEntity(reqEntity);

			alog.debug("execute: " + httppost.getRequestLine());
			HttpResponse response = httpclient.execute(httppost);
			alog.debug("statusCode is "
					+ response.getStatusLine().getStatusCode());

			HttpEntity resEntity = response.getEntity();
			if (resEntity != null) {
				InputStream is = resEntity.getContent();
				StringBuilder str = new StringBuilder();
				byte[] bs = new byte[1024];
				while (true) {
					int i = is.read(bs);
					if (i < 0) {
						break;
					}
					str.append(new String(bs, 0, i, "utf-8"));
				}
				alog.debug("response" + str);
				t = MyJsonPaser.fromJson(str.toString(), clazz);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}
	
	/**
	 * @description: 抽奖信息
	 *
	 * @param context
	 * @param reqConfig
	 * @param httpReqCallback 
	 * @author: LiuQin
	 * @date: 2015年9月12日 下午8:17:58
	 */
	public static void fetchRewardInfo(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<RewardInfoResp> httpReqCallback) {
		alog.info("");
		String url = UrlConstant.REWARD_INFO;
		int userId = (Integer)reqConfig.getData();
		String postData = HttpReqDataHelper.getRewardInfo(context, userId);
		
		printPostData(postData);
		GsonObjectVerifyRequest<RewardInfoResp> request = new GsonObjectVerifyRequest<RewardInfoResp>(
				url, postData, RewardInfoResp.class,
				new ReqListener<RewardInfoResp,RewardInfoResp>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(RewardInfoResp response) {
						alog.debug(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){							
							List<RewardInfoResp.RewardList> rewardList = response.getRewardList();
							Collections.sort(rewardList, new RewardComparator());
							response.setRewardList(rewardList);
							
							mTaskResult.setCode(TaskResult.OK);
							mTaskResult.setData(response);
						} else {
							handleUnsuccessCode(code);
							mTaskResult.setMsg(response.getDesc());
						}
					}
					
					class RewardComparator implements Comparator<RewardInfoResp.RewardList>{
						@Override
						public int compare(RewardInfoResp.RewardList lhs, RewardInfoResp.RewardList rhs) {
							
							return lhs.getSequence()-rhs.getSequence();
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 领取抽奖的奖励
	 *
	 * @param context
	 * @param reqConfig
	 * @param httpReqCallback 
	 * @author: LiuQin
	 * @date: 2015年9月12日 下午8:46:07
	 */
	public static void obtainReward(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<ObtainRewardResp> httpReqCallback) {
		alog.info("");
		String url = UrlConstant.OBTAIN_REWARD;
		ObtainRewardReq reqInfo = (ObtainRewardReq)reqConfig.getData();
		String postData = HttpReqDataHelper.ObtainReward(context, reqInfo.getUserId(), reqInfo.getRewardId());
		
		printPostData(postData);
		GsonObjectVerifyRequest<ObtainRewardResp> request = new GsonObjectVerifyRequest<ObtainRewardResp>(
				url, postData, ObtainRewardResp.class,
				new ReqListener<ObtainRewardResp,ObtainRewardResp>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(ObtainRewardResp response) {
						alog.debug(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){						
							updateUserInfo(context);
							
							mTaskResult.setCode(TaskResult.OK);
							mTaskResult.setData(response);
						} else {
							handleUnsuccessCode(code);
							mTaskResult.setData(response);
							mTaskResult.setMsg(response.getDesc());
						}
						SpHelper.put(context, SpHelper.KEY_FETCH_REWARD_TIME, TimeHelper.getCurTime());
					}
					
					public void updateUserInfo(Context context){
						resetLock();
						alog.debug("");
						ReqCallback<Void> reqCallback = new ReqCallback<Void>() {
							@Override
							public void onResult(TaskResult<Void> taskResult) {
								int code = taskResult.getCode();
								alog.info("taskResult code:" + code);
								if (code == TaskResult.OK) {
								}

								closeLock();
							}
						};
						ReqConfig reqConfig2 = ReqConfig.makeReqConfig(ReqCode.USER_AUTO_LOGIN, reqCallback, false);
						HttpHelper.userAutoLogin(context, reqConfig2);

						block();
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
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 获取所有游戏的下载次数和星级
	 *
	 * @param context
	 * @param reqConfig
	 * @param httpReqCallback 
	 * @author: LiuQin
	 * @date: 2015年9月20日 下午11:39:52
	 */
	public static void fetchDownStarCount(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<DownStarResp> httpReqCallback) {
		alog.info("");
		String url = UrlConstant.DOWN_STAR;
		String postData = HttpReqDataHelper.downStar(context);
		GsonObjectRequest<DownStarResp> request = new GsonObjectRequest<DownStarResp>(
				url, postData, DownStarResp.class,
				new ReqListener<DownStarResp,DownStarResp>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(DownStarResp response) {
						alog.debug(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){							
							mTaskResult.setData(response);
							mTaskResult.setCode(TaskResult.OK);
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
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
	public static void fetchAppConfig(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<AppConfigResp> httpReqCallback) {
		alog.info("");
		String url = UrlConstant.APP_CONFIG;
		String serverId = (String)reqConfig.getData();
		String postData = HttpReqDataHelper.deviceIdOnly(context, serverId);
		GsonObjectRequest<AppConfigResp> request = new GsonObjectRequest<AppConfigResp>(
				url, postData, AppConfigResp.class,
				new ReqListener<AppConfigResp,AppConfigResp>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(AppConfigResp response) {
						alog.debug(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){							
							if(response.getModules()!=null && !response.getModules().isEmpty()){
								StringBuilder sb = new StringBuilder();
								for (String name : response.getModules()) {
									sb.append(name+",");
								}
								AppConfig.saveAppModule(sb.toString());
							}
							AppConfig.saveStatisticsOnBootFlag(response.getStartUp() == 1);
							mTaskResult.setData(response);
							mTaskResult.setCode(TaskResult.OK);
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 获取商品的剩余数量
	 *
	 * @param context
	 * @param reqConfig
	 * @param httpReqCallback 
	 * @author: LiuQin
	 * @date: 2015年10月18日 下午2:31:45
	 */
	public static void fetchGoodsCount(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<GoodsLeftCountInfo> httpReqCallback) {
		alog.info("");
		String url = UrlConstant.GOODS_LEFT_COUNT;
		String postData = HttpReqDataHelper.deviceIdOnly(context, DataHelper.getDeviceInfo().getServerId());
		GsonObjectRequest<GoodsLeftCountResp> request = new GsonObjectRequest<GoodsLeftCountResp>(
				url, postData, GoodsLeftCountInfo.class,
				new ReqListener<GoodsLeftCountResp,GoodsLeftCountInfo>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(GoodsLeftCountResp response) {
						alog.debug(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){							
							String goodsId = (String)reqConfig.getData();
							List<GoodsLeftCountInfo> infos = response.getData();
							GoodsLeftCountInfo result = null;
							if(infos!=null && infos.size()>0){
								for (GoodsLeftCountInfo goodsLeftCountInfo : infos) {
									if(goodsId.equals(goodsLeftCountInfo.getGoodsId())){
										result = goodsLeftCountInfo;
										break;
									}
								}
							}
							if(result != null){
								mTaskResult.setData(result);
								mTaskResult.setCode(TaskResult.OK);
							} else {
								handleUnsuccessCode(6);
							}
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	public static void fetchGoodsCountSingle(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<GoodsLeftCountInfo> httpReqCallback) {
		alog.info("");
		String url = UrlConstant.GOODS_LEFT_COUNT;
//		final String goodsId = (String)reqConfig.getData();
		final GoodsInfo reqInfo= (GoodsInfo)reqConfig.getData();
		String postData = HttpReqDataHelper.goodsId(context, reqInfo.getGoodsId());
		GsonObjectRequest<GoodsLeftCountInfo> request = new GsonObjectRequest<GoodsLeftCountInfo>(
				url, postData, GoodsLeftCountInfo.class,
				new ReqListener<GoodsLeftCountInfo,GoodsLeftCountInfo>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(GoodsLeftCountInfo response) {
						alog.debug(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS){							
							GoodsInfo info = DaoHelper.getGoodsInfo(context, reqInfo.getGoodsId());
							if(info!=null){
								info.setSurplus(response.getSurplus());
								info.update();
							}
							
							reqInfo.setSurplus(response.getSurplus());
							response.setGoodsId(reqInfo.getGoodsId());
							mTaskResult.setData(response);
							mTaskResult.setCode(TaskResult.OK);
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
	
	/**
	 * @description: 同步时间
	 *
	 * @param context
	 * @param reqConfig
	 * @param httpReqCallback 
	 * @author: LiuQin
	 * @date: 2015年10月19日 上午1:20:42
	 */
	public static void syncTime(final Context context, final ReqConfig reqConfig,
			final HttpReqCallback<Long> httpReqCallback) {
		alog.info("");
		String url = UrlConstant.TIME;
		GsonObjectRequest<SyncTimeResp> request = new GsonObjectRequest<SyncTimeResp>(
				url, null, SyncTimeResp.class,
				new ReqListener<SyncTimeResp,Long>(context, reqConfig, httpReqCallback) {
					@Override
					public void onSuccess(SyncTimeResp response) {
						alog.debug(response.toString());
						int code=response.getCode();
						if(code == SERVER_RETURN_CODE.SUCCESS && response.getTime()>0){							
							mTaskResult.setData(response.getTime());
							mTaskResult.setCode(TaskResult.OK);
							
							if(!TimeHelper.isServerTimeSyn()){
								TimeHelper.setServerTimePoint(response.getTime());
							}
						} else {
							handleUnsuccessCode(code);
						}
					}
				});
		request.setSynchronized(true);
		request.setTag(reqConfig.getTaskId());
		TaskQueue.getInstanse(context).add(request);
	}
}
