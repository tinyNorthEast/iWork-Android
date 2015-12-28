package com.iwork.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.helper.ToastHelper;
import com.iwork.preferences.Preferences;
import com.iwork.ui.view.TitleBar;
import com.iwork.utils.Utils;
import com.socks.library.KLog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetActivity extends BaseActivity {

    @Bind(R.id.set_titlebar)
    TitleBar setTitlebar;
    @Bind(R.id.clear_img_cache)
    LinearLayout clearImgCache;
    @Bind(R.id.set_about)
    LinearLayout exitAccount;
    @Bind(R.id.img_size)
    TextView imgSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        ButterKnife.bind(this);
        setTitlebar.setTitle("设置");
        setTitlebar.setBackDrawableListener(backListener);
    }

    /**
     * 清除缓存
     */
    @OnClick(R.id.clear_img_cache)
    public void clearImgCache() {
        Glide.get(this).clearMemory();
        ToastHelper.showShortCompleted("已经清除缓存");
    }
    @OnClick(R.id.set_about)
    public void setAbout(){
//        Preferences.getInstance().clear();
//        finish();
    }
}
