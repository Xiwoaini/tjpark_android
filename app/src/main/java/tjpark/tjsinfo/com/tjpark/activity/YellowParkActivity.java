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
import java.time.Year;
import java.util.Iterator;

import tjpark.tjsinfo.com.tjpark.R;
import tjpark.tjsinfo.com.tjpark.util.NetConnection;

/**
 * Created by panning on 2018/1/12.
 */
//充电停车场
public class YellowParkActivity extends AppCompatActivity {

    private TextView placeId,yellowPark_placeName,yellowPark_distance,yellowPark_label,
            yellowPark_address, yellowPark_JTSJ,bluePark_JTLX,yellowPark_JTGZ1,
            yellowPark_JTGZ2,yellowPark_JTFY, yellowPark_KSCD,yellowPark_MSCD,
            yellowPark_JTKFSJ;
    private Button exitBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yellowpark);
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
        yellowPark_distance =(TextView)findViewById(R.id.yellowPark_distance);
        yellowPark_label =(TextView)findViewById(R.id.yellowPark_label);
        yellowPark_address =(TextView)findViewById(R.id.yellowPark_address);
        yellowPark_JTSJ =(TextView)findViewById(R.id.yellowPark_JTSJ);
        bluePark_JTLX =(TextView)findViewById(R.id.bluePark_JTLX);
        yellowPark_JTGZ1 =(TextView)findViewById(R.id.yellowPark_JTGZ1);
        yellowPark_JTGZ2 = (TextView)findViewById(R.id.yellowPark_JTGZ2);
        yellowPark_JTFY =(TextView)findViewById(R.id.yellowPark_JTFY);
        yellowPark_KSCD =(TextView)findViewById(R.id.yellowPark_KSCD);
        yellowPark_MSCD =(TextView)findViewById(R.id.yellowPark_MSCD);
        yellowPark_JTKFSJ = (TextView)findViewById(R.id.yellowPark_JTKFSJ);

        new Thread(runnable).start();

    }


    //新线程进行网络请求
    Runnable runnable = new Runnable(){
        @Override
        public void run() {

//调用接口查看普通
            JsonArray jsonArray0 = null;
            Intent getIntent = getIntent();
            String s = getIntent.getStringExtra("parkId");
            String strUrl="/tjpark/app/AppWebservice/detailPark?parkid='"+s+"'";
            jsonArray0 = NetConnection.getJsonArray(strUrl);
            Iterator it = jsonArray0.iterator();
            JsonObject jso = jsonArray0.get(0).getAsJsonObject();
            //调用接口查看普通
            JsonArray jsonArray1 = null;


            String strUrl1="/tjpark/app/AppWebservice/feePark?parkid='"+s+"'";
            jsonArray1 = NetConnection.getJsonArray(strUrl1);
            Iterator it1 = jsonArray1.iterator();
            JsonObject jso1 = jsonArray1.get(0).getAsJsonObject();


            //默认为隐藏的id
            placeId.setText(getIntent.getStringExtra("parkId"));
            //名称
            yellowPark_placeName.setText(getIntent.getStringExtra("place_name"));
            //距离
            yellowPark_distance.setText(getIntent.getStringExtra("place_distance"));
            //地址
            yellowPark_address.setText(getIntent.getStringExtra("place_address"));
            //类型
            yellowPark_label.setText(jso.get("lable").toString().replace("\"",""));
            //具体时间

            yellowPark_JTSJ.setText(jso.get("open_time").toString().replace("\"",""));
            bluePark_JTLX.setText(jso.get("period_type").toString().replace("\"",""));

            yellowPark_JTGZ1.setText(jso1.get("fee").toString().replace("\"",""));
            yellowPark_JTGZ2.setText(jso.get("open_time").toString().replace("\"",""));
            yellowPark_JTFY.setText(getIntent.getStringExtra("pile_fee"));
            yellowPark_KSCD.setText(getIntent.getStringExtra("fast_pile_total_num"));
            yellowPark_MSCD.setText(getIntent.getStringExtra("slow_pile_total_num"));
            yellowPark_JTKFSJ.setText(getIntent.getStringExtra("pile_time"));



        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);



        }
    };


    //预约按钮click事件
    public void yellowPark_yuYue(View view){

        Intent intent = new Intent();
        intent.setClass(YellowParkActivity.this, BlueYuYueActivity.class);
//        <!--车场名称--> <!--距离-->  <!--类型--><!--地址-->
        intent.putExtra("parkId",placeId.getText().toString());
        intent.putExtra("bluePark_placeName",yellowPark_placeName.getText().toString());
        intent.putExtra("bluePark_distance",yellowPark_distance.getText().toString());
        intent.putExtra("bluePark_label",yellowPark_label.getText().toString());
        intent.putExtra("bluePark_address",yellowPark_address.getText().toString());
        startActivity(intent);


    }

    //导航按钮
    public void blueDaoHang(View view) {


        //todo:当前位置
        Intent intent = null;
        try {

            String uri = "intent://map/direction?origin=latlng:39.9761,116.3282|name:我的位置&destination=" + yellowPark_address.getText() + "&mode=drivingion=" + "城市" + "&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";

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