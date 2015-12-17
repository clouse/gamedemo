package com.atet.tvmarket.control.classify.special;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.classify.special.anim.AlphaInItemAnimator;
import com.atet.tvmarket.control.classify.special.anim.TranslationLeftIn;
import com.atet.tvmarket.control.classify.special.detail.GameSpecialDetailAdapter;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.control.classify.special.anim.TranslationRightIn;
import com.atet.tvmarket.control.mygame.activity.MyGameActivity;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.entity.dao.GameTopicInfo;
import com.atet.tvmarket.entity.dao.TopicToGame;
import com.atet.tvmarket.model.DataConfig;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.view.FooterView;
import com.atet.tvmarket.view.LoadingView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

public class GameSpecialActivity extends BaseActivity {
	ALog alog = ALog.getLogger(GameSpecialActivity.class);
	private TextView title;//,gameName
	
	private FrameLayout fborder,flist;
	private ImageView cover, left,right,specialbg;
	
	private RecyclerView mRecyclerView,mRecyclerViewDetail;
	private GameSpecialAdapter1 mAdapter;
	private GameSpecialDetailAdapter mDetailAdapter;
	
	List<GameTopicInfo> gameTopics = new ArrayList<GameTopicInfo>();
	List<TopicToGame> games = new ArrayList<TopicToGame>();
	GameTopicInfo mGameTopicInfo; 
	
	private Timer timer;
	private int timerCount=0;
	private int detailState=0;//开发游戏详情状态，0：未打开，1、完全打开，2、动画为执行完成
	
	private RelativeLayout root, contentView;
	private LoadingView loadingView;//
	public View listFocusView=null;
	//专题游戏加载控件
	RelativeLayout loadingLayout,nulldata,exception;
	private ImageView loadDetail;
	private AnimationDrawable animationDrawable;
	private FooterView footer1,footer2;
	private List<View> animViews  = new ArrayList<View>();
	private long refreshTime = 0;
	public GameInfo downloadGameInfo;
	public boolean isFinishing = false;
	public boolean isOpenDetail = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		
		setContentView(R.layout.activity_gameclassify_special1);
		ScaleViewUtils.init(this);
		ScaleViewUtils.scaleView(getWindow().getDecorView());
		
		root = (RelativeLayout)findViewById(R.id.rl_root);
		
		setBlackTitle(true);
		
		title = (TextView)findViewById(R.id.tv_title);
		
		fborder = (FrameLayout)findViewById(R.id.fl_border);
		
		flist = (FrameLayout)findViewById(R.id.fl_list);
		cover = (ImageView)findViewById(R.id.iv_cover);
		left = (ImageView)findViewById(R.id.iv_left);
		right = (ImageView)findViewById(R.id.iv_right);
		
		specialbg = (ImageView)findViewById(R.id.iv_specialbg);
		
		//gameName = (TextView)findViewById(R.id.tv_name);
		
		mRecyclerView = (RecyclerView)findViewById(R.id.rv_special_gameclassify);
		LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
		mRecyclerView.setLayoutManager(layoutManager);
		mAdapter = new GameSpecialAdapter1(mRecyclerView,this,mImageFetcher);
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.setHasFixedSize(true);
		
		mRecyclerViewDetail = (RecyclerView)findViewById(R.id.rv_special_detail);
		mDetailAdapter =  new GameSpecialDetailAdapter(mRecyclerViewDetail,this,mImageFetcher);
		mRecyclerViewDetail.setAdapter(mDetailAdapter);
		
		LayoutManager detailLayoutManager = new GridLayoutManager(this, 2,GridLayoutManager.HORIZONTAL,false);
		mRecyclerViewDetail.setLayoutManager(detailLayoutManager);
		mRecyclerViewDetail.setItemAnimator(new AlphaInItemAnimator());
		
		contentView = (RelativeLayout)findViewById(R.id.rl_content);
		loadingView = (LoadingView)findViewById(R.id.contentLoading);
		loadingView.setDataView(contentView);
		
		loadingLayout = (RelativeLayout)findViewById(R.id.rl_detail_loading);
		loadDetail = (ImageView)findViewById(R.id.iv_load_anim);
		animationDrawable = (AnimationDrawable) loadDetail.getDrawable();
		nulldata = (RelativeLayout)findViewById(R.id.rl_null_data_tv);
		exception = (RelativeLayout)findViewById(R.id.rl_net_exception_tv);
		
		footer1 = (FooterView)findViewById(R.id.footer1);
		footer2 = (FooterView)findViewById(R.id.footer2);
		
		registReceiver();
		
