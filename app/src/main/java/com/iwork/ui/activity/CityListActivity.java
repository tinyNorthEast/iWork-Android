package com.iwork.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.impetusconsulting.iwork.R;
import com.iwork.helper.ToastHelper;
import com.iwork.model.CityList;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.ui.view.TitleBar;
import com.iwork.utils.CollectionUtil;
import com.iwork.utils.Utils;
import com.squareup.okhttp.Request;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CityListActivity extends AppCompatActivity {

    @Bind(R.id.city_titlebar)
    TitleBar cityTitlebar;
    @Bind(R.id.city_listView)
    ListView cityListView;
    private List<CityList.City> cityList;
    private List<String> cityStrings;

    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        ButterKnife.bind(this);
        cityTitlebar.setTitle("城市列表");
        cityTitlebar.setBackDrawableListener(backListener);
        getCityList();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, cityStrings);
        cityListView.setAdapter(arrayAdapter);
        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastHelper.showShortCompleted("城市：" + cityList.get(position).getAreaName());
            }
        });
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
        CommonRequest.getCityList(new ResultCallback<CityList>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(CityList response) {
                if (response.getInfoCode() == 0) {
                    cityStrings = new ArrayList<>();
                    if (!CollectionUtil.isEmpty(response.getCityLists())) {
                        for (CityList.City c : response.getCityLists()) {
                            cityStrings.add(c.getAreaName());
                        }
                    }
                    arrayAdapter = new ArrayAdapter(CityListActivity.this, android.R.layout.simple_list_item_1, cityStrings);
                    cityListView.setAdapter(arrayAdapter);
                    cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ToastHelper.showShortCompleted("城市：" + cityList.get(position).getAreaName());
                        }
                    });
                } else {
                    ToastHelper.showShortError(response.getMessage());
                }
            }
        });

    }
}
