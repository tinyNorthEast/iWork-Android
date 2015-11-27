package com.iwork.utils;

import com.iwork.Base.BaseApplication;

/**
 * Created by JianTao on 15/11/25.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class Constant {
    public static final String SMSSDKKEY = "c8d1e269ef14";
    public static final String SMSSDKSECRET = "68f4803cba8331f482b28399a202b9ca";

    /**
     * SDCARD文件目录
     */
    public static final String SDCARD_FILE_DIR = "iWork";
    public static final String SDCARD_LOCATE_DIR = "iworklocate";
    /**
     * 密码最大长度
     */
    public static final int PWD_MAX_LENGTH = 16;
    /**
     * 密码最小长度
     */
    public static final int PWD_MIN_LENGTH = 6;

    public static String CPU_ID = Utils.getCPUSerialno();
    public static String ANDROID_ID = Utils.getAndroidID(BaseApplication.getAppContext());
    public static String MD5_SERIALNO = MD5
            .toMD5("1_" + ANDROID_ID + "2_" + Utils.getIMEI(BaseApplication.getAppContext()) + "3_" + CPU_ID);
}
