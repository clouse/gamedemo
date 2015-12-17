package com.atet.tvmarket.model;

import android.content.Context;
import android.text.TextUtils;

import com.atet.api.utils.EncryptUtils;
import com.atet.tvmarket.entity.ActivityInfoReq;
import com.atet.tvmarket.entity.AdModelInfoReq;
import com.atet.tvmarket.entity.DeviceIdOnlyReq;
import com.atet.tvmarket.entity.DeviceIdWithMinTime;
import com.atet.tvmarket.entity.DeviceIdWithUserIdReq;
import com.atet.tvmarket.entity.DownStarReq;
import com.atet.tvmarket.entity.GameInfoFromDeviceId;
import com.atet.tvmarket.entity.GameInfoFromGameIdReq;
import com.atet.tvmarket.entity.GameInfoFromRankingType;
import com.atet.tvmarket.entity.GameInfosFromGameTopicReq;
import com.atet.tvmarket.entity.GameInfosFromGameTypeReq;
import com.atet.tvmarket.entity.GameSearchPinyinReq;
import com.atet.tvmarket.entity.GoodsExchangeReq;
import com.atet.tvmarket.entity.GoodsIdReq;
import com.atet.tvmarket.entity.GoodsInfoReq;
import com.atet.tvmarket.entity.IntegralReq;
import com.atet.tvmarket.entity.NewVersionInfoReq;
import com.atet.tvmarket.entity.NoticeInfoReq;
import com.atet.tvmarket.entity.ObtainUserGiftReq;
import com.atet.tvmarket.entity.RateGameReq;
import com.atet.tvmarket.entity.ServerIdInfoReq;
import com.atet.tvmarket.entity.ThirdGameInfoFromGameIdReq;
import com.atet.tvmarket.entity.UpdateInterfaceReq;
import com.atet.tvmarket.entity.UserIdReq;
import com.atet.tvmarket.entity.VideoInfoReq;

/**
 * @description: Http Post 请求的数据类
 *
 * @author: LiuQin
 * @date: 2015年7月10日 下午2:54:43 
 */
public class HttpReqDataHelper {

	/**
	 * @description: server id请求数据
	 *
	 * @param context
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年6月24日 下午6:36:59
	 */
	public static String serverIdReqData(Context context) {
		ServerIdInfoReq info = new ServerIdInfoReq();
		info.setDeviceCode(DeviceHelper.getDeviceModel(context));
		info.setProductId(DeviceHelper.getDeviceUniqueId(context));
		info.setType(DeviceHelper.getDeviceType(context));
		info.setChannelId("0");
		return MyJsonPaser.toJson(info);
	}
	
	/**
	 * @description: 游戏分类请求数据
	 *
	 * @param context
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月4日 下午2:15:50
	 */
	public static String gameTypeReqData(Context context) {
		DeviceIdWithUserIdReq info = new DeviceIdWithUserIdReq();
		info.setDeviceId(DataHelper.getDeviceInfo().getServerId());
		info.setUserId("0");
		return MyJsonPaser.toJson(info);
	}
	
	/**
	 * @description: 获取某分类下的游戏请求数据
	 *
	 * @param context
	 * @param gameTypeId
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月5日 下午4:43:00
	 */
	public static String gameInfosFromGameTypeReqData(Context context, String gameTypeId) {
		GameInfosFromGameTypeReq info =new GameInfosFromGameTypeReq();
		info.setDeviceId(DataHelper.getDeviceInfo().getServerId());
		info.setUserId("0");
		info.setTypeId(gameTypeId);
		info.setPageSize(200);
		info.setCurrentPage(1);
		return MyJsonPaser.toJson(info);
	}
	
	public static String gameInfoFromGameId(Context context, String gameId) {
		GameInfoFromGameIdReq info =new GameInfoFromGameIdReq();
		info.setDeviceId(DataHelper.getDeviceInfo().getServerId());
		info.setGameId(gameId);
		return MyJsonPaser.toJson(info);
	}
	
