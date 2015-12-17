package com.atet.tvmarket.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.entity.dao.ActDetailPhoto;
import com.atet.tvmarket.entity.dao.ActDetailPhotoDao;
import com.atet.tvmarket.entity.dao.ActInfo;
import com.atet.tvmarket.entity.dao.ActInfoDao;
import com.atet.tvmarket.entity.dao.ActionUrl;
import com.atet.tvmarket.entity.dao.ActionUrlDao;
import com.atet.tvmarket.entity.dao.AdInfo;
import com.atet.tvmarket.entity.dao.AdInfoDao;
import com.atet.tvmarket.entity.dao.AdModelInfo;
import com.atet.tvmarket.entity.dao.AdModelInfoDao;
import com.atet.tvmarket.entity.dao.DaoMaster;
import com.atet.tvmarket.entity.dao.DaoMaster.DevOpenHelper;
import com.atet.tvmarket.entity.dao.DaoSession;
import com.atet.tvmarket.entity.dao.Extramap;
import com.atet.tvmarket.entity.dao.ExtramapDao;
import com.atet.tvmarket.entity.dao.GameGiftInfo;
import com.atet.tvmarket.entity.dao.GameGiftInfoDao;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.entity.dao.GameInfoDao;
import com.atet.tvmarket.entity.dao.GameRanking;
import com.atet.tvmarket.entity.dao.GameRankingDao;
import com.atet.tvmarket.entity.dao.GameScoreRecordInfo;
import com.atet.tvmarket.entity.dao.GameScoreRecordInfoDao;
import com.atet.tvmarket.entity.dao.GameSearchPinyinInfo;
import com.atet.tvmarket.entity.dao.GameSearchPinyinInfoDao;
import com.atet.tvmarket.entity.dao.GameTopicInfo;
import com.atet.tvmarket.entity.dao.GameTopicInfoDao;
import com.atet.tvmarket.entity.dao.GameTypeInfo;
import com.atet.tvmarket.entity.dao.GameTypeInfoDao;
import com.atet.tvmarket.entity.dao.GiftInfo;
import com.atet.tvmarket.entity.dao.GiftInfoDao;
import com.atet.tvmarket.entity.dao.GoodsDetailPhoto;
import com.atet.tvmarket.entity.dao.GoodsDetailPhotoDao;
import com.atet.tvmarket.entity.dao.GoodsInfo;
import com.atet.tvmarket.entity.dao.GoodsInfoDao;
import com.atet.tvmarket.entity.dao.LocalSubTypeId;
import com.atet.tvmarket.entity.dao.LocalSubTypeIdDao;
import com.atet.tvmarket.entity.dao.LocalUpdateInfo;
import com.atet.tvmarket.entity.dao.LocalUpdateInfoDao;
import com.atet.tvmarket.entity.dao.ModelToAd;
import com.atet.tvmarket.entity.dao.ModelToAdDao;
import com.atet.tvmarket.entity.dao.NewUploadToGame;
import com.atet.tvmarket.entity.dao.NewUploadToGameDao;
import com.atet.tvmarket.entity.dao.NoticeInfo;
import com.atet.tvmarket.entity.dao.NoticeInfoDao;
import com.atet.tvmarket.entity.dao.ScreenShotInfo;
import com.atet.tvmarket.entity.dao.ScreenShotInfoDao;
import com.atet.tvmarket.entity.dao.SubTypeId;
import com.atet.tvmarket.entity.dao.SubTypeIdDao;
import com.atet.tvmarket.entity.dao.ThirdGameDownInfo;
import com.atet.tvmarket.entity.dao.ThirdGameDownInfoDao;
import com.atet.tvmarket.entity.dao.ThirdGameInfo;
import com.atet.tvmarket.entity.dao.ThirdGameInfoDao;
import com.atet.tvmarket.entity.dao.TopicToGame;
import com.atet.tvmarket.entity.dao.TopicToGameDao;
import com.atet.tvmarket.entity.dao.TypeToGame;
import com.atet.tvmarket.entity.dao.TypeToGameDao;
import com.atet.tvmarket.entity.dao.UpdateInterfaceInfo;
import com.atet.tvmarket.entity.dao.UpdateInterfaceInfoDao;
import com.atet.tvmarket.entity.dao.UserGameGiftInfo;
import com.atet.tvmarket.entity.dao.UserGameGiftInfoDao;
import com.atet.tvmarket.entity.dao.UserGameToGift;
import com.atet.tvmarket.entity.dao.UserGameToGiftDao;
import com.atet.tvmarket.entity.dao.UserGiftInfo;
import com.atet.tvmarket.entity.dao.UserGiftInfoDao;
import com.atet.tvmarket.entity.dao.VideoInfo;
import com.atet.tvmarket.entity.dao.VideoInfoDao;

import de.greenrobot.dao.query.CountQuery;
import de.greenrobot.dao.query.Query;

/**
 * @description: 数据库操作类
 *
 * @author: LiuQin
 * @date: 2015年7月10日 下午7:33:55 
 */
public class DaoHelper {
	private static DaoSession daoSession;
	private static ALog alog = ALog.getLogger(DaoHelper.class);

	public static DaoSession getInstanse(Context context) {
		if (daoSession == null) {
			synchronized (DaoHelper.class) {
				if (daoSession == null) {
					DevOpenHelper helper = new DaoMaster.DevOpenHelper(context,
							"market", null);
					DaoMaster daoMaster = new DaoMaster(
							helper.getWritableDatabase());
					daoSession = daoMaster.newSession();
				}
			}
		}
		return daoSession;
	}

	/**
	 * @description: 保存游戏分类数据
	 *
	 * @param context
	 * @param infos
	 * @throws:
	 * @author: LiuQin  
	 * @date: 2015年7月4日 下午6:11:16 
	 */
	public static boolean saveGameType(Context context, List<GameTypeInfo> infos) {
		boolean result = true;
		GameTypeInfoDao dao = DaoHelper.getInstanse(context)
				.getGameTypeInfoDao();
		dao.deleteAll();
		dao.insertInTx(infos);
		return result;
	}

	/**
	 * @description: 获取游戏分类数据
	 *
	 * @param context
	 * @return
	 * @author: LiuQin
	 * @date: 2015年7月4日 下午7:12:36
	 */
	public static List<GameTypeInfo> getGameType(Context context) {
		GameTypeInfoDao dao = DaoHelper.getInstanse(context)
				.getGameTypeInfoDao();
//		Query<GameTypeInfo> query = dao.queryBuilder()
//				.orderDesc(GameTypeInfoDao.Properties.OrderNum).build();
		Query<GameTypeInfo> query=dao.queryBuilder().build();
		return query.list();
	}

