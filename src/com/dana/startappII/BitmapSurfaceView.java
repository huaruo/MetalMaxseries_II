package com.dana.startappII;

import com.dana.modulII.BitmapSfv;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.app.Activity;

public class BitmapSurfaceView extends  Activity
{
	//Debug
	private final static String TAG = "BitmapSurfaceView";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//设置全屏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//显示自定义的SurfaceView视图
		setContentView(new BitmapSfv(this));
	}	
}