package tjpark.tjsinfo.com.tjpark;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

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

        detail_parkName=(TextView)findViewById(R.id.detail_parkName);
        detail_parkName.setText("停车场名称:");
        detail_fee=(TextView)findViewById(R.id.detail_fee);
        detail_fee.setText("收费标准:");
        detail_startTime=(TextView)findViewById(R.id.detail_startTime);
        detail_startTime.setText("停车开始时间:");
        detail_endTime=(TextView)findViewById(R.id.detail_endTime);
        detail_endTime.setText("停车结束时间:");
        detail_time=(TextView)findViewById(R.id.detail_time);
        detail_time.setText("停车场用时:");
        detail_money=(TextView)findViewById(R.id.detail_money);
        detail_money.setText("总费用:");
    }













}