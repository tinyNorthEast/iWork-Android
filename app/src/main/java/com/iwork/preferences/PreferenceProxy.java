package com.iwork.preferences;

import android.content.SharedPreferences;

import com.iwork.Base.BaseApplication;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by JianTao on 15/11/23.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class PreferenceProxy {
    private static PreferenceProxy instance;

    private SharedPreferences sharedPreferences;
    private PreferenceEditorProxy preferenceEditorProxy;

    private ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<String, Object>();

    public static PreferenceProxy getInstance() {
        if (instance != null)
            return instance;
        synchronized (PreferenceProxy.class) {
            if (instance == null)
                instance = new PreferenceProxy();
        }
        return instance;
    }

    private PreferenceProxy() {
        sharedPreferences = BaseApplication.getAppContext().getSharedPreferences("imconfig", 0);
        preferenceEditorProxy = PreferenceEditorProxy.getInstance();
        preferenceEditorProxy.setEditor(sharedPreferences.edit());
        preferenceEditorProxy.setMap(map);
    }

    public PreferenceEditorProxy getEditor() {
        return preferenceEditorProxy;
    }

    public String getString(String key, String defaultValue) {
        Object value = map.get(key);
        if (value != null)
            return String.valueOf(value);
        String string = null;
        if (sharedPreferences.contains(key)) {
            string = sharedPreferences.getString(key, defaultValue);
            if (string != null)
                map.put(key, string);
        }
        return string == null ? defaultValue : string;
    }

    public long getLong(String key, int defaultValue) {
        Object value = map.get(key);
        if (value != null)
            return Long.parseLong(String.valueOf(value));
        long longValue = Long.MIN_VALUE;
        if (sharedPreferences.contains(key)) {
            longValue = sharedPreferences.getLong(key, defaultValue);
            map.put(key, longValue);
        }
        return longValue == Long.MIN_VALUE ? defaultValue : longValue;
    }
    
    public String getStringCur(String key,String defaultValue){
    	return sharedPreferences.getString(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        Object value = map.get(key);
        if (value != null)
            return Integer.parseInt(String.valueOf(value));
        int intValue = Integer.MIN_VALUE;
        if (sharedPreferences.contains(key)) {
            intValue = sharedPreferences.getInt(key, defaultValue);
            map.put(key, intValue);
        }
        return intValue == Integer.MIN_VALUE ? defaultValue : intValue;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        Object value = map.get(key);
        if (value != null)
            return Boolean.parseBoolean(String.valueOf(value));
        boolean booleanValue = false;
        if (sharedPreferences.contains(key)) {
            booleanValue = sharedPreferences.getBoolean(key, defaultValue);
            map.put(key, booleanValue);
            return booleanValue;
        }
        return defaultValue;
    }

    public long getLong(String key, long defaultValue) {
        Object value = map.get(key);
        if (value != null)
            return Long.parseLong(String.valueOf(value));
        long longValue = Long.MIN_VALUE;
        if (sharedPreferences.contains(key)) {
            longValue = sharedPreferences.getLong(key, defaultValue);
            map.put(key, longValue);
        }
        return longValue == Long.MIN_VALUE ? defaultValue : longValue;
    }

    public float getFloat(String key, int defaultValue) {
        Object value = map.get(key);
        if (value != null)
            return Float.parseFloat(String.valueOf(value));
        float floatValue = Float.MIN_VALUE;
        if (sharedPreferences.contains(key)) {
            floatValue = sharedPreferences.getFloat(key, defaultValue);
            map.put(key, floatValue);
        }
        return floatValue == Float.MIN_VALUE ? defaultValue : floatValue;
    }
}
