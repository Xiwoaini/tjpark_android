package tjpark.tjsinfo.com.tjpark;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.hjm.bottomtabbar.BottomTabBar;

import tjpark.tjsinfo.com.tjpark.fragment.FourFragment;
import tjpark.tjsinfo.com.tjpark.fragment.OneFragment;
import tjpark.tjsinfo.com.tjpark.fragment.ThreeFragment;
import tjpark.tjsinfo.com.tjpark.fragment.TwoFragment;

/**
 * Created by panning on 2018/1/16.
 */

public class TabBarActivity extends FragmentActivity {

    private  BottomTabBar mBottomTabBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbar);
        mBottomTabBar = (BottomTabBar) findViewById(R.id.bottom_tab_bar);
        mBottomTabBar.init(getSupportFragmentManager())
                .setImgSize(50,50)
                .setFontSize(8)
                .setTabPadding(4,6,10)
                .setChangeColor(Color.DKGRAY,Color.RED)
                .addTabItem("首页", R.mipmap.ic_launcher, OneFragment.class)
                .addTabItem("订单", R.mipmap.ic_launcher, TwoFragment.class)
                .addTabItem("更多", R.mipmap.ic_launcher, ThreeFragment.class)
                .addTabItem("我的", R.mipmap.ic_launcher, FourFragment.class)
                .setTabBarBackgroundColor(Color.WHITE)
                .isShowDivider(false);

    }




}