	public static String gameInfosFromDeviceId(Context context) {
		GameInfoFromDeviceId info =new GameInfoFromDeviceId();
		info.setDeviceId(DataHelper.getDeviceInfo().getServerId());
		return MyJsonPaser.toJson(info);
	}
	
	/**
	 * @description: 游戏排行
	 *
	 * @param context
	 * @param type
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午2:54:14
	 */
	public static String gameInfosFromRankingType(Context context, int type) {
		GameInfoFromRankingType info =new GameInfoFromRankingType();
		info.setDeviceId(DataHelper.getDeviceInfo().getServerId());
		info.setType(type);
		return MyJsonPaser.toJson(info);
	}
	
	public static String deviceIdWithMinTime(Context context, long minTime) {
		DeviceIdWithMinTime info =new DeviceIdWithMinTime();
		info.setDeviceId(DataHelper.getDeviceInfo().getServerId());
		info.setMinTime(minTime);
		return MyJsonPaser.toJson(info);
	}
	
	/**
	 * @description: 专题下的游戏
	 *
	 * @param context
	 * @param topicId
	 * @param type
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午2:53:51
	 */
	public static String gameInfosFromGameTopic(Context context, String topicId, int type) {
		GameInfosFromGameTopicReq info =new GameInfosFromGameTopicReq();
		info.setTopicId(topicId);
		info.setType(type);
		return MyJsonPaser.toJson(info);
	}
	
	/**
	 * @description: 游戏搜索拼音
	 *
	 * @param context
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月7日 下午1:44:30
	 */
	public static String gameSearchPinyin(Context context) {
		GameSearchPinyinReq info =new GameSearchPinyinReq();
		info.setDeviceId(DataHelper.getDeviceInfo().getServerId());
		info.setType(1);
		return MyJsonPaser.toJson(info);
	}
	
	/**
	 * @description: 广告
	 *
	 * @param context
	 * @param modelId
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月7日 下午10:17:47
	 */
	public static String ads(Context context, String modelId, Long minTime) {
		AdModelInfoReq info =new AdModelInfoReq();
		info.setDeviceId(DataHelper.getDeviceInfo().getServerId());
		if(minTime!=null){
			info.setMinTime(minTime);
		}
		info.setModelId(modelId);
		return MyJsonPaser.toJson(info);
	}
	
	/**
	 * @description: 活动
	 *
	 * @param context
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月9日 下午9:14:12
	 */
	public static String activity(Context context, int type) {
		ActivityInfoReq info =new ActivityInfoReq();
		info.setDeviceId(DataHelper.getDeviceInfo().getServerId());
		info.setType(type);
		info.setMinTime(0);
		return MyJsonPaser.toJson(info);
	}
	
	/**
	 * @description: 商品
	 *
	 * @param context
	 * @param type
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月9日 下午9:52:00
	 */
	public static String goods(Context context, int type) {
		GoodsInfoReq info = new GoodsInfoReq();
		info.setDeviceId(DataHelper.getDeviceInfo().getServerId());
		info.setType(type);
		info.setMinTime(0);
		return MyJsonPaser.toJson(info);
	}
	
	/**
	 * @description: 礼包
	 *
	 * @param context
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月9日 下午10:19:16
	 */
	public static String gift(Context context) {
		DeviceIdWithMinTime info = new DeviceIdWithMinTime();
		info.setDeviceId(DataHelper.getDeviceInfo().getServerId());
		info.setMinTime(0);
		return MyJsonPaser.toJson(info);
	}
	
	/**
	 * @description: 用户已经领取的礼包
	 *
	 * @param context
	 * @param userId
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月10日 上午10:42:40
	 */
	public static String userGift(Context context, String userId) {
		DeviceIdWithUserIdReq info = new DeviceIdWithUserIdReq();
		info.setDeviceId(DataHelper.getDeviceInfo().getServerId());
		info.setUserId(userId);
		return MyJsonPaser.toJson(info);
	}
	
