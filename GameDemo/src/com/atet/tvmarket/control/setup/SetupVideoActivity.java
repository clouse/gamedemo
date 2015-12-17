package com.atet.tvmarket.control.setup;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.control.video.PlayVideoActivity;
import com.atet.tvmarket.entity.dao.VideoInfo;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UmengUtils;
import com.atet.tvmarket.view.LoadingView;

public class SetupVideoActivity extends BaseActivity {
	private ALog alog = ALog.getLogger(SetupVideoActivity.class);
	private Toast toast = null;
	private RelativeLayout mVideoLayout1, mVideoLayout2, mVideoLayout3;
	private TextView tvVideoName1, tvVideoName2, tvVideoName3;
	private LoadingView loadingView;
	private RelativeLayout contentView;
	private ImageView ivVideoIcon1, ivVideoIcon2, ivVideoIcon3;
	private Resources resource;
	private ColorStateList csl;
	private VideoInfo video1, video2, video3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_setup_video);
		ScaleViewUtils.scaleView(this);
		alog.info("SetupVideoActivity");
		initView();
		loadGetVideoInfo();
		setBlackTitle(false);
		resource = (Resources) getBaseContext().getResources();
		csl = (ColorStateList) resource
				.getColorStateList(R.color.video_text_normal);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setTitleData();
	}

	private void initView() {
		loadingView = (LoadingView) findViewById(R.id.contentLoading);
		contentView = (RelativeLayout) findViewById(R.id.layout_content);
		loadingView.setDataView(contentView);
		TouchListener touchListener = new TouchListener();
		FocusChangeListener focusChangeListener = new FocusChangeListener();
		ClickListener clickListener = new ClickListener();
		mVideoLayout1 = (RelativeLayout) findViewById(R.id.setup_video_dialog_layout1);
		mVideoLayout1.setOnClickListener(clickListener);
		mVideoLayout1.setOnTouchListener(touchListener);
		mVideoLayout1.setOnFocusChangeListener(focusChangeListener);
		mVideoLayout2 = (RelativeLayout) findViewById(R.id.setup_video_dialog_layout2);
		mVideoLayout2.setOnClickListener(clickListener);
		mVideoLayout2.setOnTouchListener(touchListener);
		mVideoLayout2.setOnFocusChangeListener(focusChangeListener);
		mVideoLayout3 = (RelativeLayout) findViewById(R.id.setup_video_dialog_layout3);
		mVideoLayout3.setOnClickListener(clickListener);
		mVideoLayout3.setOnTouchListener(touchListener);
		mVideoLayout3.setOnFocusChangeListener(focusChangeListener);
		tvVideoName1 = (TextView) findViewById(R.id.setup_video_dialog_videoname1);
		tvVideoName2 = (TextView) findViewById(R.id.setup_video_dialog_videoname2);
		tvVideoName3 = (TextView) findViewById(R.id.setup_video_dialog_videoname3);
		ivVideoIcon1 = (ImageView) findViewById(R.id.setup_video_dialog_icon1);
		ivVideoIcon2 = (ImageView) findViewById(R.id.setup_video_dialog_icon2);
		ivVideoIcon3 = (ImageView) findViewById(R.id.setup_video_dialog_icon3);
	}

	class ClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(SetupVideoActivity.this,
					PlayVideoActivity.class);
			VideoInfo video = null;
			int id = v.getId();
			if (id == R.id.setup_video_dialog_layout1) {
				video = video1;
			} else if (id == R.id.setup_video_dialog_layout2) {
				video = video2;
			} else if (id == R.id.setup_video_dialog_layout3) {
				video = video3;
			}
			// Umeng统计播放信息
			UmengUtils.setOnEvent(SetupVideoActivity.this,
					UmengUtils.SETUP_VIDEO_PLAY);
			intent.putExtra("isSetupVideo", true);
			intent.putExtra("videoUrl", video.getVideoUrl());
			intent.putExtra("videoType", video.getVideoType());
			startActivity(intent);
		}
	}

	public class TouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			alog.info("onTouch...");
			if (event.getAction() == MotionEvent.ACTION_UP) {
				OnFocusChangeListener listener = v.getOnFocusChangeListener();
				if (listener != null && listener instanceof FocusChangeListener) {
					((FocusChangeListener) listener).setView(v);
				}
			}
			return false;
		}
	}

	@SuppressLint("ResourceAsColor")
	public class FocusChangeListener implements OnFocusChangeListener {

		private View view;

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				if (isIdVideo1(v)) {
					tvVideoName1.setTextColor(Color.WHITE);
				} else if (isIdVideo2(v)) {
					tvVideoName2.setTextColor(Color.WHITE);
				} else if (isIdVideo3(v)) {
					tvVideoName3.setTextColor(Color.WHITE);
				}
				if (v.isInTouchMode() && v == view) {
					v.performClick();
				} else {
					view = null;
				}
			} else {
				if (isIdVideo1(v)) {
					if (csl != null) {
						tvVideoName1.setTextColor(csl);
					}
				} else if (isIdVideo2(v)) {
					if (csl != null) {
						tvVideoName2.setTextColor(csl);
					}
				} else if (isIdVideo3(v)) {
					if (csl != null) {
						tvVideoName3.setTextColor(csl);
					}
				}
			}

		}

		public View getView() {
			return view;
		}

		public void setView(View view) {
			this.view = view;
		}
	}

	/**
	 * 
	 * @Title: loadGetVideoInfo
	 * @Description: TODO(加载视频信息)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void loadGetVideoInfo() {
		alog.debug("");
		loadingView.getmHandler().sendEmptyMessage(Constant.SHOWLOADING);
		ReqCallback<List<VideoInfo>> reqCallback = new ReqCallback<List<VideoInfo>>() {
			@Override
			public void onResult(TaskResult<List<VideoInfo>> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					List<VideoInfo> info = taskResult.getData();
					if (info != null && info.size() > 0) {
						alog.info(info.toString());
						loadingView.getmHandler().sendEmptyMessage(
								Constant.DISMISSLOADING);
						setVideoLayoutShow(info);
					} else {
						if (NetUtil.isNetworkAvailable(SetupVideoActivity.this,
								true)) {
							loadingView.getmHandler().sendEmptyMessage(
									Constant.NULLDATA);
						} else {
							loadingView.getmHandler().sendEmptyMessage(
									Constant.EXCEPTION);
						}
					}
				} else {
					if (NetUtil.isNetworkAvailable(SetupVideoActivity.this,
							true)) {
						loadingView.getmHandler().sendEmptyMessage(
								Constant.NULLDATA);
					} else {
						loadingView.getmHandler().sendEmptyMessage(
								Constant.EXCEPTION);
					}
				}

			}

			@Override
			public void onUpdate(TaskResult<List<VideoInfo>> taskResult) {
				// TODO Auto-generated method stub

			}
		};
		DataFetcher.getVideoInfo(this, reqCallback, true)
				.registerUpdateListener(reqCallback).request(this);

	}

	private void setVideoLayoutShow(List<VideoInfo> videoInfos) {
		if (videoInfos == null) {
			return;
		}
		int videoCounts = videoInfos.size();
		// 设置第一个视频位视图
		if (videoCounts >= 1) {
			video1 = videoInfos.get(0);
			mVideoLayout1.setVisibility(View.VISIBLE);
			mVideoLayout1.requestFocus();
			mVideoLayout1.requestFocusFromTouch();
			tvVideoName1.setVisibility(View.VISIBLE);
			tvVideoName1.setText(video1.getVideoName());
			mImageFetcher.loadImage(video1.getVideoIcon(), ivVideoIcon1,
					R.drawable.gameranking_item_icon);
		}

		// 设置第二个视频位视图
		if (videoCounts >= 2) {
			video2 = videoInfos.get(1);
			mVideoLayout2.setVisibility(View.VISIBLE);
			tvVideoName2.setVisibility(View.VISIBLE);
			tvVideoName2.setText(video2.getVideoName());
			mImageFetcher.loadImage(video2.getVideoIcon(), ivVideoIcon2,
					R.drawable.gameranking_item_icon);
		}

		// 设置第三个视频位视图
		if (videoCounts >= 3) {
			video3 = videoInfos.get(2);
			mVideoLayout3.setVisibility(View.VISIBLE);
			tvVideoName3.setVisibility(View.VISIBLE);
			tvVideoName3.setText(video3.getVideoName());
			mImageFetcher.loadImage(video3.getVideoIcon(), ivVideoIcon3,
					R.drawable.gameranking_item_icon);
		}
	}

	/**
	 * 
	 * @Title: isIdVideo1
	 * @Description: TODO(判断是否是第一个video)
	 * @param @param v
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	private boolean isIdVideo1(View v) {
		if (v == null) {
			return false;
		}
		if (v.getId() == R.id.setup_video_dialog_layout1) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @Title: isIdVideo2
	 * @Description: TODO(判断是否是第二个video)
	 * @param @param v
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	private boolean isIdVideo2(View v) {
		if (v == null) {
			return false;
		}
		if (v.getId() == R.id.setup_video_dialog_layout2) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @Title: isIdVideo3
	 * @Description: TODO(判断是否是第三个video)
	 * @param @param v
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	private boolean isIdVideo3(View v) {
		if (v == null) {
			return false;
		}
		if (v.getId() == R.id.setup_video_dialog_layout3) {
			return true;
		}
		return false;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();
		if (action == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_BUTTON_X
					|| keyCode == KeyEvent.KEYCODE_X
					|| keyCode == KeyEvent.KEYCODE_MENU) {
				loadGetVideoInfo();
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}
}
