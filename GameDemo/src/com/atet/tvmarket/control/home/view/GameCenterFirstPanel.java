package com.atet.tvmarket.control.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atet.tvmarket.R;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UIUtils;
import com.atet.tvmarket.view.BaseImageView;

/**
 * @description: 通过容器将布局包裹起来
 *
 * @author: LiJie
 * @date: 2015年6月12日 上午9:51:26 
 */
public class GameCenterFirstPanel extends RelativeLayout{
	
	private RelativeLayout quick_start;
	private RelativeLayout quick_start_content;
	private BaseImageView quick_start_iv;
	private BaseImageView quick_start_shadow;
	private TextView quick_start_name;
	private ImageView quick_start_new;
	
	private RelativeLayout game_recommend;
	private RelativeLayout game_recommend_content;
	private BaseImageView game_recommend_iv;
	private BaseImageView game_recommend_shadow;
	private TextView game_recommend_name;
	private ImageView game_recommend_new;
	
	private RelativeLayout game_search;
	private RelativeLayout game_search_content;
	private BaseImageView game_search_iv;
	private BaseImageView game_search_shadow;
	private TextView game_search_name;
	
	
	public GameCenterFirstPanel(Context context) {
		this(context,null);
		
	}

	public GameCenterFirstPanel(Context context, AttributeSet attrs) {
		this(context,attrs,0);
		
	}

	public GameCenterFirstPanel(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		View view = LayoutInflater.from(context).inflate(R.layout.game_center_item1,this,true);
		ScaleViewUtils.init(UIUtils.getContext());
		ScaleViewUtils.scaleView(view);
		setChildrenDrawingOrderEnabled(true);
		initView();
	}
	
	private void initView() {
		quick_start = (RelativeLayout) findViewById(R.id.game_center_quick_start);
		quick_start_iv = (BaseImageView) findViewById(R.id.quick_start_iv);
		quick_start_shadow = (BaseImageView) findViewById(R.id.quick_start_shadow);
		quick_start_content = (RelativeLayout) findViewById(R.id.quick_start_content);
		quick_start_name = (TextView) findViewById(R.id.quick_start_name);
		quick_start_new = (ImageView) findViewById(R.id.quick_start_new);
		
		game_recommend = (RelativeLayout) findViewById(R.id.game_center_game_recommend);
		game_recommend_iv = (BaseImageView) findViewById(R.id.game_recommend_iv);
		game_recommend_shadow = (BaseImageView) findViewById(R.id.game_recommend_shadow);
//		game_recomment_reflect = (BaseImageView) findViewById(R.id.game_recommend_reflect);
		game_recommend_content = (RelativeLayout) findViewById(R.id.game_recommend_content);
		game_recommend_name = (TextView) findViewById(R.id.game_recommend_name);
		game_recommend_new = (ImageView) findViewById(R.id.game_recommend_new);
		
		game_search = (RelativeLayout) findViewById(R.id.game_center_game_search);
		game_search_content = (RelativeLayout) findViewById(R.id.game_search_content);
		game_search_iv = (BaseImageView) findViewById(R.id.game_search_iv);
		game_search_shadow = (BaseImageView) findViewById(R.id.game_search_shadow);
		game_search_name = (TextView) findViewById(R.id.game_search_name);
	}
	
	
/*	*//**
	 * 处理视图的叠加效果
	 */
	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		if(hasFocus()){
			 View child =  getFocusedChild();
			if (child != null){
				
				int index = indexOfChild(child);
				if (index != -1){
					if (i < index){
						return i;
					}else {
						return childCount - 1 - i + index;
					}
				}
			}else{
				return i;
			}
		}
		return super.getChildDrawingOrder(childCount, i);
	}
	
	public RelativeLayout getQuick_start() {
		return quick_start;
	}

	public void setQuick_start(RelativeLayout quick_start) {
		this.quick_start = quick_start;
	}

	public RelativeLayout getGame_recommend() {
		return game_recommend;
	}

	public void setGame_recommend(RelativeLayout game_recommend) {
		this.game_recommend = game_recommend;
	}

	public BaseImageView getQuick_start_iv() {
		return quick_start_iv;
	}

	public void setQuick_start_iv(BaseImageView quick_start_iv) {
		this.quick_start_iv = quick_start_iv;
	}

	public BaseImageView getGame_recommend_iv() {
		return game_recommend_iv;
	}

	public void setGame_recommend_iv(BaseImageView game_recommend_iv) {
		this.game_recommend_iv = game_recommend_iv;
	}

	public BaseImageView getQuick_start_shadow() {
		return quick_start_shadow;
	}

	public void setQuick_start_shadow(BaseImageView quick_start_shadow) {
		this.quick_start_shadow = quick_start_shadow;
	}

	public BaseImageView getGame_recommend_shadow() {
		return game_recommend_shadow;
	}

	public void setGame_recommend_shadow(BaseImageView game_recommend_shadow) {
		this.game_recommend_shadow = game_recommend_shadow;
	}

	public RelativeLayout getGame_recommend_content() {
		return game_recommend_content;
	}

	public void setGame_recommend_content(RelativeLayout game_recommend_content) {
		this.game_recommend_content = game_recommend_content;
	}

	public RelativeLayout getQuick_start_content() {
		return quick_start_content;
	}

	public void setQuick_start_content(RelativeLayout quick_start_content) {
		this.quick_start_content = quick_start_content;
	}

	public RelativeLayout getGame_search() {
		return game_search;
	}

	public void setGame_search(RelativeLayout game_search) {
		this.game_search = game_search;
	}

	public RelativeLayout getGame_search_content() {
		return game_search_content;
	}

	public void setGame_search_content(RelativeLayout game_search_content) {
		this.game_search_content = game_search_content;
	}

	public BaseImageView getGame_search_iv() {
		return game_search_iv;
	}

	public void setGame_search_iv(BaseImageView game_search_iv) {
		this.game_search_iv = game_search_iv;
	}

	public BaseImageView getGame_search_shadow() {
		return game_search_shadow;
	}

	public void setGame_search_shadow(BaseImageView game_search_shadow) {
		this.game_search_shadow = game_search_shadow;
	}

	public TextView getQuick_start_name() {
		return quick_start_name;
	}

	public void setQuick_start_name(TextView quick_start_name) {
		this.quick_start_name = quick_start_name;
	}

	public TextView getGame_recommend_name() {
		return game_recommend_name;
	}

	public void setGame_recommend_name(TextView game_recommend_name) {
		this.game_recommend_name = game_recommend_name;
	}

	public TextView getGame_search_name() {
		return game_search_name;
	}

	public void setGame_search_name(TextView game_search_name) {
		this.game_search_name = game_search_name;
	}

	public ImageView getQuick_start_new() {
		return quick_start_new;
	}

	public void setQuick_start_new(ImageView quick_start_new) {
		this.quick_start_new = quick_start_new;
	}

	public ImageView getGame_recommend_new() {
		return game_recommend_new;
	}

	public void setGame_recommend_new(ImageView game_recommend_new) {
		this.game_recommend_new = game_recommend_new;
	}
}
