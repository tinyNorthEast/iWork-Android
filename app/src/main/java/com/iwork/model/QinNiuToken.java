package com.iwork.model;

/**
 * Created by JianTao on 15/12/10.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class QinNiuToken {

    /**
     * infoCode : 0
     * message : token获取成功！
     * data : JldPijz7Y4EdgP4VkpYh9G2rLnsS8mNJRzBe5RSZ:liMSfXiadmWhT1bC3xHrm37-CdU=:eyJzY29wZSI6Iml3b3JrIiwicmV0dXJuQm9keSI6IntcImtleVwiOiAkKGtleSksIFwiaGFzaFwiOiAkKGV0YWcpLCBcIndpZHRoXCI6ICQoaW1hZ2VJbmZvLndpZHRoKSwgXCJoZWlnaHRcIjogJChpbWFnZUluZm8uaGVpZ2h0KX0iLCJkZWFkbGluZSI6MTQ0OTc2MjI1Nn0=
     */

    private int infoCode;
    private String message;
    private String data;

    public void setInfoCode(int infoCode) {
        this.infoCode = infoCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getInfoCode() {
        return infoCode;
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }
}
