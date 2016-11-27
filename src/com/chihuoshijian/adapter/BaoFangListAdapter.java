/**
 * 
 */
package com.chihuoshijian.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.baiduapi.map.BaiDuMap;
import com.chihuoshijian.R;
import com.chihuoshijian.adapter.CanTingListAdapter.ViewHolder;
import com.main.baofang.BaoFangList;
import com.main.baofang.BaoFangReserveInfo;
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
public class BaoFangListAdapter extends BaseAdapter{

	private Context								context;
	private ArrayList<HashMap<String, String>>	list;
	private LayoutInflater						lInflater;

	public BaoFangListAdapter(Context context,
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
							R.layout.main_list_item, null);
			vHolder = new ViewHolder();
			convertView.setTag(vHolder);
		}else{
			vHolder = (ViewHolder) convertView.getTag();
		}
		final HashMap<String, String> hashMap =
				list.get(position);
		final String url =
				Config.DOMAIN + hashMap.get("w_logo_pic");
		final float rate = Float.valueOf(hashMap.get("w_fs"));

		vHolder.gone =
				(LinearLayout) convertView
						.findViewById(R.id.main_list_item_order);
		vHolder.pic =
				(ImageView) convertView
						.findViewById(R.id.list_item_pic);
		vHolder.name =
				(TextView) convertView
						.findViewById(R.id.list_item_name);
		vHolder.rating =
				(TextView) convertView
						.findViewById(R.id.list_item_rating);
		vHolder.rb =
				(RatingBar) convertView
						.findViewById(R.id.list_item_ratingbar);
		vHolder.price =
				(TextView) convertView
						.findViewById(R.id.list_item_average);
		vHolder.distance =
				(TextView) convertView
						.findViewById(R.id.list_item_range);
		vHolder.address =
				(TextView) convertView
						.findViewById(R.id.list_item_address);
		vHolder.phone =
				(ImageView) convertView
						.findViewById(R.id.list_item_phone);
		vHolder.map =
				(LinearLayout) convertView
						.findViewById(R.id.list_item_map);
		vHolder.order =
				(TextView) convertView
						.findViewById(R.id.main_list_item_submit);
		vHolder.count =
				(TextView) convertView
						.findViewById(R.id.main_list_item_order_count);
		vHolder.bfCount =
				(LinearLayout) convertView
						.findViewById(R.id.baofang_count_ll);

		ImageLoader.getInstance().displayImage(url, vHolder.pic);
		vHolder.name.setText(hashMap.get("w_title"));
		vHolder.rb.setRating(rate);
		vHolder.rb.setClickable(false);
		vHolder.rating.setText(rate + "分");
		vHolder.price.setText("人均消费" + hashMap.get("w_jj"));
		if(Double.valueOf(hashMap.get("w_ms")) == 0){
			vHolder.distance.setText("无GPS");
		}else{
			vHolder.distance.setText("<"
					+ hashMap.get("w_ms") + "m");
		}
		vHolder.address.setText(hashMap.get("w_dz"));
		vHolder.count.setText(hashMap.get("w_bf"));
		vHolder.phone
				.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v){
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						; // 关键之处
						intent.setAction("android.intent.action.DIAL");
						intent.setData(Uri.parse("tel:"
								+ hashMap.get("w_dh")));
						context.startActivity(intent);
					}
				});
		vHolder.map
				.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v){
						// TODO Auto-generated method stub
						double lat =
								Double.valueOf(hashMap
										.get("w_latitude"));
						double lon =
								Double.valueOf(hashMap
										.get("w_longitude"));
						Log.e("lat", "" + lat);
						Log.e("lon", "" + lon);
						Intent intent =
								new Intent(context,
										BaiDuMap.class);
						intent.putExtra("lat", lat);
						intent.putExtra("lon", lon);
						intent.putExtra("title",
								hashMap.get("w_title"));
						context.startActivity(intent);
					}
				});
		vHolder.order
				.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v){
						// TODO Auto-generated method stub
						Intent intent =
								new Intent(
										context,
										BaoFangReserveInfo.class);
						intent.putExtra("uid", hashMap.get("uid"));
						intent.putExtra("id", "");
						intent.putExtra("picAddr", url);
						intent.putExtra("name", hashMap.get("w_title"));
						intent.putExtra("rate", rate);
						intent.putExtra("average", "人均消费" + hashMap.get("w_jj"));
						context.startActivity(intent);
					}
				});
		vHolder.bfCount.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, BaoFangList.class);
				intent.putExtra("uid", hashMap.get("uid"));

				intent.putExtra("picAddr", url);
				intent.putExtra("name", hashMap.get("w_title"));
				intent.putExtra("rate", rate);
				intent.putExtra("average", "人均消费" + hashMap.get("w_jj"));
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
		TextView		distance;
		LinearLayout	map;
		TextView		address;
		ImageView		phone;
		LinearLayout	gone;
		TextView		order;
		TextView		count;
		LinearLayout	bfCount;
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
