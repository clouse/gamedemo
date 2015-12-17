package com.atet.tvmarket.utils;

import android.view.KeyEvent;

/**
 * Created by zhouwei on 2015/9/2.
 */
public class KeyConversionUtils {

    /**
     * 是否需要对确定键进行转换
     * @param keyCode
     * @return
     */
    public static boolean isConversionEnter(int keyCode) {

        if (keyCode == KeyEvent.KEYCODE_A
                || keyCode == KeyEvent.KEYCODE_BUTTON_A
                || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            return true;
        }
        return false;
    }

    /**
     * 是否需要对返回键进行转换
     * @param keyCode
     * @return
     */
    public static boolean isConversionBack(int keyCode) {

        if (keyCode == KeyEvent.KEYCODE_B
                || keyCode == KeyEvent.KEYCODE_BUTTON_B) {
            return true;
        }
        return false;
    }

    /**
     * 把KeyEvent转换成确定键
     * @param event
     * @return
     */
    public static KeyEvent conversionEnter(KeyEvent event) {
        return conversion(event, KeyEvent.KEYCODE_ENTER);
    }

    /**
     * 把KeyEvent转换成返回键
     * @param event
     * @return
     */
    public static KeyEvent conversionBack(KeyEvent event) {
        return conversion(event, KeyEvent.KEYCODE_BACK);
    }

    /**
     * 转换KeyEvent(暂时没有用反射来直接修改类的值)
     * @param event
     * @param keyCode
     * @return
     */
    public static KeyEvent conversion(KeyEvent event, int keyCode) {

        if (event == null) return null;

        KeyEvent keyEvent = new KeyEvent(
                event.getDownTime(),
                event.getEventTime(),
                event.getAction(),
                keyCode,
                event.getRepeatCount(),
                event.getMetaState(),
                event.getDeviceId(),
                keyCode,
                event.getFlags(),
                event.getSource());

        return keyEvent;
    }

    /**
     * 是否需要过滤keyCode
     * @param keyCode
     * @return
     */
    public static boolean isFilterKeyCode(int keyCode) {

        if (keyCode == KeyEvent.KEYCODE_A
                || keyCode == KeyEvent.KEYCODE_BUTTON_A
                || keyCode == KeyEvent.KEYCODE_B
                || keyCode == KeyEvent.KEYCODE_BUTTON_B
                || keyCode == KeyEvent.KEYCODE_X
                || keyCode == KeyEvent.KEYCODE_BUTTON_X
                || keyCode == KeyEvent.KEYCODE_Y
                || keyCode == KeyEvent.KEYCODE_BUTTON_Y
                || keyCode == KeyEvent.KEYCODE_BUTTON_SELECT
                || keyCode == KeyEvent.KEYCODE_BUTTON_START) {
            return true;
        }
        return false;
    }

    /**
     * 处理ABXY在没有响应的情况下系统不会附加值传递过来
     * @param originalKeyCode
     * @param handlerResult
     * @return
     */
    public static boolean filterAppendKeyCode(int originalKeyCode, boolean handlerResult) {
        return !handlerResult ? isFilterKeyCode(originalKeyCode) : handlerResult;
    }
}
