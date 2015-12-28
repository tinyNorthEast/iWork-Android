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
     * data : [{"areaName":"北京","areaCode":"1000","areaSort":"B"},{"areaName":"成都","areaCode":"6510","areaSort":"C"},{"areaName":"广州","areaCode":"5810","areaSort":"G"},{"areaName":"上海","areaCode":"2900","areaSort":"S"},{"areaName":"苏州","areaCode":"3050","areaSort":"S"},{"areaName":"深圳","areaCode":"5840","areaSort":"Z"}]
     */

    private int infoCode;
    private String message;
    /**
     * areaName : 北京
     * areaCode : 1000
     * areaSort : B
     */

    @SerializedName("data")
    private List<City> citys;

    public void setInfoCode(int infoCode) {
        this.infoCode = infoCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCitys(List<City> citys) {
        this.citys = citys;
    }

    public int getInfoCode() {
        return infoCode;
    }

    public String getMessage() {
        return message;
    }

    public List<City> getCitys() {
        return citys;
    }

    public static class City {
        private String areaName;
        private int areaCode;
        private String areaSort;

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public void setAreaCode(int areaCode) {
            this.areaCode = areaCode;
        }

        public void setAreaSort(String areaSort) {
            this.areaSort = areaSort;
        }

        public String getAreaName() {
            return areaName;
        }

        public int getAreaCode() {
            return areaCode;
        }

        public String getAreaSort() {
            return areaSort;
        }
    }
}
