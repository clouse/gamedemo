package com.atet.statistics.services;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;

import com.atet.statistics.utils.DeviceInfoHelper;
import com.atet.statistics.utils.DeviceStatisticsUtils;
import com.atet.tvmarket.control.mygame.task.TaskManager;
import com.atet.tvmarket.model.DataHelper;



/**
 * 
 * 用于向服务器上传设备信息，主要用于后台统计装机量
 * 
 * @author zhaominglai
 * @date 2014/7/9
 * 
 * */
public class UpdateHardwareService extends IntentService {


	public static final String TAG = "UpdateHardwareServer";
	protected TaskManager taskManager;
	private static final String IS_UPDATED = "isUpdated";//是否已经上传过信息
	
	public UpdateHardwareService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	
	public UpdateHardwareService() {
		this("UpdateHardwareServer");
	}


		
//	/**
//	 * 上传初始化信息
//	 * */
//	public boolean UpLoadInstalledInfosTask() {
//		
//		SharedPreferences bootSP = getSharedPreferences("BOOT_INFO", MODE_PRIVATE);
//		//如果上传成功之后，不必再进行任何操作
//		if(bootSP.getBoolean(IS_UPDATED, false) && bootSP.getBoolean("hasAtetId", false))
//		{
//			return true;
//		}
//		
//		String channelId = DeviceStatisticsUtils.getChannelId(this);
//		
//		//如果没有网络就返回
//		if (!NetUtil.isNetworkAvailable(getBaseContext(), true))
//		{
//			DebugTool.info(TAG, "向网络发送装机量信息,无网络失败。");
//			//生成操作日志到本地
//			if (StatisticsRecordTestUtils.ACCOUNT_DEBUG == true)
//			{
//				String name = "统计装机量服务";
//				StringBuilder sb = new StringBuilder();
//				sb.append("上传设备信息失败！无有效的网络连接。");
//				String msg = sb.toString();
//				sb = null;
//				StatisticsRecordTestUtils.newLog(this,name, msg);
//			}
//			return false;
//		}
//		
//		if (!DeviceStatisticsUtils.isProductIdValid(this)){
//			LOG.E("test product id","invalid");
//			return false;
//		}
//		
//		//如果不存在有效的channelId号则取消本次操作，等下一次再进行上传
//		if (channelId.equals("0") || channelId.equals("1") || channelId == null)
//		{
//			//去服务器获取channelId deviceId信息，并在下一次启动的时候再来读取。
//			DeviceInfoHelper.getDeviceInfoFromNet(this, "0", DeviceStatisticsUtils.getDeviceCode(),DeviceStatisticsUtils.getProductId(this,this.getContentResolver()),DEVICE_TYPE.TYPE_TV);
//			if (StatisticsRecordTestUtils.ACCOUNT_DEBUG == true)
//			{
//				String name = "统计装机量服务";
//				StringBuilder sb = new StringBuilder();
//				sb.append("当前无有效的channelId，再尝试去服务器获取。");
//				String msg = sb.toString();
//				sb = null;
//				StatisticsRecordTestUtils.newLog(this,name, msg);
//			}
//			return false;
//		}
//		
//		
//		
//		HttpReqParams params = new HttpReqParams();
//		
//		InstalledInfo info = new InstalledInfo();
//		
//		info.setDeviceCode(DeviceStatisticsUtils.getDeviceCode());
//		info.setDeviceId(DeviceStatisticsUtils.getDeviceId(this,this.getContentResolver()));
//		info.setDeviceType(DeviceStatisticsUtils.getDeviceType(this));
//		info.setChannelId(DeviceStatisticsUtils.getChannelId(this));
//		info.setBlueToothMac(DeviceStatisticsUtils.getBlueToothMac(this));
//		info.setProductId(DeviceStatisticsUtils.getProductId(this,this.getContentResolver()));
//		info.setCpu(DeviceStatisticsUtils.getCpuName());
//		if(BaseApplication.gpuInfo!=null){
//			info.setGpu("BaseApplication.gpuInfo");
//		}else{
//			info.setGpu("nogpuinfo");
//			
//		}
//		info.setRam(DeviceStatisticsUtils.getTotalMemory(this));
//		info.setResolution(DeviceStatisticsUtils.getResolution(this));
//		info.setRom(DeviceStatisticsUtils.getRomTotalSize(this));
//		info.setSdkVersion(DeviceStatisticsUtils.getSDKVersion());
//		info.setSdCard(DeviceStatisticsUtils.getSDTotalSize(this));
//		info.setPackageName(getPackageName());
//		info.setVersionCode(DeviceStatisticsUtils.getPlatformVersion(this));
//		info.setDpi(DeviceStatisticsUtils.getDpi(this));
//		info.setInstallTime(System.currentTimeMillis());
//		
//
//		DebugTool.info(TAG, "向网络发送装机量信息");
//		params.setDeviceId(info.getDeviceId());
//		params.setDeviceCode(info.getDeviceCode());		
//		params.setDeviceType(info.getDeviceType());
//		params.setProductId(info.getProductId());
//		params.setPackageName(info.getPackageName());
//		params.setChannelId(info.getChannelId());
//		params.setCpu(info.getCpu());
//		params.setBlueToothMac(info.getBlueToothMac());
//		params.setGpu(info.getGpu());
//		params.setRom(info.getRom());
//		params.setRam(info.getRam());
//		params.setResolution(info.getResolution());
//		params.setSdCard(info.getSdCard());
//		params.setSdkVersion(info.getSdkVersion());
//		params.setVersionCode(info.getVersionCode());
//		params.setDpi(info.getDpi());
//		params.setInstallTime(info.getInstallTime());
//		params.setUserId(DeviceStatisticsUtils.getUserId(this));
//		
//		
//		TaskResult<InstalledInfo> result = HttpApi.getObject(StatisticsUrlConstant.HTTP_INSTALLED_INFOS1,StatisticsUrlConstant.HTTP_INSTALLED_INFOS2,
//				StatisticsUrlConstant.HTTP_INSTALLED_INFOS3,InstalledInfo.class, params.toJsonParam());
//		
//		DebugTool
//		.debug(TAG,
//				"UpLoadInstalledInfosTask  result.getcode="
//						+ result.getCode() + "  result.getdata="
//						+ result.getData());
//			
//		if (result.getCode() == TaskResult.OK && result.getData() != null) {
//
//			if (StatisticsRecordTestUtils.ACCOUNT_DEBUG == true)
//			{
//				String name = "统计装机量服务";
//				StringBuilder sb = new StringBuilder();
//				sb.append("上传设备信息成功");
//				sb.append("\r\n"+info.toString()+"\r\n");
//				String msg = sb.toString();
//				sb = null;
//				StatisticsRecordTestUtils.newLog(this,name, msg);
//			}
//
//		    bootSP = getSharedPreferences("BOOT_INFO", MODE_PRIVATE);
//			bootSP.edit().putBoolean(IS_UPDATED, true).commit();
//			
//
//			return true;
//		}else if (result.getCode() == TaskResult.FAILED) {
//			
//			if (StatisticsRecordTestUtils.ACCOUNT_DEBUG == true)
//			{
//				String name = "统计装机量服务";
//				StringBuilder sb = new StringBuilder();
//				sb.append("上传设备信息失败！服务器返回状态标志为失败！");
//				String msg = sb.toString();
//				sb = null;
//				StatisticsRecordTestUtils.newLog(this,name, msg);
//			}
//			
//			return false;
//		}
//		else if (result.getCode() == StatisticsTaskResult.STATISTICS_REQUSET_PARAM_ERR) {
//			
//			if (StatisticsRecordTestUtils.ACCOUNT_DEBUG == true)
//			{
//				String name = "统计装机量服务";
//				StringBuilder sb = new StringBuilder();
//				sb.append("上传设备信息失败！请求的参数发生错误");
//				String msg = sb.toString();
//				sb = null;
//				StatisticsRecordTestUtils.newLog(this,name, msg);
//			}
//			
//			return false;
//		} else if (result.getCode() == StatisticsTaskResult.STATISTICS_SYSERR) {
//			// 没有符合要求的数据
//			
//			if (StatisticsRecordTestUtils.ACCOUNT_DEBUG == true)
//			{
//				String name = "统计装机量服务";
//				StringBuilder sb = new StringBuilder();
//				sb.append("上传设备信息失败！服务器系统内部发生错误");
//				String msg = sb.toString();
//				sb = null;
//				StatisticsRecordTestUtils.newLog(this,name, msg);
//			}
//			return false;
//		} else if (result.getCode() == StatisticsTaskResult.STATISTICS_INVALIDATE_OP) {
//			// 没有符合要求的数据
//			
//			if (StatisticsRecordTestUtils.ACCOUNT_DEBUG == true)
//			{
//				String name = "统计装机量服务";
//				StringBuilder sb = new StringBuilder();
//				sb.append("上传设备信息失败！非法操作");
//				String msg = sb.toString();
//				sb = null;
//				StatisticsRecordTestUtils.newLog(this,name, msg);
//			}
//			return false;
//		} else if (result.getCode() == StatisticsTaskResult.STATISTICS_REQUSET_JSON_ERR) {
//			// 没有符合要求的数据
//			
//			if (StatisticsRecordTestUtils.ACCOUNT_DEBUG == true)
//			{
//				String name = "统计装机量服务";
//				StringBuilder sb = new StringBuilder();
//				sb.append("上传设备信息失败！服务器系统解析请求的json数据出错");
//				String msg = sb.toString();
//				sb = null;
//				StatisticsRecordTestUtils.newLog(this,name, msg);
//			}
//			return false;
//		}
//		
//		
//		return false;
//	
//	}

	@Override
	protected void onHandleIntent(Intent intent) {	
		// TODO Auto-generated method stub
		
		uploadHardwareInfos();
		
		//开机启动的时候延迟2分钟上传，防止刚开机的时候无网络不进行上传。
		/*new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 * 
				
*/				//UpLoadInstalledInfosTask();
		/*	}
		}, 2 * 6000);*/
		
		
	}


	private void uploadHardwareInfos() {
		// TODO Auto-generated method stub
		
		if(TextUtils.isEmpty(DataHelper.getDeviceInfo().getServerId())){
			DeviceInfoHelper.getDeviceInfoFromNet(this, "0", DeviceStatisticsUtils.getDeviceCode(),
					DeviceStatisticsUtils.getProductId(this,this.getContentResolver()),DeviceStatisticsUtils.getDeviceType(this));
			if(TextUtils.isEmpty(DataHelper.getDeviceInfo().getServerId())){
				return;
			}
		}
		DeviceStatisticsUtils.fetchAtetId(this);
	}



}
