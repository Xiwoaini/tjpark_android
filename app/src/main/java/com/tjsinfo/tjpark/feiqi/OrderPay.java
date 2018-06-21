package com.tjsinfo.tjpark.feiqi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tjsinfo.tjpark.R;

/**
 * Created by panning on 2018/2/1.
 */

//支付宝支付相关类
@Deprecated
public class OrderPay extends AppCompatActivity {

     private RadioGroup radioGroup=null;
     private RadioButton pay_yl,pay_wx,pay_zfb,pay_ye;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderpay);

        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);

    }



}
