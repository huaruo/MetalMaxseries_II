package com.dana.startappII;

import com.dana.modulII.PaintSfv;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.app.Activity;

public class PaintSurfaceView extends Activity
{
	//Debug
	private final static String TAG = "PaintSurfaceView";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//设置全屏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//显示自定义的SurfaceView图案
		setContentView(new PaintSfv(this));
	}
}