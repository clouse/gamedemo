package com.atet.tvmarket.utils;

import java.lang.reflect.Method;

import com.atet.tvmarket.app.Constant;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;


//import com.tcl.deviceinfo.TDeviceInfo;

public class Utils {
	private static final String TAG = "Utils";
	private static final boolean LOGI = true;
	
    public static int DEVICE_TYPE = DeviceType.DEVICE_TYPE_TCL;	
    public static class DeviceType{
        public static final int DEVICE_TYPE_GAMEBOX=1;
        public static final int DEVICE_TYPE_TCL=2;
        public static final int DEVICE_TYPE_JIUZHOU=3;
        
    } 
                                    

//	/**
//	 * 获取ClientType (型号devicecode)
//	 */
//	public static String getClientType(ContentResolver resolver) {
//		String mRet = "";
//		try {
//			Class clzSqlCommon = Class
//					.forName("com.tcl.device.authentication.SqlCommon");
//			if (LOGI)
//				DebugTool.info(TAG, "Class SqlCommon:" + clzSqlCommon);
//
//			Method mtdSqlCommon$getClientType = clzSqlCommon.getDeclaredMethod(
//					"getDeviceModel", new Class[] { ContentResolver.class });
//			if (LOGI)
//				DebugTool.info(TAG, "Method getClientType:" 
//						+ mtdSqlCommon$getClientType);
//
//			mRet = (String) mtdSqlCommon$getClientType.invoke(clzSqlCommon,
//					resolver);
//			if (LOGI)
//				DebugTool.info(TAG, "ClientType:" + mRet);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		if (mRet == null || mRet.equals("")) {
//		    //TCL ms901板
//		    mRet=com.tcl.util.Utils.getClientType(resolver);
//		}
//		if (mRet == null || mRet.equals("")) {
//		    if(Configuration.IS_CUSTOM_TCL_DEVICE_CODE){
//		        return "TCL-CN-AMM6L-E5590A-3D";
//		    }
//		    
//			mRet = android.os.Build.MODEL;
//			if (LOGI)
//				DebugTool.info(TAG, "model:" + mRet);
//		} else {
//		    if(Configuration.IS_CUSTOM_TCL_DEVICE_CODE 
//		            && !mRet.equals("TCL-CN-AMM6L-E5590A-3D") 
//		            && !mRet.equals("TCL-CN-RT95-E5700A-UDM")){
////		       return "TCL-CN-RT95-E5700A-UDM";
//		       return "TCL-CN-AMM6L-E5590A-3D";
//		    }
//		}
//		if(mRet!=null){
//		    mRet = mRet.replaceAll(" ", "");
//		}
//		return mRet;
//	}

//	/**
//	 * 获取DNumber（deviceId）
//	 */
//	public static String getDNumber(ContentResolver resolver) {
//		String mRet = "";
//		try {
//			Class clzSqlCommon = Class
//					.forName("com.tcl.device.authentication.SqlCommon");
//			if (LOGI)
//				DebugTool.info(TAG, "Class SqlCommon:" + clzSqlCommon);
//
//			Method mtdSqlCommon$getDNumber = clzSqlCommon.getDeclaredMethod(
//					"getDum", new Class[] { ContentResolver.class });
//			if (LOGI)
//				DebugTool.info(TAG, "Method getDNumber:"
//						+ mtdSqlCommon$getDNumber);
//
//			mRet = (String) mtdSqlCommon$getDNumber.invoke(clzSqlCommon,
//					resolver);
//
//			if (LOGI)
//				DebugTool.info(TAG, "DNumber:" + mRet);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		if (mRet == null || mRet.equals("")) {
//		    //TCL ms901板
//		    mRet=com.tcl.util.Utils.getDNumber(resolver);
//		}
//		DebugTool.info(TAG, "deviceId:" + mRet);
//		if (mRet == null) {
//			return "";
//		}
//		return mRet;
//	}
	
