package com.iwork.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.widget.AbsListView;

import com.ddtaxi.common.tracesdk.TraceManager;
import com.didi.beatles.model.event.BtsBaseEvent;
import com.didi.beatles.theone.business.passenger.BtsWaitingForCarActivity;
import com.didi.beatles.ui.activity.passenger.BtsPassengerWaitingForDriverActivity;
import com.didi.car.helper.AlarmManagerFactory;
import com.didi.car.net.CarServerParam;
import com.didi.car.ui.fragment.CarNewWaitForResponseFragment;
import com.didi.car.ui.fragment.CarPayWxAgentFragment;
import com.didi.car.ui.fragment.CarWaitForArrivalFragment;
import com.didi.common.base.BaseApplication;
import com.didi.common.base.DateTime;
import com.didi.common.base.DidiApp;
import com.didi.common.config.Preferences;
import com.didi.common.database.CityListHelper;
import com.didi.common.helper.ExitHelper;
import com.didi.common.helper.LocationHelper;
import com.didi.common.helper.ResourcesHelper;
import com.didi.common.helper.SUUIDHelper;
import com.didi.common.model.Address;
import com.didi.common.model.City;
import com.didi.common.model.CityList;
import com.didi.common.model.ExternalData;
import com.didi.common.model.SNSConfig;
import com.didi.common.net.ServerParam;
import com.didi.common.ui.fragment.SlideFragment;
import com.didi.flier.ui.fragment.FlierNewWaitForResponseFragment;
import com.didi.flier.ui.fragment.FlierWaitForArrivalFragment;
import com.didi.frame.FragmentMgr;
import com.didi.frame.MainActivity;
import com.didi.frame.SplashActivity;
import com.didi.soso.location.LocationController;
import com.didi.taxi.ui.fragment.TaxiVoiceOrderFragment;
import com.didi.taxi.ui.fragment.TaxiWaitForArrivalFragment;
import com.didi.taxi.ui.fragment.TaxiWaitForResponseFragment;
import com.sdu.didi.psnger.R;
import com.tencent.mm.sdk.modelbiz.JumpToBizWebview;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import org.apache.http.HttpHost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Proxy;
import java.net.SocketException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
import java.util.UUID;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.ImageUtil;

/**
 * 工具类
 *
 * @author ChenHuajiang
 * @since 2012-8-7
 */
public class Utils {
    private static CityList cityList;
    private static long lastClickTime;
    private static long delayTime = 800;
    private static long longDelayTime = 2500;
    /** 圣诞语音时限 **/
    private static final String CHRISTMAS_START_TIME = "2014-12-22 00:00:00";
    private static final String CHRISTMAS_END_TIME = "2014-12-25 23:59:59";
    /** gps定位 */
    private static final String LOC_GPS = "gps";

    /** 获取当前版本号 */
    public static String getCurrentVersion() {
        String pkgName = BaseApplication.getAppContext().getPackageName();
        try {
            PackageInfo pinfo = BaseApplication
                    .getAppContext().getPackageManager().getPackageInfo(pkgName, PackageManager.GET_CONFIGURATIONS);
            return pinfo.versionName;
        } catch (Exception e) {
        }
        return "";
    }

    /** 获取当前VersionCode */
    public static int getVersionCode() {
        String pkgName = BaseApplication.getAppContext().getPackageName();
        try {
            PackageInfo pinfo = BaseApplication
                    .getAppContext().getPackageManager().getPackageInfo(pkgName, PackageManager.GET_CONFIGURATIONS);
            return pinfo.versionCode;
        } catch (Exception e) {
        }
        return 1;
    }

    /** 是否展示气泡 */
    public static boolean isShow() {
        return true;
    }

    /** 获取软件的PackageName */
    public static String getPackageName(Context context) {
        String pkg = context.getApplicationContext().getPackageName();
        return pkg;
    }

