package tjpark.tjsinfo.com.tjpark;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by panning on 2018/1/12.
 */

public class LoginActivity  extends AppCompatActivity {

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
                //访问接口

//                Map<String, String> parameters = new HashMap<String, String>();
//                parameters.put("nameInput", "13920775231");
//                parameters.put("password", "111111");
//                parameters.put("registrationId", "1");
//                String result =sendGet("http://60.29.41.58:3000/tjpark/app/AppWebservice/userLogin", parameters);
//                System.out.println(result);



            }



        }



    }







}
