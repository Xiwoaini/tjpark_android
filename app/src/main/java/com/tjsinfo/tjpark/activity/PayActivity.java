package com.tjsinfo.tjpark.activity;

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
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

        import com.tencent.mm.opensdk.modelbase.BaseReq;
        import com.tencent.mm.opensdk.modelpay.PayReq;
        import com.tencent.mm.opensdk.openapi.IWXAPI;
        import com.tencent.mm.opensdk.openapi.WXAPIFactory;
        import com.tjsinfo.tjpark.R;
        import com.tjsinfo.tjpark.entity.REQ;
        import com.tjsinfo.tjpark.util.Util;
        import com.tjsinfo.tjpark.wxapi.SignUtil;
        import com.tjsinfo.tjpark.wxapi.XMLUtil;
        import java.net.Inet4Address;
        import java.net.InetAddress;
        import java.net.NetworkInterface;
        import java.net.SocketException;
        import java.util.Enumeration;
        import java.util.SortedMap;
        import java.util.TreeMap;

public class PayActivity extends Activity {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paydemo);
        api = WXAPIFactory.createWXAPI(this, "wxbda31b250a331d1d",true);
        boolean b= api.registerApp("wxbda31b250a331d1d");
        Button appayBtn = (Button) findViewById(R.id.appay_btn);
        appayBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";

                        final SortedMap<Object, Object> map = new TreeMap<Object, Object>();

                        map.put("appid", "wxbda31b250a331d1d");
                        map.put("mch_id", "1503922241");
                        map.put("nonce_str", SignUtil.getRandomString(32));
                        map.put("body", "apppay");
                        map.put("out_trade_no", "1535332878");
                        map.put("total_fee", "1");
                        map.put("spbill_create_ip", "192.168.10.244");
                        map.put("notify_url", "http://www.weixin.qq.com/wxpay/pay.php");
                        map.put("trade_type", "APP");

                        //签名的工具类SignUtil
                        String sign = SignUtil.createSign1("UTF-8", map);

                        //xml的工具类XMlUtil
                        String xmlString = SignUtil.getRequestXML(map, sign);
                        try {
                            byte[] buf = Util.httpPost(url, xmlString);
                            if (buf != null && buf.length > 0) {
                                String content = new String(buf);
                                REQ json = XMLUtil.parseXmlString(content);
                                PayReq req = new PayReq();
                                req.appId = json.getAppid();
                                req.partnerId = json.getPartnerid();
                                req.prepayId = json.getPrepayid();
                                req.packageValue = json.getPackages();
                                req.nonceStr = json.getNoncestr();
                                req.timeStamp = json.getTimestamp();
                                req.sign = json.getSign();
                                req.extData = "app data"; // optional
                                 api.sendReq(req);

//
                            } else {
                                Log.d("PAY_GET", "服务器请求错误");

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

