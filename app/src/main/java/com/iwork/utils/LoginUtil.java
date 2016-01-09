package com.iwork.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.impetusconsulting.iwork.R;
import com.iwork.helper.ResourcesHelper;
import com.iwork.helper.ToastHelper;
import com.iwork.preferences.Preferences;
import com.iwork.ui.activity.LoginActivity;

/**
 * Created by JianTao on 15/12/26.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class LoginUtil {
    public static boolean isLogin(){
        boolean islogin =!TextUtils.isEmpty(Preferences.getInstance().getToken());
        if (!islogin)
            ToastHelper.showShortError(ResourcesHelper.getString(R.string.no_login));
        return islogin;
    }
    public static void goToLogin(Activity context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        context.finish();
    }
}
