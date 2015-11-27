package com.iwork.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;


import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ZhangJianTao on 2015/5/22.
 */
public class Utils {
    private static long lastClickTime;
    private static final long delayTime = 800L;

    /**
     * 判断SDCard是否存在
     */
    public static boolean haveSDCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * sd卡是否可用
     */
    public static boolean isSDCardAvailble() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String getSOSOLocationPath(String path) {
        return getSDCardPath() + path + File.separator;
    }

    /**
     * 获取SD卡路径，返回格式 path+"/"
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    /**
     * SDCard是否有剩余空间
     */
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

    /**
     * 获取屏幕分辨率
     *
     * @param context
     */
    public static DisplayMetrics getDisplayPixels(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    /**
     * 获取最大像素
     *
     * @param context
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

    public static String getPixels(Context context) {
        final DisplayMetrics displayMetrics = getDisplayPixels(context);
        final int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;
        return width + "*" + height;
    }

    /**
     * 获取当前版本名称
     *
     * @param context     当前app上下文
     * @param packageName 包名
     * @return 版本名称
     */
    public static String getCurrentVersionName(Context context, String packageName) {
        try {
            PackageInfo pinfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_CONFIGURATIONS);
            return pinfo.versionName;
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 获取当前版本号
     *
     * @param context 当前app上下文
     * @return 版本号
     */
    public static int getCurrentVersionCode(Context context) {
        try {
            PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pinfo.versionCode;
        } catch (Exception e) {
        }
        return 1;
    }

    /**
     * 获取已安装的应用列表　 *
     */
    public static String getAllInstalledPkg(Context context) {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager pm = context.getPackageManager();
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

    /**
     * 获取App安装包路径
     * 说明:获取App程序安装文件路径同时还要将程序文件复制到SDCard目录下,
     * 是因为接下来的安装如果是打补丁的话,需要用这个文件与补丁包进行文件合并.
     *
     * @param context 当前application上下文对象
     * @return 安装包路径
     */
    public static String getInstallApk(Context context, String path) {
        String apkFile = null;
        String apkName = "";
        try {
            apkFile = context.getPackageManager().getApplicationInfo(context.getApplicationContext().getPackageName(), 0).sourceDir;
            //apkFile="/data/app/com.didi.es.psngr-1/base.apk"
            apkName = getFileName(apkFile);
            // FileUtil.delete(Utils.getSDCardPath() + apkName);
            // 后来修改的，之前删除的路径好像有问题，应该是sd卡加上didies的完整路径。 by yangxiaomin
            String sdCardPath = Utils.getSDCardPath() + path + File.separator + apkName;
            FileUtil.delete(sdCardPath);
            FileInputStream fis = null;
            FileOutputStream fos = null;
            try {
                // SDCard创建文件
                File out = new File(sdCardPath);
                out.createNewFile();
                // 将获取到的APK复制到SDcard
                File in = new File(apkFile);
                fis = new FileInputStream(in);
                fos = new FileOutputStream(out);
                int count;
                byte[] buffer = new byte[128 * 1024];
                while ((count = fis.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                    if (fos != null) {
                        fos.flush();
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            return apkFile;
        }
        // 返回完整路径吧，不返回文件名
        return apkFile;
    }

    /**
     * 安装apk
     *
     * @param context
     * @param apkPath 安装包的绝对路径
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
     * @param activity    启动自哪个Activity
     * @param apkPath     安装包绝对路径
     * @param requestCode 请求码
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
            KLog.e("apkPath=" + apkPath + ",errMsg=" + e.getMessage());

        }
    }

    /**
     * 获取指定文件路径的文件名
     *
     * @param path 文件路径
     * @return 完整文件名 如: xx.jpg
     */
    public static String getFileName(String path) {
        if (TextUtil.isEmpty(path))
            return "";
        return path.substring(path.lastIndexOf("/") + 1);
    }

    /**
     * 获得网络类型，如果是wifi情况下，netWorkTypes[0]为空字符串，netWorkTypes[1] 为WIFI;
     * 如果是非wifi下，netWorkTypes[0]为cmnet, netWorkTypes[1]为HSDPA
     */
    public static String[] getNetWorkType(Context context) {
        String[] netWorkTypes = new String[2];
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
     */
    public static String getNetworkType(Context context) {
        String name = "UNKNOWN";

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null) {
            if (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) {
                return "WIFI";
            }
        }

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
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

    public static String getAndroidID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
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

            for (; null != str; ) {
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

    /**
     * 简单验证手机号是否合法
     */
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
     * 获得手机的型号
     */
    public static String getModel() {
        String temp = Build.MODEL;
        if (TextUtil.isEmpty(temp)) {
            return "";
        } else {
            return temp;
        }

    }

    /**
     * 获取软件的PackageName
     */
    public static String getPackageName(Context context) {

        String pkg = context.getApplicationContext().getPackageName();
        return pkg;
    }

    /**
     * 获取当前版本号
     */
    public static String getCurrentVersion(Context context) {
        String pkgName = getPackageName(context);
        try {
            PackageInfo pinfo = context.getPackageManager().getPackageInfo(pkgName, PackageManager.GET_CONFIGURATIONS);
            return pinfo.versionName;
        } catch (Exception e) {
        }
        return "";
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

            for (; null != str; ) {
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

    /**
     * 得到手机的IMEI号
     *
     * @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephonyMgr.getDeviceId() == null) {
            return "";
        } else
            return mTelephonyMgr.getDeviceId();
    }

    /**
     * 正则表达式验证是否為數字
     *
     * @param str 验证字符串
     * @return
     */
    public static boolean isNum(String str) {

        if (TextUtils.isEmpty(str))
            return false;

        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNumatcher = pattern.matcher(str);
        return isNumatcher.matches();
    }

    public static boolean isPhone(String phone) {
        return !(TextUtil.isEmpty(phone) || !phone.startsWith("1"));
    }

    /**
     * 正则表达式验证判定是否为数字或字母
     *
     * @param str 验证字符串
     * @return
     */
    public static boolean isNumOrLetter(String str) {

        if (TextUtils.isEmpty(str))
            return false;

        Pattern pattern = Pattern.compile("[A-Za-z0-9]*");
        Matcher isNumatcher = pattern.matcher(str);
        return isNumatcher.matches();
    }

    public static boolean isTextEmpty(String text) {
        if (TextUtil.isEmpty(text))
            return true;
        return "null".equalsIgnoreCase(text);
    }

    public static void showInputMethod(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideInputMethod(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 设置resId对应的TextView 字体为粗体
     *
     * @param resId
     * @param view  用view.findViewById(resId)来查找TextView
     * @author liuwei
     */
    public static void setBoldFont(int resId, View view) {
        if (view == null || resId == 0)
            return;
        TextView tv = (TextView) view.findViewById(resId);
        if (tv == null)
            return;
        tv.getPaint().setFakeBoldText(true);
    }

    /**
     * 设置resId对应的TextView 字体为粗体
     *
     * @param view 需要加粗的TextView
     * @author liuwei
     */
    public static void setBoldFont(TextView view) {
        if (view == null)
            return;
        view.getPaint().setFakeBoldText(true);
    }

    /**
     * 调用拨打电话
     */
    public static void actionCall(Context context, String phone) {
        if (TextUtils.isEmpty(phone))
            return;
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }

    /**
     * unix时间戳转换为dateFormat
     *
     * @param beginDate
     * @return
     */
    public static String timestampToDate(String beginDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
        String sd = sdf.format(new Date(Long.parseLong(beginDate)));
        return sd;
    }

    /**
     * 自定义格式时间戳转换
     *
     * @param beginDate
     * @return
     */
    public static String timestampToDate(String beginDate, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String sd = sdf.format(new Date(Long.parseLong(beginDate)));
        return sd;
    }

    /**
     * 按指定格式获取当前日期
     *
     * @param format 指定的日期时间格式
     * @return
     */
    public static String getCurrentDate(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateStr = sdf.format(new Date());
        return dateStr;
    }

    /**
     * 将字符串转为时间戳
     *
     * @param user_time
     * @return
     */
    public static String dateToTimestamp(String user_time) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return re_time;
    }

    /**
     * 显示分钟和秒数 格式：00:00
     */
    public static String getSecond2Min(int second) {
        int minute = 60;
        if (second < 60) {
            String sec = String.format("%1$02d", second);
            return "00:" + sec;
        }
        int mins = second / minute;
        int secs = second % minute;
        if (mins >= 1) {
            return String.format("%1$02d", mins) + ":" + String.format("%1$02d", secs);
        }
        return second + "";
    }

    /**
     * 网络是否可用
     *
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo network = cm.getActiveNetworkInfo();
        if (network != null) {
            return network.isConnected() || network.isAvailable();
        } else {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            NetworkInfo.State wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            return wifiManager.isWifiEnabled() && wifiState.equals(NetworkInfo.State.CONNECTED);
        }
    }

    /**
     * 检测权限
     *
     * @param context
     * @param permission
     * @return isGranted 是否已授权
     */
    public static boolean checkUserPermission(Context context, String permission) {
        boolean permissionGranted = false;
        PackageManager pm = context.getPackageManager();
        int checkPermission = pm.checkPermission(permission, getPackageName(context));
        if (checkPermission == PackageManager.PERMISSION_GRANTED) {// 是否已授权
            permissionGranted = true;
        } else if (checkPermission == PackageManager.PERMISSION_DENIED) {// 拒绝
            permissionGranted = false;
        }
        return permissionGranted;
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


    /**
     * 根据JsonArry字符串转换为Java String Array
     *
     * @param jsonStr
     * @return
     */
    public static String[] ToStringArrayWithJsonString(String jsonStr) {
        String[] arrs = new String[]{};
        if (!TextUtil.isEmpty(jsonStr)) {
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                arrs = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    arrs[i] = jsonArray.getString(i);
                }
                return arrs;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrs;
    }

    /**
     * 获取Url的Host部分
     *
     * @param url
     * @return
     */
    public static String getUrlHost(String url) {
        String host = "";
        if (!TextUtils.isEmpty(url)) {
            try {
                host = Uri.parse(url).getHost();

            } catch (Exception ex) {

            }
        }
        return host;
    }
}
