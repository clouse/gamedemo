package com.atet.tvmarket.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.graphics.Color;
import android.widget.Button;
import android.widget.ImageButton;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.entity.dao.GameSearchPinyinInfo;

public class KeyboardUtils {
	static ALog alog = ALog.getLogger(KeyboardUtils.class);
	public static final String initKeyboardStrHor = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";// 按横排排列字母表字符串
	public static final String initKeyboardStrVer = "AGMSY4BHNTZ5CIOU06DJPV17EKQW28FLRX39";// 按竖排排列字母表字符串
	public static final int KEYBOARD_LINE_BTNCOUNT = 6; // 键盘上每一行的按键数

	public static final int PHONENUM_LENGTH = 11; // 手机号码的长度限制
	public static final int VERIFICATIONCODE_LENGTH = 6; // 验证码长度限制
	public static final int PASSWORD_LENGTH = 18; // 键盘上每一行的按键数
	public static final String STARTS = "******************************";// 31个

	/**
	 * @param 需要判断的字母
	 * @param 可点击的字母字符串
	 * @return 判断焦点是否可以向上移动
	 */
	public static boolean isNeedSetFocusup(String chargeLetter,
			String clickableLetters) {
		boolean isNeed = false;
		int lineButs = KEYBOARD_LINE_BTNCOUNT;
		String subClickableLetters = clickableLetters.substring(0,
				clickableLetters.indexOf(chargeLetter));
		int currentIndex = initKeyboardStrHor.indexOf(chargeLetter);
		int range = (currentIndex / lineButs) * lineButs;
		for (int i = 0; i < subClickableLetters.length(); i++) {
			String c = "" + subClickableLetters.charAt(i);
			if (initKeyboardStrHor.indexOf(c) <= range) {
				isNeed = true;
				break;
			}
		}
		return isNeed;
	}

	/**
	 * 
	 * @description：获取可点击的字母
	 * @param inputLetters
	 *            已经输入的字母
	 * @return 可点击的字母
	 * @author: songwei
	 */
	public static String getClickableLetters(String inputLetters,
			List<GameSearchPinyinInfo> gameSearchPinyinInfoList) {
		String clickableLetters = "";
		if (inputLetters == null) {
			inputLetters = "";
		}
		List<GameSearchPinyinInfo> gameInfos = gameSearchPinyinInfoList;
		int inputLetterLen = inputLetters.length();
		int gameLettersLength = 0;
		String gameLetters = "";
		String newLetter = "";// 当前游戏得到新的可点击字母
		boolean isNew = true;
		for (GameSearchPinyinInfo gameInfo : gameInfos) {
			gameLetters = gameInfo.getPinyin();
			gameLettersLength = gameLetters.length();
			// 如果当前游戏字母不是以输入的字母开头，无需再匹配
			if (!gameLetters.startsWith(inputLetters) && !(inputLetterLen == 0)) {
				continue;
			}
			// 如果当前游戏字母长度没有输入的拼音长，当前游戏无需再匹配可点击的字母
			if (inputLetterLen >= gameLettersLength) {
				continue;
			}

			// 查找的字符(处理出错特殊字符)
			char tempChar = gameLetters.charAt(inputLetterLen);
			int index = KeyboardUtils.initKeyboardStrVer.indexOf(tempChar);
			if (index == -1) continue;

			newLetter = "" + tempChar;
			for (int i = 0; i < clickableLetters.length(); i++) {
				// 如果该字母已经存在于原有字符串中
				if ((newLetter).equals("" + clickableLetters.charAt(i))) {
					isNew = false;
					break;
				}
				isNew = true;
			}
			if (isNew) {
				clickableLetters = clickableLetters + newLetter;
			}
		}
		return clickableLetters;
	}

