package com.atet.tvmarket.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import android.os.Environment;

/**
 * @description: IP端口
 *
 * @author: LiuQin
 * @date: 2015年6月24日 下午5:09:33
 */
public class IPPort {
	//支付是否使用线上IP
	public static boolean IS_PAY_RELEASE_IP = true;
	
	// 正式上线游戏平台 
	public static String BASE_GAME_SERVER = "http://interface.atet.tv:25001";
	public static String BASE_GAME_SEVER_HOST2 = "interface.atetchina.tv";
	public static String BASE_GAME_SERVER_RELEASE = "http://interface.atet.tv:25001";
	public static String BASE_GAME_SERVER_RELEASE2 = "http://interface.atetchina.tv:25001";
	
	// 正式上线USER
	public static String BASE_USER_SERVER = "http://user.at-et.com:80";
	public static String BASE_USER_SEVER_HOST2 = "http://user.at-et.com";

	// 接口本地测试环境
	public static String PUBLIC_TEST_BASE_GAME_SERVER = "http://61.145.164.151:80";
	public static String PUBLIC_TEST_BASE_GAME_SEVER_HOST2 = "http://61.145.164.151";
	public static String PUBLIC_TEST_BASE_GAME_SEVER_HOST3 = "http://61.145.164.151";
			
	// user本地测试环境
	public static String PUBLIC_TEST_BASE_USER_SERVER = "http://61.145.164.114:25002";
	public static String PUBLIC_TEST_BASE_USER_SEVER_HOST2 = "http://61.145.164.114:25001";

	static {
		if (Configuration.HTTP_PUBLIC_TEST_SERVER_ENABLE) {
			BASE_GAME_SERVER = PUBLIC_TEST_BASE_GAME_SERVER;
			BASE_GAME_SEVER_HOST2 = PUBLIC_TEST_BASE_GAME_SERVER;
			
			BASE_USER_SERVER = PUBLIC_TEST_BASE_USER_SERVER;
			BASE_USER_SEVER_HOST2 = PUBLIC_TEST_BASE_USER_SERVER;
		}
		
		if(Configuration.ENABLE_DEBUG_IP_FILE){
			String path = null;  
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				path = Environment.getExternalStorageDirectory().getPath()+"/ATET/tvmarket/debug/ip";
			} else {
				path = BaseApplication.getContext().getFilesDir().getPath()+"/debug/ip";
			}
			File ipFile = new File(path);
			if(ipFile.exists() && ipFile.canRead()){
				try {
					
					FileReader reader = new FileReader(ipFile);
					BufferedReader br = new BufferedReader(reader);
					String string = null;
					while((string = br.readLine()) != null) {
						string=string.trim();
						if(string.startsWith("game:")){
							string=string.substring("game:".length());
							BASE_GAME_SERVER = string;
							BASE_GAME_SEVER_HOST2 = string.split(":")[0];

						} else if(string.startsWith("user:")){
							string=string.substring("user:".length());
							BASE_USER_SERVER = string;
							BASE_USER_SEVER_HOST2 = string.split(":")[0];
						} else if(string.startsWith("pay:online")){
							IPPort.IS_PAY_RELEASE_IP = true;
						}
					}
					br.close();
					reader.close();		
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
