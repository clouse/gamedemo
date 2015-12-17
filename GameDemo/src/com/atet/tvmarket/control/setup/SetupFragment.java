package com.atet.tvmarket.control.setup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.control.common.BaseFragment;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.ScreenShot;
import com.atet.tvmarket.view.BlurDialog;

@SuppressLint("NewApi")
public class SetupFragment extends BaseFragment {
	private ALog alog = ALog.getLogger(SetupFragment.class);
	private View rootView;

	private Toast toast = null;
	private RelativeLayout mLayoutChildlock, mLayoutVideo, mLayoutHandle,
			mLayoutUpdate;

	public SetupFragment newInstance() {
		SetupFragment mineFragment = new SetupFragment();
		Bundle bundle = new Bundle();
		mineFragment.setArguments(bundle);
		return mineFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		alog.info("SetupFragment onCreate()");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		alog.info("SetupFragment onCreateView()");
		rootView = inflater.inflate(R.layout.fragment_setup_main, container,
				false);
		ScaleViewUtils.scaleView(rootView);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		alog.info("SetupFragment onActivityCreated");
		initView();
	}

	private void initView() {
		FocusChangeListener onFocusChangeListener = new FocusChangeListener();
		ClickListener onClickListener = new ClickListener();
		TouchListener onTouchListener = new TouchListener();
		mLayoutChildlock = (RelativeLayout) rootView
				.findViewById(R.id.setup_childlock_layout_bg);
		mLayoutChildlock.setOnClickListener(onClickListener);
		mLayoutChildlock.setOnTouchListener(onTouchListener);
		mLayoutChildlock.setOnFocusChangeListener(onFocusChangeListener);
		mLayoutChildlock.setNextFocusUpId(R.id.tab_setting);
		mLayoutChildlock.setNextFocusDownId(mLayoutChildlock.getId());
		mLayoutChildlock.setNextFocusLeftId(mLayoutChildlock.getId());
		mLayoutVideo = (RelativeLayout) rootView
				.findViewById(R.id.setup_video_layout_bg);
		mLayoutVideo.setOnClickListener(onClickListener);
		mLayoutVideo.setOnTouchListener(onTouchListener);
		mLayoutVideo.setOnFocusChangeListener(onFocusChangeListener);
		mLayoutVideo.setNextFocusUpId(R.id.tab_setting);
		mLayoutVideo.setNextFocusDownId(mLayoutVideo.getId());
		mLayoutHandle = (RelativeLayout) rootView
				.findViewById(R.id.setup_handle_layout_bg);
		mLayoutHandle.setNextFocusUpId(R.id.tab_setting);
		mLayoutHandle.setOnClickListener(onClickListener);
		mLayoutHandle.setOnTouchListener(onTouchListener);
		mLayoutHandle.setOnFocusChangeListener(onFocusChangeListener);
		mLayoutHandle.setNextFocusDownId(mLayoutHandle.getId());
		mLayoutUpdate = (RelativeLayout) rootView
				.findViewById(R.id.setup_update_layout_bg);
		mLayoutUpdate.setNextFocusUpId(R.id.tab_setting);
		mLayoutUpdate.setOnClickListener(onClickListener);
		mLayoutUpdate.setOnTouchListener(onTouchListener);
		mLayoutUpdate.setOnFocusChangeListener(onFocusChangeListener);
		mLayoutUpdate.setNextFocusRightId(mLayoutUpdate.getId());
		mLayoutUpdate.setNextFocusDownId(mLayoutUpdate.getId());
	}

	class TouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				OnFocusChangeListener listener = v.getOnFocusChangeListener();
				if (listener != null && listener instanceof FocusChangeListener) {
					((FocusChangeListener) listener).setView(v);
				}
			}
			return false;
		}
	}

	class ClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = null;
			int id = v.getId();
			if (id == R.id.setup_childlock_layout_bg) {
				intent = new Intent(getActivity(), SetupChildlockActivity.class);
			} else if (id == R.id.setup_video_layout_bg) {
				intent = new Intent(getActivity(), SetupVideoActivity.class);
			} else if (id == R.id.setup_handle_layout_bg) {
				intent = new Intent(getActivity(),
						SetupHandlelinkActivity.class);
			} else if (id == R.id.setup_update_layout_bg) {
				intent = new Intent(getActivity(), SetupUpdateActivity.class);
			} else {
			}
			startActivity(intent);
		}
	}

	class FocusChangeListener implements OnFocusChangeListener {

		private View view;

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				v.setScaleX(1.18f);
				v.setScaleY(1.18f);
				alog.info("[onFocusChange]" + v.toString());
				if (v.isInTouchMode() && v == view) {
					v.performClick();
				} else {
					view = null;
				}
			} else {
				v.setScaleX(1.0f);
				v.setScaleY(1.0f);
			}

		}

		public View getView() {
			return view;
		}

		public void setView(View view) {
			this.view = view;
		}
	}
}
