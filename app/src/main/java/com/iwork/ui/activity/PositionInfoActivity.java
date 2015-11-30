package com.iwork.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.helper.ResourcesHelper;
import com.iwork.ui.view.ListPickerWindow;
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
    View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = LayoutInflater.from(this).inflate(R.layout.activity_position_info, null);
        setContentView(mRootView);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.position_btn_submit)
    public void onComplete() {

    }

    private ListPickerWindow<String> mExplistPickerWindow;

    @OnClick(R.id.position_layout_exp_input)
    public void setExperence() {
        mExplistPickerWindow = new ListPickerWindow<String>(this, mRootView, ResourcesHelper.getStringArray(R.array.experience));
        mExplistPickerWindow.setListPickerListener(new ListPickerWindow.ListPickerListener<String>() {
            @Override
            public void onItemSelected(int position, String object) {
                positionExpInput.setText(object);
                KLog.i("---exp",position);
            }
        });
    }
    private ListPickerWindow<String> mPtListPicker;
    @OnClick(R.id.position_pt_rl)
    public void setPosition(){
        mPtListPicker = new ListPickerWindow<String>(this,mRootView,ResourcesHelper.getStringArray(R.array.position));
        mPtListPicker.setListPickerListener(new ListPickerWindow.ListPickerListener<String>() {
            @Override
            public void onItemSelected(int position, String object) {
                positionEdPtInput.setText(object);
                KLog.i("---pt",position);
            }
        });
    }
}
