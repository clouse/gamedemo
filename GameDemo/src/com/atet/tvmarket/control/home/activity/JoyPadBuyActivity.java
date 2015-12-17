package com.atet.tvmarket.control.home.activity;

import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;

import com.atet.tvmarket.R;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.utils.ScaleViewUtils;

public class JoyPadBuyActivity extends BaseActivity{
	//private static final int QRCODESIZE = 2000;// 二维码的宽高
	private WebView web_padBuy;
	//private ImageView iv_code;
	//private Bitmap QRBitmap;
	//private String str = "https://shop115314985.taobao.com/";
	 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_joypadbug);
		ScaleViewUtils.scaleView(getWindow().getDecorView());
		web_padBuy = (WebView) findViewById(R.id.joypadbug_desc);
		//iv_code = (ImageView) findViewById(R.id.joypadbug_code);
		
		/*new Thread() {
			@Override
			public void run() {
				try {
					QRBitmap = QRUtil.createQRCode(str,(int) ScaleViewUtils.resetTextSize(QRCODESIZE));
				} catch (WriterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();*/
		web_padBuy.getSettings().setJavaScriptEnabled(true);
		web_padBuy.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);  
		//web_padBuy.getSettings().setSupportMultipleWindows(true);
		web_padBuy.loadUrl("http://buy.at-et.com");
	}
	
}
