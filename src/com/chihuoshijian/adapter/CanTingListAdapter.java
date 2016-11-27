/**
 * 
 */
package com.chihuoshijian.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.baidu.mapapi.model.LatLng;
import com.baiduapi.map.BaiDuMap;
import com.chihuoshijian.R;
import com.main.canting.CanTingMainListPage;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
public class CanTingListAdapter extends BaseAdapter{
	
	private Context context;
	private ArrayList<HashMap<String, String>> list;
	private LayoutInflater lInflater;
	

	public CanTingListAdapter(Context context,
			ArrayList<HashMap<String, String>> list){
		super();
		this.context = context;
		this.list = list;
		this.lInflater =
				(LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		preperImageLoader();
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
			convertView = lInflater.inflate(R.layout.main_list_item, null);
			vHolder = new ViewHolder();
			convertView.setTag(vHolder);
		}else{
			vHolder = (ViewHolder) convertView.getTag();
		}
		final HashMap<String, String> hashMap = list.get(position);
		String url = Config.DOMAIN + hashMap.get("w_logo_pic");
		String pl = hashMap.get("w_pl");
		String xj = hashMap.get("w_xj");
		float rate = 0;
		if(Integer.valueOf(pl) != 0){
			rate = Integer.valueOf(xj) / Integer.valueOf(pl);
		}else{
			rate = 0;
		}
		
		vHolder.gone = (LinearLayout) convertView.findViewById(R.id.main_list_item_order);
		vHolder.pic = (ImageView) convertView.findViewById(R.id.list_item_pic);
		vHolder.name = (TextView) convertView.findViewById(R.id.list_item_name);
		vHolder.rating = (TextView) convertView.findViewById(R.id.list_item_rating);
		vHolder.rb = (RatingBar) convertView.findViewById(R.id.list_item_ratingbar);
		vHolder.price = (TextView) convertView.findViewById(R.id.list_item_average);
		vHolder.distance = (TextView) convertView.findViewById(R.id.list_item_range);
		vHolder.address = (TextView) convertView.findViewById(R.id.list_item_address);
		vHolder.phone = (ImageView) convertView.findViewById(R.id.list_item_phone);
		vHolder.map = (LinearLayout) convertView.findViewById(R.id.list_item_map);
		
		vHolder.gone.setVisibility(View.GONE);
		//ImageLoader.getInstance().displayImage(url, vHolder.pic);
		vHolder.name.setText(hashMap.get("w_title"));
		vHolder.rb.setRating(rate);
		vHolder.rb.setClickable(false);
		vHolder.rating.setText(rate + "��");
		vHolder.price.setText("�˾�����" + hashMap.get("w_jj"));
		if(Double.valueOf(hashMap.get("w_ms")) == 0){
			vHolder.distance.setText("��GPS");
		}else{
			vHolder.distance.setText("<" + hashMap.get("w_ms") + "m");
		}
		vHolder.address.setText(hashMap.get("w_dz"));
		vHolder.phone.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  ; //�ؼ�֮��
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
	
	private void preperImageLoader(){
		
		
		/******************* ����ImageLoder ***********************************************/
		File cacheDir =
					StorageUtils.getOwnCacheDirectory(context,
								"imageloader/Cache");

		ImageLoaderConfiguration config =
					new ImageLoaderConfiguration.Builder(context)
								.denyCacheImageMultipleSizesInMemory()
								.discCache(new UnlimitedDiscCache(cacheDir))// �Զ��建��·��
								.build();// ��ʼ����
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory().cacheOnDisc()
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
		
		ImageLoader.getInstance().init(config);// ȫ�ֳ�ʼ��������
		/*********************************************************************************/
	}
	
	class ViewHolder{
		ImageView pic;
		TextView name;
		RatingBar rb;
		TextView rating;
		TextView price;
		TextView distance;
		LinearLayout map;
		TextView address;
		ImageView phone;
		LinearLayout gone;
	}

}