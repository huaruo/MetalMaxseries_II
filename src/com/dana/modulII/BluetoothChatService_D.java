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
	private final Handler handler;
	
	private BluetoothChatService_D bcs = BluetoothChatService_D.this;
	
	//�������ӵļ����߳�
	private AcceptThread mAcceptThread;
	private AcceptThread mSecureAcceptThread;
	private AcceptThread mInsecureAcceptThread;
	
	//����һ���豸���߳�
	private ConnectThread mConnectThread;
	//�������Ӻ�Ĺ����߳�
	private ConnectedThread mConnectedThread;
	
	//������������ǰ״̬
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
		//����Handler
		this.handler = handler;
		//�õ���������������
		this.mAdapter = BluetoothAdapter.getDefaultAdapter();
		//���ó�ʼ״̬
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
		
		//״̬���º�ͬʱ����UI Activity
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
		//ȡ���κ��߳���ͼ������һ������
		if(mConnectThread !=null)
		{
			mConnectThread.cancel();
			mConnectThread = null;
		}
		
		//ȡ���κ��������е�����
		if(mConnectedThread != null)
		{
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		
		//����״̬Ϊ�������ȴ�����
		//����AcceptThread �߳�������BluetoothServerSocket
		if(mAcceptThread == null)
		{
			mAcceptThread = new AcceptThread(context, mAdapter, mState, NAME, MY_UUID, mHandler);
			mAcceptThread.start();
		}
		
		//����״̬Ϊ�������ȴ�����
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
		
		//ȡ���κ������̣߳���ͼ����һ������
		if(mState == Constant.STATE_CONNECTING)
		{
			if(mConnectThread != null)
			{
				mConnectThread.cancel();
				mConnectThread = null;
			}
		}
		//ȡ���κ����ڽ��е��߳�
		if(mConnectedThread !=null)
		{
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		
		//����һ�������߳������ƶ����豸
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
		//ȡ��ConnectThread�����߳�
		if(mConnectThread !=null)
		{
			mConnectThread.cancel();
			mConnectThread = null;
		}
		//ȡ�������������ӵ��߳�
		if(mConnectedThread != null)
		{
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		
		//��Ϊ�Ѿ���һ���豸��������,ȡ�����еļ����߳�
		if(mAcceptThread !=null)
		{
			mAcceptThread.cancel();
			mAcceptThread = null;
		}
		
		//����ConnectedThread�߳����������Ӻ�ִ��
		mConnectedThread = new ConnectedThread(context, socket, mHandler);
		mConnectedThread.start();
		
		//�������ӵ��豸����UI Activity����
		Message msg = handler.obtainMessage(Constant.MESSAGE_DEVICE_NAME);
		Bundle bundle = new Bundle();
		bundle.putString(Constant.DEVICE_NAME, device.getName());
		msg.setData(bundle);
		handler.sendMessage(msg);
		//״̬��Ϊ�Ѿ�����
		setState(Constant.STATE_CONNECTED);
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
		setState(Constant.STATE_NONE);
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
    		if (mState != Constant.STATE_CONNECTED) return;
    		r = mConnectedThread;
    		}
    	// ִ��д
    	r.write(out);
	}
	
	/**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
	private void connectionFailed()
	{
		setState(Constant.STATE_LISTEN);
		//��������ʧ�ܵ���Ϣ��UI����
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
		//����ʧ����Ϣ��UI����
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
					//���״̬Ϊ�����������������У������connected������
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
					//����ConnectedThread(���ڽ�����...)�߳�
					//???????????????��������
					connected(socket, socket.getRemoteDevice());
					break;
				case 0x98:
					mConnectThread = null;
					break;
				case 0x99:
					//����ʧ��
					connectionFailed();
					//����������������״̬
					bcs.start();
					break;
				default:
					break;
			}
		}
	};
}