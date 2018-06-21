package com.tjsinfo.tjpark.fragment;

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

/**
 * Created by panning on 2018/6/13.
 */

public class MapFragment  extends Fragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = new Intent();
        //(当前Activity，目标Activity)
        intent.setClass(getActivity(), MapActivity.class);
        startActivity(intent);
    }


}
