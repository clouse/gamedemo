package com.atet.statistics.utils;

import android.content.Context;

import com.atet.statistics.model.DeviceInfo;
import com.atet.tvmarket.app.UrlConstant;
import com.atet.tvmarket.control.mygame.task.TaskResult;
import com.atet.tvmarket.control.mygame.update.HttpApi;
import com.atet.tvmarket.control.mygame.update.HttpReqParams;
import com.atet.tvmarket.model.DataHelper;
import com.atet.tvmarket.model.SpHelper;


/**
 * 获取device信息的辅助类，主要是对channelId、productId的获取。
 * 
 * @author zhaominglai
 * @date 2014/09/25
 * */
public class DeviceInfoHelper {
//	public static boolean isGetDeviceId = false;// 判断是否获取到本地数据库或者服务器的deviceId
//	public static DeviceInfo deviceInfo;
	
	/**
	 * 从网络上获取设备信息
	 * 
	 * */
	public static void getDeviceInfoFromNet(Context context,String channelId,String deviceCode,String productId,int type)
	{
		
		if (productId.equals("000")){
			return;
		}
		HttpReqParams params = new HttpReqParams();
		params.setChannelId(channelId);
		params.setDeviceCode(deviceCode);
		params.setProductId(productId);
		params.setType(type);
//		DeviceInfoUtil.saveDeviceCode(deviceCode, context);
//		DeviceInfoUtil.saveProductId(productId, context);
		
		TaskResult<DeviceInfo> result = HttpApi.getObject(UrlConstant.SERVER_ID,UrlConstant.SERVER_ID,UrlConstant.SERVER_ID,
				DeviceInfo.class, params.toJsonParam());
		
		if (result.getCode() == TaskResult.DEVICE_ID_NO_DATA) {
			// 无匹配数据
		}
		if (result.getCode() == TaskResult.FAILED) {
			
		} else if (result.getCode() == TaskResult.OK) {
			// 成功获取数据
            DeviceInfo deviceInfo = result.getData();
            SpHelper.put(context, SpHelper.KEY_SERVER_ID, deviceInfo.getDeviceId());
            SpHelper.put(context, SpHelper.KEY_CHANNEL_ID, deviceInfo.getChannelId());
            SpHelper.put(context, SpHelper.KEY_DEVICE_CAPABILITY, deviceInfo.getCapability());
            DataHelper.initDeviceInfo(context);
	
//			DeviceInfoUtil.saveDeviceInfoToSP(context, deviceInfo);
			// PersistentSynUtils.addModel(deviceInfo);// 将数据插入数据库
//			DeviceInfoHelper.deviceInfo = deviceInfo;
//			DeviceInfoHelper.isGetDeviceId = true;
		}
	}
}
