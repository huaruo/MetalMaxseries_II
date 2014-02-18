package com.dana.startappII;

import com.dana.modulII.CanvasSfv;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class CanvasSurfaceView extends Activity
{
	//Debug
	private final static String TAG = "CanvasSurfaceView";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//设置全屏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//显示自定义的SurfaceView视图
		setContentView(new CanvasSfv(this));
	}
}