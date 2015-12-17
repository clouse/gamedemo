package com.atet.tvmarket.control.task;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atet.tvmarket.R;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.usertask.UserTaskInfo;
import com.atet.tvmarket.model.usertask.UserTaskInfo.UserTaskState;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.view.NewToast;

public class ManyTaskItem extends RelativeLayout {

	private TextView taskType,taskDesc;
	private LinearLayout manyTaskLayout;
	private Button recevice;
	private View itemView;
	private int itemCount = 0;
	private int mLocation;
	private int mPos;
	private List<UserTaskInfo> mUserTaskInfos = new ArrayList<UserTaskInfo>();
	TaskActivity mContext;
	public ManyTaskItem(Context context){
		this(context, null);
	}
	
	public ManyTaskItem(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public ManyTaskItem(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = (TaskActivity) context;
		LayoutInflater.from(context).inflate(R.layout.task_item_many_panel, this, true);
		taskType = (TextView)findViewById(R.id.tv_task_type);
		manyTaskLayout = (LinearLayout)findViewById(R.id.ll_task_many);
	}

	public void setTaskType(String type){
		taskType.setText(type);
	}
	
	public void setLocation(int location){
		this.mLocation = location;
	}
	
	public void setPosition(int pos){
		this.mPos = pos;
	}
	
	public void setData(List<UserTaskInfo> userTaskInfos){
		if(userTaskInfos!=null && userTaskInfos.size()>0){
			mUserTaskInfos.clear();
			mUserTaskInfos.addAll(userTaskInfos);
			manyTaskLayout.removeAllViews();
			itemCount = userTaskInfos.size();
			for(int i=0;i<itemCount;i++){
				itemView = LayoutInflater.from(getContext()).inflate(R.layout.many_task_item1, this, false);
				ScaleViewUtils.scaleView(itemView);
				taskDesc = (TextView) itemView.findViewById(R.id.tv_task_desc);
				recevice = (Button)itemView.findViewById(R.id.btn_receive);
				manyTaskLayout.addView(itemView);
				
				taskDesc.setText(userTaskInfos.get(i).getTaskRemark());
				recevice.setTag(i);
				recevice.setOnFocusChangeListener(onFocusChangeListener);
				recevice.setOnKeyListener(onKeyListener);
				UserTaskState taskState = userTaskInfos.get(i).getTaskState();
				if(taskState == UserTaskState.TASK_STATE_FINISH){
					recevice.setText("已  领  取");
					recevice.setBackgroundResource(R.drawable.common_btn_gary_selector);
					recevice.setOnClickListener(null);
				}
				else{
					recevice.setText("领  取  奖  励");
					recevice.setBackgroundResource(R.drawable.common_btn_selector);
					recevice.setOnClickListener(onClickListener);
				}
				
				if(mPos==0){
					switch (mLocation) {
					case 0:
						recevice.setNextFocusUpId(R.id.rb_routine);
						break;
					case 1:
						recevice.setNextFocusUpId(R.id.rb_daily);
						break;
					default:
						break;
					}
				}
				else{
					recevice.setNextFocusUpId(-1);
				}
			}
		}
	}
	
	OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus){
				if(v.isInTouchMode()){
					v.performClick();
				}
				
				View vv = (View) v.getParent().getParent().getParent().getParent();
				RecyclerView mRecyclerView = (RecyclerView) vv.getParent();
				int[] location = new int[2];
				vv.getLocationOnScreen(location);
				if(location[1]>mRecyclerView.getBottom()/2){
					mRecyclerView.smoothScrollBy(0, location[1] - mRecyclerView.getBottom()/2);
				}else if(location[1]<mRecyclerView.getBottom()/2){
					mRecyclerView.smoothScrollBy(0, location[1] - mRecyclerView.getBottom()/2);
				}
			}
		}
	};
	
	OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int loc = (Integer) v.getTag();
			UserTaskInfo userTaskInfo = mUserTaskInfos.get(loc);
			UserTaskState taskState = userTaskInfo.getTaskState();
			if(NetUtil.isNetworkAvailable(getContext(), true)){
				if(DataFetcher.isUserLogin()){
					if(taskState == UserTaskState.TASK_STATE_FINISH){
						//该任务已完成
						NewToast.makeToast(getContext(), "该任务已完成，奖励已领取", Toast.LENGTH_SHORT).show();
					} else if(taskState == UserTaskState.TASK_STATE_NOT_FINISH){
						//任务未完成
						NewToast.makeToast(getContext(), "该任务还未完成", Toast.LENGTH_SHORT).show();
					} else if(taskState == UserTaskState.TASK_STATE_INVALID 
							|| taskState == UserTaskState.TASK_STATE_NOT_EXIST){
						//任务无效
						NewToast.makeToast(getContext(), "该任务不存在", Toast.LENGTH_SHORT).show();
					}else if(taskState == UserTaskState.TASK_STATE_STANDBY){
						//任务已完成，去领取奖励
						mContext.btnView = (Button) v;
						mContext.mViewType = 2;
						mContext.obtainUserTaskReward(userTaskInfo);
					}
					else{
						NewToast.makeToast(getContext(), "该任务不存在", Toast.LENGTH_SHORT).show();
					}
				}
				else{
					mContext.snapDialog();
				}
			}
			else{
				NewToast.makeToast(getContext(), "网络未连接",
						Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	OnKeyListener onKeyListener = new OnKeyListener() {
		
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			int action = event.getAction();
			if(action==KeyEvent.ACTION_DOWN){
				if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT||keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
					return true;
				}
				else if (keyCode == KeyEvent.KEYCODE_DPAD_UP){
					if(System.currentTimeMillis() - mContext.curtime<400){
						return true;
					}
					else{
						mContext.curtime = System.currentTimeMillis();
					}
				}
			}
			return false;
		}
	};
}
