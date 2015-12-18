package com.iwork.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.impetusconsulting.iwork.R;


/**
 * 公共标题栏
 *
 * @author yxm
 * @version 1.0
 * @since 2015/6/1
 */
public class TitleBar extends RelativeLayout {

    /**
     * 几个构造函数
     */
    public TitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public TitleBar(Context context) {
        super(context);
        initialize();
    }

    /**
     * 设置顶部栏的布局文件
     */
    private void setContentView(int layoutResId) {
        LayoutInflater.from(getContext()).inflate(layoutResId, this, true);
    }

    /**
     * 设置标题文本
     *
     * @param titleTxt 标题文本
     */
    public void setTitle(String titleTxt) {
        title_bar_text.setText(titleTxt);
        show(title_bar_text);
        hide(title_bar_img);
    }

    /**
     * 设置标题文本
     *
     * @resid 标题文本的资源ID
     */
    public void setTitle(int resid) {
        title_bar_text.setText(getResources().getString(resid));
        show(title_bar_text);
        hide(title_bar_img);
    }

    /**
     * 显示titlebar 中间的图片(logo)
     */
    public void showCenterImg(){
        hide(title_bar_text);
        show(title_bar_img);
    }

    /**
     * 隐藏titlebar 中间图片
     */
    public void hideCenterImg(){
        hide(title_bar_img);
    }
    /**
     * 设置标题栏右侧文本内容
     * 用默认样式
     *
     * @param text 文本内容
     */
    public void setRightTextView(String text, OnClickListener listener) {
        title_bar_textview_right.setText(text);
        rightTextViewClickListener = listener;
        show(title_bar_textview_right);
        hide(title_bar_img_btn_custom_right);
        hide(title_bar_img_btn_notice);
        hide(title_bar_img_btn_more);
    }

    /**
     * 重载-设置标题栏右侧文本内容，以及样式
     * 字体大小：默认
     *
     * @param text     文本内容
     * @param selector drawable/selector样式资源ID
     *                 如:setRightTextView("保存"，R.drawable.biz_title_bar_clickable_text_selector,new OnclickListener()...)
     * @param listener
     */
    public void setRightTextView(String text, int selector, OnClickListener listener) {
        rightTextViewClickListener = listener;
        title_bar_textview_right.setTextColor(getResources().getColorStateList(selector));
        title_bar_textview_right.setText(text);
        show(title_bar_textview_right);
        hide(title_bar_img_btn_custom_right);
        hide(title_bar_img_btn_notice);
        hide(title_bar_img_btn_more);
    }

    /**
     * @param text      文本内容
     * @param sizeResid :定义字体大小的dimen
     * @param selector  drawable/selector样式资源ID
     *                  如:setRightTextView("保存"，R.dimen.font_size_large，R.drawable.biz_title_bar_clickable_text_selector,new OnclickListener()...)
     * @param listener
     */
    public void setRightTextView(String text, int sizeResid, int selector, OnClickListener listener) {
        rightTextViewClickListener = listener;
        title_bar_textview_right.setTextColor(getResources().getColorStateList(selector));
        title_bar_textview_right.setTextSize(getResources().getDimension(sizeResid));
        title_bar_textview_right.setText(text);
        show(title_bar_textview_right);
        hide(title_bar_img_btn_custom_right);
        hide(title_bar_img_btn_notice);
        hide(title_bar_img_btn_more);
    }

    /**
     * 设置左侧个人头像图标的onclick事件
     *
     * @param listener
     */
    public void setMeDrawableListener(OnClickListener listener) {
        show(title_bar_img_btn_my);
        meImgBtnClickListener = listener;
    }

    /**
     * 设置左侧返回上一页图标的onclick事件
     *
     * @param listener
     */
    public void setBackDrawableListener(OnClickListener listener) {
        show(title_bar_img_btn_back);
        backImgBtnClickListener = listener;
    }

    /**
     * 设置左侧返回上一页图标是否可见
     */
    public void hideBackDrawable() {
        hide(title_bar_img_btn_back);
    }


    /**
     * 设置右侧更多(more ...)图标的onclick事件
     *
     * @param listener
     */
    public void setMoreDrawableListener(OnClickListener listener) {
        show(title_bar_img_btn_more);
        hide(title_bar_textview_right);
        hide(title_bar_img_btn_custom_right);
        hide(title_bar_img_btn_notice);
        moreImgBtnClickListener = listener;
    }

    /**
     * 设置右侧notice图标的onclick事件
     *
     * @param listener
     */
    public void setNoticeDrawableListener(OnClickListener listener) {
        show(title_bar_img_btn_notice);
        hide(title_bar_img_btn_more);
        hide(title_bar_textview_right);
        hide(title_bar_img_btn_custom_right);
        noticeImgBtnClickListener = listener;
    }

