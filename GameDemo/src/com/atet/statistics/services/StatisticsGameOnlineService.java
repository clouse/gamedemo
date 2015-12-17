package com.atet.statistics.services;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothInputDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.input.InputManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.text.TextUtils;
import android.view.InputDevice;

import com.atet.statistics.model.GameOnlineInfo;
import com.atet.statistics.model.GamePadInfo;
import com.atet.statistics.model.JoystickInfo;
import com.atet.statistics.utils.DeviceStatisticsUtils;
import com.atet.statistics.utils.LOG;
import com.atet.statistics.utils.PackageInfoUtils;
import com.atet.statistics.utils.StatisticsRecordTestUtils;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.database.PersistentSynUtils;
import com.atet.tvmarket.model.usertask.UserTaskDaoHelper;
import com.atet.tvmarket.utils.DebugTool;
import com.tianci.media.api.Log;


/**
 * 用于统计游戏在线时长的服务，每30秒钟为一次
 * 
 * 
 * LOG　001：添加了手柄连接统计的功能。
 *          主要处理在android 4.4及4。4版本以下的两种情况。
 *          在4。4上用UsbMananger获取到的VendorId与InputeDevice中的VendorId相比对，
 *          让通过BluetoothAdapter获取到的mac地址赋值给InputDevice中获取到的蓝牙手柄上的地址。
 *          
 * LOG 002:　后经测试发现蓝牙手柄只能通过BluetoothAdapter获取到。无线手柄只能通过InputeDevice获取到，所以Usbmanager就不起作用了。
 * 
 * */
@SuppressLint("NewApi")
public class StatisticsGameOnlineService extends IntentService {

	private static String TAG = "StatisticsGameOnlineService";
	
	private static GameOnlineInfo mLastAppInfo;
	
	private static String mLastPackageName;
	
	private static Long mAppTimeStep;
	
	private static List<String> mHomeList;
	
	private InputManager mIM;
	
	private BluetoothInputDevice blueDevice;
	
	private BluetoothAdapter adapter;
	
	private UsbManager usbManager;
	
	//Bj-8
	public static final int BLUETOOTH_DEVICE_CLASS = 1480; // 用来表示蓝牙设备
	//Bj-9
	public static final int ATETBJ9_DEVICE_CLASS = 1288;
	
	public static int LAST_JOYSTICK_COUNT = 0;
	
	private HashMap<String, UsbDevice> usbMaps;
	
	private Set<Integer> vendorIdSet;
	
	private static final int GAMEPAD_MODE = 0;
	
	private static final int NO_GAMEPAD_MODE = 1;
	
