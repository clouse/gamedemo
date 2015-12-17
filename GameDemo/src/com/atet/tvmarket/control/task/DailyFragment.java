package com.atet.tvmarket.control.task;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.atet.tvmarket.R;
import com.atet.tvmarket.control.common.BaseFragment;
import com.atet.tvmarket.model.usertask.UserTaskInfo.UserTaskType;
import com.atet.tvmarket.view.recyclerview.BaseAdapter;
import com.atet.tvmarket.view.recyclerview.BaseBean;
import com.atet.tvmarket.view.recyclerview.RecyclerFragment;

public class DailyFragment extends RecyclerFragment {
	private TaskActivity context;
	private RecyclerView mRecyclerView;
	private TaskAdapter mAdapter;
	public DailyFragment newInstance(){
		DailyFragment fragment = new DailyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		context = (TaskActivity) getActivity();
		View root = inflater.from(context).inflate(R.layout.fragment_task, container, false);
		mRecyclerView = (RecyclerView) root.findViewById(R.id.rv_task_list);
		mAdapter =  new TaskAdapter(mRecyclerView,context,1);
		mRecyclerView.setAdapter(mAdapter);
		mAdapter.setData(context.dailyTaskItems);
		return root;
	}
	
	@Override
	protected RecyclerView getRecyclerView() {
		return mRecyclerView;
	}

	@Override
	public LayoutManager getLayoutManager() {
		return new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
	}

	@Override
	public ItemDecoration getItemDecoration() {
		// TODO Auto-generated method stub
		return new ItemDivider(context, R.drawable.task_many_divider);
	}

	@Override
	public BaseAdapter getAdapter() {
		if(mAdapter==null){
			mAdapter =  new TaskAdapter(mRecyclerView,context,1);
		}
		return mAdapter;
	}

	@Override
	public void setItemFocus() {
		if(mRecyclerView!=null && mRecyclerView.getChildCount()>0){
			View view  = mRecyclerView.getLayoutManager().findViewByPosition(0);
			if(view!=null){
				view.findViewById(R.id.btn_receive).requestFocus();
				context.setLastFocusView(null);
			}
		}
	}
	
	public void smoothScrollToTop(){
		if(mRecyclerView!=null && mRecyclerView.getChildCount()>0){
			mRecyclerView.smoothScrollToPosition(0);
		}
	}
}
