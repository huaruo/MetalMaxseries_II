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
		//����ȫ��
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//��ʾ�Զ����SurfaceViewͼ��
		setContentView(new PaintSfv(this));
	}
}