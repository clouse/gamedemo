package com.atet.tvmarket.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.atet.tvmarket.R;
import com.atet.tvmarket.utils.BlurDialogUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.ToastUtils;

public class BlurDialog extends Dialog {

	private Bitmap backBitmap;

	private WindowManager.LayoutParams lp;
	private View dialogView;
	private Window dialogWindow;
	private ClipImageView frontView;
	private Toast toast = null;
	private Context mContext;

	public BlurDialog(Context context, Bitmap backBitmap) {
		super(context, R.style.blur_dialog);
		this.backBitmap = backBitmap;
		mContext = context;
		init(context);
	}

	public BlurDialog(Context context, Bitmap backBitmap, int currentWindow) {
		super(context, R.style.blur_dialog);
		this.backBitmap = backBitmap;
		mContext = context;
		switch (currentWindow) {
		case BlurDialogUtil.DIALOG_SETUP_UPDATE:
			initUpdateBlurDialog(context);
			break;
		case BlurDialogUtil.DIALOG_SETUP_VIDEO:
			initVideoBlurDialog(context);
			break;
		case BlurDialogUtil.DIALOG_MINE_ACCOUNT:
			initAccountBlurDialog(context);
			break;
		default:
			init(context);
			break;
		}
	}

	public BlurDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	protected BlurDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init(context);
	}

	private void init(Context context) {
		dialogWindow = getWindow();
		dialogView = LayoutInflater.from(context).inflate(
				R.layout.dialog_mine_about, null, false);
		dialogWindow.setContentView(dialogView);
		dialogWindow.setWindowAnimations(0);
		lp = dialogWindow.getAttributes();
		dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		dialogWindow.setGravity(Gravity.BOTTOM);
		frontView = (ClipImageView) dialogView.findViewById(R.id.front_image);
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(dm);
		lp.width = dm.widthPixels;
		lp.height = dm.heightPixels;
		dialogWindow.setAttributes(lp);
		ViewGroup.LayoutParams wl = dialogView.getLayoutParams();
	}

	private void initUpdateBlurDialog(Context context) {
		dialogWindow = getWindow();
		dialogView = LayoutInflater.from(context).inflate(
				R.layout.dialog_mine_bindphone, null, false);
		ScaleViewUtils.scaleView(dialogView);
		dialogWindow.setContentView(dialogView);
		dialogWindow.setWindowAnimations(0);
		lp = dialogWindow.getAttributes();
		dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		dialogWindow.setGravity(Gravity.BOTTOM);
		frontView = (ClipImageView) dialogView.findViewById(R.id.front_image);
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(dm);
		lp.width = dm.widthPixels;
		lp.height = dm.heightPixels;
		dialogWindow.setAttributes(lp);
		ViewGroup.LayoutParams wl = dialogView.getLayoutParams();
	}

	private void initAccountBlurDialog(Context context) {
		dialogWindow = getWindow();
		dialogView = LayoutInflater.from(context).inflate(
				R.layout.dialog_mine_account_findpassword, null, false);
		ScaleViewUtils.scaleView(dialogView);
		dialogWindow.setContentView(dialogView);
		dialogWindow.setWindowAnimations(0);
		lp = dialogWindow.getAttributes();
		dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		dialogWindow.setGravity(Gravity.BOTTOM);
		frontView = (ClipImageView) dialogView.findViewById(R.id.front_image);
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(dm);
		lp.width = dm.widthPixels;
		lp.height = dm.heightPixels;
		dialogWindow.setAttributes(lp);
		ViewGroup.LayoutParams wl = dialogView.getLayoutParams();
	}

	private void initVideoBlurDialog(Context context) {
		dialogWindow = getWindow();
		dialogView = LayoutInflater.from(context).inflate(
				R.layout.dialog_setup_video, null, false);
		ScaleViewUtils.scaleView(dialogView);
		dialogWindow.setContentView(dialogView);
		dialogWindow.setWindowAnimations(0);
		lp = dialogWindow.getAttributes();
		dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		dialogWindow.setGravity(Gravity.BOTTOM);
		frontView = (ClipImageView) dialogView.findViewById(R.id.front_image);
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(dm);
		lp.width = dm.widthPixels;
		lp.height = dm.heightPixels;
		dialogWindow.setAttributes(lp);
		ViewGroup.LayoutParams wl = dialogView.getLayoutParams();

		RelativeLayout mVideoLayout1 = (RelativeLayout) dialogView
				.findViewById(R.id.setup_video_dialog_layout1);
		mVideoLayout1.setNextFocusRightId(R.id.setup_video_dialog_layout2);
		mVideoLayout1.setOnTouchListener(new TouchListener());
		mVideoLayout1.setOnFocusChangeListener(new FocusChangeListener());
		RelativeLayout mVideoLayout2 = (RelativeLayout) dialogView
				.findViewById(R.id.setup_video_dialog_layout2);
		mVideoLayout2.setNextFocusRightId(R.id.setup_video_dialog_layout3);
		mVideoLayout2.setNextFocusLeftId(R.id.setup_video_dialog_layout1);
		mVideoLayout2.setOnTouchListener(new TouchListener());
		mVideoLayout2.setOnFocusChangeListener(new FocusChangeListener());
		RelativeLayout mVideoLayout3 = (RelativeLayout) dialogView
				.findViewById(R.id.setup_video_dialog_layout3);
		mVideoLayout3.setNextFocusLeftId(R.id.setup_video_dialog_layout2);
		mVideoLayout3.setOnTouchListener(new TouchListener());
		mVideoLayout3.setOnFocusChangeListener(new FocusChangeListener());
	}

	@Override
	public void show() {
		super.show();
		Bitmap bb = Bitmap.createBitmap(backBitmap, 0, 0,
				backBitmap.getWidth(), backBitmap.getHeight());
		frontView.setImageBitmap(bb);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			dismiss();
		}
		return true;
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

		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.setup_childlock_layout_bg) {
			} else if (id == R.id.setup_video_layout_bg) {
			} else if (id == R.id.setup_handle_layout_bg) {
			} else if (id == R.id.setup_update_layout_bg) {
			} else {
			}
			ToastUtils.ShowToast(mContext, "about!!", toast);
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub

		}
	}

	class FocusChangeListener implements OnFocusChangeListener {

		private View view;

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				v.setScaleX(1.18f);
				v.setScaleY(1.18f);
				System.out.println("[onFocusChange]" + v.toString());
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
