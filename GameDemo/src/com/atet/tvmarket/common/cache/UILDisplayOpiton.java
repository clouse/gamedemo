package com.atet.tvmarket.common.cache;

import android.graphics.Bitmap.Config;

import com.atet.tvmarket.app.Configuration;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * @description: image loader图片参数设置
 *
 * @author: LiuQin
 * @date: 2015年5月27日 上午10:17:17 
 */
public class UILDisplayOpiton {

    /**
     * @description: 构建image loader图片参数
     *
     * @param loadingRes 正在加载中的图片资源
     * @param failRes 加载失败后的图片资源
     * @param emptyUriRes 地址有误时的图片资源
     * @param cornerRadiusPixels 圆角大小
     * @return 
     * @author: LiuQin
     * @date: 2015年5月27日 上午10:17:48
     */
    public static DisplayImageOptions getDefaultPhotoOption(int loadingRes, int failRes, int emptyUriRes, int cornerRadiusPixels) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder()
                .showImageOnLoading(loadingRes)
                .showImageForEmptyUri(failRes)
                .showImageOnFail(emptyUriRes)
                .cacheInMemory(Configuration.ENABLE_IMAGE_CACHE_IN_MEMORY)
                .cacheOnDisk(Configuration.ENABLE_IMAGE_CACHE_IN_DISK)
                .bitmapConfig(Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .considerExifParams(true);
        if (cornerRadiusPixels > 0) {
            builder.displayer(new RoundedBitmapDisplayer(cornerRadiusPixels));
        }
        DisplayImageOptions options=builder.build();

        return options;
    }
    
    public static DisplayImageOptions getDefaultPhotoOption(int loadingRes, int failRes, int emptyUriRes) {
        return getDefaultPhotoOption(loadingRes, failRes, emptyUriRes, 0);
    }
    
    public static DisplayImageOptions getDefaultPhotoOption(int loadingRes) {
        return getDefaultPhotoOption(loadingRes, loadingRes, loadingRes);
    }
    
    public static DisplayImageOptions getDefaultPhotoOption(int loadingRes, int cornerRadiusPixels) {
        return getDefaultPhotoOption(loadingRes, loadingRes, loadingRes, cornerRadiusPixels);
    }
}
