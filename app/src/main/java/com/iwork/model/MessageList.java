package com.iwork.model;

import android.os.Parcel;

import com.iwork.Base.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JianTao on 15/12/27.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class MessageList extends BaseModel{


    /**
     * infoCode : 0
     * message : 获取通知成功！
     * data : [{"objId":1,"user_id":100022,"content":"测试消息","create_time":1451200167000,"status":1,"n_type":1},{"objId":5,"user_id":100022,"content":"测试消息2","create_time":1451368769000,"status":1,"n_type":1}]
     */

    private int infoCode;
    private String message;
    /**
     * objId : 1
     * user_id : 100022
     * content : 测试消息
     * create_time : 1451200167000
     * status : 1
     * n_type : 1
     */

    private List<MessageDataEntity> data;

    public void setInfoCode(int infoCode) {
        this.infoCode = infoCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(List<MessageDataEntity> data) {
        this.data = data;
    }

    public int getInfoCode() {
        return infoCode;
    }

    public String getMessage() {
        return message;
    }

    public List<MessageDataEntity> getData() {
        return data;
    }

    public static class MessageDataEntity {
        private int objId;
        private int user_id;
        private String content;
        private long create_time;
        private int status;
        private int n_type;

        public void setObjId(int objId) {
            this.objId = objId;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setN_type(int n_type) {
            this.n_type = n_type;
        }

        public int getObjId() {
            return objId;
        }

        public int getUser_id() {
            return user_id;
        }

        public String getContent() {
            return content;
        }

        public long getCreate_time() {
            return create_time;
        }

        public int getStatus() {
            return status;
        }

        public int getN_type() {
            return n_type;
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

    public MessageList() {
    }

    protected MessageList(Parcel in) {
        super(in);
        this.infoCode = in.readInt();
        this.message = in.readString();
        this.data = new ArrayList<MessageDataEntity>();
        in.readList(this.data, List.class.getClassLoader());
    }

    public static final Creator<MessageList> CREATOR = new Creator<MessageList>() {
        public MessageList createFromParcel(Parcel source) {
            return new MessageList(source);
        }

        public MessageList[] newArray(int size) {
            return new MessageList[size];
        }
    };
}
