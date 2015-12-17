package com.atet.tvmarket.control.classify.area;

import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.mygame.activity.GameDetailActivity;
import com.atet.tvmarket.entity.dao.TypeToGame;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GameAreaItem extends RelativeLayout implements OnFocusChangeListener,OnClickListener,OnKeyListener,OnTouchListener{

	private View touchView;
	private RelativeLayout root;
	private LinearLayout content;
	private ImageView border;
	private ImageView gameIcon;
	private TextView gameName;
	private String gameId;
	private TypeToGame typeToGame;
	private GameAreaActivity context;
	public GameAreaItem(Context context){
		this(context, null);
	}
	
	public GameAreaItem(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public GameAreaItem(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = (GameAreaActivity) context;
		LayoutInflater.from(context).inflate(R.layout.gameclassify_area_item, this, true);
		root = (RelativeLayout)findViewById(R.id.rl_root);
		content = (LinearLayout)findViewById(R.id.ll_content);
		border = (ImageView)findViewById(R.id.iv_border);
		gameIcon = (ImageView)findViewById(R.id.iv_icon);
		gameName = (TextView)findViewById(R.id.tv_name);
		
		root.setOnFocusChangeListener(this);
		root.setOnClickListener(this);
		root.setOnKeyListener(this);
		root.setOnTouchListener(this);
	}
	
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

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if(event.getAction()==KeyEvent.ACTION_DOWN){
			GameAreaItem item  = (GameAreaItem) v.getParent();
			int pos = (Integer) item.getTag();
			if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
				if(pos==1 || pos==3){
					context.nextPage();
					return true;
				}
				
			}
			else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
				if(pos==0 || pos==3){
					context.previousPage();
					return true;
				}
				
			}
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(context,GameDetailActivity.class);
		intent.putExtra(Constant.GAMECENTER, 3);
		intent.putExtra("gameId", gameId);
		context.startActivity(intent);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus){
			v.requestLayout();
			border.setVisibility(View.VISIBLE);
			content.setScaleX(1.2f);
			content.setScaleY(1.2f);
			if(v.isInTouchMode() && v == touchView){
				v.performClick();
			}
			else{
				touchView = null;
			}
		}
		else{
			v.requestLayout();
			border.setVisibility(View.INVISIBLE);
			content.setScaleX(1.0f);
			content.setScaleY(1.0f);
		}
	}

	public void setGameIcon(String url){
		context.mImageFetcher.loadImage(url, gameIcon, R.drawable.gameranking_item_icon);
	}
	
	public void setGameName(CharSequence name){
		gameName.setText(name);
	}

	public void setGameId(String gameId){
		this.gameId = gameId;
	}
	
	public void setTypeToGame(TypeToGame typeToGame){
		this.typeToGame = typeToGame;
	}
}
