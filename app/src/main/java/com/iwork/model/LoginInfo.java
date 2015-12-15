package com.iwork.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by JianTao on 15/11/29.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class LoginInfo {

    /**
     * infoCode : 0
     * message : 登陆成功
     * data : {"zh_name":"就爱你他","role_code":100,"token":"036d91ce4ba76d8699ce99799ac49027"}
     */

    private int infoCode;
    private String message;
    /**
     * zh_name : 就爱你他
     * role_code : 100
     * token : 036d91ce4ba76d8699ce99799ac49027
     */

    @SerializedName("data")
    private DataEntity login_data;

    public void setInfoCode(int infoCode) {
        this.infoCode = infoCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setLogin_data(DataEntity login_data) {
        this.login_data = login_data;
    }

    public int getInfoCode() {
        return infoCode;
    }

    public String getMessage() {
        return message;
    }

    public DataEntity getLogin_data() {
        return login_data;
    }

    public static class DataEntity {
        private String zh_name;
        private int role_code;
        private String token;

        public void setZh_name(String zh_name) {
            this.zh_name = zh_name;
        }

        public void setRole_code(int role_code) {
            this.role_code = role_code;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getZh_name() {
            return zh_name;
        }

        public int getRole_code() {
            return role_code;
        }

        public String getToken() {
            return token;
        }
    }
}
