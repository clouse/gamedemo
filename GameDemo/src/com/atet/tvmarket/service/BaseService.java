package com.atet.tvmarket.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * @description: 所有Service的基类
 *
 * @author: LiuQin
 * @date: 2015年5月27日 上午10:03:32 
 */
public class BaseService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
