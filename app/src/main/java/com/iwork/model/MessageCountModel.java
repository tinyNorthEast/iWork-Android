package com.iwork.model;

import java.util.List;

/**
 * Created by JianTao on 15/12/30.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class MessageCountModel {

    /**
     * infoCode : 0
     * message : 获取通知成功！
     * data : [{"typeCount":2,"n_type":1}]
     */

    private int infoCode;
    private String message;
    /**
     * typeCount : 2
     * n_type : 1
     */

    private List<MessageCount> data;

    public void setInfoCode(int infoCode) {
        this.infoCode = infoCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(List<MessageCount> data) {
        this.data = data;
    }

    public int getInfoCode() {
        return infoCode;
    }

    public String getMessage() {
        return message;
    }

    public List<MessageCount> getData() {
        return data;
    }

    public static class MessageCount {
        private int typeCount;
        private int n_type;

        public void setTypeCount(int typeCount) {
            this.typeCount = typeCount;
        }

        public void setN_type(int n_type) {
            this.n_type = n_type;
        }

        public int getTypeCount() {
            return typeCount;
        }

        public int getN_type() {
            return n_type;
        }
    }
}
