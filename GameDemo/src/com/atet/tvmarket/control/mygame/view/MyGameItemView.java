package com.atet.tvmarket.control.mygame.view;

import android.view.ViewGroup;
import android.widget.*;
import com.atet.tvmarket.R;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class MyGameItemView extends RelativeLayout {
		
	private TextView title;
	
	//背景图片
	private ImageView backView;
	
	//阴影图片
	private ImageView shadowView;

	//游戏背景
	private CustomClipLoading gameView;
	
    private TextView tvProgress; // 显示下载进度的tv
    private ImageView ivDownload;  // 正中心表示正在下载的圆圈
    private ImageView ivDeleteState;
//    private ImageView downloadingView;
    private TextView ivUpdateState;
    
	
	//用来标注TvItem的属性
	private int mLocation;
	
	
	public MyGameItemView(Context context){
		this(context, null);
	}
	
	public MyGameItemView(Context context, AttributeSet attr){
		this(context,attr,0);
	}
	
	public MyGameItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 
	 * @description 初始化界面
	 * @param context
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:10:18
	 *
	 */
	public void init(Context context){
		shadowView = (ImageView) findViewById(R.id.shadow);
		gameView = (CustomClipLoading) findViewById(R.id.gameview);
		backView = (ImageView) findViewById(R.id.backview);
		title = (TextView) findViewById(R.id.title);
		tvProgress = (TextView)findViewById(R.id.mygame_tipbar_tv_progress);
		ivDeleteState = (ImageView)findViewById(R.id.mygame_delete_state);
		ivDownload = (ImageView)findViewById(R.id.mygame_download_iv);
		ivUpdateState = (TextView)findViewById(R.id.mygame_cell_update_icon);
	}




	public TextView getTitle() {
		return title;
	}

	public void setTitle(TextView title) {
		this.title = title;
	}

	public ImageView getBackView() {
		return backView;
	}

	public void setBackView(ImageView backView) {
		this.backView = backView;
	}

	public ImageView getShadowView() {
		return shadowView;
	}

	public void setShadowView(ImageView shadowView) {
		this.shadowView = shadowView;
	}

	public CustomClipLoading getGameView() {
		return gameView;
	}

	
	public TextView getTvProgress() {
		return tvProgress;
	}

	public void setTvProgress(TextView tvProgress) {
		this.tvProgress = tvProgress;
	}

	public ImageView getIvState() {
		return ivDownload;
	}

	public void setIvState(ImageView ivState) {
		this.ivDownload = ivState;
	}

	public void setGameView(CustomClipLoading gameView) {
		this.gameView = gameView;
	}

	public ImageView getIvDeleteState() {
		return ivDeleteState;
	}

	public void setIvDeleteState(ImageView ivDeleteState) {
		this.ivDeleteState = ivDeleteState;
	}

	
	public TextView getIvUpdateState() {
		return ivUpdateState;
	}

	public void setIvUpdateState(TextView ivUpdateState) {
		this.ivUpdateState = ivUpdateState;
	}



	
}
