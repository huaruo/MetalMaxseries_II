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
	//蓝牙Socket
	private final BluetoothSocket mbtSocket;
	//蓝牙设备
	private final BluetoothDevice mbtDevice;
	//蓝牙适配器
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
		//得到一个给定的蓝牙设备的BluetoothSocket
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
		
		//取消可见状态，进行链接
		mbtAdapter.cancelDiscovery();
		//创建一个BluetoothSocket链接
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
//			//链接失败
//			connectionFailed();
//			//重新启动监听服务状态
//			context.start();
//			e.printStackTrace();
			return;
		}
		//完成则重置ConnectThread
		synchronized(context)
		{
			msg.what = 0x98;
			handler.sendMessage(msg);
//			mConnectThread = null;
		}
		//开启ConnectedThread(正在进行中...)线程
		msg.what = 0x97;
		msg.obj=mbtSocket;
		handler.sendMessage(msg);
//		connected(mbtSocket, mbtDevice);
	}

	//取消链接线程ConnectThread
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