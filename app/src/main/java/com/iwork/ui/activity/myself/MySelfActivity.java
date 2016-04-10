package com.iwork.ui.activity.myself;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.helper.ToastHelper;
import com.iwork.model.MySelfModel;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.preferences.Preferences;
import com.iwork.ui.activity.common.AttentionActivity;
import com.iwork.ui.view.BadgeView;
import com.iwork.ui.view.CircleTransform;
import com.iwork.ui.view.TitleBar;
import com.iwork.utils.Constant;
import com.iwork.utils.LoginUtil;
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
    @Bind(R.id.myself_tv_roleName)
    TextView myselfTvRoleName;
    @Bind(R.id.myself_setuserinfo)
    RelativeLayout myselfSetuserinfo;
    @Bind(R.id.myself_myattention)
    RelativeLayout myselfMyattention;
    @Bind(R.id.myself_messages)
    RelativeLayout myselfMessages;
    @Bind(R.id.myself_attention_me)
    RelativeLayout myselfAttentionMe;
    @Bind(R.id.myself_messages_tv)
    TextView myselfMessagesTv;
    private BadgeView badgeView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_self);
        ButterKnife.bind(this);
        myselfTitlebar.setTitle("个人中心");
        myselfTitlebar.setBackDrawableListener(backListener);
        myselfTitlebar.setCustomImageButtonRight(R.drawable.title_bar_set, setListener);
        int role_code = Preferences.getInstance().getrole_code();
        if (role_code==Constant.HEADHUNTERID){
            myselfAttentionMe.setVisibility(View.VISIBLE);
        }else {
            myselfAttentionMe.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                    myselfTvRoleName.setText(response.getData().getRoleName());
                    Glide.with(MySelfActivity.this).load(response.getData().getPic()).transform(new CircleTransform(MySelfActivity.this)).error(R.drawable.myself_head).placeholder(R.drawable.myself_head).into(myselfIvUser);
                    Preferences.getInstance().setUserHeadUrl(response.getData().getPic());
                    Preferences.getInstance().setmail(response.getData().getMail());
                    Preferences.getInstance().setEnName(response.getData().getEn_name());
                    Preferences.getInstance().setExperience(response.getData().getExperience());
                    Preferences.getInstance().setMyselfSignature(response.getData().getSignature());
                    showBadgeView(myselfMessagesTv, response.getData().getNoticeCount() + "");
                } else if (response.getInfoCode()==Constant.TOKENFAIL){
                    LoginUtil.goToLogin(MySelfActivity.this);
                    ToastHelper.showShortError(response.getMessage());
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

    /**
     * 进入我的关注页面
     */
    @OnClick(R.id.myself_myattention)
    public void goTomyAttantion() {
        Intent intent = new Intent(this, AttentionActivity.class);
        intent.putExtra(Constant.SEARCHTYPE, 1);
        startActivity(intent);
    }

    /**
     * 进入谁关注了我的页面
     */
    @OnClick(R.id.myself_attention_me)
    public void goTomyAttantionMe() {
        Intent intent = new Intent(this, AttentionActivity.class);
        intent.putExtra(Constant.SEARCHTYPE, 2);
        startActivity(intent);
    }

    /**
     * 进入我的消息页面
     */
    @OnClick(R.id.myself_messages)
    public void goTomyMessage() {

        Intent intent = new Intent(this, MessageCountActivity.class);
        startActivity(intent);
    }

    private void showBadgeView(View v, String text) {
        badgeView = new BadgeView(this, v);
        badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badgeView.setBadgeMargin(5, 5);
        badgeView.setBadgeBackgroundColor(getResources().getColor(R.color.color_bt_bg));
        badgeView.setTextSize(8);
        badgeView.setText(text);
        badgeView.show();
    }
}
