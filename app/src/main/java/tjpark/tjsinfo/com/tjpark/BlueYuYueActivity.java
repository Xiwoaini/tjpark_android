package tjpark.tjsinfo.com.tjpark;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.feezu.liuli.timeselector.TimeSelector;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import tjpark.tjsinfo.com.tjpark.entity.Car;
import tjpark.tjsinfo.com.tjpark.entity.ParkDetail;
import tjpark.tjsinfo.com.tjpark.fragment.FourFragment;
import tjpark.tjsinfo.com.tjpark.util.CarAdapter;
import tjpark.tjsinfo.com.tjpark.util.NetConnection;
import tjpark.tjsinfo.com.tjpark.util.OrderPay;
import tjpark.tjsinfo.com.tjpark.util.PayDemoActivity;

/**
 * Created by panning on 2018/1/12.
 */

public class BlueYuYueActivity extends AppCompatActivity {
    private   List<Car> myCarList = new LinkedList<Car>();
    private TextView blue_yuYueParkName;
    private TextView blue_yuYueDistance;
    private TextView blue_yuYueType;
    private TextView blue_yuYueAddress;
    private TextView blue_yuYueMoney;
    private Button blue_yuYueCar;
    private Button blue_yuYueTime;
    private Button blue_yuYueBtn;

    private ListView listView;
    Dialog dialog;

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
        blue_yuYueParkName = (TextView) findViewById(R.id.blue_yuYueParkName);
        blue_yuYueDistance = (TextView) findViewById(R.id.blue_yuYueDistance);
        blue_yuYueType = (TextView) findViewById(R.id.blue_yuYueType);
        blue_yuYueAddress = (TextView) findViewById(R.id.blue_yuYueAddress);
        blue_yuYueMoney = (TextView) findViewById(R.id.blue_yuYueMoney);
        blue_yuYueCar = (Button) findViewById(R.id.blue_yuYueCar);
        blue_yuYueTime = (Button) findViewById(R.id.blue_yuYueTime);
        blue_yuYueBtn = (Button) findViewById(R.id.blue_yuYueBtn);

        Intent getIntent = getIntent();
        blue_yuYueParkName.setText(getIntent.getStringExtra("bluePark_placeName"));
        blue_yuYueDistance.setText(getIntent.getStringExtra("bluePark_distance"));
        blue_yuYueType.setText(getIntent.getStringExtra("bluePark_label"));
        blue_yuYueAddress.setText(getIntent.getStringExtra("bluePark_address"));

    }


    //选择车位按钮
    public void blue_yuYueCar(View view) {

        new Thread(runnable).start();

        dialog = new Dialog(BlueYuYueActivity.this);

        dialog.setContentView(R.layout.activity_selectcar);
        CarAdapter adapter = new CarAdapter(BlueYuYueActivity.this, R.layout.activity_mycarview, myCarList);
        //获取listView，
        listView = (ListView) dialog.findViewById(R.id.listView);
        //为listView赋值
        listView.setAdapter(adapter);
        BlueYuYueActivity.ListViewListener ll = new BlueYuYueActivity.ListViewListener();
        listView.setOnItemClickListener(ll);

        dialog.show();


    }

    //选择时间按钮
    public void blue_yuYueTime(View view) {

        TimeSelector timeSelector = new TimeSelector(BlueYuYueActivity.this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                Toast.makeText(BlueYuYueActivity.this, time, Toast.LENGTH_SHORT).show();
//            2015-01-01 00:00
                //时间格式化
                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd mm:ss");

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
        }, "2018-01-01 00:00", "2030-12-31 23:59:59");

        timeSelector.show();
    }

    //预约车位按钮
    public void blue_yuYueBtn(View view) {

        if (blue_yuYueCar.getText().equals("选择车辆") || blue_yuYueTime.getText().equals("选择时间")) {
            new AlertDialog.Builder(BlueYuYueActivity.this)
                    .setTitle("提示")
                    .setMessage("请选择好所有信息!")
                    .setPositiveButton("确定", null)
                    .show();
        }

        else {
            Intent intent = new Intent();
            intent.setClass(BlueYuYueActivity.this, PayDemoActivity.class);
            intent.putExtra("blue_yuYueParkName", blue_yuYueParkName.getText());

            intent.putExtra("blue_yuYueMoney", blue_yuYueMoney.getText());
            startActivity(intent);
        }

    }

    class ListViewListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            blue_yuYueCar.setText(myCarList.get(position).getPlace_number());

            dialog.hide();
        }

    }

    //新线程进行网络请求
    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            JsonArray jsonArray = null;
            String strUrl="/tjpark/app/AppWebservice/findPlate?customerid=40288afd5c43e114015c43f2d85f0000";
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
//                car.setPark_id(jso.get("park_id").toString());
                car.setCreated_time(jso.get("created_time").toString().replace("\"",""));
                car.setCustomer_id(jso.get("customer_id").toString().replace("\"",""));
                car.setPlace_number(jso.get("place_number").toString().replace("\"",""));
                myCarList.add(car);
                i++;

            }
            i=0;


            Message msg = new Message();
            Bundle data = new Bundle();

            msg.setData(data);
            handler.sendMessage(msg);
        }
    };
    //处理远程结果
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            //为listView赋值
//            listView.setAdapter(new ArrayAdapter<String>(BlueYuYueActivity.this, android.R.layout.simple_expandable_list_item_1,getData()));
//            setContentView(listView);
//            BlueYuYueActivity.ListViewListener listViewListener =new BlueYuYueActivity.ListViewListener();
//            listView.setOnItemClickListener(listViewListener);



        }
    };
    //调用远程接口获取账号车辆
    private List<String> getData(){

        List<String> data = new ArrayList<String>();
        for(int i =0;i<myCarList.size();i++){
            data.add(myCarList.get(i).getPlace_number());
        }
        return data;
    }

}