package com.atet.tvmarket.control.gamerecommand;

import java.util.List;

import com.atet.tvmarket.R;
import com.atet.tvmarket.entity.dao.ScreenShotInfo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RecommanedPanel extends RelativeLayout {
	
	private List<ScreenShotInfo> mScreenShotInfos;
	private RelativeLayout root;
	private ImageView border,cover,video;
	private TextView descText;
	private NewGameRecommandActivity context;
	private TwoPanelItem oneView,twoView;
	private View touchView = null;
	public RecommanedPanel(Context context){
		this(context, null);
	}
	public RecommanedPanel(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	public RecommanedPanel(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = (NewGameRecommandActivity) context;
		setChildrenDrawingOrderEnabled(true);
		LayoutInflater.from(context).inflate(R.layout.newrecommand_panel, this, true);
		root = (RelativeLayout)findViewById(R.id.rl_content);
		border = (ImageView)findViewById(R.id.iv_big_border);
		cover = (ImageView)findViewById(R.id.iv_big_cover);
		video = (ImageView)findViewById(R.id.iv_video);
		descText = (TextView)findViewById(R.id.tv_desc);
		
		root.setOnFocusChangeListener(onFocusChangeListener);
		root.setOnClickListener(onClickListener);
		root.setOnKeyListener(onKeyListener);
		root.setOnTouchListener(onTouchListener);
		
		oneView = (TwoPanelItem)findViewById(R.id.one_recommand_view);
		twoView = (TwoPanelItem)findViewById(R.id.two_recommand_view);
		
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
				if(v.isInTouchMode() && v==touchView){
					v.performClick();
				}
				else{
					touchView = null;
				}
				
				border.setVisibility(View.VISIBLE);
			}
			
			else{
				border.setVisibility(View.INVISIBLE);
			}
			
		}
	};
	
	OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			context.openVideo();
		}
	};
	
	OnKeyListener onKeyListener = new OnKeyListener() {
		
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if(event.getAction()==KeyEvent.ACTION_DOWN && keyCode==KeyEvent.KEYCODE_DPAD_UP){
				context.setCoverBorderFocus();
				return true;
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
	
	public RelativeLayout getRoot() {
		return root;
	}
	public TwoPanelItem getOneView() {
		return oneView;
	}
	public TwoPanelItem getTwoView() {
		return twoView;
	}
	public ImageView getCover() {
		return cover;
	}
	public ImageView getVideo() {
		return video;
	}
	public TextView getDescText() {
		return descText;
	}
	
	
}
