
package com.atet.tvmarket.control.mygame.view;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.atet.tvmarket.R;
import com.atet.tvmarket.control.mygame.utils.QRUtil;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class CustomClipLoading extends FrameLayout {

	private ClipDrawable mClipDrawable; // 可通过设置level 来实现显示多少
	private int mProgress = 0;
	private ImageView imageView;
	private Context context;
	private  Bitmap detailSrc; // 原图
	private  Bitmap detailBack; // 灰度图
	private int count = 0;  // 添加的imageView的个数，避免重复添加
	private String gameId;
	private boolean isFinish = false;  // 判断是否下载完成
	// map数组保存我的游戏中产生的灰度图
//	private  static HashMap<String, Bitmap> srcBitmap = new HashMap<String, Bitmap>(10);
//	private  static HashMap<String, Bitmap>  backBitmap = new HashMap<String, Bitmap>(10);
	
	private Handler handler= new Handler(){
        int percent;
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				   if(isFinish){  // 若已经下载完成则不再初始化灰度图，回收bitmap
				    	if(detailBack!=null && !detailBack.isRecycled()){
						       detailBack.recycle();
							   detailBack = null;
							}
					    if(detailSrc!=null && !detailSrc.isRecycled()){
						     detailSrc.recycle();
						     detailSrc = null;
						 }
						isFinish = false;
				    	return;
				    }
			    percent = msg.arg1;
				MyGameItemView myGameItemView = (MyGameItemView) CustomClipLoading.this.getParent().getParent();
			    imageView = new ImageView(context);
			    // 在当前控件上添加ImageView
			    addView(imageView, new LayoutParams(-1, -1));
			    // 设置imageView底图为灰度图
				imageView.setBackgroundDrawable(new BitmapDrawable(detailSrc));
				// 生成clicpdrawable
				ClipDrawable clipDrawable = new ClipDrawable(new BitmapDrawable(detailBack), Gravity.TOP, 2);
				// 设置在imageView上，通过设置level，则可控制clipdrawble显示多少，以覆盖灰度底图
				imageView.setImageDrawable(clipDrawable);
				mClipDrawable = (ClipDrawable) imageView.getDrawable();
				myGameItemView.getBackView().setVisibility(View.INVISIBLE);
				updateMyGameProgress(percent, false);
				isFinish = false;
				break;
			default:
				break;
			}
		}
		
	};
	public CustomClipLoading(Context context) {
		this(context, null, 0);
		this.context = context;
	}

	public CustomClipLoading(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		this.context = context;
	}

	public CustomClipLoading(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	/**
	 * 
	 * @description 初始化进度条
	 * @param percent
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28下午2:53:58
	 *
	 */
	public synchronized void startDownLoadOnMyGame(final int percent){
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				count++;
				if(detailBack == null){  //获取当前Item的bitmap
			        destroyDrawingCache();
				    buildDrawingCache();
				    detailBack = Bitmap.createBitmap(getDrawingCache());
				}
				if(detailSrc==null){  // 获得灰度图
			        detailSrc = QRUtil.toGrayscale(detailBack);
				}
				Message msg = handler.obtainMessage();
				msg.what = 1;
				msg.arg1 = percent;
				handler.sendMessage(msg);
		
			}
		});

	}
	
	
	

	/**
	 * 
	 * @description  设置进度
	 * @param percent 进度
	 * @param isreset 
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:01:24
	 *
	 */
	public synchronized void updateMyGameProgress(int percent, boolean isreset){
		if(imageView != null && mClipDrawable != null){  // 若尚未初始化灰度图
			if(percent > mProgress/100){
			  mProgress = percent*100;
			  mClipDrawable.setLevel(mProgress);
			}
		}else{
			if(count == 0)
			startDownLoadOnMyGame(percent);
		}
	}
	
	
	

	/**
	 * 
	 * @description 移除我的游戏下载进度条
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:02:09
	 *
	 */
	public void removeDrawableOnMyGame(){
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				isFinish = true;  // 避免瞬间下载的情况，下载完，但灰度尚未初始化，此时便不需要再添加灰度图了
				if(imageView != null){
					isFinish = false;
					// 移除添加的imageView
					removeView(imageView);
				}
				mClipDrawable = null;
//				removeSingleBitmap(gameId);
				if(detailBack!=null && !detailBack.isRecycled()){
				       detailBack.recycle();
					   detailBack = null;
					}
			    if(detailSrc!=null && !detailSrc.isRecycled()){
				     detailSrc.recycle();
				     detailSrc = null;
				 }
			    MyGameItemView myGameItemView = (MyGameItemView) CustomClipLoading.this.getParent().getParent();
				myGameItemView.getBackView().setVisibility(View.VISIBLE);
			}
		});
	}
	


	/**
	 * 删除我的游戏对应的Bitmap缓存
	 * @param i
	 */
//	public static void removeSingleBitmap(String gameId){
//		Bitmap temp = backBitmap.get(gameId);
//		if(temp!= null && !temp.isRecycled()){
//			backBitmap.remove(gameId);
//			temp.recycle();
//		}
//		Bitmap tempSrc = srcBitmap.get(gameId);
//		if(tempSrc != null && ! tempSrc.isRecycled()){
//		   srcBitmap.remove(gameId);
//		   tempSrc.recycle();
//		}
//	}
	
	/**
	 * 清空我的游戏界面对应的Bitmap缓存
	 */
//	public static void removeAllBitamps(){
//		Iterator iter1 = backBitmap.keySet().iterator();
//		while (iter1.hasNext()) {
//			Bitmap temp = backBitmap.get(iter1.next());
//			if(temp != null){
//				temp.recycle();
//				temp = null;
//			}
//			
//		}
//		Iterator iter2 = srcBitmap.keySet().iterator();
//		while (iter2.hasNext()) {
//			Bitmap temp = srcBitmap.get(iter2.next());
//			if(temp != null){
//				temp.recycle();
//				temp = null;
//			}
//			
//		}
//		backBitmap.clear();
//		srcBitmap.clear();
//	}
//	
	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}	
	
}
