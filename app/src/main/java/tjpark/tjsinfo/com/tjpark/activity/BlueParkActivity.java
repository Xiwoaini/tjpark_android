package tjpark.tjsinfo.com.tjpark.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Iterator;

import tjpark.tjsinfo.com.tjpark.R;
import tjpark.tjsinfo.com.tjpark.entity.Park;
import tjpark.tjsinfo.com.tjpark.fragment.OneFragment;
import tjpark.tjsinfo.com.tjpark.util.NetConnection;

/**
 * Created by panning on 2018/1/12.
 */

public class BlueParkActivity extends AppCompatActivity {

    private TextView bluePark_placeName;
    private TextView bluePark_distance;
    private TextView bluePark_label;
    private TextView bluePark_address;
    private TextView bluePark_JTSJ,bluePark_JTLX;
    private TextView bluePark_JTGZ1,bluePark_JTGZ2,bluePark_placeId;


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
        bluePark_distance =(TextView)findViewById(R.id.bluePark_distance);
        bluePark_label =(TextView)findViewById(R.id.bluePark_label);
        bluePark_address =(TextView)findViewById(R.id.bluePark_address);
        bluePark_JTSJ =(TextView)findViewById(R.id.bluePark_JTSJ);
        bluePark_JTGZ1 =(TextView)findViewById(R.id.bluePark_JTGZ1);
        bluePark_JTGZ2 =(TextView)findViewById(R.id.bluePark_JTGZ2);
        bluePark_JTLX = (TextView)findViewById(R.id.bluePark_JTLX);

        //id，默认为隐藏
        bluePark_placeId.setText(park.getId());
        //名称
        bluePark_placeName.setText(park.getPlace_name());
        //地址
        bluePark_address.setText("地址:"+park.getPlace_address());

        //距离
        bluePark_distance.setText("距离:"+park.getDistance());
        //类型
        bluePark_label.setText("类型:"+park.getLable());
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