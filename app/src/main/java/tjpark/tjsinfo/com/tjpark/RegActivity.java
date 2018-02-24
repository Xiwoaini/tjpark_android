package tjpark.tjsinfo.com.tjpark;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by panning on 2018/1/12.
 */

public class RegActivity  extends AppCompatActivity {

    //手机号
    private EditText regPhone;
    //验证码
    private  EditText regCode;
    //设置密码
    private EditText regPwd;
    //注册按钮
    private Button regBtn;
    //获取验证码
    private  Button regGetCode,exitBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        //获取对应控件
        regPhone = (EditText)findViewById(R.id.regPhone);
        regCode = (EditText)findViewById(R.id.regCode);
        regPwd=(EditText)findViewById(R.id.regPwd);
        regGetCode=(Button)findViewById(R.id.regGetCode);
        regBtn=(Button)findViewById(R.id.regBtn);
        exitBtn=(Button)findViewById(R.id.exitBtn);
        //当不输入手机号时，设置密码框，验证码框等不能输入
        regCode.setEnabled(false);
        regPwd.setEnabled(false);
        regBtn.setClickable(false);


        //返回按钮监听
        exitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }

        });
        //监听事件
        regGetCode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (regPhone.getText().toString().trim().equals("")){
                    //用户或密码错误
                    new AlertDialog.Builder(RegActivity.this)
                            .setTitle("提示")
                            .setMessage("手机号不能为空!")
                            .setPositiveButton("确定", null)
                            .show();
                }
                else if (regPhone.getText().toString().trim().length()!=11){
                    //手机号不符合长度
                    new AlertDialog.Builder(RegActivity.this)
                            .setTitle("提示")
                            .setMessage("手机号格式不正确!")
                            .setPositiveButton("确定", null)
                            .show();
                }
                else{
                //todo:符合注册的时候进进一步注册
                    Log.v("信息","符合长度");
                    regCode.setEnabled(true);
                    regBtn.setClickable(true);
                }

            }

        });
        //注册按钮监听事件
        regBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (regPwd.getText().toString().trim().equals("")){
                    new AlertDialog.Builder(RegActivity.this)
                            .setTitle("提示")
                            .setMessage("密码不能为空")
                            .setPositiveButton("确定", null)
                            .show();
                }
                else{
                    //调用远程注册接口，并进行页面跳转
                }

            }

        });



    }





}
