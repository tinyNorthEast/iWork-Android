package com.iwork.model;

import android.os.Parcel;

import com.iwork.Base.BaseModel;

/**
 * Created by JianTao on 15/12/28.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class CommonModel extends BaseModel{

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.infoCode);
        dest.writeString(this.message);
    }

    public CommonModel() {
    }

    protected CommonModel(Parcel in) {
        super(in);
        this.infoCode = in.readInt();
        this.message = in.readString();
    }

    public static final Creator<CommonModel> CREATOR = new Creator<CommonModel>() {
        public CommonModel createFromParcel(Parcel source) {
            return new CommonModel(source);
        }

        public CommonModel[] newArray(int size) {
            return new CommonModel[size];
        }
    };
}
