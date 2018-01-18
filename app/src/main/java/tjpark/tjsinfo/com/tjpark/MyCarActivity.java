package tjpark.tjsinfo.com.tjpark;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import tjpark.tjsinfo.com.tjpark.entity.Car;
import tjpark.tjsinfo.com.tjpark.util.NetConnection;

/**
 * Created by panning on 2018/1/12.
 */

public class MyCarActivity  extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycar);
        new Thread(runnable).start();
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
            System.out.print(11);
            JsonArray jsonArray = null;
            String strUrl="/tjpark/app/AppWebservice/findPlate?customerid=40288afd5c43e114015c43f2d85f0000";
            jsonArray = NetConnection.getJsonArray(strUrl);


//            Iterator it = jsonArray.iterator();
            Car c = new Car();
            System.out.print(11);
//            while (it.hasNext()) {
//                System.out.print(11);
//                JsonObject jso = jsonArray.get(0).getAsJsonObject();
//                System.out.print(jso.get("place_number"));
//
//            }



            
            
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

        data.add("测试数据4");

        return data;
    }

}