package com.atet.tvmarket.control.mygame.task;

import android.view.View;

public class NewAsynTaskListener<T> implements AsynTaskListener<T>{
    
	public volatile Integer asynPosition; 
	public View iconView;
	public  Integer gameId;
	
	public boolean preExecute(BaseTask<T> task, Integer taskKey) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onResult(Integer taskKey, TaskResult<T> result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TaskResult<T> doTaskInBackground(Integer taskKey) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setAsynPosition(Integer asynPosition){
		this.asynPosition=asynPosition;
	}
	
	public void setIconView(View iconView){
		this.iconView = iconView;
	}
	
	public void setGameId(Integer gameId){
		this.gameId = gameId;
	}

}
