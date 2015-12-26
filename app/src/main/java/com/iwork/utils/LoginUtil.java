package com.iwork.utils;

import android.text.TextUtils;

import com.impetusconsulting.iwork.R;
import com.iwork.helper.ResourcesHelper;
import com.iwork.helper.ToastHelper;
import com.iwork.preferences.Preferences;

/**
 * Created by JianTao on 15/12/26.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class LoginUtil {
    public static boolean isLogion(){
        boolean islogin =!TextUtils.isEmpty(Preferences.getInstance().getToken());
        if (!islogin)
            ToastHelper.showShortError(ResourcesHelper.getString(R.string.no_login));
        return islogin;
    }
}
