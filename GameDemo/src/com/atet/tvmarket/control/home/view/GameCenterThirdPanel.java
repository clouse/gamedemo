package com.atet.tvmarket.control.home.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.atet.tvmarket.R;
import com.atet.tvmarket.control.home.MainActivity;
import com.atet.tvmarket.control.promotion.view.ViewPagerScroller;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UIUtils;
import com.atet.tvmarket.view.BaseImageView;
import com.atet.tvmarket.view.CloseAcceTextView;

public class GameCenterThirdPanel extends RelativeLayout {
	
	private RelativeLayout main_game;
	private RelativeLayout main_game_content;
	private NoFocusViewPager main_game_iv;
	private BaseImageView main_game_shadow;
	private BaseImageView main_game_handle_icon;
	private BaseImageView main_game_control_icon;
	private CloseAcceTextView main_game_online;
	private BaseImageView main_game_released;
	private LinearLayout point_container;
	
	private RelativeLayout small_game1;
	private RelativeLayout small_game1_content;
	private BaseImageView small_game1_iv;
	private BaseImageView small_game1_shadow;
	private BaseImageView small_game1_handle_icon;
	private BaseImageView small_game1_control_icon;
	private CloseAcceTextView small_game1_online;
	private BaseImageView small_game1_released;
	private BaseImageView small_game1_video;
	
	private RelativeLayout small_game2;
	private RelativeLayout small_game2_content;
	private BaseImageView small_game2_shadow;
	private BaseImageView small_game2_iv;
	private BaseImageView small_game2_handle_icon;
	private BaseImageView small_game2_control_icon;
	private CloseAcceTextView small_game2_online;
	private BaseImageView small_game2_released;
	private BaseImageView small_game2_video;
	
//	private OnPressUpListener mOnPressUpListener;
	
//	private BaseImageView small_game1_reflect;
//	private BaseImageView small_game2_reflect;
	
	public GameCenterThirdPanel(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}
	
	public GameCenterThirdPanel(Context context, AttributeSet attrs) {
		this(context,null,0);
		// TODO Auto-generated constructor stub
	}
	
	public GameCenterThirdPanel(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setChildrenDrawingOrderEnabled(true);
		initView();
	}

	private void initView() {
		View view = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.game_center_item3,this,true);
		ScaleViewUtils.init(UIUtils.getContext());
		ScaleViewUtils.scaleView(view);
		main_game = (RelativeLayout) view.findViewById(R.id.game_center_main_game);
		main_game_iv = (NoFocusViewPager)view.findViewById(R.id.main_game);
		main_game_content = (RelativeLayout) view.findViewById(R.id.main_game_content);
		main_game_shadow = (BaseImageView) view.findViewById(R.id.main_game_shadow);
		main_game_released = (BaseImageView) view.findViewById(R.id.main_game_released);
		point_container = (LinearLayout) view.findViewById(R.id.game_point_containter);
		
		View mainItem = view.findViewById(R.id.main_game_item);
		main_game_handle_icon = (BaseImageView) mainItem.findViewById(R.id.handle_icon);
		main_game_control_icon = (BaseImageView) mainItem.findViewById(R.id.control_icon);
		main_game_online = (CloseAcceTextView) mainItem.findViewById(R.id.game_online);
		
		small_game1 = (RelativeLayout) view.findViewById(R.id.game_center_small_game1);
		small_game1_content = (RelativeLayout)view.findViewById(R.id.small_game1_content); 
		small_game1_iv = (BaseImageView) view.findViewById(R.id.small_game1_iv);
		small_game1_shadow = (BaseImageView) view.findViewById(R.id.small_game1_shadow);
		small_game1_released  = (BaseImageView) view.findViewById(R.id.small_game1_released);
		small_game1_video = (BaseImageView) view.findViewById(R.id.small_game1_video);
		
		View game1Item = view.findViewById(R.id.small_game1_item);
		small_game1_handle_icon = (BaseImageView) game1Item.findViewById(R.id.handle_icon);
		small_game1_control_icon = (BaseImageView) game1Item.findViewById(R.id.control_icon);
		small_game1_online = (CloseAcceTextView) game1Item.findViewById(R.id.game_online);
		
