package tjpark.tjsinfo.com.tjpark.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ZoomControls;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import tjpark.tjsinfo.com.tjpark.BlueParkActivity;
import tjpark.tjsinfo.com.tjpark.CarLifeDisPlayActivity;
import tjpark.tjsinfo.com.tjpark.GreenParkActivity;
import tjpark.tjsinfo.com.tjpark.ParkListActivity;
import tjpark.tjsinfo.com.tjpark.R;
import tjpark.tjsinfo.com.tjpark.YellowParkActivity;
import tjpark.tjsinfo.com.tjpark.entity.Park;
import tjpark.tjsinfo.com.tjpark.util.NetConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class OneFragment extends Fragment  {
    //前台页面控件的显示
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private String parkId="";
    //所有标记点的集合
    List<OverlayOptions> options = new ArrayList<OverlayOptions>();
    private List<Park> parkList = new LinkedList<Park>();

    private  String place_name ;

    private  String place_address ;
    private  String place_distance;
    private  String place_num;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new Thread(runnable).start();
        mMapView = (MapView)getActivity().findViewById(R.id.map_view);


        Button getParkList=(Button)getActivity().findViewById(R.id.getParkList);
        getParkList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ParkListActivity.class);
         startActivity(intent);
            }

        });


    }



    //创建mark点
    private void createMarker(){
        //构建Marker图标
        for(int i =0;i<parkList.size();i++){
            LatLng point1 = new LatLng(Double.parseDouble(parkList.get(i).getAddpoint_y()), Double.parseDouble(parkList.get(i).getAddpoint_x()));
            //根据停车场的类别，划分不同的颜色的停车场
            //停车场是否已满
            if  (parkList.get(i).getSpace_num().equals("0")){
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.activity_markernum, null);
                TextView marker_num = (TextView) view.findViewById(R.id.marker_num);
                Resources resources=getActivity().getBaseContext().getResources();
                Drawable drawable=resources.getDrawable(R.drawable.huiqipao);
                marker_num.setBackgroundDrawable(drawable);
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromBitmap(getBitmapFromView(view));

                Bundle bundle= new Bundle();
                bundle.putString("label",parkList.get(i).getLable());
                //创建OverlayOptions属性 .icon后面的为图片样子
                OverlayOptions option1 =  new MarkerOptions()
                        .position(point1).icon(bitmap).title(parkList.get(i).getPlace_name()).extraInfo(bundle);

                options.add(option1);
            }
            else{
                //蓝色停车场，普通的预约停车
                if (parkList.get(i).getLable().equals("地上,预约")||
                        parkList.get(i).getLable().equals("地上,预约,")||
                        parkList.get(i).getLable().equals("地上")){
                    //图片模式
//                    BitmapDescriptor bitmap = BitmapDescriptorFactory
//                            .fromResource(R.drawable.lanqipao);

                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.activity_markernum, null);
                    TextView marker_num = (TextView) view.findViewById(R.id.marker_num);
                    marker_num.setText(parkList.get(i).getSpace_num());
                   BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromBitmap(getBitmapFromView(view));


                    Bundle bundle= new Bundle();
                    bundle.putString("label",parkList.get(i).getLable());
                    bundle.putString("parkAddress",parkList.get(i).getPlace_address());
                    bundle.putString("placeFee","6元/小时");
                    bundle.putString("parkId",parkList.get(i).getId());
                    bundle.putString("parkDistance","0.00KM");
                    bundle.putString("parkNum",parkList.get(i).getSpace_num()+"/"+parkList.get(i).getPlace_total_num());

                    //创建OverlayOptions属性 .icon后面的为图片样子
                    OverlayOptions option1 =  new MarkerOptions()
                            .position(point1).icon(bitmap).title(parkList.get(i).getPlace_name()).extraInfo(bundle);

                    options.add(option1);


                }
                //黄色停车场，充电
                else if (parkList.get(i).getLable().contains("充电")){
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.activity_markernum, null);
                    TextView marker_num = (TextView) view.findViewById(R.id.marker_num);
                    Resources resources=getActivity().getBaseContext().getResources();
                    Drawable drawable=resources.getDrawable(R.drawable.huangqipao);
                    marker_num.setBackgroundDrawable(drawable);
                     if (parkList.get(i).getFast_pile_space_num().equals("")||parkList.get(i).getSlow_pile_space_num().equals("")){
                         marker_num.setText("0");
                     }
                     else{
                         marker_num.setText(String.valueOf(Integer.parseInt(parkList.get(i).getFast_pile_space_num())+Integer.parseInt(parkList.get(i).getSlow_pile_space_num())));
                     }

                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromBitmap(getBitmapFromView(view));


                    Bundle bundle= new Bundle();
                    bundle.putString("parkId",parkList.get(i).getId());
                    bundle.putString("yellow_placeAddress",parkList.get(i).getPlace_address());
                    bundle.putString("label",parkList.get(i).getLable());
                    bundle.putString("placeFee","6元/小时");
                    bundle.putString("yellow_placeDistance","0.00KM");
                    bundle.putString("parkNum",parkList.get(i).getSpace_num()+"/"+parkList.get(i).getPlace_total_num());
                    bundle.putString("parkFee",parkList.get(i).getPile_fee());
                    bundle.putString("kongXian",marker_num.getText().toString());

                    //创建OverlayOptions属性 .icon后面的为图片样子
                    OverlayOptions option1 =  new MarkerOptions()
                            .position(point1).icon(bitmap).title(parkList.get(i).getPlace_name()).extraInfo(bundle);
                    options.add(option1);
                }
                //绿色停车场，共享
                else if (parkList.get(i).getLable().contains("共享")){
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.activity_markernum, null);
                    TextView marker_num = (TextView) view.findViewById(R.id.marker_num);
                    Resources resources=getActivity().getBaseContext().getResources();
                    BitmapDescriptor bitmap;
                    if (parkList.get(i).getShare_num().equals("0")){
                        Drawable drawable=resources.getDrawable(R.drawable.huiqipao);
                        marker_num.setBackgroundDrawable(drawable);
                         bitmap = BitmapDescriptorFactory
                                .fromBitmap(getBitmapFromView(view));
                    }
                    else{
                        Drawable drawable=resources.getDrawable(R.drawable.lvqipao);
                        marker_num.setBackgroundDrawable(drawable);
                        bitmap  = BitmapDescriptorFactory
                                .fromResource(R.drawable.huiqipao);
                    }
                    marker_num.setText(parkList.get(i).getSpace_num());
                    Bundle bundle= new Bundle();
                    bundle.putString("parkId",parkList.get(i).getId());
                    bundle.putString("label",parkList.get(i).getLable());
                    bundle.putString("green_placeDistance","0.00Km");
                    bundle.putString("green_placeAddress",parkList.get(i).getPlace_address());
                    bundle.putString("green_shareNum",parkList.get(i).getShare_num());

                    //创建OverlayOptions属性 .icon后面的为图片样子
                    OverlayOptions option1 =  new MarkerOptions()
                            .position(point1).icon(bitmap).title(parkList.get(i).getPlace_name())
                            .extraInfo(bundle);
                    options.add(option1);
                }
                //当前用户位置
                else {
                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.huiqipao);

                }
            }
        }

    }



    //marker监听(点击某一marker的时候),当点击停车场的时候会触发此事件
    BaiduMap.OnMarkerClickListener listener = new BaiduMap.OnMarkerClickListener() {
        /**
         * 地图 Marker 覆盖物点击事件监听函数
         * @param marker 被点击的 marker
         */
        public boolean onMarkerClick(Marker marker){
            Dialog dialog = new Dialog(getActivity());

            //共享停车场
           if (marker.getExtraInfo().getString("label").contains("共享")){
               dialog.setContentView(R.layout.activity_markergreen);
               TextView green_placeName=(TextView)dialog.findViewById(R.id.green_placeName);
               TextView green_label=(TextView)dialog.findViewById(R.id.green_label);
               TextView green_placeDistance=(TextView)dialog.findViewById(R.id.green_placeDistance);
               TextView green_placeAddress=(TextView)dialog.findViewById(R.id.green_placeAddress);
               TextView green_shareNum=(TextView)dialog.findViewById(R.id.green_shareNum);

               green_placeName.setText(marker.getTitle());
               green_label.setText(marker.getExtraInfo().getString("label"));
               green_placeDistance.setText(marker.getExtraInfo().getString("green_placeDistance"));
               green_placeAddress.setText(marker.getExtraInfo().getString("green_placeAddress"));
               green_shareNum.setText(marker.getExtraInfo().getString("green_shareNum"));
               Button green_parkDetail=(Button)dialog.findViewById(R.id.green_parkDetail);

               parkId=marker.getExtraInfo().getString("parkId");
                       place_name=green_placeName.getText().toString();
               place_distance=green_placeDistance.getText().toString();
                       place_address=green_placeAddress.getText().toString();
               //共享停车场详情按钮监听事件
               green_parkDetail.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent = new Intent();
                       intent.setClass(getActivity(), GreenParkActivity.class);
                       intent.putExtra("parkId",parkId);
                       intent.putExtra("place_name",place_name);
                       intent.putExtra("place_distance",place_distance);
                       intent.putExtra("place_address",place_address);


                       startActivity(intent);


                   }
               });

           }
           //充电停车场
           else if (marker.getExtraInfo().getString("label").contains("充电")){
               dialog.setContentView(R.layout.activity_markeryellow);
               TextView yellow_placeName=(TextView)dialog.findViewById(R.id.yellow_placeName);
               TextView yellow_label=(TextView)dialog.findViewById(R.id.yellow_label);
               TextView yellow_placeDistance=(TextView)dialog.findViewById(R.id.yellow_placeDistance);
               TextView yellow_placeAddress=(TextView)dialog.findViewById(R.id.yellow_placeAddress);
               TextView yellow_placeFee=(TextView)dialog.findViewById(R.id.yellow_placeFee);
               TextView yellow_placeNum=(TextView)dialog.findViewById(R.id.yellow_placeNum);
               TextView yellow_CDFY=(TextView)dialog.findViewById(R.id.yellow_CDFY);
               TextView yellow_kongXian=(TextView)dialog.findViewById(R.id.yellow_kongXian);
               yellow_placeName.setText(marker.getTitle());
               yellow_label.setText(marker.getExtraInfo().getString("label"));
               yellow_placeDistance.setText(marker.getExtraInfo().getString("yellow_placeDistance"));
               yellow_placeAddress.setText(marker.getExtraInfo().getString("yellow_placeAddress"));
               yellow_placeFee.setText("收费标准:"+marker.getExtraInfo().getString("placeFee"));
               yellow_placeNum.setText("车位情况:"+marker.getExtraInfo().getString("parkNum"));
               yellow_CDFY.setText("充电费用:"+marker.getExtraInfo().getString("parkFee"));
               yellow_kongXian.setText("空闲充电桩:"+marker.getExtraInfo().getString("kongXian"));
               Button yellow_parkDetail=(Button)dialog.findViewById(R.id.yellow_parkDetail);

               parkId=marker.getExtraInfo().getString("parkId");
               place_name =yellow_placeName.getText().toString();
                       place_distance=yellow_placeDistance.getText().toString();
               place_address=yellow_placeAddress.getText().toString();
//TODO          右边图片圈上的停车位   place_num

               //充电停车场详情按钮监听事件
               yellow_parkDetail.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent = new Intent();
                       intent.setClass(getActivity(), YellowParkActivity.class);
                       intent.putExtra("parkId",parkId);
                       intent.putExtra("place_name",place_name);
                       intent.putExtra("place_distance",place_distance);
                       intent.putExtra("place_address",place_address);
                       intent.putExtra("place_num",place_num);

                       startActivity(intent);
                   }
                   });

           }
           //普通停车场
           else{
               dialog.setContentView(R.layout.activity_markerblue);
               //获取对应控件

               TextView blue_placeName=(TextView)dialog.findViewById(R.id.blue_placeName);
               TextView blue_label=(TextView)dialog.findViewById(R.id.blue_label);
               TextView blue_placeDistance=(TextView)dialog.findViewById(R.id.blue_placeDistance);
               TextView blue_placeAddress=(TextView)dialog.findViewById(R.id.blue_placeAddress);
               TextView blue_placeFee=(TextView)dialog.findViewById(R.id.blue_placeFee);
               TextView blue_placeNum=(TextView)dialog.findViewById(R.id.blue_placeNum);
               blue_placeName.setText(marker.getTitle());
               blue_label.setText(marker.getExtraInfo().getString("label"));
               blue_placeDistance.setText(marker.getExtraInfo().getString("parkDistance"));
               blue_placeAddress.setText(marker.getExtraInfo().getString("parkAddress"));
               blue_placeFee.setText(marker.getExtraInfo().getString("placeFee"));
               blue_placeNum.setText(marker.getExtraInfo().getString("parkNum"));
               parkId=marker.getExtraInfo().getString("parkId");
               place_name= blue_placeName.getText().toString();
               place_address=blue_placeAddress.getText().toString();
               place_distance=blue_placeDistance.getText().toString();
               place_num=blue_placeNum.getText().toString();
               Button blue_parkDetail=(Button)dialog.findViewById(R.id.blue_parkDetail);
               //普通停车场详情按钮监听事件
               blue_parkDetail.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent = new Intent();
                       intent.setClass(getActivity(), BlueParkActivity.class);
                       intent.putExtra("parkId",parkId);
                       intent.putExtra("place_name",place_name);
                       intent.putExtra("place_distance",place_distance);
                       intent.putExtra("place_address",place_address);
                       intent.putExtra("place_num",place_num);

                       startActivity(intent);
                   }
               });

           }
            dialog.show();

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
                 jsonArray.get(i).getAsJsonObject();
                JsonObject jso = jsonArray.get(i).getAsJsonObject();
                try{
                    if (jso.get("lable").toString().contains("共享")){
                        park.setId(jso.get("id").toString().replace("\"",""));
                        park.setPlace_name(jso.get("place_name").toString().replace("\"",""));
                        park.setPlace_address(jso.get("place_address").toString().replace("\"",""));
                        park.setLable(jso.get("lable").toString().replace("\"",""));
                        park.setAddpoint_x(jso.get("addpoint_x").toString().replace("\"",""));
                        park.setAddpoint_y(jso.get("addpoint_y").toString().replace("\"",""));
                        park.setPlace_type(jso.get("place_type").toString().replace("\"",""));
                        park.setShare_num(jso.get("share_num").toString().replace("\"",""));
                        park.setSpace_num(jso.get("space_num").toString().replace("\"",""));
                        //TODO:其他的属性，需要在此添加
                    }
                   else  if (jso.get("lable").toString().contains("充电")){
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
                        park.setPile_fee(jso.get("pile_fee").toString().replace("\"",""));

                        park.setFast_pile_space_num(jso.get("fast_pile_space_num").toString().replace("\"",""));
                        park.setSlow_pile_space_num(jso.get("slow_pile_space_num").toString().replace("\"",""));
                        park.setSpace_num(jso.get("space_num").toString().replace("\"",""));
                    }
                    else{
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
                        if (jso.get("place_name").toString().replace("\"","").equals("中环大厦")){
//                            Log.v("地址",jso.get("place_total_num").toString().replace("\"","") );

                        }

                    }
                    i++;
                    parkList.add(park);

                }
                catch (Exception e){
                    parkList.add(park);
                    i++;
                    continue;
                }

            }
            i=0;

            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value",jsonArray.toString());
            msg.setData(data);
            handler.sendMessage(msg);
            //在地图上批量添加Marker
            createMarker();
            mBaiduMap = mMapView.getMap();
            // 隐藏logo
            View child = mMapView.getChildAt(1);
            if (child != null && (child instanceof ImageView || child instanceof ZoomControls)){
                child.setVisibility(View.INVISIBLE);
            }


            mBaiduMap.addOverlays(options);
            //设置marker监听
            mBaiduMap.setOnMarkerClickListener(listener);
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




    //自定义marker样式，xml变view对象
    private Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }
