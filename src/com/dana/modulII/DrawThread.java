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
 * �������inBuf������
 * @author Huaruo.W
 *
 */
public class DrawThread extends Thread
{
	//Debug
	private final static String TAG = "DrawThread";
	
	private int oldX = 0; //�ϴλ��Ƶ�X����
	private int oldY = 0;//�ϴλ���
	private SurfaceView sfv; //���� 
	public int X_index = 0;//��ǰ��ͼ������ĻX�����������
	private Paint mPaint; //����
	private int wait = 50;  //�̵߳ȴ�ʱ��
	
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
					SimpleDraw(X_index, buf, rateX, rateY, baseLine);//�ѻ��������ݻ�����
					X_index = X_index + (buf.length/rateX) - 1; //����-1���Լ��ٿ�϶
					if(X_index > sfv.getHeight())
					{
						X_index =0;
					}
				}
				Thread.sleep(wait);//��ʱһ��ʱ�仺������
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * ����ָ������
	 * @param start  X�Ὺʼ��λ�ã�ȫ����
	 * @param inputBuf ������
	 * @param rateX X��������С�ı���
	 * @param rateY ����С�ı���
	 * @param baseLine
	 */
	private void SimpleDraw(int start, byte[] inputBuf, int rateX, int rateY, int baseLine)
	{
		if(start ==0)
			oldX = 0;
		//������Ҫ��СX�����
		byte[]  buffer = new byte[inputBuf.length / rateX];
		for(int  i = 0, ii =0; i<buffer.length; i++, ii = i*rateX)
			buffer[i] = inputBuf[ii];
		Canvas canvas = sfv.getHolder().lockCanvas(new Rect(0, start, sfv.getWidth(), start + buffer.length)); //�ؼ�����ȡ����
		canvas.drawColor(Color.BLACK); //�������
		
		for(int i = 0; i< buffer.length; i++)
		{
			int y = i+start;
			int x = (0xFF - (buffer[i] & 0xFF)) / rateY +baseLine; //������С���������ڻ�׼�ߡ� 0xFF-���ڷ�ת�� &0xFF���ڰ�byte���͵ĸ���ȡֵתΪ��
			canvas.drawLine(oldX, oldY, x, y, mPaint);
			oldX = x; 
			oldY = y;
		}
		sfv.getHolder().unlockCanvasAndPost(canvas);//�����������ύ���õ�ͼ��
	}
	
	
}

