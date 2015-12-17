package com.atet.tvmarket.control.gamerecommand;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.view.View.OnKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.atet.tvmarket.R;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.entity.dao.ScreenShotInfo;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.dk.animation.effect.ViewUtils;

public class NewGameScreenShotActivity extends BaseActivity {
	private ImageView screenImg,leftImg,rightImg;
	private List<ScreenShotInfo> mScreenShotInfos = new ArrayList<ScreenShotInfo>();
	private int position=0;
	private LinearLayout content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		
		if(getIntent()!=null){
			List<ScreenShotInfo> images = (List<ScreenShotInfo>) getIntent().getSerializableExtra("images");
			if(images!=null && images.size()>0){
				mScreenShotInfos.addAll(images);
			}
			
			position = getIntent().getIntExtra("position", 0);
		}
		setContentView(R.layout.newgame_screenshot_show);
		ScaleViewUtils.init(this);
		ScaleViewUtils.scaleView(getWindow().getDecorView());
		
		content = (LinearLayout)findViewById(R.id.ll_content);
		screenImg = (ImageView)findViewById(R.id.iv_screen);
		leftImg = (ImageView)findViewById(R.id.iv_screen_left);
		rightImg = (ImageView)findViewById(R.id.iv_screen_right);
		
		if(mScreenShotInfos.size()>0){
			if(position==0){
				leftImg.setVisibility(View.INVISIBLE);
				rightImg.setVisibility(View.VISIBLE);
				mImageFetcher.loadImage(mScreenShotInfos.get(position).getPhotoUrl(), screenImg, R.drawable.default_cross);
				mImageFetcher.loadImage(mScreenShotInfos.get(position+1).getPhotoUrl(), rightImg, R.drawable.default_cross);
			}
			else if(position==mScreenShotInfos.size()-1){
				leftImg.setVisibility(View.VISIBLE);
				rightImg.setVisibility(View.INVISIBLE);
				mImageFetcher.loadImage(mScreenShotInfos.get(position).getPhotoUrl(), screenImg, R.drawable.default_cross);
				mImageFetcher.loadImage(mScreenShotInfos.get(position-1).getPhotoUrl(), leftImg, R.drawable.default_cross);
			}
			else{
				leftImg.setVisibility(View.VISIBLE);
				rightImg.setVisibility(View.VISIBLE);
				mImageFetcher.loadImage(mScreenShotInfos.get(position).getPhotoUrl(), screenImg, R.drawable.default_cross);
				mImageFetcher.loadImage(mScreenShotInfos.get(position-1).getPhotoUrl(), leftImg, R.drawable.default_cross);
				mImageFetcher.loadImage(mScreenShotInfos.get(position+1).getPhotoUrl(), rightImg, R.drawable.default_cross);
			}
		}
		screenImg.requestFocus();
		screenImg.setOnKeyListener(onKeyListener);
	}
	
	OnKeyListener onKeyListener = new OnKeyListener() {
		
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			
			if(event.getAction()==KeyEvent.ACTION_DOWN){
				if(keyCode==KeyEvent.KEYCODE_DPAD_LEFT){
					left();
					return true;
				}
				else if(keyCode==KeyEvent.KEYCODE_DPAD_RIGHT){
					right();
					return true;
				}
				/*else if(keyCode==KeyEvent.KEYCODE_DPAD_UP){
					up();
					return true;
				}
				else if(keyCode==KeyEvent.KEYCODE_DPAD_DOWN){
					down();
					return true;
				}*/
			}
			
			return false;
		}
	};
	
	private void left(){
		if(position==0){
			return;
		}
		else{
			position--;
		}
		
		/*leftIn(leftImg, 0);
		leftIn(screenImg, 0);
		leftIn(rightImg, 0);*/
		leftIn(content,0);
		if(position==0){
			leftImg.setVisibility(View.INVISIBLE);
			rightImg.setVisibility(View.VISIBLE);
			mImageFetcher.loadImage(mScreenShotInfos.get(position).getPhotoUrl(), screenImg, R.drawable.default_cross);
			mImageFetcher.loadImage(mScreenShotInfos.get(position+1).getPhotoUrl(), rightImg, R.drawable.default_cross);
		}
		else if(position==mScreenShotInfos.size()-1){
			leftImg.setVisibility(View.VISIBLE);
			rightImg.setVisibility(View.INVISIBLE);
			mImageFetcher.loadImage(mScreenShotInfos.get(position).getPhotoUrl(), screenImg, R.drawable.default_cross);
			mImageFetcher.loadImage(mScreenShotInfos.get(position-1).getPhotoUrl(), leftImg, R.drawable.default_cross);
		}
		else{
			leftImg.setVisibility(View.VISIBLE);
			rightImg.setVisibility(View.VISIBLE);
			mImageFetcher.loadImage(mScreenShotInfos.get(position).getPhotoUrl(), screenImg, R.drawable.default_cross);
			mImageFetcher.loadImage(mScreenShotInfos.get(position-1).getPhotoUrl(), leftImg, R.drawable.default_cross);
			mImageFetcher.loadImage(mScreenShotInfos.get(position+1).getPhotoUrl(), rightImg, R.drawable.default_cross);
		}
		
	}
	private void right(){
		if(position==mScreenShotInfos.size()-1){
			return ;//position=0;
		}
		else{
			position++;
		}
		/*rightIn(leftImg, 0);
		rightIn(screenImg, 0);
		if(position<mScreenShotInfos.size()-1){
			rightImg.setVisibility(View.INVISIBLE);
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					rightImg.setVisibility(View.VISIBLE);
					rightIn(rightImg, 0);
				}
			}, 400);
		}*/
		rightIn(content,0);
        if(position==0){
			leftImg.setVisibility(View.INVISIBLE);
			rightImg.setVisibility(View.VISIBLE);
			mImageFetcher.loadImage(mScreenShotInfos.get(position).getPhotoUrl(), screenImg, R.drawable.default_cross);
			mImageFetcher.loadImage(mScreenShotInfos.get(position+1).getPhotoUrl(), rightImg, R.drawable.default_cross);
		}
		else if(position==mScreenShotInfos.size()-1){
			leftImg.setVisibility(View.VISIBLE);
			rightImg.setVisibility(View.INVISIBLE);
			mImageFetcher.loadImage(mScreenShotInfos.get(position).getPhotoUrl(), screenImg, R.drawable.default_cross);
			mImageFetcher.loadImage(mScreenShotInfos.get(position-1).getPhotoUrl(), leftImg, R.drawable.default_cross);
		}
		else{
			leftImg.setVisibility(View.VISIBLE);
			rightImg.setVisibility(View.VISIBLE);
			mImageFetcher.loadImage(mScreenShotInfos.get(position).getPhotoUrl(), screenImg, R.drawable.default_cross);
			mImageFetcher.loadImage(mScreenShotInfos.get(position-1).getPhotoUrl(), leftImg, R.drawable.default_cross);
			mImageFetcher.loadImage(mScreenShotInfos.get(position+1).getPhotoUrl(), rightImg, R.drawable.default_cross);
		}
	}
	
	public void leftIn(View view,long delay){
		ViewCompat.setTranslationX(view, -screenImg.getWidth());
		ViewCompat.animate(view).cancel();
        ViewCompat.animate(view).translationX(0).translationY(0).setDuration(400).setStartDelay(delay);
	}
	
	public void leftOut(View view,long delay){
		ViewCompat.setTranslationX(view, 0);
		ViewCompat.setTranslationY(view, 0);
		ViewCompat.animate(view).cancel();
        ViewCompat.animate(view).translationX(-view.getWidth()).translationY(0).setDuration(400).setStartDelay(delay);
	}
	
	public void rightIn(View view,long delay){
		ViewCompat.setTranslationX(view, view.getWidth());
		ViewCompat.animate(view).cancel();
        ViewCompat.animate(view).translationX(0).translationY(0).setDuration(400).setStartDelay(delay);
	}
	
	public void rightOut(View view,long delay){
		ViewCompat.setTranslationX(view, 0);
		ViewCompat.setTranslationY(view, 0);
		ViewCompat.animate(view).cancel();
        ViewCompat.animate(view).translationX(view.getWidth()).translationY(0).setDuration(400).setStartDelay(delay);
	}
}
