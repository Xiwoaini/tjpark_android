package tjpark.tjsinfo.com.tjpark.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;

import tjpark.tjsinfo.com.tjpark.MyCarActivity;
import tjpark.tjsinfo.com.tjpark.R;
import tjpark.tjsinfo.com.tjpark.entity.Car;
import tjpark.tjsinfo.com.tjpark.util.NetConnection;

/**
 * Created by panning on 2018/1/12.
 */

public class AddCarActivity  extends AppCompatActivity {

    private EditText addCar_PlateNum;
    private Button addCar_SaveBtn;
     private Car car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcar);

        Button exitBtn=(Button)findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }

        });

        addCar_PlateNum = (EditText)findViewById(R.id.addCar_PlateNum);
        //获取传过来的数据
          /*获取Intent中的Bundle对象*/
        Bundle bundle = this.getIntent().getExtras();
        /*获取Bundle中的数据，注意类型和key*/

         if ( bundle.getSerializable("car").equals("")){

             addCar_PlateNum.setText("");
         }
         else{
             car = (Car) bundle.get("car");
             addCar_PlateNum = (EditText)findViewById(R.id.addCar_PlateNum);
             addCar_PlateNum.setText(car.getPlace_number());
         }


        addCar_SaveBtn = (Button)findViewById(R.id.addCar_SaveBtn);
        ButtonListener buttonListener =new ButtonListener();
        addCar_SaveBtn.setOnClickListener(buttonListener);

    }

    //处理登录结果
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Intent intent = new Intent();
            intent.setClass(AddCarActivity.this, MyCarActivity.class);
            startActivity(intent);

        }
    };

    //新线程进行网络请求
    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            JsonObject res = null;
            String strUrl = "";
            if (car == null){
                strUrl ="/tjpark/app/AppWebservice/addPlateNumber?" +
                        "customerid=40288afd5c43e114015c43f2d85f0000&plateNumber="+addCar_PlateNum.getText();
            }
            else{
                 strUrl="/tjpark/app/AppWebservice/updatePlateNumber?customerid=" +
                         "40288afd5c43e114015c43f2d85f0000&plateNumber="+addCar_PlateNum.getText()+
                         "&plateid="+car.getId();
            }
            res = NetConnection.getXpath(strUrl);
            //全部返回的字符串内容

            Message msg = new Message();

            handler.sendMessage(msg);
        }
    };



    //保存按钮监听
    class ButtonListener implements  View.OnClickListener{
        //监听方法
        @Override
        public void onClick(View view) {


            if (addCar_PlateNum.getText().length()!=7){
                new AlertDialog.Builder(AddCarActivity.this)
                        .setTitle("注意")
                        .setMessage("车牌号不符合长度!")
                        .setPositiveButton("确定", null)
                        .show();
            }
            else{
                new Thread(runnable).start();


            }

        }

    }


}