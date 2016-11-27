/**
 * 
 */
package com.order;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.chihuoshijian.R;
import com.chihuoshijian.adapter.OrderListViewAdapter;
import com.chihuoshijian.view.OrderListView;
import com.main.baofang.AddMenuActivity;
import com.main.baofang.DishOrderActivity;
import com.model.ItemEntity;
import com.model.OrderItem;
import com.tools.Config;
import com.tools.Tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Liming Chu
 * 
 * @param
 * @return
 */
public class OrderDetailActivity extends Activity implements
	OnClickListener{

	private OrderListView			oListView;
	private View					header;
	private View					legs;
	private View					footer;
	private int						headerHeight;

	private TextView				back;
	private TextView				submit;
	private LinearLayout			orderInfo;
	private int						totalHeight;
	private int						legHeight;
	private ArrayList<OrderItem>	orderlist;
	private OrderListViewAdapter	olvAdapter;
	private Animation				anim;
	private LinearLayout			ll;

	private String					title;
	private int						totalCount;
	private double					totalPrice;
	private String					orderId;
	private String					orderData;
	private ArrayList<ItemEntity>	list;
	private String					uid;

	private ProgressDialog			progressDialog;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_detail);
		prepareView();

		title = getIntent().getExtras().getString("title");
		orderId =
				getIntent().getExtras().getString("orderId");
		orderData =
				getIntent().getExtras().getString("orderData");
		totalCount =
				getIntent().getExtras().getInt("totalCount");
		totalPrice =
				getIntent().getExtras().getDouble("totalPrice");
		orderlist =
				(ArrayList<OrderItem>) getIntent().getExtras().getSerializable("itemList");
		list =
				(ArrayList<ItemEntity>) getIntent().getExtras().getSerializable("cartList");
		uid = getIntent().getExtras().getString("uid");
		initHeader();
		initLegs();
		iniFooter();

		new Handler().postDelayed(new Runnable(){

			@Override
			public void run(){
				// TODO Auto-generated method stub
				initListview();
			}
		}, 1000);

	}

	private void prepareView(){
		back =
				(TextView) findViewById(R.id.order_detail_back_tv);
		submit =
				(TextView) findViewById(R.id.order_detail_submit_tv);
		orderInfo =
				(LinearLayout) findViewById(R.id.order_detail_order_info);
		oListView =
				(OrderListView) findViewById(R.id.order_detail_lv);

		back.setOnClickListener(this);
		submit.setOnClickListener(this);

	}

	private void initHeader(){
		header =
				LayoutInflater.from(getApplicationContext()).inflate(R.layout.order_detail_add_menu_item_header, null);
		TextView name =
				(TextView) header.findViewById(R.id.order_detail_rest_name);
		name.setText(title);
		TextView id =
				(TextView) header.findViewById(R.id.order_detail_order_id);
		id.setText(orderId);
		SimpleDateFormat sdf =
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TextView dt =
				(TextView) header.findViewById(R.id.order_detail_date_time);
		dt.setText(sdf.format(Calendar.getInstance().getTime()));

		measureView(header);
		headerHeight = header.getMeasuredHeight();
		Log.e("head height", "" + headerHeight);
		header.setPadding(0, -1 * headerHeight, 0, 0);
		header.invalidate();
		oListView.addHeaderView(header, null, false);
	}

	private void initLegs(){
		legs =
				LayoutInflater.from(getApplicationContext()).inflate(R.layout.order_detail_add_item2, null);
		TextView tCount =
				(TextView) legs.findViewById(R.id.order_detail_legs_total_count);
		tCount.setText(totalCount + "份");
		TextView tPrice =
				(TextView) legs.findViewById(R.id.order_detail_legs_total_price);
		tPrice.setText("￥" + totalPrice);
		measureView(legs);
		legHeight = legs.getMeasuredHeight();
		Log.e("leg height", "" + legHeight);
	}

	private void iniFooter(){
		footer =
				LayoutInflater.from(getApplicationContext()).inflate(R.layout.order_footer, null);
		ll = new LinearLayout(this);
		LayoutParams lp =
				new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		ll.setLayoutParams(lp);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.addView(legs);
		ll.setPadding(0, -1 * legHeight, 0, 0);
		ll.addView(footer);
		ll.invalidate();

		oListView.addFooterView(ll);
		oListView.setAdapter(null);
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

	// 此方法直接照搬自网络上的一个下拉刷新的demo，此处是“估计”headView的width以及height
	private void measureView(View child){
		ViewGroup.LayoutParams params =
				child.getLayoutParams();
		if(params == null){
			params =
					new ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.FILL_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec =
				ViewGroup.getChildMeasureSpec(0, 0 + 0, params.width);
		int lpHeight = params.height;
		int childHeightSpec;
		if(lpHeight > 0){
			childHeightSpec =
					MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		}else{
			childHeightSpec =
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	private void initListview(){
		olvAdapter =
				new OrderListViewAdapter(
					getApplicationContext(), orderlist);
		oListView.setAdapter(olvAdapter);
		setListViewHeightBasedOnChildren(oListView);
		startAnimation();
	}

	/**
	 * 启动打印订单动画
	 */
	private void startAnimation(){
		// cListview.layout(0, 0, -layout_order_info.getBottom(), 0);
		// height = height + mAdapter.getCount() *
		// BaseTools.dip2px(getApplicationContext(), 70);
		// height = height + (cListview.getDividerHeight() *
		// (cListview.getCount()));//总高度加上分割线高度
		anim =
				new TranslateAnimation(0.0f, 0.0f,
					-totalHeight, 0);
		anim.setDuration(3000);
		anim.setFillAfter(true);
		anim.setAnimationListener(new AnimationListener(){

			@Override
			public void
				onAnimationStart(Animation animation){
				// TODO Auto-generated method stub
				header.setPadding(0, 0, 0, 0);
				ll.setPadding(0, 0, 0, 0);
			}

			@Override
			public void
				onAnimationRepeat(Animation animation){
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation){
				// TODO Auto-generated method stub
				oListView.clearAnimation();
			}
		});
		oListView.startAnimation(anim);
	}

	private void
		setListViewHeightBasedOnChildren(ListView listView){
		ListAdapter listAdapter = listView.getAdapter();
		if(listAdapter == null){
			return;
		}
		totalHeight = 0;
		// 由于ADD了个footer，所以总量减去1
		Log.d("listAdapter.getCount()", ""
				+ listAdapter.getCount());
		for(int i = 0,len = listAdapter.getCount() - 2;i < len;i++){
			View listItem =
					listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
			Log.d("getMeasuredHeight", ""
					+ listItem.getMeasuredHeight());
		}

		totalHeight =
				totalHeight
						+ (listView.getDividerHeight() * (listAdapter.getCount() - 2));
		// ViewGroup.LayoutParams params = listView.getLayoutParams();
		// params.height = totalHeight + (listView.getDividerHeight() *
		// (listAdapter.getCount() - 1));
		// listView.setLayoutParams(params);
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
			case R.id.order_detail_back_tv:
				finish();
			break;
			case R.id.order_detail_submit_tv:
				new AsyncTask<Void, Void, String>(){

					@Override
					protected void onPreExecute(){
						// TODO Auto-generated method stub
						super.onPreExecute();
						progressDialog =
								ProgressDialog.show(OrderDetailActivity.this, null, null);
					}

					@Override
					protected String
						doInBackground(Void... params){
						// TODO Auto-generated method stub
						String code = "";
						try{
							String data =
									new Tools().doPostData(Config.DISH_SUBMIT_ORDER_URL, orderData);
							System.out.println(data);

							JSONObject job =
									new JSONObject(data);
							code = job.getString("code");
						}catch(IOException e){
							// TODO Auto-generated catch block
							e.printStackTrace();
						}catch(JSONException e){
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						return code;
					}

					@Override
					protected void
						onPostExecute(String result){
						// TODO Auto-generated method stub
						super.onPostExecute(result);
						progressDialog.dismiss();
						if(result.equals("1")){
							Toast.makeText(OrderDetailActivity.this, "订单提交成功！", Toast.LENGTH_SHORT).show();
							Intent intent =
									new Intent(
										OrderDetailActivity.this,
										OrderSuccessActivity.class);
							intent.putExtra("orderId", orderId);
							intent.putExtra("totalPrice", totalPrice);
							startActivity(intent);
						}else{
							Toast.makeText(OrderDetailActivity.this, "订单提交失败！", Toast.LENGTH_SHORT).show();
						}
					}

				}.execute();
			break;
			default:
			break;
		}
	}
}
