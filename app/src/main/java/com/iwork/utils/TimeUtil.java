package com.iwork.utils;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    /**
     * 将千毫秒格式化为yyyy-MM-dd HH:mm:ss格式
     */
    public static String formatDate(long millis) {
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
    /**
     * 将千毫秒格式化为yyyy-MM-dd HH:mm:ss格式
     */
    public static String formatDates(long millis) {
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(date);
    }

    /**
     * 将千毫秒格式化为yyyy-MM-dd格式
     */
    public static String formatDateInSimple(long millis) {
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 将千毫秒格式化为yyyy-MM-dd 周E 格式
     */
//    public static String formatDateInWithWeek(long millis) {
//        Date date = new Date(millis);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd E");
//        return sdf.format(date);
//    }

    //将毫秒格式化为小时和分钟HH:mm
    public static String formateTime(long millis) {
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("aa HH:mm");
        return sdf.format(date);
    }

    //将毫秒格式化为小时和分钟HH:mm
    public static String formateTime24(long millis) {
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(date);
    }

    //将毫秒格式化为 MM月dd日 HH:mm
    public static String formateTimeMMddHHmm(long millis) {
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
        return sdf.format(date);
    }

    /**
     * 是否超过间隔时间
     */
    public static boolean isOutOfDate(long orderTime, int offset) {
        long interval = offset * 60 * 60 * 1000;
        long currentTime = System.currentTimeMillis();
        if ((currentTime - orderTime) > interval) {
            return true;
        }
        return false;
    }

    /**
     * 是否超过间隔时间
     */
    public static boolean isOutOfDate(String orderTime, int offset) {

        long interval = offset * 60 * 60 * 1000;
        long currentTime = System.currentTimeMillis();
        long time = Long.valueOf(orderTime);
        if ((currentTime - time) > interval) {
            return true;
        }
        return false;
    }

    public static Date parseString(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date result = sdf.parse(date);
            return result;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static final int ONE_HOUR = 60; // 1小时，按分钟记
    private static final int ONE_DAY = 24 * ONE_HOUR; // 1天，按分钟记


}
