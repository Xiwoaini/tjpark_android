package tjpark.tjsinfo.com.tjpark;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import tjpark.tjsinfo.com.tjpark.entity.Park;
import tjpark.tjsinfo.com.tjpark.util.NetConnection;


public class MainActivity extends AppCompatActivity {
    //前台页面控件的显示
    private MapView mMapView = null;
    //创建OverlayOptions的集合
//BaiduMap
    private BaiduMap mBaiduMap;
    //所有标记点的集合
    List<OverlayOptions> options = new ArrayList<OverlayOptions>();
private List<Park> parkList = new LinkedList<Park>();

//初始化页面
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new Thread(runnable).start();

        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        在应用程序创建时初始化 SDK引用的Context 全局变量；
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        mMapView = (MapView) findViewById(R.id.map_view);

        //在地图上批量添加Marker
        createMarker();
        mBaiduMap = mMapView.getMap();
        mBaiduMap.addOverlays(options);
        //设置marker监听
    mBaiduMap.setOnMarkerClickListener(listener);

    }

    //创建mark点
    private void createMarker(){
        //构建Marker图标
        for(int i =0;i<parkList.size();i++){
            LatLng point1 = new LatLng(Double.parseDouble(parkList.get(i).getAddpoint_y()), Double.parseDouble(parkList.get(i).getAddpoint_x()));
            //根据停车场的类别，划分不同的颜色的停车场
            //停车场是否已满
            if  (parkList.get(i).getSpace_num().equals("0")){
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.huiqipao);
                //创建OverlayOptions属性 .icon后面的为图片样子
                OverlayOptions option1 =  new MarkerOptions()
                        .position(point1).icon(bitmap);
                options.add(option1);
            }
            else{
                //蓝色停车场，普通的预约停车
                if (parkList.get(i).getLable().equals("地上,预约")||
                        parkList.get(i).getLable().equals("地上,预约,")||
                        parkList.get(i).getLable().equals("地上")){
                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.lanqipao);
                    //创建OverlayOptions属性 .icon后面的为图片样子
                    OverlayOptions option1 =  new MarkerOptions()
                            .position(point1).icon(bitmap);
                    options.add(option1);
                }
                //黄色停车场，充电
                else if (parkList.get(i).getLable().equals("地上,预约,充电")||
                        parkList.get(i).getLable().equals("地上,充电")){
                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.huangqipao);
                    //创建OverlayOptions属性 .icon后面的为图片样子
                    OverlayOptions option1 =  new MarkerOptions()
                            .position(point1).icon(bitmap);
                    options.add(option1);
                }
                //绿色停车场，共享
                else if (parkList.get(i).getLable().equals("地上,预约,共享")||
                        parkList.get(i).getLable().equals("地上,预约,共享,充电")){
                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.lvqipao);
                    //创建OverlayOptions属性 .icon后面的为图片样子
                    OverlayOptions option1 =  new MarkerOptions()
                            .position(point1).icon(bitmap);
                    options.add(option1);
                }
                //当前用户位置
                else {
                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.huiqipao);
                    //创建OverlayOptions属性 .icon后面的为图片样子
                    OverlayOptions option1 =  new MarkerOptions()
                            .position(point1).icon(bitmap);
                    options.add(option1);
                }
            }



        }
    }

    //marker监听
    BaiduMap.OnMarkerClickListener listener = new BaiduMap.OnMarkerClickListener() {
        /**
         * 地图 Marker 覆盖物点击事件监听函数
         * @param marker 被点击的 marker
         */
        public boolean onMarkerClick(Marker marker){
//            Log.v("1","hahha");


            return true;
        }
    };


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
            Bundle data = msg.getData();
            String val = data.getString("value");
        }
    };



    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        MapView.setMapCustomEnable(false);
        mMapView = null;
    }
}
