package com.atet.tvmarket.control.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.control.mine.MineAccountManagerActivity;
import com.atet.tvmarket.control.task.TaskAdapter.CheckInTaskViewHolder;
import com.atet.tvmarket.control.task.TaskAdapter.SingleTaskViewHolder;
import com.atet.tvmarket.entity.UserInfo;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.model.usertask.CheckInTaskInfo;
import com.atet.tvmarket.model.usertask.UserTask;
import com.atet.tvmarket.model.usertask.UserTaskInfo;
import com.atet.tvmarket.model.usertask.UserTaskInfo.UserTaskState;
import com.atet.tvmarket.model.usertask.UserTaskInfo.UserTaskType;
import com.atet.tvmarket.utils.GamepadTool;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.view.CommonProgressDialog;
import com.atet.tvmarket.view.LoadingView;
import com.atet.tvmarket.view.NewToast;
import com.atet.tvmarket.view.recyclerview.BaseViewHolder;
import com.atet.tvmarket.view.recyclerview.RecyclerFragment;
/**
 * 任务专区
 * @author liuht
 *
 */
public class TaskActivity extends BaseActivity implements OnCheckedChangeListener,OnFocusChangeListener,OnClickListener{
	ALog alog = ALog.getLogger(TaskActivity.class);
	private TextView title;
	private RadioGroup mRadioGroup;
	private RadioButton routine,daily;
	private FrameLayout content;
	
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	private RecyclerFragment routineFrag,dailyFrag,activeFrag,rechargeFrag;
	private List<RecyclerFragment> fragments = new ArrayList<RecyclerFragment>();
	private View lastFocusView=null;
	private int location=0;
	private LoadingView loadingView;
	private ImageView loading;
	private AnimationDrawable mAnimationDrawable;
	
	public List<UserTask> userTaskList;
	private Map<UserTaskType, UserTaskInfo> daliyUserTaskInfoMap,achievementUserTaskInfoMap;
	public List<TaskItem> routineTaskItems = new ArrayList<TaskItem>();
	public List<TaskItem> dailyTaskItems = new ArrayList<TaskItem>();
	
	private boolean isClose = true;
	public int mViewType = -1;
	public Button btnView = null;
	public BaseViewHolder myViewHolder;
	public long curtime = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		setContentView(R.layout.activity_task);
		ScaleViewUtils.init(this);
		ScaleViewUtils.scaleView(getWindow().getDecorView());
		setBlackTitle(false);
		
		title = (TextView)findViewById(R.id.tv_title);
		
		mRadioGroup = (RadioGroup)findViewById(R.id.rg_task);
		mRadioGroup.setOnCheckedChangeListener(this);
		
		routine = (RadioButton)findViewById(R.id.rb_routine);
		daily = (RadioButton)findViewById(R.id.rb_daily);
		/*active = (RadioButton)findViewById(R.id.rb_active);
		recharge = (RadioButton)findViewById(R.id.rb_recharge);*/
		content = (FrameLayout)findViewById(R.id.fl_content);
		loadingView = (LoadingView)findViewById(R.id.contentLoading);
		loadingView.setDataView(content);
		
		loading = (ImageView)findViewById(R.id.iv_loading);
		mAnimationDrawable = (AnimationDrawable)loading.getDrawable();
		
		routine.setOnFocusChangeListener(this);
		daily.setOnFocusChangeListener(this);
		/*active.setOnFocusChangeListener(this);
		recharge.setOnFocusChangeListener(this);*/
		
		routine.setOnClickListener(this);
		daily.setOnClickListener(this);
		/*active.setOnClickListener(this);
		recharge.setOnClickListener(this);*/
		//最后一个radiobutton的向右焦点设为自己
		daily.setNextFocusRightId(daily.getId());
		
