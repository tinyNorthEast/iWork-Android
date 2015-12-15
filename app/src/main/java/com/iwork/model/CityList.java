package com.iwork.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by JianTao on 15/12/14.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class CityList {
    /**
     * infoCode : 0
     * message : 城市获取成功！
     * data : [{"id":4,"areaName":"浙江","areaCode":"10004","areaIsLeaf":true,"areaLevel":1,"areaSort":"Z","createTime":1449985702000},{"id":3,"areaName":"广州","areaCode":"10003","areaIsLeaf":true,"areaLevel":1,"areaSort":"G","createTime":1449985688000},{"id":2,"areaName":"上海","areaCode":"10002","areaIsLeaf":true,"areaLevel":1,"areaSort":"S","createTime":1449985669000},{"id":1,"areaName":"北京","areaCode":"10001","areaIsLeaf":true,"areaLevel":1,"areaSort":"B","createTime":1449985652000}]
     */

    private int infoCode;
    private String message;
    /**
     * id : 4
     * areaName : 浙江
     * areaCode : 10004
     * areaIsLeaf : true
     * areaLevel : 1
     * areaSort : Z
     * createTime : 1449985702000
     */

    @SerializedName("data")
    private List<City> CityLists;

    public void setInfoCode(int infoCode) {
        this.infoCode = infoCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCityLists(List<City> CityLists) {
        this.CityLists = CityLists;
    }

    public int getInfoCode() {
        return infoCode;
    }

    public String getMessage() {
        return message;
    }

    public List<City> getCityLists() {
        return CityLists;
    }

    public static class City {
        private int id;
        private String areaName;
        private String areaCode;
        private boolean areaIsLeaf;
        private int areaLevel;
        private String areaSort;
        private long createTime;

        public void setId(int id) {
            this.id = id;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public void setAreaIsLeaf(boolean areaIsLeaf) {
            this.areaIsLeaf = areaIsLeaf;
        }

        public void setAreaLevel(int areaLevel) {
            this.areaLevel = areaLevel;
        }

        public void setAreaSort(String areaSort) {
            this.areaSort = areaSort;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getId() {
            return id;
        }

        public String getAreaName() {
            return areaName;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public boolean isAreaIsLeaf() {
            return areaIsLeaf;
        }

        public int getAreaLevel() {
            return areaLevel;
        }

        public String getAreaSort() {
            return areaSort;
        }

        public long getCreateTime() {
            return createTime;
        }
    }
}
