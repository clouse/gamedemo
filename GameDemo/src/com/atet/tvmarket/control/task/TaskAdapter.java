package com.atet.tvmarket.control.task;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.atet.tvmarket.R;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.usertask.CheckInTaskInfo;
import com.atet.tvmarket.model.usertask.UserTaskInfo;
import com.atet.tvmarket.model.usertask.UserTaskInfo.UserTaskState;
import com.atet.tvmarket.model.usertask.UserTaskInfo.UserTaskType;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.view.NewToast;
import com.atet.tvmarket.view.recyclerview.BaseAdapter;
import com.atet.tvmarket.view.recyclerview.BaseViewHolder;

public class TaskAdapter extends BaseAdapter {

	private RecyclerView mRecyclerView;
	private TaskActivity mContext;
	private int mLocation;
	private List<TaskItem> taskItems = new ArrayList<TaskItem>();
	private List<CheckInItem> checkInItems = new ArrayList<CheckInItem>();
	public TaskAdapter(RecyclerView recyclerView,TaskActivity context, int location){
		this.mRecyclerView = recyclerView;
		this.mContext = context;
		this.mLocation = location;
	}
	
	public enum ITEM_TYPE {
		ITEM_TYPE_SINGLE,
        ITEM_TYPE_CHECKIN,
        ITEM_TYPE_MANY
    }
	
	public void setData(List<TaskItem> taskItems) {
		this.taskItems.clear();
		this.taskItems.addAll(taskItems);
		notifyDataSetChanged();
	}
	
	@Override
	public int getItemCount() {
		if(taskItems==null){
			return 0;
		}
		return taskItems.size();
	}

	@Override
	public int getItemViewType(int position) {
		TaskItem item = taskItems.get(position);
		
		if(item.getTypeId()==0){
			return ITEM_TYPE.ITEM_TYPE_SINGLE.ordinal();
		}
		else if (item.getTypeId()==1){
			return ITEM_TYPE.ITEM_TYPE_CHECKIN.ordinal();
			
		}
		else if(item.getTypeId()==2){
			return ITEM_TYPE.ITEM_TYPE_MANY.ordinal();
		}
		return super.getItemViewType(position);
	}
	
