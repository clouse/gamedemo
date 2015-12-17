package com.atet.tvmarket.model;

import java.util.List;

import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.app.Configuration;
import com.atet.tvmarket.entity.AppConfig;
import com.atet.tvmarket.entity.AppConfigResp;
import com.atet.tvmarket.entity.DownStarResp;
import com.atet.tvmarket.entity.DownloadFileReq;
import com.atet.tvmarket.entity.FileDownResultInfo;
import com.atet.tvmarket.entity.GoodsExchangeReq;
import com.atet.tvmarket.entity.GoodsExchangeResp;
import com.atet.tvmarket.entity.GoodsLeftCountResp.GoodsLeftCountInfo;
import com.atet.tvmarket.entity.NewVersionInfoResp;
import com.atet.tvmarket.entity.ObtainRewardReq;
import com.atet.tvmarket.entity.ObtainRewardResp;
import com.atet.tvmarket.entity.ObtainUserGiftReq;
import com.atet.tvmarket.entity.RateGameReq;
import com.atet.tvmarket.entity.RewardInfoResp;
import com.atet.tvmarket.entity.UpdateInterfaceReq;
import com.atet.tvmarket.entity.UserGetCaptchaReq;
import com.atet.tvmarket.entity.UserGoodsOrderInfo;
import com.atet.tvmarket.entity.UserInfo;
import com.atet.tvmarket.entity.UserLoginRegisterReq;
import com.atet.tvmarket.entity.UserModifyReq;
import com.atet.tvmarket.entity.UserTaskReq;
import com.atet.tvmarket.entity.UserWeChatLoginReq;
import com.atet.tvmarket.entity.dao.ActInfo;
import com.atet.tvmarket.entity.dao.AdInfo;
import com.atet.tvmarket.entity.dao.GameGiftInfo;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.entity.dao.GameSearchPinyinInfo;
import com.atet.tvmarket.entity.dao.GameTopicInfo;
import com.atet.tvmarket.entity.dao.GameTypeInfo;
import com.atet.tvmarket.entity.dao.GoodsInfo;
import com.atet.tvmarket.entity.dao.LocalUpdateInfo;
import com.atet.tvmarket.entity.dao.NoticeInfo;
import com.atet.tvmarket.entity.dao.ThirdGameInfo;
import com.atet.tvmarket.entity.dao.TopicToGame;
import com.atet.tvmarket.entity.dao.TypeToGame;
import com.atet.tvmarket.entity.dao.UserGameGiftInfo;
import com.atet.tvmarket.entity.dao.VideoInfo;
import com.atet.tvmarket.model.ReqConfig.ReqCode;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.model.usertask.UserTask;
import com.atet.tvmarket.model.usertask.UserTaskInfo;

import android.content.Context;

public class DataFetcher {
	
