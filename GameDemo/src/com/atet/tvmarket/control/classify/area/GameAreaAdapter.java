package com.atet.tvmarket.control.classify.area;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.atet.tvmarket.R;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.classify.detail.ThirdGameDetailActivity;
import com.atet.tvmarket.control.mygame.activity.GameDetailActivity;
import com.atet.tvmarket.entity.dao.TypeToGame;
import com.atet.tvmarket.model.DataConfig;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.view.recyclerview.BaseAdapter;
import com.atet.tvmarket.view.recyclerview.BaseViewHolder;

public class GameAreaAdapter extends BaseAdapter {

	private RecyclerView mRecyclerView;
	private GameAreaActivity context;
	private ImageFetcher mImageFetcher;
	private View lastFocusView=null;
	private List<TypeToGame> games = new ArrayList<TypeToGame>();
	private int lastColNum = 0,flag=0;
	public GameAreaAdapter(RecyclerView recyclerView,GameAreaActivity context,ImageFetcher mImageFetcher){
		this.mRecyclerView = recyclerView;
		this.context = context;
		this.mImageFetcher = mImageFetcher;
	}
	
	public void setData(List<TypeToGame> games) {
		
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		
		this.games.clear();
		this.games.addAll(games);
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		int col = 0;
		if(games.size()%3==0){
			col=games.size()/3;
		}
		else{
			col = games.size()/3+1;
		}
		lastColNum = games.size()%3;
		Log.i("life", "lastColNum:"+lastColNum);
		return col;
	}

	@Override
	public void onBindViewHolder(BaseViewHolder holder, int position) {
		ItemViewHolder viewHolder = (ItemViewHolder) holder;
		GameAreaPanel panel = (GameAreaPanel) viewHolder.itemView;
		/*panel.getOneView().setVisibility(View.INVISIBLE);
		panel.getTwoView().setVisibility(View.INVISIBLE);
		panel.getThreeView().setVisibility(View.INVISIBLE);*/
		if(position==getItemCount()-1){
			if(lastColNum==0){
				flag=3;
				panel.getOneView().setVisibility(View.VISIBLE);
				panel.getTwoView().setVisibility(View.VISIBLE);
				panel.getThreeView().setVisibility(View.VISIBLE);
			}
			else{
				flag = lastColNum;
				if(flag==1){
					panel.getOneView().setVisibility(View.VISIBLE);
					panel.getTwoView().setVisibility(View.INVISIBLE);
					panel.getThreeView().setVisibility(View.INVISIBLE);
				}
				if(flag==2){
					panel.getOneView().setVisibility(View.VISIBLE);
					panel.getTwoView().setVisibility(View.VISIBLE);
					panel.getThreeView().setVisibility(View.INVISIBLE);
				}
			}
		}
		else{
			flag=3;
			panel.getOneView().setVisibility(View.VISIBLE);
			panel.getTwoView().setVisibility(View.VISIBLE);
			panel.getThreeView().setVisibility(View.VISIBLE);
		}
		for(int i=0;i<flag;i++){
			TypeToGame typeToGame = games.get(position*3+i);
			if(i==0){
				if(typeToGame!=null){
					panel.getOneView().setGameName(typeToGame.getGameInfo().getGameName());
					panel.getOneView().setGameIcon(typeToGame.getGameInfo().getMinPhoto());
					panel.getOneView().setGameId(typeToGame.getGameId());
					if(getItemCount()-1==0){
						panel.getOneView().setTag(3);
					}
					else if(position==0){
						panel.getOneView().setTag(0);
					}
					else if(position==getItemCount()-1){
						panel.getOneView().setTag(1);
					}
					else{
						panel.getOneView().setTag(2);
					}
				}
			}
			else if(i==1){
				if(typeToGame!=null){
					panel.getTwoView().setGameName(typeToGame.getGameInfo().getGameName());
					panel.getTwoView().setGameIcon(typeToGame.getGameInfo().getMinPhoto());
					panel.getTwoView().setGameId(typeToGame.getGameId());
					if(getItemCount()-1==0){
						panel.getTwoView().setTag(3);
					}
					else if(position==0){
						panel.getTwoView().setTag(0);
					}
					else if(position==getItemCount()-1){
						panel.getTwoView().setTag(1);
					}
					else{
						panel.getTwoView().setTag(2);
					}
				}
			}
			else if(i==2){
				if(typeToGame!=null){
					panel.getThreeView().setGameName(typeToGame.getGameInfo().getGameName());
					panel.getThreeView().setGameIcon(typeToGame.getGameInfo().getMinPhoto());
					panel.getThreeView().setGameId(typeToGame.getGameId());
					if(getItemCount()-1==0){
						panel.getThreeView().setTag(3);
					}
					else if(position==0){
						panel.getThreeView().setTag(0);
					}
					else if(position==getItemCount()-1){
						panel.getThreeView().setTag(1);
					}
					else{
						panel.getThreeView().setTag(2);
					}
				}
			}
		}
		
		if(position==0){
			if(lastFocusView==null){
				panel.getOneView().requestFocusFromTouch();
			}
		}
	}

	@Override
	public BaseViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
		GameAreaPanel root = new GameAreaPanel(context);
		ScaleViewUtils.scaleView(root);
		return new ItemViewHolder(root);
	}

	class ItemViewHolder extends BaseViewHolder{
		public ItemViewHolder(View itemView) {
			super(itemView);
		}
		
		
	}

	public void setLastFocusView(View lastFocusView) {
		this.lastFocusView = lastFocusView;
	}
}
