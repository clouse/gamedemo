package com.atet.tvmarket.view;

import com.atet.tvmarket.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class FooterView extends RelativeLayout {
	
	private LinearLayout layoutB,layoutW;
	private ImageView footerlB,footerlW,footerxB,footerxW,footersB,footersW;
	int fvisible,cvisible;
	public FooterView(Context context){
		this(context, null);
	}
	
	public FooterView(Context context, AttributeSet attrs){
		super(context,attrs);
		
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.footerview);
		
		int N = a.getIndexCount();
		for(int i=0;i<N;i++){
			int attr = a.getIndex(i);
			if (attr == R.styleable.footerview_rootvisible) {
				fvisible = a.getInt(R.styleable.footerview_rootvisible, 0);
			} else if (attr == R.styleable.footerview_childvisible) {
				cvisible = a.getInt(R.styleable.footerview_childvisible, 0);
			} else {
			}
		}
		
		a.recycle();
		
		LayoutInflater.from(context).inflate(R.layout.footer_view, this, true);
		
		layoutB = (LinearLayout)findViewById(R.id.layout_b);
		layoutW = (LinearLayout)findViewById(R.id.layout_w);
		
		footerxB = (ImageView) findViewById(R.id.footerXB);
		footerxW = (ImageView) findViewById(R.id.footerXW);
		footerlB = (ImageView) findViewById(R.id.footerLB);
		footerlW = (ImageView) findViewById(R.id.footerLW);
		footersB = (ImageView) findViewById(R.id.footerSB);
		footersW = (ImageView) findViewById(R.id.footerSW);
		
		if(fvisible==0){
			layoutW.setVisibility(View.GONE);
			if(cvisible==1){
				footerlB.setVisibility(View.GONE);
			}
			else if(cvisible==2){
				footerlB.setVisibility(View.GONE);
				footerxB.setVisibility(View.GONE);
			}
			else if(cvisible==3){
				footerxB.setVisibility(View.GONE);
			}
			else  if(cvisible==4){
				footerlB.setVisibility(View.GONE);
				footerxB.setVisibility(View.GONE);
				footersB.setVisibility(View.GONE);
			}
		}
		else{
			layoutB.setVisibility(View.GONE);
			if(cvisible==1){
				footerlW.setVisibility(View.GONE);
			}
			else if(cvisible==2){
				footerlW.setVisibility(View.GONE);
				footerxW.setVisibility(View.GONE);
			}
			else if(cvisible==3){
				footerxW.setVisibility(View.GONE);
			}
			else if(cvisible==4){
				footerlW.setVisibility(View.GONE);
				footerxW.setVisibility(View.GONE);
				footersW.setVisibility(View.GONE);
			}
		}
	}
}
