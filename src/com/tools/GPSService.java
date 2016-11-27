/**
 * 
 */
package com.tools;

import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * @author Liming Chu
 *
 * @param
 * @return
 */
public class GPSService extends BroadcastReceiver{

	private Handler rHandler;
	private boolean success;
	private double latitude = 0.0;
	private double longitude = 0.0;
	
	public GPSService(Handler rHandler){
		super();
		this.rHandler = rHandler;
	}
	
	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent){
		// TODO Auto-generated method stub
		Log.e("onreceive", "onreceive");
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Location location = null;
		Criteria criteria = new Criteria();   
		// 获得最好的定位效果     
	    criteria.setAccuracy(Criteria.ACCURACY_FINE);     
	    criteria.setAltitudeRequired(false);     
	    criteria.setBearingRequired(false);     
	    criteria.setCostAllowed(false);     
	    // 使用省电模式     
	    criteria.setPowerRequirement(Criteria.POWER_LOW);     
	    String provider =locationManager.getBestProvider(criteria, true);   
	    Log.i("provider>>>>>>", provider);  
	    
		if(provider.equals("gps")){
			location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			success = true;
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			Log.e("lat1", ""+latitude);
			Log.e("lon1", ""+longitude);
		}else if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
			location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			success = true;
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			Log.e("lat1", ""+latitude);
			Log.e("lon1", ""+longitude);
		}else{
			success = false;
		}
		
		if(success){
			if(location != null){
				Log.e("success", "true");
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				Log.e("lat1", ""+latitude);
				Log.e("lon1", ""+longitude);
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("latitude", ""+latitude);
				hashMap.put("longitude", ""+longitude);
				Message msg = rHandler.obtainMessage();
				msg.what = Config.GPS;
				msg.obj = hashMap;
				rHandler.sendMessage(msg);
			}else{
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
			}
		}else{
			Message msg = rHandler.obtainMessage();
			msg.what = Config.NOGPS;
			rHandler.sendMessage(msg);
		}
		
		
	}
	
	LocationListener locationListener = new LocationListener() {
		// Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		// Provider被enable时触发此函数，比如GPS被打开
		@Override
		public void onProviderEnabled(String provider) {
			Log.e("provider", provider);
		}

		// Provider被disable时触发此函数，比如GPS被关闭
		@Override
		public void onProviderDisabled(String provider) {
			Log.e("provider", provider);
		}

		// 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
		@Override
		public void onLocationChanged(Location location) {
			if (location != null) {
				Log.e("Map", "Location changed : Lat: " + location.getLatitude() + " Lng: " + location.getLongitude());
				latitude = location.getLatitude(); // 经度
				longitude = location.getLongitude(); // 纬度
			}
		}
	};

}
