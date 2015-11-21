package com.iwork.Base;

import android.app.Application;

import com.iwork.okhttp.OkHttpClientManager;
import com.socks.library.KLog;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;

import com.impetusconsulting.iwork.BuildConfig;

import cn.sharesdk.framework.ShareSDK;
import cn.smssdk.SMSSDK;
import okio.Buffer;

/**
 * Created by Jiantao on 15/11/18.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClientManager.getInstance().getOkHttpClient().setConnectTimeout(10000, TimeUnit.MILLISECONDS);
        KLog.init(BuildConfig.LOG_DEBUG);
        JPushInterface.init(getApplicationContext());
        ShareSDK.initSDK(this);
        SMSSDK.initSDK(this, "<您的appkey>", "<您的appsecret>");
    }

}
