package com.iwork.utils;

import com.iwork.Base.BaseApplication;

/**
 * Created by JianTao on 15/11/25.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class Constant {
    public static final String SMSSDKKEY = "ca416209842a";
    public static final String SMSSDKSECRET = "b38c35b3c785b7f1d74f182cee07146d";
    public static final String OBJID = "objId";
    public static final String CLIEN = "1";
    public static final String INDUSTRYID="industryid";
    public static final String CITY = "cityid";
    public static final int REQUEST_CODE_FOR_CITY = 1006;
    public static final String ISFROMSET = "isfromSet";
    public static final String MESSAGETYPE = "messagetype";
    public static final String COMMENTID="commentid";
    public static final String SEARCHTYPE="searchType";
    public static final int REFESHTIME =3000;
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
