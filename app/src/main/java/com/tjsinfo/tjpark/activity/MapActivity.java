package com.tjsinfo.tjpark.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.tjsinfo.tjpark.R;
import com.tjsinfo.tjpark.entity.Park;
import com.tjsinfo.tjpark.util.TjParkUtils;
import com.tjsinfo.tjpark.util.clusterutil.clustering.Cluster;
import com.tjsinfo.tjpark.util.clusterutil.clustering.ClusterItem;
import com.tjsinfo.tjpark.util.clusterutil.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by panning on 2018/6/13.
 */

public class MapActivity extends Activity implements BaiduMap.OnMapLoadedCallback{
    //前台页面控件的显示
    private MapView mMapView;
    //百度地图对象
    private BaiduMap mBaiduMap;
    //点聚合管理类
    private ClusterManager<MyItem> mClusterManager;
    //地图状态
    MapStatus ms;
    //当前坐标
    public static double latitude = 0.0;
    public static double longitude = 0.0;
    public static MyLocationData oLocData;
    private SharedPreferences mSharedPreferences;
    private String personID;
    //停车场类型，用于筛选不同类型的停车场
    public  static  String parkType = "normal";

    //返回键
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(MapActivity.this, TabBarActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMapView = (MapView) findViewById(R.id.map_view);

        mSharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        personID = mSharedPreferences.getString("personID", "");
        //当前位置
       final LatLng loc = new LatLng(latitude, longitude);
        ms = new MapStatus.Builder().target(loc).zoom(17).build();
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMapLoadedCallback(this);
        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
        mClusterManager = new ClusterManager<>(this, mBaiduMap);
        // 添加Marker点
        addMarkers();
        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);

        //设置当前位置图片
        mBaiduMap.setMyLocationData(MapActivity.oLocData);
        // 设置maker点击时的响应
        mBaiduMap.setOnMarkerClickListener(mClusterManager);
        //标注为多个聚合时监听调用
        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
            @Override
            public boolean onClusterClick(Cluster<MyItem> cluster) {
                LatLng lc = new LatLng(cluster.getPosition().latitude, cluster.getPosition().longitude);

                ms = new MapStatus.Builder().target(lc).zoom(mBaiduMap.getMapStatus().zoom + 2).build();
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
                return false;
            }
        });
        //跳转到搜索
        ImageView search_address = findViewById(R.id.search_address);
        search_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到登录页
                Intent intent = new Intent();
                intent.setClass(MapActivity.this, SugAddressActivity.class);
                startActivity(intent);
            }
        });
        //返回按钮
       Button exitBtn = findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                //(当前Activity，目标Activity)
                intent.setClass(MapActivity.this, TabBarActivity.class);
                startActivity(intent);
            }

        });

        //标注为单个时监听调用
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(final MyItem item) {
                //创建视图窗口
                Dialog dialog = new Dialog(MapActivity.this);
                if (item.park.getLable().contains("共享")) {
                    dialog.setContentView(R.layout.activity_markergreen);
                    Window dialogWindow = dialog.getWindow();
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);

                    WindowManager manager = getWindowManager();
                    DisplayMetrics outMetrics = new DisplayMetrics();
                    manager.getDefaultDisplay().getMetrics(outMetrics);
                    int width = outMetrics.widthPixels;
                    int height = outMetrics.heightPixels;
                    lp.x = 0; // 新位置X坐标
                    lp.width = width; // 宽度
                    lp.height = (int) (height * 0.33);// 高度
                    lp.y = height - ((lp.height) + 120);
                    lp.alpha = 0.9f; // 透明
                    dialogWindow.setAttributes(lp);
                    TextView green_placeName = (TextView) dialog.findViewById(R.id.green_placeName);
                    TextView green_label = (TextView) dialog.findViewById(R.id.green_label);
                    TextView green_placeDistance = (TextView) dialog.findViewById(R.id.green_placeDistance);
                    TextView green_placeAddress = (TextView) dialog.findViewById(R.id.green_placeAddress);
                    TextView green_shareNum = (TextView) dialog.findViewById(R.id.green_shareNum);

                    green_placeName.setText(item.park.getPlace_name());
                    green_label.setText(item.park.getLable());
                    green_placeDistance.setText("距离: " + item.park.getDistance());
                    green_placeAddress.setText("地址: " + item.park.getPlace_address());
                    green_shareNum.setText("共享车位数: " + item.park.getShare_num());
                    //共享停车场导航按钮监听事件
                    Button green_daoHang = (Button) dialog.findViewById(R.id.green_parkDaoHang);
                    green_daoHang.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new BlueParkActivity().DaoHang(view, item.park.getPlace_address());
                        }
                    });
                    Button green_parkDetail = (Button) dialog.findViewById(R.id.green_parkDetail);
                    //共享停车场详情按钮监听事件
                    green_parkDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (null == personID || personID.equals("")) {
                                new AlertDialog.Builder(MapActivity.this)
                                        .setTitle("提示")
                                        .setMessage("请先登录!")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //跳转到登录页
                                                Intent intent = new Intent();
                                                //(当前Activity，目标Activity)
                                                intent.setClass(MapActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .show();
                                return;
                            }
                            Intent intent = new Intent();
                            intent.setClass(MapActivity.this, GreenParkActivity.class);
                            intent.putExtra("park", item.park);
                            startActivity(intent);
                        }
                    });

                }
