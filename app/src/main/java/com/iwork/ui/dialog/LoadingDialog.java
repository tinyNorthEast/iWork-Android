package com.iwork.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.impetusconsulting.iwork.R;
import com.iwork.helper.ResourcesHelper;


/**
 * @auther ZhangJianTao
 * @function 正在取消对话框
 * @since 2015/6/10
 */
public class LoadingDialog extends Dialog {

    private TextView loadingTextView;
    private String loadingContent;

    public LoadingDialog(Context context) {
        super(context, R.style.CommonDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_progressdialog);
        this.loadingTextView = (TextView) findViewById(R.id.loadingTextView);
        if (TextUtils.isEmpty(loadingContent)) {
            loadingContent = ResourcesHelper.getString(R.string.loading);
        }
        loadingTextView.setText(loadingContent);
    }

    public void setLoadingContent(String loadingContent) {
        this.loadingContent = loadingContent;
    }

}
