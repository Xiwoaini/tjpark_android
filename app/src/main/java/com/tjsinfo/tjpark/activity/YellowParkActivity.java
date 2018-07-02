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

import java.io.File;
import java.net.URISyntaxException;
import java.util.Iterator;

import com.tjsinfo.tjpark.R;
import com.tjsinfo.tjpark.entity.Park;
import com.tjsinfo.tjpark.fragment.OneFragment;
import com.tjsinfo.tjpark.util.NetConnection;

/**
 * Created by panning on 2018/1/12.
 */
//充电停车场
public class YellowParkActivity extends AppCompatActivity {

    private TextView placeId,yellowPark_placeName,
            yellowPark_address,bluePark_JTLX,KXCWKC,KXCWMC;
    private TextView YYSCJT,ZCWSJT,MFSCJT,FDJGJT;
    private Button exitBtn,yellowPark_yuYue,XQ;
    private Park park;
    //类型图片
    private ImageView img1,img2,img3,img4,img5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yellowpark);
             /*获取Intent中的Bundle对象*/
        Bundle bundle = this.getIntent().getExtras();
        park  = (Park) bundle.get("park");
        exitBtn =(Button)findViewById(R.id.exitBtn);
        //返回按钮监听
        exitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }

        });
        //获取控件
        placeId=(TextView)findViewById(R.id.placeId);
        yellowPark_placeName =(TextView)findViewById(R.id.yellowPark_placeName);
        yellowPark_address =(TextView)findViewById(R.id.yellowPark_address);
        bluePark_JTLX =(TextView)findViewById(R.id.bluePark_JTLX);
        KXCWKC= (TextView)findViewById(R.id.KXCWKC);
        KXCWMC= (TextView)findViewById(R.id.KXCWMC);
        YYSCJT= (TextView)findViewById(R.id.YYSCJT);
        ZCWSJT= (TextView)findViewById(R.id.ZCWSJT);
        MFSCJT= (TextView)findViewById(R.id.MFSCJT);
        FDJGJT= (TextView)findViewById(R.id.FDJGJT);
        img1 = (ImageView)findViewById(R.id.img1);
        img2 = (ImageView)findViewById(R.id.img2);
        img3 = (ImageView)findViewById(R.id.img3);
        img4 = (ImageView)findViewById(R.id.img4);
        img5 = (ImageView)findViewById(R.id.img5);
        //默认为隐藏的id
        placeId.setText(park.getId());
        //名称
        yellowPark_placeName.setText(park.getPlace_name());
        //地址
        yellowPark_address.setText(park.getPlace_address());
        if (!park.getLable().contains("预约")){
            yellowPark_yuYue = (Button) findViewById(R.id.yellowPark_yuYue);
            yellowPark_yuYue.setEnabled(false);
            yellowPark_yuYue.setText("不支持预约");
            yellowPark_yuYue.setBackgroundColor(getResources().getColor(R.color.gray));
        }

        new Thread(runnable).start();

        int i = 1;
        //类型图片
        if (park.getLable().contains("地上")){
            switch (i){
                case 1:
                    img1.setBackgroundResource(R.drawable.zntc);
                    i = 2;
                    break;
            }
        }
        if (park.getLable().contains("预约")){
            switch (i){
                case 1:
                    img1.setBackgroundResource(R.drawable.zntc);
                    i = 2;
                    break;
                case 2:
                    img2.setBackgroundResource(R.drawable.zntc);
                    i = 3;
                    break;
            }

        }
        if (park.getLable().contains("充电")){
            switch (i){
                case 1:
                    img1.setBackgroundResource(R.drawable.zntc);
                    i = 2;
                    break;
                case 2:
                    img2.setBackgroundResource(R.drawable.zntc);
                    i = 3;
                    break;
                case 3:
                    img3.setBackgroundResource(R.drawable.zntc);
                    i = 4;
                    break;

            }
        }
        if (park.getLable().contains("共享")){
            switch (i){
                case 1:
                    img1.setBackgroundResource(R.drawable.zntc);
                    i = 2;
                    break;
                case 2:
                    img2.setBackgroundResource(R.drawable.zntc);
                    i = 3;
                    break;
                case 3:
                    img3.setBackgroundResource(R.drawable.zntc);
                    i = 4;
                    break;
                case 4:
                    img4.setBackgroundResource(R.drawable.zntc);
                    i = 5;
                    break;
            }
        }
        if (park.getLable().contains("在线支付")){
            switch (i){
                case 1:
                    img1.setBackgroundResource(R.drawable.zntc);
                    i = 2;
                    break;
                case 2:
                    img2.setBackgroundResource(R.drawable.zntc);
                    i = 3;
                    break;
                case 3:
                    img3.setBackgroundResource(R.drawable.zntc);
                    i = 4;
                    break;
                case 4:
                    img4.setBackgroundResource(R.drawable.zntc);
                    i = 5;
                    break;
                case 5:
                    img5.setBackgroundResource(R.drawable.zntc);
                    break;
            }
        }




//详情跳转
        XQ = (Button) findViewById(R.id.XQ);
        XQ.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setClass(YellowParkActivity.this, ChargeActivity.class);
                intent.putExtra("chargeParkId",park.getId());
                startActivity(intent);

            }
        });
    }


    //新线程进行网络请求
    Runnable runnable = new Runnable(){
        @Override
        public void run() {
//调用接口查看普通
            JsonArray jsonArray0 = null;
            String strUrl="/tjpark/app/AppWebservice/findPilePark?parkid='"+park.getId()+"'";
            jsonArray0 = NetConnection.getJsonArray(strUrl);
            if (jsonArray0 == null){
                return;
            }
            Iterator it = jsonArray0.iterator();
            JsonObject jso = jsonArray0.get(0).getAsJsonObject();

            Message msg = new Message();
            handler.sendMessage(msg);


        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            FDJGJT.setText("无");
            YYSCJT.setText(park.getPile_time());
            ZCWSJT.setText(park.getPlace_total_num());
            MFSCJT.setText(park.getPile_time());
            KXCWKC.setText("空闲" + park.getFast_pile_space_num() +"/ 共 "+park.getFast_pile_total_num());
            KXCWMC.setText("空闲" + park.getSlow_pile_space_num() +"/ 共 "+park.getSlow_pile_total_num());
        }
    };


    //预约按钮click事件
    public void yellowPark_yuYue(View view){

        Intent intent = new Intent();
        intent.setClass(YellowParkActivity.this, BlueYuYueActivity.class);
        intent.putExtra("park",park);
        startActivity(intent);


    }

    //导航按钮
    public void blueDaoHang(View view) {
        //todo:当前位置
        Intent intent = null;
        try {

            String uri = "intent://map/direction?origin=latlng:"+ OneFragment.latitude+","+OneFragment.longitude+"|name:我的位置&destination=" + yellowPark_address.getText() + "&mode=drivingion=" + "城市" + "&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";

            intent = Intent.getIntent(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if(isInstallByread("com.baidu.BaiduMap")){
            startActivity(intent); //启动调用

        }else{
            new AlertDialog.Builder(YellowParkActivity.this)
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