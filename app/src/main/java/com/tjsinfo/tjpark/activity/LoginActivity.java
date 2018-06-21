package com.tjsinfo.tjpark.activity;



import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.JsonObject;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tjsinfo.tjpark.R;
import com.tjsinfo.tjpark.util.NetConnection;

/**
 * Created by panning on 2018/1/12.
 */

//登录管理类
public class LoginActivity  extends AppCompatActivity {

    //  13952775231
//    40288afd5c43e114015c43f2d85f0000
    //绑定控件属性
    private TextView username;
    private TextView password;
    private Button regBtn;
    private Button loginBtn, exitBtn;

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //获取对应控件
        username = (EditText) findViewById(R.id.editText4);
        password = (EditText) findViewById(R.id.editText2);
        regBtn = (Button) findViewById(R.id.button5);
        loginBtn = (Button) findViewById(R.id.button6);
        exitBtn = (Button) findViewById(R.id.exitBtn);

        //对登录按钮进行监听
        LoginBtn lb = new LoginBtn();
        loginBtn.setOnClickListener(lb);

        //对注册按钮进行监听
        RegBtn rg = new RegBtn();
        regBtn.setOnClickListener(rg);


        //返回按钮监听
        exitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }

        });
    }


    //处理登录结果
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            val = val.replace("\"", "");
            if (val.equals("1")) {
                //还没有被注册
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("提示")
                        .setMessage("此用户还没有被注册!")
                        .setPositiveButton("确定", null)
                        .show();
            } else if (val.equals("2")) {
                //用户或密码错误
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("提示")
                        .setMessage("用户名或密码不正确!")
                        .setPositiveButton("确定", null)
                        .show();
            } else {
                //成功登陆，保存账户，进行跳转
                mSharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
                //存储到本地
                SharedPreferences.Editor edit = mSharedPreferences.edit();
                edit.putString("personName", username.getText().toString());//这是存数据
                edit.putString("password", password.getText().toString());//这是存数据
                edit.putString("personID", val.replace("\"", ""));//这是存数据
                edit.commit();//这是将数据提交
//        mSharedPreferences.getString("loginName","");//这是获取值loginName
//        mSharedPreferences.getString("password","");


                Intent intent = new Intent();
                //(当前Activity，目标Activity)
                intent.setClass(LoginActivity.this, TabBarActivity.class);
                startActivity(intent);

            }

        }
    };


    //内部类，负责监听登录按钮
    class LoginBtn implements View.OnClickListener {
        //监听方法
        @Override
        public void onClick(View view) {

            //用户名或密码为空
//            if (TextUtils.isEmpty(username.getText().toString().trim()) || TextUtils.isEmpty(password.getText().toString().trim())) {
                if (1==2){
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("注意")
                        .setMessage("请输入用户名或验证码!")
                        .setPositiveButton("确定", null)
                        .show();
            }

            else {

                //安卓访问http需要在子线程中进行
if(true){
//                if (username.getText().toString().equals(nameInput) && password.getText().toString().equals(mobileCode)){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
//进行注册
                            JsonObject res1 = null;

                            String strUrl1 = "/tjpark/app/AppWebservice/saveUser?password=111111" + "&nameInput="
                                    + username.getText().toString() + "&registrationId=1";
                            res1 = NetConnection.getXpath(strUrl1);

//进行登录
                            JsonObject res = null;

                            String strUrl = "/tjpark/app/AppWebservice/userLogin?nameInput=" + username.getText().toString() +
                                    "&password=111111&registrationId=1";
                            res = NetConnection.getXpath(strUrl);
                            //全部返回的字符串内容

                            String result = res.get("result").toString();

                            Message msg = new Message();
                            Bundle data = new Bundle();
                            data.putString("value", result);
                            msg.setData(data);
                            handler.sendMessage(msg);
                        }
                    }).start();
                }
                else{
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("注意")
                            .setMessage("验证码输入错误!")
                            .setPositiveButton("确定", null)
                            .show();
                }


            }

        }

    }
    //手机号
    String nameInput = "";
    //一一对应的验证码
    String mobileCode = "";
    //内部类，负责监听获取验证码按钮
    class RegBtn implements View.OnClickListener {
        //监听方法
        @Override
        public void onClick(View view) {
            if (!isMobileNO(username.getText().toString().trim())){
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("注意")
                        .setMessage("请输入正确的手机号!")
                        .setPositiveButton("确定", null)
                        .show();
                return;
            }
            else{
                regBtn.setEnabled(false);
                regBtn.setBackgroundColor(Color.parseColor("#EDEDED"));
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        JsonObject res = null;

                        String strUrl = "/tjpark/app/AppWebservice/getSmsCode?nameInput=" + username.getText().toString();
                        res = NetConnection.getXpath(strUrl);
                        //全部返回的字符串内容
                        nameInput = res.get("mobile").toString().replace("\"", "");
                        mobileCode = res.get("identityCode").toString().replace("\"", "");

                    }
                }).start();

            }
            }

    }
    //判断是否是手机号
    public boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
}
