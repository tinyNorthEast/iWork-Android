package com.iwork.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.iwork.helper.ToastHelper;

/**
 * Created by JianTao on 15/12/28.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class JudgeNetIsConnectedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isNotConnected = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        if (isNotConnected && !JudgeNetIsConnectEd(context))
            ToastHelper.showShortError("无法连接服务器，请检查网络");

    }

    public static boolean JudgeNetIsConnectEd(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo == null)
            return false;
        return networkInfo.isConnected();
    }

    public interface NetOkListerer {
        public void ok(Object data);
    }
}
