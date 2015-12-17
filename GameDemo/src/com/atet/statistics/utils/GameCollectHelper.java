package com.atet.statistics.utils;

import java.util.List;

import com.atet.statistics.bases.StatisticsConstant;
import com.atet.statistics.model.CollectGameInfo;
import com.atet.tvmarket.model.database.PersistentSynUtils;
import com.atet.tvmarket.utils.DebugTool;

/**
 * 游戏数据(下载量，点击量)功能辅助类
 * 
 * @author zhaominglai
 * @date 2014/9/15
 * 
 * */
public class GameCollectHelper {
	private static final String TAG = "GameCollectHelper";
	
	public static <T> int getGameCopyRight(String packageName,List<T> gameList)
	{
		
		return StatisticsConstant.GAME_COPYRIGHT_OTHER;
	}
	
	public static <T> int getGameCopyRightPlatForm(String packageName,List<T> gameList)
	{
		
		
		return StatisticsConstant.GAME_COPYRIGHT_OTHER;
	}
	
	
	/**
	 * 向本地添加游戏记录
	 * 
	 * @author zhaominglai
	 * @param gameInfo 游戏信息实体类
	 * @param type 要更新的类型  1为点击量，2为广告点击量，3为下载量
	 * 
	 * */
	public static void addGameCollectInfo(CollectGameInfo gameInfo,int type)
	{
		if(gameInfo == null)
			return;
		DebugTool.debug(TAG, "addGameCollectInfo = "+gameInfo);
		String packageName = gameInfo.getPackageName();
		CollectGameInfo oldInfo = null;
		
		String today = DeviceStatisticsUtils.timeToDay(System.currentTimeMillis());
		
		List<CollectGameInfo> list = PersistentSynUtils.getModelList(CollectGameInfo.class," packageName = \'"+packageName+"\' and recordDate = \'" + today + "\'");
		
		int count = list.size();
		
		
		//如果数据库中无记录就新增一条记录
		if (count == 0)
		{
			List<CollectGameInfo> recordList = PersistentSynUtils.getModelList(CollectGameInfo.class," autoIncrementId > 0");
			//如果记录数超过100条则删除最前面的一条,这样保证数据库的记录数永远不会超过100条
			if (recordList != null && recordList.size() > 100)
			{
				PersistentSynUtils.delete(recordList.get(0));
			}
			
			gameInfo.setRecordDate(today);
			
			if (type == StatisticsConstant.GAME_UPDATE_ADCLICK)
			{
				if (gameInfo.getClickCount() == 0)
					gameInfo.setClickCount(1);
			}
			PersistentSynUtils.addModel(gameInfo);
			
		}else{
			
			//oldInfo为数据库中存在的记录
			oldInfo = list.get(0);
			switch(type)
			{
			
			case StatisticsConstant.GAME_UPDATE_CLICK:
				int clickCount = oldInfo.getClickCount()+1;
				oldInfo.setClickCount(clickCount);
				PersistentSynUtils.update(oldInfo);
				
				break;
			case StatisticsConstant.GAME_UPDATE_ADCLICK:
				int clickCount1 = oldInfo.getClickCount()+1;
				int adclickCount = oldInfo.getAdClick()+1;
				System.out.println("clickCount :"+clickCount1+" adclickcount: "+adclickCount);
				oldInfo.setAdClick(adclickCount);
				oldInfo.setClickCount(clickCount1);
				PersistentSynUtils.update(oldInfo);
				break;
			case StatisticsConstant.GAME_UPDATE_DOWNCOUNT:
				DebugTool.debug(TAG, "GAME_UPDATE_DOWNCOUNT");
				int downCount = oldInfo.getDownCount()+1;
				oldInfo.setDownCount(downCount);
				PersistentSynUtils.update(oldInfo);
				break;
				
			default:
				break;
			}
		}
	}
	
	
	
}
