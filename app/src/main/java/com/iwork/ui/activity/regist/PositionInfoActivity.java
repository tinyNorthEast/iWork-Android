package com.iwork.ui.activity.regist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.Base.BaseApplication;
import com.iwork.helper.ResourcesHelper;
import com.iwork.helper.ToastHelper;
import com.iwork.ui.view.ListPickerWindow;
import com.iwork.ui.view.TitleBar;
import com.iwork.utils.Constant;
import com.socks.library.KLog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PositionInfoActivity extends BaseActivity {

    @Bind(R.id.position_ed_pt_input)
    TextView positionEdPtInput;
    @Bind(R.id.position_cm_input)
    EditText positionCmInput;
    @Bind(R.id.position_layout_exp_input)
    RelativeLayout positionExlayout;
    @Bind(R.id.position_btn_submit)
    Button positionBtnSubmit;
    @Bind(R.id.position_exp_input)
    TextView positionExpInput;
    @Bind(R.id.position_pt_rl)
    RelativeLayout positionPtlayout;
    @Bind(R.id.position_titlebar)
    TitleBar titleBar;

    View mRootView;
    @Bind(R.id.registe_ed_user_invate)
    EditText registeEdUserInvate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = LayoutInflater.from(this).inflate(R.layout.activity_position_info, null);
        setContentView(mRootView);
        ButterKnife.bind(this);
        titleBar.setTitle("职位信息");
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

    @OnClick(R.id.position_btn_submit)
    public void onComplete() {
        if (TextUtils.isEmpty(positionExpInput.getText().toString().trim()) ||
                TextUtils.isEmpty(positionEdPtInput.getText().toString().trim()) ||
                TextUtils.isEmpty(positionCmInput.getText().toString().trim())) {
            ToastHelper.showShortError("请填写完整信息");
            return;
        }
        if (!TextUtils.isEmpty(registeEdUserInvate.getText().toString())) {
            BaseApplication.getAppContext().getmUserInfo().invate_code = registeEdUserInvate.getText().toString().trim();
        }
        BaseApplication.getAppContext().getmUserInfo().position = positionCmInput.getText().toString();
        Intent intent = new Intent(this, PasswordActivity.class);
        intent.putExtra(Constant.PASSWORD, true);
        startActivity(intent);
    }

    private ListPickerWindow<String> mExplistPickerWindow;


    @OnClick(R.id.position_layout_exp_input)
    public void setExperence() {
        hideInputMethod();
        mExplistPickerWindow = new ListPickerWindow<String>(this, mRootView, ResourcesHelper.getStringArray(R.array.experience));
        mExplistPickerWindow.setListPickerListener(new ListPickerWindow.ListPickerListener<String>() {
            @Override
            public void onItemSelected(int position, String object) {
                positionExpInput.setText(object);
                BaseApplication.getAppContext().getmUserInfo().experience = position;
                KLog.i("---exp", position);
            }
        });
        mExplistPickerWindow.showDialog();
    }

    private ListPickerWindow<String> mPtListPicker;

    @OnClick(R.id.position_pt_rl)
    public void setPosition() {
        hideInputMethod();
        mPtListPicker = new ListPickerWindow<String>(this, mRootView, ResourcesHelper.getStringArray(R.array.position));
        mPtListPicker.setListPickerListener(new ListPickerWindow.ListPickerListener<String>() {
            @Override
            public void onItemSelected(int position, String object) {
                positionEdPtInput.setText(object);
                BaseApplication.getAppContext().getmUserInfo().role_code = position + 100;
                KLog.i("---pt", position);
            }
        });
        mPtListPicker.showDialog();
    }

    /**
     * 显示输入法
     */
    public void showInputMethod() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(positionCmInput, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 隐藏输入法
     */
    public void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(positionCmInput.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
