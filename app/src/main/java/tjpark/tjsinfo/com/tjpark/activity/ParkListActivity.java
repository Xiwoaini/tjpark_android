package tjpark.tjsinfo.com.tjpark.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import tjpark.tjsinfo.com.tjpark.fragment.OneFragment;
import tjpark.tjsinfo.com.tjpark.util.NetConnection;
import tjpark.tjsinfo.com.tjpark.adapter.ParkAdapter;
import tjpark.tjsinfo.com.tjpark.util.TjParkUtils;

/**
 * Created by panning on 2018/1/12.
 */
//停车场列表控制类，p1为当前用户位置，p2为停车场位置
public class ParkListActivity extends AppCompatActivity {
    private static List<Park> parkList = new LinkedList<Park>();
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

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
         //赋值parkList
        getParkList();
        ParkAdapter adapter = new ParkAdapter(ParkListActivity.this, R.layout.activity_parklistview,parkList);
        listView.setAdapter(adapter);
        //添加监听
        ParkListActivity.ListViewListener listViewListener = new ParkListActivity.ListViewListener();
        listView.setOnItemClickListener(listViewListener);

    }
    /*
     * 赋值parkList
     * */
    public void  getParkList(){
        if (parkList.size()!=0){
            return;
        }
        for (String key : TjParkUtils.parkMap.keySet()) {
            parkList.add(TjParkUtils.parkMap.get(key));
        }

    }

    //内部类，负责监听listview点击某行事件,position指点击的行数,从第0行开始
    class ListViewListener implements   AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //根据停车场类型跳转到对应的视图
            if (parkList.get(position).getLable().contains("充电")){
                Intent intent = new Intent();
                intent.setClass(ParkListActivity.this, YellowParkActivity.class);
                intent.putExtra("park",parkList.get(position));
                startActivity(intent);
            }
            else if (parkList.get(position).getLable().contains("共享")){
                Intent intent = new Intent();
                intent.setClass(ParkListActivity.this, GreenParkActivity.class);
                intent.putExtra("park",parkList.get(position));
                startActivity(intent);
            }
            else{
                Intent intent = new Intent();
                intent.setClass(ParkListActivity.this, BlueParkActivity.class);
                intent.putExtra("park",parkList.get(position));
                startActivity(intent);
            }




        }
    }






}