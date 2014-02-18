package com.dana.util;

public class Constant
{
	//BluetoothChatService状态
	public static final int STATE_NONE = 0; // we're doing nothing
	public static final int STATE_LISTEN = 1; // now listening for incoming connections
	public static final int STATE_CONNECTING = 2; //now initiating an outgoing connection
	public static final int STATE_CONNECTED = 3; //now connected to a remote device
	
	//BluetoothChatService Handler 发送的消息类型
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	
	//从BluetoothChatService Handler 接收消息时使用的键名（键-值模型）
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";
}