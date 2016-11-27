/**
 * 
 */
package com.chihuoshijian.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.chihuoshijian.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Liming Chu
 *
 * @param
 * @return
 */
public class VipBaoFangOrderAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<HashMap<String, String>> list;
	private LayoutInflater lInflater;
	
	public VipBaoFangOrderAdapter(Context context,
			ArrayList<HashMap<String, String>> list){
		super();
		this.context = context;
		this.list = list;
		this.lInflater = LayoutInflater.from(context);
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
			convertView = lInflater.inflate(R.layout.order_center_baofang_item, null);
			vHolder = new ViewHolder();
			convertView.setTag(vHolder);
		}else{
			vHolder = (ViewHolder) convertView.getTag();
		}
		
		vHolder.pic = (ImageView)convertView.findViewById(R.id.order_center_baofang_item_pic);
		vHolder.name = (TextView)convertView.findViewById(R.id.order_center_baofang_item_name);
		vHolder.roomType = (TextView)convertView.findViewById(R.id.order_center_baofang_item_room_type_tv);
		vHolder.date = (TextView)convertView.findViewById(R.id.order_center_baofang_item_room_date_tv);
		vHolder.button = (TextView)convertView.findViewById(R.id.order_center_baofang_item_button);
		
		HashMap<String, String> hashMap = list.get(position);
		String status = hashMap.get("zt");
		
		vHolder.name.setText(hashMap.get("w_title"));
		vHolder.roomType.setText(hashMap.get("w_bf"));
		vHolder.date.setText(hashMap.get("ydsj"));
		if(status.equals("0")){
			vHolder.button.setText("等待商家确认预定");
			vHolder.button.setTag("0");
		}else if(status.equals("1")){
			vHolder.button.setText("商家确认，等待用餐");
			vHolder.button.setTag("1");
		}else{
			vHolder.button.setText("再次预定");
			vHolder.button.setTag("2");
		}
		
		vHolder.button.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				Log.e("tag", v.getTag().toString());
			}
		});
		return convertView;
	}
	
	class ViewHolder{
		ImageView pic;
		TextView name;
		TextView roomType;
		TextView date;
		TextView button;
		
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
