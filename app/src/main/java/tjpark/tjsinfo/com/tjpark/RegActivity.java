package tjpark.tjsinfo.com.tjpark;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    private  Button regGetCode;





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

    }


    //内部类负责监听按钮
    class BtnClick implements View.OnClickListener{
        @Override
        public void onClick(View view){



        }


    }



}
