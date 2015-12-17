package com.atet.tvmarket.control.mygame.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.UrlConstant;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.control.mygame.utils.QRUtil;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.model.net.http.download.BtnDownCommonListener;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.ScreenShot;
import com.atet.tvmarket.utils.StringTool;
import com.atet.tvmarket.view.CloseAcceTextView;
import com.atet.tvmarket.view.blur.BlurringView;
import com.google.zxing.WriterException;

/**
 * 更多精彩中的游戏显示界面
 * @author chenqingwen
 *
 */
public class RelativeGameActivity extends BaseActivity {
	private ImageView imageIcon;
	private TextView gameDetailName;
	private RatingBar gameScore;
	private TextView gameDownCount;
	private TextView gameSize;
	private TextView gameVersion;
	private TextView gameControl;
	private ImageView iv_qrcode;
	private Button downBtn;
	private BtnDownCommonListener btnDownCommonListener;
	private static final int NOTICE = 16 ,//预告游戏
			IMITATE = 1, //模拟模式
     		GAMEPAD = 2, //gamepad模式
			KEYBOARD = 4, //键盘
			CONTROL = 8,  //遥控
	        IMITATE_GAMEPAD = 3,//模拟模式+gamepad模式
	        IMITATE_KEYBOARD = 5,//模拟模式+键盘
	        IMITATE_CONTROL = 9,//模拟模式+遥控；
	        GAMEPAD_KEYBOARD = 6,//gamepad模式+键盘
	        GAMEPAD_CONTROL = 10,//gamepad+遥控
	        KEYBOARD_CONTROL = 12,//键盘+遥控
	        IMITATE_GAMEPAD_KEYBOARD = 7,//模拟模式+gamepad+键盘
	        IMITATE_GAMEPAD_CONTROL = 11,//模拟模式+gamepad+遥控
	        GAMEPAD_KEYBOARD_CONTROL = 14,//gamepad+键盘+遥控
	        IMITATE_KEYBOARD_CONTROL = 13,//模拟模式+键盘+遥控;
	        IMITATE_GAMEPAD_KEYBOARD_CONTROL = 15;
	private Handler handler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM); 
		setContentView(R.layout.relative_game_layout);
		ScaleViewUtils.init(this);
        ScaleViewUtils.scaleView(getWindow().getDecorView());
        imageIcon = (ImageView)findViewById(R.id.relative_game_iv);
		gameDetailName = (CloseAcceTextView)findViewById(R.id.relative_game_detail_name);
		gameScore = (RatingBar)findViewById(R.id.relative_game_detail_ratingbar);
		gameDownCount = (CloseAcceTextView)findViewById(R.id.relative_game_detail_downcounts);
		gameSize = (CloseAcceTextView)findViewById(R.id.relative_game_detail_size);
		gameVersion = (CloseAcceTextView)findViewById(R.id.relative_game_detail_version);
		gameControl = (CloseAcceTextView)findViewById(R.id.relative_game_detail_control);
		iv_qrcode = (ImageView)findViewById(R.id.relative_qr_code_iv);
		downBtn = (Button)findViewById(R.id.relative_game_btn);	
		init();
	}
	
	/**
	 * 
	 * @description 初始化数据
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:44:42
	 *
	 */
	private void init(){
		gameScore.setFocusable(false);
		gameScore.setFocusableInTouchMode(false);
		Intent intent = getIntent();
		final GameInfo info = (GameInfo) intent.getSerializableExtra("gameInfo");
		ImageFetcher mImageFetcher = getmImageFetcher();
		mImageFetcher.loadLocalImage(0, imageIcon, R.drawable.gameranking_item_icon);
		mImageFetcher.loadImage(info.getMinPhoto(), imageIcon, 0);
		gameDetailName.setText(info.getGameName());
		gameDownCount.setText("下载次数：" +info.getGameDownCount());
		gameSize.setText("大小："
				+ StringTool.StringToFloat(info.getGameSize()+"")+ "MB");
		gameVersion.setText("版本：" + info.getVersionName());
		Double rating = info.getStartLevel();
		if(rating == null){
			gameScore.setRating(0);
		}else{
			gameScore.setRating((float)(double)rating);
		}
	    btnDownCommonListener = new BtnDownCommonListener(this);
		btnDownCommonListener.listen(downBtn, info);
		loadQrCode(info);
		DecideAdapter(info.getHandleType(), gameControl);
	}
	
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	btnDownCommonListener.recycle();
    }
    
	/**
	 * 
	 * @description 加载二维码图片下载地址
	 * @param gameInfo
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:45:03
	 *
	 */
	private void loadQrCode(final GameInfo gameInfo){
		final String downAdress = gameInfo.getFile();
		if(null!=downAdress){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					final Bitmap bitmap;
					try {
						bitmap = QRUtil.createQRCode(
								UrlConstant.HTTP_GAME_DETAIL_QRCODE+gameInfo.getGameId(), 100);
						handler.post(new Runnable() {
							
							@Override
							public void run() {
								iv_qrcode.setImageBitmap(bitmap);
							}
						});
					} catch (WriterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == RESULT_OK && btnDownCommonListener != null) {
			// 下载游戏
			btnDownCommonListener.startDownloadGame();
		}
	}
 
	/**
	 * 
	 * @description  判断适配的类型
	 * @param handType
	 * @param mTvAdapter 显示的textView
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:45:15
	 *
	 */
	private void DecideAdapter(Integer handType,TextView mTvAdapter) {
		switch (handType) {
		case IMITATE:
			mTvAdapter.setText(getResources().getString(R.string.land_detail_adapter)
					+ getResources().getString(R.string.land_detail_hand));
			break;

		case GAMEPAD:
			mTvAdapter.setText(getResources().getString(R.string.land_detail_adapter)
					+ getResources().getString(R.string.land_detail_hand));
			break;

		case KEYBOARD:
			mTvAdapter.setText(getResources().getString(R.string.land_detail_adapter)
					+ getResources().getString(R.string.land_detail_keyboard));
			break;

		case CONTROL:
			mTvAdapter.setText(getResources().getString(R.string.land_detail_adapter)
					+ getResources().getString(R.string.land_detail_remote_controlled));
			break;
			
		case IMITATE_GAMEPAD:
			mTvAdapter.setText(getResources().getString(R.string.land_detail_adapter)
					+ getResources().getString(R.string.land_detail_hand));
			break;
			
		case IMITATE_KEYBOARD:
			mTvAdapter.setText(getResources().getString(R.string.land_detail_adapter)
					+ getResources().getString(R.string.land_detail_hand)+";"+getResources().getString(R.string.land_detail_keyboard));
			break;
			
		case IMITATE_CONTROL:
			mTvAdapter.setText(getResources().getString(R.string.land_detail_adapter)
					+ getResources().getString(R.string.land_detail_hand)+";"+getResources().getString(R.string.land_detail_remote_controlled));
			break;
			
		case GAMEPAD_KEYBOARD:
			mTvAdapter.setText(getResources().getString(R.string.land_detail_adapter)
					+ getResources().getString(R.string.land_detail_hand)+";"+getResources().getString(R.string.land_detail_keyboard));
			break;
			
		case GAMEPAD_CONTROL:
			mTvAdapter.setText(getResources().getString(R.string.land_detail_adapter)
					+ getResources().getString(R.string.land_detail_hand)+";"+getResources().getString(R.string.land_detail_remote_controlled));
			break;
			
		case KEYBOARD_CONTROL:
			mTvAdapter.setText(getResources().getString(R.string.land_detail_adapter)
					+ getResources().getString(R.string.land_detail_keyboard)+";"+getResources().getString(R.string.land_detail_remote_controlled));
			break;
			
		case IMITATE_GAMEPAD_KEYBOARD:
			mTvAdapter.setText(getResources().getString(R.string.land_detail_adapter)
					+ getResources().getString(R.string.land_detail_hand)+";"+getResources().getString(R.string.land_detail_keyboard));
			break;
			
		case IMITATE_GAMEPAD_CONTROL:
			mTvAdapter.setText(getResources().getString(R.string.land_detail_adapter)
					+ getResources().getString(R.string.land_detail_hand)+";"+getResources().getString(R.string.land_detail_remote_controlled));
			break;
			
		case GAMEPAD_KEYBOARD_CONTROL:
			mTvAdapter.setText(getResources().getString(R.string.land_detail_adapter)
					+ getResources().getString(R.string.land_detail_hand)+";"+getResources().getString(R.string.land_detail_remote_controlled)+";"+getResources().getString(R.string.land_detail_keyboard));
			break;
			
		case IMITATE_KEYBOARD_CONTROL:
			mTvAdapter.setText(getResources().getString(R.string.land_detail_adapter)
					+ getResources().getString(R.string.land_detail_hand)+";"+getResources().getString(R.string.land_detail_remote_controlled)+";"+getResources().getString(R.string.land_detail_keyboard));
			break;
			
		case IMITATE_GAMEPAD_KEYBOARD_CONTROL:
			mTvAdapter.setText(getResources().getString(R.string.land_detail_adapter)
					+ getResources().getString(R.string.land_detail_hand)+";"+getResources().getString(R.string.land_detail_remote_controlled)+";"+getResources().getString(R.string.land_detail_keyboard));
			break;
		}
	}
}
