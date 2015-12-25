package com.iwork.model;

import java.util.List;

/**
 * Created by JianTao on 15/12/25.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class CommentListModel {

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

    public static class CommentModel {
        private int objId;
        private String pic;
        private String content;
        private long create_time;

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
    }
}
