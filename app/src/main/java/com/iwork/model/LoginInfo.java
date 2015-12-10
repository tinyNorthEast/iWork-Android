package com.iwork.model;

/**
 * Created by JianTao on 15/11/29.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class LoginInfo {
    /**
     * infoCode : 10004
     * data : 该账号以及存在！
     */

    private int infoCode;
    private Data data;
    /**
     * message : 登陆成功
     */

    private String message;

    public void setInfoCode(int infoCode) {
        this.infoCode = infoCode;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public int getInfoCode() {
        return infoCode;
    }

    public Data getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
