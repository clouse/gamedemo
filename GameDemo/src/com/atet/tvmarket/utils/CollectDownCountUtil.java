package com.atet.tvmarket.utils;

import android.content.Context;

import com.atet.statistics.bases.StatisticsConstant;
import com.atet.statistics.model.CollectGameInfo;
import com.atet.statistics.utils.GameCollectHelper;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.entity.CollectDownCountInfo;
import com.atet.tvmarket.entity.Group;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.GameStatisticsHelper;
import com.atet.tvmarket.model.database.PersistentSynUtils;




/**
 * 
 * @ClassName:  CollectDownCountUtil   
 * @Description:TODO(用于统计游戏下载次数的工具类)   
 * @author wenfuqiang
 * @date:   2014-8-28 下午3:11:12
 */
public class CollectDownCountUtil {
	private static final String TAG = "CollectDownCountUtil";
	
	
	/**
	 * 
	 * @Title: addGame   
	 * @Description: TODO(将专题游戏信息添加进统计下载次数的表中)   
	 * @param: @param gameInfo      
	 * @return: void      
	 * @throws
	 */
	public static void addGame(Context mContext,GameInfo gameInfo){
		PersistentSynUtils.execDeleteData(CollectDownCountInfo.class, "WHERE gameId="
    			+gameInfo.getGameId());
		//删除表中已存在的数据
		CollectDownCountInfo collectDownCountInfo = new CollectDownCountInfo();
		collectDownCountInfo.setAdClick(0);
		collectDownCountInfo.setClickCount(0);
		collectDownCountInfo.setGameId(gameInfo.getGameId());
		collectDownCountInfo.setGameName(gameInfo.getGameName());
		collectDownCountInfo.setCpId(gameInfo.getCpId());
		collectDownCountInfo.setGameType(""+gameInfo.getTypeName());
		collectDownCountInfo.setPackageName(gameInfo.getPackageName());
		collectDownCountInfo.setRecordTime(System.currentTimeMillis());
		collectDownCountInfo.setUserId(DataFetcher.getUserIdInt());
		collectDownCountInfo.setDownCount(0);
		if(!"2".equals(gameInfo.getCpId())){
			//专题游戏
			collectDownCountInfo.setCopyRight(StatisticsConstant.GAME_COPYRIGHT_PLATFORM);
//			collectDownCountInfo.setGameType(DeviceStatisticsUtils.getGameType(BaseApplication.getContext(), gameInfo.getPackageName()));
		}else{
			//第三方游戏
			collectDownCountInfo.setCopyRight(StatisticsConstant.GAME_COPYRIGHT_THIRDPARTY);
//			collectDownCountInfo.setGameType(DeviceStatisticsUtils.getGameType(BaseApplication.getContext(), gameInfo.getPackageName()));
		}
		
		if("GameTopic".equals(gameInfo.getTypeName())){
			collectDownCountInfo.setGameType(GameStatisticsHelper.getGameTypeFromTopicInfo(BaseApplication.getContext(), gameInfo.getPackageName()));
			gameInfo.setTypeName(collectDownCountInfo.getGameType());
			GameStatisticsHelper.updateGameClickCount(gameInfo, 4);
		} else if("GameLatest".equals(gameInfo.getTypeName())){
			GameStatisticsHelper.updateGameClickCount(gameInfo, 4);
		} else if("GameActivity".equals(gameInfo.getTypeName())){
			GameStatisticsHelper.updateGameClickCount(gameInfo, 4);
		}
		
		PersistentSynUtils.addModel(collectDownCountInfo);
	}
	
    /**
     * 
     * @Title: addCollectDownCount   
     * @Description: TODO(添加统计的下载次数信息至统计表中)   
     * @param: @param gameId      
     * @return: void      
     * @throws
     */
    public static void addCollectDownCount(String gameId){
    	CollectGameInfo collectGameInfo = new CollectGameInfo();
    	CollectDownCountInfo collectDownCountInfo = new CollectDownCountInfo();
		Group<CollectDownCountInfo> downCountInfos = PersistentSynUtils.getModelList(CollectDownCountInfo.class, " gameId ='"+gameId+"'");
		if(downCountInfos.size()>0){
			collectDownCountInfo = downCountInfos.get(0);
			collectGameInfo.setAdClick(collectDownCountInfo.getAdClick());
			collectGameInfo.setClickCount(collectDownCountInfo.getClickCount());
			collectGameInfo.setGameId(collectDownCountInfo.getGameId());
			collectGameInfo.setGameName(collectDownCountInfo.getGameName());
			collectGameInfo.setCpId(collectDownCountInfo.getCpId());
			collectGameInfo.setGameType(collectDownCountInfo.getGameType());
			collectGameInfo.setPackageName(collectDownCountInfo.getPackageName());
			collectGameInfo.setRecordTime(System.currentTimeMillis());
			collectGameInfo.setUserId(collectDownCountInfo.getUserId());
			collectGameInfo.setDownCount(1);
			collectGameInfo.setCopyRight(collectDownCountInfo.getCopyRight());
			DebugTool.debug(TAG, "addCollectDownload"+collectGameInfo);
			GameCollectHelper.addGameCollectInfo(collectGameInfo, StatisticsConstant.GAME_UPDATE_DOWNCOUNT);
		}
		deleteDownCount(gameId);
    }
    
    
    /**
     * 
     * @Title: deleteDownCount   
     * @Description: TODO(根据gameId删除CollectDownCount表中信息)   
     * @param: @param gameId      
     * @return: void      
     * @throws
     */
    public static void deleteDownCount(String gameId){
    	PersistentSynUtils.execDeleteData(CollectDownCountInfo.class,
				"where gameId = '"+gameId+"'");
    }
    
    

}
