package com.atet.tvmarket.control.home.holder;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.home.inf.OnRecyItemClickListener;
import com.atet.tvmarket.control.home.view.GameCenterThirdPanel;
import com.atet.tvmarket.control.home.view.NoFocusViewPager;
import com.atet.tvmarket.control.mygame.activity.GameDetailActivity;
import com.atet.tvmarket.control.video.PlayVideoActivity;
import com.atet.tvmarket.entity.dao.AdInfo;
import com.atet.tvmarket.utils.GameAdapterTypeUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UIUtils;
import com.atet.tvmarket.utils.UmengUtils;
import com.atet.tvmarket.view.BaseImageView;

/**
 * 
 * @description: 游戏中心第三部分布局的holder
 * 
 * @author: LiJie
 * @date: 2015年6月12日 上午9:56:20
 */
public class GameCenterThirdHolder extends ViewHolder implements
		OnFocusChangeListener, OnClickListener, OnKeyListener {
	ALog alog = ALog.getLogger(GameCenterThirdHolder.class);
	public static final int IMAGEVIEWHEIGHT = 300;// 轮播图片的大小
	public static final int IMAGEVIEWWIDTH = 610;
	public static final int POINT_WIDTH = 10;
	public static final int POINT_HEIGHT = 10; // 点的大小
	public static final int POINT_LEFTMARGIN = 10;// 点之间的间距

	public GameCenterThirdPanel panel;
	private RecyclerView recyclerView;
	private OnRecyItemClickListener listener;
	private ImageFetcher imageFetcher;
	private PicAdapter picAdapter;
	private AutoSwitch autoSwitch;
	private NoFocusViewPager carousel_game;
	private LinearLayout point_container;
	private Context context;
	private int tag = 1; // 进入游戏详情

	private List<AdInfo> infos2 = new ArrayList<AdInfo>();// 轮播图的数据
	private List<AdInfo> carsourInfos = new ArrayList<AdInfo>();// 轮播图的数据

	private List<AdInfo> infos3 = new ArrayList<AdInfo>();// 方图的数据

	public GameCenterThirdHolder(View itemView, RecyclerView recyclerView,
			OnRecyItemClickListener listener, Context context) {
		super(itemView);
		this.recyclerView = recyclerView;
		this.listener = listener;
		this.context = context;
		initEvent();

	}

	private void initEvent() {
		panel = (GameCenterThirdPanel) itemView;
		imageFetcher = new ImageFetcher();
		autoSwitch = new AutoSwitch();
		picAdapter = new PicAdapter();
		carousel_game = panel.getMain_game_iv();

		panel.getMain_game().setOnFocusChangeListener(this);
		panel.getMain_game().setOnClickListener(new MyClickListener());

		panel.getSmall_game1().setOnFocusChangeListener(this);
		panel.getSmall_game1().setOnClickListener(this);
		panel.getSmall_game1().setOnKeyListener(this);

		panel.getSmall_game2().setOnFocusChangeListener(this);
		panel.getSmall_game2().setOnClickListener(this);
		panel.getSmall_game2().setOnKeyListener(this);

		// panel.setOnPressUpListener(this);
		// panel.getCarousel_game_content().setOnKeyListener(this);
		// addReflect();
	}

	public void setData(List<AdInfo> infos2, List<AdInfo> infos3) {
		this.infos2 = infos2;
		this.infos3 = infos3;

		// 判断轮播最大5张图
		carsourInfos.clear();
		if (infos2.size() > 5) {
			for (int i = 0; i < 5; i++) {
				carsourInfos.add(infos2.get(i));
			}
		} else {
			carsourInfos.addAll(infos2);
		}

		initViewPager();

		if (infos3.size() != 0) {
			imageFetcher.loadImage(infos3.get(0).getCornerMark(),panel.getSmall_game1_released(), R.drawable.translation);
			imageFetcher.loadImage(infos3.get(1).getCornerMark(),panel.getSmall_game2_released(), R.drawable.translation);
			
			UIUtils.setOnline(panel.getSmall_game1_online(), infos3.get(0).getOnline());
			UIUtils.setOnline(panel.getSmall_game2_online(), infos3.get(1).getOnline());
			
			GameAdapterTypeUtil.decideAdapter(infos3.get(0).getHandleType(),
					panel.getSmall_game1_handle_icon(),
					panel.getSmall_game1_control_icon());
			GameAdapterTypeUtil.decideAdapter(infos3.get(1).getHandleType(),
					panel.getSmall_game2_handle_icon(),
					panel.getSmall_game2_control_icon());
			imageFetcher.loadImage(infos3.get(0).getUrl(),
					panel.getSmall_game1_iv(), R.drawable.default_square);
			imageFetcher.loadImage(infos3.get(1).getUrl(),
					panel.getSmall_game2_iv(), R.drawable.default_square);
		}

	}

	/*
	 * private void addReflect() { ThreadManager.getThreadPool().execute(new
	 * Runnable() {
	 * 
	 * @Override public void run() { reflectBitmap =
	 * ImageReflectUtil.createReflectBitmap(R.drawable.game_search_item_bg,
	 * Constant.PIC_REFLECT_HEIGHT); Message msg = Message.obtain();
	 * mHandler.sendMessage(msg); } });
	 * 
	 * }
	 */

	private void initViewPager() {
		point_container = panel.getPoint_container();
		point_container.removeAllViews();

		if (carsourInfos.size() > 1) {

			for (int i = 0; i < carsourInfos.size(); i++) {
				BaseImageView point = new BaseImageView(UIUtils.getContext());
				point.setLayoutParams(new LayoutParams(POINT_WIDTH,
						POINT_HEIGHT));

				ScaleViewUtils.scaleView(point);
				point.setBackgroundResource(R.drawable.point_default);
				android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

				if (i != 0) {
					params.leftMargin = (int) ScaleViewUtils
							.resetTextSize(POINT_LEFTMARGIN);
				}

				if (i == 0) {
					point.setBackgroundResource(R.drawable.point_select);
				}

				point_container.addView(point, params);

			}
		}

		carousel_game.setAdapter(picAdapter);
		carousel_game.setCurrentItem(carsourInfos.size() * 10000 - 1);
		carousel_game.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				int count = point_container.getChildCount();
				for (int i = 0; i < count; i++) {
					if (i == position % count) {
						point_container.getChildAt(i).setBackgroundResource(
								R.drawable.point_select);
					} else {
						point_container.getChildAt(i).setBackgroundResource(
								R.drawable.point_default);
					}
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		if (carsourInfos.size() > 1) {
			autoSwitch.start();
		}

	}

	/**
	 * 
	 * @description: 处理轮播图的Adapter
	 * 
	 * @author: LiJie
	 * @date: 2015年6月12日 上午10:00:25
	 */
	class PicAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			if (infos2.size() != 0) {
				return Integer.MAX_VALUE;
			}
			return 0;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {

			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BaseImageView iv = new BaseImageView(UIUtils.getContext());
			iv.setLayoutParams(new LayoutParams(IMAGEVIEWWIDTH, IMAGEVIEWHEIGHT));
			ScaleViewUtils.scaleView(iv);
			// 控制ImageView的缩放
			if (carsourInfos.size() != 0) {
				position = position % carsourInfos.size();
				final AdInfo info = carsourInfos.get(position);
				imageFetcher.loadImage(info.getCornerMark(), panel.getMain_game_released(),R.drawable.translation);
				UIUtils.setOnline(panel.getMain_game_online(), info.getOnline());
				GameAdapterTypeUtil.decideAdapter(info.getHandleType(),
						panel.getMain_game_handle_icon(),
						panel.getMain_game_control_icon());
				imageFetcher.loadImage(info.getUrl(), iv,
						R.drawable.default_cross);
			}
			iv.setScaleType(ScaleType.FIT_XY);

			iv.setOnClickListener(new MyClickListener());

			container.addView(iv);
			return iv;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View) object);
		}
	}

	/**
	 * 
	 * @description: 跳转到游戏详情
	 * 
	 * @param info
	 * @throws:
	 * @author: LiJie
	 * @date: 2015年8月7日 上午11:48:28
	 */
	private void skipToGameDetail(final AdInfo info) {
		panel.getMain_game().requestFocus();
		Intent intent = new Intent(context, GameDetailActivity.class);
		intent.putExtra(Constant.GAMECENTER, 1);
		intent.putExtra("gameId", info.getGameId());
		alog.info(info.toString());
		context.startActivity(intent);
	}

	/**
	 * 
	 * @description: 处理轮播图的线程类
	 * 
	 * @author: LiJie
	 * @date: 2015年5月28日 下午5:49:53
	 */
	class AutoSwitch implements Runnable {
		private static final long DELAY = 6000;

		public void start() {
			stop();
			UIUtils.postDelayed(this, DELAY);
		}

		public void stop() {
			UIUtils.removeAllCallBack(this);
		}

		@Override
		public void run() {
			if (carousel_game != null) {
				int item = carousel_game.getCurrentItem();
				carousel_game.setCurrentItem(++item);
				UIUtils.postDelayed(this, DELAY);
			}
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			/*
			 * int[] location = new int[2]; v.getLocationOnScreen(location);
			 * 
			 * if (location[0] - v.getWidth() * 0.3 < recyclerView.getLeft()) {
			 * recyclerView.smoothScrollBy(-v.getWidth(), 0); }
			 */

			if (v.getId() == panel.getMain_game().getId()) {
				panel.getMain_game_shadow().setImageResource(
						R.drawable.white_focus);
				panel.getMain_game_content().setScaleX(1.1f);
				panel.getMain_game_content().setScaleY(1.1f);
				panel.getMain_game().setNextFocusDownId(
						panel.getSmall_game1().getId());
				autoSwitch.stop();
			} else if (v.getId() == R.id.game_center_small_game1) {
				panel.getSmall_game1_shadow().setImageResource(
						R.drawable.white_focus);
				panel.getSmall_game1_content().setScaleX(1.1f);
				panel.getSmall_game1_content().setScaleY(1.1f);
				panel.getSmall_game1().setNextFocusUpId(
						panel.getMain_game().getId());

				if (infos3 != null && infos3.size() != 0) {
					if (infos3.get(0) != null) {
						String videoUrl = infos3.get(0).getVideoUrl();
						if (videoUrl != null && !videoUrl.isEmpty()) {
							panel.getSmall_game1_video().setVisibility(
									View.VISIBLE);
						}
					}
				}
			} else if (v.getId() == R.id.game_center_small_game2) {
				panel.getSmall_game2_shadow().setImageResource(
						R.drawable.white_focus);
				panel.getSmall_game2_content().setScaleX(1.1f);
				panel.getSmall_game2_content().setScaleY(1.1f);
				panel.getSmall_game2().setNextFocusUpId(
						panel.getMain_game().getId());

				if (infos3 != null && infos3.size() != 0) {
					if (infos3.get(0) != null) {
						String videoUrl = infos3.get(1).getVideoUrl();
						if (videoUrl != null && !videoUrl.isEmpty()) {
							panel.getSmall_game2_video().setVisibility(
									View.VISIBLE);
						}
					}
				}
			}

			if (v.isInTouchMode()) {
				v.performClick();
			}

		} else {
			if (v.getId() == R.id.game_center_main_game) {
				panel.getMain_game_shadow().setImageResource(
						android.R.color.transparent);
				panel.getMain_game_content().setScaleX(1.0f);
				panel.getMain_game_content().setScaleY(1.0f);
				autoSwitch.start();
			} else if (v.getId() == R.id.game_center_small_game1) {
				panel.getSmall_game1_shadow().setImageResource(
						android.R.color.transparent);
				panel.getSmall_game1_content().setScaleX(1.0f);
				panel.getSmall_game1_content().setScaleY(1.0f);
				panel.getSmall_game1_video().setVisibility(View.INVISIBLE);
			} else if (v.getId() == R.id.game_center_small_game2) {
				panel.getSmall_game2_shadow().setImageResource(
						android.R.color.transparent);
				panel.getSmall_game2_content().setScaleX(1.0f);
				panel.getSmall_game2_content().setScaleY(1.0f);
				panel.getSmall_game2_video().setVisibility(View.INVISIBLE);

			}
		}
	}

	@Override
	public void onClick(View v) {
		// Umeng统计所有交互
		UmengUtils.setOnEvent(context, UmengUtils.GAMECENTER_INTERACTION);
		v.requestFocus();
		Intent intent;
		AdInfo info = null;
		View view;

		if (infos3 != null && infos3.size() != 0) {
			if (v.getId() == panel.getSmall_game1().getId()) {
				info = infos3.get(0);
				view = panel.getSmall_game1_iv();
				// Umeng统计"单项1"
				UmengUtils.setOnEvent(context, UmengUtils.GAMECENTER_SINGLE1);
			} else if (infos3.size() > 1) {
				info = infos3.get(1);
				view = panel.getSmall_game2_iv();
				// Umeng统计"单项2"
				UmengUtils.setOnEvent(context, UmengUtils.GAMECENTER_SINGLE2);
			}
			if (info != null) {
				if (info.getVideoUrl() == null || info.getVideoUrl().isEmpty()) {
					intent = new Intent(context, GameDetailActivity.class);
					intent.putExtra(Constant.GAMECENTER, 1);
					intent.putExtra("gameId", info.getGameId());
					context.startActivity(intent);
				} else {
					intent = new Intent(context, PlayVideoActivity.class);
					intent.putExtra(Constant.GAMECENTER, 1);
					intent.putExtra("videoUrl", info.getVideoUrl());
					intent.putExtra("isGameVideo", true);
					intent.putExtra("gameId", info.getGameId());
					context.startActivity(intent);
				}
			}
		}
	}

	class MyClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// Umeng统计所有交互
			UmengUtils.setOnEvent(context, UmengUtils.GAMECENTER_INTERACTION);
			if (infos2.size() != 0) {
				// Umeng统计"幻灯片"数目
				UmengUtils.setOnEvent(context, UmengUtils.GAMECENTER_PPT_CLICK);
				AdInfo adInfo = infos2.get(carousel_game.getCurrentItem()
						% infos2.size());
				skipToGameDetail(adInfo);
			}
		}
	}

	/*
	 * @Override public void onItemUp() { ((MainActivity)
	 * context).forceFocusTab(MainActivity.TAB_ID_GAMECENTER, 0); }
	 */
	@Override
	public boolean onKey(View view, int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {

				return true;
			}
		}
		return false;
	}
}