	/**
	 * @description: 领取礼包
	 *
	 * @param context
	 * @param userId
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月10日 上午10:43:53
	 */
	public static String obtainUserGift(Context context, String userId, String giftId) {
		ObtainUserGiftReq info = new ObtainUserGiftReq();
		info.setDeviceId(DataHelper.getDeviceInfo().getServerId());
		info.setUserId(userId);
		info.setGiftPackageid(giftId);
		return MyJsonPaser.toJson(info);
	}
	
	/**
	 * @description: 公告
	 *
	 * @param context
	 * @param noticeType
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月10日 上午11:37:57
	 */
	public static String notice(Context context, int noticeType) {
		NoticeInfoReq info = new NoticeInfoReq();
		info.setDeviceId(DataHelper.getDeviceInfo().getServerId());
		info.setNoticeType(noticeType);
		info.setMinTime(0);
		return MyJsonPaser.toJson(info);
	}
	
	/**
	 * @description: 视频
	 *
	 * @param context
	 * @param videoType
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午12:51:34
	 */
	public static String video(Context context, String videoType) {
		VideoInfoReq info = new VideoInfoReq();
		info.setDeviceId(DataHelper.getDeviceInfo().getServerId());
		info.setVideoType(videoType);
		info.setCurrentPage("1");
		info.setPageSize("100");
		return MyJsonPaser.toJson(info);
	}
	
	/**
	 * @description: 游戏评分
	 *
	 * @param context
	 * @param videoType
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午2:12:25
	 */
	public static String rateGame(Context context, String gameId, String userId, int score) {
		RateGameReq info = new RateGameReq();
		info.setDeviceId(DataHelper.getDeviceInfo().getServerId());
		info.setGameId(gameId);
		info.setUserId(userId);
		info.setScore(score);
		return MyJsonPaser.toJson(info);
	}
	
	/**
	 * @description: 根据gameId获取第三方游戏
	 *
	 * @param context
	 * @param gameId
	 * @return 
	 * @throws: 
	 * @author: LiuQin
	 * @date: 2015年7月12日 下午10:23:14
	 */
	public static String thirdGameInfoFromGameId(Context context, String gameId) {
		ThirdGameInfoFromGameIdReq info =new ThirdGameInfoFromGameIdReq();
		info.setDeviceId(DataHelper.getDeviceInfo().getServerId());
		info.setIds(gameId);
		return MyJsonPaser.toJson(info);
	}
	
	/**
	 * @description: 相关的游戏
	 *
	 * @param context
	 * @param gameId
	 * @param userId
	 * @param score
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月13日 下午10:02:51
	 */
	public static String relativeGame(Context context, String gameId) {
		RateGameReq info = new RateGameReq();
		info.setDeviceId(DataHelper.getDeviceInfo().getServerId());
		info.setGameId(gameId);
		return MyJsonPaser.toJson(info);
	}
	
	/**
	 * @description: 任务
	 *
	 * @param context
	 * @param userId
	 * @param taskFlag
	 * @param gameId
	 * @param taskId
	 * @param ruleId
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月18日 下午2:55:56
	 */
	public static String userTask(Context context, int userId, int taskFlag, 
			String gameId, String taskId, String ruleId) {
		IntegralReq info = new IntegralReq();
		info.setDeviceId(DataHelper.getDeviceInfo().getServerId());
		info.setUserId(userId);
		info.setFlag(taskFlag);
		info.setGameId(gameId);
		info.setTaskId(taskId);
		info.setRuleId(ruleId);
		
		return MyJsonPaser.getVerifyPostData(info);
	}
	
	/**
	 * @description: 查询新版本
	 *
	 * @param context
	 * @param gameId
	 * @return 
	 * @throws: 
	 * @author: LiuQin
	 * @date: 2015年7月28日 下午3:38:41
	 */
	public static String newVersionInfo(Context context, String appId) {
		NewVersionInfoReq info = new NewVersionInfoReq();
		info.setDeviceId(DataHelper.getDeviceInfo().getServerId());
		info.setAppId(appId);
		return MyJsonPaser.toJson(info);
	}
	
	/**
	 * @description: 用户的商品订单
	 *
	 * @param context
	 * @param userId
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月29日 下午7:05:53
	 */
	public static String userGoodsOrder(Context context, String userId) {
		UserIdReq info = new UserIdReq();
		info.setUserId(userId);
		return MyJsonPaser.toJson(info);
	}
	
