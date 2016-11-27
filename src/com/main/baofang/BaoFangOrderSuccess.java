/**
 * 
 */
package com.main.baofang;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.chihuoshijian.R;
import com.example.fatass.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * @author Liming Chu 包房预定成功页面
 * @param
 * @return
 */
public class BaoFangOrderSuccess extends Activity implements
	OnClickListener{

	private TextView	name;
	private TextView	phone;
	private TextView	dt;
	private TextView	type;

	private TextView	back;
	private TextView	ok;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baofang_order_success);
		prepareView();
		
		name.setText(getIntent().getExtras().getString("name"));
		phone.setText(getIntent().getExtras().getString("phone"));
		type.setText(getIntent().getExtras().getString("type"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dt.setText(sdf.format(Calendar.getInstance().getTime()));
	}

	private void prepareView(){
		name =
				(TextView) findViewById(R.id.bangfang_order_success_name);
		phone =
				(TextView) findViewById(R.id.bangfang_order_success_phone);
		dt =
				(TextView) findViewById(R.id.bangfang_order_success_date);
		type =
				(TextView) findViewById(R.id.bangfang_order_success_baofang_type);
		back =
				(TextView) findViewById(R.id.baofang_order_success_back);
		ok =
				(TextView) findViewById(R.id.baofang_order_success_ok);

		back.setOnClickListener(this);
		ok.setOnClickListener(this);
	}

	@Override
	public void onBackPressed(){
		// TODO Auto-generated method stub
		super.onBackPressed();
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
			case R.id.baofang_order_success_ok:
				startActivity(new Intent(
					BaoFangOrderSuccess.this,
					MainActivity.class));
			break;
			default:
			break;
		}
	}

}
