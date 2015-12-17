package com.atet.tvmarket.control.classify;

import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.home.MainActivity;
import com.atet.tvmarket.control.mygame.activity.GameDetailActivity;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GameClassifyRankingItem extends RelativeLayout{
	
	private RelativeLayout root,content;
	private ImageView border,cover;
	private TextView text;
	private View touchView;
	private String gameId;
	private ImageFetcher mImageFetcher;
	MainActivity context;
	public GameClassifyRankingItem(Context context){
		this(context, null);
	}
	
	public GameClassifyRankingItem(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public GameClassifyRankingItem(Context context, AttributeSet attrs,int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = (MainActivity)context;
		mImageFetcher = this.context.mImageFetcher;
		LayoutInflater.from(context).inflate(R.layout.gameclassify_rankingpanel_item1, this, true);
		root = (RelativeLayout)findViewById(R.id.rl_root);
		content = (RelativeLayout)findViewById(R.id.rl_content);
		border = (ImageView)findViewById(R.id.iv_border);
		cover = (ImageView)findViewById(R.id.iv_game_cover);
		text = (TextView)findViewById(R.id.tv_game_name);
		
		root.setOnTouchListener(onTouchListener);
		root.setOnFocusChangeListener(onFocusChangeListener);
		root.setOnClickListener(onClickListener);
		root.setOnKeyListener(onKeyListener);
	}

	OnTouchListener onTouchListener = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				OnFocusChangeListener listener = v.getOnFocusChangeListener();
				if(listener!=null){
					touchView = v;
				}
			}
			return false;
		}
	};
	
	OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus){
				v.requestLayout();
				if(v.isInTouchMode() && v == touchView){
					v.performClick();
				}
				border.setVisibility(View.VISIBLE);
				content.setScaleX(1.1f);
				content.setScaleY(1.1f);
			}
			else{
				v.requestLayout();
				border.setVisibility(View.INVISIBLE);
				content.setScaleX(1.0f);
				content.setScaleY(1.0f);
			}
		}
	};
	
	OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(context, GameDetailActivity.class);
			intent.putExtra("gameId", gameId);
			intent.putExtra(Constant.GAMECENTER, 2);
			context.startActivity(intent);
		}
	};
	
	OnKeyListener onKeyListener = new OnKeyListener() {
		
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			View view = (View) v.getParent();
			int pos = (Integer) view.getTag();
			int action  = event.getAction();
			if(action == KeyEvent.ACTION_DOWN){
				if(keyCode==KeyEvent.KEYCODE_DPAD_UP){
					if(pos==0||pos==1){
						context.findViewById(R.id.tab_categroy).requestFocus();
					}
					else if(pos==2 || pos==3){
						GameClassifyRankingThreePanel panel = (GameClassifyRankingThreePanel) view.getParent();
						panel.getOneView().requestFocus();
					}
					else if(pos==4 || pos==5){
						GameClassifyRankingThreePanel panel = (GameClassifyRankingThreePanel) view.getParent();
						panel.getTwoView().requestFocus();
					}
					return true;
				}
			}
			return false;
		}
	};
	
	public void setCover(String coverUrl){
		if(mImageFetcher!=null){
			mImageFetcher.loadImage(coverUrl, cover, R.drawable.gameranking_item_icon);
		}
	}
	
	public void setGameName(int order,String gameName){
		if(order==-1){
			text.setText("");
		}
		else{
			text.setText(order+"."+gameName);
		}
	}
	
	public void setGameId(String gameId){
		this.gameId = gameId;
	}
}
