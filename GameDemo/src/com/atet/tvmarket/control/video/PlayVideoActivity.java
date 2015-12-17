package com.atet.tvmarket.control.video;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.webkit.URLUtil;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.atet.tvmarket.R;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.control.mygame.activity.GameDetailActivity;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.model.DaoHelper;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.GameStatisticsHelper;
import com.atet.tvmarket.model.TaskListener;
import com.atet.tvmarket.model.netroid.request.TaskRequest;
import com.atet.tvmarket.model.task.TaskQueue;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.model.usertask.UserTaskDaoHelper;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.StringTool;
import com.atet.tvmarket.utils.UmengUtils;
import com.skyworth.framework.skysdk.util.MD5Util;

public class PlayVideoActivity extends BaseActivity implements OnClickListener {

	private MyVideoView mVideoView;
	private String remoteUrl; // 视频Url
	private String localUrl; // 本地视频url
	private static final int READY_BUFF = 2000 * 1024;
	private static final int CACHE_BUFF = 500 * 1024;
	private boolean isready = false;
	private boolean iserror = false;
	private int errorCnt = 0;
	private int curPosition = 0;
	private long mediaLength = 0;
	private long readSize = 0;
	private String okPath;
	private String path;
	private Runnable playRunnable;
	private boolean isStop = false;
	MediaController mediaController;
	private ImageButton btn_pause, btn_pre, btn_next;
	private SeekBar seekBar;
	private TextView tv_currenttime, tv_all_time;
	private ImageView prepareSliding, prepareIv;
	private long allTime;
	private RelativeLayout layout_play_controll;
	private RelativeLayout layout_play;
	private FrameLayout prepareFrameLayout;
	private TextView hintTextView;
	private int bufferPercent;
	private boolean isPlayCompletion = false;
	private boolean isPlayLocal = false;// 是否播放的是本地视频;
	private int old_duration = 0;
	private Thread downloadThread;
	private String gameId;// 游戏id
	private boolean isGameVideo; // 游戏类型
	private boolean isSetupVideo;// 设置中的视频
	// 视频播放快进快退时间
	private long VIDEO_FAST_FORWARD_TIME = 5 * 1000;
	// 播放控制条显示的时间
	private long PLAY_CONTROLL_HIDE_TIME = 3 * 1000;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 窗口不能与输入法交互，可以覆盖输入法窗口
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		setContentView(R.layout.paly_video_activity);
		ScaleViewUtils.init(this);
		ScaleViewUtils.scaleView(getWindow().getDecorView());
		findViews();
		addListener();
		init();
		isPlayLocal();
		clickStatistics(gameId);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		// TODO Auto-generated method stub
		super.startActivityForResult(intent, requestCode);
	}

	private void startImageAmin(ImageView progressImage) {
		LinearInterpolator lin = new LinearInterpolator();
		int duration = 1500;
		Animation am = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
				0.0f, Animation.RELATIVE_TO_PARENT, 1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		am.setDuration(duration);
		am.setRepeatCount(Animation.INFINITE);
		am.setInterpolator(lin);
		am.setRepeatMode(Animation.RESTART);
		progressImage.clearAnimation();
		progressImage.startAnimation(am);
	}

	private void startImageRoateAmin(ImageView progressImage) {
		prepareIv.setImageResource(R.drawable.play_video_circle);
		prepareSliding.setImageBitmap(null);
		Animation operatingAnim = AnimationUtils.loadAnimation(this,
				R.anim.start_play_video_loading);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
		prepareIv.startAnimation(operatingAnim);
	}

	@Override
	protected void onPause() {
		super.onPause();
		dismissProgressDialog();
	}

	private void findViews() {
		btn_pause = (ImageButton) findViewById(R.id.btn_pause);
		btn_pre = (ImageButton) findViewById(R.id.btn_pre);
		btn_next = (ImageButton) findViewById(R.id.btn_next);
		seekBar = (SeekBar) findViewById(R.id.video_seekbar);
		tv_currenttime = (TextView) findViewById(R.id.tv_current_time);
		tv_all_time = (TextView) findViewById(R.id.tv_all_time);
		layout_play_controll = (RelativeLayout) findViewById(R.id.playercontroll);
		layout_play = (RelativeLayout) findViewById(R.id.layout_play);
		btn_next.setNextFocusLeftId(R.id.btn_pre);
		btn_pre.setNextFocusRightId(R.id.btn_next);

		prepareSliding = (ImageView) findViewById(R.id.play_video_prepare_sliding_iv);
		prepareIv = (ImageView) findViewById(R.id.play_video_prepare_iv);
		prepareFrameLayout = (FrameLayout) findViewById(R.id.play_video_prepare_layout);
		hintTextView = (TextView) findViewById(R.id.play_video_hint_text);
		mVideoView = (MyVideoView) findViewById(R.id.vitamioView);

	}

	private void addListener() {
		layout_play.setOnClickListener(this);
		btn_pause.setOnClickListener(this);
		btn_next.setOnClickListener(this);
		btn_pre.setOnClickListener(this);
		seekBar.setMax(100);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// seekBar.setProgress(progress);
				if (fromUser) {
					mVideoView.seekTo(progress * mVideoView.getDuration() / 100);
					seekBar.setProgress(progress);
				} else {
				}
			}
		});
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		isStop = true;
		mHandler.removeCallbacksAndMessages(null);
		if (downloadThread != null) {
			downloadThread.interrupt();
			downloadThread = null;
		}
		super.onDestroy();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int keyCode = event.getKeyCode();
		if (event.getAction() == KeyEvent.ACTION_UP) {
			if (keyCode == KeyEvent.KEYCODE_Y
					|| keyCode == KeyEvent.KEYCODE_BUTTON_Y) {
				return true;
			} else if (keyCode == KeyEvent.KEYCODE_X
					|| keyCode == KeyEvent.KEYCODE_BUTTON_X
					|| keyCode == KeyEvent.KEYCODE_MENU) {
				return true;
			} else if (keyCode == KeyEvent.KEYCODE_A
					|| keyCode == KeyEvent.KEYCODE_BUTTON_A
					|| keyCode == KeyEvent.KEYCODE_DPAD_CENTER
					|| keyCode == KeyEvent.KEYCODE_ENTER) {
				if (isGameVideo) {
					if (!StringTool.isEmpty(gameId)) {
						// Umeng统计通过"首页视频"跳转到游戏详情的次数
						UmengUtils
								.setOnEvent(
										PlayVideoActivity.this,
										UmengUtils.GAMECENTER_MAINPAGEVIDEO_DETAIL_CLICK);
						// Umeng统计"首页视频"中视频未播放完成信息
						UmengUtils.setOnEvent(PlayVideoActivity.this,
								UmengUtils.GAMECENTER_VIDEO_UNCOMPLETE);
						JumpToGameDetail(gameId);
					}
				} else {
					ShowPlayControll();
					btn_pause.requestFocus();
					btn_pause.performClick();
					HidePlayControll();
				}
				return true;
			} else if (keyCode == KeyEvent.KEYCODE_B
					|| keyCode == KeyEvent.KEYCODE_BUTTON_B
					|| keyCode == KeyEvent.KEYCODE_BACK) {

				if (isGameVideo) {
					// Umeng统计"首页视频"中视频未播放完成信息
					UmengUtils.setOnEvent(PlayVideoActivity.this,
							UmengUtils.GAMECENTER_VIDEO_UNCOMPLETE);

				}
				if (isSetupVideo) {
					// Umeng统计设置中视频未播放完成信息
					UmengUtils.setOnEvent(PlayVideoActivity.this,
							UmengUtils.SETUP_VIDEO_UNCOMPLETED);
				}

				mHandler.removeCallbacks(playRunnable);
				finish();
				return true;
			} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
				if (!isGameVideo) {
					mHandler.removeMessages(HIDE_PLAY_CONTROLL);
					ShowPlayControll();
					btn_next.requestFocus();
					btn_next.performClick();
					HidePlayControll();
				}
				return true;
			} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
				if (!isGameVideo) {
					mHandler.removeMessages(HIDE_PLAY_CONTROLL);
					ShowPlayControll();
					btn_pre.requestFocus();
					btn_pre.performClick();
					HidePlayControll();
				}
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	/**
	 * 快进
	 * */
	private void fastForward() {
		curPosition = mVideoView.getCurrentPosition();
		allTime = mVideoView.getDuration();
		if (curPosition + VIDEO_FAST_FORWARD_TIME > allTime) {
			mVideoView.seekTo((int) allTime);
		} else {
			mVideoView.seekTo((int) (curPosition + VIDEO_FAST_FORWARD_TIME));
		}
	}

	/**
	 * 快退
	 * */
	private void rewind() {
		curPosition = mVideoView.getCurrentPosition();
		allTime = mVideoView.getDuration();
		if (curPosition - VIDEO_FAST_FORWARD_TIME < 0) {
			mVideoView.seekTo(0);
		} else {
			mVideoView.seekTo((int) (curPosition - VIDEO_FAST_FORWARD_TIME));
		}
	}

	private void init() {
		remoteUrl = getIntent().getStringExtra("videoUrl");// 获取传递过来的视频播放地址
		gameId = getIntent().getStringExtra("gameId");
		isGameVideo = getIntent().getBooleanExtra("isGameVideo", false);
		isSetupVideo = getIntent().getBooleanExtra("isSetupVideo", false);
		// 需要对视频链接进行处理
		String filename = MD5Util.getMD5String(remoteUrl);
		okPath = Constant.VIDEO_DATA_LOCAL_DIR + filename + ".mp4"; // 视频缓存完成的路径
		path = Constant.VIDEO_DATA_LOCAL_DIR + filename + ".temp.mp4";// 视频缓存的路径
		ImageView icon_A = (ImageView) findViewById(R.id.vedio_tag1);
		if (isGameVideo) {
			icon_A.setBackgroundResource(R.drawable.icon_a_x_w);
		} else {
			icon_A.setBackgroundResource(R.drawable.icon_a_pouse_w);
		}
		mVideoView.setFocusable(false);
		mVideoView.setFocusableInTouchMode(false);
		startImageAmin(prepareSliding);
		showProgressDialog();
		mVideoView.setOnPreparedListener(new OnPreparedListener() {
			public void onPrepared(MediaPlayer mediaplayer) {
				mVideoView.seekTo(curPosition);
				seekBar.setProgress(0);
				mediaplayer.start();
				if (!isPlayLocal) {
					mHandler.postDelayed(bufferRunnable, 0);
					startImageRoateAmin(prepareSliding);
				} else {
					dismissProgressDialog();
				}
				updatePlayState();
			}
		});

		mVideoView.setOnCompletionListener(new OnCompletionListener() {

			public void onCompletion(MediaPlayer mediaplayer) {
				// "首页视频"播放完成
				if (isGameVideo) {
					// Umeng统计"首页视频"中视频播放完成信息
					UmengUtils.setOnEvent(PlayVideoActivity.this,
							UmengUtils.GAMECENTER_VIDEO_COMPLETE);
				}
				if (isSetupVideo) {
					// Umeng统计"设置"中视频播放完成信息
					UmengUtils.setOnEvent(PlayVideoActivity.this,
							UmengUtils.SETUP_VIDEO_COMPLETED);
				}
				isPlayCompletion = true;
				mVideoView.stopPlayback();
				PlayVideoActivity.this.finish();

				if (!TextUtils.isEmpty(gameId)) {
					int userId = DataFetcher.getUserIdInt();
					if (userId > 0) {
						// 视频播放的任务统计
						UserTaskDaoHelper.saveWatchVideoRecordAsyn(
								BaseApplication.getContext(), gameId, userId,
								gameId, 0, 0);
					}
				}
			}
		});

		mVideoView.setOnErrorListener(new OnErrorListener() {

			public boolean onError(MediaPlayer mediaplayer, int i, int j) {
				iserror = true;
				errorCnt++;
				mVideoView.start();
				updatePlayState();
				showProgressDialog();
				return true;
			}
		});
	}

	/**
	 * 
	 * @Title: JumpToGameDetail
	 * @Description: TODO(跳转至游戏详情页面)
	 * @param: @param gameId
	 * @param: @param external
	 * @param: @param packageName
	 * @return: void
	 * @throws
	 */
	private void JumpToGameDetail(String gameId) {
		Intent intent = new Intent(this, GameDetailActivity.class);
		intent.putExtra("gameId", gameId);
		startActivity(intent);
		finish();
	}

	/** 是否播放本地视频 */
	public void isPlayLocal() {
		if (isExists()) {
			// 播放本地视频文件
			playvideo();
		} else {
			if (NetUtil.isNetworkAvailable(getBaseContext(), true)) {
				playvideo();
			} else {
				dismissProgressDialog();
				TextView textView = (TextView) findViewById(R.id.play_video_net_iv);
				textView.setVisibility(View.VISIBLE);
			}
		}
	}

	private void showProgressDialog() {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				prepareFrameLayout.setVisibility(View.VISIBLE);
				hintTextView.setVisibility(View.VISIBLE);
			}
		});
	}

	private void dismissProgressDialog() {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				prepareFrameLayout.setVisibility(View.INVISIBLE);
				hintTextView.setVisibility(View.INVISIBLE);
			}
		}, 200);
	}

	private void playvideo() {
		if (!URLUtil.isNetworkUrl(this.remoteUrl)) {
			// 播放本地视频
			isPlayLocal = true;
			mVideoView.setVideoPath(this.remoteUrl);
			mVideoView.start();
			mHandler.sendEmptyMessage(PLAY_STATE_UPDATE);
			return;
		}
		mHandler.sendEmptyMessage(VIDEO_STATE_UPDATE);
		mHandler.sendEmptyMessage(CACHE_VIDEO_READY);
		mHandler.sendEmptyMessage(CACHE_VIDEO_UPDATE);
		playRunnable = new Runnable() {
			// 启动线程下载视频
			@Override
			public void run() {
				FileOutputStream out = null;
				InputStream is = null;

				try {
					URL url = new URL(remoteUrl);
					HttpURLConnection httpConnection = (HttpURLConnection) url
							.openConnection();
					if (localUrl == null) {
						localUrl = path;
					}

					File cacheFile = new File(localUrl);

					if (!cacheFile.exists()) {
						cacheFile.getParentFile().mkdirs();
						cacheFile.createNewFile();
					}

					readSize = cacheFile.length();
					out = new FileOutputStream(cacheFile, true);

					httpConnection.setRequestProperty("User-Agent", "NetFox");
					httpConnection.setRequestProperty("RANGE", "bytes="
							+ readSize + "-");

					is = httpConnection.getInputStream();

					mediaLength = httpConnection.getContentLength();
					if (mediaLength == -1) {
						return;
					}

					mediaLength += readSize;

					byte buf[] = new byte[4 * 1024];
					int size = 0;
					long lastReadSize = 0;

					while ((size = is.read(buf)) != -1) {
						try {
							out.write(buf, 0, size);
							readSize += size;
						} catch (Exception e) {
							e.printStackTrace();
						}

						if (!isready) {
							if ((readSize - lastReadSize) > READY_BUFF) {
								lastReadSize = readSize;
							}
						} else {
							if ((readSize - lastReadSize) > CACHE_BUFF
									* (errorCnt + 1)) {
								lastReadSize = readSize;
							}
						}
					}

					mHandler.sendEmptyMessage(CACHE_VIDEO_END);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (out != null) {
						try {
							out.close();
						} catch (IOException e) {
							//
						}
					}

					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							//
						}
					}
				}

			}
		};
		downloadThread = new Thread(playRunnable);
		downloadThread.start();
	}

	/**
	 * 查看文件是否存在
	 * */
	private boolean isExists() {
		File file = new File(okPath);
		if (file.exists()) {
			remoteUrl = okPath;
			return true;
		}
		return false;
	}

	private final static int VIDEO_STATE_UPDATE = 0;
	private final static int CACHE_VIDEO_READY = 1;
	private final static int CACHE_VIDEO_UPDATE = 2;
	private final static int CACHE_VIDEO_END = 3;
	private final static int PLAY_STATE_UPDATE = 4;
	private final static int HIDE_PLAY_CONTROLL = 5;

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case VIDEO_STATE_UPDATE:
				double cachepercent = readSize * 100.00 / mediaLength * 1.0;
				if (cachepercent == 100.0 && localUrl != null) {
					File downFile = new File(localUrl);
					downFile.renameTo(new File(okPath));
				}

				if (isStop) {
				} else {
					mHandler.sendEmptyMessageDelayed(VIDEO_STATE_UPDATE, 1000);
					mHandler.sendEmptyMessage(PLAY_STATE_UPDATE);
				}
				break;

			case CACHE_VIDEO_READY:
				isready = true;
				if (NetUtil.isNetworkAvailable(getBaseContext(), true)) {
					mVideoView.setVideoPath(remoteUrl);
				} else {
					dismissProgressDialog();
					TextView textView = (TextView) findViewById(R.id.play_video_net_iv);
					textView.setVisibility(View.VISIBLE);
				}
				mVideoView.start();
				updatePlayState();
				break;

			case CACHE_VIDEO_UPDATE:
				if (iserror) {
					mVideoView.setVideoPath(remoteUrl);
					mVideoView.start();
					updatePlayState();
					iserror = false;
				}
				break;

			case CACHE_VIDEO_END:
				if (iserror) {
					mVideoView.setVideoPath(remoteUrl);
					mVideoView.start();
					updatePlayState();
					iserror = false;
				}
				break;

			case PLAY_STATE_UPDATE:
				allTime = mVideoView.getDuration();
				curPosition = mVideoView.getCurrentPosition();
				bufferPercent = mVideoView.getBufferPercentage();
				if (curPosition >= 0 && allTime != 0) {
					seekBar.setProgress((int) (curPosition * 100 / allTime));
					seekBar.setSecondaryProgress(bufferPercent);
				}
				if (curPosition <= allTime) {
					tv_currenttime.setText(TransformationTime(curPosition));
					tv_all_time.setText(TransformationTime(allTime));
				}

				updatePlayState();
				if (isStop) {
				} else {
					mHandler.sendEmptyMessageDelayed(PLAY_STATE_UPDATE, 1000);
				}
				break;

			case HIDE_PLAY_CONTROLL:
				layout_play_controll.setVisibility(View.INVISIBLE);
				break;
			}

			super.handleMessage(msg);
		}
	};

	/** 用于判断是否在缓冲中的线程 */
	Runnable bufferRunnable = new Runnable() {
		public void run() {
			int duration = mVideoView.getCurrentPosition();
			if (old_duration == duration && mVideoView.isPlaying()) {
				showProgressDialog();
			} else {
				dismissProgressDialog();
			}
			old_duration = duration;

			mHandler.postDelayed(bufferRunnable, 500);
		}
	};

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btn_pause) {
			if (isPlayCompletion) {
				isPlayCompletion = false;
			} else {
				if (mVideoView.isPlaying()) {
					mVideoView.pause();
				} else {
					mVideoView.start();
				}
			}
			updatePlayState();
		} else if (id == R.id.btn_next) {
			fastForward();
		} else if (id == R.id.btn_pre) {
			rewind();
		} else if (id == R.id.layout_play) {
			if (layout_play_controll.getVisibility() == View.VISIBLE) {
				layout_play_controll.setVisibility(View.INVISIBLE);
			} else {
				layout_play_controll.setVisibility(View.VISIBLE);
				HidePlayControll();
			}
		}

	}

	/**
	 * 将时间转换格式
	 * */
	private String TransformationTime(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");// 初始化Formatter的转换格式。
		formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		String hms = formatter.format(time);
		return hms;
	}

	/**
	 * 隐藏播放控制条
	 * */
	private void HidePlayControll() {
		if (layout_play_controll.getVisibility() == View.VISIBLE) {
			mHandler.sendEmptyMessageDelayed(HIDE_PLAY_CONTROLL,
					PLAY_CONTROLL_HIDE_TIME);
		} else {

		}
	}

	/**
	 * 显示播放控制条
	 * */
	private void ShowPlayControll() {
		if (layout_play_controll.getVisibility() == View.INVISIBLE) {
			mHandler.removeMessages(HIDE_PLAY_CONTROLL);
			layout_play_controll.setVisibility(View.VISIBLE);
		}
	}

	private void updatePlayState() {
		if (mVideoView.isPlaying()) {
			btn_pause.setBackgroundResource(R.drawable.btn_pause_selector);
		} else {
			btn_pause.setBackgroundResource(R.drawable.btn_play_selector);
		}
	}

	@Override
	protected void onResume() {
		mVideoView.start();
		super.onResume();
	}
	
	
	/**
	 * @description: 来自游戏中心的点击统计
	 * 
	 * @throws:
	 * @author: LiuQin
	 * @date: 2015年8月25日 下午10:57:26
	 */
	private void clickStatistics(final String gameId) {
		final Integer whereFrom = getIntent().getIntExtra(Constant.GAMECENTER, 0);
		if (whereFrom == null || whereFrom != 1  || TextUtils.isEmpty(gameId)) {
			return;
		}
		
		TaskRequest request = new TaskRequest(new TaskListener<TaskResult>() {
			@Override
			public TaskResult doTaskInBackground() {
				GameInfo gameInfo = null;
				List<GameInfo> gameInfoList = DaoHelper.getGameInfoFromGameId(getApplicationContext(), gameId);
				if (gameInfoList!=null && gameInfoList.size()>0) {
					gameInfo = gameInfoList.get(0);
				}
				if(gameInfo != null){
					GameStatisticsHelper.clickStatistics(gameInfo, whereFrom);
				}
				return null;
			}
		});
		TaskQueue.getInstanse(getApplicationContext()).add(request);
	}
}
