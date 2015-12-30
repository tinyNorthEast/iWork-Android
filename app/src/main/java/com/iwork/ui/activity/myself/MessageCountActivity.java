package com.iwork.ui.activity.myself;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.helper.ResourcesHelper;
import com.iwork.model.MessageCountModel;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.ui.activity.common.MessageActivity;
import com.iwork.ui.view.TitleBar;
import com.iwork.utils.Constant;
import com.squareup.okhttp.Request;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageCountActivity extends BaseActivity {

    @Bind(R.id.message_count_titlebar)
    TitleBar messageCountTitlebar;
    @Bind(R.id.message_count_comment_count)
    TextView messageCountCommentCount;
    @Bind(R.id.message_count_authority_count)
    TextView messageCountAuthorityCount;
    @Bind(R.id.message_count_attention_count)
    TextView messageCountAttentionCount;
    @Bind(R.id.message_count_system_count)
    TextView messageCountSystemCount;
    @Bind(R.id.message_count_comment_layout)
    RelativeLayout messageCountCommentLayout;
    @Bind(R.id.message_count_authority_layout)
    RelativeLayout messageCountAuthorityLayout;
    @Bind(R.id.message_count_attention_layout)
    RelativeLayout messageCountAttentionLayout;
    @Bind(R.id.message_count_system_layout)
    RelativeLayout messageCountSystemLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_count);
        ButterKnife.bind(this);
        messageCountTitlebar.setTitle("我的消息");
        messageCountTitlebar.setBackDrawableListener(backListener);
        getData();
    }

    private void getData() {
        CommonRequest.getMessageCount(new ResultCallback<MessageCountModel>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(MessageCountModel response) {
                if (response.getInfoCode() == 0) {
                    for (MessageCountModel.MessageCount m : response.getData()) {
                        switch (m.getN_type()) {
                            case 1:
                                messageCountCommentCount.setText(String.format(ResourcesHelper.getString(R.string.message_count), m.getTypeCount()));
                                break;
                            case 2:
                                messageCountAuthorityCount.setText(String.format(ResourcesHelper.getString(R.string.message_count), m.getTypeCount()));
                                break;
                            case 3:
                                messageCountAttentionCount.setText(String.format(ResourcesHelper.getString(R.string.message_count), m.getTypeCount()));
                                break;
                            case 4:
                                messageCountSystemCount.setText(String.format(ResourcesHelper.getString(R.string.message_count), m.getTypeCount()));
                                break;
                        }
                    }
                }
            }
        });
    }

    @OnClick(R.id.message_count_comment_layout)
    private void messagecomment() {
        Intent i = new Intent(this, MessageActivity.class);
        i.putExtra(Constant.MESSAGETYPE, 1);
        i.putExtra(Constant.ISFROMSET, true);
        startActivity(i);

    }

    @OnClick(R.id.message_count_authority_layout)
    private void messageauthorty() {
        Intent i = new Intent(this, MessageActivity.class);
        i.putExtra(Constant.MESSAGETYPE, 2);
        i.putExtra(Constant.ISFROMSET, true);
        startActivity(i);
    }

    @OnClick(R.id.message_count_attention_layout)
    private void messageattention() {
        Intent i = new Intent(this, MessageActivity.class);
        i.putExtra(Constant.MESSAGETYPE, 3);
        i.putExtra(Constant.ISFROMSET, true);
        startActivity(i);
    }

    @OnClick(R.id.message_count_system_layout)
    private void messagesystem() {
        Intent i = new Intent(this, MessageActivity.class);
        i.putExtra(Constant.MESSAGETYPE, 4);
        i.putExtra(Constant.ISFROMSET, true);
        startActivity(i);
    }
}
