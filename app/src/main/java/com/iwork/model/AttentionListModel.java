package com.iwork.model;

import java.util.List;

/**
 * Created by JianTao on 15/12/30.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class AttentionListModel {

    /**
     * infoCode : 0
     * message : 获取成功
     * data : [{"objId":7,"attention_from_id":100022,"attention_to_id":100001,"create_time":1451373743000,"status":1,"zh_name":"廖端永","pic":""}]
     */

    private int infoCode;
    private String message;
    /**
     * objId : 7
     * attention_from_id : 100022
     * attention_to_id : 100001
     * create_time : 1451373743000
     * status : 1
     * zh_name : 廖端永
     * pic :
     */

    private List<Attention> data;

    public void setInfoCode(int infoCode) {
        this.infoCode = infoCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(List<Attention> data) {
        this.data = data;
    }

    public int getInfoCode() {
        return infoCode;
    }

    public String getMessage() {
        return message;
    }

    public List<Attention> getData() {
        return data;
    }

    public static class Attention {
        private int objId;
        private int attention_from_id;
        private int attention_to_id;
        private long create_time;
        private int status;
        private String zh_name;
        private String pic;

        public void setObjId(int objId) {
            this.objId = objId;
        }

        public void setAttention_from_id(int attention_from_id) {
            this.attention_from_id = attention_from_id;
        }

        public void setAttention_to_id(int attention_to_id) {
            this.attention_to_id = attention_to_id;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setZh_name(String zh_name) {
            this.zh_name = zh_name;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public int getObjId() {
            return objId;
        }

        public int getAttention_from_id() {
            return attention_from_id;
        }

        public int getAttention_to_id() {
            return attention_to_id;
        }

        public long getCreate_time() {
            return create_time;
        }

        public int getStatus() {
            return status;
        }

        public String getZh_name() {
            return zh_name;
        }

        public String getPic() {
            return pic;
        }
    }
}
