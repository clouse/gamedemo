package com.atet.tvmarket.utils;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

/**
 * Created by rover12421 on 12/17/14.
 *
 * 使用前需要确保已经初始化
 * 初始化调用:
 * @see #init(Context context)
 */
public class ScaleViewUtils {

    private static int width = 0;
    private static int height = 0;

    /**
     * 同分辨率不再计算
     */
    private static boolean noOpt = false;

    private static final int DEFINE_WIDTH = 1920;
    private static final int DEFINE_Height = 1080;

    public static boolean isNoOpt() {
        return noOpt;
    }

    public static void setNoOpt(boolean noOpt) {
        ScaleViewUtils.noOpt = noOpt;
    }

    private static void init(int width, int height) {
        ScaleViewUtils.width = width;
        ScaleViewUtils.height = height;

        Log.d("windows", "w: " + width + ", h:" + height);

        if (DEFINE_WIDTH == width && DEFINE_Height == height) {
            setNoOpt(true);
        }
    }

    /**
     * 初始化获取设备的真是宽高,只计算一次
     * @param context
     */
    public static void init(Context context) {
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        int heightPixels = getHeightPixels(context);
        if(widthPixels > heightPixels) {
            init(widthPixels, heightPixels);
        } else {
            init(heightPixels, widthPixels);
        }
    }

    private static int getHeightPixels(Context context) {
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        int heightPixels = context.getResources().getDisplayMetrics().heightPixels;
        Display display = windowManager.getDefaultDisplay();

        int rawHeight;
        try {
            rawHeight = (Integer) Display.class.getDeclaredMethod("getRawHeight", (Class[]) null).invoke(display);
        } catch (Exception e) {
            int realHeight;
            try {
                realHeight = (Integer) Display.class.getDeclaredMethod("getRealHeight", (Class[]) null).invoke(display);
            } catch (Exception e1) {
                try {
                    Method method = Display.class.getDeclaredMethod("getRealSize", Point.class);
                    Point point = new Point();
                    method.invoke(display, point);
                    if(point.y > heightPixels) {
                        return point.y;
                    }

                    return heightPixels;
                } catch (Exception e2) {
                    return heightPixels;
                }
            }

            if(realHeight > heightPixels) {
                return realHeight;
            }

            return heightPixels;
        }

        int height = rawHeight;
        if(rawHeight <= heightPixels) {
            height = heightPixels;
        }

        return height;
    }

    /**
     * 缩放View
     * @param view
     */
    public static void scaleView(View view) {
        if(!isNoOpt() && view != null) {
            resetViewSize(view);
            if(view instanceof ViewGroup) {
                scaleView((ViewGroup) view);
            }else{
            	//scaleView(view);
            }
            
        }
    }

    /**
     * 缩放View
     * @param activity
     */
    public static void scaleView(Activity activity) {
        if(!isNoOpt() && activity != null) {
            scaleView(activity.getWindow().getDecorView());
        }
    }

    private static void scaleView(ViewGroup viewGroup) {
        for(int childId = -1 + viewGroup.getChildCount(); childId >= 0; --childId) {
            scaleView(viewGroup.getChildAt(childId));
        }

    }
    
    private static void resetViewSize(View view) {
        if(view == null) {
            Log.e("ScaleView", "child is null");
        } else {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if(layoutParams != null && layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ((ViewGroup.MarginLayoutParams)layoutParams).leftMargin = resetWidth(((ViewGroup.MarginLayoutParams) layoutParams).leftMargin);
                ((ViewGroup.MarginLayoutParams)layoutParams).rightMargin = resetWidth(((ViewGroup.MarginLayoutParams) layoutParams).rightMargin);
                ((ViewGroup.MarginLayoutParams)layoutParams).topMargin = resetHeight(((ViewGroup.MarginLayoutParams) layoutParams).topMargin);
                ((ViewGroup.MarginLayoutParams)layoutParams).bottomMargin = resetHeight(((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin);
            }

            if(layoutParams != null) {
                if(layoutParams.height != -1 && layoutParams.height != -2) {
                    layoutParams.height = resetHeight(layoutParams.height);
                }

                if(layoutParams.width != -1 && layoutParams.width != -2) {
                    layoutParams.width = resetWidth(layoutParams.width);
                }
            }

            if(layoutParams != null) {
                view.setLayoutParams(layoutParams);
            }

            view.setPadding(resetWidth(view.getPaddingLeft()), resetHeight(view.getPaddingTop()), resetWidth(view.getPaddingRight()), resetHeight(view.getPaddingBottom()));
            if(view instanceof TextView) {
                ((TextView)view).setTextSize(0, resetTextSize(((TextView) view).getTextSize()));
            }

            if (view instanceof GridView) {
                GridView gridView = (GridView)view;

                Object mHorizontalSpacing = ReflectUtils.getFieldValueQuietly(GridView.class, "mHorizontalSpacing", gridView);
                if (mHorizontalSpacing != null) gridView.setHorizontalSpacing(resetWidth((Integer)mHorizontalSpacing));

                Object mVerticalSpacing = ReflectUtils.getFieldValueQuietly(GridView.class, "mVerticalSpacing", gridView);
                if (mVerticalSpacing != null) gridView.setVerticalSpacing(resetHeight((Integer)mVerticalSpacing));
            }
            
        }

    }

    /**
     * 重新计算TextSize
     * @param textSize
     * @return
     */
    public static float resetTextSize(float textSize) {
        if (isNoOpt()) {
            return textSize;
        }
        return resetHeight(textSize);
    }

    private static float resetWidth(float w) {
        if (w < 2.0f && w > -2.0f) {
            return w;
        }
        return w * (float) width / DEFINE_WIDTH;
    }

    /**
     * 重新计算width
     * @param width
     * @return
     */
    public static int resetWidth(int width) {
        if (noOpt) {
            return width;
        }
        return (int)resetWidth((float)width);
    }

    /**
     * 重新计算height
     * @param height
     * @return
     */
    public static int resetHeight(int height) {
        if (noOpt) {
            return height;
        }
        return (int)resetHeight((float)height);
    }

    private static float resetHeight(float h) {
        if (h < 2.0f && h > -2.0f) {
            return h;
        }
        return h * (float) height / DEFINE_Height;
    }
}
