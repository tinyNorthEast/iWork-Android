package com.iwork.ui.activity.regist;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.Base.BaseApplication;
import com.iwork.helper.ToastHelper;
import com.iwork.model.CommonModel;
import com.iwork.model.LoginInfo;
import com.iwork.model.UserInfo;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.preferences.Preferences;
import com.iwork.ui.activity.MainActivity;
import com.iwork.ui.view.TitleBar;
import com.iwork.utils.Constant;
import com.iwork.utils.MD5;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.squareup.okhttp.Request;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Func2;

public class PasswordActivity extends BaseActivity {

    @Bind(R.id.password_ed_pt_input)
    EditText passwordEdPtInput;
    @Bind(R.id.password_img_phone_del)
    ImageView passwordImgPhoneDel;
    @Bind(R.id.password_cm_input)
    EditText passwordCmInput;
    @Bind(R.id.password_img_pwd_del)
    ImageView passwordImgPwdDel;
    @Bind(R.id.password_btn_submit)
    Button passwordBtnSubmit;
    @Bind(R.id.password_titlebar)
    TitleBar titleBar;


    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        ButterKnife.bind(this);
        flag = getIntent().getBooleanExtra("password", false);
        titleBar.setTitle("设置密码");
    }

    @OnClick(R.id.password_btn_submit)
    public void onComplate() {
        String password = passwordEdPtInput.getText().toString().trim();
        String passwordcm = passwordCmInput.getText().toString().trim();
        if (TextUtils.isEmpty(password) && password.length() < Constant.PWD_MIN_LENGTH) {
            ToastHelper.showShortError("请正确填写密码");
            return;
        }
        if (TextUtils.isEmpty(password) && password.length() < Constant.PWD_MIN_LENGTH && !password.equals(passwordcm)) {
            ToastHelper.showShortError("请填写相同密码");
            return;
        }
        showLoading(R.string.loading);
        final UserInfo userInfo = BaseApplication.getAppContext().getmUserInfo();
        String pw = MD5.toMD5(passwordCmInput.getText().toString());
        if (flag) {

            CommonRequest.register(userInfo.phone, pw, userInfo.zh_name, userInfo.email, userInfo.experience, userInfo.position, userInfo.role_code, null, null, new ResultCallback<LoginInfo>() {
                @Override
                public void onError(Request request, Exception e) {

                }

                @Override
                public void onResponse(LoginInfo response) {
                    if (response.getInfoCode() == 0) {
                        cancelLoading();
                        Preferences.getInstance().setPhone(userInfo.phone);
                        ToastHelper.showShortCompleted("注册成功");
                        gotoMainActivity();
                    }
                }
            });
        } else {
            CommonRequest.forgetPassword(userInfo.phone, pw, new ResultCallback<CommonModel>() {
                @Override
                public void onError(Request request, Exception e) {

                }

                @Override
                public void onResponse(CommonModel response) {
                    if (response.getInfoCode() == 0) {

                        ToastHelper.showShortCompleted("修改密码成功");
                        gotoMainActivity();
                    }
                }
            });
        }

    }

    private void gotoMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}