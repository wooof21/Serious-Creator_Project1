/**
 * 
 */
package com.chihuoshijian.adapter;

import java.io.File;
import java.util.ArrayList;

import com.chihuoshijian.R;
import com.model.ItemEntity;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tools.Config;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
public class AddMenuAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<ItemEntity>		list;
	private LayoutInflater lInflater;
	private DisplayImageOptions	options;
	private Handler handler;
	
	public AddMenuAdapter(Context context,
			ArrayList<ItemEntity> list, Handler handler){
		super();
		this.context = context;
		this.list = list;
		this.handler = handler;
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
	public View getView(final int position, View convertView,
			ViewGroup parent){
		// TODO Auto-generated method stub
		final ViewHolder vHolder = new ViewHolder();
		if(convertView == null){
			convertView = lInflater.inflate(R.layout.add_menu_item, null);
		}
		vHolder.pic = (ImageView)convertView.findViewById(R.id.add_menu_item_pic);
		vHolder.name = (TextView)convertView.findViewById(R.id.add_menu_item_name);
		vHolder.price = (TextView)convertView.findViewById(R.id.add_menu_item_price);
		vHolder.plus = (TextView)convertView.findViewById(R.id.add_menu_item_plus);
		vHolder.count = (TextView)convertView.findViewById(R.id.add_menu_item_count);
		vHolder.minus = (TextView)convertView.findViewById(R.id.add_menu_item_minus);
		vHolder.delete = (TextView)convertView.findViewById(R.id.add_menu_item_order_delete);
		
		final ItemEntity item = list.get(position);
		String url = Config.DOMAIN = item.getPicAddr();
		System.out.println(url);
		//ImageLoader.getInstance().displayImage(url, vHolder.pic, options);
		
		vHolder.name.setText(item.getTitle());
		vHolder.count.setText(""+item.getItemCount());
		String price = item.getPrice();
		double totalPrice = Double.valueOf(price) * item.getItemCount();
		vHolder.price.setText("￥" + totalPrice);
		
		vHolder.plus.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				item.setItemCount(item.getItemCount() + 1);
				vHolder.count.setText(""+item.getItemCount());
				String price = item.getPrice();
				double totalPrice = Double.valueOf(price) * item.getItemCount();
				vHolder.price.setText("￥" + totalPrice);
				
				Message msg1 = handler.obtainMessage();
				msg1.what = 1;
				msg1.obj = list;
				handler.sendMessage(msg1);
				
				Message msg2 = handler.obtainMessage();
				msg2.what = 2;
				msg2.obj = Double.valueOf(price);
				handler.sendMessage(msg2);
			}
		});
		
		vHolder.minus.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				String price = item.getPrice();
				if((item.getItemCount() - 1) == 0){
					item.setItemCount(1);
				}else{
					item.setItemCount(item.getItemCount() - 1);
					Message msg2 = handler.obtainMessage();
					msg2.what = 2;
					msg2.obj = (-1) * Double.valueOf(price);
					handler.sendMessage(msg2);
				}
				
				vHolder.count.setText(""+item.getItemCount());
				double totalPrice = Double.valueOf(price) * item.getItemCount();
				vHolder.price.setText("￥" + totalPrice);
				
				Message msg1 = handler.obtainMessage();
				msg1.what = 1;
				msg1.obj = list;
				handler.sendMessage(msg1);
				

			}
		});
		vHolder.delete.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				list.remove(position);
				Message msg = handler.obtainMessage();
				msg.what = 1;
				msg.obj = list;
				handler.sendMessage(msg);
				String price = item.getPrice();
				Message msg2 = handler.obtainMessage();
				msg2.what = 2;
				msg2.obj = (-1) * Double.valueOf(price) * item.getItemCount();
				handler.sendMessage(msg2);
				
				notifyDataSetInvalidated();
			}
		});
		
		return convertView;
	}
	
	
	@Override
	public void notifyDataSetInvalidated(){
		// TODO Auto-generated method stub
		super.notifyDataSetInvalidated();
	}


	class ViewHolder{
		ImageView pic;
		TextView name;
		TextView price;
		TextView plus;
		TextView count;
		TextView minus;
		TextView delete;
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

		options =
				new DisplayImageOptions.Builder()
						.cacheInMemory()
						.cacheOnDisc()
						.imageScaleType(
								ImageScaleType.IN_SAMPLE_INT)
						.showImageForEmptyUri(
								R.drawable.imageloader_default)
								.showImageOnFail(R.drawable.imageloader_default)
						.build();

		ImageLoader.getInstance().init(config);// 全局初始化此配置
		/*********************************************************************************/
	}

}
