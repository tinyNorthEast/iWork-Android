package com.iwork.ui.activity;

import android.animation.Animator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.model.PersonDetail;
import com.iwork.model.PersonDetail.DataEntity.CommentListEntity;
import com.iwork.model.PersonDetail.DataEntity.HeadhunterInfoEntity.DescribeListEntity;
import com.iwork.model.PersonDetail.DataEntity.HeadhunterInfoEntity.FunctionsListEntity;
import com.iwork.model.PersonDetail.DataEntity.HeadhunterInfoEntity.IndustryListEntity;
import com.iwork.model.PersonDetail.DataEntity.PerformanceListEntity;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.ui.view.FlowLayout;
import com.iwork.ui.view.ObservableScrollView;
import com.iwork.ui.view.TagAdapter;
import com.iwork.ui.view.TagFlowLayout;
import com.iwork.ui.view.TitleBar;
import com.iwork.utils.CollectionUtil;
import com.squareup.okhttp.Request;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 个人详情页
 */
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
    @Bind(R.id.detail_scrollview)
    ObservableScrollView mScrollview;
    @Bind(R.id.detail_performance_val_layout)
    LinearLayout detailPerformanceValLayout;
    @Bind(R.id.detail_bottom_sendms_layout)
    LinearLayout detailBottomSendmsLayout;
    @Bind(R.id.detail_bottom_call_layout)
    LinearLayout detailBottomCallLayout;
    @Bind(R.id.detail_comment_val_layout)
    LinearLayout detailCommentValLayout;
    @Bind(R.id.detail_comment_more_bt)
    Button detailCommentMoreBt;

    private List<DescribeListEntity> mDescribeVals;
    private List<IndustryListEntity> mIndustryVals;
    private int touchEventId = -9983761;
    private int mDySinceDirectionChange = 0;
    private boolean mIsHiding;
    private boolean mIsShowing;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail_activty);
        ButterKnife.bind(this);
        initTitleBar();
        getData();
        initBottomlayout();
    }

    /**
     * 初始化底部菜单栏
     */
    private void initBottomlayout() {
        mScrollview.setCallbacks(new ObservableScrollView.Callbacks() {
            @Override
            public void onScrollChanged(int dy) {
            }

            @Override
            public void onDownMotionEvent() {
                hide(detailBottomLayout);
            }

            @Override
            public void onUpOrCancelMotionEvent() {
                show(detailBottomLayout);
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
                    phone = response.getData().getHeadhunterInfo().getPhone();
                    detailPersonRealname.setText(response.getData().getHeadhunterInfo().getRealName());
                    Glide.with(PersonDetailActivty.this).load(response.getData().getHeadhunterInfo().getPic())
                            .error(R.drawable.detail_no_pic).placeholder(R.drawable.detail_no_pic).into(detailPersonPic);
                    setDescribeData(response.getData().getHeadhunterInfo().getDescribeList());
                    setIndustryData(response.getData().getHeadhunterInfo().getIndustryList());
                    setFunctionData(response.getData().getHeadhunterInfo().getFunctionsList());
                    setPerformanceData(response.getData().getPerformanceList());
                    setCommentData(response.getData().getCommentList());
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

    /**
     * 添加个人业绩数据
     *
     * @param list
     */
    private void setPerformanceData(List<PerformanceListEntity> list) {
        for (int i = 0; i < list.size(); i++) {
            LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.detail_performance_addview, null);
            TextView tv = (TextView) layout.findViewById(R.id.detail_performance_time_tv);
            tv.setText(list.get(i).getGroupDate());
            LinearLayout itemLayout = (LinearLayout) layout.findViewById(R.id.detail_performance_item_ly);
            List<PerformanceListEntity.ListEntity> listEntities = list.get(i).getList();
            if (!CollectionUtil.isEmpty(listEntities)) {
                for (int j = 0; j < listEntities.size(); j++) {
                    RelativeLayout rl = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.detail_performance_item_addview, null);
                    TextView commpany_tv = (TextView) rl.findViewById(R.id.detail_pf_commpany_tv);
                    TextView position_tv = (TextView) rl.findViewById(R.id.detail_pf_position_tv);
                    TextView annualSalary_tv = (TextView) rl.findViewById(R.id.detail_pf_annualSalary_tv);
                    commpany_tv.setText(listEntities.get(j).getCompanyName());
                    position_tv.setText(listEntities.get(j).getPosition());
                    annualSalary_tv.setText(listEntities.get(j).getAnnualSalary());
                    itemLayout.addView(rl);
                }
            }
            detailPerformanceValLayout.addView(layout);
        }

    }

    /**
     * 设置擅长职能数据
     *
     * @param list
     */
    private void setFunctionData(List<FunctionsListEntity> list) {
        for (int i = 0; i < list.size(); i++) {
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.detail_function_addview, null);
            TextView textView = (TextView) linearLayout.findViewById(R.id.detail_function_addview_tv);
            textView.setText(list.get(i).getFunctionsName());
            detailFunctionValLayout.addView(linearLayout);
        }

    }

    private void setCommentData(List<CommentListEntity> list) {
        int size = list.size();
        if (size>2){
            detailCommentMoreBt.setVisibility(View.VISIBLE);
            detailCommentMoreBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonDetailActivty.this,CommentActivity.class);
                    startActivity(intent);
                }
            });
        }else {
            detailCommentMoreBt.setVisibility(View.GONE);
        }
        CommentListEntity commentListEntity;
        for (int i=0;i<size;i++){
            commentListEntity = list.get(i);
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.commentlist_item_layout,null);
            TextView tv_name = (TextView) linearLayout.findViewById(R.id.comment_name_tv);
            tv_name.setText(commentListEntity.getFromName());
            TextView tv_time = (TextView) linearLayout.findViewById(R.id.comment_time_tv);
            tv_time.setText(commentListEntity.getCreate_time());
            ImageView imageView = (ImageView) linearLayout.findViewById(R.id.comment_img);

            TextView tv_content = (TextView) linearLayout.findViewById(R.id.comment_content_tv);
            tv_content.setText(commentListEntity.getContent());
            detailCommentValLayout.addView(linearLayout);
        }
    }

    /**
     * 给顾问留言
     */
    @OnClick(R.id.detail_bottom_sendms_layout)
    public void sendMessage() {
        Intent intent = new Intent(this, SendMessageActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.detail_bottom_call_layout)
    public void callPerson() {
        if (TextUtils.isEmpty(phone)) {
            return;
        }
        Intent CallIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        startActivity(CallIntent);
    }

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

    /**
     * Hide the quick return view.
     * <p>
     * Animates hiding the view, with the view sliding down and out of the screen.
     * After the view has disappeared, its visibility will change to GONE.
     *
     * @param view The quick return view
     */
    private void hide(final View view) {
        mIsHiding = true;
        ViewPropertyAnimator animator = view.animate()
                .translationY(view.getHeight())
                .setInterpolator(INTERPOLATOR)
                .setDuration(200);

        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                // Prevent drawing the View after it is gone
                mIsHiding = false;
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                // Canceling a hide should show the view
                mIsHiding = false;
                if (!mIsShowing) {
                    show(view);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        animator.start();
    }

    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();

    /**
     * Show the quick return view.
     * <p>
     * Animates showing the view, with the view sliding up from the bottom of the screen.
     * After the view has reappeared, its visibility will change to VISIBLE.
     *
     * @param view The quick return view
     */
    private void show(final View view) {
        mIsShowing = true;
        ViewPropertyAnimator animator = view.animate()
                .translationY(0)
                .setInterpolator(INTERPOLATOR)
                .setDuration(200);

        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mIsShowing = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                // Canceling a show should hide the view
                mIsShowing = false;
                if (!mIsHiding) {
                    hide(view);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        animator.start();
    }
}