    /**
     * 获取耳机连接状态，true已连接，false未连接
     *
     * @param context
     * @return
     */
    public static boolean getHeadSetState(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.isWiredHeadsetOn()) {
            return true;
        } else {
            return false;
        }
    }

    public static void postBtsEvent(Object object, String tag) {
        if (object == null) {
            EventBus.getDefault().post(new BtsBaseEvent(), tag);
        } else {
            EventBus.getDefault().post(object, tag);
        }
    }

    /**
     * 获取内存大小
     *
     * @return
     */
    public static int getMemorySize() {
        File pathFile = Environment.getDataDirectory();
        StatFs statFs = new StatFs(pathFile.getPath());
        long blockSize = statFs.getBlockSize();
        long availableSize = statFs.getAvailableBlocks();
        long memSize = availableSize * blockSize;
        int size = (int) (memSize / 1024 / 1024);
        return size;
    }

    /**
     * 删除内存文件
     *
     * @param path
     */
    public static void deleteFileDir(String path) {
        try {
            if (!TextUtil.isEmpty(path)) {
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 显示嘀嘀提示对话框
     *
     * @param context
     * @param message
     */
    public static void showDialog(Context context, String message) {
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.didi_notice)).setMessage(message)
                .setNeutralButton(context.getString(R.string.confirm), new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 网络是否可用
     *
     * @param context
     * @return true:可用 false:不可用
     */
    @SuppressLint("NewApi")
    public static boolean haveInternet(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            // 网络不可用
            return false;
        }
        if (info.isRoaming()) {
            return true;
        }
        return true;
    }

    /** 组装参数 */
    public static String addParam(String url, String parName, String parValue) {
        url += parName + "=" + parValue + "&";
        // // 上行流量dingh
        // CountFlow.UpFlow(parName + "=" + parValue + "&");
        return url;
    }
    public static boolean isEmpty(Object obj) {
        return (null == obj);
    }
    public static boolean isNotEmpty(Object obj){
        return ! isEmpty(obj);
    }
    public static boolean isEmpty(Collection<?> collection) {
        return (null == collection || collection.isEmpty());
    }

    public static boolean isNotEmpty(Collection<?> collection){
        return ! isEmpty(collection);
    }
    /**
     * 得到手机的IMEI号
     *
     * @return
     */
    public static String getIMEI() {
        TelephonyManager mTelephonyMgr = (TelephonyManager) BaseApplication.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephonyMgr.getDeviceId() == null) {
            return "";
        } else
            return mTelephonyMgr.getDeviceId();
    }

    /**
     * 得到手机的IMSI号
     *
     * @param context
     * @return
     */
    public static String getIMSI(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephonyMgr.getSubscriberId() == null) {
            return "";
        } else
            return mTelephonyMgr.getSubscriberId();
    }

    /** 获得手机的型号 */
    public static String getModel() {
        String temp = Build.MODEL;
        if (TextUtil.isEmpty(temp)) {
            return "";
        } else {
            return temp;
        }

    }

    /**
     * 今天 15：40
     * @param millis
     * @return
     */
    public static String getDayOfMillis(long millis){
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time=sdf.format(date);

        Calendar curent= Calendar.getInstance();
        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        int days=calendar.get(Calendar.DAY_OF_YEAR)-curent.get(Calendar.DAY_OF_YEAR);
        if(days==0){
            time="今天 "+time;
        }else if(days==1){
            time="明天 "+time;
        }else if(days==2){
            time="后天 "+time;
        }
        return time;
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public static boolean isAppOnForeground() {
        try {
            ActivityManager activityManager = (ActivityManager) BaseApplication.getAppContext().getSystemService(Context.ACTIVITY_SERVICE);
            String packageName = BaseApplication.getAppContext().getPackageName();
            List<RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
            if (!tasks.isEmpty()) {
                ComponentName topActivity = tasks.get(0).topActivity;
                if (topActivity.getPackageName().equals(packageName)) {
                    return true;
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 得到当前使用的接入点的代理地址
     *
     * @param context
     * @return
     */
    public static Proxy getApnProxy(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (ni.isConnected()) { // 如果有wifi连接，则选择wifi，不返回代理
            // NqLog.d("getApnProxy ni.isConnected()");
            return null;
        } else {
            HashMap<String, String> map = getCurrentApn(context);
            if (map.size() == 0) {
                return null;
            }
            return new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(map.get("proxy"), Integer.valueOf(map.get("port"))));
        }
    }

    /**
     * 得到手机当前网络是否可用
     *
     * @param context context
     * @return 1:正常网络，0:无网络，2:网络不可用
     */
    public static int getNetworkEnable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetInfo != null) {
            if (activeNetInfo.isAvailable() == true){
                return 1;
            }else{
                return 2;
            }
        }
        return 0;
    }

    /**
     * 返回用户手机运营商名称
     * @param context context
     * @return 1：移动  2：联通  3：电信
     */
    private static int getMobileProvider(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = telephonyManager.getSubscriberId(); // 返回唯一的用户ID
        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
        if (imsi != null){
            if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
                return 1;
            } else if (imsi.startsWith("46001")) {
                return 2;
            } else if (imsi.startsWith("46003")) {
                return 3;
            }
        }
        return 0;
    }

    /**
     * 获得网络类型，如果是wifi情况下，netWorkTypes[0]为空字符串，netWorkTypes[1] 为WIFI;
     * 如果是非wifi下，netWorkTypes[0]为cmnet, netWorkTypes[1]为HSDPA
     */
    public static String[] getNetWorkType() {
        String[] netWorkTypes = new String[2];
        ConnectivityManager connectMgr = (ConnectivityManager) BaseApplication.getAppContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if (connectMgr != null) {
            NetworkInfo info = connectMgr.getActiveNetworkInfo();
            if (info != null) {
                if (info.getType() == ConnectivityManager.TYPE_MOBILE) {// 用的移动数据
                    netWorkTypes[0] = info.getExtraInfo();
                    netWorkTypes[1] = info.getSubtypeName();
                } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {// 用的wifi
                    netWorkTypes[0] = null;
                    netWorkTypes[1] = info.getTypeName();
                }
            }
        }
        return netWorkTypes;
    }

    /**
     * 获取网络类型
     *
     * @return
     */
    public static String getNetworkType() {
        String name = "UNKNOWN";
        Context c = BaseApplication.getAppContext();

        ConnectivityManager connMgr = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null) {
            if (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) {
                return "WIFI";
            }
        }

        TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return name;
        }

        int type = tm.getNetworkType();
        switch (type) {
        case TelephonyManager.NETWORK_TYPE_GPRS:
        case TelephonyManager.NETWORK_TYPE_EDGE:
        case TelephonyManager.NETWORK_TYPE_CDMA:
        case TelephonyManager.NETWORK_TYPE_1xRTT:
        case TelephonyManager.NETWORK_TYPE_IDEN:
            name = "2G";
            break;
        case TelephonyManager.NETWORK_TYPE_UMTS:
        case TelephonyManager.NETWORK_TYPE_EVDO_0:
        case TelephonyManager.NETWORK_TYPE_EVDO_A:
        case TelephonyManager.NETWORK_TYPE_HSDPA:
        case TelephonyManager.NETWORK_TYPE_HSUPA:
        case TelephonyManager.NETWORK_TYPE_HSPA:
        case TelephonyManager.NETWORK_TYPE_EVDO_B:
        case TelephonyManager.NETWORK_TYPE_EHRPD:
        case TelephonyManager.NETWORK_TYPE_HSPAP:
            name = "3G";
            break;
        case TelephonyManager.NETWORK_TYPE_LTE:
            name = "4G";
            break;
        case TelephonyManager.NETWORK_TYPE_UNKNOWN:
            name = "UNKNOWN";
            break;
        default:
            name = "UNKNOWN";
            break;
        }
        return name;
    }

    public static int getNetworkTypeNum(Context context){
        int mobileType = getMobileProvider(context);
        String networkType = getNetworkType();
        int netWorkNum = getNetworkEnable(context);
        if (networkType.equalsIgnoreCase("WIFI")){
            return 10;
        }else if (netWorkNum == 0 || mobileType <= 0){// 没有获取到运营商类型或者网络不可用时
            return 0;
        }
        return getAliasNetType(networkType,mobileType);
    }

    private static int getAliasNetType(String networkType,int mobileType){
        int networkTypeNum = 0;
        if (networkType.equalsIgnoreCase("2G")){
            networkTypeNum = mobileType * 3 - 2;
        }else if (networkType.equalsIgnoreCase("3G")){
            networkTypeNum = mobileType * 3 - 1;
        }else if (networkType.equalsIgnoreCase("4G")){
            networkTypeNum = mobileType * 3;
        }
        return networkTypeNum;
    }

    /** 飞行模式是否打开 */
    public static boolean isAirPlaneModeOn(Context context) {
        ContentResolver cr = context.getContentResolver();
        String mode = Settings.System.getString(cr, Settings.System.AIRPLANE_MODE_ON);
        if ("0".equals(mode)) {
            return false;
        }
        return true;
    }

    /** 获取手机号 */
    public static String getMobilePhone(Context context) {
        TelephonyManager telephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyMgr != null) {
            String tel = telephonyMgr.getLine1Number();
            if (!TextUtil.isEmpty(tel) && tel.startsWith("+86")) {
                tel = tel.replace("+86", "");
            }
            return tel;
        } else {
            return null;
        }
    }

    /** 判断手机号是否合法 */
    public static boolean isMobile(String mobiles) {
        if (TextUtil.isEmpty(mobiles)) {
            return false;
        }
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,//D])|(18[0,5-9]))//d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /** 获取网络状态 */
    public static boolean getMobileDataStatus(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        Class<?> conMgrClass = null; // ConnectivityManager类
        Field iConMgrField = null; // ConnectivityManager类中的字段
        Object iConMgr = null; // IConnectivityManager类的引用
        Class<?> iConMgrClass = null; // IConnectivityManager类
        Method getMobileDataEnabledMethod = null; // setMobileDataEnabled方法

        try {
            // 取得ConnectivityManager类
            conMgrClass = Class.forName(conMgr.getClass().getName());
            // 取得ConnectivityManager类中的对象mService
            iConMgrField = conMgrClass.getDeclaredField("mService");
            // 设置mService可访问
            iConMgrField.setAccessible(true);
            // 取得mService的实例化类IConnectivityManager
            iConMgr = iConMgrField.get(conMgr);
            // 取得IConnectivityManager类
            iConMgrClass = Class.forName(iConMgr.getClass().getName());
            // 取得IConnectivityManager类中的getMobileDataEnabled(boolean)方法
            getMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod("getMobileDataEnabled");
            // 设置getMobileDataEnabled方法可访问
            getMobileDataEnabledMethod.setAccessible(true);
            // 调用getMobileDataEnabled方法
            return (Boolean) getMobileDataEnabledMethod.invoke(iConMgr);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    /***
     * 取得GPS开关
     *
     * @return
     */
    public static boolean isGpsOpen() {
        LocationManager lm;
        lm = (LocationManager) BaseApplication.getAppContext().getSystemService(Context.LOCATION_SERVICE);
        boolean GPS_status = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // boolean NETWORK_status =
        // lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        return GPS_status;
    }

    // private static boolean isGPSEnable(Context cxt) {
    // /*
    // * 用Setting.System来读取也可以，只是这是更旧的用法 String str =
    // * Settings.System.getString(getContentResolver(),
    // * Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
    // */
    // String str = Settings.Secure.getString(cxt.getContentResolver(),
    // Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
    //
    // if (str != null) {
    // return str.contains("gps");
    // } else {
    // return false;
    // }
    // }

    /** 自动开启GPS */
    public static void openGPS(Context ctx) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(ctx, 0, GPSIntent, 0).send();
        } catch (CanceledException e) {
            e.printStackTrace();
        }
    }

    /** 打开网络 */
    public static boolean gprsEnabled(Context ctx, boolean bEnable) {
        // if ("UNKNOWN".equals(getCurrentApnType(ctx))) {
        // return false;
        // } else {
        boolean isOpen = gprsIsOpenMethod(ctx, "getMobileDataEnabled");
        if (isOpen == !bEnable) {
            setGprsEnabled(ctx, "setMobileDataEnabled", bEnable);
        }
        return isOpen;
        // }
    }

    private static boolean gprsIsOpenMethod(Context ctx, String methodName) {
        ConnectivityManager mCM = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class<?> cmClass = mCM.getClass();
        Class<?>[] argClasses = null;
        Object[] argObject = null;

        Boolean isOpen = false;
        try {
            Method method = cmClass.getMethod(methodName, argClasses);

            isOpen = (Boolean) method.invoke(mCM, argObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isOpen;
    }

    private static void setGprsEnabled(Context ctx, String methodName, boolean isEnable) {
        ConnectivityManager mCM = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class<?> cmClass = mCM.getClass();
        Class<?>[] argClasses = new Class[1];
        argClasses[0] = boolean.class;

        try {
            Method method = cmClass.getMethod(methodName, argClasses);
            method.invoke(mCM, isEnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否开启了wifi
     *
     * @return
     */
    public static boolean isWifi(Context context) {
        return TextUtil.equals("wifi", getCurrentApnType());
    }

    /**
     * 得到手机当前正在使用的网络类型，如果是wifi的话，返回 wifi字符串，否则返回other_类型编码，参考TelephonyManager
     *
     * @return
     */
    public static String getCurrentApnType() {
        ConnectivityManager connectivityManager = (ConnectivityManager) BaseApplication.getAppContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (ni.isConnected()) { // 如果有wifi连接，则选择wifi，不返回代理
            return "wifi";
        }

        TelephonyManager telmanager = (TelephonyManager) BaseApplication.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        int typ = telmanager.getNetworkType();

        String type = "NULL";
        if (typ == TelephonyManager.NETWORK_TYPE_EDGE) {
            type = "EDGE"; // 2.75G
        }
        if (typ == TelephonyManager.NETWORK_TYPE_GPRS) {
            type = "GPRS"; // 2G
        }
        if (typ == TelephonyManager.NETWORK_TYPE_UMTS) {
            type = "UTMS"; // 3G
        }
        if (typ == TelephonyManager.NETWORK_TYPE_UNKNOWN) {
            type = "UNKNOWN";
        }
        return type;
    }

    /**
     * 判断软件主界面是否在前台
     *
     * @param ctx
     * @param pkgName
     * @return
     */
    public static boolean isAppTopFront(Context ctx, String pkgName) {
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = am.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            // 应用程序位于堆栈的顶层
            if (pkgName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
                return true;
            }
        }
        return false;

    }

    /**
     * 在SDCARD创建文件目录
     *
     * @param fileDir
     *            目录名字
     */
    public static File mkFileDir(String fileDir) {
        File saveDir = Environment.getExternalStorageDirectory();
        saveDir = new File(saveDir + File.separator);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        return saveDir;
    }

    /**
     * 获取字节数
     *
     * @param inStream
     * @return 字节数
     * @throws Exception
     */
    public static long read(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] b = outStream.toByteArray();
        return b.length;
    }

    /**
     * 读取流
     *
     * @param inStream
     * @return 字节数组
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }

    /**
     * 保存文件
     *
     * @param inputStream
     *
     * @param fileName
     *            文件名
     *
     */
    public static boolean saveFile(final Context ctx, InputStream isInputStream, final String fileName) {
        int len = 0;
        try {
            OutputStream os = new FileOutputStream(fileName);
            byte[] buf = new byte[1024];

            while ((len = isInputStream.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
            isInputStream.close();

            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /** 判断快捷方式是否存在 */
    public static boolean hasShortCut(Context context) {
        String url = "";
        if (getSystemVersion() < 8) {
            url = "content://com.android.launcher.settings/favorites?notify=true";
        } else {
            url = "content://com.android.launcher2.settings/favorites?notify=true";
        }
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(Uri.parse(url), null, "title=?", new String[] { context.getString(R.string.app_name) }, null);

        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        }

        return false;
    }

    @SuppressLint("NewApi")
    private static int getSystemVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 安装apk
     *
     * @param context
     * @param apkPath
     *            安装包的绝对路径
     */
    public static void installApk(Context context, String apkPath) {
        Uri uri = Uri.fromFile(new File(apkPath)); // APK路径
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 启动安装程序 并且需要有返回通知
     *
     * @param activity
     *            启动自哪个Activity
     * @param apkPath
     *            安装包绝对路径
     * @param requestCode
     *            请求码
     * @author liuwei
     */
    public static void installApk(Activity activity, String apkPath, int requestCode) {
        try {
            Uri uri = Uri.fromFile(new File(apkPath)); // APK路径
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            activity.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("apkPath=" + apkPath + ",errMsg=" + e.getMessage());

        }
    }

    /** 计算经纬度（如果参数为空，返回０） */
    public static double distance(LatLng a, LatLng b) {
        if (a == null || b == null) {
            return 0;
        }
        return distance(a.latitude, a.longitude, b.latitude, b.longitude);
    }

    /** 计算经纬度距离 */
    public static double distance(double lat_a, double lng_a, double lat_b, double lng_b) {
        final double EARTH_RADIUS = 6378137.0;
        double radLat1 = (lat_a * Math.PI / 180.0);
        double radLat2 = (lat_b * Math.PI / 180.0);
        double a = radLat1 - radLat2;
        double b = (lng_a - lng_b) * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    /** 测试时调用 */
    public static String getRandomHms_Test(int min, int max) {

        String hour = String.valueOf((int) Math.rint(Math.random() * (max - min)) + min);
        String minute = String.valueOf((int) Math.rint(Math.random() * (59 - 0) + 0));
        String second = String.valueOf((int) Math.rint(Math.random() * (59 - 0) + 0));
        String hms = hour + ":" + minute + ":" + second;
        // NqLog.e(new Exception(), "Link Time:" + hms1);
        return hms;
    }

    /** 测试时调用 */
    public static long getNextLinkMillisecond_Test(int nextDay, String time) {
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.DAY_OF_MONTH, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 1);
        c.set(Calendar.SECOND, (int) Math.rint(Math.random() * (59 - 0) + 0));
        // NqLog.e(new Exception(), "Next Link Time:" + nextDay + " " + h + ":"
        // + m + ":" + hms[2]);
        return c.getTimeInMillis();
    }

    /**
     * 得到天数的毫秒数
     *
     * @param days
     * @return
     */
    public static long getMilliSecondByDay(int days) {
        return 24L * 60 * 60 * 1000 * days;
    }

    /**
     * 得到小时的毫秒数
     *
     * @param hour
     * @param min
     * @return
     */
    public static long getMilliSecondByHourAndMins(int hour, int min) {
        return hour * 60 * 60 * 1000 + min * 60 * 1000;
    }

    /** 获取当前时间(千毫秒) */
    public static long getCurrentMillisecond() {
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        c.setTimeInMillis(System.currentTimeMillis());
        return c.getTimeInMillis();
    }

    /** 取得下次联网时间 */
    public static long getNextLinkMillisecond(int nextDay, String time) {
        Calendar c = null;
        c = Calendar.getInstance(TimeZone.getDefault());

        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.DAY_OF_MONTH, nextDay);
        String[] hms = time.split(":");
        if (hms != null && hms.length > 2) {
            c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hms[0]));
            c.set(Calendar.MINUTE, Integer.parseInt(hms[1]));
            c.set(Calendar.SECOND, Integer.parseInt(hms[2]));
        }
        return c.getTimeInMillis();
    }

    /**
     * 根据条件返回一定范围的随机数
     *
     * @param min
     *            随机数的最小值
     * @param max
     *            随机数的最大值
     * @return hms 时分秒
     * @author ChenHuajiang
     * */
    public static String getRandomHms(int min, int max) {
        String hour = String.valueOf((int) Math.rint(Math.random() * (max - min)) + min);
        String minute = String.valueOf((int) Math.rint(Math.random() * (59 - 0) + 0));
        String second = String.valueOf((int) Math.rint(Math.random() * (59 - 0) + 0));
        String hms = hour + ":" + minute + ":" + second;
        return hms;
    }

    // // 获取某个网络UID的接受字节数
    // public static long getUidRxBytes(int uid) {
    // XjLog.d(new Exception(), "uid RxBytes:" +
    // TrafficStats.getUidRxBytes(uid));
    // return TrafficStats.getUidRxBytes(uid);
    // }

    /**
     * 根据条件返回一定范围的随机数
     *
     * @param min
     *            随机数的最小值
     * @param max
     *            随机数的最大值
     * @return hms 时分秒
     * @author ChenHuajiang
     * */
    public static int[] getRandomHmsInt(int min, int max) {
        int hour = (int) Math.rint(Math.random() * (max - min)) + min;
        int minute = (int) Math.rint(Math.random() * (59 - 0) + 0);
        int second = (int) Math.rint(Math.random() * (59 - 0) + 0);
        int[] times = new int[3];
        times[0] = hour;
        times[1] = minute;
        times[2] = second;
        return times;
    }

    // 读取sd中的文件
    public static String readSDCardFile(String path) throws IOException {
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        String result = streamRead(fis);
        return result;
    }

    private static String streamRead(InputStream is) throws IOException {
        int buffersize = is.available();
        // 取得输入流的字节长度
        byte buffer[] = new byte[buffersize];
        is.read(buffer);
        // 将数据读入数组
        is.close();
        // 读取完毕后要关闭流。
        String result = EncodingUtils.getString(buffer, "UTF-8");
        // 防止乱码
        return result;
    }

    /** 日志输出 */
    public static void writeLog(String phone, String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd HH:mm:ss");
        String d = sdf.format(new Date());
        String string = d + "||" + s;
        FileOutputStream fos;
        File saveDir = Environment.getExternalStorageDirectory();
        String p = saveDir.getAbsolutePath() + File.separator;
        try {
            // fos = context.openFileOutput("mnt/sdcard/XjLog.txt",
            // Context.MODE_APPEND);
            fos = new FileOutputStream(new File(p + phone + ".txt"), true);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
            osw.write(string);
            osw.write("\n");
            osw.flush();
            osw.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /**
     * 向SD卡写日志
     *
     * @param phone
     *            司机电话
     * @param clz
     *            所在的类文件
     * @param content
     *            日志内容
     * */
    public static String sendLog(Context context, String phone, String clz, String content) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd HH:mm:ss");
        String d = sdf.format(new Date());
        return d + ":" + clz + ":" + content + " IP:" + getMobileIP(context);
    }

    /**
     * 向SD卡写日志
     *
     * @param phone
     *            司机电话
     * @param clz
     *            所在的类文件
     * @param content
     *            日志内容
     * */
    public static void debugLog(Context context, String phone, String clz, String content) {
        if (isAvailableSpace()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd HH:mm:ss");
            String d = sdf.format(new Date());
            String resut = d + ":" + clz + ":" + content;
            // if (TextUtil.isEmpty(phone)) {
            // phone = "00" + getIMEI(context);
            // } else if (phone.length() < 5) {
            // phone = "00" + getIMEI(context);
            // }
            writeLog(phone, resut);
        }
    }

    /** 删除SD卡文件 */
    public static void deleFile(String phone) {
        File saveDir = Environment.getExternalStorageDirectory();
        String p = saveDir.getAbsolutePath() + File.separator;
        File file = new File(p + phone);
        if (file.exists()) {
            file.delete();
        }
    }

    // 得到手机上面所有的接入点
    public static Cursor getApns(Context context) {
        // 注意，Ophone存储apn的URI，与android不一样
        // Uri uri = Uri.parse("content://settings/data_connection");
        Uri uri = Uri.parse("content://telephony/carriers");
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                context.getContentResolver().delete(uri, "apn='CTWAP'or apn='cmwap'", null);
            } while (c.moveToNext());
            c.close();
        }
        return c;

    }

    /** 判断SDCard是否存在 */
    public static boolean haveSDCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /** SDCard是否有剩余空间 */
    public static boolean isAvailableSpace() {
        if (!haveSDCard()) {
            return false;
        }
        boolean flag = false;
        int bytelength = 1024;
        File pathFile = android.os.Environment.getExternalStorageDirectory();
        android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());
        // 获取SDCard上每个block的SIZE
        long m_nBlocSize = statfs.getBlockSize();
        // 获取可供程序使用的Block的数量
        long m_nAvailaBlock = statfs.getAvailableBlocks();
        long total = m_nAvailaBlock * m_nBlocSize / bytelength;
        if (total > 5120) {
            flag = true;
        }
        return flag;
    }

    /** 手机上获取物理唯一标识码 */
    public static String getMobileID(Context ctx) {
        return Secure.getString(ctx.getContentResolver(), Secure.ANDROID_ID);
    }

    public static int getFirstVisibleIndex(AbsListView view) {
        int ret = view.getFirstVisiblePosition();

        Rect r = new Rect();
        int count = view.getChildCount();
        if (count > 0) {
            View v = view.getChildAt(0);
            if (v != null) {
                int h = v.getHeight();
                v.getGlobalVisibleRect(r);
                int off = r.bottom - r.top;
                if (off < h) {
                    if (off < h / 2) {
                        if (view.getLastVisiblePosition() < view.getCount() - 1)
                            ret++;
                    }
                }
            }
        }
        return ret;
    }

    /** 判断文件是否存在 */
    public static boolean isExists(String fileName) {
        File file = new File(fileName);

        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /** 创建文件夹 */
    public static void mkDir(String dirName) {
        String strDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + dirName;
        File dir = new File(strDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /** 将Assets里的资源文件复制到SD卡 */
    public static void assets2SDCard(Context context, String fileName, String path) {
        try {
            InputStream asset = context.getAssets().open(fileName);
            saveFile(context, asset, getSDCardPath() + path + File.separator + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readXml(Context ctx, String xml) throws Exception {
        InputStream inStream = ctx.getClassLoader().getResourceAsStream(xml);
        byte[] data = readInStream(inStream);
        return new String(data);
    }

    private static byte[] readInStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        inStream.close();
        return outputStream.toByteArray();
    }

    public static String getMobileIP(Context ctx) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);// 获取系统的连接服务
        // 实例化mActiveNetInfo对象
        NetworkInfo mActiveNetInfo = mConnectivityManager.getActiveNetworkInfo();// 获取网络连接的信息
        if (mActiveNetInfo == null) {
            return null;
        } else {
            return getIp(mActiveNetInfo);
        }
    }

    // 获取本地IP函数
    private static String getLocalIPAddress() {
        try {
            for (Enumeration<NetworkInterface> mEnumeration = NetworkInterface.getNetworkInterfaces(); mEnumeration.hasMoreElements();) {
                NetworkInterface intf = mEnumeration.nextElement();
                for (Enumeration<InetAddress> enumIPAddr = intf.getInetAddresses(); enumIPAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIPAddr.nextElement();
                    // 如果不是回环地址
                    if (!inetAddress.isLoopbackAddress()) {
                        // 直接返回本地IP地址
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // 显示IP信息
    private static String getIp(NetworkInfo mActiveNetInfo) {
        // 如果是WIFI网络
        if (mActiveNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return getLocalIPAddress();
        }
        // 如果是手机网络
        else if (mActiveNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return getLocalIPAddress();
        } else {
            return null;
        }

    }

    /**
     * 检查车牌号是否以文字加字母开头
     *
     * @param 车牌号
     * */
    public static boolean checkLicenceNumber(String s) {
        String regEx = "^[\u4E00-\u9FA3]{1}+[a-zA-Z]+[a-zA-Z0-9]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(s);
        return matcher.find();
    }

    /** 统计文件夹下所有文件的size,返回byte */
    public static long getFileSize(String root) {
        File file = new File(root);
        Vector<File> vecFile = new Vector<File>();
        File[] subFile = file.listFiles();
        long size = 0;
        try {
            for (int i = 0; i < subFile.length; i++) {
                if (subFile[i].isDirectory()) {
                    recursion(subFile[i].getAbsolutePath(), vecFile);
                } else {
                    vecFile.add(subFile[i]);
                }
            }

            for (File f : vecFile) {
                size = size + new FileInputStream(f).available();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    private static void recursion(String root, Vector<File> vecFile) {
        File file = new File(root);
        File[] subFile = file.listFiles();
        for (int i = 0; i < subFile.length; i++) {
            if (subFile[i].isDirectory()) {
                recursion(subFile[i].getAbsolutePath(), vecFile);
            } else {
                vecFile.add(subFile[i]);
            }
        }
    }

    /**
     * 得到手机屏幕宽
     *
     * @param context
     * @return
     */
    public static int getWindowWidth(Activity window) {
        DisplayMetrics dm = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 得到手机屏幕高
     *
     * @param context
     * @return
     */
    public static int getWindowHeight(Activity window) {
        DisplayMetrics dm = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 网络代理,在Wifi和非代理环境下返回null
     *
     * @param context
     * @return proxy
     * */
    public static Proxy fillProxy(Context context) {
        if (!isWifi(context)) {
            HashMap<String, String> map = getCurrentApn(context);
            if (map.size() > 0) {
                String strProxy = map.get("proxy");
                String port = map.get("port");
                if (TextUtil.isEmpty(port)) {
                    port = "80";
                }
                if ("10.0.0.200".equals(strProxy)) {
                    Properties prop = System.getProperties();
                    System.getProperties().put("proxySet", "true");
                    prop.setProperty("http.proxyHost", map.get("proxy"));
                    prop.setProperty("http.proxyPort", map.get("port"));
                } else {
                    Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(strProxy, Integer.parseInt(port)));
                    return proxy;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    /**
     * 网络代理
     *
     * @param context
     * @param param
     * */
    public static void fillProxy(Context context, HttpParams param) {
        if (!isWifi(context)) {
            HashMap<String, String> map = getCurrentApn(context);
            // XjLog.d(new Exception(), "---------proxy:" + map.get("proxy") +
            // " port:" +
            // map.get("port") + " size:" + map.size());
            if (map.size() > 0) {
                String strProxy = map.get("proxy");
                String port = map.get("port");
                if (TextUtil.isEmpty(port)) {
                    port = "80";
                }
                if ("10.0.0.200".equals(strProxy)) {
                    Properties prop = System.getProperties();
                    System.getProperties().put("proxySet", "true");
                    prop.setProperty("http.proxyHost", map.get("proxy"));
                    prop.setProperty("http.proxyPort", map.get("port"));
                } else {
                    HttpHost host = new HttpHost(strProxy, Integer.parseInt(port));
                    param.setParameter(ConnRoutePNames.DEFAULT_PROXY, host);
                }
            }
        }
    }

    // public static final String CTWAP = "ctwap";
    // public static final String CMWAP = "cmwap";
    // public static final String WAP_3G = "3gwap";
    // public static final String UNIWAP = "uniwap";
    // public static final int TYPE_NET_WORK_DISABLED = 0;// 网络不可用
    // public static final int TYPE_CM_CU_WAP = 4;// 移动联通wap10.0.0.172
    // public static final int TYPE_CT_WAP = 5;// 电信wap 10.0.0.200
    // public static final int TYPE_OTHER_NET = 6;// 电信,移动,联通,wifi 等net网络
    // public static Uri PREFERRED_APN_URI =
    // Uri.parse("content://telephony/carriers/preferapn");
    //
    // /** 此方法在部分手机上不能使用 */
    // public static int checkNetworkType(Context mContext) {
    // try {
    // final ConnectivityManager connectivityManager = (ConnectivityManager)
    // mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    // final NetworkInfo mobNetInfoActivity =
    // connectivityManager.getActiveNetworkInfo();
    // if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {
    //
    // // 注意一：
    // // NetworkInfo 为空或者不可以用的时候正常情况应该是当前没有可用网络，
    // // 但是有些电信机器，仍可以正常联网，
    // // 所以当成net网络处理依然尝试连接网络。
    // // （然后在socket中捕捉异常，进行二次判断与用户提示）。
    //
    // return TYPE_OTHER_NET;
    // } else {
    //
    // // NetworkInfo不为null开始判断是网络类型
    //
    // int netType = mobNetInfoActivity.getType();
    // if (netType == ConnectivityManager.TYPE_WIFI) {
    // // wifi net处理
    //
    // return TYPE_OTHER_NET;
    // } else if (netType == ConnectivityManager.TYPE_MOBILE) {
    //
    // // 注意二：
    // // 判断是否电信wap:
    // // 不要通过getExtraInfo获取接入点名称来判断类型，
    // // 因为通过目前电信多种机型测试发现接入点名称大都为#777或者null，
    // // 电信机器wap接入点中要比移动联通wap接入点多设置一个用户名和密码,
    // // 所以可以通过这个进行判断！
    //
    // final Cursor c = mContext.getContentResolver().query(PREFERRED_APN_URI,
    // null, null, null,
    // null);
    // if (c != null) {
    // c.moveToFirst();
    // final String user = c.getString(c.getColumnIndex("user"));
    // if (!TextUtil.isEmpty(user)) {
    // if (user.startsWith(CTWAP)) {
    // return TYPE_CT_WAP;
    // }
    // }
    // }
    // c.close();
    //
    // // 注意三：
    // // 判断是移动联通wap:
    // // 其实还有一种方法通过getString(c.getColumnIndex("proxy")获取代理ip
    // // 来判断接入点，10.0.0.172就是移动联通wap，10.0.0.200就是电信wap，但在
    // // 实际开发中并不是所有机器都能获取到接入点代理信息，例如魅族M9 （2.2）等...
    // // 所以采用getExtraInfo获取接入点名字进行判断
    //
    // String netMode = mobNetInfoActivity.getExtraInfo();
    //
    // if (netMode != null) {
    // // 通过apn名称判断是否是联通和移动wap
    // netMode = netMode.toLowerCase();
    // if (netMode.equals(CMWAP) || netMode.equals(WAP_3G) ||
    // netMode.equals(UNIWAP)) {
    //
    // return TYPE_CM_CU_WAP;
    // }
    //
    // }
    //
    // }
    // }
    // } catch (Exception ex) {
    // ex.printStackTrace();
    // return TYPE_OTHER_NET;
    // }
    //
    // return TYPE_OTHER_NET;
    //
    // }

    /**
     * 得到手机上面当前默认使用的接入点名称及端口
     *
     * @param context
     * @return K:V proxy,port,user,pwd
     */
    public static HashMap<String, String> getCurrentApn(Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (android.os.Build.VERSION.RELEASE.startsWith("4")
                || android.os.Build.VERSION.RELEASE.startsWith("5") || android.os.Build.VERSION.RELEASE.startsWith("6")) {
            String proxy = android.net.Proxy.getDefaultHost();
            int port = android.net.Proxy.getDefaultPort();
            if (!TextUtil.isEmpty(proxy)) {
                map.put("proxy", proxy);
                map.put("port", port + "");
            }
            return map;
        }

        Cursor cursor = context.getContentResolver().query(Uri.parse("content://telephony/carriers/preferapn"), null, null, null, null);

        String proxy = null;
        String port = null;
        String userName = null;
        String pwd = null;
        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToNext()) {
                proxy = cursor.getString(cursor.getColumnIndex("proxy"));
                port = cursor.getString(cursor.getColumnIndex("port"));
                userName = cursor.getString(cursor.getColumnIndex("user"));
                pwd = cursor.getString(cursor.getColumnIndex("password"));

                if (TextUtil.isEmpty(proxy) || "null".equals(proxy)) {

                } else {
                    map.put("proxy", proxy);
                    if (TextUtil.isEmpty(port) || "null".equals(port)) {
                    } else {
                        map.put("port", port);
                    }
                    if ("10.0.0.200".equals(proxy)) {
                        if (TextUtil.isEmpty(userName) || "null".equals(userName)) {
                        } else {
                            map.put("user", userName);
                        }
                        if (TextUtil.isEmpty(pwd) || "null".equals(pwd)) {
                        } else {
                            map.put("pwd", pwd);
                        }
                    }
                }
            }
            cursor.close();
        }
        return map;
    }

    /** 返回已开通城市list,里面包含每个城市的加价标准 */
    public static CityList getCityList(String json) {
        CityList list = new CityList();
        list.parseArray(json);
        return list;
    }

    /**
     * 获取指定城市的小费信息
     *
     * @param city
     *            要查询的城市
     *
     * @return 查询到的小费信息
     */
    public static ArrayList<Integer> getCityTips(String cname) {
        CityList list = Utils.getCityList(Utils.getConfig("city"));
        City city = list.getCityByName(cname);
        if (city == null)
            return null;
        return city.tipList;
    }

    public static ArrayList<Integer> getCityTipsById(String cityId) {
        CityList list = Utils.getCityList(Utils.getConfig("city"));
        City city = list.getCityById(cityId);
        if (city == null)
            return null;
        return city.tipList;
    }

    /** 获取指定城市的现在叫车方式 */
    public static int getCityType(String cid) {
        LogUtil.d("UtilsCityId=" + cid);
        CityList list = Utils.getCityList(Utils.getConfig("city"));
        City city = list.getCityById(cid);
        LogUtil.d("UtilsCityName=" + city);
        if (city == null)
            return 0;
        return city.mType;
    }

    /** 获取指定城市现在用车只输入文本提示语 */
    public static String getCityContent(String name) {
        CityList list = Utils.getCityList(Utils.getConfig("city"));
        City city = list.getCityByName(name);
        if (city == null)
            return "";
        return city.mContent;
    }

    /**
     * 读取微博分享预设文案
     *
     * @return
     */
    public static SNSConfig getShareConfig() {
        SNSConfig config = new SNSConfig();
        config.parse(Utils.getConfig("wb"));
        return config;
    }

    /**
     * 获取指定城市的中心经纬度
     *
     *
     * @return 城市中心经度
     */
    public static String getCityLng(String cname) {
        CityList list = Utils.getCityList(Utils.getConfig("city"));
        City city = list.getCityByName(cname);
        if (city == null)
            return null;
        return city.cityLng;
    }

    /**
     * 获取指定城市的中心经纬度
     *
     * @return 城市中心纬度
     */
    public static String getCityLat(String cname) {
        CityList list = Utils.getCityList(Utils.getConfig("city"));
        City city = list.getCityByName(cname);
        if (city == null)
            return null;
        return city.cityLat;
    }

    /**
     * 获取指定城市的中心经纬度
     *
     * @return 城市中心纬度
     */
    public static LatLng getLatLngByCity(String cname) {
        String c = Utils.getSimpleCityName(cname);
        CityList list = Utils.getCityList(Utils.getConfig("city"));
        City city = list.getCityByName(c);
        if (city == null)
            return null;
        return new LatLng(NumberUtil.strToDouble(city.cityLat), NumberUtil.strToDouble(city.cityLng));
    }

    /** 是否显示我的余额 */
    public static boolean shouldShowAccount() {
        String showaccountinfo = Utils.getConfig("showaccountinfo");
        LogUtil.d("shouldShowAccount : " + showaccountinfo);
        return !TextUtil.isEmpty(showaccountinfo) && "1".equals(showaccountinfo.trim());
    }

    /** 是否显示积分商城 */
    public static boolean isShowMall() {
        String showmall = Utils.getConfig("showmall");
        LogUtil.d("showmall : " + showmall);
        return !TextUtil.isEmpty(showmall) && "1".equals(showmall.trim());
    }

    public static boolean isShowTellFriend() {
        String showtellfriend = Utils.getConfig("showtellfriend");
        return !TextUtil.isEmpty(showtellfriend) && "1".equals(showtellfriend.trim());
    }

    /**
     * 获取指定城市加小费的提示
     *
     * @return 查到的提示信息
     */
    public static String getTipTitle(String cname) {
        CityList list = Utils.getCityList(Utils.getConfig("city"));
        City city = list.getCityByName(cname);
        if (city == null)
            return null;
        return city.tipTitle;
    }

    /** 获取资源文件,可能会返回Not Found异常 */
    public static String getConfig(String key) {
        try {
            String fileName = DidiApp.getInstance().getFilesDir().getAbsolutePath() + File.separator + Constant.CONFIG_NAME;
            Properties p = new Properties();
            if (!FileUtil.isExists(fileName)) {
                return ExceptionMsgUtil.EXCEPTION_MSG_NOT_FOUND;
            }
            p.load(new FileInputStream(new File(fileName)));
            return p.getProperty(key, "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ExceptionMsgUtil.EXCEPTION_MSG_NOT_FOUND;
    }

    /**
     * 去掉城市名称最后的“市”，例如：北京市->北京
     *
     * @param city
     *            要处理的城市名
     * @return 处理后的城市名
     */
    public static String getSimpleCityName(String city) {
        if (TextUtil.isEmpty(city)) {
            return null;
        } else {
            int len = city.trim().length();
            if (city.substring(len - 1).equalsIgnoreCase("市") && len > 1) {
                return city.substring(0, len - 1);
            } else {
                return city;
            }
        }
    }

    /**
     * 获取所需的比例尺
     *
     * @param distance
     *            最远的司机位置与我的距离，单位米
     * @param scrWidth
     *            屏幕的宽度，单位像素
     * @return 所需的地图比例尺
     */
    public static float getZoomLevel(double distance, int scrWidth) {
        return (float) (18 - Math.log(2 * distance / scrWidth) / Math.log(2));
    }

    /** 将图片转为圆角 */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /** 判断当前应用是否正在运行 */
    public static boolean isMyAppRunning(Context c) {
        ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = manager.getRunningTasks(Integer.MAX_VALUE);
        for (RunningTaskInfo info : list) {
            String packageName = info.topActivity.getPackageName();
            if (getPackageName(c).equalsIgnoreCase(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0 || str.trim().length() == 0;
    }

    /** 判断字符串是否为空 */
    public static boolean isTextEmpty(String text) {
        if (TextUtil.isEmpty(text))
            return true;
        return "null".equalsIgnoreCase(text);
    }

    /**
     * 从字符串资源文件读取字符串
     *
     * @param resId
     * @return
     */
    public static String getString(int resId) {
        return ResourcesHelper.getString(resId);
    }

    /**
     * 网络是否可用
     *
     * @return
     */
    public static boolean isNetworkConnected() {
        Context context = DidiApp.getInstance();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo network = cm.getActiveNetworkInfo();
        if (network != null) {
            return network.isConnected() || network.isAvailable();
        } else {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            State wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            return wifiManager.isWifiEnabled() && wifiState.equals(State.CONNECTED);
        }
    }

    /**
     * wifi选项是否开启
     *
     * @return
     */
    public static boolean isWifiEnabled () {
        Context context = DidiApp.getInstance();
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return  wifiManager.isWifiEnabled();
    }

    /**
     * gps是否开启
     * @return
     */
    public static boolean isGpsEnabled() {
        Context mContext = DidiApp.getInstance();
        String gps = Settings.System.getString(mContext.getContentResolver(), Settings.System.LOCATION_PROVIDERS_ALLOWED);
        if (TextUtils.isEmpty(gps) || !gps.contains(LOC_GPS)) {
            return false;
        } else {
            return true;
        }
    }

    public static void removeOnGlobalLayoutListener(View v, ViewTreeObserver.OnGlobalLayoutListener listener){
        if (Build.VERSION.SDK_INT < 16) {
            v.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }
    /** 获取文件名 */
    public static String getFileName(String path) {
        if (TextUtil.isEmpty(path))
            return "";
        return path.substring(path.lastIndexOf("/") + 1);
    }

    /** 获取安装包名并将其复制到SDCARD */
    public static String getInstallApk(Context context) {
        String oldFile = null;
        try {
            // 原始位置
            oldFile = context.getPackageManager().getApplicationInfo(Utils.getPackageName(context), 0).sourceDir;
        } catch (NameNotFoundException e) {
            return oldFile;
        }
        return oldFile;
        // apkName = getFileName(oldFile);
        // // XJLog.d("TAG oldapkName:" + oldFile + " apkName:" + apkName);
        // FileUtil.delete(Utils.getSDCardPath() + apkName);
        // FileInputStream fis = null;
        // FileOutputStream fos = null;
        // try {
        // // SDCard创建文件
        // File out = new File(Utils.getSDCardPath() + Constant.SDCARD_FILE_DIR + File.separator +
        // apkName);
        // out.createNewFile();
        // // 将获取到的APK复制到SDcard
        // File in = new File(oldFile);
        // fis = new FileInputStream(in);
        // fos = new FileOutputStream(out);
        // int count;
        // byte[] buffer = new byte[128 * 1024];
        // while ((count = fis.read(buffer)) > 0) {
        // fos.write(buffer, 0, count);
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // } finally {
        // try {
        // if (fis != null) {
        // fis.close();
        // }
        // if (fos != null) {
        // fos.flush();
        // fos.close();
        // }
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        //
        // }
        // // XJLog.d("TAG oldapkName:" + oldFile + " apkName:" + AppUtils.getSDCardPath() +
        // apkName);
        // return apkName;
    }

    /** 获取url后面的接口名 */
    public static String getContent(String str) {
        String regex = "[v1.1|v1.2|v1.3|v1|v2|v3|v2.1|v2.2|v3.1]/(.*?)\\?";
        Pattern p = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = p.matcher(str);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * 将日期转成毫秒
     *
     * @author ChenHuajiang
     * */
    public static long converDateToMillisecond(String date) {
        if (Utils.isTextEmpty(date))
            return 0L;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long mills = 0;
        try {
            Date d = sdf.parse(date);
            mills = d.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mills;
    }

    public static String trimText(String str) {
        if (isTextEmpty(str))
            return "";
        return str.trim();
    }

    /**
     * 通过城市id读取城市名称
     *
     * @param cid
     * @return
     */
    public static String getCityName(String cid) {
        if (cityList == null)
            cityList = Utils.getCityList(Utils.getConfig("city"));
        City city = cityList.getCityById(cid);
        if (city == null)
            return null;
        return city.name;
    }

    /**
     * 通过城市名称获取城市id
     *
     * @param cname
     * @return
     */
    public static int getCityId(String cname) {
        CityList list = Utils.getCityList(Utils.getConfig("city"));
        City city = list.getCityByName(cname);

        if (city == null) {
            String id = Preferences.getInstance().getCurrentCityId();
            TraceNetLog.log("---cityname:" + cname + " cityid:-1" + " currentid:" + id);
            if (TextUtil.isEmpty(id)) {
                return -1;
            }
            return Integer.parseInt(id);
        }

        return city.cityID;
    }

    /** 根据address获取所在城市 */
    public static String getCityIdByAddress(Address address) {
        String id = "0";
        if (address != null) {
            if (!TextUtil.isEmpty(address.cityId))
                id = address.cityId;
            if (!"0".equals(id) && !"-1".equals(id))
                return id;
            if (!TextUtil.isEmpty(address.getCity())) {
                id = CityListHelper.getCityIdByCity(address.getCity()) + "";
                if ("0".equals(id))
                    id = Utils.getCityId(address.getCity()) + "";
                if ("-1".equals(id))
                    id = "0";
            }
        }
        return id;
    }

    /**
     * 通过城市名称获取城市id
     *
     * @param cname
     * @return
     */
    public static String getCityIdString(String cname) {
        return String.valueOf(getCityId(cname));
    }

    public static String getCurrentCityId() {
        String city = Preferences.getInstance().getCurrentCity();
        return Utils.getCityIdString(city);
    }

    /** 等待抢单时弹出的提示语 */
    public static String getWaitRemarkPopMsg() {
        return Utils.getConfig("remarkPopmsg");
    }

    /** 等待抢单时弹出的提示语,点击确定时传给服务器的内容 */
    public static String getWaitRemarkTagValue() {
        return Utils.getConfig("remarkTagValue");
    }

    /** 等待抢单时弹出的提示语的时间 */
    public static String getWaitRemarkPopTime() {
        return Utils.getConfig("remarkPopTime");
    }

    /**
     * 扩大view控件的点击判断范围,用于解决控件较小时，不易点中的问题
     *
     * @param delegateView
     *            需要改变点击判断区域的view
     * @param scaleRatio
     *            ：内部将处理为scaleRatio/100 ：四个方向扩大的尺寸比例(相对于delegateView的本地坐标系)
     */
    public static void enlargeHitRect(final View delegateView, final int scaleRatio) {
        if (delegateView == null) {
            return;
        }
        final View parentView = View.class.isInstance(delegateView.getParent()) ? (View) delegateView.getParent() : null;
        if (delegateView != null && parentView != null && scaleRatio > 0) {
            delegateView.post(new Runnable() {
                @Override
                public void run() {
                    Rect newBounds = new Rect();
                    delegateView.getHitRect(newBounds);
                    int xOffset = Math.round((float) Math.abs(newBounds.left - newBounds.right) * scaleRatio / 100) / 2;// 水平方向单边偏移的大小
                    int yOffset = Math.round((float) Math.abs(newBounds.bottom - newBounds.top) * scaleRatio / 100) / 2;// 水平方向单边偏移的大小

                    newBounds.left -= xOffset;
                    newBounds.right += xOffset;
                    newBounds.top -= yOffset;
                    newBounds.bottom += yOffset;

                    parentView.setTouchDelegate(new TouchDelegate(newBounds, delegateView));
                }
            });
        }
    }

    /**
     * 将毫秒转为日期-时间字符串
     *
     * @param millisec
     * @return
     */
    public static String[] convertDateTime(long millisec) {
        Context context = DidiApp.getInstance();
        String dateString = context.getString(R.string.unknown);
        String timeString = context.getString(R.string.unknown);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millisec);
        int month = c.get(Calendar.MONTH) + 1;
        dateString = month + context.getString(R.string.month) + c.get(Calendar.DAY_OF_MONTH) + context.getString(R.string.day);
        if (0 <= c.get(Calendar.MINUTE) && c.get(Calendar.MINUTE) < 10) {
            timeString = c.get(Calendar.HOUR_OF_DAY) + ":" + "0" + c.get(Calendar.MINUTE);
        } else {
            timeString = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
        }
        long nowTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(nowTime);
        if (c.get(Calendar.YEAR) - calendar.get(Calendar.YEAR) == 0) {
            int number = c.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR);
            switch (number) {
            case 0:
                dateString = context.getString(R.string.today);
                if (0 <= c.get(Calendar.MINUTE) && c.get(Calendar.MINUTE) < 10) {
                    timeString = c.get(Calendar.HOUR_OF_DAY) + ":" + "0" + c.get(Calendar.MINUTE);
                } else {
                    timeString = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
                }
                break;
            case 1:
                dateString = context.getString(R.string.tomorrow);
                if (0 <= c.get(Calendar.MINUTE) && c.get(Calendar.MINUTE) < 10) {
                    timeString = c.get(Calendar.HOUR_OF_DAY) + ":" + "0" + c.get(Calendar.MINUTE);
                } else {
                    timeString = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
                }
                break;
            case 2:
                dateString = context.getString(R.string.acquired);
                if (0 <= c.get(Calendar.MINUTE) && c.get(Calendar.MINUTE) < 10) {
                    timeString = c.get(Calendar.HOUR_OF_DAY) + ":" + "0" + c.get(Calendar.MINUTE);
                } else {
                    timeString = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
                }
                break;
            case -1:
                dateString = context.getString(R.string.yesterday);
                if (0 <= c.get(Calendar.MINUTE) && c.get(Calendar.MINUTE) < 10) {
                    timeString = c.get(Calendar.HOUR_OF_DAY) + ":" + "0" + c.get(Calendar.MINUTE);
                } else {
                    timeString = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
                }
                break;
            default:
                break;
            }
        }
        String[] str = new String[] { dateString, timeString };
        return str;
    }

    public static String encode(String string) {
        if (Utils.isTextEmpty(string))
            return "";
        try {
            string = URLEncoder.encode(string, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
        return string;
    }

    /** 将千毫秒格式化为yyyy-MM-dd HH:mm:ss格式 */
    public static String formatDate(long millis) {
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
    /** 将千毫秒格式化为yyyy-MM-dd HH:mm格式 */
    public static String btsformatDate(long millis) {
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(date);
    }
    public static long stringToLong(String strTime)
            throws ParseException {
        Date date = stringToDate(strTime, "yyyy-MM-dd HH:mm:ss"); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    // string类型转换为date类型
    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }


    // date类型转换为long类型
    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }

    /** 打开手机自动获取网络时间和时区的开关 */
    public static void setSyncTime() {
        // try {
        // /* 设置自动获取网络时间和时区 */
        // Settings.System.putInt(DidiApp.getAppContext().getContentResolver(),
        // Settings.System.AUTO_TIME, 1);
        // Settings.System.putInt(DidiApp.getAppContext().getContentResolver(),
        // Settings.System.AUTO_TIME_ZONE, 1);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
    }

    public static String sha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }

        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes());

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前应用栈顶的Activity名称
     *
     * @return
     */
    public static String getTopActivityNameInCurrentTask() {
        String activity = "";
        ActivityManager activityManager = (ActivityManager) BaseApplication.getAppContext().getSystemService(Activity.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(100);
        String topActivityPackageName = "";
        String currentPackageName = BaseApplication.getAppContext().getPackageName();
        ComponentName componentName = null;
        for (RunningTaskInfo runningTaskInfo : runningTaskInfos) {
            componentName = runningTaskInfo.topActivity;
            topActivityPackageName = componentName.getPackageName();
            if (TextUtil.equals(currentPackageName, topActivityPackageName)) {
                activity = componentName.getClassName();
                return activity;
            }
        }
        return activity;
    }

    /**
     * 检查当前应用是否在前台运行
     *
     * @return 在前台运行时返回true
     */
    public static boolean isRunningForeground() {
        String packageName = BaseApplication.getAppContext().getPackageName();
        String topAppPackageName = getTopActivityPackegeName();

        if (TextUtil.isEmpty(packageName))
            return false;
        if (TextUtil.isEmpty(topAppPackageName))
            return false;
        return topAppPackageName.equals(packageName);
    }

    public static boolean isRunning() {
        Intent intent = new Intent();
        intent.setClass(BaseApplication.getAppContext(), MainActivity.class);
        if (BaseApplication.getAppContext().getPackageManager().resolveActivity(intent, 0) == null) {
            return false;
        }
        return true;
    }

    /**
     * 获取处于系统前台应用packageName
     *
     * @return 处于手机屏幕前台的Activity的包名
     */
    public static String getTopActivityPackegeName() {
        String topActivityPackeageName = null;
        ActivityManager activityManager = (ActivityManager) (BaseApplication.getAppContext()
                .getSystemService(android.content.Context.ACTIVITY_SERVICE));
        List<RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            if (f != null)
                topActivityPackeageName = f.getPackageName();
        }
        return topActivityPackeageName;
    }

    /**
     * 获取处于系统前台Activity名称
     *
     * @return 处于手机屏幕前台的Activity应用的名字
     */
    public static String getTopActivityLableName(String packageName) {
        String lablename = null;
        PackageManager pm = BaseApplication.getAppContext().getPackageManager();
        if (BaseApplication.getAppContext() == null)
            return lablename;
        try {
            ApplicationInfo info = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            if (info != null)
                lablename = pm.getApplicationLabel(info).toString();

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return lablename;
    }

    /** 获取SD卡路径，返回格式 path+"/" */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    /** sd卡是否可用 */
    public static boolean isSDCardAvailble() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /** 获取SD卡上的Didi目录，返回格式 path+"/" */
    public static String getDidiPath() {
        return getSDCardPath() + Constant.SDCARD_FILE_DIR + File.separator;
    }

    public static String getSOSOLocationPath() {
        return getSDCardPath() + Constant.SDCARD_LOCATE_DIR + File.separator;
    }

    public static String getAppPath() {
        return DidiApp.getAppContext().getFilesDir().getAbsolutePath() + File.separator;
    }

    public static Bitmap getBitmap(int rid) {
        BitmapFactory.Options options = ImageUtil.getDefaultOptions();
        InputStream is = BaseApplication.getAppContext().getResources().openRawResource(rid);
        return BitmapFactory.decodeStream(is, null, options);
    }

    /** 将大等于60的描述转换为min:sec格式 */
    public static String second2Min(int second) {
        int minute = 60;
        if (second < 60) {
            String secd = String.format("%1$02d", second);
            return "00:" + secd;
        }
        int minInt = second / minute;
        int secInt = second % minute;
        if (minInt >= 1) {
            String mins = String.format("%1$02d", minInt);
            String secd = String.format("%1$02d", secInt);
            return mins + ":" + secd;
        }
        return second + "";
    }
    /** 将"明天 周三 18:00"格式的时间转成long型的 */
    public static long getBtsTimeMillis(String str) {
        String tomorrow = getString(R.string.date_time_tomorrow);
        String afterTomorrow = getString(R.string.date_time_after_tomorrow);
        Calendar calendar = Calendar.getInstance();
        Date d = null;

        if (str.startsWith(tomorrow)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            d = calendar.getTime();
        } else if (str.startsWith(afterTomorrow)) {
            calendar.add(Calendar.DAY_OF_MONTH, 2);
            d = calendar.getTime();
        } else {
            if (str.split(" ").length >= 2 && str.split(" ")[0].contains("月")) {
                /* 解析时间格式"9月20日 周三 13:10" */
                String[] mm_dd_list = str.split(" ")[0].split("月|日");
                if (mm_dd_list.length == 2) {
                    String mm = mm_dd_list[0];
                    String dd = mm_dd_list[1];
                    try {
                        int month = Integer.parseInt(mm);
                        int day = Integer.parseInt(dd);
                        calendar.set(Calendar.MONTH, month-1);//-1
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            d = calendar.getTime();
        }
        d.setHours(getHour(str,2));
        d.setMinutes(getMinutes(str,2));
        d.setSeconds(0);

        return d.getTime();
    }

    /** 将"明天 周三 18:00"格式的时间转成long型的 */
    public static long getTimeMillis(String str) {
        String tomorrow = getString(R.string.date_time_tomorrow);
        String afterTomorrow = getString(R.string.date_time_after_tomorrow);
        Calendar calendar = Calendar.getInstance();
        Date d = null;

        if (str.startsWith(tomorrow)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            d = calendar.getTime();
        } else if (str.startsWith(afterTomorrow)) {
            calendar.add(Calendar.DAY_OF_MONTH, 2);
            d = calendar.getTime();
        } else {
            if (str.split(" ").length >= 2 && str.split(" ")[0].contains("-")) {
                /* 解析时间格式"10-16 13:10" */
                String[] mm_dd_list = str.split(" ")[0].split("-");
                if (mm_dd_list.length == 2) {
                    String mm = mm_dd_list[0];
                    String dd = mm_dd_list[1];
                    try {
                        int month = Integer.parseInt(mm);
                        int day = Integer.parseInt(dd);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            d = calendar.getTime();
        }
        d.setHours(getHour(str,1));
        d.setMinutes(getMinutes(str,1));
        d.setSeconds(0);

        return d.getTime();
    }

    /** 获取小时数 */
    public static int getHour(String str,int blankCount) {
        int ret = 0;
        if (str.split(" ").length < (blankCount+1)) {
            return ret;
        }
        String hh_mm = str.split(" ")[blankCount];
        String s = hh_mm.substring(0, 2);
        try {
            ret = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /** 获取分钟数 */
    private static int getMinutes(String str,int blankCount) {
        int ret = 0;
        if (str.split(" ").length < (blankCount+1)) {
            return ret;
        }
        String hh_mm = str.split(" ")[blankCount];

        String s = hh_mm.substring(3);
        try {
            ret = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 简单验证手机号是否合法
     * */
    public static boolean checkPhoneNumber(String phone) {
        if (!phone.startsWith("1")) {
            return false;
        }
        char[] phoneSplit = phone.toCharArray();
        for (int i = 1; i < phoneSplit.length - 1; i++) {
            if (phoneSplit[i] != phoneSplit[i + 1]) {
                return true;
            }
        }
        return false;
    }

    /**
     * 正则表达式验证
     *
     * @param str
     *            验证字符串
     * @return
     */
    public static boolean isNum(String str) {

        if (TextUtils.isEmpty(str))
            return false;

        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNumatcher = pattern.matcher(str);
        if (!isNumatcher.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 验证身份证号码
     *
     *
     */
    public static boolean isIdNum(String str) {

        if (TextUtils.isEmpty(str)) {
            return false;
        }

        String rgx = "^\\d{15}|^\\d{17}([0-9]|X|x)$";
        Pattern p = Pattern.compile(rgx);

        Matcher m = p.matcher(str);

        return m.matches();
    }

    /** 显示输入法 */
    public static void showInputMethod(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    /** 隐藏输入法 */
    public static void hideInputMethod(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftInputFromWindow(Context context, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

    /**
     * 根据activity当前获取焦点的控件隐藏软键盘
     */
    public static void hideSoftInputFromActivity(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

    /**
     * 显示软键盘
     */
    public static void showSoftInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    /** 格式化里程数 */
    public static String getFormattedDistance(String distance) {

        if (distance.contains("+")) {
            return distance;
        }

        DecimalFormat df1 = new DecimalFormat("#0.0");
        DecimalFormat df2 = new DecimalFormat("#0");
        double dis = Double.parseDouble(distance);
        if (dis <= 0.1) {// 99米及以内，显示0.1公里
            return "0.1";
        } else if (dis > 0.1 && dis < 10) {// 小于10公里则精确到小数点后一位
            return df1.format(dis);
        } else if (dis >= 10 && dis <= 99) {// 小于99公里则精确到各位
            return df2.format(dis);
        } else if (dis > 99) {// 大于99公里则显示为99+
            return "99+";
        }
        return distance;
    }

    /** 格式化推送时间 */
    public static String getFormattedPushTime(String time) {
        int pushTime = Integer.parseInt(time);
        if (pushTime < 1) {
            return "1";
        }
        return time;
    }

    /**
     * 将控件返回的时间分离
     *
     * @param str
     *            时间空间返回的字符串
     * @return 今天，明天，后天
     */
    public static String getDay(String str) {
        String today = ResourcesHelper.getString(R.string.date_time_today);
        String tomorrow = ResourcesHelper.getString(R.string.date_time_tomorrow);
        String afterTomorrow = ResourcesHelper.getString(R.string.date_time_after_tomorrow);

        if (str.startsWith(tomorrow)) {
            return tomorrow;
        } else if (str.startsWith(afterTomorrow)) {
            return afterTomorrow;
        } else {
            return today;
        }
    }

    public static String getTime(String str) {
        String tmp = str.substring(3, 4);
        return tmp.equals(" ") ? str.substring(4) : str.substring(3);
    }

    public static String getLastTime(String str) {
        String tmp = str.substring(str.length() - 8, str.length() - 3);
        return tmp;
    }

    public static String getDate(Context context, String str) {
        String tomorrow = ResourcesHelper.getString(R.string.date_time_tomorrow);
        String afterTomorrow = ResourcesHelper.getString(R.string.date_time_after_tomorrow);
        Calendar calendar = Calendar.getInstance();

        if (str.startsWith(tomorrow)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Date d = calendar.getTime();
            return context.getString(R.string.date_time_after_date, "" + (d.getMonth() + 1), "" + d.getDate());
        } else if (str.startsWith(afterTomorrow)) {
            calendar.add(Calendar.DAY_OF_MONTH, 2);
            Date d = calendar.getTime();
            return context.getString(R.string.date_time_after_date, "" + (d.getMonth() + 1), "" + d.getDate());
        } else {
            Date d = calendar.getTime();
            return context.getString(R.string.date_time_after_date, "" + (d.getMonth() + 1), "" + d.getDate());
        }
    }

    /** 是否定位成功 */
    public static boolean isLocated() {
        if (LocationController.getInstance().getLat() == 0 || LocationController.getInstance().getLng() == 0)
            return false;
        return true;
    }

    /**
     * 检查当前城市是否已开通服务
     *
     * @return
     */
    public static boolean isValidCity() {
        String currentCity = LocationHelper.getCurrentCity();
        /* 未定位成功 */
        if (Utils.isTextEmpty(currentCity))
            return false;
        return true;
    }

    /**
     * 防止快速重复点击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < delayTime) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static boolean isFastDoubleClickLongInterval () {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < longDelayTime) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /** 微信能力支付验证 */
    public static void weixinPayCheck(Context cxt) {
        IWXAPI api = WXAPIFactory.createWXAPI(cxt, Constant.WEIXIN_CAR_APP_ID);
        api.registerApp(Constant.WEIXIN_CAR_APP_ID);

        JumpToBizWebview.Req req = new JumpToBizWebview.Req();
        req.toUserName = Constant.WEIXIN_TOUSER_NAME;
        req.webType = 0;
        req.extMsg = "didi";
        boolean b = api.sendReq(req);
    }

    /** 针对指定机型进行Alarm适配 */
    public static boolean adapterModel(String[] modelList) {
        String model = null;
        if (modelList == null) {
            return false;
        }
        for (int i = 0; i < modelList.length; i++) {
            model = modelList[i];
            int index = model.indexOf("&&");
            if (index > 0) {
                if (Build.MODEL.startsWith(model.split("&&")[0]) && Build.MODEL.compareTo(model.split("&&")[1]) >= 0) {
                    return true;
                }
            }

            if (Build.MODEL.startsWith(modelList[i])) {
                return true;
            }
        }
        return false;
    }

    /** 获取已安装的应用列表　 **/
    public static String getAllInstalledPkg() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager pm = BaseApplication.getAppContext().getPackageManager();
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent, 0);
        StringBuilder sb = new StringBuilder();
        if (resolveInfos != null && !resolveInfos.isEmpty()) {

            for (int i = 0; i < resolveInfos.size(); i++) {
                ResolveInfo ri = resolveInfos.get(i);
                String pkgName = ri.activityInfo.packageName;
                sb.append(pkgName).append("*");
            }

        }
        return sb.toString();
    }

    /** 原链接基础上增加相应的参数　 **/
    public static String getCommonCarUrl(String url) {

        if (TextUtils.isEmpty(url)) {
            return "";
        }

        StringBuilder componUrl = new StringBuilder();
        componUrl.append(url);

        if (url.indexOf("?") == -1) {
            componUrl.append("?");
        } else {
            componUrl.append("&");
        }

        final Preferences preferences = Preferences.getInstance();

        componUrl
                .append(CarServerParam.PARAM_PHONE).append(ServerParam.SIGN_EQUAL).append(preferences.getPhone()).append("&")
                .append(CarServerParam.PARAM_APP_VERSION).append(ServerParam.SIGN_EQUAL).append(Utils.getCurrentVersion()).append("&")
                .append(CarServerParam.PARAM_LATITUDE).append(ServerParam.SIGN_EQUAL).append(LocationHelper.getCurrentLatitudeString())
                .append("&").append(CarServerParam.PARAM_LONGITUDE).append(ServerParam.SIGN_EQUAL)
                .append(LocationHelper.getCurrentLongitudeString()).append("&").append(CarServerParam.PARAM_TOKEN)
                .append(ServerParam.SIGN_EQUAL).append(preferences.getToken()).append("&").append(CarServerParam.PARAM_IMEI)
                .append(ServerParam.SIGN_EQUAL).append(Utils.getIMEI()).append("&").append(ServerParam.PARAM_DATA_TYPE)
                .append(ServerParam.SIGN_EQUAL).append(1).append("&").append(ServerParam.PARAM_CITY_ID).append(ServerParam.SIGN_EQUAL)
                .append(preferences.getCurrentCityId()).append("&").append(ServerParam.PARAM_SUUID).append(ServerParam.SIGN_EQUAL)
                .append(SUUIDHelper.getDiDiSUUID());

        return componUrl.toString();
    }

    /** 获取WebView Cookies */
    public static String getCookies(String url) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        return cookieManager.getCookie(url);
    }

    /**
     * 判断两个日期是否为同一天
     *
     * @param time1
     * @param time2
     * @return
     */
    public static boolean isInOneDay(long time1, long time2) {
        Date dateA = new Date(time1);
        Date dateB = new Date(time1);
        Calendar calDateA = Calendar.getInstance();
        calDateA.setTime(dateA);
        Calendar calDateB = Calendar.getInstance();
        calDateB.setTime(dateB);

        Log.e("taxi", calDateA.toString() + "////" + calDateB.toString());
        return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
                && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
                && calDateA.get(Calendar.DAY_OF_MONTH) == calDateB.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取屏幕分辨率
     *
     * @param context
     */
    public static DisplayMetrics getDisplayPixels(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    /**
     * Fetch screen height and width, to use as our max size when loading images as this activity
     * runs full screen
     *
     * @return
     */
    public static int getLongestDisplay(Context context) {
        // Fetch screen height and width, to use as our max size when loading images as this
        // activity runs full screen
        final DisplayMetrics displayMetrics = getDisplayPixels(context);
        final int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;

        // For this sample we'll use half of the longest width to resize our images. As the
        // image scaling ensures the image is larger than this, we should be left with a
        // resolution that is appropriate for both portrait and landscape. For best image quality
        // we shouldn't divide by 2, but this will use more memory and require a larger memory
        // cache.
        final int longest = (height > width ? height : width);
        return longest;
    }

    /**
     * 将毫秒转为日期-时间字符串
     *
     * @param millisec
     * @return
     */
    public static String[] convertIMDateTime(long millisec) {
        Context context = DidiApp.getInstance();
        String dateString = context.getString(R.string.unknown);
        String timeString = context.getString(R.string.unknown);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millisec);
        int month = c.get(Calendar.MONTH) + 1;
        dateString = month + context.getString(R.string.month) + c.get(Calendar.DAY_OF_MONTH) + context.getString(R.string.day);
        if (0 <= c.get(Calendar.MINUTE) && c.get(Calendar.MINUTE) < 10) {
            timeString = c.get(Calendar.HOUR_OF_DAY) + ":" + "0" + c.get(Calendar.MINUTE);
        } else {
            timeString = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
        }
        long nowTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(nowTime);
        if (c.get(Calendar.YEAR) - calendar.get(Calendar.YEAR) == 0) {
            timeString = c.get(Calendar.HOUR_OF_DAY) + ":" + "0" + c.get(Calendar.MINUTE);
        }
        String[] str = new String[] { dateString, timeString };
        return str;
    }

    public static boolean isActivityInRunningList(Context context, Class activityClazz) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = manager.getRunningTasks(Integer.MAX_VALUE);

        for (RunningTaskInfo taskInfo : list) {
            String className = taskInfo.topActivity.getClassName();
            if (className.equals(activityClazz.getName())) {
                return true;
            }
        }
        return false;
    }

    public static String getMacSerialno() {
        String cpuSerial = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address");
            if (pp == null) {
                return null;
            }
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str;) {
                str = input.readLine();
                if (str != null) {
                    cpuSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return cpuSerial;
    }

    public static String getCPUSerialno() {
        String macSerial = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
            if (pp == null) {
                return null;
            }
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str;) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return macSerial;
    }

    public static String getAndroidID() {
        return Settings.Secure.getString(BaseApplication.getAppContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static Bitmap getBitmapExpand(int rid) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inPreferredConfig = Config.ARGB_8888;

        options.inPurgeable = true;// 允许可清除

        options.inInputShareable = true;// 以上options的两个属性必须联合使用才会有效果

        InputStream is = BaseApplication.getAppContext().getResources().openRawResource(rid);
        return BitmapFactory.decodeStream(is, null, options);
    }

    /**
     * 是否在圣诞节期间
     *
     * @return
     */
    public static boolean isInChristmasDay() {
        long currentTime = System.currentTimeMillis();
        long startTime = converDateToMillisecond(CHRISTMAS_START_TIME);
        long endTime = converDateToMillisecond(CHRISTMAS_END_TIME);
        return currentTime >= startTime && currentTime <= endTime;
    }

    /**
     * 是否在湾流运营活动期间
     *
     * @return
     */
    public static boolean isInSpecailDayByServer() {
        long currentTime = System.currentTimeMillis() / 1000;
        long startTime = Preferences.getInstance().getWanLiuStarttime();
        long endTime = Preferences.getInstance().getWanLiuEndtime();

        if (currentTime == 0 || startTime == 0 || endTime == 0) {
            return false;
        }
        return currentTime >= startTime && currentTime <= endTime;
    }

    /**
     * 检测权限
     *
     * @param context
     * @param permission
     * @return isGranted 是否已授权
     */
    public static boolean checkUserPermission(String permission) {
        boolean permissionGranted = false;
        PackageManager pm = BaseApplication.getAppContext().getPackageManager();
        int checkPermission = pm.checkPermission(permission, getPackageName(BaseApplication.getAppContext()));
        if (checkPermission == PackageManager.PERMISSION_GRANTED) {// 是否已授权
            permissionGranted = true;
        } else if (checkPermission == PackageManager.PERMISSION_DENIED) {// 拒绝
            permissionGranted = false;
        }
        return permissionGranted;
    }

    public static String decode(String unicodeStr) {
        if (unicodeStr == null) {
            return null;
        }
        StringBuffer retBuf = new StringBuffer();
        int maxLoop = unicodeStr.length();
        for (int i = 0; i < maxLoop; i++) {
            if (unicodeStr.charAt(i) == '\\') {
                if ((i < maxLoop - 5) && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr.charAt(i + 1) == 'U')))
                    try {
                        retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                        i += 5;
                    } catch (NumberFormatException localNumberFormatException) {
                        retBuf.append(unicodeStr.charAt(i));
                    }
                else
                    retBuf.append(unicodeStr.charAt(i));
            } else {
                retBuf.append(unicodeStr.charAt(i));
            }
        }
        return retBuf.toString();
    }

    /** 获取URL地址，不含?部分 */
    public static String getUrlWithOutParam(String url) {
        if (TextUtil.isEmpty(url) || url.indexOf("?") == -1) {
            return url;
        }
        return url.split("\\?")[0];
    }

    public static boolean isAppFront(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = am.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            if (tasksInfo.get(0).topActivity.getClassName().contains("com.didi")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 接收来自外部的数据(如：华为智键)并进行业务派发
     *
     * @param data
     *            来自外部的数据
     * @param isSupportVoice
     *            是否支持语音叫车
     * @param listener
     *            回调
     *
     * @return boolean
     * */
    public static boolean dispatchBiz(ExternalData data, boolean isSupportVoice, ExternalData.ExternalListener listener) {
        if (listener == null) {
            return false;
        }

        listener.log();
        if (!isSupportVoice) {// 不支持语音叫车(重庆有政策限制,只允许文本叫车)则进入首页面
            listener.error();
            listener.goMainPage();
            return true;
        }
        if (data == null || data.orderType == 1 || (data.orderType == 0 && TextUtils.isEmpty(data.voicePath))) {// 音频为空进入首页面
            listener.goMainPage();
        } else {
            if ("SDK".equals(data.source)) {// 通过SDK调用直接进入登录页面
                listener.goLogin();
                return true;
            }
            if (data.orderType == 2) {// 回家
                listener.goHome();
            } else if (data.orderType == 3) {// 公司
                listener.goCommpany();
            } else {// 语音叫车确认发单页面
                listener.confirm();
            }
            LogUtil.d("------------exter voice:" + data.voicePath);

        }
        return true;
    }

    /**
     * 第三方叫车时，需要判断一下当前是否为主流程页面，
     *
     * @return boolean false 如果是主流程页面则给用户提示，忽略此次叫车 true可以叫车
     */
    public static boolean isValidCall() {
        FragmentMgr mgr = FragmentMgr.getInstance();
        TraceDebugLog.debugLog("-------------------huawei mgr1:" + mgr);
        if (mgr == null) {
            return true;
        }
        TraceDebugLog.debugLog("-------------------huawei mgr2:" + mgr.getCurrentFragment());
        if (mgr.getCurrentFragment() == null) {
            return true;
        }

        Class<? extends SlideFragment> clz = mgr.getCurrentFragment().getClass();
        TraceDebugLog.debugLog("-------------------huawei clz:" + clz);
        // 打车主流程画面
        if (TaxiVoiceOrderFragment.class.equals(clz) || TaxiWaitForArrivalFragment.class.equals(clz) || TaxiWaitForResponseFragment.class.equals(clz)) {
            return false;
        }
        // 专车主流程画面
        if (CarWaitForArrivalFragment.class.equals(clz) || CarNewWaitForResponseFragment.class.equals(clz)
                || CarPayWxAgentFragment.class.equals(clz)) {
            return false;
        }

        //快车主流程界面
        if (FlierNewWaitForResponseFragment.class.equals(clz) || FlierWaitForArrivalFragment.class.equals(clz)) {
            return false;
        }

        // 顺风车主流程页面
        String strClz = getTopActivityLableName(BaseApplication.getAppContext().getPackageName());
        if (BtsWaitingForCarActivity.class.equals(strClz) || BtsPassengerWaitingForDriverActivity.class.equals(strClz)) {
            return false;
        }
        return true;
    }

    /** 角标代码 */
    public static void Superscript(int cnt) {
        ContentResolver cResolver = BaseApplication.getAppContext().getContentResolver();
        String pkgName = BaseApplication.getAppContext().getPackageName();
        ContentValues values = new ContentValues();
        values.put("packageName", pkgName);
        values.put("className", "com.didi.frame.SplashActivity");
        values.put("unreadNumber", cnt);
        String selection = "packageName='" + pkgName + "' and className='com.didi.frame.SplashActivity'";
        try {
            Uri uri = Uri.parse("content://cn.nubia.launcher.unreadMark/unreadMark");
            int update = cResolver.update(uri, values, selection, null);
            if (update == 0) {
                cResolver.insert(uri, values);
            }
        } catch (Exception e) {
            // TODO: handle exception
            LogUtil.d("--------------------jiaobiao:" + e.getMessage());
            TraceNetLog.log("---------superscript excep:" + e.getMessage());
        }
        TraceNetLog.log("---------superscript cnt:" + cnt);
        LogUtil.d("-----------------jiaobiao===loop:" + cnt);
    }

    /**
     * 获取签名
     *
     * @param paramContext
     * @param paramString
     *            包名
     * @return
     */
    private static Signature[] getRawSignatures(Context paramContext, String paramString) {
        if ((paramString == null) || (paramString.length() == 0)) {
            LogUtil.d("getRawSignatures packageName is Null");
            return null;
        }
        PackageManager pm = paramContext.getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = pm.getPackageInfo(paramString, 64);
            if (packageInfo == null) {
                LogUtil.d("packageInfo is Null, packageName = " + paramString);
                return null;
            }
        } catch (NameNotFoundException e) {
            return null;
        }
        return packageInfo.signatures;

    }

    /**
     * 获取MD5加密后的签名信息
     *
     * @param context
     * @param paramString
     *            包名
     * @return
     */
    public static String getSign(Context context, String paramString) {
        Signature[] arrayOfSignatures = getRawSignatures(context, paramString);
        if (arrayOfSignatures == null || (arrayOfSignatures.length == 0)) {
            LogUtil.d("signs is Null");
            return null;
        }
        while (true) {
            int i = arrayOfSignatures.length;
            for (int j = 0; j < i; j++) {
                try {
                    return MD5.toMD5(arrayOfSignatures[j].toByteArray());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }


    /**
     * 开启数据采集sdk
     * @param context
     * @param phone
     */
    public static void startTraceService(String phone){
    	 TraceManager traceManager = TraceManager.getInstance(BaseApplication.getAppContext());
         traceManager.setUID(Preferences.getInstance().getPhone());//uid 暂定手机号
         traceManager.setLevel(TraceManager.LEVEL_LOW);
         traceManager.startTrace();
    }

    /**
     * 重新启动应用
     */
    public static void appRestart() {
        Context ctx = BaseApplication.getAppContext();
        if (ctx == null) return;

        PendingIntent RESTART_INTENT = PendingIntent.getActivity(ctx, 0, new Intent(ctx,
                SplashActivity.class), Intent.FLAG_ACTIVITY_NEW_TASK);
        AlarmManagerFactory.getAdapter().setRct(ctx, 600, RESTART_INTENT);

        ExitHelper.doExit();
    }

    /**
     * 保留1位小数
     */
    public static String transferToOneDicmal(float number){
    	DecimalFormat fnum = new DecimalFormat("##0.0");
        return fnum.format(number);
    }

    public static String transferTime(float time){
    	int finalTime = (int)(time/60) + 1;
        return finalTime+"";
    }

    /**
     * 保留2位小数
     */
    public static String transferToTwoDicmal(float number){
    	DecimalFormat fnum = new DecimalFormat("##0.00");
        return fnum.format(number);
    }

    /**
     * 保留2位小数
     */
    public static String transferToTwoDicmal(double number){
        DecimalFormat fnum = new DecimalFormat("##0.00");
        return fnum.format(number);
    }

    /**
     * 判断时间是否在10分钟内，大于等于10分钟则获取第三方的全局Token
     * */
    public static boolean isLimitInTenMinute(long currentTime) {
        boolean flag = false;
        long lastTime = Preferences.getInstance().getTTokenLastTime();
        long time = currentTime - lastTime;
        flag = (time >= 10 * 60 * 1000);
        return flag;
    }

    public static String[] getContacts(Context context){
        String[] result = new String[]{};
        ContentResolver content = context.getContentResolver();
        String[] PHONES_PROJECTION = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.PHOTO_ID};
        String PHONE_SORT_ORDER = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
        Cursor cur = content.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION, null, null, PHONE_SORT_ORDER);
        if (cur != null) {
            if (cur.getCount() > 0) {
                int i = 0;
                result = new String[cur.getCount()];
                while (cur.moveToNext()) {
                    String phoneNumber = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String phoneName = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    if (!TextUtil.isEmpty(phoneNumber)){
                        phoneNumber = phoneNumber.replace(" ", "");
                    }
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put(phoneName,phoneNumber.trim());
                        result[i++] = obj.toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                cur.close();
            }
        }
        return result;
    }

    /** 将long型转化为 “明天 18:00"格式 */
    public static String convertMillisToString(long time){
        String dateString = "";
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        switch (getDayDiff(System.currentTimeMillis(), time)) {
            case 0:
                dateString = ResourcesHelper.getString(R.string.time_picker_today)+ " "+sdf.format(date);
                break;
            case 1:
                dateString = ResourcesHelper.getString(R.string.time_picker_tomorrow)+ " "+sdf.format(date);
                break;
            case 2:
                dateString = ResourcesHelper.getString(R.string.time_picker_after_tomorrow)+ " "+sdf.format(date);
                break;
            default:
                Date date2 = new Date(time);
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                dateString = sdf2.format(date2);
                break;
        }
        return dateString;
    }
    /** 将long型转化为 “明天 周三 18:00"格式 或 “12月4号 周五 19:20” */
    public static String convertMillisToBtsTimeString(long timeMillis,boolean includeHourMinutes){
        String dateString = "";
        String weekString = "";
        String timeString = "";
        Date date = new Date(timeMillis);
        DateTime dateTime = new DateTime(timeMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        switch (getDayDiff(System.currentTimeMillis(), timeMillis)) {
            case 0:
                dateString = ResourcesHelper.getString(R.string.time_picker_today);
                break;
            case 1:
                dateString = ResourcesHelper.getString(R.string.time_picker_tomorrow);
                break;
            case 2:
                dateString = ResourcesHelper.getString(R.string.time_picker_after_tomorrow);
                break;
            default:
                Date date2 = new Date(timeMillis);
                weekString = getChineseWeekText(dateTime);
                SimpleDateFormat sdf2 = new SimpleDateFormat("MM月dd日");
                dateString = sdf2.format(date2) + " "+weekString;
                break;
        }

        if(includeHourMinutes){
            timeString = " "+sdf.format(date);
        }
        return dateString+timeString;
    }

    public static String getChineseWeekText(DateTime dateTime){
        int weekIndex = dateTime.getDayOfWeekCheneseDesc();
        return "周"+ convertToChineseWeekNumber(weekIndex);
    }

    public static String convertToChineseWeekNumber(int number){
        switch (number){
            case 1:
                return "一";
            case 2:
                return "二";
            case 3:
                return "三";
            case 4:
                return "四";
            case 5:
                return "五";
            case 6:
                return "六";
            case 0:
                return "日";
            default:
                return "";
        }
    }
    public static int getDayDiff(long time1, long time2) {
        Date dateA = new Date(time1);
        Date dateB = new Date(time2);
        Calendar calDateA = Calendar.getInstance();
        calDateA.setTime(dateA);
        Calendar calDateB = Calendar.getInstance();
        calDateB.setTime(dateB);

        if(calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
                && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)){
            return calDateB.get(Calendar.DAY_OF_MONTH) - calDateA.get(Calendar.DAY_OF_MONTH);
        }else if(calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR) && ((calDateB.get(Calendar.MONTH) - calDateA.get(Calendar.MONTH))==1
            || (calDateB.get(Calendar.MONTH) - calDateA.get(Calendar.MONTH))==-11)){//处理跨年情况
            return calDateB.get(Calendar.DAY_OF_MONTH)+(getCurrentMonthLastDay() - calDateA.get(Calendar.DAY_OF_MONTH));
        }
        return 0;
    }

    public static int getCurrentMonthLastDay()
    {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }


    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 判断当前手机是否有ROOT权限
     *
     * @return
     */
    public static boolean isRoot() {
        boolean bool = false;
        try {
            if ((!new File("/system/bin/su").exists())
                    && (!new File("/system/xbin/su").exists())) {
                bool = false;
            } else {
                bool = true;
            }
        } catch (Exception e) {
        }
        return bool;
    }

    /**
     * 判断当前手机是否允许在模拟器上面
     *
     * @return
     */
    public static boolean isRunningInEmualtor() {
        boolean qemuKernel = false;
        boolean hasPhoneNumber = false;
        boolean buildVersion = false;
        try {
            TelephonyManager tm = (TelephonyManager) BaseApplication
                    .getAppContext()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (imei != null && imei.equals("000000000000000")) {
                hasPhoneNumber = true;
            } else {
                hasPhoneNumber = false;
            }

            // getprop ro.kernel.qemu == 1 在模拟器
            Method localMethod = Class.forName("android.os.SystemProperties").getDeclaredMethod("get", new Class[]{String.class});
            localMethod.setAccessible(true);
            qemuKernel = "1".equals(localMethod.invoke(null, new Object[]{"ro.kernel.qemu"}));
            LogUtil.d(
                    "morning",
                    "qemuKernel is "+qemuKernel+"!!!"+imei+"！！！"+localMethod.invoke(null, new Object[]{"ro.kernel.qemu"}).toString());

            buildVersion = Build.MODEL.equalsIgnoreCase("sdk")
                    || Build.MODEL.equalsIgnoreCase("google_sdk")
                    || Build.MODEL.contains("Droid4X");
            LogUtil.d(
                    "morning",
                    "buildVersion is "+buildVersion+"Build.MODEL is "+ Build.MODEL);


        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("morning", "run failed" + e.getMessage());
        } finally {
            try {

            } catch (Exception e) {

            }
            LogUtil.d("morning", "run finally");
            LogUtil.d("morning", "检测到模拟器:" + "qemuKernel is " + qemuKernel
                    + "modelProduct is " + buildVersion + "hasPhoneNumber is " + hasPhoneNumber);
        }
        return qemuKernel || buildVersion  || hasPhoneNumber;
    }

    /**
     * 计算设备UUID
     * @param context
     * @return
     */
    public static String getMyUUID(Context context) {
        String uniqueId = "unknown";
        try {
            final TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String tmDevice = "" + tm.getDeviceId();
            String tmSerial = "" + tm.getSimSerialNumber();
            String androidId = ""
                    + android.provider.Settings.Secure.getString(
                    context.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
            UUID deviceUuid = new UUID(androidId.hashCode(),
                    ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            uniqueId = deviceUuid.toString();
        } catch (Exception e) {
        }
        return uniqueId;
    }


    /**
     * 扩大View的触摸和点击响应范围,最大不超过其父View范围
     *
     * @param view
     * @param top
     * @param bottom
     * @param left
     * @param right
     */
    public static void expandViewTouchDelegate(final View view, final int top,
                                               final int bottom, final int left, final int right) {

        ((View) view.getParent()).post(new Runnable() {
            @Override
            public void run() {
                Rect bounds = new Rect();
                view.setEnabled(true);
                view.getHitRect(bounds);

                bounds.top -= top;
                bounds.bottom += bottom;
                bounds.left -= left;
                bounds.right += right;

                TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

                if (View.class.isInstance(view.getParent())) {
                    ((View) view.getParent()).setTouchDelegate(touchDelegate);
                }
            }
        });
    }

    /**判断价格是否< 10*/
    public static boolean judgePriceSmallTen(String data) {
        if (data.contains(".")) {
            data = data.substring(0, data.indexOf("."));
        }
        String reg = "[^0-9]";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(data);
        int price = Integer.parseInt(matcher.replaceAll(""));
        if (price < 10) {
            return true;
        }
        return false;
    }

    /**根据小费大小动态生成价格*/
    public static String dynamicAddPrice(String data, int carTipPrice) {
        if (carTipPrice == 0) {
            return data;
        }
        String original = data;

        if (data.contains(".")) {
            data = data.substring(0, data.indexOf("."));
        }
        String reg = "[^0-9]";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(data);
        int price = Integer.parseInt(matcher.replaceAll("")) + carTipPrice ;

        LogUtil.d("price " + price + " original " + original + " tips " + carTipPrice);
        String replace = "";
        if (!original.contains(".")) {
            replace = "((\\d+))";
        } else {
            replace = "((\\d+).(\\d+))";
        }
        pattern = Pattern.compile(replace);
        matcher = pattern.matcher(original);

        try {
            LogUtil.d("data " + original + " matcher.find " + matcher.find());
            LogUtil.d("group " + matcher.group(2) + " original " +original + " price " +price);
            return original.replace(matcher.group(2), "" + price);
        } catch (IllegalStateException e) {
            return data;
        }
    }
}
