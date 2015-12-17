package com.atet.tvmarket.control.mine;

import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.atet.tvmarket.R;
import com.atet.tvmarket.control.common.BaseActivity;

public class KeyboardBaseActivity2 extends BaseActivity {
	Button btnKey_1, btnKey_2, btnKey_3, btnKey_4, btnKey_5, btnKey_6,
			btnKey_7, btnKey_8, btnKey_9, btnKey_0, btnKey_forward,
			btnKey_next, btnKey_sure;
	ImageButton imgBtnKey_delete;
	protected List<View> btnNumKeyList = new ArrayList<View>();
	CurrentTouchListener currentTouchListener = new CurrentTouchListener();
	CurrentFocusChangeListener currentFocusChangeListener = new CurrentFocusChangeListener();

	protected void initKeyboard(OnClickListener clickListener) {
		btnKey_1 = (Button) findViewById(R.id.btn_key_1);
		btnNumKeyList.add(btnKey_1);
		btnKey_2 = (Button) findViewById(R.id.btn_key_2);
		btnNumKeyList.add(btnKey_2);
		btnKey_3 = (Button) findViewById(R.id.btn_key_3);
		btnNumKeyList.add(btnKey_3);
		btnKey_4 = (Button) findViewById(R.id.btn_key_4);
		btnNumKeyList.add(btnKey_4);
		btnKey_5 = (Button) findViewById(R.id.btn_key_5);
		btnNumKeyList.add(btnKey_5);
		btnKey_6 = (Button) findViewById(R.id.btn_key_6);
		btnNumKeyList.add(btnKey_6);
		btnKey_7 = (Button) findViewById(R.id.btn_key_7);
		btnNumKeyList.add(btnKey_7);
		btnKey_8 = (Button) findViewById(R.id.btn_key_8);
		btnNumKeyList.add(btnKey_8);
		btnKey_9 = (Button) findViewById(R.id.btn_key_9);
		btnNumKeyList.add(btnKey_9);
		btnKey_0 = (Button) findViewById(R.id.btn_key_0);
		btnNumKeyList.add(btnKey_0);
		imgBtnKey_delete = (ImageButton) findViewById(R.id.btn_key_delete);
		btnNumKeyList.add(imgBtnKey_delete);
		btnKey_forward = (Button) findViewById(R.id.btn_key_foward);
		btnKey_forward.setOnClickListener(clickListener);
		btnKey_forward.setOnFocusChangeListener(currentFocusChangeListener);
		btnKey_forward.setOnTouchListener(currentTouchListener);
		btnKey_next = (Button) findViewById(R.id.btn_key_next);
		btnKey_next.setOnClickListener(clickListener);
		btnKey_next.setOnFocusChangeListener(currentFocusChangeListener);
		btnKey_next.setOnTouchListener(currentTouchListener);
		btnKey_sure = (Button) findViewById(R.id.btn_key_sure);
		btnKey_sure.setOnClickListener(clickListener);
		btnKey_sure.setOnFocusChangeListener(currentFocusChangeListener);
		btnKey_sure.setOnTouchListener(currentTouchListener);
		for (View view : btnNumKeyList) {
			view.setOnClickListener(clickListener);
			view.setOnFocusChangeListener(currentFocusChangeListener);
			view.setOnTouchListener(currentTouchListener);
		}
	}

