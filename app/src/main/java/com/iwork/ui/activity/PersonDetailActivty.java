package com.iwork.ui.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
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
import com.iwork.model.PersonDetail.DataEntity.HeadhunterInfoEntity.DescribeListEntity;
import com.iwork.model.PersonDetail.DataEntity.HeadhunterInfoEntity.FunctionsListEntity;
import com.iwork.model.PersonDetail.DataEntity.HeadhunterInfoEntity.IndustryListEntity;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.ui.view.FlowLayout;
import com.iwork.ui.view.QuickReturnFooterBehavior;
import com.iwork.ui.view.TagAdapter;
import com.iwork.ui.view.TagFlowLayout;
import com.iwork.ui.view.TitleBar;
import com.squareup.okhttp.Request;

import java.util.List;

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
    @Bind(R.id.detail_function_val_layout)
    LinearLayout detailFunctionValLayout;
    @Bind(R.id.detail_bottom_layout)
    LinearLayout detailBottomLayout;
    @Bind(R.id.detail_coordinatorlayout)
    CoordinatorLayout detailCoordinatorlayout;
    private List<DescribeListEntity> mDescribeVals;
    private List<IndustryListEntity> mIndustryVals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail_activty);
        ButterKnife.bind(this);
        initTitleBar();
        getData();
    }

    private void initTitleBar() {
        detailTitlebar.setTitle("顾问详情");
        detailTitlebar.setCustomImageButtonStore(storeListener);
        detailTitlebar.setShareDrawableListener(shareListener);
        detailTitlebar.setBackDrawableListener(backListener);
    }

    /**
     * 从服务器获取数据
     */
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
                            .error(R.drawable.detail_no_pic).placeholder(R.drawable.detail_no_pic).into(detailPersonPic);
                    setDescribeData(response.getData().getHeadhunterInfo().getDescribeList());
                    setIndustryData(response.getData().getHeadhunterInfo().getIndustryList());
                    setFunctionData(response.getData().getHeadhunterInfo().getFunctionsList());
                }
            }
        });
    }

    /**
     * 添加自我介绍数据
     *
     * @param list
     */
    private void setDescribeData(List<DescribeListEntity> list) {
        for (int i = 0; i < list.size(); i++) {
            RelativeLayout rl = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.detail_des_addview, null);
            myself_tv = (TextView) rl.findViewById(R.id.addview_tv);
            myself_tv.setText(list.get(i).getDescribe());
            detailMyselfDes.addView(rl);
        }
    }

    /**
     * 获取行业数据
     */
    private void setIndustryData(List<IndustryListEntity> list) {
        final LayoutInflater mInflater = LayoutInflater.from(this);
        detailProfessionTaglayout.setAdapter(new TagAdapter<IndustryListEntity>(list) {

            @Override
            public View getView(FlowLayout parent, int position, IndustryListEntity industryListEntity) {
                TextView t = (TextView) mInflater.inflate(R.layout.profession_tag_tv, detailProfessionTaglayout, false);
                t.setText(industryListEntity.getIndustryName());
                return t;
            }
        });
    }

    private void setFunctionData(List<FunctionsListEntity> list) {
        for (int i = 0; i < list.size(); i++) {
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.detail_function_addview, null);
            TextView textView = (TextView) linearLayout.findViewById(R.id.detail_function_addview_tv);
            textView.setText(list.get(i).getFunctionsName());
            detailFunctionValLayout.addView(linearLayout);
        }

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