	/**
	 * @description: 保存游戏信息
	 *
	 * @param context
	 * @param infos
	 * @return
	 * @author: LiuQin
	 * @date: 2015年7月5日 上午11:54:27
	 */
	public static boolean saveGameInfo(Context context, List<GameInfo> infos) {
		boolean result = true;
		GameInfoDao dao = DaoHelper.getInstanse(context).getGameInfoDao();
		dao.insertOrReplaceInTx(infos);
		return result;
	}

	/**
	 * @description: 保存游戏截图
	 *
	 * @param context
	 * @param infos
	 * @return
	 * @author: LiuQin
	 * @date: 2015年7月5日 下午2:05:07
	 */
	public static boolean saveGameScreenShotInfo(Context context,
			List<ScreenShotInfo> infos) {
		boolean result = true;
		ScreenShotInfoDao dao = DaoHelper.getInstanse(context)
				.getScreenShotInfoDao();
		dao.insertOrReplaceInTx(infos);
		return result;
	}
	
	
	/**
	 * @description: 删除指定游戏的截图
	 *
	 * @param context
	 * @param gameId
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月5日 下午4:36:06
	 */
	public static boolean delGameScreenShotInfo(Context context, String gameId) {
		boolean result = true;
		ScreenShotInfoDao dao = DaoHelper.getInstanse(context)
				.getScreenShotInfoDao();
		dao.queryBuilder()
				.where(ScreenShotInfoDao.Properties.GameId.eq(gameId)).buildDelete()
				.executeDeleteWithoutDetachingEntities();
		return result;
	}

	/**
	 * @description: 保存某个分类的游戏关联
	 *
	 * @param context
	 * @param infos
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月5日 下午3:23:00
	 */
	public static boolean saveTypeToGame(Context context, List<TypeToGame> infos) {
		boolean result = true;
		TypeToGameDao dao = DaoHelper.getInstanse(context).getTypeToGameDao();
		dao.queryBuilder()
				.where(TypeToGameDao.Properties.TypeId.eq(infos.get(0) .getTypeId())).buildDelete()
				.executeDeleteWithoutDetachingEntities();
		dao.insertOrReplaceInTx(infos);
		return result;
	}

	/**
	 * @description: 获取某个分类下的游戏
	 *
	 * @param context
	 * @param typeId
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月5日 下午3:23:23
	 */
	public static List<TypeToGame> getTypeToGame(Context context, String typeId) {
		TypeToGameDao dao = DaoHelper.getInstanse(context).getTypeToGameDao();
//		Query<TypeToGame> qc = dao
//				.queryRawCreate(" JOIN GAME_INFO G ON T.GAME_ID=G.GAME_ID JOIN GAME_TYPE_INFO GT ON GT.TYPE_ID=T.TYPE_ID WHERE T.TYPE_ID=? ORDER BY T.RETURN_ORDER ASC", typeId);
		
    	Query<TypeToGame> qc = dao.queryRawCreate(
	      " JOIN "+GameInfoDao.TABLENAME+" G"+
		      " ON T."+TypeToGameDao.Properties.GameId.columnName+"=G."+GameInfoDao.Properties.GameId.columnName+
	      " JOIN "+GameTypeInfoDao.TABLENAME+" GT"+
		      " ON T."+TypeToGameDao.Properties.TypeId.columnName+"=GT."+GameTypeInfoDao.Properties.TypeId.columnName+
	      " WHERE T."+TypeToGameDao.Properties.TypeId.columnName+"=?"+
		  " ORDER BY T."+TypeToGameDao.Properties.ReturnOrder.columnName+" ASC"    
	      , typeId);

		return qc.list();
	}
	
	public static List<GameInfo> getGameInfoFromGameId(Context context, String gameId) {
		GameInfoDao dao = DaoHelper.getInstanse(context).getGameInfoDao();
		Query<GameInfo> qc=dao.queryBuilder().where(GameInfoDao.Properties.GameId.eq(gameId)).build();
		return qc.list();
	}
	
	public static List<GameInfo> getGameInfoFromPackageName(Context context, String packageName) {
		GameInfoDao dao = DaoHelper.getInstanse(context).getGameInfoDao();
		Query<GameInfo> qc=dao.queryBuilder().where(GameInfoDao.Properties.PackageName.eq(packageName)).build();
		return qc.list();
	}
	
	/**
	 * @description: 保存新游推荐游戏
	 *
	 * @param context
	 * @param infos
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月5日 下午5:55:38
	 */
	public static boolean saveNewUploadToGame(Context context, List<NewUploadToGame> infos) {
		boolean result = true;
		NewUploadToGameDao dao = DaoHelper.getInstanse(context).getNewUploadToGameDao();
		dao.deleteAll();
		dao.insertOrReplaceInTx(infos);
		return result;
	}
	
	/**
	 * @description: 获取新游推荐游戏
	 *
	 * @param context
	 * @param typeId
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月5日 下午6:08:51
	 */
	public static List<GameInfo> getNewUploadToGame(Context context) {
		GameInfoDao dao = DaoHelper.getInstanse(context).getGameInfoDao();
//		Query<GameInfo> qc = dao.queryRawCreate(" JOIN NEW_UPLOAD_TO_GAME N ON T.GAME_ID=N.GAME_ID ORDER BY N.RETURN_ORDER ASC");
    	Query<GameInfo> qc = dao.queryRawCreate(
	      " JOIN "+NewUploadToGameDao.TABLENAME+" N"+
		      " ON T."+GameInfoDao.Properties.GameId.columnName+"=N."+NewUploadToGameDao.Properties.GameId.columnName+
	      " ORDER BY N."+NewUploadToGameDao.Properties.ReturnOrder.columnName+" ASC");
		return qc.list();
	}
	
	/**
	 * @description: 保存游戏排行
	 *
	 * @param context
	 * @param infos
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月5日 下午7:45:47
	 */
	public static boolean saveGameRanking(Context context, List<GameRanking> infos) {
		boolean result = true;
		GameRankingDao dao = DaoHelper.getInstanse(context).getGameRankingDao();
		dao.queryBuilder()
				.where(GameRankingDao.Properties.Type.eq(infos.get(0) .getType())).buildDelete()
				.executeDeleteWithoutDetachingEntities();
		dao.insertOrReplaceInTx(infos);
		return result;
	}
	
