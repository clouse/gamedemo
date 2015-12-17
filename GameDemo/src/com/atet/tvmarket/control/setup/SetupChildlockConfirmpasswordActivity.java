package com.atet.tvmarket.control.setup;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.utils.ChildlockUtils;
import com.atet.tvmarket.utils.ScaleViewUtils;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint("NewApi")
public class SetupChildlockConfirmpasswordActivity extends BaseActivity {
	private ALog alog = ALog
			.getLogger(SetupChildlockConfirmpasswordActivity.class);
	private Toast toast = null;
	private Button btnSure;
	private Intent intent = null;
	private int age = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_setup_childlock_confirmpassword);
		intent = this.getIntent();
		age = intent.getIntExtra(ChildlockUtils.AGE_KEY, ChildlockUtils.AGE1);
		ScaleViewUtils.scaleView(this);
		initView();
		setBlackTitle(false);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setTitleData();
	}

	private void initView() {
		ClickListener clickListener = new ClickListener();
		btnSure = (Button) findViewById(R.id.childlock_confirmpassword_sure_btn);
		btnSure.setOnClickListener(clickListener);
		btnSure.setOnFocusChangeListener(focusChangeListener);
		btnSure.setOnTouchListener(touchListener);
	}

	class ClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.childlock_confirmpassword_sure_btn) {
				finish();
			} else {
			}
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_B
				|| keyCode == KeyEvent.KEYCODE_BUTTON_B) {
			finish();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		alog.info("[onKeyDown]");
		// TODO Auto-generated method stub
		return false;
	}
}
