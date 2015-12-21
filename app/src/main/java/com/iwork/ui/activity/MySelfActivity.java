package com.iwork.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.ui.view.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MySelfActivity extends BaseActivity {

    @Bind(R.id.myself_titlebar)
    TitleBar myselfTitlebar;
    @Bind(R.id.myself_iv_user)
    ImageView myselfIvUser;
    @Bind(R.id.myself_tv_name)
    TextView myselfTvName;
    @Bind(R.id.myself_tv_phone)
    TextView myselfTvPhone;
    @Bind(R.id.myself_setmyinfo)
    LinearLayout myselfSetmyinfo;
    @Bind(R.id.myself_myattention)
    LinearLayout myselfMyattention;
    @Bind(R.id.myself_messages)
    LinearLayout myselfMessages;
    @Bind(R.id.myself_introduce)
    LinearLayout myselfIntroduce;
    @Bind(R.id.myself_profession)
    LinearLayout myselfProfession;
    @Bind(R.id.myself_btn_exit)
    Button myselfBtnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_self);
        ButterKnife.bind(this);
    }
}
