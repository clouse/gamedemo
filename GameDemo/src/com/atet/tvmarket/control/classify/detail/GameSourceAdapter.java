package com.atet.tvmarket.control.classify.detail;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atet.tvmarket.R;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.mygame.activity.MyGameActivity;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.entity.dao.ThirdGameDownInfo;
import com.atet.tvmarket.model.DataConfig;
import com.atet.tvmarket.utils.CommonDialogHelper;
import com.atet.tvmarket.utils.CommonDialogHelper.Callbacks;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.view.NewToast;
import com.atet.tvmarket.view.recyclerview.BaseAdapter;
import com.atet.tvmarket.view.recyclerview.BaseViewHolder;
import com.tianci.media.api.Log;

public class GameSourceAdapter extends BaseAdapter {
	
	private RecyclerView mRecyclerView;
	private ImageFetcher mImageFetcher;
	List<ThirdGameDownInfo> thirdGameDownInfos = new ArrayList<ThirdGameDownInfo>();
	private ThirdGameDetailActivity context;
	
	private GameInfo gameInfo;
	private Dialog mDialog;
	public GameSourceAdapter(ThirdGameDetailActivity context,RecyclerView mRecyclerView,ImageFetcher mImageFetcher){
		this.context = context;
		this.mRecyclerView = mRecyclerView;
		this.mImageFetcher = mImageFetcher;
	}
	
	public void setData(List<ThirdGameDownInfo> thirdGameDownInfos){
		this.thirdGameDownInfos.clear();
		this.thirdGameDownInfos.addAll(thirdGameDownInfos);
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		if(thirdGameDownInfos.size()==0){
			return 5;
		}
		return thirdGameDownInfos.size();
	}

	@Override
	public void onBindViewHolder(BaseViewHolder viewHolder, int position) {
		
		ItemViewHolder holder = (ItemViewHolder) viewHolder;
		if(thirdGameDownInfos.size()>0){
			ThirdGameDownInfo thirdGameDownInfo = thirdGameDownInfos.get(position);
			holder.setIcon(thirdGameDownInfo.getLogoUrl());
			holder.setName(thirdGameDownInfo.getRemark());
			holder.setThirdGameDownInfo(thirdGameDownInfo);
		}
		if(position==0){
			holder.layout.requestFocus();
		}
	}

	@Override
	public BaseViewHolder onCreateViewHolder(ViewGroup contianer, int viewType) {
		View root = LayoutInflater.from(contianer.getContext()).inflate(R.layout.third_game_source_item, contianer, false);
		ScaleViewUtils.scaleView(root);
		return new ItemViewHolder(root);
	}

	class ItemViewHolder extends BaseViewHolder implements OnFocusChangeListener,OnClickListener,OnTouchListener{

		private RelativeLayout border,layout;
		private ImageView backgroud,icon;
		private TextView name;
		private ThirdGameDownInfo mThirdGameDownInfo;
		private View view;
		public ItemViewHolder(View itemView) {
			super(itemView);
			
			border = (RelativeLayout)itemView.findViewById(R.id.rl_border);
			layout = (RelativeLayout)itemView.findViewById(R.id.rl_source);
			backgroud = (ImageView)itemView.findViewById(R.id.iv_back);
			icon = (ImageView)itemView.findViewById(R.id.iv_icon);
			name = (TextView)itemView.findViewById(R.id.tv_source_name);
			
			layout.setOnFocusChangeListener(this);
			layout.setOnClickListener(this);
			layout.setOnTouchListener(this);
		}

		public void setBackGroud(int resid){
			backgroud.setImageResource(resid);
		}
		
		public void setIcon(String iconUrl){
			mImageFetcher.loadImage(iconUrl, icon, R.drawable.gameranking_item_icon);
		}
		
		public void setName(CharSequence nameTxt){
			name.setText(nameTxt);
		}
		
		public void setThirdGameDownInfo(ThirdGameDownInfo mThirdGameDownInfo){
			this.mThirdGameDownInfo = mThirdGameDownInfo;
		}
		
