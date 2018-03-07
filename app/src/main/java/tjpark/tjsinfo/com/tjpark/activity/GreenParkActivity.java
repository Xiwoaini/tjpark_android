package tjpark.tjsinfo.com.tjpark;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import tjpark.tjsinfo.com.tjpark.adapter.GreenParkAdapter;
import tjpark.tjsinfo.com.tjpark.entity.GreenPark;
import tjpark.tjsinfo.com.tjpark.util.NetConnection;

/**
 * Created by panning on 2018/1/12.
 */

public class GreenParkActivity extends AppCompatActivity {

    private TextView parkId,greenPark_placeName,greenPark_distance,greenPark_address;
    private ListView listView;
    private List<GreenPark> greenParkList = new ArrayList<GreenPark>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greenpark);
        Button exitBtn=(Button)findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }

        });
        listView = (ListView)findViewById(R.id.listView);
        parkId= (TextView)findViewById(R.id.parkId);
        greenPark_placeName = (TextView)findViewById(R.id.greenPark_placeName);
        greenPark_distance = (TextView)findViewById(R.id.greenPark_distance);
        greenPark_address = (TextView)findViewById(R.id.greenPark_address);

        Intent getIntent = getIntent();
        parkId.setText(getIntent.getStringExtra("parkId"));

        greenPark_placeName.setText(getIntent.getStringExtra("place_name"));
        greenPark_distance.setText(getIntent.getStringExtra("place_distance"));
        greenPark_address.setText(getIntent.getStringExtra("place_address"));
        new Thread(runnable).start();
    }


    //新线程进行网络请求
    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            JsonArray jsonArray = null;
            String parkid = parkId.getText().toString();
            String strUrl="/tjpark/app/AppWebservice/findSharePark?parkid="+parkid;
            jsonArray = NetConnection.getJsonArray(strUrl);

            Iterator it = jsonArray.iterator();

            int i=0;
            while (it.hasNext()) {
                GreenPark greenPark = new GreenPark();
                if(i==jsonArray.size()){
                    break;
                }
                JsonObject jso = jsonArray.get(i).getAsJsonObject();
                greenPark.setId(jso.get("id").toString().replace("\"",""));
                greenPark.setPark_num(jso.get("park_num").toString().replace("\"",""));
                greenPark.setStart_time(jso.get("start_time").toString().replace("\"",""));
                greenPark.setEnd_time(jso.get("end_time").toString().replace("\"",""));
                greenPark.setPark_fee(jso.get("park_fee").toString().replace("\"",""));
                greenPark.setPlace_id(jso.get("place_id").toString().replace("\"",""));
                greenPark.setModel(jso.get("model").toString().replace("\"",""));
                greenPark.setStatus(jso.get("status").toString().replace("\"",""));
                greenPark.setShare_status(jso.get("share_status").toString().replace("\"",""));
                greenPark.setDistance(greenPark_distance.getText().toString());
                greenPark.setAddress(greenPark_address.getText().toString());
                greenPark.setPlace_name(greenPark_placeName.getText().toString());
                greenParkList.add(greenPark);
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
//
            GreenParkAdapter adapter = new GreenParkAdapter(GreenParkActivity.this, R.layout.activity_greenlistview,greenParkList);
            listView.setAdapter(adapter);


        }
    };

}