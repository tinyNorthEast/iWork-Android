package com.iwork.utils;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by JianTao on 15/11/29.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class FileConfig {
    private static String CACHE_ROOT = "";

    public static String getSDPath() {
        return Environment.getExternalStorageDirectory().getPath() + "/";
    }

    public static final String getCacheRoot(){
        if(TextUtils.isEmpty(CACHE_ROOT)){
            CACHE_ROOT = getSDPath() + "iWork/";
            File rootDir = new File(CACHE_ROOT);
            if(!rootDir.exists()){
                rootDir.mkdirs();
            }
        }
        return CACHE_ROOT;
    }


    public static final String getPhotoCachePath(){
        String path = getCacheRoot() + "photo/";
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        return path;
    }

    public static final File getPhotoOutputFile(){
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(getPhotoCachePath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");

        return mediaFile;
    }

}
