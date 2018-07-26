package com.tjsinfo.tjpark.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import com.tjsinfo.tjpark.R;
import com.tjsinfo.tjpark.entity.Order;

/**
 * Created by panning on 2018/1/25.
 */
/**
 * listview自定义格式的适配器
 */

public class OrderAdapter extends ArrayAdapter<Order>  {

    private int resourceId;

    public OrderAdapter(Context context, int textViewResourceId,
                        List<Order> objects) {
        super(context, textViewResourceId, objects);
        //拿取到子项布局ID
        resourceId = textViewResourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

            //获取当前项的实例
            Order order = getItem(position);
            //为子项动态加载布局

            TextView order_inTime = (TextView) view.findViewById(R.id.order_inTime);
            TextView order_placeName = (TextView) view.findViewById(R.id.order_placeName);
            TextView order_parkTime = (TextView) view.findViewById(R.id.order_parkTime);
            TextView order_status = (TextView) view.findViewById(R.id.order_status);
//            TextView order_realParkFee = (TextView) view.findViewById(R.id.order_realParkFee);
            TextView order_placeNumber = (TextView) view.findViewById(R.id.order_placeNumber);
            TextView order_orderId= (TextView) view.findViewById(R.id.order_orderId);
            order_inTime.setText(order.getIn_time());
            order_placeName.setText(order.getPlace_name());
            order_parkTime.setText(order.getPark_time());
            order_status.setText(order.getStatus());
//            order_realParkFee.setText(order.getReal_park_fee());
            order_placeNumber.setText(order.getPlace_number());
            order_orderId.setText(order.getId());
            return view;


    }

}

