package com.tjsinfo.tjpark.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tjsinfo.tjpark.R;
import com.tjsinfo.tjpark.activity.CarLifeDisPlayActivity;
import com.tjsinfo.tjpark.activity.MapActivity;
import com.tjsinfo.tjpark.activity.MyCarActivity;
import com.tjsinfo.tjpark.util.TjParkUtils;

/**
 * Created by panning on 2018/6/13.
 */

public class MapFragment  extends Fragment {
    Dialog d;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        d = TjParkUtils.createLoadingDialog(getActivity(),"加载中...");
        Intent intent = new Intent();
        intent.setClass(getActivity(), MapActivity.class);
        startActivity(intent);
        TjParkUtils.closeDialog(d);
    }


}
