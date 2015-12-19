package com.iwork.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.model.PersonDetail;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.ui.view.TitleBar;
import com.socks.library.KLog;
import com.squareup.okhttp.Request;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class PersonDetailActivty extends BaseActivity {

    @Bind(R.id.detail_titlebar)
    TitleBar detailTitlebar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail_activty);
        ButterKnife.bind(this);
        initTitleBar();
        getData();
    }

    private void getData() {
        CommonRequest.getDetail(16, new ResultCallback<PersonDetail>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(PersonDetail response) {
            }
        });
    }

    private void initTitleBar() {
        detailTitlebar.setTitle("顾问详情");
        detailTitlebar.setCustomImageButtonStore(storeListener);
        detailTitlebar.setShareDrawableListener(shareListener);
        detailTitlebar.setBackDrawableListener(backListener);
    }

    /**
     * 标题栏返回按钮点击监听
     */
    private View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    /**
     * 分享
     */
    private View.OnClickListener shareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            OnekeyShare oks = new OnekeyShare();
            oks.setText("我是分享文本");
            oks.setImagePath("http://f1.sharesdk.cn/imgs/2014/05/21/oESpJ78_533x800.jpg");
            // 启动分享GUI
            oks.show(PersonDetailActivty.this);
        }
    };
    /**
     * 收藏
     */
    private View.OnClickListener storeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
