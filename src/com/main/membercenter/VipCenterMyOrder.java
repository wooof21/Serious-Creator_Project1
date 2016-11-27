/**
 * 
 */
package com.main.membercenter;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chihuoshijian.R;
import com.chihuoshijian.adapter.VipCenterMyOrderListAdapter;
import com.tools.Config;
import com.tools.Tools;

import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IInterface;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Liming Chu
 * 
 * @param
 * @return
 */
public class VipCenterMyOrder extends Activity implements
		OnClickListener{

	private ListView							lv;
	private TextView							all;
	private ImageView							divider1;
	private TextView							done;
	private ImageView							divider2;
	private TextView							hold;
	private ImageView							divider3;
	private TextView							toCom;
	private ImageView							divider4;

	private ProgressDialog						progressDialog;
	private ArrayList<HashMap<String, String>>	list;
	private VipCenterMyOrderListAdapter			vcAdapter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zhongxin_my_order_main_list_page);
		prepareView();

		String which = getIntent().getStringExtra("which");
		if(which.equals("all")){
			setImageBack(R.id.zhongxin_my_order_main_all);
		}else if(which.equals("0")){
			setImageBack(R.id.zhongxin_my_order_main_hold);
		}else if(which.equals("1")){
			setImageBack(R.id.zhongxin_my_order_main_done);
		}else{
			setImageBack(R.id.zhongxin_my_order_main_tomark);
		}
		new ListAsync().execute("19", which);
	}

	private void prepareView(){
		lv =
				(ListView) findViewById(R.id.zhongxin_my_order_main_lv);
		all =
				(TextView) findViewById(R.id.zhongxin_my_order_main_all);
		divider1 =
				(ImageView) findViewById(R.id.zhongxin_my_order_top_divider1);
		done =
				(TextView) findViewById(R.id.zhongxin_my_order_main_done);
		divider2 =
				(ImageView) findViewById(R.id.zhongxin_my_order_top_divider2);
		hold =
				(TextView) findViewById(R.id.zhongxin_my_order_main_hold);
		divider3 =
				(ImageView) findViewById(R.id.zhongxin_my_order_top_divider3);
		toCom =
				(TextView) findViewById(R.id.zhongxin_my_order_main_tomark);
		divider4 =
				(ImageView) findViewById(R.id.zhongxin_my_order_top_divider4);

		all.setOnClickListener(this);
		done.setOnClickListener(this);
		hold.setOnClickListener(this);
		toCom.setOnClickListener(this);
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

	private void setImageBack(int viewID){
		switch(viewID){
			case R.id.zhongxin_my_order_main_all:
				divider1.setBackgroundColor(Color.rgb(255,
						60, 64));
				divider2.setBackgroundColor(Color.WHITE);
				divider3.setBackgroundColor(Color.WHITE);
				divider4.setBackgroundColor(Color.WHITE);
			break;
			case R.id.zhongxin_my_order_main_done:
				divider2.setBackgroundColor(Color.rgb(255,
						60, 64));
				divider1.setBackgroundColor(Color.WHITE);
				divider3.setBackgroundColor(Color.WHITE);
				divider4.setBackgroundColor(Color.WHITE);
			break;
			case R.id.zhongxin_my_order_main_hold:
				divider3.setBackgroundColor(Color.rgb(255,
						60, 64));
				divider2.setBackgroundColor(Color.WHITE);
				divider1.setBackgroundColor(Color.WHITE);
				divider4.setBackgroundColor(Color.WHITE);
			break;
			case R.id.zhongxin_my_order_main_tomark:
				divider4.setBackgroundColor(Color.rgb(255,
						60, 64));
				divider2.setBackgroundColor(Color.WHITE);
				divider3.setBackgroundColor(Color.WHITE);
				divider1.setBackgroundColor(Color.WHITE);
			break;
			default:
			break;
		}
	}

	class ListAsync extends AsyncTask<Object, Void, String>{

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
							VipCenterMyOrder.this, null,
							null);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String doInBackground(Object... params){
			// TODO Auto-generated method stub
			String url =
					Config.MY_ORDER_URL + params[0]
							+ "&zt=" + params[1];
			Log.e("url", url);
			String data = new Tools().getURL(url);
			System.out.println(data);
			list = new ArrayList<HashMap<String, String>>();
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
						hashMap.put("num",
								job1.getString("num"));
						hashMap.put("pj",
								job1.getString("pj"));
						hashMap.put("uid",
								job1.getString("uid"));
						hashMap.put("w_logo_pic", job1
								.getString("w_logo_pic"));
						hashMap.put("w_title",
								job1.getString("w_title"));
						hashMap.put("w_xj",
								job1.getString("w_xj"));
						hashMap.put("zt",
								job1.getString("zt"));
						hashMap.put("je",
								job1.getString("je"));
						hashMap.put("id",
								job1.getString("id"));

						list.add(hashMap);
					}
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
			System.out.println(list.size());
			vcAdapter =
					new VipCenterMyOrderListAdapter(
							VipCenterMyOrder.this, list,
							rHandler);
			lv.setAdapter(vcAdapter);

		}
	}

	class DeleteAsync extends
			AsyncTask<String, Void, String>{

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String doInBackground(String... params){
			// TODO Auto-generated method stub
			String url =
					Config.MY_ORDER_DELETE_URL + params[0];
			Log.e("url", url);
			String data = new Tools().getURL(url);
			System.out.println(data);
			String code = "";
			try{
				JSONObject job = new JSONObject(data);
				code = job.getString("code");
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
				Toast.makeText(VipCenterMyOrder.this,
						"É¾³ý¶©µ¥³É¹¦", Toast.LENGTH_SHORT)
						.show();
				new ListAsync().execute("19", "all");
			}else{
				Toast.makeText(VipCenterMyOrder.this,
						"É¾³ý¶©µ¥Ê§°Ü", Toast.LENGTH_SHORT)
						.show();
			}
		}

	}

	private Handler	rHandler	= new Handler(){

									@Override
									public
											void
											handleMessage(
													Message msg){
										// TODO Auto-generated method stub
										super.handleMessage(msg);
										switch(msg.what){
											case 1:
												String uid =
														(String) msg.obj;
												new DeleteAsync()
														.execute(uid);
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
		switch(v.getId()){
			case R.id.zhongxin_my_order_main_all:
				setImageBack(R.id.zhongxin_my_order_main_all);
				new ListAsync().execute("19", "all");
			break;
			case R.id.zhongxin_my_order_main_done:
				setImageBack(R.id.zhongxin_my_order_main_done);
				new ListAsync().execute("19", "1");
			break;
			case R.id.zhongxin_my_order_main_hold:
				setImageBack(R.id.zhongxin_my_order_main_hold);
				new ListAsync().execute("19", "0");
			break;
			case R.id.zhongxin_my_order_main_tomark:
				setImageBack(R.id.zhongxin_my_order_main_tomark);
				new ListAsync().execute("19", "2");
			break;
			default:
			break;
		}
	}
}
