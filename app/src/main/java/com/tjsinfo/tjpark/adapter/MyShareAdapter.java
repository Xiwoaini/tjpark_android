package com.tjsinfo.tjpark.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.tjsinfo.tjpark.activity.IncludeDetailActivity;
import com.tjsinfo.tjpark.activity.MyShareActivity;

import java.util.LinkedList;
import java.util.List;

import com.tjsinfo.tjpark.R;
import com.tjsinfo.tjpark.activity.UpdateShareActivity;
import com.tjsinfo.tjpark.entity.MyShare;
import com.tjsinfo.tjpark.util.NetConnection;

/**
 * Created by panning on 2018/1/25.
 */
/**
 * listview自定义格式的适配器
 */
public class MyShareAdapter extends ArrayAdapter<MyShare> {

    private Context mContext;
    private int resourceId;
    private LayoutInflater bsman = null;
    private String customer_id,status = "";
    private  List<String> shareId = new LinkedList<String>();

    public MyShareAdapter(Context context, int textViewResourceId,
                          List<MyShare> objects) {
        super(context, textViewResourceId, objects);

        resourceId = textViewResourceId;
        this.mContext = context;
        bsman = LayoutInflater.from(context);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MyShare myShare = getItem(position);
        //为子项动态加载布局
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        TextView FBSJ = (TextView) view.findViewById(R.id.FBSJ);
        TextView TCCMG = (TextView) view.findViewById(R.id.TCCMG);
        TextView CWH = (TextView) view.findViewById(R.id.CWH);
        TextView DQZT = (TextView) view.findViewById(R.id.DQZT);
        final TextView KSSJ = (TextView) view.findViewById(R.id.KSSJ);
        final TextView JSSJ = (TextView) view.findViewById(R.id.JSSJ);
        final TextView GXMS = (TextView) view.findViewById(R.id.GXMS);
        TextView GTFY = (TextView) view.findViewById(R.id.GTFY);
        final Button ZT = (Button) view.findViewById(R.id.ZT);
        Button BJ = (Button) view.findViewById(R.id.BJ);
        Button SRMX = (Button) view.findViewById(R.id.SRMX);
        customer_id=myShare.getCustomer_id();
        if (myShare.getStatus().equals("审核中")){
            DQZT.setText(myShare.getStatus());
            ZT.setText("取消");
        }
        else{
            DQZT.setText(myShare.getShare_status());
            ZT.setText(myShare.getButtonName());
        }
        status = myShare.getButtonName();
        FBSJ.setText(myShare.getCreate_time());
        TCCMG.setText(myShare.getPlace_name());
        CWH.setText(myShare.getPark_num());

        KSSJ.setText(myShare.getStart_time());
        JSSJ.setText(myShare.getEnd_time());
        GXMS.setText(myShare.getModel());
        GTFY.setText(myShare.getPark_fee()+"元/小时");
        shareId.add(myShare.getId());

        //状态按钮监听

        ZT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(mContext);

                normalDialog.setTitle("提示");
                normalDialog.setMessage("您确定此操作吗?");
                normalDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        JsonArray res = null;
                                        String  strUrl = "";
                                        if (ZT.getText().toString().equals("取消")){
                                            strUrl="/tjpark/app/AppWebservice/updateShareStatus?" +
                                                    "id=" +shareId.get(position)+
                                                    "&customerid=" +customer_id+
                                                    "&status=取消";
                                        }
                                        else{
                                            strUrl="/tjpark/app/AppWebservice/updateShareStatus?" +
                                                    "id=" +shareId.get(position)+
                                                    "&customerid=" +customer_id+
                                                    "&status=" +status;
                                        }

                                            res = NetConnection.getJsonArray(strUrl);


                                        Message msg = new Message();
                                        handler.sendMessage(msg);
                                    }
                                }).start();


                            }
                        });
                normalDialog.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                // 显示
                normalDialog.show();
            }

        });
        //编辑按钮监听
        BJ.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, UpdateShareActivity.class);
                intent.putExtra("KSSJ",KSSJ.getText());
                intent.putExtra("JSSJ",JSSJ.getText());
                intent.putExtra("GXMS",GXMS.getText());
                intent.putExtra("id",shareId.get(position));
                mContext.startActivity(intent);
            }

        });
        //收入明细按钮监听
        SRMX.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, IncludeDetailActivity.class);
                intent.putExtra("shareid",shareId.get(position));
                mContext.startActivity(intent);


            }

        });


        return view;
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//刷新页面
            Intent intent = new Intent();
            intent.setClass(mContext, MyShareActivity.class);
            mContext.startActivity(intent);
        }};


}

