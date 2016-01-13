package com.iwork.ui.activity.myself;

import android.os.Bundle;
import android.text.TextUtils;
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

public class SetMyselfPassword extends BaseActivity {

    @Bind(R.id.setmyself_password_titlebar)
    TitleBar setmyselfPasswordTitlebar;
    @Bind(R.id.setmyself_password_old_ed_pt_input)
    EditText setmyselfPasswordOldEdPtInput;
    @Bind(R.id.setmyself_password_ed_pt_input)
    EditText setmyselfPasswordEdPtInput;
    @Bind(R.id.setmyself_password_cm_input)
    EditText setmyselfPasswordCmInput;
    @Bind(R.id.setmyself_password_btn_submit)
    Button setmyselfPasswordBtnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_myself_password);
        ButterKnife.bind(this);
        setmyselfPasswordTitlebar.setTitle("设置密码");
        setmyselfPasswordTitlebar.setBackDrawableListener(backListener);
    }

    @OnClick(R.id.setmyself_password_btn_submit)
    public void submit() {
        String oldps = setmyselfPasswordOldEdPtInput.getText().toString().trim();
        String ps = setmyselfPasswordEdPtInput.getText().toString().trim();
        String pscm = setmyselfPasswordCmInput.getText().toString().trim();
        if (TextUtil.isEmpty(oldps) && oldps.length() < Constant.PWD_MIN_LENGTH) {
            ToastHelper.showShortError("请正确填写旧密码");
            return;
        }
        if (TextUtils.isEmpty(ps) && ps.length() < Constant.PWD_MIN_LENGTH) {
            ToastHelper.showShortError("请正确填写新密码");
            return;
        }
        if (TextUtils.isEmpty(pscm) && pscm.length() < Constant.PWD_MIN_LENGTH && !pscm.equals(ps)) {
            ToastHelper.showShortError("请填写相同密码");
            return;
        }
        CommonRequest.updataPassword(oldps, ps, new ResultCallback<CommonModel>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(CommonModel response) {
                if (response.getInfoCode() == 0) {
                    ToastHelper.showShortCompleted("修改密码成功");
                    finish();
                }else {
                    ToastHelper.showShortError(response.getMessage());
                }
            }
        });
    }
}
