package com.tjsinfo.tjpark.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tjsinfo.tjpark.R;
import com.tjsinfo.tjpark.entity.Park;

/**
 * Created by panning on 2018/6/27.
 */

public class PayResultActivity extends Activity {
    Intent getIntent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payresult);

        Bundle bundle = this.getIntent().getExtras();
        String moeny  =  bundle.getString("moeny");
        TextView  payMoney= findViewById(R.id.payMoney);
        payMoney.setText(moeny);

        Button pay_btn = findViewById(R.id.pay_btn);
        pay_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                //(当前Activity，目标Activity)
                intent.setClass(PayResultActivity.this, TabBarActivity.class);
                startActivity(intent);
            }

        });




    }

}
