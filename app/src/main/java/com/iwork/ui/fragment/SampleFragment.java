package com.iwork.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.impetusconsulting.iwork.R;
import com.iwork.adapter.recyclerview.BaseAdapterHelper;
import com.iwork.adapter.recyclerview.BaseQuickAdapter;
import com.iwork.adapter.recyclerview.QuickAdapter;
import com.iwork.helper.ToastHelper;
import com.iwork.model.CommonModel;
import com.iwork.model.MainList;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.preferences.Preferences;
import com.iwork.ui.activity.LoginActivity;
import com.iwork.ui.activity.common.CommentActivity;
import com.iwork.ui.activity.persondetail.PersonDetailActivty;
import com.iwork.ui.view.BadgeView;
import com.iwork.ui.view.DividerItemDecoration;
import com.iwork.utils.CollectionUtil;
import com.iwork.utils.Constant;
import com.iwork.utils.LoginUtil;
import com.iwork.utils.UiThreadHandler;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.squareup.okhttp.Request;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 */
public class SampleFragment extends Fragment {
    private int industryid = 0;
    @Bind(R.id.recyclerView)
    XRecyclerView recyclerView;
    @Bind(R.id.main_nodata_img)
    private ImageView main_nodata_img;
    private int pageNo = 1;
    private int cityId;
    private List<MainList.Person> persons = Collections.emptyList();
    QuickAdapter<MainList.Person> mAdapter;
    private OnFragmentInteractionListener mListener;
    private BadgeView badgeView;

    public SampleFragment() {
        // Required empty public constructor
    }

    private static final String ARG_POSITION = "position";

    public static SampleFragment newInstance(int objId) {
        SampleFragment f = new SampleFragment();
        Bundle b = new Bundle();
        b.putInt(Constant.INDUSTRYID, objId);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            industryid = getArguments().getInt(Constant.INDUSTRYID);
        }
    }

    private View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_sample, container, false);
        ButterKnife.bind(this, mRootView);
        cityId = Preferences.getInstance().getCurrentCityId();
