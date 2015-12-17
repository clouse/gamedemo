package com.atet.tvmarket.control.gamerecommand;

import com.atet.tvmarket.R;
import com.atet.tvmarket.common.cache.ImageFetcher;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class TwoPanelItem extends RelativeLayout{

	private RelativeLayout topView,bottomView;
	private ImageView topBorder,topCover,bottomBorder,bottomCover;
	private NewGameRecommandActivity context;
	private View touchView = null;
	public TwoPanelItem(Context context){
		this(context, null);
	}
	public TwoPanelItem(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	public TwoPanelItem(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = (NewGameRecommandActivity) context;
		setChildrenDrawingOrderEnabled(true);
		LayoutInflater.from(context).inflate(R.layout.newrecommand_twopanel_item, this, true);
		topView = (RelativeLayout)findViewById(R.id.rl_top);
		bottomView = (RelativeLayout)findViewById(R.id.rl_bottom);
		
		topBorder = (ImageView)findViewById(R.id.iv_top_border);
		topCover = (ImageView)findViewById(R.id.iv_top_cover);
		bottomBorder = (ImageView)findViewById(R.id.iv_bottom_border);
		bottomCover = (ImageView)findViewById(R.id.iv_bottom_cover);
		
		topView.setOnFocusChangeListener(onFocusChangeListener);
		topView.setOnKeyListener(onKeyListener);
		topView.setOnClickListener(onClickListener);
		topView.setOnTouchListener(onTouchListener);
		
		bottomView.setOnFocusChangeListener(onFocusChangeListener);
		bottomView.setOnKeyListener(onKeyListener);
		bottomView.setOnClickListener(onClickListener);
		bottomView.setOnTouchListener(onTouchListener);
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
	
	OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus){
				if(v.isInTouchMode() && v == touchView){
					v.performClick();
				}
				else{
					touchView=null;
				}
				
				if(v.getId()==topView.getId()){
					topBorder.setVisibility(View.VISIBLE);
				}
				else if(v.getId()==bottomView.getId()){
					bottomBorder.setVisibility(View.VISIBLE);
				}
				
			}
			
			else{
				if(v.getId()==topView.getId()){
					topBorder.setVisibility(View.INVISIBLE);
				}
				else if(v.getId()==bottomView.getId()){
					bottomBorder.setVisibility(View.INVISIBLE);
				}
			}
			
		}
	};
	
	OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int pos = (Integer) v.getTag();
			context.openShot(pos);
		}
	};
	
	OnKeyListener onKeyListener = new OnKeyListener() {
		
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if(event.getAction()==KeyEvent.ACTION_DOWN){
				int pos = (Integer) v.getTag();
				if(keyCode==KeyEvent.KEYCODE_DPAD_UP && v==topView){
					context.setCoverBorderFocus();
					return true;
				}
				else if(keyCode==KeyEvent.KEYCODE_DPAD_LEFT && (pos==0||pos==1)){
					context.setBigBorderFocus();
					return true;
				}
			}
			
			return false;
		}
	};
	
	OnTouchListener onTouchListener = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				OnFocusChangeListener listener = v.getOnFocusChangeListener();
				if (listener != null) {
					touchView = v;
				}
			}
			return false;
		}
	};
	
	public RelativeLayout getTopView() {
		return topView;
	}
	public RelativeLayout getBottomView() {
		return bottomView;
	}
	public ImageView getTopCover() {
		return topCover;
	}
	public ImageView getBottomCover() {
		return bottomCover;
	}
}
