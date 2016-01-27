package com.iwork.ui.activity.regist;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.Base.BaseApplication;
import com.iwork.helper.ToastHelper;
import com.iwork.model.RequestMessage;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.ui.view.TitleBar;
import com.iwork.utils.Constant;
import com.iwork.utils.NetConstant;
import com.iwork.utils.TextUtil;
import com.iwork.utils.Utils;
import com.socks.library.KLog;
import com.squareup.okhttp.Request;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterActivity extends BaseActivity {

    @Bind(R.id.registe_ed_phone_input)
    EditText registeEdPhoneInput;
    @Bind(R.id.registe_img_phone_del)
    ImageView registeImgPhoneDel;
    @Bind(R.id.registe_tv_get_code)
    TextView registeTvGetCode;
    @Bind(R.id.registe_ed_code_input)
    EditText registeEdCodeInput;
    @Bind(R.id.registe_btn_submit)
    Button registeBtnSubmit;
    @Bind(R.id.register_title_bar)
    TitleBar titleBar;

    private Subscription subscription = null;
    private Observable<CharSequence> phoneChangeObservable;
    private Observable<CharSequence> codeChangeObservable;
    private boolean isRegiste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setTextChangeWatch();
        SMSSDK.initSDK(this, Constant.SMSSDKKEY, Constant.SMSSDKSECRET);
        SMSSDK.registerEventHandler(eventHandler);
        SMSSDK.getSupportedCountries();
        isRegiste = getIntent().getBooleanExtra("isRegiste", false);
        if (isRegiste) {
            titleBar.setTitle("注册");
        } else {
            titleBar.setTitle("忘记密码");
        }
        titleBar.setBackDrawableListener(backListener);
        showInputMethod();
    }

    private void setTextChangeWatch() {
        registeEdPhoneInput.addTextChangedListener(phoneWatcher);
        registeEdCodeInput.addTextChangedListener(codeWatcher);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideInputMethod();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    /**
     * 电话号码输入框监听
     */
    private TextWatcher phoneWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String code = registeEdCodeInput.getText().toString();
            String p = registeEdPhoneInput.getText().toString();
            if (TextUtils.isEmpty(p)) {
            } else {
            }
            if (!Utils.isPhone(p)) {
                setGetCodeShow(false);
                if (!TextUtils.isEmpty(p))
                    ToastHelper.showShortError(R.string.phone_errns);
                return;
            }
            if (p.length() == 11) {
                setGetCodeShow(true);
            } else {
                setGetCodeShow(false);
                return;
            }

            if (!Utils.isNum(code))
                return;
        }
    };

    /**
     * 验证码输入监听
     */
    private TextWatcher codeWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String code = registeEdCodeInput.getText().toString();
            String p = registeEdPhoneInput.getText().toString();
            if (code.length() < 4) {
                return;
            }
            if (!Utils.isNum(p))
                return;

        }
    };


    private void setGetCodeShow(boolean isenble) {
        if (isenble)
            registeTvGetCode.setTextColor(getResources().getColor(R.color.text_color_bg));
        else
            registeTvGetCode.setTextColor(getResources().getColor(R.color.text_color_s_gray));
        registeTvGetCode.setEnabled(isenble);
    }

    private String phone;
    private EventHandler eventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            super.afterEvent(event, result, data);
            Message msg = new Message();
            msg.arg1 = event;
            msg.arg2 = result;
            msg.obj = data;
            handle.sendMessage(msg);
        }
    };
    private Handler handle = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            cancelLoading();
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    if (isRegiste) {
                        CommonRequest.checkphonestatus(phone, phoneStatusCallback);
                    } else {
                        jumpNext();
                    }
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    ToastHelper.showShortCompleted(R.string.get_code_succ);
                    setTimeCount();
                }
            } else {
                // 根据服务器返回的网络错误，给toast提示
                try {
                    ((Throwable) data).printStackTrace();
                    Throwable throwable = (Throwable) data;

                    KLog.e("--", ((Throwable) data).getMessage());

                    JSONObject object = new JSONObject(
                            throwable.getMessage());
                    String des = object.optString("detail");
                    if (!TextUtils.isEmpty(des)) {
                        ToastHelper.showShortError(des);
                    } else {
                        ToastHelper.showShortError("获取验证码失败");
                    }
                } catch (Exception e) {

                }
                // 如果木有找到资源，默认提示
                ToastHelper.showShortError("验证码错误");
            }
            return false;
        }
    });

    public void jumpNext() {
        BaseApplication.getmUserInfo().phone = phone;
        if (isRegiste) {
            Intent intent = new Intent(this, SignUserInfoActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, PasswordActivity.class);
            intent.putExtra(Constant.PASSWORD, false);
            startActivity(intent);
        }
    }

    @OnClick(R.id.registe_tv_get_code)
    public void getCodeOnClick() {

        getCode();
    }

    private void getCode() {
        phone = registeEdPhoneInput.getText().toString().trim();
        if (Utils.isPhone(phone)) {
            if (isRegiste) {
                CommonRequest.checkphonestatus(phone, phoneStatusCallback);
            } else {
                showLoading(R.string.login_getCoding);
                SMSSDK.getVerificationCode("86", phone);
            }
        } else {
            ToastHelper.showShortError(R.string.phone_errns);
        }
    }

    public static final int REQUEST_READ_PHONE_STATE = 123;