    /**
     * 设置右侧自定义ImageButton
     *
     * @param src
     * @param listener
     */
    public void setCustomImageButtonRight(int src, OnClickListener listener) {
        title_bar_img_btn_custom_right.setImageDrawable(getResources().getDrawable(src));
        show(title_bar_img_btn_custom_right);
        hide(title_bar_textview_right);
        hide(title_bar_img_btn_more);
        hide(title_bar_img_btn_notice);
        customImgBtnRightClickListener = listener;
    }

    /**
     * 设置左侧自定义ImageButton
     *
     * @param src
     * @param listener
     */
    public void setCustomImageButtonLeft(int src,String s, OnClickListener listener) {
        title_bar_img_btn_custom_left.setImageDrawable(getResources().getDrawable(src));
        show(title_layout_left);
        title_bar_textview_left.setText(s);
        customImgBtnLeftClickListener = listener;
    }


    /**
     * 显示指定的视图
     *
     * @param v
     */
    public void show(View v) {
        v.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏指定的视图
     *
     * @param v
     */
    public void hide(View v) {
        v.setVisibility(View.GONE);
    }

    /**
     * 初始化
     */
    private void initialize() {
        //设置布局文件
        setContentView(R.layout.title_bar);
        title_bar_text = (TextView) findViewById(R.id.title_bar_text);
        title_bar_img_btn_my = (ImageView) findViewById(R.id.title_bar_img_btn_my);
        title_bar_img_btn_back = (ImageView) findViewById(R.id.title_bar_img_btn_back);
        title_bar_img_btn_notice = (ImageView) findViewById(R.id.title_bar_img_btn_notice);
        title_bar_img_btn_more = (ImageView) findViewById(R.id.title_bar_img_btn_share);
        title_bar_textview_right = (TextView) findViewById(R.id.title_bar_textview_right);
        //可自定义的ImageButton
        title_bar_img_btn_custom_left = (ImageView) findViewById(R.id.title_bar_img_btn_custom_left);
        title_bar_img_btn_custom_right = (ImageView) findViewById(R.id.title_bar_img_btn_custom_right);
        title_layout_left = (LinearLayout) findViewById(R.id.title_bar_layout_custom_left);
        title_bar_textview_left = (TextView) findViewById(R.id.title_bar_textview_left);
        title_bar_img = (ImageView) findViewById(R.id.title_bar_img);
        setListeners();
    }

    /**
     * 设置控件的监听器
     */
    private void setListeners() {

        title_bar_img_btn_my.setOnClickListener(onClickListener);
        title_bar_img_btn_back.setOnClickListener(onClickListener);
        title_bar_img_btn_notice.setOnClickListener(onClickListener);
        title_bar_img_btn_more.setOnClickListener(onClickListener);
        title_bar_textview_right.setOnClickListener(onClickListener);
        title_layout_left.setOnClickListener(onClickListener);
        title_bar_img_btn_custom_right.setOnClickListener(onClickListener);
    }

    /**
     * 标题栏中view的onclic监听器
     */
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            int vid = view.getId();
            switch (vid) {
                case R.id.title_bar_img_btn_my:
                    if (meImgBtnClickListener != null) {
                        meImgBtnClickListener.onClick(view);
                    }
                    break;
                case R.id.title_bar_img_btn_back:
                    if (backImgBtnClickListener != null) {
                        backImgBtnClickListener.onClick(view);
                    }
                    break;
                case R.id.title_bar_layout_custom_left:
                    if (customImgBtnLeftClickListener != null) {
                        customImgBtnLeftClickListener.onClick(view);
                    }
                    break;
                case R.id.title_bar_img_btn_notice:
                    if (noticeImgBtnClickListener != null) {
                        noticeImgBtnClickListener.onClick(view);
                    }
                    break;
                case R.id.title_bar_img_btn_share:
                    if (moreImgBtnClickListener != null) {
                        moreImgBtnClickListener.onClick(view);
                    }
                    break;
                case R.id.title_bar_img_btn_custom_right:
                    if (customImgBtnRightClickListener != null) {
                        customImgBtnRightClickListener.onClick(view);
                    }
                    break;
                case R.id.title_bar_textview_right:
                    //点击右侧文本
                    if (rightTextViewClickListener != null) {
                        rightTextViewClickListener.onClick(view);
                    }
                    break;

            }
        }
    };


    /*可自定义的两个ImageButton*/
    private ImageView title_bar_img_btn_custom_left;
    private ImageView title_bar_img_btn_custom_right;

    private ImageView title_bar_img_btn_notice;
    private ImageView title_bar_img_btn_back;
    private ImageView title_bar_img_btn_more,title_bar_img;
    private TextView title_bar_textview_right,title_bar_textview_left;
    private ImageView title_bar_img_btn_my;
    private TextView title_bar_text;
    private LinearLayout title_layout_left;

    /*下面是所有控件的事件Listener*/
    private OnClickListener customImgBtnLeftClickListener;
    private OnClickListener customImgBtnRightClickListener;
    private OnClickListener noticeImgBtnClickListener;
    private OnClickListener rightTextViewClickListener;
    private OnClickListener moreImgBtnClickListener;
    private OnClickListener backImgBtnClickListener;
    private OnClickListener meImgBtnClickListener;
}
