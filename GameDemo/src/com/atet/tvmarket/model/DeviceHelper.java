package com.atet.tvmarket.model;

import java.lang.reflect.Method;

import com.atet.statistics.utils.DeviceStatisticsUtils;
import com.atet.tvmarket.app.Configuration;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.app.Constant.DEVICE_TYPE;
import com.atet.tvmarket.utils.MacAdressUtil;
import com.tcl.deviceinfo.TDeviceInfo;
import com.tvos.common.SpiFlashUtils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class DeviceHelper {
	/**
	 * @description: 获取设备型号
	 *
	 * @return
	 * @author: LiuQin
	 * @date: 2015年6月24日 下午5:42:08
	 */
	public static String getDeviceModel(Context context) {
		String result = null;
		if(Configuration.IS_CUSTOM_TCL_DEVICE_CODE){
//			result = "ROWA-CN-RT95-UD1000";
			result = "TCL-CN-MS918-E6800A-UDM";
		} else {
			result = getClientType(context.getContentResolver());
		}
		return result;
	}

	/**
	 * @description: 获取设备的唯一id号
	 *
	 * @return
	 * @author: LiuQin
	 * @date: 2015年6月24日 下午5:42:18
	 */
	public static String getDeviceUniqueId(Context context) {
		String result = null;
		if(Configuration.IS_CUSTOM_DEVICE_UNIQUE_ID){
			result = "123456";
		} else {
			result = getDNumber(context, context.getContentResolver());
		}
		return result;
	}

	/**
	 * @description: 获取设备类型
	 *
	 * @param context
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年6月24日 下午6:32:05
	 */
	public static int getDeviceType(Context context) {
		int deviceType = DEVICE_TYPE.sCustomDeviceType;
		if (deviceType == DEVICE_TYPE.TYPE_UNKNOWN) {
			if (DEVICE_TYPE.PHONE_MARKET_PACKAGE_NAME.equals(context
					.getPackageName())) {
				deviceType = DEVICE_TYPE.TYPE_PHONE;
			} else {
				deviceType = DEVICE_TYPE.TYPE_TV;
			}
			DEVICE_TYPE.sCustomDeviceType = deviceType;
		}
		return deviceType;
	}
	
    public static int BRAND_TYPE = DeviceType.DEVICE_TYPE_TCL;	
    public static class DeviceType{
        public static final int DEVICE_TYPE_GAMEBOX=1;
        public static final int DEVICE_TYPE_TCL=2;
        public static final int DEVICE_TYPE_JIUZHOU=3;
        
    } 
	
	
    public static String getClientType(ContentResolver resolver) {
        String mRet = "";
        if(BRAND_TYPE==DeviceType.DEVICE_TYPE_TCL){
            try {
                Class clzSqlCommon = Class.forName("com.tcl.device.authentication.SqlCommon");

                Method mtdSqlCommon$getClientType = clzSqlCommon.getDeclaredMethod(
                        "getDeviceModel", new Class[] { ContentResolver.class });

                mRet = (String) mtdSqlCommon$getClientType.invoke(clzSqlCommon,
                        resolver);
            } catch (Exception e) {
//                e.printStackTrace();
            }
            if (mRet == null || mRet.equals("")) {
                //TCL ms901板
                mRet=com.tcl.util.Utils.getClientType(resolver);
            }  
            
            try {
            	if (mRet == null || mRet.equals("")){
            		mRet=SpiFlashUtils.queryClientType();
            	}
            } catch (Throwable e) {
            	// TODO Auto-generated catch block
//            	e.printStackTrace();
            }
            
            try {
				if (mRet == null || mRet.equals("")){
					TDeviceInfo devinfo = TDeviceInfo.getInstance(); 
					mRet=devinfo.getClientType(devinfo.getProjectID());
				}
			} catch (Throwable e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
        }
        
    	if(mRet == null || mRet.equals("")){
    		//创维的获取设备型号的方法
    		mRet = android.os.SystemProperties.get("ro.build.skymodel");
    	}
        
        if (mRet == null || mRet.equals("")) {
            mRet = android.os.Build.MODEL;
        }
        if(mRet!=null){
            mRet = mRet.replaceAll(" ", "");
        }
        
        if(Configuration.IS_CUSTOM_TCL_DEVICE_CODE){
        	return "TCL-CN-AMM6L-E5590A-3D";
        }
        return mRet;
    }
	
    /**
     * 获取DNumber
     */
    public static String getDNumber(Context context,ContentResolver resolver) {
        String mRet = "";
        if(BRAND_TYPE==DeviceType.DEVICE_TYPE_TCL){
            try {
                Class clzSqlCommon = Class
                        .forName("com.tcl.device.authentication.SqlCommon");

                Method mtdSqlCommon$getDNumber = clzSqlCommon.getDeclaredMethod(
                        "getDum", new Class[] { ContentResolver.class });

                mRet = (String) mtdSqlCommon$getDNumber.invoke(clzSqlCommon,
                        resolver);

            } catch (Exception e) {
//                e.printStackTrace();
            }
            try {
				if (mRet == null || mRet.equals("")) {
					//TCL ms901板
					mRet=com.tcl.util.Utils.getDNumber(resolver);
				}
			} catch (Throwable e1) {
				// TODO Auto-generated catch block
//				e1.printStackTrace();
			}
            
            
            try {
				if (mRet == null || mRet.equals("")) {
					TDeviceInfo devinfo = TDeviceInfo.getInstance(); 
					mRet = devinfo.getDeviceID();
				}
			} catch (Throwable e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
        }
        
        if(mRet == null || mRet.equals("")){
        	//取wifi的物理地址
        	mRet = MacAdressUtil.getWifiMacAddress(context);
        }
        
        if(mRet == null || mRet.equals("")){
        	//获取以太网的物理地址
        	mRet = MacAdressUtil.getMacAddress();
        }
        
        
        if(mRet == null || mRet.equals("")){
        	//获取蓝牙的物理地址
        	mRet = MacAdressUtil.getBluetoothMacAdress();
        }

        if (mRet == null || mRet.equals("")) {
            TelephonyManager TelephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE); 
            mRet = TelephonyMgr.getDeviceId(); 
        }
        if (mRet == null || mRet.equals("")) {
            return "";
        }
        return mRet;
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
//	public static void saveTvDeviceInfoToSP(Context mContext,DeviceInfo deviceInfo){
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
//				if (!(deviceInfo.getServerId() == null || "".equals(deviceInfo
//						.getServerId()))) {
//					editor.putString("DEVICE_DEVICE_ID",
//							deviceInfo.getServerId());
//				}
////				if (!(deviceInfo.getType() == null || "".equals(deviceInfo
////						.getType()))) {
////					editor.putInt("DEVICE_TYPE", deviceInfo.getType());
////				}
//				editor.putInt("DEVICE_TYPE", DEVICE_TYPE.TYPE_TV);
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
	 * @description: 获取旧版本保存的设备信息
	 * 
	 * @throws: 
	 * @author: LiuQin
	 */
	public static void initDeviceInfoFromOrderVersion(Context context) {
		Boolean result = false;
		result = (Boolean)SpHelper.get(context, SpHelper.KEY_INIT_DEVICEINFO_APP, result);
		if(result){
			return;
		}
		SharedPreferences preferences = context.getSharedPreferences(Constant.DEVICE_INFO_SP, Context.MODE_WORLD_READABLE);
		
		String deviceId = preferences.getString(Constant.DEVICE_DEVICE_ID, "");
		if (!TextUtils.isEmpty(deviceId)) {
			String channelId = preferences.getString(Constant.DEVICE_CHANNEL_ID, "0");
			
			SpHelper.put(context, SpHelper.KEY_SERVER_ID, deviceId);
			SpHelper.put(context, SpHelper.KEY_CHANNEL_ID, channelId);
		}
		String atetId = DeviceStatisticsUtils.getAtetId(context);
		if (!atetId.equals("1") && !TextUtils.isEmpty(atetId)) {
			SpHelper.put(context, SpHelper.KEY_ATET_ID, atetId);
		}
		
		SpHelper.put(context, SpHelper.KEY_INIT_DEVICEINFO_APP, true);
	}
}
