package com.iwork.net;

import com.iwork.model.Demo;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.okhttp.request.OkHttpRequest;
import com.iwork.utils.Constant;
import com.iwork.utils.TextUtil;
import com.iwork.utils.Utils;
import com.squareup.okhttp.OkHttpClient;

import java.util.HashMap;
import java.util.Map;

import retrofit.BaseUrl;
import retrofit.Retrofit;

/**
 * Created by JianTao on 15/11/28.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class CommonRequest {

    public static final String BASE_PARAM = "/version/{version}/";

    //    public static Retrofit getBaseRetrfit(){
//        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.BASE_URL).build();
//        return retrofit;
//    }
    public static void register(String phone, String password, String zh_name, String mail,
                                int experience, String position, int role_code,
                                String invate_code, String pic, ResultCallback<Demo> callback) {
        Map<String, String> params = new HashMap<>();
        params.put(ServerParam.PHONE, phone);
        params.put(ServerParam.PASSWORD, password);//MD5
        params.put(ServerParam.ZH_NAME, zh_name);
        params.put(ServerParam.MAIL, mail);
        params.put(ServerParam.EXPERIENCE, experience + "");
        params.put(ServerParam.POSITION, position);
        params.put(ServerParam.ROLE_CODE, role_code + "");
        if (!TextUtil.isEmpty(invate_code))
            params.put(ServerParam.INVATE_CODE, invate_code);
        if (!TextUtil.isEmpty(pic))
            params.put(ServerParam.PIC, pic);
        String url = createUrl("/api/v1/user/regist.action", params);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    protected static String createUrl(String path, Map<String, String> params) {
        params.put(ServerParam.CLIENT, Constant.CLIEN);
        params.put(ServerParam.EQ_NUM, Constant.ANDROID_ID);
        params.put(ServerParam.VERSION, Utils.getCurrentVersion());
        return Constant.BASE_URL + path;
    }
}
