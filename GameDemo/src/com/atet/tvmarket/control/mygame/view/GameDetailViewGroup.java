package com.atet.tvmarket.control.mygame.view;

/**
 * 游戏详情自定义ViewGroup
 */
import android.widget.*;
import com.atet.tvmarket.R;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.control.video.PlayVideoActivity;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.model.net.http.download.BtnDownListenner;
import com.atet.tvmarket.utils.ScaleViewUtils;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;


public class GameDetailViewGroup extends ViewGroup {

	private Context context;
	private Scroller mScroller = null;  //滚动监听
	private int ScreenShootRows;   //游戏详情图片的列数
	private GameDetailFirstItemView logoItem;   //第一个item
    private GameDetailSecondItemView remarkItem;  //第二个item
	private ImageFetcher mImageFetcher;
	private int VIEW_MARGIN = 5;  //Item之间的间距
	private int allStartLeft = 0; 
	private BtnDownListenner mBtnDownListenner;  //游戏详情下载按钮的监听器
	/** 游戏详情的图片类型 */
	public static final int GAME_DETAIL_SCREEN_TYPE_IMG = 1;
	public static final int GAME_DETAIL_SCREEN_TYPE_VIDEO = 2;
	
	public GameDetailViewGroup(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
		setChildrenDrawingOrderEnabled(true);
	}

