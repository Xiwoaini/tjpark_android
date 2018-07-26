package com.tjsinfo.tjpark.fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tjsinfo.tjpark.activity.LoginActivity;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.tjsinfo.tjpark.activity.DetailActivity;
import com.tjsinfo.tjpark.R;

import com.tjsinfo.tjpark.activity.SplashActivity;
import com.tjsinfo.tjpark.entity.Order;
import com.tjsinfo.tjpark.util.NetConnection;
import com.tjsinfo.tjpark.adapter.OrderAdapter;
import com.tjsinfo.tjpark.util.TjParkUtils;


/**
 *底部第二页
 */
public class TwoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnScrollListener {
    private ListView listView;
    private List<Order> orderList = new LinkedList<Order>();
    private List<Order> orderListShow = new LinkedList<Order>();
    private SharedPreferences mSharedPreferences;
    Dialog d;
    private String personID;
         public TwoFragment() {

      }
     //下拉组件
     private SwipeRefreshLayout swipeRefreshLayout;
     //上拉组件
     private int totalCount;// 数据总条数
     private View view_more;
    private ProgressBar pb;
    private TextView tvLoad;
    private int lastVisibleIndex;


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

             d =TjParkUtils.createLoadingDialog(getActivity(),"加载中...");
            //初始化下拉控件
            swipeRefreshLayout = (SwipeRefreshLayout)getActivity().findViewById(R.id.swipe_refresh_layout);
            swipeRefreshLayout.setColorSchemeResources(
                    R.color.colorPrimary,
                    R.color.gray,
                    R.color.red
            );
            swipeRefreshLayout.setOnRefreshListener(this);
            listView = (ListView) getActivity().findViewById(R.id.orderListView);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    JsonArray jsonArray = null;
                    String strUrl = "/tjpark/app/AppWebservice/findParkRecord?customerid="+personID;
                    jsonArray = NetConnection.getJsonArray(strUrl);

                    if (null == jsonArray) {
                        Message msg = new Message();
                        handler.sendMessage(msg);
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


        }

    }

    OrderAdapter adapter;

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //下拉初始化
            initViews();
            // 初始化数据
            initData();
            if (orderList.size() == 0){
                //暂无数据
                Toast.makeText(getActivity(), "暂无停车订单。", Toast.LENGTH_SHORT).show();
                TjParkUtils.closeDialog(d);
                return;
            }

             adapter = new OrderAdapter(getActivity(), R.layout.activity_orderview, orderListShow);
            listView.setAdapter(adapter);
            //添加监听
            TwoFragment.ListViewListener listViewListener = new TwoFragment.ListViewListener();
            listView.setOnItemClickListener(listViewListener);
            TjParkUtils.closeDialog(d);
            swipeRefreshLayout.setRefreshing(false);
            // 添加底部加载布局
            listView.addFooterView(view_more);
            // 设置监听
            setListeners();
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

    //重写下拉组件
    @Override
    public void onRefresh() {
        orderList.clear();
        // 网络请求
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

    }

    //下拉加载更多
    private void initViews() {
        // 构建底部加载布局
        view_more = (View) getLayoutInflater()
                .inflate(R.layout.view_more, null);
        // 进度条
        pb = (ProgressBar) view_more.findViewById(R.id.progressBar);
        // “正在加载...”文本控件
        tvLoad = (TextView) view_more.findViewById(R.id.tv_Load);

    }
    ///重新下拉加载更多
//    监听listView的滑动状态的改变
    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

        if (i == SCROLL_STATE_IDLE) {

            pb.setVisibility(View.VISIBLE);
            tvLoad.setVisibility(View.VISIBLE);
            loadMoreData();//加载更多数据
        }

    }
    //加载更多数据
    private void loadMoreData() {
        // 获取此时的总条目数

        // 一次加载4条数据，即下拉加载的执行
        if (orderListShow.size() + 4 < orderList.size()) {
            initData(orderListShow.size(), orderListShow.size()+4);// 往后继续加载4个数据
        } else {// 数据不足15条直接加载到结束


            initData(orderListShow.size(), orderList.size());// 模拟网络获取数据曹祖
            // 数据全部加载完成后，移除底部的view
            listView.removeFooterView(view_more);
            Toast.makeText(getActivity(), "没有更多的数据了...", Toast.LENGTH_SHORT).show();
        }

    }
//    监听listView的滑动
    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        // 计算最后可见条目的索引
        lastVisibleIndex = orderListShow.size() - 1;
        // 当adapter中的所有条目数已经和要加载的数据总条数相等时，则移除底部的View
        if (i2 == orderList.size() + 1) {
            // 移除底部的加载布局
            listView.removeFooterView(view_more);
        }

    }
    private void setListeners() {
        if (orderList.size() > 4) {
            // listView设置滑动简监听
            listView.setOnScrollListener(this);
        } else {
            // 假如数据总数少于等于15条，直接移除底部的加载布局，不需要再加载更多的数据
            listView.removeFooterView(view_more);
        }
    }
    private void initData() {
        orderListShow.clear();
        //显示数据个数
        if (orderList.size() <= 4) {
            orderListShow = orderList;
        }
        else{
             for (int i =0;i<4;i++){
                 orderListShow.add(orderList.get(i));
             }
        }

    }
    //继续加载数据
    private void initData(final int start, final int end) {
       for (int i = start;i<end;i++){
            orderListShow.add(orderList.get(i));
       }
        Message msg = new Message();
        handlerMore.handleMessage(msg);
    }
    private Handler handlerMore = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter.notifyDataSetChanged();
        }
    };


}