package com.atet.tvmarket.control.mygame.view;

import java.util.ArrayList;

import com.atet.tvmarket.R;

import android.R.color;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * 游戏详情自定义评分条
 * @author chenqingwen
 *
 */
public class ScoreRatingBar extends LinearLayout{
	ArrayList<CheckBox> views = new ArrayList<CheckBox>(); // 用CheckBox实现5个评分星星

	private int selectIndex;

	public ScoreRatingBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public ScoreRatingBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ScoreRatingBar(Context context) {
		super(context);
		init(context);
	}


	/**
	 * 
	 * @description 初始化评分条
	 * @param context
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:10:46
	 *
	 */
	public void init(Context context){
		setOrientation(HORIZONTAL);
		for(int i=0; i<5; i++){  // 初始化
		  CheckBox checkBox = new CheckBox(context);
		  checkBox.setId(i);
		  checkBox.setBackgroundResource(R.drawable.rating_full_selector);  // 设置背景样式
		  checkBox.setTag(new Integer(i));  // 设置标记，判断是哪个星星图标
		  checkBox.setButtonDrawable(new ColorDrawable(color.transparent)); // 去除checkbox按钮
		  checkBox.setOnFocusChangeListener(onFocusChangeListener);
		  checkBox.setNextFocusDownId(R.id.game_detail_score_btn);
		  checkBox.setNextFocusUpId(R.id.game_detail_rbt_two);
		  checkBox.setClickable(true);
			checkBox.setFocusable(true);
			checkBox.setFocusableInTouchMode(true);
		  if(i == 0){
			  checkBox.setNextFocusLeftId(checkBox.getId());
		  }else if(i == 4){
			  checkBox.setNextFocusRightId(checkBox.getId());
		  }
		  views.add(checkBox);
		  addView(checkBox);
		}

		// 默认一个星
		selectIndex = 0;
		views.get(selectIndex).setChecked(true);
	}
	
	private OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View arg0, boolean arg1) {
			// TODO Auto-generated method stub
			int location = (Integer)arg0.getTag();
			if(arg1){
			switch (location) {
			case 0:
				selectIndex = location;
				views.get(0).setChecked(true);
				views.get(1).setChecked(false);
				views.get(2).setChecked(false);
				views.get(3).setChecked(false);
				views.get(4).setChecked(false);
				break;
			case 1:
				selectIndex = location;
				views.get(0).setChecked(true);
				views.get(1).setChecked(true);
				views.get(2).setChecked(false);
				views.get(3).setChecked(false);
				views.get(4).setChecked(false);
				break;
			case 2 :
				selectIndex = location;
				views.get(0).setChecked(true);
				views.get(1).setChecked(true);
				views.get(2).setChecked(true);
				views.get(3).setChecked(false);
				views.get(4).setChecked(false);
				break;
			case 3 :
				selectIndex = location;
				views.get(0).setChecked(true);
				views.get(1).setChecked(true);
				views.get(2).setChecked(true);
				views.get(3).setChecked(true);
				views.get(4).setChecked(false);
				break;
			case 4 :
				selectIndex = location;
				views.get(0).setChecked(true);
				views.get(1).setChecked(true);
				views.get(2).setChecked(true);
				views.get(3).setChecked(true);
				views.get(4).setChecked(true);
				break;
			}
	      }
	   }
	};
	
	/**
	 * 
	 * @description 判断评分条是否处于聚焦状态
	 * @return
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:11:02
	 *
	 */
	public boolean hasFocused(){
		return views.get(0).isFocused() || views.get(1).isFocused() ||
		views.get(2).isFocused() || views.get(3).isFocused() ||
		views.get(4).isFocused();
	}

	public void requestRatingBarFocus() {
		// 获取焦点
		views.get(selectIndex).requestFocusFromTouch();
	}
	
	/**
	 * 
	 * @description 获取评分条的评分数
	 * @return
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:11:56
	 *
	 */
	public int getScore(){
		int count = 0;
		if(views.get(0).isChecked()){
			count++;
		}
		if(views.get(1).isChecked()){
			count++;
		}
		if(views.get(2).isChecked()){
			count++;
		}
		if(views.get(3).isChecked()){
			count++;
		}
		if(views.get(4).isChecked()){
			count++;
		}
		return count;
	}
}
