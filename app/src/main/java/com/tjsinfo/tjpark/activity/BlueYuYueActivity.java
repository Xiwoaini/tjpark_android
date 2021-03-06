package com.tjsinfo.tjpark.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.feezu.liuli.timeselector.TimeSelector;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import com.tjsinfo.tjpark.R;
import com.tjsinfo.tjpark.adapter.SelectCarAdapter;
import com.tjsinfo.tjpark.entity.Car;
import com.tjsinfo.tjpark.entity.Park;
import com.tjsinfo.tjpark.entity.ParkYuYue;
import com.tjsinfo.tjpark.util.NetConnection;
import com.tjsinfo.tjpark.wxapi.PayDemoActivity;

/**
 * Created by panning on 2018/1/12.
 */

public class BlueYuYueActivity extends AppCompatActivity {
    private SharedPreferences mSharedPreferences;
    private   List<Car> myCarList = new LinkedList<Car>();
    private TextView blue_yuYueParkName;
    private TextView blue_yuYueAddress;
    private TextView blue_yuYueMoney,placeId, plateId;
    private ImageView imageView1,imageView2,imageView3,imageView4,imageView5;
    private Button blue_yuYueCar;
    private Button blue_yuYueTime;
    private Button blue_yuYueBtn;
    private Park park = new Park();

    private ListView listView;
    Dialog dialog;

    Map<String,String> map =new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//初始化加载
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blueyuyue);

        Button exitBtn=(Button)findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }

        });

