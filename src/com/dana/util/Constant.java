package com.dana.util;

public class Constant
{
	//BluetoothChatService״̬
	public static final int STATE_NONE = 0; // we're doing nothing
	public static final int STATE_LISTEN = 1; // now listening for incoming connections
	public static final int STATE_CONNECTING = 2; //now initiating an outgoing connection
	public static final int STATE_CONNECTED = 3; //now connected to a remote device
	
	//BluetoothChatService Handler ���͵���Ϣ����
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	
	//��BluetoothChatService Handler ������Ϣʱʹ�õļ�������-ֵģ�ͣ�
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";
}