/**
 * 
 */
package com.main.baofang;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chihuoshijian.R;
import com.tools.Config;
import com.tools.Tools;

import android.R.integer;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Liming Chu 包房预留信息填写
 * @param
 * @return
 */
public class BaoFangReserveInfo extends Activity implements
	OnClickListener{

	/** view */
	private ImageView		back;
	private LinearLayout	llIncluded;
	private EditText		reserveName;
	private EditText		reservePhone;
	private Spinner			reserveTime;
	private Spinner			type;
	private EditText		mark;
	private LinearLayout	gone1;
	private TextView		resBack;
	private TextView		resOrder;
	/** view included */
	private ImageView		pic;
	private TextView		name;
	private RatingBar		ratingBar;
	private TextView		rating;
	private TextView		average;
	private TextView		range;
	private LinearLayout	map;
	private TextView		address;
	private ImageView		phone;
	private LinearLayout	gone;

	private ProgressDialog	progressDialog;
	private HashMap<String, String>	timeMap, bfMap;
	private String					timeId, bfId;
	protected String[]				item, item2;
	private String					timePrefix;
	private String	uid;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baofang_reserve_info);
		prepareView();

		name.setText(getIntent().getStringExtra("name"));
		ratingBar.setRating(getIntent().getFloatExtra("rate", 0));
		rating.setText(getIntent().getFloatExtra("rate", 0)
				+ "分");
		average.setText(getIntent().getStringExtra("average"));
		String picAddr =
				getIntent().getStringExtra("picAddr");
		uid = getIntent().getStringExtra("uid");
		bfId = getIntent().getExtras().getString("id");
		Log.e("bfid", bfId);
		new SpinnerAsync().execute(uid);
	}

	private void prepareView(){
		back =
				(ImageView) findViewById(R.id.baofang_reserve_back);
		llIncluded =
				(LinearLayout) findViewById(R.id.baofang_reserve_info_ll_included);
		reserveName =
				(EditText) findViewById(R.id.baofang_reserve_name_et);
		reservePhone =
				(EditText) findViewById(R.id.baofang_reserve_phone_et);
		reserveTime =
				(Spinner) findViewById(R.id.baofang_reserve_time_spinner);

		type =
				(Spinner) findViewById(R.id.baofang_reserve_type_spinner);
		
		mark =
				(EditText) findViewById(R.id.baofang_reserve_mark_et);
		resBack =
				(TextView) findViewById(R.id.baofang_order_success_back);
		resOrder =
				(TextView) findViewById(R.id.baofang_order_success_ok);
		gone1 =
				(LinearLayout) llIncluded.findViewById(R.id.main_list_item_gone_for_reserve_ll);
		gone1.setVisibility(View.GONE);
		pic =
				(ImageView) llIncluded.findViewById(R.id.list_item_pic);
		name =
				(TextView) llIncluded.findViewById(R.id.list_item_name);
		ratingBar =
				(RatingBar) llIncluded.findViewById(R.id.list_item_ratingbar);
		rating =
				(TextView) llIncluded.findViewById(R.id.list_item_rating);
		average =
				(TextView) llIncluded.findViewById(R.id.list_item_average);
		range =
				(TextView) llIncluded.findViewById(R.id.list_item_range);
		range.setVisibility(View.GONE);
		map =
				(LinearLayout) llIncluded.findViewById(R.id.list_item_map);
		address =
				(TextView) llIncluded.findViewById(R.id.list_item_address);
		phone =
				(ImageView) llIncluded.findViewById(R.id.list_item_phone);
		gone =
				(LinearLayout) llIncluded.findViewById(R.id.main_list_item_order);
		gone.setVisibility(View.GONE);

		ratingBar.setClickable(false);
		back.setOnClickListener(this);
		resBack.setOnClickListener(this);
		resOrder.setOnClickListener(this);
	}

	class SpinnerAsync extends
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
			progressDialog =
					ProgressDialog.show(BaoFangReserveInfo.this, null, null);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String doInBackground(Object... params){
			// TODO Auto-generated method stub
			Log.e("url", Config.BF_RESERVE_MAIN_URL
					+ params[0]);
			String data =
					new Tools().getURL(Config.BF_RESERVE_MAIN_URL
							+ params[0]);
		//	String data = "{\"code\":1,\"result1\":[{\"id\":\"320\",\"classname\":\"9:00-11:00\"},{\"id\":\"321\",\"classname\":\"11:00-13:00\"},{\"id\":\"322\",\"classname\":\"17:00-20:00\"},{\"id\":\"323\",\"classname\":\"20:00-23:00\"}],\"result2\":[{\"id\":\"16\",\"classname\":\"\u5bb4\u4f1a\u538521-100\u4eba\",\"ky\":\"1\"},{\"id\":\"10\",\"classname\":\"\u5927\u5305\u623f11-20\u4eba\",\"ky\":\"3\"},{\"id\":\"11\",\"classname\":\"\u4e2d\u5305\u623f6-10\u4eba\",\"ky\":\"5\"},{\"id\":\"9\",\"classname\":\"\u5c0f\u5305\u623f1-5\u4eba\",\"ky\":\"10\"}]}";
			String code = "";
			ArrayList<HashMap<String, String>> timeList =
					new ArrayList<HashMap<String, String>>();
			ArrayList<HashMap<String, String>> bfList =
					new ArrayList<HashMap<String, String>>();
			timeMap = new HashMap<String, String>();
			bfMap = new HashMap<String, String>();
			try{
				JSONObject job = new JSONObject(data);
				code = job.getString("code");
				if(code.equals("1")){
					JSONArray jArray1 =
							job.getJSONArray("result1");
					JSONArray jArray2 =
							job.getJSONArray("result2");
					for(int i = 0,j = jArray1.length();i < j;i++){
						JSONObject job1 =
								jArray1.optJSONObject(i);
						HashMap<String, String> hashMap =
								new HashMap<String, String>();
						hashMap.put("classname", "今天"
								+ job1.getString("classname"));
						hashMap.put("id", job1.getString("id"));
						timeMap.put("今天"
								+ job1.getString("classname"), job1.getString("id"));
						timeList.add(hashMap);
					}
					for(int i = 0,j = jArray1.length();i < j;i++){
						JSONObject job1 =
								jArray1.optJSONObject(i);
						HashMap<String, String> hashMap =
								new HashMap<String, String>();
						hashMap.put("classname", "明天"
								+ job1.getString("classname"));
						hashMap.put("id", job1.getString("id"));
						timeMap.put("明天"
								+ job1.getString("classname"), job1.getString("id"));
						timeList.add(hashMap);
					}
					for(int m = 0,n = jArray2.length();m < n;m++){
						JSONObject job2 =
								jArray2.optJSONObject(m);
						HashMap<String, String> hashMap =
								new HashMap<String, String>();
						hashMap.put("classname", job2.getString("classname"));
						hashMap.put("id", job2.getString("id"));
						hashMap.put("ky", job2.getString("ky"));
						bfMap.put(job2.getString("classname"), job2.getString("id"));
						bfMap.put(job2.getString("id"), job2.getString("classname"));
						bfList.add(hashMap);
					}
					Message msg1 = handler.obtainMessage();
					msg1.what = 1;
					msg1.obj = timeList;
					handler.sendMessage(msg1);
					Message msg2 = handler.obtainMessage();
					msg2.what = 2;
					msg2.obj = bfList;
					handler.sendMessage(msg2);

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
		protected void onPostExecute(String result){
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
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
											ArrayList<HashMap<String, String>> list =
													(ArrayList<HashMap<String, String>>) msg.obj;
											item =
													new String[list.size()];
											for(int i = 0,j =
													list.size();i < j;i++){
												item[i] =
														list.get(i).get("classname");
											}
											ArrayAdapter<String> adapter =
													new ArrayAdapter<String>(
														BaoFangReserveInfo.this,
														android.R.layout.simple_spinner_item,
														item);
											reserveTime.setAdapter(adapter);
											
											reserveTime.setOnItemSelectedListener(new OnItemSelectedListener(){

												@Override
												public
													void
													onItemSelected(AdapterView<?> parent,
															View view,
															int position,
															long id){
													// TODO Auto-generated
													// method stub
													timeId =
															timeMap.get(item[position]);
													Log.e("time id", timeId);
													if(item[position].contains("今天")){
														timePrefix =
																"今天";
													}else{
														timePrefix =
																"明天";
													}
												}

												@Override
												public
													void
													onNothingSelected(AdapterView<?> parent){
													// TODO Auto-generated
													// method stub

												}
											});
										break;

										case 2:
											ArrayList<HashMap<String, String>> list2 =
													(ArrayList<HashMap<String, String>>) msg.obj;
											item2 =
													new String[list2.size()];
											for(int i = 0,j =
													list2.size();i < j;i++){
												item2[i] =
														list2.get(i).get("classname");
											}
											ArrayAdapter<String> adapter2 =
													new ArrayAdapter<String>(
														BaoFangReserveInfo.this,
														android.R.layout.simple_spinner_item,
														item2);
											type.setAdapter(adapter2);
											type.setOnItemSelectedListener(new OnItemSelectedListener(){

												@Override
												public
													void
													onItemSelected(AdapterView<?> parent,
															View view,
															int position,
															long id){
													// TODO Auto-generated
													// method stub
													bfId =
															bfMap.get(item2[position]);
													Log.e("bf id", bfId);
												}

												@Override
												public
													void
													onNothingSelected(AdapterView<?> parent){
													// TODO Auto-generated
													// method stub

												}
											});
										break;
										default:
										break;
									}
								}

							};

	@Override
	public void onBackPressed(){
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	private boolean validate(){
		boolean ok = false;
		if(reserveName.getText().toString() == null){
			ok = false;
			Toast.makeText(BaoFangReserveInfo.this, "请输入姓名", Toast.LENGTH_SHORT).show();
		}else if(reservePhone.getText().toString() == null){
			ok = false;
			Toast.makeText(BaoFangReserveInfo.this, "请输入手机号", Toast.LENGTH_SHORT).show();
		}else if(reservePhone.getText().toString().length() != 11){
			ok = false;
			Toast.makeText(BaoFangReserveInfo.this, "请输入正确的11位手机号", Toast.LENGTH_SHORT).show();
		}else{
			ok = true;
		}
		return ok;
	}
	
	private String getReserveInfo(){
		StringBuilder sb = new StringBuilder();
		sb.append("name=");
		sb.append(reserveName.getText().toString());
		sb.append("&bid=");
		sb.append(bfId);
		sb.append("&uid=");
		sb.append(uid);
		sb.append("&tel=");
		sb.append(reservePhone.getText().toString());
		sb.append("&ycsj=");
		sb.append(timePrefix);
		sb.append("|");
		sb.append(timeId);
		sb.append("&userid=");
		sb.append("19");
		sb.append("&bz=");
		sb.append(mark.getText().toString());
		
		System.out.println(sb.toString());
		
		return sb.toString();
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
			case R.id.baofang_reserve_back:
				finish();
			break;
			case R.id.baofang_order_success_back:
				finish();
			break;
			case R.id.baofang_order_success_ok:
				if(validate() == false){
					
				}else{
					new AsyncTask<Void, Void, String>(){
						
						
						@Override
						protected void onPreExecute(){
							// TODO Auto-generated method stub
							super.onPreExecute();
							progressDialog = ProgressDialog.show(BaoFangReserveInfo.this, null, null);
						}


						@Override
						protected String
							doInBackground(Void... params){
							// TODO Auto-generated method stub
							String code = "";
							String msg = "";
							try{
								String data = new Tools().doPostData(Config.BF_RESERVE_SUBMIT_URL, getReserveInfo());
								System.out.println(data);
								JSONObject job = new JSONObject(data);
								code = job.getString("code");
								msg = job.getString("result");
							}catch(IOException e){
								// TODO Auto-generated catch block
								e.printStackTrace();
							}catch(JSONException e){
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return code + "-" + msg;
						}

						@Override
						protected void
							onPostExecute(String result){
							// TODO Auto-generated method stub
							super.onPostExecute(result);
							progressDialog.dismiss();
							String[] s = result.split("-");
							if(s[0].equals("1")){
								Toast.makeText(BaoFangReserveInfo.this, "包房预定成功！", Toast.LENGTH_SHORT).show();
								Intent intent = new Intent(BaoFangReserveInfo.this, BaoFangOrderSuccess.class);
								intent.putExtra("name", reserveName.getText().toString());
								intent.putExtra("phone", reservePhone.getText().toString());
								intent.putExtra("type", bfMap.get(bfId));
								startActivity(intent);
							}else{
								Toast.makeText(BaoFangReserveInfo.this, s[1], Toast.LENGTH_SHORT).show();
							}
						}
					}.execute();
				}
			break;
			default:
			break;
		}
	}

}
