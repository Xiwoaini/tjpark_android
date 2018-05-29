package tjpark.tjsinfo.com.tjpark.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import tjpark.tjsinfo.com.tjpark.R;
import tjpark.tjsinfo.com.tjpark.adapter.MyShareAdapter;
import tjpark.tjsinfo.com.tjpark.adapter.OrderAdapter;
import tjpark.tjsinfo.com.tjpark.entity.MyShare;
import tjpark.tjsinfo.com.tjpark.entity.Order;
import tjpark.tjsinfo.com.tjpark.fragment.TwoFragment;
import tjpark.tjsinfo.com.tjpark.util.NetConnection;

/**
 * Created by panning on 2018/1/12.
 */

//我的共享车位
public class MyShareActivity extends AppCompatActivity {
    private SharedPreferences mSharedPreferences;
    List<MyShare> myShareList =new ArrayList<MyShare>();
    private ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myshare);
        listview=(ListView)findViewById(R.id.listView);

        mSharedPreferences = getSharedPreferences("userInfo",MODE_PRIVATE);
        final String customerid=mSharedPreferences.getString("personID","");
        new Thread(new Runnable() {
            @Override
            public void run() {
                JsonArray jsonArray = null;
                String strUrl="/tjpark/app/AppWebservice/findMySharePark?customerid="+customerid ;
                jsonArray = NetConnection.getJsonArray(strUrl);
                //全部返回的字符串内容
                if (null==jsonArray ){
                    return;
                }
                Iterator it = jsonArray.iterator();

                int i=0;
                while (it.hasNext()) {
                    MyShare myShare = new MyShare();
                    if(i==jsonArray.size()){
                        break;
                    }
                    try {
                        JsonObject jso = jsonArray.get(i).getAsJsonObject();
                        myShare.setId(jso.get("id").toString().replace("\"",""));
                        myShare.setPlace_id(jso.get("place_id").toString().replace("\"",""));
                        myShare.setPlace_name(jso.get("place_name").toString().replace("\"",""));
                        myShare.setPark_num(jso.get("park_num").toString().replace("\"",""));
                        myShare.setCustomer_id(jso.get("customer_id").toString().replace("\"",""));
                        myShare.setPhone(jso.get("phone").toString().replace("\"",""));
                        myShare.setCreate_time(jso.get("create_time").toString().replace("\"",""));
//
                        myShare.setPark_fee(jso.get("park_fee").toString().replace("\"",""));
                        myShare.setStart_time(jso.get("start_time").toString().replace("\"",""));
                        myShare.setEnd_time(jso.get("end_time").toString().replace("\"",""));
                        myShare.setStatus(jso.get("status").toString().replace("\"",""));
//
                        myShare.setShare_status(jso.get("share_status").toString().replace("\"",""));
//
                        myShare.setContacts_name(jso.get("contacts_name").toString().replace("\"",""));
                        myShare.setModel(jso.get("model").toString().replace("\"",""));
                        myShare.setButtonName(jso.get("buttonName").toString().replace("\"",""));
                        i++;

                        myShareList.add(myShare);
                    }
                    catch (Exception e){
                        i++;
                        myShareList.add(myShare);
                    }
                }

                Message msg = new Message();
                handler.sendMessage(msg);
            }
        }).start();

        Button exitBtn=findViewById(R.id.exitBtn);
        //返回按钮监听
        exitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MyShareActivity.this, TabBarActivity.class);
                intent.putExtra("currentTab",3);
                startActivity(intent);
            }

        });
    }



    //回调
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MyShareAdapter adapter = new MyShareAdapter(MyShareActivity.this, R.layout.activity_sharelistview,myShareList);
            listview.setAdapter(adapter);

        }};

}