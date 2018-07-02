package com.tjsinfo.tjpark.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.hjm.bottomtabbar.BottomTabBar;
import com.tjsinfo.tjpark.fragment.FourFragment;
import com.tjsinfo.tjpark.fragment.MapFragment;
import com.tjsinfo.tjpark.fragment.OneFragment;
import com.tjsinfo.tjpark.fragment.ThreeFragment;
import com.tjsinfo.tjpark.fragment.TwoFragment;

import com.tjsinfo.tjpark.R;
import com.tjsinfo.tjpark.tabbar.IndexActivity;

/**
 * Created by panning on 2018/1/16.
 */
//tabbar
public class TabBarActivity extends FragmentActivity {

    private BottomTabBar mBottomTabBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbar);
        mBottomTabBar = (BottomTabBar) findViewById(R.id.bottom_tab_bar);
        Bundle bundle = this.getIntent().getExtras();
        int i = 0;
        try{
            i  =  Integer.parseInt(String.valueOf(bundle.get("currentTab")));
        }
        catch (Exception e){

        }
        mBottomTabBar.setOnTabChangeListener(new BottomTabBar.OnTabChangeListener(){
                    public void onTabChange(int position, String name, View view){
                        switch (position){
                            case 1:
                                mBottomTabBar.setFontSize(0.0f);
                            break;
                            case 2:

                                break;
                            default:

                        }

}
                                             }
        );
        mBottomTabBar.init(getSupportFragmentManager())
                .setImgSize(60,60)
                .setFontSize(10)
                .setTabPadding(4,6,10)
                .setChangeColor(getResources().getColor(R.color.dianjihou),getResources().getColor(R.color.dianjiqian))
                .addTabItem("首页", R.drawable.icon1,R.drawable.icon11, IndexActivity.class)
                .addTabItem("附近", R.drawable.icon2,R.drawable.icon21, MapFragment.class)
                .addTabItem("订单",R.drawable.icon3, R.drawable.icon31, TwoFragment.class)
                .addTabItem("我的",R.drawable.icon4, R.drawable.icon41, FourFragment.class)
                .setTabBarBackgroundColor(getResources().getColor(R.color.tabback))
                .isShowDivider(false).setCurrentTab(i);



    }

}
