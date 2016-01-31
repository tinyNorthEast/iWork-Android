package com.iwork.Base;

import com.google.gson.Gson;

/**
 * Created by JianTao on 16/1/31.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class AppService {
    private static Gson sGson;
    private static AppService instance = new AppService();

    public AppService() {

    }

    public void initService() {
        sGson = new Gson();
    }

    public static Gson getsGson() {
        return sGson;
    }

    public static AppService getInstance() {
        return instance;
    }

}
