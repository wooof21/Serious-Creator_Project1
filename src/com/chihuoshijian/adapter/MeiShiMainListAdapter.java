/**
 * 
 */
package com.chihuoshijian.adapter;

import java.io.File;
import java.util.HashMap;

import java.util.ArrayList;

import com.baiduapi.map.BaiDuMap;
import com.chihuoshijian.R;
import com.chihuoshijian.adapter.MainPageListAdapter.ViewHolder;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tools.Config;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * @author Liming Chu
 * 
 * @param
 * @return
 */
public class MeiShiMainListAdapter extends BaseAdapter{

	private Context								context;
	private ArrayList<HashMap<String, String>>	list;
	private LayoutInflater						lInflater;

	public MeiShiMainListAdapter(Context context,
			ArrayList<HashMap<String, String>> list){
		super();
		this.context = context;
		this.list = list;
		this.lInflater =
				(LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		preperImageLoader();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount(){
		// TODO Auto-generated method stub
		return list.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position){
		// TODO Auto-generated method stub
		return list.get(position);
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
	public View getView(int position, View convertView,
			ViewGroup parent){
		// TODO Auto-generated method stub
		ViewHolder vHolder = null;
		if(convertView == null){
			convertView =
					lInflater.inflate(
							R.layout.meishi_main_list_item,
							null);
			vHolder = new ViewHolder();
			convertView.setTag(vHolder);
		}else{
			vHolder = (ViewHolder) convertView.getTag();
		}
		final HashMap<String, String> hashMap =
				list.get(position);
		String url = Config.DOMAIN + hashMap.get("w_pic");
		float rate = Float.valueOf(hashMap.get("w_xj"));

		vHolder.name =
				(TextView) convertView
						.findViewById(R.id.meishi_main_list_item_name);
		vHolder.pic =
				(ImageView) convertView
						.findViewById(R.id.meishi_main_list_item_pic);
		vHolder.rb =
				(RatingBar) convertView
						.findViewById(R.id.meishi_main_list_item_ratingbar);
		vHolder.rating =
				(TextView) convertView
						.findViewById(R.id.meishi_main_list_item_rating);
		vHolder.price =
				(TextView) convertView
						.findViewById(R.id.meishi_main_list_item_price);
		vHolder.fdName =
				(TextView) convertView
						.findViewById(R.id.meishi_main_list_item_fd_name);
		vHolder.distance =
				(TextView) convertView
						.findViewById(R.id.meishi_main_list_item_range);
		vHolder.map =
				(LinearLayout) convertView
						.findViewById(R.id.meishi_main_list_item_map);
		vHolder.address =
				(TextView) convertView
						.findViewById(R.id.meishi_main_list_item_address);
		vHolder.average =
				(TextView) convertView
						.findViewById(R.id.meishi_main_list_item_average);
		vHolder.phone =
				(ImageView) convertView
						.findViewById(R.id.meishi_main_list_item_phone);

		// ImageLoader.getInstance().displayImage(url, vHolder.pic);
		vHolder.name.setText(hashMap.get("n_title"));
		vHolder.fdName.setText(hashMap.get("w_title"));
		vHolder.rb.setRating(rate);
		vHolder.rb.setClickable(false);
		vHolder.rating.setText(rate + "分");
		vHolder.price.setText("人均消费" + hashMap.get("w_jj"));
		vHolder.price.setText("￥"+hashMap.get("n_dj"));
		vHolder.address.setText(hashMap.get("w_dz"));
		vHolder.phone.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  ; //关键之处
				intent.setAction("android.intent.action.DIAL");
				intent.setData(Uri.parse("tel:" + hashMap.get("w_dh")));
				context.startActivity(intent);
			}
		});
		vHolder.map.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				double lat = Double.valueOf(hashMap.get("w_latitude"));
				double lon = Double.valueOf(hashMap.get("w_longitude"));
				Log.e("lat", ""+lat);
				Log.e("lon", ""+lon);
				Intent intent = new Intent(context, BaiDuMap.class);
				intent.putExtra("lat", lat);
				intent.putExtra("lon", lon);
				intent.putExtra("title", hashMap.get("w_title"));
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	class ViewHolder{
		ImageView		pic;
		TextView		name;
		RatingBar		rb;
		TextView		rating;
		TextView		price;
		TextView		fdName;
		TextView		distance;
		LinearLayout	map;
		TextView		address;
		ImageView		phone;
		TextView		average;
	}

	private void preperImageLoader(){

		/******************* 配置ImageLoder ***********************************************/
		File cacheDir =
				StorageUtils.getOwnCacheDirectory(context,
						"imageloader/Cache");

		ImageLoaderConfiguration config =
				new ImageLoaderConfiguration.Builder(
						context)
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

}
