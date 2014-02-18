package com.dana.modulII;

import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.dana.util.Constant;

public class BluetoothChatService_D
{
	//Debug
	private static final String TAG = "BluetoothChatService";
	
	//当创建socket服务时的SDP名称
	private static final String NAME = "BluetoothChat";
	private static final String NAME_SECURE = "BluetoothChatSecure";
	private static final String NAME_INSECURE = "BluetoothChatInsecure";
	
	//应用程序的唯一UUID
	private static final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
	private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
	
	//本地蓝牙适配器
	private final BluetoothAdapter mAdapter;
	//Handler
	private final Handler handler;
	
	private BluetoothChatService_D bcs = BluetoothChatService_D.this;
	
	//请求链接的监听线程
	private AcceptThread mAcceptThread;
	private AcceptThread mSecureAcceptThread;
	private AcceptThread mInsecureAcceptThread;
	
	//链接一个设备的线程
	private ConnectThread mConnectThread;
	//建立连接后的管理线程
	private ConnectedThread mConnectedThread;
	
	//蓝牙适配器当前状态
	private int mState;
	
	private Context context;
	/**
	 * Constructor. Prepares a new BluetoothChat session.
     * @param context  The UI Activity Context
     * @param handler  A Handler to send messages back to the UI Activity
	 */
	public BluetoothChatService_D(Context context, Handler handler)
	{
		this.context = context;
		//设置Handler
		this.handler = handler;
		//得到本地蓝牙适配器
		this.mAdapter = BluetoothAdapter.getDefaultAdapter();
		//设置初始状态
		mState = Constant.STATE_NONE;
		
	}
	
	/**
     * Set the current state of the chat connection
     * @param state  An integer defining the current connection state
     */
	private synchronized void setState(int state)
	{
		Log.i(TAG, "setState() " + mState +" -> " + state);
		mState = state;
		
		//状态更新后，同时更新UI Activity
		handler.obtainMessage(Constant.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
	}
	
	/**
     * Return the current connection state. 
     */
	public synchronized int getState()
	{
		return mState;
	}
	
	/**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume() 
     */
	public synchronized void start()
	{
		Log.i(TAG, "start");
		//取消任何线程视图，建立一个连接
		if(mConnectThread !=null)
		{
			mConnectThread.cancel();
			mConnectThread = null;
		}
		
		//取消任何正在运行的链接
		if(mConnectedThread != null)
		{
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		
		//设置状态为监听，等待链接
		//启动AcceptThread 线程来监听BluetoothServerSocket
		if(mAcceptThread == null)
		{
			mAcceptThread = new AcceptThread(context, mAdapter, mState, NAME, MY_UUID, mHandler);
			mAcceptThread.start();
		}
		
		//设置状态为监听，等待链接
		setState(Constant.STATE_LISTEN);
	}
	
	/**
     * Start the ConnectThread to initiate a connection to a remote device.
     * @param device  The BluetoothDevice to connect
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
	public synchronized void connect(BluetoothDevice device)
	{
		Log.i(TAG, "connect to: " + device);
		
		//取消任何链接线程，试图简历一个链接
		if(mState == Constant.STATE_CONNECTING)
		{
			if(mConnectThread != null)
			{
				mConnectThread.cancel();
				mConnectThread = null;
			}
		}
		//取消任何正在进行的线程
		if(mConnectedThread !=null)
		{
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		
		//启动一个链接线程链接制定的设备
		mConnectThread = new ConnectThread(context, device, mAdapter, MY_UUID, mHandler);
		mConnectThread.start();
		setState(Constant.STATE_CONNECTING);
	}
	
	
	/**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     * @param socket  The BluetoothSocket on which the connection was made
     * @param device  The BluetoothDevice that has been connected
     */
	public synchronized void connected(BluetoothSocket socket, BluetoothDevice device)
	{
		Log.i(TAG, "connected");
		//取消ConnectThread链接线程
		if(mConnectThread !=null)
		{
			mConnectThread.cancel();
			mConnectThread = null;
		}
		//取消所有正在链接的线程
		if(mConnectedThread != null)
		{
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		
		//因为已经和一个设备建立链接,取消所有的监听线程
		if(mAcceptThread !=null)
		{
			mAcceptThread.cancel();
			mAcceptThread = null;
		}
		
		//启动ConnectedThread线程来管理连接和执行
		mConnectedThread = new ConnectedThread(context, socket, mHandler);
		mConnectedThread.start();
		
		//发送链接的设备名称UI Activity界面
		Message msg = handler.obtainMessage(Constant.MESSAGE_DEVICE_NAME);
		Bundle bundle = new Bundle();
		bundle.putString(Constant.DEVICE_NAME, device.getName());
		msg.setData(bundle);
		handler.sendMessage(msg);
		//状态变为已经链接
		setState(Constant.STATE_CONNECTED);
	}
	
	//停止所有线程
	public synchronized void stop() {
		Log.i(TAG, "stop");
		//取消ConnectThread线程
		if(mConnectThread !=null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}
		//取消已经链接的线程
		if(mConnectedThread != null)
		{
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		//取消监听线程
		if (mAcceptThread != null)
		{
			mAcceptThread.cancel();
			mAcceptThread = null;
		}
		//状态设置为准备状态
		setState(Constant.STATE_NONE);
	}
		
	/**
	 * Write to the ConnectedThread in an unsynchronized manner
	 * @param out The bytes to write
	 * @see ConnectedThread#write(byte[])
	 */
		//写入自己要发送出来的消息
	public void write(byte[] out) 
	{
		// Create temporary object
    	ConnectedThread r;
    	// Synchronize a copy of the ConnectedThread
    	synchronized (this) {
    		//判断是否处于已经链接状态
    		if (mState != Constant.STATE_CONNECTED) return;
    		r = mConnectedThread;
    		}
    	// 执行写
    	r.write(out);
	}
	
	/**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
	private void connectionFailed()
	{
		setState(Constant.STATE_LISTEN);
		//发送链接失败的消息到UI界面
		Message msg = handler.obtainMessage(Constant.MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString(Constant.TOAST, "Unable to connect device");
		msg.setData(bundle);
		handler.sendMessage(msg);
		//Start the service over to restart listening mode
		bcs.start();
	}
	
	/**
     * Indicate that the connection was lost and notify the UI Activity.
     */
	private void connectionLost()
	{
		setState(Constant.STATE_LISTEN);
		//发送失败消息到UI界面
		Message msg = handler.obtainMessage(Constant.MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString(Constant.TOAST, "Device connection was lost");
		msg.setData(bundle);
		handler.sendMessage(msg);
		//Start the service over to restart listening mode
		bcs.start();
	}
	
	private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			BluetoothSocket socket;
			switch(msg.what)
			{
				case 0x95:
					//如果状态为监听或者正在链接中，则调用connected来连接
					socket = (BluetoothSocket) msg.obj;
					connected(socket, socket.getRemoteDevice());
					break;
				case 0x96:
					connectionLost();
					//Start the service over to restart  listening mode;
					bcs.start();
					break;
				case 0x97:
					socket = (BluetoothSocket) msg.obj;
					//开启ConnectedThread(正在进行中...)线程
					//???????????????这里会出错。
					connected(socket, socket.getRemoteDevice());
					break;
				case 0x98:
					mConnectThread = null;
					break;
				case 0x99:
					//链接失败
					connectionFailed();
					//重新启动监听服务状态
					bcs.start();
					break;
				default:
					break;
			}
		}
	};
}