	public static DataRequester getServerId(Context context, ReqCallback<String> reqCallback, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.FETCH_SERVER_ID, reqCallback, null, isMain));
	}
	
	/**
	 * @description: 获取游戏分类
	 *
	 * @param context
	 * @param reqCallback
	 * @param isMain 是否在主线程中回调
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午5:25:22
	 */
	public static DataRequester getGameType(Context context, ReqCallback<List<GameTypeInfo>> reqCallback, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.FETCH_GAME_TYPE, reqCallback, null, isMain));
	}
	
	/**
	 * @description: 获取某个分类下的游戏
	 *
	 * @param context
	 * @param typeInfo
	 * @param reqCallback
	 * @param isMain 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午5:22:32
	 */
	public static DataRequester getGameInfosFromGameType(Context context, GameTypeInfo typeInfo, 
			ReqCallback<List<TypeToGame>> reqCallback, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.FETCH_GAME_INFOS_FROM_GAME_TYPE, reqCallback,
				typeInfo, isMain));
	}
	
	/**
	 * @description: 获取某个分类下的游戏
	 *
	 * @param context
	 * @param typeInfo
	 * @param reqCallback
	 * @param isMain 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午5:22:32
	 */
	public static DataRequester getGameInfosFromGameType2(Context context, GameTypeInfo typeInfo, 
			ReqCallback<GameTypeInfo> reqCallback, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.FETCH_GAME_INFOS_FROM_GAME_TYPE2, reqCallback,
				typeInfo, isMain));
	}
	
	/**
	 * @description: 通过game id获取游戏
	 *
	 * @param context
	 * @param gameId
	 * @param reqCallback
	 * @param isMain 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午5:19:51
	 */
	public static DataRequester getGameInfoFromGameId(Context context, String gameId, 
			ReqCallback<List<GameInfo>> reqCallback, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.FETCH_GAME_INFO_FROM_GAME_ID, reqCallback,
				gameId, isMain));
	}
	
	/**
	 * @description: 获取新游推荐
	 *
	 * @param context
	 * @param reqCallback
	 * @param isMain 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午5:18:05
	 */
	public static DataRequester getGameNewUploaded(Context context, ReqCallback<List<GameInfo>> reqCallback, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.FETCH_GAME_INFOS_FROM_NEW_UPLOAD, reqCallback,
				null, isMain));
	}
	
	/**
	 * @description: 获取游戏排行
	 *
	 * @param context
	 * @param type
	 * @param reqCallback
	 * @param isMain 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午5:15:52
	 */
	public static DataRequester getGameRanking(Context context, int type, ReqCallback<List<GameInfo>> reqCallback, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.FETCH_GAME_INFOS_FROM_GAME_RANKING, reqCallback,
				type, isMain));
	}
	
	
	/**
	 * @description: 获取游戏专题
	 *
	 * @param context
	 * @param gameTopicInfo
	 * @param reqCallback
	 * @param isMain 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午5:11:43
	 */
	public static DataRequester getGameTopic(Context context, ReqCallback<List<GameTopicInfo>> reqCallback, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.FETCH_GAME_TOPIC, reqCallback, null, isMain));
	}
	
	/**
	 * @description: 获取专题下的游戏
	 *
	 * @param context
	 * @param gameTopicInfo
	 * @param reqCallback
	 * @param isMain 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午5:09:37
	 */
	public static DataRequester getGameInfosFromGameTopic(Context context, GameTopicInfo gameTopicInfo, 
			ReqCallback<List<TopicToGame>> reqCallback, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.FETCH_GAME_INFOS_FROM_GAME_TOPIC, reqCallback,
				gameTopicInfo, isMain));
	}
	
	/**
	 * @description: 获取专题下的游戏
	 *
	 * @param context
	 * @param gameTopicInfo
	 * @param reqCallback
	 * @param isMain 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午5:09:37
	 */
	public static DataRequester getGameInfosFromGameTopic2(Context context, GameTopicInfo gameTopicInfo, 
			ReqCallback<GameTopicInfo> reqCallback, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.FETCH_GAME_INFOS_FROM_GAME_TOPIC2, reqCallback,
				gameTopicInfo, isMain));
	}
	
	/**
	 * @description: 游戏拼音在数据库里是否存在数据
	 *
	 * @param context
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午4:59:56
	 */
	public static boolean isGameSearchPinyinDataExist(Context context){
		 return DaoHelper.getAllGameSearchInfoCount(context) > 0 ? false : true;
	}
	
	
	/**
	 * @description: 获取所有的游戏拼音
	 *
	 * @param context
	 * @param reqCallback
	 * @param isMain 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午4:54:20
	 */
	public static DataRequester getGameSearchPinyin(Context context, ReqCallback<List<GameSearchPinyinInfo>> reqCallback, boolean isMain) {
			return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.FETCH_GAME_SEARCH_PINYIN, reqCallback, null, isMain));
	}
	
	/**
	 * @description: 通过输入的拼音搜索对应的游戏
	 *
	 * @param context
	 * @param reqCallback
	 * @param isMain 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午4:56:16
	 */
	public static DataRequester getGameSearchPinyinByInput(Context context, String input,
			ReqCallback<List<GameSearchPinyinInfo>> reqCallback, boolean isMain) {
			return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.FETCH_GAME_SEARCH_PINYIN_BY_PINYIN, reqCallback, input, isMain));
	}
	
	/**
	 * @description: 获取游戏中心数据
	 *
	 * @param context
	 * @param reqCallback
	 * @param isMain 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午4:45:46
	 */
	public static DataRequester getGameCenterInfo(Context context, ReqCallback<List<AdInfo>> reqCallback, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.FETCH_GAME_CENTER_INFO, 
				reqCallback, DataConfig.AD_MODEL_ID_GAME_CENTER, isMain));
	}
		
	/**
	 * @description: 获取开机广告
	 *
	 * @param context
	 * @param reqCallback
	 * @param isMain 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午4:42:17
	 */
	public static DataRequester getLauchAd(Context context, ReqCallback<List<String>> reqCallback, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.FETCH_LAUNCH_AD, 
				reqCallback, DataConfig.AD_MODEL_ID_LAUNCH, isMain));
	}
	
	/**
	 * @description: 获取活动
	 *
	 * @param context
	 * @param actType
	 * @param reqCallback
	 * @param isMain 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午4:39:48
	 */
	public static DataRequester getAct(Context context, int actType,
			ReqCallback<List<ActInfo>> reqCallback, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.FETCH_ACTIVITY, reqCallback, actType, isMain));
	}
	
	/**
	 * @description: 获取商品
	 *
	 * @param context
	 * @param goodsType
	 * @param reqCallback
	 * @param isMain 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午4:36:25
	 */
	public static DataRequester getGoods(Context context, int goodsType,
			ReqCallback<List<GoodsInfo>> reqCallback, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.FETCH_GOODS, reqCallback, goodsType, isMain));
	}
	
	/**
	 * @description: 获取游戏礼包
	 *
	 * @param context
	 * @param reqCallback
	 * @param isMain 
	 * @throws: 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午4:33:58
	 */
	public static DataRequester getGameGift(Context context, ReqCallback<List<GameGiftInfo>> reqCallback, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.FETCH_GIFT, reqCallback, null, isMain));
	}
	
	/**
	 * @description: 领取礼包
	 *
	 * @param context
	 * @param userId
	 * @param giftId
	 * @param reqCallback
	 * @param isMain 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午4:24:55
	 */
	public static DataRequester obtainUserGift(Context context, String userId, String giftId, 
			ReqCallback<String> reqCallback, boolean isMain) {
		ObtainUserGiftReq obtainUserGiftReq=new ObtainUserGiftReq();
		obtainUserGiftReq.setUserId(userId);
		obtainUserGiftReq.setGiftPackageid(giftId);
		return DataHelper.makeDataRequester(context,
				ReqConfig.makeReqConfig(ReqCode.OBTAIN_USER_GIFT, reqCallback, obtainUserGiftReq, isMain));
	}
	
	/**
	 * @description: 获取用户领取的礼包，按游戏分类
	 *
	 * @param context
	 * @param userId
	 * @param reqCallback
	 * @param isMain
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月17日 下午7:17:57
	 */
	public static DataRequester getUserGameGift(Context context, String userId,
			ReqCallback<List<UserGameGiftInfo>> reqCallback, boolean isMain) {
		return DataHelper.makeDataRequester(context,
				ReqConfig.makeReqConfig(ReqCode.FETCH_USER_GIFT, reqCallback, userId, isMain));
	}
	
	/**
	 * @description: 游戏评分
	 *
	 * @param context 
	 * @param userId
	 * @param gameId 
	 * @param score
	 * @param reqCallback
	 * @param isMain 是否在主线程回调
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午4:10:31
	 */
	public static DataRequester rateGame(Context context, String userId, String gameId, int score, 
			ReqCallback<String> reqCallback, boolean isMain) {
		RateGameReq rateGameReq = new RateGameReq();
		rateGameReq.setUserId(userId);
		rateGameReq.setGameId(gameId);
		rateGameReq.setScore(score);

		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.RATE_GAME,
				reqCallback, rateGameReq, isMain));
	}
	
	/**
	 * @description: 用户是否对该游戏评分
	 *
	 * @param context
	 * @param gameId
	 * @param userId
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午4:09:40
	 */
	public static boolean isGameRated(Context context, String gameId, String userId){
		return DaoHelper.isGameRated(context, gameId, userId);
	}
	
	/**
	 * @description: 获取视频教学
	 *
	 * @param context
	 * @param reqCallback
	 * @param isMain 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午4:15:23
	 */
	public static DataRequester getVideoInfo(Context context, ReqCallback<List<VideoInfo>> reqCallback, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.FETCH_VIDEO, reqCallback, DataConfig.VIDEO_TYPE_GUIDE, isMain));
	}
	
	/**
	 * @description: 通过game id获取第三方游戏
	 *
	 * @param context
	 * @param gameId
	 * @param reqCallback
	 * @param isMain 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午5:19:51
	 */
	public static DataRequester getThirdGameInfoFromGameId(Context context, String gameId, 
			ReqCallback<List<ThirdGameInfo>> reqCallback, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.FETCH_THIRD_GAME_INFO_FROM_GAME_ID, reqCallback,
				gameId, isMain));
	}
	
	/**
	 * @description: 获取公告
	 *
	 * @param context
	 * @param noticeType
	 * @param reqCallback
	 * @param isMain 
	 * @throws: 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午4:18:41
	 */
	public static DataRequester getNoticeInfo(Context context, int noticeType, ReqCallback<List<NoticeInfo>> reqCallback, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.FETCH_NOTICE, reqCallback, noticeType, isMain));
	}
	
	/**
	 * @description: 用户是否登录
	 *
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午6:44:31
	 */
	public static boolean isUserLogin(){
		int userId = 0;
		UserInfo userInfo = BaseApplication.userInfo;
		if(userInfo!=null){
			userId = userInfo.getUserId();
		}
		
		return userId > 0;
	}
	
	public static String getUserId(){
		return getUserIdInt() + "";
	}
	
	public static int getUserIdInt(){
		int userId = 0;
		UserInfo userInfo = BaseApplication.userInfo;
		if(userInfo!=null){
			userId = userInfo.getUserId();
		}
		return userId;
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
	public static DataRequester getRelativeGameInfosFromGameId(Context context, String gameId, 
			ReqCallback<List<GameInfo>> reqCallback, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.FETCH_RELATIVE_GAME_INFO_FROM_GAME_ID, reqCallback,
				gameId, isMain));
	}
	
	/**
	 * @description: 获取推荐的游戏搜索
	 *
	 * @param context
	 * @param reqCallback
	 * @param isMain
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月14日 下午2:38:52
	 */
	public static DataRequester getRecommendGameSearchInfo(Context context, ReqCallback<List<GameSearchPinyinInfo>> reqCallback, boolean isMain) {
			return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.FETCH_RECOMMEND_GAME_SEARCH_INFO, reqCallback, null, isMain));
	}
	
	/**
	 * @description: 获取用户任务
	 *
	 * @param context
	 * @param reqCallback
	 * @param isMain
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月18日 下午3:31:13
	 */
	public static DataRequester getUserTask(Context context, ReqCallback<UserTask> reqCallback, boolean isMain) {
		UserTaskReq reqInfo = new UserTaskReq();
		reqInfo.setUserId(DataFetcher.getUserIdInt());;
		reqInfo.setFlag(DataConfig.TASK_FLAG_FETCH);
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.FETCH_USER_TASK, reqCallback, reqInfo, isMain));
	}
	
	/**
	 * @description: 获取用户任务
	 *
	 * @param context
	 * @param reqCallback
	 * @param isMain
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月18日 下午3:31:13
	 */
	public static DataRequester getUserTask2(Context context, ReqCallback<List<UserTask>> reqCallback, boolean isMain) {
		UserTaskReq reqInfo = new UserTaskReq();
		reqInfo.setUserId(DataFetcher.getUserIdInt());;
		reqInfo.setFlag(DataConfig.TASK_FLAG_FETCH);
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.FETCH_USER_TASK2, reqCallback, reqInfo, isMain));
	}
	
	public static DataRequester obtainReward(Context context, ReqCallback<UserTaskInfo> reqCallback, UserTaskInfo info, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.OBTAIN_USER_TASK_REWARD, reqCallback, info, isMain));
	}
	
	/**
	 * @description: 获取新版本信息
	 *
	 * @param context
	 * @param reqCallback
	 * @param isMain
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月29日 上午11:33:07
	 */
	public static DataRequester getNewVersionInfo(Context context, ReqCallback<NewVersionInfoResp> reqCallback, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.FETCH_NEW_VERSION_INFO, reqCallback, DataConfig.MARKET_APPID, isMain));
	}
	
	/**
	 * @description: 下载新版本的apk
	 *
	 * @param context
	 * @param reqCallback
	 * @param isMain
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月29日 下午2:12:55
	 */
	public static DataRequester downloadNewVersionApk(Context context, ReqCallback<FileDownResultInfo> reqCallback, 
			NewVersionInfoResp info, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.DOWNLOAD_NEW_VERSION_APK, reqCallback, info, isMain));
	}
	
	/**
	 * @description: 用户商品兑换记录
	 *
	 * @param context
	 * @param reqCallback
	 * @param isMain
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月29日 下午8:23:29
	 */
	public static DataRequester getUserGoodsOrderInfos(Context context, ReqCallback<List<UserGoodsOrderInfo>> reqCallback,
			String userId, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.USER_GOODS_ORDER, reqCallback, userId, isMain));
	}
	
	/**
	 * @description: 兑换商品
	 *
	 * @param context
	 * @param reqCallback
	 * @param info
	 * @param isMain
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月30日 下午1:40:53
	 */
	public static DataRequester exchangeGoods(Context context, ReqCallback<GoodsExchangeResp> reqCallback,
			GoodsExchangeReq info, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.EXCHANGE_GOODS, reqCallback, info, isMain));
	}
	
	/**
	 * @description: 注册新用户
	 *
	 * @param context
	 * @param reqCallback
	 * @param info
	 * @param isMain
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月4日 下午12:13:29
	 */
	public static DataRequester userRegister(Context context, ReqCallback<UserInfo> reqCallback,
			UserLoginRegisterReq info, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.USER_REGISTER, reqCallback, info, isMain));
	}
	
	/**
	 * @description: 用户登录
	 *
	 * @param context
	 * @param reqCallback
	 * @param info
	 * @param isMain
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月4日 下午12:13:50
	 */
	public static DataRequester userLogin(Context context, ReqCallback<UserInfo> reqCallback,
			 UserLoginRegisterReq info, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.USER_LOGIN, reqCallback, info, isMain));
	}
	
	/**
	 * @description: 获取验证码
	 *
	 * @param context
	 * @param reqCallback
	 * @param info
	 * @param isMain
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月4日 下午12:18:38
	 */
	public static DataRequester userGetCaptcha(Context context, ReqCallback<Void> reqCallback,
			UserGetCaptchaReq info, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.USER_GET_CAPTCHA, reqCallback, info, isMain));
	}
	
	/**
	 * @description: 微信用户登录
	 *
	 * @param context
	 * @param reqCallback
	 * @param info
	 * @param isMain
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月4日 下午12:20:48
	 */
	public static DataRequester userWeChatLogin(Context context, ReqCallback<UserInfo> reqCallback,
			UserWeChatLoginReq info, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.USER_WECHAT_LOGIN, reqCallback, info, isMain));
	}
	
	/**
	 * @description: 获取所有登录过的用户
	 *
	 * @param context
	 * @param reqCallback
	 * @param isMain
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月4日 下午3:22:57
	 */
	public static DataRequester userGetAllLoginedUsers(Context context, ReqCallback<List<UserInfo>> reqCallback, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.USER_GET_ALL_LOGINED_USERS, reqCallback, null, isMain));
	}
	
	/**
	 * @description: 获取最后登录的用户
	 *
	 * @param context
	 * @param reqCallback
	 * @param isMain
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月4日 下午3:23:54
	 */
	public static DataRequester userGetLastLoginedUser(Context context, ReqCallback<UserInfo> reqCallback, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.USER_GET_LAST_LOGINED_USER, reqCallback, null, isMain));
	}
	
	/**
	 * @description: 修改密码
	 *
	 * @param context
	 * @param reqCallback
	 * @param isMain
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月4日 下午10:43:59
	 */
	public static DataRequester userUpdatePassword(Context context, ReqCallback<Void> reqCallback,
			UserModifyReq info, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.USER_UPDATE_PASSWORD, reqCallback, info, isMain));
	}
	
	/**
	 * @description: 修改手机号
	 *
	 * @param context
	 * @param reqCallback
	 * @param isMain
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月4日 下午10:45:16
	 */
	public static DataRequester userUpdatePhoneNum(Context context, ReqCallback<Void> reqCallback,
			UserModifyReq info,boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.USER_UPDATE_PHONE_NUM, reqCallback, info, isMain));
	}
	
	
	/**
	 * @description: 找回密码
	 *
	 * @param context
	 * @param reqCallback
	 * @param info
	 * @param isMain
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月14日 下午3:32:44
	 */
	public static DataRequester resetPassword(Context context, ReqCallback<Void> reqCallback,
			UserLoginRegisterReq info, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.USER_RESET_PASSWORD, reqCallback, info, isMain));
	}
	
	/**
	 * @description: 获取可更新的接口
	 *
	 * @param context
	 * @param reqCallback
	 * @param info
	 * @param isMain
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月6日 下午6:36:25
	 */
	public static DataRequester getUpdatableInterface(Context context, ReqCallback<List<LocalUpdateInfo>> reqCallback,
			UpdateInterfaceReq info,boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.UPDATABLE_INTERFACE, reqCallback, info, isMain));
	}
	
	/**
	 * @description: 自动登录指定userId的帐号
	 *
	 * @param context
	 * @param reqCallback
	 * @param userId
	 * @param isMain
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月19日 上午10:55:10
	 */
	public static DataRequester userAutoLoginByUserId(Context context, ReqCallback<UserInfo> reqCallback,
			 Integer userId, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.USER_AUTO_LOGIN_BY_USERID, reqCallback, userId, isMain));
	}
	
	/**
	 * @description: 自动登录最后一个帐号
	 *
	 * @param context
	 * @param reqCallback
	 * @param userId
	 * @param isMain
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年10月9日 下午9:25:39
	 */
	public static DataRequester userAutoLogin(Context context, ReqCallback<UserInfo> reqCallback, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.USER_AUTO_LOGIN, reqCallback, null, isMain));
	}
	
	public static void initUserInfo(Context context){
		ReqCallback<UserInfo> reqCallback = new ReqCallback<UserInfo>() {
			@Override
			public void onResult(TaskResult<UserInfo> taskResult) {
				int code = taskResult.getCode();
				if (code == TaskResult.OK) {
					UserInfo info = taskResult.getData();
					BaseApplication.userInfo = info;
				}
			}
		};
		DataFetcher.userGetLastLoginedUser(context, reqCallback, false).request(context);
	}
	
	/**
	 * @description: 获取设备的性能等级
	 *
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月21日 下午3:49:17
	 */
	public static int getDeviceCapability(Context context){
		Integer capability = DataConfig.DeviceCapability.HIGH;
		return (Integer)SpHelper.get(context, SpHelper.KEY_DEVICE_CAPABILITY, capability);
	}
	
	/**
	 * @description: 下载文件
	 *
	 * @param context
	 * @param reqCallback
	 * @param url
	 * @param isMain
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月21日 下午10:39:29
	 */
	public static DataRequester downloadFile(Context context, ReqCallback<FileDownResultInfo> reqCallback, 
			DownloadFileReq reqInfo, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.DOWNLOAD_FILE, reqCallback, reqInfo, isMain));
	}
	
	/**
	 * @description: 抽奖信息
	 *
	 * @param context
	 * @param reqCallback
	 * @param isMain
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年9月12日 下午8:24:17
	 */
	public static DataRequester getRewardInfo(Context context, ReqCallback<RewardInfoResp> reqCallback, int userId, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.FETCH_REWARD_INFO, reqCallback, userId, isMain));
	}
	
	/**
	 * @description: 领取抽奖的奖励
	 *
	 * @param context
	 * @param reqCallback
	 * @param reqInfo
	 * @param isMain
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年9月12日 下午9:00:23
	 */
	public static DataRequester obtainReward(Context context, ReqCallback<ObtainRewardResp> reqCallback, ObtainRewardReq reqInfo, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.OBTAIN_REWARD, reqCallback, reqInfo, isMain));
	}
	
	/**
	 * @description: 所有游戏的下载次数和星级
	 *
	 * @param context
	 * @param reqCallback
	 * @param reqInfo
	 * @param isMain
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年9月20日 下午11:50:48
	 */
	public static DataRequester getAllGameDownStar(Context context, ReqCallback<DownStarResp> reqCallback, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.DOWN_STAR, reqCallback, isMain));
	}
	
	/**
	 * @description: 获取App配置
	 *
	 * @param context
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年9月22日 下午6:50:18
	 */
	public static AppConfig getAppConfig(){
		return new AppConfig();
	}
	
	/**
	 * @description: 获取服务器的App配置
	 *
	 * @param context
	 * @param reqCallback
	 * @param isMain
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年9月22日 下午8:33:51
	 */
	public static DataRequester getServerAppConfig(Context context, ReqCallback<AppConfigResp> reqCallback, String serverId, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.APP_CONFIG, reqCallback, serverId, isMain));
	}
	
	/**
	 * @description: 获取有新内容的接口
	 *
	 * @param context
	 * @param reqCallback
	 * @param isMain
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年10月8日 下午11:06:32
	 */
	public static void getNewContentInterface(Context context, ReqCallback<List<String>> reqCallback, boolean isMain) {
		DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.NEW_CONTENT_INTERFACE, reqCallback, null, isMain)).request(context);
	}
	
	/**
	 * @description: 移除有新内容的接口
	 *
	 * @param context
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年10月9日 上午12:05:47
	 */
	public static void removeNewContentInterface(Context context, String interfaceName) {
		DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.REMOVE_NEW_CONTENT_INTERFACE, null, interfaceName, false)).request(context);
	}
	
	/**
	 * @description: 是否存在新下载的游戏
	 *
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年10月9日 上午12:17:37
	 */
	public static boolean existNewDownloadGame(){
		if(Configuration.IS_FILL_TEST_NEW_CONTENT_TIP){
			Boolean testValue = true;
			SpHelper.put(BaseApplication.getContext(), SpHelper.KEY_EXIST_NEW_MY_GAME, testValue);
		}
		Boolean defaultValue = false;
		return (Boolean)SpHelper.get(BaseApplication.getContext(), SpHelper.KEY_EXIST_NEW_MY_GAME, defaultValue);
	}
	
	/**
	 * @description: 移除新下载游戏记录
	 *
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年10月9日 上午12:19:40
	 */
	public static void removeNewDownloadGame(){
		Boolean defaultValue = false;
		SpHelper.put(BaseApplication.getContext(), SpHelper.KEY_EXIST_NEW_MY_GAME, defaultValue);
	}
	
	/**
	 * @description: 获取商品的剩余数量
	 *
	 * @param context
	 * @param goodsId
	 * @param reqCallback
	 * @param isMain
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年10月18日 下午2:56:37
	 */
	public static DataRequester getGoodsLfetCount(Context context, String goodsId,
			ReqCallback<GoodsLeftCountInfo> reqCallback, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.FETCH_GOODS_LEFT_COUNT, reqCallback, goodsId, isMain));
	}
	
	/**
	 * @description: 商品剩余数量
	 *
	 * @param context
	 * @param goodInfo
	 * @param reqCallback
	 * @param isMain
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年10月19日 上午1:54:14
	 */
	public static DataRequester getGoodsLfetCount(Context context, GoodsInfo goodInfo,
			ReqCallback<GoodsLeftCountInfo> reqCallback, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.FETCH_GOODS_LEFT_COUNT, reqCallback, goodInfo, isMain));
	}
	
	/**
	 * @description: 同步时间
	 *
	 * @param context
	 * @param reqCallback
	 * @param isMain
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年10月19日 上午1:54:06
	 */
	public static DataRequester syncTime(Context context, ReqCallback<GoodsLeftCountInfo> reqCallback, boolean isMain) {
		return DataHelper.makeDataRequester(context, ReqConfig.makeReqConfig(ReqCode.SYNC_TIME, reqCallback, null, isMain));
	}
}