	public static List<GameInfo> getGameRanking(Context context, int type, int limit) {
		GameInfoDao dao = DaoHelper.getInstanse(context).getGameInfoDao();
//		Query<GameInfo> qc = dao.queryRawCreate(" JOIN GAME_RANKING N ON T.GAME_ID=N.GAME_ID WHERE N.TYPE=? ORDER BY N.RETURN_ORDER ASC",type);
    	Query<GameInfo> qc = dao.queryRawCreate(
	      " JOIN "+GameRankingDao.TABLENAME+" GR"+
		      " ON T."+GameInfoDao.Properties.GameId.columnName+"=GR."+GameRankingDao.Properties.GameId.columnName+
	      " WHERE GR."+GameRankingDao.Properties.Type.columnName+"=?"+
	      " ORDER BY GR."+GameRankingDao.Properties.ReturnOrder.columnName+" ASC LIMIT ?"
	      ,type,limit);
		return qc.list();
	}
	
	/**
	 * @description: 保存游戏专题
	 *
	 * @param context
	 * @param infos
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月6日 下午2:12:41
	 */
	public static boolean saveGameTopic(Context context, List<GameTopicInfo> infos) {
		boolean result = true;
		GameTopicInfoDao dao = DaoHelper.getInstanse(context)
				.getGameTopicInfoDao();
		dao.deleteAll();
		dao.insertInTx(infos);
		return result;
	}

	/**
	 * @description: 获取游戏专题
	 *
	 * @param context
	 * @return
	 * @author: LiuQin
	 * @date: 2015年7月4日 下午7:12:36
	 */
	public static List<GameTopicInfo> getGameTopic(Context context) {
		GameTopicInfoDao dao = DaoHelper.getInstanse(context)
				.getGameTopicInfoDao();
		Query<GameTopicInfo> query=dao.queryBuilder().build();
		return query.list();
	}
	
	/**
	 * @description: 保存某专题下的游戏
	 *
	 * @param context
	 * @param infos
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月6日 下午5:18:58
	 */
	public static boolean saveTopicToGame(Context context, List<TopicToGame> infos) {
		boolean result = true;
		TopicToGameDao dao = DaoHelper.getInstanse(context).getTopicToGameDao();
		dao.queryBuilder()
				.where(TopicToGameDao.Properties.TopicId.eq(infos.get(0).getTopicId())).buildDelete()
				.executeDeleteWithoutDetachingEntities();
		dao.insertOrReplaceInTx(infos);
		return result;
	}

	/**
	 * @description: 获取某个专题下的游戏(该专题全部为版本游戏)
	 *
	 * @param context
	 * @param topicId
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月5日 下午3:23:23
	 */
	public static List<TopicToGame> getTopicToGame(Context context, String topicId) {
		TopicToGameDao dao = DaoHelper.getInstanse(context).getTopicToGameDao();
		
    	Query<TopicToGame> qc = dao.queryRawCreate(
	      " JOIN "+GameInfoDao.TABLENAME+" G"+
		      " ON T."+TopicToGameDao.Properties.GameId.columnName+"=G."+GameInfoDao.Properties.GameId.columnName+
	      " JOIN "+GameTopicInfoDao.TABLENAME+" GT"+
		      " ON T."+TopicToGameDao.Properties.TopicId.columnName+"=GT."+GameTopicInfoDao.Properties.TopicId.columnName+
	      " WHERE T."+TopicToGameDao.Properties.TopicId.columnName+"=?"+
		      " AND T."+TopicToGameDao.Properties.Type.columnName+"=?"+
		  " ORDER BY T."+TopicToGameDao.Properties.ReturnOrder.columnName+" ASC"    
	      , topicId,1);

		return qc.list();
	}
	
	/**
	 * @description: 获取某个专题下的游戏(该专题全部为第三方游戏)
	 *
	 * @param context
	 * @param topicId
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月6日 下午6:47:20
	 */
	public static List<TopicToGame> getTopicToGameThird(Context context, String topicId) {
		TopicToGameDao dao = DaoHelper.getInstanse(context).getTopicToGameDao();
		
    	Query<TopicToGame> qc = dao.queryRawCreate(
	      " JOIN "+ThirdGameInfoDao.TABLENAME+" G"+
		      " ON T."+TopicToGameDao.Properties.GameId.columnName+"=G."+ThirdGameInfoDao.Properties.GameId.columnName+
	      " JOIN "+GameTopicInfoDao.TABLENAME+" GT"+
		      " ON T."+TopicToGameDao.Properties.TopicId.columnName+"=GT."+GameTopicInfoDao.Properties.TopicId.columnName+
	      " WHERE T."+TopicToGameDao.Properties.TopicId.columnName+"=?"+
		      " AND T."+TopicToGameDao.Properties.Type.columnName+"=?"+
		  " ORDER BY T."+TopicToGameDao.Properties.ReturnOrder.columnName+" ASC"    
	      , topicId,0);

		return qc.list();
	}
	
	/**
	 * @description: 保存第三方游戏
	 *
	 * @param context
	 * @param infos
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月6日 下午9:00:04
	 */
	public static boolean saveThirdGameInfo(Context context, List<ThirdGameInfo> infos) {
		boolean result = true;
		ThirdGameInfoDao dao = DaoHelper.getInstanse(context).getThirdGameInfoDao();
		dao.insertOrReplaceInTx(infos);
		return result;
	}
	
	
	/**
	 * @description: 保存第三方游戏
	 *
	 * @param context
	 * @param info
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月12日 下午10:18:21
	 */
	public static boolean saveThirdGameInfo(Context context, ThirdGameInfo info) {
		boolean result = true;
		ThirdGameInfoDao dao = DaoHelper.getInstanse(context).getThirdGameInfoDao();
		dao.insertOrReplaceInTx(info);
		return result;
	}
	
	/**
	 * @description: 根据gameId获取第三方游戏
	 *
	 * @param context
	 * @param gameId
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月12日 下午10:37:41
	 */
	public static List<ThirdGameInfo> getThirdGameInfoFromGameId(Context context, String gameId) {
		ThirdGameInfoDao dao = DaoHelper.getInstanse(context).getThirdGameInfoDao();
		Query<ThirdGameInfo> qc=dao.queryBuilder().where(ThirdGameInfoDao.Properties.GameId.eq(gameId)).build();
		return qc.list();
	}
	
