package tjpark.tjsinfo.com.tjpark;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.feezu.liuli.timeselector.TimeSelector;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import tjpark.tjsinfo.com.tjpark.adapter.MyAdapter;
import tjpark.tjsinfo.com.tjpark.util.NetConnection;

/**
 * Created by panning on 2018/1/12.
 */

public class UpdateShareActivity extends AppCompatActivity {

    String[] typeArray = {"每天","周中","周末"};
    private EditText startTime,endTime;
    private Spinner sType;
    private Button saveBtn;
    Intent getIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shareedit);
        saveBtn= (Button)findViewById(R.id.saveBtn);
        startTime = (EditText)findViewById(R.id.startTime);
        endTime = (EditText)findViewById(R.id.endTime);
        sType = (Spinner)findViewById(R.id.sType);

        final List<String> dataType = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            dataType.add(typeArray[i]);
        }
        MyAdapter adapterType = new MyAdapter(this);
        sType.setAdapter(adapterType);
        adapterType.setDatas(dataType);


        //开始时间
        getIntent= getIntent();
        startTime.setText(getIntent.getStringExtra("KSSJ"));

        startTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    TimeSelector timeSelector = new TimeSelector(UpdateShareActivity.this, new TimeSelector.ResultHandler() {
                        @Override
                        public void handle(String time) {

                            startTime.setText(time);
                        }
                    }, "2018-01-01 00:00", "2030-12-31 23:59:59");

                    timeSelector.show();
                }
            }
        });
        // 结束时间
        endTime.setText(getIntent.getStringExtra("JSSJ"));

        endTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    TimeSelector timeSelector = new TimeSelector(UpdateShareActivity.this, new TimeSelector.ResultHandler() {
                        @Override
                        public void handle(String time) {
                            Toast.makeText(UpdateShareActivity.this, time, Toast.LENGTH_SHORT).show();
                            startTime.setText(time);
                        }
                    }, "2018-01-01 00:00", "2030-12-31 23:59:59");

                    timeSelector.show();
                }
            }
        });

//保存按钮
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
//
                        JsonObject res = null;
                        String strUrl="/tjpark/app/AppWebservice/updateSharePark?" +
                                "id=" +getIntent.getStringExtra("id")+
                                "&start_time=" +startTime.getText()+
                                "&end_time=" +endTime.getText()+
                                "&model="+sType.getSelectedItem().toString();
                        res = NetConnection.getXpath(strUrl);




                        Message msg = new Message();
                        handler.sendMessage(msg);
                    }
                }).start();


            }

        });

        //返回按钮监听
        Button exitBtn = findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }

        });

    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {


            super.handleMessage(msg);

            new AlertDialog.Builder(UpdateShareActivity.this)
                    .setTitle("提示")
                    .setMessage("修改成功!")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent();
                            intent.setClass(UpdateShareActivity.this, MyShareActivity.class);
                            startActivity(intent);
                        }
                    })
                    .show();

        }};

}