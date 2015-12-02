package com.iwork.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.Base.BaseApplication;
import com.iwork.helper.ToastHelper;
import com.iwork.model.Demo;
import com.iwork.model.UserInfo;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
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

    private Observable<CharSequence> passwordChangeObservable;
    private Observable<CharSequence> passwordConfimObservable;
    private Subscription mSubscription = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        ButterKnife.bind(this);
        setTextChangeWatch();
    }

    private void setTextChangeWatch() {
        passwordChangeObservable = RxTextView.textChanges(passwordEdPtInput).skip(1);
        passwordConfimObservable = RxTextView.textChanges(passwordCmInput).skip(1);
        mSubscription = (Subscription) Observable.combineLatest(passwordChangeObservable, passwordConfimObservable, new Func2<CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence password, CharSequence password_confim) {
                boolean passwValid = !TextUtils.isEmpty(password) && password.length() > Constant.PWD_MIN_LENGTH;
                if (!passwValid) {
                    passwordEdPtInput.setError("请输入至少6位密码");
                }
                boolean passwcomValid = !TextUtils.isEmpty(password_confim) && password_confim.length() > Constant.PWD_MIN_LENGTH;
                if (!passwcomValid) {
                    passwordCmInput.setError("请输入至少6位密码");
                }
                boolean isSame = password.equals(password_confim);
                if (!isSame){
                    ToastHelper.showShortError("请输入相同密码");
                }
                return passwValid&&passwcomValid&&isSame;
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
                    passwordBtnSubmit.setEnabled(true);
                } else {
                    passwordBtnSubmit.setEnabled(false);
                }
            }
        });
    }
    @OnClick(R.id.password_btn_submit)
    public void onComplate(){
        String pw = MD5.toMD5(passwordCmInput.getText().toString());
        UserInfo userInfo = BaseApplication.getAppContext().mUserInfo;
        CommonRequest.register(userInfo.phone, pw, userInfo.zh_name, userInfo.email, userInfo.experience, userInfo.position, userInfo.role_code, null, null, new ResultCallback<Demo>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(Demo response) {
                if (response.getInfoCode()==0){
                    ToastHelper.showShortCompleted("注册成功");
                }
            }
        });
    }

}
