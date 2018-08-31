package com.tjsinfo.tjpark.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tjsinfo.tjpark.activity.BlueYuYueActivity;
import com.tjsinfo.tjpark.activity.MyCarActivity;
import com.tjsinfo.tjpark.activity.SelectPhotoActivity;
import com.tjsinfo.tjpark.entity.Car;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import com.tjsinfo.tjpark.R;
import com.tjsinfo.tjpark.feiqi.MainActivity;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by panning on 2018/1/25.
 */
/**
 * listview自定义格式的适配器
 */
public class CarAdapter extends ArrayAdapter<Car>   {
    private Context context;
    private int resourceId;
    public static String customerid;

    //相册请求码
    private static final int ALBUM_REQUEST_CODE = 1;
    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 2;
    //剪裁请求码
    private static final int CROP_REQUEST_CODE = 3;



    //调用照相机返回图片文件
    private File tempFile;
    /** 选择文件 */
    public static final int TO_SELECT_PHOTO = 1;
    //相机相关变量  end



    public CarAdapter(Context context, int textViewResourceId,
                      List<Car> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        resourceId = textViewResourceId;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Car car = getItem(position);

        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        TextView plate = (TextView) view.findViewById(R.id.plate);

        plate.setText(car.getPlace_number());
        //计算屏幕宽度
        try{
            WindowManager wm1 = ((MyCarActivity)context).getWindowManager();
            int width1 = wm1.getDefaultDisplay().getWidth();

            plate.setWidth((int)(width1*0.8));
        }
        catch (Exception e){
            WindowManager wm1 = ((BlueYuYueActivity)context).getWindowManager();
            int width1 = wm1.getDefaultDisplay().getWidth();

            plate.setWidth((int)(width1*0.8));
        }
        if("暂未审核".equals(car.getIf_certify())){
            Button verBtn = (Button) view.findViewById(R.id.verBtn);
            verBtn.setText("暂未审核");

            verBtn.setTextColor(Color.BLUE);
        }
        else  if("完成认证".equals(car.getIf_certify())){
            Button verBtn = (Button) view.findViewById(R.id.verBtn);
            verBtn.setTextColor(Color.GRAY);
            verBtn.setText("完成认证");
        }
        else{
            //立即认证按钮
            Button verBtn = (Button) view.findViewById(R.id.verBtn);
            verBtn.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, SelectPhotoActivity.class);
                    intent.putExtra("plateId",car.getId());
                    intent.putExtra("coustmerId",customerid);
                    ((MyCarActivity)context).startActivityForResult(intent, CarAdapter.TO_SELECT_PHOTO);

                    intent = null;


                }
            });
        }


        return view;
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };




}
