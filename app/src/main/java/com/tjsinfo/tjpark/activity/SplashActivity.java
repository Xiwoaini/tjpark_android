package com.tjsinfo.tjpark.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tjsinfo.tjpark.util.NetWorkUtils;

import java.util.Iterator;

import com.tjsinfo.tjpark.R;
import com.tjsinfo.tjpark.entity.Park;
import com.tjsinfo.tjpark.util.NetConnection;
import com.tjsinfo.tjpark.util.TjParkUtils;


/**
 * Created by panning on 2018/2/8.
 */

//用于页面初始化
public class SplashActivity extends Activity {

    //定位SDK的核心类
    private LocationClient mLocClient;
    public static boolean locationTrue = true;
    //定位SDK监听函数
    public SplashActivity.MyLocationListenner locListener = new SplashActivity.MyLocationListenner();

//初始化地图管理类
    BMapManager bMapManager = new BMapManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        LocationManager lm;//【位置检测】
        lm = (LocationManager) getSystemService(SplashActivity.LOCATION_SERVICE);
        if (NetWorkUtils.getAPNType(getApplicationContext()) == 0) {
//当前无网络
            final AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(SplashActivity.this);

            normalDialog.setTitle("注意");
            normalDialog.setMessage("当前无网络连接!");
            normalDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //退出程序
                            android.os.Process.killProcess(android.os.Process.myPid());    //获取PID
                            System.exit(0);
                        }
                    });

            // 显示
            normalDialog.show();
            //结束当前的 Activity

        }

        else {
            boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (ok) {
                if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    SplashActivity.locationTrue = false;
                }
                else{
                    SplashActivity.locationTrue = true;
                }

            }
            else {
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(SplashActivity.this);

                normalDialog.setTitle("注意");
                normalDialog.setMessage("系统检测到您未开启设备的位置信息。");
                normalDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(intent, 1315);
                            }
                        });

                // 显示
                normalDialog.show();
            }
           SDKInitializer.initialize(getApplicationContext());
           //加载启动图片
           setContentView(R.layout.splash);
           mLocClient = new LocationClient(this);
           mLocClient.registerLocationListener(locListener);
           LocationClientOption option = new LocationClientOption();
           option.setOpenGps(true);              //打开GPS
           option.setCoorType("bd09ll");        //设置坐标类型
           option.setScanSpan(1000);            //设置发起定位请求的间隔时间为5000ms
           mLocClient.setLocOption(option);     //设置定位参数
           mLocClient.start();
       }



    }

    public   class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //获取经纬度
            MapActivity.latitude = location.getLatitude();
            MapActivity.longitude = location.getLongitude();

            if (MapActivity.latitude != 0.0 && MapActivity.longitude != 0.0) {
                MapActivity.oLocData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        .direction(100)
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .build();
                mLocClient.stop();
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        JsonArray jsonArray = null;
                        String strUrl = "/tjpark/app/AppWebservice/findPark";
                        jsonArray = NetConnection.getJsonArray(strUrl);

                        Iterator it = jsonArray.iterator();
                        //p1当前位置经纬度
                        LatLng p1 = new LatLng(MapActivity.latitude, MapActivity.longitude);
                        int i = 0;
                        while (it.hasNext()) {
                            Park park = new Park();
                            if (i == jsonArray.size()) {
                                break;
                            }
                            jsonArray.get(i).getAsJsonObject();
                            JsonObject jso = jsonArray.get(i).getAsJsonObject();
                            try {
                                if (jso.get("lable").toString().contains("共享")) {

                                    park.setId(jso.get("id").toString().replace("\"", ""));
                                    park.setPlace_name(jso.get("place_name").toString().replace("\"", ""));
                                    park.setPlace_address(jso.get("place_address").toString().replace("\"", ""));
                                    park.setLable(jso.get("lable").toString().replace("\"", ""));
                                    park.setAddpoint_x(jso.get("addpoint_x").toString().replace("\"", ""));
                                    park.setAddpoint_y(jso.get("addpoint_y").toString().replace("\"", ""));
                                    park.setPlace_type(jso.get("place_type").toString().replace("\"", ""));
                                    park.setShare_num(jso.get("share_num").toString().replace("\"", ""));
                                    park.setSpace_num(jso.get("space_num").toString().replace("\"", ""));
//计算距离
                                    LatLng p2 = new LatLng(Double.parseDouble(jso.get("addpoint_y").toString().replace("\"", "")), Double.parseDouble(jso.get("addpoint_x").toString().replace("\"", "")));
                                    park.setDistance(String.valueOf(Math.ceil(DistanceUtil.getDistance(p1, p2)) / 1000)+"KM");
                                    TjParkUtils.parkMapByNormal.put(park.getId(), park);
                                    //TODO:其他的属性，需要在此添加
                                } else if (jso.get("lable").toString().contains("充电")) {
                                    park.setId(jso.get("id").toString().replace("\"", ""));
                                    park.setPlace_name(jso.get("place_name").toString().replace("\"", ""));
                                    park.setPlace_address(jso.get("place_address").toString().replace("\"", ""));
                                    park.setLable(jso.get("lable").toString().replace("\"", ""));
                                    park.setAddpoint_x(jso.get("addpoint_x").toString().replace("\"", ""));
                                    park.setAddpoint_y(jso.get("addpoint_y").toString().replace("\"", ""));
                                    park.setPlace_type(jso.get("place_type").toString().replace("\"", ""));
                                    //总车位数
                                    park.setPlace_total_num(jso.get("place_total_num").toString().replace("\"", ""));
                                    park.setSpace_num(jso.get("space_num").toString().replace("\"", ""));
                                    park.setPile_fee(jso.get("pile_fee").toString().replace("\"", ""));
                                    park.setPile_time(jso.get("pile_time").toString().replace("\"", ""));
                                    park.setFast_pile_space_num(jso.get("fast_pile_space_num").toString().replace("\"", ""));
                                    park.setFast_pile_total_num(jso.get("fast_pile_total_num").toString().replace("\"", ""));
                                    park.setSlow_pile_space_num(jso.get("slow_pile_space_num").toString().replace("\"", ""));
                                    park.setSlow_pile_total_num(jso.get("slow_pile_total_num").toString().replace("\"", ""));
                                    park.setSpace_num(jso.get("space_num").toString().replace("\"", ""));
//计算距离
                                    park.setParkPileId(jso.get("parkPileId").toString().replace("\"", ""));
                                    LatLng p2 = new LatLng(Double.parseDouble(jso.get("addpoint_y").toString().replace("\"", "")), Double.parseDouble(jso.get("addpoint_x").toString().replace("\"", "")));
                                    park.setDistance(String.valueOf(Math.ceil(DistanceUtil.getDistance(p1, p2)) / 1000)+"KM");

                                } else {
                                    park.setId(jso.get("id").toString().replace("\"", ""));
                                    park.setPlace_name(jso.get("place_name").toString().replace("\"", ""));
                                    park.setPlace_address(jso.get("place_address").toString().replace("\"", ""));
                                    park.setLable(jso.get("lable").toString().replace("\"", ""));
                                    park.setAddpoint_x(jso.get("addpoint_x").toString().replace("\"", ""));
                                    park.setAddpoint_y(jso.get("addpoint_y").toString().replace("\"", ""));
                                    park.setPlace_type(jso.get("place_type").toString().replace("\"", ""));
                                    //总车位数
                                    park.setPlace_total_num(jso.get("place_total_num").toString().replace("\"", ""));
                                    park.setSpace_num(jso.get("space_num").toString().replace("\"", ""));

                                    //计算距离
                                    LatLng p2 = new LatLng(Double.parseDouble(jso.get("addpoint_y").toString().replace("\"", "")), Double.parseDouble(jso.get("addpoint_x").toString().replace("\"", "")));
                                    park.setDistance(String.valueOf(Math.ceil(DistanceUtil.getDistance(p1, p2)) / 1000)+"KM");
                                    TjParkUtils.parkMapByNormal.put(park.getId(), park);
                                }
                                if (park.getLable().contains("预约")){
                                    TjParkUtils.parkMapByYuYue.put(park.getId(), park);
                                }
                                if (park.getLable().contains("共享")){
                                    TjParkUtils.parkMapByShare.put(park.getId(), park);
                                }
                               if (park.getLable().contains("充电")){
                                    TjParkUtils.parkMapByCharge.put(park.getId(), park);
                                }
                                if (park.getLable().contains("支付")){
                                    TjParkUtils.parkMapByPay.put(park.getId(), park);
                                }

                                i++;
                                TjParkUtils.parkMap.put(park.getId(), park);

                            } catch (Exception e) {
                                TjParkUtils.parkMap.put(park.getId(), park);
                                i++;
                                continue;
                            }

                        }
                        i = 0;

                        //页面跳转
                        Intent intent = new Intent(SplashActivity.this, TabBarActivity.class);
                        startActivity(intent);
                        //结束当前的 Activity
                        SplashActivity.this.finish();
                    }
                }).start();

            }


        }
    }



}