//    EditText eSearch;
//    ImageView ivDeleteText;
//    ListView mListView;
//    ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
//
//    ArrayList<String> mListTitle = new ArrayList<String>();
//    ArrayList<String> mListText = new ArrayList<String>();
//
//    SimpleAdapter adapter;
//
//    Handler myhandler = new Handler();
//
//
//    private void set_mListView_adapter()
//    {
//        mListView = (ListView)getActivity().findViewById(R.id.mListView);
//
//        getmData(mData);
//
//        adapter = new SimpleAdapter(this,mData,android.R.layout.simple_list_item_2,
//                new String[]{"title","text"},new int[]{android.R.id.text1,android.R.id.text2});
//
//
//        mListView.setAdapter(adapter);
//    }
//
//    private void set_eSearch_TextChanged()
//    {
//        eSearch = (EditText) getActivity().findViewById(R.id.etSearch);
//
//        eSearch.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
//                                          int arg3) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // TODO Auto-generated method stub
//
//                if(s.length() == 0){
//                    ivDeleteText.setVisibility(View.GONE);
//                }
//                else {
//                    ivDeleteText.setVisibility(View.VISIBLE);
//                }
//
//                myhandler.post(eChanged);
//            }
//        });
//
//    }
//
//
//
//    Runnable eChanged = new Runnable() {
//
//        @Override
//        public void run() {
//            // TODO Auto-generated method stub
//            String data = eSearch.getText().toString();
//
//            mData.clear();
//
//            getmDataSub(mData, data);
//
//            adapter.notifyDataSetChanged();
//
//        }
//    };
//
//
//    private void getmDataSub(ArrayList<Map<String, Object>> mDataSubs, String data)
//    {
//        int length = mListTitle.size();
//        for(int i = 0; i < length; ++i){
//            if(mListTitle.get(i).contains(data) || mListText.get(i).contains(data)){
//                Map<String,Object> item = new HashMap<String,Object>();
//                item.put("title", mListTitle.get(i));
//                item.put("text",  mListText.get(i));
//                mDataSubs.add(item);
//            }
//        }
//    }
//
//
//
//    private void set_ivDeleteText_OnClick()
//    {
//        ivDeleteText = (ImageView)getActivity().findViewById(R.id.ivDeleteText);
//        ivDeleteText.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//                eSearch.setText("");
//            }
//        });
//    }
//
//
//
//    private void getmData(ArrayList<Map<String, Object>> mDatas)
//    {
//        Map<String, Object> item = new HashMap<String, Object>();
//        mListTitle.add("����һ������!");
//        mListText.add("�����ı�.\n2014.09.18.19.50");
//
//        item.put("title", mListTitle.get(0));
//        item.put("text", mListText.get(0));
//        mDatas.add(item);
//
//
//        mListTitle.add("������һ������!");
//        mListText.add("������һ���ı�.\n2014.09.18.19.51");
//
//        item = new HashMap<String, Object>();
//        item.put("title", mListTitle.get(1));
//        item.put("text", mListText.get(1));
//        mDatas.add(item);
//    }







//    @Override
//    protected void onResume() {
//        super.onResume();
//        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
//        mMapView.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
//        mMapView.onPause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mMapView = null;
//        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
//        mMapView.onDestroy();
//        MapView.setMapCustomEnable(false);
//    }




}
