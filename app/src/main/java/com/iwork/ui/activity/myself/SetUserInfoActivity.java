package com.iwork.ui.activity.myself;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.helper.ResourcesHelper;
import com.iwork.helper.ToastHelper;
import com.iwork.model.CommonModel;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.ui.view.ListPickerWindow;
import com.iwork.ui.view.TitleBar;
import com.socks.library.KLog;
import com.squareup.okhttp.Request;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetUserInfoActivity extends BaseActivity {

    @Bind(R.id.setuser_titlebar)
    TitleBar setuserTitlebar;
    @Bind(R.id.myself_setmypassword)
    RelativeLayout myselfSetmypassword;
    @Bind(R.id.myself_setmyexpersence)
    RelativeLayout myselfSetmyexpersence;
    @Bind(R.id.myself_setmyemail_ed)
    EditText myselfSetmyemailEd;
    @Bind(R.id.myself_setmyename_ed)
    EditText myselfSetmyenameEd;
    @Bind(R.id.myself_setmycompany_ed)
    EditText myselfSetmycompanyEd;
    @Bind(R.id.myself_setmyexpersence_tv)
    TextView myselfSetmyexpersenceTv;
    @Bind(R.id.set_head_icon_iv)
    ImageView setHeadIconIv;
    @Bind(R.id.set_head_icon_rl)
    RelativeLayout setHeadIcon_layout;
    private ListPickerWindow<String> mExplistPickerWindow;
    private int experience;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_user_info);
        ButterKnife.bind(this);
        setuserTitlebar.setTitle("修改个人信息");
        setuserTitlebar.setBackDrawableListener(backListener);
        setuserTitlebar.setRightTextView("保存", R.color.white, saveListener);
    }

    private View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String email = myselfSetmyemailEd.getText().toString().trim();
            String en_name = myselfSetmyenameEd.getText().toString().trim();
            String company = myselfSetmycompanyEd.getText().toString().trim();
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                ToastHelper.showShortError("请正确填写您的邮箱");
                return;
            }
            CommonRequest.setUserInfo(en_name, email, company, experience, new ResultCallback<CommonModel>() {
                @Override
                public void onError(Request request, Exception e) {

                }

                @Override
                public void onResponse(CommonModel response) {
                    if (response.getInfoCode() == 0) {
                        ToastHelper.showShortCompleted("修改信息成功");
                        finish();
                    } else {
                        ToastHelper.showShortError(response.getMessage());
                    }
                }
            });

        }
    };

    @OnClick(R.id.myself_setmypassword)
    public void setPassword() {
        Intent intent = new Intent(this, SetMyselfPassword.class);
        startActivity(intent);
    }

    @OnClick(R.id.myself_setmyexpersence)
    public void setMyselfSetmyexpersence() {
        mExplistPickerWindow = new ListPickerWindow<String>(this, getCurrentFocus(), ResourcesHelper.getStringArray(R.array.experience));
        mExplistPickerWindow.setListPickerListener(new ListPickerWindow.ListPickerListener<String>() {
            @Override
            public void onItemSelected(int position, String object) {
                myselfSetmyexpersenceTv.setText(object);
                experience = position;
                KLog.i("---exp", position);
            }
        });
        mExplistPickerWindow.showDialog();
    }
    @OnClick(R.id.set_head_icon_rl)
    public void setHeadIcon(){

    }

}
