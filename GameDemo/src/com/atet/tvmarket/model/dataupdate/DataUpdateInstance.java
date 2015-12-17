package com.atet.tvmarket.model.dataupdate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.ConditionVariable;
import android.os.Environment;
import android.text.TextUtils;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.common.event.EventBus;
import com.atet.tvmarket.entity.DownStarResp;
import com.atet.tvmarket.entity.DownStarResp.DownStarCount;
import com.atet.tvmarket.entity.NewVersionInfoResp;
import com.atet.tvmarket.entity.Resp;
import com.atet.tvmarket.entity.TaskEvent;
import com.atet.tvmarket.entity.UpdateInterfaceReq;
import com.atet.tvmarket.entity.UploadExceptionReq;
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
import com.atet.tvmarket.entity.dao.VideoInfo;
import com.atet.tvmarket.model.DaoHelper;
import com.atet.tvmarket.model.DataConfig;
import com.atet.tvmarket.model.DataConfig.UpdateInterface;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.DataHelper;
import com.atet.tvmarket.model.DataRequester;
import com.atet.tvmarket.model.HttpHelper;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.SpHelper;
import com.atet.tvmarket.model.TimeHelper;
import com.atet.tvmarket.model.dataupdate.DataUpdateService.OnCompleteCallback;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.model.usertask.UserTaskDaoHelper;

/**
 * @description: 数据更新服务的实例
 *
 * @author: LiuQin
 * @date: 2015年8月6日 下午10:19:08 
 */
public class DataUpdateInstance {
	private static final String TAG = "DataUpdateInstance";
	static ALog alog = ALog.getLogger(DataUpdateService.class);
	private Context mContext;
	private OnCompleteCallback mCallback;
	private volatile boolean mCancel = false;
	
	public DataUpdateInstance(Context context, OnCompleteCallback callback) {
		mContext = context;
		mCallback = callback;
	}
	
	public void doUpdate(){
		alog.debug("");
		new Thread(updateRunnable).start();
	}
	
	public void cancel(){
		mCancel = true;
	}
	
	private boolean isStop(){
		return mCancel;
	}
	
	private Context getContext(){
		return mContext;
	}
	
	private void completeCallback(){
		if(mCallback != null){
			mCallback.onComplete();
		}
	}
	
	private void recycle(){
		mContext = null;
	}
	
