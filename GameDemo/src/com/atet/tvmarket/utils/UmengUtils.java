package com.atet.tvmarket.utils;

import android.content.Context;

import com.atet.tvmarket.app.Configuration;
import com.umeng.analytics.MobclickAgent;

public class UmengUtils {

	// 统计页面时长
	public static final String GAMECENTER_FRAGMENT = "GameCenterFragment"; // 游戏中心
	public static final String GAMECLASSIFY_FRAGMENT = "GameClassifyFragment";// 游戏分类
	public static final String PROMOTION_FRAGMENT = "PromotionFragment"; // 活动
	public static final String MINE_FRAGMENT = "MineFragment"; // 我的

	// 游戏中心
	public static final String PAGE_GAMECENTER_FRAGMENT = "1gamecenter_fragment"; // 游戏中心
	public static final String GAMECENTER_INTERACTION = "1gamecenter_interaction"; // "游戏中心"交互统计
	public static final String GAMECENTER_SINGLE1 = "1gamecenter_single1"; // "单项模块1"统计
	public static final String GAMECENTER_SINGLE2 = "1gamecenter_single2"; // "单项模块2"统计
	public static final String GAMECENTER_SINGLE3 = "1gamecenter_single3"; // "单项模块3"统计
	public static final String GAMECENTER_SINGLE4 = "1gamecenter_single4"; // "单项模块4"统计
	public static final String GAMECENTER_SINGLE5 = "1gamecenter_single5"; // "单项模块5"统计
	public static final String GAMECENTER_SINGLE6 = "1gamecenter_single6"; // "单项模块6"统计
	public static final String GAMECENTER_SINGLE7 = "1gamecenter_single7"; // "单项模块7"统计
	public static final String GAMECENTER_SINGLE8 = "1gamecenter_single8"; // "单项模块8"统计
	public static final String GAMECENTER_QUICKSTART_CLICK = "1gamecenter_quickstart_click"; // "快速开始"
	public static final String GAMECENTER_NEW_CLICK = "1gamecenter_new_click"; // "游戏中心"
	public static final String GAMECENTER_SEARCH_CLICK = "1gamecenter_search_click"; // 统计进入到搜索界面的次数
	public static final String GAMECENTER_SEARCH_RESULT_CLICK = "1gamecenter_search_result_click"; // 统计搜索次数
	public static final String GAMECENTER_MAINPAGEVIDEO_CLICK = "1gamecenter_mainpagevideo_click"; // "首页视频"的所有点击次数
	// public static final String GAMECENTER_MAINPAGEVIDEO_VIDEO_CLICK =
	// "1gamecenter_mainpagevideo_video_click"; // "首页视频"中视频的点击次数
	public static final String GAMECENTER_MAINPAGEVIDEO_UNVIDEO_CLICK = "1gamecenter_mainpagevideo_unvideo_click"; // "首页视频"中非视频的点击次数
	public static final String GAMECENTER_MAINPAGEVIDEO_DETAIL_CLICK = "1gamecenter_mainpagevideo_detail"; // 通过首页视频进入到游戏详情
	public static final String GAMECENTER_VIDEO_COMPLETE = "1gamecenter_video_complete"; // 首页视频播放完成
	public static final String GAMECENTER_VIDEO_UNCOMPLETE = "1gamecenter_video_uncomplete"; // 首页视频未播放完成
	public static final String GAMECENTER_PPT_CLICK = "1gamecenter_ppt_click"; // "轮播"

