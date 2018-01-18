package tjpark.tjsinfo.com.tjpark;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by panning on 2018/1/12.
 */

//车生活的12个按钮
public class CarLifeActivity  extends AppCompatActivity {

    //绑定控件属性
    private Button carLifeBtn1;
    private Button carLifeBtn2;
    private Button carLifeBtn3;
    private Button carLifeBtn4;
    private Button carLifeBtn5;
    private Button carLifeBtn6;
    private Button carLifeBtn7;
    private Button carLifeBtn8;
    private Button carLifeBtn9;
    private Button carLifeBtn10;
    private Button carLifeBtn11;
    private Button carLifeBtn12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carlife);
        carLifeBtn1 = (Button) findViewById(R.id.button1);
        carLifeBtn2 = (Button) findViewById(R.id.button2);
        carLifeBtn3 = (Button) findViewById(R.id.button3);
        carLifeBtn4 = (Button) findViewById(R.id.button4);
        carLifeBtn5 = (Button) findViewById(R.id.button5);
        carLifeBtn6 = (Button) findViewById(R.id.button6);
        carLifeBtn7 = (Button) findViewById(R.id.button7);
        carLifeBtn8 = (Button) findViewById(R.id.button8);
        carLifeBtn9 = (Button) findViewById(R.id.button9);
        carLifeBtn10 = (Button) findViewById(R.id.button10);
        carLifeBtn11 = (Button) findViewById(R.id.button11);
        carLifeBtn12 = (Button) findViewById(R.id.button12);

//        //对12个按钮进行监听
        CarLifeBtn clb = new CarLifeBtn();
        carLifeBtn1.setOnClickListener(clb);
        carLifeBtn2.setOnClickListener(clb);
        carLifeBtn3.setOnClickListener(clb);
        carLifeBtn4.setOnClickListener(clb);
        carLifeBtn5.setOnClickListener(clb);
        carLifeBtn6.setOnClickListener(clb);
        carLifeBtn7.setOnClickListener(clb);
        carLifeBtn8.setOnClickListener(clb);
        carLifeBtn9.setOnClickListener(clb);
        carLifeBtn10.setOnClickListener(clb);
        carLifeBtn11.setOnClickListener(clb);
        carLifeBtn12.setOnClickListener(clb);

    }

    //内部类，负责监听12个按钮
    class CarLifeBtn implements View.OnClickListener{
    //添加监听按钮
        @Override
        public void onClick(View view) {

            System.out.print(view);

            //违章查询   http://m.weizhang8.cn/
            if (carLifeBtn1.getText().toString().trim().equals("注册")){
                Intent intent = new Intent();

          //(当前Activity，目标Activity)
                intent.setClass(CarLifeActivity.this, CarLifeDisPlayActivity.class);
                startActivity(intent);

            }
            //驾驶证年检 http://vip.6838520.com/m-xcrj2/
            else if (carLifeBtn1.getText().toString().trim().equals("注册")) {

            }
//            机动车年检
            else if (carLifeBtn1.getText().toString().trim().equals("注册")) {

            }
//            缴纳车险 http://u.pingan.com/upingan/insure/bdwx/bdwx.html?area=c03-bdwap-07&mediasource=C03-BDWAP-1-BQ-30723
            else if (carLifeBtn1.getText().toString().trim().equals("注册")) {

            }
//            洗车 http://vip.6838520.com/m-xcrj2/
            else if (carLifeBtn1.getText().toString().trim().equals("注册")) {

            }
//            安全代驾 http://daijia.xiaojukeji.com/
            else if (carLifeBtn1.getText().toString().trim().equals("注册")) {

            }
//            共享单车
            else if (carLifeBtn1.getText().toString().trim().equals("注册")) {

            }
//            二手车 http://m.xin.com/tianjin/?channel=a16b46c1064d40488e159740f4&abtest=5_B
            else if (carLifeBtn1.getText().toString().trim().equals("注册")) {

            }
//            修车
            else if (carLifeBtn1.getText().toString().trim().equals("注册")) {

            }
//            4S保养 http://m.yixiuche.com
            else if (carLifeBtn1.getText().toString().trim().equals("注册")) {

            }
//            道路救援 https://www.ipaosos.com/wshop/index.php#ca_source=Baidu
            else if (carLifeBtn1.getText().toString().trim().equals("注册")) {

            }
//            汽车金融 http://m.carbank.cn/sales/20170601_a/?invitecode=hz160940&channel=%e7%99%be%e5%ba%a6SEM%e7%a7%bb%e5%8a%a8g&launchArea=%e5%85%a8%e5%9c%b0%e5%9f%9f&promotionMethod=%e5%8f%8c%e5%90%91%e8%af%8d_%e6%b5%8b%e8%af%95&unit=%e6%b1%bd%e8%bd%a6%e9%87%91%e8%9e%8d_%e5%b9%b3%e5%8f%b0&keyword=%e6%b1%bd%e8%bd%a6%e9%87%91%e8%9e%8d%e5%85%ac%e5%8f%b8&teltype=4000656082
            else{
                Intent intent = new Intent();
                //(当前Activity，目标Activity)
                intent.setClass(CarLifeActivity.this, LoginActivity.class);
                startActivity(intent);
            }




        }
    }


}