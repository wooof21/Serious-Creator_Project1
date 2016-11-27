/**
 * 
 */
package com.chihuoshijian.adapter;

import java.util.HashMap;

import java.util.ArrayList;

import com.baidu.navi.location.l;
import com.chihuoshijian.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author Liming Chu
 *
 * @param
 * @return
 */
public class BaoFangInfoListAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<HashMap<String, String>> list;
	private LayoutInflater lInflater;
	
	public BaoFangInfoListAdapter(Context context,
			ArrayList<HashMap<String, String>> list){
		super();
		this.context = context;
		this.list = list;
		this.lInflater = LayoutInflater.from(context);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount(){
		// TODO Auto-generated method stub
		return list.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position){
		// TODO Auto-generated method stub
		return list.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position){
		// TODO Auto-generated method stub
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView,
			ViewGroup parent){
		// TODO Auto-generated method stub
		ViewHolder vHolder = null;
		if(convertView == null){
			convertView = lInflater.inflate(R.layout.baofang_list_item, null);
			vHolder = new ViewHolder();
			convertView.setTag(vHolder);
		}else{
			vHolder = (ViewHolder) convertView.getTag();
		}
		HashMap<String, String> hashMap = list.get(position);
		
		vHolder.name = (TextView)convertView.findViewById(R.id.baofang_list_item_name);
		vHolder.count = (TextView)convertView.findViewById(R.id.baofang_list_item_count);
		vHolder.total = (TextView)convertView.findViewById(R.id.baofang_list_item_total);
		
		vHolder.name.setText(hashMap.get("name"));
		vHolder.count.setText(hashMap.get("count"));
		vHolder.total.setText("¼ä(¹²" + hashMap.get("total") + "¼ä)");
		return convertView;
	}
	class ViewHolder{
		TextView name;
		TextView count;
		TextView total;
	}

}