	public static List<ThirdGameInfo> getThirdGameInfoFromGamePackageName(Context context, String packageName) {
		ThirdGameInfoDao dao = DaoHelper.getInstanse(context).getThirdGameInfoDao();
		Query<ThirdGameInfo> qc=dao.queryBuilder().where(ThirdGameInfoDao.Properties.PackageName.eq(packageName)).build();
		return qc.list();
	}
	
	/**
	 * @description: 保存第三方游戏的下载地址
	 *
	 * @param context
	 * @param infos
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月6日 下午9:00:46
	 */
	public static boolean saveThirdGameDownInfo(Context context,
			List<ThirdGameDownInfo> infos) {
		boolean result = true;
		ThirdGameDownInfoDao dao = DaoHelper.getInstanse(context)
				.getThirdGameDownInfoDao();
		dao.insertOrReplaceInTx(infos);
		return result;
	}
	
	/**
	 * @description: 删除指定第三方游戏的下载地址
	 *
	 * @param context
	 * @param gameId
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月6日 下午9:02:43
	 */
	public static boolean delThirdGameDownInfo(Context context, String gameId) {
		boolean result = true;
		ThirdGameDownInfoDao dao = DaoHelper.getInstanse(context)
				.getThirdGameDownInfoDao();
		dao.queryBuilder()
				.where(ThirdGameDownInfoDao.Properties.GameId.eq(gameId)).buildDelete()
				.executeDeleteWithoutDetachingEntities();
		return result;
	}
	
	/**
	 * @description: 保存游戏搜索信息
	 *
	 * @param context
	 * @param infos
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月7日 下午1:34:11
	 */
	public static boolean saveGameSearchInfo(Context context, List<GameSearchPinyinInfo> infos) {
		boolean result = true;
		GameSearchPinyinInfoDao dao = DaoHelper.getInstanse(context)
				.getGameSearchPinyinInfoDao();
		dao.deleteAll();
		dao.insertInTx(infos);
		return result;
	}
	
	/**
	 * @description: 获取所有的游戏搜索信息
	 *
	 * @param context
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月7日 下午1:36:48
	 */
	public static List<GameSearchPinyinInfo> getAllGameSearchInfo(Context context, int limit) {
		GameSearchPinyinInfoDao dao = DaoHelper.getInstanse(context)
				.getGameSearchPinyinInfoDao();
		Query<GameSearchPinyinInfo> query=dao.queryBuilder().limit(limit).build();
		return query.list();
	}
	
	/**
	 * @description: 搜索包含特定拼音的游戏
	 *
	 * @param context
	 * @param gameId
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月7日 下午1:39:22
	 */
	public static List<GameSearchPinyinInfo> getGameSearchInfoByPinyin(Context context, String pinyin) {
		GameSearchPinyinInfoDao dao = DaoHelper.getInstanse(context)
				.getGameSearchPinyinInfoDao();
		Query<GameSearchPinyinInfo> qc=dao.queryBuilder().where(GameSearchPinyinInfoDao.Properties.Pinyin.like(pinyin+"%")).build();
		return qc.list();
	}
	
	/**
	 * @description: 获取拼音搜索数据库的游戏数量
	 *
	 * @param context
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月7日 下午2:42:47
	 */
	public static long getAllGameSearchInfoCount(Context context) {
		GameSearchPinyinInfoDao dao = DaoHelper.getInstanse(context)
				.getGameSearchPinyinInfoDao();
		CountQuery<GameSearchPinyinInfo> query=dao.queryBuilder().buildCount();
		return query.count();
	}
	
	
	/**
	 * @description: 保存AdModel
	 *
	 * @param context
	 * @param info
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月8日 下午9:20:58
	 */
	public static boolean saveAdModelInfo(Context context, AdModelInfo info) {
		alog.debug("");
		boolean result = true;
		AdModelInfoDao dao = DaoHelper.getInstanse(context).getAdModelInfoDao();
		dao.insertOrReplaceInTx(info);
		return result;
	}
	
	/**
	 * @description: 保存AdModel
	 *
	 * @param context
	 * @param info
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月8日 下午9:20:58
	 */
	public static boolean saveAdModelInfo(Context context, List<AdModelInfo> infos) {
		alog.debug(" data size:"+infos.size());
		boolean result = true;
		AdModelInfoDao dao = DaoHelper.getInstanse(context).getAdModelInfoDao();
		dao.insertOrReplaceInTx(infos);
		return result;
	}
	
	/**
	 * @description: 保存广告
	 *
	 * @param context
	 * @param infos
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月7日 下午9:21:22
	 */
	public static boolean saveAdsInfo(Context context, List<AdInfo> infos) {
		alog.debug(" data size:"+infos.size());
		boolean result = true;
		AdInfoDao dao = DaoHelper.getInstanse(context).getAdInfoDao();
		dao.insertOrReplaceInTx(infos);
		return result;
	}
	
	/**
	 * @description: 保存广告的actionUrl
	 *
	 * @param context
	 * @param infos
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月7日 下午10:04:34
	 */
	public static boolean saveAdsActionUrl(Context context,
			List<ActionUrl> infos) {
		alog.debug(" data size:"+infos.size());
		boolean result = true;
		ActionUrlDao dao = DaoHelper.getInstanse(context)
				.getActionUrlDao();
		dao.insertOrReplaceInTx(infos);
		return result;
	}
	
	/**
	 * @description: 保存extramap
	 *
	 * @param context
	 * @param infos
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月7日 下午10:09:54
	 */
	public static boolean saveExtramap(Context context,
			List<Extramap> infos) {
		alog.debug(" data size:"+infos.size());
		boolean result = true;
		ExtramapDao dao = DaoHelper.getInstanse(context)
				.getExtramapDao();
		dao.insertOrReplaceInTx(infos);
		return result;
	}
	
	/**
	 * @description: 删除指定的extramap
	 *
	 * @param context
	 * @param actionUrlId
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月7日 下午10:10:05
	 */
	public static boolean delExtramaWithAdId(Context context, String actionUrlId) {
		boolean result = true;
		ExtramapDao dao = DaoHelper.getInstanse(context)
				.getExtramapDao();
		dao.queryBuilder()
				.where(ExtramapDao.Properties.ActionUrlId.eq(actionUrlId)).buildDelete()
				.executeDeleteWithoutDetachingEntities();
		return result;
	}
	
