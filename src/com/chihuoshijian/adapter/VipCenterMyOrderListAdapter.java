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
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * @author Liming Chu
 *
 * @param
 * @return
 */
public class VipCenterMyOrderListAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<HashMap<String, String>> list;
	private LayoutInflater lInflater;
	private Handler rHandler;
	public VipCenterMyOrderListAdapter(Context context,
			ArrayList<HashMap<String, String>> list, Handler rHandler){
		super();
		this.context = context;
		this.list = list;
		this.rHandler = rHandler;
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
			convertView = lInflater.inflate(R.layout.order_center_list_item, null);
			vHolder = new ViewHolder();
			convertView.setTag(vHolder);
		}else{
			vHolder = (ViewHolder) convertView.getTag();
		}
		final HashMap<String, String> hashMap = list.get(position);
		float rate = Float.valueOf(hashMap.get("w_xj"));
		
		vHolder.num = (TextView)convertView.findViewById(R.id.order_center_list_item_order_no);
		vHolder.pic = (ImageView)convertView.findViewById(R.id.order_center_list_item_pic);
		vHolder.name = (TextView)convertView.findViewById(R.id.order_center_list_item_name);
		vHolder.price = (TextView)convertView.findViewById(R.id.order_center_list_item_price);
		vHolder.rb = (RatingBar)convertView.findViewById(R.id.order_center_list_item_ratingbar);
		vHolder.rating = (TextView)convertView.findViewById(R.id.order_center_list_item_rating);
		vHolder.status = (TextView)convertView.findViewById(R.id.order_center_list_item_status);
		
		vHolder.num.setText(hashMap.get("num"));
		vHolder.name.setText(hashMap.get("w_title"));
		vHolder.rb.setRating(rate);
		vHolder.rating.setText(rate+"分");
		vHolder.price.setText("￥" + hashMap.get("je"));
		String zt = hashMap.get("zt");
		String pj = hashMap.get("pj");
		if(zt.equals("1")){
			vHolder.status.setBackgroundResource(R.drawable.corners_bg_blue_sr);
			if(pj.equals("1")){
				vHolder.status.setVisibility(View.GONE);
			}else{
				vHolder.status.setText("评价");
				vHolder.status.setTag("1");
			}
		}else{
			vHolder.status.setBackgroundResource(R.drawable.corners_bg_lred_sr);
			vHolder.status.setText("删除");
			vHolder.status.setTag("2");
		}
		
		vHolder.status.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				if(v.getTag().toString().equals("1")){
					System.out.println("评鉴");
				}else{
					System.out.println("删除");
					Message msg = rHandler.obtainMessage();
					msg.what = 1;
					msg.obj  = hashMap.get("id");
					rHandler.sendMessage(msg);
				}
			}
		});
		return convertView;
	}
	
	class ViewHolder{
		TextView num;
		ImageView pic;
		TextView name;
		RatingBar rb;
		TextView rating;
		TextView price;
		TextView status;
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
