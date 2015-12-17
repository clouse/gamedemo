package com.atet.tvmarket.utils;

import java.util.Arrays;

public class StringUtils {

	public static String sortByAlphabet(String sourceStr) {
		if (sourceStr == null) {
			return null;
		}
		char[] letterArray = new char[sourceStr.length()];
		for (int i = 0; i < sourceStr.length(); i++) {
			letterArray[i] = sourceStr.charAt(i);
		}
		Arrays.sort(letterArray);
		String strSorted = "";
		for (char c : letterArray) {
			strSorted += c;
		}
		return strSorted;
	}
	
	/** 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false */
	public static boolean isEmpty(String value) {
		if (value != null && !"".equalsIgnoreCase(value.trim())
				&& !"null".equalsIgnoreCase(value.trim())) {
			return false;
		} else {
			return true;
		}
	}
}
