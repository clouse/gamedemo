package com.atet.tvmarket.utils;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

/**
 * 手柄按键类
 * 
 * @author: LiuQin
 * @date: 2014-4-12
 */
public class GamepadTool {
    
    /**
     * 判断是否手柄的A键
     * 
     * @param keyCode
     * @return 
     * @throws
     * @author:LiuQin
     * @date 2014-4-12 
     */
    public static boolean isButtonA(int keyCode){
        return keyCode == KeyEvent.KEYCODE_A || keyCode == KeyEvent.KEYCODE_BUTTON_A ;
    }

    public static boolean isButtonB(int keyCode){
        return keyCode == KeyEvent.KEYCODE_B || keyCode == KeyEvent.KEYCODE_BUTTON_B ;
    }
    
    public static boolean isButtonX(int keyCode){
        return keyCode == KeyEvent.KEYCODE_X || keyCode == KeyEvent.KEYCODE_BUTTON_X ;
    }
    
    public static boolean isButtonY(int keyCode){
        return keyCode == KeyEvent.KEYCODE_Y || keyCode == KeyEvent.KEYCODE_BUTTON_Y ;
    }
    
    public static boolean isButtonL1(int keyCode){
        return keyCode == KeyEvent.KEYCODE_Q || keyCode == KeyEvent.KEYCODE_BUTTON_L1 ;
    }
    
    public static boolean isButtonL2(int keyCode){
        return keyCode == KeyEvent.KEYCODE_S || keyCode == KeyEvent.KEYCODE_BUTTON_L2 ;
    }
    
    public static boolean isButtonR1(int keyCode){
        return keyCode == KeyEvent.KEYCODE_J || keyCode == KeyEvent.KEYCODE_BUTTON_R1 ;
    }
    
    public static boolean isButtonR2(int keyCode){
        return keyCode == KeyEvent.KEYCODE_T || keyCode == KeyEvent.KEYCODE_BUTTON_R2 ;
    }
    
    public static boolean isButtonXY(int keyCode){
        return isButtonX(keyCode) || isButtonY(keyCode);
    }
    
    public static boolean isDirectionLeft(int keyCode){
        return keyCode == KeyEvent.KEYCODE_DPAD_LEFT;
    }
    
    public static boolean isDirectionUp(int keyCode){
        return keyCode == KeyEvent.KEYCODE_DPAD_UP;
    }
    
    public static boolean isDirectionRight(int keyCode){
        return keyCode == KeyEvent.KEYCODE_DPAD_RIGHT;
    }
    
    public static boolean isDirectionDown(int keyCode){
        return keyCode == KeyEvent.KEYCODE_DPAD_DOWN;
    }
    
    public static boolean isButtonGame(int keyCode){
        return keyCode == KeyEvent.KEYCODE_U;
    }
    
    public static boolean isButtonBack(int keyCode){
        return keyCode == KeyEvent.KEYCODE_BACK;
    }
    
    /**
     * @description: 手柄云键
     *
     * @param keyCode
     * @return 
     * @author: LiuQin
     */
    public static boolean isButtonCloud(int keyCode){
        return keyCode == KeyEvent.KEYCODE_K;
    }
    
    /**
     * @description: 处理云键
     *
     * @param context
     * @param event
     * @return 
     * @author: LiuQin
     * @date: 2015年9月21日 上午2:06:30
     */
    public static boolean handleButtonCloud(Context context, KeyEvent event){
    	if(isButtonCloud(event.getKeyCode())){
			sendBroderCast(context, event.getKeyCode(),event.getAction());
			return true;
    	}
    	return false;
    }
    
    /**
     * @description: 处理Game键
     *
     * @param context
     * @param event
     * @return 
     * @author: LiuQin
     */
    public static boolean handleButtonGame(Context context, KeyEvent event){
    	if(isButtonGame(event.getKeyCode())){
			sendBroderCast(context, event.getKeyCode(),event.getAction());
			return true;
    	}
    	return false;
    }
    
	/**
	 * @description: 发送广播到手柄输入法
	 *
	 * @param context
	 * @param Keycode
	 * @param Action 
	 * @author: LiuQin
	 */
	private static void sendBroderCast(Context context, int Keycode,int Action){
		Intent intent = new Intent();
		intent.setAction("com.atet.tvgamepad.menu");
		intent.putExtra("Keycode", Keycode);
		intent.putExtra("Action", Action);
		context.sendBroadcast(intent);
	}
}
