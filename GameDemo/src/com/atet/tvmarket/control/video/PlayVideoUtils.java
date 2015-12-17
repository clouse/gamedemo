package com.atet.tvmarket.control.video;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Environment;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.TextureView.SurfaceTextureListener;
import android.widget.VideoView;

public class PlayVideoUtils {
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@SuppressLint("NewApi")
	public static MediaPlayer addVideoView(View arg0, final MediaPlayer mp){
		final Activity activity = (Activity) arg0.getContext();
		final ViewGroup viewGroup = (ViewGroup) arg0.getParent();
		final TextureView videoView = new TextureView(activity);
		mp.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				viewGroup.removeView(videoView);
			}
		});
		if (viewGroup != null) {
			videoView.setSurfaceTextureListener(new SurfaceTextureListener() {
				
				@Override
				public void onSurfaceTextureUpdated(SurfaceTexture arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onSurfaceTextureSizeChanged(SurfaceTexture arg0, int arg1,
						int arg2) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public boolean onSurfaceTextureDestroyed(SurfaceTexture arg0) {
					// TODO Auto-generated method stub
					return true;
				}
				
				@Override
				public void onSurfaceTextureAvailable(SurfaceTexture arg0, int arg1,
						int arg2) {
					// TODO Auto-generated method stub
					Surface s = new Surface(videoView.getSurfaceTexture());
					try {
						mp.setDataSource(Environment.getExternalStorageDirectory()+"/" + "moon.mp4");
						mp.setSurface(s);
						mp.prepare();
						mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
						mp.start();

					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
		
			android.widget.RelativeLayout.LayoutParams layoutParams = new android.widget.RelativeLayout.LayoutParams(viewGroup.getWidth() -4,
					viewGroup.getHeight() -4);
			layoutParams.leftMargin =4;
			layoutParams.rightMargin =4;
			layoutParams.topMargin = 4;
			layoutParams.bottomMargin = 4;
			viewGroup.addView(videoView, layoutParams);
			videoView.setFocusable(false);
			videoView.setFocusableInTouchMode(false);
			arg0.requestFocus();
			return mp;
		}
		return null;
	}
	
	
	public static MyVideoView addVideoView(Context context, View arg0, String path){
		Activity activity = (Activity) context;
		final ViewGroup viewGroup = (ViewGroup) arg0.getParent();
		if (viewGroup != null) {
			final MyVideoView videoView = new MyVideoView(activity);
			videoView.setVideoPath(path);
			android.widget.RelativeLayout.LayoutParams layoutParams = new android.widget.RelativeLayout.LayoutParams(viewGroup.getWidth() -4,
					viewGroup.getHeight() -4);
			layoutParams.leftMargin = 4;
			layoutParams.rightMargin = 4;
			layoutParams.topMargin = 4;
			layoutParams.bottomMargin = 4;
			videoView.setFocusable(false);
			videoView.setFocusableInTouchMode(false);
			viewGroup.addView(videoView, layoutParams);
			videoView.start();
			videoView.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer arg0) {
					// TODO Auto-generated method stub
					viewGroup.removeView(videoView);
				}
			});
			return videoView;
		}	
		return null;
	}
	
	public static MyVideoView addVideoView2(Context context, View arg0, String path, int margain){
		Activity activity = (Activity) context;
		final ViewGroup viewGroup = (ViewGroup) arg0.getParent();
		if (viewGroup != null) {
			final MyVideoView videoView = new MyVideoView(activity);
			videoView.setVideoPath(path);
			android.widget.RelativeLayout.LayoutParams layoutParams = new android.widget.RelativeLayout.LayoutParams(viewGroup.getWidth() -4,
					viewGroup.getHeight() -4);
			layoutParams.leftMargin = margain;
			layoutParams.rightMargin = margain;
			layoutParams.topMargin = margain;
			layoutParams.bottomMargin = margain;
			videoView.setFocusable(false);
			videoView.setFocusableInTouchMode(false);
			viewGroup.addView(videoView, layoutParams);
			videoView.start();
			videoView.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer arg0) {
					// TODO Auto-generated method stub
					viewGroup.removeView(videoView);
				}
			});
			return videoView;
		}	
		return null;
	}
}
