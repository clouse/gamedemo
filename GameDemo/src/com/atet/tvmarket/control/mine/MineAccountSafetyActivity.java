package com.atet.tvmarket.control.mine;

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

/**
 * 
 * @author songwei 该类中方法的说明，可以参照"MineAccountManagerActivity"中
 */
public class MineAccountSafetyActivity extends BaseActivity {
	private ALog alog = ALog.getLogger(MineAccountSafetyActivity.class);

	private Toast toast = null;
	private RelativeLayout mAccountsafeLayoutBg1, mAccountsafeLayoutBg2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_mine_accountsafe);
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
		mAccountsafeLayoutBg1 = (RelativeLayout) findViewById(R.id.mine_accountsafe_layoutbg_1);
		mAccountsafeLayoutBg1.setOnClickListener(clickListener);
		mAccountsafeLayoutBg1.setOnTouchListener(touchListener);
		mAccountsafeLayoutBg1.setOnFocusChangeListener(focusChangeListener);
		mAccountsafeLayoutBg2 = (RelativeLayout) findViewById(R.id.mine_accountsafe_layoutbg_2);
		mAccountsafeLayoutBg2.setOnClickListener(clickListener);
		mAccountsafeLayoutBg2.setOnTouchListener(touchListener);
		mAccountsafeLayoutBg2.setOnFocusChangeListener(focusChangeListener);
	}

	class ClickListener implements OnClickListener {
		Intent intent = null;

		@Override
		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.mine_accountsafe_layoutbg_1) {
				intent = new Intent(MineAccountSafetyActivity.this,
						MineModifyPasswordActivity.class);
			} else if (id == R.id.mine_accountsafe_layoutbg_2) {
				intent = new Intent(MineAccountSafetyActivity.this,
						MineBindPhoneActivity.class);
			} else {
			}
			startActivity(intent);
		}
	}
}
