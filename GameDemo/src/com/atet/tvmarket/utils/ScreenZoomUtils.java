package com.atet.tvmarket.utils;


public class ScreenZoomUtils {
	/**
	 * @Description:以1280为标准，对其他屏幕的一个缩放值
	 * @author fcs
	 * @date 2013-6-6 下午5:19:10
	 */
	public static float getScreenWZoom_1280(int mScreenWidth) {
		return ((float) mScreenWidth) / 1280;
	}

	/**
	 * @Description:以720为标准，对其他屏幕的一个缩放值
	 * @author fcs
	 * @date 2013-6-6 下午5:26:49
	 */
	public static float getSceennHZoom_720(int mScreenHeight) {
		return ((float) mScreenHeight) / 720;
	}
}
