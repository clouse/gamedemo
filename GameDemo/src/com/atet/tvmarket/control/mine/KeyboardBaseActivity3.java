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

public class KeyboardBaseActivity3 extends BaseActivity {
	Button btnKey_1, btnKey_2, btnKey_3, btnKey_4, btnKey_5, btnKey_6,
			btnKey_7, btnKey_8, btnKey_9, btnKey_0, btnKey_clear,
			btnKey_forward, btnKey_next;
	ImageButton imgBtnKey_delete;
	protected List<View> btnNumKeyList = new ArrayList<View>();
	CurrentTouchListener currentTouchListener = new CurrentTouchListener();
	CurrentFocusChangeListener currentFocusChangeListener = new CurrentFocusChangeListener();

	protected static final int MODIFYPASSWORD_SRC_PASSWORD_FLAG = 0;
	protected static final int MODIFYPASSWORD_NEW_PASSWORD_FLAG = 1;
	protected static final int MODIFYPASSWORD_CONFIRM_PASSWORD_FLAG = 2;
	protected static final int MODIFYPASSWORD_SURE_FLAG = 3;

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
		btnKey_clear = (Button) findViewById(R.id.btn_key_clear);
		btnNumKeyList.add(btnKey_clear);
		btnKey_0 = (Button) findViewById(R.id.btn_key_0);
		btnNumKeyList.add(btnKey_0);
		imgBtnKey_delete = (ImageButton) findViewById(R.id.btn_key_delete);
		btnNumKeyList.add(imgBtnKey_delete);
		btnKey_forward = (Button) findViewById(R.id.btn_key_foward);
		btnKey_forward.setNextFocusUpId(btnKey_forward.getId());
		btnKey_forward.setOnClickListener(clickListener);
		btnKey_forward.setOnFocusChangeListener(currentFocusChangeListener);
		btnKey_forward.setOnTouchListener(currentTouchListener);
		btnKey_next = (Button) findViewById(R.id.btn_key_next);
		btnKey_next.setNextFocusDownId(btnKey_next.getId());
		btnKey_next.setOnClickListener(clickListener);
		btnKey_next.setOnFocusChangeListener(currentFocusChangeListener);
		btnKey_next.setOnTouchListener(currentTouchListener);
		/*
		 * btnKey_sure = (Button) findViewById(R.id.btn_key_sure);
		 * btnKey_sure.setOnClickListener(clickListener);
		 * btnKey_sure.setOnFocusChangeListener(currentFocusChangeListener);
		 * btnKey_sure.setOnTouchListener(currentTouchListener);
		 */
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

	/**
	 * 
	 * @Title: isEndOfCursor
	 * @Description: TODO(判断编辑器是否以游标结尾)
	 * @param @param textView
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
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

	/**
	 * 
	 * @Title: deleteCursor
	 * @Description: TODO(用于删除结尾的游标)
	 * @param @param textView
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
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

	/**
	 * 
	 * @Title: addCursor
	 * @Description: TODO(在编辑框尾部添加游标)
	 * @param @param textView 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void addCursor(TextView textView) {
		if (textView.getText() == null) {
			textView.setText("|");
			return;
		}
		String textStr = "" + textView.getText();
		int length = textStr.length();
		if (length == 0) {
			textView.setText("|");
			return;
		}
		if (isEndOfCursor(textView)) {
			return;
		}
		textView.setText(textStr + "|");
	}

	// 为控件v添加事件监听
	protected void setListeners(View v, OnClickListener clickListener,
			OnTouchListener touchListener,
			OnFocusChangeListener focusChangeListener) {
		v.setOnClickListener(clickListener);
		v.setOnFocusChangeListener(focusChangeListener);
		v.setOnTouchListener(touchListener);
	}

	/*
	 * protected boolean isNextOrForwardBtn(View view) {
	 * System.out.println("[isNextOrForwardBtn]1"); if (view == null) {
	 * System.out.println("[isNextOrForwardBtn]2"); return false; } if
	 * (view.getId() == R.id.btn_key_next || view.getId() ==
	 * R.id.btn_key_foward) { System.out.println("[isNextOrForwardBtn]3");
	 * return true; } return false; }
	 */

