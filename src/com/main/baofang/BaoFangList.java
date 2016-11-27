/**
 * 
 */
package com.main.baofang;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chihuoshijian.R;
import com.chihuoshijian.adapter.BaoFangInfoListAdapter;
import com.tools.Config;
import com.tools.Tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Liming Chu
 * 
 * @param
 * @return
 */
public class BaoFangList extends Activity implements
	OnClickListener{

	private ListView							lv;
	private TextView							back;
	private TextView							order;

	private ProgressDialog						progressDialog;
	private ArrayList<HashMap<String, String>>	list;
	private String								uid;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baofang_list_main);
		prepareView();

		uid = getIntent().getExtras().getString("uid");
		new ListAsync().execute(uid);

	}

	private void prepareView(){
		lv =
				(ListView) findViewById(R.id.baofang_list_main_lv);
		back =
				(TextView) findViewById(R.id.baofang_list_main_back);
		order =
				(TextView) findViewById(R.id.baofang_list_main_order);

		back.setOnClickListener(this);
		order.setOnClickListener(this);
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
			progressDialog =
					ProgressDialog.show(BaoFangList.this, null, null);
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
			String url =
					Config.BF_INFO_LIST_URL + params[0];
			Log.e("url", url);
			String data = new Tools().getURL(url);
	//		String data = "{\"code\":1,\"result\":[{\"id\":\"9\",\"sl\":\"10\",\"sy\":5,\"uid\":23,\"bftitle\":\"\u5c0f\u5305\u623f1-5\u4eba\"},{\"id\":\"10\",\"sl\":\"3\",\"sy\":2,\"uid\":23,\"bftitle\":\"\u5927\u5305\u623f11-20\u4eba\"},{\"id\":\"11\",\"sl\":\"5\",\"sy\":2,\"uid\":23,\"bftitle\":\"\u4e2d\u5305\u623f6-10\u4eba\"},{\"id\":\"16\",\"sl\":\"1\",\"sy\":0,\"uid\":23,\"bftitle\":\"\u5bb4\u4f1a\u538521-100\u4eba\"}]}";
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
						hashMap.put("id", job1.getString("id"));
						hashMap.put("name", job1.getString("bftitle"));
						hashMap.put("total", job1.getString("sl"));
						hashMap.put("count", job1.getString("sy"));
						hashMap.put("uid", job1.getString("uid"));

						list.add(hashMap);
					}
				}
			}catch(JSONException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return list;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected
			void
			onPostExecute(final ArrayList<HashMap<String, String>> result){
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			BaoFangInfoListAdapter adapter =
					new BaoFangInfoListAdapter(
						BaoFangList.this, result);
			lv.setAdapter(adapter);

			lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void
					onItemClick(AdapterView<?> parent,
							View view,
							int position,
							long id){
					// TODO Auto-generated method stub
					Intent intent =
							new Intent(BaoFangList.this,
								BaoFangReserveInfo.class);
					intent.putExtra("id", result.get(position).get("id"));
					intent.putExtra("uid", result.get(position).get("uid"));
					intent.putExtra("picAddr", getIntent().getExtras().getString("picAddr"));
					intent.putExtra("name", getIntent().getExtras().getString("name"));
					intent.putExtra("rate", getIntent().getExtras().getFloat("rate"));
					intent.putExtra("average", getIntent().getExtras().getString("average"));
					startActivity(intent);
				}
			});
		}

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
			case R.id.baofang_list_main_back:
				finish();
			break;
			case R.id.baofang_list_main_order:
				Intent intent =
						new Intent(BaoFangList.this,
							BaoFangReserveInfo.class);
				intent.putExtra("uid", uid);
				intent.putExtra("id", "");

				intent.putExtra("picAddr", getIntent().getExtras().getString("picAddr"));
				intent.putExtra("name", getIntent().getExtras().getString("name"));
				intent.putExtra("rate", getIntent().getExtras().getFloat("rate"));
				intent.putExtra("average", getIntent().getExtras().getString("average"));
				startActivity(intent);
			break;
			default:
			break;
		}

	}
}
