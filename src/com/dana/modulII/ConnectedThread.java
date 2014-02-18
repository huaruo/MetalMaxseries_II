package com.dana.modulII;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.dana.util.Constant;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * This thread runs during a connection with a remote device.
 * It handles all incoming and outgoing transmissions.
 */
public class ConnectedThread extends Thread
{
	//Debug
	private final static String TAG = "ConnectedThread";
	
	//BluetoothSocket
	private final BluetoothSocket mbtSocket;
	//���������
	private final InputStream mInStream;
	private final OutputStream mOutStream;
	
	private Context context;
	private Handler handler;
	
	public ConnectedThread(Context context, BluetoothSocket socket, Handler handler)
	{
		Log.i(TAG,"create ConnectedThread");
		this.context = context;
		this.mbtSocket = socket;
		this.handler = handler;
		
		InputStream tmpIn = null;
		OutputStream tmpOut = null;
		//�õ�BluetoothSocket�����������
		try {
			tmpIn = socket.getInputStream();
			tmpOut = socket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "temp sockets not created", e);
//			e.printStackTrace();
		}
		this.mInStream = tmpIn;
		this.mOutStream = tmpOut;
	}
	
	public void run()
	{
		Log.i(TAG, "BEGIN mConnectedThread");
		byte[] buffer = new byte[1024];
		int bytes;
		Message msg  = handler.obtainMessage();
		
		//����������
		while(true)
		{
			//���������ж�ȡ ����
			try {
				bytes = mInStream.read(buffer);
				//����һ����Ϣ��UI�߳̽��и���
				handler.obtainMessage(Constant.MESSAGE_READ,bytes,-1,buffer).sendToTarget();
			} catch (IOException e) {
				//�����쳣�������Ӷ�ʧ
				Log.i(TAG, "disconnected", e);
				msg.what = 0x96;
				handler.sendMessage(msg);
//				connectionLost();
//				//Start the service over to restart  listening mode;
//				context.start();
				// TODO Auto-generated catch block
//				e.printStackTrace();
				break;
			}	
		}
	}
	
	/**
	 * д��Ҫ���͵���Ϣ
	 * @param buffer  The bytes to write
	 */
	public void write(byte[] buffer)
	{
		try {
			mOutStream.write(buffer);
			//��д����Ϣͬʱ���ݸ�UI����
			handler.obtainMessage(Constant.MESSAGE_WRITE,-1,-1,buffer).sendToTarget();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG,"Exception during write", e);
//			e.printStackTrace();
		}
	}
	
	//ȡ��ConnectedThread���ӹ����߳�
	public void cancel()
	{
		try {
			mbtSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "close() of connect socket failed", e);
//			e.printStackTrace();
		}
	}
}