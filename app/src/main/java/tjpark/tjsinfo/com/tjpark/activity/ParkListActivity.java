package tjpark.tjsinfo.com.tjpark.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import tjpark.tjsinfo.com.tjpark.R;
import tjpark.tjsinfo.com.tjpark.activity.BlueParkActivity;
import tjpark.tjsinfo.com.tjpark.entity.Park;
import tjpark.tjsinfo.com.tjpark.util.NetConnection;
import tjpark.tjsinfo.com.tjpark.adapter.ParkAdapter;

/**
 * Created by panning on 2018/1/12.
 */
//停车场列表控制类，p1为当前用户位置，p2为停车场位置
public class ParkListActivity extends AppCompatActivity {
    private List<Park> parkList = new LinkedList<Park>();
private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new Thread(runnable).start();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parklist);

        Button exitBtn=(Button)findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }

        });

//        //获取listView，
         listView = (ListView)findViewById(R.id.parkListView);


    }


    //新线程进行网络请求
    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            JsonArray jsonArray = null;

            String strUrl="/tjpark/app/AppWebservice/findPark";
            jsonArray = NetConnection.getJsonArray(strUrl);

            Iterator it = jsonArray.iterator();

            int i=0;
            while (it.hasNext()) {
                Park park = new Park();
                if(i==jsonArray.size()){
                    break;
                }
                JsonObject jso = jsonArray.get(i).getAsJsonObject();
                park.setId(jso.get("id").toString().replace("\"",""));
                park.setPlace_name(jso.get("place_name").toString().replace("\"",""));
                park.setPlace_address(jso.get("place_address").toString().replace("\"",""));
                park.setLable(jso.get("lable").toString().replace("\"",""));
                park.setAddpoint_x(jso.get("addpoint_x").toString().replace("\"",""));
                park.setAddpoint_y(jso.get("addpoint_y").toString().replace("\"",""));
                park.setPlace_type(jso.get("place_type").toString().replace("\"",""));
                //总车位数
                park.setPlace_total_num(jso.get("place_total_num").toString().replace("\"",""));
                park.setSpace_num(jso.get("space_num").toString().replace("\"",""));
                //TODO:其他的属性，需要在此添加

                parkList.add(park);
                i++;

            }
            i=0;

            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value",jsonArray.toString());
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //处理parkList里的各个停车场
            //计算距离,
            LatLng p1 = new LatLng(39.114033,117.265205);
            for (Park park:parkList
                 ) {
                LatLng p2 = new LatLng(Double.parseDouble(park.getAddpoint_y()),Double.parseDouble(park.getAddpoint_x()));
                park.setDistance("5.0KM");

//                String s= String.valueOf(DistanceUtil. getDistance(p1, p2));
            }
            ParkAdapter adapter = new ParkAdapter(ParkListActivity.this, R.layout.activity_parklistview,parkList);
            listView.setAdapter(adapter);
            //添加监听
            ParkListActivity.ListViewListener listViewListener = new ParkListActivity.ListViewListener();
            listView.setOnItemClickListener(listViewListener);

    }
    };



    //内部类，负责监听listview点击某行事件,position指点击的行数,从第0行开始
    class ListViewListener implements   AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //根据停车场类型跳转到对应的视图
            if (parkList.get(position).getLable().contains("充电")){
                Intent intent = new Intent();
                intent.setClass(ParkListActivity.this, YellowParkActivity.class);
                intent.putExtra("parkId",parkList.get(position).getId());
                intent.putExtra("place_name",parkList.get(position).getPlace_name());
                intent.putExtra("place_address",parkList.get(position).getPlace_address());
                intent.putExtra("place_distance",parkList.get(position).getDistance());

                startActivity(intent);
            }
            else if (parkList.get(position).getLable().contains("共享")){
                Intent intent = new Intent();
                intent.setClass(ParkListActivity.this, GreenParkActivity.class);
                intent.putExtra("parkId",parkList.get(position).getId());
                intent.putExtra("place_name",parkList.get(position).getPlace_name());
                intent.putExtra("place_address",parkList.get(position).getPlace_address());
                intent.putExtra("place_distance",parkList.get(position).getDistance());

                startActivity(intent);
            }
            else{
                //            跳转
                Intent intent = new Intent();
                intent.setClass(ParkListActivity.this, BlueParkActivity.class);
                intent.putExtra("parkId",parkList.get(position).getId());
                intent.putExtra("place_name",parkList.get(position).getPlace_name());
                intent.putExtra("place_address",parkList.get(position).getPlace_address());
                intent.putExtra("place_distance",parkList.get(position).getDistance());

                startActivity(intent);
            }




        }
    }






}