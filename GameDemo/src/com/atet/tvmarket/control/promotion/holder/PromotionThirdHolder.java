package com.atet.tvmarket.control.promotion.holder;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewParent;
import android.widget.RelativeLayout;

import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.home.MainActivity;
import com.atet.tvmarket.control.home.inf.OnRecyItemClickListener;
import com.atet.tvmarket.control.promotion.activity.PromotionDetailsActivity;
import com.atet.tvmarket.control.promotion.panel.PromotionFirstPanel;
import com.atet.tvmarket.control.promotion.panel.PromotionFourthPanel;
import com.atet.tvmarket.entity.dao.ActInfo;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.DataConfig.UpdateInterface;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.view.BaseImageView;

/**
 * @description: 游戏中心第二部分布局的holder
 * 
 * @author: LiJie
 * @date: 2015年6月12日 上午9:55:15
 */
public class PromotionThirdHolder extends ViewHolder implements
		OnFocusChangeListener, OnClickListener, OnKeyListener {
	private RecyclerView mRecyclerView;
	private ImageFetcher imageFetcher;
	public RelativeLayout more_game;
	private RelativeLayout more_game_content;
	private BaseImageView more_game_icon;
	private BaseImageView more_game_shadow;
	public BaseImageView more_game_new;
	private ActInfo actInfo;
	private Context context;
	private OnRecyItemClickListener mListener;

	public PromotionThirdHolder(View itemView, RecyclerView recyclerView,OnRecyItemClickListener mListener,
			Context context) {
		super(itemView);
		ScaleViewUtils.scaleView(itemView);
		this.mListener = mListener;
		this.mRecyclerView = recyclerView;
		this.context = context;
		initView();

	}

	private void initView() {
		imageFetcher = new ImageFetcher();
		more_game = (RelativeLayout) itemView.findViewById(R.id.promotion_more_game);
		more_game_content = (RelativeLayout) itemView.findViewById(R.id.promotion_more_game_content);
		more_game_icon = (BaseImageView) itemView.findViewById(R.id.promotion_more_game_iv);
		more_game_shadow = (BaseImageView) itemView.findViewById(R.id.promotion_more_game_shadow);
		more_game_new = (BaseImageView) itemView.findViewById(R.id.promotion_more_game_new);

		more_game.setOnFocusChangeListener(this);
		more_game.setOnClickListener(this);
		more_game.setOnKeyListener(this);
	}

	public void setData(ActInfo actInfo) {
		this.actInfo = actInfo;
		imageFetcher.loadImage(actInfo.getErectPhoto(), more_game_icon,R.drawable.default_vertical);
	}
	
	public void checkUpdate(List<String> interfaceList,int position ) {
		
		if(position  == 3 && interfaceList.contains(UpdateInterface.GAME_GIFT)){
			//礼包有更新
			more_game_new.setVisibility(View.VISIBLE);
			//点击后移除本地记录
		}
		
		if(position == 2 && interfaceList.contains(UpdateInterface.USER_TASK)){
			//每日任务有更新
			more_game_new.setVisibility(View.VISIBLE);
			//点击后移除本地记录
		
		}
		
	}
	
	public void setDefaultData(int position) {
		if(position == 2){
			more_game_icon.setBackgroundResource(R.drawable.integral_default);
		}else{
			more_game_icon.setBackgroundResource(R.drawable.gift_default);
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			
			int[] location = new int[2];
			v.getLocationOnScreen(location);

			if (location[0] + v.getWidth() * 1.5 > mRecyclerView.getRight()) {
				mRecyclerView.smoothScrollBy(v.getWidth(), 0);
			}
			
			if (location[0] - v.getWidth() < mRecyclerView.getLeft()) {
				mRecyclerView.smoothScrollBy(-v.getWidth(), 0);
			}
			
			if(v.isInTouchMode()){
				v.performClick();
			}

			if (v.getId() == R.id.promotion_more_game) {
				more_game_shadow.setBackgroundResource(R.drawable.white_focus);
				more_game_content.setScaleX(1.1f);
				more_game_content.setScaleY(1.1f);
				more_game.setNextFocusUpId(R.id.tab_gamecenter);
				more_game.setNextFocusDownId(more_game.getId());
			}
		} else {
			if (v.getId() == R.id.promotion_more_game) {
				more_game_shadow.setBackgroundResource(android.R.color.transparent);
				more_game_content.setScaleX(1.0f);
				more_game_content.setScaleY(1.0f);
			}
		}
	}

	@Override
	public void onClick(View v) {
		if(mListener != null){
			mListener.onItemClick(v, getPosition());
		}
		/*Intent intent = new Intent(context,PromotionDetailsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Constant.ACTINFO,actInfo);
		intent.putExtras(bundle);
		context.startActivity(intent);*/
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		int action = event.getAction();
		RelativeLayout rl = (RelativeLayout) v.getParent();
		int position = mRecyclerView.getChildAdapterPosition(rl);
		if (action == KeyEvent.ACTION_DOWN) {
			
			if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
				((MainActivity) context).forceFocusTab(MainActivity.TAB_ID_PROMOTION, 0);
				if(mRecyclerView.getChildAt(0).getId() == R.id.promotion_first_panel){
					PromotionFirstPanel panel = (PromotionFirstPanel) mRecyclerView.getChildAt(0);
					((MainActivity) context).setPromotionFcous(panel.promotion_area);
				}else{
					((MainActivity) context).setPromotionFcous(mRecyclerView.getChildAt(0));
				}
				
				return true;
			}else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
				if(position <= 3){
					return false;
				}else{
					PromotionFourthPanel panel = (PromotionFourthPanel)mRecyclerView.getLayoutManager().findViewByPosition(position-1);
					panel.getItem4_top().requestFocus();
					return true;
				}
			}else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
				if(position < 3){
					return false;
				}else if(position < mRecyclerView.getLayoutManager().getItemCount()-1){
					PromotionFourthPanel panel = (PromotionFourthPanel)mRecyclerView.getLayoutManager().findViewByPosition(position+1);
					panel.getItem4_top().requestFocus();
					return true;
				}
			}else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
				
				return true;
			}
		}
		return false;
	}

}
