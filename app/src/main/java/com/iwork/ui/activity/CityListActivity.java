package com.iwork.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.impetusconsulting.iwork.R;
import com.iwork.Base.AppService;
import com.iwork.Base.BaseActivity;
import com.iwork.helper.ToastHelper;
import com.iwork.model.CityList;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.preferences.Preferences;
import com.iwork.ui.view.TitleBar;
import com.iwork.utils.CollectionUtil;
import com.iwork.utils.Constant;
import com.iwork.utils.TextUtil;
import com.iwork.utils.Utils;
import com.squareup.okhttp.Request;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CityListActivity extends BaseActivity {

    @Bind(R.id.city_titlebar)
    TitleBar cityTitlebar;
    @Bind(R.id.city_listView)
    ListView cityListView;
    private List<CityList.City> cityList;
    private List<String> cityStrings;

    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        ButterKnife.bind(this);
        cityTitlebar.setTitle("城市列表");
        cityTitlebar.setBackDrawableListener(backListener);
        getCityList();
        EventBus.getDefault().register(this);
    }

    /**
     * 标题栏返回按钮点击监听
     */
    private View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    /**
     * 获取城市列表
     */
    public void getCityList() {
        cityList = new ArrayList<>();
        if (Utils.JudgeNetIsConnectEd(this)) {

            CommonRequest.getCityList(new ResultCallback<CityList>() {
                @Override
                public void onError(Request request, Exception e) {

                }

                @Override
                public void onResponse(final CityList response) {
                    if (response.getInfoCode() == 0) {
                        cityStrings = new ArrayList<>();
                        if (!CollectionUtil.isEmpty(response.getCitys())) {
                            for (CityList.City c : response.getCitys()) {
                                cityStrings.add(c.getAreaName());
                            }
                        }
                        arrayAdapter = new ArrayAdapter<String>(CityListActivity.this, android.R.layout.simple_list_item_1, cityStrings);
                        cityListView.setAdapter(arrayAdapter);
                        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                ToastHelper.showShortCompleted("城市：" + response.getCitys().get(position).getAreaName());
                                int cityid = response.getCitys().get(position).getAreaCode();
                                EventBus.getDefault().post(cityid, Constant.CITY);
                                EventBus.getDefault().post(response.getCitys().get(position).getAreaName(), Constant.CITY);
                                Preferences.getInstance().setCurrentCityId(cityid);
                                finish();
                            }
                        });
                        String citylists = AppService.getsGson().toJson(response);
                        Preferences.getInstance().setCityListModel(citylists);
                    } else {
                        ToastHelper.showShortError(response.getMessage());
                    }
                }
            });
        } else {
            String citys = Preferences.getInstance().getCityListModel();
            if (!TextUtil.isEmpty(citys)) {
                cityStrings = new ArrayList<>();
                final CityList response = AppService.getsGson().fromJson(citys, CityList.class);
                if (!CollectionUtil.isEmpty(response.getCitys())) {
                    for (CityList.City c : response.getCitys()) {
                        cityStrings.add(c.getAreaName());
                    }
                }
                arrayAdapter = new ArrayAdapter<String>(CityListActivity.this, android.R.layout.simple_list_item_1, cityStrings);
                cityListView.setAdapter(arrayAdapter);
                cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ToastHelper.showShortCompleted("城市：" + response.getCitys().get(position).getAreaName());
                        int cityid = response.getCitys().get(position).getAreaCode();
                        EventBus.getDefault().post(cityid, Constant.CITY);
                        EventBus.getDefault().post(response.getCitys().get(position).getAreaName(), Constant.CITY);
                        Preferences.getInstance().setCurrentCityId(cityid);
                        finish();
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
