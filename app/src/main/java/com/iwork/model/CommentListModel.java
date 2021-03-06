package com.iwork.model;

import android.os.Parcel;

import com.iwork.Base.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JianTao on 15/12/25.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class CommentListModel extends BaseModel {

    /**
     * infoCode : 0
     * message : 获取成功
     * data : [{"objId":2,"pic":"","content":"不好帅","create_time":1450349473000},{"objId":1,"pic":"","content":"你好吗","create_time":1450349431000}]
     */

    private int infoCode;
    private String message;
    /**
     * objId : 2
     * pic :
     * content : 不好帅
     * create_time : 1450349473000
     */

    private List<CommentModel> data;

    public void setInfoCode(int infoCode) {
        this.infoCode = infoCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(List<CommentModel> data) {
        this.data = data;
    }

    public int getInfoCode() {
        return infoCode;
    }

    public String getMessage() {
        return message;
    }

    public List<CommentModel> getData() {
        return data;
    }

    public static class CommentModel extends BaseModel{
        private int objId;
        private String pic;
        private String content;
        private long create_time;
        private String fromName;
        private int c_main_id;
        private int c_from_id;

        public int getC_from_id() {
            return c_from_id;
        }

        public void setC_from_id(int c_from_id) {
            this.c_from_id = c_from_id;
        }

        public int getMainId() {
            return c_main_id;
        }

        public void setMainId(int mainId) {
            this.c_main_id = mainId;
        }

        public String getFromName() {
            return fromName;
        }

        public void setFromName(String fromName) {
            this.fromName = fromName;
        }

        public void setObjId(int objId) {
            this.objId = objId;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public int getObjId() {
            return objId;
        }

        public String getPic() {
            return pic;
        }

        public String getContent() {
            return content;
        }

        public long getCreate_time() {
            return create_time;
        }

        public CommentModel() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.objId);
            dest.writeString(this.pic);
            dest.writeString(this.content);
            dest.writeLong(this.create_time);
            dest.writeString(this.fromName);
            dest.writeInt(this.c_main_id);
            dest.writeInt(this.c_from_id);
        }

        protected CommentModel(Parcel in) {
            super(in);
            this.objId = in.readInt();
            this.pic = in.readString();
            this.content = in.readString();
            this.create_time = in.readLong();
            this.fromName = in.readString();
            this.c_main_id = in.readInt();
            this.c_from_id = in.readInt();
        }

        public static final Creator<CommentModel> CREATOR = new Creator<CommentModel>() {
            public CommentModel createFromParcel(Parcel source) {
                return new CommentModel(source);
            }

            public CommentModel[] newArray(int size) {
                return new CommentModel[size];
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
        dest.writeList(this.data);
    }

    public CommentListModel() {
    }

    protected CommentListModel(Parcel in) {
        super(in);
        this.infoCode = in.readInt();
        this.message = in.readString();
        this.data = new ArrayList<CommentModel>();
        in.readList(this.data, List.class.getClassLoader());
    }

    public static final Creator<CommentListModel> CREATOR = new Creator<CommentListModel>() {
        public CommentListModel createFromParcel(Parcel source) {
            return new CommentListModel(source);
        }

        public CommentListModel[] newArray(int size) {
            return new CommentListModel[size];
        }
    };
}
