package tjpark.tjsinfo.com.tjpark;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Iterator;

import tjpark.tjsinfo.com.tjpark.entity.ParkDetail;
import tjpark.tjsinfo.com.tjpark.util.NetConnection;
import tjpark.tjsinfo.com.tjpark.util.OrderAdapter;

/**
 * Created by panning on 2018/1/12.
 */

public class BlueParkActivity extends AppCompatActivity {

    private TextView bluePark_placeName;
    private TextView bluePark_distance;
    private TextView bluePark_label;
    private TextView bluePark_address;
    private TextView bluePark_JTSJ,bluePark_JTLX;
    private TextView bluePark_JTGZ1,bluePark_JTGZ2;
    private ParkDetail parkDetail = new ParkDetail();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluepark);
        new Thread(runnable).start();

    }

    //新线程进行网络请求
    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            //获取控件
            bluePark_placeName =(TextView)findViewById(R.id.bluePark_placeName);
            bluePark_distance =(TextView)findViewById(R.id.bluePark_distance);
            bluePark_label =(TextView)findViewById(R.id.bluePark_label);
            bluePark_address =(TextView)findViewById(R.id.bluePark_address);
            bluePark_JTSJ =(TextView)findViewById(R.id.bluePark_JTSJ);
            bluePark_JTGZ1 =(TextView)findViewById(R.id.bluePark_JTGZ1);
            bluePark_JTGZ2 =(TextView)findViewById(R.id.bluePark_JTGZ2);
            bluePark_JTLX = (TextView)findViewById(R.id.bluePark_JTLX);


//调用接口
            JsonArray jsonArray = null;
            Intent getIntent = getIntent();
            String s = getIntent.getStringExtra("parkId");
            String strUrl="/tjpark/app/AppWebservice/detailPark?parkid='"+s+"'";
            jsonArray = NetConnection.getJsonArray(strUrl);
            Iterator it = jsonArray.iterator();
            JsonObject jso = jsonArray.get(0).getAsJsonObject();

//            place_num ，剩余车位数
            //名称
            bluePark_placeName.setText(getIntent.getStringExtra("place_name"));
            //地址
            bluePark_address.setText("地址:"+getIntent.getStringExtra("place_address"));

            //距离
            bluePark_distance.setText("距离:"+getIntent.getStringExtra("place_distance"));
            //类型
            bluePark_label.setText("类型:"+jso.get("lable").toString().replace("\"",""));

//计费时段下的开始和结束时间
            bluePark_JTSJ.setText(jso.get("open_time").toString().replace("\"",""));
//是否收费
            bluePark_JTLX.setText(jso.get("type").toString().replace("\"",""));
            bluePark_JTGZ2.setText(jso.get("open_time").toString().replace("\"",""));



        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);



        }
    };


    //预约按钮click事件
    public void bluePark_yuYue(View view){

        Intent intent = new Intent();
        intent.setClass(BlueParkActivity.this, BlueYuYueActivity.class);
//        <!--车场名称--> <!--距离-->  <!--类型--><!--地址-->
                intent.putExtra("bluePark_placeName",bluePark_placeName.getText().toString());
                intent.putExtra("bluePark_distance",bluePark_distance.getText().toString());
                intent.putExtra("bluePark_label",bluePark_label.getText().toString());
                intent.putExtra("bluePark_address",bluePark_address.getText().toString());
        startActivity(intent);

    }

    //导航按钮
    public void daoHang(View view) {
//        if (loc1 == null || loc2 == null) {
//            return;
//        }
//        if (loc1.getAddress() == null || "".equals(loc1.getAddress())) {
//            loc1.setAddress("我的位置");
//        }
//        if (loc2.getAddress() == null || "".equals(loc2.getAddress())) {
//            loc2.setAddress("目的地");
//        }
//        try {
//
////            Intent intent = Intent.getIntent("intent://map/direction?origin=latlng:" + loc1.getStringLatLng() + "|name:" + loc1.getAddress() + "&destination=latlng:" + loc2.getStringLatLng() + "|name:" + loc2.getAddress() + "&mode=transit&src=某某公司#Intent;" + "scheme=bdapp;package=com.baidu.BaiduMap;end");
//
////            Intent intent = Intent.getIntent("intent://map/direction?origin=|name:" + loc1.getAddress() + "&destination=|name:" + loc2.getAddress() + "&mode=transit&src=某某公司#Intent;" + "scheme=bdapp;package=com.baidu.BaiduMap;end");
//
//            //起点  此处不传值默认选择当前位置
//            Intent intent = Intent.getIntent("intent://map/direction?destination=|name:" + loc2.getAddress() + "&mode=transit&src=某某公司#Intent;" + "scheme=bdapp;package=com.baidu.BaiduMap;end");
//
//            context.startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(context, "地址解析错误", Toast.LENGTH_SHORT).show();
//        }
    }



}