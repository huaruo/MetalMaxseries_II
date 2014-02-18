package com.dana.startappII;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.dana.modulII.ClsWaveDiagram;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class WaveDiagram extends Activity
{
	//Debug
	private final static String TAG = "WaveDiagram";
	
	private SurfaceView sfvWave;
	private  Paint mPaint;
	
	private ClsWaveDiagram clsWaveDiagram;
	private InputStream inStream;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wavediagram);
		
		sfvWave = (SurfaceView) findViewById(R.id.wavediagram_sfv);
		sfvWave.setOnTouchListener(touchListener);
		
		mPaint  = new Paint();
		mPaint.setColor(Color.GREEN);//…Ë÷√ª≠± Œ™¬Ã…´
		mPaint.setStrokeWidth(2); //…Ë÷√ª≠± ¥÷œ∏
		
//		inStream = new ByteArrayInputStream("ABCgfdsfver33f".getBytes());
		try {
			inStream = new ByteArrayInputStream("ABCgfdsfver33f".getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		clsWaveDiagram = new ClsWaveDiagram(WaveDiagram.this, inStream, 3, 1, sfvWave.getWidth()/2);//∫·œÚªÊ÷∆≤®–ŒÕº
		clsWaveDiagram.Start(sfvWave, mPaint, 80);
		Log.e(TAG,"start");
	}
	
	@Override
	public void onDestroy()
	{
		clsWaveDiagram.Stop();
		super.onDestroy();
	}
	
	private OnTouchListener touchListener = new OnTouchListener()
	{
		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			clsWaveDiagram.baseLine = (int)event.getX();
			return true;
		}
		
	};
}