//获取控件
        plateId= (TextView) findViewById(R.id.plateId);
        placeId= (TextView) findViewById(R.id.placeId);
        blue_yuYueParkName = (TextView) findViewById(R.id.blue_yuYueParkName);
        blue_yuYueAddress = (TextView) findViewById(R.id.blue_yuYueAddress);
        blue_yuYueMoney = (TextView) findViewById(R.id.blue_yuYueMoney);
        blue_yuYueCar = (Button) findViewById(R.id.blue_yuYueCar);
        blue_yuYueTime = (Button) findViewById(R.id.blue_yuYueTime);
        blue_yuYueBtn = (Button) findViewById(R.id.blue_yuYueBtn);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView4 = (ImageView) findViewById(R.id.imageView4);
        imageView5 = (ImageView) findViewById(R.id.imageView5);


        Bundle bundle = this.getIntent().getExtras();
        park  = (Park) bundle.get("park");
        placeId.setText(park.getPlace_id());
        blue_yuYueParkName.setText(park.getPlace_name());
        blue_yuYueAddress.setText("地址: "+park.getPlace_address() +"   [" +park.getDistance()+"]");

        //类型图片
        if (park.getLable().equals("地上")){
            imageView1.setImageResource(R.drawable.dstb);
            imageView1.setVisibility(View.VISIBLE);

        }
        else if (park.getLable().equals("地上,预约")){
            imageView1.setImageResource(R.drawable.dstb);
            imageView1.setVisibility(View.VISIBLE);
            imageView2.setImageResource(R.drawable.yytb);
            imageView2.setVisibility(View.VISIBLE);
        }
        else if (park.getLable().equals("地上,预约，共享")){
            imageView1.setImageResource(R.drawable.dstb);
            imageView1.setVisibility(View.VISIBLE);
            imageView2.setImageResource(R.drawable.yytb);
            imageView2.setVisibility(View.VISIBLE);
            imageView3.setImageResource(R.drawable.gxtb);
            imageView3.setVisibility(View.VISIBLE);

        }
        else if (park.getLable().equals("地上,充电")){
            imageView1.setImageResource(R.drawable.dstb);
            imageView1.setVisibility(View.VISIBLE);
            imageView2.setImageResource(R.drawable.cdtb);
            imageView2.setVisibility(View.VISIBLE);
        }
        else if (park.getLable().equals("地上,预约,充电")){
            imageView1.setImageResource(R.drawable.dstb);
            imageView1.setVisibility(View.VISIBLE);
            imageView2.setImageResource(R.drawable.yytb);
            imageView2.setVisibility(View.VISIBLE);
            imageView3.setImageResource(R.drawable.cdtb);
            imageView3.setVisibility(View.VISIBLE);
        }

    }


    //选择车位按钮
    public void blue_yuYueCar(View view) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (myCarList.size() != 0 ){

                }
                else{
                    mSharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
                    String customerid=mSharedPreferences.getString("personID","");
                    JsonArray jsonArray = null;
                    String strUrl="/tjpark/app/AppWebservice/findPlate?customerid="+customerid;
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

                        car.setCreated_time(jso.get("created_time").toString().replace("\"",""));
                        car.setCustomer_id(jso.get("customer_id").toString().replace("\"",""));
                        car.setPlace_number(jso.get("place_number").toString().replace("\"",""));
                        myCarList.add(car);
                        i++;

                    }
                    i=0;
                }
                if (myCarList.size() ==0){
                    new AlertDialog.Builder(BlueYuYueActivity.this)
                            .setTitle("提示")
                            .setMessage("请先添加车辆。")
                            .setPositiveButton("确定", null)
                            .show();
                }
                Message msg = new Message();
                Bundle data = new Bundle();

                msg.setData(data);
                handler.sendMessage(msg);
            }
        }).start();



    }

    //选择时间按钮
    public void blue_yuYueTime(View view) {

        //时间格式化
        final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);

        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        TimeSelector timeSelector = new TimeSelector(BlueYuYueActivity.this, new TimeSelector.ResultHandler() {

            @Override
            public void handle(String time) {

                try {

                    Long l = (sdf.parse(time).getTime()) - (new Date().getTime());
                    Double d = Math.ceil(l.doubleValue() / 3600000);
                    if ((d * 6)<=0){
                        new AlertDialog.Builder(BlueYuYueActivity.this)
                                .setTitle("提示")
                                .setMessage("预约时间不能小于当前时间!")
                                .setPositiveButton("确定", null)
                                .show();
                    }
                    else{
                        blue_yuYueMoney.setText(String.valueOf(d * 6) + "元");
                        blue_yuYueTime.setText(time);
                    }

                } catch (Exception e) {
                    //请重新选择时间
                    Log.v("时间差为:", "错误");
                }

            }
        }, sdf.format(new Date().getTime()), sdf.format( new Date().getTime()+604800000));

        timeSelector.show();
    }

    //预约车位按钮
    public void blue_yuYueBtn(View view) {

        if (blue_yuYueCar.getText().equals("选择入场车辆") || blue_yuYueTime.getText().equals("选择离场时间")) {
            new AlertDialog.Builder(BlueYuYueActivity.this)
                    .setTitle("提示")
                    .setMessage("请选择好所有信息!")
                    .setPositiveButton("确定", null)
                    .show();
        }

        else {
            mSharedPreferences = getSharedPreferences("userInfo",MODE_PRIVATE);
            String customer_id=mSharedPreferences.getString("personID","");
            //普通预约需要的参数(reservableParkIn)
            Intent intent = new Intent();
            ParkYuYue parkYuYue =new ParkYuYue();
            parkYuYue.setCustomer_id(customer_id);
            parkYuYue.setPlate_number(blue_yuYueCar.getText().toString());
            parkYuYue.setPlate_id(plateId.getText().toString());
            parkYuYue.setPlace_id(park.getId());
            parkYuYue.setPlace_name(blue_yuYueParkName.getText().toString());
            parkYuYue.setReservation_time(blue_yuYueTime.getText().toString());
            parkYuYue.setReservation_fee(blue_yuYueMoney.getText().toString() );
            parkYuYue.setShare_id(park.getShare_id());
            parkYuYue.setPayMode(park.getLable());
            parkYuYue.setDetail_type("预约停车");
            intent.setClass(BlueYuYueActivity.this, PayDemoActivity.class);
            intent.putExtra("yuYueOrder", parkYuYue);
            startActivity(intent);

        }

    }

    class ListViewListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            blue_yuYueCar.setText(myCarList.get(position).getPlace_number());
            plateId.setText(myCarList.get(position).getId());
            dialog.hide();
        }

    }


    //处理远程结果
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");

            dialog = new Dialog(BlueYuYueActivity.this);

            dialog.setContentView(R.layout.activity_selectcar);
            SelectCarAdapter adapter = new SelectCarAdapter(BlueYuYueActivity.this, R.layout.activity_selectcaradapter, myCarList);
            //获取listView，
            listView = (ListView) dialog.findViewById(R.id.listView);
            //为listView赋值
            listView.setAdapter(adapter);
            BlueYuYueActivity.ListViewListener ll = new BlueYuYueActivity.ListViewListener();
            listView.setOnItemClickListener(ll);
            try{
                dialog.show();
            }
            catch (Exception e){

            }

        }
    };
    //调用远程接口获取账号车辆
    private List<String> getData(){

        List<String> data = new ArrayList<String>();
        for(int i =0;i<myCarList.size();i++){
            data.add(myCarList.get(i).getPlace_number());
            map.put(myCarList.get(i).getId(),myCarList.get(i).getPlace_number());
        }
        return data;
    }

}