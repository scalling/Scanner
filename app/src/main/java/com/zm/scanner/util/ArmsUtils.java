package com.zm.scanner.util;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

/**
 * @author: zengmei
 * @version: v
 * @date: 2019/1/28
 * @description: 描述...
 **/
public class ArmsUtils {
    /**
     * dp 转 px
     *
     * @param context {@link Context}
     * @param dpValue {@code dpValue}
     * @return {@code pxValue}
     */
    public static int dip2px(@NonNull Context context, float dpValue) {
        final float scale = getResources(context).getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    /**
     * 获得资源
     */
    public static Resources getResources(Context context) {
        return context.getResources();
    }
    /**
     * 获得屏幕的宽度
     *
     * @return
     */
    public static int getScreenWidth(Context context) {
        return getResources(context).getDisplayMetrics().widthPixels;
    }
    /**
     * 从 dimens 中获得尺寸
     *
     * @param context
     * @param id
     * @return
     */
    public static int getDimens(Context context, int id) {
        return (int) getResources(context).getDimension(id);
    }

}
