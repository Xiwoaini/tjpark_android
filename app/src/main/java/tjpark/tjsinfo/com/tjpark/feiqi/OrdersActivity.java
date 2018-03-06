package tjpark.tjsinfo.com.tjpark.feiqi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
 * Created by panning on 2018/1/12.
 */

@Deprecated
//订单页控制，目前没作用(TwoFragment代替)
public class OrdersActivity  extends AppCompatActivity {
    private ListView listView;
    private  List<Order> orderList = new LinkedList<Order>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new Thread(runnable).start();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
//        //获取listView，
        listView = (ListView)findViewById(R.id.orderListView);

    }


    //新线程进行网络请求
    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            JsonArray jsonArray = null;

            String strUrl="/tjpark/app/AppWebservice/findParkRecord?customerid=40288afd5c43e114015c43f2d85f0000";
            jsonArray = NetConnection.getJsonArray(strUrl);

            Iterator it = jsonArray.iterator();

            int i=0;
            while (it.hasNext()) {
                Order order = new Order();
                if(i==jsonArray.size()){
                    break;
                }
                try{
                    JsonObject jso = jsonArray.get(i).getAsJsonObject();
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

            OrderAdapter adapter = new OrderAdapter(OrdersActivity.this, R.layout.activity_orderview,orderList);
            listView.setAdapter(adapter);
            //添加监听
            ListViewListener listViewListener = new ListViewListener();
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
            intent.setClass(OrdersActivity.this, DetailActivity.class);
            //传值,数据少的情况下

            intent.putExtra("detail",orderList.get(position));
            startActivity(intent);

        }
    }





}
