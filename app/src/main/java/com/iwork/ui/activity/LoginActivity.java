package com.iwork.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.helper.ToastHelper;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.ui.dialog.LoadingDialog;
import com.iwork.ui.view.TitleBar;
import com.iwork.utils.Constant;
import com.iwork.utils.MD5;
import com.iwork.utils.TextUtil;
import com.iwork.utils.Utils;
import com.jakewharton.rxbinding.widget.RxCheckedTextView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.squareup.okhttp.Request;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func2;
import rx.subscriptions.CompositeSubscription;

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

    private Observable<CharSequence> phoneChangeObservable;
    private Observable<CharSequence> passwordChangeObservable;
    private Subscription mSubscription = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setTextChangeWatch();
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

    boolean phoneValid, passwValid;

    private void setTextChangeWatch() {
        phoneChangeObservable = RxTextView.textChanges(loginEdPhoneInput).skip(1);
        passwordChangeObservable = RxTextView.textChanges(loginEdPwdInput).skip(1);
        mSubscription = Observable.combineLatest(phoneChangeObservable, passwordChangeObservable, new Func2<CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence phone, CharSequence password) {
                phoneValid = Utils.isPhone(phone);
                if (!phoneValid) {
//                    loginEdPhoneInput.setError("请输入正确的手机号");
                }
                passwValid = !TextUtils.isEmpty(password) && password.length() > Constant.PWD_MIN_LENGTH;
                if (!passwValid) {
//                    loginEdPwdInput.setError("请输入至少6位密码");
                }
                return phoneValid && passwValid;
            }
        }).subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    loginBtnSubmit.setEnabled(true);
                } else {
                    loginBtnSubmit.setEnabled(false);
                }
            }
        });


    }

    private ResultCallback<String> callback = new ResultCallback<String>() {
        @Override
        public void onError(Request request, Exception e) {

        }

        @Override
        public void onResponse(String response) {
            cancelLoading();
            ToastHelper.showShortCompleted("登录成功");
            finish();
        }
    };

    @OnClick(R.id.login_btn_submit)
    public void login() {
        if (!phoneValid){
            ToastHelper.showShortError("请输入正确的手机号");
            return;
        }
        if (!phoneValid){
            ToastHelper.showShortError("请输入至少6位密码");
            return;
        }
        String phone = loginEdPhoneInput.getText().toString();
        String password = MD5.toMD5(loginEdPwdInput.getText().toString());
        showLoading(R.string.login_loading);
        CommonRequest.login(phone,password,callback);
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
        if (mSubscription != null)
            mSubscription.unsubscribe();
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