	/**
	 * 
	 * @Title: setBtnkeyUnusable
	 * @Description: TODO(设置按钮不可用)
	 * @param @param btn 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void setBtnkeyUnusable(Button btn) {
		btn.setEnabled(false);
		btn.setFocusable(false);
		btn.setFocusableInTouchMode(false);
		btn.setClickable(false);
		btn.setTextColor(getResources().getColor(R.color.unenable_gray));
	}

	/**
	 * 
	 * @Title: setBtnkeyUsable
	 * @Description: TODO(将按钮设置为可用)
	 * @param @param btn 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void setBtnkeyUsable(Button btn) {
		btn.setEnabled(true);
		btn.setFocusable(true);
		btn.setFocusableInTouchMode(true);
		btn.setClickable(true);
		btn.setTextColor(getResources().getColor(R.color.white));
	}

	/**
	 * 
	 * @Title: setNumkeyUnUsable
	 * @Description: TODO(设置所有的数字键不可用)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
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
						.setImageResource(R.drawable.keyboard_delete_arrow_unused);
			}
		}
	}

	/**
	 * 
	 * @Title: setNumkeyUsable
	 * @Description: TODO(设置所有的数字键可用)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
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
						.setImageResource(R.drawable.keyboard_delete_arrow);
			}
		}
	}

	/**
	 * 
	 * @Title: setFocusToNextBtn
	 * @Description: TODO(聚焦在"下一行"按钮)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void setFocusToNextBtn() {
		if (!btnKey_next.isEnabled()) {
			setBtnkeyUsable(btnKey_next);
		}
		btnKey_next.requestFocus();
		btnKey_next.requestFocusFromTouch();
	}

	/**
	 * 
	 * @Title: setFocusToForwardBtn
	 * @Description: TODO(聚焦在"上一行"按钮)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void setFocusToForwardBtn() {
		if (!btnKey_forward.isEnabled()) {
			setBtnkeyUsable(btnKey_forward);
		}
		btnKey_forward.requestFocus();
		btnKey_forward.requestFocusFromTouch();
	}

	/**
	 * 
	 * @Title: setFocusToBtn1
	 * @Description: TODO(聚焦在数字按键1)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void setFocusToBtn1() {
		if (!btnKey_1.isEnabled()) {
			setBtnkeyUsable(btnKey_1);
		}
		btnKey_1.requestFocus();
		btnKey_1.requestFocusFromTouch();
	}

	/**
	 * 
	 * @Title: setFirstTextViewUI
	 * @Description: TODO(设置第一个编辑框被选中时的视图情况)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void setFirstTextViewUI() {
		// 所有数字键可用
		setNumkeyUsable();
		// "上一个"按钮不可用
		setBtnkeyUnusable(btnKey_forward);
		// "下一个"按钮可用
		setBtnkeyUsable(btnKey_next);
		btnKey_next.setNextFocusUpId(btnKey_next.getId());
		btnKey_next.setNextFocusLeftId(0);
	}

	/**
	 * 
	 * @Title: setMiddleTextViewUI
	 * @Description: TODO(设置中间编辑框被选中时的视图情况)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void setMiddleTextViewUI() {
		// 所有数字键可用
		setNumkeyUsable();
		// "上一个"按钮可用
		setBtnkeyUsable(btnKey_forward);
		btnKey_forward.setNextFocusLeftId(0);
		btnKey_forward.setNextFocusDownId(0);
		// "下一个"按钮可用
		setBtnkeyUsable(btnKey_next);
		btnKey_next.setNextFocusLeftId(0);
		btnKey_next.setNextFocusUpId(0);
	}

	/**
	 * 
	 * @Title: setMiddleButtonUI
	 * @Description: TODO(设置中间按钮被选中时的视图情况)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
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
		// setBtnkeyUsable(btnKey_sure);
	}

	/**
	 * 
	 * @Title: setMiddleButtonUI
	 * @Description: TODO(设置最后一个为按钮且被选中时的视图情况)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void setLastButtonUI() {
		// 所有数字键不可用
		setNumkeyUnUsable();
		// "上一个"按钮可用
		setBtnkeyUsable(btnKey_forward);
		btnKey_forward.setNextFocusUpId(btnKey_forward.getId());
		btnKey_forward.setNextFocusRightId(btnKey_forward.getId());
		// "下一个"按钮不可用
		setBtnkeyUnusable(btnKey_next);
	}

	/**
	 * 
	 * @Title: setAllUsable
	 * @Description: TODO(设置小键盘上面的所有按键都可用)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void setAllUsable() {
		setNumkeyUsable();
		setBtnkeyUsable(btnKey_forward);
		btnKey_forward.setNextFocusUpId(0);
		btnKey_forward.setNextFocusRightId(0);
		setBtnkeyUsable(btnKey_next);
		btnKey_next.setNextFocusUpId(0);
		btnKey_next.setNextFocusLeftId(0);
	}

	/**
	 * 
	 * @Title: setAllUnUsable
	 * @Description: TODO(设置小键盘上面的所有按键都不可用)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void setAllUnUsable() {
		setNumkeyUnUsable();
		setBtnkeyUnusable(btnKey_forward);
		setBtnkeyUnusable(btnKey_next);
	}

	/**
	 * 
	 * @Title: setFirstButtonUI
	 * @Description: TODO(设置第一个为按钮且被选中时的视图情况)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void setFirstButtonUI() {
		// 所有数字键不可用
		setNumkeyUnUsable();
		// "上一个"按钮不可用
		setBtnkeyUnusable(btnKey_forward);
		// "下一个"按钮可用
		setBtnkeyUsable(btnKey_next);
		btnKey_next.setNextFocusDownId(btnKey_next.getId());
	}

	/**
	 * 
	 * @Title: setNextFocusSelf
	 * @Description: TODO(将当前视图下一个聚焦按钮设置为自己)
	 * @param @param view 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void setNextFocusSelf(View view) {
		view.setNextFocusUpId(view.getId());
		view.setNextFocusDownId(view.getId());
		view.setNextFocusLeftId(view.getId());
		view.setNextFocusRightId(view.getId());
	}

	/**
	 * 
	 * @Title: setKeyboardNextFocus
	 * @Description: TODO(设置默认时数字键盘的焦点)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected void setKeyboardNextFocus() {
		btnKey_1.setNextFocusUpId(btnKey_1.getId());
		btnKey_1.setNextFocusLeftId(btnKey_1.getId());
		btnKey_2.setNextFocusUpId(btnKey_2.getId());
		btnKey_3.setNextFocusUpId(btnKey_3.getId());
		btnKey_4.setNextFocusLeftId(btnKey_4.getId());
		btnKey_7.setNextFocusLeftId(btnKey_7.getId());
		btnKey_clear.setNextFocusLeftId(btnKey_clear.getId());
		btnKey_clear.setNextFocusDownId(btnKey_clear.getId());
		btnKey_0.setNextFocusDownId(btnKey_0.getId());
		imgBtnKey_delete.setNextFocusDownId(imgBtnKey_delete.getId());
		btnKey_forward.setNextFocusLeftId(btnKey_forward.getId());
	}
}
