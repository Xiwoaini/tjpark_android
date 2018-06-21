package com.tjsinfo.tjpark.fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tjsinfo.tjpark.activity.LoginActivity;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.tjsinfo.tjpark.activity.DetailActivity;
import com.tjsinfo.tjpark.R;

import com.tjsinfo.tjpark.entity.Order;
import com.tjsinfo.tjpark.util.NetConnection;
import com.tjsinfo.tjpark.adapter.OrderAdapter;
import com.tjsinfo.tjpark.util.TjParkUtils;


/**
 *底部第二页
 */
public class TwoFragment extends Fragment {
    private ListView listView;
    private List<Order> orderList = new LinkedList<Order>();
    private SharedPreferences mSharedPreferences;
    Dialog d;
private String personID;
    public TwoFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_orders, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSharedPreferences = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);

         personID = mSharedPreferences.getString("personID", "");
        if (null == personID || personID.equals("")) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("提示")
                    .setMessage("请先登录!")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           //跳转到登录页
                            Intent intent = new Intent();
                            //(当前Activity，目标Activity)
                            intent.setClass(getActivity(), LoginActivity.class);
                            startActivity(intent);
                        }
                    })
                    .show();

        } else {
             d =TjParkUtils.createLoadingDialog(getActivity(),"加载中");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    JsonArray jsonArray = null;

                    String strUrl = "/tjpark/app/AppWebservice/findParkRecord?customerid="+personID;
                    jsonArray = NetConnection.getJsonArray(strUrl);

                    if (null == jsonArray) {
                        return;
                    }

                    Iterator it = jsonArray.iterator();

                    int i = 0;
                    while (it.hasNext()) {
                        Order order = new Order();
                        if (i == jsonArray.size()) {
                            break;
                        }
                        try {
                            JsonObject jso = jsonArray.get(i).getAsJsonObject();

                            if (jso.get("status").toString().replace("\"", "").equals("待支付")) {
                                order.setReservation_park_fee(jso.get("reservation_park_fee").toString().replace("\"", ""));
                            }
                            else if(jso.get("status").toString().replace("\"", "").equals("正在计时")){

                            }
                            else if (jso.get("status").toString().replace("\"", "").equals("已完成")){
                                order.setOut_time(jso.get("out_time").toString().replace("\"", ""));
                            }
                            order.setId(jso.get("id").toString().replace("\"", ""));
                            order.setPlace_id(jso.get("place_id").toString().replace("\"", ""));
                            order.setPlace_name(jso.get("place_name").toString().replace("\"", ""));
                            order.setPlace_number(jso.get("place_number").toString().replace("\"", ""));
                            order.setPark_time(jso.get("park_time").toString().replace("\"", ""));
                            order.setReal_park_fee(jso.get("real_park_fee").toString().replace("\"", ""));
                            order.setStatus(jso.get("status").toString().replace("\"", ""));
                            order.setIn_time(jso.get("in_time").toString().replace("\"", ""));
                            orderList.add(order);
                            i++;
                        } catch (Exception e) {
                            i++;
                            continue;
                        }
                    }
                    i = 0;

                    Message msg = new Message();
                    handler.sendMessage(msg);
                }
            }).start();

            listView = (ListView) getActivity().findViewById(R.id.orderListView);
        }

    }



    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            OrderAdapter adapter = new OrderAdapter(getActivity(), R.layout.activity_orderview, orderList);
            listView.setAdapter(adapter);
            //添加监听
            TwoFragment.ListViewListener listViewListener = new TwoFragment.ListViewListener();
            listView.setOnItemClickListener(listViewListener);
            TjParkUtils.closeDialog(d);

        }
    };

    //内部类，负责监听listview点击某行事件
    class ListViewListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//            跳转到详情页面
            Intent intent = new Intent();
            intent.setClass(getActivity(), DetailActivity.class);
            intent.putExtra("detail", orderList.get(position));
            startActivity(intent);

        }
    }
}