	/**
	 * @description: 商品兑换
	 *
	 * @param context
	 * @param userId
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月30日 上午11:15:35
	 */
	public static String goodsExchange(Context context, GoodsExchangeReq info) {
		String data= "{userId:"+info.getUserId()+",goodsId:\""+info.getGoodsId()+"\",counts:"+info.getCount()+"}";
		String sign = DataHelper.signPostData(data);
		String postData = "data="+data+"&sign="+sign+"";
		return postData;
	}
	
	/**
	 * @description: 数据更新接口
	 *
	 * @param context
	 * @param gameId
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月6日 下午4:29:11
	 */
	public static String updateInterface(Context context, long lastTime) {
		UpdateInterfaceReq info = new UpdateInterfaceReq();
		info.setDeviceId(DataHelper.getDeviceInfo().getServerId());
		info.setLastTime(lastTime);
		return MyJsonPaser.toJson(info);
	}
	
	/**
	 * @description: 获取所有新任务
	 *
	 * @param context
	 * @param userId
	 * @param taskFlag
	 * @param gameId
	 * @param taskId
	 * @param ruleId
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月19日 下午3:05:13
	 */
	public static String getAllUserTask(Context context, int userId, int taskFlag, 
			String gameId, String taskId, String ruleId) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("{deviceId:\""+DataHelper.getDeviceInfo().getServerId()+"\",flag:"+taskFlag+",userId:"+userId);
		if(!TextUtils.isEmpty(taskId)){
			sb.append(",taskId:\""+taskId+"\"");
		}
		if(!TextUtils.isEmpty(ruleId)){
			sb.append(",ruleId:\""+ruleId+"\"");
		}
		if(!TextUtils.isEmpty(gameId)){
			sb.append(",gameId:\""+gameId+"\"");
		}
		sb.append("}");
		String data=sb.toString();
		String sign = DataHelper.signPostData(data);
		String postData = "data="+data+"&sign="+sign+"";
		return postData;
	}
	
	/**
	 * @description: 抽奖信息
	 *
	 * @param context
	 * @param userId
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年9月12日 下午8:10:01
	 */
	public static String getRewardInfo(Context context, int userId) {
		String data="{userId:"+userId+"}";
		String sign = DataHelper.signPostData(data);
		String postData = "data="+data+"&sign="+sign+"";
		return postData;
	}
	
	/**
	 * @description: 领取抽奖的奖励
	 *
	 * @param context
	 * @param userId
	 * @param rewardId
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年9月12日 下午8:49:13
	 */
	public static String ObtainReward(Context context, int userId, String rewardId) {
		StringBuilder sb = new StringBuilder();
		sb.append("{userId:"+userId);
		if(!TextUtils.isEmpty(rewardId)){
			sb.append(",rewardId:\""+rewardId+"\"");
		}
		sb.append("}");
		String data=sb.toString();
		String sign = DataHelper.signPostData(data);
		String postData = "data="+data+"&sign="+sign+"";
		return postData;
	}
	
	/**
	 * @description: 所有游戏的下载次数和星级
	 *
	 * @param context
	 * @param lastTime
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年9月20日 下午11:38:29
	 */
	public static String downStar(Context context) {
		DownStarReq info = new DownStarReq();
		info.setDeviceId(DataHelper.getDeviceInfo().getServerId());
		info.setCurrentPage(1);
		info.setPageSize(5000);
		return MyJsonPaser.toJson(info);
	}
	
	public static String deviceIdOnly(Context context, String serverId){
		DeviceIdOnlyReq info = new DeviceIdOnlyReq();
		info.setDeviceId(serverId);
//		info.setDeviceId("20140910175136992102");
		return MyJsonPaser.toJson(info);
	}
	
	public static String goodsId(Context context, String goodsId){
		GoodsIdReq info = new GoodsIdReq();
		info.setGoodsId(goodsId);
		return MyJsonPaser.toJson(info);
	}
}
