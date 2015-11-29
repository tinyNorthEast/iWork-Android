package com.iwork.net;

import com.iwork.model.Demo;
import com.iwork.model.RegisterBean;
import com.iwork.utils.NetConstant;
import com.socks.library.KLog;

import java.io.IOException;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.Field;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by JianTao on 15/11/28.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class RegisterRequest {
    public interface Register {
        @FormUrlEncoded
        @POST("/api/v1/user/regist.action/")
        Call<String> register(@Field("phone") String phone,
                              @Field("password") String password,
                              @Field("zh_name") String zh_name,
                              @Field("mail") String mail, @Field("positon") String position,
                              @Field("experience") int experience,
                              @Field("role_code") int role_code,
                              @Field("client") int client, @Field("eq_num") String eq_num);

        @FormUrlEncoded
        @POST("/api/v1/user/regist.action")
        Call<RegisterBean> register(@Field("phone") String phone,
                                    @Field("password") String password,
                                    @Field("zh_name") String zh_name,
                                    @Field("mail") String mail, @Field("positon") String position,
                                    @Field("experience") int experience,
                                    @Field("role_code") int role_code,
                                    @Field("client") int client, @Field("eq_num") String eq_num,
                                    @Field("invate_code") String invate_code,
                                    @Field("pic") String pic);

    }

    public static void getRegister(Callback<String> callback) throws IOException {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(NetConstant.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        Register registe = retrofit.create(Register.class);
        Call<String> call = registe.register("13028000116", "123456", "张三", "a_tao123@163.com", "测试", 1, 100, 1, "sdlfjasldfjlsdf");
        call.enqueue(callback);
//        RegisterBean registerBean =call.execute().body();
//        System.out.print("---"+registerBean.toString());
    }
}