//充电停车场
                else if (item.park.getLable().contains("充电")) {
                    dialog.setContentView(R.layout.activity_markeryellow);
                    dialog.setTitle("充电停车场");
                    Window dialogWindow = dialog.getWindow();
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
                    WindowManager manager = getWindowManager();
                    DisplayMetrics outMetrics = new DisplayMetrics();
                    manager.getDefaultDisplay().getMetrics(outMetrics);
                    int width = outMetrics.widthPixels;
                    int height = outMetrics.heightPixels;

                    lp.width = width; // 宽度
                    lp.height = (int) (height * 0.38);// 高度
                    lp.alpha = 0.9f; // 透明
                    lp.x = 0; // 新位置X坐标
                    lp.y = height - ((lp.height) + 150); // 新位置Y坐标
                    dialogWindow.setAttributes(lp);

                    TextView yellow_placeName = (TextView) dialog.findViewById(R.id.yellow_placeName);
                    TextView yellow_label = (TextView) dialog.findViewById(R.id.yellow_label);
                    TextView yellow_placeDistance = (TextView) dialog.findViewById(R.id.yellow_placeDistance);
                    TextView yellow_placeAddress = (TextView) dialog.findViewById(R.id.yellow_placeAddress);
                    TextView yellow_placeFee = (TextView) dialog.findViewById(R.id.yellow_placeFee);
                    TextView yellow_placeNum = (TextView) dialog.findViewById(R.id.yellow_placeNum);
                    TextView yellow_CDFY = (TextView) dialog.findViewById(R.id.yellow_CDFY);
                    TextView yellow_kongXian = (TextView) dialog.findViewById(R.id.yellow_kongXian);
                    yellow_placeName.setText(item.park.getPlace_name());
                    yellow_label.setText(item.park.getLable());
                    yellow_placeDistance.setText("距离: " + item.park.getDistance());
                    yellow_placeAddress.setText("地址: " + item.park.getPlace_address());
                    yellow_placeFee.setText("收费标准: 6元/小时");
                    yellow_placeNum.setText("车位情况:" + item.park.getSpace_num());
                    yellow_CDFY.setText("充电费用:" + item.park.getPile_fee());
                    yellow_kongXian.setText("空闲充电桩:" + String.valueOf(Integer.parseInt(item.park.getSlow_pile_space_num()) + Integer.parseInt(item.park.getFast_pile_space_num())));
                    //充电停车场导航按钮监听事件
                    Button yellow_daoHang = (Button) dialog.findViewById(R.id.yellow_parkDaoHang);
                    yellow_daoHang.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new BlueParkActivity().DaoHang(view, item.park.getPlace_address());
                        }
                    });
                    Button yellow_parkDetail = (Button) dialog.findViewById(R.id.yellow_parkDetail);
                    //充电停车场详情按钮监听事件
                    yellow_parkDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (null == personID || personID.equals("")) {
                                new AlertDialog.Builder(MapActivity.this)
                                        .setTitle("提示")
                                        .setMessage("请先登录!")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //跳转到登录页
                                                Intent intent = new Intent();
                                                //(当前Activity，目标Activity)
                                                intent.setClass(MapActivity.this, LoginActivity.class);
                                                startActivity(intent);

                                            }
                                        })
                                        .show();
                                return;
                            }
                            Intent intent = new Intent();
                            intent.setClass(MapActivity.this, YellowParkActivity.class);
                            intent.putExtra("park", item.park);
                            startActivity(intent);
                        }
                    });
                }
