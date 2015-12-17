package com.atet.tvmarket.control.mygame.task;

import java.util.HashMap;
import android.content.Context;
import android.view.View;

/**
 * @author time：2012-8-13 上午10:48:51 description: 异步任务管理器
 */
public class TaskManager {
	Context context;

	/** 当前Activity异步任务列表 */
	private HashMap<Integer, BaseTask<?>> taskMap = new HashMap<Integer, BaseTask<?>>();

	public TaskManager(Context context) {
		this.context = context;
	}

	/**
	 * 
	 * @Title: startTask
	 * @Description: TODO 启动某个任务，同时添加这个任务，
	 * @param @param listener
	 * @param @param taskKey
	 * @return void
	 * @throws
	 */
	public <T> void startTask(AsynTaskListener<T> listener, Integer taskKey) {
		try {
			addTask(listener, taskKey).execute((Void) null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Title: addTask
	 * @Description: TODO 添加一个任务
	 * @param @param listener
	 * @param @param taskKey
	 * @param @return
	 * @return BaseTask<T>
	 * @throws
	 */

	public <T> void startTask(NewAsynTaskListener<T> listener, Integer taskKey,
			Integer position) {
		try {
			listener.setAsynPosition(position);
			addTask(listener, taskKey).execute((Void) null);
//			DebugTool.debug("TaskManager", "添加了一个异步任务");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public <T> void startTask(NewAsynTaskListener<T> listener, Integer taskKey,
			View view,Integer gameId) {
		try {
			listener.setIconView(view);
			listener.setGameId(gameId);
			addTask(listener, taskKey).execute((Void) null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	public <T> BaseTask<T> addTask(AsynTaskListener<T> listener, Integer taskKey) {

		BaseTask<T> taskInMap = (BaseTask<T>) taskMap.get(taskKey);
		if (taskInMap != null
				&& taskInMap.getStatus() == android.os.AsyncTask.Status.RUNNING) {
			return taskInMap;
		}

		taskInMap = new BaseTask<T>(context, listener, taskKey);
		taskMap.put(taskKey, taskInMap);

		return taskInMap;
	}

	/**
	 * 
	 * @Title: Exceute
	 * @Description: TODO 执行task
	 * @param @param taskKey
	 * @return void
	 * @throws
	 */
	public void Exceute(Integer taskKey) {
		BaseTask<?> taskInMap = taskMap.get(taskKey);
		if (taskInMap != null
				&& taskInMap.getStatus() != android.os.AsyncTask.Status.RUNNING) {
			taskInMap.execute((Void) null);
		}
	}

	/**
	 * 
	 * @Title: cancelTask
	 * @Description: TODO 取消某个任务
	 * @param @param taskKey
	 * @return void
	 * @throws
	 */
	public void cancelTask(Integer taskKey) {
		BaseTask<?> taskInMap = taskMap.get(taskKey);
		if (taskInMap != null
				&& taskInMap.getStatus() == android.os.AsyncTask.Status.RUNNING) {
			taskInMap.cancel(true);
			taskMap.remove(taskKey);
		}
	}

	/**
	 * 
	 * @Title: cancelAllTasks
	 * @Description: TODO 取消所有的任务
	 * @param
	 * @return void
	 * @throws
	 */
	public void cancelAllTasks() {
		if (taskMap != null && !taskMap.isEmpty()) {
			for (Integer taskKey : taskMap.keySet()) {
				cancelTask(taskKey);
			}
		}
	}

}
