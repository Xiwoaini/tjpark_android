package tjpark.tjsinfo.com.tjpark.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.feezu.liuli.timeselector.TimeSelector;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import tjpark.tjsinfo.com.tjpark.R;
import tjpark.tjsinfo.com.tjpark.entity.Park;
import tjpark.tjsinfo.com.tjpark.adapter.MyAdapter;
import tjpark.tjsinfo.com.tjpark.util.NetConnection;

/**
 * Created by panning on 2018/1/12.
 */
//共享车位发布
public class ShareReleaseActivity  extends AppCompatActivity {
    private Spinner sPlace,sType,sAddress;
    private EditText sNum,sName,sPhone,sMoney,startTime,endTime;
    private CheckBox checkBtn;


    List<String> parkList = new LinkedList<String>();
    List<Park> parkLidstID = new LinkedList<Park>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharerelease);

        //控件获取
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        checkBtn = findViewById(R.id.checkBtn);
        sNum = findViewById(R.id.sNum);
        sName = findViewById(R.id.sName);
        sPhone = findViewById(R.id.sPhone);
        sMoney = findViewById(R.id.sMoney);

        //spinner控件获取
        sPlace = findViewById(R.id.sPlace);
        //地区适配
          sAddress = findViewById(R.id.sAddress);
        final List<String> datas = new ArrayList<>();
        String[] addArray = {"和平区","南开区","河西区","红桥区","河北区","河东区"};
        //数据添加
        for (int i = 0; i < 6; i++) {
            datas.add(addArray[i]);
        }

        MyAdapter adapter = new MyAdapter(this);
        sAddress.setAdapter(adapter);
        adapter.setDatas(datas);

        //类型适配
        sType =  findViewById(R.id.sType);
        final List<String> dataType = new ArrayList<>();
        String[] typeArray = {"每天","周中","周末"};
        for (int i = 0; i < 3; i++) {
            dataType.add(typeArray[i]);
        }
        MyAdapter adapterType = new MyAdapter(this);
        sType.setAdapter(adapterType);
        adapterType.setDatas(dataType);


        //发布按钮监听
        Button shareBtn = findViewById(R.id.shareBtn);
        //监听事件
        shareBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new Thread(  new Runnable(){
                    @Override
                    public void run() {
                        int tmp = 0;
                        for (int i=0;i<parkLidstID.size();i++){
                            if  (parkLidstID.get(i).getPlace_name().equals(sPlace.getSelectedItem())){
                                tmp= i;
                                break;
                            }

                        }
                        parkList.clear();
//
                         SharedPreferences mSharedPreferences;
                        mSharedPreferences =getSharedPreferences("userInfo",MODE_PRIVATE);
                        JsonObject jsonObject = null;


                        String strUrl="/tjpark/app/AppWebservice/addShareSpace?" +
                                "place_id="+parkLidstID.get(tmp).getId() +
                                "&space_num="+parkLidstID.get(tmp).getPlace_name() +
                                "&name="+sName.getText() +
                                "&phone="+sPhone.getText() +
                                "&fee="+sMoney.getText() +
                                "&start_time="+startTime.getText() +
                                "&end_time="+endTime.getText() +
                                "&customerid=" +mSharedPreferences.getString("personID","")+
                                "&model="+sType.getSelectedItem().toString();
                        try{
                            jsonObject = NetConnection.getXpath(strUrl);

                            Intent intent = new Intent();
                            intent.setClass(ShareReleaseActivity.this, TabBarActivity.class);
                            startActivity(intent);

                        }
                       catch (Exception e){
                           Intent intent = new Intent();
                           intent.setClass(ShareReleaseActivity.this, TabBarActivity.class);
                           startActivity(intent);
                       }

                    }
                }).start();


            }
        });



        /**选项选择监听*/
        sAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               final String str =datas.get(position);

                new Thread(  new Runnable(){
                    @Override
                    public void run() {
                        parkList.clear();
                        JsonArray jsonArray = null;
                        String strUrl="/tjpark/app/AppWebservice/findSharePlace?district="+str;
                        jsonArray = NetConnection.getJsonArray(strUrl);
                        if (null==jsonArray ){
                            return;
                        }
                        Iterator it = jsonArray.iterator();
                        int i=0;
                        while (it.hasNext()) {
                            if (i == jsonArray.size()){
                               break;
                            }

                            Park park = new Park();
                            JsonObject jso = jsonArray.get(i).getAsJsonObject();
                            park.setId(jso.get("place_id").toString().replace("\"",""));
                            park.setPlace_name(jso.get("place_name").toString().replace("\"",""));
                            parkLidstID.add(park);
                            parkList.add(jso.get("place_name").toString().replace("\"",""));
                            i++;
                        }
                    }
                }).start();
                Message msg = new Message();
                Bundle data = new Bundle();

                msg.setData(data);
                handler.sendMessage(msg);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                    return;
            }
        });

//选择时间监听
        startTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    TimeSelector timeSelector = new TimeSelector(ShareReleaseActivity.this, new TimeSelector.ResultHandler() {
                        @Override
                        public void handle(String time) {
                            Toast.makeText(ShareReleaseActivity.this, time, Toast.LENGTH_SHORT).show();
                                    startTime.setText(time);
                        }
                    }, "2018-01-01 00:00", "2030-12-31 23:59:59");

                    timeSelector.show();
                }
            }
        });
        endTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    TimeSelector timeSelector = new TimeSelector(ShareReleaseActivity.this, new TimeSelector.ResultHandler() {
                        @Override
                        public void handle(String time) {
                            endTime.setText(time);
                        }
                    }, "2018-01-01 00:00", "2030-12-31 23:59:59");

                    timeSelector.show();
                }
            }
        });
        //返回按钮监听
        Button exitBtn = findViewById(R.id.exitBtn);
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
            MyAdapter addAdapter = new MyAdapter(ShareReleaseActivity.this);
            sPlace.setAdapter(addAdapter);
            addAdapter.setDatas(parkList);
        }};




}