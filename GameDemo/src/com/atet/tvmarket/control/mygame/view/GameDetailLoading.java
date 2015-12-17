package com.atet.tvmarket.control.mygame.view;

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
import android.widget.FrameLayout.LayoutParams;

import com.atet.tvmarket.control.mygame.utils.QRUtil;

/**
 * 自定义游戏详情进度条控件
 * @author chenqingwen
 * 由两张图片实现
 * 一张灰度图，一张原图
 */
public class GameDetailLoading extends FrameLayout{

	private ClipDrawable mClipDrawable;
	private int mProgress = 0;  //当前显示的进度
	private ImageView imageView; // 
	private Context context;
	private  Bitmap detailSrc;  //原图
	private  Bitmap detailBack;  // 灰度图
	// 若当前在下载，判断是否可以开始加载进度条，
	//避免图片未加载出来游戏图时，便开始加载灰度图
	private Boolean isShowOnGameDetail = false;  
	private Boolean isFinish = false; // 判断是否已经下载完成

	private Handler handler= new Handler(){
        int percent;
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
			    percent = msg.arg1;
			    if(isFinish){  // 若已经下载完成则不再初始化灰度图
			    	if(detailBack!=null && !detailBack.isRecycled()){ // 回收bitmap
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
			    imageView = new ImageView(context);  // 添加进度条
			    addView(imageView, new LayoutParams(-1, -1));
				imageView.setBackgroundDrawable(new BitmapDrawable(detailSrc));
				ClipDrawable clipDrawable = new ClipDrawable(new BitmapDrawable(detailBack), Gravity.TOP, 2);
				imageView.setImageDrawable(clipDrawable);
				mClipDrawable = (ClipDrawable) imageView.getDrawable();
				mProgress = percent*100;
				mClipDrawable.setLevel(mProgress);
				invalidate();
				isFinish = false;
				break;
			default:
			}
		}
		
	};
	public GameDetailLoading(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		this.context = context;
		// TODO Auto-generated constructor stub
	}



	public GameDetailLoading(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		// TODO Auto-generated constructor stub
	}



	public GameDetailLoading(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		// TODO Auto-generated constructor stub
	}



	public GameDetailLoading(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}




	/**
	 * 
	 * @description 游戏详情添加下载进度条
	 * @param percent
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:03:15
	 *
	 */
	private void startDownLoadOnGameDetail(final int percent){
			new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(detailBack == null){ // 获取当前控件的bitmap
				        destroyDrawingCache();
					    buildDrawingCache();
					    detailBack = Bitmap.createBitmap(getDrawingCache());
					}
					if(detailSrc==null){  // 转灰度图
				        detailSrc = QRUtil.toGrayscale(detailBack);
					}
					Message msg = handler.obtainMessage();
					msg.what = 0;
					msg.arg1 = percent;
					handler.sendMessage(msg);
				}
				
			}.start();
	}
	
	
	/**
	 * 
	 * @description 界面更新游戏下载进度
	 * @param percent
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:03:33
	 *
	 */
	public void updateGameDetailProgress(int percent){  // 下载进度改变，更新进度条显示
		if(isShowOnGameDetail){
			if(imageView == null && mClipDrawable == null){ // 若未添加进度条，则调用添加进度条函数
				startDownLoadOnGameDetail(percent);
			}else{
				if(imageView != null && mClipDrawable != null){  //若存在，则直接修改
					mProgress = percent*100;
					mClipDrawable.setLevel(mProgress);
				}
			}
		}else{
			return;
		}
	}
	
	/**
	 * 
	 * @description  移除下载进度条
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:03:58
	 *
	 */
	public void removeDrawableOnGameDetail(){  //
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				isFinish = true;
				if(imageView != null){
					isFinish = false;
				}
				if(mClipDrawable != null || imageView!= null){
					  removeView(imageView);
					  mClipDrawable = null;
					  imageView = null;
						if(detailBack!=null && !detailBack.isRecycled()){
						       detailBack.recycle();
							   detailBack = null;
							}
					    if(detailSrc!=null && !detailSrc.isRecycled()){
						     detailSrc.recycle();
						     detailSrc = null;
						 }
					}
			}
		});
	}



	public Boolean getIsShowOnGameDetail() {
		return isShowOnGameDetail;
	}



	public void setIsShowOnGameDetail(Boolean isShowOnGameDetail) {
		this.isShowOnGameDetail = isShowOnGameDetail;
	}	
	
	
}
