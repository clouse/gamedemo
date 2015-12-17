package com.atet.tvmarket.model;

import java.io.File;
import java.io.FileInputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.atet.tvmarket.app.Constant;

public class ImgHelper {
	public static Bitmap loadBitmap(Context context, String path){
        try {
        	FileInputStream fis = new FileInputStream(new File(path));
            if (fis != null) {
                final BitmapFactory.Options options = new BitmapFactory.Options();  
                options.inJustDecodeBounds = true;  
                BitmapFactory.decodeStream(fis, null, options);
                
                options.inSampleSize = calculateInSampleSize(options, Constant.HEIGHT, Constant.WIDTH);
                
                // Decode bitmap with inSampleSize set 
                options.inJustDecodeBounds = false;
//                options.inPreferredConfig=Bitmap.Config.RGB_565;
                
                fis = new FileInputStream(new File(path));
                final Bitmap bitmap = BitmapFactory.decodeStream(fis,null,options);
//                if (bitmap != null) {
//                    int bytesCount=bitmap.getByteCount();
//                }
                return bitmap;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }
	
	public static int calculateInSampleSize(BitmapFactory.Options options,  
            int reqWidth, int reqHeight) {  
        // Raw height and width of image  
        final int height = options.outHeight;  
        final int width = options.outWidth;  
        int inSampleSize = 1;  
  
        if (height > reqHeight || width > reqWidth) {  
  
            // Calculate ratios of height and width to requested height and  
            // width  
            final int heightRatio = Math.round((float) height  
                    / (float) reqHeight);  
            final int widthRatio = Math.round((float) width / (float) reqWidth);  
  
            // Choose the smallest ratio as inSampleSize value, this will  
            // guarantee  
            // a final image with both dimensions larger than or equal to the  
            // requested height and width.  
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;  
        }  
  
        return inSampleSize;  
    }  
}
