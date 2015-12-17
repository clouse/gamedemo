package com.atet.tvmarket.utils;

import com.atet.tvmarket.R;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by zhouwei on 2015/9/1.
 */
public class GAnimationUtils {

    private static Animation scaleSmallAnimation;
    private static Animation scaleBigAnimation;

    public static void init(Context context) {

        if (scaleSmallAnimation != null && scaleBigAnimation != null) return ;

        scaleBigAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_scale_big);
        scaleSmallAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_scale_small);
    }

    public static void zoomOut(View view) {

        if (view == null) return ;

        view.startAnimation(scaleBigAnimation);

        // 不带动画的放大(保持与游戏大厅一致)
//        view.setScaleX(1.1f);
//        view.setScaleY(1.1f);
    }

    public static void zoomIn(View view) {

        if (view == null) return ;

        view.startAnimation(scaleSmallAnimation);

        // 不带动画的缩小(保持与游戏大厅一致)
//        view.setScaleX(1.0f);
//        view.setScaleY(1.0f);
    }
}
