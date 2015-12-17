package com.atet.tvmarket.model;

import java.util.List;

import android.content.Context;

import com.atet.statistics.bases.StatisticsConstant;
import com.atet.statistics.model.CollectGameInfo;
import com.atet.statistics.utils.DeviceStatisticsUtils;
import com.atet.statistics.utils.GameCollectHelper;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.entity.dao.GameTopicInfo;
import com.atet.tvmarket.entity.dao.GameTypeInfo;
import com.atet.tvmarket.entity.dao.ThirdGameInfo;
import com.atet.tvmarket.entity.dao.TopicToGame;
import com.atet.tvmarket.entity.dao.TypeToGame;

public class GameStatisticsHelper {
	
	/**
	 * @description: 点击统计
	 *
	 * @param gameInfo
	 * @param whereFrom 
	 * @author: LiuQin
	 * @date: 2015年9月26日 下午4:19:28
	 */
	public static void clickStatistics(GameInfo gameInfo, int whereFrom){
		if(whereFrom == StatisticsConstant.FROM_GAME_CENTER){
			//游戏中心
			gameInfo.setTypeName("GameCenter");
		} else if(whereFrom == StatisticsConstant.FROM_GAME_RANKING){
			//游戏排行
			gameInfo.setTypeName("GameRanking");
		} else if(whereFrom == StatisticsConstant.FROM_GAME_TYPE){
			//游戏分类
			gameInfo.setTypeName(GameStatisticsHelper.getGameTypeFromGameTypeInfo(BaseApplication.getContext(), gameInfo.getPackageName()));
		} else if(whereFrom == StatisticsConstant.FROM_GAME_SEARCH){
			// 游戏搜索
			gameInfo.setTypeName("GameSearch");
		}
		GameStatisticsHelper.updateGameClickCount(gameInfo, whereFrom);
	}
	
	/**
	 * @description: 第三方游戏的点击次数
	 *
	 * @param gameInfo 
	 * @author: LiuQin
	 * @date: 2015年8月25日 下午10:00:44
	 */
	public static void updateThirdGameClickCount(ThirdGameInfo gameInfo, Integer whereFrom) {
		// 如果未统计点击次数
		CollectGameInfo collectGameInfo = new CollectGameInfo();

		collectGameInfo.setPackageName(gameInfo.getPackageName());
		collectGameInfo.setCpId(""+2);
		collectGameInfo.setGameName(gameInfo.getGameName());
		collectGameInfo.setClickCount(1);
		collectGameInfo.setDownCount(0);
		if(whereFrom != null && whereFrom == StatisticsConstant.FROM_GAME_SEARCH){
			collectGameInfo.setGameType("GameSearch");
		} else {
			collectGameInfo.setGameType(getGameTypeFromTopicThirdInfo(BaseApplication.getContext(), gameInfo.getPackageName()));
		}
		collectGameInfo.setUserId(DataFetcher.getUserIdInt());
		collectGameInfo.setGameId(gameInfo.getGameId());
		collectGameInfo.setRecordTime(System.currentTimeMillis());
		collectGameInfo.setCopyRight(StatisticsConstant.GAME_COPYRIGHT_THIRDPARTY);
		collectGameInfo.setAdClick(0);
		GameCollectHelper.addGameCollectInfo(collectGameInfo, StatisticsConstant.GAME_UPDATE_CLICK);
	}
	
	/**
	 * @description: 版权游戏的点击次数
	 *
	 * @param gameInfo
	 * @param isFromGameCenter 
	 * @throws: 
	 * @author: LiuQin
	 * @date: 2015年8月25日 下午10:05:49
	 */
	public static void updateGameClickCount(GameInfo gameInfo, int from){
		//如果未统计点击次数
		CollectGameInfo collectGameInfo  = new CollectGameInfo();
		collectGameInfo.setPackageName(gameInfo.getPackageName());
		collectGameInfo.setCpId(gameInfo.getCpId());
		collectGameInfo.setGameName(gameInfo.getGameName());
		collectGameInfo.setGameType(gameInfo.getTypeName());
		collectGameInfo.setUserId(DataFetcher.getUserIdInt());
		collectGameInfo.setDownCount(0);
		collectGameInfo.setGameId(gameInfo.getGameId());
		collectGameInfo.setCopyRight(StatisticsConstant.GAME_COPYRIGHT_PLATFORM);
		collectGameInfo.setRecordTime(System.currentTimeMillis());
		if(from == 1){
			//游戏中心
			collectGameInfo.setClickCount(0);
			collectGameInfo.setAdClick(1);
			GameCollectHelper.addGameCollectInfo(collectGameInfo, StatisticsConstant.GAME_UPDATE_ADCLICK);
		} else {
			collectGameInfo.setClickCount(1);
			collectGameInfo.setAdClick(0);
			GameCollectHelper.addGameCollectInfo(collectGameInfo, StatisticsConstant.GAME_UPDATE_CLICK);
		}
	}
	
    public static String getGameTypeFromGameTypeInfo(Context context,String packageName)
    {
    	List<TypeToGame> gameList1 = DaoHelper.getTypeToGameFromPackageName(BaseApplication.getContext(), packageName);
    	
    	if (gameList1.size() > 0){
    		TypeToGame typeToGame = gameList1.get(0);
    		GameTypeInfo typeInfo = typeToGame.getGameTypeInfo();
    		if(typeInfo!=null){
    			return typeInfo.getName();
    		}
    	}
  
    	return "NoTypeFound";
    }
    
    public static String getGameTypeFromTopicThirdInfo(Context context,String packageName)
    {
    	List<TopicToGame> gameList2 = DaoHelper.getTopicToGameThirdFromPackageName(BaseApplication.getContext(), packageName);
    	if (gameList2.size() > 0){
    		TopicToGame topicToGame = gameList2.get(0);
    		GameTopicInfo topicInfo = topicToGame.getGameTopicInfo();
    		if(topicInfo!=null){
    			return topicInfo.getName();
    		}
    	}
  
    	return "NoTopicFound";
    }
    
    public static String getGameTypeFromTopicInfo(Context context,String packageName)
    {
    	List<TopicToGame> gameList2 = DaoHelper.getTopicToGameFromPackageName(BaseApplication.getContext(), packageName);
    	if (gameList2.size() > 0){
    		TopicToGame topicToGame = gameList2.get(0);
    		GameTopicInfo topicInfo = topicToGame.getGameTopicInfo();
    		if(topicInfo!=null){
    			return topicInfo.getName();
    		}
    	}
    	
    	return getGameTypeFromTopicThirdInfo(context, packageName);
    }
}