	private Runnable updateRunnable = new Runnable() {
		ConditionVariable cv = new ConditionVariable();
		
		@Override
		public void run() {
			try {
				Logtrace.debug(TAG, "========数据同步服务启动========");
				boolean isNormalTime = false;
				if(isExceptionTimesUp()){
					testGetServerId();
					if(!TextUtils.isEmpty(DataHelper.getDeviceInfo().getServerId())){
						List<LocalUpdateInfo> localUpdateInfoList = null;
						if(isNormalTimesUp()){
							isNormalTime = true;
							localUpdateInfoList =  new ArrayList<LocalUpdateInfo>();
							testGetUpdatableInterface(localUpdateInfoList);
						} else {
							Logtrace.debug(TAG, "【数据更新接口】从本地获取上次更新失败的数据");
							localUpdateInfoList = DaoHelper.getLocalUpdatableInterface(getContext());
						}
						alog.debug("localUpdateInfoList size:"+localUpdateInfoList.size());
						int existCount = localUpdateInfoList.size();
						Logtrace.debug(TAG, "【数据更新接口】存在更新的接口数量："+existCount);

						for (LocalUpdateInfo localUpdateInfo : localUpdateInfoList) {
							if(isStop()){
								break;
							}
							update(localUpdateInfo);
						}

						if(existCount>0){
							localUpdateInfoList = DaoHelper.getLocalUpdatableInterface(getContext());
							Logtrace.debug(TAG, "数据更新结束，更新成功的接口数量："+
									(existCount-localUpdateInfoList.size())+"失败数量:"+localUpdateInfoList.size());
						}

						testGetAllGameDownStar();
					}
					saveLastExecuteTime();
				}

				uploadExceptionRecord(getContext().getApplicationContext());
				if(isNormalTime){
					UserTaskDaoHelper.delGameOnlineInvalid(getContext().getApplicationContext());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			setNextSleep(getContext());
			completeCallback();
			recycle();
		}
		
		private void resetLock() {
			cv.close();
		}

		private void block() {
			cv.block();
		}

		private void closeLock() {
			cv.open();
		}
		
		public void update(LocalUpdateInfo localUpdateInfo){
			String interfaceName = localUpdateInfo.getLocalName();
			alog.debug("interfaceName:"+interfaceName + "subId:"+localUpdateInfo.getLocalSubId());
			if(interfaceName.equals(UpdateInterface.ADS)){
				String modelId = localUpdateInfo.getLocalSubId();
				//广告的模板id
				if(modelId.equals(DataConfig.AD_MODEL_ID_GAME_CENTER)){
					//游戏中心
					testGetGameCenterInfo(localUpdateInfo);
				} else if(modelId.equals(DataConfig.AD_MODEL_ID_LAUNCH)){
					//开机广告
					testGetLaunchAdInfo(localUpdateInfo);
				}
			} else if(interfaceName.equals(UpdateInterface.GAME_ACTIVITY)){
				//活动
				testGetAct(localUpdateInfo);
			} else if(interfaceName.equals(UpdateInterface.GAME_GOODS)){
				//商品
				testGetGoods(localUpdateInfo);
			} else if(interfaceName.equals(UpdateInterface.GAME_GIFT)){
				//礼包
				testGetGameGift(localUpdateInfo);
			} else if(interfaceName.equals(UpdateInterface.GAME_NEW_UPLOADED)){
				//最近上线
				testGetNewUploadGameInfos(localUpdateInfo);
			} else if(interfaceName.equals(UpdateInterface.GAME_RANKING)){
				//排行
				String typeId = localUpdateInfo.getLocalSubId();
				if(TextUtils.isDigitsOnly(typeId)){
					int type = Integer.parseInt(typeId);
					testGetGameRanking(localUpdateInfo, type);
				}
			} else if(interfaceName.equals(UpdateInterface.GAME_TOPIC)){
				//专题
				testGetGameTopic(localUpdateInfo);
			} else if(interfaceName.equals(UpdateInterface.GAME_TOPIC_DETAIL)){
				//专题详情
				String typeId = localUpdateInfo.getLocalSubId();
				testGetGameInfosFromGameTopic2(typeId);
			} else if(interfaceName.equals(UpdateInterface.GAME_TYPE)){
				//分类
				testGetGameType(localUpdateInfo);
			} else if(interfaceName.equals(UpdateInterface.GAME_TYPE_DETAIL)){
				//分类详情
				String typeId = localUpdateInfo.getLocalSubId();
				testGetGameInfosFromGameType2(typeId);
			} else if(interfaceName.equals(UpdateInterface.VIDEO_GUIDE)){
				String typeId = localUpdateInfo.getLocalSubId();
				if(typeId.equals(DataConfig.VIDEO_TYPE_GUIDE)){
					testGetVideoInfo();
				}
			} else if(interfaceName.equals(UpdateInterface.NOTICE)){
				String typeId = localUpdateInfo.getLocalSubId();
				if(TextUtils.isDigitsOnly(typeId)){
					int type = Integer.parseInt(typeId);
					testGetNoticeInfo(type);
				}
			} else if(interfaceName.equals(UpdateInterface.QUERY_VERSION)){
				String typeId = localUpdateInfo.getLocalSubId();
				if(typeId.equals(DataConfig.MARKET_APPID)){
					testGetNewVersionInfo();
				}
			} else if(interfaceName.equals(UpdateInterface.PINYIN_SEARCH)){
				testGetGameSearchPinyin();
			}
		}
		
		/**
		 * @description: 数据更新后更新本地更新时间
		 *
		 * @param localUpdateInfo 
		 * @author: LiuQin
		 * @date: 2015年8月7日 上午11:06:55
		 */
//		private void updateLocalTime(LocalUpdateInfo localUpdateInfo){
//			long updateTime=localUpdateInfo.getUpdateInterfaceInfo().getUpdateTime();
//			localUpdateInfo.setLocalUpdateTime(updateTime);
//			alog.debug("update local time:"+updateTime);
//			localUpdateInfo.update();
//		}
		
		/**
		 * @description: 获取server id
		 * 
		 * @author: LiuQin
		 * @date: 2015年7月4日 下午7:06:12
		 */
		public void testGetServerId() {
			if (!TextUtils.isEmpty(DataHelper.getDeviceInfo().getServerId())) {
				return;
			}
			
			resetLock();
			DataHelper.initDeviceInfo(getContext());

			alog.debug("device info:" + DataHelper.getDeviceInfo().toString());
			if (TextUtils.isEmpty(DataHelper.getDeviceInfo().getServerId())) {
				Logtrace.debug(TAG, "【服务器请求ID接口】不存在");
				alog.debug("server id not exist, will request to server");
				ReqCallback<String> reqCallback = new ReqCallback<String>() {
					@Override
					public void onResult(TaskResult<String> taskResult) {
						int code = taskResult.getCode();
						alog.info("taskResult code:" + code);
						Logtrace.debug(TAG, "【服务器请求ID接口】更新结果："+code);
						if (code == TaskResult.OK) {
							alog.info(taskResult.getData());
						}

						closeLock();
					}
				};
				DataFetcher.getServerId(getContext(), reqCallback, false).request(getContext());

				block();
			} else {
				alog.debug("server id exist:" + DataHelper.getDeviceInfo().getServerId());
			}
		}
		
		/**
		 * @description: 获取可更新的接口
		 *
		 * @param localUpdateInfoList 
		 * @author: LiuQin
		 * @date: 2015年8月6日 下午10:20:25
		 */
		public void testGetUpdatableInterface(final List<LocalUpdateInfo> localUpdateInfoList) {
			Logtrace.debug(TAG, "【数据更新接口】更新开始");
			resetLock();

			Long lastTime = 0l;
			lastTime = (Long)SpHelper.get(getContext(), SpHelper.KEY_UPDATE_INTERFACE, lastTime);
			UpdateInterfaceReq reqInfo = new UpdateInterfaceReq();
			reqInfo.setLastTime(lastTime);
			
			ReqCallback<List<LocalUpdateInfo>> reqCallback = new ReqCallback<List<LocalUpdateInfo>>() {
				@Override
				public void onResult(TaskResult<List<LocalUpdateInfo>> taskResult) {
					int code = taskResult.getCode();
					alog.info("taskResult code:" + code);
					Logtrace.debug(TAG, "【数据更新接口】更新结果:"+code);
					if (code == TaskResult.OK) {
						List<LocalUpdateInfo> infos = taskResult.getData();
//						for (LocalUpdateInfo localUpdateInfo : infos) {
//							alog.info(localUpdateInfo.getLocalname());
//						}
						localUpdateInfoList.addAll(infos);
					}

					closeLock();
				}
			};

			DataFetcher.getUpdatableInterface(getContext(), reqCallback, reqInfo, false)
				.update(getContext());

			block();
		}
		
		/**
		 * @description: 游戏中心
		 * 
		 * @author: LiuQin
		 * @date: 2015年8月7日 上午11:00:58
		 */
		public void testGetGameCenterInfo(final LocalUpdateInfo localUpdateInfo) {
			Logtrace.debug(TAG, "【游戏中心接口】更新开始");
			resetLock();

			alog.debug("");
			ReqCallback<List<AdInfo>> reqCallback = new ReqCallback<List<AdInfo>>() {
				@Override
				public void onResult(TaskResult<List<AdInfo>> taskResult) {
					int code = taskResult.getCode();
					alog.info("taskResult code:" + code);
					Logtrace.debug(TAG, "【游戏中心接口】更新结果:"+code);
					if (code == TaskResult.OK) {
//						List<AdInfo> infos = taskResult.getData();
//						if (infos != null && !infos.isEmpty()) {
//							for (AdInfo adInfo : infos) {
//								alog.info("ad name:"+adInfo.getGameName()+" sizeType:"+adInfo.getSizeType());
//							}
//						}
//						postEvent(taskResult);
					}

					closeLock();
				}
			};
			DataFetcher.getGameCenterInfo(getContext(), reqCallback, false)
				.update(getContext());

			block();
		}
		
		public void testGetLaunchAdInfo(final LocalUpdateInfo localUpdateInfo) {
			Logtrace.debug(TAG, "【开机广告接口】更新开始");
			resetLock();

			alog.debug("");
			ReqCallback<List<String>> reqCallback = new ReqCallback<List<String>>() {
				@Override
				public void onResult(TaskResult<List<String>> taskResult) {
					int code = taskResult.getCode();
					alog.info("taskResult code:" + code);
					Logtrace.debug(TAG, "【开机广告接口】更新结果:"+code);
					if (code == TaskResult.OK) {
//						List<AdInfo> infos = taskResult.getData();
//						if (infos != null && !infos.isEmpty()) {
//							for (AdInfo adInfo : infos) {
//								alog.info("ad name:"+adInfo.getGameName()+" sizeType:"+adInfo.getSizeType());
//							}
//						}
//						postEvent(taskResult);
					}

					closeLock();
				}
			};
			DataFetcher.getLauchAd(getContext(), reqCallback, false)
				.update(getContext());

			block();
		}
		
		
		/**
		 * @description: 活动
		 * 
		 * @author: LiuQin
		 * @date: 2015年7月9日 下午9:36:09
		 */
		public void testGetAct(final LocalUpdateInfo localUpdateInfo) {
			Logtrace.debug(TAG, "【活动接口】更新开始");
			resetLock();

			alog.debug("");
			ReqCallback<List<ActInfo>> reqCallback = new ReqCallback<List<ActInfo>>() {
				@Override
				public void onResult(TaskResult<List<ActInfo>> taskResult) {
					int code = taskResult.getCode();
					alog.info("taskResult code:" + code);
					Logtrace.debug(TAG, "【活动接口】更新结果:"+code);
					if (code == TaskResult.OK) {
//						List<ActInfo> infos = taskResult.getData();
//						if (infos != null && !infos.isEmpty()) {
//							alog.info(infos.toString());
//						}
//						postEvent(taskResult);
					}
					
					closeLock();
				}
			};
			DataFetcher.getAct(getContext(), DataConfig.ACTIVITY_TYPE_ALL, reqCallback, false)
				.update(getContext());

			block();
		}
		
		/**
		 * @description: 商品
		 * 
		 * @throws:
		 * @author: LiuQin
		 * @date: 2015年7月9日 下午10:36:16
		 */
		public void testGetGoods(final LocalUpdateInfo localUpdateInfo) {
			Logtrace.debug(TAG, "【商品接口】更新开始");
			resetLock();

			alog.debug("");
			ReqCallback<List<GoodsInfo>> reqCallback = new ReqCallback<List<GoodsInfo>>() {
				@Override
				public void onResult(TaskResult<List<GoodsInfo>> taskResult) {
					int code = taskResult.getCode();
					alog.info("taskResult code:" + code);
					Logtrace.debug(TAG, "【商品接口】更新结果:"+code);
					if (code == TaskResult.OK) {
//						List<GoodsInfo> infos = taskResult.getData();
//						if (infos != null && !infos.isEmpty()) {
//							alog.info(infos.toString());
//						}
//						postEvent(taskResult);
					}
					
					closeLock();
				}
			};
			DataFetcher.getGoods(getContext(), DataConfig.GOODS_TYPE_ALL, reqCallback, false)
				.update(getContext());

			block();
		}
		
		/**
		 * @description: 游戏礼包
		 * 
		 * @author: LiuQin
		 * @date: 2015年7月9日 下午10:36:33
		 */
		public void testGetGameGift(final LocalUpdateInfo localUpdateInfo) {
			Logtrace.debug(TAG, "【游戏礼包接口】更新开始");
			resetLock();

			alog.debug("");
			ReqCallback<List<GameGiftInfo>> reqCallback = new ReqCallback<List<GameGiftInfo>>() {
				@Override
				public void onResult(TaskResult<List<GameGiftInfo>> taskResult) {
					int code = taskResult.getCode();
					alog.info("taskResult code:" + code);
					Logtrace.debug(TAG, "【游戏礼包接口】更新结果:"+code);
					if (code == TaskResult.OK) {
//						List<GameGiftInfo> infos = taskResult.getData();
//						if (infos != null && !infos.isEmpty()) {
//							alog.info(infos.toString());
//						}
//						postEvent(taskResult);
					}

					closeLock();
				}
			};
			DataFetcher.getGameGift(getContext(), reqCallback, false).update(getContext());

			block();
		}
		
		/**
		 * @description: 获取新游推荐
		 * 
		 * @author: LiuQin
		 * @date: 2015年7月5日 下午6:20:50
		 */
		public void testGetNewUploadGameInfos(final LocalUpdateInfo localUpdateInfo) {
			Logtrace.debug(TAG, "【新游推荐接口】更新开始");
			resetLock();

			ReqCallback<List<GameInfo>> reqCallback = new ReqCallback<List<GameInfo>>() {
				@Override
				public void onResult(TaskResult<List<GameInfo>> taskResult) {
					int code = taskResult.getCode();
					alog.info("taskResult code:" + code);
					Logtrace.debug(TAG, "【新游推荐接口】更新结果:"+code);
					if (code == TaskResult.OK) {
//						List<GameInfo> info = taskResult.getData();
//						if (info != null && !info.isEmpty()) {
//							alog.info(info.toString());
//						}
//						postEvent(taskResult);
					}

					closeLock();
				}

				@Override
				public void onUpdate(TaskResult<List<GameInfo>> taskResult) {

				}
			};
			DataFetcher.getGameNewUploaded(getContext(), reqCallback, false).update(getContext());

			block();
		}

		/**
		 * @description: 游戏排行
		 * 
		 * @author: LiuQin
		 * @date: 2015年7月5日 下午8:08:04
		 */
		public void testGetGameRanking(final LocalUpdateInfo localUpdateInfo, int type) {
			Logtrace.debug(TAG, "【游戏排行接口】更新开始,type:"+type);
			resetLock();

			// 获取手柄游戏的排行
//			int type = DataConfig.GAME_RANKING_TYPE_GAMEPAD;
			ReqCallback<List<GameInfo>> reqCallback = new ReqCallback<List<GameInfo>>() {
				@Override
				public void onResult(TaskResult<List<GameInfo>> taskResult) {
					int code = taskResult.getCode();
					alog.info("taskResult code:" + code);
					Logtrace.debug(TAG, "【游戏排行接口】更新结果:"+code);
					if (code == TaskResult.OK) {
//						List<GameInfo> info = taskResult.getData();
//						if (info != null && !info.isEmpty()) {
//							alog.info(info.toString());
//						}
//						postEvent(taskResult);
					}

					closeLock();
				}
			};
			DataFetcher.getGameRanking(getContext(), type, reqCallback, false).update(getContext());

			block();
		}
		
		/**
		 * @description: 游戏分类
		 * 
		 * @author: LiuQin
		 * @date: 2015年8月7日 上午11:27:23
		 */
		public void testGetGameType(final LocalUpdateInfo localUpdateInfo) {
			Logtrace.debug(TAG, "【游戏分类接口】更新开始");
			resetLock();

			//创建结果回调监听，如果不需要知道结果，可不创建
			ReqCallback<List<GameTypeInfo>> reqCallback = new ReqCallback<List<GameTypeInfo>>() {
				//调用 DataRequester的request方法后结果会回调到这个接口
				@Override
				public void onResult(TaskResult<List<GameTypeInfo>> taskResult) {
					int code = taskResult.getCode();
					alog.info("taskResult code:" + code);
					Logtrace.debug(TAG, "【游戏分类接口】更新结果:"+code);
					if (code == TaskResult.OK) {
						//数据请求成功
//						List<GameTypeInfo> infos = taskResult.getData();
//						if (infos != null && !infos.isEmpty()) {
//							alog.info(infos.toString());
//						}
//						postEvent(taskResult);
					}
					
					closeLock();
				}
			};

			//构造DataRequester
			DataRequester dataRequester = DataFetcher.getGameType(getContext(), reqCallback, false);
			//立即请求数据
			dataRequester.update(getContext());

			block();
		}
		
		/**
		 * @description: 获取游戏专题
		 * 
		 * @throws:
		 * @author: LiuQin
		 * @date: 2015年7月6日 下午7:08:30
		 */
		public void testGetGameTopic(final LocalUpdateInfo localUpdateInfo) {
			Logtrace.debug(TAG, "【游戏专题接口】更新开始");
			resetLock();

			ReqCallback<List<GameTopicInfo>> reqCallback = new ReqCallback<List<GameTopicInfo>>() {
				@Override
				public void onResult(TaskResult<List<GameTopicInfo>> taskResult) {
					int code = taskResult.getCode();
					alog.info("taskResult code:" + code);
					Logtrace.debug(TAG, "【游戏专题接口】更新结果:"+code);
					if (code == TaskResult.OK) {
//						List<GameTopicInfo> infos = taskResult.getData();
//						if (infos != null && !infos.isEmpty()) {
//							alog.info(infos.toString());
//						}
//						postEvent(taskResult);
					}

					closeLock();
				}
			};
			DataFetcher.getGameTopic(getContext(), reqCallback, false).update(getContext());

			block();
		}

		/**
		 * @description: 先获取专题，再取某个专题下的游戏
		 * 
		 * @throws:
		 * @author: LiuQin
		 * @date: 2015年7月6日 下午9:43:34
		 */
		public void testGetGameInfosFromGameTopic2(final String topicId) {
			resetLock();

			ReqCallback<List<GameTopicInfo>> reqCallback = new ReqCallback<List<GameTopicInfo>>() {
				@Override
				public void onResult(TaskResult<List<GameTopicInfo>> taskResult) {
					int code = taskResult.getCode();
					alog.info("taskResult code:" + code);
					if (code != TaskResult.OK) {
						closeLock();
						return;
					}
					
					alog.debug("获取专题成功");
					GameTopicInfo gameTopicInfo = null;
					List<GameTopicInfo> infos = taskResult.getData();
					for (GameTopicInfo topicInfo : infos) {
						if(topicInfo.getTopicId().equals(topicId)){
							gameTopicInfo = topicInfo;
							break;
						}
					}
					
					if(gameTopicInfo == null){
						alog.warn("topic not found,topicId:"+topicId);
						closeLock();
						return;
					}

					Logtrace.debug(TAG, "【专题详情接口】更新开始,专题名称:"+gameTopicInfo.getName());
					alog.debug("获取【"+gameTopicInfo.getName()+"】专题下的游戏 ");
					ReqCallback<GameTopicInfo> reqCallback = new ReqCallback<GameTopicInfo>() {
						@Override
						public void onResult(TaskResult<GameTopicInfo> taskResult) {
							int code = taskResult.getCode();
							alog.info("taskResult code:" + code);
							Logtrace.debug(TAG, "【专题详情接口】更新结果:"+code);
							if (code == TaskResult.OK) {
//								postEvent(taskResult);
//								GameTopicInfo gameTopicInfo = taskResult.getData();
//								if (gameTopicInfo != null) {
//									// alog.info(infos.toString());
//									List<TopicToGame> topicToGames = gameTopicInfo.getTopicToGameList();
//									if(topicToGames!=null && !topicToGames.isEmpty()){
//										alog.debug("从网络获取到的【"+gameTopicInfo.getName()+"】专题下的游戏 ");
//										for (TopicToGame info: topicToGames) {
//											if(info.getType() == DataConfig.GAME_TYPE_COPYRIGHT){
//												//版权游戏
//												alog.info("版权游戏名称:"+ info.getGameInfo().getGameName());
//											} else {
//												//第三方游戏
//												alog.info("第三方游戏名称:"+ info.getThirdGameInfo().getGameName());
//											}
//										}
//									}
//								}
							}
							closeLock();
						}
					};
					DataFetcher.getGameInfosFromGameTopic2(getContext(), gameTopicInfo, reqCallback, false).update(getContext());
				}
			};
			DataFetcher.getGameTopic(getContext(), reqCallback, false).update(getContext());

			block();
		}
		
		/**
		 * @description: 先获取游戏分类，再获取其中一个分类下的游戏
		 * 
		 * @author: LiuQin
		 * @date: 2015年7月5日 下午3:42:48
		 */
		public void testGetGameInfosFromGameType2(final String typeId) {
			resetLock();

			ReqCallback<List<GameTypeInfo>> reqCallback = new ReqCallback<List<GameTypeInfo>>() {
				@Override
				public void onResult(TaskResult<List<GameTypeInfo>> taskResult) {
					int code = taskResult.getCode();
					alog.info("taskResult code:" + code);
					if (code != TaskResult.OK) {
						closeLock();
						return;
					}
					
					alog.debug("获取分类成功");
					GameTypeInfo gameTypeInfo = null;
					List<GameTypeInfo> infos = taskResult.getData();
					for (GameTypeInfo typeInfo : infos) {
						if(typeInfo.getTypeId().equals(typeId)){
							gameTypeInfo = typeInfo;
							break;
						}
					}
					
					if(gameTypeInfo == null){
						alog.warn("game type not found,typeId:"+typeId);
						closeLock();
						return;
					}

					alog.debug("获取【"+gameTypeInfo.getName()+"】分类下的游戏 ");
					Logtrace.debug(TAG, "【分类详情接口】更新开始,分类名称:"+gameTypeInfo.getName());
//					List<TypeToGame> typeToGames = gameTypeInfo.getTypeToGameList();
					ReqCallback<GameTypeInfo> reqCallback = new ReqCallback<GameTypeInfo>() {
						@Override
						public void onResult(TaskResult<GameTypeInfo> taskResult) {
							int code = taskResult.getCode();
							alog.info("taskResult code:" + code);
							Logtrace.debug(TAG, "【分类详情接口】更新结果:"+code);
							if (code == TaskResult.OK) {
//								postEvent(taskResult);
//								GameTypeInfo gameTypeInfo = taskResult.getData();
//								if (gameTypeInfo != null) {
//									// alog.info(infos.toString());
//									List<TypeToGame> typeToGames = gameTypeInfo.getTypeToGameList();
//									if(typeToGames!=null && !typeToGames.isEmpty()){
//										alog.debug("从网络获取到的【"+gameTypeInfo.getName()+"】分类下的游戏 ");
//										for (TypeToGame info: typeToGames) {
//											alog.info("游戏名称:"+ info.getGameInfo().getGameName());
//										}
//									}
//								}
							}
							closeLock();
						}
					};
					DataFetcher.getGameInfosFromGameType2(getContext(), gameTypeInfo, reqCallback, false).update(getContext());
				}
			};
			DataFetcher.getGameType(getContext(), reqCallback, false).update(getContext());

			block();
		}
		
		/**
		 * @description: 视频
		 * 
		 * @throws:
		 * @author: LiuQin
		 * @date: 2015年7月10日 下午1:22:04
		 */
		public void testGetVideoInfo() {
			Logtrace.debug(TAG, "【视频教学接口】更新开始");
			resetLock();

			alog.debug("");
			ReqCallback<List<VideoInfo>> reqCallback = new ReqCallback<List<VideoInfo>>() {
				@Override
				public void onResult(TaskResult<List<VideoInfo>> taskResult) {
					int code = taskResult.getCode();
					alog.info("taskResult code:" + code);
					Logtrace.debug(TAG, "【视频教学接口】更新结果:"+code);
					if (code == TaskResult.OK) {
//						List<VideoInfo> infos = taskResult.getData();
//						alog.info(infos.toString());
//						postEvent(taskResult);
					}

					closeLock();
				}
			};
			DataFetcher.getVideoInfo(getContext(), reqCallback, false).update(getContext());

			block();
		}
		
		/**
		 * @description: 公告
		 * 
		 * @throws:
		 * @author: LiuQin
		 * @date: 2015年7月10日 下午12:29:20
		 */
		public void testGetNoticeInfo(int type) {
			Logtrace.debug(TAG, "【公告接口】更新开始,type:"+type);
			resetLock();

			alog.debug("");
			ReqCallback<List<NoticeInfo>> reqCallback = new ReqCallback<List<NoticeInfo>>() {
				@Override
				public void onResult(TaskResult<List<NoticeInfo>> taskResult) {
					int code = taskResult.getCode();
					alog.info("taskResult code:" + code);
					Logtrace.debug(TAG, "【公告接口】更新结果:"+code);
					if (code == TaskResult.OK) {
//						List<NoticeInfo> infos = taskResult.getData();
//						alog.info(infos.toString());
//						postEvent(taskResult);
					}

					closeLock();
				}
			};
			DataFetcher.getNoticeInfo(getContext(), type, reqCallback, false).update(getContext());

			block();
		}
		
		/**
		 * @description: 获取新版本信息,用于检测升级
		 * 
		 * @throws: 
		 * @author: LiuQin
		 * @date: 2015年7月29日 上午11:34:02
		 */
		public void testGetNewVersionInfo() {
			Logtrace.debug(TAG, "【获取新版本接口】更新开始");
			resetLock();

			ReqCallback<NewVersionInfoResp> reqCallback = new ReqCallback<NewVersionInfoResp>() {
				@Override
				public void onGetCacheData(String requestTag, boolean result) {
					super.onGetCacheData(requestTag, result);
					if(!result){
						// 不存在缓存数据，需要从网络获取，界面根据ui设计是否显示正在加载的进度条
						alog.info("no cache data");
					}
				}

				@Override
				public void onResult(TaskResult<NewVersionInfoResp> taskResult) {
					int code = taskResult.getCode();
					alog.info("taskResult code:" + code);
					Logtrace.debug(TAG, "【获取新版本接口】更新结果:"+code);
					if (code == TaskResult.OK) {
//						final NewVersionInfoResp info = taskResult.getData();
//						if(info.isNewVersionExist()){
//							// 存在新版本
//							alog.info("有新版本可以升级");
//							alog.info(info.toString());
//						} else {
//							// 已经是最新版本
//							alog.info("大厅已经是最新版本");
//						}
//						postEvent(taskResult);
					}

					closeLock();
				}
			};
			DataFetcher.getNewVersionInfo(getContext(), reqCallback, false).update(getContext());

			block();
		}
		
		/**
		 * @description: 拼音搜索
		 * 
		 * @throws:
		 * @author: LiuQin
		 * @date: 2015年7月7日 下午2:30:56
		 */
		public void testGetGameSearchPinyin() {
			Logtrace.debug(TAG, "【拼音搜索接口】更新开始");
			resetLock();
			
			// 从服务器取所有数据
			ReqCallback<List<GameSearchPinyinInfo>> reqCallback = new ReqCallback<List<GameSearchPinyinInfo>>() {
				@Override
				public void onResult(
						TaskResult<List<GameSearchPinyinInfo>> taskResult) {
					int code = taskResult.getCode();
					alog.info("taskResult code:" + code);
					Logtrace.debug(TAG, "【拼音搜索接口】更新结果:"+code);
					if (code == TaskResult.OK) {
//						List<GameSearchPinyinInfo> infos = taskResult.getData();
//						if (infos != null && !infos.isEmpty()) {
//							alog.info(infos.toString());
//						}
//						postEvent(taskResult);
					}

					closeLock();
				}
			};
			DataFetcher.getGameSearchPinyin(getContext(), reqCallback, false).update(getContext());

			block();
		}
		
		/**
		 * @description: 获取所有游戏的下载次数和星级
		 * 
		 * @author: LiuQin
		 * @date: 2015年9月20日 下午11:52:28
		 */
		public void testGetAllGameDownStar() {
			Logtrace.debug(TAG, "【游戏下载次数和星级接口】更新开始");
			resetLock();

			alog.debug("");

			Long lastUpdateTime = 0l;
			lastUpdateTime = (Long)SpHelper.get(getContext(), SpHelper.KEY_DOWN_STAR_LAST_UPDATE_TIME, lastUpdateTime);
			if(TimeHelper.isToday(lastUpdateTime)){
				alog.debug("今天已经更新过，更新时间:"+lastUpdateTime);
				return;
			}

			ReqCallback<DownStarResp> reqCallback = new ReqCallback<DownStarResp>() {
				@Override
				public void onResult(TaskResult<DownStarResp> taskResult) {
					int code = taskResult.getCode();
					alog.info("taskResult code:" + code);
					Logtrace.debug(TAG, "【游戏下载次数和星级接口】获取数据结果:"+code);
					if (code == TaskResult.OK) {
						DownStarResp info = taskResult.getData();
//						alog.info(info.toString());

						List<DownStarCount> downStarCountInfos = info.getData();
						if(downStarCountInfos!=null && downStarCountInfos.size()>0){
							Map<String,DownStarCount> downStarMap = new HashMap<String, DownStarCount>(); 
							for (DownStarCount downStarCount : downStarCountInfos) {
								downStarMap.put(downStarCount.getGameId(), downStarCount);
							}
							List<GameInfo> gameInfos = DaoHelper.getAllGameInfo(getContext());
							DownStarCount downStarCount;
							if(!gameInfos.isEmpty()){
								for (GameInfo gameInfo : gameInfos) {
									downStarCount = downStarMap.get(gameInfo.getGameId());
									if(downStarCount!=null && downStarCount.getDownCount()!=gameInfo.getGameDownCount()
											&& downStarCount.getStartLevel()!=gameInfo.getStartLevel()){
										alog.debug("update downcount for game:"+gameInfo.getGameName());
										gameInfo.setGameDownCount(downStarCount.getDownCount());
										gameInfo.setStartLevel(downStarCount.getStartLevel());

										gameInfo.update();
									}
								}
							}
							downStarMap.clear();
							Logtrace.debug(TAG, "【游戏下载次数和星级接口】完成");
						}

						SpHelper.put(getContext(), SpHelper.KEY_DOWN_STAR_LAST_UPDATE_TIME, TimeHelper.getCurTime());
					}

					closeLock();
				}
			};
			DataFetcher.getAllGameDownStar(getContext(), reqCallback, false).request(getContext());

			block();
		}

	};
	
	private void saveLastExecuteTime(){
		if(!isStop()){
			SpHelper.put(getContext(), SpHelper.KEY_LAST_DATA_UPDATE_TIME, TimeHelper.getCurTime());
		}
	}
	
	private boolean isNormalTimesUp(){
		boolean result = false;
		Long defaultTime = 0l;
		long lastUpdateTime = (Long)SpHelper.get(getContext(), SpHelper.KEY_LAST_DATA_UPDATE_TIME, defaultTime);
		long elapsedTime = TimeHelper.getCurTime()-lastUpdateTime + 1;
		if(elapsedTime >= DataConfig.NomalSpaceTime || elapsedTime <= 0){
			result = true;
		}
		alog.debug("isNormalTimesUp:"+result+" elapsedTime(min):"+(elapsedTime/60/1000));
		Logtrace.debug(TAG, "是否达到正常更新时间间隔:"+result+ " 距离上次更新时间(min):"+(elapsedTime/60/1000));
		return result;
	}
	
	private boolean isExceptionTimesUp(){
		boolean result = false;
		Long defaultTime = 0l;
		long lastUpdateTime = (Long)SpHelper.get(getContext(), SpHelper.KEY_LAST_DATA_UPDATE_TIME, defaultTime);
		long elapsedTime = TimeHelper.getCurTime()-lastUpdateTime + 1;
		if(elapsedTime >= DataConfig.ExceptionSpaceTime || elapsedTime <= 0){
			result = true;
		}
		alog.debug("isExceptionTimesUp:"+result+" elapsedTime(min):"+(elapsedTime/60/1000));
		Logtrace.debug(TAG, "是否达到异常更新时间间隔:"+result+ " 距离上次更新时间(min):"+(elapsedTime/60/1000));
		return result;
	}
	
	public static void setNextSleep(Context context){
		List<LocalUpdateInfo> localUpdateInfoList = DaoHelper.getLocalUpdatableInterface(context);
		long time = DataConfig.NomalSpaceTime;
		if(localUpdateInfoList.size() > 0 || !DaoHelper.getLocalUpdateInfoSize(context)){
			//有数据更新失败
			time = DataConfig.ExceptionSpaceTime;
		}
		Long defaultTime = 0l;
		long lastUpdateTime = (Long)SpHelper.get(context, SpHelper.KEY_LAST_DATA_UPDATE_TIME, defaultTime);
		time -= (TimeHelper.getCurTime() - lastUpdateTime);
		if(time <= 0){
			time = 10 * 60 * 1000;
		}
		Logtrace.debug(TAG, "下次服务启动睡眠(s):"+(time/1000));
		TimeHelper.starDataUpdateService(context, time);
	}	
	
//	private void postEvent(TaskResult taskResult){
//		TaskEvent taskEvent = new TaskEvent();
//		taskEvent.setTag(taskResult.getTaskId());
//		taskEvent.setTaskResult(taskResult);
//		EventBus.getDefault().post(taskEvent);
//	}
	
	/**
	 * @description: 上传报错日志
	 *
	 * @param context 
	 * @author: LiuQin
	 * @date: 2015年10月9日 上午1:42:17
	 */
	private void uploadExceptionRecord(Context context){
		Logtrace.debug(TAG, "【上传报错日志】");
		List<String> pathList = new ArrayList<String>();
		String path = null;  
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {  
			path = Environment.getExternalStorageDirectory().getPath()+"/ATET/tvmarket/crash/";
			pathList.add(path);
		}
		path = context.getFilesDir().getPath()+"/crash/";
		pathList.add(path);
		
		File file;
		for (String whichPath : pathList) {
			file = new File(whichPath);
			if(file.exists()){
				File[] exceptionFilesArr = file.listFiles();
				if(exceptionFilesArr!=null && exceptionFilesArr.length>0){
					for (int i = 0; i < exceptionFilesArr.length; i++) {
						File exceptionFile = exceptionFilesArr[i];
						UploadExceptionReq reqInfo = getReqInfo(exceptionFile);
						if(reqInfo!=null){
							Resp resp = HttpHelper.uploadExceptionLog(context, reqInfo, Resp.class);
							Logtrace.debug(TAG, "【上传报错日志】结果:"+resp.getCode());
							if(resp!=null && resp.getCode() == 0){
								exceptionFile.delete();
							}
						}
					}
				}
			}
		}
		
	}
	
	/**
	 * @description: 获取上传日志请求参数
	 *
	 * @param file
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年10月9日 上午1:42:33
	 */
	private UploadExceptionReq getReqInfo(File file){
		if(!file.exists() || !file.canRead()){
			return null;
		}
		UploadExceptionReq info = new UploadExceptionReq();
		try {
			Map<String,String> map = new HashMap<String, String>();
			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);
			String str = null;
			int i = 0;
			String[] arr;
			while((str = br.readLine()) != null) {
				arr = str.split("=");
				map.put(arr[0], arr[1]);
				if(++i>3){
					//读取前4行
					break;
				}
			}
			br.close();
			reader.close();		
			
			info.setPackageName(map.get("packageName"));
			info.setAppName(map.get("appName"));
			info.setVersionName(map.get("versionName"));
			info.setExceptionName(map.get("exceptionName"));
			map.clear();
		} catch (Exception e) {
			e.printStackTrace();
			info = null;
		}
		
		if(TextUtils.isEmpty(info.getExceptionName())){
			info.setExceptionName("Unknown");
		}
		
		if(info != null && !TextUtils.isEmpty(info.getPackageName())
				&& !TextUtils.isEmpty(info.getAppName())
				&& !TextUtils.isEmpty(info.getVersionName())){
			info.setDeviceId(DataHelper.getDeviceInfo().getServerId());
			info.setProductId(DataHelper.getDeviceInfo().getDeviceUniqueId());
			info.setFile(file);
		} else {
			alog.debug("error to get reqInfo:"+file.getAbsolutePath());
			info = null;
		}
		
		return info;
	}
}
