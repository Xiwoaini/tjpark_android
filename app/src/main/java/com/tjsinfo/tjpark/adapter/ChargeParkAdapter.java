package com.tjsinfo.tjpark.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tjsinfo.tjpark.R;
import com.tjsinfo.tjpark.entity.Car;
import com.tjsinfo.tjpark.entity.ChargePark;

import java.util.List;

/**
 * Created by panning on 2018/7/2.
 */

public class ChargeParkAdapter extends ArrayAdapter<ChargePark> {

    private int resourceId;

    public ChargeParkAdapter(Context context, int textViewResourceId,
                      List<ChargePark> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChargePark chargePark = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView chargeParkId = (TextView) view.findViewById(R.id.chargeParkId);
        TextView numbers = (TextView) view.findViewById(R.id.numbers);
        TextView parkStatus = (TextView) view.findViewById(R.id.parkStatus);

        switch (chargePark.getStatus())
        {
            case "0":
                //离网
                parkStatus.setText("离网");
                break;
            case "1":
                //空闲
                parkStatus.setText("空闲");
                break;
            case "2":
                //占用(未充电)
                parkStatus.setText("占用");
                break;
            case "3":
                //占用(充电中)
                parkStatus.setText("占用");
                break;
            case "4":
                //占用(预约锁定)
                parkStatus.setText("占用");
                break;
            case "255":
                //故障
                parkStatus.setText("故障");
                break;
                default:
                    parkStatus.setText("暂用");
                    break;
        }
        String num = chargePark.getNumbers();
        numbers.setText(num);
        if (num.length() > 3){
            chargeParkId.setText(chargePark.getNumbers().substring(num.length()-3,num.length()));
        }
else{
            chargeParkId.setText("无法获取");
        }

        return view;
    }


}
