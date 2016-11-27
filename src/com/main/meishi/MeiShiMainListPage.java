/**
 * 
 */
package com.main.meishi;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import com.chihuoshijian.adapter.BaoFangListAdapter;
import com.chihuoshijian.adapter.MeiShiMainListAdapter;
import com.chihuoshijian.view.ExpandTabView;
import com.chihuoshijian.view.ViewLeft;
import com.chihuoshijian.view.ViewMiddle;
import com.chihuoshijian.view.ViewRight;
import com.main.baofang.BaoFangMainListPage;
import com.main.canting.CanTingDetailActivity;
import com.tools.Config;
import com.tools.Tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author Liming Chu
 *
 * @param
 * @return
 */
public class MeiShiMainListPage extends Activity{

	private ExpandTabView			expandTabView;
	private ArrayList<View>			mViewArray	=
														new ArrayList<View>();
	private ViewMiddle				viewMiddle;
	private ViewRight				viewRight;

	private ArrayList<String>		groups;
	private HashMap<String, String>	childrenItem;
	private ArrayList<String[]>		children;

	private ProgressDialog			progressDialog;
	private ListView	lv;
	private ArrayList<HashMap<String, String>>	msList;
	private LocationClient	mLocationClient;
	private BDLocationListener myListener = new MyLocationListener();
	private double lat = 0, lon = 0;
	
