package com.atet.tvmarket.control.gamerecommand.anim;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView.ItemAnimator;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.View;

public class TranslationRightIn extends ItemAnimator {

	@Override
	public boolean animateAdd(ViewHolder arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean animateChange(ViewHolder holder1, ViewHolder holder2, int arg2,
			int arg3, int arg4, int arg5) {
		holder1.itemView.setVisibility(View.INVISIBLE);
		ViewCompat.setTranslationX(holder2.itemView, holder2.itemView.getWidth());
		
		ViewCompat.animate(holder2.itemView).cancel();
	    ViewCompat.animate(holder2.itemView).translationX(0).translationY(0).setDuration(400).setListener(new ViewPropertyAnimatorListener() {
			
			@Override
			public void onAnimationStart(View view) {
				
			}
			
			@Override
			public void onAnimationEnd(View view) {
				Log.i("life",view.getTag().toString());
				int pos = (Integer) view.getTag();
				if(pos==0){
					 ViewCompat.animate(view).scaleX(1.1f).scaleY(1.1f);
				}
			}
			
			@Override
			public void onAnimationCancel(View view) {
				
			}
		});
		
		return true;
	}

	@Override
	public boolean animateMove(ViewHolder arg0, int arg1, int arg2, int arg3,
			int arg4) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean animateRemove(ViewHolder arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void endAnimation(ViewHolder arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endAnimations() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void runPendingAnimations() {
		// TODO Auto-generated method stub

	}

}
