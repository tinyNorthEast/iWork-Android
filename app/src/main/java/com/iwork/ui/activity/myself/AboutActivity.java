package com.iwork.ui.activity.myself;

import android.os.Bundle;

import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.ui.view.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutActivity extends BaseActivity {

    @Bind(R.id.about_titlebar)
    TitleBar aboutTitlebar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        aboutTitlebar.setBackDrawableListener(backListener);
    }
}
