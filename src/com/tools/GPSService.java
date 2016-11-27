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
		// �����õĶ�λЧ��     
	    criteria.setAccuracy(Criteria.ACCURACY_FINE);     
	    criteria.setAltitudeRequired(false);     
	    criteria.setBearingRequired(false);     
	    criteria.setCostAllowed(false);     
	    // ʹ��ʡ��ģʽ     
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
		// Provider��״̬�ڿ��á���ʱ�����ú��޷�������״ֱ̬���л�ʱ�����˺���
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		// Provider��enableʱ�����˺���������GPS����
		@Override
		public void onProviderEnabled(String provider) {
			Log.e("provider", provider);
		}

		// Provider��disableʱ�����˺���������GPS���ر�
		@Override
		public void onProviderDisabled(String provider) {
			Log.e("provider", provider);
		}

		// ������ı�ʱ�����˺��������Provider������ͬ�����꣬���Ͳ��ᱻ����
		@Override
		public void onLocationChanged(Location location) {
			if (location != null) {
				Log.e("Map", "Location changed : Lat: " + location.getLatitude() + " Lng: " + location.getLongitude());
				latitude = location.getLatitude(); // ����
				longitude = location.getLongitude(); // γ��
			}
		}
	};

}
