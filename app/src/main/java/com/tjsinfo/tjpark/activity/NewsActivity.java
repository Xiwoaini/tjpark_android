package com.tjsinfo.tjpark.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tjsinfo.tjpark.R;
import com.tjsinfo.tjpark.entity.News;
import com.tjsinfo.tjpark.entity.Park;
import com.tjsinfo.tjpark.util.NetConnection;

import java.util.Iterator;

/**
 * Created by panning on 2018/7/6.
 */

public class NewsActivity extends Activity {
    private TextView XWBT,FBSJ,XWJTNR;
    private News news;
    private Integer currentPage;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        imageView = findViewById(R.id.imageView);
        XWBT = findViewById(R.id.XWBT);
        FBSJ = findViewById(R.id.FBSJ);
        XWJTNR = findViewById(R.id.XWJTNR);
        Bundle bundle = this.getIntent().getExtras();
        currentPage  = Integer.parseInt(bundle.get("currentPage").toString());
        //返回按钮
       Button exitBtn = (Button)findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                JsonArray res = null;
                String strUrl = "/tjpark/app/AppWebservice/getNews";
                res= NetConnection.getJsonArray(strUrl);
                if (res == null){
                    return;
                }
                Iterator it = res.iterator();
                JsonObject jso = res.get(currentPage).getAsJsonObject();
                news  = new News();
                try{
                    news.setId(jso.get("id").toString().replace("\"", ""));
                    news.setTitle(jso.get("n_title").toString().replace("\"", ""));
                    news.setContail(jso.get("n_contail").toString().replace("\"", ""));
                    news.setReleasetime(jso.get("n_releasetime").toString().replace("\"", ""));
                    news.setUpdatetime(jso.get("n_updatetime").toString().replace("\"", ""));
                    news.setImgs(jso.get("n_img").toString().replace("\"", ""));
                    news.setName(jso.get("n_name").toString().replace("\"", ""));
                }
                catch(Exception e){
                    Message msg = new Message();
                    handler.sendMessage(msg);
                }


                Message msg = new Message();
                handler.sendMessage(msg);

            }
        }).start();
    }



    //处理结果
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            XWBT.setText(news.getTitle());
            FBSJ.setText("天津停车   "+news.getReleasetime());
            XWJTNR.setText(news.getContail());
            if (null == news.getImgs()||news.getImgs().equals("")){
                imageView.setBackgroundResource(R.drawable.bg1);
            }


        }
    };

}
