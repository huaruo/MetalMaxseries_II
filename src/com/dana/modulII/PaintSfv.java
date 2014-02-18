/**
 * @author Huaruo.W
 */
package com.dana.modulII;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class PaintSfv extends SurfaceView
{
	//用于控制SurfaceView
	private SurfaceHolder sfh;
	//声明一条线程
	private Thread th;
	//线程消亡的标识位
	private boolean flag;
	//声明一个画布
	private Canvas canvas;
	//声明屏幕的宽高
	private static int screenW,screenH;
	
	/**
	 * SurfaceView初始化函数
	 */
	public PaintSfv(Context c)
	{
		super(c);
		//实例SurfaceHolder
		sfh = this.getHolder();
		//为SurfaceView添加状态监听
		sfh.addCallback(callback);
		//设置焦点
		setFocusable(true);
	}
	
	
	private Callback callback = new Callback()
	{
		/**
		 * SurfaceView视图创建，响应此函数
		 */
		@Override
		public void surfaceCreated(SurfaceHolder holder)
		{
			screenW = PaintSfv.this.getWidth();
			screenH = PaintSfv.this.getHeight();
			flag = true;
			//实例线程
			th = new Thread(runn);
			//启动线程
			th.start();
		}
		/**
		 * SurfaceView视图状态发生改变，响应此函数
		 */
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
		{
			
		}
		/**
		 * SurfaceView视图消亡时，响应此函数
		 */
		@Override
		public void surfaceDestroyed(SurfaceHolder holder)
		{
			flag = false;
		}
	};
	
	/**
	 * 游戏绘图
	 */
	public void myDraw()
	{
		try
		{
			canvas = sfh.lockCanvas();
			if(canvas !=null)
			{
				canvas.drawColor(Color.WHITE);
				//----设置画笔无锯齿
				Paint paint1 = new Paint();
				canvas.drawCircle(40, 30, 20, paint1);
				paint1.setAntiAlias(true);
				canvas.drawCircle(100, 30, 20, paint1);
				//----设置画笔的透明度
				canvas.drawText("无透明度", 100, 70, new Paint());
				Paint paint2 = new Paint();
				paint2.setAlpha(0x77);
				canvas.drawText("半透明度", 20, 70, paint2);
				//----设置绘制文本的锚点
				canvas.drawText("锚点", 20, 90, new Paint());
				Paint paint3 = new Paint();
				//设置以文本的中心点绘制
				paint3.setTextAlign(Paint.Align.CENTER);
				canvas.drawText("锚点", 20, 105, paint3);
				//----获取文本的长度
				Paint paint4 = new Paint();
				float len = paint4.measureText("文本宽度:");
				canvas.drawText("文本长度:" +len, 20, 130, new Paint());
				//----设置画笔样式
				canvas.drawRect(new Rect(20,140,40,160), new Paint());
				Paint paint5= new Paint();
				//设置画笔不填完
				paint5.setStyle(Style.STROKE);
				canvas.drawRect(new Rect(60, 140, 80, 160), paint5);
				//----设置画笔颜色
				Paint paint6 = new Paint();
				paint6.setColor(Color.GRAY);
				canvas.drawText("灰色",30, 180, paint6);
				//----设置画笔的粗细程度
				canvas.drawLine(20, 200, 70, 200, new Paint());
				Paint paint7 = new Paint();
				paint7.setStrokeWidth(7);
				canvas.drawLine(20, 220, 70, 220, paint7);
				//----设置画笔绘制文本的字体粗细
				Paint paint8 = new Paint();
				paint8.setTextSize(20);
				canvas.drawText("文字尺寸",20, 260, paint8);
				//----设置画笔的ARGB分量
				Paint paint9 = new Paint();
				paint9.setARGB(0x77, 0xff, 0x00, 0x00);
				canvas.drawText("红色半透明",20,290,paint9);
			}
		}
		catch(Exception e)
		{
		}
		finally
		{
			if(canvas != null)
				sfh.unlockCanvasAndPost(canvas);
		}
	}
	
	/**
	 * 触屏事件监听
	 */
	@Override 
	public boolean onTouchEvent(MotionEvent event)
	{
		return true;
	}
	
	/**
	 * 按键事件监听
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 游戏逻辑
	 */
	private void logic()
	{}
	
	private Runnable runn = new Runnable()
	{
		@Override
		public void run()
		{
			while(flag)
			{
				long start = System.currentTimeMillis();
				myDraw();
				logic();
				long end = System.currentTimeMillis();
				try
				{
					if(end-start<50)
					{
						Thread.sleep(50-(end-start));
					}
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	};
}