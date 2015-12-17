package com.atet.tvmarket.control.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
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
public class GameCenterfourthPanel extends RelativeLayout {

	private RelativeLayout more_game_content;
//	private BaseImageView more_game_reflect;
	private BaseImageView more_game_shadow;
	private BaseImageView more_game_iv;
	private RelativeLayout more_game;
	private BaseImageView handle_icon;
	private BaseImageView control_icon;
	private CloseAcceTextView game_online;
	private BaseImageView more_game_released;
	private BaseImageView more_game_video;
//	private OnPressUpListener mOnPressUpListener;

	public GameCenterfourthPanel(Context context) {
		this(context, null);

	}

	public GameCenterfourthPanel(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public GameCenterfourthPanel(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		View view = LayoutInflater.from(context).inflate(R.layout.game_center_item4, this, true);
		ScaleViewUtils.init(UIUtils.getContext());
		ScaleViewUtils.scaleView(view);
		initView();
	}

	private void initView() {
		more_game = (RelativeLayout) findViewById(R.id.more_game);
		more_game_content = (RelativeLayout) findViewById(R.id.more_game_content);
//		more_game_reflect = (BaseImageView) findViewById(R.id.more_game_reflect);
		more_game_shadow = (BaseImageView) findViewById(R.id.more_game_shadow);
		more_game_iv = (BaseImageView) findViewById(R.id.more_game_iv);
		handle_icon = (BaseImageView) findViewById(R.id.handle_icon);
		control_icon = (BaseImageView) findViewById(R.id.control_icon);
		game_online = (CloseAcceTextView) findViewById(R.id.game_online);
		more_game_released = (BaseImageView) findViewById(R.id.more_game_released);
		more_game_video = (BaseImageView) findViewById(R.id.more_game_vedio);
	}

	/*@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();
		
		View focusView = ((RelativeLayout) ((RelativeLayout) getFocusedChild())
				.getFocusedChild()).getFocusedChild();
		if (focusView != null) {
			if (focusView.getId() == more_game_content.getId()) {
				if (action == KeyEvent.ACTION_DOWN
						&& keyCode == KeyEvent.KEYCODE_DPAD_UP) {
					mOnPressUpListener.onItemUp();
					return true;
				}
			}
		}
		return super.dispatchKeyEvent(event);
	}*/
	
	public BaseImageView getMore_game_video() {
		return more_game_video;
	}

	public void setMore_game_video(BaseImageView more_game_video) {
		this.more_game_video = more_game_video;
	}

	/*public interface OnPressUpListener{
		void onItemUp();
	}
	
	public void setOnPressUpListener(OnPressUpListener mOnPressUpListener){
		this.mOnPressUpListener = mOnPressUpListener;
	}*/
	/**
	 * 处理视图的叠加效果
	 */
	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		if (hasFocus()) {
			View child = getFocusedChild();
			if (child != null) {

				int index = indexOfChild(child);
				if (index != -1) {

					if (i < index) {
						return i;
					} else {
						return childCount - 1 - i + index;
					}
				}
			} else {
				return i;
			}
		}
		return super.getChildDrawingOrder(childCount, i);
	}

	public RelativeLayout getMore_game_content() {
		return more_game_content;
	}

	public void setMore_game_content(RelativeLayout more_game_content) {
		this.more_game_content = more_game_content;
	}

	public BaseImageView getMore_game_shadow() {
		return more_game_shadow;
	}

	public void setMore_game_shadow(BaseImageView more_game_shadow) {
		this.more_game_shadow = more_game_shadow;
	}

	public BaseImageView getMore_game_iv() {
		return more_game_iv;
	}

	public void setMore_game_iv(BaseImageView more_game_iv) {
		this.more_game_iv = more_game_iv;
	}

	public RelativeLayout getMore_game() {
		return more_game;
	}

	public void setMore_game(RelativeLayout more_game) {
		this.more_game = more_game;
	}

	public BaseImageView getHandle_icon() {
		return handle_icon;
	}

	public void setHandle_icon(BaseImageView handle_icon) {
		this.handle_icon = handle_icon;
	}

	public BaseImageView getControl_icon() {
		return control_icon;
	}

	public void setControl_icon(BaseImageView control_icon) {
		this.control_icon = control_icon;
	}

	public CloseAcceTextView getGame_online() {
		return game_online;
	}

	public void setGame_online(CloseAcceTextView game_online) {
		this.game_online = game_online;
	}

	public BaseImageView getMore_game_released() {
		return more_game_released;
	}

	public void setMore_game_released(BaseImageView more_game_released) {
		this.more_game_released = more_game_released;
	}
	
}