/**
 * 
 */
package com.fragment.mainbottom;

import com.chihuoshijian.R;
import com.example.fatass.MainActivity;
import com.main.baofang.AddMenuActivity;
import com.main.baofang.BaoFangMainListPage;
import com.main.baofang.BaoFangReserveInfo;
import com.main.canting.CanTingMainListPage;
import com.main.membercenter.MemberCenterMainActivity;
import com.main.search.SearchActivity;
import com.order.OrderDetailActivity;
import com.tools.Tags;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 底部4个导航控件fragment
 * 
 * @author Liming Chu
 * @param
 * @return
 */

public class MainBottomFragment extends Fragment implements
		OnClickListener{

	private LinearLayout	ll1;
	private LinearLayout	ll2;
	private LinearLayout	ll3;
	private LinearLayout	ll4;

	/** 底部导航4个按钮 */
	private ImageView		iv1;	// 搜包房
	private ImageView		iv2;	// 搜餐厅
	private ImageView		iv3;	// 搜美食
	private ImageView		iv4;	// 吃货中心
	
	private String currentActivity;

	@Override
	public void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		System.out.println(getActivity().getClass()
				.getName().toString());
		System.out.println(getActivity().getClass()
				.getSimpleName().toString());
		currentActivity = getActivity().getClass()
				.getSimpleName().toString();

	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState){
		// TODO Auto-generated method stub

		View view =
				inflater.inflate(R.layout.bottom_fragment,
						container, false);
		ll1 =
				(LinearLayout) view
						.findViewById(R.id.bottom_fragment_ll1);
		ll2 =
				(LinearLayout) view
						.findViewById(R.id.bottom_fragment_ll2);
		ll3 =
				(LinearLayout) view
						.findViewById(R.id.bottom_fragment_ll3);
		ll4 =
				(LinearLayout) view
						.findViewById(R.id.bottom_fragment_ll4);

		if(getActivity().getClass().getSimpleName()
				.toString().equals("BaoFangReserveInfo")){
			ll1.setClickable(false);
		}else{
			ll1.setOnClickListener(this);
		}

		if(getActivity().getClass().getSimpleName()
				.toString().equals("AddMenuActivity")){
			ll2.setClickable(false);
		}else{
			ll2.setOnClickListener(this);
		}

		if(getActivity().getClass().getSimpleName()
				.toString().equals("BaoFangReserveInfo")){
			ll3.setClickable(false);
		}else{
			ll3.setOnClickListener(this);
		}

		if(getActivity().getClass().getSimpleName()
				.toString()
				.equals("MemberCenterMainActivity")){
			ll4.setClickable(false);
		}else{

			ll4.setOnClickListener(this);
		}

		iv1 =
				(ImageView) view
						.findViewById(R.id.bottom_fragment_iv1);
		iv2 =
				(ImageView) view
						.findViewById(R.id.bottom_fragment_iv2);
		iv3 =
				(ImageView) view
						.findViewById(R.id.bottom_fragment_iv3);
		iv4 =
				(ImageView) view
						.findViewById(R.id.bottom_fragment_iv4);

		String className =
				getActivity().getClass().getSimpleName()
						.toString();
		if(className.equals("BaoFangMainListPage")){
			iv1.setImageResource(R.drawable.bottom_fragment_baofang_state_pressed);
			iv2.setImageResource(R.drawable.bottom_fragment_canting_state_normal);
			iv3.setImageResource(R.drawable.bottom_fragment_meishi_state_normal);
			iv4.setImageResource(R.drawable.bottom_fragment_zhongxin_state_normal);
			ll1.setClickable(false);
			ll2.setOnClickListener(this);
			ll3.setOnClickListener(this);
			ll4.setOnClickListener(this);
		}else if(className.equals("CanTingMainListPage")){
			iv1.setImageResource(R.drawable.bottom_fragment_baofang_state_normal);
			iv2.setImageResource(R.drawable.bottom_fragment_canting_state_pressed);
			iv3.setImageResource(R.drawable.bottom_fragment_meishi_state_normal);
			iv4.setImageResource(R.drawable.bottom_fragment_zhongxin_state_normal);
			ll2.setClickable(false);
			ll1.setOnClickListener(this);
			ll3.setOnClickListener(this);
			ll4.setOnClickListener(this);
		}else if(className.equals("SearchActivity")){
			iv1.setImageResource(R.drawable.bottom_fragment_baofang_state_normal);
			iv2.setImageResource(R.drawable.bottom_fragment_canting_state_normal);
			iv3.setImageResource(R.drawable.bottom_fragment_meishi_state_pressed);
			iv4.setImageResource(R.drawable.bottom_fragment_zhongxin_state_normal);
			ll3.setClickable(false);
			ll2.setOnClickListener(this);
			ll1.setOnClickListener(this);
			ll4.setOnClickListener(this);
		}else if(className
				.equals("MemberCenterMainActivity")){
			iv1.setImageResource(R.drawable.bottom_fragment_baofang_state_normal);
			iv2.setImageResource(R.drawable.bottom_fragment_canting_state_normal);
			iv3.setImageResource(R.drawable.bottom_fragment_meishi_state_normal);
			iv4.setImageResource(R.drawable.bottom_fragment_zhongxin_state_pressed);
			ll4.setClickable(false);
			ll2.setOnClickListener(this);
			ll3.setOnClickListener(this);
			ll1.setOnClickListener(this);
		}
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v){
		// TODO Auto-generated method stub
		Intent intent = null;
		switch(v.getId()){
			case R.id.bottom_fragment_ll1:
				intent =
						new Intent(getActivity(),
								BaoFangMainListPage.class);
			break;
			case R.id.bottom_fragment_ll2:
				intent =
						new Intent(getActivity(),
								CanTingMainListPage.class);
			break;
			case R.id.bottom_fragment_ll3:
				intent =
						new Intent(getActivity(),
								SearchActivity.class);
				String[] tags =
						Tags.getInstance().getTags();
				System.out.println("2 " + tags);
				intent.putExtra("tags", tags);
			break;
			case R.id.bottom_fragment_ll4:
				intent =
						new Intent(
								getActivity(),
								MemberCenterMainActivity.class);
			break;
			default:
			break;
		}
		startActivity(intent);
		if(currentActivity.equals("MainActivity")){
			
		}else{
			getActivity().finish();
		}
	}

	@Override
	public void onPause(){
		// TODO Auto-generated method stub
		super.onPause();
	}

}
