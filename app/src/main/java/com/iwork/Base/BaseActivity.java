package com.iwork.Base;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.iwork.net.JudgeNetIsConnectedReceiver;
import com.iwork.ui.dialog.DialogHelper;
import com.iwork.ui.dialog.LoadingDialog;
import com.socks.library.KLog;

/**
 * Created by JianTao on 15/11/26.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class BaseActivity extends AppCompatActivity {

    private boolean isFinished;
    private JudgeNetIsConnectedReceiver judgeNetIsConnectedReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addAcitivty(this);
        String name = ActivityManager.getInstance().getTopActivityName();
        judgeNetIsConnectedReceiver = new JudgeNetIsConnectedReceiver();
        KLog.d("TopClassName=" + name);

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(judgeNetIsConnectedReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (judgeNetIsConnectedReceiver != null)
            this.unregisterReceiver(judgeNetIsConnectedReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isFinished)
            ActivityManager.getInstance().removeActivity(this);
        isFinished = true;
    }

    @Override
    public void finish() {
        super.finish();
        isFinished = true;
        ActivityManager.getInstance().removeActivity(this);
    }


    private LoadingDialog loadingDialog;

    protected void showLoading(int sid) {
        if (loadingDialog != null) {
            loadingDialog.cancel();
            loadingDialog = null;
        }
        loadingDialog = new DialogHelper.LoadingDialogBuilder(this).setLoadingContent(getString(sid)).creat();
        loadingDialog.show();
    }

    protected void cancelLoading() {
        if (loadingDialog != null) {
            loadingDialog.cancel();
            loadingDialog = null;
        }
    }

    /**
     * 标题栏返回按钮点击监听
     */
    protected View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };
}
