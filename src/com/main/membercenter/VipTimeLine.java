/**
 * 
 */
package com.main.membercenter;

import java.io.File;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.chihuoshijian.R;
import com.main.membercenter.MemberCenterMainActivity.DetailAsync;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tools.Config;
import com.tools.Tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

/**
 * @author Liming Chu
 * 
 * @param
 * @return
 */
public class VipTimeLine extends Activity{

	private ImageView	head;
	private EditText name;
	private EditText	phone;
	private Spinner		sex;
	private Spinner		province;
	private Spinner		city;
	
	private ProgressDialog	progressDialog;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_line);
		prepareView();
		preperImageLoader();
		String id =
				getIntent().getExtras().getString("openId");
		String token =
				getIntent().getExtras().getString("token");
		
		new InfoAsync().execute(token, id);
	}

	private void prepareView(){
		head =
				(ImageView) findViewById(R.id.time_line_round_iv);
		name = (EditText)findViewById(R.id.time_line_et1);
		phone = (EditText) findViewById(R.id.time_line_et2);
		sex =
				(Spinner) findViewById(R.id.time_line_spinner1);
		province =
				(Spinner) findViewById(R.id.time_line_spinner2);
		city =
				(Spinner) findViewById(R.id.time_line_spinner3);
	}

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg){
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			HashMap<String, String> hashMap = (HashMap<String, String>) msg.obj;
			name.setText(hashMap.get("name"));
			if(hashMap.get("sex").toString().equals("1")){
				sex.setSelection(0);
			}else{
				sex.setSelection(1);
			}
			
			ImageLoader.getInstance().displayImage(hashMap.get("headimgurl"), head);
		}
		
	};
	class InfoAsync
			extends
			AsyncTask<Object, Void, HashMap<String, String>>{

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
					ProgressDialog.show(
							VipTimeLine.this,
							null, null);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected HashMap<String, String> doInBackground(
				Object... params){
			// TODO Auto-generated method stub
			String url =
					Config.WEIXIN_GET_PERSONAL_INFO_URL
							+ params[0] + "&openid="
							+ params[1];
			Log.e("url", url);
			String data = new Tools().getURL(url);
			System.out.println(data);
			HashMap<String, String> hashMap =
					new HashMap<String, String>();
			try{
				JSONObject job = new JSONObject(data);

				hashMap.put("name",
						job.getString("nickname"));
				hashMap.put("sex", job.getString("sex"));
				hashMap.put("province",
						job.getString("province"));
				hashMap.put("city", job.getString("city"));
				hashMap.put("headimgurl",
						job.getString("headimgurl"));

				Message msg = handler.obtainMessage();
				msg.obj = hashMap;
				handler.sendMessage(msg);

			}catch(JSONException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return hashMap;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(
				HashMap<String, String> result){
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
		}

	}
	
	private void preperImageLoader(){

		/******************* 配置ImageLoder ***********************************************/
		File cacheDir =
				StorageUtils.getOwnCacheDirectory(
						VipTimeLine.this,
						"imageloader/Cache");

		ImageLoaderConfiguration config =
				new ImageLoaderConfiguration.Builder(
						VipTimeLine.this)
						.denyCacheImageMultipleSizesInMemory()
						.discCache(
								new UnlimitedDiscCache(
										cacheDir))// 自定义缓存路径
						.build();// 开始构建

		DisplayImageOptions options =
				new DisplayImageOptions.Builder()
						.cacheInMemory()
						.cacheOnDisc()
						.imageScaleType(
								ImageScaleType.IN_SAMPLE_INT)
						.build();

		ImageLoader.getInstance().init(config);// 全局初始化此配置
		/*********************************************************************************/
	}
}
