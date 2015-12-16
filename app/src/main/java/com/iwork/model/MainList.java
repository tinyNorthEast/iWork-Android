package com.iwork.model;

import java.util.List;

/**
 * Created by JianTao on 15/12/16.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class MainList {

    /**
     * infoCode : 0
     * message : 获取数据成功
     * data : [{"objId":18,"realName":"杨宇1","pic":"http://www.7xoors.com1.z0.glb.clouddn.com/JPG-20151214211424-278.jpg","ranking":0,"commentCount":0,"industryList":[{"objId":2,"createTime":1450243459918,"industryName":"财务"}]},{"objId":15,"realName":"彭召辉3","pic":"http://www.7xoors.com1.z0.glb.clouddn.com/JPG-20151214211010-127.jpg","ranking":0,"commentCount":0,"industryList":[{"objId":2,"createTime":1450243459921,"industryName":"财务"}]},{"objId":11,"realName":"rrrr","pic":"http://www.7xoors.com1.z0.glb.clouddn.com/JPG-20151214211057-953.jpg","ranking":0,"commentCount":0,"industryList":[{"objId":3,"createTime":1450243459924,"industryName":"法务"},{"objId":2,"createTime":1450243459924,"industryName":"财务"}]},{"objId":10,"realName":"ldy03","pic":"http://www.7xoors.com1.z0.glb.clouddn.com/JPG-20151214211159-314.jpg","ranking":0,"commentCount":0,"industryList":[{"objId":3,"createTime":1450243459927,"industryName":"法务"},{"objId":2,"createTime":1450243459927,"industryName":"财务"}]},{"objId":9,"realName":"ldy03","pic":"http://www.7xoors.com1.z0.glb.clouddn.com/JPG-20151214211227-139.jpg","ranking":0,"commentCount":0,"industryList":[{"objId":3,"createTime":1450243459930,"industryName":"法务"},{"objId":1,"createTime":1450243459930,"industryName":"HR"}]},{"objId":8,"realName":"ldy03","pic":"d7b18032_ca58_4d82_bda0_b9a232acffc0","ranking":0,"commentCount":0,"industryList":[]},{"objId":7,"realName":"ldy03","pic":"http://www.7xoors.com1.z0.glb.clouddn.com/JPG-20151214211246-037.jpg","ranking":0,"commentCount":0,"industryList":[{"objId":6,"createTime":1450243459936,"industryName":"汽车&机械"}]},{"objId":4,"realName":"ldy02","pic":"http://www.7xoors.com1.z0.glb.clouddn.com/JPG-20151214211318-757.jpg","ranking":0,"commentCount":0,"industryList":[{"objId":11,"createTime":1450243459939,"industryName":"地产"}]},{"objId":2,"realName":"3","pic":"http://www.7xoors.com1.z0.glb.clouddn.com/PNG-20151214211338-884.png","ranking":0,"commentCount":0,"industryList":[{"objId":2,"createTime":1450243459942,"industryName":"财务"}]},{"objId":1,"realName":"1","pic":"http://www.7xoors.com1.z0.glb.clouddn.com/JPG-20151214211359-951.jpg","ranking":0,"commentCount":0,"industryList":[{"objId":9,"createTime":1450243459945,"industryName":"化工"}]}]
     */

    private int infoCode;
    private String message;
    /**
     * objId : 18
     * realName : 杨宇1
     * pic : http://www.7xoors.com1.z0.glb.clouddn.com/JPG-20151214211424-278.jpg
     * ranking : 0
     * commentCount : 0
     * industryList : [{"objId":2,"createTime":1450243459918,"industryName":"财务"}]
     */

    private List<Person> data;

    public void setInfoCode(int infoCode) {
        this.infoCode = infoCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(List<Person> data) {
        this.data = data;
    }

    public int getInfoCode() {
        return infoCode;
    }

    public String getMessage() {
        return message;
    }

    public List<Person> getData() {
        return data;
    }

    public static class Person {
        private int objId;
        private String realName;
        private String pic;
        private int ranking;
        private int commentCount;
        /**
         * objId : 2
         * createTime : 1450243459918
         * industryName : 财务
         */

        private List<IndustryListEntity> industryList;

        public void setObjId(int objId) {
            this.objId = objId;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public void setRanking(int ranking) {
            this.ranking = ranking;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }

        public void setIndustryList(List<IndustryListEntity> industryList) {
            this.industryList = industryList;
        }

        public int getObjId() {
            return objId;
        }

        public String getRealName() {
            return realName;
        }

        public String getPic() {
            return pic;
        }

        public int getRanking() {
            return ranking;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public List<IndustryListEntity> getIndustryList() {
            return industryList;
        }

        public static class IndustryListEntity {
            private int objId;
            private long createTime;
            private String industryName;

            public void setObjId(int objId) {
                this.objId = objId;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public void setIndustryName(String industryName) {
                this.industryName = industryName;
            }

            public int getObjId() {
                return objId;
            }

            public long getCreateTime() {
                return createTime;
            }

            public String getIndustryName() {
                return industryName;
            }
        }
    }
}
