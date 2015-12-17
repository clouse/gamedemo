package com.atet.tvmarket.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class ChildlockUtils {
	public static final String CHILDLOCK_SP_NAME = "childlock";
	public static final String AGE_KEY = "age";
	public static final String PASSWORD_KEY = "password";
	public static final int AGE1 = 100; // 表示所有的年龄
	public static final int AGE2 = 18;
	public static final int AGE3 = 12;
	public static final String UP = "U"; // 方向键"up"
	public static final String DOWN = "D";// 方向键"down"
	public static final String LEFT = "L";// 方向键"left"
	public static final String RIGHT = "R";// 方向键"right"
	public static final String CONTEXT_FLAG = "gameDetail";
	public static final int REQUEST_CODE = 0;
	public static final String UNIVERSIAL_PASSWORD = "UUDU"; // 万能密码
	private Context context;

	/**
	 * 
	 * @Title: isInAllowedAge
	 * @Description: TODO(判断是否处于所有年龄游戏阶段)
	 * @param @param age
	 * @param @param context
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public static boolean isInAllowedAge(Integer age, Context context) {
		if (age == null) return true;
		SharedPreferences childlockSharedPreferences = context
				.getSharedPreferences(CHILDLOCK_SP_NAME, Activity.MODE_PRIVATE);
		int childlockAge = childlockSharedPreferences.getInt(AGE_KEY, AGE1);
		if (age.intValue() <= childlockAge) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @Title: isTheRightPassword
	 * @Description: TODO(密码是否正确)
	 * @param @param password
	 * @param @param context
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */

	public static boolean isTheRightPassword(String password, Context context) {
		SharedPreferences childlockSharedPreferences = context
				.getSharedPreferences(CHILDLOCK_SP_NAME, Activity.MODE_PRIVATE);
		String childlockPassword = childlockSharedPreferences.getString(
				PASSWORD_KEY, "");
		if (password.equals(childlockPassword)) {
			return true;
		}

		if (UNIVERSIAL_PASSWORD.equals(password)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @Title: getAgeState
	 * @Description: TODO(判断当前所选年龄阶段)
	 * @param @param context
	 * @param @return 设定文件
	 * @return int 返回类型
	 * @throws
	 */

	public static int getAgeState(Context context) {
		SharedPreferences childlockSharedPreferences = context
				.getSharedPreferences(CHILDLOCK_SP_NAME, Activity.MODE_PRIVATE);
		int ageState = childlockSharedPreferences.getInt(AGE_KEY, AGE1);
		return ageState;
	}

	/**
	 * 
	 * @Title: getChildlockPassword
	 * @Description: TODO(获取当前童锁密码)
	 * @param @param context
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String getChildlockPassword(Context context) {
		SharedPreferences childlockSharedPreferences = context
				.getSharedPreferences(CHILDLOCK_SP_NAME, Activity.MODE_PRIVATE);
		String passwordString = childlockSharedPreferences.getString(
				PASSWORD_KEY, "");
		return passwordString;
	}

	/**
	 * 
	 * @Title: hasSetChildlockPassword
	 * @Description: TODO(判断是否设已经设置了童锁密码)
	 * @param @param context
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public static boolean hasSetChildlockPassword(Context context) {
		SharedPreferences childlockSharedPreferences = context
				.getSharedPreferences(CHILDLOCK_SP_NAME, Activity.MODE_PRIVATE);
		String passwordString = childlockSharedPreferences.getString(
				PASSWORD_KEY, "");
		if ("".equals(passwordString)) {
			return false;
		}

		if (passwordString.length() == 4) {
			return true;
		}
		return false;
	}
}
