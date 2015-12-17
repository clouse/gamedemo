package com.atet.tvmarket.control.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.atet.tvmarket.R;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UIUtils;
import com.atet.tvmarket.view.BaseImageView;
import com.atet.tvmarket.view.CloseAcceTextView;

/**
 * @description: 通过容器将布局包裹起来
 * 
 * @author: LiJie
 * @date: 2015年6月12日 上午9:51:26
 */
public class GameCenterfifthPanel extends RelativeLayout {
	private RelativeLayout item5_top;
	private RelativeLayout item5_top_content;
	private BaseImageView item5_top_iv;
	private BaseImageView item5_top_shadow;
	private BaseImageView item5_top_handle_icon;
	private BaseImageView item5_top_control_icon;
	private CloseAcceTextView item5_top_game_online;
	private BaseImageView item5_top_released;
	private BaseImageView item5_top_video;
	
	private RelativeLayout item5_bellow;
	private RelativeLayout item5_bellow_content;
	private BaseImageView item5_bellow_iv;
	private BaseImageView item5_bellow_shadow;
	private BaseImageView item5_bellow_handle_icon;
	private BaseImageView item5_bellow_control_icon;
	private CloseAcceTextView item5_bellow_game_online;
	private BaseImageView item5_bellow_released;
	private BaseImageView item5_bellow_video;
	
	public GameCenterfifthPanel(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}
	public GameCenterfifthPanel(Context context, AttributeSet attrs) {
		this(context, null,0);
		// TODO Auto-generated constructor stub
	}

	public GameCenterfifthPanel(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setChildrenDrawingOrderEnabled(true);
		View view = LayoutInflater.from(context).inflate(R.layout.game_center_item5,this,true);
		ScaleViewUtils.init(UIUtils.getContext());
		ScaleViewUtils.scaleView(view);
		initView();
		
	}
	private void initView() {
		item5_top = (RelativeLayout) findViewById(R.id.item5_top);
		item5_top_iv = (BaseImageView) findViewById(R.id.item5_top_iv);
		item5_top_shadow = (BaseImageView) findViewById(R.id.item5_top_shadow);
		item5_top_content = (RelativeLayout) findViewById(R.id.item5_top_content);
		item5_top_released = (BaseImageView) findViewById(R.id.item5_top_released);
		item5_top_video = (BaseImageView) findViewById(R.id.item5_top_video);
		
		View topView = findViewById(R.id.item5_top_bottom_item);
		item5_top_handle_icon = (BaseImageView) topView.findViewById(R.id.handle_icon);
		item5_top_control_icon = (BaseImageView) topView.findViewById(R.id.control_icon);
		item5_top_game_online = (CloseAcceTextView) topView.findViewById(R.id.game_online);
		
		item5_bellow = (RelativeLayout) findViewById(R.id.item5_bellow);
		item5_bellow_iv = (BaseImageView) findViewById(R.id.item5_bellow_iv);
		item5_bellow_shadow = (BaseImageView) findViewById(R.id.item5_bellow_shadow);
		item5_bellow_content = (RelativeLayout) findViewById(R.id.item5_bellow_content);
		item5_bellow_released = (BaseImageView) findViewById(R.id.item5_bellow_released);
		item5_bellow_video = (BaseImageView) findViewById(R.id.item5_bellow_video);
		
		View bellowView = findViewById(R.id.item5_bellow_bottom_item);
		item5_bellow_handle_icon = (BaseImageView) bellowView.findViewById(R.id.handle_icon);
		item5_bellow_control_icon = (BaseImageView) bellowView.findViewById(R.id.control_icon);
		item5_bellow_game_online = (CloseAcceTextView) bellowView.findViewById(R.id.game_online);
		
	}
	
