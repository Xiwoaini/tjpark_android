package com.tjsinfo.tjpark.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Iterator;

import com.tjsinfo.tjpark.R;
import com.tjsinfo.tjpark.util.NetConnection;

/**
 * Created by panning on 2018/1/12.
 */

public class IncludeDetailActivity extends AppCompatActivity {
   private  TextView KSSJ,JSSJ,SR;
    Intent getIntent = null;
    private String startTime ,endTime,shouRu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_includedetail);
        getIntent= getIntent();
        KSSJ = (TextView)findViewById(R.id.KSSJ);
        JSSJ = (TextView)findViewById(R.id.JSSJ);
        SR = (TextView)findViewById(R.id.SR);
        new Thread(new Runnable() {
            @Override
            public void run() {
                JsonArray jsonArray = null;
                String strUrl="/tjpark/app/AppWebservice/findShareFee?shareid="+getIntent.getStringExtra("shareid") ;
                jsonArray = NetConnection.getJsonArray(strUrl);
                //全部返回的字符串内容
                if (null==jsonArray ){
                    return;
                }
                Iterator it = jsonArray.iterator();


                while (it.hasNext()) {
                    try {
                        JsonObject jso = jsonArray.get(0).getAsJsonObject();
                        startTime = jso.get("start_time").toString().replace("\"", "");
                        endTime = jso.get("out_time").toString().replace("\"", "");
                        shouRu = String.valueOf(Double.valueOf(jso.get("real_park_fee").toString().replace("\"", ""))+ Double.valueOf(jso.get("reservation_fee").toString().replace("\"", "")));
                        break ;
                    } catch (Exception e) {
                        break ;
                    }

                }
                Message msg = new Message();
                handler.sendMessage(msg);
            }
        }).start();


        Button exitBtn=(Button)findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }

        });
    }



    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (startTime == null){
                startTime = "暂无数据";
            }
            if (endTime == null){
                endTime = "暂无数据";
            }
            if (shouRu == null){
                shouRu = "暂无数据";
            }
            KSSJ.setText("开始时间： "+startTime );
            JSSJ.setText("结束时间： "+endTime);
            SR.setText("收入： "+shouRu);

        }};

}