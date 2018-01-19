package tjpark.tjsinfo.com.tjpark;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import tjpark.tjsinfo.com.tjpark.entity.Car;
import tjpark.tjsinfo.com.tjpark.util.NetConnection;

/**
 * Created by panning on 2018/1/12.
 */

public class MyCarActivity  extends AppCompatActivity {

    private ListView listView;
    private static List<Car> myCarList = new LinkedList<Car>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new Thread(runnable).start();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycar);

        //获取listView，
        listView = (ListView)findViewById(R.id.listView);
        listView = new ListView(this);

        //为listView赋值
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,getData()));

        setContentView(listView);
    }

    //新线程进行网络请求
    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            JsonArray jsonArray = null;
            String strUrl="/tjpark/app/AppWebservice/findPlate?customerid=40288afd5c43e114015c43f2d85f0000";
            jsonArray = NetConnection.getJsonArray(strUrl);


            Iterator it = jsonArray.iterator();

            int i=0;
            while (it.hasNext()) {
                Car car = new Car();
                if(i==jsonArray.size()){
                    break;
                }
                JsonObject jso = jsonArray.get(i).getAsJsonObject();
                car.setId(jso.get("id").toString().replace("\"",""));
//                car.setPark_id(jso.get("park_id").toString());
                car.setCreated_time(jso.get("created_time").toString().replace("\"",""));
                car.setCustomer_id(jso.get("customer_id").toString().replace("\"",""));
                car.setPlace_number(jso.get("place_number").toString().replace("\"",""));
                myCarList.add(car);
                i++;
//                Log.v("1",""+jso.get("place_number"));
            }
            i=0;
            //全部返回的字符串内容
//

//            System.out.println(jsonArray.size());
//            String result = jsonArray.get("result").toString();

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
        for(int i =0;i<myCarList.size();i++){
            data.add(myCarList.get(i).getPlace_number());
        }
        return data;
    }

}