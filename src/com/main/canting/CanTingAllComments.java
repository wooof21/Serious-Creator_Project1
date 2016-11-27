/**
 * 
 */
package com.main.canting;

import java.util.ArrayList;
import java.util.HashMap;

import com.chihuoshijian.R;
import com.chihuoshijian.adapter.MeiShiCommentListAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * @author Liming Chu
 *
 * @param
 * @return
 */
public class CanTingAllComments extends Activity{

	private RatingBar allRb;
	private TextView allRating;
	private ListView lv;
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_list_main);
		prepareView();
		
		float rate = getIntent().getFloatExtra("rate", 0);
		allRb.setRating(rate);
		allRating.setText(rate + "·Ö");
		
		ArrayList<HashMap<String, String>> list =
				(ArrayList<HashMap<String, String>>) getIntent()
						.getSerializableExtra("list");
		
		String type = getIntent().getExtras().getString("type");
		MeiShiCommentListAdapter mAdapter = new MeiShiCommentListAdapter(CanTingAllComments.this, list, type);
		lv.setAdapter(mAdapter);
	}
	
	private void prepareView(){
		allRb = (RatingBar)findViewById(R.id.comment_list_mian_ratingbar);
		allRating = (TextView)findViewById(R.id.comment_list_mian_rating);
		lv = (ListView)findViewById(R.id.comment_list_mian_lv);
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed(){
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
}
