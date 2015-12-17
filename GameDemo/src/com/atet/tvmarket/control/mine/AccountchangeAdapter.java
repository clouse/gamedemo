package com.atet.tvmarket.control.mine;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atet.tvmarket.R;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.entity.UserInfo;
import com.atet.tvmarket.utils.ScaleViewUtils;

public class AccountchangeAdapter extends
		RecyclerView.Adapter<AccountchangeAdapter.ViewHolder> {

	private List<UserInfo> userInfos;
	private int itemCount = 0;
	private IAccountIconClick accountIconClickInterface = null;
	private Context context;
	private RecyclerView mRecycleView;
	public static final String LOGIN = "0";
	public static final String REGISTER = "-1";
	private ImageFetcher mImageFetcher;
	public AccountchangeAdapter(List<UserInfo> userInfos,
			IAccountIconClick accountIconClickInterface,
			RecyclerView recycleView,ImageFetcher imageFetcher) {
		super();
		itemCount = userInfos.size() + 2;
		this.userInfos = userInfos;
		this.accountIconClickInterface = accountIconClickInterface;
		mRecycleView = recycleView;
		this.mImageFetcher = imageFetcher;
	}

	/**
	 * 
	 * @Title: dataChange
	 * @Description: TODO(adapter监听数据变化)
	 * @param @param userInfos 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void dataChange(List<UserInfo> userInfos) {
		if (userInfos == null) {
			userInfos = new ArrayList<UserInfo>();
		} else {
			this.userInfos = userInfos;
			itemCount = userInfos.size() + 2;
		}
		notifyDataSetChanged();
	}

	public class ImageClickLisener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Integer position = (Integer) v.getTag();
			UserInfo userInfo = new UserInfo();
			if (position == itemCount - 1) {
				userInfo.setUserName(LOGIN);
				accountIconClickInterface.accountIconClick(userInfo);
			} else if (position == itemCount - 2) {
				userInfo.setUserName(REGISTER);
				accountIconClickInterface.accountIconClick(userInfo);
			} else {
				accountIconClickInterface.accountIconClick(userInfos
						.get(position));
			}
		}
	}

	public class RecycleOnTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				RecycleOnFocusChangeListener listener = (RecycleOnFocusChangeListener) v
						.getOnFocusChangeListener();
				if (listener != null
						&& listener instanceof RecycleOnFocusChangeListener) {
					((RecycleOnFocusChangeListener) listener).setView(v);
				}
			}

			return false;
		}
	}

	public class RecycleOnFocusChangeListener implements OnFocusChangeListener {

		private View view;

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				int[] location = new int[2];
				v.getLocationOnScreen(location);
				// 控制左右滑动时列表的显示
				if (location[0] + v.getWidth() * 1.5 > BaseApplication.mScreenWidth) {
					mRecycleView
							.smoothScrollBy((int) ((v.getWidth()) * 1.5), 0);
				}
				if (location[0] - v.getWidth() < mRecycleView.getLeft()) {
					mRecycleView.smoothScrollBy(v.getWidth()
							- (BaseApplication.mScreenWidth - location[0]), 0);
				}
				// if (v instanceof RelativeLayout) {
				// RelativeLayout.LayoutParams linearParams =
				// (RelativeLayout.LayoutParams) v
				// .getLayoutParams();
				// linearParams.setMargins(50, 35, 50, 45);// 分别是margin_top那四个属性
				// v.setLayoutParams(linearParams);
				// // v.setBackgroundResource(R.drawable.focus_test);
				// }
				v.setScaleX(1.1f);
				v.setScaleY(1.1f);

				View pv = (View) v.getParent();
				pv.findViewById(R.id.iv_border).setVisibility(View.VISIBLE);

				System.out.println("[onFocusChange]" + v.toString());
				if (v.isInTouchMode() && v == view) {
					v.performClick();
				} else {
					view = null;
				}
			} else {
				// if (v instanceof RelativeLayout) {
				// RelativeLayout.LayoutParams linearParams =
				// (RelativeLayout.LayoutParams) v
				// .getLayoutParams();
				// linearParams.setMargins(23, 27, 23, 53);// 分别是margin_top那四个属性
				// v.setLayoutParams(linearParams);
				// }
				v.setScaleX(1.0f);
				v.setScaleY(1.0f);
				View pv = (View) v.getParent();
				pv.findViewById(R.id.iv_border).setVisibility(View.INVISIBLE);
			}

		}

		public View getView() {
			return view;
		}

		public void setView(View view) {
			this.view = view;
		}

	}

	public interface IAccountIconClick {
		public void accountIconClick(UserInfo userInfo);
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public ImageView mIVIcon;
		public TextView mTVName;
		public RelativeLayout mLayout;
		public ImageView mBorder;

		public ViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
			mIVIcon = (ImageView) itemView
					.findViewById(R.id.item_accountchange_icon);
			mTVName = (TextView) itemView
					.findViewById(R.id.item_accountchange_name);
			mLayout = (RelativeLayout) itemView
					.findViewById(R.id.item_accountchange_layout_bg);
			mBorder = (ImageView) itemView.findViewById(R.id.iv_border);
		}
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return userInfos.size() + 2;
	}

	@SuppressLint("NewApi")
	@Override
	public void onBindViewHolder(ViewHolder viewHodler, int position) {
		// 最后一个图标
		if (position == itemCount - 1) {
			viewHodler.mTVName.setText(context
					.getString(R.string.mine_changeaccount_otheraccount_btn));
			viewHodler.mIVIcon
					.setBackgroundResource(R.drawable.mine_accountchange_icon);
			// 倒数第二个图标
		} else if (position == itemCount - 2) {
			viewHodler.mTVName.setText(context
					.getString(R.string.mine_changeaccount_register_btn));
			viewHodler.mIVIcon
					.setBackgroundResource(R.drawable.mine_accountchange_add);
		} else {
			viewHodler.mTVName.setText(userInfos.get(position).getUserName());
			mImageFetcher.loadImage(userInfos.get(position).getIcon(), viewHodler.mIVIcon, R.drawable.mine_accountchange_other);
			/*viewHodler.mIVIcon
					.setBackgroundResource(R.drawable.mine_accountchange_other);*/
		}
		viewHodler.mLayout.setOnClickListener(new ImageClickLisener());
		viewHodler.mLayout.setNextFocusLeftId(0);
		viewHodler.mLayout.setNextFocusRightId(0);
		viewHodler.mLayout.setOnTouchListener(new RecycleOnTouchListener());
		viewHodler.mLayout
				.setOnFocusChangeListener(new RecycleOnFocusChangeListener());
		viewHodler.mLayout.setTag(position);
		if (position == 0) {
			viewHodler.mLayout.requestFocus();
			viewHodler.mLayout.requestFocusFromTouch();
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int typeStyle) {
		// TODO Auto-generated method stub
		context = parent.getContext();
		View view = View.inflate(context, R.layout.item_accountchange, null);
		ScaleViewUtils.scaleView(view);
		ViewHolder holder = new ViewHolder(view);
		return holder;
	}
}
