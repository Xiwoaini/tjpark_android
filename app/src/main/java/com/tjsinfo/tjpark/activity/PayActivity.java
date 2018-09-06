package com.tjsinfo.tjpark.activity;

        import android.app.Activity;
        import android.content.Context;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.net.wifi.WifiInfo;
        import android.net.wifi.WifiManager;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Message;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;

        import com.tencent.mm.opensdk.modelpay.PayReq;
        import com.tencent.mm.opensdk.openapi.IWXAPI;
        import com.tencent.mm.opensdk.openapi.WXAPIFactory;
        import com.tjsinfo.tjpark.R;
        import com.tjsinfo.tjpark.entity.REQ;
        import com.tjsinfo.tjpark.util.Util;
        import com.tjsinfo.tjpark.util.wechatUtil.CommonUtil;
        import com.tjsinfo.tjpark.util.wechatUtil.ConfigUtil;
        import com.tjsinfo.tjpark.util.wechatUtil.PayCommonUtil;
        import com.tjsinfo.tjpark.util.wechatUtil.XMLUtil;
        import com.tjsinfo.tjpark.wxapi.HttpUtil;
        import com.tjsinfo.tjpark.wxapi.SignUtil;

        import java.io.IOException;
        import java.net.Inet4Address;
        import java.net.InetAddress;
        import java.net.NetworkInterface;
        import java.net.SocketException;
        import java.util.Date;
        import java.util.Enumeration;
        import java.util.Map;
        import java.util.SortedMap;
        import java.util.TreeMap;

public class PayActivity extends Activity {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paydemo);
        api = WXAPIFactory.createWXAPI(this, ConfigUtil.APPID,true);
        boolean b= api.registerApp(ConfigUtil.APPID);
        Button appayBtn = (Button) findViewById(R.id.appay_btn);
        appayBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";

                        final SortedMap<Object, Object> map = new TreeMap<Object, Object>();

                        map.put("appid", ConfigUtil.APPID);
                        map.put("mch_id", ConfigUtil.MCH_ID);
                        map.put("nonce_str", PayCommonUtil.CreateNoncestr());
                        map.put("body", "apppay");
                        map.put("out_trade_no", String.valueOf(new Date().getTime()));
                        map.put("total_fee", "1");
                        map.put("spbill_create_ip", "192.168.10.244");
                        map.put("notify_url", ConfigUtil.NOTIFY_URL);
                        map.put("trade_type", "APP");
                        //一次验签
                        String sign = PayCommonUtil.createSign("UTF-8",map);
                        map.put("sign",sign);
                        //xml的工具类XMlUtil
                        String requestXML = PayCommonUtil.getRequestXml(map);

                        String result = CommonUtil.httpsRequest(ConfigUtil.UNIFIED_ORDER_URL,"POST",requestXML);
                        try {
//                            String result = HttpUtil.sendXMLDataByPost(ConfigUtil.UNIFIED_ORDER_URL,requestXML);
                            Map obj = XMLUtil.doXMLParse(result);
                            if ("SUCCESS".equals(obj.get("result_code"))){
                                final SortedMap<Object, Object> twoMap = new TreeMap<Object, Object>();
                                twoMap.put("appid",ConfigUtil.APPID);
                                twoMap.put("partnerid",ConfigUtil.MCH_ID);
                                twoMap.put("prepayid",XMLUtil.doXMLParse(result).get("prepay_id"));
                                twoMap.put("noncestr",XMLUtil.doXMLParse(result).get("nonce_str"));
                                twoMap.put("timestamp",String.valueOf(System.currentTimeMillis()).toString().substring(0,10));
                                twoMap.put("package","Sign=WXPay");
                                String twoSign = PayCommonUtil.createSign("UTF-8", twoMap);
                                obj.put("sign",twoSign);
                                obj.put("timestamp",twoMap.get("timestamp"));
                                obj.put("result",obj);

                                PayReq req = new PayReq();
                                req.appId = ConfigUtil.APPID;
                                req.partnerId = ConfigUtil.MCH_ID;
                                req.prepayId = twoMap.get("prepayid").toString();
                                req.packageValue = twoMap.get("package").toString();
                                req.nonceStr =twoMap.get("noncestr").toString();
                                req.timeStamp = twoMap.get("timestamp").toString();
                                req.sign = twoSign;
                                req.extData = "app data"; // optional
                                boolean b =req.checkArgs();
                                api.sendReq(req);

                            }
                        } catch (Exception e) {
                            Log.e("PAY_GET", "异常：" + e.getMessage());
                        }






                    }
                }).start();





//
            }
        });
    }

    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }
    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }
    //处理结果
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            api.sendReq(req);

        }
    };
}

