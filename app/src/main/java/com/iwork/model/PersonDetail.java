package com.iwork.model;

import android.os.Parcel;

import com.iwork.Base.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JianTao on 15/12/17.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class PersonDetail extends BaseModel{


    /**
     * infoCode : 0
     * message : 获取数据成功
     * data : {"performanceList":[{"groupDate":"2015/12","list":[{"objId":10,"companyName":"xxxxx","headhunterId":16,"position":"经理","annualSalary":"11111"}]}],"commentList":[{"objId":1,"c_from_id":1,"c_to_user_id":16,"content":"你好吗","toUserName":"测试人员"},{"objId":2,"c_from_id":4,"c_to_user_id":16,"content":"不好帅","toUserName":"测试人员"}],"headhunterInfo":{"objId":16,"realName":"廖端永","phone":"18201413265","companyName":"搜狐","position":"高级开发","workTime":1448380800000,"phone400":"0101234","pic":"http://www.7xoors.com1.z0.glb.clouddn.com/JPG-20151214210947-312.jpg","ranking":1,"participated":1,"commentCount":2,"industryList":[{"objId":3,"createTime":1450922224046,"industryName":"的撒旦法"}],"functionsList":[{"objId":1,"createTime":1450922224049,"functionsName":"职能1"},{"objId":2,"createTime":1450922224049,"functionsName":"职能2"}],"describeList":[{"objId":1,"headhunterId":16,"describe":"测试1","status":1},{"objId":2,"headhunterId":16,"describe":"测试2","status":1},{"objId":3,"headhunterId":16,"describe":"测试3","status":1},{"objId":4,"headhunterId":16,"describe":"测试4","status":1},{"objId":5,"headhunterId":16,"describe":"测试5","status":1}],"isAuth":0}}
     */

    private int infoCode;
    private String message;
    /**
     * performanceList : [{"groupDate":"2015/12","list":[{"objId":10,"companyName":"xxxxx","headhunterId":16,"position":"经理","annualSalary":"11111"}]}]
     * commentList : [{"objId":1,"c_from_id":1,"c_to_user_id":16,"content":"你好吗","toUserName":"测试人员"},{"objId":2,"c_from_id":4,"c_to_user_id":16,"content":"不好帅","toUserName":"测试人员"}]
     * headhunterInfo : {"objId":16,"realName":"廖端永","phone":"18201413265","companyName":"搜狐","position":"高级开发","workTime":1448380800000,"phone400":"0101234","pic":"http://www.7xoors.com1.z0.glb.clouddn.com/JPG-20151214210947-312.jpg","ranking":1,"participated":1,"commentCount":2,"industryList":[{"objId":3,"createTime":1450922224046,"industryName":"的撒旦法"}],"functionsList":[{"objId":1,"createTime":1450922224049,"functionsName":"职能1"},{"objId":2,"createTime":1450922224049,"functionsName":"职能2"}],"describeList":[{"objId":1,"headhunterId":16,"describe":"测试1","status":1},{"objId":2,"headhunterId":16,"describe":"测试2","status":1},{"objId":3,"headhunterId":16,"describe":"测试3","status":1},{"objId":4,"headhunterId":16,"describe":"测试4","status":1},{"objId":5,"headhunterId":16,"describe":"测试5","status":1}],"isAuth":0}
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

    public static class DataEntity extends BaseModel{
        /**
         * objId : 16
         * realName : 廖端永
         * phone : 18201413265
         * companyName : 搜狐
         * position : 高级开发
         * workTime : 1448380800000
         * phone400 : 0101234
         * pic : http://www.7xoors.com1.z0.glb.clouddn.com/JPG-20151214210947-312.jpg
         * ranking : 1
         * participated : 1
         * commentCount : 2
         * industryList : [{"objId":3,"createTime":1450922224046,"industryName":"的撒旦法"}]
         * functionsList : [{"objId":1,"createTime":1450922224049,"functionsName":"职能1"},{"objId":2,"createTime":1450922224049,"functionsName":"职能2"}]
         * describeList : [{"objId":1,"headhunterId":16,"describe":"测试1","status":1},{"objId":2,"headhunterId":16,"describe":"测试2","status":1},{"objId":3,"headhunterId":16,"describe":"测试3","status":1},{"objId":4,"headhunterId":16,"describe":"测试4","status":1},{"objId":5,"headhunterId":16,"describe":"测试5","status":1}]
         * isAuth : 0
         */

        private HeadhunterInfoEntity headhunterInfo;
        /**
         * groupDate : 2015/12
         * list : [{"objId":10,"companyName":"xxxxx","headhunterId":16,"position":"经理","annualSalary":"11111"}]
         */

        private List<PerformanceListEntity> performanceList;
        /**
         * objId : 1
         * c_from_id : 1
         * c_to_user_id : 16
         * content : 你好吗
         * toUserName : 测试人员
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

        public static class HeadhunterInfoEntity extends BaseModel{
            private int objId;
            private String realName;
            private String phone;
            private String companyName;
            private String position;
            private long workTime;
            private String phone400;
            private String pic;
            private int ranking;
            private int participated;
            private int commentCount;
            private int isAuth;
            private int userId;

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            /**
             * objId : 3
             * createTime : 1450922224046
             * industryName : 的撒旦法
             */

            private List<IndustryListEntity> industryList;
            /**
             * objId : 1
             * createTime : 1450922224049
             * functionsName : 职能1
             */

            private List<FunctionsListEntity> functionsList;
            /**
             * objId : 1
             * headhunterId : 16
             * describe : 测试1
             * status : 1
             */

            private List<DescribeListEntity> describeList;

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

            public void setRanking(int ranking) {
                this.ranking = ranking;
            }

            public void setParticipated(int participated) {
                this.participated = participated;
            }

            public void setCommentCount(int commentCount) {
                this.commentCount = commentCount;
            }

            public void setIsAuth(int isAuth) {
                this.isAuth = isAuth;
            }

            public void setIndustryList(List<IndustryListEntity> industryList) {
                this.industryList = industryList;
            }

            public void setFunctionsList(List<FunctionsListEntity> functionsList) {
                this.functionsList = functionsList;
            }

            public void setDescribeList(List<DescribeListEntity> describeList) {
                this.describeList = describeList;
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

            public int getRanking() {
                return ranking;
            }

            public int getParticipated() {
                return participated;
            }

            public int getCommentCount() {
                return commentCount;
            }

            public int getIsAuth() {
                return isAuth;
            }

            public List<IndustryListEntity> getIndustryList() {
                return industryList;
            }

            public List<FunctionsListEntity> getFunctionsList() {
                return functionsList;
            }

            public List<DescribeListEntity> getDescribeList() {
                return describeList;
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
            }

            public static class DescribeListEntity {
                private int objId;
                private int headhunterId;
                private String describe;
                private int status;

                public void setObjId(int objId) {
                    this.objId = objId;
                }

                public void setHeadhunterId(int headhunterId) {
                    this.headhunterId = headhunterId;
                }

                public void setDescribe(String describe) {
                    this.describe = describe;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public int getObjId() {
                    return objId;
                }

                public int getHeadhunterId() {
                    return headhunterId;
                }

                public String getDescribe() {
                    return describe;
                }

                public int getStatus() {
                    return status;
                }
            }

            public HeadhunterInfoEntity() {
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                super.writeToParcel(dest, flags);
                dest.writeInt(this.objId);
                dest.writeString(this.realName);
                dest.writeString(this.phone);
                dest.writeString(this.companyName);
                dest.writeString(this.position);
                dest.writeLong(this.workTime);
                dest.writeString(this.phone400);
                dest.writeString(this.pic);
                dest.writeInt(this.ranking);
                dest.writeInt(this.participated);
                dest.writeInt(this.commentCount);
                dest.writeInt(this.isAuth);
                dest.writeInt(this.userId);
                dest.writeList(this.industryList);
                dest.writeList(this.functionsList);
                dest.writeList(this.describeList);
            }

            protected HeadhunterInfoEntity(Parcel in) {
                super(in);
                this.objId = in.readInt();
                this.realName = in.readString();
                this.phone = in.readString();
                this.companyName = in.readString();
                this.position = in.readString();
                this.workTime = in.readLong();
                this.phone400 = in.readString();
                this.pic = in.readString();
                this.ranking = in.readInt();
                this.participated = in.readInt();
                this.commentCount = in.readInt();
                this.isAuth = in.readInt();
                this.userId = in.readInt();
                this.industryList = new ArrayList<IndustryListEntity>();
                in.readList(this.industryList, List.class.getClassLoader());
                this.functionsList = new ArrayList<FunctionsListEntity>();
                in.readList(this.functionsList, List.class.getClassLoader());
                this.describeList = new ArrayList<DescribeListEntity>();
                in.readList(this.describeList, List.class.getClassLoader());
            }

            public static final Creator<HeadhunterInfoEntity> CREATOR = new Creator<HeadhunterInfoEntity>() {
                public HeadhunterInfoEntity createFromParcel(Parcel source) {
                    return new HeadhunterInfoEntity(source);
                }

                public HeadhunterInfoEntity[] newArray(int size) {
                    return new HeadhunterInfoEntity[size];
                }
            };
        }

        public static class PerformanceListEntity {
            private String groupDate;
            /**
             * objId : 10
             * companyName : xxxxx
             * headhunterId : 16
             * position : 经理
             * annualSalary : 11111
             */

            private List<ListEntity> list;

            public void setGroupDate(String groupDate) {
                this.groupDate = groupDate;
            }

            public void setList(List<ListEntity> list) {
                this.list = list;
            }

            public String getGroupDate() {
                return groupDate;
            }

            public List<ListEntity> getList() {
                return list;
            }

            public static class ListEntity {
                private int objId;
                private String companyName;
                private int headhunterId;
                private String position;
                private String annualSalary;

                public void setObjId(int objId) {
                    this.objId = objId;
                }

                public void setCompanyName(String companyName) {
                    this.companyName = companyName;
                }

                public void setHeadhunterId(int headhunterId) {
                    this.headhunterId = headhunterId;
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

                public String getCompanyName() {
                    return companyName;
                }

                public int getHeadhunterId() {
                    return headhunterId;
                }

                public String getPosition() {
                    return position;
                }

                public String getAnnualSalary() {
                    return annualSalary;
                }
            }
        }

        public static class CommentListEntity {
            private int objId;
            private int c_from_id;
            private int c_to_user_id;
            private String content;
            private String fromName;
            private String create_time;

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
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

        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(this.headhunterInfo, 0);
            dest.writeList(this.performanceList);
            dest.writeList(this.commentList);
        }

        public DataEntity() {
        }

        protected DataEntity(Parcel in) {
            super(in);
            this.headhunterInfo = in.readParcelable(HeadhunterInfoEntity.class.getClassLoader());
            this.performanceList = new ArrayList<PerformanceListEntity>();
            in.readList(this.performanceList, List.class.getClassLoader());
            this.commentList = new ArrayList<CommentListEntity>();
            in.readList(this.commentList, List.class.getClassLoader());
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
        dest.writeParcelable(this.data, flags);
    }

    public PersonDetail() {
    }

    protected PersonDetail(Parcel in) {
        super(in);
        this.infoCode = in.readInt();
        this.message = in.readString();
        this.data = in.readParcelable(DataEntity.class.getClassLoader());
    }

    public static final Creator<PersonDetail> CREATOR = new Creator<PersonDetail>() {
        public PersonDetail createFromParcel(Parcel source) {
            return new PersonDetail(source);
        }

        public PersonDetail[] newArray(int size) {
            return new PersonDetail[size];
        }
    };
}