		setTitleData();
		getUserTask();
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if(arg0==9001 && arg1==8001){
			setTitleData();
			getUserTask();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setTitleData();
		isClose = true;
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if(isClose){
			routine.requestFocusFromTouch();
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
	}
	
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		fragmentManager = getSupportFragmentManager();
		for(RecyclerFragment fragment:fragments){
			fragmentTransaction = fragmentManager.beginTransaction();
			if(fragment.isVisible()){
				fragmentTransaction.hide(fragment).commitAllowingStateLoss();
			}
		}
		
		if (checkedId == R.id.rb_routine) {
			location=0;
			fragmentTransaction = fragmentManager.beginTransaction();
			if(routineFrag==null){
				routineFrag = new RoutineFragment().newInstance();
				fragments.add(routineFrag);
				fragmentTransaction.add(R.id.fl_content, routineFrag).commitAllowingStateLoss();
			}
			else{
				fragmentTransaction.show(routineFrag).commitAllowingStateLoss();
			}
			routineFrag.smoothScrollToTop();
		} else if (checkedId == R.id.rb_daily) {
			location=1;
			fragmentTransaction = fragmentManager.beginTransaction();
			if(dailyFrag==null){
				dailyFrag = new DailyFragment().newInstance();
				fragments.add(dailyFrag);
				fragmentTransaction.add(R.id.fl_content, dailyFrag).commitAllowingStateLoss();
			}
			else{
				fragmentTransaction.show(dailyFrag).commitAllowingStateLoss();
			}
			dailyFrag.smoothScrollToTop();
		} 
		
	}

	
	