	@Override
	public void onBindViewHolder(BaseViewHolder viewHolder, int position) {
		TaskItem item = taskItems.get(position);
		Button btnReceive=null;
		if(item.getTypeId()==0){
			UserTaskInfo taskInfo = item.getUserTaskInfo();
			SingleTaskViewHolder singleTask = (SingleTaskViewHolder) viewHolder;
			singleTask.setUserTaskInfo(taskInfo);
			singleTask.setTaskType(taskInfo.getTaskName());
			singleTask.setTaskDesc(taskInfo.getTaskRemark());
			singleTask.setTaskTip(taskInfo.getTaskStateRemark());
			//singleTask.getReceiveReward().setText(item.getBtnText());
			btnReceive = singleTask.getReceiveReward();
			btnReceive.setTag(position);
		}
		else if(item.getTypeId()==1){
			CheckInTaskInfo taskInfo = (CheckInTaskInfo) item.getUserTaskInfo();
			CheckInTaskViewHolder checkInTask  = (CheckInTaskViewHolder) viewHolder;
			checkInTask.setCheckInTaskInfo(taskInfo);
			checkInTask.setTaskType(taskInfo.getTaskName());
			checkInTask.setTaskDesc(taskInfo.getTaskRemark());
			checkInItems.clear();
			if(taskInfo.getTaskType().equals(UserTaskType.TASK_TYPE_CHECKIN_DAILY)){
				checkInItems.add(new CheckInItem("周一", taskInfo.getIntegral(CheckInTaskInfo.MON)+"积分", 1, taskInfo.isCheckIn(CheckInTaskInfo.MON)));
				checkInItems.add(new CheckInItem("周二", taskInfo.getIntegral(CheckInTaskInfo.TUE)+"积分", 1, taskInfo.isCheckIn(CheckInTaskInfo.TUE)));
				checkInItems.add(new CheckInItem("周三", taskInfo.getIntegral(CheckInTaskInfo.WED)+"积分", 1, taskInfo.isCheckIn(CheckInTaskInfo.WED)));
				checkInItems.add(new CheckInItem("周四", taskInfo.getIntegral(CheckInTaskInfo.THR)+"积分", 1, taskInfo.isCheckIn(CheckInTaskInfo.THR)));
				checkInItems.add(new CheckInItem("周五", taskInfo.getIntegral(CheckInTaskInfo.FRI)+"积分", 1, taskInfo.isCheckIn(CheckInTaskInfo.FRI)));
				checkInItems.add(new CheckInItem("周六", taskInfo.getIntegral(CheckInTaskInfo.SAT)+"积分", 1, taskInfo.isCheckIn(CheckInTaskInfo.SAT)));
				checkInItems.add(new CheckInItem("周日", taskInfo.getIntegral(CheckInTaskInfo.SUN)+"积分", 1, taskInfo.isCheckIn(CheckInTaskInfo.SUN)));
			}
			else if(taskInfo.getTaskType().equals(UserTaskType.TASK_TYPE_MARKET_LOGIN)){
				checkInItems.add(new CheckInItem("1天", taskInfo.getIntegral(CheckInTaskInfo.MON)+"积分", 1, taskInfo.isCheckIn(CheckInTaskInfo.MON)));
				checkInItems.add(new CheckInItem("2天", taskInfo.getIntegral(CheckInTaskInfo.TUE)+"积分", 1, taskInfo.isCheckIn(CheckInTaskInfo.TUE)));
				checkInItems.add(new CheckInItem("3天", taskInfo.getIntegral(CheckInTaskInfo.WED)+"积分", 1, taskInfo.isCheckIn(CheckInTaskInfo.WED)));
				checkInItems.add(new CheckInItem("4天", taskInfo.getIntegral(CheckInTaskInfo.THR)+"积分", 1, taskInfo.isCheckIn(CheckInTaskInfo.THR)));
				checkInItems.add(new CheckInItem("5天", taskInfo.getIntegral(CheckInTaskInfo.FRI)+"积分", 1, taskInfo.isCheckIn(CheckInTaskInfo.FRI)));
				checkInItems.add(new CheckInItem("6天", taskInfo.getIntegral(CheckInTaskInfo.SAT)+"积分", 1, taskInfo.isCheckIn(CheckInTaskInfo.SAT)));
				checkInItems.add(new CheckInItem("7天", taskInfo.getIntegral(CheckInTaskInfo.SUN)+"积分", 1, taskInfo.isCheckIn(CheckInTaskInfo.SUN)));
			}
			checkInTask.setRecyclerView(checkInItems);
			//checkInTask.getReceiveReward().setText(item.getBtnText());
			btnReceive = checkInTask.getReceiveReward();
			btnReceive.setTag(position);
		}
		else if(item.getTypeId()==2){
			UserTaskInfo taskInfo = item.getUserTaskInfo();
			ManyTaskViewHolder holder = (ManyTaskViewHolder) viewHolder;
			ManyTaskItem manyTaskItem = (ManyTaskItem) holder.itemView;
			manyTaskItem.setTaskType(taskInfo.getTaskName());
			manyTaskItem.setData(taskInfo.getSubTaskInfos());
			manyTaskItem.setLocation(mLocation);
			manyTaskItem.setPosition(position);
			/*holder.setTaskType(taskInfo.getTaskName());
			
			List<UserTaskInfo> subtTaskInfos = taskInfo.getSubTaskInfos();
			holder.setRecyclerView(subtTaskInfos,position);*/
			
		}
		if(btnReceive!=null){
			if(position==0){
				switch (mLocation) {
				case 0:
					btnReceive.setNextFocusUpId(R.id.rb_routine);
					break;
				case 1:
					btnReceive.setNextFocusUpId(R.id.rb_daily);
					break;
				/*case 2:
					btnReceive.setNextFocusUpId(R.id.rb_active);
					break;
				case 3:
					btnReceive.setNextFocusUpId(R.id.rb_recharge);
					break;*/
				default:
					break;
				}
			}
			else{
				btnReceive.setNextFocusUpId(-1);
			}
			if(position==getItemCount()-1){
				btnReceive.setNextFocusDownId(btnReceive.getId());
			}
			else{
				btnReceive.setNextFocusDownId(-1);
			}
			btnReceive.setNextFocusLeftId(btnReceive.getId());
		}
		//mRecyclerView.smoothScrollToPosition(position);
	}

