package com.tjsinfo.tjpark.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.google.gson.JsonObject;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tjsinfo.tjpark.R;
import com.tjsinfo.tjpark.activity.PayResultActivity;
import com.tjsinfo.tjpark.activity.TabBarActivity;
import com.tjsinfo.tjpark.entity.ParkYuYue;
import com.tjsinfo.tjpark.entity.REQ;
import com.tjsinfo.tjpark.wxapi.Constants;
import com.tjsinfo.tjpark.wxapi.SignUtil;
import com.tjsinfo.tjpark.wxapi.XMLUtil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


/**
 *  重要说明:
 *  
 *  这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
 *  真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
 *  防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险； 
 */
//216.86
public class 	PayDemoActivity extends FragmentActivity {

	ParkYuYue parkYuYue = new ParkYuYue();
	/** 支付宝支付业务：入参app_id */
	public static final String APPID = "2017120600411807";
	
	/** 支付宝账户登录授权业务：入参pid值 */
	public static final String PID = "";
	/** 支付宝账户登录授权业务：入参target_id值 */
	public static final String TARGET_ID = "";

	private RadioButton pay_wx,pay_zfb;
	private String plate_number,plate_id,place_id,place_name,reservation_time,reservation_fee;


	/** 商户私钥，pkcs8格式 */
	public static final String RSA2_PRIVATE = "MIIEowIBAAKCAQEApWEVBmdvsBp676mALL9QELrHm6AkG0gVZvDvNca7ItrmPp4iLMNvlqe0H6UfhjEiz/QPz6vjPzT0MeZ5rywEQU4UdRBCtEWte6krTVzBjamoqfxpkJ+urVme0iuCMUMO8FoiN84sRVJJ5oIf1IagJsdUIS15LdBr1Q2WniCse7uxGkRBEK0OTT6bFQz8P1Aq4y651aecFDvGxqr7vCzWgMlh0PyNo+Piiy3m6aqFFDZmw06rNIaOg6CztLAOav9UEHxkSJuczIHuRSFOOVzlu+EN4eGm3j8/NUO5c8XzrqupRo5DYOtyl5h8RLBwe0Zoh9fq7lwmGZeRspUl8sr7cQIDAQABAoIBACvgsh9c2jkzDWMA6cz1hVyq8cLMnkfOvD7vtcfizkvVIDmE4zRVNgoWvKeYu+BysPXTn05OIKDof9GtgKOFXiuld7AHfGswAXNJ0v9XmNLpLKLNIYUJmOLNYGIKwSQo0pHamDGONhi+WHUcGS3d+ifPwvZ6higtoC6KyGdz6893836Gmd3FArvux4ER1WpMdM4lGuTHUF6SSZFajQdjjmYuCTw31iCrrD6tiKrd1J/JCG/w/+JW4Tfaf69bmSO4PLg5VFvUo+a9VF/0UxxViIYkj6rmwYMNbqPOiU/Xeee+oFTT3NQFUm842ZoLbwwyOxMf4XgK/BBhat+yg48QjjECgYEA26D3baaWi6Yp6QzvNepNKrMgANXP6pU7fp8jpSBCtQ4mTlq5ztWP2+ieUyYNFb2ha01gpGCp3wVrWDhG05TFNVg6g7iMv8WQZrgRK8ZUjgx1JxmA9UIVbnk+jEAbiEt5ABIUlgLWoziyQVnXhAgXmebA0YjvMK5VsVqX99vRA98CgYEAwMQ83E0wWy6CH6Fj77sK58S6yZQ3QcBy8OIJCmASkAUXzpXAcYiEAj4Q+IVjnHvlAxon+mlJheAmDW2Fqr14809mneF9Z/Xn4ma27Z0/gsxhRkDVWeT36NyR2eUIy9zb+NV7IAfGJ3bCw/kzmZhxMDEnSRlj8l2wx4i6/06Raq8CgYBP4gs80bO+FXD2+CJljNQGbOJ+C0a1fxQFqSJQ5Bv/OKdMJomgpmLNzJ0RhyyJNNDqc1lsUFBY8uKpUsbIHDtifLXDxTNEaTptchOkxV1p0TQnRYp3KlMbPHQ4lPSurSzUjr74FQ42jd+gD2po9nyHGLwXOmQtY6t9d4MAvu4WJwKBgQCZ039lpcsy2DhKmXWwdqhLL3iHJ9m4hKS0iQwB1Yy6lPXcizAY6YG+cF0GlRtaYpvsD9FbSO29AZQcHwwNpkmAkBopXym97kPvLVxI3bUy4Xm2oEIhDFCw6GMTaGvOkx6OwX0RoGKGV4Uw8go1Rar9dBwPf018uTs632eqGL5+TQKBgAz/rOXeKCUzn4fbLf/hOG0Xvy6VaznRTHJGgciiTOkvZj9LESfOBiNEtFZyyVhLm1W2tlGm3cHApLU7f7ETe1tm5RI0AX8BLUM/7L7iI2aEuI3F2G8sVv0oi2aPJbX4IcK/242wQqrF6MCiA10UHLEwvUCaDs239cWoRYUmB1WR";
	public static final String RSA_PRIVATE = "";
	
	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_AUTH_FLAG = 2;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				@SuppressWarnings("unchecked")
				PayResult payResult = new PayResult((Map<String, String>) msg.obj);
				/**
				 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
				 */
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息
				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为9000则代表支付成功
				if (TextUtils.equals(resultStatus, "9000")) {
//					 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//TODO：支付成功,跳转到订单页，并刷新,需要回调远程支付成功的接口
//					回调接口
					new Thread(  new Runnable(){
						@Override
						public void run() {
							//取出本地id
							SharedPreferences mSharedPreferences= getSharedPreferences("userInfo", MODE_PRIVATE);
							String userid=mSharedPreferences.getString("personID","");

							JsonObject res = null;
							String strUrl = "";
							if (parkYuYue.getPayMode().equals("正在计时")){
								strUrl="/tjpark/app/AppWebservice/parkPay?" +
										"userid=" + userid +
										"&parkid=" +parkYuYue.getRecord_id()+
										"&fee=" +parkYuYue.getReservation_fee() +
										"&place_name="+parkYuYue.getPlace_name() +
										"&payMode=alipay" +
										"&place_id="+parkYuYue.getPlace_id();
								res =NetConnection.getXpath(strUrl);
								Toast.makeText(PayDemoActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
								Intent intent = new Intent();
								intent.setClass(PayDemoActivity.this, PayResultActivity.class);
								intent.putExtra("money",parkYuYue.getReservation_fee());
								startActivity(intent);
							}
							else if (parkYuYue.getPayMode().contains("共享")){
								strUrl="/tjpark/app/AppWebservice/reservableParkInByShare?" +
										"customer_id=" + userid +
										"&plate_number=" +parkYuYue.getPlate_number()+
										"&plate_id=" +parkYuYue.getPlate_id() +
										"&place_id="+parkYuYue.getPlace_id() +
										"&place_name=" +parkYuYue.getPlace_name()+
										"&reservation_time="+parkYuYue.getReservation_time() +
										"&reservation_fee=" +parkYuYue.getReservation_fee().replace("元","") +
										"&payMode=alipay" +
										"&shareid="+parkYuYue.getShare_id() ;
								res =NetConnection.getXpath(strUrl);
								Toast.makeText(PayDemoActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
								Intent intent = new Intent();
								intent.setClass(PayDemoActivity.this, PayResultActivity.class);
								intent.putExtra("money",parkYuYue.getReservation_fee());
//								intent.putExtra("currentTab",1);
								startActivity(intent);
							}
							//默认为正在计时支付
							else{
//
								JsonObject res1 = null;
								strUrl="/tjpark/app/AppWebservice/reservableParkIn?" +
										"customer_id=" + userid +
										"&plate_number=" +plate_number+
										"&plate_id=" +plate_id+
										"&place_id="+place_id +
										"&place_name=" + place_name +
										"&reservation_time=" +reservation_time+
										"&reservation_fee=" +reservation_fee.replace("元","")+
										"&payMode=alipay";
//
								res1 =NetConnection.getXpath(strUrl);
								Toast.makeText(PayDemoActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
								Intent intent = new Intent();
								intent.setClass(PayDemoActivity.this, PayResultActivity.class);
								intent.putExtra("money",reservation_fee);
//								intent.putExtra("currentTab",1);
								startActivity(intent);
							}


						}
					}).start();

				} else {
					// 该笔订单真实的支付结果，需要依赖服务端的异步通知。
					new android.support.v7.app.AlertDialog.Builder(PayDemoActivity.this)
							.setTitle("信息")
							.setMessage("支付失败!")
							.setPositiveButton("确定", null)
							.show();

				}
				break;
			}
			case SDK_AUTH_FLAG: {
				@SuppressWarnings("unchecked")
				AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
				String resultStatus = authResult.getResultStatus();

				// 判断resultStatus 为“9000”且result_code
				// 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
				if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
					// 获取alipay_open_id，调支付时作为参数extern_token 的value
					// 传入，则支付账户为该授权账户
					Toast.makeText(PayDemoActivity.this,
							"授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
							.show();
				} else {
					// 其他状态值则为授权失败
					Toast.makeText(PayDemoActivity.this,
							"授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();
					new android.support.v7.app.AlertDialog.Builder(PayDemoActivity.this)
							.setTitle("信息")
							.setMessage("授权失败，请稍后重试!")
							.setPositiveButton("确定", null)
							.show();
				}
				break;
			}
			default:
				break;
			}
		};
	};
	private IWXAPI msgApi; //微信支付api

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orderpay);

		Button exitBtn=(Button)findViewById(R.id.exitBtn);
		exitBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				onBackPressed();
			}

		});


		Bundle bundle = this.getIntent().getExtras();
		 parkYuYue =(ParkYuYue)bundle.getSerializable("yuYueOrder");
		TextView pay_placeName=(TextView)findViewById(R.id.pay_placeName);
		TextView pay_money=(TextView)findViewById(R.id.pay_money);
		TextView DDLXJT=(TextView)findViewById(R.id.DDLXJT);
		TextView CPHMJT=(TextView)findViewById(R.id.CPHMJT);
		TextView YYDJT =(TextView)findViewById(R.id.YYDJT);
		TextView TCSJJT =(TextView)findViewById(R.id.TCSJJT);
		pay_placeName.setText(parkYuYue.getPlace_name());
		pay_money.setText(parkYuYue.getReservation_fee());
		DDLXJT.setText(parkYuYue.getDetail_type());
		CPHMJT.setText(parkYuYue.getPlate_number());
		YYDJT.setText(parkYuYue.getReservation_time().equals("")?"已进场":parkYuYue.getReservation_time());
		TCSJJT.setText(parkYuYue.getPark_time().equals("")?"暂未进场":parkYuYue.getPark_time());


		plate_number = parkYuYue.getPlate_number();
		plate_id=parkYuYue.getPlate_id();
		place_id=parkYuYue.getPlace_id();
		place_name=parkYuYue.getPlace_name();
		reservation_time=parkYuYue.getReservation_time();
		reservation_fee=parkYuYue.getReservation_fee();

		//选中支付方式
		  pay_wx = findViewById(R.id.pay_wx);
		pay_zfb = findViewById(R.id.pay_wx);

	}

