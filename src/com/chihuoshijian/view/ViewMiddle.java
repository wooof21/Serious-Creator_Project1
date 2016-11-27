package com.chihuoshijian.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.chihuoshijian.R;
import com.chihuoshijian.adapter.TextAdapter;

import android.content.Context;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

;

public class ViewMiddle extends LinearLayout implements ViewBaseAction {
	
	private ListView regionListView;
	private ListView plateListView;
	private ArrayList<String> groups;
	private HashMap<String, String> childrenItem;
	private ArrayList<String[]> children;
	private ArrayList<String> _groups = new ArrayList<String>();
	private LinkedList<String> _childrenItem = new LinkedList<String>();
	private SparseArray<LinkedList<String>> _children = new SparseArray<LinkedList<String>>();
	private TextAdapter plateListViewAdapter;
	private TextAdapter earaListViewAdapter;
	private OnSelectListener mOnSelectListener;
	private int tEaraPosition = 0;
	private int tBlockPosition = 0;
	private String showString;

	public ViewMiddle(Context context, ArrayList<String> groups, HashMap<String, String> childrenItem,
			ArrayList<String[]> children) {
		super(context);
		this.groups = groups;
		this.childrenItem = childrenItem;
		this.children = children;
		init(context);
	}

	public ViewMiddle(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public void updateShowText(String showArea, String showBlock) {
		if (showArea == null || showBlock == null) {
			return;
		}
		for (int i = 0; i < _groups.size(); i++) {
			if (_groups.get(i).equals(showArea)) {
				earaListViewAdapter.setSelectedPosition(i);
				_childrenItem.clear();
				if (i < children.size()) {
					_childrenItem.addAll(_children.get(i));
				}
				tEaraPosition = i;
				break;
			}
		}
		for (int j = 0; j < _childrenItem.size(); j++) {
			if (_childrenItem.get(j).replace("不限", "").equals(showBlock.trim())) {
				plateListViewAdapter.setSelectedPosition(j);
				tBlockPosition = j;
				break;
			}
		}
		setDefaultSelect();
	}

	private void init(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_region, this, true);
		regionListView = (ListView) findViewById(R.id.listView);
		plateListView = (ListView) findViewById(R.id.listView2);
		setBackgroundDrawable(getResources().getDrawable(
				R.drawable.choosearea_bg_mid));

		
		for(int i=0, j=groups.size();i<j;i++){
			_groups.add(groups.get(i));
		}
		
		for(int i=0,j=children.get(0).length;i<j;i++){
			_childrenItem.add(children.get(0)[i]);
		}
		
		Log.e("1", ""+_children.size());
		Log.e("2", ""+groups.size());

		earaListViewAdapter = new TextAdapter(context, _groups,
				R.drawable.choose_item_selected,
				R.drawable.choose_eara_item_selector);
		earaListViewAdapter.setTextSize(17);
		earaListViewAdapter.setSelectedPositionNoNotify(tEaraPosition);
		regionListView.setAdapter(earaListViewAdapter);
		earaListViewAdapter
				.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(View view, int position) {
						if (position < groups.size()) {
							System.out
									.println(_groups.get(position));
							System.out
									.println(children.get(position));
							System.out
									.println(childrenItem.toString());
							
							_childrenItem.clear();
							for(int i=0,j=children.get(position).length;i<j;i++){
								_childrenItem.add(children.get(position)[i]);
							}
							
							System.out
									.println(_childrenItem);
							
							plateListViewAdapter.notifyDataSetChanged();
						}
					}
				});
		if (tEaraPosition < _children.size())
			_childrenItem.addAll(_children.get(tEaraPosition));
		plateListViewAdapter = new TextAdapter(context, _childrenItem,
				R.drawable.choose_item_right,
				R.drawable.choose_plate_item_selector);
		plateListViewAdapter.setTextSize(15);
		plateListViewAdapter.setSelectedPositionNoNotify(tBlockPosition);
		plateListView.setAdapter(plateListViewAdapter);
		plateListViewAdapter
				.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(View view, final int position) {
						showString = _childrenItem.get(position);
						Log.e("ViewMiddle onItemClickListener showString", showString);
						Log.e("ViewMiddle onItemClickListener id", childrenItem.get(showString));
						if (mOnSelectListener != null) {
							
							mOnSelectListener.getValue(showString);
						}

					}
				});
		if (tBlockPosition < _childrenItem.size())
			showString = _childrenItem.get(tBlockPosition);
//		if (showString.contains("不限")) {
//			showString = showString.replace("不限", "");
//		}
		setDefaultSelect();

	}

	public void setDefaultSelect() {
		regionListView.setSelection(tEaraPosition);
		plateListView.setSelection(tBlockPosition);
	}

	public String getShowText() {
		return showString;
	}
	

	public void setShowString(String showString){
		this.showString = showString;
	}

	public void setOnSelectListener(OnSelectListener onSelectListener) {
		mOnSelectListener = onSelectListener;
	}

	public interface OnSelectListener {
		public void getValue(String showText);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}
}
