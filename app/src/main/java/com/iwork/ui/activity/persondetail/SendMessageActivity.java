package com.iwork.ui.activity.persondetail;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.helper.ToastHelper;
import com.iwork.model.CommonModel;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.ui.view.TitleBar;
import com.iwork.utils.Constant;
import com.iwork.utils.TextUtil;
import com.squareup.okhttp.Request;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendMessageActivity extends BaseActivity {

    @Bind(R.id.sendmes_titlebar)
    TitleBar sendmesTitlebar;
    @Bind(R.id.sendmes_ed)
    EditText sendmesEd;
    @Bind(R.id.sendmes_btn_submit)
    Button sendmesBtnSubmit;
    private int c_main_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        ButterKnife.bind(this);
        sendmesTitlebar.setTitle("给顾问留言");
        sendmesTitlebar.setBackDrawableListener(backListener);
        c_main_id = getIntent().getIntExtra(Constant.C_MAIN_ID, 0);
    }

    @OnClick(R.id.sendmes_btn_submit)
    public void sendMessage() {
        String content = sendmesEd.getText().toString().trim();
        if (TextUtil.isEmpty(content)) {
            ToastHelper.showShortError("请输入您的评论");
            return;
        }
        CommonRequest.sendComment(c_main_id, 0, content, new ResultCallback<CommonModel>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(CommonModel response) {
                if (response.getInfoCode() == 0)
                    ToastHelper.showShortInfo("发表评论成功");
                finish();
            }
        });
    }
}
