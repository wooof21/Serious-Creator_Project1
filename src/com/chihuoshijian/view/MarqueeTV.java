package com.chihuoshijian.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View.OnClickListener;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class MarqueeTV extends TextView{

	public MarqueeTV(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	
	
	public MarqueeTV(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}



	public MarqueeTV(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	


	@Override
	public void setText(CharSequence text, BufferType type) {
		// TODO Auto-generated method stub
		super.setText(text, type);
	}



	@Override
	public void setOnClickListener(OnClickListener l) {
		// TODO Auto-generated method stub
		super.setOnClickListener(l);
	}



	@Override
	public void setTag(Object tag) {
		// TODO Auto-generated method stub
		super.setTag(tag);
	}



	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		// TODO Auto-generated method stub
		if(focused){
			super.onFocusChanged(focused, direction, previouslyFocusedRect);
		}
		
	}


	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		// TODO Auto-generated method stub
		if(hasWindowFocus){
			super.onWindowFocusChanged(hasWindowFocus);
		}
		
	}


	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		// TODO Auto-generated method stub
		return true;
	}

	
}
