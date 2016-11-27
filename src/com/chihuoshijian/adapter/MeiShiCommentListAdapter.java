/**
 * 
 */
package com.chihuoshijian.adapter;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import com.baidu.location.v;
import com.chihuoshijian.R;
import com.main.canting.CanTingDetailActivity;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tools.Config;
import com.tools.ScrollListView;

import android.R.integer;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * @author Liming Chu
 *
 * @param
 * @return
 */
public class MeiShiCommentListAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<HashMap<String, String>> list;
	private String type;
	private LayoutInflater lInflater;
	private ArrayList<String>	picList;
	
	public MeiShiCommentListAdapter(Context context,
			ArrayList<HashMap<String, String>> list, String type){
		super();
		this.context = context;
		this.list = list;
		this.type = type;
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
			convertView = lInflater.inflate(R.layout.comment_list_item, null);
			vHolder = new ViewHolder();
			convertView.setTag(vHolder);
		}else{
			vHolder = (ViewHolder) convertView.getTag();
		}
		HashMap<String, String> hashMap = list.get(position);
		
		vHolder.name = (TextView)convertView.findViewById(R.id.baofang_order_select_commend_name);
		vHolder.rb = (RatingBar)convertView.findViewById(R.id.baofang_order_select_commend_rb);
		vHolder.date = (TextView)convertView.findViewById(R.id.baofang_order_select_commend_date);
		vHolder.comment = (TextView)convertView.findViewById(R.id.baofang_order_select_commend);
		vHolder.pics = (GridView)convertView.findViewById(R.id.baofang_order_select_commend_pic_gv);
		
		vHolder.name.setText(hashMap.get("u_wxname"));
		vHolder.date.setText(hashMap.get("addtime"));
		vHolder.rb.setRating(Float.valueOf(hashMap.get("xj")));
		vHolder.comment.setText(hashMap.get("nr"));
		
		if(type.equals("1")){
			String pic_all = hashMap.get("pic");
			String[] picStrings = pic_all.split("\\|");
			picList = new ArrayList<String>();
			for(int i=1,j=picStrings.length;i<j;i++){
				System.out.println(i + "  " + picStrings[i]);
				picList.add(picStrings[i]);
			}
			
			GridAdapter gAdapter = new GridAdapter(context, picList);
			ScrollListView.setGridViewHeightBasedOnChildren(vHolder.pics);
			vHolder.pics.setAdapter(gAdapter);
			
			vHolder.pics.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent,
						View view, final int position, long id){
					// TODO Auto-generated method stub
					new AsyncTask<Void, Void, ImageView>(){

						@Override
						protected
						ImageView
								doInBackground(
										Void... params){
							// TODO Auto-generated method stub
							WindowManager wm = ((Activity) context).getWindowManager();
							int width = wm.getDefaultDisplay().getWidth();
							int height = wm.getDefaultDisplay().getHeight();
							Log.e("screen width", width+"");
							float scaleX = 0;
							float scaleY = 0;
							Bitmap bmp = null;
							int pic_w = 0;
							int pic_h = 0;
							try{
								URL url = new URL(Config.DOMAIN + picList.get(position));
								bmp = BitmapFactory.decodeStream(url.openStream());
								pic_w = bmp.getWidth();
								pic_h = bmp.getHeight();
								Log.e("pic_w", pic_w+"");
								Log.e("pic_h", pic_h+"");//
								float scale = (float)width / pic_w;
								Log.e("scale", ""+scale);
								scaleX = (float)width / pic_w;
								scaleY = (float)height / pic_h;
//								if(scale < 1){
//									scaleX = width;
//									scaleY = new Float(scale * pic_h).intValue();
//									Log.e("scaleY1", ""+scaleY);
//								}else{
//									scaleX = new Float(scale * pic_w).intValue();
//									scaleY = new Float(scale * pic_h).intValue();
//									Log.e("scaleY2", ""+scaleY);
//								}
								
								Log.e("scaleX", ""+scaleX);
								Log.e("scaleY3", ""+scaleY);

							}catch(MalformedURLException e){
								// TODO Auto-generated catch block
								e.printStackTrace();
							}catch(IOException e){
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							/* 产生reSize后的Bitmap对象 */
						    Matrix matrix = new Matrix();  
						    if(scaleY > 2){
						    	matrix.postScale(scaleX, 2.25f); 
						    }else{
						    	matrix.postScale(scaleX, 0.75f); 
						    }
						    
						    Bitmap resizeBmp = Bitmap.createBitmap(bmp,0,0,pic_w,
						    		pic_h,matrix,true);
							ImageView image = new ImageView(context);
							image.setImageBitmap(resizeBmp);
							
							return image;
						}

						@Override
						protected
								void
								onPostExecute(
										ImageView result){
							// TODO Auto-generated method stub
							super.onPostExecute(result);
							Dialog dialog;
							dialog = new Dialog(context, R.style.dialog);
							dialog.setContentView(result);
							dialog.show();
							Window win = dialog.getWindow();
							win.getDecorView().setPadding(0, 0, 0, 0);
							WindowManager.LayoutParams lp = win.getAttributes();
							        lp.width = WindowManager.LayoutParams.FILL_PARENT;
							        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
							        win.setAttributes(lp);
						}
						
						
					}.execute();
				}});
		}else{
			vHolder.pics.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	class ViewHolder{
		TextView name;
		RatingBar rb;
		TextView date;
		TextView comment;
		GridView pics;
		
	}
	class GridAdapter extends BaseAdapter{
		
		private Context context; 
		private ArrayList<String> list;
		
		public GridAdapter(Context context, ArrayList<String> list){
			super();
			this.context = context;
			this.list = list;
			
			//preperImageLoader();
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
			ImageView iv;
			if(convertView == null){
				iv = new ImageView(context);
				iv.setAdjustViewBounds(true);//设置边界对齐 
				iv.setLayoutParams(new GridView.LayoutParams(120, 80));//设置ImageView对象
				iv.setScaleType(ImageView.ScaleType.FIT_XY);//设置刻度的类型 
//				iv.setPadding(2,2,2,2);//设置间距 
			}else{
				iv = (ImageView) convertView; 
			}
			
			ImageLoader.getInstance().displayImage(Config.DOMAIN + list.get(position), iv);
			
			return iv;
		}
		
	}
	private void preperImageLoader(){
		
		
		/******************* 配置ImageLoder ***********************************************/
		File cacheDir =
					StorageUtils.getOwnCacheDirectory(context,
								"imageloader/Cache");

		ImageLoaderConfiguration config =
					new ImageLoaderConfiguration.Builder(context)
								.denyCacheImageMultipleSizesInMemory()
								.discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
								.build();// 开始构建
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory().cacheOnDisc()
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
		
		ImageLoader.getInstance().init(config);// 全局初始化此配置
		/*********************************************************************************/
	}

}
