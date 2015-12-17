package com.atet.tvmarket.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
public class ScreenShot {
	public static Bitmap screenShotBmp;
    // 获取指定Activity的截屏，保存到png文件
    public static Bitmap takeScreenShot(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Log.i("TAG", "" + statusBarHeight);
        // 获取屏幕长和高
        /*int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();*/
        int width = b1.getWidth();
        int height = b1.getHeight();  
        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        int randomW = (int) (Math.random()*(width));
        int randomH = (int)(Math.random()*(height));
        
        int x = 0,y = 0;
        if(randomW+480>width){
        	x = width-480;
        }
        else{
        	x = randomW;
        }
        
        if(randomH+270>height){
        	y = height-270;
        }
        else{
        	y = randomH;
        }
        
        Bitmap b = Bitmap.createBitmap(b1, x, y, 480, 270);
        view.destroyDrawingCache();
        return b;
    }
    
    public static Bitmap takeAllScreen(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Log.i("TAG", "" + statusBarHeight);
        // 获取屏幕长和高
        /*int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();*/
        int width = b1.getWidth();
        int height = b1.getHeight();  
        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        /*int randomW = (int) (Math.random()*(width));
        int randomH = (int)(Math.random()*(height));
        
        int x = 0,y = 0;
        if(randomW+480>width){
        	x = width-480;
        }
        else{
        	x = randomW;
        }
        
        if(randomH+270>height){
        	y = height-270;
        }
        else{
        	y = randomH;
        }*/
        
        Bitmap b = Bitmap.createBitmap(b1, 0, 0, width, height);
        view.destroyDrawingCache();
        return b;
    }
    
    // 保存到sdcard
    public static void savePic(Bitmap b, String strFileName) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(strFileName);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}