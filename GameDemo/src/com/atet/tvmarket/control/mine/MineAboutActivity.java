package com.atet.tvmarket.control.mine;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.Toast;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.control.mygame.utils.QRUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.google.zxing.WriterException;

public class MineAboutActivity extends BaseActivity {
	private ALog alog = ALog.getLogger(MineAboutActivity.class);
	private Toast toast = null;
	private ImageView ivQrcode;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_mine_about);
		ScaleViewUtils.scaleView(this);
		setBlackTitle(false);
		initView();
	}

	private void initView() {
		// 显示二维码
		ivQrcode = (ImageView) findViewById(R.id.mine_about_qrcode_iv);
		new Thread(new Runnable() {

			@Override
			public void run() {
				final Bitmap bitmap;
				try {
					bitmap = QRUtil.createQRCode(Constant.ATET_OFFICE_ADDRESS,
							300);
					handler.post(new Runnable() {

						@Override
						public void run() {
							ivQrcode.setImageBitmap(bitmap);
						}
					});
				} catch (WriterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setTitleData();
	}
}
