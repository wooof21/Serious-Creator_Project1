package com.example.fatass;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import com.baiduapi.map.BaiDuMap;
import com.chihuoshijian.R;
import com.chihuoshijian.adapter.CanTingListAdapter;
import com.chihuoshijian.adapter.MainPageListAdapter;
import com.chihuoshijian.login.LogInActivity;
import com.main.baofang.BaoFangMainListPage;
import com.main.baofang.BaoFangOrderSuccess;
import com.main.canting.CanTingDetailActivity;
import com.main.canting.CanTingMainListPage;
import com.main.search.SearchActivity;
import com.order.OrderDetailActivity;
import com.order.OrderSuccessActivity;
import com.tools.Config;
import com.tools.ScrollListView;
import com.tools.Tags;
import com.tools.Tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.location.GpsStatus.NmeaListener;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements
	OnClickListener{

	/** view */
	private ImageView			bfIv;				// 搜包房按钮
	private ImageView			ctIv;				// 搜餐厅按钮
	private ImageView			msIv;				// 搜美食按钮
	private TextView			rmTv;				// 热门搜索中的单个textview
	private LinearLayout		search;			// 搜索按钮
	private ListView			lv;
	private ProgressDialog		progressDialog;
	private MainPageListAdapter	mpAdapter;

	private boolean				isFristTime	= true;
	private static MainActivity	instance;

	public static MainActivity getInstance(){
		
		if(instance == null){
			instance = new MainActivity();
		}
		return instance;
	}

	public String[] getTags(){
		return tags;
	}

	/** 标签之间的间距 px */
	final int									itemMargins	=
																	30;

	/** 标签的行间距 px */
	final int									lineMargins	=
																	15;

	private ViewGroup							container	=
																	null;

	private String[]							tags;						;
	private ArrayList<HashMap<String, String>>	tjList;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		prepareView();

		container =
				(ViewGroup) findViewById(R.id.container);

		new SearchAsync().execute();
	}

	/**
	 * 初始化控件
	 * */
	private void prepareView(){
		bfIv =
				(ImageView) findViewById(R.id.main_sou_baofang);
		ctIv =
				(ImageView) findViewById(R.id.main_sou_canting);
		msIv =
				(ImageView) findViewById(R.id.main_sou_meishi);
		search =
				(LinearLayout) findViewById(R.id.main_search_ll);
		lv = (ListView) findViewById(R.id.main_lv);

		bfIv.setOnClickListener(this);
		ctIv.setOnClickListener(this);
		msIv.setOnClickListener(this);
		// search.setOnClickListener(this);

	}

	class SearchAsync extends
		AsyncTask<Void, Void, String[]>{

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
					ProgressDialog.show(MainActivity.this, null, null);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String[] doInBackground(Void... params){
			// TODO Auto-generated method stub
			System.out.println(Config.MAIN_SEARCH_LIST_URL);
			String data =
					new Tools().getURL(Config.MAIN_SEARCH_LIST_URL);
			// String data =
			// "{\"code\":1,\"result\":[{\"n_title\":\"\u5df4\u897f\u70e4\u8089\"},"
			// + "{\"n_title\":\"\u5982\u4e00\u574a\u8c46\u635e\"},"
			// + "{\"n_title\":\"\u9505\u7172\u8089\"},"
			// + "{\"n_title\":\"\u5927\u5510\u70e4\u8089\"},"
			// + "{\"n_title\":\"\u91d1\u724c\u70e4\u573a\"},"
			// + "{\"n_title\":\"\u7f8a\u874e\u5b50\"},"
			// + "{\"n_title\":\"\u6d77\u5e95\u635e\"},"
			// + "{\"n_title\":\"\u767e\u5408\u996d\u5305\"},"
			// + "{\"n_title\":\"\u5c0f\u5c0f\u706b\u9505\"}]}";
			try{
				JSONObject job = new JSONObject(data);
				String code = job.getString("code");
				if(code.equals("1")){
					JSONArray jArray =
							job.getJSONArray("result");
					tags = new String[jArray.length()];
					for(int i = 0,j = jArray.length();i < j;i++){
						JSONObject job1 =
								jArray.optJSONObject(i);
						tags[i] = job1.getString("n_title");
					}
					Tags.getInstance().setTags(tags);
				}
			}catch(JSONException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return tags;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String[] result){
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			new ListAsync().execute();
		}
	}

	class ListAsync
		extends
		AsyncTask<Object, Void, ArrayList<HashMap<String, String>>>{

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
		protected ArrayList<HashMap<String, String>>
			doInBackground(Object... params){
			// TODO Auto-generated method stub
			String code = "";
			String url = Config.MAIN_REC_LIST_URL;
			Log.e("url", url);
			String data = new Tools().getURL(url);
			// String data =
			// "{\"code\":1,\"result\":[{\"id\":\"8\",\"w_title\":\"\u957f\u6625\u6d6a\u6f2b\u559c\u90fd1\",\"w_logo_pic\":\"/upload/23/2015-05/27/556515cc4c6242126.jpg\",\"w_jj\":\"40-50\u5143\",\"w_dz\":\"\u957f\u6625\u5e02\u671d\u9633\u533a\u5de5\u519c\u5927\u8def1726\u53f7\uff08\u767e\u8111\u6c47\u659c\u5bf9\u9762\uff09\",\"w_dh\":\"0431-85611555\",\"w_longitude\":\"125.31173\",\"w_latitude\":\"43.872483\",\"w_bfs\":60,\"w_fs\":\"3.0\",\"uid\":\"23\"},{\"id\":\"16\",\"w_title\":\"\u4e1c\u5317\u864e\u6469\u6258\u8f66\u961f\",\"w_logo_pic\":\"/upload/32/2015-06/02/556d539886a5b8116.jpg\",\"w_jj\":\"61-70\u5143\",\"w_dz\":\"\u5409\u6797\u7701\u957f\u6625\u5e02\u536b\u661f\u8def\u4e0e\u536b\u5149\u8857\u4ea4\u6c47\u5904\",\"w_dh\":\"13674308311\",\"w_longitude\":\"125.322956\",\"w_latitude\":\"43.838811\",\"w_bfs\":60,\"w_fs\":\"3.0\",\"uid\":\"32\"},{\"id\":\"35\",\"w_title\":\"\u5fae\u542f\u5e73\u53f0\u9ed8\u8ba4\u6a21\u677f\",\"w_logo_pic\":\"/upload/2014-02/11/52f9cf8a6e0cc6918.png\",\"w_jj\":\"61-70\u5143\",\"w_dz\":\"\u5409\u6797\u7701\u957f\u6625\u5e02\u671d\u9633\u533a\u98de\u8dc3\u8def\",\"w_dh\":\"043189642189\",\"w_longitude\":\"125.281975\",\"w_latitude\":\"43.823242\",\"w_bfs\":80,\"w_fs\":\"4.0\",\"uid\":\"65\"},{\"id\":\"29\",\"w_title\":\"\u56fd\u71d5\u5802\",\"w_logo_pic\":\"/upload/58/2014-08/18/53f153390a5879513.jpg\",\"w_jj\":\"100\u5143\u4ee5\u4e0a\",\"w_dz\":\"\u6d59\u6c5f\u7701\u676d\u5dde\u5e02\u4e0a\u57ce\u533a\u79ef\u5584\u574a\u5df710\u53f7\",\"w_dh\":\"057156196683\",\"w_longitude\":\"120.175468\",\"w_latitude\":\"30.254697\",\"w_bfs\":125,\"w_fs\":\"6.2\",\"uid\":\"58\"},{\"id\":\"85\",\"w_title\":\"\u5343\u821f\u542f\u822a\u79d1\u6280\u6709\u9650\u516c\u53f8\",\"w_logo_pic\":\"/upload/125/2014-07/29/53d7239409b466645.gif\",\"w_jj\":\"40-50\u5143\",\"w_dz\":\"\u5409\u6797\u7701\u957f\u6625\u5e02\u671d\u9633\u533a\u897f\u5b89\u5927\u8def1596\u53f721\u697c2108\u5ba4\",\"w_dh\":\"0431-81931300\",\"w_longitude\":\"125.312118\",\"w_latitude\":\"43.899421\",\"w_bfs\":60,\"w_fs\":\"3.0\",\"uid\":\"125\"}]}";

			System.out.println(data);
			tjList =
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
						hashMap.put("w_fs", job1.getString("w_fs"));
						tjList.add(hashMap);
					}
				}
			}catch(JSONException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return tjList;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected
			void
			onPostExecute(ArrayList<HashMap<String, String>> result){
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			Log.e("tlist size", "" + tjList.size());
			if(tjList.size() != 0){
				mpAdapter =
						new MainPageListAdapter(
							MainActivity.this, tjList);
				lv.setAdapter(mpAdapter);
				ScrollListView.setListViewHeightBasedOnChildren(lv);
			}
			lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void
					onItemClick(AdapterView<?> parent,
							View view,
							int position,
							long id){
					// TODO Auto-generated method stub
					Intent intent =
							new Intent(MainActivity.this,
								CanTingDetailActivity.class);
					intent.putExtra("uid", tjList.get(position).get("uid"));
					startActivity(intent);
				}
			});
		}

	}

	/**
	 * 首页热门搜索， 自动换行linearlayout
	 * */
	@Override
	public void onWindowFocusChanged(boolean hasFocus){
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus && isFristTime){
			isFristTime = false;
			final int containerWidth =
					container.getMeasuredWidth()
							- container.getPaddingRight()
							- container.getPaddingLeft();

			final LayoutInflater inflater =
					getLayoutInflater();

			/** 用来测量字符的宽度 */
			final Paint paint = new Paint();
			final TextView textView =
					(TextView) inflater.inflate(R.layout.remen_textview, null);
			final int itemPadding =
					textView.getCompoundPaddingLeft()
							+ textView.getCompoundPaddingRight();
			final LinearLayout.LayoutParams tvParams =
					new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
			tvParams.setMargins(0, 0, itemMargins, 0);

			paint.setTextSize(textView.getTextSize());

			LinearLayout layout = new LinearLayout(this);
			layout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
			layout.setOrientation(LinearLayout.HORIZONTAL);
			container.addView(layout);

			final LinearLayout.LayoutParams params =
					new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT);
			params.setMargins(0, lineMargins, 0, 0);

			/** 一行剩下的空间 **/
			int remainWidth = containerWidth;

			for(int i = 0;i < tags.length;++i){
				final String text = tags[i];
				final float itemWidth =
						paint.measureText(text)
								+ itemPadding;
				if(remainWidth > itemWidth){
					addItemView(inflater, layout, tvParams, text, i);
				}else{
					resetTextViewMarginsRight(layout);
					layout = new LinearLayout(this);
					layout.setLayoutParams(params);
					layout.setOrientation(LinearLayout.HORIZONTAL);

					/** 将前面那一个textview加入新的一行 */
					addItemView(inflater, layout, tvParams, text, i);
					container.addView(layout);
					remainWidth = containerWidth;
				}
				remainWidth =
						(int) (remainWidth - itemWidth + 0.5f)
								- itemMargins;
			}
			resetTextViewMarginsRight(layout);
		}
	}

	/**
	 * 将每行最后一个textview的MarginsRight去掉
	 * */
	private void
		resetTextViewMarginsRight(ViewGroup viewGroup){
		final TextView tempTextView =
				(TextView) viewGroup.getChildAt(viewGroup.getChildCount() - 1);
		tempTextView.setLayoutParams(new LinearLayout.LayoutParams(
			LayoutParams.WRAP_CONTENT,
			LayoutParams.WRAP_CONTENT));
	}

	/**
	 * 将view添加到viewgroup中
	 * */
	private void addItemView(LayoutInflater inflater,
			ViewGroup viewGroup,
			LayoutParams params,
			String text,
			int tag){
		rmTv =
				(TextView) inflater.inflate(R.layout.remen_textview, null);
		rmTv.setTag(tag);
		rmTv.setText(text);
		rmTv.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				TextView tv = (TextView) v;
				Toast.makeText(MainActivity.this, tv.getText().toString(), Toast.LENGTH_LONG).show();
				// Intent intent1 = new Intent(MainActivity.this,
				// SearchActivity.class);
				// intent1.putExtra("tags", tags);
				// startActivity(intent1);
			}
		});
		viewGroup.addView(rmTv, params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v){
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.main_sou_baofang:
				Intent intent =
						new Intent(MainActivity.this,
							BaoFangMainListPage.class);
				startActivity(intent);
			break;
			// case R.id.main_search_ll:
			// Intent intent1 = new Intent(MainActivity.this,
			// SearchActivity.class);
			// intent1.putExtra("tags", tags);
			// startActivity(intent1);
			// break;
			case R.id.main_sou_canting:
				if(new Tools().getInternet(MainActivity.this)){
					Intent intent2 =
							new Intent(MainActivity.this,
								CanTingMainListPage.class);
					startActivity(intent2);
				}else{
					Toast.makeText(MainActivity.this, "未检测到网络，请重试！", Toast.LENGTH_LONG).show();
				}

			break;
			case R.id.main_sou_meishi:
				Intent intent3 =
						new Intent(MainActivity.this,
							SearchActivity.class);
				intent3.putExtra("tags", tags);
				startActivity(intent3);
			// Intent intent3 = new Intent(MainActivity.this,
			// LogInActivity.class);
			// startActivity(intent3);
			break;
			default:
			break;
		}

	}

}