	// 游戏详情
	public static final String GAMEDETAIL_RECOMMON = "1gamedetail_summary";// "游戏详情"游戏简介
	public static final String GAMEDETAIL_MARK = "1gamedetail_mark";// "游戏详情"精彩评分
	public static final String GAMEDETAIL_SUBMIT = "1gamedetail_submit";// "游戏详情"提交
	public static final String GAMEDETAIL_MORE = "1gamedetail_more";// "游戏详情"更多精彩
	public static final String GAMEDETAIL_DOWNLOAD = "1gamedetail_download";// "游戏详情"下载
	public static final String GAMEDETAIL_INSTALL = "1gamedetail_install";// "游戏详情"安装
	public static final String GAMEDETAIL_START = "1gamedetail_start";// "游戏详情"启动
	public static final String GAMEDETAIL_MORE_DOWNLOAD = "1gamedetail_more_download";// "游戏详情""更多精彩"中的下载

	public static final String PAGE_GAMECLASSIFY_FRAGMENT = "2gameclassify_fragment";// 游戏分类
	public static final String GAMECLASSIFY_INTERACTION = "2gameclassify_interaction";// 游戏分类交互统计
	public static final String GAMECLASSIFY_RANK_MORE_CLICK = "2gameclassify_rank_more_click";// 游戏分类"更多"点击
	public static final String GAMECLASSIFY_RANK_DETAIL_CLICK = "2gameclassify_rank_detail_click";// 游戏排行中"游戏详情"统计
	public static final String GAMECLASSIFY_SPECIAL_CLICK = "2gameclassify_special_click";// "专题游戏点击"统计
	public static final String GAMECLASSIFY_SINGLE1 = "2gameclassify_single1"; // "单项模块1"统计
	public static final String GAMECLASSIFY_SINGLE2 = "2gameclassify_single2"; // "单项模块2"统计
	public static final String GAMECLASSIFY_SINGLE3 = "2gameclassify_single3"; // "单项模块3"统计
	public static final String GAMECLASSIFY_SINGLE4 = "2gameclassify_single4"; // "单项模块4"统计
	public static final String GAMECLASSIFY_SINGLE5 = "2gameclassify_single5"; // "单项模块5"统计
	public static final String GAMECLASSIFY_SINGLE6 = "2gameclassify_single6"; // "单项模块6"统计

	// 活动
	// -------活动专区----------
	public static final String PAGE_PROMOTION_FRAGMENT = "3promotion_fragment"; // 活动
	public static final String PROMOTION_INTERACTION = "3promotion_interaction"; // "活动"总交互
	public static final String PROMOTION_AREA_ENTER = "3promotion_area_enter"; // "活动专区"进入统计
	public static final String PROMOTION_AREA_DETAIL_CLICK = "3promotion_area_detail_click";// "活动详情"进入统计
	public static final String PROMOTION_AREA_JOIN = "3promotion_area_join";// "活动"参与统计
	// -------积分兑换--------
	public static final String PROMOTION_INTEGRAL_EXCHANGE_ENTER = "3promotion_integral_exchange_enter"; // "积分兑换"进入统计
	public static final String PROMOTION_INTEGRAL_GOODS_CLICK = "3promotion_integral_goods_click";// "兑换物品点击"统计
	public static final String PROMOTION_INTEGRAL_EXCHANGE_CLICK = "3promotion_integral_exchange_click";// "兑换"统计
	public static final String PROMOTION_INTEGRAL_EXCHANGE_SUCCESS = "3promotion_integral_exchange_success";// "成功兑换"统计
	// --------图片货物-----------
	public static final String PROMOTION_GOODSPIC_ENTER = "3promotion_goodspic_enter"; // "活动"中"图片货物"进入统计
	// --------领取礼包---------
	public static final String PROMOTION_GIFT_ENTER = "3promotion_gift_enter"; // 进入"礼包"模块统计
	public static final String PROMOTION_GIFT_RECEIVE_CLICK = "3promotion_gift_receive_click";// 统计点击事件
	public static final String PROMOTION_GIFT_RECEIVE_SUCCESS = "3promotion_gift_receive_success";// 统计礼包领取成功信息
	// "我的"页面相关
	public static final String PAGE_MINE_FRAGMENT = "4mine_fragment"; // 我的
	public static final String MINE_INTERACTION = "4mine_interaction"; // "我的"交互次数
	// ---"账户"---
	public static final String MINE_ACCOUNT_FIRST_COME = "4mine_account_first_come"; // 第一次进入到终端
	public static final String MINE_ACCOUNT_FIRST_LOGIN = "4mine_account_first_login"; // 第一次登录
	// 设置
	public static final String PAGE_SETUP_FRAGMENT = "5setup_fragment";// 设置
	public static final String SETUP_FRAGMENT_INTERACTIVE = "5setup_interaction";// "设置"总交互数
	public static final String SETUP_CHILDLOCK_ENTER = "5setup_childlock_enter"; // 进入到童锁界面
	public static final String SETUP_CHILDLOCK_ALL = "5setup_childlock_all"; // 设定所有游戏都能玩
	public static final String SETUP_CHILDLOCK_18 = "5setup_childlock_eight";// 设定适合十八岁以下
	public static final String SETUP_CHILDLOCK_12 = "5setup_childlock_twelve";// 设定适合十二岁以下
	public static final String SETUP_VIDEO_ENTER = "5setup_video_enter"; //
	public static final String SETUP_VIDEO_PLAY = "5setup_video_play"; // 统计播放信息
	public static final String SETUP_VIDEO_COMPLETED = "5setup_video_completed";// 统计播放完成信息
	public static final String SETUP_VIDEO_UNCOMPLETED = "5setup_video_uncompleted";// 统计未播放完成信息
	public static final String SETUP_HANDLE_ENTER = "5setup_handle_enter"; //
	public static final String SETUP_UPDATE_ENTER = "5setup_update_enter"; //
	public static final String SETUP_UPDATE_CLICK = "5setup_update_click"; //
	public static final String SETUP_UPDATE_COMPLETED = "5setup_update_completed"; //

