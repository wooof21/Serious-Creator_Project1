/**
 * 
 */
package com.main.canting;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
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
import com.baidu.mapapi.map.BaiduMap;
import com.chihuoshijian.R;
import com.chihuoshijian.adapter.CookingRecomListAdapter;
import com.main.baofang.BaoFangReserveInfo;
import com.main.baofang.DishOrderActivity;
import com.main.meishi.MeiShiCommentMain;
import com.main.meishi.MeiShiMainListPage;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tools.Config;
import com.tools.ScrollListView;
import com.tools.Tools;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Liming Chu
 * 
 * @param
 * @return
 */
public class CanTingDetailActivity extends Activity
	implements OnClickListener{

	private TextView							name;
	private RatingBar							rb;
	private TextView							rating;
	private TextView							average;
	private TextView							address;
	private TextView							distance;
	private ImageView							phone;
	private ImageView							map;
	private TextView							order;
	private TextView							reserve;
	private ListView							lv;
	private TextView							comName;
	private RatingBar							comRb;
	private TextView							date;
	private TextView							commend;
	private GridView							pics;
	private LinearLayout						checkAll;
	private ImageView							clect;

	private LocationClient						mLocationClient;
	private BDLocationListener					myListener	=
																	new MyLocationListener();
	private double								myLat		= 0,
			myLon = 0;
	private ProgressDialog						progressDialog;
	private BaiduMap							bMap;
	private ArrayList<HashMap<String, String>>	list;
	private ArrayList<HashMap<String, String>>	cList;
	private String								uid;
	protected float								rate;
	protected String							picAddr;
	private String								userId;
	private boolean								status;
	protected ArrayList<String>					picList;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baofang_order_select);
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

		uid = getIntent().getStringExtra("uid");

		userId = "19";

		new ClectStatusAsync().execute();

	}

	private void prepareView(){
		name =
				(TextView) findViewById(R.id.baofang_order_select_name);
		rb =
				(RatingBar) findViewById(R.id.baofang_order_select_tuijian_list_item_ratingbar);
		rating =
				(TextView) findViewById(R.id.baofang_order_select_tuijian_list_item_rating);
		average =
				(TextView) findViewById(R.id.baofang_order_select_average);
		address =
				(TextView) findViewById(R.id.baofang_order_select_address);
		distance =
				(TextView) findViewById(R.id.baofang_order_select_distance);
		phone =
				(ImageView) findViewById(R.id.baofang_order_select_phone);
		map =
				(ImageView) findViewById(R.id.baofang_order_select_map);
		order =
				(TextView) findViewById(R.id.baofang_order_select_order);
		reserve =
				(TextView) findViewById(R.id.baofang_order_select_reserve);
		lv =
				(ListView) findViewById(R.id.baofang_order_select_lv);
		comName =
				(TextView) findViewById(R.id.baofang_order_select_commend_name);
		comRb =
				(RatingBar) findViewById(R.id.baofang_order_select_commend_rb);
		date =
				(TextView) findViewById(R.id.baofang_order_select_commend_date);
		commend =
				(TextView) findViewById(R.id.baofang_order_select_commend);
		pics =
				(GridView) findViewById(R.id.baofang_order_select_commend_pic_gv);
		checkAll =
				(LinearLayout) findViewById(R.id.baofang_order_select_commend_check_all);
		clect =
				(ImageView) findViewById(R.id.baofang_order_select_operate);

		rb.setClickable(false);
		comRb.setClickable(false);
		phone.setOnClickListener(this);
		map.setOnClickListener(this);
		order.setOnClickListener(this);
		reserve.setOnClickListener(this);
		checkAll.setOnClickListener(this);
		clect.setOnClickListener(this);
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
			myLat = location.getLatitude();
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			myLon = location.getLongitude();

			Log.e("location", sb.toString());
		}
	}

	class ClectStatusAsync extends
		AsyncTask<Void, Void, Void>{

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
					ProgressDialog.show(CanTingDetailActivity.this, null, null);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Void doInBackground(Void... params){
			// TODO Auto-generated method stub
			String url =
					Config.FD_DETAIL_CLECT_STATUS_URL + uid
							+ "&userid=" + userId;
			Log.e("url", url);
			String data = new Tools().getURL(url);
			// String data = "{\"code\":1,\"result\":\"1\"}";
			System.out.println(data);

			try{
				JSONObject job = new JSONObject(data);
				String code = job.getString("code");
				if(code.equals("1")){
					String result = job.getString("result");
					Message msg = handler.obtainMessage();
					msg.what = 3;
					msg.obj = result;
					handler.sendMessage(msg);
				}
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
			new DetailAsync().execute(uid);
		}

	}

	class DetailAsync extends
		AsyncTask<String, Void, HashMap<String, String>>{

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
		protected HashMap<String, String>
			doInBackground(String... params){
			// TODO Auto-generated method stub
			Log.e("url", Config.FD_MAIN_LIST_URL
					+ params[0]);
			String data =
					new Tools().getURL(Config.FD_MAIN_LIST_URL
							+ params[0]);
			// String data =
			// "{\"code\":1,\"result\":{\"id\":\"8\",\"w_title\":\"\u957f\u6625\u6d6a\u6f2b\u559c\u90fd1\",\"w_bg_pic\":\"/upload/23/2015-05/19/555acddd7cdb98335.jpg\",\"w_jj\":\"40-50\u5143\",\"w_dz\":\"\u957f\u6625\u5e02\u671d\u9633\u533a\u5de5\u519c\u5927\u8def1726\u53f7\uff08\u767e\u8111\u6c47\u659c\u5bf9\u9762\uff09\",\"w_dh\":\"0431-85611555\",\"w_pl\":\"55\",\"w_xj\":\"165\",\"w_longitude\":\"125.31173\",\"w_latitude\":\"43.872483\"}}";
			HashMap<String, String> hashMap =
					new HashMap<String, String>();
			try{
				JSONObject job = new JSONObject(data);
				String code = job.getString("code");
				if(code.equals("1")){
					JSONObject job1 =
							job.getJSONObject("result");
					hashMap.put("w_title", job1.getString("w_title"));
					hashMap.put("w_bg_pic", job1.getString("w_bg_pic"));
					hashMap.put("w_jj", job1.getString("w_jj"));
					hashMap.put("w_dz", job1.getString("w_dz"));
					hashMap.put("w_dh", job1.getString("w_dh"));
					hashMap.put("w_pl", job1.getString("w_pl"));
					hashMap.put("w_xj", job1.getString("w_xj"));
					hashMap.put("w_longitude", job1.getString("w_longitude"));
					hashMap.put("w_latitude", job1.getString("w_latitude"));
				}
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
		protected void
			onPostExecute(HashMap<String, String> result){
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			Message msg = handler.obtainMessage();
			msg.obj = result;
			msg.what = 1;
			handler.sendMessage(msg);

			new DetailListAsync().execute(uid);
		}

	}

	class DetailListAsync extends
		AsyncTask<Object, Void, String>{

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
		protected String doInBackground(Object... params){
			// TODO Auto-generated method stub
			Log.e("url", Config.FD_MAIN_TUIJIAN_LIST_URL
					+ params[0]);
			String data =
					new Tools().getURL(Config.FD_MAIN_TUIJIAN_LIST_URL
							+ params[0]);
			// String data =
			// "{\"code\":1,\"result\":[{\"id\":\"30495\",\"n_title\":\"\u4efb\u53d4\u6263\u80891\",\"n_dj\":\"25\",\"n_caipin_pl\":\"1\",\"n_xj\":\"2\",\"n_pic\":\"/upload/23/2015-05/20/555c2756481b87578.jpg\"},{\"id\":\"30496\",\"n_title\":\"\u5218\u963f\u59e8\u56db\u559c\u4e38\u5b50\",\"n_dj\":\"75\",\"n_caipin_pl\":\"0\",\"n_xj\":\"0\",\"n_pic\":\"/upload/23/2015-05/20/555c28b2cd3812695.jpg\"},{\"id\":\"30497\",\"n_title\":\"\u5218\u963f\u59e8\u7c73\u7c89\u8089\",\"n_dj\":\"35\",\"n_caipin_pl\":\"0\",\"n_xj\":\"0\",\"n_pic\":\"/upload/23/2015-05/20/555c28cadbabd9728.jpg\"}]}";
			list = new ArrayList<HashMap<String, String>>();
			String code = "";
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
						hashMap.put("id", job1.getString("id"));
						hashMap.put("n_title", job1.getString("n_title"));
						hashMap.put("n_dj", job1.getString("n_dj"));
						hashMap.put("n_caipin_pl", job1.getString("n_caipin_pl"));
						hashMap.put("n_xj", job1.getString("n_xj"));
						hashMap.put("n_pic", job1.getString("n_pic"));
						hashMap.put("uid", uid);

						list.add(hashMap);
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
				CookingRecomListAdapter crlAdapter =
						new CookingRecomListAdapter(
							CanTingDetailActivity.this,
							list);
				ScrollListView.setListViewHeightBasedOnChildren(lv);
				lv.setAdapter(crlAdapter);

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
									CanTingDetailActivity.this,
									MeiShiCommentMain.class);
						intent.putExtra("id", list.get(position).get("id"));
						intent.putExtra("uid", uid);
						startActivity(intent);
					}
				});

			}

			new CommendAsync().execute(uid);
		}

	}

	class CommendAsync
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
			Log.e("url", Config.FD_MAIN_COMMEND_LIST_URL
					+ params[0]);
			String data =
					new Tools().getURL(Config.FD_MAIN_COMMEND_LIST_URL
							+ params[0]);
			cList =
					new ArrayList<HashMap<String, String>>();
			try{
				JSONObject job = new JSONObject(data);
				String code = job.getString("code");
				if(code.equals("1")){
					JSONArray jArray =
							job.getJSONArray("result");
					for(int i = 0,j = jArray.length();i < j;i++){
						JSONObject job1 =
								jArray.optJSONObject(i);
						HashMap<String, String> hashMap =
								new HashMap<String, String>();
						hashMap.put("id", job1.getString("id"));
						hashMap.put("u_wxname", job1.getString("u_wxname"));
						hashMap.put("xj", job1.getString("xj"));
						hashMap.put("nr", job1.getString("nr"));
						hashMap.put("addtime", job1.getString("addtime"));
						hashMap.put("pic", job1.getString("pic"));

						cList.add(hashMap);
					}
				}
			}catch(JSONException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return cList;
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
			if(cList.size() != 0){
				Message msg = handler.obtainMessage();
				msg.what = 2;
				msg.obj = result;
				handler.sendMessage(msg);
			}
		}
	}

	class ClectAsync extends AsyncTask<Void, Void, String>{

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String doInBackground(Void... params){
			// TODO Auto-generated method stub
			String url =
					Config.FD_DETAIL_CLECT_OR_NOT_URL + uid
							+ "&userid=" + userId;
			Log.e("url", url);
			String data = new Tools().getURL(url);
			System.out.println(data);
			String result = "";

			try{
				JSONObject job = new JSONObject(data);
				result = job.getString("result");

			}catch(JSONException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
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
				Toast.makeText(CanTingDetailActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(CanTingDetailActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
			}
		}

	}

	private Handler	handler	= new Handler(){

								@Override
								public
									void
									handleMessage(Message msg){
									// TODO Auto-generated method stub
									super.handleMessage(msg);
									switch(msg.what){
										case 1:
											HashMap<String, String> hashMap =
													(HashMap<String, String>) msg.obj;
											name.setText(hashMap.get("w_title"));
											float xj =
													Float.valueOf(hashMap.get("w_xj"));
											float pl =
													Float.valueOf(hashMap.get("w_pl"));
											rate =
													(float) new Tools().round(xj
															/ pl, 1, BigDecimal.ROUND_HALF_UP);
											picAddr =
													hashMap.get("w_bg_pic");
											rb.setRating(rate);
											rating.setText(rate
													+ "分");
											average.setText("人均"
													+ hashMap.get("w_jj"));
											address.setText(hashMap.get("w_dz"));
											double lat =
													Double.valueOf(hashMap.get("w_latitude"));
											double lon =
													Double.valueOf(hashMap.get("w_longitude"));
										// double dist= new
										// Tools().getDistance(lat, myLat, lon,
										// myLon);
										// if(dist == -1){
										// distance.setText("未开启GPS,无法测量距离");
										// }else{
										// distance.setText("距离<" + dist + "m");
										// }
										break;
										case 2:
											HashMap<String, String> map =
													((ArrayList<HashMap<String, String>>) msg.obj).get(0);
											comName.setText(map.get("u_wxname"));
											comRb.setRating(Float.valueOf(map.get("xj")));
											date.setText(map.get("addtime"));
											commend.setText(map.get("nr"));
											String pic_all =
													map.get("pic");
											if(pic_all != null){
												String[] picStrings =
														pic_all.split("\\|");
												picList =
														new ArrayList<String>();
												for(int i =
														1,j =
														picStrings.length;i < j;i++){
													System.out.println(i
															+ "  "
															+ picStrings[i]);
													picList.add(picStrings[i]);
												}

												GridAdapter gAdapter =
														new GridAdapter(
															CanTingDetailActivity.this,
															picList);
												ScrollListView.setGridViewHeightBasedOnChildren(pics);
												pics.setAdapter(gAdapter);
												pics.setOnItemClickListener(new OnItemClickListener(){

													@Override
													public
														void
														onItemClick(AdapterView<?> parent,
																View view,
																final int position,
																long id){
														// TODO Auto-generated
														new AsyncTask<Void, Void, ImageView>(){

															@Override
															protected
																ImageView
																doInBackground(Void... params){
																// TODO
																// Auto-generated
																// method stub
																WindowManager wm =
																		CanTingDetailActivity.this.getWindowManager();
																int width =
																		wm.getDefaultDisplay().getWidth();
																int height =
																		wm.getDefaultDisplay().getHeight();
																Log.e("screen width", width
																		+ "");
																float scaleX =
																		0;
																float scaleY =
																		0;
																Bitmap bmp =
																		null;
																int pic_w =
																		0;
																int pic_h =
																		0;
																try{
																	URL url =
																			new URL(
																				Config.DOMAIN
																						+ picList.get(position));
																	bmp =
																			BitmapFactory.decodeStream(url.openStream());
																	pic_w =
																			bmp.getWidth();
																	pic_h =
																			bmp.getHeight();
																	Log.e("pic_w", pic_w
																			+ "");
																	Log.e("pic_h", pic_h
																			+ "");//
																	float scale =
																			(float) width
																					/ pic_w;
																	Log.e("scale", ""
																			+ scale);
																	scaleX =
																			(float) width
																					/ pic_w;
																	scaleY =
																			(float) height
																					/ pic_h;
																	// if(scale
																	// < 1){
																	// scaleX =
																	// width;
																	// scaleY =
																	// new
																	// Float(scale
																	// *
																	// pic_h).intValue();
																	// Log.e("scaleY1",
																	// ""+scaleY);
																	// }else{
																	// scaleX =
																	// new
																	// Float(scale
																	// *
																	// pic_w).intValue();
																	// scaleY =
																	// new
																	// Float(scale
																	// *
																	// pic_h).intValue();
																	// Log.e("scaleY2",
																	// ""+scaleY);
																	// }

																	Log.e("scaleX", ""
																			+ scaleX);
																	Log.e("scaleY3", ""
																			+ scaleY);

																}catch(MalformedURLException e){
																	// TODO
																	// Auto-generated
																	// catch
																	// block
																	e.printStackTrace();
																}catch(IOException e){
																	// TODO
																	// Auto-generated
																	// catch
																	// block
																	e.printStackTrace();
																}

																/* 产生reSize后的Bitmap对象 */
																Matrix matrix =
																		new Matrix();
																if(scaleY > 2){
																	matrix.postScale(scaleX, 2.25f);
																}else{
																	matrix.postScale(scaleX, 0.75f);
																}
																Bitmap resizeBmp =
																		Bitmap.createBitmap(bmp, 0, 0, pic_w, pic_h, matrix, true);
																ImageView image =
																		new ImageView(
																			CanTingDetailActivity.this);
																image.setImageBitmap(resizeBmp);

																return image;
															}

															@Override
															protected
																void
																onPostExecute(ImageView result){
																// TODO
																// Auto-generated
																// method stub
																super.onPostExecute(result);
																Dialog dialog;
																dialog =
																		new Dialog(
																			CanTingDetailActivity.this,
																			R.style.dialog);
																dialog.setContentView(result);
																dialog.show();
																Window win =
																		dialog.getWindow();
																win.getDecorView().setPadding(0, 0, 0, 0);
																WindowManager.LayoutParams lp =
																		win.getAttributes();
																lp.width =
																		WindowManager.LayoutParams.FILL_PARENT;
																lp.height =
																		WindowManager.LayoutParams.WRAP_CONTENT;
																win.setAttributes(lp);
															}

														}.execute();

													}
												});
											}

										break;
										case 3:
											String result =
													(String) msg.obj;
											if(result.equals("0")){
												status =
														false;
												clect.setImageResource(R.drawable.baofang_order_select_heart_normal);
											}else{
												status =
														true;
												clect.setImageResource(R.drawable.baofang_order_select_heart_pressed);
											}
										break;
										default:
										break;
									}

								}

							};

	class GridAdapter extends BaseAdapter{

		private Context				context;
		private ArrayList<String>	list;

		public GridAdapter(Context context,
							ArrayList<String> list){
			super();
			this.context = context;
			this.list = list;

			preperImageLoader();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount(){
			// TODO Auto-generated method stub
			return list.size();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int position){
			// TODO Auto-generated method stub
			return list.get(position);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int position){
			// TODO Auto-generated method stub
			return position;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		@Override
		public View getView(int position,
				View convertView,
				ViewGroup parent){
			// TODO Auto-generated method stub
			ImageView iv;
			if(convertView == null){
				iv = new ImageView(context);
				iv.setAdjustViewBounds(true);// 设置边界对齐
				iv.setLayoutParams(new GridView.LayoutParams(
					120, 80));// 设置ImageView对象
				iv.setScaleType(ImageView.ScaleType.FIT_XY);// 设置刻度的类型
				// iv.setPadding(2,2,2,2);//设置间距
			}else{
				iv = (ImageView) convertView;
			}

			ImageLoader.getInstance().displayImage(Config.DOMAIN
					+ list.get(position), iv);

			return iv;
		}

		private void preperImageLoader(){

			/******************* 配置ImageLoder ***********************************************/
			File cacheDir =
					StorageUtils.getOwnCacheDirectory(CanTingDetailActivity.this, "imageloader/Cache");

			ImageLoaderConfiguration config =
					new ImageLoaderConfiguration.Builder(
						CanTingDetailActivity.this).denyCacheImageMultipleSizesInMemory().discCache(new UnlimitedDiscCache(
						cacheDir))// 自定义缓存路径
					.build();// 开始构建

			DisplayImageOptions options =
					new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

			ImageLoader.getInstance().init(config);// 全局初始化此配置
			/*********************************************************************************/
		}

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
			case R.id.baofang_order_select_reserve:
				intent =
						new Intent(
							CanTingDetailActivity.this,
							BaoFangReserveInfo.class);
				intent.putExtra("rate", rate);
				intent.putExtra("name", name.getText().toString());
				intent.putExtra("average", average.getText().toString());
				intent.putExtra("picAddr", picAddr);
				intent.putExtra("uid", uid);

			break;
			case R.id.baofang_order_select_commend_check_all:
				// cList
				intent =
						new Intent(
							CanTingDetailActivity.this,
							CanTingAllComments.class);
				intent.putExtra("rate", rate);
				intent.putExtra("list", cList);
				intent.putExtra("type", "1");
			break;
			case R.id.baofang_order_select_order:
				intent =
						new Intent(
							CanTingDetailActivity.this,
							DishOrderActivity.class);
				intent.putExtra("uid", uid);
				intent.putExtra("title", name.getText().toString());
			break;
			case R.id.baofang_order_select_operate:
				new ClectAsync().execute();
				if(status == false){
					status = true;
					clect.setImageResource(R.drawable.baofang_order_select_heart_pressed);
				}else{
					status = false;
					clect.setImageResource(R.drawable.baofang_order_select_heart_normal);
				}
			break;
			default:
			break;
		}
		startActivity(intent);
	}
}