	public GameDetailViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
		setChildrenDrawingOrderEnabled(true);
	}

	public GameDetailViewGroup(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
		setChildrenDrawingOrderEnabled(true);
	}

	
	private void init(){
		mScroller = new Scroller(context);
		mImageFetcher =  ((BaseActivity) context).getmImageFetcher();
		initDefaultView(); // 初始化界面
	}
	

	/**
	 * 
	 * @description 初始化界面
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:06:50
	 *
	 */
	public void initDefaultView() {
		removeAllViews();
		logoItem = new GameDetailFirstItemView(context);
		addView(logoItem);
		remarkItem = new GameDetailSecondItemView(context);
		addView(remarkItem);
		int imgRows = 2;//游戏截屏的列数，默认为2
		ScreenShootRows = imgRows;
		for(int i = 0;i<imgRows ;i++){
			View ScreenShootItem = inflate(context, R.layout.game_detail_screen_shoot_item, null);
			addView(ScreenShootItem);
		}
		
		logoItem.getLogoViewFocus().requestFocus();
		logoItem.getLogoViewFocus().setOnTouchListener(mOnTouchListener);
		logoItem.getLogoViewFocus().setOnFocusChangeListener(mOnTouchLogoFocusChangeListener);
		remarkItem.getRbtOne().setOnTouchListener(mOnTouchListener);
		remarkItem.getRbtTwo().setOnTouchListener(mOnTouchListener);
		remarkItem.getRbtThree().setOnTouchListener(mOnTouchListener);
		remarkItem.getRbtOne().setOnFocusChangeListener(mOnTouchFocusChangeListener);
		remarkItem.getRbtTwo().setOnFocusChangeListener(mOnTouchFocusChangeListener);
		remarkItem.getRbtThree().setOnFocusChangeListener(mOnTouchFocusChangeListener);
		logoItem.getLogoViewFocus().setNextFocusRightId(remarkItem.getRbtOne().getId());	
		initScreenShoot();	
	}


	/**
	 * 
	 * @description 初始化游戏截图控件 
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:07:13
	 *
	 */
	private void initScreenShoot( ){
	    for(int i = 0;i<ScreenShootRows;i++){
	    	// 设置游戏截图Item的点击以及焦点事件
		    setScreenShootListener(i);
	    }
		if(getChildCount()>2){  // 若有游戏截图，则设置第二个Item与截图的焦点交互
        	remarkItem.getRbtThree().setNextFocusRightId(getChildAt(2).findViewById(R.id.game_detail_screenshot_item2).getId());
        	remarkItem.getRbtThree().setNextFocusDownId(remarkItem.getThreeView0().getId());
        	getChildAt(2).findViewById(R.id.game_detail_screenshot_item2).setNextFocusLeftId(remarkItem.getRbtThree().getId());
        	getChildAt(2).findViewById(R.id.game_detail_screenshot_item3).setNextFocusLeftId(remarkItem.getRbtThree().getId());
        }
	}
	

	
	/**
	 * 
	 * @description  设置单个截图的点击以及焦点事件
	 * @param i
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:07:41
	 *
	 */
	private void setScreenShootListener(int i){
			View view = getChildAt(i+2);
			ImageView screenShoot_top_item2 = (ImageView) view.findViewById(R.id.game_detail_screenshot_item2);
			ImageView screenShoot_top_item3 = (ImageView) view.findViewById(R.id.game_detail_screenshot_item3);
        	screenShoot_top_item2.setOnTouchListener(mOnTouchListener);
        	screenShoot_top_item2.setOnFocusChangeListener(mOnTouchFocusChangeListener);
        	screenShoot_top_item3.setOnTouchListener(mOnTouchListener);
        	screenShoot_top_item3.setOnFocusChangeListener(mOnTouchFocusChangeListener);
        	screenShoot_top_item2.setTag(i+2);
        	screenShoot_top_item3.setTag(i+2);
        	screenShoot_top_item2.setFocusable(true);
        	screenShoot_top_item3.setFocusable(true);
	}
	
	/**
	 * 游戏截图的点击事件类
	 * @author chenqingwen
	 *@url 视频url
	 */
  class GameScreenOnClickListener  implements View.OnClickListener {
	  private String url;
	  GameScreenOnClickListener(String url){
		  this.url = url;
	  }
	  
	@Override
	public void onClick(View arg0) { // 点击事件，实现视频的播放
		// TODO Auto-generated method stub	
		Intent intent = new Intent();
		intent.setClass(context, PlayVideoActivity.class);
		intent.putExtra("videoUrl", url);
		context.startActivity(intent);
	}	  
	}
	
	/**
	 * 
	 * @description 通知更新数据显示
	 * @param gameInfo
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:08:03
	 *
	 */
	public void notifiDataChange(final GameInfo gameInfo) {   
        //游戏详情的第一个item
        logoItem.getGameName().setText(gameInfo.getGameName());  // 设置游戏名
        try {  // 加载游戏图片
			mImageFetcher.loadGameDetailImage(gameInfo.getErectPhoto(), logoItem.getLogoView(),logoItem.getLogoViewContainer(), R.drawable.default_vertical);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        // 添加游戏下载监听
        setDownListener(gameInfo);
        //游戏详情的第二个item,显示数据
        remarkItem.setData(gameInfo);
        // 根据游戏截图个数，判断是否需要添加更多的游戏截图View
        removeScreenShootView(gameInfo.getImgs().size());
      	ScreenShootRows = gameInfo.getImgs().size()/2;
        for(int i = 0;i<ScreenShootRows;i++){  // 加载游戏截图图片
        	View view = getChildAt(i+2);
        	ImageView screenShoot_top = (ImageView) view.findViewById(R.id.game_detail_screenshot_iv2);
        	ImageView screenShoot_bottom = (ImageView) view.findViewById(R.id.game_detail_screenshot_iv3);
        	ImageView videoIvTop = (ImageView) view.findViewById(R.id.game_detail_video_iv2);
          	ImageView videoIvBottom = (ImageView) view.findViewById(R.id.game_detail_video_iv3);
        	if(i>2)
        	  setScreenShootListener(i);
            if(gameInfo.getImgs().get(i*2).getType()== GAME_DETAIL_SCREEN_TYPE_IMG){  //游戏截图
            	mImageFetcher.loadImage(gameInfo.getImgs().get(i*2).getPhotoUrl(), screenShoot_top,R.drawable.default_recommand);
            }else{  // 显示视频
            	videoIvTop.setVisibility(View.VISIBLE);
            	// 图片属性暂时没有，显示默认图片
            	mImageFetcher.loadLocalImage(R.drawable.default_recommand, screenShoot_top, R.drawable.default_recommand);
            	screenShoot_top.setOnClickListener(new GameScreenOnClickListener(gameInfo.getImgs().get(i*2).getPhotoUrl()));
            }
            
            if(gameInfo.getImgs().get(i*2+1).getType() == GAME_DETAIL_SCREEN_TYPE_IMG){
    			mImageFetcher.loadImage(gameInfo.getImgs().get(i*2+1).getPhotoUrl(), screenShoot_bottom, R.drawable.default_recommand);
    		}else{
    			videoIvBottom.setVisibility(View.VISIBLE);
    	     	mImageFetcher.loadLocalImage(R.drawable.default_recommand, screenShoot_bottom, R.drawable.default_recommand);
    	     	screenShoot_top.setOnClickListener(new GameScreenOnClickListener(gameInfo.getImgs().get(i*2 +1).getPhotoUrl()));
    		}
            
        	}
		requestLayout();
	}
	
	
	

	/**
	 * 
	 * @description 移除屏幕截图的图片
	 * @param imgCounts 游戏截图数目
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:08:16
	 *
	 */
	private void removeScreenShootView(int imgCounts){
		int imgRows = 0;//游戏截屏的列数		
		if(imgCounts > 4 ){  // 大于默认的截图列数，添加View
			imgRows = (imgCounts-4)/2;
			ScreenShootRows = imgRows+2;
			for(int i = 0;i<imgRows ;i++){
				View ScreenShootItem = inflate(context, R.layout.game_detail_screen_shoot_item, null);
				ScaleViewUtils.scaleView(ScreenShootItem);
				addView(ScreenShootItem);
			}
		}else if(imgCounts == 4){
			
		}else if(imgCounts >=2 && imgCounts <= 3){
			if (null != getChildAt(3)) removeViewAt(3);
		}else{
			if (null != getChildAt(3)) removeViewAt(3);
		}
	}

	/**
	 * 
	 * @description 添加游戏下载监听
	 * @param gameInfo
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:08:35
	 *
	 */
	private void setDownListener(GameInfo gameInfo) {
		if (mBtnDownListenner == null) {
			mBtnDownListenner = new BtnDownListenner(context);
		}
		mBtnDownListenner.listen(logoItem.getLogoViewContainer(), logoItem.getmBtnUpload(),logoItem.getDownIv(), gameInfo);
	}
	
	
	/**
	 * 
	 * @description 回收btndownlistener
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:08:45
	 *
	 */
	public void recyleBtndownListener(){
		if(mBtnDownListenner!=null){
			mBtnDownListenner.recycle();
		}
		mBtnDownListenner = null;
	}
	
	/**
	 * 
	 * @description  开始下载游戏
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:08:59
	 *
	 */
	public void startDownloadGame(){
		mBtnDownListenner.startDownloadGame();
	}
	
	
	
	private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				OnFocusChangeListener listener = v.getOnFocusChangeListener();
				if (listener != null) {
					if(listener instanceof OnTouchFocusChangeListener)
					   ((OnTouchFocusChangeListener) listener).setView(v);
					else if(listener instanceof OnTouchLogoFocusChangeListener){
						 ((OnTouchLogoFocusChangeListener) listener).setView(v);
					}
				}
			}
			return false;
		}
	};
	
	private OnTouchFocusChangeListener mOnTouchFocusChangeListener = new OnTouchFocusChangeListener();

	class OnTouchFocusChangeListener implements View.OnFocusChangeListener {
		private View view;

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				if (v.isInTouchMode() && v == view) {
					v.performClick();
				} else {
					view = null;
				}
				if(v instanceof RadioButton){
					RadioButton rbtn = (RadioButton)v;
					rbtn.setChecked(true);
					startScroll((View)v.getParent());
				}else{
					startScroll(v);
				}
			}
			requestLayout();
		}

		public View getView() {
			return view;
		}

		public void setView(View view) {
			this.view = view;
		}
	}
	
	private OnTouchLogoFocusChangeListener mOnTouchLogoFocusChangeListener = new OnTouchLogoFocusChangeListener();

	class OnTouchLogoFocusChangeListener implements View.OnFocusChangeListener {
		private View view;

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				if (v.isInTouchMode() && v == view) {
					v.performClick();
				} else {
					view = null;
				}
				// 游戏详情第一个Item 下载图标聚焦显示，不聚焦消失
				GameDetailFirstItemView detailFirstItemView = (GameDetailFirstItemView) v.getParent().getParent();
				detailFirstItemView.getDownIv().setVisibility(View.VISIBLE);
				startScroll(v);
			}else{
				GameDetailFirstItemView detailFirstItemView = (GameDetailFirstItemView) v.getParent().getParent();
				detailFirstItemView.getDownIv().setVisibility(View.INVISIBLE);
			}
			requestLayout();
		}

		public View getView() {
			return view;
		}

		public void setView(View view) {
			this.view = view;
		}
	}
	
	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
		// 左边多滚了
		if (getScrollX() < 0) {
			scrollTo(0, 0);
			postInvalidate();

		}
		if (getScrollX() + getWidth() > allStartLeft
				&& allStartLeft > getWidth()) {// 右边多滚了
			scrollTo(allStartLeft - getWidth(), 0);
			postInvalidate();
		}
		super.computeScroll();
	}


	/**
	 * 
	 * @description 开始滚动
	 * @param view
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午11:09:56
	 *
	 */
	public void startScroll(View view) {
		if (view != null) {
			if (mScroller != null) {
				if (!mScroller.isFinished()) {
					mScroller.abortAnimation();
				}
			}

			int[] location = new int[2];
			view.getLocationOnScreen(location);
			// 如果当前控件显示不全，则开始滚动
			if (location[0] + view.getWidth() * 1.5 > getWidth()) {
				mScroller.startScroll(getScrollX(), 0,
						(int)((Math.abs(location[0])+view.getWidth()*1.5-getWidth()*2/3))
						, 0, (int)((Math.abs(location[0])+view.getWidth()*1.5-getWidth())));
			} else if (location[0] < 0) {
				mScroller.startScroll(getScrollX(), 0,
						(int)(location[0]*2.5)		// 之前1.5修改成2.5不知道这种写法对不
						, 0, Math.abs((int)(location[0]*1.5)));

			}
			invalidate();
		}
	}
	
	
	// 处理触摸的速率
		private VelocityTracker mVelocityTracker = null;
		private static final int TOUCH_STATE_REST = 0;
		private static final int TOUCH_STATE_SCROLLING = 1;
		private int mTouchState = TOUCH_STATE_REST;
		// --------------------------
		// 处理触摸事件 ~
		public static int SNAP_VELOCITY = 600;
		private int mTouchSlop = 0;
		private float mLastionMotionX = 0;

		@Override
		public boolean onInterceptTouchEvent(MotionEvent ev) {
			final int action = ev.getAction();
			// 表示已经开始滑动了，不需要走该Action_MOVE方法了(第一次时可能调用)。
			if ((action == MotionEvent.ACTION_MOVE)
					&& (mTouchState != TOUCH_STATE_REST)) {
				return true;
			}

			final float x = ev.getX();

			switch (action) {
			case MotionEvent.ACTION_MOVE:
				final int xDiff = (int) Math.abs(mLastionMotionX - x);
				// 超过了最小滑动距离
				if (xDiff > mTouchSlop) {
					mTouchState = TOUCH_STATE_SCROLLING;
				}
				break;

			case MotionEvent.ACTION_DOWN:
				mLastionMotionX = x;
				mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
						: TOUCH_STATE_SCROLLING;

				break;

			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				mTouchState = TOUCH_STATE_REST;
				break;
			}
			return mTouchState != TOUCH_STATE_REST;
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if (mVelocityTracker == null) {
				mVelocityTracker = VelocityTracker.obtain();
			}

			mVelocityTracker.addMovement(event);

			super.onTouchEvent(event);

			// 手指位置地点
			float x = event.getX();

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// 如果屏幕的动画还没结束，你就按下了，我们就结束该动画
				if (mScroller != null) {
					if (!mScroller.isFinished()) {
						mScroller.abortAnimation();
					}
				}
				mLastionMotionX = x;
				break;
			case MotionEvent.ACTION_MOVE:
				int detaX = (int) (mLastionMotionX - x);
				scrollBy(detaX, 0);
				mLastionMotionX = x;

				break;
			case MotionEvent.ACTION_UP:

				final VelocityTracker velocityTracker = mVelocityTracker;
				velocityTracker.computeCurrentVelocity(1000);

				// 多滚了回弹
				int leftPositonX = getScrollX();
				if (leftPositonX < 0) {
					scrollBy(-leftPositonX, 0);
				} else if (leftPositonX + getWidth() > allStartLeft) {
					scrollBy(-(leftPositonX + getWidth() - allStartLeft), 0);
				}
				invalidate();

				if (mVelocityTracker != null) {
					mVelocityTracker.recycle();
					mVelocityTracker = null;
				}

				mTouchState = TOUCH_STATE_REST;

				break;
			case MotionEvent.ACTION_CANCEL:
				mTouchState = TOUCH_STATE_REST;
				break;
			}
			return true;
		}		

		
			
		@Override
		protected void onLayout(boolean changed, int l, int t, int r, int b) {
			// TODO Auto-generated method stub
			int childCount = getChildCount();
			int startLeft = 0;
			int startTop = 0;
			for (int i = 0; i < childCount; i++) {
				View child = getChildAt(i);
				int width= child.getMeasuredWidth();
				int height = child.getMeasuredHeight();

				// 特别注意为了调整图片之前的间隔而写成这样(因为之前别人就这么处理的，不好修改)
				if (i > 2) {
					startLeft -= ScaleViewUtils.resetWidth(40);
					child.layout(startLeft, startTop, startLeft + width,
							startTop + height);
				} else {
					child.layout(startLeft, startTop, startLeft + width,
							startTop + height);
				}
				startLeft += width + VIEW_MARGIN;

			}
			allStartLeft = startLeft;
		}
		
		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			int width = MeasureSpec.getSize(widthMeasureSpec);
			int height = MeasureSpec.getSize(heightMeasureSpec);

			setMeasuredDimension(width, height);

			int childCount = getChildCount();
			for (int i = 0; i < childCount; i++) {
				try {
					View child = getChildAt(i);

					child.measure(getWidth(), getHeight());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		

		// 重写绘制顺序
		@Override
		protected int getChildDrawingOrder(int childCount, int i) {
			if (hasFocus()) {
				View view = getFocusedChild();
				if (view != null) {
					int index = indexOfChild(view);
					if (index >= 0) {
						if (i < index) {
							return i;
						} else {
							return childCount - 1 - i + index;
						}
					}
				}
			}

			return super.getChildDrawingOrder(childCount, i);
		}

}
