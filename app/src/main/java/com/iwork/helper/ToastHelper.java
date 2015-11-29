package com.iwork.helper;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseApplication;
import com.iwork.utils.TextUtil;

/**
 * @auther ZhangJianTao
 * @function Toast帮助类
 * @since 2015/6/10
 */
public class ToastHelper {
    private static ImageView imageView;
    private static TextView textView;

    private static Toast toast;

    public enum IconType {
        /** 叹号 */
        INFO,
        /** 完成(对勾) */
        COMPLETE,
    }

    @SuppressLint("InflateParams")
    private static void createToast() {
        View toastView = LayoutInflater.from(BaseApplication.getAppContext()).inflate(R.layout.common_toast, null);
        imageView = (ImageView) toastView.findViewById(R.id.imgViewIcon);
        textView = (TextView) toastView.findViewById(R.id.txtViewContent);
        toast = new Toast(BaseApplication.getAppContext());
        toast.setView(toastView);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private static void setIcon(IconType iconType, ImageView imageView) {
        if (iconType == null) {
            imageView.setBackgroundResource(R.drawable.common_toast_icon_info);
            return;
        }

        if (IconType.INFO.toString().equals(iconType.name())) {
            imageView.setBackgroundResource(R.drawable.common_toast_icon_info);

        } else if (IconType.COMPLETE.toString().equals(iconType.name())) {
            imageView.setBackgroundResource(R.drawable.common_toast_icon_complete);
        }
    }

    public static void showLongInfo(int stringResId) {
        showLongInfo(ResourcesHelper.getString(stringResId));
    }

    public static void showShortInfo(int stringResId) {
        showShortInfo(ResourcesHelper.getString(stringResId));
    }

    public static void showShortError(int stringResId) {
        showShortError(ResourcesHelper.getString(stringResId));
    }

    public static void showLongCompleteMessage(int resId) {
        showLongCompleteMessage(ResourcesHelper.getString(resId));
    }

    public static void showShortCompleted(String msg) {
        if (toast == null)
            createToast();
        textView.setText(msg);
        setIcon(IconType.COMPLETE, imageView);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showShortCompleted(int stringResId) {
        showShortCompleted(ResourcesHelper.getString(stringResId));
    }

    public static void showShortInfo(String msg) {
        if (toast == null)
            createToast();
        textView.setText(msg);
        setIcon(IconType.INFO, imageView);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showLongInfo(String msg) {
        if (toast == null)
            createToast();
        textView.setText(msg);
        setIcon(IconType.INFO, imageView);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    public static void showShortError(String msg) {
        if (toast == null)
            createToast();
        textView.setText(msg);
        setIcon(IconType.INFO, imageView);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showLongCompleteMessage(String msg) {
        if (toast == null)
            createToast();
        textView.setText(msg);
        setIcon(IconType.COMPLETE, imageView);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

}
