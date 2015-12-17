package com.atet.tvmarket.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.atet.common.logging.ALog;

public class MineLayoutView extends RelativeLayout {
	private ALog alog = ALog.getLogger(MineLayoutView.class);

	public MineLayoutView(Context context) {
		this(context, null);
		setChildrenDrawingOrderEnabled(true);
	}

	public MineLayoutView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		setChildrenDrawingOrderEnabled(true);
	}

	public MineLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setChildrenDrawingOrderEnabled(true);
	}

	/**
	 * 处理视图的叠加效果
	 */
	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		alog.info("childCount=" + childCount);
//		NewToast.makeToast(getContext(), "childCount=" + childCount, 3000).show();
		View child = getFocusedChild();
		// alog.info("child:" + child.toString());
		if (child != null) {
			alog.info("child:" + child.toString());
//			NewToast.makeToast(getContext(), "child:" + child.toString(), 3000)
//					.show();
			int index = indexOfChild(child);
			alog.info("index:" + index);
//			NewToast.makeToast(getContext(), "i3=" + i, 3000).show();
			if (index != -1) {
				if (i < index) {
					alog.info("i1=" + i);
//					NewToast.makeToast(getContext(), "i1=" + i, 3000).show();
					return i;
				} else {
					alog.info("i2=" + i);
//					NewToast.makeToast(getContext(), "i2=" + i, 3000).show();
					return childCount - 1 - i + index;
				}
			}
		} else {
			alog.info("i3=" + i);
//			NewToast.makeToast(getContext(), "i3=" + i, 3000).show();
			return i;
		}
		return super.getChildDrawingOrder(childCount, i);
	}
}
