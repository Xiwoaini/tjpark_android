package tjpark.tjsinfo.com.tjpark.feiqi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import tjpark.tjsinfo.com.tjpark.activity.CarLifeDisPlayActivity;
import tjpark.tjsinfo.com.tjpark.R;

/**
 * Created by panning on 2018/1/12.
 */
@Deprecated
//车生活的12个按钮
public class CarLifeActivity  extends FragmentActivity {

    //绑定控件属性
    private ImageButton carLifeBtn1;
    private ImageButton carLifeBtn2;
    private ImageButton carLifeBtn3;
    private ImageButton carLifeBtn4;
    private ImageButton carLifeBtn5;
    private ImageButton carLifeBtn6;
    private ImageButton carLifeBtn7;
    private ImageButton carLifeBtn8;
    private ImageButton carLifeBtn9;
    private ImageButton carLifeBtn10;
    private ImageButton carLifeBtn11;
    private ImageButton carLifeBtn12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carlife);
        carLifeBtn1 = (ImageButton) findViewById(R.id.imgBtn1);
        carLifeBtn2 = (ImageButton) findViewById(R.id.imgBtn2);
        carLifeBtn3 = (ImageButton) findViewById(R.id.imgBtn3);
        carLifeBtn4 = (ImageButton) findViewById(R.id.imgBtn4);
        carLifeBtn5 = (ImageButton) findViewById(R.id.imgBtn5);
        carLifeBtn6 = (ImageButton) findViewById(R.id.imgBtn6);
        carLifeBtn7 = (ImageButton) findViewById(R.id.imgBtn7);
        carLifeBtn8 = (ImageButton) findViewById(R.id.imgBtn8);
        carLifeBtn9 = (ImageButton) findViewById(R.id.imgBtn9);
        carLifeBtn10 = (ImageButton) findViewById(R.id.imgBtn10);
        carLifeBtn11 = (ImageButton) findViewById(R.id.imgBtn11);
        carLifeBtn12 = (ImageButton) findViewById(R.id.imgBtn12);


//        //对12个按钮进行监听
                carLifeBtn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.v("被点击","按钮1");
                CarLifeDisPlayActivity.strUrl = "http://m.weizhang8.cn/";
                Intent intent = new Intent();

                //(当前Activity，目标Activity)
                intent.setClass(CarLifeActivity.this, CarLifeDisPlayActivity.class);
                startActivity(intent);
            }

        });
        carLifeBtn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CarLifeDisPlayActivity.strUrl = "http://vip.6838520.com/m-xcrj2/";
                Intent intent = new Intent();

                //(当前Activity，目标Activity)
                intent.setClass(CarLifeActivity.this, CarLifeDisPlayActivity.class);
                startActivity(intent);
            }

        });
        carLifeBtn3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CarLifeDisPlayActivity.strUrl = "http://vip.6838520.com/m-xcrj2/";
                Intent intent = new Intent();

                //(当前Activity，目标Activity)
                intent.setClass(CarLifeActivity.this, CarLifeDisPlayActivity.class);
                startActivity(intent);
            }

        });
        carLifeBtn4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CarLifeDisPlayActivity.strUrl = "http://u.pingan.com/upingan/insure/bdwx/bdwx.html?area=c03-bdwap-07&mediasource=C03-BDWAP-1-BQ-30723";
                Intent intent = new Intent();

                //(当前Activity，目标Activity)
                intent.setClass(CarLifeActivity.this, CarLifeDisPlayActivity.class);
                startActivity(intent);
            }

        });
        carLifeBtn5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CarLifeDisPlayActivity.strUrl = "http://vip.6838520.com/m-xcrj2/";
                Intent intent = new Intent();

                //(当前Activity，目标Activity)
                intent.setClass(CarLifeActivity.this, CarLifeDisPlayActivity.class);
                startActivity(intent);
            }

        });
        carLifeBtn6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CarLifeDisPlayActivity.strUrl = "http://daijia.xiaojukeji.com/";
                Intent intent = new Intent();

                //(当前Activity，目标Activity)
                intent.setClass(CarLifeActivity.this, CarLifeDisPlayActivity.class);
                startActivity(intent);
            }

        });
        carLifeBtn7.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CarLifeDisPlayActivity.strUrl = "http://vip.6838520.com/m-xcrj2/";
                Intent intent = new Intent();

                //(当前Activity，目标Activity)
                intent.setClass(CarLifeActivity.this, CarLifeDisPlayActivity.class);
                startActivity(intent);
            }

        });
        carLifeBtn8.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CarLifeDisPlayActivity.strUrl = "http://m.xin.com/tianjin/?channel=a16b46c1064d40488e159740f4&abtest=5_B";
                Intent intent = new Intent();

                //(当前Activity，目标Activity)
                intent.setClass(CarLifeActivity.this, CarLifeDisPlayActivity.class);
                startActivity(intent);
            }

        });
        carLifeBtn9.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CarLifeDisPlayActivity.strUrl = "http://vip.6838520.com/m-xcrj2/";
                Intent intent = new Intent();

                //(当前Activity，目标Activity)
                intent.setClass(CarLifeActivity.this, CarLifeDisPlayActivity.class);
                startActivity(intent);
            }

        });
        carLifeBtn10.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CarLifeDisPlayActivity.strUrl = "http://m.yixiuche.com";
                Intent intent = new Intent();

                //(当前Activity，目标Activity)
                intent.setClass(CarLifeActivity.this, CarLifeDisPlayActivity.class);
                startActivity(intent);
            }

        });
        carLifeBtn11.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CarLifeDisPlayActivity.strUrl = "https://www.ipaosos.com/wshop/index.php#ca_source=Baidu";
                Intent intent = new Intent();

                //(当前Activity，目标Activity)
                intent.setClass(CarLifeActivity.this, CarLifeDisPlayActivity.class);
                startActivity(intent);
            }

        });
        carLifeBtn12.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CarLifeDisPlayActivity.strUrl = "http://m.carbank.cn/sales/20170601_a/?invitecode=hz160940&channel=%e7%99%be%e5%ba%a6SEM%e7%a7%bb%e5%8a%a8g&launchArea=%e5%85%a8%e5%9c%b0%e5%9f%9f&promotionMethod=%e5%8f%8c%e5%90%91%e8%af%8d_%e6%b5%8b%e8%af%95&unit=%e6%b1%bd%e8%bd%a6%e9%87%91%e8%9e%8d_%e5%b9%b3%e5%8f%b0&keyword=%e6%b1%bd%e8%bd%a6%e9%87%91%e8%9e%8d%e5%85%ac%e5%8f%b8&teltype=4000656082";

                Intent intent = new Intent();

                //(当前Activity，目标Activity)
                intent.setClass(CarLifeActivity.this, CarLifeDisPlayActivity.class);
                startActivity(intent);
            }

        });

    }
}





