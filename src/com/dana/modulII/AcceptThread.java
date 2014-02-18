package com.dana.modulII;

import java.io.IOException;
import java.util.UUID;

import com.dana.util.Constant;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


/**
 * This thread runs while listening for incoming connections. It behaves
 * like a server-side client. It runs until a connection is accepted
 * (or until cancelled).
 */
public class AcceptThread extends Thread
{
	//Debug
	private final static String TAG = "AcceptThread"; 
	
	//����socket����
	private final BluetoothServerSocket mbtServerSocket;
	private String mSocketType;
	
	private Context context;
	private Handler handler;
	//����������
	private final BluetoothAdapter mbtAdapter;
	private final UUID MY_UUID; 
	private final String NAME;
	//������������ǰ״̬
	private int mState;
	
	public AcceptThread(Context context, BluetoothAdapter adapter, int mState, String name, UUID MY_UUID, Handler handler)
	{
		this.context = context;
		this.handler = handler;
		this.mbtAdapter = adapter;
		this.mState = mState;
		this.NAME = name;
		this.MY_UUID = MY_UUID;
		
		BluetoothServerSocket tmp = null;
		//����һ���µ�socket�������
		try {
			tmp= adapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, "Socket Type:" + mSocketType + "listen() failed", e);
//			e.printStackTrace();
		}
		
		mbtServerSocket = tmp;
	}
	
	public void run()
	{
		Log.i(TAG, "Socket Type: " + mSocketType + "BEGIN mAcceptThread" + this);
		
		setName("AcceptThread" + mSocketType);
		
		Message msg = handler.obtainMessage();
		BluetoothSocket socket = null;
		//�����ǰû��������һֱ����socket����
		while(mState != Constant.STATE_CONNECTED)
		{
			//������������ӣ�����ܣ��������ã��������ӳɹ���һ���쳣
			try {
				socket = mbtServerSocket.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.i(TAG, "Socket Type:" + mSocketType + "accept() failed", e);
				break;
//				e.printStackTrace();
			}
			
			//�������һ������
			if(socket !=null)
			{
				synchronized(context)
				{
					switch(mState)
					{
						case Constant.STATE_LISTEN:
						case Constant.STATE_CONNECTING:
							msg.what = 0x95;
							msg.obj = socket;
							handler.sendMessage(msg);
//							//���״̬Ϊ�����������������У������connectedδ����
//							connected(socket, socket.getRemoteDevice());
							break;
							case Constant.STATE_NONE:
							case Constant.STATE_CONNECTED:
								//���û���豸�����Ѿ����ӣ���ֹ��socket
						try {
							socket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							Log.i(TAG,"Could not close unwanted socket", e);
//							e.printStackTrace();
						}
								break;
					}
				}
			}
		}
		
		Log.i(TAG, "END mAcceptThread, socket Type: " + mSocketType);
	}
	
	
	//�ر�BluetoothServerSocket
	public void cancel()
	{
		Log.i(TAG, "Socket Type: " + mSocketType + "cancel" + this);
		try {
			mbtServerSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, "Socket Type: " +mSocketType + "close() of server failed", e);
//			e.printStackTrace();
		}
	}
}