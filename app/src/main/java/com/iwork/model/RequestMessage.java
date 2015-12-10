package com.iwork.model;

/**
 * Created by JianTao on 15/12/10.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class RequestMessage {

    /**
     * infoCode : 20000
     * message : 该电话已被使用！
     */

    private int infoCode;
    private String message;

    public void setInfoCode(int infoCode) {
        this.infoCode = infoCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getInfoCode() {
        return infoCode;
    }

    public String getMessage() {
        return message;
    }
}