//        persons = new ArrayList<>();
        initXRecyclerView();
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        pageNo = 1;
        getData(cityId);
    }

    private void initAdapter(final List<MainList.Person> persons) {

        mAdapter = new QuickAdapter<MainList.Person>(getContext(), R.layout.recycler_item, persons) {
            @Override
            protected void convert(BaseAdapterHelper helper, final MainList.Person item) {
                helper.getTextView(R.id.item_zh_name).setText(item.getRealName());
                Glide.with(getContext()).load(item.getPic()).error(R.drawable.main_no_pic).placeholder(R.drawable.main_no_pic).
                        into(helper.getImageView(R.id.item_pic));
                TextView ranking_flag_tv = helper.getTextView(R.id.item_flag);
                if (item.getRanking() == 1) {
                    ranking_flag_tv.setVisibility(View.VISIBLE);
                    ranking_flag_tv.setText("人气顾问第一名");
                    ranking_flag_tv.setBackgroundResource(R.drawable.main_first);
                } else if (item.getRanking() == 2) {
                    ranking_flag_tv.setVisibility(View.VISIBLE);
                    ranking_flag_tv.setText("人气顾问第二名");
                    ranking_flag_tv.setBackgroundResource(R.drawable.main_second);
                } else if (item.getRanking() == 3) {
                    ranking_flag_tv.setVisibility(View.VISIBLE);
                    ranking_flag_tv.setText("人气顾问第三名");
                    ranking_flag_tv.setBackgroundResource(R.drawable.main_thirdly);
                } else {
                    ranking_flag_tv.setVisibility(View.GONE);
                }
                if (item.getCommentCount() != 0)
                    showBadgeView(helper.getLayout(R.id.item_comment), item.getCommentCount() + "");

                helper.getLayout(R.id.item_comment).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!LoginUtil.isLogin()) {
                            ToastHelper.showShortError(getResources().getString(R.string.no_login));
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            return;
                        }
                        Intent intent = new Intent(getActivity(), CommentActivity.class);
                        intent.putExtra(Constant.COMMENTID, item.getUserId());
                        startActivity(intent);
                    }
                });
                final CheckBox checkBox = helper.getCheckBox(R.id.item_good);
                if (item.getIsAttention() == 1) {
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int isAttention;
                        if (isChecked) {
                            isAttention = 1;
                        } else {
                            isAttention = 0;
                        }
                        CommonRequest.saveAttention(item.getUserId(), isAttention, new ResultCallback<CommonModel>() {
                            @Override
                            public void onError(Request request, Exception e) {
                                checkBox.setChecked(false);
                            }

                            @Override
                            public void onResponse(CommonModel response) {
                                if (response.getInfoCode() == 0) {
                                    ToastHelper.showShortCompleted(response.getMessage());
                                } else if (response.getInfoCode() == Constant.TOKENFAIL) {
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(intent);
                                    checkBox.setChecked(false);
                                    ToastHelper.showShortError(response.getMessage());
                                } else {
                                    ToastHelper.showShortError(response.getMessage());
                                    checkBox.setChecked(false);
                                }
                            }
                        });
                    }
                });
                if (!CollectionUtil.isEmpty(item.getIndustryList())) {
                    int size = item.getIndustryList().size();
                    if (size > 1) {
                        helper.getTextView(R.id.item_position).setText(item.getIndustryList().get(0).getIndustryName());
                        helper.getTextView(R.id.item_decollate).setVisibility(View.VISIBLE);
                        helper.getTextView(R.id.item_role).setText(item.getIndustryList().get(1).getIndustryName());
                    } else {
                        helper.getTextView(R.id.item_position).setText(item.getIndustryList().get(0).getIndustryName());
                    }
                }

            }
        };
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), PersonDetailActivty.class);
                intent.putExtra(Constant.OBJID, persons.get(position).getObjId());
                intent.putExtra(Constant.USERID, persons.get(position).getUserId());
                startActivity(intent);

            }
        });
    }

    /**
     * 初始化列表布局
     */
    private void initXRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setBackgroundColor(getResources().getColor(R.color.white));
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setLaodingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setLoadingMoreEnabled(true);
        recyclerView.setLoadingListener(loadingListener);
    }

    /**
     * 上拉刷新 下拉加载 监听
     */
    private XRecyclerView.LoadingListener loadingListener = new XRecyclerView.LoadingListener() {
        @Override
        public void onRefresh() {
            getData(cityId);
            UiThreadHandler.postOnceDelayed(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                    if (recyclerView != null)
                        recyclerView.refreshComplete();
                }
            }, Constant.REFESHTIME);
        }

        @Override
        public void onLoadMore() {
            pageNo++;
            getDataMore(cityId);
            UiThreadHandler.postOnceDelayed(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                    if (recyclerView != null)
                        recyclerView.loadMoreComplete();
                }
            }, Constant.REFESHTIME);
        }
    };

    private void showBadgeView(View v, String text) {
        badgeView = new BadgeView(getActivity(), v);
        badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badgeView.setBadgeMargin(5, 5);
        badgeView.setBadgeBackgroundColor(getResources().getColor(R.color.color_bt_bg));
        badgeView.setTextSize(8);
        badgeView.setText(text);
        badgeView.show();
    }

    /**
     * 从网络获取数据
     */
    @Subscriber(tag = Constant.CITY)
    public void getData(int cityId) {

        CommonRequest.getPersonList(pageNo, industryid, cityId, new ResultCallback<MainList>() {

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(MainList response) {
                if (response.getInfoCode() == 0) {
                    main_nodata_img.setVisibility(View.GONE);
                    persons = response.getData();
                    initAdapter(persons);
                } else if (response.getInfoCode() == Constant.NODATA) {
                    ToastHelper.showShortError(response.getMessage());
                    main_nodata_img.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void showNoDataView() {
        if (main_nodata_img.getVisibility() != View.VISIBLE)
            main_nodata_img.setVisibility(View.VISIBLE);
    }

    private void hideNoDataView() {
        if (main_nodata_img.getVisibility() == View.VISIBLE) {
            main_nodata_img.setVisibility(View.GONE);
        }
    }

    /**
     * 从网络获取数据
     */
    @Subscriber(tag = Constant.CITY)
    public void getDataMore(int cityId) {

        CommonRequest.getPersonList(pageNo, industryid, cityId, new ResultCallback<MainList>() {

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(MainList response) {
                if (response.getInfoCode() == 0) {
                    if (!CollectionUtil.isEmpty(response.getData())) {
                        hideNoDataView();
                        main_nodata_img.setVisibility(View.GONE);
                        persons.addAll(response.getData());
                        mAdapter.notifyDataSetChanged();
                    }
                } else if (response.getInfoCode() == Constant.NODATA) {
                    recyclerView.loadMoreComplete();
                    recyclerView.setLoadingMoreEnabled(false);
                    ToastHelper.showShortCompleted("已经没有更多数据啦");
                } else {
                    ToastHelper.showShortError(response.getMessage());
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
