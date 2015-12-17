package com.atet.tvmarket.control.home.holder;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.home.inf.OnRecyItemClickListener;
import com.atet.tvmarket.control.home.view.GameCenterFirstPanel;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.DataConfig.UpdateInterface;
import com.atet.tvmarket.utils.UIUtils;


/**
 * @description: 游戏中心的第一部分的holder
 *
 * @author: Lijie
 * @date: 2015年6月12日 上午9:43:51 
 */
public class GameCenterFirstHolder extends ViewHolder implements 
		OnFocusChangeListener, OnClickListener, OnKeyListener{
	private ALog alog = ALog.getLogger(GameCenterFirstHolder.class);
	private OnRecyItemClickListener mListener;
	public GameCenterFirstPanel panel ;
	private RecyclerView recyclerView;
	private ImageFetcher imageFetcher;
	
	/*private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			panel.getGame_recomment_reflect().setImageBitmap(reflectBitmap);
		};
	}; */
	
	public GameCenterFirstHolder(View itemView,RecyclerView recyclerView,
			OnRecyItemClickListener listener) {
		super(itemView);
		this.mListener = listener;
		this.recyclerView = recyclerView;
		initEvent();
	}
	
	private void initEvent() {
		
		imageFetcher = new ImageFetcher();
		panel = (GameCenterFirstPanel) itemView.findViewById(R.id.game_center_first_panel);
		
		panel.getQuick_start().setOnFocusChangeListener(this);
		panel.getQuick_start().setOnClickListener(this);
		panel.getQuick_start().setOnKeyListener(this);
		
		panel.getGame_recommend().setOnFocusChangeListener(this);
		panel.getGame_recommend().setOnClickListener(this);
		panel.getGame_recommend().setOnKeyListener(this);
		
		panel.getGame_search().setOnFocusChangeListener(this);
		panel.getGame_search().setOnClickListener(this);
		panel.getGame_search().setOnKeyListener(this);
		//DataFetcher.getAppConfig().isGameRecommendExist()
		if(DataFetcher.getAppConfig().isGameRecommendExist()){
			//新游戏推荐是否存在
			imageFetcher.loadLocalImage(R.drawable.game_center_new_recommend, panel.getGame_recommend_iv(), R.drawable.game_center_new_recommend);
		}else{
			imageFetcher.loadLocalImage(R.drawable.game_center_joypad, panel.getGame_recommend_iv(), R.drawable.game_center_joypad);
			panel.getGame_recommend_name().setText(R.string.joypad_buy);
		}
		imageFetcher.loadLocalImage(R.drawable.game_center_quick_start, panel.getQuick_start_iv(), R.drawable.game_center_quick_start);
		imageFetcher.loadLocalImage(R.drawable.game_center_game_search, panel.getGame_search_iv(), R.drawable.game_center_game_search);
//		addReflect();
	}
	
	public void setData(List<String> interfaceList) {
		
		if (DataFetcher.existNewDownloadGame()) {
			// 快速开始有新下载的游戏
			panel.getQuick_start_new().setVisibility(View.VISIBLE);
			// 点击后移除本地记录
		}

		if (interfaceList.contains(UpdateInterface.GAME_NEW_UPLOADED)) {
			// 新品推荐有更新
			panel.getGame_recommend_new().setVisibility(View.VISIBLE);
			// 点击后移除本地记录
		}
		
	}
	
	/*private void addReflect() {
		
		ThreadManager.getThreadPool().execute(new Runnable() {
			
			@Override
			public void run() {
				reflectBitmap = ImageReflectUtil.createReflectBitmap(R.drawable.game_search_item_bg, Constant.PIC_REFLECT_HEIGHT);
				Message msg = Message.obtain();
				mHandler.sendMessage(msg);
			}
		});
		
	}*/

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		
		if(hasFocus){
			
			if(v.getId() == panel.getQuick_start().getId()){
				recyclerView.smoothScrollBy(-v.getWidth() * 2, 0);
				panel.getQuick_start_shadow().setImageResource(R.drawable.white_focus);
				panel.getQuick_start_content().setScaleX(1.1f);
				panel.getQuick_start_content().setScaleY(1.1f);
				panel.getQuick_start().setNextFocusRightId(R.id.main_push_content);
				panel.getQuick_start().setNextFocusUpId(R.id.tab_gamecenter);
			}
			if(v.getId() == panel.getGame_recommend().getId()){
				panel.getGame_recommend_shadow().setImageResource(R.drawable.white_focus);
				panel.getGame_recommend_content().setScaleX(1.1f);
				panel.getGame_recommend_content().setScaleY(1.1f);
			}
			if(v.getId() == panel.getGame_search().getId()){
				panel.getGame_search_shadow().setImageResource(R.drawable.white_focus);
				panel.getGame_search_content().setScaleX(1.1f);
				panel.getGame_search_content().setScaleY(1.1f);
			}
			
			if(v.isInTouchMode()){
				v.performClick();
			}
		}
		else{
			if(v.getId() == panel.getQuick_start().getId()){
				panel.getQuick_start_shadow().setImageResource(android.R.color.transparent);
				panel.getQuick_start_content().setScaleX(1.0f);
				panel.getQuick_start_content().setScaleY(1.0f);
			}
			if(v.getId() == panel.getGame_recommend().getId()){
				panel.getGame_recommend_shadow().setImageResource(android.R.color.transparent);
				panel.getGame_recommend_content().setScaleX(1.0f);
				panel.getGame_recommend_content().setScaleY(1.0f);
			}
			if(v.getId() == panel.getGame_search().getId()){
				panel.getGame_search_shadow().setImageResource(android.R.color.transparent);
				panel.getGame_search_content().setScaleX(1.0f);
				panel.getGame_search_content().setScaleY(1.0f);
			}
			
		}
	}
	@Override
	public void onClick(View view) {
		if(mListener != null){
			mListener.onItemClick(view, getPosition());
		}
	}

	@Override
	public boolean onKey(View view, int keyCode, KeyEvent event) {
		if(event.getAction() == KeyEvent.ACTION_DOWN){
			if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
				if(view.getId() == panel.getQuick_start().getId()){
					panel.getGame_recommend().requestFocus();
//					NewToast.makeToast(UIUtils.getContext(), "quick点击了", 0).show();
					return true;
				}else if(view.getId() == panel.getGame_recommend().getId()){
					panel.getGame_search().requestFocus();
//					NewToast.makeToast(UIUtils.getContext(), "recommed点击了", 0).show();
					return true;
				}else{
					
					return true;
				}
			}else if(keyCode == KeyEvent.KEYCODE_DPAD_UP) {
				if(view.getId() == panel.getGame_search().getId()){
					panel.getGame_recommend().requestFocus();
//					NewToast.makeToast(UIUtils.getContext(), "quick点击了", 0).show();
					return true;
				}else if(view.getId() == panel.getGame_recommend().getId()){
					panel.getQuick_start().requestFocus();
//					NewToast.makeToast(UIUtils.getContext(), "recommed点击了", 0).show();
					return true;
				}
			}
			
		}
		return false;
	}
	
}
