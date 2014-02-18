package com.dana.modulII;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceView;

/**
 * 负责绘制inBuf中数据
 * @author Huaruo.W
 *
 */
public class DrawThread extends Thread
{
	//Debug
	private final static String TAG = "DrawThread";
	
	private int oldX = 0; //上次绘制的X坐标
	private int oldY = 0;//上次绘制
	private SurfaceView sfv; //画板 
	public int X_index = 0;//当前画图所在屏幕X轴的坐标坐标
	private Paint mPaint; //画笔
	private int wait = 50;  //线程等待时间
	
	private Context context;
	private InputStream inStream;
		
	private int rateX = 0;
	private int rateY = 0;
	private int baseLine = 0;
	private boolean isRecording = false;
	
	public DrawThread(Context context, InputStream inStream, SurfaceView sfv, Paint mPaint, int wait,int rateX, int rateY, int baseLine, boolean isRecording)
	{
		this.context = context;
		this.sfv  = sfv;
		this.mPaint= mPaint;
		this.wait =wait;
		this.inStream= inStream;
		this.rateX = rateX;
		this.isRecording = isRecording;
	}
	
	public void run()
	{	
		while(isRecording)
		{
			try {
				byte[] temp = new byte[1024];
				int len = inStream.read(temp);
				Log.e(TAG, String .valueOf(len));
				
				if(len > 0)
				{
					byte[] buf = new byte[len];
					System.arraycopy(temp, 0 , buf, 0, buf.length);
					SimpleDraw(X_index, buf, rateX, rateY, baseLine);//把缓冲区数据画出来
					X_index = X_index + (buf.length/rateX) - 1; //这里-1可以减少空隙
					if(X_index > sfv.getHeight())
					{
						X_index =0;
					}
				}
				Thread.sleep(wait);//延时一定时间缓冲数据
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 绘制指定区域
	 * @param start  X轴开始的位置（全屏）
	 * @param inputBuf 缓冲区
	 * @param rateX X轴数据缩小的比例
	 * @param rateY 轴缩小的比例
	 * @param baseLine
	 */
	private void SimpleDraw(int start, byte[] inputBuf, int rateX, int rateY, int baseLine)
	{
		if(start ==0)
			oldX = 0;
		//根据需要缩小X轴比例
		byte[]  buffer = new byte[inputBuf.length / rateX];
		for(int  i = 0, ii =0; i<buffer.length; i++, ii = i*rateX)
			buffer[i] = inputBuf[ii];
		Canvas canvas = sfv.getHolder().lockCanvas(new Rect(0, start, sfv.getWidth(), start + buffer.length)); //关键：获取画布
		canvas.drawColor(Color.BLACK); //清除背景
		
		for(int i = 0; i< buffer.length; i++)
		{
			int y = i+start;
			int x = (0xFF - (buffer[i] & 0xFF)) / rateY +baseLine; //调节缩小比例。调节基准线。 0xFF-用于翻转， &0xFF用于把byte类型的负数取值转为正
			canvas.drawLine(oldX, oldY, x, y, mPaint);
			oldX = x; 
			oldY = y;
		}
		sfv.getHolder().unlockCanvasAndPost(canvas);//解锁画布，提交画好的图像
	}
	
	
}

