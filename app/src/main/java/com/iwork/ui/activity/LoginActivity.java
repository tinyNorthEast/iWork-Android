package com.iwork.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.helper.ToastHelper;
import com.iwork.model.LoginInfo;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.preferences.Preferences;
import com.iwork.ui.activity.regist.RegisterActivity;
import com.iwork.ui.view.TitleBar;
import com.iwork.utils.Constant;
import com.iwork.utils.MD5;
import com.iwork.utils.Utils;
import com.socks.library.KLog;
import com.squareup.okhttp.Request;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.login_ed_phone_input)
    EditText loginEdPhoneInput;
    @Bind(R.id.login_img_phone_del)
    ImageView loginImgPhoneDel;
    @Bind(R.id.login_ed_pwd_input)
    EditText loginEdPwdInput;
    @Bind(R.id.login_img_pwd_del)
    ImageView loginImgPwdDel;
    @Bind(R.id.login_btn_submit)
    Button loginBtnSubmit;
    @Bind(R.id.login_tv_forgot_pw)
    TextView loginTvForgotPw;
    @Bind(R.id.login_tv_sign_now)
    TextView loginTvSignNow;
    @Bind(R.id.login_random)
    TextView loginRandom;
    @Bind(R.id.login_titlebar)
    TitleBar titleBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        showInputMethod();
        titleBar.setTitle("登陆");
        titleBar.setBackDrawableListener(backListener);
    }

    /**
     * 标题栏返回按钮点击监听
     */
    private View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    /**
     * 登录回调
     */
    private ResultCallback<LoginInfo> callback = new ResultCallback<LoginInfo>() {
        @Override
        public void onError(Request request, Exception e) {

        }

        @Override
        public void onResponse(LoginInfo response) {
            KLog.i("--login", response.toString());
            cancelLoading();
            if (response.getInfoCode() == 0) {
                ToastHelper.showShortCompleted("登录成功");
                Preferences.getInstance().setToken(response.getLogin_data().getToken());
                Preferences.getInstance().setZhName(response.getLogin_data().getZh_name());
                Preferences.getInstance().setUserId(response.getLogin_data().getUserId());
                Preferences.getInstance().setPhone(phone);
                JPushInterface.setAlias(getApplicationContext(), response.getLogin_data().getUserId(), null);
                finish();
            } else {
                ToastHelper.showShortError(response.getMessage());
            }
        }
    };
    private String phone;
    @OnClick(R.id.login_btn_submit)
    public void loginSubmit() {
        phone = loginEdPhoneInput.getText().toString();
        String password = loginEdPwdInput.getText().toString();
        if (!Utils.isPhone(phone)) {
            ToastHelper.showShortError("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(password) && password.length() > Constant.PWD_MIN_LENGTH) {
            ToastHelper.showShortError("请输入至少6位密码");
            return;
        }
        String passwordMd5 = MD5.toMD5(password);
        showLoading(R.string.login_loading);
        CommonRequest.login(phone, password, callback);
    }

    @OnClick(R.id.login_random)
    public void setLoginRandom() {
        finish();
    }

    @OnClick(R.id.login_tv_sign_now)
    public void registerNow() {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("isRegiste", true);
        startActivity(intent);
    }

    @OnClick(R.id.login_tv_forgot_pw)
    public void fogetPassword() {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("isRegiste", false);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideInputMethod();
    }

    /**
     * 显示输入法
     */
    public void showInputMethod() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(loginEdPhoneInput, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 隐藏输入法
     */
    public void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(loginEdPhoneInput.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}

