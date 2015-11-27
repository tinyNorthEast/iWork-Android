package com.iwork.ui.activity;

import android.os.Bundle;
import android.os.PatternMatcher;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.utils.Constant;
import com.iwork.utils.TextUtil;
import com.iwork.utils.Utils;
import com.jakewharton.rxbinding.widget.RxCheckedTextView;
import com.jakewharton.rxbinding.widget.RxTextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func2;

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

    private Observable<CharSequence> phoneChangeObservable;
    private Observable<CharSequence> passwordChangeObservable;
    private Subscription mSubscription = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setTextChangeWatch();
    }


    private void setTextChangeWatch() {
        phoneChangeObservable = RxTextView.textChanges(loginEdPhoneInput).skip(1);
        passwordChangeObservable = RxTextView.textChanges(loginEdPwdInput).skip(1);
        mSubscription = Observable.combineLatest(phoneChangeObservable, passwordChangeObservable, new Func2<CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence phone, CharSequence password) {
                boolean phoneValid = Utils.isPhone(phone);
                if (!phoneValid) {
                    loginEdPhoneInput.setError("请输入正确的手机号");
                }
                boolean passwValid = !TextUtils.isEmpty(password) && password.length() > Constant.PWD_MIN_LENGTH;
                if (!passwValid) {
                    loginEdPwdInput.setError("请输入至少6位密码");
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

    @OnClick(R.id.login_btn_submit)
    public void login() {
        Toast.makeText(this, "s", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.login_random)
    public void setLoginRandom() {
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSubscription != null)
            mSubscription.unsubscribe();
    }
}

