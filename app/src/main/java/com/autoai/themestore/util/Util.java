package com.autoai.themestore.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by dongrp on 2018/9/30.
 * 打印工具类
 */

public class Util {
    private static final String TAG = "dongrp";//应用的总TAG

    public static void d(Object cls, Object str) {
        if (Configs.isDebug) {//如果是调试模式，才打印log
            android.util.Log.d(TAG, cls.getClass().getSimpleName() + " -->: " + str);
        }
    }

    public static void toastShort(Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void toastShort(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

}
