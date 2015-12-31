package com.iwork.model;

import android.os.Parcel;

import com.iwork.Base.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JianTao on 15/12/28.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class IndustryListModel extends BaseModel{

    /**
     * infoCode : 0
     * message : 获取成功！
     * data : [{"objId":1,"name":"得得得","tip":"ddd"},{"objId":2,"name":"烦烦烦","tip":"dsf"},{"objId":3,"name":"的撒旦法","tip":"sdfss"}]
     */

    private int infoCode;
    private String message;
    /**
     * objId : 1
     * name : 得得得
     * tip : ddd
     */

    private List<Industry> data;

    public void setInfoCode(int infoCode) {
        this.infoCode = infoCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(List<Industry> data) {
        this.data = data;
    }

    public int getInfoCode() {
        return infoCode;
    }

    public String getMessage() {
        return message;
    }

    public List<Industry> getData() {
        return data;
    }

    public static class Industry {
        private int objId;
        private String name;
        private String tip;

        public void setObjId(int objId) {
            this.objId = objId;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setTip(String tip) {
            this.tip = tip;
        }

        public int getObjId() {
            return objId;
        }

        public String getName() {
            return name;
        }

        public String getTip() {
            return tip;
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
        dest.writeList(this.data);
    }

    public IndustryListModel() {
    }

    protected IndustryListModel(Parcel in) {
        super(in);
        this.infoCode = in.readInt();
        this.message = in.readString();
        this.data = new ArrayList<Industry>();
        in.readList(this.data, List.class.getClassLoader());
    }

    public static final Creator<IndustryListModel> CREATOR = new Creator<IndustryListModel>() {
        public IndustryListModel createFromParcel(Parcel source) {
            return new IndustryListModel(source);
        }

        public IndustryListModel[] newArray(int size) {
            return new IndustryListModel[size];
        }
    };
}
