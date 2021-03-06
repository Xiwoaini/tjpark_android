package com.tjsinfo.tjpark.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.google.gson.JsonObject;
import com.tjsinfo.tjpark.activity.LoginActivity;
import com.tjsinfo.tjpark.activity.ParkListActivity;
import com.tjsinfo.tjpark.activity.YellowParkActivity;
import com.tjsinfo.tjpark.util.clusterutil.clustering.ClusterItem;
import com.tjsinfo.tjpark.util.clusterutil.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.tjsinfo.tjpark.R;
import com.tjsinfo.tjpark.activity.BlueParkActivity;
import com.tjsinfo.tjpark.activity.GreenParkActivity;
import com.tjsinfo.tjpark.entity.Park;
import com.tjsinfo.tjpark.util.NetConnection;
import com.tjsinfo.tjpark.util.TjParkUtils;
import com.tjsinfo.tjpark.util.clusterutil.clustering.Cluster;


/**
 *底部第一页
 */
public class OneFragment extends Fragment implements BaiduMap.OnMapLoadedCallback {
    //前台页面控件的显示
    private MapView mMapView;
    //百度地图对象
    private BaiduMap mBaiduMap;
    //图片控件
    private ImageView imageView;
    //点聚合管理类
    private ClusterManager<MyItem> mClusterManager;
    //地图状态
    MapStatus ms;
    //当前坐标
    public static double latitude = 0.0;
    public static double longitude = 0.0;
    //地址模糊搜索
    private SearchView sugSearch;
    private List<String> sugAddress  = new ArrayList<String>();
    private ArrayAdapter arrayAdapter;
    private ListView searchListView;

    public static MyLocationData oLocData;
    private SharedPreferences mSharedPreferences;
    private String personID;
 //初始化视图
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //初始化控件
        super.onActivityCreated(savedInstanceState);
        imageView = getActivity().findViewById(R.id.imageView);
        mMapView = (MapView) getActivity().findViewById(R.id.map_view);
        mSharedPreferences = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);

        personID = mSharedPreferences.getString("personID", "");
        //当前位置
        LatLng loc = new LatLng(latitude,longitude);
        ms = new MapStatus.Builder().target(loc).zoom(17).build();
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMapLoadedCallback(this);
        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
        mClusterManager = new ClusterManager<>(getActivity(), mBaiduMap);
        // 添加Marker点
        addMarkers();
        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
        //设置当前位置图片
        mBaiduMap.setMyLocationData(OneFragment.oLocData);

        // 设置maker点击时的响应
        mBaiduMap.setOnMarkerClickListener(mClusterManager);
        //标注为多个聚合时监听调用
        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
            @Override
            public boolean onClusterClick(Cluster<MyItem> cluster) {
                LatLng lc = new LatLng(cluster.getPosition().latitude,cluster.getPosition().longitude);

                ms = new MapStatus.Builder().target(lc).zoom(mBaiduMap.getMapStatus().zoom+2).build();
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
                return false;
            }
        });
        //标注为单个时监听调用
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(final MyItem item) {
                //创建视图窗口
                Dialog dialog = new Dialog(getActivity());
                if (item.park.getLable().contains("共享")){
                    Window dialogWindow = dialog.getWindow();
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);

                    WindowManager manager = getActivity().getWindowManager();
                    DisplayMetrics outMetrics = new DisplayMetrics();
                    manager.getDefaultDisplay().getMetrics(outMetrics);
                    int width = outMetrics.widthPixels;
                    int height = outMetrics.heightPixels;
                    lp.width = width; // 宽度
                    lp.height = (int)(height*0.33);// 高度
                    lp.x = 0; // 新位置X坐标
                    lp.y = height-((lp.height)+150);
                    lp.alpha = 0.9f; // 透明
                    dialogWindow.setAttributes(lp);
                    dialog.setContentView(R.layout.activity_markergreen);
                    TextView green_placeName=(TextView)dialog.findViewById(R.id.green_placeName);

                    TextView green_placeAddress=(TextView)dialog.findViewById(R.id.green_placeAddress);
                    TextView green_shareNum=(TextView)dialog.findViewById(R.id.green_shareNum);

                    green_placeName.setText(item.park.getPlace_name());

                    green_placeAddress.setText("地址: "+item.park.getPlace_address());
                    green_shareNum.setText("共享车位数: "+item.park.getShare_num());
                    //共享停车场导航按钮监听事件
                    Button green_daoHang =  (Button)dialog.findViewById(R.id.green_parkDaoHang);
                    green_daoHang.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                            new BlueParkActivity().DaoHang(view,item.park.getPlace_address());
                         }
                    });
                    Button green_parkDetail=(Button)dialog.findViewById(R.id.green_parkDetail);
                    //共享停车场详情按钮监听事件
                    green_parkDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (null == personID || personID.equals("")) {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("提示")
                                        .setMessage("请先登录!")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //跳转到登录页
                                                Intent intent = new Intent();
                                                //(当前Activity，目标Activity)
                                                intent.setClass(getActivity(), LoginActivity.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .show();
                                return ;
                            }
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), GreenParkActivity.class);
                            intent.putExtra("park",item.park);
                            startActivity(intent);
                        }
                    });

                }
