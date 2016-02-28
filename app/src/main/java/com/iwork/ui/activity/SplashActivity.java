package com.iwork.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.preferences.Preferences;
import com.iwork.utils.UiThreadHandler;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    // 动态添加图片，只需将图片保存在数组中即可
    private int[] guides = {R.drawable.splash_guide_one, R.drawable.splash_guide_two, R.drawable.splash_guide_three, R.drawable.splash_guide_four};
    private static final int TO_END = 0;// 设置标记 当到达最后一张时
    private static final int LEAVE_FROAM_END = 1;// 当离开最后一张时
    private List<View> guider_views = new ArrayList<View>();
    private ViewPager viewPager;
    private Button iv_start;
    private LinearLayout splash_bg;
    private FrameLayout splash_dot_layout;
    private ImageView iv_currentDot;// 当前的坐标点
    private LinearLayout dotLayout;// 储存点的容器
    private int offset;// 记录坐标点的位移量
    private int currentPos = 0;// 记录当前的位置
    private GuidePaperAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        initView();
        initSpalsh();
    }

    private void initSpalsh() {
//        if (Preferences.getInstance().getSplashHasShow()){
        splash_dot_layout.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
        splash_bg.setVisibility(View.VISIBLE);
        if (!Preferences.getInstance().getSplashButtonShow()) {
            iv_start.setVisibility(View.VISIBLE);
        } else {
            iv_start.setVisibility(View.GONE);
            UiThreadHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }, 2000);
        }
//        }
    }

    private void initView() {
        // TODO Auto-generated method stub
        splash_bg = (LinearLayout) findViewById(R.id.splash_bg);
        dotLayout = (LinearLayout) findViewById(R.id.dot_contain);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        iv_currentDot = (ImageView) findViewById(R.id.cur_dot);
        iv_start = (Button) findViewById(R.id.splash_bg_bt);
        splash_dot_layout = (FrameLayout) findViewById(R.id.splash_dot_layout);
        iv_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                Preferences.getInstance().setSplashButtonShow();
                finish();
            }
        });

        initDot();
        initGuide();
        // 当iv_currentDot的所在的树形层次将要被绘出时 此方法被调用
        iv_currentDot.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        // TODO Auto-generated method stub
                        // 获取点图片的宽度，点的移动动画时用
                        offset = iv_currentDot.getWidth();
                        return true;
                    }
                });
        adapter = new GuidePaperAdapter(guider_views);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);
    }

    private void initGuide() {
        // TODO Auto-generated method stub
        guider_views.clear();
        ImageView view = null;
        for (int i = 0; i < guides.length; i++) {
            view = buildImageView(guides[i]);
            guider_views.add(view);
        }
    }

    /**
     * @param id
     * @return 初始化guide的 view
     */
    private ImageView buildImageView(int id) {
        ImageView iv = new ImageView(this);
        iv.setImageResource(id);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        iv.setLayoutParams(params);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        return iv;
    }

    /**
     * * 初始化点 ImageVIew
     *
     * @return 返回true说明初始化点成功，否则实例化失败
     */
    private boolean initDot() {
        if (guides.length > 0) {
            ImageView dotView;
            for (int i = 0; i < guides.length; i++) {
                dotView = new ImageView(this);
                dotView.setImageResource(R.drawable.dot1_w);
                dotView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                dotLayout.addView(dotView);
            }
            return true;
        } else {
            return false;
        }
    }

    class GuidePaperAdapter extends PagerAdapter {
        private List<View> views;

        public GuidePaperAdapter(List<View> views) {
            super();
            this.views = views;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return views.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            View view = views.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            // super.destroyItem(container, position, object);
            container.removeView(views.get(position));
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

    }

    /**
     * 用handler通知iv_start 显示
     */
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == TO_END) {
                iv_start.setVisibility(View.VISIBLE);
            } else if (msg.what == LEAVE_FROAM_END) {
                iv_start.setVisibility(View.GONE);
            }
        }

        ;
    };

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int position) {
        // 在此设置dot的移动动画
        moveCursorTo(position);
        // 当到最后一张时
        if (position == guides.length - 1) {
            handler.sendEmptyMessage(TO_END);
        } else {
            handler.sendEmptyMessage(LEAVE_FROAM_END);
        }
        currentPos = position;
    }

    /**
     * 滑动时 坐标点点移动动画
     *
     * @param position
     */
    private void moveCursorTo(int position) {
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation(offset
                * currentPos, offset * position, 0, 0);
        animationSet.addAnimation(translateAnimation);
        animationSet.setDuration(300);
        animationSet.setFillAfter(true);
        iv_currentDot.startAnimation(animationSet);
    }

}
