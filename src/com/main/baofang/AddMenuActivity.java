/**
 * 
 */
package com.main.baofang;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.chihuoshijian.R;
import com.chihuoshijian.adapter.AddMenuAdapter;
import com.model.ItemEntity;
import com.model.OrderItem;
import com.order.OrderDetailActivity;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Liming Chu
 * 
 * @param
 * @return
 */
public class AddMenuActivity extends Activity implements
	OnClickListener{

	private ListView				lv;
	private TextView				add;
	private TextView				submit;
	private TextView				dCount;
	private TextView				totalPrice;

	private double					tPrice;
	private int						tCount;
	private ArrayList<ItemEntity>	list;
	private String					uid;
	
	
	private String title;
	private String orderId;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_menu);
		prepareView();

		list =
				(ArrayList<ItemEntity>) getIntent().getExtras().getSerializable("cartList");

		tPrice =
				getIntent().getExtras().getDouble("totalPrice");
		tCount =
				getIntent().getExtras().getInt("totalCount");
		uid = getIntent().getExtras().getString("uid");
		title = getIntent().getExtras().getString("title");
		
		dCount.setText(tCount + "·Ý");
		totalPrice.setText("£¤" + tPrice);

		AddMenuAdapter adapter =
				new AddMenuAdapter(this, list, handler);
		lv.setAdapter(adapter);
	}

	private void prepareView(){
		lv = (ListView) findViewById(R.id.add_menu_lv);
		add =
				(TextView) findViewById(R.id.add_menu_add_dish);
		submit =
				(TextView) findViewById(R.id.add_menu_submit);
		dCount =
				(TextView) findViewById(R.id.add_menu_dish_count);
		totalPrice =
				(TextView) findViewById(R.id.add_menu_total_price);

		add.setOnClickListener(this);
		submit.setOnClickListener(this);
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
											list =
													new ArrayList<ItemEntity>();
											list =
													(ArrayList<ItemEntity>) msg.obj;
											for(int i = 0,j =
													list.size();i < j;i++){
												System.out.println(list.get(i).getTitle());
												System.out.println(list.get(i).getItemCount());
											}
										break;
										case 2:
											double price =
													(Double) msg.obj;
											if(price > 0){
												tCount++;
											}else{
												tCount--;
											}
											tPrice += price;
											dCount.setText(tCount
													+ "·Ý");
											totalPrice.setText("£¤"
													+ tPrice);
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

	private String orderDetail(){
		StringBuilder sb = new StringBuilder();
		int i, j;
		sb.append("userid=");
		sb.append("19");
		sb.append("&uid=");
		sb.append(uid);
		sb.append("&num=");
		sb.append("N" + uid + "19" + System.currentTimeMillis());
		orderId = "N" + uid + "19" + System.currentTimeMillis();
		sb.append("&je=");
		sb.append(tPrice);
		sb.append("&buy=");
		for(i=0,j=list.size();i<j;i++){
			sb.append(list.get(i).getId());
			sb.append(",");
			sb.append(list.get(i).getItemCount());
			sb.append(",");
			sb.append(list.get(i).getPrice());
			if(i < list.size() - 1){
				sb.append("||");
			}
		}
		System.out.println(sb.toString());
		return sb.toString();
	}

	private ArrayList<OrderItem> getItemList(){
		ArrayList<OrderItem> itemList = new ArrayList<OrderItem>();
		for(int i=0,j=list.size();i<j;i++){
			OrderItem order = new OrderItem();
			order.setName(list.get(i).getTitle());
			order.setCount("x" + list.get(i).getItemCount());
			order.setPrice("£¤" + list.get(i).getPrice());
			
			itemList.add(order);
		}
		
		return itemList;
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
			case R.id.add_menu_add_dish:
				intent = getIntent();
				intent.putExtra("cartList", list);
				setResult(1000, intent);
				finish();
			break;
			case R.id.add_menu_submit:
				String orderData = orderDetail();
				intent = new Intent(AddMenuActivity.this, OrderDetailActivity.class);
				intent.putExtra("title", title);
				intent.putExtra("orderId", orderId);
				intent.putExtra("orderData", orderData);
				intent.putExtra("totalCount", tCount);
				intent.putExtra("totalPrice", tPrice);
				intent.putExtra("itemList", getItemList());
				intent.putExtra("cartList", list);
				intent.putExtra("uid", uid);
				startActivity(intent);
			break;
			default:
			break;
		}
	}

}
