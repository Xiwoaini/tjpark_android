package com.tjsinfo.tjpark.tabbar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.tjsinfo.tjpark.R;
import com.tjsinfo.tjpark.activity.AddCarActivity;
import com.tjsinfo.tjpark.activity.BlueParkActivity;
import com.tjsinfo.tjpark.activity.CarLifeDisPlayActivity;
import com.tjsinfo.tjpark.activity.LoginActivity;
import com.tjsinfo.tjpark.activity.MapActivity;
import com.tjsinfo.tjpark.activity.NewsActivity;
import com.tjsinfo.tjpark.activity.SplashActivity;
import com.tjsinfo.tjpark.activity.SugAddressActivity;
import com.tjsinfo.tjpark.fragment.ThreeFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;


/**
 * Created by panning on 2018/6/12.
 */
//首页绑定类
public class IndexActivity  extends Fragment implements ViewPager.OnPageChangeListener  {

//前台控件绑定
    private TextView current_address,login_status;
    private  ImageView search_address;
    private ImageButton imgBtn1,imgBtn2,imgBtn3,imgBtn8;
    private TableRow tabLogin;
    private TableLayout tabLayout;
    private ImageButton csh1,csh2,csh3,csh4,csh5,csh6,csh7,csh8;
    private static boolean jg = true;

    //轮播图
    private ViewPager viewPager;
    private int[] imageResIds;
    private ArrayList<ImageView> imageViewList;
    boolean isRunning = false;

    private SharedPreferences mSharedPreferences;
    //初始化视图
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_index, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //初始化控件
        super.onActivityCreated(savedInstanceState);
        super.onCreate(savedInstanceState);

