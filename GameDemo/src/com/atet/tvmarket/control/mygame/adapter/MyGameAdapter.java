package com.atet.tvmarket.control.mygame.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.home.adapter.BaseAdapter;
import com.atet.tvmarket.control.home.adapter.BaseViewHolder;
import com.atet.tvmarket.control.home.entity.BaseGameInfo;
import com.atet.tvmarket.control.mygame.activity.MyGameActivity;
import com.atet.tvmarket.control.mygame.utils.DownloadingUtil;
import com.atet.tvmarket.control.mygame.view.MyGameItemView;
import com.atet.tvmarket.control.mygame.view.MyGameRecyclerView;
import com.atet.tvmarket.model.entity.MyGameInfo;
import com.atet.tvmarket.model.net.http.download.DownloadTask;
import com.atet.tvmarket.model.net.http.download.FileDownInfo;
import com.atet.tvmarket.model.net.http.download.FileUtils;
import com.atet.tvmarket.utils.AppUtil;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.StringTool;
import com.atet.tvmarket.view.NewToast;

/**
 * 我的游戏适配器
 */
public class MyGameAdapter extends BaseAdapter {
	private static  boolean ivDeleteState = false;  // 是否处于删除状态
    private MyGameRecyclerView recyclerView;
	private View lastFocusView=null;  // 最后聚焦的View
	private ImageFetcher mImageFetcher;
    private MyGameActivity context;
    private Handler mHandler;
    private DownloadingUtil downloadingUtil;  // 下载安装Util
    private ArrayList<MyGameInfo> arrayList = new ArrayList();  //游戏信息
    private DownloadTask mDownTask;  // 实现下载的对象
	private static final int IMAGE_ROUND_RATIO=8;  // 图片圆角度
	
