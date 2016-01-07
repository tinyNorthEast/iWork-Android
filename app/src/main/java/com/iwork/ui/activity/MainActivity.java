package com.iwork.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.model.IndustryListModel;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.preferences.Preferences;
import com.iwork.ui.activity.myself.MySelfActivity;
import com.iwork.ui.fragment.SampleFragment;
import com.iwork.ui.view.SlidingTabLayout;
import com.iwork.ui.view.TitleBar;
import com.iwork.ui.view.ViewPagerAdapter;
import com.iwork.ui.view.scroll.FixedSpeedScroller;
import com.iwork.utils.Constant;
import com.squareup.okhttp.Request;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.lang.reflect.Field;
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
    private long currentBackTime;
    private long lastBackTime;

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
//        setViewPagerScrollSpeed();
        slidingTabs.setViewPager(viewpager);
    }

    /**
     * 设置ViewPager的滑动速度
     */
    private void setViewPagerScrollSpeed() {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewpager.getContext());
            mScroller.set(viewpager, scroller);
        } catch (NoSuchFieldException e) {

        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {

        }
    }

    @Subscriber(tag = Constant.CITY)
    public void setCity(String city) {
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
                    for (Fragment s : sampleFragments) {
                        if (s != null && s instanceof SampleFragment) {
                            s.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//获取当前系统时间的毫秒数
            currentBackTime = System.currentTimeMillis();
            //比较上次按下返回键和当前按下返回键的时间差，如果大于2秒，则提示再按一次退出
            if (currentBackTime - lastBackTime > 2 * 1000) {
                Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
                lastBackTime = currentBackTime;
            } else { //如果两次按下的时间差小于2秒，则退出程序
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
