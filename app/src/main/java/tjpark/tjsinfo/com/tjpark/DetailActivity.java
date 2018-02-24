package tjpark.tjsinfo.com.tjpark;

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

import tjpark.tjsinfo.com.tjpark.entity.Order;
import tjpark.tjsinfo.com.tjpark.util.NetConnection;
import tjpark.tjsinfo.com.tjpark.util.OrderAdapter;

/**
 * Created by panning on 2018/1/12.
 */

public class DetailActivity  extends AppCompatActivity {

    //绑定前台控件
    private TextView detail_parkName;
    private TextView detail_fee;
    private TextView detail_startTime;
    private TextView detail_endTime;
    private TextView detail_time;
    private TextView detail_money;


    //初始化
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Button exitBtn=(Button)findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }

        });

        detail_parkName=(TextView)findViewById(R.id.detail_parkName);
        detail_fee=(TextView)findViewById(R.id.detail_fee);
        detail_startTime=(TextView)findViewById(R.id.detail_startTime);
        detail_endTime=(TextView)findViewById(R.id.detail_endTime);
        detail_time=(TextView)findViewById(R.id.detail_time);
        detail_money=(TextView)findViewById(R.id.detail_money);
        //获取传过来的数据
          /*获取Intent中的Bundle对象*/
        Bundle bundle = this.getIntent().getExtras();
        /*获取Bundle中的数据，注意类型和key*/
        Order order =new Order();
        order = (Order)bundle.getSerializable("detail");
        detail_parkName.setText(order.getPlace_name());
        detail_fee.setText("收费标准:  6元/小时");
        detail_startTime.setText("停车开始时间:  "+order.getIn_time());
        detail_endTime.setText("停车结束时间:  "+order.getOut_time());
        detail_time.setText("停车场用时:  "+order.getPark_time());
        detail_money.setText("总费用:  "+order.getReal_park_fee());
    }


}