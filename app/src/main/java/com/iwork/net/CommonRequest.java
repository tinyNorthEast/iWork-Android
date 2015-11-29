package com.iwork.net;

import com.iwork.utils.Constant;

import retrofit.BaseUrl;
import retrofit.Retrofit;

/**
 * Created by JianTao on 15/11/28.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class CommonRequest {

    public static final String BASE_PARAM = "/version/{version}/";

    public static Retrofit getBaseRetrfit(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.BASE_URL).build();
        return retrofit;
    }

}