//
                else {
                    dialog.setContentView(R.layout.activity_markerblue);
                    Window dialogWindow = dialog.getWindow();
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);

                    WindowManager manager = getWindowManager();
                    DisplayMetrics outMetrics = new DisplayMetrics();
                    manager.getDefaultDisplay().getMetrics(outMetrics);
                    int width = outMetrics.widthPixels;
                    int height = outMetrics.heightPixels;
                    lp.x = 0; // 新位置X坐标
                    lp.width = width; // 宽度
                    lp.height = (int) (height * 0.33);// 高度
                    lp.y = height - ((lp.height) + 120);
                    lp.alpha = 0.9f; // 透明
                    dialogWindow.setAttributes(lp);
                    //获取对应控件

                    TextView blue_placeName = (TextView) dialog.findViewById(R.id.blue_placeName);
                    TextView blue_label = (TextView) dialog.findViewById(R.id.blue_label);
                    TextView blue_placeDistance = (TextView) dialog.findViewById(R.id.blue_placeDistance);
                    TextView blue_placeAddress = (TextView) dialog.findViewById(R.id.blue_placeAddress);
                    TextView blue_placeFee = (TextView) dialog.findViewById(R.id.blue_placeFee);
                    TextView blue_placeNum = (TextView) dialog.findViewById(R.id.blue_placeNum);
                    blue_placeName.setText(item.park.getPlace_name());
                    blue_label.setText(item.park.getLable());
                    blue_placeDistance.setText("距离: " + item.park.getDistance());
                    blue_placeAddress.setText("地址: " + item.park.getPlace_address());
                    blue_placeFee.setText("收费标准: 6元/小时");
                    blue_placeNum.setText("剩余车位: " + item.park.getSpace_num());
                    Button blue_parkDetail = (Button) dialog.findViewById(R.id.blue_parkDetail);
                    //普通停车场导航按钮监听事件
                    Button blue_daoHang = (Button) dialog.findViewById(R.id.blue_parkDaoHang);
                    blue_daoHang.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new BlueParkActivity().DaoHang(view, item.park.getPlace_address());
                        }
                    });
                    //普通停车场详情按钮监听事件
                    blue_parkDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (null == personID || personID.equals("")) {
                                new AlertDialog.Builder(MapActivity.this)
                                        .setTitle("提示")
                                        .setMessage("请先登录!")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //跳转到登录页
                                                Intent intent = new Intent();
                                                //(当前Activity，目标Activity)
                                                intent.setClass(MapActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .show();
                                return;
                            }
                            Intent intent = new Intent();
                            intent.setClass(MapActivity.this, BlueParkActivity.class);
                            intent.putExtra("park", item.park);
                            startActivity(intent);
                        }
                    });
                }
                dialog.show();
                return false;
            }
        });

        //地图图片监听
        ImageView imageView1 = findViewById(R.id.imageView1);
        ImageView imageView2 = findViewById(R.id.imageView2);
        ImageView imageView3 = findViewById(R.id.imageView3);
        imageView1.setOnClickListener(new View.OnClickListener(){
            public  void onClick(View var1){
                //共享和普通
                parkType = "normal";
                addMarkers();
                ms = new MapStatus.Builder().target(loc).zoom(mBaiduMap.getMapStatus().zoom).build();
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));

            }
        });
        imageView2.setOnClickListener(new View.OnClickListener(){
            public  void onClick(View var1){
                //充电停车场
                parkType = "charge";
                addMarkers();
                ms = new MapStatus.Builder().target(loc).zoom(mBaiduMap.getMapStatus().zoom).build();
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
            }
        });
        imageView3.setOnClickListener(new View.OnClickListener(){
            public  void onClick(View var1){
                //线上支付
                parkType = "pay";
                addMarkers();
                ms = new MapStatus.Builder().target(loc).zoom(mBaiduMap.getMapStatus().zoom).build();
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
            }
        });
    }


    //   地图加载,实现接口OnMapLoadedCallback
    @Override
    public void onMapLoaded() {
        ms = new MapStatus.Builder().zoom(17).build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
    }



    /**
     * 每个Marker点，包含Marker点坐标以及图标
     */
    public class MyItem implements ClusterItem {
        private final LatLng mPosition;
        //marker点的停车场属性
        public Park park = new Park();

        public MyItem(LatLng latLng,Park park) {
            mPosition = latLng;
            this.park = park;
        }


        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        //marker的具体坐标，根据停车场的类型选择对应的坐标
        @Override
        public BitmapDescriptor getBitmapDescriptor() {
            if (park.getLable().contains("充电")){
                LayoutInflater inflater = (LayoutInflater) MapActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.activity_markernum, null);
                TextView marker_num = (TextView) view.findViewById(R.id.marker_num);
                Resources resources=MapActivity.this.getBaseContext().getResources();
                Drawable drawable=resources.getDrawable(R.drawable.huangqipao);
                marker_num.setBackgroundDrawable(drawable);
                try{
                    marker_num.setText(String.valueOf(Integer.parseInt(park.getFast_pile_space_num())+Integer.parseInt(park.getSlow_pile_space_num())));
                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromBitmap(getBitmapFromView(view));
                    return bitmap;
                }
                catch (Exception e){

                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromBitmap(getBitmapFromView(view));
                    return bitmap;
                }


            }
            else if (park.getLable().contains("共享")){
                LayoutInflater inflater = (LayoutInflater)MapActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.activity_markernum, null);
                TextView marker_num = (TextView) view.findViewById(R.id.marker_num);
                Resources resources=MapActivity.this.getBaseContext().getResources();
                Drawable drawable=resources.getDrawable(R.drawable.lvqipao);
                marker_num.setBackgroundDrawable(drawable);
                try{
                    marker_num.setText(park.getShare_num());
                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromBitmap(getBitmapFromView(view));
                    return bitmap;
                }
                catch (Exception e){
                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromBitmap(getBitmapFromView(view));
                    return bitmap;
                }

            }
            else{
                LayoutInflater inflater = (LayoutInflater)MapActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.activity_markernum, null);
                TextView marker_num = (TextView) view.findViewById(R.id.marker_num);
                Resources resources=MapActivity.this.getBaseContext().getResources();
                Drawable drawable=resources.getDrawable(R.drawable.lanqipao);
                marker_num.setBackgroundDrawable(drawable);
                try{
                    marker_num.setText(park.getSpace_num());
                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromBitmap(getBitmapFromView(view));
                    return bitmap;
                }
                catch (Exception e){
                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromBitmap(getBitmapFromView(view));
                    return bitmap;
                }

            }
        }
    }

    //自定义marker样式，xml变view对象
    private Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        return view.getDrawingCache();
    }
    /**
     * 向地图添加Marker点
     */
    public void addMarkers() {
        List<MyItem> items = new ArrayList<MyItem>();
        mClusterManager.clearItems();
        Iterator iter = null;

        if (parkType.equals("normal")){
           iter = TjParkUtils.parkMapByNormal.entrySet().iterator();
        }
        else if (parkType.equals("charge")){
           iter = TjParkUtils.parkMapByCharge.entrySet().iterator();
        }
        else if (parkType.equals("pay")){
           iter = TjParkUtils.parkMapByPay.entrySet().iterator();
        }
        else if (parkType.equals("yuyue")){
            iter = TjParkUtils.parkMapByYuYue.entrySet().iterator();
        }
        else if (parkType.equals("share")){
            iter = TjParkUtils.parkMapByShare.entrySet().iterator();
        }
        else{
            iter = TjParkUtils.parkMap.entrySet().iterator();
        }

        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Park park = (Park)entry.getValue();
            LatLng ll = new LatLng(Double.parseDouble(park.getAddpoint_y()), Double.parseDouble(park.getAddpoint_x()));

            LatLngBounds lp = mBaiduMap.getMapStatus().bound;
            items.add(new MyItem(ll,park));
        }
        mClusterManager.addItems(items);


    }










}