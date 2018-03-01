package tjpark.tjsinfo.com.tjpark;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import tjpark.tjsinfo.com.tjpark.util.CarAdapter;
import tjpark.tjsinfo.com.tjpark.util.NetConnection;
import tjpark.tjsinfo.com.tjpark.util.ParkAdapter;

/**
 * Created by panning on 2018/1/12.
 */

public class MyCarActivity  extends AppCompatActivity {

    private ListView listView;
    private   List<Car> myCarList = new LinkedList<Car>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new Thread(runnable).start();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycar);
        Button exitBtn=(Button)findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }

        });
        Button addBtn=(Button)findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
//          //(当前Activity，目标Activity)
                intent.setClass(MyCarActivity.this, AddCarActivity.class);
                intent.putExtra("car","");
                startActivity(intent);
            }

        });
        //获取listView，
        listView = (ListView)findViewById(R.id.carListView);


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

            }

            i=0;


            Message msg = new Message();
            Bundle data = new Bundle();

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


            CarAdapter adapter = new CarAdapter(MyCarActivity.this, R.layout.activity_mycarview,myCarList);
            //为listView赋值
            listView.setAdapter(adapter);
            ListViewListener listViewListener =new ListViewListener();
            listView.setOnItemClickListener(listViewListener);



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


    //内部类，负责监听listview点击某行事件
    class ListViewListener implements   AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //position指点击的行数,从第0行开始
//            跳转
            Intent intent = new Intent();
            intent.setClass(MyCarActivity.this, AddCarActivity.class);
            //传值,数据少的情况下
            intent.putExtra("car",myCarList.get(position));

            startActivity(intent);

        }
    }



}