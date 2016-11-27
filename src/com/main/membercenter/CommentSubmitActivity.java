/**
 * 
 */
package com.main.membercenter;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;

import com.chihuoshijian.R;
import com.chihuoshijian.imagelist.PhotoActivity;
import com.chihuoshijian.imagelist.PicBucketListActivity;
import com.tools.Bmp;
import com.tools.Config;
import com.tools.FileUtils;
import com.tools.ScrollListView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/**
 * @author Liming Chu
 * 
 * @param
 * @return
 */
public class CommentSubmitActivity extends Activity{

	private RelativeLayout			topBg;
	private TextView				name;
	private RatingBar				rb;
	private EditText				et;
	private GridView				gv;
	private TextView				submit;

	private BaseAdapter				adapter;
	public ProgressDialog			progressDialog;
	private HashMap<String, String>	params;

	private ArrayList<File>			fileList;
	private Handler					handler;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_submit);
		prepareView();
		Init();

		fileList = new ArrayList<File>();
		handler = new Handler();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume(){
		// TODO Auto-generated method stub
		super.onResume();
		fileList.clear();
	}
	public void Init(){
		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		((GridAdapter) adapter).update();
		ScrollListView.setGridViewHeightBasedOnChildren(gv);
		gv.setAdapter(adapter);
		gv.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0,
					View arg1,
					int arg2,
					long arg3){
				if(arg2 == Bmp.bmp.size()){
					new PopupWindows(
						CommentSubmitActivity.this, gv);
				}else{
					Intent intent =
							new Intent(
								CommentSubmitActivity.this,
								PhotoActivity.class);
					intent.putExtra("ID", arg2);
					startActivity(intent);
					finish();
				}
			}
		});
		submit.setOnClickListener(new OnClickListener(){

			public void onClick(View v){
				List<String> list = new ArrayList<String>();
				for(int i = 0;i < Bmp.drr.size();i++){
					String Str =
							Bmp.drr.get(i).substring(Bmp.drr.get(i).lastIndexOf("/") + 1, Bmp.drr.get(i).lastIndexOf("."));
					list.add(FileUtils.SDPATH + Str
							+ ".JPEG");
					System.out.println(list.get(i));

					File file = new File(list.get(i));
					fileList.add(file);
				}
				new SubmitAsync().execute(fileList);

				// 高清的压缩图片全部就在 list 路径里面了
				// 高清的压缩过的 bmp 对象 都在 Bimp.bmp里面
				// 完成上传服务器后 .........
				// FileUtils.deleteDir();
			}
		});
	}

	class SubmitAsync extends
		AsyncTask<ArrayList<File>, Void, String>{

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
					ProgressDialog.show(CommentSubmitActivity.this, null, null);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String
			doInBackground(ArrayList<File>... params){
			// TODO Auto-generated method stub
			// 保存需上传文件信息
			String url =
					Config.COMMENT_SUBMIT_URL + "23"
							+ "&xj=3"
							+ "&oid=1235235231324"
							+ "&spid=3049"
							+ "&nr=ahsaofoahsf"
							+ "&userid=19";

			uploadFile(params[0], url);

			return null;
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
			// Toast.makeText(CommentSubmitActivity.this, result,
			// Toast.LENGTH_SHORT).show();//
		}
	}

	/**
	 * android上传文件到服务器
	 * 
	 * @param file
	 *            需要上传的文件
	 * @param RequestURL
	 *            请求的rul
	 * @return 返回响应的内容
	 */
	public static String uploadFile(ArrayList<File> file,
			String RequestURL){
		String result = null;
		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型,
														// 这个参数来说明我们这传的是文件不是字符串了

		try{

			URL url = new URL(RequestURL);
			HttpURLConnection conn =
					(HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10 * 1000);
			conn.setConnectTimeout(10 * 1000);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", "utf-8"); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE
					+ ";boundary=" + BOUNDARY);

			StringBuilder sb = new StringBuilder();
			for(int i = 0,j = file.size();i < j;i++){
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);

				sb.append("Content-Disposition: form-data; name=\""
						+ "uploadfile" + "\"" + LINE_END);
				sb.append("Content-Type: text/plain; charset="
						+ "utf-8" + LINE_END);
				sb.append("Content-Transfer-Encoding: 8bit"
						+ LINE_END);
				sb.append(LINE_END);
				sb.append("uploadfile");
				sb.append(LINE_END);
			}
			DataOutputStream outStream =
					new DataOutputStream(
						conn.getOutputStream());
			outStream.write(sb.toString().getBytes());
			if(file != null){
				for(int i = 0,j = file.size();i < j;i++){
					StringBuffer sb1 = new StringBuffer();
					sb1.append(PREFIX);
					sb1.append(BOUNDARY);
					sb1.append(LINE_END);
					/**
					 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
					 * filename是文件的名字，包含后缀名的 比如:abc.png
					 */

					sb1.append("Content-Disposition: form-data; name=\"uploadfile[]\"; filename=\""
							+ file.get(i).getName()
							+ "\""
							+ LINE_END);
					sb1.append("Content-Type: application/octet-stream; charset="
							+ "utf-8" + LINE_END);
					sb1.append(LINE_END);
					outStream.write(sb1.toString().getBytes());
					InputStream is =
							new FileInputStream(file.get(i));
					byte[] bytes = new byte[1024];
					int len = 0;
					while((len = is.read(bytes)) != -1){
						outStream.write(bytes, 0, len);
					}
					is.close();
					outStream.write(LINE_END.getBytes());
				}
			}

			// 请求结束标志
			byte[] end_data =
					(PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
			outStream.write(end_data);
			outStream.flush();
			// 得到响应码
			int res = conn.getResponseCode();
			InputStream in = conn.getInputStream();
			StringBuilder sbResult = new StringBuilder();
			if(res == 200){
				int ch;
				while((ch = in.read()) != -1){
					sbResult.append((char) ch);
				}
			}
			result = sbResult.toString();
			outStream.close();
			conn.disconnect();
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return result;
	}

	private void prepareView(){
		topBg =
				(RelativeLayout) findViewById(R.id.comment_submit_top_rl);
		name =
				(TextView) findViewById(R.id.comment_submit_name);
		rb =
				(RatingBar) findViewById(R.id.comment_submit_rb);
		et =
				(EditText) findViewById(R.id.comment_submit_et);
		gv =
				(GridView) findViewById(R.id.comment_submit_gv);
		submit =
				(TextView) findViewById(R.id.comment_submit_submit);
		et.clearFocus();
	}

	class GridAdapter extends BaseAdapter{
		private LayoutInflater	inflater;					// 视图容器
		private int				selectedPosition	= -1;	// 选中的位置
		private boolean			shape;

		public boolean isShape(){
			return shape;
		}

		public void setShape(boolean shape){
			this.shape = shape;
		}

		public GridAdapter(Context context){
			inflater = LayoutInflater.from(context);
		}

		public void update(){
			loading();
		}

		public int getCount(){
			return(Bmp.bmp.size() + 1);
		}

		public Object getItem(int arg0){

			return null;
		}

		public long getItemId(int arg0){

			return 0;
		}

		public void setSelectedPosition(int position){
			selectedPosition = position;
		}

		public int getSelectedPosition(){
			return selectedPosition;
		}

		/**
		 * ListView Item设置
		 */
		public View getView(int position,
				View convertView,
				ViewGroup parent){
			final int coord = position;
			ViewHolder holder = null;
			if(convertView == null){

				convertView =
						inflater.inflate(R.layout.item_published_grida, parent, false);
				holder = new ViewHolder();
				holder.image =
						(ImageView) convertView.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}

			if(position == Bmp.bmp.size()){
				holder.image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_focused));
				if(position == 9){
					holder.image.setVisibility(View.GONE);
				}
			}else{
				holder.image.setImageBitmap(Bmp.bmp.get(position));
			}

			return convertView;
		}

		public class ViewHolder{
			public ImageView	image;
		}

		Handler	handler	= new Handler(){

							public void
								handleMessage(Message msg){
								switch(msg.what){
									case 1:
										adapter.notifyDataSetChanged();
									break;
								}
								super.handleMessage(msg);
							}
						};

		public void loading(){
			new Thread(new Runnable(){
				public void run(){
					while(true){
						if(Bmp.max == Bmp.drr.size()){
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						}else{
							try{
								String path =
										Bmp.drr.get(Bmp.max);
								System.out.println(path);
								Bitmap bm =
										Bmp.revitionImageSize(path);
								Bmp.bmp.add(bm);
								String newStr =
										path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
								FileUtils.saveBitmap(bm, ""
										+ newStr);
								Bmp.max += 1;
								Message message =
										new Message();
								message.what = 1;
								handler.sendMessage(message);
							}catch(IOException e){

								e.printStackTrace();
							}
						}
					}
				}
			}).start();
		}
	}

	class PopupWindows extends PopupWindow{

		public PopupWindows(Context mContext, View parent){

			View view =
					View.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_ins));
			LinearLayout ll_popup =
					(LinearLayout) view.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 =
					(Button) view.findViewById(R.id.item_popupwindows_camera);
			Button bt2 =
					(Button) view.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 =
					(Button) view.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener(){
				public void onClick(View v){
					photo();
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener(){
				public void onClick(View v){
					Intent intent =
							new Intent(
								CommentSubmitActivity.this,
								PicBucketListActivity.class);
					startActivity(intent);
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener(){
				public void onClick(View v){
					dismiss();
				}
			});

		}
	}

	private static final int	TAKE_PICTURE	= 0x000000;
	private String				path			= "";

	public void photo(){
		Intent openCameraIntent =
				new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		openCameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

		StringBuffer sDir = new StringBuffer();
		if(hasSDcard()){
			sDir.append(Environment.getExternalStorageDirectory()
					+ "/chihuoshijian/");
		}else{
			String dataPath =
					Environment.getRootDirectory().getPath();
			sDir.append(dataPath + "/chihuoshijian/");
		}

		File fileDir = new File(sDir.toString());
		if(!fileDir.exists()){
			fileDir.mkdirs();
		}
		File file =
				new File(
					fileDir,
					String.valueOf(System.currentTimeMillis())
							+ ".jpg");

		path = file.getPath();
		Uri imageUri = Uri.fromFile(file);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	public static boolean hasSDcard(){
		String status =
				Environment.getExternalStorageState();
		if(status.equals(Environment.MEDIA_MOUNTED)){
			return true;
		}else{
			return false;
		}
	}

	public String getString(String s){
		String path = null;
		if(s == null)
			return "";
		for(int i = s.length() - 1;i > 0;i++){
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart(){
		((GridAdapter) adapter).update();
		super.onRestart();
	}

	protected void onActivityResult(int requestCode,
			int resultCode,
			Intent data){
		switch(requestCode){
			case TAKE_PICTURE:
				if(Bmp.drr.size() < 9 && resultCode == -1){
					Bmp.drr.add(path);
				}
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed(){
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}