	// 设置自定义事件
	public static void setOnEvent(Context mContext, String mEventId) {
		if (Configuration.ENABLE_STASTIC) {
			MobclickAgent.onEvent(mContext, mEventId);
		}
	}

	public static void onPageStart(String page) {
		if (Configuration.ENABLE_STASTIC) {
			MobclickAgent.onPageStart(page);
		}
	}

	public static void onPageEnd(String page) {
		if (Configuration.ENABLE_STASTIC) {
			MobclickAgent.onPageEnd(page);
		}
	}

	// 用于统计页面时长
	public static void onResume(Context context) {
		if (Configuration.ENABLE_STASTIC) {
			MobclickAgent.onResume(context);
		}
	}

	// 用于统计时长
	public static void onPause(Context context) {
		if (Configuration.ENABLE_STASTIC) {
			MobclickAgent.onPause(context);
		}
	}

	// 在kill进程前调用，用于保存统计的数据
	public static void onKillProcess(Context context) {
		if (Configuration.ENABLE_STASTIC) {
			MobclickAgent.onKillProcess(context);
		}
	}

	// 开启调试模式，true表示设置为普通测试模式，区分"集成测试"
	public static void setDebugMode(boolean debug) {
		if (Configuration.ENABLE_STASTIC) {
			MobclickAgent.setDebugMode(debug);
		}
	}

	// 是否使用默认的全activit的统计时长的方式，true表示使用默认的全activity
	public static void openActivityDurationTrack(boolean isDefault) {
		if (Configuration.ENABLE_STASTIC) {
			MobclickAgent.openActivityDurationTrack(isDefault);
		}
	}

	// 设置数据的发送策略，设置后下次启动将上次统计数据发送到Umeng后台
	public static void updateOnlineConfig(Context context) {
		if (Configuration.ENABLE_STASTIC) {
			MobclickAgent.updateOnlineConfig(context);
		}
	}

	// 设置session的值
	public static void setSessionContinueMillis(long sessionTime) {
		if (Configuration.ENABLE_STASTIC) {
			MobclickAgent.setSessionContinueMillis(sessionTime);
		}
	}

}
