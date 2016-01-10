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
        flag = getIntent().getBooleanExtra(Constant.PASSWORD, false);
        titleBar.setTitle("设置密码");
        titleBar.setBackDrawableListener(backListener);
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
        String pw = passwordCmInput.getText().toString();
        if (flag) {

            CommonRequest.register(userInfo.phone, pw, userInfo.zh_name, userInfo.en_name, userInfo.email, userInfo.company, userInfo.experience, userInfo.position, userInfo.role_code, userInfo.invate_code, userInfo.pic_url, new ResultCallback<LoginInfo>() {
                @Override
                public void onError(Request request, Exception e) {
                    cancelLoading();
                }

                @Override
                public void onResponse(LoginInfo response) {
                    cancelLoading();
                    if (response.getInfoCode() == 0) {
                        Preferences.getInstance().setPhone(userInfo.phone);
                        Preferences.getInstance().setToken(response.getLogin_data().getToken());
                        ToastHelper.showShortCompleted("注册成功");
                        gotoMainActivity();
                    } else {
                        ToastHelper.showShortError(response.getMessage());
                    }
                }
            });
        } else {
            CommonRequest.forgetPassword(userInfo.phone, pw, new ResultCallback<CommonModel>() {
                @Override
                public void onError(Request request, Exception e) {
                    cancelLoading();
                }

                @Override
                public void onResponse(CommonModel response) {
                    cancelLoading();
                    if (response.getInfoCode() == 0) {
                        ToastHelper.showShortCompleted("修改密码成功");
                        gotoMainActivity();
                    } else {
                        ToastHelper.showShortError(response.getMessage());

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