	public static List<AdInfo> getAdsInfos(Context context) {
		AdInfoDao dao = DaoHelper.getInstanse(context).getAdInfoDao();
		Query<AdInfo> qc=dao.queryBuilder().build();
		return qc.list();
	}
	
	/**
	 * @description: 获取AdModelInfo
	 *
	 * @param context
	 * @param modelId
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月8日 下午9:51:14
	 */
	public static List<AdModelInfo> getAdModelInfo(Context context, String modelId) {
		AdModelInfoDao dao = DaoHelper.getInstanse(context).getAdModelInfoDao();
		Query<AdModelInfo> qc=dao.queryBuilder()
				.where(AdModelInfoDao.Properties.ModelId.eq(modelId)).build();
		return qc.list();
	}
	
	
	/**
	 * @description: 保存活动
	 *
	 * @param context
	 * @param infos
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月9日 下午9:01:37
	 */
	public static boolean saveActInfo(Context context, List<ActInfo> infos) {
		boolean result = true;
		ActInfoDao dao = DaoHelper.getInstanse(context)
				.getActInfoDao();
		dao.deleteAll();
		dao.insertInTx(infos);
		return result;
	}
	
	/**
	 * @description: 保存活动详情图片
	 *
	 * @param context
	 * @param infos
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月5日 下午2:59:45
	 */
	public static boolean saveActivityDetailPictures(Context context, List<ActDetailPhoto> infos){
		boolean result = true;
		ActDetailPhotoDao dao = DaoHelper.getInstanse(context).getActDetailPhotoDao();
		dao.deleteAll();
		dao.insertInTx(infos);
		return result;
	}
	
	/**
	 * @description: 获取所有活动
	 *
	 * @param context
	 * @param recommend
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月15日 下午2:57:45
	 */
	public static List<ActInfo> getAllActivityInfo(Context context) {
		ActInfoDao dao = DaoHelper.getInstanse(context)
				.getActInfoDao();
		Query<ActInfo> query=dao.queryBuilder()
				.build();
		return query.list();
	}
	
	/**
	 * @description: 获取所有活动的数量
	 *
	 * @param context
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年10月22日 上午4:34:42
	 */
	public static long getAllActivityInfoCount(Context context) {
		ActInfoDao dao = DaoHelper.getInstanse(context)
				.getActInfoDao();
		long result = dao.queryBuilder()
				.buildCount().count();
		return result;
	}
	
	/**
	 * @description: 获取推荐的活动
	 *
	 * @param context
	 * @param recommend
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月15日 下午2:58:03
	 */
	public static List<ActInfo> getRecommendActivityInfo(Context context, int recommend) {
		ActInfoDao dao = DaoHelper.getInstanse(context)
				.getActInfoDao();
		Query<ActInfo> query=dao.queryBuilder()
				.where(ActInfoDao.Properties.Recommend.eq(recommend))
				.build();
		return query.list();
	}
	
	/**
	 * @description: 保存商品
	 *
	 * @param context
	 * @param infos
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月9日 下午9:47:40
	 */
	public static boolean saveGoodsInfo(Context context, List<GoodsInfo> infos) {
		boolean result = true;
		GoodsInfoDao dao = DaoHelper.getInstanse(context)
				.getGoodsInfoDao();
		dao.deleteAll();
		dao.insertInTx(infos);
		return result;
	}
	
	/**
	 * @description: 保存商品详情图片
	 *
	 * @param context
	 * @param infos
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月5日 下午3:00:27
	 */
	public static boolean saveGoodsDetailPictures(Context context, List<GoodsDetailPhoto> infos){
		boolean result = true;
		GoodsDetailPhotoDao dao = DaoHelper.getInstanse(context).getGoodsDetailPhotoDao();
		dao.deleteAll();
		dao.insertInTx(infos);
		return result;
	}
	
	/**
	 * @description: 获取所有的商品
	 *
	 * @param context
	 * @param recommend
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月15日 下午3:06:56
	 */
	public static List<GoodsInfo> getAllGoodsInfo(Context context) {
		GoodsInfoDao dao = DaoHelper.getInstanse(context)
				.getGoodsInfoDao();
		Query<GoodsInfo> query=dao.queryBuilder()
				.build();
		return query.list();
	}
	
	/**
	 * @description: 获取所有商品的数量
	 *
	 * @param context
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年10月22日 上午4:35:42
	 */
	public static long getAllGoodsInfoCount(Context context) {
		GoodsInfoDao dao = DaoHelper.getInstanse(context)
				.getGoodsInfoDao();
		long result = dao.queryBuilder()
				.buildCount().count();
		return result;
	}
	
	public static GoodsInfo getGoodsInfo(Context context, String goodsId) {
		GoodsInfoDao dao = DaoHelper.getInstanse(context)
				.getGoodsInfoDao();
		Query<GoodsInfo> query=dao.queryBuilder().where(GoodsInfoDao.Properties.GoodsId.eq(goodsId))
				.build();
		return query.unique();
	}
	
	/**
	 * @description: 获取推荐商品
	 *
	 * @param context
	 * @param recommend
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月9日 下午9:48:16
	 */
	public static List<GoodsInfo> getRecommendGoodsInfo(Context context, int recommend) {
		GoodsInfoDao dao = DaoHelper.getInstanse(context)
				.getGoodsInfoDao();
		Query<GoodsInfo> query=dao.queryBuilder()
				.where(ActInfoDao.Properties.Recommend.eq(recommend))
				.build();
		return query.list();
	}
	
	/**
	 * @description: 保存礼包对应的游戏关联
	 *
	 * @param context
	 * @param infos
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月9日 下午10:12:56
	 */
	public static boolean saveGameGiftInfo(Context context, List<GameGiftInfo> infos) {
		boolean result = true;
		GameGiftInfoDao dao = DaoHelper.getInstanse(context)
				.getGameGiftInfoDao();
		dao.deleteAll();
		dao.insertInTx(infos);
		return result;
	}
	
	/**
	 * @description: 获取礼包对应的游戏关联
	 *
	 * @param context
	 * @return 
	 * @throws: 
	 * @author: LiuQin
	 * @date: 2015年7月9日 下午10:14:38
	 */
	public static List<GameGiftInfo> getGameGiftInfo(Context context) {
		GameGiftInfoDao dao = DaoHelper.getInstanse(context)
				.getGameGiftInfoDao();
		Query<GameGiftInfo> query=dao.queryBuilder().build();
		return query.list();
	}
	
