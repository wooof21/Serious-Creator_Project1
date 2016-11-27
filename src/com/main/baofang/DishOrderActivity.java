/**
 * 
 */
package com.main.baofang;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chihuoshijian.R;
import com.chihuoshijian.view.DragSupportView;
import com.chihuoshijian.view.DragView;
import com.chihuoshijian.view.DragView.DragListener;
import com.chihuoshijian.view.PinnedHeaderListView.PinnedHeaderAdapter;
import com.chihuoshijian.view.PinnedHeaderListView;
import com.main.canting.CanTingDetailActivity;
import com.main.meishi.MeiShiCommentMain;
import com.model.ItemEntity;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tools.Config;
import com.tools.Tools;

import android.R.integer;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * @author Liming Chu
 * 
 * @param
 * @return
 */
public class DishOrderActivity extends Activity implements OnClickListener{

	private ProgressDialog				progressDialog;
	private ArrayList<ItemEntity>		list;
	private ArrayList<ItemEntity>		sortList;
	private PinnedHeaderListView		listView;
	private ImageView					slide;
	private LinearLayout				ll;
	private TextView					count;

	private DragView					dView;
	private DragSupportView				dsView;
	private RelativeLayout				midRL;
	private RelativeLayout				dsoMain;
	private RelativeLayout				leftMenu;
	private ListView					leftLV;

	// ���ɲ��Բ˵�ѡ������
	private List<Map<String, String>>	leftData;
	private String						uid;

	private ViewGroup					anim_mask_layout;	// ������
	private ImageView					buyImg;				// �����ڽ������ܵ�СͼƬ
	private int							buyNum	= 0;		// ��������
	private ArrayList<ItemEntity>		cartList;

