package com.tjsinfo.tjpark.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tjsinfo.tjpark.entity.Park;

import java.util.HashMap;
import java.util.Map;

import com.tjsinfo.tjpark.R;

/**
 * Created by panning on 2018/1/15.
 */

///ip地址.
public class TjParkUtils {


    public static String windowIp="http://60.29.41.58:3000";
//    public static String windowIp="http://192.168.168.221:8080";

    //parkMap集合,地图显示等功能会用到此集合,所有集合
    public  static Map<String, Park> parkMap = new HashMap<String, Park>();
    //普通停车场
    public  static Map<String, Park> parkMapByNormal = new HashMap<String, Park>();
    //充电停车场
    public  static Map<String, Park> parkMapByCharge = new HashMap<String, Park>();
    //在线支付停车场
    public  static Map<String, Park> parkMapByPay = new HashMap<String, Park>();
    //共享停车场
    public  static Map<String, Park> parkMapByShare = new HashMap<String, Park>();
    //预约停车场
    public  static Map<String, Park> parkMapByYuYue = new HashMap<String, Park>();

    public static String StringToUTF(String s) {
        try{
            return java.net.URLEncoder.encode(s,"utf-8");
        }
        catch (Exception e){
            e.printStackTrace();
            return "";
        }

    }
        public static Dialog createLoadingDialog(Context context, String msg) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v
                .findViewById(R.id.dialog_loading_view);// 加载布局
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.MyDialogStyle);// 创建自定义样式dialog
        loadingDialog.setCancelable(true); // 是否可以按“返回键”消失
        loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        /**
         *将显示Dialog的方法封装在这里面
         */
        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
//        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        loadingDialog.show();

        return loadingDialog;
    }
    //     * 关闭dialog
//             *
//             * @param mDialogUtils
//        */
public static void closeDialog(Dialog mDialogUtils) {
        if (mDialogUtils != null && mDialogUtils.isShowing()) {
        mDialogUtils.dismiss();
        }
        }
}






