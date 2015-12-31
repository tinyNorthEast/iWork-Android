package com.iwork.model;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.iwork.Base.BaseModel;

/**
 * Created by JianTao on 15/11/29.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class LoginInfo extends BaseModel{

    /**
     * infoCode : 0
     * message : 登陆成功
     * data : {"zh_name":"就爱你他","role_code":100,"token":"036d91ce4ba76d8699ce99799ac49027"}
     */

    private int infoCode;
    private String message;
    /**
     * zh_name : 就爱你他
     * role_code : 100
     * token : 036d91ce4ba76d8699ce99799ac49027
     */

    @SerializedName("data")
    private DataEntity login_data;

    public void setInfoCode(int infoCode) {
        this.infoCode = infoCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setLogin_data(DataEntity login_data) {
        this.login_data = login_data;
    }

    public int getInfoCode() {
        return infoCode;
    }

    public String getMessage() {
        return message;
    }

    public DataEntity getLogin_data() {
        return login_data;
    }

    public static class DataEntity extends BaseModel{
        private String zh_name;
        private int role_code;
        private String token;
        private String userId;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public void setZh_name(String zh_name) {
            this.zh_name = zh_name;
        }

        public void setRole_code(int role_code) {
            this.role_code = role_code;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getZh_name() {
            return zh_name;
        }

        public int getRole_code() {
            return role_code;
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
            dest.writeInt(this.role_code);
            dest.writeString(this.token);
            dest.writeString(this.userId);
        }

        public DataEntity() {
        }

        protected DataEntity(Parcel in) {
            super(in);
            this.zh_name = in.readString();
            this.role_code = in.readInt();
            this.token = in.readString();
            this.userId = in.readString();
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
        dest.writeString(this.message);
        dest.writeParcelable(this.login_data, flags);
    }

    public LoginInfo() {
    }

    protected LoginInfo(Parcel in) {
        super(in);
        this.infoCode = in.readInt();
        this.message = in.readString();
        this.login_data = in.readParcelable(DataEntity.class.getClassLoader());
    }

    public static final Creator<LoginInfo> CREATOR = new Creator<LoginInfo>() {
        public LoginInfo createFromParcel(Parcel source) {
            return new LoginInfo(source);
        }

        public LoginInfo[] newArray(int size) {
            return new LoginInfo[size];
        }
    };
}
