package com.atet.tvmarket.control.promotion.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout.LayoutParams;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.control.home.decoration.PromotionAreaInsetDecoration;
import com.atet.tvmarket.control.home.inf.OnRecyItemClickListener;
import com.atet.tvmarket.control.promotion.holder.PromotionAreaHolder;
import com.atet.tvmarket.entity.dao.ActDetailPhoto;
import com.atet.tvmarket.entity.dao.ActInfo;
import com.atet.tvmarket.model.DataConfig;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.GamepadTool;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UIUtils;
import com.atet.tvmarket.utils.UmengUtils;
import com.atet.tvmarket.view.LoadingView;

public class PromotionAreaActivity extends BaseActivity implements
		OnRecyItemClickListener {
	ALog alog = ALog.getLogger(PromotionAreaActivity.class);
	private RecyclerView recyclerView;
	private LinearLayoutManager manager;
	private PromotionAreaAdapter mAdapter;
	private PromotionAreaInsetDecoration insertDecoration;
	private List<ActInfo> actInfos = new ArrayList<ActInfo>();
	private PromotionAreaHolder holder;

	private LoadingView loadingView;

	private Handler mHander = new Handler() {
		public void handleMessage(Message msg) {
			// 遍历取出图片的数据
			for (int i = 0; i < actInfos.size(); i++) {
				ActInfo actInfo = actInfos.get(i);
				List<ActDetailPhoto> detailPhotos = actInfo.getDetailPhotos();
				for (int j = 0; j < detailPhotos.size(); j++) {
				//	alog.info("---- " + detailPhotos.get(j).toString());
				}
			}
			mAdapter.setData();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		setBlackTitle(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setTitleData();
	}

	private void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_promotion_area);
		ScaleViewUtils.scaleView(getWindow().getDecorView());
		recyclerView = (RecyclerView) findViewById(R.id.promotion_area_recycler_view);

		loadingView = (LoadingView) findViewById(R.id.promotion_area_Loading);
		loadingView.setDataView(recyclerView);
		manager = new LinearLayoutManager(getApplicationContext());
		mAdapter = new PromotionAreaAdapter();
		insertDecoration = new PromotionAreaInsetDecoration(this);

		mAdapter.setOnRecyClickListener(this);
		manager.setOrientation(LinearLayoutManager.HORIZONTAL);
		recyclerView.addItemDecoration(insertDecoration);
		recyclerView.setLayoutManager(manager);
		recyclerView.setAdapter(mAdapter);
		loadData(false);
	}

	private void loadData(boolean isRefesh) {
		if(actInfos != null){
			actInfos.clear();
		}
		loadingView.getmHandler().sendEmptyMessage(Constant.SHOWLOADING);

		ReqCallback<List<ActInfo>> reqCallback = new ReqCallback<List<ActInfo>>() {
			@Override
			public void onResult(TaskResult<List<ActInfo>> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					actInfos = taskResult.getData();
					if (actInfos != null && !actInfos.isEmpty()) {
						alog.info(actInfos.toString());
						alog.info("size = " + actInfos.size());
						mHander.sendEmptyMessage(0);

						loadingView.getmHandler().sendEmptyMessage(
								Constant.DISMISSLOADING);
					} else {
						if (NetUtil.isNetworkAvailable(
								PromotionAreaActivity.this, true)) {
							loadingView.getmHandler().sendEmptyMessage(
									Constant.NULLDATA);
						} else {
							loadingView.getmHandler().sendEmptyMessage(
									Constant.EXCEPTION);
						}
					}
				} else {
					alog.info("failed");
					if (NetUtil.isNetworkAvailable(PromotionAreaActivity.this,
							true)) {
						loadingView.getmHandler().sendEmptyMessage(
								Constant.NULLDATA);
					} else {
						loadingView.getmHandler().sendEmptyMessage(
								Constant.EXCEPTION);
					}
				}

			}

			@Override
			public void onUpdate(TaskResult<List<ActInfo>> taskResult) {

			}
		};
		if(isRefesh){
			
			DataFetcher.getAct(getApplicationContext(), DataConfig.ACTIVITY_TYPE_ALL,reqCallback, false)
			.registerUpdateListener(reqCallback)
			.refresh(getApplicationContext());
		}else{
			DataFetcher.getAct(getApplicationContext(), DataConfig.ACTIVITY_TYPE_ALL,reqCallback, false)
			.registerUpdateListener(reqCallback)
			.request(getApplicationContext());
			
		}
	}

	class PromotionAreaAdapter extends Adapter<ViewHolder> {
		private OnRecyItemClickListener mListener;

		@Override
		public int getItemCount() {
			// TODO Auto-generated method stub
			if (actInfos.size() != 0) {
				return actInfos.size();
			}
			return 0;
		}

		public void setData() {
			recyclerView.setAdapter(this);
		}

		@Override
		public void onBindViewHolder(ViewHolder viewholder, int position) {
			holder = (PromotionAreaHolder) viewholder;
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);

			if (position == 0) {
				holder.item_content.requestFocus();
				params.leftMargin = (int) ScaleViewUtils.resetTextSize(80);
			}

			if (position == getItemCount() - 1) {
				params.rightMargin = (int) ScaleViewUtils.resetTextSize(150);
			}

			holder.itemView.setLayoutParams(params);

			if (actInfos != null && actInfos.size() != 0) {
				holder.setData(actInfos.get(position));
			}
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
			LayoutInflater inflater = LayoutInflater.from(UIUtils.getContext());
			View root = inflater.inflate(R.layout.promotion_area_item,
					viewGroup, false);
			return new PromotionAreaHolder(root, recyclerView, mListener);
		}

		public void setOnRecyClickListener(OnRecyItemClickListener listener) {
			this.mListener = listener;
		}
	}

	@Override
	public void onItemClick(View view, int position) {
		view.requestFocus();
		// Umeng统计 点击详情次数
		UmengUtils.setOnEvent(PromotionAreaActivity.this,
				UmengUtils.PROMOTION_AREA_DETAIL_CLICK);
		Intent intent = new Intent(this, PromotionDetailsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Constant.ACTINFO, actInfos.get(position));
		intent.putExtras(bundle);
		startActivity(intent);
	}

	class RecyclerViewFocus implements OnFocusChangeListener {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {

				View view = recyclerView.getChildAt(0).findViewById(
						R.id.promotion_area_item_content);
				view.requestFocus();
			}
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();

		// X键刷新
		if (action == KeyEvent.ACTION_DOWN) {
			if (GamepadTool.isButtonX(keyCode)
					|| keyCode == KeyEvent.KEYCODE_MENU) {
				loadData(true);
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}
}
