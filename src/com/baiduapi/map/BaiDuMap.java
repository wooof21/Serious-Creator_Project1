/**
 * 
 */
package com.baiduapi.map;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.chihuoshijian.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * @author Liming Chu
 * 
 * @param
 * @return
 */
public class BaiDuMap extends Activity{

	private MapView		mapView;
	private BaiduMap	mBaiduMap;
	private double		lat;
	private double		lon;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.baidu_map_view);
		mapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mapView.getMap();
		// 普通地图
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		// 卫星地图
		// mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(false);

		String title = getIntent().getStringExtra("title");
		lat = getIntent().getDoubleExtra("lat", 0.0);
		lon = getIntent().getDoubleExtra("lon", 0.0);
		Log.e("lat", "" + lat);
		Log.e("lon", "" + lon);
		LatLng point = new LatLng(lat, lon);

		MapStatus mMapStatus =
				new MapStatus.Builder().target(point)
						.zoom(19).build();
		MapStatusUpdate mMapStatusUpdate =
				MapStatusUpdateFactory
						.newMapStatus(mMapStatus);
		mBaiduMap.setMapStatus(mMapStatusUpdate);

		// 构建文字Option对象，用于在地图上添加文字
		OverlayOptions tOption =
				new TextOptions().bgColor(0xAAFFFF00)
						.fontSize(34).fontColor(0xFFFF00FF)
						.text(title).rotate(30)
						.position(point);
		// 在地图上添加该文字对象并显示
		 //构建Marker图标
		 BitmapDescriptor bitmap = BitmapDescriptorFactory
		 .fromResource(R.drawable.icon_gcoding);
		 //构建MarkerOption，用于在地图上添加Marker
		 OverlayOptions mOption = new MarkerOptions()
		 .position(point)
		 .icon(bitmap);
		// 在地图上添加Marker，并显示
		mBaiduMap.addOverlay(tOption);
		mBaiduMap.addOverlay(mOption);
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener(){
			
			@Override
			public boolean onMarkerClick(Marker arg0){
				// TODO Auto-generated method stub
				Log.e("marker", "clicked");
				
				return false;
			}
		});
		

	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mapView.onDestroy();
	}

	@Override
	protected void onResume(){
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mapView.onResume();
	}

	@Override
	protected void onPause(){
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mapView.onPause();
	}

}
