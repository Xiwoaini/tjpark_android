package tjpark.tjsinfo.com.tjpark.activity;

import android.app.Application;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.hjm.bottomtabbar.BottomTabBar;

import tjpark.tjsinfo.com.tjpark.R;
import tjpark.tjsinfo.com.tjpark.entity.Park;
import tjpark.tjsinfo.com.tjpark.fragment.FourFragment;
import tjpark.tjsinfo.com.tjpark.fragment.OneFragment;
import tjpark.tjsinfo.com.tjpark.fragment.ThreeFragment;
import tjpark.tjsinfo.com.tjpark.fragment.TwoFragment;

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

        mBottomTabBar.init(getSupportFragmentManager())
                .setImgSize(50,50)
                .setFontSize(8)
                .setTabPadding(4,6,10)
                .setChangeColor(Color.BLUE,Color.DKGRAY)
                .addTabItem("首页", R.drawable.iconsy,R.drawable.iconsy, OneFragment.class)
                .addTabItem("订单", R.drawable.icondd,R.drawable.icondd, TwoFragment.class)
                .addTabItem("更多",R.drawable.iconyy, R.drawable.iconyy, ThreeFragment.class)
                .addTabItem("我的",R.drawable.iconwd, R.drawable.iconwd, FourFragment.class)
                .setTabBarBackgroundColor(Color.WHITE)
                .isShowDivider(false).setCurrentTab(i);

    }

}