	@Override
	public BaseViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
		View root;
		if(viewType==ITEM_TYPE.ITEM_TYPE_SINGLE.ordinal()){
			root = LayoutInflater.from(container.getContext()).inflate(R.layout.task_item_single, container, false);
			return new SingleTaskViewHolder(root);
		}
		else if(viewType==ITEM_TYPE.ITEM_TYPE_CHECKIN.ordinal()){
			root = LayoutInflater.from(container.getContext()).inflate(R.layout.task_item_checkin, container, false);
			return new CheckInTaskViewHolder(root);
		}
		else if(viewType==ITEM_TYPE.ITEM_TYPE_MANY.ordinal()){
			//root = LayoutInflater.from(container.getContext()).inflate(R.layout.task_item_many, container, false);
			ManyTaskItem taskItem = new ManyTaskItem(mContext);
			return new ManyTaskViewHolder(taskItem);
		}
		return null;
	}

	class SingleTaskViewHolder extends BaseViewHolder implements OnFocusChangeListener,OnClickListener,OnKeyListener{

		private TextView taskType,taskDesc,taskTip;
		private Button receiveReward;
		private UserTaskInfo userTaskInfo;
		
		public SingleTaskViewHolder(View itemView) {
			super(itemView);
			ScaleViewUtils.scaleView(itemView);
			taskType = (TextView)itemView.findViewById(R.id.tv_task_type);
			taskDesc = (TextView)itemView.findViewById(R.id.tv_task_desc);
			taskTip = (TextView)itemView.findViewById(R.id.tv_task_tip);
			receiveReward = (Button)itemView.findViewById(R.id.btn_receive);
			receiveReward.setOnFocusChangeListener(this);
			receiveReward.setOnKeyListener(this);
		}
		public void setTaskType(CharSequence taskTypeStr) {
			taskType.setText(taskTypeStr);
		}
		public void setTaskDesc(CharSequence taskDescStr) {
			taskDesc.setText(taskDescStr);
		}
		public void setTaskTip(CharSequence taskTipStr) {
			taskTip.setText(taskTipStr);
		}
		public TextView getTaskDesc() {
			return taskDesc;
		}
		
		public Button getReceiveReward() {
			return receiveReward;
		}
		
		public void setUserTaskInfo(UserTaskInfo userTaskInfo){
			this.userTaskInfo = userTaskInfo;
			UserTaskState taskState = userTaskInfo.getTaskState();
			if(taskState == UserTaskState.TASK_STATE_FINISH){
				receiveReward.setText("已  领  取");
				receiveReward.setBackgroundResource(R.drawable.common_btn_gary_selector);
				receiveReward.setOnClickListener(null);
			}
			else{
				receiveReward.setText("领  取  奖  励");
				receiveReward.setBackgroundResource(R.drawable.common_btn_selector);
				receiveReward.setOnClickListener(this);
			}
		}
		
		@Override
		public void onClick(View v) {
			UserTaskState taskState = userTaskInfo.getTaskState();
			if(NetUtil.isNetworkAvailable(mContext, true)){
				if(DataFetcher.isUserLogin()){
					if(taskState == UserTaskState.TASK_STATE_FINISH){
						//该任务已完成
						NewToast.makeToast(mContext, "该任务已完成，奖励已领取", Toast.LENGTH_SHORT).show();
					} else if(taskState == UserTaskState.TASK_STATE_NOT_FINISH){
						//任务未完成
						NewToast.makeToast(mContext, "该任务还未完成", Toast.LENGTH_SHORT).show();
					} else if(taskState == UserTaskState.TASK_STATE_INVALID 
							|| taskState == UserTaskState.TASK_STATE_NOT_EXIST){
						//任务无效
						NewToast.makeToast(mContext, "该任务不存在", Toast.LENGTH_SHORT).show();
					}else if(taskState == UserTaskState.TASK_STATE_STANDBY){
						//任务已完成，去领取奖励
						receiveReward.setOnClickListener(null);
						mContext.btnView = receiveReward;
						mContext.mViewType = 0;
						mContext.myViewHolder = SingleTaskViewHolder.this;
						mContext.obtainUserTaskReward(userTaskInfo);
					}
					else{
						NewToast.makeToast(mContext, "该任务不存在", Toast.LENGTH_SHORT).show();
					}
				}
				else{
					mContext.snapDialog();
				}
				
			}else{
				NewToast.makeToast(mContext, "网络未连接",
						Toast.LENGTH_SHORT).show();
			}
			
		}
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus){
				View vv = (View) v.getParent().getParent();
				int[] location = new int[2];
				vv.getLocationOnScreen(location);
				if(location[1]>mRecyclerView.getBottom()/2){
					mRecyclerView.smoothScrollBy(0, location[1] - mRecyclerView.getBottom()/2);
				}else if(location[1]<mRecyclerView.getBottom()/2){
					mRecyclerView.smoothScrollBy(0, location[1] - mRecyclerView.getBottom()/2);
				}
				if(v.isInTouchMode()){
					v.performClick();
				}
				
			}
		}
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
	}
	
	class CheckInTaskViewHolder extends BaseViewHolder implements OnFocusChangeListener,OnClickListener,OnKeyListener{

		private TextView taskType,taskDesc;
		private RecyclerView mItemRecyclerView;
		private Button receiveReward;
		private CheckInTaskAdapter mItemAdapter;
		private CheckInTaskInfo checkInTaskInfo;
		
		public CheckInTaskViewHolder(View itemView) {
			super(itemView);
			ScaleViewUtils.scaleView(itemView);
			taskType = (TextView)itemView.findViewById(R.id.tv_task_type);
			taskDesc = (TextView)itemView.findViewById(R.id.tv_task_desc);
			mItemRecyclerView = (RecyclerView)itemView.findViewById(R.id.rv_task_many);
			receiveReward = (Button)itemView.findViewById(R.id.btn_receive);
			receiveReward.setOnFocusChangeListener(this);
			receiveReward.setOnKeyListener(this);
		}
		public void setTaskType(CharSequence taskTypeStr) {
			taskType.setText(taskTypeStr);
		}
		public void setTaskDesc(CharSequence taskDescStr) {
			taskDesc.setText(taskDescStr);
		}
		
		public void setCheckInTaskInfo(CheckInTaskInfo checkInTaskInfo){
			this.checkInTaskInfo = checkInTaskInfo;
			UserTaskState taskState = checkInTaskInfo.getTaskState();
			Log.i("life", "taskState:"+taskState);
			if(taskState == UserTaskState.TASK_STATE_FINISH){
				receiveReward.setText("已  领  取");
				receiveReward.setBackgroundResource(R.drawable.common_btn_gary_selector);
				receiveReward.setOnClickListener(null);
			}
			else{
				if(checkInTaskInfo.getTaskType().equals(UserTaskType.TASK_TYPE_CHECKIN_DAILY)){
					receiveReward.setText("每  日  签  到");
				}
				else{
					receiveReward.setText("领  取  奖  励");
				}
				receiveReward.setBackgroundResource(R.drawable.common_btn_selector);
				receiveReward.setOnClickListener(this);
			}
		}
		
		public void setRecyclerView(List<CheckInItem> taskItems){
			LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,false);
			mItemRecyclerView.setLayoutManager(layoutManager);
			mItemAdapter = new CheckInTaskAdapter(mItemRecyclerView);
			mItemRecyclerView.setAdapter(mItemAdapter);
			mItemAdapter.setData(taskItems);
		}
		
		public TextView getTaskDesc() {
			return taskDesc;
		}
		
		public Button getReceiveReward() {
			return receiveReward;
		}
		
		@Override
		public void onClick(View v) {
			UserTaskState taskState = checkInTaskInfo.getTaskState();
			if(NetUtil.isNetworkAvailable(mContext, true)){
				if(DataFetcher.isUserLogin()){
					if(taskState == UserTaskState.TASK_STATE_FINISH){
						//该任务已完成
						NewToast.makeToast(mContext, "该任务已完成，奖励已领取", Toast.LENGTH_SHORT).show();
					} else if(taskState == UserTaskState.TASK_STATE_NOT_FINISH){
						//任务未完成
						NewToast.makeToast(mContext, "该任务还未完成", Toast.LENGTH_SHORT).show();
					} else if(taskState == UserTaskState.TASK_STATE_INVALID 
							|| taskState == UserTaskState.TASK_STATE_NOT_EXIST){
						//任务无效
						NewToast.makeToast(mContext, "该任务不存在", Toast.LENGTH_SHORT).show();
					}else if(taskState == UserTaskState.TASK_STATE_STANDBY){
						//任务已完成，去领取奖励
						receiveReward.setOnClickListener(null);
						mContext.btnView = receiveReward;
						mContext.mViewType = 1;
						mContext.myViewHolder = CheckInTaskViewHolder.this;
						mContext.obtainUserTaskReward(checkInTaskInfo);
					}
					else{
						NewToast.makeToast(mContext, "该任务不存在", Toast.LENGTH_SHORT).show();
					}
				}
				else{
					mContext.snapDialog();
				}
			}
			else{
				NewToast.makeToast(mContext, "网络未连接",
						Toast.LENGTH_SHORT).show();
			}
		}
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus){
				
				View vv = (View) v.getParent().getParent();
				int[] location = new int[2];
				vv.getLocationOnScreen(location);
				if(location[1]>mRecyclerView.getBottom()/2){
					mRecyclerView.smoothScrollBy(0, location[1] - mRecyclerView.getBottom()/2);
				}else if(location[1]<mRecyclerView.getBottom()/2){
					mRecyclerView.smoothScrollBy(0, location[1] - mRecyclerView.getBottom()/2);
				}
				if(v.isInTouchMode()){
					v.performClick();
				}
				
			}
			
		}
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
	}
	
	class ManyTaskViewHolder extends BaseViewHolder{

		public ManyTaskViewHolder(View itemView) {
			super(itemView);
			ScaleViewUtils.scaleView(itemView);
		}
	}
}
