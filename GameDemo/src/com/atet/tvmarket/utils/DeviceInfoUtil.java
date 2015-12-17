package com.atet.tvmarket.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.atet.statistics.model.DeviceInfo;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.model.DataConfig;
import com.atet.tvmarket.model.DataHelper;
import com.atet.tvmarket.model.DeviceHelper;

/**
 * 
 * @ClassName: DeviceInfoUtil
 * @Description:TODO(用来取1、设备型号 2、本机设备productId 3、服务器返回的deviceId
 *                             4、服务器返回的channelId)
 * @author wenfuqiang
 * @date: 2014-8-21 上午11:22:12
 */
public class DeviceInfoUtil {
	private static final String TAG = "DeviceInfoUtil";

	/**
	 * 
	 * @Title: getDeviceId
	 * @Description: TODO(获取设备的产品id)
	 * @param: @return
	 * @return: String
	 * @throws
	 */
	public static String getProductId(Context mContext, ContentResolver resolver) {
		return DeviceHelper.getDeviceUniqueId(BaseApplication.getContext());
	}

	/**
	 * 
	 * @Title: getDeviceId
	 * @Description: TODO(设备从服务器返回的设备id)
	 * @param: @return 
	 * @return: String
	 * @throws
	 */
	public static String getDeviceId(Context mContext) {
//		SharedPreferences preferences = mContext.getSharedPreferences(
//				 "deviceInfo", mContext.MODE_WORLD_READABLE);
//		return preferences.getString("DEVICE_DEVICE_ID", "");
		String serverId =DataHelper.getDeviceInfo().getServerId(); 
		return (serverId == null) ? "" : serverId;
	}

	/**
	 * 
	 * @Title: getDeviceCode
	 * @Description: TODO(获取本机的设备的设备型号)
	 * @param: @return
	 * @return: String
	 * @throws
	 */
	public static String getDeviceCode(ContentResolver resolver) {
		return DeviceHelper.getDeviceModel(BaseApplication.getContext());
	}

	/**
	 * 
	 * @Title: getChannelId
	 * @Description: TODO(服务器返回的channelId)
	 * @param: @return
	 * @return: String
	 * @throws
	 */
//	public static String getChannelId(Context mContext) {
////		SharedPreferences preferences = mContext.getSharedPreferences(
////				"deviceInfo", mContext.MODE_WORLD_READABLE);
////		return preferences.getString("DEVICE_CHANNEL_ID", "0");
//	}

	/**
	 * 
	 * @Title: getDeviceType
	 * @Description: TODO(获取设备的类型)
	 * @param: @param mContext
	 * @param: @return int: 1为TV,2为手机,3为平板
	 * @return: int
	 * @throws
	 */
	public static int getDeviceType(Context mContext) {
//		SharedPreferences preferences = mContext.getSharedPreferences(
//				"deviceInfo", mContext.MODE_WORLD_READABLE);
//		return preferences.getInt("DEVICE_TYPE", 1);
		int type = 1;
		String packageName = mContext.getPackageName();
		if(packageName.equals("com.sxhl.market")){
			type = 2;
		}
		return type;
	}
	
	
	
