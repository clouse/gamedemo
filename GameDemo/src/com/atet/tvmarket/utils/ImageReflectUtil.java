package com.atet.tvmarket.utils;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.view.View;
import android.view.View.MeasureSpec;

import com.atet.tvmarket.app.BaseApplication;

/**
 * 倒影生成
 * 
 * */
public class ImageReflectUtil {
	public static int reflectImageHeight = (int) (46 * BaseApplication.sScreenWZoom);
	
	private static HashMap<Integer, SoftReference<Bitmap>> cacheImages = new HashMap<Integer, SoftReference<Bitmap>>();
	
	/** 设置倒影的高度 */
	public static void setReflectImageHeight(int reflectImageHeight) {
		ImageReflectUtil.reflectImageHeight = reflectImageHeight;
	}
	
	public static Bitmap createReflectBitmap(int id, int reflectBitmapHeight) {
		
		if(cacheImages != null){
			SoftReference<Bitmap> softReference = cacheImages.get(id);
			if(softReference != null){
				Bitmap bitmap = softReference.get();
				if(bitmap != null){
					return bitmap;
				}
			}
		}
		InputStream is = UIUtils.getResources().openRawResource(id);  
		Bitmap bitmap = BitmapFactory.decodeStream(is);
		Bitmap reflectBitmap = ImageReflectUtil.createReflectedImage(bitmap,
				reflectBitmapHeight);
		cacheImages.put(id, new SoftReference<Bitmap>(reflectBitmap));
		return reflectBitmap;
	}

	public static Bitmap convertViewToBitmap1(View paramView) {
		paramView.measure(View.MeasureSpec.makeMeasureSpec(0, 0),
				View.MeasureSpec.makeMeasureSpec(0, 0));
		paramView.layout(paramView.getLeft(), paramView.getTop(),
				paramView.getLeft() + paramView.getMeasuredWidth(),
				paramView.getTop() + paramView.getMeasuredHeight());
		paramView.destroyDrawingCache();// 在获取新的图像cache之前，先销毁之前的cache
		paramView.buildDrawingCache();
		return paramView.getDrawingCache();
	}

	public static Bitmap convertViewToBitmap(View paramView) {
		paramView.setDrawingCacheEnabled(true);
		paramView.measure(
				View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		paramView.layout(paramView.getLeft(), paramView.getTop(),
				paramView.getLeft() + paramView.getMeasuredWidth(),
				paramView.getTop() + paramView.getMeasuredHeight());
		// paramView.destroyDrawingCache();//在获取新的图像cache之前，先销毁之前的cache
		paramView.buildDrawingCache();
		Bitmap mbmp = Bitmap.createBitmap(paramView.getDrawingCache());
		paramView.setDrawingCacheEnabled(false);
		return mbmp;

	}

	public static Bitmap createCutReflectedImage(Bitmap paramBitmap,
			int paramInt, boolean isRecyle) {
		int i = paramBitmap.getWidth();
		int j = paramBitmap.getHeight();
		Bitmap localBitmap2 = null;
		if (j <= paramInt + reflectImageHeight) {
			localBitmap2 = null;
		} else {
			Matrix localMatrix = new Matrix();
			localMatrix.preScale(1.0F, -1.0F);
			Bitmap localBitmap1 = Bitmap.createBitmap(paramBitmap, 0, j
					- reflectImageHeight - paramInt, i, reflectImageHeight,
					localMatrix, true);
			localBitmap2 = Bitmap.createBitmap(i, reflectImageHeight,
					Bitmap.Config.ARGB_8888);
			Canvas localCanvas = new Canvas(localBitmap2);
			localCanvas.drawBitmap(localBitmap1, 0.0F, 0.0F, null);
			LinearGradient localLinearGradient = new LinearGradient(0.0F, 0.0F,
					0.0F, localBitmap2.getHeight(), -2130706433, 16777215,
					TileMode.CLAMP);
			Paint localPaint = new Paint();
			localPaint.setShader(localLinearGradient);
			localPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
			localCanvas.drawRect(0.0F, 0.0F, i, localBitmap2.getHeight(),
					localPaint);
			if (!localBitmap1.isRecycled()) {
				localBitmap1.recycle();
				localBitmap1 = null;
			}
			if (isRecyle && !paramBitmap.isRecycled()) {
				paramBitmap.recycle();
				paramBitmap = null;
			}
			System.gc();
		}
		return localBitmap2;
	}

	public static Bitmap createReflectedImage(Bitmap paramBitmap, int paramInt) {
		int i = paramBitmap.getWidth();
		int j = paramBitmap.getHeight();
		Bitmap localBitmap2;
		if (j <= paramInt) {
			localBitmap2 = null;
		} else {
			Matrix localMatrix = new Matrix();
			localMatrix.preScale(1.0F, -1.0F);
			Bitmap localBitmap1 = Bitmap.createBitmap(paramBitmap, 0, j
					- paramInt, i, paramInt, localMatrix, true);
			localBitmap2 = Bitmap.createBitmap(i, paramInt,
					Bitmap.Config.ARGB_8888);
			Canvas localCanvas = new Canvas(localBitmap2);
			localCanvas.drawBitmap(localBitmap1, 0.0F, 0.0F, null);
			LinearGradient localLinearGradient = new LinearGradient(0.0F, 0.0F,
					0.0F, localBitmap2.getHeight(), -2130706433, 16777215,
					TileMode.CLAMP);
			Paint localPaint = new Paint();
			localPaint.setShader(localLinearGradient);
			localPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
			localCanvas.drawRect(0.0F, 0.0F, i, localBitmap2.getHeight(),
					localPaint);
		}
		return localBitmap2;
	}
}
