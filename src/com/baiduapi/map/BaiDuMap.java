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
		// ��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext
		// ע��÷���Ҫ��setContentView����֮ǰʵ��
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.baidu_map_view);
		mapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mapView.getMap();
		// ��ͨ��ͼ
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		// ���ǵ�ͼ
		// mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
		// ������λͼ��
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

		// ��������Option���������ڵ�ͼ���������
		OverlayOptions tOption =
				new TextOptions().bgColor(0xAAFFFF00)
						.fontSize(34).fontColor(0xFFFF00FF)
						.text(title).rotate(30)
						.position(point);
		// �ڵ�ͼ����Ӹ����ֶ�����ʾ
		 //����Markerͼ��
		 BitmapDescriptor bitmap = BitmapDescriptorFactory
		 .fromResource(R.drawable.icon_gcoding);
		 //����MarkerOption�������ڵ�ͼ�����Marker
		 OverlayOptions mOption = new MarkerOptions()
		 .position(point)
		 .icon(bitmap);
		// �ڵ�ͼ�����Marker������ʾ
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
		// ��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���
		mapView.onDestroy();
	}

	@Override
	protected void onResume(){
		super.onResume();
		// ��activityִ��onResumeʱִ��mMapView. onResume ()��ʵ�ֵ�ͼ�������ڹ���
		mapView.onResume();
	}

	@Override
	protected void onPause(){
		super.onPause();
		// ��activityִ��onPauseʱִ��mMapView. onPause ()��ʵ�ֵ�ͼ�������ڹ���
		mapView.onPause();
	}

}
