package com.iwork.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.ui.view.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SendMessageActivity extends BaseActivity {

    @Bind(R.id.sendmes_titlebar)
    TitleBar sendmesTitlebar;
    @Bind(R.id.sendmes_ed)
    EditText sendmesEd;
    @Bind(R.id.sendmes_btn_submit)
    Button sendmesBtnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        ButterKnife.bind(this);
        sendmesTitlebar.setTitle("给顾问留言");
        sendmesTitlebar.setBackDrawableListener(backListener);
    }
}