		getGameTopics();
	}
	@Override
	protected void onResume() {
		super.onResume();
		setTitleData();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregistReceiver();
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == RESULT_OK){
			if(downloadGameInfo!=null){
				MyGameActivity.addToMyGameList(GameSpecialActivity.this,downloadGameInfo);
			}
			
		}
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		
		int action = event.getAction();
		int keyCode = event.getKeyCode();
		if (action == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_BUTTON_X || keyCode == KeyEvent.KEYCODE_X || keyCode == KeyEvent.KEYCODE_MENU) {
				if(System.currentTimeMillis() - refreshTime>=1000){
					refreshTime = System.currentTimeMillis();
					if(flist.getVisibility()==View.VISIBLE){
						getGameTopics();
						if(mRecyclerView.getChildCount()>0){
							mRecyclerView.smoothScrollToPosition(0);
						}
					}
					else{
						mDetailAdapter.setLastFocusView(null);
						getGameInfosFromGameTopic(gameTopics.get(mAdapter.startPos%gameTopics.size()));
					}
				}
				return true;
			}
		}
		if(keyCode==KeyEvent.KEYCODE_DPAD_CENTER||keyCode==KeyEvent.KEYCODE_A
				||keyCode==KeyEvent.KEYCODE_BUTTON_A||keyCode == KeyEvent.KEYCODE_ENTER){
			if(flist.getVisibility()==View.VISIBLE){
				OpenDetail();
				return true;
			}
		}
		if(keyCode==KeyEvent.KEYCODE_B||keyCode==KeyEvent.KEYCODE_BUTTON_B||keyCode==KeyEvent.KEYCODE_BACK){
			if(flist.getVisibility()!=View.VISIBLE){
				if(detailState==1){
					detailState=2;
					mHandler.sendEmptyMessage(3);
				}
				return true;
			}
			else{
				finish();
				return true;
			}
		}
		
		return super.dispatchKeyEvent(event);
	}


	private long privousTime=0;

	OnKeyListener onKeyListener = new OnKeyListener() {
		
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if(event.getAction()==KeyEvent.ACTION_DOWN){
				if(v.getId() == fborder.getId() && flist.getVisibility()==View.VISIBLE){
					if(gameTopics.size()==1){
						return true;
					}
				}
				else if(mRecyclerViewDetail.getVisibility()==View.VISIBLE){
					if(keyCode==KeyEvent.KEYCODE_DPAD_RIGHT){
						mRecyclerViewDetail.getLayoutManager().findViewByPosition(0).requestFocus();
					}
				}
				
				return true;
			}
			return false;
		}
	};
	
	OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			OpenDetail();
		}
	};
	
	private void OpenDetail(){
		fborder.setOnClickListener(null);
		fborder.clearFocus();
		fborder.setFocusable(false);
		fborder.setFocusableInTouchMode(false);
		flist.setVisibility(View.INVISIBLE);
		right.setVisibility(View.INVISIBLE);
		cover.setVisibility(View.VISIBLE);
		specialbg.setVisibility(View.VISIBLE);	
		isOpenDetail = true;
		specialBgOpenAnimator();
	}
	
	public void setCurrentGameTopicInfo(){
		mGameTopicInfo = gameTopics.get(mAdapter.startPos%gameTopics.size());
		title.setText(mGameTopicInfo.getName());	
		mImageFetcher.loadImage(mGameTopicInfo.getPhoto(), cover, R.drawable.default_special);
	}
	
	private void closeDetail(){
		isFinishing = false;
		mDetailAdapter.setLastFocusView(null);
		mRecyclerViewDetail.setVisibility(View.INVISIBLE);
		dismissLoading();
		specialBgCloseAnimator();
	}
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				if(timerCount>= 6 || timerCount>= games.size()){
					isFinishing = true;
					detailState=1;
					if(timer!=null){
						timer.cancel();
						timer =null;
					}
				}
				else{
					TopicToGame game = games.get(timerCount);
					mDetailAdapter.addData(game);
					timerCount++;
				}
				break;
			case 1:
				title.setText(gameTopics.get(0).getName());
				mImageFetcher.loadImage(gameTopics.get(0).getPhoto(), cover, R.drawable.default_special);
				mAdapter.setData(gameTopics,0);
				fborder.requestFocus();
				fborder.setOnKeyListener(onKeyListener);
				fborder.setOnClickListener(onClickListener);
				animViews.clear();
				break;
			case 2:
				dismissLoading();
				mRecyclerViewDetail.setVisibility(View.VISIBLE);
				mDetailAdapter.setData(games);
				setAdapters();
				break;
			case 3:
				closeDetail();
				break;
			case Constant.SHOWLOADING:
				showLoading();
				break;
			case Constant.DISMISSLOADING:
				dismissLoading();
				break;
			case Constant.NULLDATA:
				showNullData();
				break;
			case Constant.EXCEPTION:
				showException();
				break;
			default:
				break;
			}
		}
		
	};
	
	public void specialBgOpenAnimator(){
		
        ViewHelper.setAlpha(specialbg,0f);
        ViewHelper.setPivotX(specialbg, 0);
        ViewHelper.setPivotY(specialbg, 0);
        
        ObjectAnimator bgScaleAnim=ObjectAnimator.ofFloat(specialbg, "scaleX", 0, .5f, 1.0f).setDuration(1500);
        ObjectAnimator bgAlphaAnim=ObjectAnimator.ofFloat(specialbg, "alpha", 1).setDuration(1500);

        final AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(bgScaleAnim,bgAlphaAnim);
        mAnimatorSet.start();
        mAnimatorSet.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator arg0) {
		
			}
			
			@Override
			public void onAnimationRepeat(Animator arg0) {
				
			}
			
			@Override 
			public void onAnimationEnd(Animator arg0) {
				getGameInfosFromGameTopic(gameTopics.get(mAdapter.startPos%gameTopics.size()));
			}
			
			@Override
			public void onAnimationCancel(Animator arg0) {
				
			}
		});
	}
	
	public void specialBgCloseAnimator(){
        
        ViewHelper.setAlpha(specialbg,1.0f);
        ViewHelper.setPivotX(specialbg, 1);
        ViewHelper.setPivotY(specialbg, 1);
        
        ObjectAnimator bgScaleAnim=ObjectAnimator.ofFloat(specialbg, "scaleX", 1, .5f, 0).setDuration(1500);
        ObjectAnimator bgAlphaAnim=ObjectAnimator.ofFloat(specialbg, "alpha", 0).setDuration(1500);
        //bgScaleAnim.setStartDelay(500);
        //bgAlphaAnim.setStartDelay(500);
        final AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(bgScaleAnim,bgAlphaAnim);
        mAnimatorSet.start();
        mAnimatorSet.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator arg0) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animator arg0) {
				
			}
			
			@Override
			public void onAnimationEnd(Animator arg0) {
				specialbg.setVisibility(View.INVISIBLE);
				fborder.setOnClickListener(onClickListener);
				fborder.setFocusable(true);
				fborder.setFocusableInTouchMode(true);
				fborder.requestFocus();
				cover.setVisibility(View.INVISIBLE);
				flist.setVisibility(View.VISIBLE);
				right.setVisibility(View.VISIBLE);
				mDetailAdapter.clearData();
				detailState=0;
				if(listFocusView!=null){
					listFocusView.requestFocus();
				}
				//mAnimatorSet.cancel();
			}
			
			@Override
			public void onAnimationCancel(Animator arg0) {
				
			}
		});
	}
	
	private void getGameTopics(){
		
		ReqCallback<List<GameTopicInfo>> reqCallback = new ReqCallback<List<GameTopicInfo>>() {
			@Override
			public void onGetCacheData(String requestTag, boolean result) {
				if(!result){
					loadingView.getmHandler().sendEmptyMessage(Constant.SHOWLOADING);
					
				}
			}
			@Override
			public void onResult(TaskResult<List<GameTopicInfo>> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					List<GameTopicInfo> infos = taskResult.getData();
					if (infos != null && !infos.isEmpty()) {
						alog.info(infos.toString());
						gameTopics.clear();
						gameTopics.addAll(infos);
						mHandler.sendEmptyMessage(1);
						loadingView.getmHandler().sendEmptyMessage(Constant.DISMISSLOADING);
					}
					else{
						if(NetUtil.isNetworkAvailable(GameSpecialActivity.this, true)){
							loadingView.getmHandler().sendEmptyMessage(Constant.NULLDATA);
						}
						else{
							loadingView.getmHandler().sendEmptyMessage(Constant.EXCEPTION);
						}
					}
				}
				else{
					if(NetUtil.isNetworkAvailable(GameSpecialActivity.this, true)){
						loadingView.getmHandler().sendEmptyMessage(Constant.NULLDATA);
					}
					else{
						loadingView.getmHandler().sendEmptyMessage(Constant.EXCEPTION);
					}
				}
			}

			@Override
			public void onUpdate(TaskResult<List<GameTopicInfo>> taskResult) {

			}
		};
		DataFetcher.getGameTopic(this, reqCallback, false)
				.registerUpdateListener(reqCallback).request(this);
	}
	
	private void getGameInfosFromGameTopic(GameTopicInfo gameTopicInfo){
		detailState=1;
		
		List<TopicToGame> topicToGames = gameTopicInfo.getTopicToGameList();
		if(topicToGames!=null && !topicToGames.isEmpty()){
			alog.debug("缓存已经存在该专题的游戏");
			games.clear();
			for (TopicToGame info: topicToGames) {
				if(info.getType() == DataConfig.GAME_TYPE_COPYRIGHT){
					//版权游戏
					alog.info("版权游戏名称:"+ info.getGameInfo().getGameName());
				} else {
					//第三方游戏
					alog.info("第三方游戏名称:"+ info.getThirdGameInfo().getGameName());
				}
				
			}
			if(detailState==1){
				detailState=0;
				games.addAll(topicToGames);
				mHandler.sendEmptyMessage(2);
			}
			return;
		}
		
		ReqCallback<GameTopicInfo> reqCallback = new ReqCallback<GameTopicInfo>() {
			@Override
			public void onGetCacheData(String requestTag, boolean result) {
				if(!result){
					mRecyclerViewDetail.setVisibility(View.INVISIBLE);
					mHandler.sendEmptyMessage(Constant.SHOWLOADING);
				}
			}
			@Override
			public void onResult(TaskResult<GameTopicInfo> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					GameTopicInfo gameTopicInfo = taskResult.getData();
					if (gameTopicInfo != null) {
						// alog.info(infos.toString());
						List<TopicToGame> topicToGames = gameTopicInfo.getTopicToGameList();
						if(topicToGames!=null && !topicToGames.isEmpty()){
							alog.debug("从网络获取到的【"+gameTopicInfo.getName()+"】专题下的游戏 ");
							games.clear();
							for (TopicToGame info: topicToGames) {
								if(info.getType() == DataConfig.GAME_TYPE_COPYRIGHT){
									//版权游戏
									alog.info("版权游戏名称:"+ info.getGameInfo().getGameName());
								} else {
									//第三方游戏
									alog.info("第三方游戏名称:"+ info.getThirdGameInfo().getGameName());
								}
							}
							if(detailState==1){
								detailState=0;
								games.addAll(topicToGames);
								mHandler.sendEmptyMessage(2);
							}
						}
						else{
							detailState=1;
							if(NetUtil.isNetworkAvailable(GameSpecialActivity.this, true)){
								mHandler.sendEmptyMessage(Constant.NULLDATA);
							}
							else{
								detailState=1;
								mHandler.sendEmptyMessage(Constant.EXCEPTION);
							}
						}
					}
					else{
						detailState=1;
						if(NetUtil.isNetworkAvailable(GameSpecialActivity.this, true)){
							mHandler.sendEmptyMessage(Constant.NULLDATA);
						}
						else{
							detailState=1;
							mHandler.sendEmptyMessage(Constant.EXCEPTION);
						}
					}
				}
				else{
					detailState=1;
					if(NetUtil.isNetworkAvailable(GameSpecialActivity.this, true)){
						mHandler.sendEmptyMessage(Constant.NULLDATA);
					}
					else{
						detailState=1;
						mHandler.sendEmptyMessage(Constant.EXCEPTION);
					}
				}
			}

			@Override
			public void onUpdate(TaskResult<GameTopicInfo> taskResult) {

			}
		};
		DataFetcher.getGameInfosFromGameTopic2(GameSpecialActivity.this, gameTopicInfo, reqCallback, false)
				.registerUpdateListener(reqCallback)
				.request(GameSpecialActivity.this);
	}
	private void showLoading(){
		loadingLayout.setVisibility(View.VISIBLE);
		animationDrawable.start();
		nulldata.setVisibility(View.INVISIBLE);
		exception.setVisibility(View.INVISIBLE);
	}
	
	private void dismissLoading(){
		animationDrawable.stop();
		nulldata.setVisibility(View.INVISIBLE);
		exception.setVisibility(View.INVISIBLE);
		loadingLayout.setVisibility(View.INVISIBLE);
	}
	
	private void showNullData(){
		animationDrawable.stop();
		loadingLayout.setVisibility(View.INVISIBLE);
		exception.setVisibility(View.INVISIBLE);
		nulldata.setVisibility(View.VISIBLE);
	}
	
	private void showException(){
		//loadingLayout.setVisibility(View.VISIBLE);
		animationDrawable.stop();
		loadingLayout.setVisibility(View.INVISIBLE);
		nulldata.setVisibility(View.INVISIBLE);
		exception.setVisibility(View.VISIBLE);
	}
	
	private void setAdapters(){
		timerCount=0;
		timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				mHandler.sendEmptyMessage(0);
			}
		}, 0, 400);
		
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
								if(flist.getVisibility()==View.VISIBLE){
									getGameTopics();
								}
								else{
									mDetailAdapter.setLastFocusView(null);
									getGameInfosFromGameTopic(gameTopics.get(mAdapter.startPos%gameTopics.size()));
								}
							}
						}
					} else {
						// 网络断开了
						isConnected = false;
						alog.debug("当前网络断开了");
						//loadingView.showContentNetExceptionOrNullData(true);
						if(flist.getVisibility()!=View.VISIBLE){
							loadingView.getmHandler().sendEmptyMessage(Constant.EXCEPTION);
						}
						else{
							detailState=1;
							mHandler.sendEmptyMessage(Constant.EXCEPTION);
						}
						
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
}
