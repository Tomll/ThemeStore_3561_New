package com.mapbar.wesmart.themestore.util;

/**
 * Created by dongrp on 2018/9/30.
 * 打印工具类
 */

public class LogUtil {
    private static final String TAG = "dongrp";//应用的总TAG

    public static void d(Object cls, Object str) {
        if (Configs.isDebug) {//如果是调试模式，才打印log
            android.util.Log.d(TAG, cls.getClass().getSimpleName()+ " -->: " + str);
        }
    }
}
