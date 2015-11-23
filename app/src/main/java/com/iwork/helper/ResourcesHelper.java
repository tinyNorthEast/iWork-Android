package com.iwork.helper;

import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import com.iwork.Base.BaseApplication;

/**
 * 获取资源
 * 
 */

public class ResourcesHelper {
    public static Resources getResources(){
        return BaseApplication.getAppContext().getResources();
    }
    public static int getColor(int rid) {
        return BaseApplication.getAppContext().getResources().getColor(rid);
    }

    public static ColorStateList getColorStateList(int rid) {
        return BaseApplication.getAppContext().getResources().getColorStateList(rid);
    }

    public static String getString(int rid) {
        return BaseApplication.getAppContext().getResources().getString(rid);
    }

    public static String getString(int rid, int param1, int param2) {
        return BaseApplication.getAppContext().getResources().getString(rid, param1, param2);
    }

    public static String getString(int rid, String str) {
        return BaseApplication.getAppContext().getResources().getString(rid, str);
    }

    public static String[] getStringArray(int rid) {
        return BaseApplication.getAppContext().getResources().getStringArray(rid);
    }

    public static Drawable getDrawable(int rid) {
        return BaseApplication.getAppContext().getResources().getDrawable(rid);
    }

    public static float getDimension(int rid) {
        return BaseApplication.getAppContext().getResources().getDimension(rid);
    }

    public static DisplayMetrics getDisplayMetrics() {
        return BaseApplication.getAppContext().getResources().getDisplayMetrics();
    }

    public static int getDisplayMetrics(int x) {
        return BaseApplication.getAppContext().getResources().getDimensionPixelSize(x);
    }

    public static int getDimensionPixelSize(int x) {
        return BaseApplication.getAppContext().getResources().getDimensionPixelSize(x);
    }

    public static int getInteger(int rid) {
        return BaseApplication.getAppContext().getResources().getInteger(rid);
    }

    public static XmlResourceParser getXml(int rid) {
        return BaseApplication.getAppContext().getResources().getXml(rid);
    }

    public static Configuration getConfiguration() {
        return BaseApplication.getAppContext().getResources().getConfiguration();
    }
}
