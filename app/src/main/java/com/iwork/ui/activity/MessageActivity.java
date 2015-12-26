package com.iwork.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.ui.view.TitleBar;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MessageActivity extends BaseActivity {

    @Bind(R.id.message_titlebar)
    TitleBar messageTitlebar;
    @Bind(R.id.message_xrecyclerView)
    XRecyclerView messageXrecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        messageTitlebar.setTitle("消息列表");
        messageTitlebar.setBackDrawableListener(mBackListener);
    }

    private View.OnClickListener mBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MessageActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    };
}