	private void getUserTask(){
		ReqCallback<List<UserTask>> reqCallback = new ReqCallback<List<UserTask>>(){
			@Override
			public void onGetCacheData(String requestTag, boolean result) {
				if(!result){
					loadingView.getmHandler().sendEmptyMessage(Constant.SHOWLOADING);
				}
			}
			
			@Override
			public void onResult(TaskResult<List<UserTask>> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					userTaskList = taskResult.getData();
					if(userTaskList!=null && userTaskList.size()>0){
						for(int i=0;i<userTaskList.size();i++){
							if(i==0){
								daliyUserTaskInfoMap = userTaskList.get(0).getUserTaskInfoMap();
							}
							if(i==1){
								achievementUserTaskInfoMap = userTaskList.get(1).getUserTaskInfoMap();
							}
						}
					}
					/*for(Map.Entry<UserTaskType, UserTaskInfo> entry:daliyUserTaskInfoMap.entrySet()){
						UserTaskType userTaskType = entry.getKey();
						UserTaskInfo userTaskInfo = entry.getValue();
						if(!userTaskInfo.haveSubTaskInfos()){
							alog.info("类型："+userTaskType);
							alog.info("    任务："+userTaskInfo.getTaskName());
							alog.info("    描述："+userTaskInfo.getTaskRemark());
							alog.info("    状态："+userTaskInfo.getTaskState());
							alog.info("    ================================================");
						}
						else{
							//具有子任务的任务,遍历子任务
							alog.info("子类型："+userTaskType);
							alog.info("    任务："+userTaskInfo.getTaskName());
							alog.info("    描述："+userTaskInfo.getTaskRemark());
							alog.info("    状态："+userTaskInfo.getTaskState());
							for (UserTaskInfo info : userTaskInfo.getSubTaskInfos()) {
								alog.info("    子任务类型："+info.getTaskType());
								alog.info("    子任务描述："+info.getTaskRemark());
								alog.info("    子任务状态："+info.getTaskState());
								alog.info("    ================================================");
							}
						}
						
					}
					
					for(Map.Entry<UserTaskType, UserTaskInfo> entry:achievementUserTaskInfoMap.entrySet()){
						UserTaskType userTaskType = entry.getKey();
						UserTaskInfo userTaskInfo = entry.getValue();
						if(!userTaskInfo.haveSubTaskInfos()){
							alog.info("成就类型："+userTaskType);
							alog.info("    任务："+userTaskInfo.getTaskName());
							alog.info("    描述："+userTaskInfo.getTaskRemark());
							alog.info("    状态："+userTaskInfo.getTaskState());
							alog.info("    ================================================");
						}
						else{
							//具有子任务的任务,遍历子任务
							alog.info("成就子类型："+userTaskType);
							alog.info("    任务："+userTaskInfo.getTaskName());
							alog.info("    描述："+userTaskInfo.getTaskRemark());
							alog.info("    状态："+userTaskInfo.getTaskState());
							for (UserTaskInfo info : userTaskInfo.getSubTaskInfos()) {
								alog.info("    子任务类型："+info.getTaskType());
								alog.info("    子任务描述："+info.getTaskRemark());
								alog.info("    子任务状态："+info.getTaskState());
								alog.info("    ================================================");
							}
						}
						
					}
					
					//通过类型直接获取指定的任务
					//获取签到任务，与其它任务稍有不同，可以获取一周签到状态
					CheckInTaskInfo checkInTaskInfo=(CheckInTaskInfo)userTaskList.get(0).getUserTaskInfo(UserTaskType.TASK_TYPE_CHECKIN_DAILY);
					if(checkInTaskInfo!=null){
						//获取任务状态
						UserTaskState taskState = checkInTaskInfo.getTaskState();
						if(taskState != UserTaskState.TASK_STATE_INVALID 
								&& taskState != UserTaskState.TASK_STATE_NOT_EXIST){
							//任务有效
							
							//获取7天签到状态
							//第1天是否签到
							checkInTaskInfo.isCheckIn(CheckInTaskInfo.MON);
							//...其它5天
							//第7天是否签到
							checkInTaskInfo.isCheckIn(CheckInTaskInfo.SUN);
							
							alog.info("================================================");
							alog.info("签到任务的7天签到状态");
							for (int i = 1; i < 8; i++) {
								boolean isSign = checkInTaskInfo.isCheckIn(i);
								alog.info("    第"+i+"天 签到状态："+isSign);
							}
							
							alog.info("    今天签到状态："+taskState);
							if(taskState == UserTaskState.TASK_STATE_STANDBY){
								//今天未签到,去签到领取奖励
//								obtainUserTaskReward(checkInTaskInfo);
							}
						} 
					}
					
					//获取大厅连续登录任务
					checkInTaskInfo=(CheckInTaskInfo)userTaskList.get(0).getUserTaskInfo(UserTaskType.TASK_TYPE_MARKET_LOGIN);
					if(checkInTaskInfo!=null){
						//获取任务状态
						UserTaskState taskState = checkInTaskInfo.getTaskState();
						if(taskState != UserTaskState.TASK_STATE_INVALID 
								&& taskState != UserTaskState.TASK_STATE_NOT_EXIST){
							//任务有效
							
							//获取一周登录状态
							//周一是否登录
							checkInTaskInfo.isCheckIn(CheckInTaskInfo.MON);
							//...其它5天
							//周日是否签到
							checkInTaskInfo.isCheckIn(CheckInTaskInfo.SUN);
							
							alog.info("================================================");
							alog.info("登录任务的一周登录状态");
							for (int i = 1; i < 8; i++) {
								boolean isSign = checkInTaskInfo.isCheckIn(i);
								alog.info("    第"+i+"天 登录状态："+isSign);
							}
							
							alog.info("    今天登录状态："+taskState);
							if(taskState == UserTaskState.TASK_STATE_STANDBY){
								//今天未签到,去签到领取奖励
//								obtainUserTaskReward(checkInTaskInfo);
							}
						} 
					}*/
					getDatas();
					loadingView.getmHandler().sendEmptyMessage(Constant.DISMISSLOADING);
				}

				else{
					if(NetUtil.isNetworkAvailable(TaskActivity.this, true)){
						loadingView.getmHandler().sendEmptyMessage(Constant.NULLDATA);
					}
					else{
						loadingView.getmHandler().sendEmptyMessage(Constant.EXCEPTION);
					}
				}

			}
		};
		DataFetcher.getUserTask2(this, reqCallback, false).request(this);

	}
	
	
	/**
	 * @description: 获取任务奖励
	 * 
	 * @throws: 
	 * @author: LiuQin
	 * @date: 2015年7月22日 下午6:16:58
	 */
	public void obtainUserTaskReward(UserTaskInfo userTaskInfo) {
		
		alog.debug("");
		UserTaskState taskState = userTaskInfo.getTaskState();
		mHandler.sendEmptyMessage(4);
		//已完成任务，去领取奖励
		
		//任务名称
		String taskName = userTaskInfo.getTaskName();
		//任务描述
		String taskRemark = userTaskInfo.getTaskRemark();
		alog.info("================================================");
		alog.info("任务："+taskName);
		alog.info("    描述："+taskRemark);
		alog.info("    状态："+taskState);

		alog.info("获取任务奖励");
		ReqCallback<UserTaskInfo> reqCallback = new ReqCallback<UserTaskInfo>() {
			@Override
			public void onResult(TaskResult<UserTaskInfo> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					//领取奖励成功，UserTaskInfo状态会被更新，需要刷新此任务界面
					UserTaskInfo userTaskInfo = taskResult.getData();
//					UserInfo userInfo = BaseApplication.userInfo;
//					alog.info("领取奖励的任务："+userTaskInfo.getTaskName());
//					alog.info("    状态："+userTaskInfo.getTaskState());
//					if(userTaskInfo.getIntegral()>0){
//						alog.info("    领取的积分："+userTaskInfo.getIntegral());
//						userInfo.setIntegral(userInfo.getIntegral()+userTaskInfo.getIntegral());
//					}
//					if(userTaskInfo.getCoupon()>0){
//						alog.info("    领取的A券："+userTaskInfo.getCoupon());
//						userInfo.setIntegral(userInfo.getCoupon()+userTaskInfo.getCoupon());
//					}
//					BaseApplication.userInfo = userInfo;
					
					Message msg = Message.obtain();
					msg.obj = userTaskInfo;
					msg.what = 5;
					mHandler.sendMessage(msg);
					
				} else {
					//领取奖励失败
					String errMsg = taskResult.getMsg();
					alog.info("errMsg:"+errMsg);
					
					Message msg = Message.obtain();
					msg.obj = errMsg;
					msg.what = 6;
					mHandler.sendMessage(msg);
				}
			}
		};
		userTaskInfo.obtainReward(this, reqCallback, false);
	}

	public void snapDialog() {
		CommonProgressDialog mDialog = new CommonProgressDialog.Builder(this)
			.setMessage(R.string.login_or_out)
			.setPositiveButton(R.string.ok_login, new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					isClose = false;
					Intent intent = new Intent(TaskActivity.this,MineAccountManagerActivity.class);
					startActivityForResult(intent, 9001);
				}
				
			})
			.setNegativeButton(R.string.cancle_login, null)
			.create(); 
		mDialog.setParams(mDialog);
		mDialog.show();
		
	}
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 4:
				loading.setVisibility(View.VISIBLE);
				mAnimationDrawable.start();
				break;
			case 5:
				mAnimationDrawable.stop();
				loading.setVisibility(View.INVISIBLE);
				UserTaskInfo userTaskInfo = (UserTaskInfo) msg.obj;
				NewToast.makeToast(TaskActivity.this, "领取奖励成功", Toast.LENGTH_SHORT).show();
				setTitleData();
				setButtonBg(userTaskInfo);
				break;
			case 6:
				mAnimationDrawable.stop();
				loading.setVisibility(View.INVISIBLE);
				if(NetUtil.isNetworkAvailable(TaskActivity.this, true)){
					String errMsg = msg.obj.toString();
					NewToast.makeToast(TaskActivity.this, errMsg, Toast.LENGTH_SHORT).show();
				}
				else{
					NewToast.makeToast(TaskActivity.this, "网络未连接",
							Toast.LENGTH_SHORT).show();
				}
				btnView.setOnClickListener((OnClickListener) myViewHolder);
				break;
			case 7:
				if(routineFrag!=null){
					((TaskAdapter)routineFrag.getAdapter()).setData(routineTaskItems);
					routineFrag.smoothScrollToTop();
				}
				if(dailyFrag!=null){
					((TaskAdapter)dailyFrag.getAdapter()).setData(dailyTaskItems);
					dailyFrag.smoothScrollToTop();
				}
				break;
			default:
				break;
			}
		}
		
	};
	
	public void setButtonBg(UserTaskInfo userTaskInfo){
		if(btnView!=null){
			btnView.setText("已  领  取");
			btnView.setBackgroundResource(R.drawable.common_btn_gary_selector);
			btnView.setOnClickListener((OnClickListener) myViewHolder);
			if(mViewType==1){
				CheckInTaskInfo taskInfo = (CheckInTaskInfo) userTaskInfo;
				CheckInTaskViewHolder checkInTask = (CheckInTaskViewHolder) myViewHolder;
				checkInTask.setCheckInTaskInfo(taskInfo);
				List<CheckInItem> checkInItems = new ArrayList<CheckInItem>();
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
			}
			
		}
	}
	
	public void getDatas(){
		routineTaskItems.clear();
		dailyTaskItems.clear();
		if(daliyUserTaskInfoMap!=null && daliyUserTaskInfoMap.size()>0){
			for(Map.Entry<UserTaskType, UserTaskInfo> entry:daliyUserTaskInfoMap.entrySet()){
				UserTaskType userTaskType = entry.getKey();
				UserTaskInfo userTaskInfo = entry.getValue();
				UserTaskState taskState = userTaskInfo.getTaskState();
				if(taskState != UserTaskState.TASK_STATE_INVALID 
						&& taskState != UserTaskState.TASK_STATE_NOT_EXIST){
					if(userTaskType.equals(UserTaskType.TASK_TYPE_MARKET_LOGIN)){
						routineTaskItems.add(new TaskItem(1, userTaskInfo, userTaskType, "领  取  奖  励"));
					}else if(userTaskType.equals(UserTaskType.TASK_TYPE_CHECKIN_DAILY)){
						routineTaskItems.add(new TaskItem(1, userTaskInfo, userTaskType, "每  日  签  到"));
					}else if(userTaskType.equals(UserTaskType.TASK_TYPE_WATCH_VIDEO)){
						routineTaskItems.add(new TaskItem(2, userTaskInfo, userTaskType, "领  取  奖  励"));
					}else if(userTaskType.equals(UserTaskType.TASK_TYPE_GAME_DOWNLOAD)){
						routineTaskItems.add(new TaskItem(2, userTaskInfo, userTaskType, "领  取  奖  励"));
					}else if(userTaskType.equals(UserTaskType.TASK_TYPE_GAME_ONLINE)){
						routineTaskItems.add(new TaskItem(2, userTaskInfo, userTaskType, "领  取  奖  励"));
					}else{
						routineTaskItems.add(new TaskItem(0, userTaskInfo, userTaskType, "领  取  奖  励"));
					}
					
				}
			}
		}
		
		if(achievementUserTaskInfoMap!=null && achievementUserTaskInfoMap.size()>0){
			for(Map.Entry<UserTaskType, UserTaskInfo> entry:achievementUserTaskInfoMap.entrySet()){
				UserTaskType userTaskType = entry.getKey();
				UserTaskInfo userTaskInfo = entry.getValue();
				UserTaskState taskState = userTaskInfo.getTaskState();
				if(taskState != UserTaskState.TASK_STATE_INVALID 
						&& taskState != UserTaskState.TASK_STATE_NOT_EXIST){
					dailyTaskItems.add(new TaskItem(0, userTaskInfo, userTaskType, "领  取  奖  励"));
				}
			}
		}
		mHandler.sendEmptyMessage(7);
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus){
			lastFocusView = getCurrentFocus();
			if(v.isInTouchMode()){
				v.performClick();
			}
			((RadioButton)v).setChecked(true);
		}
	}

	@Override
	public void onClick(View v) {
		
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if(event.getAction()==KeyEvent.ACTION_DOWN ){
			int keyCode = event.getKeyCode();
			if(lastFocusView!=null && lastFocusView instanceof RadioButton && GamepadTool.isDirectionDown(keyCode)){
				switch (location) {
				case 0:
					if(routineFrag!=null){
						routineFrag.setItemFocus();
					}
					break;
				case 1:
					if(dailyFrag!=null){
						dailyFrag.setItemFocus();
					}
					break;
				default:
					break;
				}
				
				return true;
			}
			else if(lastFocusView!=null && lastFocusView instanceof RadioButton && GamepadTool.isDirectionLeft(keyCode)){
				snapToPreviesScreen(getCurrentFocusTab());
				return true;
			}
			else if(lastFocusView!=null && lastFocusView instanceof RadioButton && GamepadTool.isDirectionRight(keyCode)){
				snapToNextScreen(getCurrentFocusTab());
				return true;
			}
			else if(GamepadTool.isButtonR2(keyCode)){
				snapToNextScreen(getCurrentFocusTab());
				return true;
			}
			else if(GamepadTool.isButtonL2(keyCode)){
				snapToPreviesScreen(getCurrentFocusTab());
				return true;
			}
			else if(GamepadTool.isButtonX(keyCode)|| keyCode == KeyEvent.KEYCODE_MENU){
				if(location==0){
					routine.requestFocusFromTouch();
				}
				else{
					daily.requestFocusFromTouch();
				}
				//refFlag = location;
				getUserTask();
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	private View getCurrentFocusTab(){
		if(routine.isChecked()){
			return routine;
		}
		else{
			return daily;
		}
	}
	
	private void snapToPreviesScreen(View v) {
		if(v==routine){
			daily.requestFocusFromTouch();
		}
		else if(v==daily){
			routine.requestFocusFromTouch();
		}
	}
	
	private void snapToNextScreen(View v) {
		if(v==routine){
			daily.requestFocusFromTouch();
		}
		else if(v==daily){
			routine.requestFocusFromTouch();
		}
	}
	
	public View getLastFocusView() {
		return lastFocusView;
	}

	public void setLastFocusView(View lastFocusView) {
		this.lastFocusView = lastFocusView;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}
	
	public interface OnRecevicedListener{
		public void onRecevice(Button button);
	}
}
