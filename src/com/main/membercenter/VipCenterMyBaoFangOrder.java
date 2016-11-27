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
import com.chihuoshijian.adapter.VipBaoFangOrderAdapter;
import com.tools.Config;
import com.tools.Tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Liming Chu
 * 
 * @param
 * @return
 */
public class VipCenterMyBaoFangOrder extends Activity
		implements OnClickListener{

	private TextView							ordering;
	private TextView							confirmed;
	private TextView							done;

	private ImageView							divider1;
	private ImageView							divider2;
	private ImageView							divider3;

	private ListView							lv;

	private ProgressDialog						progressDialog;
	private ArrayList<HashMap<String, String>>	list;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vip_center_my_baofang_order);
		prepareView();

		new ListAsync().execute("19", "0");
	}

	private void prepareView(){
		ordering =
				(TextView) findViewById(R.id.vip_center_my_bf_order_ordering);
		confirmed =
				(TextView) findViewById(R.id.vip_center_my_bf_order_confirmed);
		done =
				(TextView) findViewById(R.id.vip_center_my_bf_order_done);
		divider1 =
				(ImageView) findViewById(R.id.vip_center_my_bf_order__top_divider1);
		divider2 =
				(ImageView) findViewById(R.id.vip_center_my_bf_order__top_divider2);
		divider3 =
				(ImageView) findViewById(R.id.vip_center_my_bf_order__top_divider3);
		lv =
				(ListView) findViewById(R.id.vip_center_my_bf_order_lv);

		ordering.setOnClickListener(this);
		confirmed.setOnClickListener(this);
		done.setOnClickListener(this);
	}

	class ListAsync extends AsyncTask<String, Void, String>{

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
							VipCenterMyBaoFangOrder.this,
							null, null);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String doInBackground(String... params){
			// TODO Auto-generated method stub
			String url =
					Config.BF_ORDER_RECORD_URL + params[0]
							+ "&zt=" + params[1];
			Log.e("url", url);
			String data = new Tools().getURL(url);
			System.out.println(data);
			String code = "";
			list = new ArrayList<HashMap<String, String>>();

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
						hashMap.put("uid",
								job1.getString("uid"));
						hashMap.put("w_bf",
								job1.getString("w_bf"));
						hashMap.put("w_logo_pic", job1
								.getString("w_logo_pic"));
						hashMap.put("w_title",
								job1.getString("w_title"));
						hashMap.put("ydsj",
								job1.getString("ydsj"));
						hashMap.put("zt",
								job1.getString("zt"));

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
			progressDialog.dismiss();
			if(result.equals("1")){
				VipBaoFangOrderAdapter vAdapter =
						new VipBaoFangOrderAdapter(
								VipCenterMyBaoFangOrder.this,
								list);
				lv.setAdapter(vAdapter);
			}
		}
	}

	private void setImageBack(int viewId){
		switch(viewId){
			case R.id.vip_center_my_bf_order_ordering:
				divider1.setBackgroundColor(Color.rgb(255,
						60, 64));
				divider2.setBackgroundColor(Color.WHITE);
				divider3.setBackgroundColor(Color.WHITE);
			break;
			case R.id.vip_center_my_bf_order_confirmed:
				divider2.setBackgroundColor(Color.rgb(255,
						60, 64));
				divider1.setBackgroundColor(Color.WHITE);
				divider3.setBackgroundColor(Color.WHITE);
			break;
			case R.id.vip_center_my_bf_order_done:
				divider3.setBackgroundColor(Color.rgb(255,
						60, 64));
				divider2.setBackgroundColor(Color.WHITE);
				divider1.setBackgroundColor(Color.WHITE);
			break;
			default:
			break;
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

		switch(v.getId()){
			case R.id.vip_center_my_bf_order_ordering:
				setImageBack(R.id.vip_center_my_bf_order_ordering);
				new ListAsync().execute("19", "0");
			break;
			case R.id.vip_center_my_bf_order_confirmed:
				setImageBack(R.id.vip_center_my_bf_order_confirmed);
				new ListAsync().execute("19", "1");
			break;
			case R.id.vip_center_my_bf_order_done:
				setImageBack(R.id.vip_center_my_bf_order_done);
				new ListAsync().execute("19", "2");
			break;
			default:
			break;
		}
	}
}
