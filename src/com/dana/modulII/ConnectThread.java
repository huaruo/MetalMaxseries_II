package com.dana.modulII;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * This thread runs while attempting to make an outgoing connection
 * with a device. It runs straight through; the connection either
 * succeeds or fails.
 */
public class ConnectThread extends Thread
{
	//Debug
	private final static String TAG = "ConnectThread";
	//����Socket
	private final BluetoothSocket mbtSocket;
	//�����豸
	private final BluetoothDevice mbtDevice;
	//����������
	private final BluetoothAdapter mbtAdapter;
	private Handler handler;
	
	private Context context;
	
	private String mSocketType;
	
	public ConnectThread(Context context, BluetoothDevice device, BluetoothAdapter adapter, UUID MY_UUID, Handler handler)
	{
		this.context = context;
		this.mbtDevice = device;
		this.mbtAdapter = adapter;
		this.handler = handler;
		BluetoothSocket tmp = null;
		//�õ�һ�������������豸��BluetoothSocket
		try {
			tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, "Socket Type:" + mSocketType + "create() failed", e);
//			e.printStackTrace();
		}
		this.mbtSocket = tmp;
	}
	
	public void run()
	{
		Log.i(TAG, "BEGIN mConnectThread SocketType:" + mSocketType);
		setName("ConnectThread" + mSocketType);
		
		Message msg = handler.obtainMessage();
		
		//ȡ���ɼ�״̬����������
		mbtAdapter.cancelDiscovery();
		//����һ��BluetoothSocket����
		try {
			mbtSocket.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			try {
				mbtSocket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				Log.i(TAG,"unable to close() " + mSocketType + "socket suring connection failure", e1);
//				e1.printStackTrace();
			}
			
			msg.what = 0x99;
			handler.sendMessage(msg);
//			//����ʧ��
//			connectionFailed();
//			//����������������״̬
//			context.start();
//			e.printStackTrace();
			return;
		}
		//���������ConnectThread
		synchronized(context)
		{
			msg.what = 0x98;
			handler.sendMessage(msg);
//			mConnectThread = null;
		}
		//����ConnectedThread(���ڽ�����...)�߳�
		msg.what = 0x97;
		msg.obj=mbtSocket;
		handler.sendMessage(msg);
//		connected(mbtSocket, mbtDevice);
	}

	//ȡ�������߳�ConnectThread
	public void cancel()
	{
		try {
			mbtSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, "close() of connect" + mSocketType + " socket failed", e);
//			e.printStackTrace();
		}
	}
}