//	4028815d5d63fa14015d63fb4f110000
	/**
	 * 支付宝支付业务
	 * 
	 * @param v
	 */
	public void payV2(View v) {
		//微信支付
		if (pay_wx.isChecked()){
			sendPayRequest();
			return;
		}
		//默认为支付宝

		if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
			new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialoginterface, int i) {
							//
							finish();
						}
					}).show();

			return;
		}
	
		/**
		 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
		 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
		 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险； 
		 * 
		 * orderInfo的获取必须来自服务端；
		 */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
		Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2,reservation_fee.replace("元",""));
		String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

		String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
		String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
		final String orderInfo = orderParam + "&" + sign;
		
		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				PayTask alipay = new PayTask(PayDemoActivity.this);
				Map<String, String> result = alipay.payV2(orderInfo, true);
//				Log.i("msp", result.toString());
				
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * 支付宝账户授权业务
	 * 
	 * @param v
	 */
	public void authV2(View v) {
		if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID)
				|| (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))
				|| TextUtils.isEmpty(TARGET_ID)) {
//			需要配置PARTNER |APP_ID| RSA_PRIVATE| TARGET_ID
			new AlertDialog.Builder(this).setTitle("警告").setMessage("支付失败，请稍后重试!")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialoginterface, int i) {
						}
					}).show();
			return;
		}

		/**
		 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
		 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
		 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险； 
		 * 
		 * authInfo的获取必须来自服务端；
		 */
		boolean rsa2 = (RSA2_PRIVATE.length() > 0);
		Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
		String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);
		
		String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
		String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
		final String authInfo = info + "&" + sign;
		Runnable authRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造AuthTask 对象
				AuthTask authTask = new AuthTask(PayDemoActivity.this);
				// 调用授权接口，获取授权结果
				Map<String, String> result = authTask.authV2(authInfo, true);

				Message msg = new Message();
				msg.what = SDK_AUTH_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread authThread = new Thread(authRunnable);
		authThread.start();
	}
	
	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();

	}

	/**
	 * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
	 *
	 * @param v
	 */
	public void h5Pay(View v) {
		Intent intent = new Intent(this, H5PayDemoActivity.class);
		Bundle extras = new Bundle();
		/**
		 * url 是要测试的网站，在 Demo App 中会使用 H5PayDemoActivity 内的 WebView 打开。
		 *
		 * 可以填写任一支持支付宝支付的网站（如淘宝或一号店），在网站中下订单并唤起支付宝；
		 * 或者直接填写由支付宝文档提供的“网站 Demo”生成的订单地址
		 * （如 https://mclient.alipay.com/h5Continue.htm?h5_route_token=303ff0894cd4dccf591b089761dexxxx）
		 * 进行测试。
		 *
		 * H5PayDemoActivity 中的 MyWebViewClient.shouldOverrideUrlLoading() 实现了拦截 URL 唤起支付宝，
		 * 可以参考它实现自定义的 URL 拦截逻辑。
		 */
		String url = "http://m.taobao.com";
		extras.putString("url", url);
		intent.putExtras(extras);
		startActivity(intent);
	}



	//微信支付
	/**调用微信支付*/
	public void sendPayRequest() {
		msgApi = WXAPIFactory.createWXAPI(this, Constants.APP_ID);


		//请求的xml数据
		final  String xmlString;
		//SrotedMap集合可以自动排序安装规则
		final SortedMap<Object, Object> map = new TreeMap<Object, Object>();


		//请求参数、参与签名的参数

		map.put("appid", Constants.APP_ID);
		map.put("body", "天津停车APP-停车缴费");
		map.put("mch_id", "1503922241");
		map.put("spbill_create_ip", getIPAddress(PayDemoActivity.this));
		final String nor_str =  SignUtil.getRandomString(32);
		map.put("nonce_str", nor_str);

		map.put("total_fee", "1");
		map.put("out_trade_no", String.valueOf(System.currentTimeMillis()).toString().substring(0,10));
		map.put("trade_type", "APP");
		map.put("notify_url", "http://www.weixin.qq.com/wxpay/pay.php");
		//签名的工具类SignUtil

		String sign = SignUtil.createSign1("UTF-8", map);
//		map.put("sign", sign);
		//xml的工具类XMlUtil
		xmlString = SignUtil.getRequestXML(map,sign);

		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					//
					String result = SignUtil.httpsRequest("https://api.mch.weixin.qq.com/pay/unifiedorder","POST", xmlString);
					REQ req = XMLUtil.parseXmlString(result);
					//发起调用(微信自带PayReq类，包含请求参数)
					PayReq request = new PayReq();
					//appid
					request.appId = Constants.APP_ID;
					//商户id
					request.partnerId = req.getPartnerid();
					request.prepayId= req.getPrepayid();
					request.packageValue = req.getPackages();
					request.nonceStr= nor_str;
					request.timeStamp= req.getTimestamp();
					request.sign= req.getSign();
                    // 发出授权申请
					msgApi.registerApp(Constants.APP_ID);
					Log.v("结果",String.valueOf(msgApi.sendReq(request)));


				}
				catch (Exception e){
					e.printStackTrace();
				}


			}
		}).start();

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

}
