package com.iwork.ui.view;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.impetusconsulting.iwork.R;
import com.iwork.utils.WindowUtil;

import java.util.Arrays;
import java.util.List;

import com.iwork.adapter.CommonWheelAdapter;

/**
 * @description:列表型选择弹窗
 * @author: "zhangjiantao"
 */
public class ListPickerWindow<T> {

    protected final CommonWheelAdapter<T> mWheelAdapter;
    protected Context mContext;
    protected TextView mBtnConfirm;
    protected TextView mTvTitle;
    protected TextView mBtnCancel;
    protected WheelView mSexWheel;
    private View mParent;
    private PopupWindow mPopupWindow;
    private View mContentView;
    private ListPickerListener<T> mPickerListener;
    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bar_btn_confirm:
                    /** 点击声音 */
                    if (mPickerListener != null) {
                        int index = mSexWheel.getCurrentItemIndex();
                        T object = mWheelAdapter.getDataAt(index);
                        if (object != null)
                            mPickerListener.onItemSelected(index, mWheelAdapter.getDataAt(index));
                    }
                    dismiss();
                    break;
                case R.id.bar_btn_cancel:
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 构造方法
     *
     * @param context           上下文
     * @param parentView        父View
     * @param dataArray         展示数据（注意覆写T的toString方法）
     * @param showTranslucentBg 是否展示半透明的大背景
     */
    public ListPickerWindow(Context context, View parentView, T[] dataArray, boolean showTranslucentBg) {
        this(context, parentView, Arrays.asList(dataArray), showTranslucentBg);
    }

    /**
     * 构造方法
     *
     * @param context    上下文
     * @param parentView 父View
     * @param dataArray  展示数据（注意覆写T的toString方法）
     */
    public ListPickerWindow(Context context, View parentView, T[] dataArray) {
        this(context, parentView, Arrays.asList(dataArray));
    }

    /**
     * 构造方法
     *
     * @param context    上下文
     * @param parentView 父View
     * @param dataList   展示数据（注意覆写T的toString方法）
     */
    public ListPickerWindow(Context context, View parentView, List<T> dataList) {
        this(context, parentView, dataList, false);
    }

    /**
     * 构造方法
     *
     * @param context           上下文
     * @param parentView        父View
     * @param dataList          展示数据（注意覆写T的toString方法）
     * @param showTranslucentBg 是否展示半透明的大背景
     */
    public ListPickerWindow(Context context, View parentView, List<T> dataList, boolean showTranslucentBg) {
        this(context, parentView, dataList, showTranslucentBg, "");
    }

    /**
     * 构造方法
     *
     * @param context           上下文
     * @param parentView        父View
     * @param dataList          展示数据（注意覆写T的toString方法）
     * @param showTranslucentBg 是否展示半透明的大背景
     * @param titleResId        标题
     */
    public ListPickerWindow(Context context, View parentView, List<T> dataList, boolean showTranslucentBg, int titleResId) {
        this(context, parentView, dataList, showTranslucentBg, context.getString(titleResId));
    }
    /**
     * 构造方法
     *
     * @param context           上下文
     * @param parentView        父View
     * @param dataList          展示数据（注意覆写T的toString方法）
     * @param showTranslucentBg 是否展示半透明的大背景
     * @param title             标题
     */
    public ListPickerWindow(Context context, View parentView, List< T > dataList, boolean showTranslucentBg, String title) {
        mContext = context;
        mParent = parentView;

        if (showTranslucentBg) {
            mContentView = View.inflate(mContext, R.layout.list_picker_window_with_bg, null);
        } else {
            mContentView = View.inflate(mContext, R.layout.list_picker_window, null);
        }
        WindowUtil.resizeRecursively(mContentView);
        mBtnConfirm = (TextView)mContentView.findViewById(R.id.bar_btn_confirm);
        mBtnCancel = (TextView)mContentView.findViewById(R.id.bar_btn_cancel);
        mTvTitle = (TextView)mContentView.findViewById(R.id.bar_title);
        mTvTitle.setText(title);

        mSexWheel = (WheelView)mContentView.findViewById(R.id.wheelview);
        mWheelAdapter = new CommonWheelAdapter<T>(dataList);
        mSexWheel.setAdapter(mWheelAdapter);

        mBtnConfirm.setOnClickListener(mOnClickListener);
        mBtnCancel.setOnClickListener(mOnClickListener);

        mPopupWindow = newSelectPopupWindow(mContentView);
        customDesign();
    }

    //if you want custom design, override this
    public void customDesign() {

    }

    /**
     * 设置标题
     * @param title
     */
    public void setTitle(String title){
        mTvTitle.setText(title);
    }

    /**
     * 设置标题
     * @param titleResId
     */
    public void setTitle(int titleResId){
        mTvTitle.setText(mContext.getString(titleResId));
    }


    private PopupWindow newSelectPopupWindow(View view) {
        PopupWindow popupWindow = new PopupWindow(view, WindowUtil.getWindowWidth(), LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        return popupWindow;
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener dismissListener) {
        mPopupWindow.setOnDismissListener(dismissListener);
    }

    public void dismiss() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    public void showDialog() {
        if (mPopupWindow != null && !mPopupWindow.isShowing()) {
        	mPopupWindow.showAtLocation(mParent, Gravity.BOTTOM, 0, 0);
        }
    }

    public void setListPickerListener(ListPickerListener<T> listener){
    	mPickerListener = listener;
    }

    public void setSelectedItem(int position){
    	mSexWheel.select(position);
    }

    public void setSelectedItem(T t){
    	int index = mWheelAdapter.indexOf(t);
    	if(index != -1){
    		mSexWheel.select(index);
    	}
    }

    public interface ListPickerListener<T> {
        public void onItemSelected(int position, T object);
    }

}
