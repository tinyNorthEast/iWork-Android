package com.iwork.Base;

import android.app.Activity;
import android.text.TextUtils;

import com.iwork.utils.TextUtil;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * activity管理器
 * 
 * @author jiantao
 */
public class ActivityManager {
    private static ActivityManager controller;

    public static ActivityManager getInstance() {
        if (controller == null)
            controller = new ActivityManager();
        return controller;
    }

    private ActivityManager() {

    }

    private LinkedHashMap<String, BaseActivity> actMap = new LinkedHashMap<String, BaseActivity>();

    public void addAcitivty(BaseActivity ac) {
        if (actMap == null || ac == null)
            return;
        actMap.put(ac.getClass().getName(), ac);
    }

    public void removeActivity(Activity ac) {
        if (actMap == null || ac == null)
            return;
        String name = ac.getClass().getName();
        KLog.d("Actopname=" + name);
        actMap.remove(name);
    }

    public void removeActivity(String name) {
        if (actMap == null || TextUtil.isEmpty(name))
            return;
        actMap.remove(name);
    }

    /**
     * 根据类名来获取Activity
     * 
     * @param activityName
     * @return 移除包含的BaseActivity子类
     */
    public BaseActivity getActivity(String activityName) {
        if (actMap == null || TextUtils.isEmpty(activityName))
            return null;
        BaseActivity act;
        if ((act = actMap.get(activityName)) != null) {
            return act;
        }
        return null;
    }

    public void clearActivity() {
        ArrayList<Activity> removelist = new ArrayList<Activity>();
        Iterator<BaseActivity> it = actMap.values().iterator();
        while (it.hasNext()) {
            BaseActivity act = it.next();
            if (act == null)
                continue;
            removelist.add(act);
        }
        for (int i = 0; i < removelist.size(); i++) {
            removelist.get(i).finish();
        }
    }

    /**
     * 移除掉除传入activity类名以外的activity
     * */
    public void removeActivites(String name) {
        KLog.d("CurBaseName=" + name);
        ArrayList<Activity> removelist = new ArrayList<Activity>();
        Iterator<BaseActivity> it = actMap.values().iterator();
        while (it.hasNext()) {
            BaseActivity ac = it.next();
            if (ac == null)
                continue;
            String cur = ac.getClass().getName();
            if (!cur.equals(name)) {
                removelist.add(ac);
            }
        }
        for (int i = 0; i < removelist.size(); i++) {
            removelist.get(i).finish();
        }
    }

    public boolean isClear() {
        return actMap == null || actMap.size() == 0;
    }

    public String getTopActivityName() {
        String clz = "";
        Iterator<BaseActivity> it = actMap.values().iterator();
        BaseActivity a = null;
        while (it.hasNext()) {
            a = it.next();
        }
        if (a != null)
            clz = a.getClass().getName();
        return clz;
    }

    /**
     * 获取应用当前界面Activity
     * */
    public BaseActivity getTopActivityInApp() {
        Iterator<BaseActivity> it = actMap.values().iterator();
        BaseActivity a = null;
        while (it.hasNext()) {
            a = it.next();
        }
        if (a != null) {
            KLog.d("ActivityInApp=" + a.getClass().getName());
            return a;
        }
        return null;
    }
}