	public StatisticsGameOnlineService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public StatisticsGameOnlineService()
	{
		this(TAG);
	}

	
	
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		
			runAppStatistic();
	}

	/**
	 * 统计游戏的在线时长
	 * 
	 * */
	private void runAppStatistic() {
		// TODO Auto-generated method stub
		
		/**
		 * 本服务只统计非平台、非桌面类的游戏或者应用。
		 * 
		 * 1、不统计平台本身
		 * 2、不统计桌面内应用
		 * 
		 * */
		
		String runningPackageName = PackageInfoUtils.getRunningPackageName(this);
		String platFormName = getPackageName();
		
		if (adapter == null){
			adapter = BluetoothAdapter.getDefaultAdapter();
		}
		
		if (blueDevice == null && adapter != null && adapter.isEnabled()){
			adapter.getProfileProxy(this, new InputDeviceServiceListener(), 4);
		}
		
		
		if (Build.VERSION.SDK_INT >= 16){
			
			if (mIM == null){
				mIM = (InputManager) getSystemService(Context.INPUT_SERVICE);
			}
		}
		
		
		List<JoystickInfo> joyList = null;
		
		
		if(mLastPackageName == null){
			/**
			 * 设备开机后，第一次运行该服务，此时lastPackageName为空
			 * */
			mLastPackageName = runningPackageName;
			
			//如果不是平台或者桌面类的应用在运行，则新增一条记录
			if(DeviceStatisticsUtils.isAtetGame(runningPackageName)){
//				if(!mLastPackageName.equals(platFormName) && !isHomeApp(runningPackageName)){
				addInfoWhenAppBoot(runningPackageName);
				LOG.E(TAG,"sisuation 1");
				return;
			}
			
			LOG.E(TAG,"sisuation 2");
			
			
		}else if (!DeviceStatisticsUtils.isAtetGame(runningPackageName)) {
			/**
			 * 如果当前运行的应用是桌面应用或者是平台本身，则将mAppTimeStep置０
			 * */
			mLastPackageName = runningPackageName;
			mAppTimeStep = 0L;
			
			LOG.E(TAG,"sisuation 3");
			return;
		} else if (mLastPackageName.equals(runningPackageName) && DeviceStatisticsUtils.isAtetGame(runningPackageName)){
			
			/**
			 * 游戏连续运行的情况
			 * 
			 * 游戏连续运行就需要更新数据库中的记录，而不是新增记录。
			 * 
			 * 更新的信息主要是开始时间、运行时长、结束时间。
			 * 
			 * 开始时间要用结束时间-运行时长来动态更新
			 * */
			
			if(mLastAppInfo != null) {
				
				mAppTimeStep++;
				
				long curTime = System.currentTimeMillis();
				long duration = mAppTimeStep*30;
				
				/**
				 * 如果不是开始时间与结束时间不是同一天，则分割时间
				 * */
				if(DeviceStatisticsUtils.ifTwoTimeStampBetweenOneDay(mLastAppInfo.getStartTime(), curTime))
				{
					long endTime = DeviceStatisticsUtils.getTimeToday(DeviceStatisticsUtils.timeToDay(curTime))- 1000;
					long durantion1 = (curTime - endTime)/1000;
					mLastAppInfo.setEndTime(endTime);
					mLastAppInfo.setLongTime(duration - durantion1);
					mLastAppInfo.setStartTime(curTime - duration*1000);
					if (checkDatabaseIfCanUpdate(mLastAppInfo.getAutoIncrementId())){
						/**
						 * 如果数据库中可以更新当前记录，则执行更新动作
						 * */
						PersistentSynUtils.update(mLastAppInfo);
					}
					else
					{
						/**
						 * 如果数据库中不能更新记录，则新建记录
						 * 
						 * */
						addInfoWhenAppBoot(runningPackageName);
					}
					
					addInfoAtOneDayFirst(runningPackageName,endTime + 1000);
					
					saveGameOnlineUsertaskRecord(BaseApplication.getContext(), runningPackageName, (int)((duration-durantion1)/60));
					return;
				}
				
				
				mLastAppInfo.setEndTime(curTime);
				mLastAppInfo.setLongTime(duration);
				mLastAppInfo.setStartTime(curTime - duration*1000);
				
				List<GamePadInfo> blueMacList = getBluetoothGamePad();
				
				if (Build.VERSION.SDK_INT >= 16 ){
					//如果当前有手柄连接
					
					joyList = getJoysticks();
					
					
					
					/**
					 * 1。先判断有没有蓝牙手柄连接，如果有的话，将蓝牙手柄的mac地址赋值给前两个手柄，虽然不怎么准确，但考虑的实际硬件信息。只得这样。
					 * 2。将信息用,拼接起来，然后去掉最后一个,
					 * 
					 * 
					 * */
					StringBuilder sbMac = new StringBuilder();
					StringBuilder sbProId = new StringBuilder();
					StringBuilder sbFacId = new StringBuilder();
					StringBuilder sbHandleName = new StringBuilder();
					
					for (int i = 0;i < joyList.size();i++){
						JoystickInfo info1 = joyList.get(i);
						
						if(i < blueMacList.size()){
							GamePadInfo info2 = blueMacList.get(i);
							sbMac.append(info2.getMac()+",");
							sbHandleName.append(info2.getName()+",");
						}else{
							sbMac.append(info1.getMac()+",");
							sbHandleName.append(info1.getHandlerName()+",");
						}
						sbProId.append(info1.getProductId()+",");
						sbFacId.append(info1.getManufactorId()+",");
					}
					
					String infoMac = null;
					if (sbMac.length() > 0){
						infoMac = sbMac.toString();
					}
					String newMac = null;
					if (infoMac != null && !TextUtils.isEmpty(infoMac)){
						int macIndex = infoMac.lastIndexOf(',');
						 newMac =infoMac.substring(0, macIndex);
					}else{
						newMac = "no";
					}
					String infoFac = null;
					if (sbFacId.length() > 0){
						infoFac = sbFacId.toString();
					}
					String newFac = null;
					if (infoFac != null && !TextUtils.isEmpty(infoFac)){
						int facIndex = infoFac.lastIndexOf(',');
						newFac =infoFac.substring(0, facIndex);
					}else{
						newFac = "no";
					}
					String infoPro = null;
					if (sbFacId.length() > 0){
						infoPro = sbProId.toString();
					}
					String newPro = null;
					if (infoPro != null && !TextUtils.isEmpty(infoPro)){
						int proIndex = infoPro.lastIndexOf(',');
						newPro =infoPro.substring(0, proIndex);
					}else{
						newPro = "no";
					}
					
					String infoName = null;
					if (sbHandleName.length() > 0){
						infoName = sbHandleName.toString();
					}
					String newHandleName = null;
					if (infoName != null && !TextUtils.isEmpty(infoName)){
						int proIndex = infoName.lastIndexOf(',');
						newHandleName =infoName.substring(0, proIndex);
					}else{
						newHandleName = "no";
					}
					
					if (LAST_JOYSTICK_COUNT < joyList.size()){
						mLastAppInfo.setHandleMac(newMac);
						mLastAppInfo.setHandleFactoryId(newFac);
						mLastAppInfo.setHandleProductId(newPro);
						mLastAppInfo.setHandleName(newHandleName);
					}
					if (mLastAppInfo.getIsUseHandle() == NO_GAMEPAD_MODE && joyList.size() == 0){
						
						mLastAppInfo.setIsUseHandle(NO_GAMEPAD_MODE);
					}else{
						mLastAppInfo.setIsUseHandle(GAMEPAD_MODE);
						
					}
					LAST_JOYSTICK_COUNT = joyList.size();
					
				}
				
				if (checkDatabaseIfCanUpdate(mLastAppInfo.getAutoIncrementId()))
				{
					/**
					 * 如果数据库中可以更新当前记录，则执行更新动作
					 * */
					PersistentSynUtils.update(mLastAppInfo);
					updateInfoLog(runningPackageName, mLastAppInfo.getGameName(), curTime - duration*1000, duration, curTime);
				}
				else
				{
					/**
					 * 如果数据库中不能更新记录，则新建记录
					 * 
					 * */
					addInfoWhenAppBoot(runningPackageName);
					//add by zml 添加log日志，当更新数据不正常时会新建记录
					addNewInfoLogDetail(runningPackageName);
					
				}
				saveGameOnlineUsertaskRecord(BaseApplication.getContext(), runningPackageName, (int)(duration/60));
//				LOG.E(TAG,"sisuation 4");
			}
		}
		else if(!mLastPackageName.equals(runningPackageName))
		{
			/**
			 * 上一次检测到的应用不是当前运行的应用。
			 * 
			 * 1、上次是其他应用，这次是平台。
			 * 2、上次是平台，这次是其他应用　。
			 * 
			 * */
			
			
				LOG.E(TAG,"sisuation 5 lastpkgname: "+mLastPackageName+"  runningpkgname:"+runningPackageName);
				addInfoWhenAppBoot(runningPackageName);
				mLastPackageName = runningPackageName;
				return;
			
		}
	}

	private void addInfoWhenAppBoot(String packageName) {
		// TODO Auto-generated method stub
		long curruntTime = System.currentTimeMillis();
		GameOnlineInfo info = new GameOnlineInfo();
		checkDataBaseIsOverwrite();
		mAppTimeStep = 0L;
		info.setUserId(DeviceStatisticsUtils.getUserId(this));
		info.setGameId(DeviceStatisticsUtils.getGameId(packageName));
		info.setGameType(DeviceStatisticsUtils.getGameType(this, packageName));
		info.setGameName(DeviceStatisticsUtils.getGameName(this,packageName));
		info.setVersionCode(DeviceStatisticsUtils.getVersionName(this, packageName));
		info.setPackageName(packageName);
		info.setCopyRight(DeviceStatisticsUtils.getCopyRight(packageName));
		info.setCpId(DeviceStatisticsUtils.getCpId(packageName));
		info.setStartTime(curruntTime);
		info.setLongTime(mAppTimeStep);
		info.setEndTime(curruntTime);
		
		if (Build.VERSION.SDK_INT < 16){
			
			info.setHandleMac("no");
			info.setHandleFactoryId("no");
			info.setHandleProductId("no");
			info.setHandleName("no");
			info.setIsUseHandle(NO_GAMEPAD_MODE);
			
			long id = PersistentSynUtils.addModel(info);
			
			if (id != -1)
			{
				mLastAppInfo = info;
				mLastAppInfo.setAutoIncrementId(id+"");
				mLastPackageName = packageName;
				addNewInfoLog(packageName, info.getGameName(), curruntTime, curruntTime);
			}
			
			return;
		}
		
		List<JoystickInfo> joyList = getJoysticks();
		List<GamePadInfo> blueMacList = getBluetoothGamePad();
		
		if (joyList.size()> 0){
			//如果当前有手柄连接
			
			/**
			 * 1。先判断有没有蓝牙手柄连接，如果有的话，将蓝牙手柄的mac地址赋值给前两个手柄，虽然不怎么准确，但考虑的实际硬件信息。只得这样。
			 * 2。将信息用,拼接起来，然后去掉最后一个,
			 * 
			 * 
			 * */
			StringBuilder sbMac = new StringBuilder();
			StringBuilder sbProId = new StringBuilder();
			StringBuilder sbFacId = new StringBuilder();
			StringBuilder sbHandleName = new StringBuilder();
			
			for (int i = 0;i < joyList.size();i++){
				JoystickInfo info1 = joyList.get(i);
				
				if(i < blueMacList.size()){
					GamePadInfo info2 = blueMacList.get(i);
					sbMac.append(info2.getMac()+",");
					sbHandleName.append(info2.getName()+",");
				}else{
					sbMac.append(info1.getMac()+",");
					sbHandleName.append(info1.getHandlerName()+",");
				}
				sbProId.append(info1.getProductId()+",");
				sbFacId.append(info1.getManufactorId()+",");
			}
			
			String infoMac = null;
			if (sbMac.length() > 0){
				infoMac = sbMac.toString();
			}
			String newMac = null;
			if (infoMac != null && !TextUtils.isEmpty(infoMac)){
				int macIndex = infoMac.lastIndexOf(',');
				 newMac =infoMac.substring(0, macIndex);
			}else{
				newMac = "no";
			}
			String infoFac = null;
			if (sbFacId.length() > 0){
				infoFac = sbFacId.toString();
			}
			String newFac = null;
			if (infoFac != null && !TextUtils.isEmpty(infoFac)){
				int facIndex = infoFac.lastIndexOf(',');
				newFac =infoFac.substring(0, facIndex);
			}else{
				newFac = "no";
			}
			String infoPro = null;
			if (sbFacId.length() > 0){
				infoPro = sbProId.toString();
			}
			String newPro = null;
			if (infoPro != null && !TextUtils.isEmpty(infoPro)){
				int proIndex = infoPro.lastIndexOf(',');
				newPro =infoPro.substring(0, proIndex);
			}else{
				newPro = "no";
			}
			
			String infoName = null;
			if (sbHandleName.length() > 0){
				infoName = sbHandleName.toString();
			}
			String newHandleName = null;
			if (infoName != null && !TextUtils.isEmpty(infoName)){
				int proIndex = infoName.lastIndexOf(',');
				newHandleName =infoName.substring(0, proIndex);
			}else{
				newHandleName = "no";
			}
			
			info.setHandleMac(newMac);
			info.setHandleFactoryId(newFac);
			info.setHandleProductId(newPro);
			info.setIsUseHandle(GAMEPAD_MODE);
			info.setHandleName(newHandleName);
			LAST_JOYSTICK_COUNT = joyList.size();
			
			
		}else{
			//当前无手柄连接
			LOG.E("test joystick ","no joysticks");
			
			info.setHandleMac("no");
			info.setHandleFactoryId("no");
			info.setHandleProductId("no");
			info.setHandleName("no");
			info.setIsUseHandle(NO_GAMEPAD_MODE);
			LAST_JOYSTICK_COUNT = 0;
		}
		
		//有蓝牙连接或者是2。4G无线连接
		/*if(blueMacList.size() > 0 || joyList.size() > 0){
			StringBuilder sbMac = new StringBuilder();
			StringBuilder sbProId = new StringBuilder();
			StringBuilder sbFacId = new StringBuilder();
			
			for (int i = 0;i < blueMacList.size();i++){
				sbMac.append(blueMacList.get(i).getMac()+",");
				String deviceName = blueMacList.get(i).getName();
				if (deviceName == null || TextUtils.isEmpty(deviceName)){
					sbProId.append("GAMEPAD"+",");
				}else{
					sbProId.append(deviceName+",");
				}
				sbFacId.append("ATET"+",");
			}
			
			if(joyList.size() > 0){
				
				for (int i = 0;i < joyList.size() / 2;i++){
					JoystickInfo info1=joyList.get(i);
					
					if (Build.VERSION.SDK_INT < 19){
						//如果版本号小于4。4则将蓝牙手柄list中的mac地址，赋值给joysticklist中前面的手柄。
						if (i < blueMacList.size()){
							sbMac.append(blueMacList.get(i++)+",");
						}else{
							sbMac.append(info1.getMac()+",");
						}
						
					}else{
						//4.4的版本可以比较usbmanager获取的vendorID来识别是不是2.4g手柄。
						if (j < blueMacList.size()){
							//如果当前的手柄不是2.4G手柄，就当成蓝牙手柄，并将mac地址赋值给手柄
							
							//可以获取到usb设备信息的情况
							if (vendorIdSet != null && vendorIdSet.size() > 0){
								if(!vendorIdSet.contains(info1.getManufactorId())){
									sbMac.append(blueMacList.get(j++)+",");
								}else{
									//如果是2.4G手柄就直接赋值
									sbMac.append(info1.getMac()+",");
								}
							}else{
								//获取不到usb设备信息处理方式同4.4以下版本一样
								sbMac.append(blueMacList.get(j++)+",");
							}
							
							//sbMac.append(blueMacList.get(i++)+",");
						}else{
							
							//非蓝牙手柄
							sbMac.append(info1.getMac()+",");
						}
						
					}
					
					sbMac.append(info1.getMac()+",");
					sbProId.append(info1.getProductId()+",");
					sbFacId.append(info1.getManufactorId()+",");
				}
			}
			
			String infoMac = sbMac.toString();
			String newMac = null;
			if (infoMac != null){
				int macIndex = infoMac.lastIndexOf(',');
				newMac =infoMac.substring(0, macIndex);
			}else{
				newMac = "no";
			}
			String infoFac = sbFacId.toString();
			String newFac = null;
			if (infoFac != null){
				int facIndex = infoFac.lastIndexOf(',');
				newFac =infoFac.substring(0, facIndex);
			}else{
				newFac = "no";
			}
			String infoPro = sbProId.toString();
			String newPro = null;
			if (infoPro != null){
				int proIndex = infoPro.lastIndexOf(',');
				newPro =infoPro.substring(0, proIndex);
			}else{
				newPro = "no";
			}
			
			info.setHandleMac(newMac);
			info.setHandleFactoryId(newFac);
			info.setHandleProductId(newPro);
			info.setIsUseHandle(GAMEPAD_MODE);
			LAST_JOYSTICK_COUNT = joyList.size()/2 + blueMacList.size();
		}else{
			
			LOG.E("test joystick ","no joysticks");
			//无手柄连接的情况
			info.setHandleMac("no");
			info.setHandleFactoryId("no");
			info.setHandleProductId("no");
			info.setIsUseHandle(NO_GAMEPAD_MODE);
			LAST_JOYSTICK_COUNT = 0;
		}*/
		
		long id = PersistentSynUtils.addModel(info);
		
		if (id != -1)
		{
			mLastAppInfo = info;
			mLastAppInfo.setAutoIncrementId(id+"");
			mLastPackageName = packageName;
			addNewInfoLog(packageName, info.getGameName(), curruntTime, curruntTime);
		}
	}
	
	
	private void addInfoAtOneDayFirst(String packageName,long startTime) {
		// TODO Auto-generated method stub
		long curruntTime = System.currentTimeMillis();
		GameOnlineInfo info = new GameOnlineInfo();
		checkDataBaseIsOverwrite();
		mAppTimeStep = 0L;
		info.setUserId(DeviceStatisticsUtils.getUserId(this));
		info.setGameId(DeviceStatisticsUtils.getGameId(packageName));
		info.setGameType(DeviceStatisticsUtils.getGameType(this, packageName));
		info.setGameName(DeviceStatisticsUtils.getGameName(this,packageName));
		info.setVersionCode(DeviceStatisticsUtils.getVersionName(this, packageName));
		info.setPackageName(packageName);
		info.setCopyRight(DeviceStatisticsUtils.getCopyRight(packageName));
		info.setCpId(DeviceStatisticsUtils.getCpId(packageName));
		info.setStartTime(startTime);
		info.setLongTime((curruntTime-startTime)/1000);
		info.setEndTime(curruntTime);
		
		if (Build.VERSION.SDK_INT < 16){
			
			info.setHandleMac("no");
			info.setHandleFactoryId("no");
			info.setHandleProductId("no");
			info.setIsUseHandle(NO_GAMEPAD_MODE);
			
			long id = PersistentSynUtils.addModel(info);
			
			if (id != -1)
			{
				mLastAppInfo = info;
				mLastAppInfo.setAutoIncrementId(id+"");
				mLastPackageName = packageName;
				addNewInfoLog(packageName, info.getGameName(), curruntTime, curruntTime);
			}
			
			return;
		}
		
		List<JoystickInfo> joyList = getJoysticks();
		List<GamePadInfo> blueMacList = getBluetoothGamePad();
		
		if (joyList.size()> 0){
			//如果当前有手柄连接
			
			/**
			 * 1。先判断有没有蓝牙手柄连接，如果有的话，将蓝牙手柄的mac地址赋值给前两个手柄，虽然不怎么准确，但考虑的实际硬件信息。只得这样。
			 * 2。将信息用,拼接起来，然后去掉最后一个,
			 * 
			 * 
			 * */
			StringBuilder sbMac = new StringBuilder();
			StringBuilder sbProId = new StringBuilder();
			StringBuilder sbFacId = new StringBuilder();
			StringBuilder sbHandleName = new StringBuilder();
			
			for (int i = 0;i < joyList.size();i++){
				JoystickInfo info1 = joyList.get(i);
				
				if(i < blueMacList.size()){
					GamePadInfo info2 = blueMacList.get(i);
					sbMac.append(info2.getMac()+",");
					sbHandleName.append(info2.getName()+",");
				}else{
					sbMac.append(info1.getMac()+",");
					sbHandleName.append(info1.getHandlerName()+",");
				}
				sbProId.append(info1.getProductId()+",");
				sbFacId.append(info1.getManufactorId()+",");
			}
			
			String infoMac = null;
			if (sbMac.length() > 0){
				infoMac = sbMac.toString();
			}
			String newMac = null;
			if (infoMac != null && !TextUtils.isEmpty(infoMac)){
				int macIndex = infoMac.lastIndexOf(',');
				 newMac =infoMac.substring(0, macIndex);
			}else{
				newMac = "no";
			}
			String infoFac = null;
			if (sbFacId.length() > 0){
				infoFac = sbFacId.toString();
			}
			String newFac = null;
			if (infoFac != null && !TextUtils.isEmpty(infoFac)){
				int facIndex = infoFac.lastIndexOf(',');
				newFac =infoFac.substring(0, facIndex);
			}else{
				newFac = "no";
			}
			String infoPro = null;
			if (sbFacId.length() > 0){
				infoPro = sbProId.toString();
			}
			String newPro = null;
			if (infoPro != null && !TextUtils.isEmpty(infoPro)){
				int proIndex = infoPro.lastIndexOf(',');
				newPro =infoPro.substring(0, proIndex);
			}else{
				newPro = "no";
			}
			
			String infoName = null;
			if (sbHandleName.length() > 0){
				infoName = sbHandleName.toString();
			}
			String newHandleName = null;
			if (infoName != null && !TextUtils.isEmpty(infoName)){
				int proIndex = infoName.lastIndexOf(',');
				newHandleName =infoName.substring(0, proIndex);
			}else{
				newHandleName = "no";
			}
			
			info.setHandleMac(newMac);
			info.setHandleFactoryId(newFac);
			info.setHandleProductId(newPro);
			info.setIsUseHandle(GAMEPAD_MODE);
			info.setHandleName(newHandleName);
			LAST_JOYSTICK_COUNT = joyList.size();
			
			
		}else{
			//当前无手柄连接
			LOG.E("test joystick ","no joysticks");
			
			info.setHandleMac("no");
			info.setHandleFactoryId("no");
			info.setHandleProductId("no");
			info.setHandleName("no");
			info.setIsUseHandle(NO_GAMEPAD_MODE);
			LAST_JOYSTICK_COUNT = 0;
		}
		
		
		/*if(joyList != null && blueMacList != null && joyList.size() > 0){
			StringBuilder sbMac = new StringBuilder();
			StringBuilder sbProId = new StringBuilder();
			StringBuilder sbFacId = new StringBuilder();
			
			
			
			if(joyList.size() > 0){
				for (int i = 0,j = 0;i < joyList.size();i++){
					JoystickInfo info1=joyList.get(i);
					
					if (Build.VERSION.SDK_INT < 19){
						//如果版本号小于4。4则将蓝牙手柄list中的mac地址，赋值给joysticklist中前面的手柄。
						if (i < blueMacList.size()){
							sbMac.append(blueMacList.get(i++)+",");
						}else{
							sbMac.append(info1.getMac()+",");
						}
						
					}else{
						//4.4的版本可以比较usbmanager获取的vendorID来识别是不是2.4g手柄。
						if (j < blueMacList.size()){
							//如果当前的手柄不是2.4G手柄，就当成蓝牙手柄，并将mac地址赋值给手柄
							
							//可以获取到usb设备信息的情况
							if (vendorIdSet != null && vendorIdSet.size() > 0){
								if(!vendorIdSet.contains(info1.getManufactorId())){
									sbMac.append(blueMacList.get(j++)+",");
								}else{
									//如果是2.4G手柄就直接赋值
									sbMac.append(info1.getMac()+",");
								}
							}else{
								//获取不到usb设备信息处理方式同4.4以下版本一样
								sbMac.append(blueMacList.get(j++)+",");
							}
							
						//	sbMac.append(blueMacList.get(i++)+",");
						}else{
							
							//非蓝牙手柄
							sbMac.append(info1.getMac()+",");
						}
						
					}
					
					sbProId.append(info1.getProductId()+",");
					sbFacId.append(info1.getManufactorId()+",");
				}
			}
			
			String infoMac = sbMac.toString();
			String newMac = null;
			if (infoMac != null){
				int macIndex = infoMac.lastIndexOf(',');
				newMac =infoMac.substring(0, macIndex);
			}else{
				newMac = "no";
			}
			String infoFac = sbFacId.toString();
			String newFac = null;
			if (infoFac != null){
				int facIndex = infoFac.lastIndexOf(',');
				newFac =infoFac.substring(0, facIndex);
			}else{
				newFac = "no";
			}
			String infoPro = sbProId.toString();
			String newPro = null;
			if (infoPro != null){
				int proIndex = infoPro.lastIndexOf(',');
				newPro =infoPro.substring(0, proIndex);
			}else{
				newPro = "no";
			}
			
			info.setHandleMac(newMac);
			info.setHandleFactoryId(newFac);
			info.setHandleProductId(newPro);
			info.setIsUseHandle(GAMEPAD_MODE);
			LAST_JOYSTICK_COUNT = joyList.size();
		}else{
			LOG.E("test joystick ","no joysticks");
			//无手柄连接的情况
			info.setHandleMac("no");
			info.setHandleFactoryId("no");
			info.setHandleProductId("no");
			info.setIsUseHandle(NO_GAMEPAD_MODE);
			LAST_JOYSTICK_COUNT = 0;
		}*/
		
		//有蓝牙连接或者是2。4G无线连接
		/*if(blueMacList.size() > 0 || joyList.size() > 0){
			StringBuilder sbMac = new StringBuilder();
			StringBuilder sbProId = new StringBuilder();
			StringBuilder sbFacId = new StringBuilder();
			
			for (int i = 0;i < blueMacList.size();i++){
				sbMac.append(blueMacList.get(i).getMac()+",");
				String deviceName = blueMacList.get(i).getName();
				if (deviceName == null || TextUtils.isEmpty(deviceName)){
					sbProId.append("GAMEPAD"+",");
				}else{
					sbProId.append(deviceName+",");
				}
				sbFacId.append("ATET"+",");
			}
			
			if(joyList.size() > 0){
				
				for (int i = 0;i < joyList.size() / 2;i++){
					JoystickInfo info1=joyList.get(i);
					
					if (Build.VERSION.SDK_INT < 19){
						//如果版本号小于4。4则将蓝牙手柄list中的mac地址，赋值给joysticklist中前面的手柄。
						if (i < blueMacList.size()){
							sbMac.append(blueMacList.get(i++)+",");
						}else{
							sbMac.append(info1.getMac()+",");
						}
						
					}else{
						//4.4的版本可以比较usbmanager获取的vendorID来识别是不是2.4g手柄。
						if (j < blueMacList.size()){
							//如果当前的手柄不是2.4G手柄，就当成蓝牙手柄，并将mac地址赋值给手柄
							
							//可以获取到usb设备信息的情况
							if (vendorIdSet != null && vendorIdSet.size() > 0){
								if(!vendorIdSet.contains(info1.getManufactorId())){
									sbMac.append(blueMacList.get(j++)+",");
								}else{
									//如果是2.4G手柄就直接赋值
									sbMac.append(info1.getMac()+",");
								}
							}else{
								//获取不到usb设备信息处理方式同4.4以下版本一样
								sbMac.append(blueMacList.get(j++)+",");
							}
							
							//sbMac.append(blueMacList.get(i++)+",");
						}else{
							
							//非蓝牙手柄
							sbMac.append(info1.getMac()+",");
						}
						
					}
					
					sbMac.append(info1.getMac()+",");
					sbProId.append(info1.getProductId()+",");
					sbFacId.append(info1.getManufactorId()+",");
				}
			}
			
			String infoMac = sbMac.toString();
			String newMac = null;
			if (infoMac != null){
				int macIndex = infoMac.lastIndexOf(',');
				newMac =infoMac.substring(0, macIndex);
			}else{
				newMac = "no";
			}
			String infoFac = sbFacId.toString();
			String newFac = null;
			if (infoFac != null){
				int facIndex = infoFac.lastIndexOf(',');
				newFac =infoFac.substring(0, facIndex);
			}else{
				newFac = "no";
			}
			String infoPro = sbProId.toString();
			String newPro = null;
			if (infoPro != null){
				int proIndex = infoPro.lastIndexOf(',');
				newPro =infoPro.substring(0, proIndex);
			}else{
				newPro = "no";
			}
			
			info.setHandleMac(newMac);
			info.setHandleFactoryId(newFac);
			info.setHandleProductId(newPro);
			info.setIsUseHandle(GAMEPAD_MODE);
			LAST_JOYSTICK_COUNT = joyList.size()/2 + blueMacList.size();
		}else{
			
			LOG.E("test joystick ","no joysticks");
			//无手柄连接的情况
			info.setHandleMac("no");
			info.setHandleFactoryId("no");
			info.setHandleProductId("no");
			info.setIsUseHandle(NO_GAMEPAD_MODE);
			LAST_JOYSTICK_COUNT = 0;
		}*/
		
		long id = PersistentSynUtils.addModel(info);
		
		if (id != -1)
		{
			mLastAppInfo = info;
			mLastAppInfo.setAutoIncrementId(id+"");
			mLastPackageName = packageName;
			
			addNewInfoLog(packageName, info.getGameName(), startTime, curruntTime);
		}
	}
	
	/**
	 * 判断指定的程序是不是桌面类APP
	 * */
	private boolean isHomeApp(String packageName)
	{
		
		if(mHomeList == null || mHomeList.size() == 0)
		{
			mHomeList = getHomes();
		}
		
		if(mHomeList != null && mHomeList.size() > 0)
		{
			return mHomeList.contains(packageName);
		}
		
		return false;
	}
	
	/** 
	 * 获得属于桌面的应用的应用包名称 
	 * @return 返回包含所有包名的字符串列表 
	 */  
	private List<String> getHomes() {  
	    List<String> names = new ArrayList<String>();  
	    PackageManager packageManager = this.getPackageManager();  
	    //属性  
	    Intent intent = new Intent(Intent.ACTION_MAIN);  
	    intent.addCategory(Intent.CATEGORY_HOME);  
	    List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,  
	            PackageManager.MATCH_DEFAULT_ONLY);  
	    for(ResolveInfo ri : resolveInfo){  
	        names.add(ri.activityInfo.packageName);  
	        System.out.println(ri.activityInfo.packageName);  
	    }  
	    return names;  
	}  
	
	private void checkDataBaseIsOverwrite()
	{
		List<GameOnlineInfo> recordList = PersistentSynUtils.getModelList(GameOnlineInfo.class," autoIncrementId > 0");
		
		if (recordList != null && recordList.size() > 100)
		{
			StatisticsRecordTestUtils.databaseOverwriteLog(this, "游戏在线统计服务");
			PersistentSynUtils.delete(recordList.get(0));
		}
				
	}
	
	/**
	 * 
	 * 检测是否可以向数据库中更新记录。
	 * 一般情况出现在上传服务成功后将数据库中的记录清空了。
	 * 
	 * */
	private boolean checkDatabaseIfCanUpdate(String id)
	{
		List<GameOnlineInfo> recordList = PersistentSynUtils.getModelList(GameOnlineInfo.class, " autoIncrementId = "+ id);
		
		return recordList.size() > 0 ? true:false;
	}
	
	private void addNewInfoLog(String packageName,String gameName,long startTime,long endTime)
	{
		StringBuilder sb = new StringBuilder();
		if (StatisticsRecordTestUtils.ACCOUNT_DEBUG == true)
		{
			String name = "游戏运行时长统计服务";
			sb.append("\r\n 新增游戏运行记录  ");
			sb.append(" 游戏包名："+packageName);
			sb.append(" 应用名称："+gameName);
			sb.append(" 开始时间："+DeviceStatisticsUtils.getDateToString(startTime));
			sb.append(" 结束时间："+DeviceStatisticsUtils.getDateToString(endTime));
			sb.append("\r\n");
			String msg = sb.toString();
			sb = null;
			StatisticsRecordTestUtils.newLog(this,name, msg);
		}
	}
	
	private void addNewInfoLogDetail(String packageName)
	{
		StringBuilder sb = new StringBuilder();
		if (StatisticsRecordTestUtils.ACCOUNT_DEBUG == true)
		{
			String name = "游戏运行时长统计服务";
			sb.append("\r\n 新增游戏运行记录   原因是数据库不能更新原来的记录 ");
			sb.append(" 游戏包名："+packageName);
			sb.append("\r\n");
			String msg = sb.toString();
			sb = null;
			StatisticsRecordTestUtils.newLog(this,name, msg);
		}
	}
	
	private void updateInfoLog(String packageName,String gameName,long startTime,long longTime,long endTime)
	{
		StringBuilder sb = new StringBuilder();
		if (StatisticsRecordTestUtils.ACCOUNT_DEBUG == true)
		{
			String name = "游戏运行时长统计服务";
			sb.append("\r\n 游戏连续运行  ");
			sb.append(" 游戏包名："+packageName);
			sb.append(" 应用名称："+gameName);
			sb.append(" 开始时间："+DeviceStatisticsUtils.getDateToString(startTime));
			sb.append(" 运行时长："+longTime + "秒");
			sb.append(" 结束时间："+DeviceStatisticsUtils.getDateToString(endTime));
			sb.append("\r\n");
			String msg = sb.toString();
			sb = null;
			StatisticsRecordTestUtils.newLog(this,name, msg);
		}
	}
	
	private void noInternetLog(String tag)
	{
		StringBuilder sb = new StringBuilder();
		if (StatisticsRecordTestUtils.ACCOUNT_DEBUG == true)
		{
			String name = tag;
			sb.append("\r\n 无网络，服务上传失败  ");
			sb.append("\r\n");
			String msg = sb.toString();
			sb = null;
			StatisticsRecordTestUtils.newLog(this,name, msg);
		}
	}
	
	public List<JoystickInfo> getJoysticks(){
//		InputManager im = (InputManager) getSystemService(Context.INPUT_SERVICE);
		int[] deviceIds = mIM.getInputDeviceIds();
		
		if (deviceIds == null){
			return null;
		}
		List<JoystickInfo> list = new ArrayList<JoystickInfo>();
		LOG.E("getJoystick size:",deviceIds.length+"");
		int id = 0;
		
		for(int i = 0;i < deviceIds.length;i++){
			
			InputDevice device = mIM.getInputDevice(deviceIds[i]);
			
			if (device != null && isJoystick(device)){
				id++;
				JoystickInfo info = new JoystickInfo();
				
				if (Build.VERSION.SDK_INT < 19){
					info.setManufactorId("ATET");
					info.setProductId("Joystick001");
					//LOG.E("joystick info", device.toString());
				}else{
					int vendorId = device.getVendorId();
					
					/*if (vendorIdSet != null && vendorIdSet.contains(vendorId)){
						info.setType(1);
					}*/
					info.setManufactorId(device.getVendorId()+"");
					info.setProductId(device.getProductId()+"");
					
					LOG.E("getJoystick productId:",device.getProductId()+"  vendorId: "+vendorId+device.toString());
//					LOG.E("getJoystick productId:",device.getProductId()+"  vendorId: "+vendorId+device.toString());
					
				}
				info.setMac(DeviceStatisticsUtils.getAtetId(this)+id+"");
				info.setHandlerName("2.4G");
				list.add(info);
			}
		}
		
		LOG.E("test joystick count:",list.size()+"");
		return list;
	}
	
	/**
	 * 检测是不是手柄
	 * 
	 * @author zhaominglai
	 * @date 2014-12-20 11:47
	 * @param source InputDevice中的参数，用来识别输入设备类型
	 * */
	public boolean isJoystick(InputDevice device){
		//add by zml 2015-3-2 TCL901电视板内置的手柄需要剔除掉
		if (device.getName().contains("TCL_RC660K")){
			return false;
		}
		
		int source = device.getSources();
		
		Class clz = device.getClass();
		Field isExternal = null;
		try {
			isExternal = clz.getDeclaredField("mIsExternal");
			isExternal.setAccessible(true);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean isNoBuiltin = false;
		
		try {
			isNoBuiltin = isExternal.getBoolean(device);
			LOG.E("test is real joystick---",isNoBuiltin+"");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LOG.E("test joystick type ,source:  ",device.getSources()+"");
		
		//2.4g无线手柄有dpad属性，所以过滤掉包括dpad属性的2.4G手柄。
//		return  (source & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK ? true
//				:(source & InputDevice.SOURCE_GAMEPAD)  == InputDevice.SOURCE_GAMEPAD?true:false;
		
		//add by zml 2015-1-29 15:06  2。4G手柄在程序中显示为2个，需要剔除一个
		boolean condition1 = (source & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK			
				&& (source & InputDevice.SOURCE_GAMEPAD)  == InputDevice.SOURCE_GAMEPAD 
				&& (source  & InputDevice.SOURCE_DPAD ) == InputDevice.SOURCE_DPAD;
		boolean condition2 = (source & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK			
				&& (source & InputDevice.SOURCE_GAMEPAD)  == InputDevice.SOURCE_GAMEPAD 
				&& (source  & InputDevice.SOURCE_MOUSE ) == InputDevice.SOURCE_MOUSE;
		
		return  (condition1 || condition2) && isNoBuiltin;
	}
	
	
	/**
	 * 
	 * 获取连接到的蓝牙手柄
	 * @author zhaominglai
	 * @date 2014-12-20
	 * 
	 * */
	List<GamePadInfo> getBluetoothGamePad(){
		if (blueDevice == null && adapter != null && adapter.isEnabled()){
			adapter.getProfileProxy(this, new InputDeviceServiceListener(), 4);
		}
		
		List<GamePadInfo> gamePadList = new ArrayList<GamePadInfo>();
		
		if (blueDevice == null){
			return gamePadList;
		}
		
		List<BluetoothDevice> list = blueDevice.getConnectedDevices();
		
		for (int i = 0;i<list.size();i++){
			BluetoothDevice device = list.get(i);
			if (isGamePad(device)){
				GamePadInfo info = new GamePadInfo();
				info.setMac(device.getAddress());
				info.setName(device.getName());
				gamePadList.add(info);
			}
		}
		
		LOG.E("test joystkck bluepad count ",list.size()+"");
		
	  return gamePadList;
	}
	
	/**
	 * 
	 * @Title: isGamePad
	 * @Description: TODO 判断是否为蓝牙设备
	 * @param @param device
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	@SuppressLint("NewApi")
	private boolean isGamePad(BluetoothDevice device) {
		if (device == null) {
			return false;
		}
		boolean result = false;
		int deviceClass = device.getBluetoothClass().getDeviceClass();// 设备类对象
		if (deviceClass == BLUETOOTH_DEVICE_CLASS || deviceClass == ATETBJ9_DEVICE_CLASS) {
			result = true;
		}

		LOG.E(TAG, "isGamePad:" + result + " device name:" + device.getName()
				+ " addr:" + device.getAddress() + " deviceClass:"
				+ deviceClass);
		return result;
	}
	
	private final class InputDeviceServiceListener implements
			BluetoothProfile.ServiceListener {

		// service连接时回调
		public void onServiceConnected(int profile, BluetoothProfile proxy) {
			DebugTool.info(TAG,
				"[onServiceConnected] Bluetooth service connected");
			blueDevice = (BluetoothInputDevice) proxy;
	
		}

		@Override
		public void onServiceDisconnected(int arg0) {
		// TODO Auto-generated method stub
			blueDevice = null;
		
		}
		
		
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		if (adapter != null){
			adapter.closeProfileProxy(BluetoothProfile.INPUT_DEVICE, blueDevice);
		}
	}

	private void saveGameOnlineUsertaskRecord(Context context, String packageName, int durationMin){
		if(mAppTimeStep <= 0 || mAppTimeStep % 4 != 0){
			return;
		}
		//2分钟更新一次
		String gameId = DeviceStatisticsUtils.getGameId(packageName);
		if(TextUtils.isEmpty(gameId)){
			return;
		}
		int userId = DataFetcher.getUserIdInt();
		if(userId > 0 ){
//			UserTaskDaoHelper.saveGameOnlineRecord(context, userId, gameId, durationMin, 0, 0);
			UserTaskDaoHelper.saveGameOnlineRecord(context, userId, gameId, 2, 0, 0);
		}
	}
}
