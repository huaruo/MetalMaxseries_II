/**
 * @author Huaruo.W
 */

package com.dana.modulII;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class CanvasSfv extends SurfaceView
{
	//用于控制SurfaceView
	private SurfaceHolder sfh;
	//声明一个画笔
	private Paint paint;
	//声明一条线程
	private Thread th;
	//线程消亡的标识位
	private boolean flag;
	//声明一个画布
	private Canvas canvas;
	//声明屏幕的宽高
	private int screenW, screenH;
	//设置画布绘图无锯齿
	private PaintFlagsDrawFilter pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
	/**
	 * SurfaceView初始化函数
	 */
	public CanvasSfv(Context c)
	{
		super(c);
		//实例SurfaceHolder
		sfh = this.getHolder();
		//为SurfaceView添加状态监听
		sfh.addCallback(callback);
		//实例一个画笔
		paint = new Paint();
		//设置画笔颜色为白色
		paint.setColor(Color.WHITE);
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
			screenW = CanvasSfv.this.getWidth();
			screenH = CanvasSfv.this.getHeight();
			Log.i("screenW", String.valueOf(screenW));
			Log.i("screenH", String.valueOf(screenH));
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
				//----设置画布绘图无锯齿
				canvas.setDrawFilter(pfd);
				//----利用填充画布，刷屏
				canvas.drawColor(Color.BLACK);
				//----绘制文本
				canvas.drawText("drawText", 10, 10, paint);
				//----绘制像素点
				canvas.drawPoint(10, 20, paint);
				//----绘制多个像素点
				canvas.drawPoints(new float[]{10,30,30,30}, paint);
				//----绘制直线
				canvas.drawLine(10, 40, 50, 40, paint);
				//----绘制多条直线
				canvas.drawLines(new float[]{10,50,50,50,70,50,110,50},paint);
				//----绘制矩形
				canvas.drawRect(10,60,40,100,paint);
				//----绘制矩形2
				Rect rect= new Rect(10, 110, 60 ,130);
				canvas.drawRect(rect, paint);
				canvas.drawRect(rect, paint);
				//----绘制圆角矩形
				RectF rectF = new RectF(10, 140, 60, 170);
				canvas.drawRoundRect(rectF, 20, 20 ,paint);
				//----绘制圆形
				canvas.drawCircle(20,200,20,paint);
				//----绘制弧形
				canvas.drawArc(new RectF(150, 20 , 200, 70), 0, 230, true, paint);
				//----绘制椭圆
				canvas.drawOval(new RectF(150,80,180,100), paint);
				//----绘制指定路径图形
				Path path = new Path();
				//设置路径起点
				path.moveTo(160,150);
				//路线1
				path.lineTo(200, 150);
				//路线2
				path.lineTo(180, 200);
				//路径结束
				path.close();
				canvas.drawPath(path, paint);
				//----绘制指定路径图形
				Path pathCircle = new Path();
				//添加一个圆形的路径
				pathCircle.addCircle(130, 260, 20,  Path.Direction.CCW);
				//----绘制带圆形的路径文本
				canvas.drawTextOnPath("PathText", pathCircle, 10, 20, paint);
			}
		}
		catch (Exception e)
		{
		}
		finally
		{
			if(canvas !=null)
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
					if(end - start<50)
					{
						Thread.sleep(50-(end - start));
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