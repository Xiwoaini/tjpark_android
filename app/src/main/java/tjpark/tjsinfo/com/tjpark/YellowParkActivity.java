package tjpark.tjsinfo.com.tjpark;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Iterator;

import tjpark.tjsinfo.com.tjpark.util.NetConnection;

/**
 * Created by panning on 2018/1/12.
 */

public class YellowParkActivity extends AppCompatActivity {

    private TextView yellowPark_placeName,yellowPark_distance,yellowPark_label,
            yellowPark_address, yellowPark_JTSJ,bluePark_JTLX,yellowPark_JTGZ1,
            yellowPark_JTGZ2,yellowPark_JTFY, yellowPark_KSCD,yellowPark_MSCD,
            yellowPark_JTKFSJ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yellowpark);
        new Thread(runnable).start();
    }


    //新线程进行网络请求
    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            //获取控件
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


//调用接口
            JsonArray jsonArray = null;
            Intent getIntent = getIntent();
            String s = getIntent.getStringExtra("parkId");
            String strUrl="/tjpark/app/AppWebservice/detailPark?parkid='"+s+"'";
            jsonArray = NetConnection.getJsonArray(strUrl);
            Iterator it = jsonArray.iterator();
            JsonObject jso = jsonArray.get(0).getAsJsonObject();


//todo 需要查询充电停车场的id，还需要调用远程接口
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
            yellowPark_JTGZ1.setText(jso.get("lable").toString().replace("\"",""));
            yellowPark_JTGZ2.setText(jso.get("open_time").toString().replace("\"",""));
            yellowPark_JTFY.setText(jso.get("lable").toString().replace("\"",""));
            yellowPark_KSCD.setText(jso.get("lable").toString().replace("\"",""));
            yellowPark_MSCD.setText(jso.get("lable").toString().replace("\"",""));
            yellowPark_JTKFSJ.setText(jso.get("lable").toString().replace("\"",""));
//[{"lable":"地上,预约,充电","start_time":"07:00","end_time":"16:00","type":"收费","period_type":"收费","open_time":"07:00-16:00"}]
//            intent.putExtra("place_num",place_num);



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
//        intent.putExtra("bluePark_placeName",bluePark_placeName.getText().toString());
        intent.putExtra("bluePark_placeName",yellowPark_placeName.getText().toString());
        intent.putExtra("bluePark_distance",yellowPark_distance.getText().toString());
        intent.putExtra("bluePark_label",yellowPark_label.getText().toString());
        intent.putExtra("bluePark_address",yellowPark_address.getText().toString());
        startActivity(intent);


    }
}