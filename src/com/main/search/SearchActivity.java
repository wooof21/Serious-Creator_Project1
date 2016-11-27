/**
 * 
 */
package com.main.search;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chihuoshijian.R;
import com.chihuoshijian.adapter.MeiShiMainListAdapter;
import com.example.fatass.MainActivity;
import com.main.canting.CanTingDetailActivity;
import com.main.meishi.MeiShiMainListPage;
import com.tools.Config;
import com.tools.Tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

/**
 * @author Liming Chu
 * 
 * @param
 * @return
 */
public class SearchActivity extends Activity{

	private ImageView	back;
	private EditText	searchEt;
	private TextView	searchTv;
	private ListView	lv;

	private TextView rmTv;
	private ProgressDialog progressDialog;
	private boolean		isFristTime	= true;

	/** 标签之间的间距 px */
	final int			itemMargins	= 10;

	/** 标签的行间距 px */
	final int			lineMargins	= 10;

	private ViewGroup	container	= null;

	private String[]	tags;
	private ArrayList<HashMap<String, String>> list;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);
		prepareView();
		container =
				(ViewGroup) findViewById(R.id.search_container);
		tags = getIntent().getStringArrayExtra("tags");
		System.out.println(tags);
		new ListAsync().execute();
	}

	private void prepareView(){
		back = (ImageView) findViewById(R.id.search_back);
		searchEt = (EditText) findViewById(R.id.search_et);
		searchTv =
				(TextView) findViewById(R.id.search_frame_search);
		lv = (ListView) findViewById(R.id.search_lv);
	}

	/**
	 * 自动换行linearlayout
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
					(TextView) inflater.inflate(
							R.layout.remen_textview, null);
			final int itemPadding =
					textView.getCompoundPaddingLeft()
							+ textView
									.getCompoundPaddingRight();
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
					addItemView(inflater, layout, tvParams,
							text, i);
				}else{
					resetTextViewMarginsRight(layout);
					layout = new LinearLayout(this);
					layout.setLayoutParams(params);
					layout.setOrientation(LinearLayout.HORIZONTAL);

					/** 将前面那一个textview加入新的一行 */
					addItemView(inflater, layout, tvParams,
							text, i);
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
	private void resetTextViewMarginsRight(
			ViewGroup viewGroup){
		final TextView tempTextView =
				(TextView) viewGroup.getChildAt(viewGroup
						.getChildCount() - 1);
		tempTextView
				.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
	}

	/**
	 * 将view添加到viewgroup中
	 * */
	private void addItemView(LayoutInflater inflater,
			ViewGroup viewGroup, LayoutParams params,
			String text, int tag){
		rmTv =
				(TextView) inflater.inflate(
						R.layout.remen_textview, null);
		rmTv.setTag(tag);
		rmTv.setText(text);
		rmTv.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				TextView tv = (TextView) v;
				Intent intent = new Intent(SearchActivity.this, MeiShiMainListPage.class);
				intent.putExtra("n_title", tv.getText().toString());
				startActivity(intent);
			}
		});
		viewGroup.addView(rmTv, params);
	}

	class ListAsync extends AsyncTask<Void, Void, String>{

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute(){
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = ProgressDialog.show(SearchActivity.this, null, null);
		}
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String doInBackground(Void... params){
			// TODO Auto-generated method stub
			System.out.println(Config.MS_MAIN_HOT_SEARCH_LIST_URL);
			String data = new Tools().getURL(Config.MS_MAIN_HOT_SEARCH_LIST_URL);
			System.out.println(data);
			String code = "";
			list = new ArrayList<HashMap<String,String>>();
			try{
				JSONObject job = new JSONObject(data);
				code = job.getString("code");
				if(code.equals("1")){
					JSONArray jArray = job.getJSONArray("result");
					for(int i=0,j=jArray.length();i<j;i++){
						JSONObject job1 = jArray.optJSONObject(i);
						HashMap<String, String> hashMap = new HashMap<String, String>();
						hashMap.put("n_dj", job1.getString("n_dj"));
						hashMap.put("n_pic", job1.getString("n_pic"));
						hashMap.put("n_title", job1.getString("n_title"));
						hashMap.put("uid", job1.getString("uid"));
						hashMap.put("w_dh", job1.getString("w_dh"));
						hashMap.put("w_dz", job1.getString("w_dz"));
						hashMap.put("w_jj", job1.getString("w_jj"));
						hashMap.put("w_latitude", job1.getString("w_latitude"));
						hashMap.put("w_longitude", job1.getString("w_longitude"));
						hashMap.put("w_title", job1.getString("w_title"));
						hashMap.put("w_xj", job1.getString("w_xj"));
						
						list.add(hashMap);
					}
				}
			}catch(JSONException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return code;
		}
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String result){
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			if(result.equals("1")){
				MeiShiMainListAdapter msAdapter = new MeiShiMainListAdapter(SearchActivity.this, list);
				lv.setAdapter(msAdapter);
				
				lv.setOnItemClickListener(new OnItemClickListener(){

					@Override
					public void
							onItemClick(
									AdapterView<?> parent,
									View view,
									int position, long id){
						// TODO Auto-generated method stub
						Intent intent = new Intent(SearchActivity.this, CanTingDetailActivity.class);
						intent.putExtra("uid", list.get(position).get("uid"));
						startActivity(intent);
					}});
			}
		}
		
	}
	@Override
	public void onBackPressed(){
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

}
