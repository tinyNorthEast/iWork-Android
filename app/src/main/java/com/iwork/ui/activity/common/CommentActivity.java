package com.iwork.ui.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.bumptech.glide.Glide;
import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.adapter.recyclerview.BaseAdapterHelper;
import com.iwork.adapter.recyclerview.BaseQuickAdapter;
import com.iwork.adapter.recyclerview.QuickAdapter;
import com.iwork.helper.ToastHelper;
import com.iwork.model.CommentListModel;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.preferences.Preferences;
import com.iwork.ui.activity.persondetail.SendMessageActivity;
import com.iwork.ui.view.CircleTransform;
import com.iwork.ui.view.DividerItemDecoration;
import com.iwork.ui.view.TitleBar;
import com.iwork.utils.CollectionUtil;
import com.iwork.utils.Constant;
import com.iwork.utils.LoginUtil;
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

public class CommentActivity extends BaseActivity {

    @Bind(R.id.comment_titlebar)
    TitleBar commentTitlebar;
    @Bind(R.id.comment_xrecyclerview)
    XRecyclerView commentXrecyclerview;
    private List<CommentListModel.CommentModel> comments = Collections.emptyList();
    private QuickAdapter<CommentListModel.CommentModel> mAdapter;
    private int to_user_id, pageNo;
    private int pageSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        commentTitlebar.setTitle("评论列表");
        commentTitlebar.setBackDrawableListener(backListener);
        to_user_id = getIntent().getIntExtra(Constant.COMMENTID, 0);
        initXRecyclerView();
        getData();
    }

    private void getData() {
        CommonRequest.getCommonentListData(to_user_id, pageNo, pageSize, new ResultCallback<CommentListModel>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(CommentListModel response) {
                if (response.getInfoCode() == 0) {
                    if (!CollectionUtil.isEmpty(response.getData())) {
//                        comments.addAll(response.getData());
                        comments = response.getData();
                        initAdapter();
                    } else {
                        ToastHelper.showShortInfo("当前无评论内容");
                    }

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
                    commentXrecyclerview.refreshComplete();
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
                    commentXrecyclerview.loadMoreComplete();
                }
            }, Constant.REFESHTIME);
        }
    };

    /**
     * 初始化列表布局
     */
    private void initXRecyclerView() {
        commentXrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        commentXrecyclerview.setBackgroundColor(getResources().getColor(R.color.white));
        commentXrecyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        commentXrecyclerview.setLaodingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        commentXrecyclerview.setLoadingMoreEnabled(true);
        commentXrecyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        commentXrecyclerview.setLoadingListener(loadingListener);
    }

    private void initAdapter() {
        mAdapter = new QuickAdapter<CommentListModel.CommentModel>(this, R.layout.commentlist_item_layout, comments) {
            @Override
            protected void convert(BaseAdapterHelper helper, CommentListModel.CommentModel item) {
                helper.getTextView(R.id.comment_name_tv).setText(item.getFromName());
                helper.getTextView(R.id.comment_content_tv).setText(item.getContent());
                helper.getTextView(R.id.comment_time_tv).setText(TimeUtil.formateTimeMMddHHmm(item.getCreate_time()));
                Glide.with(CommentActivity.this).load(item.getPic()).transform(new CircleTransform(CommentActivity.this)).error(R.drawable.head_icon).placeholder(R.drawable.head_icon).into(helper.getImageView(R.id.comment_img));
            }
        };
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int role_code = Preferences.getInstance().getrole_code();
                if (LoginUtil.isLogin() && role_code == Constant.HEADHUNTERID) {
                    int myUserId = Preferences.getInstance().getUserId();
                    if (myUserId != 0 && comments.get(position).getC_from_id() == myUserId) {
                        return;
                    }
                    Intent intent = new Intent(CommentActivity.this, SendMessageActivity.class);
                    intent.putExtra(Constant.C_MAIN_ID, comments.get(position).getMainId());
                    intent.putExtra(Constant.USERID, comments.get(position).getC_from_id());
                    intent.putExtra(Constant.COMMENTTITLE, "回复留言");
                    startActivity(intent);
                }
            }
        });
        commentXrecyclerview.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
