package com.iwork.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.model.MySelfModel;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.ui.view.TitleBar;
import com.squareup.okhttp.Request;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MySelfActivity extends BaseActivity {

    @Bind(R.id.myself_titlebar)
    TitleBar myselfTitlebar;
    @Bind(R.id.myself_iv_user)
    ImageView myselfIvUser;
    @Bind(R.id.myself_tv_name)
    TextView myselfTvName;
    @Bind(R.id.myself_tv_phone)
    TextView myselfTvPhone;
    @Bind(R.id.myself_setuserinfo)
    RelativeLayout myselfSetuserinfo;
    @Bind(R.id.myself_myattention)
    RelativeLayout myselfMyattention;
    @Bind(R.id.myself_messages)
    RelativeLayout myselfMessages;
    @Bind(R.id.myself_attention_me)
    RelativeLayout myselfAttentionMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_self);
        ButterKnife.bind(this);
        myselfTitlebar.setTitle("个人中心");
        myselfTitlebar.setCustomImageButtonRight(R.drawable.title_bar_set, setListener);
        getMySelfData();
    }

    private View.OnClickListener setListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MySelfActivity.this, SetActivity.class);
            startActivity(intent);
        }
    };

    public void getMySelfData() {
        CommonRequest.getMySelfData(new ResultCallback<MySelfModel>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(MySelfModel response) {
                if (response.getInfoCode() == 0) {
                    myselfTvName.setText(response.getData().getZh_name());
                }
            }
        });
    }

    /**
     * 进入设置我的个人信息页面
     */
    @OnClick(R.id.myself_setuserinfo)
    public void goTosetUserInfo() {
        Intent intent = new Intent(this, SetUserInfoActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.myself_myattention)
    public void goTomyAttantion() {

    }

    /**
     * 进入谁关注了我的页面
     */
    @OnClick(R.id.myself_attention_me)
    public void goTomyAttantionMe() {

    }

    /**
     * 进入我的消息页面
     */
    @OnClick(R.id.myself_messages)
    public void goTomyMessage() {

    }
}
