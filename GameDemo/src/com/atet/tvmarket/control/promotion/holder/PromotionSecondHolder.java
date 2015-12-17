package com.atet.tvmarket.control.promotion.holder;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
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
import android.widget.RelativeLayout;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.home.inf.OnRecyItemClickListener;
import com.atet.tvmarket.control.home.view.NoFocusViewPager;
import com.atet.tvmarket.control.promotion.activity.IntegralDetailsActivity;
import com.atet.tvmarket.entity.dao.GoodsInfo;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UIUtils;
import com.atet.tvmarket.utils.UmengUtils;
import com.atet.tvmarket.view.BaseImageView;
import com.atet.tvmarket.view.TvRecyclerView;

/**
 * @description: 游戏中心第二部分布局的holder
 * 
 * @author: LiJie
 * @date: 2015年6月12日 上午9:55:15
 */
public class PromotionSecondHolder extends ViewHolder implements
		OnFocusChangeListener, OnKeyListener {
	// 轮播图点的宽高
	ALog alog = ALog.getLogger(PromotionSecondHolder.class);
	private static final int POINT_WIDTH = 66;
	private static final int POINT_HEIGHT = 67;
	// 轮播图的宽高
	private static final int IMAGEVIEWWIDTH = 680;
	private static final int IMAGEVIEWHEIGHT = 620;
	private static final int POINT_LEFTMARGIN = -10;

	private TvRecyclerView mRecyclerView;
	private OnRecyItemClickListener mlistener;
	private ImageFetcher imageFetcher;
	private RelativeLayout pro_carousel_game;
	private RelativeLayout pro_carousel_game_content;
	private NoFocusViewPager carousel_game_iv;
	private BaseImageView carousel_game_shadow;
	private LinearLayout point_container;
	private PicAdapter picAdapter;
	private AutoSwitch autoSwitch;
	private List<GoodsInfo> infos = new ArrayList<GoodsInfo>();
	private Context context;

	private int[] tagDefaultIDs = new int[] { R.drawable.point_default1,
			R.drawable.point_default2, R.drawable.point_default3,
			R.drawable.point_default4, R.drawable.point_default5 };

	private int[] tagSelectIDs = new int[] { R.drawable.point_select1,
			R.drawable.point_select2, R.drawable.point_select3,
			R.drawable.point_select4, R.drawable.point_select5 };

	public PromotionSecondHolder(View itemView, TvRecyclerView recyclerView,
			OnRecyItemClickListener listener, Context context) {
		super(itemView);
		ScaleViewUtils.scaleView(itemView);
		this.mlistener = listener;
		this.mRecyclerView = recyclerView;
		this.context = context;
		initView();
	}

	private void initView() {
		imageFetcher = new ImageFetcher();
		autoSwitch = new AutoSwitch();
		picAdapter = new PicAdapter();
		pro_carousel_game = (RelativeLayout) itemView
				.findViewById(R.id.promotion_carousel_game);
		pro_carousel_game_content = (RelativeLayout) itemView
				.findViewById(R.id.promotion_carousel_game_content);
		carousel_game_iv = (NoFocusViewPager) itemView
				.findViewById(R.id.promotion_carousel_game_iv);
		point_container = (LinearLayout) itemView
				.findViewById(R.id.promotion_point_containter);
		carousel_game_shadow = (BaseImageView) itemView
				.findViewById(R.id.promotion_carousel_game_shadow);

		pro_carousel_game.setOnFocusChangeListener(this);
		pro_carousel_game.setOnClickListener(new MyClickListener());
		pro_carousel_game.setOnKeyListener(this);

	}

	public void setData(List<GoodsInfo> GoodsInfos) {
		infos.clear();
		if (GoodsInfos.size() > 5) {
			for (int i = 0; i < 5; i++) {
				infos.add(GoodsInfos.get(i));
			}
		} else {
			infos.addAll(GoodsInfos);
		}

		initViewPager();
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
			if (infos.size() != 0) {
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
			if (infos.size() != 0) {
				position = position % infos.size();
			}
			final GoodsInfo goodsInfo = infos.get(position);

			imageFetcher.loadImage(goodsInfo.getSquarePhoto(), iv,
					R.drawable.default_big);

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
	 * @description: 跳转到活动详情
	 * 
	 * @param goodsInfo
	 * @throws:
	 * @author: LiJie
	 * @date: 2015年8月7日 上午11:47:43
	 */
	private void skipToActDetail(GoodsInfo goodsInfo) {
		pro_carousel_game.requestFocus();
		Intent intent = new Intent(context, IntegralDetailsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Constant.GOODSINFO, goodsInfo);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	/**
	 * 
	 * @description: 初始化轮播图，加入导航的点
	 * 
	 * @throws:
	 * @author: LiJie
	 * @date: 2015年6月12日 上午10:03:35
	 */
	private void initViewPager() {

		point_container.removeAllViews();
		// int count = 0;
		if (infos.size() > 1) {

			// count = infos.size() > 5 ? 5 : infos.size();

			for (int i = 0; i < infos.size(); i++) {
				BaseImageView point = new BaseImageView(UIUtils.getContext());
				point.setLayoutParams(new LayoutParams(POINT_WIDTH,
						POINT_HEIGHT));
				ScaleViewUtils.scaleView(point);
				point.setBackgroundResource(tagDefaultIDs[i]);
				android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

				if (i != 0) {
					params.leftMargin = (int) ScaleViewUtils
							.resetTextSize(POINT_LEFTMARGIN);
				}

				if (i == 0) {
					point.setBackgroundResource(tagSelectIDs[0]);
				}

				point_container.addView(point, params);

			}
		}

		carousel_game_iv.setAdapter(picAdapter);
		carousel_game_iv.setCurrentItem(infos.size() * 10000);
		carousel_game_iv.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				int count = point_container.getChildCount();
				for (int i = 0; i < count; i++) {
					if (i == position % count) {
						alog.info("lijie" + position);
						point_container.getChildAt(i).setBackgroundResource(
								tagSelectIDs[i]);
					} else {
						point_container.getChildAt(i).setBackgroundResource(
								tagDefaultIDs[i]);
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

		if (infos.size() > 1) {
			autoSwitch.start();
		}
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
			if (infos.size() > 1) {
				stop();
				UIUtils.postDelayed(this, DELAY);
			}
		}

		public void stop() {
			UIUtils.removeAllCallBack(this);
		}

		@Override
		public void run() {
			int item = carousel_game_iv.getCurrentItem();
			carousel_game_iv.setCurrentItem(++item);
			UIUtils.postDelayed(this, DELAY);
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {

			/*
			 * int[] location = new int[2]; v.getLocationOnScreen(location);
			 * 
			 * if(location[0] - v.getWidth() < mRecyclerView.getLeft()){
			 * mRecyclerView.smoothScrollBy(-v.getWidth(), 0); }
			 */

			if (v.getId() == pro_carousel_game.getId()) {
				carousel_game_shadow.setImageResource(R.drawable.white_focus);
				pro_carousel_game_content.setScaleX(1.1f);
				pro_carousel_game_content.setScaleY(1.1f);
				pro_carousel_game.setNextFocusLeftId(R.id.promotion_area);
				pro_carousel_game.setNextFocusUpId(R.id.tab_promotion);
				pro_carousel_game.setNextFocusDownId(pro_carousel_game.getId());
				autoSwitch.stop();
			}
		} else {
			if (v.getId() == pro_carousel_game.getId()) {
				carousel_game_shadow
						.setImageResource(android.R.color.transparent);
				pro_carousel_game_content.setScaleX(1.0f);
				pro_carousel_game_content.setScaleY(1.0f);
				autoSwitch.start();
			}
		}
	}

	class MyClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (infos.size() != 0) {
				// Umeng统计交互事件
				UmengUtils
						.setOnEvent(context, UmengUtils.PROMOTION_INTERACTION);
				// Umeng统计"物品图片"点击次数
				UmengUtils.setOnEvent(context,
						UmengUtils.PROMOTION_GOODSPIC_ENTER);
				GoodsInfo goodsInfo = infos.get(carousel_game_iv
						.getCurrentItem() % infos.size());
				skipToActDetail(goodsInfo);
			}
		}
	}

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
