package com.iwork.model;

import android.os.Parcel;

import com.iwork.Base.BaseModel;

/**
 * Created by JianTao on 15/11/28.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class RegisterBean extends BaseModel{

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

    public static class DataEntity extends BaseModel{
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.zh_name);
            dest.writeString(this.token);
        }

        public DataEntity() {
        }

        protected DataEntity(Parcel in) {
            super(in);
            this.zh_name = in.readString();
            this.token = in.readString();
        }

        public static final Creator<DataEntity> CREATOR = new Creator<DataEntity>() {
            public DataEntity createFromParcel(Parcel source) {
                return new DataEntity(source);
            }

            public DataEntity[] newArray(int size) {
                return new DataEntity[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.infoCode);
        dest.writeParcelable(this.data, flags);
    }

    public RegisterBean() {
    }

    protected RegisterBean(Parcel in) {
        super(in);
        this.infoCode = in.readInt();
        this.data = in.readParcelable(DataEntity.class.getClassLoader());
    }

    public static final Creator<RegisterBean> CREATOR = new Creator<RegisterBean>() {
        public RegisterBean createFromParcel(Parcel source) {
            return new RegisterBean(source);
        }

        public RegisterBean[] newArray(int size) {
            return new RegisterBean[size];
        }
    };
}
