package com.iwork.ui.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.ui.dialog.DialogHelper;
import com.iwork.ui.view.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetUserInfoActivity extends BaseActivity {

    @Bind(R.id.setuser_titlebar)
    TitleBar setuserTitlebar;
    @Bind(R.id.myself_setmypassword)
    LinearLayout myselfSetmypassword;
    @Bind(R.id.myself_setmyemail)
    LinearLayout myselfSetmyemail;
    @Bind(R.id.myself_setmyename)
    LinearLayout myselfSetmyename;
    @Bind(R.id.myself_setmyexpersence)
    LinearLayout myselfSetmyexpersence;
    @Bind(R.id.myself_setmycompany)
    LinearLayout myselfSetmycompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_user_info);
        ButterKnife.bind(this);
        setuserTitlebar.setTitle("设置个人信息");
        setuserTitlebar.setBackDrawableListener(backListener);
    }

    @OnClick(R.id.myself_setmypassword)
    public void setPassword() {

    }

    @OnClick(R.id.myself_setmyemail)
    public void setMyselfSetmyemail() {

    }

    @OnClick(R.id.myself_setmyename)
    public void setMyselfSetmyename() {

    }

    @OnClick(R.id.myself_setmyexpersence)
    public void setMyselfSetmyexpersence() {

    }

    @OnClick(R.id.myself_setmycompany)
    public void setMyselfSetmycompany() {

    }
}