    public MyGameAdapter(MyGameRecyclerView recyclerView, MyGameActivity context, ImageFetcher mImageFetcher, Handler mHandler, DownloadTask mDownTask) {
    	super();
    	this.recyclerView = recyclerView;
        this.mHandler = mHandler;
        this.context = context;
        this.mImageFetcher = mImageFetcher;
        this.mDownTask = mDownTask;
        downloadingUtil = new DownloadingUtil(context, mHandler);
        downloadingUtil.setImageFetcher(mImageFetcher);

    }

//    private Handler initDownHandler = new Handler(){
//
//		@Override
//		public void handleMessage(Message msg) {
//			// TODO Auto-generated method stub
//			if(msg.what == 1){
//				int pos = msg.arg1;
//				String gameId = (String) msg.obj;
//				String currentId = null;
//				if(pos <arrayList.size()){
//				   currentId = arrayList.get(pos).getGameId();
//				}else{
//					return;
//				}
//				if(gameId.equals(currentId)){  // 需初始化下载进度的游戏尚在当前界面中
//				// 下载进度
//				    final int downedPercent = mDownTask.getDownedPercent(currentId);
//				    MyGameItemView myGameItemView = (MyGameItemView) recyclerView.getChildAt(pos);
//				    if(myGameItemView != null){
//				    	myGameItemView.getGameView().setGameId(gameId);
//				        myGameItemView.getGameView().updateMyGameProgress(downedPercent, true);
//				    }
//				}
//			}
//		}
//    	
//    };

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0x12345) {  // 安装预装游戏
				boolean isOk = msg.getData().getBoolean("getApkConfig");
				if (isOk) {
					MyGameInfo myGameInfo = (MyGameInfo) msg.getData().getSerializable("myGameInfo");
					int pos = msg.getData().getInt("pos");
					MyGameItemView gameItemView = (MyGameItemView) recyclerView.getChildAt(pos);
					downloadingUtil.showInstallApkDialog(myGameInfo, gameItemView.getTvProgress());
				} else {
					NewToast.makeToast(
							context,
							context.getResources().getString(R.string.apkinstall_get_fail),
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	};
	
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

		View view = LayoutInflater.from(context).inflate(R.layout.mygame_item_view, viewGroup, false);
		((MyGameItemView) view).init(context);

        SingleViewHolder viewHolder = new SingleViewHolder(view);
        return viewHolder;
    }


	@Override
    public void onBindViewHolder(BaseViewHolder holder, int pos) {
    	SingleViewHolder singleViewHolder = (SingleViewHolder)holder;
		singleViewHolder.setBackgroundResource(pos); // 设置背景
		singleViewHolder.setMyGameUpdateIcon(arrayList, pos); // 设置游戏更新图标
		singleViewHolder.setMyGameName(arrayList, pos);  // 设置游戏名
		singleViewHolder.setIvDeleteIcon(); // 设置删除图标
		singleViewHolder.setDownloadingIcon(arrayList.get(pos), pos);// 设置游戏图标以及游戏状态
		singleViewHolder.view.setTag(pos);
		singleViewHolder.view.setOnClickListener(new ItemViewOnClickListener(singleViewHolder, pos)); // 设置点击事件
		if(pos == 0){
			if(lastFocusView==null){
				singleViewHolder.view.requestFocus();
			}
			else{
				singleViewHolder.view.clearFocus();
			}
		}
    } 

    @Override
    public int getItemCount() {
		return arrayList.size();
    }

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
    

	/**
	 * 
	 * @description adapter中单一数据改变
	 * @param info
	 * @param position
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:46:03
	 *
	 */
    public void notifySingleDataChanged(MyGameInfo info, int position){
    	arrayList.set(position, info);
    }
    
    
	public  class SingleViewHolder extends BaseViewHolder  implements OnFocusChangeListener, OnKeyListener, View.OnTouchListener {
        public MyGameItemView view;
        
    	
        public SingleViewHolder(View itemView) {
            super(itemView);
        	ScaleViewUtils.scaleView(itemView);
            view = (MyGameItemView)itemView;
    		itemView.setOnFocusChangeListener(this);
    		itemView.setOnKeyListener(this);
			itemView.setOnTouchListener(this);
        }
        
        /**
         * 
         * @description 设置ItemView 背景
         * @param pos
         * @throws
         * @author：陈庆文
         * @date 2015-8-28上午10:47:16
         *
         */
        public void setBackgroundResource(int pos){
    		view.getGameView().setBackgroundResource(R.color.rbtn_focus_bg);
        }
         
		/**
		 * 
		 * @description  设置游戏名
		 * @param arrayList
		 * @param pos
		 * @throws
		 * @author：陈庆文
		 * @date 2015-8-28上午10:47:06
		 *
		 */
        public void setMyGameName(ArrayList<MyGameInfo> arrayList, int pos){
    		view.getTitle().setText(arrayList.get(pos).getName());
        }
        
        
		/**
		 * 
		 * @description 设置游戏更新图标
		 * @param arrayList
		 * @param pos
		 * @throws
		 * @author：陈庆文
		 * @date 2015-8-28上午10:46:59
		 *
		 */
        public void setMyGameUpdateIcon(ArrayList<MyGameInfo> arrayList, int pos){
    		if(arrayList.get(pos).getState() == Constant.GAME_STATE_NEED_UPDATE){
    			view.getIvUpdateState().setVisibility(View.VISIBLE);
    		}else{
    			view.getIvUpdateState().setVisibility(View.INVISIBLE);
    		}
        }
        
		/**
		 * 
		 * @description 设置对应游戏状态的图标
		 * @param info
		 * @param pos
		 * @throws
		 * @author：陈庆文
		 * @date 2015-8-28上午10:46:38
		 *
		 */
        public void setDownloadingIcon(MyGameInfo info, final int pos){
        	mImageFetcher.loadLocalImage(R.drawable.gameranking_item_icon, view.getBackView(),R.drawable.gameranking_item_icon);
    		// 游戏状态
    		int gameState = info.getState() & 0xff;
    		// 游戏没有下载或者游戏下载错误
    		if (gameState == Constant.GAME_STATE_NOT_DOWNLOAD
    				|| gameState == Constant.GAME_STATE_DOWNLOAD_ERROR) {
    		
				// 下载进度
				final int downedPercent = mDownTask.getDownedPercent(info.getGameId());
    			// 如果游戏正在下载中(在下载队列中)
    			if (mDownTask.isDownloading(info.getGameId())) {
					view.getIvState().setBackgroundResource(R.drawable.game_detail_downing_btn_selector);
    				// 如果进度不为0，显示下载进度信息
    				if (downedPercent >= 0) {
    					view.getTvProgress().setText(downedPercent +"%");
    					loadDownloadingImage(info, pos);
    				} else {
    					// 如果进度信息小于0，则显示"等待"
    					view.getTvProgress().setText(R.string.down_btn_wait_to_start);
    					loadDownloadingImage2(info, pos);
    				}
    			} else {
    				// 如果游戏没有下载，显示"暂停"图标
    				if (gameState == Constant.GAME_STATE_NOT_DOWNLOAD) {
    					if (downedPercent >= 0) {
    						view.getTvProgress().setText(downedPercent +"%");
    					}else{
    						view.getTvProgress().setText("0%");
    					}
    					view.getIvState().setBackgroundResource(R.drawable.game_detail__downpause_btn_selector);
    				} else {
    					// 设置下载错误图标
    					view.getIvState()
    							.setBackgroundResource(R.drawable.mygame_download_error);
    				}
    				loadDownloadingImage2(info, pos);
    			}
    		} else {
    			// 如果游戏还没有安装
    			if (gameState == Constant.GAME_STATE_NOT_INSTALLED_APK) {
    				Drawable apkIcon = FileUtils.getApkIcon(info.getLocalDir()
    						+ info.getLocalFilename(), context);
    				if (apkIcon != null) {
    					view.getBackView().setImageDrawable(apkIcon);
    				} else {
    					view.getBackView().setImageResource(R.drawable.icon);
    				}
    				view.getIvState().setBackgroundResource(R.drawable.game_detail_install_btn_selector);
    				// 若当前该游戏处于正在安装状态，则显示安装中
    				if(DownloadingUtil.getmInstallingPackageName()!=null && DownloadingUtil.getmInstallingPackageName().equals(info.getPackageName())){
    					view.getTvProgress().setText(R.string.apkinstall_dialog_msg_installing);
    				}
    			} else if (gameState == Constant.GAME_STATE_NOT_INSTALLED) {
    				// 若当前该游戏处于正在安装状态，则显示安装中
    				if(DownloadingUtil.getmInstallingPackageName()!=null && DownloadingUtil.getmInstallingPackageName().equals(info.getPackageName())){
    					view.getTvProgress().setText(R.string.apkinstall_dialog_msg_installing);
    				}
					view.getIvState().setBackgroundResource(R.drawable.game_detail_install_btn_selector);
    				if (info.getIconUrl() != null) {
    					mImageFetcher.loadImage(info.getIconUrl(),
    							view.getBackView(), MyGameAdapter.IMAGE_ROUND_RATIO, 0);
    				} else {
    					try {
    						mImageFetcher.loadImage2(info.getLaunchAct(),
    								view.getBackView(),
    								MyGameAdapter.IMAGE_ROUND_RATIO, context,
    								info.getPackageName(), info.getLaunchAct(),
    								info.getIconUrl(), 0);
    					} catch (Exception e) {
    						// TODO: handle exception
    						e.printStackTrace();
    					}
    				}
    			} else {
    				try {
    					view.getIvState().setBackgroundResource(R.drawable.game_detail_run_btn_selector);
    					mImageFetcher.loadImage2(info.getLaunchAct(),
    							view.getBackView(), MyGameAdapter.IMAGE_ROUND_RATIO,
    							context, info.getPackageName(),
    							info.getLaunchAct(), info.getIconUrl(), 0);
    				} catch (Exception e) {
    					// TODO: handle exception
    					e.printStackTrace();
    				}
    			}
    		}
	
        }
        
        /**
         * 
         * @description 下载状态下加载游戏图标
         * @param info
         * @param pos
         * @throws
         * @author：陈庆文
         * @date 2015-8-28上午10:47:28
         *
         */
        private void loadDownloadingImage2(MyGameInfo info, int pos){
          	// 在图标处显示游戏图标
        	if (info.getIconUrl() != null) {
				mImageFetcher.loadImage(info.getIconUrl(), view.getBackView(),
						MyGameAdapter.IMAGE_ROUND_RATIO, 0);
			} else {
				try {
					mImageFetcher.loadImage2(
							info.getPackageName() + "/" + info.getLaunchAct(),
							view.getBackView(), MyGameAdapter.IMAGE_ROUND_RATIO,
							context, info.getPackageName(),
							info.getLaunchAct(), info.getIconUrl(), 0);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
        }
        
        /**
         * 
         * @description 非下载状态下加载游戏图标
         * @param info 我的游戏信息
         * @param pos 对应位置
         * @throws
         * @author：陈庆文
         * @date 2015-8-28上午10:47:53
         *
         */
        private void loadDownloadingImage(MyGameInfo info, int pos){
        	// 在图标处显示游戏图标
			if (info.getIconUrl() != null) {
				mImageFetcher.loadMyGameImage(info.getIconUrl(), view.getBackView(),
						MyGameAdapter.IMAGE_ROUND_RATIO, 0, pos, info.getGameId());
			} else {
				try {
					mImageFetcher.loadMyGameImage2(
							info.getPackageName() + "/" + info.getLaunchAct(),
							view.getBackView(), MyGameAdapter.IMAGE_ROUND_RATIO,
							context, info.getPackageName(),
							info.getLaunchAct(), info.getIconUrl(), 0, pos, info.getGameId());
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
        }
        // 设置删除图标
        public void setIvDeleteIcon(){
    		if(ivDeleteState){
    			view.getIvDeleteState().setVisibility(View.VISIBLE);
    		}else{
    			view.getIvDeleteState().setVisibility(View.INVISIBLE);
    		}
        }

		@Override
		public void onFocusChange(View v, boolean hasFocus) {  // 设置焦点事件
			// TODO Auto-generated method stub
			if(hasFocus){
				view.getShadowView().setImageResource(R.drawable.market_common_border);
				view.setScaleX(1.1f);
				view.setScaleY(1.1f);
			}
			else{
				view.getShadowView().setImageResource(android.R.color.transparent);
				view.setScaleX(1.0f);
				view.setScaleY(1.0f);
			}
		}

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {  // 监听左右选择，进行翻页操作

			if(event.getAction()==KeyEvent.ACTION_DOWN){
				int pos = (Integer) v.getTag();
				if(pos>=9 && keyCode==KeyEvent.KEYCODE_DPAD_RIGHT){
					context.nextPage();
					lastFocusView=null;
					return true;
				}else if(pos<=2 && keyCode==KeyEvent.KEYCODE_DPAD_LEFT){
					context.previousPage(false);
					lastFocusView=null;
					return true;
				} else if (keyCode==KeyEvent.KEYCODE_DPAD_RIGHT
						&& (pos + 3) >= getItemCount()) {
					// 不处理最右的切换到向下的焦点
					return true;
				} else if (KeyEvent.KEYCODE_DPAD_DOWN == keyCode
						&& (pos + 1) >= getItemCount()) {
					// 不处理向下焦点(如果向下没有控件不切换焦点)
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (MotionEvent.ACTION_UP == event.getAction()) {
				view.requestFocusFromTouch();
				view.performClick();
			}
			return true;
		}
	}


  
	/**
	 * 
	 * @description 设置是否处于删除状态
	 * @param ivDeleteState
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:48:39
	 *
	 */
    public void setDeleteState(boolean ivDeleteState){
    	MyGameAdapter.ivDeleteState = ivDeleteState;
    }
    
 
    /**
     * 点击事件监听类
     * @author chenqingwen
     *
     */
    class ItemViewOnClickListener implements View.OnClickListener{
		SingleViewHolder singleViewHolder;
		int mPos;
		public ItemViewOnClickListener(SingleViewHolder singleViewHolder, int pos){
			this.singleViewHolder = singleViewHolder;
			mPos = pos;
		}
		
		
		@Override
		public void onClick(View v) {
			if (!ivDeleteState) {  // 不处于删除状态
				downOrRun(v, arrayList.get(mPos),mPos);
			} else { // 处于删除状态，则弹出删除对话框
				downloadingUtil.showDeleteDialog(arrayList.get(mPos),mPos, mDownTask, singleViewHolder.view.getTvProgress()); // 显示删除对话框
			}
		}
	}


 
    /**
     * 
     * @description  点击事件，根据游戏状态的不同，作出相应的处理
     * @param v
     * @param myGameInfo
     * @param index
     * @throws
     * @author：陈庆文
     * @date 2015-8-28上午10:49:19
     *
     */
    public void downOrRun(View v, final MyGameInfo myGameInfo, final int index){
        final MyGameItemView view = (MyGameItemView)v;
    	switch(myGameInfo.getState()){
    	//游戏未安装
    	case Constant.GAME_STATE_NOT_INSTALLED_APK :  // 用户自己放置apk在指定目录下的游戏
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					boolean isOk = FileUtils.getApkConfig(myGameInfo.getPackageName());
					Message msg = new Message();
					msg.what = 0x12345;
					Bundle bundle = new Bundle();
					bundle.putBoolean("getApkConfig", isOk);
					bundle.putSerializable("myGameInfo", myGameInfo);
					bundle.putInt("index", index);
					msg.setData(bundle);
					handler.sendMessage(msg);
				}
			}).start();
            break;
            
        // 游戏需更新    
        case Constant.GAME_STATE_NEED_UPDATE :
        	downloadingUtil.showUpdateDialog(myGameInfo,index);
    	    break;
    	    
    	// 游戏未下载    
        case Constant.GAME_STATE_NOT_DOWNLOAD :
    		if (!mDownTask.isDownloading(myGameInfo.getGameId())) {
				// 如果当前不可下载
				if (!NetUtil.isEnableDownload(context, true))
					return;
				
				notDownloadHanlde(myGameInfo);
				
			}else{
				mDownTask.setDownloadStop(myGameInfo.getGameId());
			}
        	break;
        	
        // 游戏下载错误
        case Constant.GAME_STATE_DOWNLOAD_ERROR :
     		if (!mDownTask.isDownloading(myGameInfo.getGameId())) {
				// 如果当前不可下载
				if (!NetUtil.isEnableDownload(context, true))
					return;
				notDownloadHanlde(myGameInfo);		
			}else{
				mDownTask.setDownloadStop(myGameInfo.getGameId());
			}
        	break;
        	
        // 游戏未安装
        case Constant.GAME_STATE_NOT_INSTALLED :
        	final File apkFile = new File(myGameInfo.getLocalDir(),
					myGameInfo.getLocalFilename());
			if (apkFile.exists()) {
				// 如果是.zip文件
				if (apkFile.getName().toLowerCase()
						.endsWith(MyGameActivity.ZIP_EXT)) {
		        	if(!StringTool.isEmpty(DownloadingUtil.getmInstallingPackageName())){
		        		NewToast.makeToast(context, R.string.exist_game_installing, Toast.LENGTH_LONG).show();
		        		return;
		        	}
		        	// 设置当前安装的游戏，作为判断，点击其他游戏，则将无法安装
		        	DownloadingUtil.setmInstallingPackageName(myGameInfo.getPackageName());
					final String unApkPath = MyGameActivity
							.getLocalUnApkPath(myGameInfo);
					final String packageName = myGameInfo.getPackageName();
					final String name = myGameInfo.getName();
					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							downloadingUtil.unZipApk(apkFile.getAbsolutePath(),
									Constant.GAME_ZIP_DATA_LOCAL_DIR,
									unApkPath, packageName, name, view.getTvProgress());
						}
					}).start();
				} else {
					// 文件已经下载，直接安装
					NewToast.makeToast(context,
							R.string.install_not_support_apk_file,
							Toast.LENGTH_SHORT).show();
				}
			} else {
				// 文件已丢失				
				downloadingUtil.showReDownloadDialog(
						myGameInfo);
			}
        	break;
        	
        // 游戏已安装
        case Constant.GAME_STATE_INSTALLED :
			// 已安装
			if (!AppUtil.startAppByPkgName(context,
					myGameInfo.getPackageName())) {
				String downUrl = myGameInfo.getDownUrl();
				if (downUrl == null || downUrl.equals("")) {
					NewToast.makeToast(context, R.string.manage_warn_app_delete,
							Toast.LENGTH_SHORT).show();
				} else {
					if (myGameInfo != null) {
						File zipFile = new File(myGameInfo.getLocalDir(),
								myGameInfo.getLocalFilename());
						if (zipFile != null && zipFile.exists()) {
							// 重新安装
							downloadingUtil.showReInstallDialog(myGameInfo, view.getTvProgress());
						} else {
							// 重新下载
							downloadingUtil.showReDownloadDialog(
									myGameInfo);
						}
					} else {
						NewToast.makeToast(context, R.string.manage_warn_delete,
								Toast.LENGTH_SHORT).show();
					}
				}
			} else {
				    // 点击运行，交换数据，改变位置
                    context.swapData(index);
	        		lastFocusView = null;
			}
        	break;
        	
        default:
        	
    	}
    }



    /**
     * 
     * @description 设置adapter数据
     * @param items
     * @throws
     * @author：陈庆文
     * @date 2015-8-28上午10:50:03
     *
     */
	public void setItem(ArrayList<MyGameInfo> items) {
		// TODO Auto-generated method stub	
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		lastFocusView = null;
	    arrayList.clear();
	    arrayList.addAll(items);
	}
	
	/**
	 * 获取对应游戏Id在当前界面所在的位置
	 * @param gameId
	 * @return
	 */
	public int getPositionForGameId(String gameId){
		int size = arrayList.size();
		for(int i=0; i<size; i++){
			if(arrayList.get(i).getGameId().equals(gameId))
				return i;
		}
		return -1;
	}
	

	/**
	 * 
	 * @description 处理下载或者暂停下载
	 * @param myGameInfo
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:50:25
	 *
	 */
	public void notDownloadHanlde(MyGameInfo myGameInfo) {
		// 如果游戏不是处于下载状态(不处于下载队列中)
		if (!mDownTask.isDownloading(myGameInfo.getGameId())) {
			// 如果游戏状态为下载错误状态
			if (myGameInfo.getState() == Constant.GAME_STATE_DOWNLOAD_ERROR) {
				// 将状态改为没有下载
				myGameInfo.setState(Constant.GAME_STATE_NOT_DOWNLOAD);
				// 更新我的游戏信息
//				PersistentSynUtils.update(myGameInfo);
			}
			// 开始下载当前游戏
			startDownload(mDownTask, myGameInfo);
		} else { // 如果正处于下载中，则暂停
			mDownTask.setDownloadStop(myGameInfo.getGameId());
		}
	}
	
	

	/**
	 * 
	 * @description 开始下载
	 * @param downTask 下载辅助类对象
	 * @param myGameInfo
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:50:59
	 *
	 */
	private void startDownload(DownloadTask downTask, MyGameInfo myGameInfo) {
		FileDownInfo fileDownInfo = new FileDownInfo(myGameInfo.getGameId(),
				myGameInfo.getDownUrl(), myGameInfo.getLocalDir(),
				myGameInfo.getLocalFilename());
		fileDownInfo.setExtraData(myGameInfo.getName());
		fileDownInfo.setObject(myGameInfo);
		// add by wenfuqiang
		if (myGameInfo.getDownToken() == Constant.DOWN_FROM_THIRD) {
			// 每次下载都获取一次下载地址

		} else {
			fileDownInfo.setDownUrl(myGameInfo.getDownUrl());
		}
//		if (myGameInfo.getAutoIncrementId() != null) {
//			myGameInfo.setDbId(myGameInfo.getAutoIncrementId());
//		}
		downTask.download(fileDownInfo, myGameInfo.getDownToken());
	}


	@Override
	public void setItems(List<BaseGameInfo> myItems) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onViewRecycled(BaseViewHolder holder) {
		// TODO Auto-generated method stub
		super.onViewRecycled(holder);
		SingleViewHolder singleViewHolder = (SingleViewHolder) holder;
		singleViewHolder.view.getGameView().removeDrawableOnMyGame();
	}
	
}
