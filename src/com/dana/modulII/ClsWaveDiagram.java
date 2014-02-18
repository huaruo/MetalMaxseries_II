package com.dana.modulII;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Paint;
import android.view.SurfaceView;

public class ClsWaveDiagram
{
	//Debud
	private final static String TAG = "ClsWaveDiagram";
	
	private boolean isRecording = false;//线程控制标记
	private InputStream inStream = null;//数据输入流
	private DrawThread  mThread;
	
	public int rateX = 1; //X轴缩小的比例
	public int rateY = 1; //Y轴缩小的比例
	public int baseLine = 0; //Y轴基线
	
	private Context context;
	
	public ClsWaveDiagram(Context context, InputStream inStream,  int rateX, int rateY, int baseLine)
	{
		this.context = context;
		this.inStream = inStream;
		this.rateX = rateX;
		this.rateY = rateY;
		this.baseLine = baseLine;
	}
	
	public void Start(SurfaceView sfv, Paint mPaint, int wait)
	{
		isRecording = true;
		mThread = new DrawThread(context, inStream, sfv, mPaint, wait, rateX, rateY, baseLine,isRecording);
		mThread.start();
	}
	
	
	/**
	 * 停止
	 */
	public void Stop()
	{
		isRecording = false;
	}
}