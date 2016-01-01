package com.iwork.ui.activity.myself;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.helper.ResourcesHelper;
import com.iwork.helper.ToastHelper;
import com.iwork.model.InvateCodeMode;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.ui.view.TitleBar;
import com.squareup.okhttp.Request;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InvateCodeActivity extends BaseActivity {

    @Bind(R.id.invatecode_titlebar)
    TitleBar invatecodeTitlebar;
    @Bind(R.id.invatecode_1)
    TextView invatecode1;
    @Bind(R.id.invatecode_2)
    TextView invatecode2;
    private List<InvateCodeMode.InvateCode> invates = Collections.emptyList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invate_code);
        ButterKnife.bind(this);
        invatecodeTitlebar.setTitle("邀请码");
        invatecodeTitlebar.setBackDrawableListener(backListener);
        getData();
    }

    private void getData() {
        CommonRequest.getInvateCode(new ResultCallback<InvateCodeMode>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(InvateCodeMode response) {
                InvateCodeMode.InvateCode i;
                if (response.getInfoCode() == 0) {
                    i = response.getData().get(0);
                    if (i.getStatus() == 1) {
                        invatecode1.setText(i.getCode()+"");
                        invatecode1.setTextColor(ResourcesHelper.getColor(R.color.color_bt_bg));
                        invatecode1.setClickable(true);
                    } else {
                        invatecode1.setTextColor(ResourcesHelper.getColor(R.color.color_gray));
                        invatecode1.setClickable(false);
                    }
                    i = response.getData().get(1);
                    if (i.getStatus() == 1) {
                        invatecode2.setText(i.getCode()+"");
                        invatecode2.setTextColor(ResourcesHelper.getColor(R.color.color_bt_bg));
                        invatecode2.setClickable(true);
                    } else {
                        invatecode2.setClickable(false);
                        invatecode2.setTextColor(ResourcesHelper.getColor(R.color.color_gray));
                    }
                }
            }
        });
    }

    @OnClick(R.id.invatecode_1)
    public void saveInvate1() {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("invatecode", invatecode1.getText().toString().trim());
        cm.setPrimaryClip(clip);
        ToastHelper.showShortInfo("已经复制到剪切板");
    }

    @OnClick(R.id.invatecode_2)
    public void saveInvate2() {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("invatecode", invatecode2.getText().toString().trim());
        cm.setPrimaryClip(clip);
        ToastHelper.showShortInfo("已经复制到剪切板");
    }
}