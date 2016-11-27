/**
 * 
 */
package com.main.canting;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.chihuoshijian.R;
import com.chihuoshijian.adapter.CanTingListAdapter;
import com.chihuoshijian.view.ExpandTabView;
import com.chihuoshijian.view.ViewLeft;
import com.chihuoshijian.view.ViewMiddle;
import com.chihuoshijian.view.ViewRight;
import com.main.baofang.BaoFangMainListPage;
import com.tools.Config;
import com.tools.GPSService;
import com.tools.Tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Liming Chu
 * 
 * @param
 * @return
 */
public class CanTingMainListPage extends Activity{

	private ExpandTabView						expandTabView;
	private ArrayList<View>						mViewArray	=
																	new ArrayList<View>();
	private ViewLeft							viewLeft;
	private ViewMiddle							viewMiddle;
	private ViewRight							viewRight;

	private String[]							leftItems;
	private String[]							leftItemsId;

	private ArrayList<String>					groups;
	private HashMap<String, String>				childrenItem;
	private ArrayList<String[]>					children;

	private ProgressDialog						progressDialog;

	private ArrayList<HashMap<String, String>>	ctList;
	private CanTingListAdapter					ctAdapter;

	private ListView							lv;
	private double								latitude;
	private double								longitude;
	private LocationClient						mLocationClient;
	private BDLocationListener					myListener	=
																	new MyLocationListener();
	private double								lat			= 0,
			lon = 0;
	private int									page,
			w_jl = 0;
	private String								bid			= "0",
			w_qy = "0", w_title = "0", w_order = "0";
	private EditText							et;
	private TextView							search;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.canting_main_list);
		prepareView();
		mLocationClient =
				new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		LocationClientOption option =
				new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(false);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);
		mLocationClient.start();

		new LeftAsync().execute();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause(){
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	protected void onStop(){
		// TODO Auto-generated method stub
		mLocationClient.stop();
		super.onStop();
	}

	private void prepareView(){
		lv =
				(ListView) findViewById(R.id.canting_main_list_page_lv);
		et = (EditText) findViewById(R.id.search_et);
		search =
				(TextView) findViewById(R.id.search_frame_search);

		search.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				if(et.getText().toString() != null){
					w_title = et.getText().toString();
				}else{
					w_title = "0";
				}
				new ListAsync().execute("" + 1, bid, w_qy, w_title, w_order, ""
						+ lon, "" + lat);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed(){
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	private class MyLocationListener implements
		BDLocationListener{
		@Override
		public void onReceiveLocation(BDLocation location){
			if(location == null)
				return;
			StringBuffer sb = new StringBuffer(256);
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			lat = location.getLatitude();
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			lon = location.getLongitude();

			Log.e("location", sb.toString());
		}
	}

	// private void getLocation(){
	// LocationManager locationManager = (LocationManager)
	// getSystemService(Context.LOCATION_SERVICE);
	// Location location = null;
	// Criteria criteria = new Criteria();
	// // 获得最好的定位效果
	// criteria.setAccuracy(Criteria.ACCURACY_FINE);
	// criteria.setAltitudeRequired(false);
	// criteria.setBearingRequired(false);
	// criteria.setCostAllowed(false);
	// // 使用省电模式
	// criteria.setPowerRequirement(Criteria.POWER_LOW);
	// String provider =locationManager.getBestProvider(criteria, true);
	// Log.i("provider>>>>>>", provider);
	// location = locationManager.getLastKnownLocation(provider);
	// latitude = location.getLatitude();
	// longitude = location.getLongitude();
	// Log.e("lat1", ""+latitude);
	// Log.e("lon1", ""+longitude);
	// //获得当前位置 location为空是一直取 从onLocationChanged里面取
	// while(location==null)
	// {
	// location =locationManager.requestLocationUpdates("gps", 1000, 0,
	// locationListener);
	// }
	// //locationListener
	// LocationListener locationListener = new LocationListener()
	// {
	//
	// @Override
	// public void onLocationChanged(Location location)
	// {
	//
	// }
	//
	// @Override
	// public void onProviderDisabled(String provider)
	// {
	//
	// }
	//
	// @Override
	// public void onProviderEnabled(String provider)
	// {
	//
	// }
	//
	// @Override
	// public void onStatusChanged(String provider, int status, Bundle extras)
	// {
	//
	// }
	//
	// };
	// locationManager.requestLocationUpdates(provider, 1000, 10,
	// locationListener);
	// }
	//
	// LocationListener locationListener = new LocationListener() {
	// // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
	// @Override
	// public void onStatusChanged(String provider, int status, Bundle extras) {
	// }
	//
	// // Provider被enable时触发此函数，比如GPS被打开
	// @Override
	// public void onProviderEnabled(String provider) {
	// Log.e("provider", provider);
	// }
	//
	// // Provider被disable时触发此函数，比如GPS被关闭
	// @Override
	// public void onProviderDisabled(String provider) {
	// Log.e("provider", provider);
	// }
	//
	// // 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
	// @Override
	// public void onLocationChanged(Location location) {
	// if (location != null) {
	// Log.e("Map", "Location changed : Lat: " + location.getLatitude() +
	// " Lng: " + location.getLongitude());
	// latitude = location.getLatitude(); // 经度
	// longitude = location.getLongitude(); // 纬度
	// }
	// }
	// };

	class LeftAsync extends AsyncTask<Void, Void, String>{

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute(){
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog =
					ProgressDialog.show(CanTingMainListPage.this, null, null);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String doInBackground(Void... params){
			// TODO Auto-generated method stub
			System.out.println(com.tools.Config.cxUrl);
			String data =
					new Tools().getURL(com.tools.Config.cxUrl);
			System.out.println(data);

			String code = "";
			try{
				JSONObject job = new JSONObject(data);
				code = job.getString("code");
				if(code.equals("1")){
					JSONArray jArray =
							job.getJSONArray("result");
					leftItems = new String[jArray.length()];
					leftItemsId =
							new String[jArray.length()];
					for(int i = 0,j = jArray.length();i < j;i++){
						JSONObject job1 =
								jArray.optJSONObject(i);
						leftItems[i] =
								job1.getString("classname");
						leftItemsId[i] =
								job1.getString("id");
					}
				}
			}catch(JSONException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return code;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String result){
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result.equals("1")){
				new MidAsync().execute();
			}else{
				Toast.makeText(CanTingMainListPage.this, "菜系获取失败！", Toast.LENGTH_LONG).show();
			}
		}

	}

	class MidAsync extends AsyncTask<Void, Void, String>{

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute(){
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String doInBackground(Void... params){
			// TODO Auto-generated method stub
			System.out.println(Config.sqUrl);
			String data = new Tools().getURL(Config.sqUrl);
			System.out.println(data);
			String code = "";

			try{
				JSONObject job = new JSONObject(data);
				code = job.getString("code");
				if(code.equals("1")){
					JSONArray jArray =
							job.getJSONArray("result");
					groups = new ArrayList<String>();
					childrenItem =
							new HashMap<String, String>();
					children = new ArrayList<String[]>();
					String[] name;
					for(int i = 0,j = jArray.length();i < j;i++){
						JSONObject job1 =
								jArray.optJSONObject(i);
						groups.add(job1.getString("classname"));
						JSONArray jArray2 =
								job1.getJSONArray("next");
						name = new String[jArray2.length()];
						for(int m = 0,n = jArray2.length();m < n;m++){
							JSONObject job2 =
									jArray2.optJSONObject(m);
							name[m] =
									job2.getString("classname");
							childrenItem.put(job2.getString("classname"), job2.getString("id"));
							// childrenItem.put("nextname",
							// job2.getString("classname"));
						}
						children.add(name);

					}

				}
			}catch(JSONException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return code;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String result){
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result.equals("1")){
				// progressDialog.dismiss();
				initView();
				initVaule();
				initListener();

				// getLocation();
				if(lat == 0 && lon == 0){
					Log.e("location", "null");
					new ListAsync().execute("" + 1, bid, w_qy, w_title, w_order, ""
							+ lon, "" + lat);
				}else{
					Log.e("location", lat + " + " + lon);
					new ListAsync().execute("" + 1, bid, w_qy, w_title, w_order, ""
							+ lon, "" + lat);
				}

			}

		}

	}

	class ListAsync extends AsyncTask<Object, Void, String>{

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String doInBackground(Object... params){
			// TODO Auto-generated method stub
			String code = "";
			String url = "";
			if(params[2].equals("1")
					|| params[2].equals("2")
					|| params[2].equals("3")
					|| params[2].equals("4")
					|| params[2].equals("5")){
				if(params[5].equals("0.0")
						&& params[6].equals("0.0")){
					url =
							Config.CT_MAIN_LIST_URL
									+
									// "&page="
									// + params[0] +
									"&bid=" + params[1]
									+ "&w_jl=" + params[2]
									+ "&w_title="
									+ params[3]
									+ "&w_order="
									+ params[4] + "&lng1=0"
									+ "&lat1=0";
				}else{
					url =
							Config.CT_MAIN_LIST_URL
									+
									// "&page="
									// + params[0] +
									"&bid=" + params[1]
									+ "&w_jl=" + params[2]
									+ "&w_title="
									+ params[3]
									+ "&w_order="
									+ params[4] + "&lng1="
									+ params[5] + "&lat1="
									+ params[6];
				}
			}else{
				if(params[5].equals("0.0")
						&& params[6].equals("0.0")){
					url =
							Config.CT_MAIN_LIST_URL
									+
									// "&page="
									// + params[0] +
									"&bid=" + params[1]
									+ "&w_qy=" + params[2]
									+ "&w_title="
									+ params[3]
									+ "&w_order="
									+ params[4] + "&lng1=0"
									+ "&lat1=0";
				}else{
					url =
							Config.CT_MAIN_LIST_URL
									+
									// "&page="
									// + params[0] +
									"&bid=" + params[1]
									+ "&w_qy=" + params[2]
									+ "&w_title="
									+ params[3]
									+ "&w_order="
									+ params[4] + "&lng1="
									+ params[5] + "&lat1="
									+ params[6];
				}
			}

			Log.e("url", url);

			String data = new Tools().getURL(url);
			System.out.println(data);
			ctList =
					new ArrayList<HashMap<String, String>>();

			try{
				JSONObject job = new JSONObject(data);
				code = job.getString("code");
				if(code.equals("1")){
					JSONArray jArray =
							job.getJSONArray("result");
					for(int i = 0,j = jArray.length();i < j;i++){
						JSONObject job1 =
								jArray.optJSONObject(i);
						HashMap<String, String> hashMap =
								new HashMap<String, String>();
						hashMap.put("uid", job1.getString("uid"));
						hashMap.put("w_title", job1.getString("w_title"));
						hashMap.put("w_logo_pic", job1.getString("w_logo_pic"));
						hashMap.put("w_jj", job1.getString("w_jj"));
						hashMap.put("w_dz", job1.getString("w_dz"));
						hashMap.put("w_dh", job1.getString("w_dh"));
						hashMap.put("w_longitude", job1.getString("w_longitude"));
						hashMap.put("w_latitude", job1.getString("w_latitude"));
						hashMap.put("w_pl", job1.getString("w_pl"));
						hashMap.put("w_xj", job1.getString("w_xj"));
						hashMap.put("w_ms", job1.getString("w_ms"));

						ctList.add(hashMap);
					}
				}
			}catch(JSONException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return (String) params[2];
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String result){
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			ctAdapter =
					new CanTingListAdapter(
						CanTingMainListPage.this, ctList);
			lv.setAdapter(ctAdapter);

			lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void
					onItemClick(AdapterView<?> parent,
							View view,
							int position,
							long id){
					// TODO Auto-generated method stub
					Intent intent =
							new Intent(
								CanTingMainListPage.this,
								CanTingDetailActivity.class);
					intent.putExtra("uid", ctList.get(position).get("uid"));
					startActivity(intent);
				}
			});
		}
	}

	@Override
	protected void onActivityResult(int requestCode,
			int resultCode,
			Intent data){
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
			case 1002:
				if(resultCode == 0){
					Toast.makeText(CanTingMainListPage.this, "开启GPS失败！", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(CanTingMainListPage.this, "开启GPS成功！", Toast.LENGTH_LONG).show();
				}
			break;

			default:
			break;
		}
	}

	private void initView(){

		expandTabView =
				(ExpandTabView) findViewById(R.id.canting_main_list_page_expandtab_view);
		viewLeft =
				new ViewLeft(CanTingMainListPage.this,
					leftItems, leftItemsId);
		viewMiddle =
				new ViewMiddle(this, groups, childrenItem,
					children);
		viewRight = new ViewRight(this);

		viewLeft.setShowText("餐厅");
		viewMiddle.setShowString("全城");
		viewRight.setShowText("智能排序");

	}

	private void initVaule(){

		mViewArray.add(viewLeft);
		mViewArray.add(viewMiddle);
		mViewArray.add(viewRight);
		ArrayList<String> mTextArray =
				new ArrayList<String>();
		mTextArray.add("餐厅");
		mTextArray.add("全城");
		mTextArray.add("智能排序");
		expandTabView.setValue(mTextArray, mViewArray);
		expandTabView.setTitle(viewLeft.getShowText(), 0);
		expandTabView.setTitle(viewMiddle.getShowText(), 1);
		expandTabView.setTitle(viewRight.getShowText(), 2);

	}

	private void initListener(){

		viewLeft.setOnSelectListener(new ViewLeft.OnSelectListener(){

			@Override
			public void getValue(String distance,
					String showText){
				onRefresh(viewLeft, showText);
				bid = distance;
				new ListAsync().execute("" + 1, bid, w_qy, w_title, w_order, ""
						+ lon, "" + lat);
			}
		});

		viewMiddle.setOnSelectListener(new ViewMiddle.OnSelectListener(){

			@Override
			public void getValue(String showText){

				onRefresh(viewMiddle, showText);
				w_qy = getMidId(showText);
				new ListAsync().execute("" + 1, bid, w_qy, w_title, w_order, ""
						+ lon, "" + lat);
			}
		});

		viewRight.setOnSelectListener(new ViewRight.OnSelectListener(){

			@Override
			public void getValue(String distance,
					String showText){
				onRefresh(viewRight, showText);
				w_order = distance;
				new ListAsync().execute("" + 1, bid, w_qy, w_title, w_order, ""
						+ lon, "" + lat);
			}
		});

	}

	private void onRefresh(View view, String showText){

		expandTabView.onPressBack();
		int position = getPositon(view);
		if(position >= 0
				&& !expandTabView.getTitle(position).equals(showText)){
			expandTabView.setTitle(showText, position);
		}
		Toast.makeText(CanTingMainListPage.this, showText, Toast.LENGTH_SHORT).show();

	}

	private String getMidId(String text){
		return childrenItem.get(text);
	}

	private int getPositon(View tView){
		for(int i = 0;i < mViewArray.size();i++){
			if(mViewArray.get(i) == tView){
				return i;
			}
		}
		return -1;
	}

}
