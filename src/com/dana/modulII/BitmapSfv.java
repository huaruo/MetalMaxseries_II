package com.dana.modulII;

import com.dana.startappII.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * @author Huaruo.W
 */

public class BitmapSfv extends SurfaceView
{
	//用于控制SurfaceView
	private SurfaceHolder sfh;
	//声明一个画笔
	private Paint paint;
	//声明一个线程
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
	public BitmapSfv(Context c)
	{
		super(c);
		//实例化SurfaceHolder
		sfh = this.getHolder();
		//为SurfaceView添加状态监听
		sfh.addCallback(callback);
		//实例一个画笔
		paint = new Paint();
		//设置画笔颜色为白色
		paint.setColor(Color.WHITE);
		//设置焦点
		setFocusable(true);
		bmp = BitmapFactory.decodeResource(this.getResources(),R.drawable.image);
	}
	
	private Callback callback = new Callback()
	{
		/**
		 * SurfaceView视图创建，响应此函数
		 */
		@Override
		public void surfaceCreated(SurfaceHolder holder)
		{
			screenW = BitmapSfv.this.getWidth();
			screenH = BitmapSfv.this.getHeight();
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
				//----------绘制位图
				//canvas.drawBitmap(bmp, 0, 0, paint);
				//----------旋转位图(方式1)
				//canvas.save();
				//canvas.rotate(30, bmp.getWidth()/2, bmp.getHeight()/2);
				//canvas.drawBitmap(bmp, 0, 0, paint);
				//canvas.restore();
				//canvas.drawBitmap(bmp, 100, 0, paint);
				//----------旋转位图(方式2)
				//Matrix mx = new Matrix();
				//mx.postRotate(30, bmp.getWidth() / 2, bmp.getHeight() / 2);
				//canvas.drawBitmap(bmp, mx, paint);
				//----------平移位图(方式1)
				//canvas.save();
				//canvas.translate(10, 10);
				//canvas.drawBitmap(bmp, 0, 0, paint);
				//canvas.restore();
				//----------平移位图(方式2)
				//Matrix maT = new Matrix();
				//maT.postTranslate(10, 10);
				//canvas.drawBitmap(bmp, maT, paint);
				//----------缩放位图(方式1)
				//canvas.save();
				//canvas.scale(2f, 2f, 50 + bmp.getWidth() / 2, 50 + bmp.getHeight() / 2);
				//canvas.drawBitmap(bmp, 50, 50, paint);
				//canvas.restore();
				//canvas.drawBitmap(bmp, 50, 50, paint);
				//----------缩放位图(方式2)
				//Matrix maS = new Matrix();
				//maS.postTranslate(50, 50);
				//maS.postScale(2f, 2f, 50 + bmp.getWidth() / 2, 50 + bmp.getHeight() / 2);
				//canvas.drawBitmap(bmp, maS, paint);
				//canvas.drawBitmap(bmp, 50, 50, paint);
				//----------镜像反转位图(方式1)
				//X轴镜像
				//canvas.drawBitmap(bmp, 0, 0, paint);
				//canvas.save();
				//canvas.scale(-1, 1, 100 + bmp.getWidth() / 2, 100 + bmp.getHeight() / 2);
				//canvas.drawBitmap(bmp, 100, 100, paint);
				//canvas.restore();
				//Y轴镜像
				//canvas.drawBitmap(bmp, 0, 0, paint);
				//canvas.save();
				//canvas.scale(1, -1, 100 + bmp.getWidth() / 2, 100 + bmp.getHeight() / 2);
				//canvas.drawBitmap(bmp, 100, 100, paint);
				//canvas.restore();
				//----------镜像反转位图(方式2)
				//X轴镜像
				canvas.drawBitmap(bmp, 0, 0,paint);
				Matrix maMiX = new Matrix();
				maMiX.postTranslate(100, 100);
				maMiX.postScale(-1, 1, 100 + bmp.getWidth()/2, 100 + bmp.getHeight()/2);
				canvas.drawBitmap(bmp, maMiX, paint);
				//Y轴镜像
				canvas.drawBitmap(bmp, 0, 0,paint);
				Matrix maMiY = new Matrix();
				maMiY.postTranslate(100, 100);
				maMiY.postScale(1,-1,100+bmp.getWidth()/2,100+bmp.getHeight()/2);
				canvas.drawBitmap(bmp, maMiY, paint);
			}
		}
		catch(Exception e)
		{
			//TODO:handle exception
		}
		finally
		{
			if(canvas!=null)
			{
				sfh.unlockCanvasAndPost(canvas);
			}
		}
	}
	
	/**
	 * 触屏 事件监听
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
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
				try
				{
					if(end - start<50)
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