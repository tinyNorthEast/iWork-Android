package com.iwork.preferences;

import android.content.Context;

import com.iwork.Base.BaseApplication;
import com.socks.library.KLog;

/**
 * Created by JianTao on 15/11/23.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class Preferences {
    private PreferenceProxy mPref;
    private PreferenceEditorProxy mEditor;

    private static Preferences instance = null;
    public static boolean mLogout = false;

    public Preferences(Context context) {
        mPref = PreferenceProxy.getInstance();
        mEditor = mPref.getEditor();
    }

    public static synchronized Preferences getInstance() {
        if (instance == null) {
            instance = new Preferences(BaseApplication.getAppContext());
        }
        KLog.d("getInstance : " + instance);
        return instance;
    }

    /**
     * 保存 token
     *
     * @param token
     */
    public void setToken(String token) {
        mEditor.putString("token", token);
        mEditor.commit();
    }

    public String getToken() {
        return mPref.getString("token", "");
    }

    /**
     * 保存 中文名
     *
     * @param zhName
     */
    public void setZhName(String zhName) {
        mEditor.putString("zh_name", zhName);
        mEditor.commit();
    }

    public String getZhName() {
        return mPref.getString("zh_name", "");
    }

    /**
     * 保存UserId
     *
     * @param userId
     */
    public void setUserId(String userId) {
        mEditor.putString("userId", userId);
        mEditor.commit();
    }

    public String getUserId() {
        return mPref.getString("userId", "");
    }

    /**
     * 保存当前的城市id
     * @param cityid
     */
    public void setCurrentCityId(int cityid) {
        mEditor.putInt("cityId", cityid);
        mEditor.commit();
    }

    public int getCurrentCityId() {
        return mPref.getInt("cityId", 10001);
    }

    /**
     * 清除数据
     */
    public void clear() {
        reset();
        mEditor.clear();
    }

    private void reset() {

    }

    public void commit() {
        if (mEditor != null)
            mEditor.commit();
    }
}
