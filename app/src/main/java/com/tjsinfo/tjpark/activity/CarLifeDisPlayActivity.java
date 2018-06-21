package com.tjsinfo.tjpark.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.tjsinfo.tjpark.R;

/**
 * Created by panning on 2018/1/16.
 */

//车生活具体展示
public class CarLifeDisPlayActivity  extends AppCompatActivity {

    public static String strUrl = "";

    private WebView webView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carlifedisplay);
        Button exitBtn=(Button)findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }

        });
        Log.v("值为:",strUrl);
        if (strUrl.equals("")){
            strUrl="http://m.weizhang8.cn/";
        }

        webView = (WebView)findViewById(R.id.webView);
        webView.loadUrl(strUrl);
    }




}