	public BaseImageView getItem5_top_video() {
		return item5_top_video;
	}
	public void setItem5_top_video(BaseImageView item5_top_video) {
		this.item5_top_video = item5_top_video;
	}
	public BaseImageView getItem5_bellow_video() {
		return item5_bellow_video;
	}
	public void setItem5_bellow_video(BaseImageView item5_bellow_video) {
		this.item5_bellow_video = item5_bellow_video;
	}
	/**
	 * 处理视图的叠加效果
	 */
	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		if(hasFocus()){
			 View child =  getFocusedChild();
			Log.i("life", child.toString());
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
	public RelativeLayout getItem5_top() {
		return item5_top;
	}
	public void setItem5_top(RelativeLayout item5_top) {
		this.item5_top = item5_top;
	}
	public RelativeLayout getItem5_top_content() {
		return item5_top_content;
	}
	public void setItem5_top_content(RelativeLayout item5_top_content) {
		this.item5_top_content = item5_top_content;
	}
	public BaseImageView getItem5_top_iv() {
		return item5_top_iv;
	}
	public void setItem5_top_iv(BaseImageView item5_top_iv) {
		this.item5_top_iv = item5_top_iv;
	}
	public BaseImageView getItem5_top_shadow() {
		return item5_top_shadow;
	}
	public void setItem5_top_shadow(BaseImageView item5_top_shadow) {
		this.item5_top_shadow = item5_top_shadow;
	}
	public RelativeLayout getItem5_bellow() {
		return item5_bellow;
	}
	public void setItem5_bellow(RelativeLayout item5_bellow) {
		this.item5_bellow = item5_bellow;
	}
	public RelativeLayout getItem5_bellow_content() {
		return item5_bellow_content;
	}
	public void setItem5_bellow_content(RelativeLayout item5_bellow_content) {
		this.item5_bellow_content = item5_bellow_content;
	}
	public BaseImageView getItem5_bellow_iv() {
		return item5_bellow_iv;
	}
	public void setItem5_bellow_iv(BaseImageView item5_bellow_iv) {
		this.item5_bellow_iv = item5_bellow_iv;
	}
	public BaseImageView getItem5_bellow_shadow() {
		return item5_bellow_shadow;
	}
	public void setItem5_bellow_shadow(BaseImageView item5_bellow_shadow) {
		this.item5_bellow_shadow = item5_bellow_shadow;
	}
	public BaseImageView getItem5_top_handle_icon() {
		return item5_top_handle_icon;
	}
	public void setItem5_top_handle_icon(BaseImageView item5_top_handle_icon) {
		this.item5_top_handle_icon = item5_top_handle_icon;
	}
	public BaseImageView getItem5_top_control_icon() {
		return item5_top_control_icon;
	}
	public void setItem5_top_control_icon(BaseImageView item5_top_control_icon) {
		this.item5_top_control_icon = item5_top_control_icon;
	}
	public CloseAcceTextView getItem5_top_game_online() {
		return item5_top_game_online;
	}
	public void setItem5_top_game_online(CloseAcceTextView item5_top_game_online) {
		this.item5_top_game_online = item5_top_game_online;
	}
	public BaseImageView getItem5_bellow_handle_icon() {
		return item5_bellow_handle_icon;
	}
	public void setItem5_bellow_handle_icon(BaseImageView item5_bellow_handle_icon) {
		this.item5_bellow_handle_icon = item5_bellow_handle_icon;
	}
	public BaseImageView getItem5_bellow_control_icon() {
		return item5_bellow_control_icon;
	}
	public void setItem5_bellow_control_icon(BaseImageView item5_bellow_control_icon) {
		this.item5_bellow_control_icon = item5_bellow_control_icon;
	}
	public CloseAcceTextView getItem5_bellow_game_online() {
		return item5_bellow_game_online;
	}
	public void setItem5_bellow_game_online(
			CloseAcceTextView item5_bellow_game_online) {
		this.item5_bellow_game_online = item5_bellow_game_online;
	}
	public BaseImageView getItem5_top_released() {
		return item5_top_released;
	}
	public void setItem5_top_released(BaseImageView item5_top_released) {
		this.item5_top_released = item5_top_released;
	}
	public BaseImageView getItem5_bellow_released() {
		return item5_bellow_released;
	}
	public void setItem5_bellow_released(BaseImageView item5_bellow_released) {
		this.item5_bellow_released = item5_bellow_released;
	}
	
	
}