	public class CurrentTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			System.out.println("onTouch...");
			if (event.getAction() == MotionEvent.ACTION_UP) {
				OnFocusChangeListener listener = v.getOnFocusChangeListener();
				if (listener != null
						&& listener instanceof CurrentFocusChangeListener) {
					((CurrentFocusChangeListener) listener).setView(v);
				}
			}
			return false;
		}
	}

	public class CurrentFocusChangeListener implements OnFocusChangeListener {

		private View view;

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {

				if (v.isInTouchMode() && v == view) {
					v.performClick();
				} else {
					view = null;
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

	protected void changeToFocusMode(View view) {
		if (view != null) {
			System.out.println("[changeToFocusMode]btn1 get focus");
			view.requestFocus();
			view.requestFocusFromTouch();
		}
	}

	// 判断编辑器是否以游标结尾
	protected boolean isEndOfCursor(TextView textView) {
		if (textView.getText() == null) {
			return false;
		}
		String textStr = "" + textView.getText();
		System.out.println("[isEndOfCursor] string=" + textStr);
		if (textStr.length() == 0) {
			return false;
		}
		int length = textStr.length();
		char end = textStr.charAt(length - 1);
		if (end == '|') {
			return true;
		}
		return false;
	}

	// 用于删除结尾的游标
	protected String deleteCursor(TextView textView) {
		if (textView.getText() == null) {
			System.out.println("[deleteCursor]delele |1");
			return "";
		}
		String textStr = "" + textView.getText();
		int length = textStr.length();
		if (length == 0) {
			System.out.println("[deleteCursor]delele |2");
			return "";
		}
		if (isEndOfCursor(textView)) {
			textStr = textStr.substring(0, length - 1);
			System.out.println("[deleteCursor]delele |3");
		}
		textView.setText(textStr);
		return textStr;
	}

	// 在尾部添加游标
	protected void addCursor(TextView textView) {
		if (textView.getText() == null) {
			textView.setText("|");
			System.out.println("[addCursor]add | 1");
			return;
		}
		String textStr = "" + textView.getText();
		int length = textStr.length();
		if (length == 0) {
			textView.setText("|");
			System.out.println("[addCursor]add | 2");
			return;
		}
		if (isEndOfCursor(textView)) {
			return;
		}
		textView.setText(textStr + "|");
		System.out.println("[addCursor]add | 3");
	}

	// 为控件v添加事件监听
	protected void setListeners(View v, OnClickListener clickListener,
			OnTouchListener touchListener,
			OnFocusChangeListener focusChangeListener) {
		v.setOnClickListener(clickListener);
		v.setOnFocusChangeListener(focusChangeListener);
		v.setOnTouchListener(touchListener);
	}

	protected boolean isNextOrForwardBtn(View view) {
		System.out.println("[isNextOrForwardBtn]1");
		if (view == null) {
			System.out.println("[isNextOrForwardBtn]2");
			return false;
		}
		if (view.getId() == R.id.btn_key_next
				|| view.getId() == R.id.btn_key_foward) {
			System.out.println("[isNextOrForwardBtn]3");
			return true;
		}
		return false;
	}

	// 设置按钮不可用
	protected void setBtnkeyUnusable(Button btn) {
		btn.setEnabled(false);
		btn.setFocusable(false);
		btn.setFocusableInTouchMode(false);
		btn.setClickable(false);
		btn.setTextColor(getResources().getColor(R.color.unenable_gray));
	}

	// 设置按钮可用
	protected void setBtnkeyUsable(Button btn) {
		btn.setEnabled(true);
		btn.setFocusable(true);
		btn.setFocusableInTouchMode(true);
		btn.setClickable(true);
		btn.setTextColor(getResources().getColor(R.color.white));
	}

	// 设置所有的数字键不可用
	protected void setNumkeyUnUsable() {
		for (View view : btnNumKeyList) {
			view.setEnabled(false);
			view.setFocusable(false);
			view.setFocusableInTouchMode(false);
			view.setClickable(false);
			if (view instanceof Button) {
				((Button) view).setTextColor(getResources().getColor(
						R.color.unenable_gray));
			} else if (view instanceof ImageButton) {
				((ImageButton) view)
						.setImageResource(R.drawable.search_key_delete_arrow_unusable);
			}
		}
	}

	// 设置所有的数字键可用
	protected void setNumkeyUsable() {
		for (View view : btnNumKeyList) {
			view.setEnabled(true);
			view.setFocusable(true);
			view.setFocusableInTouchMode(true);
			view.setClickable(true);
			if (view instanceof Button) {
				((Button) view).setTextColor(getResources().getColor(
						R.color.white));
			} else if (view instanceof ImageButton) {
				((ImageButton) view)
						.setImageResource(R.drawable.search_key_delete_arrow);
			}
		}
	}

	protected void setFocusToNextBtn() {
		if (!btnKey_next.isEnabled()) {
			setBtnkeyUsable(btnKey_next);
		}
		btnKey_next.requestFocus();
		btnKey_next.requestFocusFromTouch();
	}

	protected void setFocusToForwardBtn() {
		if (!btnKey_forward.isEnabled()) {
			setBtnkeyUsable(btnKey_forward);
		}
		btnKey_forward.requestFocus();
		btnKey_forward.requestFocusFromTouch();
	}

	protected void setFocusToBtn1() {
		if (!btnKey_1.isEnabled()) {
			setBtnkeyUsable(btnKey_1);
		}
		btnKey_1.requestFocus();
		btnKey_1.requestFocusFromTouch();
	}

	protected void setFocusToBtnsure() {
		if (!btnKey_sure.isEnabled()) {
			setBtnkeyUsable(btnKey_sure);
		}
		btnKey_sure.requestFocus();
		btnKey_sure.requestFocusFromTouch();
	}

	// 设置当聚焦到左边第一个View时(均为textview)的UI情况
	protected void setFirstTextViewUI() {
		// 所有数字键可用
		setNumkeyUsable();
		// "上一个"按钮不可用
		setBtnkeyUnusable(btnKey_forward);
		// "下一个"按钮可用
		setBtnkeyUsable(btnKey_next);
		btnKey_next.setNextFocusUpId(0);
		btnKey_next.setNextFocusLeftId(0);
		// "确定"键不可用
		setBtnkeyUnusable(btnKey_sure);

	}

	// 设置当聚焦到左边中间为Textview的UI情况
	protected void setMiddleTextViewUI() {
		// 所有数字键可用
		setNumkeyUsable();
		// "上一个"按钮可用
		setBtnkeyUsable(btnKey_forward);
		btnKey_forward.setNextFocusUpId(0);
		btnKey_forward.setNextFocusRightId(0);
		// "下一个"按钮可用
		setBtnkeyUsable(btnKey_next);
		btnKey_next.setNextFocusUpId(0);
		btnKey_next.setNextFocusLeftId(0);
		// "确定"键不可用
		setBtnkeyUnusable(btnKey_sure);

	}

	// 设置当聚焦到左边中间为Textview的UI情况
	protected void setMiddleButtonUI() {
		// 所有数字键不可用
		setNumkeyUnUsable();
		// "上一个"按钮可用
		setBtnkeyUsable(btnKey_forward);
		btnKey_forward.setNextFocusUpId(0);
		btnKey_forward.setNextFocusRightId(0);
		// "下一个"按钮可用
		setBtnkeyUsable(btnKey_next);
		btnKey_next.setNextFocusUpId(0);
		btnKey_next.setNextFocusLeftId(0);
		// "确定"键可用
		setBtnkeyUsable(btnKey_sure);
	}

	// 设置当聚焦到左边第一个View时(均为Button)的UI情况
	protected void setLastButtonUI() {
		// 所有数字键不可用
		setNumkeyUnUsable();
		// "上一个"按钮可用
		setBtnkeyUsable(btnKey_forward);
		btnKey_forward.setNextFocusUpId(btnKey_forward.getId());
		btnKey_forward.setNextFocusRightId(btnKey_forward.getId());
		// "下一个"按钮不可用
		setBtnkeyUnusable(btnKey_next);
		// "确定"键可用
		setBtnkeyUsable(btnKey_sure);

	}

	// 设置小键盘上面的所有按键都可用
	protected void setAllUsable() {
		setNumkeyUsable();
		setBtnkeyUsable(btnKey_forward);
		btnKey_forward.setNextFocusUpId(0);
		btnKey_forward.setNextFocusRightId(0);
		setBtnkeyUsable(btnKey_next);
		btnKey_next.setNextFocusUpId(0);
		btnKey_next.setNextFocusLeftId(0);
		setBtnkeyUsable(btnKey_sure);

	}

	// 设置小键盘上面的所有按键都不可用
	protected void setAllUnUsable() {
		setNumkeyUnUsable();
		setBtnkeyUnusable(btnKey_forward);
		setBtnkeyUnusable(btnKey_next);
		setBtnkeyUnusable(btnKey_sure);
	}

	protected void setFirstButtonUI() {
		// 所有数字键不可用
		setNumkeyUnUsable();
		// "上一个"按钮不可用
		setBtnkeyUnusable(btnKey_forward);
		// "下一个"按钮可用
		setBtnkeyUsable(btnKey_next);
		btnKey_next.setNextFocusUpId(btnKey_next.getId());
		btnKey_next.setNextFocusLeftId(btnKey_next.getId());
		// "确定"键不可用
		setBtnkeyUnusable(btnKey_sure);

	}
}
