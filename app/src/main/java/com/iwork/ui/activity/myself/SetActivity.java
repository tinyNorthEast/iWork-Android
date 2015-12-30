package com.iwork.ui.activity.myself;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.Base.BaseApplication;
import com.iwork.helper.ToastHelper;
import com.iwork.model.InvateCodeMode;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.preferences.Preferences;
import com.iwork.ui.view.TitleBar;
import com.squareup.okhttp.Request;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class SetActivity extends BaseActivity {

    @Bind(R.id.set_titlebar)
    TitleBar setTitlebar;
    @Bind(R.id.clear_img_cache)
    LinearLayout clearImgCache;
    @Bind(R.id.set_about)
    LinearLayout exitAccount;
    @Bind(R.id.img_size)
    TextView imgSize;
    @Bind(R.id.set_recommon)
    LinearLayout setRecommon;
    @Bind(R.id.set_push_switch)
    Switch setPushSwitch;
    @Bind(R.id.myself_btn_exit)
    Button myselfBtnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        ButterKnife.bind(this);
        setTitlebar.setTitle("设置");
        setTitlebar.setBackDrawableListener(backListener);
        setPushSwitch();
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
    public void setAbout() {
    }

    /**
     * 生成邀请码
     */
    @OnClick(R.id.set_recommon)
    public void setInvate(){

    }
    @OnClick(R.id.myself_btn_exit)
    public void setExitAccount(){
        Preferences.getInstance().clear();
        finish();
    }
    public void setPushSwitch(){
        setPushSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ToastHelper.showShortCompleted("开启推送");
                    JPushInterface.resumePush(BaseApplication.getAppContext());
                }else {
                    ToastHelper.showShortCompleted("推送关闭");
                    JPushInterface.stopPush(BaseApplication.getAppContext());
                }
            }
        });
    }
}