	/**
	 * @description 更改字母键盘的显示
	 * @param keyboardButtons
	 *            键盘中所有的按键集合
	 * @param letters
	 *            当前键盘中可以被点击的按键字母字符串
	 * @param btnDefaultFocus
	 *            当键盘没有可以选择的按键时，默认的聚焦按键
	 */
	public static void changeKeyboardView(Map<String, Button> keyboardButtons,
			String letters, ImageButton btnDefaultFocus) {
		int firstLetter = 0;
		int charIndex = 0;
		KeyboardUtils.setKeyBtnsUnused(keyboardButtons);
		letters = StringUtils.sortByAlphabet(letters);
		System.out.println("letters2 == " + letters);
		for (int i = 0; i < letters.length(); i++) {
			String letter = ("" + letters.charAt(i)).toUpperCase();
			Button btnKey = keyboardButtons.get(letter);
			KeyboardUtils.setKeyBtnUseable(btnKey);
			if(btnKey !=null){
				if (!KeyboardUtils.isNeedSetFocusup(letter, letters)) {
					btnKey.setNextFocusUpId(btnKey.getId());
					System.out.println("[changeKeyboardView]" + btnKey.getText());
				} else {
					btnKey.setNextFocusUpId(0);
				}
			}
			charIndex = KeyboardUtils.initKeyboardStrVer.indexOf(letter);
			if (i == 0) {
				firstLetter = charIndex;
			}
			if (charIndex < firstLetter && charIndex != -1) {
				firstLetter = charIndex;
			}
		}
		//判断特殊字符
		if(letters.length() == 1){
			char[] charArray = letters.toCharArray();
			char ch = charArray[0] ;
			if(!((ch >= 'A'  && ch <= 'Z') || (ch >= '0'  && ch <= '9'))){
				btnDefaultFocus.requestFocus();
				btnDefaultFocus.requestFocusFromTouch();
				return;
			}
		}
		
		if (letters == null || "".equals(letters)) {
			btnDefaultFocus.requestFocus();
			btnDefaultFocus.requestFocusFromTouch();
		} else {
			Button btnFirstLetter = keyboardButtons.get(("" + KeyboardUtils.initKeyboardStrVer.charAt(firstLetter)).toUpperCase());
			btnFirstLetter.requestFocus();
			btnFirstLetter.requestFocusFromTouch();
		}
	}
	
	private static  int getIndex(Map<String, Button> keyboardButtons,Button btn){
		
		for (int i = 0; i < keyboardButtons.size(); i++){
			String letter  = "" + initKeyboardStrHor.charAt(i);
			if(letter .equals(btn.getText().toString())){
				return i;
			};
		}
		return -1;	
		}
	
	
	private static int getStartSearchIndex(Map<String, Button> keyboardButtons,Button btn){
		int index = getIndex(keyboardButtons, btn);
		if(index != -1){
			return (((int)index/6)+1) * 6  ; 
		}
		return keyboardButtons.size();
	}
	
	/**
	 * 
	 * @Title: hasUsableKeysBelow
	 * @Description: 
	 *               TODO(用于处理A,B,G按键按"下方向键"无法聚焦到"清除"按键的情况，这个方法只能临时处理方案，须有以后发现更多的现象后再做分析
	 *               )
	 * @param @param keyboardButtons
	 * @param @param btn
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public static boolean hasUsableKeysBelow(Map<String, Button> keyboardButtons, Button btn) {
			int startSearchIndex = getStartSearchIndex(keyboardButtons,btn); 
			//System.out.println("startSearchIndex = " + startSearchIndex);
			for (int i = startSearchIndex; i < keyboardButtons.size(); i++) {
				String letter = "" + initKeyboardStrHor.charAt(i);
				Button b = keyboardButtons.get(letter);
				if (b.isEnabled()) {
					alog.info("true");
					return true;
				}
			}
		return false;
		/*if ("A".equals(btn.getText())) {
			for (int i = 6; i < keyboardButtons.size(); i++) {
				String letter = "" + initKeyboardStrHor.charAt(i);
				Button b = keyboardButtons.get(letter);
				if (b.isEnabled()) {
					alog.info("true");
					return true;
				}
			}
		} else if ("B".equals(btn.getText())) {
			for (int i = 6; i < keyboardButtons.size(); i++) {
				String letter = "" + initKeyboardStrHor.charAt(i);
				Button b = keyboardButtons.get(letter);
				if (b.isEnabled()) {
					alog.info("true");
					return true;
				}
			}
		} else if ("G".equals(btn.getText())) {
			for (int i = 12; i < keyboardButtons.size(); i++) {
				String letter = "" + initKeyboardStrHor.charAt(i);
				Button b = keyboardButtons.get(letter);
				if (b.isEnabled()) {
					alog.info("true");
					return true;
				}
			}
		}
		alog.info("false");
		return false;*/
	}
	
	

	public static boolean SearshUpKeyFocus(Map<String, Button> keyboardButtons,Button btn) {
		if ("3".equals(btn.getText())) {
			Button b = keyboardButtons.get("X");
			if (b.isEnabled()) {
				alog.info("true");
				return true;
			}
		}
		return false;
	}

	/**
	 * 设置所有键盘字母按键按钮设为不可用
	 * 
	 * @param keyboardButtons
	 */
	public static void setKeyBtnsUnused(Map<String, Button> keyboardButtons) {
		for (Button btn : keyboardButtons.values()) {
			btn.setEnabled(false);
			btn.setFocusable(false);
			btn.setFocusableInTouchMode(false);
			btn.setTextColor(Color.GRAY);
		}
	}

	/**
	 * 设置键盘按键按钮可用
	 * 
	 * @param btn
	 */
	public static void setKeyBtnUseable(Button btn) {
		if(btn != null){
			btn.setEnabled(true);
			btn.setFocusable(true);
			btn.setFocusableInTouchMode(true);
			btn.setTextColor(Color.WHITE);
			}
	}
}
