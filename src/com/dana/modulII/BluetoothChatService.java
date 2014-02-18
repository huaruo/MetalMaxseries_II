package com.dana.modulII;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import com.dana.util.Constant;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BluetoothChatService{
	
	//Debugging
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
	private final Handler mHandler;
	//请求链接的监听线程
	private AcceptThread mAcceptThread;
	private AcceptThread mSecureAcceptThread;
	private AcceptThread mInsecureAcceptThread;
	
	//链接一个设备的线程
	private ConnectThread mConnectThread;
	//建立链接后的管理线程
	private ConnectedThread mConnectedThread;
	//当前状态
	private int mState;
	
	public static final int STATE_NONE = 0; // we're doing nothing
	public static final int STATE_LISTEN = 1; // now listening for incoming connections
	public static final int STATE_CONNECTING = 2; //now initiating an outgoing connection
	public static final int STATE_CONNECTED = 3; //now connected to a remote device
	
	/**
	 * Constructor. Prepares a new BluetoothChat session.
     * @param context  The UI Activity Context
     * @param handler  A Handler to send messages back to the UI Activity
	 */
	public BluetoothChatService(Context context, Handler handler)
	{
		//得到本地蓝牙适配器
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		//设置初始状态
		mState = STATE_NONE;
		//设置Handler
		mHandler = handler;
	}
	
    /**
     * Set the current state of the chat connection
     * @param state  An integer defining the current connection state
     */
	private synchronized void setState(int state) {
		Log.i(TAG, "setState() " + mState + " -> " + state);
		mState = state;
		
		//状态更新之后更新UI Activity
		mHandler.obtainMessage(Constant.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
	}
	
    /**
     * Return the current connection state. 
     */
	public synchronized int getState(){
		return mState;
	}
	
	 /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume() 
     */
	public synchronized void start() {
		Log.i(TAG, "start");
		
		// 取消任何线程视图，建立一个连接
		if (mConnectThread != null)
		{
			mConnectThread.cancel();
			mConnectThread = null;
		}
		// 取消任何正在运行的链接
		if (mConnectedThread != null)
		{
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		//设置状态为监听,等待链接
		//启动AcceptThread线程来监听BluetoothServerSocket
		if (mAcceptThread == null) {
			mAcceptThread = new AcceptThread();
			mAcceptThread.start();
		}
		//设置状态为监听, 等待链接
		setState(STATE_LISTEN);
	}
	
	 /**
     * Start the ConnectThread to initiate a connection to a remote device.
     * @param device  The BluetoothDevice to connect
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
	public synchronized void connect(BluetoothDevice device) {
		Log.i(TAG, "connect to: " + device);
		
		//取消任何链接线程，视图建立一个连接
		if(mState == STATE_CONNECTING) {
			if(mConnectThread !=null)
			{
				mConnectThread.cancel();
				mConnectThread = null;
			}
		}
		//取消任何正在运行的线程
		if(mConnectedThread != null)
		{
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		
		//启动一个连接线程链接指定的设备
		mConnectThread = new ConnectThread(device);
		mConnectThread.start();
		setState(STATE_CONNECTING);
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
		if(mConnectThread != null)
		{
			mConnectThread.cancel();
			mConnectThread = null;
		}
		//取消所有正在链接的线程
		if(mConnectedThread !=null)
		{
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		//因为已经和一个设备建立链接，取消所有的监听线程
		if(mAcceptThread != null)
		{
			mAcceptThread.cancel();
			mAcceptThread = null;
		}
		//启动ConnectedThread线程来管理连接和执行
		mConnectedThread = new ConnectedThread(socket);
		mConnectedThread.start();
		
		//发送链接的设备名称UI Activity界面
		Message msg = mHandler.obtainMessage(Constant.MESSAGE_DEVICE_NAME);
		Bundle bundle = new Bundle();
		bundle.putString(Constant.DEVICE_NAME, device.getName());
		msg.setData(bundle);
		mHandler.sendMessage(msg);
		//状态变为已经链接
		setState(STATE_CONNECTED);
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
		setState(STATE_NONE);
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
    		if (mState != STATE_CONNECTED) return;
    		r = mConnectedThread;
    		}
    	// 执行写
    	r.write(out);
    }
	
    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
	private void connectionFailed() {
		setState(STATE_LISTEN);
		//发送链接失败的消息到UI界面
		Message msg = mHandler.obtainMessage(Constant.MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString(Constant.TOAST, "Unable to connect device");
		msg.setData(bundle);
		mHandler.sendMessage(msg);
		//Start the service over to restart  listening mode
		BluetoothChatService.this.start();
	}
	
	 /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
	private void connectionLost()
	{
		setState(STATE_LISTEN);
		//发送失败消息到UI界面
		Message msg = mHandler.obtainMessage(Constant.MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString(Constant.TOAST,  "Device connection was lost");
		msg.setData(bundle);
		mHandler.sendMessage(msg);
		//Start the service over to restart listening mode
		BluetoothChatService.this.start();
	}
	
    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
	private class AcceptThread extends Thread {
		//本地socket服务
		private final BluetoothServerSocket mmServerSocket;
		private String mSocketType;
		
		public AcceptThread() {
			BluetoothServerSocket tmp = null;
			//创建一个新的socket服务监听
			try  {
				tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
			} catch (IOException e) {
				Log.i(TAG, "Socket Type:" + mSocketType + "listen() failed", e);
			}
			mmServerSocket = tmp;
		}
		
		public void run() {
			Log.i(TAG, "Socket Type: " + mSocketType + "BEGIN mAcceptThread" + this);
			
			setName("AcceptThread"  + mSocketType);
			BluetoothSocket socket = null;
			
			//如果当前没有链接则一直监听socket服务
			while (mState != STATE_CONNECTED)
			{
				//如果有请求链接，则接受；阻塞调用。返回链接成功和一个异常
				try{
					if(mmServerSocket!=null)
						socket = mmServerSocket.accept();
				} catch (IOException e) {
					Log.i(TAG, "Socket Type:" + mSocketType + "accept() failed", e);
					break;
				}
				
				//如果接受一个链接
				if(socket !=null)
				{
					synchronized(BluetoothChatService.this){
						switch (mState)
						{
						case STATE_LISTEN:
						case STATE_CONNECTING:
							//如果状态为监听或者正在链接中，则调用connected来链接
							connected(socket, socket.getRemoteDevice());
							break;
						case STATE_NONE:
						case STATE_CONNECTED:
							//如果没有设备或者已经链接，终止该socket
							try{
								socket.close();
							} catch(IOException e){
								Log.i(TAG,"Could not close unwanted socket", e);
							}
							break;
						}
					}
				}
			}
			Log.i(TAG, "END mAcceptThread, socket Type: " + mSocketType);
		}
		
		//关闭BluetoothServerSocket
		public void cancel() {
			Log.i(TAG, "Socket Type: " + mSocketType + "cancel" + this);
			try{
				mmServerSocket.close();
			} catch (IOException e)
			{
				Log.i(TAG, "Socket Type: " +mSocketType + "close() of server failed", e);
			}
		}
	}
	
    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
	private class ConnectThread extends Thread {
		//蓝牙Socket
		private final  BluetoothSocket mmSocket;
		//蓝牙设备
		private final BluetoothDevice mmDevice;
		private String mSocketType;
		
		public ConnectThread(BluetoothDevice device) {
			mmDevice = device;
			BluetoothSocket tmp =null;
			
			//得到一个给定的蓝牙设备的BluetoothSocket
			try{
				tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
			}
			catch (IOException e) {
				Log.i(TAG, "Socket Type:" + mSocketType + "create() failed", e);
			}
			mmSocket = tmp;
		}
		
		public void run(){
			Log.i(TAG, "BEGIN mConnectThread SocketType:" + mSocketType);
			setName("ConnectThread" + mSocketType);
			
			//取消可见状态，进行链接。
			mAdapter.cancelDiscovery();
			
			//创建一个BluetoothSocket链接
			try{
				mmSocket.connect();
			} 
			//如果异常 则关闭socket
			catch (IOException e)
			{
				try{
					mmSocket.close();
				}
				catch(IOException e2)
				{
					Log.i(TAG,"unable to close() " + mSocketType + "socket suring connection failure", e2);
				}
				//链接失败
				connectionFailed();
				//重新启动监听服务状态
				BluetoothChatService.this.start();
				return;
			}
			
			//完成则重置ConnectThread
			synchronized(BluetoothChatService.this) {
				mConnectThread = null;
			}
			
			//开启ConnectedThread(正在运行中...)线程
			connected(mmSocket,  mmDevice);
		}
		//取消链接线程ConnectThread
		public void cancel() {
			try{
				mmSocket.close();
			}
			catch (IOException e) {
				Log.i(TAG, "close() of connect" + mSocketType + " socket failed", e);
			}
		}
	}
	
	 /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
	private class ConnectedThread extends Thread {
		//BluetoothSocket
		private final BluetoothSocket mmSocket;
		//输入输出流
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;
		
		public ConnectedThread(BluetoothSocket socket){
			Log.i(TAG, "create ConnectedThread");
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;
			//得到BluetoothSocket的输入输出流
			try{
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e)
			{
				Log.e(TAG, "temp sockets not created", e);
			}
			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}
		public void run() {
			Log.i(TAG, "BEGIN mConnectedThread");
			byte[]  buffer = new byte[1024];
			int bytes;
			
			//监听输入流
			while (true) {
				try{
					//从输入流中读取数据
					bytes = mmInStream.read(buffer);
					//发送一个消息到UI线程进行更新
					mHandler.obtainMessage(Constant.MESSAGE_READ, bytes, -1, buffer).sendToTarget();
				}
				catch (IOException e)
				{
					//出现异常，则链接丢失
					Log.i(TAG, "disconnected", e);
					connectionLost();
					//Start the service over to restart listening mode
					BluetoothChatService.this.start();
					break;
				}
			}
		}
	    /**
	    * 写入要发送的消息
	    * @param buffer  The bytes to write
	    */
		public void write(byte[] buffer) {
			try{
				mmOutStream.write(buffer);
				
				//将写的消息同时传递给UI界面
				mHandler.obtainMessage(Constant.MESSAGE_WRITE, -1, -1, buffer).sendToTarget();
			}
			catch (IOException e)
			{
				Log.e(TAG, "Exception during write", e);
			}
		}
		
		//取消ConnectedThread链接管理线程
		public void cancel()
		{
			try {
				mmSocket.close();
			}
			catch (IOException e)
			{
				Log.e(TAG, "close() of connect socket failed", e);
			}
		}
	}
}