package com.iwork.Base;

import android.app.Application;

import com.iwork.model.UserInfo;
import com.iwork.okhttp.OkHttpClientManager;
import com.iwork.utils.Constant;
import com.socks.library.KLog;

import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;

import com.impetusconsulting.iwork.BuildConfig;

import cn.sharesdk.framework.ShareSDK;
import cn.smssdk.SMSSDK;

/**
 * Created by Jiantao on 15/11/18.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class BaseApplication extends Application {

    private static BaseApplication mContext;

    public UserInfo mUserInfo;
    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClientManager.getInstance().getOkHttpClient().setConnectTimeout(10000, TimeUnit.MILLISECONDS);
        KLog.init(BuildConfig.LOG_DEBUG);
        JPushInterface.setDebugMode(BuildConfig.JPUSH_DEBUG);
        JPushInterface.init(this);
        ShareSDK.initSDK(this);
        SMSSDK.initSDK(this, Constant.SMSSDKKEY,Constant.SMSSDKSECRET);
        mContext = this;
    }
    public static BaseApplication getAppContext() {
        return mContext;
    }
}
