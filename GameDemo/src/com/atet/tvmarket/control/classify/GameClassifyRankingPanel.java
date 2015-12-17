package com.atet.tvmarket.control.classify;

import java.util.ArrayList;
import java.util.List;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.classify.ranking.GameRankingActivity;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.view.recyclerview.BaseBean;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 
 * @author htliu
 *
 */
public class GameClassifyRankingPanel extends RelativeLayout{
	
	private ImageFetcher mImageFetcher;
	RecyclerView gameRankingList;
	LinearLayout more;
	ImageView border;
	GameClassifyRankingPanelAdapter mAdapter;
	private List<GameInfo> myGames = new ArrayList<GameInfo>();
	ALog alog = ALog.getLogger(GameClassifyRankingPanel.class);
	public GameClassifyRankingPanel(Context context){
		this(context,null);
	}
	
	public GameClassifyRankingPanel(Context context, AttributeSet attrs){
		this(context,attrs,0);
	}

	public GameClassifyRankingPanel(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		LayoutInflater.from(context).inflate(R.layout.gameclassify_rankingpanel, this, true);
		gameRankingList = (RecyclerView)findViewById(R.id.rv_ranking_list);
		more = (LinearLayout)findViewById(R.id.ll_more);
		border = (ImageView)findViewById(R.id.iv_border);
		GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
		layoutManager.setOrientation(RecyclerView.VERTICAL);
		gameRankingList.setLayoutManager(layoutManager);
		gameRankingList.addItemDecoration(new GameClassifyRankingPanelInsetDecoration(context));
		mImageFetcher = new ImageFetcher();
		mAdapter = new GameClassifyRankingPanelAdapter(context,gameRankingList,mImageFetcher);
		gameRankingList.setAdapter(mAdapter);
		more.setOnFocusChangeListener(onFocusChangeListener);
		more.setOnClickListener(onClickListener);
		more.setOnKeyListener(onKeyListener);
		
	}
	
	public void setGames(List<GameInfo> games){
		myGames.clear();
		mAdapter.setData(games);
	}
	
	public RecyclerView getGameRankingList() {
		return gameRankingList;
	}

	public void setGameRankingList(RecyclerView gameRankingList) {
		this.gameRankingList = gameRankingList;
	}
	
	public LinearLayout getMore() {
		return more;
	}

	public void setMore(LinearLayout more) {
		this.more = more;
	}
	
	OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus){
				v.requestLayout();
				border.setImageResource(R.drawable.game_detail_focus);
				if(v.isInTouchMode()){
					v.performClick();
				}
			}
			else{
				v.requestLayout();
				border.setImageResource(android.R.color.transparent);
			}
		}
	};
	
	OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getContext(),GameRankingActivity.class);
			getContext().startActivity(intent);
		}
	};
	
	OnKeyListener onKeyListener = new OnKeyListener() {
		
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if(event.getAction()==KeyEvent.ACTION_DOWN ){
				if(keyCode==KeyEvent.KEYCODE_DPAD_UP){
					gameRankingList.getLayoutManager().findViewByPosition(mAdapter.getItemCount()-1).requestFocus();
					return true;
				}else if(keyCode==KeyEvent.KEYCODE_DPAD_RIGHT){
					RecyclerView recyclerView = (RecyclerView) v.getParent().getParent().getParent().getParent();
					GameClassifySinglePanel panel = (GameClassifySinglePanel) recyclerView.getLayoutManager().findViewByPosition(1);
					panel.getLayout().requestFocus();
					return true;
				}
				else if(keyCode==KeyEvent.KEYCODE_DPAD_DOWN || keyCode==KeyEvent.KEYCODE_DPAD_LEFT){
					return true;
				}
			}
			return false;
		}
	};
}
