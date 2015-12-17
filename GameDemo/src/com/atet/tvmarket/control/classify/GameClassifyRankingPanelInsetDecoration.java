package com.atet.tvmarket.control.classify;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.atet.tvmarket.R;

/**
 * ItemDecoration implementation that applies an inset margin
 * around each child of the RecyclerView. The inset value is controlled
 * by a dimension resource.
 */
public class GameClassifyRankingPanelInsetDecoration extends RecyclerView.ItemDecoration {

    private int mInsets_top,mInsets_right,mInsets_bottom,mInsets_left;

    public GameClassifyRankingPanelInsetDecoration(Context context) {
    	mInsets_top = context.getResources().getDimensionPixelSize(R.dimen.card_insets_top);
    	mInsets_right = context.getResources().getDimensionPixelSize(R.dimen.card_insets_right);
    	mInsets_bottom = context.getResources().getDimensionPixelSize(R.dimen.card_insets_bottom);
    	mInsets_left = context.getResources().getDimensionPixelSize(R.dimen.card_insets_left);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //We can supply forced insets for each item view here in the Rect
        outRect.set(0, 0, 0, 0);
    }
}