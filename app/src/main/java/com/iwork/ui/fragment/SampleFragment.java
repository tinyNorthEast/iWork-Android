package com.iwork.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.impetusconsulting.iwork.R;
import com.iwork.adapter.recyclerview.BaseAdapterHelper;
import com.iwork.adapter.recyclerview.BaseQuickAdapter;
import com.iwork.adapter.recyclerview.QuickAdapter;
import com.iwork.helper.ResourcesHelper;
import com.iwork.helper.ToastHelper;
import com.iwork.model.MainList;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.ui.activity.LoginActivity;
import com.iwork.ui.activity.PersonDetailActivty;
import com.iwork.ui.view.BadgeView;
import com.iwork.utils.CollectionUtil;
import com.iwork.utils.LoginUtil;
import com.iwork.utils.UiThreadHandler;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 */
public class SampleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static int POSITION = 0;
    @Bind(R.id.recyclerView)
    XRecyclerView recyclerView;
    private int pageNo = 1;
//    private List<MainList.Person> persons;
    QuickAdapter<MainList.Person> mAdapter;
    private OnFragmentInteractionListener mListener;
    private BadgeView badgeView;

    public SampleFragment() {
        // Required empty public constructor
    }

    private static final String ARG_POSITION = "position";

    public static SampleFragment newInstance(int position) {
        SampleFragment f = new SampleFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            POSITION = getArguments().getInt(ARG_POSITION);
        }
    }

    private View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_sample, container, false);
        ButterKnife.bind(this, mRootView);
        getData();
        initXRecyclerView();
        return mRootView;
    }

    private void initAdapter(List<MainList.Person> persons) {

        mAdapter = new QuickAdapter<MainList.Person>(getContext(), R.layout.recycler_item, persons) {
            @Override
            protected void convert(BaseAdapterHelper helper, MainList.Person item) {
                if (CollectionUtil.isEmpty(item.getIndustryList())){
                    return;
                }
                helper.getTextView(R.id.item_position).setText(item.getIndustryList().get(0).getIndustryName());
                helper.getTextView(R.id.item_zh_name).setText(item.getRealName());
                Glide.with(getContext()).load(item.getPic()).error(R.drawable.main_no_pic).placeholder(R.drawable.main_no_pic).
                        into(helper.getImageView(R.id.item_pic));

                showBadgeView(helper.getLayout(R.id.item_comment), item.getCommentCount() + "");
            }
        };
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                    Intent intent = new Intent(getActivity(), PersonDetailActivty.class);
                    intent.putExtra("position", position);
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
            mAdapter.notifyDataSetChanged();
            UiThreadHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.refreshComplete();
                }
            }, 5000);
        }

        @Override
        public void onLoadMore() {
            mAdapter.notifyDataSetChanged();
            UiThreadHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.loadMoreComplete();
                }
            }, 5000);
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
    public void getData() {

        CommonRequest.getPersonList(pageNo, new ResultCallback<MainList>() {

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(MainList response) {
                if (response.getInfoCode() == 0) {
                    initAdapter(response.getData());
                }
            }
        });
    }

    public static class RecyclerItemViewHolder extends RecyclerView.ViewHolder {

        private TextView mItemTextView;

        public RecyclerItemViewHolder(View p) {
            super(p);
        }

        public void setItemText(CharSequence text) {
            mItemTextView.setText(text);
        }
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<String> mItemList;

        public RecyclerAdapter(List<String> mItemList) {
            this.mItemList = mItemList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
            return new RecyclerItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            RecyclerItemViewHolder holder = (RecyclerItemViewHolder) viewHolder;
            String itemtext = mItemList.get(position);
            holder.setItemText(itemtext);
        }

        @Override
        public int getItemCount() {
            return mItemList == null ? 0 : mItemList.size();
        }
    }

    private List<String> createItemList() {
        List<String> itemList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            itemList.add("Item " + i);
        }
        return itemList;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
