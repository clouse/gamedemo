package com.atet.tvmarket.control.mine;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.atet.tvmarket.R;
import com.atet.tvmarket.control.common.BaseActivity;

public class KeyboardBaseActivity extends BaseActivity {
	Button btnKey_1, btnKey_2, btnKey_3, btnKey_4, btnKey_5, btnKey_6,
			btnKey_7, btnKey_8, btnKey_9, btnKey_0, btnKey_forward,
			btnKey_next;
	ImageButton imgBtnKey_delete;
	protected List<Button> btnNumKeyList = new ArrayList<Button>();

	protected void initKeyboard(OnClickListener clickListener) {
		btnKey_1 = (Button) findViewById(R.id.btn_key_1);
		btnKey_1.setOnClickListener(clickListener);
		btnNumKeyList.add(btnKey_1);
		btnKey_2 = (Button) findViewById(R.id.btn_key_2);
		btnKey_2.setOnClickListener(clickListener);
		btnNumKeyList.add(btnKey_2);
		btnKey_3 = (Button) findViewById(R.id.btn_key_3);
		btnKey_3.setOnClickListener(clickListener);
		btnNumKeyList.add(btnKey_3);
		btnKey_4 = (Button) findViewById(R.id.btn_key_4);
		btnKey_4.setOnClickListener(clickListener);
		btnNumKeyList.add(btnKey_4);
		btnKey_5 = (Button) findViewById(R.id.btn_key_5);
		btnKey_5.setOnClickListener(clickListener);
		btnNumKeyList.add(btnKey_5);
		btnKey_6 = (Button) findViewById(R.id.btn_key_6);
		btnKey_6.setOnClickListener(clickListener);
		btnNumKeyList.add(btnKey_6);
		btnKey_7 = (Button) findViewById(R.id.btn_key_7);
		btnKey_7.setOnClickListener(clickListener);
		btnNumKeyList.add(btnKey_7);
		btnKey_8 = (Button) findViewById(R.id.btn_key_8);
		btnKey_8.setOnClickListener(clickListener);
		btnNumKeyList.add(btnKey_8);
		btnKey_9 = (Button) findViewById(R.id.btn_key_9);
		btnKey_9.setOnClickListener(clickListener);
		btnNumKeyList.add(btnKey_9);
		btnKey_0 = (Button) findViewById(R.id.btn_key_0);
		btnKey_0.setOnClickListener(clickListener);
		btnNumKeyList.add(btnKey_0);
		imgBtnKey_delete = (ImageButton) findViewById(R.id.btn_key_delete);
		imgBtnKey_delete.setOnClickListener(clickListener);
		btnKey_forward = (Button) findViewById(R.id.btn_key_foward);
		btnKey_forward.setOnClickListener(clickListener);
		btnKey_next = (Button) findViewById(R.id.btn_key_next);
		btnKey_next.setOnClickListener(clickListener);
//		btnKey_sure = (Button) findViewById(R.id.btn_key_sure);
//		btnKey_sure.setOnClickListener(clickListener);
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
	}

	// 设置按钮可用
	protected void setBtnkeyUsable(Button btn) {
		btn.setEnabled(true);
		btn.setFocusable(true);
		btn.setFocusableInTouchMode(true);
		btn.setClickable(true);
	}

	// 设置所有的数字键不可用
	protected void setNumkeyUnUsable() {
		for (Button btn : btnNumKeyList) {
			btn.setEnabled(false);
			btn.setFocusable(false);
			btn.setFocusableInTouchMode(false);
			btn.setClickable(false);
		}
		imgBtnKey_delete.setEnabled(false);
		imgBtnKey_delete.setFocusable(false);
		imgBtnKey_delete.setFocusableInTouchMode(false);
		imgBtnKey_delete.setClickable(false);
	}

	// 设置所有的数字键可用
	protected void setNumkeyUsable() {
		for (Button btn : btnNumKeyList) {
			btn.setEnabled(true);
			btn.setFocusable(true);
			btn.setFocusableInTouchMode(true);
			btn.setClickable(true);
		}
		imgBtnKey_delete.setEnabled(true);
		imgBtnKey_delete.setFocusable(true);
		imgBtnKey_delete.setFocusableInTouchMode(true);
		imgBtnKey_delete.setClickable(true);
	}
}
