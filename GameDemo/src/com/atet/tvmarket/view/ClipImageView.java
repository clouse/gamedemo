package com.atet.tvmarket.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ClipImageView extends ImageView {

	private Bitmap bt;

	public ClipImageView(Context context) {
		super(context);
	}

	public ClipImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ClipImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		// 先回收之前的bitmap
		if (null != bt && !bt.isRecycled()) {
			bt.recycle();
			bt = null;
		}
		this.bt = bm;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// 双缓存，先把图片绘制在bitmap上，然后再绘制到canvas上面
		if (null != bt) {
			canvas.drawBitmap(bt, 0, 0, new Paint());
		}
	}
}