    public static String getClientType(ContentResolver resolver) {
        String mRet = "";
//        if(DEVICE_TYPE==DeviceType.DEVICE_TYPE_TCL){
//            try {
//                Class clzSqlCommon = Class.forName("com.tcl.device.authentication.SqlCommon");
//
//                Method mtdSqlCommon$getClientType = clzSqlCommon.getDeclaredMethod(
//                        "getDeviceModel", new Class[] { ContentResolver.class });
//
//                mRet = (String) mtdSqlCommon$getClientType.invoke(clzSqlCommon,
//                        resolver);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            if (mRet == null || mRet.equals("")) {
//                //TCL ms901板
//                mRet=com.tcl.util.Utils.getClientType(resolver);
//            }  
//            
//            try {
//            	if (mRet == null || mRet.equals("")){
//            		mRet=SpiFlashUtils.queryClientType();
//            	}
//            } catch (Throwable e) {
//            	// TODO Auto-generated catch block
//            	e.printStackTrace();
//            }
//            
//            try {
//				if (mRet == null || mRet.equals("")){
//					TDeviceInfo devinfo = TDeviceInfo.getInstance(); 
//					mRet=devinfo.getClientType(devinfo.getProjectID());
//				}
//			} catch (Throwable e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//        }
//        
//        
//        if (mRet == null || mRet.equals("")) {
//            mRet = android.os.Build.MODEL;
//        }
//        if(mRet!=null){
//            mRet = mRet.replaceAll(" ", "");
//        }
//        
//        if(Configuration.IS_USE_DEMO){
//        	return "demo";
//        }
//        
//        if(Configuration.IS_CUSTOM_TCL_DEVICE_CODE){
//        	return "TCL-CN-AMM6L-E5590A-3D";
//        }
        return mRet;
    }
	
    /**
     * 获取DNumber
     */
    public static String getDNumber(Context context,ContentResolver resolver) {
//    	SharedPreferences preferences = context.getSharedPreferences(Constant.PRODUCT_ID, context.MODE_WORLD_READABLE);
//    	Editor editor = preferences.edit();
        String mRet = "";
//        mRet = preferences.getString(Constant.PRODUCT_ID, "");
//        if(!(mRet == null || mRet.equals("")|| mRet.trim().equals("000"))){
//        	return mRet;
//        }
//        mRet = "";
//        if(DEVICE_TYPE==DeviceType.DEVICE_TYPE_TCL){
//            try {
//                Class clzSqlCommon = Class
//                        .forName("com.tcl.device.authentication.SqlCommon");
//                DebugTool.info(TAG, "Class SqlCommon:" + clzSqlCommon);
//
//                Method mtdSqlCommon$getDNumber = clzSqlCommon.getDeclaredMethod(
//                        "getDum", new Class[] { ContentResolver.class });
//                DebugTool.info(TAG, "Method getDNumber:" + mtdSqlCommon$getDNumber);
//
//                mRet = (String) mtdSqlCommon$getDNumber.invoke(clzSqlCommon,
//                        resolver);
//
//                DebugTool.info(TAG, "DNumber:" + mRet);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            try {
//				if (mRet == null || mRet.equals("")) {
//					//TCL ms901板
//					mRet=com.tcl.util.Utils.getDNumber(resolver);
//				}
//			} catch (Throwable e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//            
//            
//            try {
//				if (mRet == null || mRet.equals("")) {
//					TDeviceInfo devinfo = TDeviceInfo.getInstance(); 
//					mRet = devinfo.getDeviceID();
//				}
//			} catch (Throwable e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//        }
//        
////        if(Configuration.IS_USE_DEMO){
////        	return "demo001";
////        }
//        
//        if(mRet == null || mRet.equals("")){
//        	//取wifi的物理地址
//        	mRet = MacAdressUtil.getWifiMacAddress(context);
//        	DebugTool.debug(TAG, "wifi Mac = "+mRet);
//        }
//        
//        if(mRet == null || mRet.equals("")){
//        	//获取以太网的物理地址
//        	mRet = MacAdressUtil.getMacAddress();
//        	DebugTool.debug(TAG, "ethe Mac = "+mRet);
//        }
//        
//        
//        if(mRet == null || mRet.equals("")){
//        	//获取蓝牙的物理地址
//        	mRet = MacAdressUtil.getBluetoothMacAdress();
//        	DebugTool.debug(TAG, "blue Mac = "+mRet);
//        }
//
//        if (mRet == null || mRet.equals("")) {
//            TelephonyManager TelephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE); 
//            mRet = TelephonyMgr.getDeviceId(); 
// 
//        }
//        if (mRet == null || mRet.equals("")) {
//            return "";
//        }
//        if(mRet!=null){
//            mRet = mRet.replaceAll(" ", "");
//            editor.putString(Constant.PRODUCT_ID, mRet);
//            editor.commit();
//        }
        return mRet;
    }
}
