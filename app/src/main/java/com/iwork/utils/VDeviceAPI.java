package com.iwork.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class VDeviceAPI {

    /** 获取IMEI号 */
    public static String getImei(Context c) {
        TelephonyManager manager = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
        String strImei = "";
        if (manager != null) {
            strImei = manager.getDeviceId();
        }

        if (strImei == null || strImei.length() == 0 || strImei.equals("null")) {
            strImei = "35"
                    + Build.BOARD.length() % 10
                    + Build.BRAND.length() % 10
                    + Build.CPU_ABI.length() % 10
                    + Build.DEVICE.length() % 10
                    + Build.DISPLAY.length() % 10
                    + Build.HOST.length() % 10
                    + Build.ID.length() % 10
                    + Build.MANUFACTURER.length() % 10
                    + Build.MODEL.length() % 10
                    + Build.PRODUCT.length() % 10
                    + Build.TAGS.length() % 10
                    + Build.TYPE.length() % 10
                    + Build.USER.length() % 10;
        }
        
        String last = getLastModifiedMD5Str();
        return strImei + last;
    }
    
    /**
     * 获取当前时间
     * 
     * @return  返回当前的系统时间
     */
    public static String currentTime() {
        long time = System.currentTimeMillis();
        return time + "";
    }
    
    /** 获取系统文件的属性字符串 */
    private static final String getLastModifiedMD5Str() {
        String path = "/system/build.prop";
        File f = new File(path);
        Long modified = f.lastModified();
        
        char[] data = md5(modified.toString());

        if (data == null) {
            return null;
        } else {
            return new String(data);
        }
    }
    
    /** 生成md5串 */
    private static final char[] md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            int count = (messageDigest.length << 1);
            char[] data = new char[count];
            int tmp;
            byte idx = 0;
            for (int i = 0; i < count; i += 2) {
                tmp = messageDigest[idx] & 0xFF;
                idx++;
                if (tmp < 16) {
                    data[i] = '0';
                    data[i + 1] = getHexchar(tmp);
                } else {
                    data[i] = getHexchar(tmp >> 4);
                    data[i + 1] = getHexchar(tmp & 0xF);
                }
            }
            return data;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /** int转char */
    private static final char getHexchar(int value) {
        if (value < 10) {
            return (char) ('0' + value);
        } else {
            return (char) ('A' + value - 10);
        }
    }

    /**
     * 获取设备是否是小米手机
     * @param context
     * @return
     */
    public static boolean isMIPhone(Context context) {
        String manufactrue = android.os.Build.MANUFACTURER;
        String model = android.os.Build.MODEL;
        if ("Xiaomi".equalsIgnoreCase(manufactrue)) {
            return true;
        } else {
            if (model.startsWith("MI") && (model.compareTo("MI 2") >= 0)) {
                return true;
            }
        }
        return false;
    }
}
