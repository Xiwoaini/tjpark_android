package tjpark.tjsinfo.com.tjpark.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import tjpark.tjsinfo.com.tjpark.R;


/**
 * Created by panning on 2018/2/8.
 */

//用于页面初始化
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载启动图片
        setContentView(R.layout.splash);
        //后台处理耗时任务
        new Thread(new Runnable() {
            @Override
            public void run() {
                //耗时任务，比如加载网络数据
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //跳转至 MainActivity
                        Intent intent = new Intent(SplashActivity.this, TabBarActivity.class);
                        startActivity(intent);
                        //结束当前的 Activity
                        SplashActivity.this.finish();
                    }
                });
            }
        }).start();





    }









}

