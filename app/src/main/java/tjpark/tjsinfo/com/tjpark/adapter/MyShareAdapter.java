package tjpark.tjsinfo.com.tjpark.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.List;

import tjpark.tjsinfo.com.tjpark.BlueYuYueActivity;
import tjpark.tjsinfo.com.tjpark.IncludeDetailActivity;
import tjpark.tjsinfo.com.tjpark.LoginActivity;
import tjpark.tjsinfo.com.tjpark.MyShareActivity;
import tjpark.tjsinfo.com.tjpark.R;
import tjpark.tjsinfo.com.tjpark.RegActivity;
import tjpark.tjsinfo.com.tjpark.UpdateShareActivity;
import tjpark.tjsinfo.com.tjpark.entity.Car;
import tjpark.tjsinfo.com.tjpark.entity.MyShare;
import tjpark.tjsinfo.com.tjpark.util.NetConnection;

/**
 * Created by panning on 2018/1/25.
 */
public class MyShareAdapter extends ArrayAdapter<MyShare> {

    private Context mContext;
    private int resourceId;
    private LayoutInflater bsman = null;
    private String id,customer_id,status = "";
    /**
     *context:当前活动上下文
     *textViewResourceId:ListView子项布局的ID
     *objects：要适配的数据
     */
    public MyShareAdapter(Context context, int textViewResourceId,
                          List<MyShare> objects) {
        super(context, textViewResourceId, objects);

        resourceId = textViewResourceId;
        this.mContext = context;
        bsman = LayoutInflater.from(context);
    }

    /**
     * LIstView中每一个子项被滚动到屏幕的时候调用
     * position：滚到屏幕中的子项位置，可以通过这个位置拿到子项实例
     * convertView：之前加载好的布局进行缓存
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyShare myShare = getItem(position);
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
        Button ZT = (Button) view.findViewById(R.id.ZT);
        Button BJ = (Button) view.findViewById(R.id.BJ);
        Button SRMX = (Button) view.findViewById(R.id.SRMX);
        customer_id=myShare.getCustomer_id();
        status = myShare.getButtonName();
        FBSJ.setText(myShare.getCreate_time());
        TCCMG.setText(myShare.getPlace_name());
        CWH.setText(myShare.getPark_num());
        DQZT.setText(myShare.getShare_status());
        KSSJ.setText(myShare.getStart_time());
        JSSJ.setText(myShare.getEnd_time());
        GXMS.setText(myShare.getModel());
        GTFY.setText(myShare.getPark_fee()+"元/小时");
        id = myShare.getId();
        //状态按钮监听
        ZT.setText(myShare.getButtonName());
        ZT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                updateShareStatus

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
                                        JsonObject res = null;
                                        String strUrl="/tjpark/app/AppWebservice/updateShareStatus?" +
                                                "id=" +id+
                                                "&customerid=" +customer_id+
                                                "&status=" +status;
                                            res = NetConnection.getXpath(strUrl);


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
                intent.putExtra("id",id);
                mContext.startActivity(intent);
            }

        });
        //收入明细按钮监听
        SRMX.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, IncludeDetailActivity.class);
                intent.putExtra("shareid",id);
                mContext.startActivity(intent);


            }

        });


        return view;
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);



        }};


}

