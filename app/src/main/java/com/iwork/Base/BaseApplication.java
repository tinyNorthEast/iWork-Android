package com.iwork.Base;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.impetusconsulting.iwork.BuildConfig;
import com.iwork.model.UserInfo;
import com.iwork.okhttp.OkHttpClientManager;
import com.iwork.utils.Constant;
import com.socks.library.KLog;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import cn.jiajixin.nuwa.Nuwa;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;
import cn.smssdk.SMSSDK;

/**
 * Created by Jiantao on 15/11/18.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class BaseApplication extends Application {

    private static Context mContext;

    private Set<String> tagSet;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        AppService.getInstance().initService();
        OkHttpClientManager.getInstance().getOkHttpClient().setConnectTimeout(10000, TimeUnit.MILLISECONDS);
        JPushInterface.init(getApplicationContext());
        JPushInterface.setDebugMode(BuildConfig.JPUSH_DEBUG);
        KLog.init(BuildConfig.LOG_DEBUG);
        ShareSDK.initSDK(this);
        SMSSDK.initSDK(this, Constant.SMSSDKKEY, Constant.SMSSDKSECRET);
        userInfo = new UserInfo();
    }

    public static Context getAppContext() {
        return mContext;
    }
    private static UserInfo userInfo;
    public static UserInfo getmUserInfo() {
        return userInfo;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Nuwa.init(this);
        Nuwa.loadPatch(this, "/sdcard/patch.jar");
        Log.d("ylm", getFilesDir().getAbsolutePath());
    }
}
