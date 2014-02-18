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
		//����ȫ��
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//��ʾ�Զ����SurfaceView��ͼ
		setContentView(new CanvasSfv(this));
	}
}