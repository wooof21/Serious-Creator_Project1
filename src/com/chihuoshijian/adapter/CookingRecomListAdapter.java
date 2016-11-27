/**
 * 
 */
package com.chihuoshijian.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.chihuoshijian.R;
import com.chihuoshijian.adapter.BaoFangListAdapter.ViewHolder;
import com.main.baofang.DishOrderActivity;
import com.main.canting.CanTingDetailActivity;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.content.Context;
import android.content.Intent;
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
public class CookingRecomListAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<HashMap<String, String>> list;
	private LayoutInflater						lInflater;
	
	public CookingRecomListAdapter(Context context,
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
			convertView =
					lInflater.inflate(
							R.layout.cooking_recommendation_list_item, null);
			vHolder = new ViewHolder();
			convertView.setTag(vHolder);
		}else{
			vHolder = (ViewHolder) convertView.getTag();
		}
		vHolder.pic = (ImageView)convertView.findViewById(R.id.cooking_recommendation_list_item_pic);
		vHolder.name = (TextView)convertView.findViewById(R.id.cooking_recommendation_list_item_name);
		vHolder.price = (TextView)convertView.findViewById(R.id.cooking_recommendation_list_item_price);
		vHolder.rb = (RatingBar)convertView.findViewById(R.id.cooking_recommendation_list_item_ratingbar);
		vHolder.rating = (TextView)convertView.findViewById(R.id.cooking_recommendation_list_item_rating);
		vHolder.order =(TextView)convertView.findViewById(R.id.cooking_recommendation_list_item_order);
		final HashMap<String, String> hashMap =
				list.get(position);
		
		vHolder.name.setText(hashMap.get("n_title"));
		vHolder.price.setText("￥" + hashMap.get("n_dj"));
		float xj = Float.valueOf(hashMap.get("n_xj"));
		float pl = Float.valueOf(hashMap.get("n_caipin_pl"));
		float rate = 0;
		if(xj == 0){
			rate = 0;
		}else{
			rate = xj / pl;
		}
		vHolder.rb.setRating(rate);
		vHolder.rating.setText(rate + "分");
		
		vHolder.order.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				Intent intent =
						new Intent(
								context,
								DishOrderActivity.class);
				intent.putExtra("uid", hashMap.get("uid"));
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
		TextView		order;
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
