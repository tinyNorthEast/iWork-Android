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
        KLog.d("token-->", mPref.getString("token", ""));
        return mPref.getString("token", "");
    }

    /**
     * 保存 七牛token
     *
     * @param token
     */
    public void setQiNiuToken(String token) {
        mEditor.putString("qiniutoken", token);
        mEditor.commit();
    }

    public String getQiNiuToken() {
        KLog.d("token-->", mPref.getString("qiniutoken", ""));
        return mPref.getString("qiniutoken", "");
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
     *
     * @param cityid
     */
    public void setCurrentCityId(int cityid) {
        mEditor.putInt("cityId", cityid);
        mEditor.commit();
    }

    public int getCurrentCityId() {
        return mPref.getInt("cityId", 1000);
    }

    /**
     * 保存手机号
     *
     * @param phone
     */
    public void setPhone(String phone) {
        mEditor.putString("phone", phone);
        mEditor.commit();
    }

    public String getPhone() {
        return mPref.getString("phone", "");
    }

    /**
     * 保存邮箱
     *
     * @param mail
     */
    public void setmail(String mail) {
        mEditor.putString("mail", mail);
        mEditor.commit();
    }

    public String getmail() {
        return mPref.getString("mail", "");
    }

    /**
     * 保存role_code
     *
     * @param role_code
     */
    public void setrole_code(int role_code) {
        mEditor.putInt("role_code", role_code);
        mEditor.commit();
    }

    public int getrole_code() {
        return mPref.getInt("role_code", 0);
    }
 /**
     * 保存头像Url
     *
     * @param url
     */
    public void setUserHeadUrl(String url) {
        mEditor.putString("user_headurl", url);
        mEditor.commit();
    }

    public String getUserHeadUrl() {
        return mPref.getString("user_headurl", "");
    }


    /**
     * 清除数据
     */
    public void clear() {
        reset();
        mEditor.clear();
    }

    private void reset() {
        setUserId("");
        setToken("");
//        setPhone("");
    }

    public void commit() {
        if (mEditor != null)
            mEditor.commit();
    }
}
