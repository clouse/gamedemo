package com.atet.tvmarket.control.mygame.view;

/**
 * 游戏详情第一个Item
 */
import android.view.ViewGroup;
import com.atet.tvmarket.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameDetailFirstItemView extends LinearLayout{
	private TextView mBtnUpload;  // 下载按钮
	private ImageView logoView;  // 游戏图片View
	private ImageView  logoViewFocus;  // 聚焦的ImageView
	private GameDetailLoading logoViewContainer; // 进度条控件
	private TextView gameName;  //游戏名
	private ImageView downIv;  // 下载图标

	

	public GameDetailFirstItemView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public GameDetailFirstItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public GameDetailFirstItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	/**
	 * 
	 * @description 初始化界面
	 * @param context
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:02:32
	 *
	 */
	private void init(Context context){
		View view = LayoutInflater.from(context).inflate(R.layout.game_detail_logo_item, this, true);
		logoViewContainer = (GameDetailLoading) view.findViewById(R.id.game_detail_logo_container);
		logoViewFocus = (ImageView) view.findViewById(R.id.game_detail_logo_layout);
        logoView = (ImageView) view.findViewById(R.id.game_detail_logo_iv);
        mBtnUpload = (TextView) view.findViewById(R.id.btn_start);
        gameName = (TextView)view.findViewById(R.id.game_detail_title);
        downIv = (ImageView)view.findViewById(R.id.game_detail_down_iv);
        logoViewFocus.setOnClickListener(onClickListener);

		setFocus();  // 设置焦点
	}

	private void setFocus(){
		logoViewFocus.setNextFocusDownId(R.id.game_detail_logo_layout);
	}
	public TextView getmBtnUpload() {
		return mBtnUpload;
	}

	public void setmBtnUpload(TextView mBtnUpload) {
		this.mBtnUpload = mBtnUpload;
	}

	public ImageView getLogoView() {
		return logoView;
	}

	public void setLogoView(ImageView logoView) {
		this.logoView = logoView;
	}


	public ImageView getLogoViewFocus() {
		return logoViewFocus;
	}

	
	public GameDetailLoading getLogoViewContainer() {
		return logoViewContainer;
	}


	public TextView getGameName() {
		return gameName;
	}

	public void setGameName(TextView gameName) {
		this.gameName = gameName;
	}


	public ImageView getDownIv() {
		return downIv;
	}

	public void setDownIv(ImageView downIv) {
		this.downIv = downIv;
	}


	
	/**
	 * 下载图标实现点击效果
	 */
	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mBtnUpload.performClick();
			downIv.setPressed(true);
			postDelayed(new Runnable() {
				public void run() {
					downIv.setPressed(false);
				}
			}, 100);
		}
	};
	
	
	
}