	private String title;
	private MeiShiMainListAdapter	msAdapter;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baofang_main_list_page);
		
		prepareView();
		mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
	    mLocationClient.registerLocationListener(myListener);    //注册监听函数
	    LocationClientOption option = new LocationClientOption();
	    option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
	    option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
	    option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
	    option.setIsNeedAddress(false);//返回的定位结果包含地址信息
	    option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
	    mLocationClient.setLocOption(option);
	    mLocationClient.start();
	    
	    title = getIntent().getStringExtra("n_title");
	    System.out.println(title);
	    new MidAsync().execute();

	    
	    
	}
	
	private void prepareView(){
		lv = (ListView)findViewById(R.id.baofang_main_list_page_lv);
	}

	private class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
		            return ;
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
//			String data =
//					"{\"code\":1,\"result\":[{\"id\":1,\"classname\":\"\u9644\u8fd1\",\"next\":[{\"id\":1,\"classname\":\"1km\"},{\"id\":2,\"classname\":\"2km\"},{\"id\":3,\"classname\":\"3km\"},{\"id\":4,\"classname\":\"4km\"},{\"id\":5,\"classname\":\"5km\"}]},{\"id\":0,\"classname\":\"\u70ed\u95e8\u5546\u5708\",\"next\":[{\"id\":\"299\",\"classname\":\"\u7ea2\u65d7\u8857\"},{\"id\":\"300\",\"classname\":\"\u6b27\u4e9a\u5356\u573a\"},{\"id\":\"305\",\"classname\":\"\u6842\u6797\u8def\"},{\"id\":\"308\",\"classname\":\"\u91cd\u5e86\u8def\u4eba\u6c11\u5e7f\u573a\"},{\"id\":\"313\",\"classname\":\"\u65b0\u5929\u5730\u8d2d\u7269\u516c\u56ed\"}]},{\"id\":\"296\",\"classname\":\"\u671d\u9633\u533a\",\"next\":[{\"id\":\"297\",\"classname\":\"\u6587\u5316\u5e7f\u573a\"},{\"id\":\"298\",\"classname\":\"\u5efa\u8bbe\u8857\"},{\"id\":\"299\",\"classname\":\"\u7ea2\u65d7\u8857\"},{\"id\":\"300\",\"classname\":\"\u6b27\u4e9a\u5356\u573a\"},{\"id\":\"301\",\"classname\":\"\u6e56\u897f\u8def\"},{\"id\":\"302\",\"classname\":\"\u671d\u9633\u6865\"},{\"id\":\"303\",\"classname\":\"\u6c38\u660c\"},{\"id\":\"304\",\"classname\":\"\u5357\u6e56\u516c\u56ed\"},{\"id\":\"305\",\"classname\":\"\u6842\u6797\u8def\"},{\"id\":\"306\",\"classname\":\"\u8f89\u5357\u8857\"}]},{\"id\":\"307\",\"classname\":\"\u5357\u5173\u533a\",\"next\":[{\"id\":\"308\",\"classname\":\" \u91cd\u5e86\u8def\u4eba\u6c11\u5e7f\u573a\"},{\"id\":\"309\",\"classname\":\"\u65b0\u6625\"},{\"id\":\"310\",\"classname\":\"\u5357\u5173\u533a\u653f\u5e9c\"},{\"id\":\"311\",\"classname\":\"\u536b\u661f\u5e7f\u573a\"},{\"id\":\"312\",\"classname\":\"\u5357\u5cad\"},{\"id\":\"313\",\"classname\":\"\u65b0\u5929\u5730\u8d2d\u7269\u516c\u56ed\"},{\"id\":\"314\",\"classname\":\"\u5de5\u5927\u5357\u95e8\"},{\"id\":\"315\",\"classname\":\"\u5168\u5b89\u5e7f\u573a\"},{\"id\":\"316\",\"classname\":\"\u4e94\u73af\u4f53\u80b2\u573a\"},{\"id\":\"317\",\"classname\":\"\u4e1c\u5357\u6e56\u5927\u8def\"},{\"id\":\"318\",\"classname\":\"\u4e1c\u5e7f\u573a\"}]},{\"id\":\"156\",\"classname\":\"\u5bbd\u57ce\u533a\",\"next\":[{\"id\":\"170\",\"classname\":\"\u80dc\u5229\u516c\u56ed\"},{\"id\":\"171\",\"classname\":\"\u96c1\u9e23\u6e56\"},{\"id\":\"172\",\"classname\":\"\u706b\u8f66\u7ad9\"},{\"id\":\"173\",\"classname\":\"\u51ef\u65cb\u8def\"},{\"id\":\"174\",\"classname\":\"\u897f\u5e7f\u573a\"},{\"id\":\"175\",\"classname\":\"\u4e00\u5321\u8857\"}]},{\"id\":\"157\",\"classname\":\"\u7eff\u56ed\u533a\",\"next\":[{\"id\":\"176\",\"classname\":\"\u957f\u6625\u516c\u56ed\u65b0\u7af9\"},{\"id\":\"177\",\"classname\":\"\u9526\u6c5f\u5e7f\u573a\u7535\u5f71\u57ce\"}]},{\"id\":\"180\",\"classname\":\"\u4e8c\u9053\u533a\",\"next\":[{\"id\":\"181\",\"classname\":\"\u4e1c\u76db\"},{\"id\":\"182\",\"classname\":\"\u4e1c\u7ad9\"}]},{\"id\":\"158\",\"classname\":\"\u9ad8\u85aa\u533a\",\"next\":[{\"id\":\"178\",\"classname\":\"\u7845\u8c37\u5927\u8857\"},{\"id\":\"179\",\"classname\":\"\u5409\u5927\u5357\u6821\"}]},{\"id\":\"129\",\"classname\":\"\u7ecf\u6d4e\u5f00\u53d1\u533a\",\"next\":[{\"id\":\"183\",\"classname\":\"\u8d5b\u5f97\u5e7f\u573a\"},{\"id\":\"184\",\"classname\":\"\u73e0\u6d77\u8def\"},{\"id\":\"185\",\"classname\":\"\u4e50\u5929\u739b\u7279\"},{\"id\":\"186\",\"classname\":\"\u4e34\u6cb3\u8857\"},{\"id\":\"187\",\"classname\":\"\u8d5b\u5fb7\u5e7f\u573a\"},{\"id\":\"188\",\"classname\":\"\u5929\u5730\u5341\u4e8c\u574a\"},{\"id\":\"189\",\"classname\":\"\u4e2d\u4e1c\u533a\u57df\"}]},{\"id\":\"130\",\"classname\":\"\u51c0\u6708\u5f00\u53d1\u533a\",\"next\":[{\"id\":\"190\",\"classname\":\"\u51c0\u6708\u6f6d\"},{\"id\":\"191\",\"classname\":\"\u4e16\u8363\u8def\"},{\"id\":\"192\",\"classname\":\"\u51c0\u6708\u5927\u5b66\u57ce\"},{\"id\":\"193\",\"classname\":\"\u957f\u5f71\u4e16\u7eaa\u57ce\"},{\"id\":\"194\",\"classname\":\"\u51c0\u6708\u516c\u56ed\"}]},{\"id\":\"131\",\"classname\":\"\u6c7d\u8f66\u5f00\u53d1\u533a\",\"next\":[{\"id\":\"195\",\"classname\":\"\u4e1c\u98ce\u5927\u8857\"},{\"id\":\"196\",\"classname\":\"\u8f66\u767e\"},{\"id\":\"197\",\"classname\":\"\u98de\u8dc3\u5e7f\u573a\"},{\"id\":\"198\",\"classname\":\"\u5929\u8302\u57ce\u4e2d\u592e\"}]},{\"id\":\"132\",\"classname\":\"\u53cc\u9633\u533a\",\"next\":[{\"id\":\"199\",\"classname\":\"\u53cc\u9633\u5927\u8857\"},{\"id\":\"200\",\"classname\":\"\u5546\u8d38\u57ce\"},{\"id\":\"201\",\"classname\":\"\u9e7f\u57ce\u660e\u73e0\"},{\"id\":\"202\",\"classname\":\"\u5cad\u4e0b\"},{\"id\":\"203\",\"classname\":\"\u660e\u6c5f\u8857\"}]},{\"id\":\"133\",\"classname\":\"\u5fb7\u60e0\u5e02\",\"next\":[{\"id\":\"204\",\"classname\":\"\u80dc\u5229\u8857\u9053\"},{\"id\":\"205\",\"classname\":\"\u5efa\u8bbe\u8857\u9053\"},{\"id\":\"206\",\"classname\":\"\u60e0\u53d1\u8857\u9053\"},{\"id\":\"207\",\"classname\":\"\u590f\u5bb6\u5e97\u8857\u9053\"},{\"id\":\"208\",\"classname\":\"\u5927\u9752\u5634\u9547\"},{\"id\":\"209\",\"classname\":\"\u90ed\u5bb6\u9547\"},{\"id\":\"210\",\"classname\":\"\u677e\u82b1\u6c5f\u9547\"},{\"id\":\"211\",\"classname\":\"\u8fbe\u5bb6\u6c9f\u9547\"},{\"id\":\"212\",\"classname\":\"\u5927\u623f\u8eab\u9547\"},{\"id\":\"213\",\"classname\":\"\u5c94\u8def\u53e3\u9547\"},{\"id\":\"214\",\"classname\":\"\u6731\u57ce\u5b50\u9547\"},{\"id\":\"215\",\"classname\":\"\u5e03\u6d77\u9547\"},{\"id\":\"216\",\"classname\":\"\u5929\u53f0\u9547\"},{\"id\":\"217\",\"classname\":\"\u83dc\u56ed\u5b50\u9547\"},{\"id\":\"218\",\"classname\":\"\u540c\u592a\u4e61\"},{\"id\":\"219\",\"classname\":\"\u8fb9\u5c97\u4e61\"},{\"id\":\"220\",\"classname\":\"\u4e94\u53f0\u4e61\"},{\"id\":\"221\",\"classname\":\"\u671d\u9633\u4e61\"},{\"id\":\"222\",\"classname\":\"\u5fb7\u60e0\u5e02\u5176\u4ed6\"}]},{\"id\":\"134\",\"classname\":\"\u4e5d\u53f0\u5e02\",\"next\":[{\"id\":\"223\",\"classname\":\"\u4e5d\u53f0\u8857\u9053\"},{\"id\":\"224\",\"classname\":\"\u8425\u57ce\u8857\u9053\"},{\"id\":\"225\",\"classname\":\"\u4e5d\u90ca\u8857\u9053\"},{\"id\":\"226\",\"classname\":\"\u897f\u8425\u57ce\u8857\u9053\"},{\"id\":\"227\",\"classname\":\"\u6c90\u77f3\u6cb3\u9547\"},{\"id\":\"228\",\"classname\":\"\u57ce\u5b50\u8857\u9547\"},{\"id\":\"229\",\"classname\":\"\u5176\u5854\u6728\u9547\"},{\"id\":\"230\",\"classname\":\"\u4e0a\u6cb3\u6e7e\u9547\"},{\"id\":\"231\",\"classname\":\"\u571f\u4eec\u5cad\u9547\"},{\"id\":\"232\",\"classname\":\"\u82c7\u5b50\u6c9f\u9547\"},{\"id\":\"233\",\"classname\":\"\u5174\u9686\u9547\"},{\"id\":\"234\",\"classname\":\"\u7eaa\u5bb6\u9547\"},{\"id\":\"235\",\"classname\":\"\u6ce2\u6ce5\u6cb3\u9547\"},{\"id\":\"236\",\"classname\":\"\u80e1\u5bb6\u56de\u65cf\u4e61\"},{\"id\":\"237\",\"classname\":\"\u83bd\u5361\u6ee1\u65cf\u4e61\"},{\"id\":\"238\",\"classname\":\"\u4e5d\u53f0\u5e02\u5176\u4ed6\"}]},{\"id\":\"135\",\"classname\":\"\u6986\u6811\u5e02\",\"next\":[{\"id\":\"239\",\"classname\":\"\u534e\u660c\u8857\u9053\"},{\"id\":\"240\",\"classname\":\"\u6b63\u9633\u8857\u9053\"},{\"id\":\"241\",\"classname\":\"\u57f9\u82f1\u8857\u9053\"},{\"id\":\"242\",\"classname\":\"\u57ce\u90ca\u8857\u9053\"},{\"id\":\"243\",\"classname\":\"\u4e94\u68f5\u6811\u9547\"},{\"id\":\"244\",\"classname\":\"\u5f13\u68da\u9547\"},{\"id\":\"245\",\"classname\":\"\u95f5\u5bb6\u9547\"},{\"id\":\"246\",\"classname\":\"\u5927\u5761\u9547\"},{\"id\":\"247\",\"classname\":\"\u9ed1\u6797\u9547\"},{\"id\":\"248\",\"classname\":\"\u571f\u6865\u9547\"},{\"id\":\"249\",\"classname\":\"\u65b0\u7acb\u9547\"},{\"id\":\"250\",\"classname\":\"\u5927\u5cad\u9547\"},{\"id\":\"251\",\"classname\":\"\u4e8e\u5bb6\u9547\"},{\"id\":\"252\",\"classname\":\"\u6cd7\u6cb3\u9547\"},{\"id\":\"253\",\"classname\":\"\u516b\u53f7\u9547\"},{\"id\":\"254\",\"classname\":\"\u5218\u5bb6\u9547\"},{\"id\":\"255\",\"classname\":\"\u79c0\u6c34\u9547\"},{\"id\":\"256\",\"classname\":\"\u4fdd\u5bff\u9547\"},{\"id\":\"257\",\"classname\":\"\u65b0\u5e84\u9547\"},{\"id\":\"258\",\"classname\":\"\u80b2\u6c11\u4e61\"},{\"id\":\"259\",\"classname\":\"\u7ea2\u661f\u4e61\"},{\"id\":\"260\",\"classname\":\"\u592a\u5b89\u4e61\"},{\"id\":\"261\",\"classname\":\"\u5148\u5cf0\u4e61\"},{\"id\":\"262\",\"classname\":\"\u9752\u5c71\u4e61\"},{\"id\":\"263\",\"classname\":\"\u5ef6\u548c\u671d\u9c9c\u65cf\u4e61\"},{\"id\":\"264\",\"classname\":\"\u6069\u80b2\u4e61\"},{\"id\":\"265\",\"classname\":\"\u57ce\u53d1\u4e61\"},{\"id\":\"266\",\"classname\":\"\u73af\u57ce\u4e61\"},{\"id\":\"267\",\"classname\":\"\u6986\u6811\u5e02\u5176\u4ed6\"}]},{\"id\":\"136\",\"classname\":\"\u519c\u5b89\u53bf\",\"next\":[{\"id\":\"268\",\"classname\":\"\u519c\u5b89\u9547\"},{\"id\":\"269\",\"classname\":\"\u4f0f\u9f99\u6cc9\u9547\"},{\"id\":\"270\",\"classname\":\"\u54c8\u62c9\u6d77\u9547\"},{\"id\":\"271\",\"classname\":\"\u9760\u5c71\u9547\"},{\"id\":\"272\",\"classname\":\"\u5f00\u5b89\u9547\"},{\"id\":\"273\",\"classname\":\"\u70e7\u9505\u9547\"},{\"id\":\"274\",\"classname\":\"\u9ad8\u5bb6\u5e97\u9547\"},{\"id\":\"275\",\"classname\":\"\u534e\u5bb6\u9547\"},{\"id\":\"276\",\"classname\":\"\u4e09\u76db\u7389\u9547\"},{\"id\":\"277\",\"classname\":\"\u5df4\u5409\u5792\u9547\"},{\"id\":\"278\",\"classname\":\"\u4e09\u5c97\u9547\"},{\"id\":\"279\",\"classname\":\"\u524d\u5c97\u4e61\"},{\"id\":\"280\",\"classname\":\"\u9f99\u738b\u4e61\"},{\"id\":\"281\",\"classname\":\"\u4e07\u987a\u4e61\"},{\"id\":\"282\",\"classname\":\"\u6768\u6811\u6797\u4e61\"},{\"id\":\"283\",\"classname\":\"\u6c38\u5b89\u4e61\"},{\"id\":\"284\",\"classname\":\"\u9752\u5c71\u53e3\u4e61\"},{\"id\":\"285\",\"classname\":\"\u9ec4\u9c7c\u5708\u4e61\"},{\"id\":\"286\",\"classname\":\"\u65b0\u519c\u4e61\"},{\"id\":\"287\",\"classname\":\"\u4e07\u91d1\u5854\u4e61\"},{\"id\":\"288\",\"classname\":\"\u5c0f\u57ce\u5b50\u4e61\"},{\"id\":\"289\",\"classname\":\"\u519c\u5b89\u53bf\u5176\u4ed6\"}]}]}";
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
						groups.add(job1
								.getString("classname"));
						JSONArray jArray2 =
								job1.getJSONArray("next");
						name = new String[jArray2.length()];
						for(int m = 0,n = jArray2.length();m < n;m++){
							JSONObject job2 =
									jArray2.optJSONObject(m);
							name[m] =
									job2.getString("classname");
							childrenItem
									.put(job2
											.getString("classname"),
											job2.getString("id"));
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
				
				initView();
				initVaule();
				initListener();
				if(lat == 0 && lon == 0){
					Log.e("location", "null");
					new ListAsync().execute(title, null, null, null, null,null);
				}else{
					Log.e("location", lat + " + " + lon);
					new ListAsync().execute(title, null, null, null, lat, lon);
				}
			}else{
				progressDialog.dismiss();
			}

		}

	}
	
	class ListAsync extends AsyncTask<Object, Void, String>{

		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute(){
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String doInBackground(Object... params){
			// TODO Auto-generated method stub
			String code = "";
			String url = Config.MS_MAIN_LIST_URL;
			if(params[0] != null){
				url += ("&n_title=" + params[0]);
			}
			if(params[1] != null){
				url += ("&w_qy=" + params[1]);
			}
			if(params[2] != null){
				url += ("&w_jl=" + params[2]);
			}
			if(params[3] != null){
				url += ("&w_order=" + params[3]);
			}
			if(params[4] != null){
				url += ("&lat1=" + params[4]);
			}
			if(params[5] != null){
				url += ("&lng1=" + params[5]);
			}
			Log.e("url", url);
			
			
			msList =
					new ArrayList<HashMap<String, String>>();
			
			try{
				String data =
						new Tools()
								.getURL(url);
				System.out.println(data);
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
						hashMap.put("uid",
								job1.getString("uid"));	
						hashMap.put("w_title",
								job1.getString("w_title"));
						hashMap.put("n_title",
								job1.getString("n_title"));
						hashMap.put("n_pic", job1
								.getString("n_pic"));
						hashMap.put("n_dj",
								job1.getString("n_dj"));
						hashMap.put("w_dz",
								job1.getString("w_dz"));
						hashMap.put("w_dh",
								job1.getString("w_dh"));
						hashMap.put("w_longitude", job1
								.getString("w_longitude"));
						hashMap.put("w_latitude", job1.getString("w_latitude"));
						hashMap.put("w_fs",
								job1.getString("w_fs"));
						hashMap.put("w_jj",
								job1.getString("w_jj"));

						msList.add(hashMap);
					}
				}
			}catch(JSONException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return (String) params[1];
		}
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String result){
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(progressDialog != null){
				progressDialog.dismiss();
			}
			if(result == null){
				msAdapter =
						new MeiShiMainListAdapter(
								MeiShiMainListPage.this,
								msList);
				lv.setAdapter(msAdapter);

			}else{
				msAdapter.notifyDataSetChanged();
			}
			lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(
						AdapterView<?> parent, View view,
						int position, long id){
					// TODO Auto-generated method stub
					Intent intent = new Intent(MeiShiMainListPage.this, CanTingDetailActivity.class);
					intent.putExtra("uid", msList.get(position).get("uid"));
					startActivity(intent);
				}});
		}
	}
	
	private void initView(){

		expandTabView =
				(ExpandTabView) findViewById(R.id.baofang_main_list_page_expandtab_view);
		viewMiddle =
				new ViewMiddle(this, groups, childrenItem,
						children);
		viewRight = new ViewRight(this);

		viewMiddle.setShowString("全城");
		viewRight.setShowText("智能排序");

	}

	private void initVaule(){

		mViewArray.add(viewMiddle);
		mViewArray.add(viewRight);
		ArrayList<String> mTextArray =
				new ArrayList<String>();
		mTextArray.add("全城");
		mTextArray.add("智能排序");
		Log.e("mViewArray", ""+mViewArray.size());
		expandTabView.setValue(mTextArray, mViewArray);
		expandTabView.setTitle(viewMiddle.getShowText(), 0);
		expandTabView.setTitle(viewRight.getShowText(), 1);

	}

	private void initListener(){
		viewMiddle
				.setOnSelectListener(new ViewMiddle.OnSelectListener(){

					@Override
					public void getValue(String showText){

						onRefresh(viewMiddle, showText);

					}
				});

		viewRight
				.setOnSelectListener(new ViewRight.OnSelectListener(){

					@Override
					public void getValue(String distance,
							String showText){
						onRefresh(viewRight, showText);
					}
				});

	}

	private void onRefresh(View view, String showText){

		expandTabView.onPressBack();
		int position = getPositon(view);
		if(position >= 0
				&& !expandTabView.getTitle(position)
						.equals(showText)){
			expandTabView.setTitle(showText, position);
		}
		Toast.makeText(MeiShiMainListPage.this, showText,
				Toast.LENGTH_SHORT).show();

	}

	private int getPositon(View tView){
		for(int i = 0;i < mViewArray.size();i++){
			if(mViewArray.get(i) == tView){
				return i;
			}
		}
		return -1;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed(){
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
}
