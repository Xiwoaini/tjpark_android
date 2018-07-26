package com.tjsinfo.tjpark.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonObject;
import com.tjsinfo.tjpark.R;
import com.tjsinfo.tjpark.fragment.OneFragment;
import com.tjsinfo.tjpark.util.NetConnection;

/**
 * Created by panning on 2018/1/12.
 */

public class SugAddressActivity extends AppCompatActivity {
    private List<String> sugAddress  = new ArrayList<String>();
    private SearchView mSearchView;
    private ListView mListView;
   private ArrayAdapter arrayAdapter;
   private Button exitBtn;
   private TextView deleteExit;

    private SharedPreferences mSharedPreferences;
    //历史查询
    private String exitAddress ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sug);
        mSearchView = (SearchView) findViewById(R.id.sugSearch);

        mListView = (ListView) findViewById(R.id.searchListView);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sugAddress);
        mListView.setAdapter(arrayAdapter);
        mListView.setTextFilterEnabled(true);


        //添加监听
        SugAddressActivity.ListViewListener ll=new SugAddressActivity.ListViewListener();
        mListView.setOnItemClickListener(ll);


        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //点击搜索的时候
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                mListView.setVisibility(View.VISIBLE);

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

        //返回按钮
        exitBtn = (Button)findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                mSuggestionSearch.destroy();
                onBackPressed();
            }

        });
        deleteExit = findViewById(R.id.deleteExit);

        //尝试读取历史记录
        try{

            mSharedPreferences = getSharedPreferences("exitAddress", MODE_PRIVATE);
            Map<String,?> map = mSharedPreferences.getAll();
            Set<String> exitSet  = map.keySet();
            Iterator it = exitSet.iterator();
            while(it.hasNext()) {
                Object key = it.next();
                sugAddress.add(map.get(key).toString());
            }
            if (sugAddress.size() == 0) {
                deleteExit.setEnabled(false);
                deleteExit.setText("暂无历史搜索");
            }
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sugAddress);
            arrayAdapter.notifyDataSetChanged();
        }
        catch (Exception e){

        }
//删除历史记录

        deleteExit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(SugAddressActivity.this)
                        .setTitle("提示")
                        .setMessage("确定删除历史记录吗?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor edit = mSharedPreferences.edit();
                                edit.clear();
                                edit.commit();
                                mListView.setVisibility(View.INVISIBLE);
                            }
                        }).setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .show();

            }
        });
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
            deleteExit.setVisibility(View.INVISIBLE);
            arrayAdapter.notifyDataSetChanged();

        }
    };


//listView监听item点击(点击后跳转到地图进行搜索)
    class ListViewListener implements   AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final String address= sugAddress.get(position);
            exitAddress = address;

            mSharedPreferences = getSharedPreferences("exitAddress", MODE_PRIVATE);
            //存储到本地
            SharedPreferences.Editor edit = mSharedPreferences.edit();
            edit.putString(String.valueOf(System.currentTimeMillis()).toString().substring(0,10),exitAddress);

            edit.commit();//这是将数据提交
            new Thread( new Runnable() {
                @Override
                public void run() {
                    JsonObject res = null;
                    String strUrl = "http://api.map.baidu.com/geocoder/v2/?address="+address+"&output=json&ak=EXj18khot93RCrLj6yizGXo69iCEP5FC&mcode=08:58:81:B0:2A:74:77:E1:75:5F:D4:D2:42:A2:7A:B8:3E:06:8A:2B;com.tjsinfo.tjpark";
                    res = NetConnection.getAddressStatus(strUrl);
                    try{
//嵌套取坐标
                    JsonObject data=res.getAsJsonObject("result");

                    Object data1= data.getAsJsonObject("location");

                    JsonObject data2=(JsonObject)data1;

                    MapActivity.latitude = Double.valueOf(((JsonObject) data1).get("lat").toString());
                    MapActivity.longitude =Double.valueOf(((JsonObject) data1).get("lng").toString());
                    LatLng loc = new LatLng(MapActivity.latitude,MapActivity.longitude);
                        MapActivity.oLocData = new MyLocationData.Builder()
                                .direction(100)
                                .latitude(loc.latitude)
                                .longitude(loc.longitude)
                                .build();
                    MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(loc);
                    //跳转
                    Intent intent = new Intent();
                    intent.setClass(SugAddressActivity.this, MapActivity.class);
                    startActivity(intent);
                    }
                    catch (Exception e){
                        new AlertDialog.Builder(SugAddressActivity.this)
                                .setTitle("提示")
                                .setMessage("搜索的位置不存在!")
                                .setPositiveButton("确定", null)
                                .show();
                    }
                }
            }).start();

        }




    }


}