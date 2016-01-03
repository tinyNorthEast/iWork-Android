package com.iwork.adapter.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl;
import com.impetusconsulting.iwork.R;
import com.iwork.helper.ToastHelper;
import com.iwork.model.CommonModel;
import com.iwork.model.MessageList;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.ui.activity.common.CommentActivity;
import com.iwork.ui.activity.persondetail.PersonDetailActivty;
import com.iwork.utils.Constant;
import com.jcodecraeer.xrecyclerview.progressindicator.indicator.BallRotateIndicator;
import com.squareup.okhttp.Request;

import java.util.Collections;
import java.util.List;

import butterknife.OnClick;

public class RecyclerViewAdapter extends RecyclerSwipeAdapter<RecyclerViewAdapter.SimpleViewHolder> {

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView message_content_title, message_content_content;
        LinearLayout dele_ly, button_ly;
        Button bt_cancel, bt_confim;
        ImageView iv_message_content_iv;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.message_swipe);
            message_content_title = (TextView) itemView.findViewById(R.id.message_item_content_title_tv);
            message_content_content = (TextView) itemView.findViewById(R.id.message_item_content_tv);
            button_ly = (LinearLayout) itemView.findViewById(R.id.message_item_content_bt_layout);
            dele_ly = (LinearLayout) itemView.findViewById(R.id.message_item_dele_layout);
            bt_cancel = (Button) itemView.findViewById(R.id.message_item_content_bt_cancel);
            bt_confim = (Button) itemView.findViewById(R.id.message_item_content_bt_confim);
            iv_message_content_iv = (ImageView) itemView.findViewById(R.id.message_item_content_iv);

        }
    }

    private Context mContext;
    private List<MessageList.MessageDataEntity> mDataset = Collections.emptyList();


    public RecyclerViewAdapter(Context context, List<MessageList.MessageDataEntity> objects) {
        this.mContext = context;
        this.mDataset = objects;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_dele_layout, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        final MessageList.MessageDataEntity item = mDataset.get(position);
        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        viewHolder.dele_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonRequest.deleteMessage(mDataset.get(position).getObjId(), new ResultCallback<CommonModel>() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(CommonModel response) {
                        if (response.getInfoCode() == 0) {
                            mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                            mDataset.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, mDataset.size());
                            mItemManger.closeAllItems();
                        }
                    }
                });
            }
        });
        viewHolder.message_content_title.setText(item.getContent());
//        viewHolder.swipeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (item.getN_type()){
//                    case 1:
//                        Intent intent = new Intent(mContext, CommentActivity.class);
//                        intent.putExtra(Constant.COMMENTID, item.getRecord_id());
//                        mContext.startActivity(intent);
//                        break;
//                    case 3:
//                        Intent intent3 = new Intent(mContext, PersonDetailActivty.class);
//                        intent3.putExtra(Constant.OBJID, item.getRecord_id());
//                        mContext.startActivity(intent3);
//                        break;
//                }
//            }
//        });
        if (item.getN_type() == 2) {
            viewHolder.button_ly.setVisibility(View.VISIBLE);
            viewHolder.bt_confim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonRequest.updataAuth(item.getRecord_id(), 2, new ResultCallback<CommonModel>() {
                        @Override
                        public void onError(Request request, Exception e) {

                        }

                        @Override
                        public void onResponse(CommonModel response) {
                            if (response.getInfoCode() == 0) {
                                ToastHelper.showShortCompleted(response.getMessage());
                                viewHolder.dele_ly.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            });
            viewHolder.bt_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonRequest.updataAuth(item.getRecord_id(), 0, new ResultCallback<CommonModel>() {
                        @Override
                        public void onError(Request request, Exception e) {

                        }

                        @Override
                        public void onResponse(CommonModel response) {
                            if (response.getInfoCode() == 0) {
                                ToastHelper.showShortCompleted(response.getMessage());
                                viewHolder.dele_ly.setVisibility(View.GONE);
                            }
                        }
                    });

                }
            });
        } else {
            viewHolder.button_ly.setVisibility(View.GONE);
        }
        mItemManger.bindView(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.message_swipe;
    }
}
