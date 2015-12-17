package com.atet.tvmarket.control.classify.special.anim;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView.ItemAnimator;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

public class AlphaInItemAnimator extends ItemAnimator {

	@Override
	public boolean animateAdd(ViewHolder holder) 
	{
		ViewCompat.setAlpha(holder.itemView, 0);
        
		ViewCompat.animate(holder.itemView).cancel();
        ViewCompat.animate(holder.itemView).alpha(1f).setDuration(400).setListener(new ViewPropertyAnimatorListener() {
			
			@Override
			public void onAnimationStart(View arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(View arg0) {
				dispatchAnimationsFinished();
			}
			
			@Override
			public void onAnimationCancel(View view) {
				
			}
		}).start();
		return true;
	}

	@Override
	public boolean animateChange(ViewHolder arg0, ViewHolder arg1, int arg2,
			int arg3, int arg4, int arg5) {
		return false;
	}

	@Override
	public boolean animateMove(ViewHolder arg0, int arg1, int arg2, int arg3,
			int arg4) {
		
		return false;
	}

	@Override
	public boolean animateRemove(ViewHolder arg0) {
		
		return false;
	}

	@Override
	public void endAnimation(ViewHolder arg0) {
		
	}

	@Override
	public void endAnimations() {

	}

	@Override
	public boolean isRunning() {
		return false;
	}

	@Override
	public void runPendingAnimations() {
		
	}

}
