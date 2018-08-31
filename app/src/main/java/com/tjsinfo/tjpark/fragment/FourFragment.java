package com.tjsinfo.tjpark.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.tjsinfo.tjpark.activity.LoginActivity;
import com.tjsinfo.tjpark.activity.MapActivity;
import com.tjsinfo.tjpark.activity.MyCarActivity;
import com.tjsinfo.tjpark.activity.MyShareActivity;
import com.tjsinfo.tjpark.R;
import com.tjsinfo.tjpark.activity.ShareReleaseActivity;
import com.tjsinfo.tjpark.activity.TabBarActivity;
import com.tjsinfo.tjpark.entity.Person;
import com.tjsinfo.tjpark.adapter.PersonAdapter;
import com.tjsinfo.tjpark.util.NetConnection;


/**
 *底部第四页
 */
public class FourFragment extends Fragment {
    //取得存入的id值
    private SharedPreferences mSharedPreferences;

    //登录状态
    private TextView textStatus;
    //登录或退出按钮
    private Button btnStatus;
    private String personID;


    public FourFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.activity_person, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //获取控件
        textStatus = (TextView)getActivity().findViewById(R.id.textStatus);
        btnStatus = (Button)getActivity().findViewById(R.id.btnStatus);
        mSharedPreferences = getActivity().getSharedPreferences("userInfo",getActivity().MODE_PRIVATE);
//       从本地取数据：
        String username=mSharedPreferences.getString("personName","");
        personID = mSharedPreferences.getString("personID", "");
        if (!(username.equals(""))||!(personID.equals(""))){
            textStatus.setText("已登录");
            btnStatus.setText(username);

        }
        else{
            textStatus.setText("未登录");
            btnStatus.setText("登录");
        }
        FourFragment.StatusBtn sb=new FourFragment.StatusBtn();
        btnStatus.setOnClickListener(sb);


//获取listView，
        ListView listView = (ListView)getActivity().findViewById(R.id.personListView);
        PersonAdapter adapter = new PersonAdapter(getActivity(), R.layout.activity_listview,getData());
        //为listView赋值
        listView.setAdapter(adapter);
        FourFragment.ListViewListener ll=new FourFragment.ListViewListener();
        listView.setOnItemClickListener(ll);

    }

    private List<Person> getData(){
    String[] strData = new String[]{"我的车辆","我的钱包","长租车位","我的共享车位","我的充电桩","登出账户"};
    int[] imgData = new int[]{R.drawable.wdcl,R.drawable.wdqb,R.drawable.czcw,R.drawable.wdgxcw,R.drawable.fjcdz,R.drawable.xtsz};
        List<Person> data = new ArrayList<Person>();
        for(int i =0;i<6;i++){
            Person p =new Person();
            p.setPersonImg(imgData[i]);
            p.setPersonItem(strData[i]);
            data.add(p);
        }

        return data;
    }

    class ListViewListener implements   AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //未登录的情况
            if (btnStatus.getText().equals("登录")){
                new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage("请先登录!")
                        .setPositiveButton("确定", null)
                        .show();
            }
         else{
                //position指点击的行数
                Intent intent = new Intent();
                switch(position)
                {
                    //我的车辆
                    case 0:
                        intent.setClass(getActivity(), MyCarActivity.class);
                        startActivity(intent);
                        break;
                    //我的钱包
                    case 1:
                        new AlertDialog.Builder(getActivity())
                                .setTitle("")
                                .setMessage("敬请期待!")
                                .setPositiveButton("确定", null)
                                .show();
                        break;
                    //长租车位
                    case 2:
                        intent.setClass(getActivity(), ShareReleaseActivity.class);
                        startActivity(intent);
                        break;
                    //我的共享车位
                    case 3:
                        intent.setClass(getActivity(), MyShareActivity.class);
                        startActivity(intent);
                        break;
                    //我的充电桩
                    case 4:
                        new AlertDialog.Builder(getActivity())
                                .setTitle("")
                                .setMessage("敬请期待!")
                                .setPositiveButton("确定", null)
                                .show();
                        break;
                    //退出
                    case 5:
                        final AlertDialog.Builder normalDialog =
                                new AlertDialog.Builder(getActivity());
                                 normalDialog.setTitle("提示");
                         normalDialog.setMessage("您确定要退出此账户吗?");
                         normalDialog.setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mSharedPreferences.edit().remove("personName");
                                        mSharedPreferences.edit().remove("password");
                                        mSharedPreferences.edit().remove("personID");
                                        mSharedPreferences.edit().clear().commit();
                                        Intent intent = new Intent();
                                        //(当前Activity，目标Activity)
                                        intent.putExtra("currentTab",3);
                                        intent.setClass(getActivity(), TabBarActivity.class);
                                        startActivity(intent);
                                    }
                                });
                        normalDialog.setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        return;
                                    }
                                });
                        // 显示
                        normalDialog.show();
                        break;

                }
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
                intent.setClass(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
            else{
                btnStatus.setEnabled(false);
            }

        }
    }


}
