package com.iwork.Base;

import android.app.Application;

import com.iwork.model.UserInfo;
import com.iwork.okhttp.OkHttpClientManager;
import com.iwork.utils.Constant;
import com.iwork.utils.Utils;
import com.socks.library.KLog;

import java.util.HashSet;
import java.util.Set;
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

    private UserInfo mUserInfo;
    private Set<String> tagSet;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        OkHttpClientManager.getInstance().getOkHttpClient().setConnectTimeout(10000, TimeUnit.MILLISECONDS);
        KLog.init(BuildConfig.LOG_DEBUG);
        JPushInterface.setDebugMode(BuildConfig.JPUSH_DEBUG);
        JPushInterface.init(this);
        ShareSDK.initSDK(this);
        SMSSDK.initSDK(this, Constant.SMSSDKKEY, Constant.SMSSDKSECRET);
    }

    public static BaseApplication getAppContext() {
        if (mContext == null)
            return new BaseApplication();
        return mContext;
    }

    public UserInfo getmUserInfo() {
        if (mUserInfo != null)
            return mUserInfo;
        else
            return new UserInfo();
    }
}
