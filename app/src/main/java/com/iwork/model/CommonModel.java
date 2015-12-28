package com.iwork.model;

/**
 * Created by JianTao on 15/12/28.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class CommonModel {

    /**
     * infoCode : 0
     * message : 操作成功
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
