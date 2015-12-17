package com.atet.tvmarket.view;

import java.util.Timer;
import java.util.TimerTask;

import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UIUtils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoadingView extends RelativeLayout {

	private RelativeLayout contentLoading;
	private LinearLayout contentIsLoading, contentIsException;
	private ImageView emptyProgress;
	private TextView emptyText;
	private AnimationDrawable animationDrawable;
	private int progress = 0;
	private Timer timer;
	private View dataView;

	public LoadingView(Context context) {
		this(context, null);
	}

	public LoadingView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		LayoutInflater.from(context).inflate(R.layout.layout_loading, this,
				true);
		contentLoading = (RelativeLayout) findViewById(R.id.contentLoading);
		contentIsLoading = (LinearLayout) findViewById(R.id.isloading_layout);
		contentIsException = (LinearLayout) findViewById(R.id.net_exception_layout);

		emptyProgress = (ImageView) findViewById(R.id.emptyProgress);
		emptyText = (TextView) findViewById(R.id.emptyText);
	}

	private void start() {
		animationDrawable = (AnimationDrawable) emptyProgress.getDrawable();
		animationDrawable.start();

		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				if (progress < 100) {
					mHandler.sendEmptyMessage(9999);
				}
			}
		}, 0, 50);
	}

	private void stop() {
		if(animationDrawable!=null){
			animationDrawable.stop();
		}
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.SHOWLOADING:
				dataView.setVisibility(View.INVISIBLE);
				contentLoading.setVisibility(View.VISIBLE);
				contentIsLoading.setVisibility(View.VISIBLE);
				contentIsException.setVisibility(View.INVISIBLE);
				progress = 0;
				start();
				break;
			case Constant.DISMISSLOADING:
				stop();
				emptyText.setText(100 + "%  加载中...");
				dataView.setVisibility(View.VISIBLE);
				contentLoading.setVisibility(View.INVISIBLE);
				contentIsLoading.setVisibility(View.VISIBLE);
				contentIsException.setVisibility(View.INVISIBLE);
				break;
			case Constant.NULLDATA:
				stop();
				progress = 0;
				dataView.setVisibility(View.INVISIBLE);
				contentLoading.setVisibility(View.VISIBLE);
				contentIsLoading.setVisibility(View.INVISIBLE);
				contentIsException.setVisibility(View.VISIBLE);
				contentIsException.findViewById(R.id.net_exception_tv)
						.setVisibility(View.GONE);
				contentIsException.findViewById(R.id.null_data_tv)
						.setVisibility(View.VISIBLE);
				break;
			case Constant.EXCEPTION:
				stop();
				progress = 0;
				dataView.setVisibility(View.INVISIBLE);
				contentLoading.setVisibility(View.VISIBLE);
				contentIsLoading.setVisibility(View.INVISIBLE);
				contentIsException.setVisibility(View.VISIBLE);
				contentIsException.findViewById(R.id.net_exception_tv)
						.setVisibility(View.VISIBLE);
				contentIsException.findViewById(R.id.null_data_tv)
						.setVisibility(View.GONE);
				break;
			case 9999:
				emptyText.setText(progress + "%  加载中...");
				if(progress<100)
				{
					progress++;
				}
				break;
				
			case Constant.PULLGIFT:
				emptyText.setText(R.string.gift_is_Receiveding);
				emptyText.setTextSize(UIUtils.getDimens(R.dimen.gift_is_received_size));
				//dataView.setVisibility(View.INVISIBLE);
				contentIsLoading.setVisibility(View.VISIBLE);
				contentIsException.setVisibility(View.INVISIBLE);
				animationDrawable = (AnimationDrawable) emptyProgress.getDrawable();
				animationDrawable.start();
				break;
				
			case Constant.WEBVIEWLOADING:
				emptyText.setTextColor(Color.RED);
				//dataView.setVisibility(View.INVISIBLE);
				contentIsLoading.setVisibility(View.VISIBLE);
				contentIsException.setVisibility(View.INVISIBLE);
				animationDrawable = (AnimationDrawable) emptyProgress.getDrawable();
				animationDrawable.start();
				break;
			default:
				break;
			}
		}

	};

	public void setDataView(View dataView) {
		this.dataView = dataView;
	}

	public Handler getmHandler() {
		return mHandler;
	}
}
