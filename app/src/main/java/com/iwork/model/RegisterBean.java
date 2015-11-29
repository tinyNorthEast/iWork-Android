package com.iwork.model;

/**
 * Created by JianTao on 15/11/28.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class RegisterBean {

    /**
     * infoCode : 0
     * data : {"zh_name":"测试人员","token":"e74a3996ebf1071b09f3d86776d21f20"}
     */

    private int infoCode;
    /**
     * zh_name : 测试人员
     * token : e74a3996ebf1071b09f3d86776d21f20
     */

    private DataEntity data;

    public void setInfoCode(int infoCode) {
        this.infoCode = infoCode;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public int getInfoCode() {
        return infoCode;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        private String zh_name;
        private String token;

        public void setZh_name(String zh_name) {
            this.zh_name = zh_name;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getZh_name() {
            return zh_name;
        }

        public String getToken() {
            return token;
        }
    }
}
