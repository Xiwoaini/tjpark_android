package tjpark.tjsinfo.com.tjpark.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tjpark.tjsinfo.com.tjpark.R;

/**
 * Created by panning on 2018/1/12.
 */

public class PersonActivity extends FragmentActivity {
    private SharedPreferences mSharedPreferences;
    //登录状态
    private TextView textStatus;
    //登录或退出按钮
    private Button btnStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        //获取控件
        textStatus = (TextView)findViewById(R.id.textStatus);
        btnStatus = (Button)findViewById(R.id.btnStatus);
        mSharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);


//       从本地取数据：
        String username=mSharedPreferences.getString("loginName","");
        String password=mSharedPreferences.getString("password","");

        System.out.print("值为:"+username);
        if ((username!="")&&(password!="")){
            textStatus.setText("已登录");
            btnStatus.setText("退出");

        }
        else{
            textStatus.setText("未登录");
            btnStatus.setText("登录");
        }
        StatusBtn sb=new StatusBtn();
        btnStatus.setOnClickListener(sb);



        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview,getData());
        //获取listView，
        ListView  listView = (ListView)findViewById(R.id.personListView);
        //为listView赋值
        listView.setAdapter(adapter);
        ListViewListener ll=new ListViewListener();
        listView.setOnItemClickListener(ll);

    }



    private List<String> getData(){

        List<String> data = new ArrayList<String>();
        data.add("我的车辆");
        data.add("我的共享车位");
        data.add("共享车位发布");
        data.add("我的月卡");
        data.add("我的优惠券");
        data.add("我的积分");
        data.add("我的发票");
        return data;
    }

    class ListViewListener implements   AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //position指点击的行数
            Intent intent = new Intent();
            switch(position)
            {
                //我的车辆
                case 0:
                    //跳转代码，(当前Activity，目标Activity)
                    intent.setClass(PersonActivity.this, MyCarActivity.class);
                    startActivity(intent);
                    break;
                //我的共享车位
                case 1:
                    intent.setClass(PersonActivity.this, MyShareActivity.class);
                    startActivity(intent);
                    break;
                //共享车位发布
                case 2:
                    intent.setClass(PersonActivity.this, ShareReleaseActivity.class);
                    startActivity(intent);
                    break;
                //我的月卡
                case 3:
                    new AlertDialog.Builder(PersonActivity.this)
                            .setTitle("提示")
                            .setMessage("此功能暂未开放!")
                            .setPositiveButton("确定", null)
                            .show();
                    break;
                //我的优惠券
                case 4:
                    new AlertDialog.Builder(PersonActivity.this)
                            .setTitle("提示")
                            .setMessage("此功能暂未开放!")
                            .setPositiveButton("确定", null)
                            .show();
                    break;
                //我的积分
                case 5:
                    new AlertDialog.Builder(PersonActivity.this)
                            .setTitle("提示")
                            .setMessage("此功能暂未开放!")
                            .setPositiveButton("确定", null)
                            .show();
                    break;
                //我的发票s
                case 6:
                    new AlertDialog.Builder(PersonActivity.this)
                            .setTitle("提示")
                            .setMessage("此功能暂未开放!")
                            .setPositiveButton("确定", null)
                            .show();
                    break;
            }
        }
    }


    //监听登录或退出按钮
    class StatusBtn implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            //当是登录按钮
            if (btnStatus.getText().toString().equals("登录")){
                Intent intent = new Intent();
                //(当前Activity，目标Activity)
                intent.setClass(PersonActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            else{
                //退出按钮
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.clear();
                editor.commit();
            }


        }
    }

}