		small_game2 = (RelativeLayout) view.findViewById(R.id.game_center_small_game2);
		small_game2_content = (RelativeLayout)view.findViewById(R.id.small_game2_content);
		small_game2_iv = (BaseImageView) view.findViewById(R.id.small_game2_iv);
		small_game2_shadow = (BaseImageView) view.findViewById(R.id.small_game2_shadow);
		small_game2_released = (BaseImageView) view.findViewById(R.id.small_game2_released);
		small_game2_video = (BaseImageView) view.findViewById(R.id.small_game2_video);
		
		View game2Item = view.findViewById(R.id.small_game2_item);
		small_game2_handle_icon = (BaseImageView) game2Item.findViewById(R.id.handle_icon);
		small_game2_control_icon = (BaseImageView) game2Item.findViewById(R.id.control_icon);
		small_game2_online = (CloseAcceTextView) game2Item.findViewById(R.id.game_online);
		
	}
	/**
	 * 处理小游戏向下点击无效
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();
		View focusView = ((RelativeLayout) getFocusedChild())
				.getFocusedChild();
		if (focusView != null) {
			if (focusView.getId() == small_game1.getId() || 
					focusView.getId() == small_game2.getId()) {
				if (action == KeyEvent.ACTION_DOWN
						&& keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
					
					return true;
				}else if(action == KeyEvent.ACTION_DOWN
						&& keyCode == KeyEvent.KEYCODE_DPAD_UP){
					main_game.requestFocus();
					return true;
				}
			}/*else if(focusView.getId() == main_game.getId()){
				if(action == KeyEvent.ACTION_DOWN
						&& keyCode == KeyEvent.KEYCODE_DPAD_UP){
					mOnPressUpListener.onItemUp();
					MainActivity.previousFocusView = main_game;
					return true;
				}
			}*/
		}
		return super.dispatchKeyEvent(event);
	}
	
	/*public interface OnPressUpListener{
		void onItemUp();
	}

	public void setOnPressUpListener(OnPressUpListener mOnPressUpListener){
		this.mOnPressUpListener = mOnPressUpListener;
	}*/
	
	/**
	 * 处理view的排列顺序
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
	
	
	public BaseImageView getSmall_game1_video() {
		return small_game1_video;
	}

	public void setSmall_game1_video(BaseImageView small_game1_video) {
		this.small_game1_video = small_game1_video;
	}

	public BaseImageView getSmall_game2_video() {
		return small_game2_video;
	}

	public void setSmall_game2_video(BaseImageView small_game2_video) {
		this.small_game2_video = small_game2_video;
	}

	public LinearLayout getPoint_container() {
		return point_container;
	}

	public void setPoint_container(LinearLayout point_container) {
		this.point_container = point_container;
	}

	public RelativeLayout getMain_game_content() {
		return main_game_content;
	}

	public void setMain_game_content(RelativeLayout main_game_content) {
		this.main_game_content = main_game_content;
	}

	
	public RelativeLayout getMain_game() {
		return main_game;
	}

	public void setMain_game(RelativeLayout main_game) {
		this.main_game = main_game;
	}

	public NoFocusViewPager getMain_game_iv() {
		return main_game_iv;
	}

	public void setMain_game_iv(NoFocusViewPager main_game_iv) {
		this.main_game_iv = main_game_iv;
	}

	public RelativeLayout getSmall_game1_content() {
		return small_game1_content;
	}

	public void setSmall_game1_content(RelativeLayout small_game1_content) {
		this.small_game1_content = small_game1_content;
	}

	public RelativeLayout getSmall_game2_content() {
		return small_game2_content;
	}

	public void setSmall_game2_content(RelativeLayout small_game2_content) {
		this.small_game2_content = small_game2_content;
	}

	public BaseImageView getSmall_game1_iv() {
		return small_game1_iv;
	}

	public void setSmall_game1_iv(BaseImageView small_game1_iv) {
		this.small_game1_iv = small_game1_iv;
	}

	public BaseImageView getSmall_game2_iv() {
		return small_game2_iv;
	}

	public void setSmall_game2_iv(BaseImageView small_game2_iv) {
		this.small_game2_iv = small_game2_iv;
	}

	public BaseImageView getMain_game_shadow() {
		return main_game_shadow;
	}

	public void setMain_game_shadow(BaseImageView main_game_shadow) {
		this.main_game_shadow = main_game_shadow;
	}

	public BaseImageView getSmall_game1_shadow() {
		return small_game1_shadow;
	}

	public void setSmall_game1_shadow(BaseImageView small_game1_shadow) {
		this.small_game1_shadow = small_game1_shadow;
	}

	public BaseImageView getSmall_game2_shadow() {
		return small_game2_shadow;
	}

	public void setSmall_game2_shadow(BaseImageView small_game2_shadow) {
		this.small_game2_shadow = small_game2_shadow;
	}

	public RelativeLayout getSmall_game1() {
		return small_game1;
	}

	public void setSmall_game1(RelativeLayout small_game1) {
		this.small_game1 = small_game1;
	}

	public RelativeLayout getSmall_game2() {
		return small_game2;
	}

	public void setSmall_game2(RelativeLayout small_game2) {
		this.small_game2 = small_game2;
	}

	public BaseImageView getMain_game_handle_icon() {
		return main_game_handle_icon;
	}

	public void setMain_game_handle_icon(BaseImageView main_game_handle_icon) {
		this.main_game_handle_icon = main_game_handle_icon;
	}

	public BaseImageView getMain_game_control_icon() {
		return main_game_control_icon;
	}

	public void setMain_game_control_icon(BaseImageView main_game_control_icon) {
		this.main_game_control_icon = main_game_control_icon;
	}

	public BaseImageView getSmall_game1_handle_icon() {
		return small_game1_handle_icon;
	}

	public void setSmall_game1_handle_icon(BaseImageView small_game1_handle_icon) {
		this.small_game1_handle_icon = small_game1_handle_icon;
	}

	public BaseImageView getSmall_game1_control_icon() {
		return small_game1_control_icon;
	}

	public void setSmall_game1_control_icon(BaseImageView small_game1_control_icon) {
		this.small_game1_control_icon = small_game1_control_icon;
	}

	public BaseImageView getSmall_game2_handle_icon() {
		return small_game2_handle_icon;
	}

	public void setSmall_game2_handle_icon(BaseImageView small_game2_handle_icon) {
		this.small_game2_handle_icon = small_game2_handle_icon;
	}

	public BaseImageView getSmall_game2_control_icon() {
		return small_game2_control_icon;
	}

	public void setSmall_game2_control_icon(BaseImageView small_game2_control_icon) {
		this.small_game2_control_icon = small_game2_control_icon;
	}

	public CloseAcceTextView getMain_game_online() {
		return main_game_online;
	}

	public void setMain_game_online(CloseAcceTextView main_game_online) {
		this.main_game_online = main_game_online;
	}

	public CloseAcceTextView getSmall_game1_online() {
		return small_game1_online;
	}

	public void setSmall_game1_online(CloseAcceTextView small_game1_online) {
		this.small_game1_online = small_game1_online;
	}

	public CloseAcceTextView getSmall_game2_online() {
		return small_game2_online;
	}

	public void setSmall_game2_online(CloseAcceTextView small_game2_online) {
		this.small_game2_online = small_game2_online;
	}

	public BaseImageView getMain_game_released() {
		return main_game_released;
	}

	public void setMain_game_released(BaseImageView main_game_released) {
		this.main_game_released = main_game_released;
	}

	public BaseImageView getSmall_game1_released() {
		return small_game1_released;
	}

	public void setSmall_game1_released(BaseImageView small_game1_released) {
		this.small_game1_released = small_game1_released;
	}

	public BaseImageView getSmall_game2_released() {
		return small_game2_released;
	}

	public void setSmall_game2_released(BaseImageView small_game2_released) {
		this.small_game2_released = small_game2_released;
	}
	
	
	
}
