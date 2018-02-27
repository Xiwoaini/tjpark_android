package tjpark.tjsinfo.com.tjpark.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import tjpark.tjsinfo.com.tjpark.LoginActivity;
import tjpark.tjsinfo.com.tjpark.MyCarActivity;
import tjpark.tjsinfo.com.tjpark.MyShareActivity;
import tjpark.tjsinfo.com.tjpark.R;
import tjpark.tjsinfo.com.tjpark.ShareReleaseActivity;
import tjpark.tjsinfo.com.tjpark.entity.Person;
import tjpark.tjsinfo.com.tjpark.util.PersonAdapter;


/**
 *tabbar的person页
 */
public class FourFragment extends Fragment {
    private SharedPreferences mSharedPreferences;
    //登录状态
    private TextView textStatus;
    //登录或退出按钮
    private Button btnStatus;


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
        String password=mSharedPreferences.getString("password","");


//        System.out.print("值为:"+username);
        if (!(username.equals(""))&&!(password.equals(""))){
            textStatus.setText("已登录");
            btnStatus.setText("退出");

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
    String[] strData = new String[]{"我的车辆","我的共享车位","共享车位发布","我的月卡","我的优惠券","我的积分","我的发票"};
    int[] imgData = new int[]{R.drawable.mycar,R.drawable.sharecar,R.drawable.releasecar,R.drawable.yueka,R.drawable.youhui,R.drawable.qian,R.drawable.fapiao};
        List<Person> data = new ArrayList<Person>();
        for(int i =0;i<7;i++){
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
                        //跳转代码，(当前Activity，目标Activity)
                        intent.setClass(getActivity(), MyCarActivity.class);
                        startActivity(intent);
                        break;
                    //我的共享车位
                    case 1:
                        intent.setClass(getActivity(), MyShareActivity.class);
                        startActivity(intent);
                        break;
                    //共享车位发布
                    case 2:
                        intent.setClass(getActivity(), ShareReleaseActivity.class);
                        startActivity(intent);
                        break;
                    //我的月卡
                    case 3:
                        new AlertDialog.Builder(getActivity())
                                .setTitle("提示")
                                .setMessage("此功能暂未开放!")
                                .setPositiveButton("确定", null)
                                .show();
                        break;
                    //我的优惠券
                    case 4:
                        new AlertDialog.Builder(getActivity())
                                .setTitle("提示")
                                .setMessage("此功能暂未开放!")
                                .setPositiveButton("确定", null)
                                .show();
                        break;
                    //我的积分
                    case 5:
                        new AlertDialog.Builder(getActivity())
                                .setTitle("提示")
                                .setMessage("此功能暂未开放!")
                                .setPositiveButton("确定", null)
                                .show();
                        break;
                    //我的发票s
                    case 6:
                        new AlertDialog.Builder(getActivity())
                                .setTitle("提示")
                                .setMessage("此功能暂未开放!")
                                .setPositiveButton("确定", null)
                                .show();
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
                //(当前Activity，目标Activity)
                intent.setClass(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
            else{

                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(getActivity());

                normalDialog.setTitle("注意");
                normalDialog.setMessage("您确定要退出此账户吗?");
                normalDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
           //退出按钮
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.clear();
                editor.commit();
                textStatus.setText("未登录");
                btnStatus.setText("登录");
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


            }


        }
    }


}
