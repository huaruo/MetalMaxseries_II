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
	
	//������socket����ʱ��SDP����
	private static final String NAME = "BluetoothChat";
	private static final String NAME_SECURE = "BluetoothChatSecure";
	private static final String NAME_INSECURE = "BluetoothChatInsecure";
	
	//Ӧ�ó����ΨһUUID
	private static final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
	private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
	
	//��������������
	private final BluetoothAdapter mAdapter;
	//Handler
	private final Handler mHandler;
	//�������ӵļ����߳�
	private AcceptThread mAcceptThread;
	private AcceptThread mSecureAcceptThread;
	private AcceptThread mInsecureAcceptThread;
	
	//����һ���豸���߳�
	private ConnectThread mConnectThread;
	//�������Ӻ�Ĺ����߳�
	private ConnectedThread mConnectedThread;
	//��ǰ״̬
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
		//�õ���������������
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		//���ó�ʼ״̬
		mState = STATE_NONE;
		//����Handler
		mHandler = handler;
	}
	
    /**
     * Set the current state of the chat connection
     * @param state  An integer defining the current connection state
     */
	private synchronized void setState(int state) {
		Log.i(TAG, "setState() " + mState + " -> " + state);
		mState = state;
		
		//״̬����֮�����UI Activity
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
		
		// ȡ���κ��߳���ͼ������һ������
		if (mConnectThread != null)
		{
			mConnectThread.cancel();
			mConnectThread = null;
		}
		// ȡ���κ��������е�����
		if (mConnectedThread != null)
		{
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		//����״̬Ϊ����,�ȴ�����
		//����AcceptThread�߳�������BluetoothServerSocket
		if (mAcceptThread == null) {
			mAcceptThread = new AcceptThread();
			mAcceptThread.start();
		}
		//����״̬Ϊ����, �ȴ�����
		setState(STATE_LISTEN);
	}
	
	 /**
     * Start the ConnectThread to initiate a connection to a remote device.
     * @param device  The BluetoothDevice to connect
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
	public synchronized void connect(BluetoothDevice device) {
		Log.i(TAG, "connect to: " + device);
		
		//ȡ���κ������̣߳���ͼ����һ������
		if(mState == STATE_CONNECTING) {
			if(mConnectThread !=null)
			{
				mConnectThread.cancel();
				mConnectThread = null;
			}
		}
		//ȡ���κ��������е��߳�
		if(mConnectedThread != null)
		{
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		
		//����һ�������߳�����ָ�����豸
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
		
		//ȡ��ConnectThread�����߳�
		if(mConnectThread != null)
		{
			mConnectThread.cancel();
			mConnectThread = null;
		}
		//ȡ�������������ӵ��߳�
		if(mConnectedThread !=null)
		{
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		//��Ϊ�Ѿ���һ���豸�������ӣ�ȡ�����еļ����߳�
		if(mAcceptThread != null)
		{
			mAcceptThread.cancel();
			mAcceptThread = null;
		}
		//����ConnectedThread�߳����������Ӻ�ִ��
		mConnectedThread = new ConnectedThread(socket);
		mConnectedThread.start();
		
		//�������ӵ��豸����UI Activity����
		Message msg = mHandler.obtainMessage(Constant.MESSAGE_DEVICE_NAME);
		Bundle bundle = new Bundle();
		bundle.putString(Constant.DEVICE_NAME, device.getName());
		msg.setData(bundle);
		mHandler.sendMessage(msg);
		//״̬��Ϊ�Ѿ�����
		setState(STATE_CONNECTED);
	}
	
	//ֹͣ�����߳�
	public synchronized void stop() {
		Log.i(TAG, "stop");
		//ȡ��ConnectThread�߳�
		if(mConnectThread !=null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}
		//ȡ���Ѿ����ӵ��߳�
		if(mConnectedThread != null)
		{
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		//ȡ�������߳�
		if (mAcceptThread != null)
		{
			mAcceptThread.cancel();
			mAcceptThread = null;
		}
		//״̬����Ϊ׼��״̬
		setState(STATE_NONE);
	}
	
	   /**
     * Write to the ConnectedThread in an unsynchronized manner
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
	//д���Լ�Ҫ���ͳ�������Ϣ
    public void write(byte[] out) 
    {
    	// Create temporary object
    	ConnectedThread r;
    	// Synchronize a copy of the ConnectedThread
    	synchronized (this) {
    		//�ж��Ƿ����Ѿ�����״̬
    		if (mState != STATE_CONNECTED) return;
    		r = mConnectedThread;
    		}
    	// ִ��д
    	r.write(out);
    }
	
    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
	private void connectionFailed() {
		setState(STATE_LISTEN);
		//��������ʧ�ܵ���Ϣ��UI����
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
		//����ʧ����Ϣ��UI����
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
		//����socket����
		private final BluetoothServerSocket mmServerSocket;
		private String mSocketType;
		
		public AcceptThread() {
			BluetoothServerSocket tmp = null;
			//����һ���µ�socket�������
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
			
			//�����ǰû��������һֱ����socket����
			while (mState != STATE_CONNECTED)
			{
				//������������ӣ�����ܣ��������á��������ӳɹ���һ���쳣
				try{
					if(mmServerSocket!=null)
						socket = mmServerSocket.accept();
				} catch (IOException e) {
					Log.i(TAG, "Socket Type:" + mSocketType + "accept() failed", e);
					break;
				}
				
				//�������һ������
				if(socket !=null)
				{
					synchronized(BluetoothChatService.this){
						switch (mState)
						{
						case STATE_LISTEN:
						case STATE_CONNECTING:
							//���״̬Ϊ�����������������У������connected������
							connected(socket, socket.getRemoteDevice());
							break;
						case STATE_NONE:
						case STATE_CONNECTED:
							//���û���豸�����Ѿ����ӣ���ֹ��socket
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
		
		//�ر�BluetoothServerSocket
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
		//����Socket
		private final  BluetoothSocket mmSocket;
		//�����豸
		private final BluetoothDevice mmDevice;
		private String mSocketType;
		
		public ConnectThread(BluetoothDevice device) {
			mmDevice = device;
			BluetoothSocket tmp =null;
			
			//�õ�һ�������������豸��BluetoothSocket
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
			
			//ȡ���ɼ�״̬���������ӡ�
			mAdapter.cancelDiscovery();
			
			//����һ��BluetoothSocket����
			try{
				mmSocket.connect();
			} 
			//����쳣 ��ر�socket
			catch (IOException e)
			{
				try{
					mmSocket.close();
				}
				catch(IOException e2)
				{
					Log.i(TAG,"unable to close() " + mSocketType + "socket suring connection failure", e2);
				}
				//����ʧ��
				connectionFailed();
				//����������������״̬
				BluetoothChatService.this.start();
				return;
			}
			
			//���������ConnectThread
			synchronized(BluetoothChatService.this) {
				mConnectThread = null;
			}
			
			//����ConnectedThread(����������...)�߳�
			connected(mmSocket,  mmDevice);
		}
		//ȡ�������߳�ConnectThread
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
		//���������
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;
		
		public ConnectedThread(BluetoothSocket socket){
			Log.i(TAG, "create ConnectedThread");
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;
			//�õ�BluetoothSocket�����������
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
			
			//����������
			while (true) {
				try{
					//���������ж�ȡ����
					bytes = mmInStream.read(buffer);
					//����һ����Ϣ��UI�߳̽��и���
					mHandler.obtainMessage(Constant.MESSAGE_READ, bytes, -1, buffer).sendToTarget();
				}
				catch (IOException e)
				{
					//�����쳣�������Ӷ�ʧ
					Log.i(TAG, "disconnected", e);
					connectionLost();
					//Start the service over to restart listening mode
					BluetoothChatService.this.start();
					break;
				}
			}
		}
	    /**
	    * д��Ҫ���͵���Ϣ
	    * @param buffer  The bytes to write
	    */
		public void write(byte[] buffer) {
			try{
				mmOutStream.write(buffer);
				
				//��д����Ϣͬʱ���ݸ�UI����
				mHandler.obtainMessage(Constant.MESSAGE_WRITE, -1, -1, buffer).sendToTarget();
			}
			catch (IOException e)
			{
				Log.e(TAG, "Exception during write", e);
			}
		}
		
		//ȡ��ConnectedThread���ӹ����߳�
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