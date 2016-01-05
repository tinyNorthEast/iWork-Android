package com.iwork.ui.activity.myself;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.impetusconsulting.iwork.R;
import com.iwork.Base.ActivityManager;
import com.iwork.Base.BaseActivity;
import com.iwork.Base.BaseApplication;
import com.iwork.helper.ToastHelper;
import com.iwork.model.InvateCodeMode;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.preferences.Preferences;
import com.iwork.ui.activity.MainActivity;
import com.iwork.ui.view.TitleBar;
import com.iwork.utils.DataClearManager;
import com.iwork.utils.UiThreadHandler;
import com.socks.library.KLog;
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
        imgSize.setText(DataClearManager.getApplicationDataSize(BaseApplication.getAppContext()));
    }

    /**
     * 清除缓存
     */
    @OnClick(R.id.clear_img_cache)
    public void clearImgCache() {
        DataClearManager.cleanApplicationData(BaseApplication.getAppContext());
        Glide.get(this).clearMemory();
        ToastHelper.showShortCompleted("已经清除缓存");
//        imgSize.setText(DataClearManager.getApplicationDataSize(BaseApplication.getAppContext()));
        imgSize.setVisibility(View.GONE);

    }

    @OnClick(R.id.set_about)
    public void setAbout() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    /**
     * 生成邀请码
     */
    @OnClick(R.id.set_recommon)
    public void setInvate() {
        Intent intent = new Intent(this, InvateCodeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.myself_btn_exit)
    public void setExitAccount() {
        Preferences.getInstance().clear();
        ActivityManager.getInstance().removeActivites(MainActivity.class.getName());
    }

    public void setPushSwitch() {
        setPushSwitch.setChecked(!JPushInterface.isPushStopped(BaseApplication.getAppContext()));
        setPushSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ToastHelper.showShortCompleted("开启推送");
                    JPushInterface.resumePush(BaseApplication.getAppContext());
                } else {
                    ToastHelper.showShortCompleted("推送关闭");
                    UiThreadHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            JPushInterface.stopPush(BaseApplication.getAppContext());
                        }
                    },2000);
                }
            }
        });
    }
}
