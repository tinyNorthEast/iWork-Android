package com.iwork.net;

import android.preference.Preference;
import android.text.TextUtils;

import com.iwork.model.CityList;
import com.iwork.model.CommentListModel;
import com.iwork.model.CommonModel;
import com.iwork.model.IndustryListModel;
import com.iwork.model.LoginInfo;
import com.iwork.model.MainList;
import com.iwork.model.MessageList;
import com.iwork.model.MySelfModel;
import com.iwork.model.PersonDetail;
import com.iwork.model.QinNiuToken;
import com.iwork.model.RequestMessage;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.okhttp.request.OkHttpRequest;
import com.iwork.preferences.Preferences;
import com.iwork.utils.Constant;
import com.iwork.utils.NetConstant;
import com.iwork.utils.TextUtil;
import com.iwork.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JianTao on 15/11/28.
 * Copyright © 2015 impetusconsulting. All rights reserved
 */
public class CommonRequest {

    public static final String BASE_PARAM = "/version/{version}/";

    /**
     * 登录接口
     *
     * @param phone    手机号
     * @param password 密码 MD5
     * @param callback 回调接口
     */
    public static void login(String phone, String password, ResultCallback<LoginInfo> callback) {
        Map<String, String> params = new HashMap<>();
        params.put(ServerParam.PHONE, phone);
        params.put(ServerParam.PASSWORD, password);
        String url = createUrl("/api/v1/user/login.action", params);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    /**
     * 注册接口
     *
     * @param phone       手机号
     * @param password    密码
     * @param zh_name     中文名
     * @param mail        邮箱
     * @param experience  工作经验
     * @param position    职位
     * @param role_code   100-猎头顾问 101-企业HR 102-候选人
     * @param invate_code 邀请码
     * @param pic         头像
     * @param callback    回调
     */
    public static void register(String phone, String password, String zh_name, String mail,
                                int experience, String position, int role_code,
                                String invate_code, String pic, ResultCallback<LoginInfo> callback) {
        Map<String, String> params = new HashMap<>();
        params.put(ServerParam.PHONE, phone);
        params.put(ServerParam.PASSWORD, password);//MD5
        params.put(ServerParam.ZH_NAME, zh_name);
        params.put(ServerParam.MAIL, mail);
        params.put(ServerParam.EXPERIENCE, experience + "");
        params.put(ServerParam.POSITION, position);
        params.put(ServerParam.ROLE_CODE, role_code + "");
        if (!TextUtil.isEmpty(invate_code))
            params.put(ServerParam.INVATE_CODE, invate_code);
        if (!TextUtil.isEmpty(pic))
            params.put(ServerParam.PIC, pic);
        String url = createUrl("/api/v1/user/regist.action", params);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    /**
     * 检查手机号是否使用过
     *
     * @param phone
     * @param callback
     */
    public static void checkphonestatus(String phone, ResultCallback<RequestMessage> callback) {
        Map<String, String> params = new HashMap<>();
        params.put(ServerParam.PHONE, phone);
        String url = createUrl("/api/v1/user/phone.action", params);
        new OkHttpRequest.Builder().url(url).params(params).get(callback);
    }

    /**
     * 获取七牛token
     *
     * @param callback
     */
    public static void getQiniuToken(ResultCallback<QinNiuToken> callback) {
        Map<String, String> params = new HashMap<>();
        String url = createUrl("/api/v1/qiniu/getQiniuToken.action", params);
        new OkHttpRequest.Builder().url(url).get(callback);
    }

    /**
     * 更改密码
     *
     * @param phone
     * @param password
     * @param callback
     */
    public static void updataPassword(String phone, String password, ResultCallback<String> callback) {
        Map<String, String> params = new HashMap<>();
        params.put(ServerParam.PHONE, phone);
        params.put(ServerParam.PASSWORD, password);
        String url = createUrl("/api/v1/user/updatePassword", params);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    /**
     * 获取城市列表
     *
     * @param callback
     */
    public static void getCityList(ResultCallback<CityList> callback) {
        Map<String, String> params = new HashMap<>();

        String url = createUrl("/api/v1/city/findCityList.action", params);
        new OkHttpRequest.Builder().url(url).params(params).get(callback);
    }

    /**
     * 获取首页用户信息
     *
     * @param pageNo
     * @param callback
     */
    public static void getPersonList(int pageNo, ResultCallback<MainList> callback) {
        Map<String, String> params = new HashMap<>();
        params.put(ServerParam.PAGENO, pageNo + "");
        String url = createUrl("/api/v1/headhunter/list.action", params);
        new OkHttpRequest.Builder().url(url).params(params).get(callback);
    }

    /**
     * 获取首页详情页
     *
     * @param headHunterId 猎头id
     * @param callback
     */
    public static void getDetail(int headHunterId, ResultCallback<PersonDetail> callback) {
        Map<String, String> params = new HashMap<>();
        params.put(ServerParam.HEADHUNTERID, headHunterId + "");
        String token = Preferences.getInstance().getToken();
        params.put(ServerParam.TOKEN, token);
        String url = createUrl("/api/v1/headhunter/detail.action", params);
        new OkHttpRequest.Builder().url(url).params(params).get(callback);
    }

    /**
     * 获取个人中心数据
     *
     * @param callback
     */
    public static void getMySelfData(ResultCallback<MySelfModel> callback) {
        Map<String, String> params = new HashMap<>();
        String token = Preferences.getInstance().getToken();
        params.put(ServerParam.TOKEN, token);
        String url = createUrl("/api/v1/user/get.action", params);
        new OkHttpRequest.Builder().url(url).params(params).get(callback);
    }

    /**
     * 获取评论列表
     *
     * @param to_user_id
     * @param callback
     */
    public static void getCommonentListData(int to_user_id, ResultCallback<CommentListModel> callback) {
        Map<String, String> params = new HashMap<>();
        String token = Preferences.getInstance().getToken();
        params.put(ServerParam.TOKEN, token);
        params.put(ServerParam.TO_USER_ID, to_user_id + "");
        String url = createUrl("/api/v1/comment/findCommentList.action", params);
        new OkHttpRequest.Builder().url(url).params(params).get(callback);
    }

    /**
     * 获取评论列表
     *
     * @param callback
     */
    public static void getMessageListData(ResultCallback<MessageList> callback) {
        Map<String, String> params = new HashMap<>();
        params.put(ServerParam.TOKEN, Preferences.getInstance().getToken());
        String url = createUrl("/api/v1/notice/findNoticeList.action", params);
        new OkHttpRequest.Builder().url(url).params(params).get(callback);
    }

    /**
     * 获取行业列表
     *
     * @param callback
     */
    public static void getIndustryList(ResultCallback<IndustryListModel> callback) {
        Map<String, String> params = new HashMap<>();
        String url = createUrl("/api/v1/industry/findIndustryList.action", params);
        new OkHttpRequest.Builder().url(url).params(params).get(callback);
    }

    /**
     * 修改个人信息
     *
     * @param en_name
     * @param mail
     * @param company
     * @param experience
     * @param callback
     */
    public static void setUserInfo(String en_name, String mail, String company, int experience, ResultCallback<CommonModel> callback) {
        Map<String, String> params = new HashMap<>();
        if (!TextUtil.isEmpty(en_name)) {
            params.put(ServerParam.EN_NAME, en_name);
        }
        if (!TextUtil.isEmpty(mail))
            params.put(ServerParam.MAIL, mail);
        if (!TextUtil.isEmpty(company))
            params.put(ServerParam.COMPANY, company);
        if (experience != 0)
            params.put(ServerParam.EXPERIENCE, experience + "");
        String url = createUrl("/api/v1/user/update.action",params);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    protected static String createUrl(String path, Map<String, String> params) {
        params.put(ServerParam.CLIENT, Constant.CLIEN);
        params.put(ServerParam.EQ_NUM, Constant.ANDROID_ID);
        params.put(ServerParam.VERSION, Utils.getCurrentVersion());
        return NetConstant.BASE_URL + path;
    }
}
