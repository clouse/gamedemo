package com.atet.tvmarket.control.gamerecommand;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.control.gamerecommand.anim.TranslationLeftIn;
import com.atet.tvmarket.control.gamerecommand.anim.TranslationRightIn;
import com.atet.tvmarket.control.mygame.activity.MyGameActivity;
import com.atet.tvmarket.control.video.PlayVideoActivity;
import com.atet.tvmarket.control.video.PlayVideoUtils;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.entity.dao.ScreenShotInfo;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.net.http.download.BtnDownCommonListener;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.GameAdapterTypeUtil;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.ScreenShot;
import com.atet.tvmarket.view.LoadingView;
import com.nineoldandroids.animation.AnimatorSet;

public class NewGameRecommandActivity extends BaseActivity {
	ALog alog = ALog.getLogger(NewGameRecommandActivity.class);
	private TextView title;
	private RecyclerView mRecyclerView;
	private RatingBar commentScore;
	private TextView downLoadCount,gameSize,versionCode;
	private Button btnDownLaod;
	private ImageView coverBorder, hanldeIcon,controlIcon;
	private List<ImageView> imgs = new ArrayList<ImageView>(); 
	
	private LinearLayout gameView, detailView,coverView;
	private RelativeLayout gameNameView;
	
	private TextView gameName;
	
	private NewGameRecommandAdapter1 mAdapter;
	List<GameInfo> games = new ArrayList<GameInfo>();
	private AnimatorSet mAnimatorSet;
    {
        mAnimatorSet = new AnimatorSet();
    }
	
    private RelativeLayout contentView;
    private LoadingView loadingView;
    
  	private GameInfo mGameInfo;
  	private List<ScreenShotInfo> mScreenShotInfos = new ArrayList<ScreenShotInfo>();
  	private BtnDownCommonListener btnDownCommonListener;
  	
  	private RecommanedPanel panel;
  	public String videoUrl="";
  	private View touchView=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		
		setContentView(R.layout.activity_newgamerecommand);
		ScaleViewUtils.init(this);
		ScaleViewUtils.scaleView(getWindow().getDecorView());
		mHandler.sendEmptyMessage(1);
		
		setBlackTitle(false);
		
		title = (TextView)findViewById(R.id.tv_title);
		
		coverBorder = (ImageView)findViewById(R.id.iv_border);
		coverBorder.setOnFocusChangeListener(onFocusChangeListener);
		coverBorder.setOnKeyListener(onKeyListener);
		coverBorder.setOnTouchListener(onTouchListener);
		
		mRecyclerView = (RecyclerView)findViewById(R.id.rv_gamerecommand);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(RecyclerView.HORIZONTAL);
		mRecyclerView.setLayoutManager(layoutManager);
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setItemViewCacheSize(0);
		mRecyclerView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		
		gameView = (LinearLayout)findViewById(R.id.ll_game);
		detailView = (LinearLayout)findViewById(R.id.ll_game_detail);
		coverView = (LinearLayout)findViewById(R.id.ll_game_cover);
		gameNameView = (RelativeLayout)findViewById(R.id.rl_gamename);
		
		setViewVisible(View.INVISIBLE);
		
		gameName = (TextView)findViewById(R.id.tv_gamename);
		
		commentScore = (RatingBar)findViewById(R.id.rb_comment_score);
		downLoadCount = (TextView)findViewById(R.id.tv_download_count);
		gameSize = (TextView)findViewById(R.id.tv_game_size);
		versionCode = (TextView)findViewById(R.id.tv_game_version);
		hanldeIcon = (ImageView)findViewById(R.id.iv_handle_icon);
		controlIcon = (ImageView)findViewById(R.id.iv_control_icon);
		btnDownLaod = (Button)findViewById(R.id.btn_download);
		btnDownLaod.setOnFocusChangeListener(onFocusChangeListener);
		btnDownLaod.setOnKeyListener(onKeyListener);
		btnDownLaod.setOnTouchListener(touchListener);
		