	/**
	 * 
	 * @Title: saveDeviceInfoToSP   
	 * @Description: TODO(将设备信息保存至sp文件中)   
	 * @param: @param mContext  上下文
	 * @param: @param deviceInfo   设备的信息  
	 * @return: void      
	 * @throws
	 */
//	public static void saveDeviceInfoToSP(Context mContext,DeviceInfo deviceInfo){
//		SharedPreferences preferences = mContext.getSharedPreferences(
//				"deviceInfo", mContext.MODE_WORLD_READABLE);
//		Editor editor = preferences.edit();
//		try {
//			synchronized(deviceInfo){
//				editor.putBoolean("LOGIN_IS_REGISTER", true);
//				if (!(deviceInfo.getChannelId() == null || "".equals(deviceInfo
//						.getChannelId()))) {
//					editor.putString("DEVICE_CHANNEL_ID",
//							deviceInfo.getChannelId());
//				}
//				if (!(deviceInfo.getDeviceId() == null || "".equals(deviceInfo
//						.getDeviceId()))) {
//					editor.putString("DEVICE_DEVICE_ID",
//							deviceInfo.getDeviceId());
//				}
//				if (!(deviceInfo.getType() == null || "".equals(deviceInfo
//						.getType()))) {
//					editor.putInt("DEVICE_TYPE", deviceInfo.getType());
//				}
//				editor.commit();
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally{
//			preferences = null;
//			editor = null;
//		}
//	}
	
	/**
	 * 调用统计模块方法获取atetId
	 * 
	 * @param context
	 * @return 
	 * @throws
	 * @author:LiuQin
	 * @date 2014-10-12 
	 */
	public static boolean fetchAtetId(Context context){
	    return com.atet.statistics.utils.DeviceStatisticsUtils.fetchAtetId(context);
	}
	
	/**
	 * 
	 * @Title: isNeedGetDeviceIdFromNet   
	 * @Description: TODO(判断是否需要从网络重新获取设备的id)   
	 * @param: @return  true  需要从网络获取设备Id
	 * @param： @return false  不需要从网络获取设备Id    
	 * @return: boolean      
	 * @throws
	 */
//	public static boolean isNeedGetDeviceIdFromNet(Context mContext){
//		SharedPreferences preferences = mContext.getSharedPreferences(
//				"deviceInfo", mContext.MODE_WORLD_READABLE);
//		String deviceCode =  preferences.getString(Constant.DEVICE_DEVICE_CODE, "");
//		String productId = preferences.getString(Constant.DEVICE_PRODUCT_ID, "");
//		RecodUtil.appendRecord(mContext, "deviceCode 文件值 = "+deviceCode);
//		RecodUtil.appendRecord(mContext, "productId 文件值 = "+productId);
//		DebugTool.debug(TAG, "deviceCode="+deviceCode);
//		DebugTool.debug(TAG, "nowDeviceCode ="+Utils.getClientType(mContext.getContentResolver()));
//		if(deviceCode.equals("")
//				||null == deviceCode
//				||!(deviceCode.equals(Utils.getClientType(mContext.getContentResolver())))){
//			//如果存进文件的deviceCode 与当前获取的deviceCode不一样，或者deviceCode 等于空，则返回true
//			DebugTool.debug(TAG, "isNeedGetDeviceIdFromNet "+true);
//			RecodUtil.appendRecord(mContext, "deviceCode 当前时间取的值 = "+Utils.getClientType(mContext.getContentResolver()));
//			RecodUtil.appendRecord(mContext, "存进文件的deviceCode 与当前获取的deviceCode不一样，或者deviceCode 等于空；");
//			return true;
//		}
//		
//		if(productId.equals("")
//				||null == productId
//				||!(productId.trim().equals(Utils.getDNumber(mContext,mContext.getContentResolver())))){
//			DebugTool.debug(TAG, "isNeedGetDeviceIdFromNet "+true);
//			RecodUtil.appendRecord(mContext, "productId 当前时间取的值 = "+Utils.getDNumber(mContext,mContext.getContentResolver()));
//			RecodUtil.appendRecord(mContext, "productId为空或者两个取的productId不相等  ，productId="+Utils.getDNumber(mContext,mContext.getContentResolver()));
//			return true;
//		}
//		DebugTool.debug(TAG, "isNeedGetDeviceIdFromNet "+false);
//		RecodUtil.appendRecord(mContext, "deviceCode 以及productId 合法，不需要重新取deviceId");
//		return false;
//	}
	
	
	/**
	 * 
	 * @Title: saveDeviceCode   
	 * @Description: TODO(保存设备的deviceCode到文件当中)   
	 * @param: @param deviceCode      
	 * @return: void      
	 * @throws
	 */
//	 public static void saveDeviceCode(String deviceCode,Context mContext){
//		 SharedPreferences preferences = mContext.getSharedPreferences(
//					"deviceInfo", mContext.MODE_WORLD_READABLE);
//			try {
//				Editor editor = preferences.edit();
//				if (!(deviceCode == null || "".equals(deviceCode))) {
//					editor.putString("DEVICE_DEVICE_CODE", deviceCode);
//				}
//				editor.commit();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	 }
	 
	 
	 /**
	  * 
	  * @Title: saveProductId   
	  * @Description: TODO()   
	  * @param: @param productId
	  * @param: @param mContext      
	  * @return: void      
	  * @throws
	  */
//	 public static void saveProductId(String productId,Context mContext){
//		 SharedPreferences preferences = mContext.getSharedPreferences(
//					"deviceInfo", mContext.MODE_WORLD_READABLE);
//			try {
//				Editor editor = preferences.edit();
//				if (!(productId == null || "".equals(productId))) {
//					editor.putString("DEVICE_PRODUCT_ID", productId);
//				}
//				editor.commit();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	 }
	
}
