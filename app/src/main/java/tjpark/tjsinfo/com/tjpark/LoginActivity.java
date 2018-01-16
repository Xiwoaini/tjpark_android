package tjpark.tjsinfo.com.tjpark;


import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.JsonObject;



import tjpark.tjsinfo.com.tjpark.util.NetConnection;

/**
 * Created by panning on 2018/1/12.
 */

public class LoginActivity  extends AppCompatActivity  {

    //绑定控件属性
    private TextView username;
    private  TextView password;
    private Button regBtn;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.editText4);
        password = (EditText)findViewById(R.id.editText2);
        regBtn = (Button)findViewById(R.id.button5);
        loginBtn = (Button)findViewById(R.id.button6);
        LoginBtn lb = new LoginBtn();
        loginBtn.setOnClickListener(lb);



    }




    //内部类，负责监听登录按钮
    class LoginBtn implements View.OnClickListener{
//监听方法
        @Override
        public void onClick(View view) {
            new Thread(runnable).start();  //启动子线程
            //用户名或密码为空
            if(username.getText().toString() == null || username.getText().toString() == "" ||
                    password.getText().toString() == null || password.getText().toString() == ""){

                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("注意")
                        .setMessage("请输入用户名或密码!")
                        .setPositiveButton("确定", null)
                        .show();
            }
            else{

            }

        }

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");


        }
    };

    //新线程进行网络请求
    Runnable runnable = new Runnable(){
        @Override
        public void run() {

         JsonObject res = null;
        res =NetConnection.getXpath("/tjpark/app/AppWebservice/userLogin?password=111111&registrationId=1&nameInput=13920775231");
            System.out.println(res);
            System.out.println(res.get("result"));

            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value","请求结果");
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };




}
