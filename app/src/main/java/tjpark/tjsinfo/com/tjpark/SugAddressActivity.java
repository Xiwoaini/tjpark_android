package tjpark.tjsinfo.com.tjpark;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import tjpark.tjsinfo.com.tjpark.entity.Car;
import tjpark.tjsinfo.com.tjpark.fragment.FourFragment;
import tjpark.tjsinfo.com.tjpark.fragment.OneFragment;
import tjpark.tjsinfo.com.tjpark.util.NetConnection;

/**
 * Created by panning on 2018/1/12.
 */

public class SugAddressActivity extends AppCompatActivity {
    private List<String> sugAddress  = new ArrayList<String>();;
    private SearchView mSearchView;
    private ListView mListView;
   private ArrayAdapter arrayAdapter;

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
                Log.v("1111","1111");
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                //SUG搜索
                SuggestionSearch mSuggestionSearch = SuggestionSearch.newInstance();

                mSuggestionSearch.setOnGetSuggestionResultListener(SUGListener);
                // 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                        .keyword(s)//指定建议关键字 必填
                        .city(s));//请求城市 必填

                return false;
            }
        });

    }
//        释放在线建议查询实例；
//        mSuggestionSearch.destroy();


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
            onBackPressed();


        }




    }


}