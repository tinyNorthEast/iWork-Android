package com.iwork.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.model.PersonDetail;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.ui.view.FlowLayout;
import com.iwork.ui.view.TagAdapter;
import com.iwork.ui.view.TagFlowLayout;
import com.iwork.ui.view.TitleBar;
import com.squareup.okhttp.Request;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class PersonDetailActivty extends BaseActivity {

    @Bind(R.id.detail_titlebar)
    TitleBar detailTitlebar;
    @Bind(R.id.detail_myself_des)
    LinearLayout detailMyselfDes;
    @Bind(R.id.detail_person_pic)
    ImageView detailPersonPic;
    @Bind(R.id.detail_person_realname)
    TextView detailPersonRealname;
    @Bind(R.id.detail_profession_taglayout)
    TagFlowLayout detailProfessionTaglayout;
    TextView myself_tv;
    private String[] mVals = new String[]
            {"房地产", "金融", "IT互联网", "消费电子", "服装"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail_activty);
        ButterKnife.bind(this);
        initTitleBar();
        getData();
        addView();
    }

    private void addView() {
        final LayoutInflater mInflater = LayoutInflater.from(this);

        for (int i = 0; i < 5; i++) {
            RelativeLayout rl = (RelativeLayout) mInflater.inflate(R.layout.addtextview, null);
            myself_tv = (TextView) rl.findViewById(R.id.addview_tv);
            myself_tv.setText(String.format("%s、8年以上猎头经验", i + 1));
            detailMyselfDes.addView(rl);
        }
        detailProfessionTaglayout.setAdapter(new TagAdapter<String>(mVals) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {
                TextView t = (TextView) mInflater.inflate(R.layout.profession_tag_tv,detailProfessionTaglayout,false);
                t.setText(o);
                return t;
            }
        });
    }

    private void getData() {
        CommonRequest.getDetail(16, new ResultCallback<PersonDetail>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(PersonDetail response) {
                if (response.getInfoCode() == 0) {
                    detailPersonRealname.setText(response.getData().getHeadhunterInfo().getRealName());
                    Glide.with(PersonDetailActivty.this).load(response.getData().getHeadhunterInfo().getPic())
                            .error(R.drawable.detail_no_pic).placeholder(R.drawable.detail_no_pic);
                }
            }
        });
    }

    private void initTitleBar() {
        detailTitlebar.setTitle("顾问详情");
        detailTitlebar.setCustomImageButtonStore(storeListener);
        detailTitlebar.setShareDrawableListener(shareListener);
        detailTitlebar.setBackDrawableListener(backListener);
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
     * 分享
     */
    private View.OnClickListener shareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            OnekeyShare oks = new OnekeyShare();
            oks.setText("我是分享文本");
            oks.setImagePath("http://f1.sharesdk.cn/imgs/2014/05/21/oESpJ78_533x800.jpg");
            // 启动分享GUI
            oks.show(PersonDetailActivty.this);
        }
    };
    /**
     * 收藏
     */
    private View.OnClickListener storeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
