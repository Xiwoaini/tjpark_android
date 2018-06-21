package com.tjsinfo.tjpark.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tjsinfo.tjpark.adapter.GreenParkAdapter;
import com.tjsinfo.tjpark.entity.Park;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tjsinfo.tjpark.R;

import com.tjsinfo.tjpark.util.NetConnection;

/**
 * Created by panning on 2018/1/12.
 */

public class GreenParkActivity extends AppCompatActivity {

    private TextView parkId,greenPark_placeName,greenPark_distance,greenPark_address;
    private ListView listView;
    private List<Park> greenParkList = new ArrayList<Park>();
    private Park park;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greenpark);
            /*获取Intent中的Bundle对象*/
        Bundle bundle = this.getIntent().getExtras();
        park  = (Park) bundle.get("park");
        Button exitBtn=(Button)findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }

        });
        listView = (ListView)findViewById(R.id.listView);
        parkId= (TextView)findViewById(R.id.parkId);
        greenPark_placeName = (TextView)findViewById(R.id.greenPark_placeName);
        greenPark_distance = (TextView)findViewById(R.id.greenPark_distance);
        greenPark_address = (TextView)findViewById(R.id.greenPark_address);


        parkId.setText(park.getId());
        greenPark_placeName.setText(park.getPlace_name());
        greenPark_distance.setText(park.getDistance());
        greenPark_address.setText(park.getPlace_address());

        new Thread(runnable).start();
    }


    //新线程进行网络请求
    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            JsonArray jsonArray = null;
            String parkid = parkId.getText().toString();
            String strUrl="/tjpark/app/AppWebservice/findSharePark?parkid="+parkid;
            jsonArray = NetConnection.getJsonArray(strUrl);
            if (jsonArray == null){
                return ;
            }

            Iterator it = jsonArray.iterator();

            int i=0;
            while (it.hasNext()) {
               Park greenPark = new Park();
                if(i==jsonArray.size()){
                    break;
                }
                JsonObject jso = jsonArray.get(i).getAsJsonObject();
                greenPark.setShare_id(jso.get("id").toString().replace("\"",""));
                greenPark.setPark_num(jso.get("park_num").toString().replace("\"",""));
                greenPark.setStart_time(jso.get("start_time").toString().replace("\"",""));
                greenPark.setEnd_time(jso.get("end_time").toString().replace("\"",""));
                greenPark.setPark_fee(jso.get("park_fee").toString().replace("\"",""));
                greenPark.setPlace_id(jso.get("place_id").toString().replace("\"",""));
                greenPark.setModel(jso.get("model").toString().replace("\"",""));
                greenPark.setStatus(jso.get("status").toString().replace("\"",""));
                greenPark.setShare_status(jso.get("share_status").toString().replace("\"",""));
                greenPark.setDistance(greenPark_distance.getText().toString());
                greenPark.setPlace_name(greenPark_placeName.getText().toString());
                greenPark.setPlace_address(park.getPlace_address());
                greenPark.setLable(park.getLable());
                greenParkList.add(greenPark);
                i++;
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
//
            GreenParkAdapter adapter = new GreenParkAdapter(GreenParkActivity.this, R.layout.activity_greenlistview,greenParkList);
            listView.setAdapter(adapter);


        }
    };

}