//充电停车场
                else if (item.park.getLable().contains("充电")){
                    dialog.setContentView(R.layout.activity_markeryellow);
                    dialog.setTitle("充电停车场");
                    Window dialogWindow = dialog.getWindow();
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
                    WindowManager manager = getActivity().getWindowManager();
                    DisplayMetrics outMetrics = new DisplayMetrics();
                    manager.getDefaultDisplay().getMetrics(outMetrics);
                    int width = outMetrics.widthPixels;
                    int height = outMetrics.heightPixels;

                    lp.width = width; // 宽度
                    lp.height = (int)(height*0.38);// 高度
                    lp.alpha = 0.9f; // 透明
                    lp.x = 0; // 新位置X坐标
                    lp.y = height-((lp.height)+150); // 新位置Y坐标
                    dialogWindow.setAttributes(lp);

                    TextView yellow_placeName=(TextView)dialog.findViewById(R.id.yellow_placeName);
                    TextView yellow_placeAddress=(TextView)dialog.findViewById(R.id.yellow_placeAddress);

                    yellow_placeName.setText(item.park.getPlace_name());

                    yellow_placeAddress.setText("地址: "+item.park.getPlace_address());

                    //充电停车场导航按钮监听事件
                    Button yellow_daoHang =  (Button)dialog.findViewById(R.id.yellow_parkDaoHang);
                    yellow_daoHang.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new BlueParkActivity().DaoHang(view,item.park.getPlace_address());
                        }
                    });
                    Button yellow_parkDetail=(Button)dialog.findViewById(R.id.yellow_parkDetail);
                    //充电停车场详情按钮监听事件
                    yellow_parkDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (null == personID || personID.equals("")) {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("提示")
                                        .setMessage("请先登录!")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //跳转到登录页
                                                Intent intent = new Intent();
                                                //(当前Activity，目标Activity)
                                                intent.setClass(getActivity(), LoginActivity.class);
                                                startActivity(intent);

                                            }
                                        })
                                        .show();
                                return ;
                            }
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), YellowParkActivity.class);
                            intent.putExtra("park",item.park);
                            startActivity(intent);
                        }
                    });
                }
