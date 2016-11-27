/**
 * 
 */
package com.main.meishi;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chihuoshijian.R;
import com.main.canting.CanTingAllComments;
import com.main.canting.CanTingDetailActivity;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tools.Config;
import com.tools.Tools;

import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author Liming Chu
 *
 * @param
 * @return
 */
public class MeiShiCommentMain extends Activity implements OnClickListener{

	private RelativeLayout topBg;
	private TextView dName;
	private TextView overallCom;
	private RatingBar overallRb;
	private TextView overallRating;
	private TextView name;
	private RatingBar rb;
	private TextView date;
	private TextView comment;
	private GridView pics;
	private LinearLayout checkAll;
	private TextView back;
	private TextView order;
	
	private ProgressDialog progressDialog;
	public BitmapDrawable	drawable;
	private String	id;
	private String	uid;
	
	private ArrayList<HashMap<String, String>> cList;
	protected Float	rate;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meishi_comment_main);
		prepapreView();
		preperImageLoader();
		
		id = getIntent().getExtras().getString("id");
		uid = getIntent().getExtras().getString("uid");
		new InfoAsync().execute(id);
	}
	
	private void prepapreView(){
		topBg = (RelativeLayout)findViewById(R.id.meishi_comment_top_rl);
		dName = (TextView)findViewById(R.id.meishi_comment_dish_name);
		overallCom = (TextView)findViewById(R.id.meishi_comment_overall_comment);
		overallRb = (RatingBar)findViewById(R.id.meishi_comment_overall_rb);
		overallRating = (TextView)findViewById(R.id.meishi_comment_overall_rate);
		name = (TextView)findViewById(R.id.meishi_comment_name);
		rb = (RatingBar)findViewById(R.id.meishi_comment_rb);
		date = (TextView)findViewById(R.id.meishi_comment_date);
		comment = (TextView)findViewById(R.id.meishi_comment_commend);
		pics = (GridView)findViewById(R.id.meishi_comment_pic_gv);
		checkAll = (LinearLayout)findViewById(R.id.meishi_comment_check_all);
		back = (TextView)findViewById(R.id.meishi_comment_back);
		order = (TextView)findViewById(R.id.meishi_comment_order);
		
		checkAll.setOnClickListener(this);
		back.setOnClickListener(this);
		order.setOnClickListener(this);
	}
	
	class InfoAsync extends AsyncTask<Object, Void, String>{

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute(){
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = ProgressDialog.show(MeiShiCommentMain.this, null, null);
		}
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String doInBackground(Object... params){
			// TODO Auto-generated method stub
			String url = Config.MS_DISH_COMENT_MAIN_URL + params[0];
			Log.e("url", url);
			String data =new Tools().getURL(url);
			System.out.println(data);
			String code = "";
			try{
				JSONObject job = new JSONObject(data);
				code = job.getString("code");
				if(code.equals("1")){
					HashMap<String, String> hashMap = new HashMap<String, String>();
					JSONObject job1 = job.getJSONObject("result");
					hashMap.put("id", job1.getString("id"));
					hashMap.put("n_title", job1.getString("n_title"));
					hashMap.put("uid", job1.getString("uid"));
					hashMap.put("n_pic", job1.getString("n_pic"));
					hashMap.put("n_contents", job1.getString("n_contents"));
					hashMap.put("n_dj", job1.getString("n_dj"));
					hashMap.put("n_zxs", job1.getString("n_zxs"));
					
					String pUrl = Config.DOMAIN + hashMap.get("n_pic");
					URL url1 = new URL(pUrl);
					Bitmap bmp = BitmapFactory.decodeStream(url1.openStream());
					drawable = new BitmapDrawable(bmp);
					
					Message msg = handler.obtainMessage();
					msg.what = 1;
					msg.obj = hashMap;
					handler.sendMessage(msg);
				}
			}catch(JSONException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(MalformedURLException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(IOException e){
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
			if(result.equals("1")){
				new CommentAsync().execute(uid, id);
			}
		}
		
	}
	
	class CommentAsync extends AsyncTask<Object, Void, Void>{

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Void doInBackground(Object... params){
			// TODO Auto-generated method stub
			String url = Config.MS_DISH_COMENT_LIST_URL + params[0] + "&spid=" + params[1];
			Log.e("url", url);
			String data = new Tools().getURL(url);
			System.out.println(data);
			cList = new ArrayList<HashMap<String,String>>();
			try{
				JSONObject job = new JSONObject(data);
				String code = job.getString("code");
				if(code.equals("1")){
					JSONArray jArray = job.getJSONArray("result");
					for(int i=0,j=jArray.length();i<j;i++){
						JSONObject job1 = jArray.optJSONObject(i);
						HashMap<String, String> hashMap = new HashMap<String, String>();
						hashMap.put("id", job1.getString("id"));
						hashMap.put("nr", job1.getString("nr"));
						hashMap.put("u_wxname", job1.getString("u_wxname"));
						hashMap.put("xj", job1.getString("xj"));
						hashMap.put("addtime", job1.getString("addtime"));
						
						cList.add(hashMap);
						
						Message msg = handler.obtainMessage();
						msg.what = 2;
						msg.obj = cList;
						handler.sendMessage(msg);
					}
				}
			}catch(JSONException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Void result){
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
		}
	}
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg){
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
				case 1:
					HashMap<String, String> hashMap = (HashMap<String, String>) msg.obj;
					topBg.setBackgroundDrawable(drawable);
					String oc = hashMap.get("n_contents");
					if(oc == null){
						overallCom.setText("赞无评价");
					}else{
						overallCom.setText(oc);
					}
					dName.setText(hashMap.get("n_title"));
					rate = Float.valueOf(hashMap.get("n_zxs"));
					overallRb.setRating(rate);
					overallRating.setText(rate + "分");
				break;
				case 2:
					ArrayList<HashMap<String, String>> list = (ArrayList<HashMap<String, String>>) msg.obj;
					HashMap<String, String> hashMap1 = list.get(0);
					Float rate = Float.valueOf(hashMap1.get("xj"));
					rb.setRating(rate);
					date.setText(hashMap1.get("addtime"));
					comment.setText(hashMap1.get("nr"));
					name.setText(hashMap1.get("u_wxname"));
					break;
				default:
				break;
			}
		}
		
	};
	
	private void preperImageLoader(){

		/******************* 配置ImageLoder ***********************************************/
		File cacheDir =
				StorageUtils.getOwnCacheDirectory(
						MeiShiCommentMain.this,
						"imageloader/Cache");

		ImageLoaderConfiguration config =
				new ImageLoaderConfiguration.Builder(
						MeiShiCommentMain.this)
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
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed(){
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v){
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.meishi_comment_check_all:
			Intent intent = new Intent(MeiShiCommentMain.this, CanTingAllComments.class);
			intent.putExtra("rate", rate);
			intent.putExtra("list", cList);
			intent.putExtra("type", "2");
			startActivity(intent);
			break;
			case R.id.meishi_comment_back:
				finish();
				break;
			case R.id.meishi_comment_order:
				
				break;
			default:
			break;
		}
	}
}
