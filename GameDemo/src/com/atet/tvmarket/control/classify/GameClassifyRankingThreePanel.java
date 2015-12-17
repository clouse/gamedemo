package com.atet.tvmarket.control.classify;

import com.atet.tvmarket.R;
import com.atet.tvmarket.control.home.MainActivity;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class GameClassifyRankingThreePanel extends RelativeLayout{
	
	private MainActivity context;
	GameClassifyRankingItem oneView,twoView,threeView;
	public GameClassifyRankingThreePanel(Context context){
		this(context, null);
	}
	
	public GameClassifyRankingThreePanel(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public GameClassifyRankingThreePanel(Context context, AttributeSet attrs,int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = (MainActivity) context;
		setChildrenDrawingOrderEnabled(true);
		LayoutInflater.from(context).inflate(R.layout.gameclassify_ranking_threepanel, this, true);
		oneView = (GameClassifyRankingItem)findViewById(R.id.one_view);
		twoView = (GameClassifyRankingItem)findViewById(R.id.two_view);
		threeView = (GameClassifyRankingItem)findViewById(R.id.three_view);
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
	
	public GameClassifyRankingItem getOneView() {
		return oneView;
	}

	public GameClassifyRankingItem getTwoView() {
		return twoView;
	}

	public GameClassifyRankingItem getThreeView() {
		return threeView;
	}
}
