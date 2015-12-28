package com.iwork.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;

import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.helper.ToastHelper;
import com.iwork.model.IndustryListModel;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.preferences.Preferences;
import com.iwork.ui.fragment.SampleFragment;
import com.iwork.ui.view.SlidingTabLayout;
import com.iwork.ui.view.TitleBar;
import com.iwork.ui.view.ViewPagerAdapter;
import com.iwork.utils.Constant;
import com.squareup.okhttp.Request;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BaseActivity implements SampleFragment.OnFragmentInteractionListener {
    @Bind(R.id.main_title_bar)
    TitleBar titleBar;
    @Bind(R.id.sliding_tabs)
    SlidingTabLayout slidingTabs;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    private String titles[] = new String[]{"金融", "消费品", "房产"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        titleBar.hideBackDrawable();
        titleBar.setMeDrawableListener(loginListener);
        titleBar.setCustomImageButtonLeft(R.drawable.common_icon_transfer_down, "北京", positionListener);
        titleBar.showCenterImg();
        getIndustryData();
        EventBus.getDefault().register(this);
    }

    private void getIndustryData() {
        CommonRequest.getIndustryList(new ResultCallback<IndustryListModel>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(IndustryListModel response) {
                if (response.getInfoCode() == 0) {
                    initTabLayout(response.getData());
                }
            }
        });
    }

    private void initTabLayout(List<IndustryListModel.Industry> list) {

        viewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), list));
//        slidingTabs.setDistributeEvenly(true);
//        slidingTabs.setCustomTabView(R.layout.slidingtablayout_view,R.id.sliding_tabs_tv);
        slidingTabs.setViewPager(viewpager);
//        slidingTabs.setSelectedIndicatorColors(R.color.color_bt_bg);
    }
    @Subscriber(tag = Constant.CITY)
    public void setCity(String city){
        titleBar.setCustomImageButtonLeft(city);
    }
    private View.OnClickListener positionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, CityListActivity.class);
            startActivityForResult(intent, Constant.REQUEST_CODE_FOR_CITY);
        }
    };
    private View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(Preferences.getInstance().getToken())) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            } else {
                Intent i = new Intent(MainActivity.this, MySelfActivity.class);
                startActivity(i);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.REQUEST_CODE_FOR_CITY:
                    List<Fragment> sampleFragments = getSupportFragmentManager().getFragments();
                    for (Fragment s:sampleFragments){
                        if (s!=null&&s instanceof SampleFragment){
                            s.onActivityResult(requestCode,resultCode,data);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
