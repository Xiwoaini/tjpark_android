package com.tjsinfo.tjpark.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tjsinfo.tjpark.entity.Park;
import com.tjsinfo.tjpark.fragment.OneFragment;
import com.tjsinfo.tjpark.util.NetConnection;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Iterator;

import com.tjsinfo.tjpark.R;

/**
 * Created by panning on 2018/1/12.
 */

public class BlueParkActivity extends AppCompatActivity {

    private TextView bluePark_placeName;
    private TextView bluePark_address;
    private TextView bluePark_JTSJ,bluePark_JTLX;
    private TextView bluePark_JTGZ1,bluePark_JTGZ2,bluePark_placeId;
    private Button bluePark_yuYue;
    private ImageView imageView1,imageView2,imageView3,imageView4,imageView5;

    JsonArray jsonArray = null;
    Intent getIntent = null;
    JsonObject jso = null;
    JsonArray jsonArray1 = null;
    JsonObject jso1 = null;
    Park park =new Park();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluepark);

        Bundle bundle = this.getIntent().getExtras();
        park  = (Park) bundle.get("park");
        Button exitBtn=(Button)findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }

        });
        //获取控件

        bluePark_placeId=(TextView)findViewById(R.id.bluePark_placeId);
        bluePark_placeName =(TextView)findViewById(R.id.bluePark_placeName);
        bluePark_address =(TextView)findViewById(R.id.bluePark_address);
        bluePark_JTSJ =(TextView)findViewById(R.id.bluePark_JTSJ);
        bluePark_JTGZ1 =(TextView)findViewById(R.id.bluePark_JTGZ1);
        bluePark_JTGZ2 =(TextView)findViewById(R.id.bluePark_JTGZ2);
        bluePark_JTLX = (TextView)findViewById(R.id.bluePark_JTLX);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView4 = (ImageView) findViewById(R.id.imageView4);
        imageView5 = (ImageView) findViewById(R.id.imageView5);

        //id，默认为隐藏
        bluePark_placeId.setText(park.getId());
        //名称
        bluePark_placeName.setText(park.getPlace_name());
        //地址
        bluePark_address.setText("地址:"+park.getPlace_address()+"   [" +park.getDistance() + "]");


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



        if (!park.getLable().contains("预约")){
            bluePark_yuYue = (Button) findViewById(R.id.bluePark_yuYue);
            bluePark_yuYue.setEnabled(false);
            bluePark_yuYue.setText("不支持预约");
            bluePark_yuYue.setBackgroundColor(getResources().getColor(R.color.gray));
        }
        new Thread(runnable).start();

    }

    //新线程进行网络请求
    Runnable runnable = new Runnable(){
        @Override
        public void run() {



//调用接口

            String strUrl="/tjpark/app/AppWebservice/detailPark?parkid='"+park.getId()+"'";
            jsonArray = NetConnection.getJsonArray(strUrl);
            if  (jsonArray == null){
                return ;
            }
            Iterator it = jsonArray.iterator();
            jso = jsonArray.get(0).getAsJsonObject();


            String strUrl1="/tjpark/app/AppWebservice/feePark?parkid='"+park.getId()+"'";
            jsonArray1 = NetConnection.getJsonArray(strUrl1);
            if  (jsonArray1 == null){
                return ;
            }
            Iterator it1 = jsonArray1.iterator();
              jso1 = jsonArray1.get(0).getAsJsonObject();

            Message msg = new Message();
            handler.sendMessage(msg);
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


//计费时段下的开始和结束时间
            bluePark_JTSJ.setText(jso.get("open_time").toString().replace("\"",""));
//是否收费
            bluePark_JTLX.setText(jso.get("type").toString().replace("\"",""));

            bluePark_JTGZ1.setText(jso1.get("park_time").toString().replace("\"","")+"  "+jso1.get("fee").toString().replace("\"","")+"  ");
            bluePark_JTGZ2.setText(jso.get("open_time").toString().replace("\"",""));


        }
    };


    //预约按钮click事件
    public void bluePark_yuYue(View view){

        Intent intent = new Intent();
        intent.setClass(BlueParkActivity.this, BlueYuYueActivity.class);
                intent.putExtra("park",park);

                startActivity(intent);

    }
    //导航按钮
    public void DaoHang(View view,String address) {
        //todo:当前位置
        Intent intent = null;
        try {

            String uri = "intent://map/direction?origin=latlng:"+ OneFragment.latitude+","+OneFragment.longitude+"|name:我的位置&destination=" + address + "&mode=drivingion=" + "城市" + "&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";

            intent = Intent.getIntent(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if(isInstallByread("com.baidu.BaiduMap")){
            startActivity(intent); //启动调用

        }else{
            new AlertDialog.Builder(view.getContext())
                    .setTitle("注意")
                    .setMessage("请先安装百度地图!")
                    .setPositiveButton("确定", null)
                    .show();

        }


    }
    //导航按钮
    public void blueDaoHang(View view) {
  //todo:当前位置
        Intent intent = null;
        try {

            String uri = "intent://map/direction?origin=latlng:"+ OneFragment.latitude+","+OneFragment.longitude+"|name:我的位置&destination=" + bluePark_address.getText() + "&mode=drivingion=" + "城市" + "&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";

            intent = Intent.getIntent(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if(isInstallByread("com.baidu.BaiduMap")){
            startActivity(intent); //启动调用

        }else{
            new AlertDialog.Builder(BlueParkActivity.this)
                    .setTitle("注意")
                    .setMessage("请先安装百度地图!")
                    .setPositiveButton("确定", null)
                    .show();

        }


    }

    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

}