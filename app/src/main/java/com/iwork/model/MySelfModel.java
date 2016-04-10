package com.iwork.model;

import android.os.Parcel;

import com.iwork.Base.BaseModel;

/**
 * Created by JianTao on 15/12/24.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class MySelfModel extends BaseModel {


    /**
     * infoCode : 0
     * message : 操作成功
     * data : {"zh_name":"剑涛","mail":"a_tao123@163.com","en_name":"jacket","experience":1,"roleName":"猎头","pic":"","noticeCount":1}
     */

    private int infoCode;
    private String message;
    /**
     * zh_name : 剑涛
     * mail : a_tao123@163.com
     * en_name : jacket
     * experience : 1
     * roleName : 猎头
     * pic :
     * noticeCount : 1
     */

    private DataEntity data;

    public void setInfoCode(int infoCode) {
        this.infoCode = infoCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public int getInfoCode() {
        return infoCode;
    }

    public String getMessage() {
        return message;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity extends BaseModel {
        private String zh_name;
        private String mail;
        private String en_name;
        private int experience;
        private String roleName;
        private String pic;
        private int noticeCount;
        private String signature;

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public void setZh_name(String zh_name) {
            this.zh_name = zh_name;
        }

        public void setMail(String mail) {
            this.mail = mail;
        }

        public void setEn_name(String en_name) {
            this.en_name = en_name;
        }

        public void setExperience(int experience) {
            this.experience = experience;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public void setNoticeCount(int noticeCount) {
            this.noticeCount = noticeCount;
        }

        public String getZh_name() {
            return zh_name;
        }

        public String getMail() {
            return mail;
        }

        public String getEn_name() {
            return en_name;
        }

        public int getExperience() {
            return experience;
        }

        public String getRoleName() {
            return roleName;
        }

        public String getPic() {
            return pic;
        }

        public int getNoticeCount() {
            return noticeCount;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.zh_name);
            dest.writeString(this.mail);
            dest.writeString(this.en_name);
            dest.writeInt(this.experience);
            dest.writeString(this.roleName);
            dest.writeString(this.pic);
            dest.writeInt(this.noticeCount);
        }

        public DataEntity() {
        }

        protected DataEntity(Parcel in) {
            super(in);
            this.zh_name = in.readString();
            this.mail = in.readString();
            this.en_name = in.readString();
            this.experience = in.readInt();
            this.roleName = in.readString();
            this.pic = in.readString();
            this.noticeCount = in.readInt();
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
        dest.writeParcelable(this.data, 0);
    }

    public MySelfModel() {
    }

    protected MySelfModel(Parcel in) {
        super(in);
        this.infoCode = in.readInt();
        this.message = in.readString();
        this.data = in.readParcelable(DataEntity.class.getClassLoader());
    }

    public static final Creator<MySelfModel> CREATOR = new Creator<MySelfModel>() {
        public MySelfModel createFromParcel(Parcel source) {
            return new MySelfModel(source);
        }

        public MySelfModel[] newArray(int size) {
            return new MySelfModel[size];
        }
    };
}
