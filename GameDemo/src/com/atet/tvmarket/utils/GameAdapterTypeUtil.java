package com.atet.tvmarket.utils;

import android.view.View;

public class GameAdapterTypeUtil {
	private static final int IMITATE = 1, GAMEPAD = 2, KEYBOARD = 4,CONTROL = 8,
			IMITATEANDCONTROL=9,GAMEPADANDCONTROL=10,KEYBOARDANDCONTROL=12 ,ALL=15;
	
	/**
	 * 判断适配的类型
	 * */
	public static String decideAdapter(Integer handType) {
		switch (handType) {
		case IMITATE:
			return "手柄";
		case GAMEPAD:
			return "手柄";
		case KEYBOARD:
			return "手柄";
		case CONTROL:
			return "遥控器";
		case IMITATEANDCONTROL:
			return "手柄+遥控器";
		case GAMEPADANDCONTROL:
			return "手柄+遥控器";
		case KEYBOARDANDCONTROL:
			return "手柄+遥控器";
		case ALL:
			return "手柄+遥控器";
		}
		
		return "";
	}
	
	/**
	 * 判断适配的类型
	 * */
	public static void decideAdapter(Integer handType,View v1,View v2) {
		switch (handType) {
		case IMITATE:
			v1.setVisibility(View.VISIBLE);
			v2.setVisibility(View.GONE);
			break;
		case GAMEPAD:
			v1.setVisibility(View.VISIBLE);
			v2.setVisibility(View.GONE);
			break;
		case KEYBOARD:
			v1.setVisibility(View.VISIBLE);
			v2.setVisibility(View.GONE);
			break;
		case CONTROL:
			v1.setVisibility(View.GONE);
			v2.setVisibility(View.VISIBLE);
			break;
		case IMITATEANDCONTROL:
			v1.setVisibility(View.VISIBLE);
			v2.setVisibility(View.VISIBLE);
			break;
		case GAMEPADANDCONTROL:
			v1.setVisibility(View.VISIBLE);
			v2.setVisibility(View.VISIBLE);
			break;

		case KEYBOARDANDCONTROL:
			v1.setVisibility(View.VISIBLE);
			v2.setVisibility(View.VISIBLE);
			break;
		case ALL:
			v1.setVisibility(View.VISIBLE);
			v2.setVisibility(View.VISIBLE);
			break;
		default:
			v1.setVisibility(View.VISIBLE);
			v2.setVisibility(View.GONE);
			break;
		}
	}
}
