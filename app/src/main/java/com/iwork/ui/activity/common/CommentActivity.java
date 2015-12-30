package com.iwork.ui.activity.common;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.adapter.recyclerview.BaseAdapterHelper;
import com.iwork.adapter.recyclerview.QuickAdapter;
import com.iwork.model.CommentListModel;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.ui.view.TitleBar;
import com.iwork.utils.CollectionUtil;
import com.iwork.utils.Constant;
import com.iwork.utils.TimeUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.squareup.okhttp.Request;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CommentActivity extends BaseActivity {

    @Bind(R.id.comment_titlebar)
    TitleBar commentTitlebar;
    @Bind(R.id.comment_xrecyclerview)
    XRecyclerView commentXrecyclerview;
    private List<CommentListModel.CommentModel> comments;
    private QuickAdapter<CommentListModel.CommentModel> mAdapter;
    private int to_user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        commentTitlebar.setTitle("评论列表");
        commentTitlebar.setBackDrawableListener(backListener);
        to_user_id = getIntent().getIntExtra(Constant.COMMENTID,0);
        getData();
    }

    private void getData() {
        CommonRequest.getCommonentListData(to_user_id, new ResultCallback<CommentListModel>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(CommentListModel response) {
                if (response.getInfoCode() == 0) {
                    if (!CollectionUtil.isEmpty(response.getData())) {
                        comments.addAll(response.getData());
                        initAdapter();
                    }

                }
            }
        });
    }

    private void initAdapter() {
        mAdapter = new QuickAdapter<CommentListModel.CommentModel>(this, R.layout.commentlist_item_layout, comments) {
            @Override
            protected void convert(BaseAdapterHelper helper, CommentListModel.CommentModel item) {
                helper.getTextView(R.id.comment_content_tv).setText(item.getContent());
                helper.getTextView(R.id.comment_time_tv).setText(TimeUtil.formateTimeMMddHHmm(item.getCreate_time()));
                Glide.with(CommentActivity.this).load(item.getPic()).into(helper.getImageView(R.id.comment_img));
            }
        };
        commentXrecyclerview.setAdapter(mAdapter);
    }
}
