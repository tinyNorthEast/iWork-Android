package com.iwork.ui.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.TokenWatcher;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.daimajia.swipe.util.Attributes;
import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.adapter.recyclerview.RecyclerViewAdapter;
import com.iwork.helper.ToastHelper;
import com.iwork.model.MessageList;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.ui.activity.MainActivity;
import com.iwork.ui.view.DividerItemDecoration;
import com.iwork.ui.view.TitleBar;
import com.iwork.utils.CollectionUtil;
import com.iwork.utils.Constant;
import com.iwork.utils.UiThreadHandler;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MessageActivity extends BaseActivity {

    @Bind(R.id.message_titlebar)
    TitleBar messageTitlebar;
    @Bind(R.id.message_xrecyclerView)
    XRecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private int messageType;
    private List<MessageList.MessageDataEntity> messageLists = Collections.emptyList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        messageTitlebar.setTitle("消息列表");
        messageType = getIntent().getIntExtra(Constant.MESSAGETYPE, 1);
        boolean isfromset = getIntent().getBooleanExtra(Constant.ISFROMSET, true);
        if (isfromset) {
            messageTitlebar.setBackDrawableListener(backListener);
        } else {
            messageTitlebar.setBackDrawableListener(mBackListener);
        }
        initXRecyclerView();
        getMessageDate();
    }

    private void getMessageDate() {
        CommonRequest.getMessageListData(messageType,pageNo, new ResultCallback<MessageList>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(MessageList response) {
                if (response.getInfoCode() == 0) {
                    if (!CollectionUtil.isEmpty(response.getData())) {
                        messageLists=response.getData();
                        initAdaper(messageLists);
                    }
                } else if (response.getInfoCode()==Constant.NODATA){
                    ToastHelper.showShortError(response.getMessage());
                }
            }
        });
    }

    private void getMessageMoreDate() {
        CommonRequest.getMessageListData(messageType,pageNo, new ResultCallback<MessageList>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(MessageList response) {
                if (response.getInfoCode() == 0) {
                    if (!CollectionUtil.isEmpty(response.getData())) {
                        messageLists.addAll(response.getData());
                    }
                } else if (response.getInfoCode()==Constant.NODATA){
                    recyclerView.setLoadingMoreEnabled(false);
                    ToastHelper.showShortError(response.getMessage());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            loadingListener.onRefresh();
        }
    }

    /**
     * 初始化列表布局
     */
    private void initXRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setBackgroundColor(getResources().getColor(R.color.white));
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setLaodingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLoadingMoreEnabled(true);
        recyclerView.setLoadingListener(loadingListener);
    }

    private void initAdaper(List<MessageList.MessageDataEntity> list) {
        mAdapter = new RecyclerViewAdapter(this, list);
        ((RecyclerViewAdapter) mAdapter).setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(mAdapter);
    }

    private int pageNo = 1;
    /**
     * 上拉刷新 下拉加载 监听
     */
    private XRecyclerView.LoadingListener loadingListener = new XRecyclerView.LoadingListener() {
        @Override
        public void onRefresh() {
            getMessageDate();
            mAdapter.notifyDataSetChanged();
            UiThreadHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.refreshComplete();
                }
            }, Constant.REFESHTIME);
        }

        @Override
        public void onLoadMore() {
            pageNo++;
            getMessageMoreDate();
            mAdapter.notifyDataSetChanged();
            UiThreadHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.loadMoreComplete();
                }
            }, Constant.REFESHTIME);
        }
    };
    private View.OnClickListener mBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MessageActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    };

    private ArrayList<String> createItemList() {
        ArrayList<String> itemList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            itemList.add("Item " + i);
        }
        return itemList;
    }

}
