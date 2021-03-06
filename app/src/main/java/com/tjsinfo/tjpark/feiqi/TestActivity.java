//package tjpark.tjsinfo.com.tjpark.fragment;
//
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.app.Fragment;
//import android.util.DisplayMetrics;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.SearchView;
//import android.widget.TextView;
//import com.baidu.mapapi.map.BaiduMap;
//import com.baidu.mapapi.map.BitmapDescriptor;
//import com.baidu.mapapi.map.BitmapDescriptorFactory;
//import com.baidu.mapapi.map.MapStatus;
//import com.baidu.mapapi.map.MapStatusUpdate;
//import com.baidu.mapapi.map.MapStatusUpdateFactory;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.Marker;
//import com.baidu.mapapi.map.MarkerOptions;
//import com.baidu.mapapi.map.OverlayOptions;
//import com.baidu.mapapi.model.LatLng;
//import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
//import com.baidu.mapapi.search.sug.SuggestionResult;
//import com.baidu.mapapi.search.sug.SuggestionSearch;
//import com.baidu.mapapi.search.sug.SuggestionSearchOption;
//import com.baidu.mapapi.utils.DistanceUtil;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import BlueParkActivity;
//import GreenParkActivity;
//import ParkListActivity;
//import R;
//import YellowParkActivity;
//import Park;
//import NetConnection;
//
//
//import ClusterItem;
//import ClusterManager;
//
//
///**
// *底部第一页
// */
//public class OneFragment extends Fragment implements BaiduMap.OnMapLoadedCallback {
//	//前台页面控件的显示
//	private MapView mMapView = null;
//	//百度地图对象
//	private BaiduMap mBaiduMap;
//	private String parkId = "";
//	//所有标记点的集合
//	List<OverlayOptions> options = new ArrayList<OverlayOptions>();
//	//远程接口返回的json封装成parkList集合
//	private List<Park> parkList = new LinkedList<Park>();
//	private Map<String, Park> parkMap = new HashMap<String, Park>();
//	private String place_name;
//
//	private String place_address;
//	private String place_distance;
//	private String place_num;
//	private ImageView imageView;
//	public static double latitude = 0.0;
//	public static double longitude = 0.0;
//	//地址搜索框
//	private SearchView sugSearch;
//	private List<String> sugAddress = new ArrayList<String>();
//	private ArrayAdapter arrayAdapter;
//
//	private ListView searchListView;
//
//	//点聚合管理类
//	private ClusterManager<OneFragment.MyItem> mClusterManager;
//	//地图状态
//	MapStatus ms;
//
//
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//							 Bundle savedInstanceState) {
//
//		return inflater.inflate(R.layout.activity_main, container, false);
//	}
//
//	//    实现接口OnMapLoadedCallback
//	@Override
//	public void onMapLoaded() {
//		ms = new MapStatus.Builder().zoom(9).build();
//		mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
//	}
//
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//		mMapView = (MapView) getActivity().findViewById(R.id.map_view);
//		mBaiduMap = mMapView.getMap();
//		new Thread(runnable).start();
//
//		//附近停车场监听事件
//		final Button getParkList = (Button) getActivity().findViewById(R.id.getParkList);
//		getParkList.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View view) {
//				Intent intent = new Intent();
//				intent.setClass(getActivity(), ParkListActivity.class);
//				startActivity(intent);
//			}
//
//		});
//
//		imageView = getActivity().findViewById(R.id.imageView);
//		sugSearch = getActivity().findViewById(R.id.sugSearch);
//		searchListView = (ListView) getActivity().findViewById(R.id.searchListView);
//		//为搜索地址框设置监听
//		sugSearch.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
//			@Override
//			public void onFocusChange(View view, boolean b) {
//				searchListView.setVisibility(View.VISIBLE);
//				mMapView.setVisibility(View.INVISIBLE);
//				imageView.setVisibility(View.INVISIBLE);
//
//				arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, sugAddress);
//				searchListView.setAdapter(arrayAdapter);
//				searchListView.setTextFilterEnabled(true);
//			}
//		});
//
////        //添加监听
//		OneFragment.ListViewListener ll = new OneFragment.ListViewListener();
//		searchListView.setOnItemClickListener(ll);
//		sugSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//			//点击搜索的时候
//			@Override
//			public boolean onQueryTextSubmit(String s) {
//
//				return false;
//			}
//
//			@Override
//			public boolean onQueryTextChange(String s) {
//				//SUG搜索
//				SuggestionSearch mSuggestionSearch = SuggestionSearch.newInstance();
//
//				mSuggestionSearch.setOnGetSuggestionResultListener(SUGListener);
//				// 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
//				mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
//						.keyword(s)//指定建议关键字 必填
//						.city(s));//请求城市 必填
//
//				return false;
//			}
//		});
//
//	}
//
//
//	//创建mark点
//	private void createMarker() {
//
//
//		//构建Marker图标
//		for (int i = 0; i < parkList.size(); i++) {
//			LatLng point1 = new LatLng(Double.parseDouble(parkList.get(i).getAddpoint_y()), Double.parseDouble(parkList.get(i).getAddpoint_x()));
//			//根据停车场的类别，划分不同的颜色的停车场
//			//停车场是否已满
////
//			if (parkList.get(i).getSpace_num().equals("0") && parkList.get(i).getShare_num().equals("0")) {
//				LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				View view = inflater.inflate(R.layout.activity_markernum, null);
//				TextView marker_num = (TextView) view.findViewById(R.id.marker_num);
//				Resources resources = getActivity().getBaseContext().getResources();
//				Drawable drawable = resources.getDrawable(R.drawable.huiqipao);
//				marker_num.setBackgroundDrawable(drawable);
//				BitmapDescriptor bitmap = BitmapDescriptorFactory
//						.fromBitmap(getBitmapFromView(view));
//
//				Bundle bundle = new Bundle();
//				bundle.putString("label", parkList.get(i).getLable());
//				//创建OverlayOptions属性 .icon后面的为图片样子
//				OverlayOptions option1 = new MarkerOptions()
//						.position(point1).icon(bitmap).title(parkList.get(i).getPlace_name()).extraInfo(bundle);
//
//				options.add(option1);
//			} else {
//
//				//黄色停车场，充电
//				if (parkList.get(i).getLable().contains("充电")) {
//					LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//					View view = inflater.inflate(R.layout.activity_markernum, null);
//					TextView marker_num = (TextView) view.findViewById(R.id.marker_num);
//					Resources resources = getActivity().getBaseContext().getResources();
//					Drawable drawable = resources.getDrawable(R.drawable.huangqipao);
//					marker_num.setBackgroundDrawable(drawable);
//					if (parkList.get(i).getFast_pile_space_num().equals("") || parkList.get(i).getSlow_pile_space_num().equals("")) {
//						marker_num.setText("0");
//					} else {
//						marker_num.setText(String.valueOf(Integer.parseInt(parkList.get(i).getFast_pile_space_num()) + Integer.parseInt(parkList.get(i).getSlow_pile_space_num())));
//					}
//
//					BitmapDescriptor bitmap = BitmapDescriptorFactory
//							.fromBitmap(getBitmapFromView(view));
//
//
//					Bundle bundle = new Bundle();
//					bundle.putString("parkId", parkList.get(i).getId());
//					bundle.putString("yellow_placeAddress", parkList.get(i).getPlace_address());
//					bundle.putString("label", parkList.get(i).getLable());
//					bundle.putString("placeFee", "6元/小时");
//					bundle.putString("yellow_placeDistance", parkList.get(i).getDistance() + "KM");
//					bundle.putString("parkNum", parkList.get(i).getSpace_num() + "/" + parkList.get(i).getPlace_total_num());
//					bundle.putString("parkFee", parkList.get(i).getPile_fee());
//					bundle.putString("kongXian", marker_num.getText().toString());
//					//充电费用
//					bundle.putString("pile_fee", parkList.get(i).getPile_fee());
//					//开放时间
//					bundle.putString("pile_time", parkList.get(i).getPile_time());
//					//快充
//					bundle.putString("fast_pile_space_num", "共" + parkList.get(i).getFast_pile_total_num() + "个，可用" + parkList.get(i).getFast_pile_space_num() + "个");
//					//慢充
//					bundle.putString("slow_pile_space_num", "共" + parkList.get(i).getSlow_pile_total_num() + "个，可用" + parkList.get(i).getSlow_pile_space_num() + "个");
//
//					//创建OverlayOptions属性 .icon后面的为图片样子
//					OverlayOptions option1 = new MarkerOptions()
//							.position(point1).icon(bitmap).title(parkList.get(i).getPlace_name()).extraInfo(bundle);
//					options.add(option1);
//				}
//				//绿色停车场，共享
//				else if (parkList.get(i).getLable().contains("共享")) {
//					LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//					View view = inflater.inflate(R.layout.activity_markernum, null);
//					TextView marker_num = (TextView) view.findViewById(R.id.marker_num);
//					Resources resources = getActivity().getBaseContext().getResources();
//
//					if (parkList.get(i).getShare_num().equals("0")) {
//						Drawable drawable = resources.getDrawable(R.drawable.huiqipao);
//						marker_num.setBackgroundDrawable(drawable);
//
//					} else {
//
//						Drawable drawable = resources.getDrawable(R.drawable.lvqipao);
//						marker_num.setBackgroundDrawable(drawable);
//						marker_num.setText(parkList.get(i).getShare_num());
//					}
//
//					BitmapDescriptor bitmap = BitmapDescriptorFactory
//							.fromBitmap(getBitmapFromView(view));
//
//					Bundle bundle = new Bundle();
//					bundle.putString("parkId", parkList.get(i).getId());
//					bundle.putString("label", parkList.get(i).getLable());
//					bundle.putString("green_placeDistance", parkList.get(i).getDistance() + "KM");
//					bundle.putString("green_placeAddress", parkList.get(i).getPlace_address());
//					bundle.putString("green_shareNum", parkList.get(i).getShare_num());
//
//					//创建OverlayOptions属性 .icon后面的为图片样子
//					OverlayOptions option1 = new MarkerOptions()
//							.position(point1).icon(bitmap).title(parkList.get(i).getPlace_name())
//							.extraInfo(bundle);
//					options.add(option1);
//				} else {
//					//蓝色停车场，普通的预约停车
//					LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//					View view = inflater.inflate(R.layout.activity_markernum, null);
//					TextView marker_num = (TextView) view.findViewById(R.id.marker_num);
//					marker_num.setText(parkList.get(i).getSpace_num());
//					BitmapDescriptor bitmap = BitmapDescriptorFactory
//							.fromBitmap(getBitmapFromView(view));
//
//
//					Bundle bundle = new Bundle();
//					bundle.putString("label", parkList.get(i).getLable());
//					bundle.putString("parkAddress", parkList.get(i).getPlace_address());
//					bundle.putString("placeFee", "6元/小时");
//					bundle.putString("parkId", parkList.get(i).getId());
//					bundle.putString("parkDistance", parkList.get(i).getDistance() + "KM");
//					bundle.putString("parkNum", parkList.get(i).getSpace_num() + "/" + parkList.get(i).getPlace_total_num());
//
//					//创建OverlayOptions属性 .icon后面的为图片样子
//					OverlayOptions option1 = new MarkerOptions()
//							.position(point1).icon(bitmap).title(parkList.get(i).getPlace_name()).extraInfo(bundle);
//
//					options.add(option1);
//				}
//			}
//		}
//
//	}
//
//	//marker监听(点击某一marker的时候),当点击停车场的时候会触发此事件
//	BaiduMap.OnMarkerClickListener listener = new BaiduMap.OnMarkerClickListener() {
//		/**
//		 * 地图 Marker 覆盖物点击事件监听函数
//		 * @param marker 被点击的 marker
//		 */
//		public boolean onMarkerClick(Marker marker) {
//			Dialog dialog = new Dialog(getActivity());
//
//			//共享停车场
//			if (marker.getExtraInfo().getString("label").contains("共享")) {
//				Window dialogWindow = dialog.getWindow();
//				WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//				dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
//
//				WindowManager manager = getActivity().getWindowManager();
//				DisplayMetrics outMetrics = new DisplayMetrics();
//				manager.getDefaultDisplay().getMetrics(outMetrics);
//				int width = outMetrics.widthPixels;
//				int height = outMetrics.heightPixels;
//				lp.x = 0; // 新位置X坐标
//				lp.y = height - 800; // 新位置Y坐标
//				lp.width = width; // 宽度
//				lp.height = 530; // 高度
//				lp.alpha = 0.9f; // 透明
//				dialogWindow.setAttributes(lp);
//
//				dialog.setContentView(R.layout.activity_markergreen);
//				TextView green_placeName = (TextView) dialog.findViewById(R.id.green_placeName);
//				TextView green_label = (TextView) dialog.findViewById(R.id.green_label);
//				TextView green_placeDistance = (TextView) dialog.findViewById(R.id.green_placeDistance);
//				TextView green_placeAddress = (TextView) dialog.findViewById(R.id.green_placeAddress);
//				TextView green_shareNum = (TextView) dialog.findViewById(R.id.green_shareNum);
//
//				green_placeName.setText(marker.getTitle());
//				green_label.setText(marker.getExtraInfo().getString("label"));
//				green_placeDistance.setText(marker.getExtraInfo().getString("green_placeDistance"));
//				green_placeAddress.setText(marker.getExtraInfo().getString("green_placeAddress"));
//				green_shareNum.setText(marker.getExtraInfo().getString("green_shareNum"));
//				Button green_parkDetail = (Button) dialog.findViewById(R.id.green_parkDetail);
//
//				parkId = marker.getExtraInfo().getString("parkId");
//				place_name = green_placeName.getText().toString();
//				place_distance = green_placeDistance.getText().toString();
//				place_address = green_placeAddress.getText().toString();
//				//共享停车场详情按钮监听事件
//				green_parkDetail.setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View view) {
//						Intent intent = new Intent();
//						intent.setClass(getActivity(), GreenParkActivity.class);
//						intent.putExtra("parkId", parkId);
//						intent.putExtra("place_name", place_name);
//						intent.putExtra("place_distance", place_distance);
//						intent.putExtra("place_address", place_address);
//
//						startActivity(intent);
//
//
//					}
//				});
//
//			}
//			//充电停车场
//			else if (marker.getExtraInfo().getString("label").contains("充电")) {
//				dialog.setContentView(R.layout.activity_markeryellow);
//				dialog.setTitle("充电停车场");
//				Window dialogWindow = dialog.getWindow();
//				WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//				dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
//
//				WindowManager manager = getActivity().getWindowManager();
//				DisplayMetrics outMetrics = new DisplayMetrics();
//				manager.getDefaultDisplay().getMetrics(outMetrics);
//				int width = outMetrics.widthPixels;
//				int height = outMetrics.heightPixels;
//				lp.x = 0; // 新位置X坐标
//				lp.y = height - 800; // 新位置Y坐标
//				lp.width = width; // 宽度
//				lp.height = 600; // 高度
//				lp.alpha = 0.9f; // 透明
//				dialogWindow.setAttributes(lp);
//
//				TextView yellow_placeName = (TextView) dialog.findViewById(R.id.yellow_placeName);
//				TextView yellow_label = (TextView) dialog.findViewById(R.id.yellow_label);
//				TextView yellow_placeDistance = (TextView) dialog.findViewById(R.id.yellow_placeDistance);
//				TextView yellow_placeAddress = (TextView) dialog.findViewById(R.id.yellow_placeAddress);
//				TextView yellow_placeFee = (TextView) dialog.findViewById(R.id.yellow_placeFee);
//				TextView yellow_placeNum = (TextView) dialog.findViewById(R.id.yellow_placeNum);
//				TextView yellow_CDFY = (TextView) dialog.findViewById(R.id.yellow_CDFY);
//				TextView yellow_kongXian = (TextView) dialog.findViewById(R.id.yellow_kongXian);
//				yellow_placeName.setText(marker.getTitle());
//				yellow_label.setText(marker.getExtraInfo().getString("label"));
//				yellow_placeDistance.setText(marker.getExtraInfo().getString("yellow_placeDistance"));
//				yellow_placeAddress.setText(marker.getExtraInfo().getString("yellow_placeAddress"));
//				yellow_placeFee.setText("收费标准:" + marker.getExtraInfo().getString("placeFee"));
//				yellow_placeNum.setText("车位情况:" + marker.getExtraInfo().getString("parkNum"));
//				yellow_CDFY.setText("充电费用:" + marker.getExtraInfo().getString("parkFee"));
//				yellow_kongXian.setText("空闲充电桩:" + marker.getExtraInfo().getString("kongXian"));
//				Button yellow_parkDetail = (Button) dialog.findViewById(R.id.yellow_parkDetail);
//
//				parkId = marker.getExtraInfo().getString("parkId");
//				place_name = yellow_placeName.getText().toString();
//				place_distance = yellow_placeDistance.getText().toString();
//				place_address = yellow_placeAddress.getText().toString();
//				final String pile_fee1 = marker.getExtraInfo().getString("pile_fee");
//				final String pile_time1 = marker.getExtraInfo().getString("pile_time");
//				final String fast_pile_space_num1 = marker.getExtraInfo().getString("fast_pile_space_num");
//				final String slow_pile_space_num1 = marker.getExtraInfo().getString("slow_pile_space_num");
//
//
//				//充电停车场详情按钮监听事件
//				yellow_parkDetail.setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View view) {
//						Intent intent = new Intent();
//						intent.setClass(getActivity(), YellowParkActivity.class);
//						intent.putExtra("parkId", parkId);
//						intent.putExtra("place_name", place_name);
//						intent.putExtra("place_distance", place_distance);
//						intent.putExtra("place_address", place_address);
//						intent.putExtra("pile_time", pile_time1);
//						intent.putExtra("pile_fee", pile_fee1);
//						intent.putExtra("slow_pile_total_num", slow_pile_space_num1);
//						intent.putExtra("fast_pile_total_num", fast_pile_space_num1);
////slow_pile_space_num1 pile_time1
//						startActivity(intent);
//					}
//				});
//
//			}
//			//普通停车场
//			else {
//				dialog.setContentView(R.layout.activity_markerblue);
//				Window dialogWindow = dialog.getWindow();
//				WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//				dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
//
//				WindowManager manager = getActivity().getWindowManager();
//				DisplayMetrics outMetrics = new DisplayMetrics();
//				manager.getDefaultDisplay().getMetrics(outMetrics);
//				int width = outMetrics.widthPixels;
//				int height = outMetrics.heightPixels;
//				lp.x = 0; // 新位置X坐标
//				lp.y = height - 800; // 新位置Y坐标
//				lp.width = width; // 宽度
//				lp.height = 530; // 高度
//				lp.alpha = 0.9f; // 透明
//				dialogWindow.setAttributes(lp);
//				//获取对应控件
//
//				TextView blue_placeName = (TextView) dialog.findViewById(R.id.blue_placeName);
//				TextView blue_label = (TextView) dialog.findViewById(R.id.blue_label);
//				TextView blue_placeDistance = (TextView) dialog.findViewById(R.id.blue_placeDistance);
//				TextView blue_placeAddress = (TextView) dialog.findViewById(R.id.blue_placeAddress);
//				TextView blue_placeFee = (TextView) dialog.findViewById(R.id.blue_placeFee);
//				TextView blue_placeNum = (TextView) dialog.findViewById(R.id.blue_placeNum);
//				blue_placeName.setText(marker.getTitle());
//				blue_label.setText(marker.getExtraInfo().getString("label"));
//				blue_placeDistance.setText(marker.getExtraInfo().getString("parkDistance"));
//				blue_placeAddress.setText(marker.getExtraInfo().getString("parkAddress"));
//				blue_placeFee.setText(marker.getExtraInfo().getString("placeFee"));
//				blue_placeNum.setText(marker.getExtraInfo().getString("parkNum"));
//				parkId = marker.getExtraInfo().getString("parkId");
//				place_name = blue_placeName.getText().toString();
//				place_address = blue_placeAddress.getText().toString();
//				place_distance = blue_placeDistance.getText().toString();
//				place_num = blue_placeNum.getText().toString();
//				Button blue_parkDetail = (Button) dialog.findViewById(R.id.blue_parkDetail);
//				//普通停车场详情按钮监听事件
//				blue_parkDetail.setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View view) {
//						Intent intent = new Intent();
//						intent.setClass(getActivity(), BlueParkActivity.class);
//						intent.putExtra("parkId", parkId);
//						intent.putExtra("place_name", place_name);
//						intent.putExtra("place_distance", place_distance);
//						intent.putExtra("place_address", place_address);
//						intent.putExtra("place_num", place_num);
//
//						startActivity(intent);
//					}
//				});
//
//			}
//			dialog.show();
//
//			return true;
//		}
//	};
//
//	//新线程进行网络请求
//	Runnable runnable = new Runnable() {
//		@Override
//		public void run() {
//			JsonArray jsonArray = null;
//			String strUrl = "/tjpark/app/AppWebservice/findPark";
//			jsonArray = NetConnection.getJsonArray(strUrl);
//
//			Iterator it = jsonArray.iterator();
//			//p1当前位置经纬度
//			LatLng p1 = new LatLng(latitude, longitude);
//			int i = 0;
//			while (it.hasNext()) {
//				Park park = new Park();
//				if (i == jsonArray.size()) {
//					break;
//				}
//				jsonArray.get(i).getAsJsonObject();
//				JsonObject jso = jsonArray.get(i).getAsJsonObject();
//
//				try {
//					if (jso.get("lable").toString().contains("共享")) {
//						park.setId(jso.get("id").toString().replace("\"", ""));
//						park.setPlace_name(jso.get("place_name").toString().replace("\"", ""));
//						park.setPlace_address(jso.get("place_address").toString().replace("\"", ""));
//						park.setLable(jso.get("lable").toString().replace("\"", ""));
//						park.setAddpoint_x(jso.get("addpoint_x").toString().replace("\"", ""));
//						park.setAddpoint_y(jso.get("addpoint_y").toString().replace("\"", ""));
//						park.setPlace_type(jso.get("place_type").toString().replace("\"", ""));
//						park.setShare_num(jso.get("share_num").toString().replace("\"", ""));
//						park.setSpace_num(jso.get("space_num").toString().replace("\"", ""));
////计算距离
//						LatLng p2 = new LatLng(Double.parseDouble(jso.get("addpoint_y").toString().replace("\"", "")), Double.parseDouble(jso.get("addpoint_x").toString().replace("\"", "")));
//						park.setDistance(String.valueOf(Math.ceil(DistanceUtil.getDistance(p1, p2)) / 1000));
//						//TODO:其他的属性，需要在此添加
//					} else if (jso.get("lable").toString().contains("充电")) {
//						park.setId(jso.get("id").toString().replace("\"", ""));
//						park.setPlace_name(jso.get("place_name").toString().replace("\"", ""));
//						park.setPlace_address(jso.get("place_address").toString().replace("\"", ""));
//						park.setLable(jso.get("lable").toString().replace("\"", ""));
//						park.setAddpoint_x(jso.get("addpoint_x").toString().replace("\"", ""));
//						park.setAddpoint_y(jso.get("addpoint_y").toString().replace("\"", ""));
//						park.setPlace_type(jso.get("place_type").toString().replace("\"", ""));
//						//总车位数
//						park.setPlace_total_num(jso.get("place_total_num").toString().replace("\"", ""));
//						park.setSpace_num(jso.get("space_num").toString().replace("\"", ""));
//						park.setPile_fee(jso.get("pile_fee").toString().replace("\"", ""));
//						park.setPile_time(jso.get("pile_time").toString().replace("\"", ""));
//						park.setFast_pile_space_num(jso.get("fast_pile_space_num").toString().replace("\"", ""));
//						park.setFast_pile_total_num(jso.get("fast_pile_total_num").toString().replace("\"", ""));
//						park.setSlow_pile_space_num(jso.get("slow_pile_space_num").toString().replace("\"", ""));
//						park.setSlow_pile_total_num(jso.get("slow_pile_total_num").toString().replace("\"", ""));
//						park.setSpace_num(jso.get("space_num").toString().replace("\"", ""));
////计算距离
//						LatLng p2 = new LatLng(Double.parseDouble(jso.get("addpoint_y").toString().replace("\"", "")), Double.parseDouble(jso.get("addpoint_x").toString().replace("\"", "")));
//						park.setDistance(String.valueOf(Math.ceil(DistanceUtil.getDistance(p1, p2)) / 1000));
//					} else {
//						park.setId(jso.get("id").toString().replace("\"", ""));
//						park.setPlace_name(jso.get("place_name").toString().replace("\"", ""));
//						park.setPlace_address(jso.get("place_address").toString().replace("\"", ""));
//						park.setLable(jso.get("lable").toString().replace("\"", ""));
//						park.setAddpoint_x(jso.get("addpoint_x").toString().replace("\"", ""));
//						park.setAddpoint_y(jso.get("addpoint_y").toString().replace("\"", ""));
//						park.setPlace_type(jso.get("place_type").toString().replace("\"", ""));
//						//总车位数
//						park.setPlace_total_num(jso.get("place_total_num").toString().replace("\"", ""));
//						park.setSpace_num(jso.get("space_num").toString().replace("\"", ""));
//
//						//计算距离
//						LatLng p2 = new LatLng(Double.parseDouble(jso.get("addpoint_y").toString().replace("\"", "")), Double.parseDouble(jso.get("addpoint_x").toString().replace("\"", "")));
//
//						park.setDistance(String.valueOf(Math.ceil(DistanceUtil.getDistance(p1, p2)) / 1000));
//
//					}
//					i++;
//					parkMap.put(park.getId(), park);
//
//				} catch (Exception e) {
//					parkMap.put(park.getId(), park);
//					i++;
//					continue;
//				}
//
//			}
//			i = 0;
//			Message msg = new Message();
//			handler.sendMessage(msg);
//
//		}
//	};
//
//
//	Handler handler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//
//			//在地图上批量添加Marker
//			addMarkers();
//			mBaiduMap.addOverlays(options);
//			//地图初始化之前实例化点聚合管理类
//
//
//			//设置marker监听
//
//			mBaiduMap.setOnMarkerClickListener(listener);
//			//开启定位图层
//			mBaiduMap.setMyLocationEnabled(true);
//
//			//地理坐标基本数据结构
//			LatLng loc = new LatLng(latitude, longitude);
//
//			//MapStatusUpdate描述地图将要发生的变化
//
//			MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(loc);
////缩放级别
//			mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(15).build()));
//
//			//更新地图状态
//			mBaiduMap.animateMapStatus(msu);
//		}
//	};
//
//
//	//自定义marker样式，xml变view对象
//	private Bitmap getBitmapFromView(View view) {
//		view.destroyDrawingCache();
//		view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.UNSPECIFIED);
//		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
//		view.setDrawingCacheEnabled(true);
//		Bitmap bitmap = view.getDrawingCache();
//		return bitmap;
//	}
//
//	//sug监听
//	OnGetSuggestionResultListener SUGListener = new OnGetSuggestionResultListener() {
//		public void onGetSuggestionResult(SuggestionResult res) {
//			if (res == null || res.getAllSuggestions() == null) {
//
//				return;
//				//未找到相关结果
//			}
//			sugAddress.clear();
//			List<SuggestionResult.SuggestionInfo> l = res.getAllSuggestions();
//			for (SuggestionResult.SuggestionInfo sugArredss : l
//					) {
//
//				String city = sugArredss.city + sugArredss.district + sugArredss.key;
//				sugAddress.add(city);
//			}
//			arrayAdapter.notifyDataSetChanged();
//
//		}
//	};
//
//	//listView监听item点击
//	class ListViewListener implements AdapterView.OnItemClickListener {
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//			mMapView.setVisibility(View.VISIBLE);
//			searchListView.setVisibility(View.INVISIBLE);
//			imageView.setVisibility(View.VISIBLE);
//			final String address = sugAddress.get(position);
////
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//
//					JsonObject res = null;
//					String strUrl = "http://api.map.baidu.com/geocoder/v2/?address=" + address + "&output=json&ak=EXj18khot93RCrLj6yizGXo69iCEP5FC&mcode=08:58:81:B0:2A:74:77:E1:75:5F:D4:D2:42:A2:7A:B8:3E:06:8A:2B;tjpark.tjsinfo.com.tjpark";
//
//					res = NetConnection.getAddressStatus(strUrl);
//
////嵌套取坐标
//					JsonObject data = res.getAsJsonObject("result");
//
//					Object data1 = data.getAsJsonObject("location");
//
//					JsonObject data2 = (JsonObject) data1;
//
//					latitude = Double.valueOf(((JsonObject) data1).get("lat").toString());
//					longitude = Double.valueOf(((JsonObject) data1).get("lng").toString());
//					LatLng loc = new LatLng(latitude, longitude);
//					MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(loc);
////缩放级别
//					mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(15).build()));
//
//					//更新地图状态
//					mBaiduMap.animateMapStatus(msu);
//
//
//				}
//			}).start();
//			sugSearch.setQuery(address, true);
//			//清除焦点
//			sugSearch.clearFocus();
//		}
//
//	}
//
//
//	/**
//	 * 每个Marker点，包含Marker点坐标以及图标
//	 */
//	public class MyItem implements ClusterItem {
//		private final LatLng mPosition;
//
//		public MyItem(LatLng latLng) {
//			mPosition = latLng;
//		}
//
//		@Override
//		public LatLng getPosition() {
//			return mPosition;
//		}
//
//		@Override
//		public BitmapDescriptor getBitmapDescriptor() {
//			return BitmapDescriptorFactory
//					.fromResource(R.drawable.huiqipao);
//		}
//	}
//
//	/**
//	 * 向地图添加Marker点
//	 */
//	public void addMarkers() {
//		// 添加Marker点
//		LatLng llA = new LatLng(39.963175, 116.400244);
//		LatLng llB = new LatLng(39.942821, 116.369199);
//		LatLng llC = new LatLng(39.939723, 116.425541);
//		LatLng llD = new LatLng(39.906965, 116.401394);
//		LatLng llE = new LatLng(39.956965, 116.331394);
//		LatLng llF = new LatLng(39.886965, 116.441394);
//		LatLng llG = new LatLng(39.996965, 116.411394);
//
//		List<OneFragment.MyItem> items = new ArrayList<OneFragment.MyItem>();
////TODO:
//
//		items.add(new OneFragment.MyItem(llA));
//		items.add(new OneFragment.MyItem(llB));
//		items.add(new OneFragment.MyItem(llC));
//		items.add(new OneFragment.MyItem(llD));
//		items.add(new OneFragment.MyItem(llE));
//		items.add(new OneFragment.MyItem(llF));
//		items.add(new OneFragment.MyItem(llG));
//		mClusterManager = new ClusterManager<OneFragment.MyItem>(getActivity(), mBaiduMap);
//		mClusterManager.addItems(items);
//
//	}
//
//
//
//
//
//}