	private String title;
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dish_order_slding_menu_main);
		prepareView();
		/**
		 * �����Ҫ�ڱ��Activity������Ҳʵ�ֲ໬�˵�Ч������Ҫ�ڲ���������DragLayout��ͬ��Activity��ʽ����
		 * Ȼ����onCreate������ʹ��; Activity���沿�֣���Ҫ������MyRelativeLayout��.
		 */

		dView.setDragListener(new DragListener(){// ��������
			@Override
			public void onOpen(){
			}

			@Override
			public void onClose(){
			}

			@Override
			public void onDrag(float percent){

			}
		});

		uid = getIntent().getStringExtra("uid");
		title = getIntent().getExtras().getString("title");
		new ListAsync().execute(uid);

		cartList = new ArrayList<ItemEntity>();
	}

	private void prepareView(){
		dView =
				(DragView) findViewById(R.id.dish_order_dragview);
		dsView =
				(DragSupportView) findViewById(R.id.dish_order_dragsupport);
		midRL =
				(RelativeLayout) findViewById(R.id.dish_order_middle);
		dsoMain =
				(RelativeLayout) midRL
						.findViewById(R.id.dish_order_main);
		listView =
				(PinnedHeaderListView) dsoMain
						.findViewById(R.id.dish_order_pinned_list_view);
		slide =
				(ImageView) dsoMain
						.findViewById(R.id.dish_order_main_slide_button);
		ll =
				(LinearLayout) dsoMain
						.findViewById(R.id.dish_order_shopping_cart_ll);
		count =
				(TextView) dsoMain
						.findViewById(R.id.dish_order_shopping_cart_count);
		leftMenu =
				(RelativeLayout) findViewById(R.id.dish_order_left);
		leftLV =
				(ListView) leftMenu
						.findViewById(R.id.sliding_menu_leftmenu_lv);
		LinearLayout _ll = new LinearLayout(this);
		LayoutParams lp =
				new LayoutParams(LayoutParams.MATCH_PARENT,
						180);
		_ll.setLayoutParams(lp);
		listView.addFooterView(_ll);
		
		slide.setOnClickListener(this);
		ll.setOnClickListener(this);
	}

	private Handler		handler	= new Handler(){

									@Override
									public
											void
											handleMessage(
													Message msg){
										// TODO Auto-generated method stub
										super.handleMessage(msg);
										Log.e("handler",
												"handl msg");
										// * �����µ�HeaderView�����ö���HeaderView
										View HeaderView =
												getLayoutInflater()
														.inflate(
																R.layout.pinned_list_view_header,
																listView,
																false);
										listView.setPinnedHeader(HeaderView);

										PinnedListAdapter adapter =
												new PinnedListAdapter(
														DishOrderActivity.this,
														list);
										listView.setAdapter(adapter);

										listView.setOnScrollListener(adapter);

										listView.setOnItemClickListener(new OnItemClickListener(){

											@Override
											public
													void
													onItemClick(
															AdapterView<?> parent,
															View view,
															int position,
															long id){
												// TODO Auto-generated method
												// stub
												Intent intent =
														new Intent(
																DishOrderActivity.this,
																MeiShiCommentMain.class);
												intent.putExtra(
														"id",
														list.get(
																position)
																.getId());
												intent.putExtra(
														"uid",
														uid);
												startActivity(intent);
											}
										});

									}

								};


	class ListAsync extends AsyncTask<Object, Void, String>{

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute(){
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog =
					ProgressDialog.show(
							DishOrderActivity.this, null,
							null);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String doInBackground(Object... params){
			// TODO Auto-generated method stub
			Log.e("url", Config.DISH_ORDER_LIST_URL
					+ params[0]);
			String data =
					new Tools()
							.getURL(Config.DISH_ORDER_LIST_URL
									+ params[0]);
			String code = "";
			list = new ArrayList<ItemEntity>();
			leftData = new ArrayList<Map<String, String>>();
			try{
				JSONObject job = new JSONObject(data);
				code = job.getString("code");
				if(code.equals("1")){
					JSONArray jArray =
							job.getJSONArray("result1");
					for(int i = 0,j = jArray.length();i < j;i++){
						JSONObject job1 =
								jArray.optJSONObject(i);
						String classname =
								job1.getString("classname");
						JSONArray jArray2 =
								job1.getJSONArray("next");
						HashMap<String, String> nameMap =
								new HashMap<String, String>();
						nameMap.put("item", classname);
						leftData.add(nameMap);

						for(int m = 0,n = jArray2.length();m < n;m++){
							JSONObject job2 =
									jArray2.optJSONObject(m);
							String pf =
									job2.getString("pf");
							float rate = 0;
							if(pf.equals("false")){
								rate = 0;
								Log.e("pf", pf);
							}else{
								rate = Float.valueOf(pf);
							}
							ItemEntity item =
									new ItemEntity(
											classname,
											job2.getString("id"),
											job2.getString("n_title"),
											job2.getString("n_dj"),
											rate,
											job2.getString("n_pic"),
											1);

							list.add(item);
						}
					}
				}
			}catch(JSONException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return code;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String result){
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			if(result.equals("1")){
				Message msg = handler.obtainMessage();
				handler.sendMessage(msg);
			}

			leftLV.setAdapter(new SimpleAdapter(
					DishOrderActivity.this,
					leftData,
					R.layout.sliding_menu_menulist_item_text,
					new String[] { "item" },
					new int[] { R.id.menu_text }));
			leftLV.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(
						AdapterView<?> parent, View view,
						int position, long id){
					// TODO Auto-generated method stub
					String classname =
							leftData.get(position).get(
									"item");
					sortList = new ArrayList<ItemEntity>();
					for(int i = 0,j = list.size();i < j;i++){
						if(!(list.get(i).getClassname()
								.equals(classname))){
							sortList.add(list.get(i));
						}else{
							sortList.add(0, list.get(i));
						}
					}
					for(int i = 0,j = sortList.size();i < j;i++){
						System.out.println(sortList.get(i)
								.getClassname());
					}
					dView.close();

					PinnedListAdapter adapter =
							new PinnedListAdapter(
									DishOrderActivity.this,
									sortList);
					listView.setAdapter(adapter);

					listView.setOnScrollListener(adapter);
				}
			});

		}

	}

	class PinnedListAdapter extends BaseAdapter implements
			OnScrollListener, PinnedHeaderAdapter{

		private ViewHolder				vHolder	= null;
		private Context					context;
		private ArrayList<ItemEntity>	list;
		private LayoutInflater			lInflater;
		private DisplayImageOptions		options;

		public PinnedListAdapter(Context context,
				ArrayList<ItemEntity> list){
			super();
			this.context = context;
			this.list = list;
			this.lInflater = LayoutInflater.from(context);

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
			Log.e("size", "" + list.size());
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
		public View getView(final int position, View convertView,
				ViewGroup parent){
			// TODO Auto-generated method stub

			if(convertView == null){
				convertView =
						lInflater.inflate(
								R.layout.pinned_listview_item,
								null);
			}
			
			vHolder = new ViewHolder();

			vHolder.pic =
					(ImageView) convertView
							.findViewById(R.id.pinned_list_item_pic);
			vHolder.title =
					(TextView) convertView
							.findViewById(R.id.title);
			vHolder.name =
					(TextView) convertView
							.findViewById(R.id.pinned_list_item_name);
			vHolder.price =
					(TextView) convertView
							.findViewById(R.id.pinned_list_item_price);
			vHolder.rb =
					(RatingBar) convertView
							.findViewById(R.id.pinned_list_item_ratingbar);
			vHolder.rating =
					(TextView) convertView
							.findViewById(R.id.pinned_list_item_rating);
			vHolder.order =
					(TextView) convertView
							.findViewById(R.id.pinned_list_item_order);

			vHolder.rb.setClickable(false);
			// ��ȡ����
			final ItemEntity item = (ItemEntity) list.get(position);
			ImageLoader.getInstance().displayImage(
					Config.DOMAIN + item.getPicAddr(),
					vHolder.pic);
			vHolder.rb.setRating(item.getRate());
			vHolder.title.setText(item.getClassname());
			vHolder.name.setText(item.getTitle());
			vHolder.price.setText(item.getPrice());
			vHolder.rating.setText("" + item.getRate());


			final String url =
					Config.DOMAIN + item.getPicAddr();
			
			vHolder.order
					.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v){
							// TODO Auto-generated method stub
							int[] start_location =
									new int[2];// һ���������飬�����洢��ť������Ļ��X��Y����
							v.getLocationInWindow(start_location);// ���ǻ�ȡ����ť������Ļ��X��Y���꣨��Ҳ�Ƕ�����ʼ�����꣩
							buyImg = new ImageView(context);
							ImageLoader
									.getInstance()
									.displayImage(url,
											buyImg, options);
							start_location[0] = 40;
							start_location[1] -= 80;
							setAnim(buyImg, start_location);// ��ʼִ�ж���
							
							Log.e("position", ""+position);
							Log.e("click", item.getTitle());

							
							if(getCartList().contains(item)){
								System.out
										.println("contains");
								getCartList().get(getCartList().indexOf(item)).setItemCount(getCartList().get(getCartList().indexOf(item)).getItemCount() + 1);
								//item.setItemCount(item.getItemCount() + 1);
							}else{
								System.out
								.println("not contains");
								getCartList().add(item);	
							}
							
							for(int i=0,j=getCartList().size();i<j;i++){
								System.out.println("3 " + getCartList().get(i).getTitle());
								System.out.println("3 " + getCartList().get(i).getItemCount());
							}
						}
					});
			
			
			if(needTitle(position)){
				// ��ʾ���Ⲣ��������
				vHolder.title.setText(item.getClassname());
				vHolder.title.setVisibility(View.VISIBLE);
			}else{
				// ���������ر���
				vHolder.title.setVisibility(View.GONE);
			}

			return convertView;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.chihuoshijian.view.PinnedHeaderListView.PinnedHeaderAdapter#
		 * getPinnedHeaderState(int)
		 */
		@Override
		public int getPinnedHeaderState(int position){
			// TODO Auto-generated method stub
			if(getCount() == 0 || position < 0){
				return PinnedHeaderAdapter.PINNED_HEADER_GONE;
			}

			if(isMove(position) == true){
				return PinnedHeaderAdapter.PINNED_HEADER_PUSHED_UP;
			}

			return PinnedHeaderAdapter.PINNED_HEADER_VISIBLE;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.chihuoshijian.view.PinnedHeaderListView.PinnedHeaderAdapter#
		 * configurePinnedHeader(android.view.View, int, int)
		 */
		@Override
		public void configurePinnedHeader(View headerView,
				int position, int alpaha){
			// TODO Auto-generated method stub
			// ���ñ��������
			ItemEntity itemEntity =
					(ItemEntity) getItem(position);
			String headerValue = itemEntity.getClassname();

			Log.e("", "header = " + headerValue);

			if(!TextUtils.isEmpty(headerValue)){
				TextView headerTextView =
						(TextView) headerView
								.findViewById(R.id.header);
				headerTextView.setText(headerValue);
			}

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.widget.AbsListView.OnScrollListener#onScrollStateChanged(
		 * android .widget.AbsListView, int)
		 */
		@Override
		public void onScrollStateChanged(AbsListView view,
				int scrollState){
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.widget.AbsListView.OnScrollListener#onScroll(android.widget.
		 * AbsListView, int, int, int)
		 */
		@Override
		public void onScroll(AbsListView view,
				int firstVisibleItem, int visibleItemCount,
				int totalItemCount){
			// TODO Auto-generated method stub

			if(view instanceof PinnedHeaderListView){
				((PinnedHeaderListView) view)
						.controlPinnedHeader(firstVisibleItem);
			}
		}

		// ===========================================================
		// Methods
		// ===========================================================

		/**
		 * �ж��Ƿ���Ҫ��ʾ����
		 * 
		 * @param position
		 * @return
		 */
		private boolean needTitle(int position){
			// ��һ���϶��Ƿ���
			if(position == 0){
				return true;
			}

			// �쳣����
			if(position < 0){
				return false;
			}

			// ��ǰ // ��һ��
			ItemEntity currentEntity =
					(ItemEntity) getItem(position);
			ItemEntity previousEntity =
					(ItemEntity) getItem(position - 1);
			if(null == currentEntity
					|| null == previousEntity){
				return false;
			}

			String currentTitle =
					currentEntity.getClassname();
			String previousTitle =
					previousEntity.getClassname();
			if(null == previousTitle
					|| null == currentTitle){
				return false;
			}

			// ��ǰitem����������һ��item��������ͬ�����ʾ��item���ڲ�ͬ����
			if(currentTitle.equals(previousTitle)){
				return false;
			}

			return true;
		}

		private boolean isMove(int position){
			// ��ȡ��ǰ����һ��
			ItemEntity currentEntity =
					(ItemEntity) getItem(position);
			ItemEntity nextEntity =
					(ItemEntity) getItem(position + 1);
			if(null == currentEntity || null == nextEntity){
				return false;
			}

			// ��ȡ����header����
			String currentTitle =
					currentEntity.getClassname();
			String nextTitle = nextEntity.getClassname();
			if(null == currentTitle || null == nextTitle){
				return false;
			}

			// ��ǰ��������һ��header����ǰ����Ҫ�ƶ���
			if(!currentTitle.equals(nextTitle)){
				return true;
			}

			return false;
		}

		private void preperImageLoader(){

			/******************* ����ImageLoder ***********************************************/
			File cacheDir =
					StorageUtils.getOwnCacheDirectory(
							context, "imageloader/Cache");

			ImageLoaderConfiguration config =
					new ImageLoaderConfiguration.Builder(
							context)
							.denyCacheImageMultipleSizesInMemory()
							.discCache(
									new UnlimitedDiscCache(
											cacheDir))// �Զ��建��·��
							.build();// ��ʼ����

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

			ImageLoader.getInstance().init(config);// ȫ�ֳ�ʼ��������

			/*********************************************************************************/
		}

		class ViewHolder{
			ImageView	pic;
			TextView	title;
			TextView	name;
			TextView	price;
			RatingBar	rb;
			TextView	rating;
			TextView	order;
		}
	}

	/**
	 * @Description: ����������
	 * @param
	 * @return void
	 * @throws
	 */
	private ViewGroup createAnimLayout(){
		ViewGroup rootView =
				(ViewGroup) this.getWindow().getDecorView();
		LinearLayout animLayout = new LinearLayout(this);
		LinearLayout.LayoutParams lp =
				new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT);
		animLayout.setLayoutParams(lp);
		animLayout.setId(Integer.MAX_VALUE);
		animLayout
				.setBackgroundResource(android.R.color.transparent);
		rootView.addView(animLayout);
		return animLayout;
	}

	private View addViewToAnimLayout(final ViewGroup vg,
			final View view, int[] location){
		int x = location[0];
		int y = location[1];
		LinearLayout.LayoutParams lp =
				new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.leftMargin = x;
		lp.topMargin = y;
		view.setLayoutParams(lp);
		return view;
	}

	private void
			setAnim(final View v, int[] start_location){
		anim_mask_layout = null;
		anim_mask_layout = createAnimLayout();
		anim_mask_layout.addView(v);// �Ѷ���С����ӵ�������

		final View view =
				addViewToAnimLayout(anim_mask_layout, v,
						start_location);
		int[] end_location = new int[2];// ���������洢��������λ�õ�X��Y����
		ll.getLocationInWindow(end_location);// shopCart���Ǹ����ﳵ

		// ����
		Animation scaleAnimation =
				new ScaleAnimation(0.3f, 0.1f, 0.6f, 0.1f);
		// ���ö���ʱ��
		scaleAnimation.setDuration(10);
		scaleAnimation
				.setInterpolator(new LinearInterpolator());
		scaleAnimation.setRepeatCount(0);// �����ظ�ִ�еĴ���
		scaleAnimation.setFillAfter(true);
		// ��ת
		Animation rotateAnimation =
				new RotateAnimation(0f, 360f);
		rotateAnimation.setRepeatCount(0);// �����ظ�ִ�еĴ���

		// ����λ��
		int endX =
				end_location[0] + 100 - start_location[0];// ����λ�Ƶ�X����
		int endY = end_location[1] - start_location[1];// ����λ�Ƶ�y����

		TranslateAnimation translateAnimationX =
				new TranslateAnimation(0, endX, 0, 0);
		translateAnimationX
				.setInterpolator(new LinearInterpolator());
		translateAnimationX.setRepeatCount(0);// �����ظ�ִ�еĴ���
		translateAnimationX.setFillAfter(true);

		TranslateAnimation translateAnimationY =
				new TranslateAnimation(0, 0, 0, endY);
		translateAnimationY
				.setInterpolator(new AccelerateInterpolator());
		translateAnimationY.setRepeatCount(0);// �����ظ�ִ�еĴ���
		translateAnimationX.setFillAfter(true);

		AnimationSet set = new AnimationSet(false);
		set.setFillAfter(false);
		set.addAnimation(scaleAnimation);
		set.addAnimation(rotateAnimation);
		set.addAnimation(translateAnimationY);
		set.addAnimation(translateAnimationX);
		set.setDuration(1000);// ������ִ��ʱ��
		view.startAnimation(set);
		// ���������¼�
		set.setAnimationListener(new AnimationListener(){
			// �����Ŀ�ʼ
			@Override
			public void
					onAnimationStart(Animation animation){
				v.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(
					Animation animation){
				// TODO Auto-generated method stub
			}

			// �����Ľ���
			@Override
			public void onAnimationEnd(Animation animation){
				v.setVisibility(View.GONE);
				buyNum++;// �ù���������1
				count.setText(buyNum + "");//

			}
		});

	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v){
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.dish_order_main_slide_button:
				dView.open();
			break;
			case R.id.dish_order_shopping_cart_ll:
				double totalPrice = 0;
				int totalCount = 0;
				for(int i=0,j=getCartList().size();i<j;i++){
					System.out.println("1 " + getCartList().get(i).getTitle());
					System.out.println("1 " + getCartList().get(i).getItemCount());
					double price = Double.valueOf(getCartList().get(i).getPrice());
					totalPrice += price * getCartList().get(i).getItemCount();
					totalCount += getCartList().get(i).getItemCount();
				}
				Intent intent = new Intent(DishOrderActivity.this, AddMenuActivity.class);
				intent.putExtra("cartList", getCartList());
				intent.putExtra("totalPrice", totalPrice);
				intent.putExtra("totalCount", totalCount);
				intent.putExtra("uid", uid);
				intent.putExtra("title", title);
				startActivityForResult(intent, 100);
				break;
			default:
			break;
		}
	}
	
	
	
	public ArrayList<ItemEntity> getCartList(){
		return cartList;
	}

	public void setCartList(ArrayList<ItemEntity> cartList){
		this.cartList = cartList;
	}

	private boolean isContains(String title){
		boolean isIn = false;
		if(getCartList().size() == 0){
			isIn = false;
		}else{
			for(int i=0,j=getCartList().size();i<j;i++){
				if(getCartList().get(i).getTitle().equals(title)){
					isIn = true;
				}else{
					isIn = false;
				}
			}
		}
		return isIn;
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode,
			int resultCode, Intent data){
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch(resultCode){
			case 1000:
				setCartList((ArrayList<ItemEntity>) data.getExtras().getSerializable("cartList"));
				buyNum = 0;
				for(int i=0,j=getCartList().size();i<j;i++){
					System.out.println("2 " + getCartList().get(i).getTitle());
					System.out.println("2 " + getCartList().get(i).getItemCount());
					buyNum += getCartList().get(i).getItemCount();
				}
				count.setText(buyNum + "");//
			break;

			default:
			break;
		}
	}
}
