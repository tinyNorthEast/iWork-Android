package com.iwork.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.helper.ToastHelper;
import com.iwork.preferences.Preferences;
import com.iwork.ui.view.SampleFragment;
import com.iwork.ui.view.SlidingTabLayout;
import com.iwork.ui.view.TitleBar;
import com.iwork.ui.view.ViewPagerAdapter;

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
    private String titles[] = new String[]{"金融", "消费品", "房产", "医疗",
            "全部", "互联网", "工业", "教育", "汽车", "政府部门"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        titleBar.hideBackDrawable();
        titleBar.setCustomImageButtonRight(R.drawable.common_my, loginListener);
        titleBar.setCustomImageButtonLeft(R.drawable.common_icon_transfer_down, "北京", positionListener);
        titleBar.showCenterImg();
        viewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), titles));
//        slidingTabs.setDistributeEvenly(true);
//        slidingTabs.setCustomTabView(R.layout.slidingtablayout_view,R.id.sliding_tabs_tv);
        slidingTabs.setViewPager(viewpager);
//        slidingTabs.setSelectedIndicatorColors(R.color.color_bt_bg);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                OnekeyShare oks = new OnekeyShare();
//                oks.setText("我是分享文本");
//                oks.setImagePath("http://f1.sharesdk.cn/imgs/2014/05/21/oESpJ78_533x800.jpg");
//                // 启动分享GUI
//                oks.show(MainActivity.this);
//
//            }
//        });

    }

    private View.OnClickListener positionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            ToastHelper.showShortCompleted("选择城市");
            Intent intent = new Intent(MainActivity.this,CityListActivity.class);
            startActivity(intent);
        }
    };
    private View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(Preferences.getInstance().getToken())) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }else {
                ToastHelper.showShortCompleted("您已经登录");
            }
        }
    };

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
