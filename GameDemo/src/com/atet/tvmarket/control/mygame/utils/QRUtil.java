package com.atet.tvmarket.control.mygame.utils;

import java.util.Hashtable;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class QRUtil {
	private static final int BLACK = 0xff000000;
	

	/**
	 * 
	 * @description 创建二维码
	 * @param str
	 * @param widthAndHeight 宽度和高度
	 * @return
	 * @throws WriterException
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:00:20
	 *
	 */
	public static Bitmap createQRCode(String str,int widthAndHeight) throws WriterException {
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();  
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); 
		BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = BLACK;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
	
	/**
	 * 
	 * @description 转化成灰度图
	 * @param bmpOriginal 原图
	 * @return
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:00:43
	 *
	 */
	public synchronized static Bitmap toGrayscale(Bitmap bmpOriginal)
	{
		   int width = bmpOriginal.getWidth();      //获取位图的宽 
	          int height = bmpOriginal.getHeight();    //获取位图的高 
	            
	          int []pixels = new int[width * height];  //通过位图的大小创建像素点数组 
	            
	          bmpOriginal.getPixels(pixels, 0, width, 0, 0, width, height); 
	          int alpha = 0xFF << 24;    
	          for(int i = 0; i < height; i++)  { 
	            for(int j = 0; j < width; j++) { 
	              int grey = pixels[width * i + j]; 
	                
	              int red = ((grey    & 0x00FF0000 ) >> 16); 
	              int green = ((grey & 0x0000FF00) >> 8); 
	              int blue = (grey & 0x000000FF); 
	              grey = (int)((float) red * 0.1 + (float)green * 0.19 + (float)blue * 0.11); 
	              grey = alpha | (grey << 16) | (grey << 8) | grey; 
	              if(pixels[width * i + j] != 255)
	              pixels[width * i + j] = grey; 
	            } 
	          } 
	          Bitmap result = Bitmap.createBitmap(width, height, Config.RGB_565); 
	          result.setPixels(pixels, 0, width, 0, 0, width, height); 
	          return result; 
//		   int width, height;
//	        height = bmpOriginal.getHeight();
//	        width = bmpOriginal.getWidth();
//	        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//	        Canvas c = new Canvas(bmpGrayscale);
//	        Paint paint = new Paint();
//	        ColorMatrix cm = new ColorMatrix();
//	        cm.setSaturation(0);
//	        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
//	        paint.setColorFilter(f);
//	        c.drawBitmap(bmpOriginal, 0, 0, paint);
//	        return bmpGrayscale;
	  }
	
}
