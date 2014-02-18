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
	//���ڿ���SurfaceView
	private SurfaceHolder sfh;
	//����һ������
	private Paint paint;
	//����һ���߳�
	private Thread th;
	//�߳������ı�ʶλ
	private boolean flag;
	//����һ������
	private Canvas canvas;
	//������Ļ�Ŀ��
	private int screenW, screenH;
	private Bitmap bmp;
	
	/**
	 * SurfaceView��ʼ������
	 */
	public BitmapSfv(Context c)
	{
		super(c);
		//ʵ����SurfaceHolder
		sfh = this.getHolder();
		//ΪSurfaceView���״̬����
		sfh.addCallback(callback);
		//ʵ��һ������
		paint = new Paint();
		//���û�����ɫΪ��ɫ
		paint.setColor(Color.WHITE);
		//���ý���
		setFocusable(true);
		bmp = BitmapFactory.decodeResource(this.getResources(),R.drawable.image);
	}
	
	private Callback callback = new Callback()
	{
		/**
		 * SurfaceView��ͼ��������Ӧ�˺���
		 */
		@Override
		public void surfaceCreated(SurfaceHolder holder)
		{
			screenW = BitmapSfv.this.getWidth();
			screenH = BitmapSfv.this.getHeight();
			flag = true;
			//ʵ���߳�
			th = new Thread(runn);
			//�����߳�
			th.start();
		}
		
		/**
		 * SurfaceView��ͼ״̬�����ı䣬��Ӧ�˺���
		 */
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
		{}
		/**
		 * SurfaceView��ͼ����ʱ����Ӧ�˺���
		 */
		@Override
		public void surfaceDestroyed(SurfaceHolder holder)
		{
			flag = false;
		}
	};
	
	/**
	 * ��Ϸ��ͼ
	 */
	public void myDraw()
	{
		try
		{
			canvas = sfh.lockCanvas();
			if(canvas !=null)
			{
				canvas.drawColor(Color.BLACK);
				//----------����λͼ
				//canvas.drawBitmap(bmp, 0, 0, paint);
				//----------��תλͼ(��ʽ1)
				//canvas.save();
				//canvas.rotate(30, bmp.getWidth()/2, bmp.getHeight()/2);
				//canvas.drawBitmap(bmp, 0, 0, paint);
				//canvas.restore();
				//canvas.drawBitmap(bmp, 100, 0, paint);
				//----------��תλͼ(��ʽ2)
				//Matrix mx = new Matrix();
				//mx.postRotate(30, bmp.getWidth() / 2, bmp.getHeight() / 2);
				//canvas.drawBitmap(bmp, mx, paint);
				//----------ƽ��λͼ(��ʽ1)
				//canvas.save();
				//canvas.translate(10, 10);
				//canvas.drawBitmap(bmp, 0, 0, paint);
				//canvas.restore();
				//----------ƽ��λͼ(��ʽ2)
				//Matrix maT = new Matrix();
				//maT.postTranslate(10, 10);
				//canvas.drawBitmap(bmp, maT, paint);
				//----------����λͼ(��ʽ1)
				//canvas.save();
				//canvas.scale(2f, 2f, 50 + bmp.getWidth() / 2, 50 + bmp.getHeight() / 2);
				//canvas.drawBitmap(bmp, 50, 50, paint);
				//canvas.restore();
				//canvas.drawBitmap(bmp, 50, 50, paint);
				//----------����λͼ(��ʽ2)
				//Matrix maS = new Matrix();
				//maS.postTranslate(50, 50);
				//maS.postScale(2f, 2f, 50 + bmp.getWidth() / 2, 50 + bmp.getHeight() / 2);
				//canvas.drawBitmap(bmp, maS, paint);
				//canvas.drawBitmap(bmp, 50, 50, paint);
				//----------����תλͼ(��ʽ1)
				//X�᾵��
				//canvas.drawBitmap(bmp, 0, 0, paint);
				//canvas.save();
				//canvas.scale(-1, 1, 100 + bmp.getWidth() / 2, 100 + bmp.getHeight() / 2);
				//canvas.drawBitmap(bmp, 100, 100, paint);
				//canvas.restore();
				//Y�᾵��
				//canvas.drawBitmap(bmp, 0, 0, paint);
				//canvas.save();
				//canvas.scale(1, -1, 100 + bmp.getWidth() / 2, 100 + bmp.getHeight() / 2);
				//canvas.drawBitmap(bmp, 100, 100, paint);
				//canvas.restore();
				//----------����תλͼ(��ʽ2)
				//X�᾵��
				canvas.drawBitmap(bmp, 0, 0,paint);
				Matrix maMiX = new Matrix();
				maMiX.postTranslate(100, 100);
				maMiX.postScale(-1, 1, 100 + bmp.getWidth()/2, 100 + bmp.getHeight()/2);
				canvas.drawBitmap(bmp, maMiX, paint);
				//Y�᾵��
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
	 * ���� �¼�����
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		return true;
	}
	
	/**
	 * �����¼�����
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * ��Ϸ�߼�
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