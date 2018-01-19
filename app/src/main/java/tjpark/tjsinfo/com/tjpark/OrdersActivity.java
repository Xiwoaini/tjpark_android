package tjpark.tjsinfo.com.tjpark;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import tjpark.tjsinfo.com.tjpark.entity.Car;
import tjpark.tjsinfo.com.tjpark.entity.Order;
import tjpark.tjsinfo.com.tjpark.util.NetConnection;

/**
 * Created by panning on 2018/1/12.
 */

public class OrdersActivity  extends ListActivity {
    private ListView listView;
    private  List<Order> orderList = new LinkedList<Order>();
    private List<Map<String, String>> data = new ArrayList<Map<String,String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new Thread(runnable).start();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("姓名", "风晴雪");
        map1.put("性别", "女的");
        data.add(map1);
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("姓名", "悭臾");
        map2.put("性别", "公的");
        data.add(map2);
        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("姓名", "百里屠苏");
        map3.put("性别", "男的");
        data.add(map3);
         setListAdapter(new SimpleAdapter(this,data,android.R.layout.simple_list_item_2,
                new String[]{"姓名","性别"},            //每行显示一组姓名和性别
                new int[]{android.R.id.text1,android.R.id.text2}   //名字在text1上显示，性别在text2上显示
        ));

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
                    order.setPlace_name(jso.get("place_name").toString().replace("\"",""));
                    order.setPlace_number(jso.get("place_number").toString().replace("\"",""));
                    order.setStatus(jso.get("status").toString().replace("\"",""));
                    order.setReal_park_fee(jso.get("real_park_fee").toString().replace("\"",""));
                    order.setPlace_id(jso.get("place_id").toString().replace("\"",""));

                    order.setPark_time(jso.get("park_time").toString().replace("\"",""));
                    order.setCreate_time(jso.get("create_time").toString().replace("\"",""));
                    orderList.add(order);
                    i++;
                }
                catch(Exception e){
                        continue;
                }
            }
            i=0;


            Message msg = new Message();
            Bundle data = new Bundle();
//            data.putString("value",result);
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };
    //处理远程结果
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");


        }
    };

    //调用远程接口获取账号车辆
    private List<String> getData(){

        List<String> data = new ArrayList<String>();
        for(int i =0;i<orderList.size();i++){
            data.add(orderList.get(i).getCreate_time());

        }
        return data;
    }
}
