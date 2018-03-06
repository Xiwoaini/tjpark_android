package tjpark.tjsinfo.com.tjpark;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import tjpark.tjsinfo.com.tjpark.entity.Order;

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
    private TextView detail_payNeed;
    private TextView detail_paySuccess;
    private Button detail_pay;


    //初始化
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取传过来的数据
        Bundle bundle = this.getIntent().getExtras();
        /*获取Bundle中的数据，注意类型和key*/
        Order order =new Order();
        order = (Order)bundle.getSerializable("detail");
        if (order.getStatus().equals("待支付")){
            setContentView(R.layout.activity_detailpay);
            detail_pay =(Button)findViewById(R.id.detail_pay);
            detail_paySuccess=(TextView) findViewById(R.id.detail_paySuccess);
            detail_paySuccess.setText("已支付费用 : "+order.getReservation_park_fee());
            detail_payNeed=(TextView)findViewById(R.id.detail_payNeed);
            detail_payNeed.setText("需支付费用 : "+String.valueOf(Double.parseDouble(order.getReal_park_fee().replace("元",""))- Double.parseDouble(order.getReservation_park_fee().replace("元","")))+"元");

//            补差支付
            detail_pay.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
//                    parkPay

                }

            });

        }

        else if (order.getStatus().equals("正在计时")){
            setContentView(R.layout.activity_detailcurrent);
            detail_pay =(Button)findViewById(R.id.detail_pay);

//            正在计时支付
            detail_pay.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
//                    getPayMoney getPlaceIp
                }

            });
        }
//        已完成
        else{
            setContentView(R.layout.activity_detail);

        }
        detail_parkName=(TextView)findViewById(R.id.detail_parkName);
        detail_fee=(TextView)findViewById(R.id.detail_fee);
        detail_startTime=(TextView)findViewById(R.id.detail_startTime);
        detail_endTime=(TextView)findViewById(R.id.detail_endTime);
        detail_time=(TextView)findViewById(R.id.detail_time);
        detail_money=(TextView)findViewById(R.id.detail_money);

        detail_parkName.setText(order.getPlace_name());
        detail_fee.setText("收费标准 : 6元/小时");
        detail_startTime.setText("停车开始时间 : "+order.getIn_time());
        if (order.getOut_time().equals("")){
            Date date =new Date();
            SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            detail_endTime.setText("停车结束时间 : "+sdf.format(date));
        }
        else{
            detail_endTime.setText("停车结束时间:  "+order.getOut_time());
        }
        detail_time.setText("停车场用时 : "+order.getPark_time());
        detail_money.setText("总费用 : "+order.getReal_park_fee());

        Button exitBtn=(Button)findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }

        });


    }


}