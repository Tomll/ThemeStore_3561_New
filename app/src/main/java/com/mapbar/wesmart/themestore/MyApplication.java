package com.mapbar.wesmart.themestore;

import android.app.Application;
import android.content.SharedPreferences;

import com.zhy.autolayout.config.AutoLayoutConifg;

/**
 * Created by dongrp on 2018/10/16.
 */

public class MyApplication extends Application {

    public static SharedPreferences sp;
    public static SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();
        AutoLayoutConifg.getInstance().useDeviceSize();
        sp = getSharedPreferences("theme_store_sp", MODE_PRIVATE);
        editor = sp.edit();
    }
}
