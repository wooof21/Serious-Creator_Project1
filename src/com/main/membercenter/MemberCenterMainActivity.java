/**
 * 
 */
package com.main.membercenter;

import java.io.File;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.chihuoshijian.R;
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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author Liming Chu
 * 
 * @param
 * @return
 */
public class MemberCenterMainActivity extends Activity
		implements OnClickListener{

	private ImageView		head;
	private TextView		name;
	private TextView		clectCount;
	private TextView		couponCount;
	private LinearLayout	all;
	private RelativeLayout	done;
	private TextView		doneCount;
	private RelativeLayout	hold;
	private TextView		holdCount;
	private RelativeLayout	toMark;
	private TextView		toMarkCount;
	private LinearLayout	bfOrder;
	private LinearLayout	pCenter;
	private LinearLayout	myClect_ll;
	private LinearLayout	myCoupon_ll;

	private ProgressDialog	progressDialog;
	private String			id;
	private String			token;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zhongxin_main);
		prepareView();
		preperImageLoader();

//		id = getIntent().getExtras().getString("openId");
//		token = getIntent().getExtras().getString("token");
//
//		new InfoAsync().execute(token, id);
	}

	private void prepareView(){
		head =
				(ImageView) findViewById(R.id.zhongxin_main_round_iv);
		name = (TextView) findViewById(R.id.zhongxin_name);
		clectCount =
				(TextView) findViewById(R.id.zhongxin_my_clect_count);
		couponCount =
				(TextView) findViewById(R.id.zhongxin_my_coupon_count);
		all =
				(LinearLayout) findViewById(R.id.zhongxin_all_order);
		done =
				(RelativeLayout) findViewById(R.id.zhongxin_main_order_done_rl);
		doneCount =
				(TextView) findViewById(R.id.zhongxin_done_tv);
		hold =
				(RelativeLayout) findViewById(R.id.zhong_xin_hold_rl);
		holdCount =
				(TextView) findViewById(R.id.zhongxin_hold_tv);
		toMark =
				(RelativeLayout) findViewById(R.id.zhongxin_tomark_rl);
		toMarkCount =
				(TextView) findViewById(R.id.zhongxin_tomark_tv);
		bfOrder =
				(LinearLayout) findViewById(R.id.zhongxin_baofang_order);
		pCenter =
				(LinearLayout) findViewById(R.id.zhongxin_personal_center);
		myClect_ll =
				(LinearLayout) findViewById(R.id.zhongxin_my_clect_ll);
		myCoupon_ll =
				(LinearLayout) findViewById(R.id.zhongxin_my_coupon_ll);

		all.setOnClickListener(this);
		done.setOnClickListener(this);
		hold.setOnClickListener(this);
		toMark.setOnClickListener(this);
		bfOrder.setOnClickListener(this);
		pCenter.setOnClickListener(this);
		myClect_ll.setOnClickListener(this);
		myCoupon_ll.setOnClickListener(this);
	}

	@Override
	public void onBackPressed(){
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

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
							MemberCenterMainActivity.this,
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
				msg.what = 2;
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
			if(result != null){
				new DetailAsync().execute();
			}
		}

	}

	class DetailAsync extends AsyncTask<Void, Void, Void>{

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Void doInBackground(Void... params){
			// TODO Auto-generated method stub
			String url =
					Config.VIP_CENTER_MAIN_DETAIL_URL
							+ "19";
			Log.e("url", url);
			String data = new Tools().getURL(url);
			System.out.println(data);

			try{
				JSONObject job = new JSONObject(data);
				String code = job.getString("code");
				JSONObject job1 =
						job.getJSONObject("result");
				HashMap<String, String> hashMap =
						new HashMap<String, String>();
				hashMap.put("u_wxname",
						job1.getString("u_wxname"));
				hashMap.put("u_pic",
						job1.getString("u_pic"));
				hashMap.put("id", job1.getString("id"));
				hashMap.put("u_wid",
						job1.getString("u_wid"));
				hashMap.put("u_sc", job1.getString("u_sc"));
				hashMap.put("u_yhj",
						job1.getString("u_yhj"));
				hashMap.put("u_dd_ywc",
						job1.getString("u_dd_ywc"));
				hashMap.put("u_dd_wwc",
						job1.getString("u_dd_wwc"));
				hashMap.put("u_dd_dpj",
						job1.getString("u_dd_dpj"));

				Message msg = handler.obtainMessage();
				msg.obj = hashMap;
				msg.what = 1;
				handler.sendMessage(msg);
			}catch(JSONException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Void result){
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
		}
	}

	private Handler	handler	= new Handler(){

								@Override
								public void handleMessage(
										Message msg){
									// TODO Auto-generated method stub
									super.handleMessage(msg);
									switch(msg.what){
										case 1:
											HashMap<String, String> hashMap =
													(HashMap<String, String>) msg.obj;
											name.setText(hashMap
													.get("u_wxname"));
											clectCount
													.setText(hashMap
															.get("u_sc"));
											couponCount
													.setText(hashMap
															.get("u_yhj"));
											String ywc =
													hashMap.get("u_dd_ywc");
											String wwc =
													hashMap.get("u_dd_wwc");
											String dpj =
													hashMap.get("u_dd_dpj");
											if(ywc.equals("0")){
												doneCount
														.setVisibility(View.GONE);
											}else{
												doneCount
														.setText(ywc);
											}
											if(wwc.equals("0")){
												holdCount
														.setVisibility(View.GONE);
											}else{
												holdCount
														.setText(wwc);
											}
											if(dpj.equals("0")){
												toMarkCount
														.setVisibility(View.GONE);
											}else{
												toMarkCount
														.setText(dpj);
											}
										break;
										case 2:
											HashMap<String, String> hashMap2 =
													(HashMap<String, String>) msg.obj;
											name.setText(hashMap2
													.get("name"));
											ImageLoader
													.getInstance()
													.displayImage(
															hashMap2.get("headimgurl"),
															head);
										break;
										default:
										break;
									}
								}

							};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v){
		// TODO Auto-generated method stub
		Intent intent = null;
		switch(v.getId()){
			case R.id.zhongxin_all_order:
				intent =
						new Intent(
								MemberCenterMainActivity.this,
								VipCenterMyOrder.class);
				intent.putExtra("which", "all");
			break;
			case R.id.zhongxin_main_order_done_rl:
				intent =
						new Intent(
								MemberCenterMainActivity.this,
								VipCenterMyOrder.class);
				intent.putExtra("which", "1");
			break;
			case R.id.zhong_xin_hold_rl:
				intent =
						new Intent(
								MemberCenterMainActivity.this,
								VipCenterMyOrder.class);
				intent.putExtra("which", "0");
			break;
			case R.id.zhongxin_tomark_rl:
				intent =
						new Intent(
								MemberCenterMainActivity.this,
								VipCenterMyOrder.class);
				intent.putExtra("which", "2");
			break;
			case R.id.zhongxin_baofang_order:
				intent =
						new Intent(
								MemberCenterMainActivity.this,
								VipCenterMyBaoFangOrder.class);
			break;
			case R.id.zhongxin_personal_center:
				intent =
						new Intent(
								MemberCenterMainActivity.this,
								VipTimeLine.class);
				intent.putExtra("openId", id);
				intent.putExtra("token", token);
			break;
			case R.id.zhongxin_my_clect_ll:
				intent = new Intent(MemberCenterMainActivity.this, CommentSubmitActivity.class);
			break;
			case R.id.zhongxin_my_coupon_ll:

			break;
			default:
			break;
		}
		startActivity(intent);
	}

	private void preperImageLoader(){

		/******************* 配置ImageLoder ***********************************************/
		File cacheDir =
				StorageUtils.getOwnCacheDirectory(
						MemberCenterMainActivity.this,
						"imageloader/Cache");

		ImageLoaderConfiguration config =
				new ImageLoaderConfiguration.Builder(
						MemberCenterMainActivity.this)
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
