package com.tjsinfo.tjpark.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

/**
 * Created by panning on 2018/5/22.
 */
//网络状态检测
public class NetWorkStatusReceiver extends BroadcastReceiver {

    public NetWorkStatusReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            Toast.makeText(context, "network changed", Toast.LENGTH_LONG).show();
//            BaseActivity.isNetWorkConnected = NetWorkUtils.getAPNType(context)>0;
        }
    }
}



