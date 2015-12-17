package com.atet.tvmarket.control.classify.area;

import com.atet.tvmarket.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class GameAreaPanel extends RelativeLayout{

	private GameAreaItem oneView,twoView,threeView;
	public GameAreaPanel(Context context){
		this(context, null);
	}
	
	public GameAreaPanel(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public GameAreaPanel(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setChildrenDrawingOrderEnabled(true);
		LayoutInflater.from(context).inflate(R.layout.gameclassify_area_panel, this, true);
		oneView = (GameAreaItem)findViewById(R.id.one_areaview);
		twoView = (GameAreaItem)findViewById(R.id.two_areaview);
		threeView = (GameAreaItem)findViewById(R.id.three_areaview);
	}

	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		if (hasFocus()) {
			View child = getFocusedChild();

			if (child != null) {
				int index = indexOfChild(child);
				if (index != -1) {

					if (i < index) {
						return i;
					} else {
						return childCount - 1 - i + index;
					}
				}
			} else {
				return i;
			}

		}

		return super.getChildDrawingOrder(childCount, i);

	}

	public GameAreaItem getOneView() {
		return oneView;
	}

	public GameAreaItem getTwoView() {
		return twoView;
	}

	public GameAreaItem getThreeView() {
		return threeView;
	}
	
}
