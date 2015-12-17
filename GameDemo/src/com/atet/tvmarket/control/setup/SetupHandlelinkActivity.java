package com.atet.tvmarket.control.setup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.ScreenShot;

public class SetupHandlelinkActivity extends BaseActivity {
	public static final int HANDLE_TYPE_1 = 1;
	public static final int HANDLE_TYPE_2 = 2;
	public static final int HANDLE_TYPE_3 = 3;
	private ALog alog = ALog.getLogger(SetupHandlelinkActivity.class);
	private Toast toast = null;
	private RelativeLayout mHandlelinkLayout1, mHandlelinkLayout2,
			mHandlelinkLayout3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_setup_handle);
		ScaleViewUtils.scaleView(this);
		setBlackTitle(false);
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setTitleData();
	}

	private void initView() {
		ClickListener clickListener = new ClickListener();
		mHandlelinkLayout1 = (RelativeLayout) findViewById(R.id.setup_handlelink_dialog_layout1);
		mHandlelinkLayout1.setOnClickListener(clickListener);
		mHandlelinkLayout1.setOnTouchListener(touchListener);
		mHandlelinkLayout1.setOnFocusChangeListener(focusChangeListener);
		mHandlelinkLayout2 = (RelativeLayout) findViewById(R.id.setup_handlelink_dialog_layout2);
		mHandlelinkLayout2.setOnClickListener(clickListener);
		mHandlelinkLayout2.setOnTouchListener(touchListener);
		mHandlelinkLayout2.setOnFocusChangeListener(focusChangeListener);
		mHandlelinkLayout3 = (RelativeLayout) findViewById(R.id.setup_handlelink_dialog_layout3);
		mHandlelinkLayout3.setOnClickListener(clickListener);
		mHandlelinkLayout3.setOnTouchListener(touchListener);
		mHandlelinkLayout3.setOnFocusChangeListener(focusChangeListener);
	}

	class ClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = null;
			int id = v.getId();
			if (id == R.id.setup_handlelink_dialog_layout1) {
				// 跳转到2.4G手柄控制界面
				intent = new Intent(SetupHandlelinkActivity.this,
						SetupHandlelinkDetail3Activity.class);
			} else if (id == R.id.setup_handlelink_dialog_layout2) {
				intent = new Intent(SetupHandlelinkActivity.this,
						SetupHandlelinkDetailActivity.class);
				intent.putExtra("handleType", HANDLE_TYPE_1);
			} else if (id == R.id.setup_handlelink_dialog_layout3) {
				intent = new Intent(SetupHandlelinkActivity.this,
						SetupHandlelinkDetailActivity.class);
				intent.putExtra("handleType", HANDLE_TYPE_2);
			} else {
			}
			startActivity(intent);
		}
	}
}
