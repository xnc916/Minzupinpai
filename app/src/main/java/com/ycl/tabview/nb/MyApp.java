package com.ycl.tabview.nb;

import android.app.Application;

import com.apkfuns.logutils.LogUtils;


public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LogUtils.configAllowLog = true;

        LogUtils.configTagPrefix = "lnyp-";
    }
}
