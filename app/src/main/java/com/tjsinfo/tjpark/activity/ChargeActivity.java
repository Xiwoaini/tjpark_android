package com.tjsinfo.tjpark.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tjsinfo.tjpark.R;
import com.tjsinfo.tjpark.adapter.ChargeParkAdapter;
import com.tjsinfo.tjpark.adapter.PersonAdapter;
import com.tjsinfo.tjpark.entity.ChargePark;
import com.tjsinfo.tjpark.entity.Park;
import com.tjsinfo.tjpark.fragment.FourFragment;
import com.tjsinfo.tjpark.util.NetConnection;
import com.tjsinfo.tjpark.util.TjParkUtils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by panning on 2018/6/28.
 */

public class ChargeActivity extends Activity {
    ListView listView ;
    List<ChargePark> chargeParkList = new LinkedList<ChargePark>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chargepark);

        Button exitBtn= findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        listView = (ListView)findViewById(R.id.chargeListView);
       /*获取Intent中的Bundle对象*/
        Bundle bundle = this.getIntent().getExtras();
       final String id   = bundle.get("chargeParkId").toString();

       new Thread(
               new Runnable() {
                   @Override
                   public void run() {
                       JsonArray res = null;
                       String strUrl = "/tjpark/app/AppWebservice/findPileParkByPileId?" +
                               "parkPileId=" + id;
                       try{
                           res = NetConnection.getJsonArray(strUrl);
                           if (res == null){
                               return;
                           }
                           int i = 0;
                           Iterator it = res.iterator();
                           while (it.hasNext()) {
                               if (i == res.size()) {
                                   break;
                               }
                               JsonObject jso = res.get(i).getAsJsonObject();
                               ChargePark chargePark = new ChargePark();
                               chargePark.setStatus(jso.get("status").toString().replace("\"", ""));
                               chargePark.setNumbers(jso.get("number").toString().replace("\"", ""));
                               chargeParkList.add(chargePark);
                               i+=1;
                           }

                           Message msg = new Message();
                           handler.sendMessage(msg);
                       }
                       catch (Exception e){
                           e.printStackTrace();
                       }
                   }
               }
       ).start();


    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ChargeParkAdapter chargeParkAdapter = new ChargeParkAdapter(ChargeActivity.this, R.layout.activity_chargelistview,chargeParkList);

        //为listView赋值
        listView.setAdapter(chargeParkAdapter);
        }
    };





}