	/**
	 * @description: 保存礼包
	 *
	 * @param context
	 * @param infos
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月9日 下午10:16:04
	 */
	public static boolean saveGiftInfo(Context context, List<GiftInfo> infos) {
		boolean result = true;
		GiftInfoDao dao = DaoHelper.getInstanse(context)
				.getGiftInfoDao();
		dao.deleteAll();
		dao.insertInTx(infos);
		return result;
	}
	
	/**
	 * @description: 获取礼包
	 *
	 * @param context
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月9日 下午10:44:21
	 */
	public static List<GiftInfo> getGiftInfo(Context context) {
		GiftInfoDao dao = DaoHelper.getInstanse(context)
				.getGiftInfoDao();
		Query<GiftInfo> query=dao.queryBuilder().build();
		return query.list();
	}
	
	/**
	 * @description: 保存用户领取的礼包
	 *
	 * @param context
	 * @param infos
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月9日 下午10:45:11
	 */
	public static boolean saveUserGiftInfo(Context context, List<UserGiftInfo> infos) {
		boolean result = true;
		UserGiftInfoDao dao = DaoHelper.getInstanse(context)
				.getUserGiftInfoDao();
		dao.deleteAll();
//		dao.queryBuilder()
//				.where(UserGiftInfoDao.Properties.UserId.eq(infos.get(0).getUserId())).buildDelete()
//				.executeDeleteWithoutDetachingEntities();
//		dao.insertInTx(infos);
		dao.insertOrReplaceInTx(infos);
		return result;
	}
	
	
	public static boolean saveUserGameGiftInfo(Context context, List<UserGameGiftInfo> infos) {
		boolean result = true;
		UserGameGiftInfoDao dao = DaoHelper.getInstanse(context)
				.getUserGameGiftInfoDao();
		dao.deleteAll();
		dao.insertOrReplaceInTx(infos);
		return result;
	}
	
	
	public static boolean saveUserGameToGift(Context context, List<UserGameToGift> infos) {
		boolean result = true;
		UserGameToGiftDao dao = DaoHelper.getInstanse(context)
				.getUserGameToGiftDao();
//		dao.queryBuilder()
//				.where(UserGameToGiftDao.Properties.UserId.eq(infos.get(0).getUserId())).buildDelete()
//				.executeDeleteWithoutDetachingEntities();
		dao.deleteAll();
		dao.insertInTx(infos);
		return result;
	}
	
	/**
	 * @description: 用户领取的礼包
	 *
	 * @param context
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月9日 下午10:44:21
	 */
	public static List<UserGiftInfo> getUserGiftInfo(Context context, String userId) {
		UserGiftInfoDao dao = DaoHelper.getInstanse(context)
				.getUserGiftInfoDao();
		Query<UserGiftInfo> query=dao.queryBuilder()
//				.where(UserGiftInfoDao.Properties.UserId.eq(userId))
				.build();
		return query.list();
	}
	
	
	public static List<UserGameGiftInfo> getUserGameGiftInfo(Context context) {
		UserGameGiftInfoDao dao = DaoHelper.getInstanse(context)
				.getUserGameGiftInfoDao();
		Query<UserGameGiftInfo> query=dao.queryBuilder()
				.build();
		return query.list();
	}
	
	/**
	 * @description: 保存公告
	 *
	 * @param context
	 * @param infos
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月10日 上午11:27:54
	 */
	public static boolean saveNoticeInfo(Context context, List<NoticeInfo> infos) {
		boolean result = true;
		NoticeInfoDao dao = DaoHelper.getInstanse(context).getNoticeInfoDao();
		dao.insertOrReplaceInTx(infos);
		return result;
	}
	
	/**
	 * @description: 获取公告
	 *
	 * @param context
	 * @param noticeType
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月10日 上午11:29:21
	 */
	public static List<NoticeInfo> getNoticeInfo(Context context, int noticeType) {
		long curTime = TimeHelper.getCurTime();
		NoticeInfoDao dao = DaoHelper.getInstanse(context)
				.getNoticeInfoDao();
		Query<NoticeInfo> query=dao.queryBuilder()
				.where(NoticeInfoDao.Properties.NoticeType.eq(noticeType),
						NoticeInfoDao.Properties.StartTime.le(curTime),
						NoticeInfoDao.Properties.EndTime.ge(curTime))
				.build();
		return query.list();
	}
	
	/**
	 * @description: 视频
	 *
	 * @param context
	 * @param infos
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午12:43:06
	 */
	public static boolean saveVideoInfo(Context context, List<VideoInfo> infos) {
		boolean result = true;
		VideoInfoDao dao = DaoHelper.getInstanse(context).getVideoInfoDao();
		// 当前只有一种教学视频，全部删除
		dao.deleteAll();
		dao.insertInTx(infos);
		return result;
	}
	
	/**
	 * @description: 获取视频
	 *
	 * @param context
	 * @param noticeType
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月10日 上午11:29:21
	 */
	public static List<VideoInfo> getVideoInfo(Context context, String videoType) {
		VideoInfoDao dao = DaoHelper.getInstanse(context)
				.getVideoInfoDao();
		Query<VideoInfo> query=dao.queryBuilder()
				.where(VideoInfoDao.Properties.VideoType.eq(videoType))
				.build();
		return query.list();
	}
	
	
	/**
	 * @description: 记录已评分的游戏
	 *
	 * @param context
	 * @param infos
	 * @return 
	 * @throws: 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午1:53:14
	 */
	public static boolean saveGameScoreRecordInfo(Context context, GameScoreRecordInfo info) {
		boolean result = true;
		GameScoreRecordInfoDao dao = DaoHelper.getInstanse(context).getGameScoreRecordInfoDao();
		dao.insert(info);
		return result;
	}
	
