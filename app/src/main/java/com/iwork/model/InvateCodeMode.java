package com.iwork.model;

import android.os.Parcel;

import com.iwork.Base.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JianTao on 15/12/29.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class InvateCodeMode extends BaseModel{

    /**
     * infoCode : 0
     * message : 操作成功
     * data : [{"objId":2,"invate_from_id":100022,"invate_to_phone":"","code":437830,"status":1},{"objId":3,"invate_from_id":100022,"invate_to_phone":"","code":416341,"status":1}]
     */

    private int infoCode;
    private String message;
    /**
     * objId : 2
     * invate_from_id : 100022
     * invate_to_phone :
     * code : 437830
     * status : 1
     */

    private List<InvateCode> data;

    public void setInfoCode(int infoCode) {
        this.infoCode = infoCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(List<InvateCode> data) {
        this.data = data;
    }

    public int getInfoCode() {
        return infoCode;
    }

    public String getMessage() {
        return message;
    }

    public List<InvateCode> getData() {
        return data;
    }

    public static class InvateCode {
        private int objId;
        private int invate_from_id;
        private String invate_to_phone;
        private int code;
        private int status;

        public void setObjId(int objId) {
            this.objId = objId;
        }

        public void setInvate_from_id(int invate_from_id) {
            this.invate_from_id = invate_from_id;
        }

        public void setInvate_to_phone(String invate_to_phone) {
            this.invate_to_phone = invate_to_phone;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getObjId() {
            return objId;
        }

        public int getInvate_from_id() {
            return invate_from_id;
        }

        public String getInvate_to_phone() {
            return invate_to_phone;
        }

        public int getCode() {
            return code;
        }

        public int getStatus() {
            return status;
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

    public InvateCodeMode() {
    }

    protected InvateCodeMode(Parcel in) {
        super(in);
        this.infoCode = in.readInt();
        this.message = in.readString();
        this.data = new ArrayList<InvateCode>();
        in.readList(this.data, List.class.getClassLoader());
    }

    public static final Creator<InvateCodeMode> CREATOR = new Creator<InvateCodeMode>() {
        public InvateCodeMode createFromParcel(Parcel source) {
            return new InvateCodeMode(source);
        }

        public InvateCodeMode[] newArray(int size) {
            return new InvateCodeMode[size];
        }
    };
}

