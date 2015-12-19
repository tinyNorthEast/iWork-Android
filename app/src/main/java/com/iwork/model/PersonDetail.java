package com.iwork.model;

import java.util.List;

/**
 * Created by JianTao on 15/12/17.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class PersonDetail {

    /**
     * infoCode : 0
     * message : 获取数据成功
     * data : {"performanceList":[{"objId":10,"status":1,"createTime":1449758846000,"companyName":"sssss","headhunterId":16,"hiddenName":"xxxxx","position":"经理","annualSalary":"11111"}],"commentList":[{"objId":1,"c_from_id":1,"c_to_user_id":16,"content":"你好吗"},{"objId":2,"c_from_id":4,"c_to_user_id":16,"content":"不好帅"}],"headhunterInfo":{"objId":16,"realName":"廖端永","phone":"18201413265","companyName":"搜狐","position":"高级开发","workTime":1448380800000,"phone400":"0101234","pic":"http://www.7xoors.com1.z0.glb.clouddn.com/JPG-20151214210947-312.jpg","describe":"阿斯顿飞洒","ranking":1,"participated":1,"commentCount":2,"industryList":[{"objId":3,"createTime":1450361350555,"industryName":"法务"}],"functionsList":[{"objId":82,"createTime":1450361350557,"functionsName":"外部审计-四大"},{"objId":2,"createTime":1450361350557,"functionsName":"洽谈"}]}}
     */

    private int infoCode;
    private String message;
    /**
     * performanceList : [{"objId":10,"status":1,"createTime":1449758846000,"companyName":"sssss","headhunterId":16,"hiddenName":"xxxxx","position":"经理","annualSalary":"11111"}]
     * commentList : [{"objId":1,"c_from_id":1,"c_to_user_id":16,"content":"你好吗"},{"objId":2,"c_from_id":4,"c_to_user_id":16,"content":"不好帅"}]
     * headhunterInfo : {"objId":16,"realName":"廖端永","phone":"18201413265","companyName":"搜狐","position":"高级开发","workTime":1448380800000,"phone400":"0101234","pic":"http://www.7xoors.com1.z0.glb.clouddn.com/JPG-20151214210947-312.jpg","describe":"阿斯顿飞洒","ranking":1,"participated":1,"commentCount":2,"industryList":[{"objId":3,"createTime":1450361350555,"industryName":"法务"}],"functionsList":[{"objId":82,"createTime":1450361350557,"functionsName":"外部审计-四大"},{"objId":2,"createTime":1450361350557,"functionsName":"洽谈"}]}
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

    public static class DataEntity {
        /**
         * objId : 16
         * realName : 廖端永
         * phone : 18201413265
         * companyName : 搜狐
         * position : 高级开发
         * workTime : 1448380800000
         * phone400 : 0101234
         * pic : http://www.7xoors.com1.z0.glb.clouddn.com/JPG-20151214210947-312.jpg
         * describe : 阿斯顿飞洒
         * ranking : 1
         * participated : 1
         * commentCount : 2
         * industryList : [{"objId":3,"createTime":1450361350555,"industryName":"法务"}]
         * functionsList : [{"objId":82,"createTime":1450361350557,"functionsName":"外部审计-四大"},{"objId":2,"createTime":1450361350557,"functionsName":"洽谈"}]
         */

        private HeadhunterInfoEntity headhunterInfo;
        /**
         * objId : 10
         * status : 1
         * createTime : 1449758846000
         * companyName : sssss
         * headhunterId : 16
         * hiddenName : xxxxx
         * position : 经理
         * annualSalary : 11111
         */

        private List<PerformanceListEntity> performanceList;
        /**
         * objId : 1
         * c_from_id : 1
         * c_to_user_id : 16
         * content : 你好吗
         */

        private List<CommentListEntity> commentList;

        public void setHeadhunterInfo(HeadhunterInfoEntity headhunterInfo) {
            this.headhunterInfo = headhunterInfo;
        }

        public void setPerformanceList(List<PerformanceListEntity> performanceList) {
            this.performanceList = performanceList;
        }

        public void setCommentList(List<CommentListEntity> commentList) {
            this.commentList = commentList;
        }

        public HeadhunterInfoEntity getHeadhunterInfo() {
            return headhunterInfo;
        }

        public List<PerformanceListEntity> getPerformanceList() {
            return performanceList;
        }

        public List<CommentListEntity> getCommentList() {
            return commentList;
        }

        public static class HeadhunterInfoEntity {
            private int objId;
            private String realName;
            private String phone;
            private String companyName;
            private String position;
            private long workTime;
            private String phone400;
            private String pic;
            private String describe;
            private int ranking;
            private int participated;
            private int commentCount;
            /**
             * objId : 3
             * createTime : 1450361350555
             * industryName : 法务
             */

            private List<IndustryListEntity> industryList;
            /**
             * objId : 82
             * createTime : 1450361350557
             * functionsName : 外部审计-四大
             */

            private List<FunctionsListEntity> functionsList;

            public void setObjId(int objId) {
                this.objId = objId;
            }

            public void setRealName(String realName) {
                this.realName = realName;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public void setCompanyName(String companyName) {
                this.companyName = companyName;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public void setWorkTime(long workTime) {
                this.workTime = workTime;
            }

            public void setPhone400(String phone400) {
                this.phone400 = phone400;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public void setDescribe(String describe) {
                this.describe = describe;
            }

            public void setRanking(int ranking) {
                this.ranking = ranking;
            }

            public void setParticipated(int participated) {
                this.participated = participated;
            }

            public void setCommentCount(int commentCount) {
                this.commentCount = commentCount;
            }

            public void setIndustryList(List<IndustryListEntity> industryList) {
                this.industryList = industryList;
            }

            public void setFunctionsList(List<FunctionsListEntity> functionsList) {
                this.functionsList = functionsList;
            }

            public int getObjId() {
                return objId;
            }

            public String getRealName() {
                return realName;
            }

            public String getPhone() {
                return phone;
            }

            public String getCompanyName() {
                return companyName;
            }

            public String getPosition() {
                return position;
            }

            public long getWorkTime() {
                return workTime;
            }

            public String getPhone400() {
                return phone400;
            }

            public String getPic() {
                return pic;
            }

            public String getDescribe() {
                return describe;
            }

            public int getRanking() {
                return ranking;
            }

            public int getParticipated() {
                return participated;
            }

            public int getCommentCount() {
                return commentCount;
            }

            public List<IndustryListEntity> getIndustryList() {
                return industryList;
            }

            public List<FunctionsListEntity> getFunctionsList() {
                return functionsList;
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

                @Override
                public String toString() {
                    return "IndustryListEntity{" +
                            "objId=" + objId +
                            ", createTime=" + createTime +
                            ", industryName='" + industryName + '\'' +
                            '}';
                }
            }

            public static class FunctionsListEntity {
                private int objId;
                private long createTime;
                private String functionsName;

                public void setObjId(int objId) {
                    this.objId = objId;
                }

                public void setCreateTime(long createTime) {
                    this.createTime = createTime;
                }

                public void setFunctionsName(String functionsName) {
                    this.functionsName = functionsName;
                }

                public int getObjId() {
                    return objId;
                }

                public long getCreateTime() {
                    return createTime;
                }

                public String getFunctionsName() {
                    return functionsName;
                }

                @Override
                public String toString() {
                    return "FunctionsListEntity{" +
                            "objId=" + objId +
                            ", createTime=" + createTime +
                            ", functionsName='" + functionsName + '\'' +
                            '}';
                }
            }

            @Override
            public String toString() {
                return "HeadhunterInfoEntity{" +
                        "objId=" + objId +
                        ", realName='" + realName + '\'' +
                        ", phone='" + phone + '\'' +
                        ", companyName='" + companyName + '\'' +
                        ", position='" + position + '\'' +
                        ", workTime=" + workTime +
                        ", phone400='" + phone400 + '\'' +
                        ", pic='" + pic + '\'' +
                        ", describe='" + describe + '\'' +
                        ", ranking=" + ranking +
                        ", participated=" + participated +
                        ", commentCount=" + commentCount +
                        ", industryList=" + industryList +
                        ", functionsList=" + functionsList +
                        '}';
            }
        }

        public static class PerformanceListEntity {
            private int objId;
            private int status;
            private long createTime;
            private String companyName;
            private int headhunterId;
            private String hiddenName;
            private String position;
            private String annualSalary;

            public void setObjId(int objId) {
                this.objId = objId;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public void setCompanyName(String companyName) {
                this.companyName = companyName;
            }

            public void setHeadhunterId(int headhunterId) {
                this.headhunterId = headhunterId;
            }

            public void setHiddenName(String hiddenName) {
                this.hiddenName = hiddenName;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public void setAnnualSalary(String annualSalary) {
                this.annualSalary = annualSalary;
            }

            public int getObjId() {
                return objId;
            }

            public int getStatus() {
                return status;
            }

            public long getCreateTime() {
                return createTime;
            }

            public String getCompanyName() {
                return companyName;
            }

            public int getHeadhunterId() {
                return headhunterId;
            }

            public String getHiddenName() {
                return hiddenName;
            }

            public String getPosition() {
                return position;
            }

            public String getAnnualSalary() {
                return annualSalary;
            }

            @Override
            public String toString() {
                return "PerformanceListEntity{" +
                        "objId=" + objId +
                        ", status=" + status +
                        ", createTime=" + createTime +
                        ", companyName='" + companyName + '\'' +
                        ", headhunterId=" + headhunterId +
                        ", hiddenName='" + hiddenName + '\'' +
                        ", position='" + position + '\'' +
                        ", annualSalary='" + annualSalary + '\'' +
                        '}';
            }
        }

        public static class CommentListEntity {
            private int objId;
            private int c_from_id;
            private int c_to_user_id;
            private String content;

            public void setObjId(int objId) {
                this.objId = objId;
            }

            public void setC_from_id(int c_from_id) {
                this.c_from_id = c_from_id;
            }

            public void setC_to_user_id(int c_to_user_id) {
                this.c_to_user_id = c_to_user_id;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getObjId() {
                return objId;
            }

            public int getC_from_id() {
                return c_from_id;
            }

            public int getC_to_user_id() {
                return c_to_user_id;
            }

            public String getContent() {
                return content;
            }

            @Override
            public String toString() {
                return "CommentListEntity{" +
                        "objId=" + objId +
                        ", c_from_id=" + c_from_id +
                        ", c_to_user_id=" + c_to_user_id +
                        ", content='" + content + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "DataEntity{" +
                    "commentList=" + commentList +
                    ", headhunterInfo=" + headhunterInfo.toString() +
                    ", performanceList=" + performanceList +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PersonDetail{" +
                "infoCode=" + infoCode +
                ", message='" + message + '\'' +
                ", data=" + data.toString() +
                '}';
    }
}
