package com.atet.tvmarket.view.recyclerview;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.atet.tvmarket.control.common.BaseFragment;

public abstract class RecyclerFragment extends BaseFragment{
	private RecyclerView mList;
	private BaseAdapter mAdapter;
	protected abstract RecyclerView  getRecyclerView();
	public abstract RecyclerView.LayoutManager getLayoutManager();
	public abstract RecyclerView.ItemDecoration getItemDecoration();
	public abstract BaseAdapter getAdapter();
	public abstract void setItemFocus();
	public abstract void smoothScrollToTop();
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
		mList = getRecyclerView();
		mList.setLayoutManager(getLayoutManager());
		mList.addItemDecoration(getItemDecoration());

		mList.getItemAnimator().setAddDuration(1000);
		mList.getItemAnimator().setChangeDuration(1000);
		mList.getItemAnimator().setMoveDuration(1000);
		mList.getItemAnimator().setRemoveDuration(1000);

		mAdapter = getAdapter();		
		mList.setAdapter(mAdapter);
    }
}
