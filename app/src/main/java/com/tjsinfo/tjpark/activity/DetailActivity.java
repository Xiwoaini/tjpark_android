package com.tjsinfo.tjpark.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tjsinfo.tjpark.entity.Order;
import com.tjsinfo.tjpark.entity.ParkYuYue;
import com.tjsinfo.tjpark.wxapi.PayDemoActivity;

import com.tjsinfo.tjpark.R;

import com.tjsinfo.tjpark.util.NetConnection;

/**
 * Created by panning on 2018/1/12.
 */

public class DetailActivity  extends AppCompatActivity {
    private SharedPreferences mSharedPreferences;
    //绑定前台控件
    private TextView detail_parkName;
    private TextView detail_fee;
    private TextView detail_startTime;
    private TextView detail_endTime;
    private TextView detail_time;
    private TextView detail_money;
    private TextView detail_payNeed;
    private TextView detail_paySuccess;
    private Button detail_pay;
    Order order =new Order();
    private String yuYueMoney;

    //初始化
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取传过来的数据
        Bundle bundle = this.getIntent().getExtras();
        /*获取Bundle中的数据，注意类型和key*/
        order = (Order)bundle.getSerializable("detail");
        //补差支付
        if (order.getStatus().equals("待支付")){
            setContentView(R.layout.activity_detailpay);
        detail_parkName=(TextView)findViewById(R.id.detail_parkName);
        detail_fee=(TextView)findViewById(R.id.detail_fee);
        detail_startTime=(TextView)findViewById(R.id.detail_startTime);
        detail_endTime=(TextView)findViewById(R.id.detail_endTime);
        detail_time=(TextView)findViewById(R.id.detail_time);
        detail_money=(TextView)findViewById(R.id.detail_money);
        detail_pay =(Button)findViewById(R.id.detail_pay);
        detail_paySuccess=(TextView) findViewById(R.id.detail_paySuccess);
        detail_paySuccess.setText("已支付费用 : "+order.getReservation_park_fee());
        detail_payNeed=(TextView)findViewById(R.id.detail_payNeed);
        detail_payNeed.setText("需支付费用 : "+String.valueOf(Double.parseDouble(order.getReal_park_fee().replace("元",""))- Double.parseDouble(order.getReservation_park_fee().replace("元","")))+"元");

//            补差支付
            detail_pay.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                }
            });
        }
//        正在计时
        else if (order.getStatus().equals("正在计时")){
           final ParkYuYue parkYuYue =new ParkYuYue();
            setContentView(R.layout.activity_detailcurrent);
            //获取
            detail_parkName=(TextView)findViewById(R.id.detail_parkName);
            detail_fee=(TextView)findViewById(R.id.detail_fee);
            detail_startTime=(TextView)findViewById(R.id.detail_startTime);
            detail_time=(TextView)findViewById(R.id.detail_time);
            detail_payNeed=(TextView)findViewById(R.id.detail_payNeed);
            //赋值
            detail_parkName.setText(order.getPlace_name());
            detail_startTime.setText("开始时间："+order.getIn_time());
            detail_fee.setText("收费标准：6元/小时");
            detail_time.setText("停车用时："+order.getPark_time());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    JsonArray jsonArray = null;
                    String strUrl = "/tjpark/app/AppWebservice/getPayMoney?" +
                            "parkRecordId=" + order.getId() ;
                    jsonArray = NetConnection.getJsonArray(strUrl);
                    JsonObject jso = jsonArray.get(0).getAsJsonObject();
                    mSharedPreferences = getSharedPreferences("userInfo",MODE_PRIVATE);
                    String customer_id=mSharedPreferences.getString("personID","");
                    //普通预约需要的参数(reservableParkIn)

                    parkYuYue.setCustomer_id(customer_id);
                    parkYuYue.setPlate_number(order.getPlace_number());
                    parkYuYue.setRecord_id(order.getId());
                    parkYuYue.setPlace_name(order.getPlace_name());
                    parkYuYue.setPlace_id(order.getPlace_id());
                    parkYuYue.setPayMode("正在计时");
                    parkYuYue.setDetail_type("出场缴费");
                    parkYuYue.setPark_time(order.getPark_time());
                    //分转元
                    String tmp = jso.get("fee").toString().replace("\"", "");
                    parkYuYue.setReservation_fee(String.valueOf(Double.parseDouble(tmp)/100));
                    yuYueMoney = parkYuYue.getReservation_fee();

                    Message msg = new Message();
                    Bundle data = new Bundle();

                    msg.setData(data);
                    handler.sendMessage(msg);

                }
            }).start();


            detail_pay =(Button)findViewById(R.id.detail_pay);

//            正在计时支付 TODO 前一页
            detail_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                     intent.setClass(DetailActivity.this, PayDemoActivity.class);
                     intent.putExtra("yuYueOrder", parkYuYue);
                     startActivity(intent);

                }

            });
        }
//
//        已完成
        else{
            setContentView(R.layout.activity_detail);
            detail_parkName=(TextView)findViewById(R.id.detail_parkName);
            detail_fee=(TextView)findViewById(R.id.detail_fee);
            detail_startTime=(TextView)findViewById(R.id.detail_startTime);
            detail_endTime=(TextView)findViewById(R.id.detail_endTime);
            detail_time=(TextView)findViewById(R.id.detail_time);
            detail_money=(TextView)findViewById(R.id.detail_money);

            detail_parkName.setText(order.getPlace_name());
            detail_fee.setText("收费标准：6元/小时");
            detail_startTime.setText("开始时间：" + order.getIn_time());
            detail_endTime.setText("结束时间：" + order.getOut_time());
            detail_time.setText("停车用时："+order.getPark_time());
            detail_money.setText("停车费用："+order.getReal_park_fee());

        }


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
            detail_payNeed.setText("共需支付："+yuYueMoney+"元");
        }
    };

}