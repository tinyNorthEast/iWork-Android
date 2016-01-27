package com.iwork.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.iwork.ui.activity.common.MessageActivity;
import com.iwork.utils.Constant;
import com.socks.library.KLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

public class MyMessageReceiver extends BroadcastReceiver {
    private static final String TAG = "MyMessageReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        Bundle bundle = intent.getExtras();
        KLog.i(TAG, intent.getAction() + ", extras: " + printBundle(bundle));
        int n_type = 0;
        try {
            if (bundle != null) {

                JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA,""));
                Iterator<String> it = json.keys();

                while (it.hasNext()) {
                    String myKey = it.next().toString();
                    if (myKey.equals(Constant.N_TYPE)) {
                        n_type = json.optInt(myKey);
                    }
                    KLog.i(TAG, n_type);
                }
            }
        } catch (Exception e) {
            KLog.e(TAG, e.toString());
        }
        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Intent myIntent = new Intent(context, MessageActivity.class);
            myIntent.putExtra(Constant.ISFROMSET, false);
            myIntent.putExtra(Constant.MESSAGETYPE, n_type);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(myIntent);
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey1:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey2:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    KLog.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    KLog.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey3:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

}
