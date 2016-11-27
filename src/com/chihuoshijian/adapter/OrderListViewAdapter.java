/**
 * 
 */
package com.chihuoshijian.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.chihuoshijian.R;
import com.model.OrderItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Liming Chu
 * 
 * @param
 * @return
 */
public class OrderListViewAdapter extends BaseAdapter{

	private Context								context;
	private ArrayList<OrderItem>	orderlist;
	private LayoutInflater						inflater	=
																	null;

	public OrderListViewAdapter(Context context,
								ArrayList<OrderItem> orderlist){
		super();
		this.context = context;
		this.orderlist = orderlist;
		this.inflater =
				(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount(){
		// TODO Auto-generated method stub
		return orderlist == null ? 0 : orderlist.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position){
		// TODO Auto-generated method stub
		return orderlist.get(position);
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
		ViewHolder vHolder = new ViewHolder();
		convertView =
				inflater.inflate(R.layout.order_detail_add_item1, null);

		vHolder.name =
				(TextView) convertView.findViewById(R.id.order_detail_lv_item_name);
		vHolder.count =
				(TextView) convertView.findViewById(R.id.order_detail_lv_item_count);
		vHolder.price =
				(TextView) convertView.findViewById(R.id.order_detail_lv_item_price);
		vHolder.name.setText(orderlist.get(position).getName());
		vHolder.count.setText(orderlist.get(position).getCount());
		vHolder.price.setText(orderlist.get(position).getPrice());
		
		return convertView;
	}

	class ViewHolder{
		TextView	name;
		TextView	count;
		TextView	price;
	}

}