		@Override
		public void onClick(View v) {
			gameInfo = new GameInfo();
			if(mThirdGameDownInfo!=null){
				if(mThirdGameDownInfo.getUrl()==null || mThirdGameDownInfo.getUrl().trim()==""){
					NewToast.makeToast(context,context.getResources().getString(R.string.search_url_null),Toast.LENGTH_SHORT).show();
					return;
				}
				else{
					gameInfo.setDownToken(mThirdGameDownInfo.getDownToken());
					gameInfo.setFile(mThirdGameDownInfo.getUrl());
					gameInfo.setGameId(mThirdGameDownInfo.getGameId());
					gameInfo.setGameSize(mThirdGameDownInfo.getThirdGameInfo().getGameSize());
					gameInfo.setGameName(mThirdGameDownInfo.getThirdGameInfo().getGameName());
					gameInfo.setPackageName(mThirdGameDownInfo.getThirdGameInfo().getPackageName());
					gameInfo.setMinPhoto(mThirdGameDownInfo.getThirdGameInfo().getMinPhoto());
					gameInfo.setVersionCode(mThirdGameDownInfo.getThirdGameInfo().getVersionCode());
					gameInfo.setVersionName(mThirdGameDownInfo.getThirdGameInfo().getVersionName());
					gameInfo.setTypeName(mThirdGameDownInfo.getRemark());
					gameInfo.setCpId(""+2);
					gameInfo.setGameType(DataConfig.GAME_TYPE_COPYRIGHT);
					
					mDialog = CommonDialogHelper
							.openDialog(context,context.getResources().getString(R.string.down_btn_not_download),
									context.getResources().getString(R.string.manage_down_from_complany_download)
											+ mThirdGameDownInfo.getRemark()
											+ context.getResources().getString(R.string.manage_download),caller);
					mDialog.setOnKeyListener(mOnDialogKeyListener);
				}
			}
		}

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus){
				border.setBackgroundResource(R.drawable.myborder);
				backgroud.setVisibility(View.INVISIBLE);
				v.setScaleX(1.2f);
				v.setScaleY(1.2f);
				
				if(v.isInTouchMode() && v==view){
					v.performClick();
				}
				else{
					view=null;
				}
			}
			else{
				backgroud.setVisibility(View.VISIBLE);
				border.setBackgroundResource(android.R.color.transparent);
				v.setScaleX(1.0f);
				v.setScaleY(1.0f);
			}
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				OnFocusChangeListener listener = v.getOnFocusChangeListener();
				if(listener!=null){
					view = v;
				}
			}
			return false;
		}
		
	}
	
	Callbacks caller = new Callbacks() {
		
		@Override
		public void clickOk() {
			gameInfo.setTypeName("GameSearch");
			context.downloadGameInfo = gameInfo;
			MyGameActivity.chekAgeDownloadGame(context, gameInfo);
			Log.i("life","gameInfo:"+ gameInfo.toString());
			context.finish();
		}
		
		@Override
		public void clickCancle() {
			
		}
	};
	
	/** 用于监听dialog的按键监听器 */
	private DialogInterface.OnKeyListener mOnDialogKeyListener = new DialogInterface.OnKeyListener() {
		@Override
		public boolean onKey(DialogInterface dialogInterface, int keyCode,
				KeyEvent event) {
			// TODO Auto-generated method stub
			View focusView = ((Dialog) dialogInterface).getCurrentFocus();
			if (focusView == null) {
				return false;
			}

			if (keyCode == KeyEvent.KEYCODE_A
					|| keyCode == KeyEvent.KEYCODE_BUTTON_A) {
				KeyEvent keyevent = new KeyEvent(event.getAction(),
						KeyEvent.KEYCODE_ENTER);
				focusView.dispatchKeyEvent(keyevent);
				return true;
			} else if (keyCode == KeyEvent.KEYCODE_B
					|| keyCode == KeyEvent.KEYCODE_BUTTON_B) {
				Dialog dialog = (Dialog) dialogInterface;
				if (event.getAction() == KeyEvent.ACTION_UP && dialog != null
						&& dialog.isShowing()) {
					dialog.dismiss();
				}
				return true;
			} else if (keyCode == KeyEvent.KEYCODE_X
					|| keyCode == KeyEvent.KEYCODE_Y
					|| keyCode == KeyEvent.KEYCODE_BUTTON_X
					|| keyCode == KeyEvent.KEYCODE_BUTTON_Y) {
				return true;
			}
			return false;
		}
	};
}
