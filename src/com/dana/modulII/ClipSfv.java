package com.dana.modulII;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.dana.startappII.R;

/**
 * @author Dana
 */
public class ClipSfv extends SurfaceView
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
	private Bitmap bmp;
	
	/**
	 * SurfaceView初始化函数
	 */
	public ClipSfv(Context c)
	{
		super(c);
		//实例SurfaceHolder
		sfh=this.getHolder();
		//为SurfaceView添加状态监听
		sfh.addCallback(callback);
		//实例一个画笔
		paint = new Paint();
		//设置画笔颜色为白色
		paint.setColor(Color.WHITE);
		//设置画笔无锯齿
		paint.setAntiAlias(true);
		//设置焦点
		setFocusable(true);
		bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.image);
	}
	
	private Callback callback = new Callback()
	{
		/**
		 * SurfaceView视图创建，响应此函数
		 */
		@Override
		public void surfaceCreated(SurfaceHolder holder)
		{
			screenW = ClipSfv.this.getWidth();
			screenH = ClipSfv.this.getHeight();
			flag = true;
			//实例线程
			th = new Thread(runn);
			//启动线程
			th.start();
		}
		/**
		 * SurfaceView视图状态发生改变 ，响应此函数
		 */
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
		{}
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
				canvas.drawColor(Color.BLACK);
				//----设置矩形可视区域
				//canvas.save();
				//canvas.clipRect(0, 0, 20, 20);
				//canvas.drawBitmap(bmp, 0, 0, paint);
				//canvas.restore();
				//----利用Path设置可视区域
				//canvas.save();
				//Path path = new Path();
				//path.addCircle(30, 30, 30, Direction.CCW);
				//canvas.clipPath(path);
				//canvas.drawBitmap(bmp, 0, 0, paint);
				//canvas.restore();
				//----利用Region设置可视区域
				canvas.save();
				Region region = new Region();
				region.op(new Rect(20,20,100,100), Region.Op.UNION);
				region.op(new Rect(40,20,80,150), Region.Op.XOR);
				canvas.clipRegion(region);
				canvas.drawBitmap(bmp, 0, 0,paint);
				canvas.restore();
			}
		}
		catch(Exception e)
		{
			//TODO: handle exception
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
		return super.onKeyDown(keyCode,event);
	}
	
	/**
	 * 游戏逻辑
	 */
	private void logic()
	{
	}
	
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
				try{
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