//
                else{
                    dialog.setContentView(R.layout.activity_markerblue);
                    Window dialogWindow = dialog.getWindow();
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);

                    WindowManager manager = getActivity().getWindowManager();
                    DisplayMetrics outMetrics = new DisplayMetrics();
                    manager.getDefaultDisplay().getMetrics(outMetrics);
                    int width = outMetrics.widthPixels;
                    int height = outMetrics.heightPixels;
                    lp.x = 0; // 新位置X坐标
                    lp.width = width; // 宽度
                    lp.height = (int)(height*0.33);// 高度
                    lp.y = height-((lp.height)+150);
                    lp.alpha = 0.9f; // 透明
                    dialogWindow.setAttributes(lp);
                    //获取对应控件

                    TextView blue_placeName=(TextView)dialog.findViewById(R.id.blue_placeName);


                    TextView blue_placeAddress=(TextView)dialog.findViewById(R.id.blue_placeAddress);

                    TextView blue_placeNum=(TextView)dialog.findViewById(R.id.blue_placeNum);
                    blue_placeName.setText(item.park.getPlace_name());


                    blue_placeAddress.setText("地址: "+item.park.getPlace_address());

                    blue_placeNum.setText("剩余车位: "+item.park.getSpace_num());
                    Button blue_parkDetail=(Button)dialog.findViewById(R.id.blue_parkDetail);
                    //普通停车场导航按钮监听事件
                    Button blue_daoHang =  (Button)dialog.findViewById(R.id.blue_parkDaoHang);
                    blue_daoHang.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new BlueParkActivity().DaoHang(view,item.park.getPlace_address());
                        }
                    });
                    //普通停车场详情按钮监听事件
                    blue_parkDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (null == personID || personID.equals("")) {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("提示")
                                        .setMessage("请先登录!")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //跳转到登录页
                                                Intent intent = new Intent();
                                                //(当前Activity，目标Activity)
                                                intent.setClass(getActivity(), LoginActivity.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .show();
                                return ;
                            }
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), BlueParkActivity.class);
                            intent.putExtra("park",item.park);
                            startActivity(intent);
                        }
                    });
                }
                dialog.show();
                return false;
            }
        });

        //地址模糊搜索
        sugSearch = getActivity().findViewById(R.id.sugSearch);
        searchListView=(ListView)getActivity().findViewById(R.id.searchListView);
        //为搜索地址框设置监听
        sugSearch.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                searchListView.setVisibility(View.VISIBLE);
                mMapView.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.INVISIBLE);

                arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, sugAddress);
                searchListView.setAdapter(arrayAdapter);
                searchListView.setTextFilterEnabled(true);
            }
        });
        //        //添加监听
        OneFragment.ListViewListener ll=new OneFragment.ListViewListener();
        searchListView.setOnItemClickListener(ll);
        sugSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //点击搜索的时候
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals("")){
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    // 隐藏软键盘
                    imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
                    searchListView.setVisibility(View.INVISIBLE);
                    mMapView.setVisibility(View.VISIBLE);
                    return false;
                }
                searchListView.setVisibility(View.VISIBLE);
                mMapView.setVisibility(View.INVISIBLE);
                //SUG搜索
                SuggestionSearch mSuggestionSearch = SuggestionSearch.newInstance();

                mSuggestionSearch.setOnGetSuggestionResultListener(SUGListener);
                // 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                        .keyword(s)//指定建议关键字 必填
                        .city("天津"));//请求城市 必填

                return false;
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
        public   Park park = new Park();

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
                     LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                     View view = inflater.inflate(R.layout.activity_markernum, null);
                     TextView marker_num = (TextView) view.findViewById(R.id.marker_num);
                     Resources resources=getActivity().getBaseContext().getResources();
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
                     LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                     View view = inflater.inflate(R.layout.activity_markernum, null);
                     TextView marker_num = (TextView) view.findViewById(R.id.marker_num);
                     Resources resources=getActivity().getBaseContext().getResources();
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
                     LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                     View view = inflater.inflate(R.layout.activity_markernum, null);
                     TextView marker_num = (TextView) view.findViewById(R.id.marker_num);
                     Resources resources=getActivity().getBaseContext().getResources();
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
        Iterator iter = TjParkUtils.parkMap.entrySet().iterator();
            while (iter.hasNext()) {
               Map.Entry entry = (Map.Entry) iter.next();
               Park park = (Park)entry.getValue();
                LatLng ll = new LatLng(Double.parseDouble(park.getAddpoint_y()), Double.parseDouble(park.getAddpoint_x()));

                LatLngBounds lp = mBaiduMap.getMapStatus().bound;
                   items.add(new MyItem(ll,park));
 }
        mClusterManager.addItems(items);


    }





    //sug监听
    OnGetSuggestionResultListener SUGListener = new OnGetSuggestionResultListener() {
        public void onGetSuggestionResult(SuggestionResult res) {
            if (res == null || res.getAllSuggestions() == null) {

                return;
                //未找到相关结果
            }
            sugAddress.clear();
            List<SuggestionResult.SuggestionInfo> l=res.getAllSuggestions();
            for (SuggestionResult.SuggestionInfo sugArredss: l
                    ) {

                String city =sugArredss.city+sugArredss.district+sugArredss.key;
                sugAddress.add(city);
            }
            arrayAdapter.notifyDataSetChanged();

        }
    };

    //listView监听item点击
    class ListViewListener implements   AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mMapView.setVisibility(View.VISIBLE);
            searchListView.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
            final String address= sugAddress.get(position);
//
            new Thread( new Runnable() {
                @Override
                public void run() {
                    JsonObject res = null;
                    String strUrl = "http://api.map.baidu.com/geocoder/v2/?address="+address+"&output=json&ak=EXj18khot93RCrLj6yizGXo69iCEP5FC&mcode=08:58:81:B0:2A:74:77:E1:75:5F:D4:D2:42:A2:7A:B8:3E:06:8A:2B;com.tjsinfo.tjpark";
                    res = NetConnection.getAddressStatus(strUrl);
//嵌套取坐标
                    JsonObject data=res.getAsJsonObject("result");

                    Object data1= data.getAsJsonObject("location");

                    JsonObject data2=(JsonObject)data1;

                    latitude = Double.valueOf(((JsonObject) data1).get("lat").toString());
                    longitude =Double.valueOf(((JsonObject) data1).get("lng").toString());
                    LatLng loc = new LatLng(latitude,longitude);
                    MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(loc);
//缩放级别
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(15).build()));
                    //设置当前位置图片

                    mBaiduMap.setMyLocationData(OneFragment.oLocData);
                    //更新地图状态
                    mBaiduMap.animateMapStatus(msu);


                }
            }).start();
            sugSearch.setQuery(address,true);
            //清除焦点
            sugSearch.clearFocus();
        }

    }


}
