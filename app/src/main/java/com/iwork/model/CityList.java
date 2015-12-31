package com.iwork.model;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.iwork.Base.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JianTao on 15/12/14.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class CityList extends BaseModel{

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.infoCode);
        dest.writeString(this.message);
        dest.writeList(this.citys);
    }

    public CityList() {
        super();
    }

    protected CityList(Parcel in) {
        super(in);
        this.infoCode = in.readInt();
        this.message = in.readString();
        this.citys = new ArrayList<City>();
        in.readList(this.citys, List.class.getClassLoader());
    }

    public static final Creator<CityList> CREATOR = new Creator<CityList>() {
        public CityList createFromParcel(Parcel source) {
            return new CityList(source);
        }

        public CityList[] newArray(int size) {
            return new CityList[size];
        }
    };
}
