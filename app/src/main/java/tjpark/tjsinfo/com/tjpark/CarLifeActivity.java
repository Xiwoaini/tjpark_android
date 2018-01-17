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
//    private Button carLifeBtn2;
//    private Button carLifeBtn3;
//    private Button carLifeBtn4;
//    private Button carLifeBtn5;
//    private Button carLifeBtn6;
//    private Button carLifeBtn7;
//    private Button carLifeBtn8;
//    private Button carLifeBtn9;
//    private Button carLifeBtn10;
//    private Button carLifeBtn11;
//    private Button carLifeBtn12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carlife);
        carLifeBtn1 = (Button) findViewById(R.id.button1);
//
//
//
//        //对登录按钮进行监听
        CarLifeBtn clb = new CarLifeBtn();
        carLifeBtn1.setOnClickListener(clb);
    }

    //内部类，负责监听12个按钮
    class CarLifeBtn implements View.OnClickListener{
    //添加监听按钮
        @Override
        public void onClick(View view) {
            if (carLifeBtn1.getText().toString().trim().equals("注册")){
                Intent intent = new Intent();

          //(当前Activity，目标Activity)
                intent.setClass(CarLifeActivity.this, LoginActivity.class);
                startActivity(intent);

            }
            else if (carLifeBtn1.getText().toString().trim().equals("注册")) {

            }
            else if (carLifeBtn1.getText().toString().trim().equals("注册")) {

            }
            else if (carLifeBtn1.getText().toString().trim().equals("注册")) {

            }
            else if (carLifeBtn1.getText().toString().trim().equals("注册")) {

            }
            else if (carLifeBtn1.getText().toString().trim().equals("注册")) {

            }
            else if (carLifeBtn1.getText().toString().trim().equals("注册")) {

            }
            else if (carLifeBtn1.getText().toString().trim().equals("注册")) {

            }
            else if (carLifeBtn1.getText().toString().trim().equals("注册")) {

            }
            else if (carLifeBtn1.getText().toString().trim().equals("注册")) {

            }
            else if (carLifeBtn1.getText().toString().trim().equals("注册")) {

            }
            else{
                Intent intent = new Intent();
                //(当前Activity，目标Activity)
                intent.setClass(CarLifeActivity.this, LoginActivity.class);
                startActivity(intent);
            }




        }
    }


}