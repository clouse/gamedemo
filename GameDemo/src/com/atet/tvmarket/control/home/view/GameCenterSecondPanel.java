package com.atet.tvmarket.control.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.VideoView;

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
public class GameCenterSecondPanel extends RelativeLayout {

	private RelativeLayout main_push_content;
	private RelativeLayout main_push;
//	private BaseImageView main_push_reflect;
	private BaseImageView main_push_shadow;
	private BaseImageView main_push_iv;
//	private OnPressUpListener mOnPressUpListener;
	private BaseImageView handle_icon;
	private BaseImageView control_icon;
	
	private BaseImageView main_push_vedio;
	private BaseImageView main_push_released;
	private CloseAcceTextView game_online;

	public GameCenterSecondPanel(Context context) {
		this(context, null);

	}

	public GameCenterSecondPanel(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public GameCenterSecondPanel(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setChildrenDrawingOrderEnabled(true);
		View view = LayoutInflater.from(context).inflate(
				R.layout.game_center_item2, this, true);
		ScaleViewUtils.init(UIUtils.getContext());
		ScaleViewUtils.scaleView(view);
		initView();
	}

	private void initView() {
		main_push_content = (RelativeLayout) findViewById(R.id.main_push_content);
		main_push = (RelativeLayout) findViewById(R.id.main_push);
//		main_push_reflect = (BaseImageView) findViewById(R.id.main_push_reflect);
		main_push_shadow = (BaseImageView) findViewById(R.id.main_push_shadow);
		main_push_iv = (BaseImageView) findViewById(R.id.main_push_iv);
		main_push_vedio = (BaseImageView) findViewById(R.id.main_push_vedio);
		main_push_released = (BaseImageView) findViewById(R.id.main_push_released);
		
		handle_icon = (BaseImageView) findViewById(R.id.handle_icon);
		control_icon = (BaseImageView) findViewById(R.id.control_icon);
		game_online = (CloseAcceTextView) findViewById(R.id.game_online);
	}

	/*@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();
		View focusView = ((RelativeLayout) ((RelativeLayout) getFocusedChild())
				.getFocusedChild()).getFocusedChild();
		if (focusView != null) {
			if (focusView.getId() == main_push_content.getId()) {
				if (action == KeyEvent.ACTION_DOWN
						&& keyCode == KeyEvent.KEYCODE_DPAD_UP) {
					mOnPressUpListener.onItemUp();
					return true;
				}
			}
		}
		return super.dispatchKeyEvent(event);
	}*/
	
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

	public RelativeLayout getMain_push_content() {
		return main_push_content;
	}

	public void setMain_push_content(RelativeLayout main_push_content) {
		this.main_push_content = main_push_content;
	}

	public BaseImageView getMain_push_shadow() {
		return main_push_shadow;
	}

	public void setMain_push_shadow(BaseImageView main_push_shadow) {
		this.main_push_shadow = main_push_shadow;
	}

	public BaseImageView getMain_push_iv() {
		return main_push_iv;
	}

	public void setMain_push_iv(BaseImageView main_push_iv) {
		this.main_push_iv = main_push_iv;
	}

	public RelativeLayout getMain_push() {
		return main_push;
	}

	public void setMain_push(RelativeLayout main_push) {
		this.main_push = main_push;
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

	public BaseImageView getMain_push_vedio() {
		return main_push_vedio;
	}

	public void setMain_push_vedio(BaseImageView main_push_vedio) {
		this.main_push_vedio = main_push_vedio;
	}
	public BaseImageView getMain_push_released() {
		return main_push_released;
	}

	public void setMain_push_released(BaseImageView main_push_released) {
		this.main_push_released = main_push_released;
	}

	
	
}