		panel =(RecommanedPanel)findViewById(R.id.rp_panel);
		panel.getOneView().getTopView().setTag(0);
		panel.getOneView().getBottomView().setTag(1);
		panel.getTwoView().getTopView().setTag(2);
		panel.getTwoView().getBottomView().setTag(3);
		imgs.add(panel.getOneView().getTopCover());
		imgs.add(panel.getOneView().getBottomCover());
		imgs.add(panel.getTwoView().getTopCover());
		imgs.add(panel.getTwoView().getBottomCover());
		
		mAdapter = new NewGameRecommandAdapter1(mRecyclerView,this,mImageFetcher);
		mRecyclerView.setAdapter(mAdapter);
		
		contentView = (RelativeLayout)findViewById(R.id.rl_content);
		loadingView = (LoadingView)findViewById(R.id.contentLoading);
		loadingView.setDataView(contentView);
		registReceiver();
		getData();
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		setTitleData();
	}
	private void setViewVisible(int val){
		for(int i=0;i<detailView.getChildCount();i++){
			detailView.getChildAt(i).setVisibility(val);
		}
		for(int i=0;i<gameNameView.getChildCount();i++){
			gameNameView.getChildAt(i).setVisibility(val);
		}
		for(int i=0;i<coverView.getChildCount();i++){
			coverView.getChildAt(i).setVisibility(val);
		}
	}
	
	public void setCoverBorderFocus(){
		coverBorder.setVisibility(View.VISIBLE);
		coverBorder.requestFocus();
	}
	
	public void setBigBorderFocus(){
		panel.getRoot().requestFocus();
	}
	
	public void openVideo(){
		if(panel.getVideo().getVisibility()==View.VISIBLE){
			Intent intent = new Intent(NewGameRecommandActivity.this, PlayVideoActivity.class);
			intent.putExtra("videoUrl", videoUrl);
			Log.i("life", "videoUrl:"+videoUrl);
			intent.putExtra("isGameVideo", true);
			if(mGameInfo!=null){
				intent.putExtra("gameId", mGameInfo.getGameId());
			}else{
				intent.putExtra("gameId", "");
			}
			startActivity(intent);
		}
	}
	
	public void openShot(int smallPos){
		Intent intent = new Intent(NewGameRecommandActivity.this,NewGameScreenShotActivity.class);
		intent.putExtra("images", (Serializable)mScreenShotInfos);
		intent.putExtra("position", smallPos);
		startActivity(intent);
	}
	
	OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus){
				
				if(v==coverBorder){
					coverBorder.setVisibility(View.VISIBLE);
				}
				if(v.isInTouchMode() && v==touchView){
					v.performClick();
				}
				else{
					touchView = null;
				}
			}
			else{
				if(v==coverBorder){
					coverBorder.setVisibility(View.INVISIBLE);
				}
			}
		}
	};
	
	OnTouchListener onTouchListener = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				OnFocusChangeListener listener = v.getOnFocusChangeListener();
				if (listener != null) {
					touchView = v;
				}
			}
			return false;
		}
	};
	
	private long privousTime=0;
	
	OnKeyListener onKeyListener = new OnKeyListener() {
		
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if(event.getAction() == KeyEvent.ACTION_DOWN){
				
				if(v==coverBorder){
					if(games.size()==1){
						return true;
					}
					else if(keyCode==KeyEvent.KEYCODE_DPAD_LEFT){
						if(privousTime==0){
							privousTime = System.currentTimeMillis();
							leftMove(v);
							
						}
						else{
							if(System.currentTimeMillis()-privousTime>500){
								privousTime = System.currentTimeMillis();
								leftMove(v);
							}
						}
						return true;
					}
					else if(keyCode==KeyEvent.KEYCODE_DPAD_RIGHT){
						if(privousTime==0){
							privousTime = System.currentTimeMillis();
							rightMove(v);
						}
						else{
							if(System.currentTimeMillis()-privousTime>500){
								privousTime = System.currentTimeMillis();
								rightMove(v);
							}
						}
						return true;
					}
					else if(keyCode==KeyEvent.KEYCODE_DPAD_DOWN){
						btnDownLaod.requestFocus();
						return true;
					}
				}
				
				else if(v==btnDownLaod){
					if(keyCode==KeyEvent.KEYCODE_DPAD_LEFT||keyCode==KeyEvent.KEYCODE_DPAD_DOWN){
						return true;
					}
					else if(keyCode==KeyEvent.KEYCODE_DPAD_RIGHT){
						panel.getRoot().requestFocus();
						return true;
					}
					else if(keyCode==KeyEvent.KEYCODE_DPAD_UP){
						coverBorder.setVisibility(View.VISIBLE);
						coverBorder.requestFocus();
						return true;
					}
				}
			}
			return false;
		}
	};
	
	private void leftMove(View v){
		mRecyclerView.setItemAnimator(new TranslationLeftIn());
		GameInfo gameInfo = games.get(games.size()-1);
		games.remove(games.size()-1);
		games.add(0,gameInfo);
		mAdapter.setData(games, 1);
		visibleView(games.get(0));
	}
	private void rightMove(View v){
		mRecyclerView.setItemAnimator(new TranslationRightIn());
		GameInfo gameInfo = games.get(0);
		games.remove(0);
		games.add(gameInfo);
		mAdapter.setData(games, 1);
		
		visibleView(games.get(0));
	}
	
	public void viewAnimator(){
		
		setViewVisible(View.INVISIBLE);
		
		ViewCompat.setAlpha(detailView, 0f);
		ViewCompat.setPivotX(detailView, 0);
		ViewCompat.setPivotY(detailView, 0);
		ViewCompat.animate(detailView).cancel();
		ViewCompat.animate(detailView).scaleY(1.0f).alpha(1.0f).setDuration(400).start();
		
		ViewCompat.setAlpha(gameNameView, 0f);
		ViewCompat.setPivotX(gameNameView, 0);
		ViewCompat.setPivotY(gameNameView, 0);
		ViewCompat.animate(gameNameView).cancel();
		ViewCompat.animate(gameNameView).scaleX(1.0f).alpha(1.0f).setDuration(400).setStartDelay(400).start();
		
		ViewCompat.setAlpha(coverView, 0f);
		ViewCompat.setPivotX(coverView, 0);
		ViewCompat.setPivotY(coverView, 0);
		ViewCompat.animate(coverView).cancel();
		ViewCompat.animate(coverView).scaleX(1.0f).alpha(1.0f).setDuration(400).setStartDelay(400).start();
	}
	
	public void visibleView(final GameInfo gameInfo){
		
		setViewVisible(View.VISIBLE);
		if(btnDownCommonListener!=null){
			btnDownCommonListener.recycle();
		}
		mGameInfo = gameInfo;
		
        gameName.setText(gameInfo.getGameName());
        if(gameInfo.getStartLevel()!=null){
        	 commentScore.setRating(Float.valueOf(gameInfo.getStartLevel().toString()));
        }
        else{
        	commentScore.setRating(0);
        }
        downLoadCount.setText(gameInfo.getGameDownCount()+"");
        versionCode.setText("V"+gameInfo.getVersionName());
        gameSize.setText((gameInfo.getGameSize()/1024/1024)+"M");
        GameAdapterTypeUtil.decideAdapter(gameInfo.getHandleType(),hanldeIcon,controlIcon);
        ScreenShotInfo videoScreenShotInfo=null;
        if(gameInfo.getImgs()!=null && gameInfo.getImgs().size()>0){
        	mScreenShotInfos.clear();
        	mScreenShotInfos.addAll(gameInfo.getImgs());
        	
        	for(int i=0;i<mScreenShotInfos.size();i++){
        		if(mScreenShotInfos.get(i).getType()==2){
        			videoScreenShotInfo = mScreenShotInfos.get(i);
        			mScreenShotInfos.remove(i);
        			break;
        		}
            }
        	int tmp=0;
        	for(int i=0;i<mScreenShotInfos.size();i++){
        		if(mScreenShotInfos.get(i).getType()==1){
        			mImageFetcher.loadImage(mScreenShotInfos.get(i).getPhotoUrl(), imgs.get(i), R.drawable.default_cross);
        			if(tmp++==4){
        				break;
        			}
        		}
            }
        	if(videoScreenShotInfo!=null){
        		//显示播放视屏图标
        		panel.getVideo().setVisibility(View.VISIBLE);
        		panel.getCover().setVisibility(View.VISIBLE);
        		panel.getDescText().setVisibility(View.INVISIBLE);
        		mImageFetcher.loadImage(gameInfo.getMaxPhoto(), panel.getCover(), R.drawable.default_cross);
        		videoUrl = videoScreenShotInfo.getPhotoUrl();
        	}
        	else{
        		//隐藏播放视屏图标
        		panel.getVideo().setVisibility(View.INVISIBLE);
        		panel.getCover().setVisibility(View.INVISIBLE);
        		panel.getDescText().setVisibility(View.VISIBLE);
        		panel.getDescText().setText("  游戏简介 : "+gameInfo.getRemark());
        	}
        	
        	
        }
        else{
        	panel.getVideo().setVisibility(View.INVISIBLE);
    		panel.getCover().setVisibility(View.INVISIBLE);
    		panel.getDescText().setVisibility(View.VISIBLE);
    		panel.getDescText().setText("  游戏简介 : "+gameInfo.getRemark());
        	mImageFetcher.loadImage("", imgs.get(0), R.drawable.default_cross);
            mImageFetcher.loadImage("", imgs.get(1), R.drawable.default_cross);
            mImageFetcher.loadImage("", imgs.get(2), R.drawable.default_cross);
            mImageFetcher.loadImage("", imgs.get(3), R.drawable.default_cross);
        }
        
        //数据统计来源
        gameInfo.setTypeName("GameLatest");
        
        btnDownCommonListener = new BtnDownCommonListener(this);
		btnDownCommonListener.listen(btnDownLaod, gameInfo);
        
        coverBorder.requestFocus();
        
        
        long delayTime = 0;
        
        for(int i=0;i<detailView.getChildCount();i++){
        	View view  = detailView.getChildAt(i);
        	if(view!=null){
        		delayTime +=100;
        		ViewCompat.setAlpha(view, 0f);
            	ViewCompat.animate(view).cancel();
            	ViewCompat.animate(view).alpha(1.0f).setDuration(100).setStartDelay(delayTime).start();
        	}
        	
        }
        
        for(int i=0;i<gameNameView.getChildCount();i++){
        	View view  = gameNameView.getChildAt(i);
        	if(view!=null){
        		delayTime +=100;
        		ViewCompat.setAlpha(view, 0f);
            	ViewCompat.animate(view).cancel();
            	ViewCompat.animate(view).alpha(1.0f).setDuration(100).setStartDelay(delayTime).start();
        	}
        	
        }
        
        for(int i=0;i<panel.getChildCount();i++){
        	View view  = panel.getChildAt(i);
        	if(view!=null){
        		delayTime +=100;
        		ViewCompat.setAlpha(view, 0f);
            	ViewCompat.animate(view).cancel();
            	ViewCompat.animate(view).alpha(1.0f).setDuration(100).setStartDelay(delayTime).start();
        	}
        }
	}
	
	private void getData(){
		
		ReqCallback<List<GameInfo>> reqCallback = new ReqCallback<List<GameInfo>>() {
			
			@Override
			public void onGetCacheData(String requestTag, boolean result) {
				if(!result){
					loadingView.getmHandler().sendEmptyMessage( Constant.SHOWLOADING);
				}
			}
			
			@Override
			public void onResult(TaskResult<List<GameInfo>> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					List<GameInfo> info = taskResult.getData();
					if (info != null && !info.isEmpty()) {
						alog.info(info.toString());
						games.clear();
						games.addAll(info);
						mHandler.sendEmptyMessage(0);
						
						loadingView.getmHandler().sendEmptyMessage(Constant.DISMISSLOADING);
					}
					else{
						if(NetUtil.isNetworkAvailable(NewGameRecommandActivity.this, true)){
							loadingView.getmHandler().sendEmptyMessage(Constant.NULLDATA);
						}
						else{
							loadingView.getmHandler().sendEmptyMessage(Constant.EXCEPTION);
						}
					}
				}
				else{
					if(NetUtil.isNetworkAvailable(NewGameRecommandActivity.this, true)){
						loadingView.getmHandler().sendEmptyMessage(Constant.NULLDATA);
					}
					else{
						loadingView.getmHandler().sendEmptyMessage(Constant.EXCEPTION);
					}
				}

			}

			@Override
			public void onUpdate(TaskResult<List<GameInfo>> taskResult) {

			}
		};
		DataFetcher.getGameNewUploaded(this, reqCallback, false)
				.registerUpdateListener(reqCallback).request(this);
	}
	
	 @Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if(arg1 == RESULT_OK){
			btnDownCommonListener.startDownloadGame();
		}

	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		alog.info("ondestroy");
		mAnimatorSet.cancel();
		if(ScreenShot.screenShotBmp!=null){
			ScreenShot.screenShotBmp.recycle();
			ScreenShot.screenShotBmp=null;
		}
		unregistReceiver();
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();
		if (action == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_BUTTON_X || keyCode == KeyEvent.KEYCODE_X || keyCode == KeyEvent.KEYCODE_MENU) {
				getData();
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}
	
	private boolean isFirstRegister = false;
	private boolean isConnected = true;
	/**
	 * @author htliu
	 * @description 用于监听网络状态变化的广播接受者
	 */
	BroadcastReceiver netBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context mContext, Intent intent) {
			try {
				if (!isFirstRegister) {
					NetworkInfo networkInfo = null;
					ConnectivityManager cm = (ConnectivityManager) mContext
							.getSystemService(Context.CONNECTIVITY_SERVICE);
					networkInfo = cm.getActiveNetworkInfo();
					alog.debug("接收网络监听广播");
					if (null != networkInfo) {
						boolean isConnect = networkInfo
								.isConnectedOrConnecting();
						alog.debug( "netBroadcastReceiver isConnect = "
										+ isConnect);
						if (isConnect) {
							if(!isConnected){
								isConnected = true;
								getData();
							}
						}
					} else {
						// 网络断开了
						isConnected = false;
						alog.debug("当前网络断开了");
						//loadingView.showContentNetExceptionOrNullData(true);
						loadingView.getmHandler().sendEmptyMessage(Constant.EXCEPTION);
					}
				} else {
					isFirstRegister = false;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	};
	
	/**
	 * 
	 * @Title: registReceiver
	 * @Description: TODO(注册广播监听网络变化)
	 * @param:
	 * @return: void
	 * @throws
	 */
	public void registReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		alog.debug("registReceiver()");
		registerReceiver(netBroadcastReceiver, intentFilter);
		isFirstRegister = true;
	}

	/**
	 * 
	 * @Title: unregistReceiver
	 * @Description: TODO(注销广播监听)
	 * @param:
	 * @return: void
	 * @throws
	 */
	public void unregistReceiver() {
		alog.debug("unregistReceiver()");
		unregisterReceiver(netBroadcastReceiver);
	}
	
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				visibleView(games.get(0));
				mAdapter.setData(games,0);
				break;
			case 1:
				break;

			default:
				break;
			}
		}
		
	};
	
}
