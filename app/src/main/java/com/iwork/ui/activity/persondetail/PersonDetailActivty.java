package com.iwork.ui.activity.persondetail;

import android.animation.Animator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.helper.ResourcesHelper;
import com.iwork.helper.ToastHelper;
import com.iwork.model.CommonModel;
import com.iwork.model.PersonDetail;
import com.iwork.model.PersonDetail.DataEntity.CommentListEntity;
import com.iwork.model.PersonDetail.DataEntity.HeadhunterInfoEntity.DescribeListEntity;
import com.iwork.model.PersonDetail.DataEntity.HeadhunterInfoEntity.FunctionsListEntity;
import com.iwork.model.PersonDetail.DataEntity.HeadhunterInfoEntity.IndustryListEntity;
import com.iwork.model.PersonDetail.DataEntity.PerformanceListEntity;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.preferences.Preferences;
import com.iwork.ui.activity.LoginActivity;
import com.iwork.ui.activity.common.CommentActivity;
import com.iwork.ui.dialog.CommonDialog;
import com.iwork.ui.view.CircleTransform;
import com.iwork.ui.view.FlowLayout;
import com.iwork.ui.view.ObservableScrollView;
import com.iwork.ui.view.TagAdapter;
import com.iwork.ui.view.TagFlowLayout;
import com.iwork.ui.view.TitleBar;
import com.iwork.utils.CollectionUtil;
import com.iwork.utils.Constant;
import com.iwork.utils.LoginUtil;
import com.iwork.utils.TextUtil;
import com.iwork.utils.TimeUtil;
import com.socks.library.KLog;
import com.squareup.okhttp.Request;

