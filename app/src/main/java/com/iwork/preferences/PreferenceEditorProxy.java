package com.iwork.preferences;

import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.socks.library.KLog;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by JianTao on 15/11/23.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class PreferenceEditorProxy {
    private static final int MAX_MODIFY_COUNT = 0;

    private static final int CLASS_INDEX_INT = 0;
    private static final int CLASS_INDEX_LONG = 1;
    private static final int CLASS_INDEX_FLOAT = 2;
    private static final int CLASS_INDEX_STRING = 3;
    private static final int CLASS_INDEX_BOOLEAN = 4;

    private static PreferenceEditorProxy instance;

    private Editor editor;

    private ConcurrentHashMap<String, Object> map;
    private ConcurrentHashMap<String, Integer> modifiedMap = new ConcurrentHashMap<String, Integer>();

    private Handler handler;

    public static PreferenceEditorProxy getInstance() {
        if (instance != null)
            return instance;
        synchronized (PreferenceEditorProxy.class) {
            if (instance == null)
                instance = new PreferenceEditorProxy();
        }
        return instance;
    }

    private PreferenceEditorProxy() {
        HandlerThread thread = new HandlerThread(PreferenceEditorProxy.class.getSimpleName());
        thread.start();
        handler = new Handler(thread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                save();
                Object obj = msg.obj;
                if (obj == null)
                    return;
                PerferenceListener l = (PerferenceListener) obj;
                l.onCommit();
            }
        };
    }

    void setEditor(Editor editor) {
        this.editor = editor;
    }

    void setMap(ConcurrentHashMap<String, Object> map) {
        this.map = map;
    }

    public void clear() {
        this.editor.clear();
        this.editor.commit();
    }

    public void putString(String key, String value) {
        if (value == null)
            value = "";
        map.put(key, value);
        modifiedMap.put(key, CLASS_INDEX_STRING);
    }

    public void putLong(String key, long value) {
        map.put(key, value);
        modifiedMap.put(key, CLASS_INDEX_LONG);
    }

    public void putInt(String key, int value) {
        map.put(key, value);
        modifiedMap.put(key, CLASS_INDEX_INT);
    }

    public void putBoolean(String key, boolean value) {
        map.put(key, value);
        modifiedMap.put(key, CLASS_INDEX_BOOLEAN);
    }

    public void putFloat(String key, float value) {
        map.put(key, value);
        modifiedMap.put(key, CLASS_INDEX_FLOAT);
    }

    public void commit() {
        if (modifiedMap.size() < MAX_MODIFY_COUNT)
            return;
        handler.sendEmptyMessage(0);
    }

    /** 不缓存，立即执行 */
    public void commitCur() {
        this.editor.commit();
    }

    public void putStringCur(String key, String value) {
        if (this.editor == null)
            return;
        this.editor.putString(key, value);
    }
    
    public void putIntCur(String key,int value){
    	if(this.editor==null){
    		return;
    	}
    	this.editor.putInt(key, value);
    }

    /**
     * 立即Commit，只可以在应用退出的时候调用！
     */
    public void commitImmediately(final PerferenceListener listener) {
        handler.sendMessage(Message.obtain(handler, 0, listener));
    }

    private void save() {
        Set<String> keySet = modifiedMap.keySet();
        Object value = null;
        int typeIndex = -1;
        for (String key : keySet) {
            value = map.get(key);
            typeIndex = modifiedMap.remove(key);
            put(key, value, typeIndex);
            KLog.d("PreferenceEditorProxy save key : " + key);
        }
        KLog.d("PreferenceEditorProxy save end ... ");
        this.editor.commit();
    }

    private void put(String key, Object value, int typeIndex) {
        switch (typeIndex) {
        case CLASS_INDEX_INT:
            this.editor.putInt(key, Integer.parseInt(String.valueOf(value)));
            break;
        case CLASS_INDEX_LONG:
            this.editor.putLong(key, Long.parseLong(String.valueOf(value)));
            break;
        case CLASS_INDEX_FLOAT:
            this.editor.putFloat(key, Float.parseFloat(String.valueOf(value)));
            break;
        case CLASS_INDEX_STRING:
            this.editor.putString(key, String.valueOf(value));
            break;
        case CLASS_INDEX_BOOLEAN:
            this.editor.putBoolean(key, Boolean.parseBoolean(String.valueOf(value)));
            break;
        }
    }

    public interface PerferenceListener {
        void onCommit();
    }
}