	/**
	 * @description: 用户是否已对该游戏评分
	 *
	 * @param context
	 * @param gameId
	 * @param userId
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月10日 下午2:05:04
	 */
	public static boolean isGameRated(Context context, String gameId, String userId) {
		GameScoreRecordInfoDao dao = DaoHelper.getInstanse(context)
				.getGameScoreRecordInfoDao();
		CountQuery<GameScoreRecordInfo> query=dao.queryBuilder()
				.where(GameScoreRecordInfoDao.Properties.GameId.eq(gameId),GameScoreRecordInfoDao.Properties.UserId.eq(userId))
				.buildCount();
		return query.count() > 0 ? true : false;
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
	public static List<GameInfo> getRelativeGameInfo(Context context, String gameId) {
		List<GameInfo> gameInfoList = new ArrayList<GameInfo>();
		TypeToGameDao dao = DaoHelper.getInstanse(context).getTypeToGameDao();
		Query<TypeToGame> qc = dao.queryBuilder()
				.where(TypeToGameDao.Properties.GameId.eq(gameId))
				.build();
		List<TypeToGame> typeToGameList= qc.list();
		if(!typeToGameList.isEmpty()){
			String typeId = typeToGameList.get(0).getTypeId();
			qc = dao.queryBuilder()
					.where(TypeToGameDao.Properties.TypeId.eq(typeId), TypeToGameDao.Properties.GameId.notEq(gameId))
					.build();
			typeToGameList = qc.list();
			int index;
			TypeToGame typeToGame;
			while (typeToGameList.size()>0 && gameInfoList.size()<6){
				index = getRandomInt(typeToGameList.size());
				typeToGame = typeToGameList.remove(index);
				gameInfoList.add(typeToGame.getGameInfo());
			}
		}
		return gameInfoList;
	}
	
	public static int getRandomInt(int length){
		Random random = new Random();
		int number= random.nextInt(length);
		return number;
	}
	
	/**
	 * @description: 获取推荐的游戏搜索
	 *
	 * @param context
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月14日 下午1:44:53
	 */
	public static List<GameSearchPinyinInfo> getRecommendGameSearchInfo(Context context) {
		int limit = 6;
		List<GameSearchPinyinInfo> gameSearchPinyinInfoList = new ArrayList<GameSearchPinyinInfo>();
		List<GameInfo> gameInfoList = DaoHelper.getGameRanking(context, DataConfig.GAME_RANKING_TYPE_GAMEPAD, limit);
		if(gameInfoList.size()>0){
			GameInfo gameInfo;
			int gameInfoSize = gameInfoList.size();
			for (int i = 0; i < gameInfoSize; i++) {
				GameSearchPinyinInfo gameSearchPinyinInfo = new GameSearchPinyinInfo();
				gameInfo = gameInfoList.get(i);
				gameSearchPinyinInfo.setGameId(gameInfo.getGameId());
				gameSearchPinyinInfo.setIcon(gameInfo.getMinPhoto());
				gameSearchPinyinInfo.setGameName(gameInfo.getGameName());
				gameSearchPinyinInfo.setAppendixZip(gameInfo.getAppendixZip());
				gameSearchPinyinInfo.setDownCount(gameInfo.getGameDownCount());
				gameSearchPinyinInfo.setFile(gameInfo.getFile());
				gameSearchPinyinInfo.setStarLevel(gameInfo.getStartLevel());
				gameSearchPinyinInfo.setType(DataConfig.GAME_TYPE_COPYRIGHT);
				
				gameSearchPinyinInfoList.add(gameSearchPinyinInfo);
			}
		} else {
			gameSearchPinyinInfoList = DaoHelper.getAllGameSearchInfo(context, limit);
		}
		
		return gameSearchPinyinInfoList;
	}
	
	public static boolean saveModelToAd(Context context, List<ModelToAd> infos) {
		boolean result = true;
		ModelToAdDao dao = DaoHelper.getInstanse(context).getModelToAdDao();
		dao.queryBuilder()
				.where(ModelToAdDao.Properties.ModelKey.eq(infos.get(0).getModelKey())).buildDelete()
				.executeDeleteWithoutDetachingEntities();
		dao.insertOrReplaceInTx(infos);
		return result;
	}
	
	/**
	 * @description: 保存接口更新数据
	 *
	 * @param context
	 * @param infos
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月6日 下午5:03:01
	 */
	public static boolean saveUpdateInterfaceInfo(Context context, List<UpdateInterfaceInfo> infos) {
		boolean result = true;
		UpdateInterfaceInfoDao dao = DaoHelper.getInstanse(context).getUpdateInterfaceInfoDao();
		dao.insertOrReplaceInTx(infos);
		return result;
	}
	
	
	/**
	 * @description: 保存接口更新数据
	 *
	 * @param context
	 * @param info
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月6日 下午7:22:56
	 */
	public static boolean saveUpdateInterfaceInfo(Context context, UpdateInterfaceInfo info) {
		boolean result = true;
		UpdateInterfaceInfoDao dao = DaoHelper.getInstanse(context).getUpdateInterfaceInfoDao();
		dao.insertOrReplaceInTx(info);
		return result;
	}
	
	/**
	 * @description: 
	 *
	 * @param context
	 * @param infos
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月6日 下午5:04:11
	 */
	public static boolean saveLocalUpdateInfo(Context context, List<LocalUpdateInfo> infos) {
		boolean result = true;
		LocalUpdateInfoDao dao = DaoHelper.getInstanse(context).getLocalUpdateInfoDao();
		dao.insertOrReplaceInTx(infos);
		return result;
	}
	
	public static boolean saveLocalUpdateInfo(Context context, LocalUpdateInfo infos) {
		boolean result = true;
		LocalUpdateInfoDao dao = DaoHelper.getInstanse(context).getLocalUpdateInfoDao();
		dao.insertOrReplaceInTx(infos);
		return result;
	}
	
	public static boolean getLocalUpdateInfoSize(Context context) {
		LocalUpdateInfoDao dao = DaoHelper.getInstanse(context).getLocalUpdateInfoDao();
		CountQuery<LocalUpdateInfo> query=dao.queryBuilder().buildCount();
		return query.count() > 0;
	}
	
	public static boolean isLocalUpdateInfoExist(Context context, String uniqueId) {
		LocalUpdateInfoDao dao = DaoHelper.getInstanse(context).getLocalUpdateInfoDao();
		CountQuery<LocalUpdateInfo> query=dao.queryBuilder().where(LocalUpdateInfoDao.Properties.LocalUniqueId.eq(uniqueId)).buildCount();
		return query.count() > 0;
	}
	
	/**
	 * @description: 查询可以更新的接口
	 *
	 * @param context
	 * @param gameId
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月6日 下午5:46:32
	 */
	public static List<LocalUpdateInfo> getLocalUpdatableInterface(Context context) {
		LocalUpdateInfoDao dao = DaoHelper.getInstanse(context).getLocalUpdateInfoDao();
    	Query<LocalUpdateInfo> qc = dao.queryRawCreate(
	      " JOIN "+UpdateInterfaceInfoDao.TABLENAME+" G"+
		      " ON T."+LocalUpdateInfoDao.Properties.LocalUniqueId.columnName+"=G."+UpdateInterfaceInfoDao.Properties.UniqueId.columnName+
	      " WHERE T."+LocalUpdateInfoDao.Properties.LocalUpdateTime.columnName+"<"+UpdateInterfaceInfoDao.Properties.UpdateTime.columnName);
		
		return qc.list();
	}
	
//	public static boolean saveSubTypeId(Context context, List<SubTypeId> infos) {
//		boolean result = true;
//		SubTypeIdDao dao = DaoHelper.getInstanse(context).getSubTypeIdDao();
//		dao.insertOrReplaceInTx(infos);
//		return result;
//	}
	
//	public static List<LocalSubTypeId> getLocalUpdatableSubTypeIds(Context context) {
//		LocalSubTypeIdDao dao = DaoHelper.getInstanse(context).getLocalSubTypeIdDao();
//    	Query<LocalSubTypeId> qc = dao.queryRawCreate(
//	      " JOIN "+SubTypeIdDao.TABLENAME+" G"+
//		      " ON T."+LocalSubTypeIdDao.Properties.Localname.columnName+"=G."+SubTypeIdDao.Properties.InterfaceName.columnName+
//	      " WHERE T."+LocalSubTypeIdDao.Properties.LocalUpdateTime.columnName+"<"+SubTypeIdDao.Properties.UpdateTime.columnName);
//		
//		return qc.list();
//	}
	
	public static LocalUpdateInfo getLocalUpdateInfo(Context context, String uniqueId) {
		LocalUpdateInfoDao dao = DaoHelper.getInstanse(context).getLocalUpdateInfoDao();
		Query<LocalUpdateInfo> query=dao.queryBuilder()
				.where(LocalUpdateInfoDao.Properties.LocalUniqueId.eq(uniqueId))
				.build();
		return query.unique();
	}
	
	public static List<LocalUpdateInfo> getNewContentLocalUpdateInfo(Context context) {
		LocalUpdateInfoDao dao = DaoHelper.getInstanse(context).getLocalUpdateInfoDao();
		Query<LocalUpdateInfo> query=dao.queryBuilder()
				.where(LocalUpdateInfoDao.Properties.LocalState.eq(DataConfig.INTERFACE_STATE_NOTSEE))
				.build();
		return query.list();
	}
	
	public static List<LocalUpdateInfo> getNewContentLocalUpdateInfo(Context context, String name) {
		LocalUpdateInfoDao dao = DaoHelper.getInstanse(context).getLocalUpdateInfoDao();
		Query<LocalUpdateInfo> query=dao.queryBuilder()
				.where(LocalUpdateInfoDao.Properties.LocalState.eq(DataConfig.INTERFACE_STATE_NOTSEE),
						LocalUpdateInfoDao.Properties.LocalName.eq(name))
				.build();
		return query.list();
	}
	
//	public static boolean saveLocalSubTypeId(Context context, LocalSubTypeId infos) {
//		boolean result = true;
//		LocalSubTypeIdDao dao = DaoHelper.getInstanse(context).getLocalSubTypeIdDao();
//		dao.insertOrReplaceInTx(infos);
//		return result;
//	}
	
	public static List<TypeToGame> getTypeToGameFromPackageName(Context context, String packageName) {
		TypeToGameDao dao = DaoHelper.getInstanse(context).getTypeToGameDao();
    	Query<TypeToGame> qc = dao.queryRawCreate(
	      " JOIN "+GameInfoDao.TABLENAME+" G"+
		      " ON T."+TypeToGameDao.Properties.GameId.columnName+"=G."+GameInfoDao.Properties.GameId.columnName+
	      " JOIN "+GameTypeInfoDao.TABLENAME+" GT"+
		      " ON T."+TypeToGameDao.Properties.TypeId.columnName+"=GT."+GameTypeInfoDao.Properties.TypeId.columnName+
	      " WHERE G."+GameInfoDao.Properties.PackageName.columnName+"=?"
	      , packageName);

		return qc.list();
	}
	
	public static List<TopicToGame> getTopicToGameThirdFromPackageName(Context context, String packageName) {
		TopicToGameDao dao = DaoHelper.getInstanse(context).getTopicToGameDao();
		
    	Query<TopicToGame> qc = dao.queryRawCreate(
	      " JOIN "+ThirdGameInfoDao.TABLENAME+" G"+
		      " ON T."+TopicToGameDao.Properties.GameId.columnName+"=G."+ThirdGameInfoDao.Properties.GameId.columnName+
	      " JOIN "+GameTopicInfoDao.TABLENAME+" GT"+
		      " ON T."+TopicToGameDao.Properties.TopicId.columnName+"=GT."+GameTopicInfoDao.Properties.TopicId.columnName+
	      " WHERE G."+GameInfoDao.Properties.PackageName.columnName+"=?"+
		      " AND T."+TopicToGameDao.Properties.Type.columnName+"=?"+
		  " ORDER BY T."+TopicToGameDao.Properties.ReturnOrder.columnName+" ASC"    
	      , packageName,0);

		return qc.list();
	}
	
	public static List<TopicToGame> getTopicToGameFromPackageName(Context context, String packageName) {
		TopicToGameDao dao = DaoHelper.getInstanse(context).getTopicToGameDao();
		
    	Query<TopicToGame> qc = dao.queryRawCreate(
	      " JOIN "+GameInfoDao.TABLENAME+" G"+
		      " ON T."+TopicToGameDao.Properties.GameId.columnName+"=G."+GameInfoDao.Properties.GameId.columnName+
	      " JOIN "+GameTopicInfoDao.TABLENAME+" GT"+
		      " ON T."+TopicToGameDao.Properties.TopicId.columnName+"=GT."+GameTopicInfoDao.Properties.TopicId.columnName+
	      " WHERE G."+GameInfoDao.Properties.PackageName.columnName+"=?"+
		      " AND T."+TopicToGameDao.Properties.Type.columnName+"=?"+
		  " ORDER BY T."+TopicToGameDao.Properties.ReturnOrder.columnName+" ASC"    
	      , packageName,1);

		return qc.list();
	}
	
	/**
	 * @description: 获取所有的版权游戏信息
	 *
	 * @param context
	 * @param infos
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年9月21日 上午12:07:18
	 */
	public static List<GameInfo> getAllGameInfo(Context context) {
		GameInfoDao dao = DaoHelper.getInstanse(context).getGameInfoDao();
		return dao.queryBuilder().build().list();
	}
}