        //轮播图开始
        // 初始化布局 View视图
        initViews();
        // Model数据
        initData();
        // Controller 控制器
        initAdapter();
        try{
            // 开启轮询
            new Thread() {
                public void run() {
                    isRunning = true;
                    while (isRunning) {
                        try {
                            Thread.sleep(5000);
                            // 往下跳一位
                            if (getActivity() == null){
                                continue;
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
                ;
            }.start();
        }
        catch (Exception e){

        }
        viewPager.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //阻止手指离开时onTouch方法的继续执行
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 新的条目被选中时调用
                        int newPosition = viewPager.getCurrentItem() % imageViewList.size();
                        Log.v("1",String.valueOf(newPosition));
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), NewsActivity.class);
                        intent.putExtra("currentPage", newPosition);
                        startActivity(intent);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                }
                return true;
            }
        });


        //轮播图结束
        //获取前台绑定控件
        current_address = getActivity().findViewById(R.id.current_address);
        search_address = getActivity().findViewById(R.id.search_address);
        imgBtn1 = getActivity().findViewById(R.id.imgBtn1);
        imgBtn2 = getActivity().findViewById(R.id.imgBtn2);
        imgBtn3 = getActivity().findViewById(R.id.imgBtn3);
        imgBtn8 = getActivity().findViewById(R.id.imgBtn8);
        csh1 = getActivity().findViewById(R.id.csh1);
        csh2 = getActivity().findViewById(R.id.csh2);
        csh3 = getActivity().findViewById(R.id.csh3);
        csh4 = getActivity().findViewById(R.id.csh4);
        csh5 = getActivity().findViewById(R.id.csh5);
        csh6 = getActivity().findViewById(R.id.csh6);
        csh7 = getActivity().findViewById(R.id.csh7);
        csh8 = getActivity().findViewById(R.id.csh8);


        login_status = getActivity().findViewById(R.id.login_status);
        mSharedPreferences = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);
        //判断当前登录状态
        if (mSharedPreferences.getString("personID", "").equals("")){
            login_status.setText("您当前未登录，请点击前往登录");
            login_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //跳转到登录页
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
        else{
            tabLayout= getActivity().findViewById(R.id.tabLayout);
            tabLogin = getActivity().findViewById(R.id.tabLogin);
            tabLayout.removeView(tabLogin);

            //显示最近一个未完成的订单
        }
        //跳转到地图搜索
        search_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到搜索
                Intent intent = new Intent();
                intent.setClass(getActivity(), SugAddressActivity.class);
                startActivity(intent);
            }
        });
        imgBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到地图,普通类型
                Intent intent = new Intent();
                intent.setClass(getActivity(), MapActivity.class);
                MapActivity.parkType = "normal";
                startActivity(intent);
            }
        });
        imgBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到地图，预约类型
                Intent intent = new Intent();
                intent.setClass(getActivity(), MapActivity.class);
                MapActivity.parkType = "yuyue";
                startActivity(intent);
            }
        });
        imgBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到地图，共享类型
                Intent intent = new Intent();
                intent.setClass(getActivity(), MapActivity.class);
                MapActivity.parkType = "share";
                startActivity(intent);
            }
        });

        imgBtn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MapActivity.class);
                MapActivity.parkType = "charge";
                startActivity(intent);
            }
        });

        TextView nearPark = getActivity().findViewById(R.id.nearPark);
        nearPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到地图,普通类型
                Intent intent = new Intent();
                intent.setClass(getActivity(), MapActivity.class);
                MapActivity.parkType = "normal";
                startActivity(intent);
            }
        });


        csh1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarLifeDisPlayActivity.strUrl = "http://m.weizhang8.cn/";
                Intent intent = new Intent();
                //(当前Activity，目标Activity)
                intent.setClass(getActivity(), CarLifeDisPlayActivity.class);
                startActivity(intent);
            }
        });
        csh2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarLifeDisPlayActivity.strUrl = "http://m.carbank.cn/sales/20170601_a/?invitecode=hz160940&channel=%e7%99%be%e5%ba%a6SEM%e7%a7%bb%e5%8a%a8g&launchArea=%e5%85%a8%e5%9c%b0%e5%9f%9f&promotionMethod=%e5%8f%8c%e5%90%91%e8%af%8d_%e6%b5%8b%e8%af%95&unit=%e6%b1%bd%e8%bd%a6%e9%87%91%e8%9e%8d_%e5%b9%b3%e5%8f%b0&keyword=%e6%b1%bd%e8%bd%a6%e9%87%91%e8%9e%8d%e5%85%ac%e5%8f%b8&teltype=4000656082";
                Intent intent = new Intent();
                //(当前Activity，目标Activity)
                intent.setClass(getActivity(), CarLifeDisPlayActivity.class);
                startActivity(intent);
            }
        });
        csh3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarLifeDisPlayActivity.strUrl = "https://www.ipaosos.com/wshop/index.php#ca_source=Baidu";
                Intent intent = new Intent();
                //(当前Activity，目标Activity)
                intent.setClass(getActivity(), CarLifeDisPlayActivity.class);
                startActivity(intent);
            }
        });
        csh4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarLifeDisPlayActivity.strUrl = "http://daijia.xiaojukeji.com/";
                Intent intent = new Intent();
                //(当前Activity，目标Activity)
                intent.setClass(getActivity(), CarLifeDisPlayActivity.class);
                startActivity(intent);
            }
        });
        csh5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarLifeDisPlayActivity.strUrl = "http://u.pingan.com/upingan/insure/bdwx/bdwx.html?area=c03-bdwap-07&mediasource=C03-BDWAP-1-BQ-30723";
                Intent intent = new Intent();
                //(当前Activity，目标Activity)
                intent.setClass(getActivity(), CarLifeDisPlayActivity.class);
                startActivity(intent);
            }
        });
        csh6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarLifeDisPlayActivity.strUrl = "http://m.xin.com/tianjin/?channel=a16b46c1064d40488e159740f4&abtest=5_B";
                Intent intent = new Intent();
                //(当前Activity，目标Activity)
                intent.setClass(getActivity(), CarLifeDisPlayActivity.class);
                startActivity(intent);
            }
        });
        csh7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarLifeDisPlayActivity.strUrl = "http://m.yixiuche.com/";
                Intent intent = new Intent();
                //(当前Activity，目标Activity)
                intent.setClass(getActivity(), CarLifeDisPlayActivity.class);
                startActivity(intent);
            }
        });
        csh8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarLifeDisPlayActivity.strUrl = "http://price.m.yiche.com/zuidijia/nb1765/?WT.mc_id=mbdyqk__sutengkuan4Sdian";
                Intent intent = new Intent();
                //(当前Activity，目标Activity)
                intent.setClass(getActivity(), CarLifeDisPlayActivity.class);
                startActivity(intent);
            }
        });



        if (jg){
            if (!SplashActivity.locationTrue){
                jg = false;
                new AlertDialog.Builder(getActivity())
                        .setTitle("")
                        .setMessage("请允许天津停车使用您的当前位置以此来显示附近停车场。")
                        .setPositiveButton("确定", null)
                        .show();
            }
        }



    }




    private void initViews() {
        viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
        viewPager.setOnPageChangeListener(this);// 设置页面更新监听
    }

    /**
     * 初始化要显示的数据
     */
    private void initData() {
        // 图片资源id数组
        imageResIds = new int[]{R.drawable.bg1, R.drawable.bg2, R.drawable.bg3};
        // 初始化要展示的5个ImageView
        imageViewList = new ArrayList<ImageView>();

        ImageView imageView;
        View pointView;
        LinearLayout.LayoutParams layoutParams;
        for (int i = 0; i < imageResIds.length; i++) {
            // 初始化要显示的图片对象
            imageView = new ImageView(getActivity());
            imageView.setBackgroundResource(imageResIds[i]);
            imageViewList.add(imageView);

            // 加小白点, 指示器
            pointView = new View(getActivity());
            pointView.setBackgroundResource(R.drawable.bg1);
            layoutParams = new LinearLayout.LayoutParams(3, 3);
            if (i != 0)
                layoutParams.leftMargin = 10;
            // 设置默认所有都不可用
            pointView.setEnabled(false);
        }
    }

    private void initAdapter() {
        // 设置适配器
        viewPager.setAdapter(new MyAdapter());


        // 默认设置到中间的某个位置
        int pos = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % imageViewList.size());
        // 2147483647 / 2 = 1073741823 - (1073741823 % 5)
        viewPager.setCurrentItem(1); // 设置到某个位置
    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        // 3. 指定复用的判断逻辑, 固定写法
        @Override
        public boolean isViewFromObject(View view, Object object) {
            // 当划到新的条目, 又返回来, view是否可以被复用.
            // 返回判断规则
            return view == object;
        }

        // 1. 返回要显示的条目内容, 创建条目
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // container: 容器: ViewPager
            // position: 当前要显示条目的位置 0 -> 4

//          newPosition = position % 5
            int newPosition = position % imageViewList.size();

            ImageView imageView = imageViewList.get(newPosition);
            // a. 把View对象添加到container中
            try{
                // b. 把View对象返回给框架, 适配器
                container.addView(imageView);
                return imageView; // 必须重写, 否则报异常
            }
            catch(Exception e){
                return imageView; // 必须重写, 否则报异常
            }



        }

        // 2. 销毁条目
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // object 要销毁的对象
//            container.removeView((View) object);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        // 滚动时调用
    }

    @Override
    public void onPageSelected(int position) {
        // 新的条目被选中时调用
        int newPosition = position % imageViewList.size();

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // 滚动状态变化时调用
    }
}