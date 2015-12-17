package com.atet.statistics.utils;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class DemoGLSurfaceView extends GLSurfaceView {  
  
    DemoRenderer mRenderer;  
    public DemoGLSurfaceView(Context context) {  
        super(context);  
        setEGLConfigChooser(8, 8, 8, 8, 0, 0);  
        mRenderer = new DemoRenderer();  
        setRenderer(mRenderer);  
    }
	public DemoGLSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setEGLConfigChooser(8, 8, 8, 8, 0, 0);  
        mRenderer = new DemoRenderer();  
        setRenderer(mRenderer);  
	}  
   
    
}  