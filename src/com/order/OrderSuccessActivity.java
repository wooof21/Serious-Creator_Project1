/**
 * 
 */
package com.order;

import java.util.Hashtable;

import com.chihuoshijian.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.main.membercenter.VipCenterMyOrder;
import com.tools.Config;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Liming Chu
 * 
 * @param
 * @return
 */
public class OrderSuccessActivity extends Activity
	implements OnClickListener{

	private int			QR_WIDTH	= 180, QR_HEIGHT = 180;

	private TextView	orderNo;
	private TextView	printNo;
	private TextView	price;
	private ImageView	QR_IV;
	private TextView	back;
	private TextView	check;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_success);
		prepareView();

		String orderId =
				getIntent().getExtras().getString("orderId");
		double totalPrice =
				getIntent().getExtras().getDouble("totalPrice");
		orderNo.setText(orderId);
		price.setText("￥" + totalPrice);
		String p = orderId.substring(orderId.length() - 6);
		printNo.setText(p);
		createQRImage(Config.ORDER_NUM_QR_CODE_URL
				+ orderId);
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
	}

	private void prepareView(){
		orderNo =
				(TextView) findViewById(R.id.order_success_order_no);
		printNo =
				(TextView) findViewById(R.id.order_success_print_no);
		price =
				(TextView) findViewById(R.id.order_success_total_price);
		QR_IV =
				(ImageView) findViewById(R.id.order_success_qr_code_iv);
		back =
				(TextView) findViewById(R.id.baofang_order_success_back);
		check =
				(TextView) findViewById(R.id.baofang_order_success_check);

		back.setOnClickListener(this);
		check.setOnClickListener(this);
	}

	// 要转换的地址或字符串,可以是中文
	private void createQRImage(String url){
		try{
			// 判断URL合法性
			if(url == null || "".equals(url)
					|| url.length() < 1){
				return;
			}
			Hashtable<EncodeHintType, String> hints =
					new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// 图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix =
					new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
			// 下面这里按照二维码的算法，逐个生成二维码的图片，
			// 两个for循环是图片横列扫描的结果
			for(int y = 0;y < QR_HEIGHT;y++){
				for(int x = 0;x < QR_WIDTH;x++){
					if(bitMatrix.get(x, y)){
						pixels[y * QR_WIDTH + x] =
								0xff000000;
					}else{
						pixels[y * QR_WIDTH + x] =
								0xffffffff;
					}
				}
			}
			// 生成二维码图片的格式，使用ARGB_8888
			Bitmap bitmap =
					Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
			// 显示到一个ImageView上面
			QR_IV.setImageBitmap(bitmap);
		}catch(WriterException e){
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v){
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.baofang_order_success_back:
				finish();
			break;
			case R.id.baofang_order_success_check:
				Intent intent = new Intent(OrderSuccessActivity.this, VipCenterMyOrder.class);
				intent.putExtra("which", "all");
				startActivity(intent);
			break;
			default:
			break;
		}
	}

}