//    private void requestPermission() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            int checkREAD_PHONE_STATE = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
//            if (checkREAD_PHONE_STATE != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,
//                        Manifest.permission.RECEIVE_SMS}, REQUEST_READ_PHONE_STATE);
//                return;
//            } else {
//                getCode();
//            }
//        } else {
//            getCode();
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCode();
                } else {
                    ToastHelper.showShortError("获取权限失败，请允许权限申请");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @OnClick(R.id.registe_btn_submit)
    public void next() {
        showLoading(R.string.loading);
        phone = registeEdPhoneInput.getText().toString().trim();
        if (!Utils.isPhone(phone)) {
            ToastHelper.showShortError(R.string.phone_errns);
            return;
        } else {
            String code = registeEdCodeInput.getText().toString().trim();
            if (!TextUtil.isEmpty(code)) {
//                SMSSDK.submitVerificationCode("86", phone, code);
                cancelLoading();
                jumpNext();
            } else {
                ToastHelper.showShortError("请填写正确的验证码");
            }
        }

    }

    private ResultCallback<RequestMessage> phoneStatusCallback = new ResultCallback<RequestMessage>() {
        @Override
        public void onError(Request request, Exception e) {

        }

        @Override
        public void onResponse(RequestMessage response) {
            KLog.i("---phonestatus", response.toString());
            if (response.getInfoCode() == NetConstant.PARAM_OK) {
                showLoading(R.string.login_getCoding);
                SMSSDK.getVerificationCode("86", phone);

            } else if (response.getInfoCode() == NetConstant.PARAM_ALREADY_PHONE) {
                ToastHelper.showShortError(response.getMessage());
            } else {
                ToastHelper.showShortError(response.getMessage());
            }

        }

    };

    private void setTimeCount() {
        Observable.interval(1, TimeUnit.SECONDS).take(60)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                        setGetCodeShow(true);
                        registeTvGetCode.setText("重新发送");
                    }

                    @Override
                    public void onError(Throwable e) {
                        setGetCodeShow(true);
                        registeTvGetCode.setText("重新发送");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        registeTvGetCode.setEnabled(false);
                        setGetCodeShow(false);
                        registeTvGetCode.setText((60 - aLong) + "s");
                    }
                });
    }

    /**
     * 显示输入法
     */
    public void showInputMethod() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(registeEdPhoneInput, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 隐藏输入法
     */
    public void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(registeEdPhoneInput.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
