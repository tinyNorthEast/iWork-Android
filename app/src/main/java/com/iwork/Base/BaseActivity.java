package com.iwork.Base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.iwork.ui.dialog.DialogHelper;
import com.iwork.ui.dialog.LoadingDialog;

/**
 * Created by JianTao on 15/11/26.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
}
