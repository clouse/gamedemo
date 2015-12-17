package com.atet.tvmarket.control.home.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout.LayoutParams;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.home.MainActivity;
import com.atet.tvmarket.control.home.holder.GameCenterFirstHolder;
import com.atet.tvmarket.control.home.holder.GameCenterSecondHolder;
import com.atet.tvmarket.control.home.holder.GameCenterThirdHolder;
import com.atet.tvmarket.control.home.holder.GameCenterfifthHolder;
import com.atet.tvmarket.control.home.holder.GameCenterfourthHolder;
import com.atet.tvmarket.control.home.inf.OnRecyItemClickListener;
import com.atet.tvmarket.entity.dao.AdInfo;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UIUtils;

/*
 * File：RecyclerViewAdapter.java
 *
 * Copyright (C) 2015 ATET_MARKET_3.6 Project
 * Date：2015年5月28日 下午2:39:01
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */

/**
 * @description: 处理游戏中心的RecyclerView的Adapter
 * 
 * @author: LiJie
 * @date: 2015年5月28日 下午5:53:30
 */
public class GameCeneterAdapter extends Adapter<ViewHolder> implements
		OnKeyListener {
	ALog alog = ALog.getLogger(GameCeneterAdapter.class);
	private int viewType;
	private RecyclerView recyclerView;
	private OnRecyItemClickListener mListener;
	private GameCenterThirdHolder thirdHolder;
	private GameCenterfourthHolder fourHolder;
	private GameCenterfifthHolder fifthHolder;
	private Context context;
	private List<AdInfo> infos = new ArrayList<AdInfo>();
	private List<AdInfo> infos1 = new ArrayList<AdInfo>();
	private List<AdInfo> infos2 = new ArrayList<AdInfo>();
	private List<AdInfo> infos3 = new ArrayList<AdInfo>();
	private List<AdInfo> infos4 = new ArrayList<AdInfo>();
	private int size;
	private List<String> interfaceList;

	public GameCeneterAdapter(RecyclerView mRecyclerView, Context context,
			List<AdInfo> infos) {
		this.recyclerView = mRecyclerView;
		this.context = context;

	}
	
	public void setData(List<AdInfo> adInfos) {
		infos.clear();
		infos1.clear();
		infos2.clear();
		infos3.clear();
		infos4.clear();
		infos.addAll(adInfos);
		
		if (infos != null) {
			alog.info("size = " + infos.size());
			for (int i = 0; i < infos.size(); i++) {
				String type = infos.get(i).getSizeType();
				alog.info(infos.get(i).getSizeType());
				if ("yixing".equals(type)) {
					infos1.add(infos.get(i));
				} else if ("heng".equals(type)) {
					infos2.add(infos.get(i));
				} else if ("fang".equals(type)) {
					infos3.add(infos.get(i));
				} else {
					infos4.add(infos.get(i));
				}
			}
			int size3 = infos3.size() % 2 == 0 ? infos3.size() / 2 : infos3.size() / 2 + 1;
			
			//判断方图和竖图的数量,竖图和方图
			if(size3 - 1 > infos4.size()){
				size = 3 + infos4.size() * 2;
			}else if (infos4.size() > size3){
				size = 2 + size3 * 2;
			}else{
				size = 2 + size3 + infos4.size();
			}
			
			alog.info("infos3  =  " + size + "---" + infos1.size() + "--" + infos2.size()+ "--" + infos3.size() + "--" +infos4.size());
		}
		recyclerView.setAdapter(this);
		
	}
	
	public void checkUpdata(List<String> interfaceList) {
		this.interfaceList  = interfaceList;
		notifyDataSetChanged();
		
	}

	@Override
	public int getItemCount() {
		if (size != 0) {
			return size;
		}
		return 3;
	}

	@Override
	public int getItemViewType(int position) {
		switch (position) {
		case 0:
			viewType = Constant.VIEWHOLDER_TYPE_0;
			break;
		case 1:
			viewType = Constant.VIEWHOLDER_TYPE_1;
			break;
		case 2:
			viewType = Constant.VIEWHOLDER_TYPE_2;
			break;
		default:
			if (position >= 3) {
				if (position % 2 == 1) {
					viewType = Constant.VIEWHOLDER_TYPE_3;
				} else {
					viewType = Constant.VIEWHOLDER_TYPE_4;
				}
			}
			break;

		}

		return viewType;
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		if(position == 0){
			GameCenterFirstHolder firstHolder= (GameCenterFirstHolder) viewHolder; 
			if(interfaceList != null && interfaceList.size() != 0){
				firstHolder.setData(interfaceList);
			}
		}
		if (position == 1) {
			GameCenterSecondHolder holder = (GameCenterSecondHolder) viewHolder;
			if (infos1.size() != 0) {
				holder.setData(infos1.get(0));
			}

		} else if (position == 2) {
			thirdHolder = (GameCenterThirdHolder) viewHolder;
			thirdHolder.panel.getMain_game().setOnKeyListener(this);
			if(infos2.size() != 0 && infos3.size() != 0){
				thirdHolder.setData(infos2, infos3);
			}
		} else if (position > 2) {
			if (position % 2 == 1) {
				fourHolder = (GameCenterfourthHolder) viewHolder;
				fourHolder.panel.getMore_game().setOnKeyListener(this);
				LayoutParams params = new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

				if (position == 3) {
					params.leftMargin = -(int)ScaleViewUtils.resetTextSize(16);
					fourHolder.panel.getMore_game().setNextFocusLeftId(
							R.id.game_center_main_game);
				}

				if (position == getItemCount() - 1) {
					params.rightMargin = 50;
				}
				fourHolder.itemView.setLayoutParams(params);
				alog.info("position = " + position + "----size = "
						+ infos4.size());
				position = (position - 3) / 2;
				
				if(infos4.size() != 0){
					fourHolder.setData(infos4.get(position));
				}

			} else {
				fifthHolder = (GameCenterfifthHolder) viewHolder;
				//fifthHolder.panel.getItem5_top().setOnKeyListener(this);
				if(infos3.size() != 0){
					fifthHolder.setData(infos3, position);
				}
			}

		}

	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(UIUtils.getContext());
		View root = null;
		ViewHolder holder = null;
		switch (viewType) {

		case Constant.VIEWHOLDER_TYPE_0:
			root = inflater.inflate(R.layout.game_center_fist_panel, viewGroup,
					false);
			holder = new GameCenterFirstHolder(root, recyclerView, mListener);
			break;
		case Constant.VIEWHOLDER_TYPE_1:
			root = inflater.inflate(R.layout.game_center_second_panel,
					viewGroup, false);
			holder = new GameCenterSecondHolder(root, recyclerView, mListener,context);

			break;
		case Constant.VIEWHOLDER_TYPE_2:
			root = inflater.inflate(R.layout.game_center_third_panel,
					viewGroup, false);
			holder = new GameCenterThirdHolder(root, recyclerView, mListener,
					context);
			break;
		case Constant.VIEWHOLDER_TYPE_3:
			root = inflater.inflate(R.layout.game_center_fourth_panel,
					viewGroup, false);
			holder = new GameCenterfourthHolder(root, recyclerView, mListener,context);
			break;
		case Constant.VIEWHOLDER_TYPE_4:
			root = inflater.inflate(R.layout.game_center_fifth_panel,
					viewGroup, false);
			holder = new GameCenterfifthHolder(root, recyclerView,context);
			break;

		default:
			break;
		}

		return holder;
	}

	/**
	 * 
	 * @description: 处理点击事件的监听
	 * 
	 * @param listener
	 * @throws:
	 * @author: LiJie
	 * @date: 2015年6月12日 上午9:41:56
	 */
	public void setOnRecyItemClickListener(OnRecyItemClickListener listener) {
		this.mListener = listener;
	}

	/**
	 * 处理当游戏中心界面第一个holder不可见是，点击下键跳转到上一个聚焦的view
	 */
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		int action = event.getAction();

		if (action == KeyEvent.ACTION_DOWN
				&& keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			((MainActivity) context).forceFocusTab(MainActivity.TAB_ID_GAMECENTER, 0);
			((MainActivity) context).setGameCenterFcous(recyclerView.getChildAt(0));
			return true;
		}
		return false;
	}

}
