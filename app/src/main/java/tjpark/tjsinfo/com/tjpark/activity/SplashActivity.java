package tjpark.tjsinfo.com.tjpark.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import tjpark.tjsinfo.com.tjpark.R;
import tjpark.tjsinfo.com.tjpark.fragment.OneFragment;


/**
 * Created by panning on 2018/2/8.
 */

//用于页面初始化
public class SplashActivity extends Activity {
    //定位SDK的核心类
    private LocationClient mLocClient;
    //定位SDK监听函数
    public SplashActivity.MyLocationListenner locListener = new SplashActivity.MyLocationListenner();
    //是否首次定位
    private boolean isFirstLoc = true;
    //定位图层显示模式 (普通-跟随-罗盘)
    private MyLocationConfiguration.LocationMode mCurrentMode;
    //定位图标描述
    private BitmapDescriptor mCurrentMarker = null;//当前位置经纬度


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    public   class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            if (OneFragment.latitude != 0.0 && OneFragment.longitude != 0.0) {
                mLocClient.stop();
               return ;
            }
            //获取经纬度
            OneFragment.latitude = location.getLatitude();
            OneFragment.longitude = location.getLongitude();

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(100)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            Intent intent = new Intent(SplashActivity.this, TabBarActivity.class);
            startActivity(intent);
            //结束当前的 Activity
            SplashActivity.this.finish();

        }
    }



}