import org.simple.eventbus.EventBus;

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
    TagFlowLayout detailFunctionValLayout;
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
    @Bind(R.id.detail_person_favorite_iv)
    CheckBox detailPersonFavorite;
    @Bind(R.id.detail_comment_layout)
    RelativeLayout detailCommentLayout;
    @Bind(R.id.detail_performance_bt)
    Button detailPerformanceBt;

    private List<DescribeListEntity> mDescribeVals;
    private List<IndustryListEntity> mIndustryVals;
    private int touchEventId = -9983761;
    private int mDySinceDirectionChange = 0;
    private boolean mIsHiding;
    private boolean mIsShowing;
    private String phone, hr_mail;
    private int objId;
    private int headhunter_id;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail_activty);
        ButterKnife.bind(this);
        initTitleBar();
        objId = getIntent().getIntExtra(Constant.OBJID, 0);
        userId = getIntent().getIntExtra(Constant.USERID, 0);
        hr_mail = Preferences.getInstance().getmail();
        getData();
        initBottomlayout();
        EventBus.getDefault().register(this);
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
        CommonRequest.getDetail(objId, new ResultCallback<PersonDetail>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(PersonDetail response) {
                if (response.getInfoCode() == 0) {
                    phone = response.getData().getHeadhunterInfo().getPhone400();
                    headhunter_id = response.getData().getHeadhunterInfo().getUserId();
                    userId = response.getData().getHeadhunterInfo().getUserId();
                    detailPersonRealname.setText(response.getData().getHeadhunterInfo().getRealName());
                    Glide.with(PersonDetailActivty.this).load(response.getData().getHeadhunterInfo().getPic()).fitCenter().centerCrop()
                            .error(R.drawable.detail_no_pic).placeholder(R.drawable.detail_no_pic).into(detailPersonPic);
                    setDescribeData(response.getData().getHeadhunterInfo().getDescribeList());
                    setIndustryData(response.getData().getHeadhunterInfo().getIndustryList());
                    setFunctionData(response.getData().getHeadhunterInfo().getFunctionsList());
                    setPerformanceData(response.getData().getPerformanceList());
                    setCommentData(response.getData().getCommentList());
                    int role_code = Preferences.getInstance().getrole_code();
                    if (LoginUtil.isLogin() && role_code == Constant.COMMPANYHRID && response.getData().getHeadhunterInfo().getIsAuth() == 0) {
                        detailPerformanceBt.setVisibility(View.VISIBLE);
                    } else {
                        detailPerformanceBt.setVisibility(View.GONE);
                    }
                    if (response.getData().getHeadhunterInfo().getIsAttention() == 1) {
                        detailPersonFavorite.setChecked(true);
                    } else {
                        detailPersonFavorite.setChecked(false);
                    }
                    int size = response.getData().getHeadhunterInfo().getCommentCount();
                    if (size > 2) {
                        detailCommentMoreBt.setVisibility(View.VISIBLE);
                        detailCommentMoreBt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(PersonDetailActivty.this, CommentActivity.class);
                                intent.putExtra(Constant.COMMENTID, headhunter_id);
                                startActivity(intent);
                            }
                        });
                    } else {
                        detailCommentMoreBt.setVisibility(View.GONE);
                    }
                    setFavorite(response.getData().getHeadhunterInfo().getIsAttention());
                } else if (response.getInfoCode() == Constant.TOKENFAIL) {
                    ToastHelper.showShortError(response.getMessage());
                    LoginUtil.goToLogin(PersonDetailActivty.this);
                } else {
                    ToastHelper.showShortError(response.getMessage());
                }
            }
        });
    }

    /**
     * 申请查看权限
     */
    @OnClick(R.id.detail_performance_bt)
    public void getAuthoried() {
        final CommonDialog dialog = new CommonDialog(this);
        dialog.setTitle("请确认您的邮箱");
        dialog.show();
        dialog.setEdVisable(View.VISIBLE);
        dialog.setEdText(hr_mail);
        dialog.setListener(new CommonDialog.CommonDialogListener() {
            @Override
            public void submit() {
                String email = dialog.getEdtext();
                if (TextUtil.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    ToastHelper.showShortError("请正确填写您的邮箱");
                    return;
                }
                CommonRequest.getAuth(headhunter_id, email, new ResultCallback<CommonModel>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        KLog.e("getAuthoried", e.toString());
                    }

                    @Override
                    public void onResponse(CommonModel response) {
                        if (response.getInfoCode() == 0) {
                            ToastHelper.showLongCompleteMessage(ResourcesHelper.getString(R.string.get_author_success));
                            detailPerformanceBt.setVisibility(View.GONE);
                        } else if (response.getInfoCode() == Constant.TOKENFAIL) {
                            ToastHelper.showShortError(response.getMessage());
                            LoginUtil.goToLogin(PersonDetailActivty.this);
                        } else {
                            ToastHelper.showShortError(response.getMessage());
                        }
                        dialog.dismiss();
                    }
                });
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
//        for (int i = 0; i < list.size(); i++) {
//            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.detail_function_addview, null);
//            TextView textView = (TextView) linearLayout.findViewById(R.id.detail_function_addview_tv);
//            textView.setText(list.get(i).getFunctionsName());
//            detailFunctionValLayout.addView(linearLayout);
//        }
        final LayoutInflater mInflater = LayoutInflater.from(this);
        detailFunctionValLayout.setAdapter(new TagAdapter<FunctionsListEntity>(list) {

            @Override
            public View getView(FlowLayout parent, int position, FunctionsListEntity functionsListEntity) {
                TextView t = (TextView) mInflater.inflate(R.layout.profession_tag_tv, detailProfessionTaglayout, false);
                t.setText(functionsListEntity.getFunctionsName());
                return t;
            }
        });

    }

    /**
     * 获取评论列表
     *
     * @param list
     */
    private void setCommentData(List<CommentListEntity> list) {

        CommentListEntity commentListEntity;
        for (int i = 0; i < list.size(); i++) {
            commentListEntity = list.get(i);
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.commentlist_item_layout, null);
            TextView tv_name = (TextView) linearLayout.findViewById(R.id.comment_name_tv);
            tv_name.setText(commentListEntity.getFromName());
            TextView tv_time = (TextView) linearLayout.findViewById(R.id.comment_time_tv);
            tv_time.setText(TimeUtil.formatDateInSimple2(commentListEntity.getCreate_time()));
            ImageView imageView = (ImageView) linearLayout.findViewById(R.id.comment_img);
            Glide.with(this).load(commentListEntity.getPic()).transform(new CircleTransform(this)).error(R.drawable.myself_head).placeholder(R.drawable.myself_head).into(imageView);
            TextView tv_content = (TextView) linearLayout.findViewById(R.id.comment_content_tv);
            tv_content.setText(commentListEntity.getContent());
            detailCommentValLayout.addView(linearLayout);
        }
    }

    /**
     * 点击收藏
     */
    public void setFavorite(final int favorite) {
        if (userId ==Preferences.getInstance().getUserId()){
            return;
        }
        detailPersonFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final int isAttention;
                if (isChecked) {
                    isAttention = 1;
                } else {
                    isAttention = 0;
                }
                CommonRequest.saveAttention(userId, isAttention, new ResultCallback<CommonModel>() {
                    @Override
                    public void onError(Request request, Exception e) {
//                        detailPersonFavorite.setChecked(false);
                        setCheckBoxStatus(detailPersonFavorite,favorite);
                    }

                    @Override
                    public void onResponse(CommonModel response) {
                        if (response.getInfoCode() == 0) {
                            ToastHelper.showShortCompleted(response.getMessage());
                            EventBus.getDefault().post(isAttention,Constant.ATTENTION_STATE);
                        } else if (response.getInfoCode() == Constant.TOKENFAIL) {
                            Intent intent = new Intent(PersonDetailActivty.this, LoginActivity.class);
                            startActivity(intent);
                            setCheckBoxStatus(detailPersonFavorite,favorite);
                            ToastHelper.showShortError(response.getMessage());
                        } else {
                            ToastHelper.showShortError(response.getMessage());
                            setCheckBoxStatus(detailPersonFavorite,favorite);
                        }
                    }
                });
            }
        });
    }

    /**
     * 进入详情页
     */
    @OnClick(R.id.detail_comment_layout)
    public void goToComment() {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra(Constant.COMMENTID, headhunter_id);
        startActivity(intent);
    }

    /**
     * 给顾问留言
     */
    @OnClick(R.id.detail_bottom_sendms_layout)
    public void sendMessage() {
        if (!LoginUtil.isLogin()) {
            ToastHelper.showShortError(R.string.no_login);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }
        Intent intent = new Intent(this, SendMessageActivity.class);
        intent.putExtra(Constant.C_MAIN_ID, headhunter_id);
        intent.putExtra(Constant.USERID, userId);
        intent.putExtra(Constant.COMMENTTITLE, "给顾问留言");
        startActivity(intent);
    }

    /**
     * 打电话
     */
    @OnClick(R.id.detail_bottom_call_layout)
    public void callPerson() {
        if (TextUtils.isEmpty(phone)) {
            return;
        }
        Intent CallIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
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
     * <p/>
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

    /**
     * 设置关注图标的状态
     * @param checkBox
     * @param attention
     */

    private void setCheckBoxStatus(CheckBox checkBox, int attention) {
        if (attention == 1) {
            checkBox.setBackgroundResource(R.drawable.common_store_select);
        } else {
            checkBox.setBackgroundResource(R.drawable.myself_set_attention);
        }
    }

    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();

    /**
     * Show the quick return view.
     * <p/>
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
