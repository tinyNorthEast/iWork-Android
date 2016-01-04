package com.iwork.ui.activity.common;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.adapter.recyclerview.BaseAdapterHelper;
import com.iwork.adapter.recyclerview.QuickAdapter;
import com.iwork.helper.ToastHelper;
import com.iwork.model.AttentionListModel;
import com.iwork.model.CommentListModel;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.ui.view.DividerItemDecoration;
import com.iwork.ui.view.TitleBar;
import com.iwork.utils.CollectionUtil;
import com.iwork.utils.Constant;
import com.iwork.utils.TimeUtil;
import com.iwork.utils.UiThreadHandler;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AttentionActivity extends BaseActivity {

    @Bind(R.id.attention_titlebar)
    TitleBar attentionTitlebar;
    private int searchType;
    private int pageNo=1;
    private int pageSize = 10;
    @Bind(R.id.attention_xrecyclerview)
    XRecyclerView attentionXrecyclerview;
    private List<AttentionListModel.Attention> attentions = Collections.emptyList();
    private QuickAdapter<AttentionListModel.Attention> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention);
        ButterKnife.bind(this);
        initXRecyclerView();
        searchType = getIntent().getIntExtra(Constant.SEARCHTYPE, 1);
        if (searchType == 1) {
            attentionTitlebar.setTitle("我的关注");
        } else {
            attentionTitlebar.setTitle("谁关注了我");
        }
        attentionTitlebar.setBackDrawableListener(backListener);
        getData();
    }

    private void getData() {
        CommonRequest.getAttentionList(searchType, pageNo, pageSize, new ResultCallback<AttentionListModel>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(AttentionListModel response) {
                if (response.getInfoCode() == 0) {
                    if (CollectionUtil.isEmpty(response.getData())){
                        ToastHelper.showShortInfo("当前没有消息");
                        return;
                    }
                    attentions=response.getData();
                    initAdapter();
                }
            }
        });
    }

    private XRecyclerView.LoadingListener loadingListener = new XRecyclerView.LoadingListener() {
        @Override
        public void onRefresh() {
            pageNo = 1;
            getData();
            UiThreadHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    attentionXrecyclerview.refreshComplete();
                }
            }, Constant.REFESHTIME);
        }

        @Override
        public void onLoadMore() {
            pageNo++;
            getData();
            UiThreadHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    attentionXrecyclerview.loadMoreComplete();
                }
            }, Constant.REFESHTIME);
        }
    };

    /**
     * 初始化列表布局
     */
    private void initXRecyclerView() {
        attentionXrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        attentionXrecyclerview.setBackgroundColor(getResources().getColor(R.color.white));
        attentionXrecyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        attentionXrecyclerview.setLaodingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        attentionXrecyclerview.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        attentionXrecyclerview.setLoadingMoreEnabled(true);
        attentionXrecyclerview.setLoadingListener(loadingListener);
    }

    private void initAdapter() {
        mAdapter = new QuickAdapter<AttentionListModel.Attention>(this, R.layout.attentionlist_item_layout, attentions) {
            @Override
            protected void convert(BaseAdapterHelper helper, AttentionListModel.Attention item) {
                helper.getTextView(R.id.attention_name_tv).setText(item.getZh_name());
                helper.getTextView(R.id.attention_time_tv).setText(TimeUtil.formateTimeMMddHHmm(item.getCreate_time()));
                Glide.with(AttentionActivity.this).load(item.getPic()).into(helper.getImageView(R.id.attention_img));
            }
        };
        attentionXrecyclerview.setAdapter(mAdapter);
    }
}
