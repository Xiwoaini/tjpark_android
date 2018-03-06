package tjpark.tjsinfo.com.tjpark.fragment;


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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import tjpark.tjsinfo.com.tjpark.DetailActivity;
import tjpark.tjsinfo.com.tjpark.R;
import tjpark.tjsinfo.com.tjpark.entity.Order;
import tjpark.tjsinfo.com.tjpark.util.NetConnection;
import tjpark.tjsinfo.com.tjpark.adapter.OrderAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class TwoFragment extends Fragment {
private ListView listView;
    private List<Order> orderList = new LinkedList<Order>();
    private SharedPreferences mSharedPreferences;
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
        mSharedPreferences = getActivity().getSharedPreferences("userInfo",getActivity().MODE_PRIVATE);

        String  personID =mSharedPreferences.getString("personID","");
        if (null==personID || personID.equals("")){
            new AlertDialog.Builder(getActivity())
                    .setTitle("提示")
                    .setMessage("请先登录!")
                    .setPositiveButton("确定", null)
                    .show();

        }
        else {
            new Thread(runnable).start();

            listView= (ListView)getActivity().findViewById(R.id.orderListView);
        }

    }


    //新线程进行网络请求
    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            JsonArray jsonArray = null;

            String strUrl="/tjpark/app/AppWebservice/findParkRecord?customerid=40288afd5c43e114015c43f2d85f0000";
            jsonArray = NetConnection.getJsonArray(strUrl);

            if (null==jsonArray ){
                return;
            }

            Iterator it = jsonArray.iterator();

            int i=0;
            while (it.hasNext()) {
                Order order = new Order();
                if(i==jsonArray.size()){
                    break;
                }
                try{
                    JsonObject jso = jsonArray.get(i).getAsJsonObject();

                    if (jso.get("status").toString().replace("\"","").equals("待支付")){
                        order.setReservation_park_fee(jso.get("reservation_park_fee").toString().replace("\"",""));
                    }
                    order.setId(jso.get("id").toString().replace("\"",""));
                    order.setPlace_id(jso.get("place_id").toString().replace("\"",""));
                    order.setPlace_name(jso.get("place_name").toString().replace("\"",""));
                    order.setPlace_number(jso.get("place_number").toString().replace("\"",""));
                    order.setPark_time(jso.get("park_time").toString().replace("\"",""));
                    order.setReal_park_fee(jso.get("real_park_fee").toString().replace("\"",""));
                    order.setStatus(jso.get("status").toString().replace("\"",""));
                    order.setIn_time(jso.get("in_time").toString().replace("\"",""));
                    order.setOut_time(jso.get("out_time").toString().replace("\"",""));


                    //TODO:其他的属性，需要在此添加

                    orderList.add(order);
                    i++;
                }
                catch(Exception e){
                    i++;
                    continue;
                }
            }
            i=0;

            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value",jsonArray.toString());
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            OrderAdapter adapter = new OrderAdapter(getActivity(), R.layout.activity_orderview,orderList);
            listView.setAdapter(adapter);
            //添加监听
            TwoFragment.ListViewListener listViewListener = new TwoFragment.ListViewListener();
            listView.setOnItemClickListener(listViewListener);



        }
    };
    //内部类，负责监听listview点击某行事件
    class ListViewListener implements   AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //position指点击的行数,从第0行开始
//            跳转
            Intent intent = new Intent();
            intent.setClass(getActivity(), DetailActivity.class);
            //传值,数据少的情况下

            intent.putExtra("detail",orderList.get(position));
            startActivity(intent);

        }
    }



//    @GET
//    @Path("/parkPay")
//    @Produces(MediaType.TEXT_PLAIN)
//    public String parkPay(@QueryParam("userid") String userid,
//                          @QueryParam("parkid") String parkid,
//                          @QueryParam("fee") String fee,
//                          @QueryParam("place_name") String place_name,
//                          @QueryParam("payMode") String payMode,
//                          @QueryParam("place_id") String place_id
//    ) {

}

//
//192.168.168.221:8080/tjpark/app/AppWebservice/reservableParkIn?customer_id=402840288afd5c43e114015c43f2d85f0000&plate_number=津a11111&plate_id=4028b8815fdd3022015fdd64df290000&place_id=297e1fda5e50e569015e50ff11cd0004&place_name=小白楼朗香街&reservation_time=100&reservation_fee=200&payMode=支付宝
//
//reservableParkIn?customer_id=402840288afd5c43e114015c43f2d85f0000&plate_number=津a11111&plate_id=4028b8815fdd3022015fdd64df290000&place_id=297e1fda5e50e569015e50ff11cd0004&place_name=小白楼朗香街&reservation_time=100&reservation_fee=200&payMode=支付宝