package com.atet.tvmarket.view.recyclerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView.Recycler;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;
import android.view.View.MeasureSpec;

public class MyLinearLayoutManager extends LinearLayoutManager {

	public MyLinearLayoutManager(Context context) {
		super(context);
	}

	@Override
	public void onMeasure(Recycler recycler, State state, int widthSpec,
			int heightSpec) {
		View view  = recycler.getViewForPosition(0);
		if(view!=null){
			measureChild(view, widthSpec, heightSpec);
			int measuredWidth = MeasureSpec.getSize(widthSpec);  
			int measuredHeight = view.getMeasuredHeight();  
			setMeasuredDimension(measuredWidth, measuredHeight);  
		}
	}
}
