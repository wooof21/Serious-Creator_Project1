package com.chihuoshijian.view;


import com.chihuoshijian.view.DragView.Status;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Activity������㲼�֣�Э���˵�������Ӧ
 * 
 * @author zihao
 * 
 */
public class DragSupportView extends LinearLayout {
	private DragView dl;

	public DragSupportView(Context context) {
		super(context);
	}

	public DragSupportView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setDragLayout(DragView dl) {
		this.dl = dl;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (dl.getStatus() != Status.Close) {
			return true;
		}
		return super.onInterceptTouchEvent(event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (dl.getStatus() != Status.Close) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				dl.close();
			}
			return true;
		}
		return super.onTouchEvent(event);
	}

}
