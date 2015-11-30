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
