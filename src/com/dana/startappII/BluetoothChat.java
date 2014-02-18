package com.dana.startappII;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.dana.modulII.BluetoothChatService;
import com.dana.modulII.ClsUtils;
import com.dana.util.Constant;
import com.dana.util.General;

public class BluetoothChat extends Activity
{
	//Debug
	private static final String TAG = "BluetoothChat";
	
	private BluetoothChat bc = BluetoothChat.this;
	
	private TextView menuTitletv;
	private ListView mConversationlv;
	private EditText mOutet;
	private Button mSendbtn;
	
	//Intent请求代码（请求链接，请求可见）
	private static final int REQUEST_CONNECT_DEVICE =1;
	private static final int REQUEST_ENABLE_BT = 2;
	
	//将要发送出去的字符串
	private StringBuffer mOutStringBuffer;
	
	//本地蓝牙适配器
	private BluetoothAdapter mbtAdapter = null;
	//聊天服务的对象
	private BluetoothChatService mChatService = null;
	//链接的设备的名称
	private String mConnectedDeviceName = null;
	//Array adapter for the conversation thread
	private ArrayAdapter<String> mConversationArrayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//设置窗口布局
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.bluetooth);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.custom_title);
		
		//设置自定义title布局
		menuTitletv=(TextView) findViewById(R.id.clt_menuTitle);
		menuTitletv.setVisibility(View.GONE);
		menuTitletv =(TextView) findViewById(R.id.clt_left_tv);
		menuTitletv.setVisibility(View.VISIBLE);
		menuTitletv.setText("Bluetooth Chat");
		menuTitletv =(TextView) findViewById(R.id.clt_right_tv);
		menuTitletv.setVisibility(View.VISIBLE);
		
		//初始化编辑框
		mOutet = (EditText) findViewById(R.id.bluetooth_message_et);
		//初始化发送按钮，
		mSendbtn = (Button) findViewById(R.id.bluetooth_send_btn);
		
		//得到一个本地蓝牙适配器
		mbtAdapter = BluetoothAdapter.getDefaultAdapter();
		//getDefaultAdapter()函数用于获取本地蓝牙适配器,然后检测是否为null,如果为null，则表示没有蓝牙设备支持
		if(mbtAdapter == null)
		{
			General.msgDialog(bc, "提示", "Bluetooth is not available");
			finish();
			return;
		}		
	}
	//在onStart()函数后，我们将检测蓝牙是否被打开，如果没有打开，则请求打开，否则就设置一些聊天信息的准备工作
	@Override
	public void onStart()
	{
		super.onStart();
		Log.i(TAG,"++ ON START ++");
	}
	
	@Override
	public synchronized void onResume()
	{
		super.onResume();
		Log.i(TAG, "+++ ON RESUME +++");
		// Performing this check in onResume() covers the case in which BT was
	    // not enabled during onStart(), so we were paused to enable it...
	    // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
		if(mChatService !=null)
		{
			//如果当前状态为STATE_NONE,则需要开启蓝牙聊天服务
			if(mChatService.getState() == Constant.STATE_NONE)
			{
				//开始一个蓝牙聊天服务
				mChatService.start();
			}
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		Log.i(TAG, "onActivityResult" +  resultCode);
		switch(requestCode)
		{
			case REQUEST_ENABLE_BT:
				if(resultCode == Activity.RESULT_OK)
				{
					//蓝牙已经打开，设置聊天会话
					setupChat();
				}
				else
				{
					//不能打开蓝牙
					Log.i(TAG,"Bluetooth is not enabled.");
					Toast.makeText(bc, "Bluetooth was not enabled. Leaving Bluetooth Chat.", Toast.LENGTH_SHORT).show();
				}
				break;
			case REQUEST_CONNECT_DEVICE:
				//当ScanBluetoothDeviceList返回设备链接
				if(resultCode == Activity.RESULT_OK)
				{
					//从Intent中得到设备的MAC地址
					String address = data.getExtras().getString(ScanBluetoothDeviceList.EXTRA_DEVICE_ADDRESS);
					//得到蓝牙设备对象
					BluetoothDevice device= mbtAdapter.getRemoteDevice(address);
					try {
						if(device.getBondState() == BluetoothDevice.BOND_NONE)
						{
							Toast.makeText(bc, "远程设备发送蓝牙配对请求", Toast.LENGTH_LONG).show();
							//createBond
							boolean bondedStatus = ClsUtils.createBond(device.getClass(),device);
							Log.i(TAG, "配对状态: " + Boolean.toString(bondedStatus));
						}
						else if(device.getBondState() == BluetoothDevice.BOND_BONDED)
						{
							Toast.makeText(bc, device.getBondState() + " ...正在连接...", Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//初始化设备，执行蓝牙连接
					mChatService = new BluetoothChatService(this, mHandler);
					//尝试链接这个设备
					mChatService.connect(device);
				}	
				break;
			default:
				break;
		}
	}
	
	//The Handler that gets information back from the BluetoothChatServic
	private final Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
				case Constant.MESSAGE_STATE_CHANGE:
					Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
					switch(msg.arg1)
					{
						case Constant.STATE_CONNECTED:
							//设置状态为已连接
							menuTitletv.setText(R.string.title_connected_to);
							//添加设备名称
							menuTitletv.append(mConnectedDeviceName);
							if(mConversationArrayAdapter !=null)
								//清理聊天记录
								mConversationArrayAdapter.clear();
							break;
						case Constant.STATE_CONNECTING:
							//设置正在链接
							menuTitletv.setText("connecting...");
							break;
						case Constant.STATE_LISTEN:
						case Constant.STATE_NONE:
							//出于监听状态或者没有准备状态，则显示没有链接
							menuTitletv.setText("not connected.");
							break;
						default:
							break;
					}
					break;
				case Constant.MESSAGE_WRITE:
					byte[] writeBuf = (byte[]) msg.obj;
					//将自己写入的 消息也显示到合适列表中
					String writeMessage = new String(writeBuf);
					mConversationArrayAdapter.add("Me: " + writeMessage);
					break;
				case Constant.MESSAGE_READ:
					byte[] readBuf = (byte[]) msg.obj;
					//取得内容并添加到聊天对话列表中
					String readMessage = new String(readBuf, 0, msg.arg1);
					mConversationArrayAdapter.add(mConnectedDeviceName + ": " + readMessage);
					break;
				case Constant.MESSAGE_DEVICE_NAME:
					//保存链接的设备名称,并显示一个toast提示
					mConnectedDeviceName = msg.getData().getString(Constant.DEVICE_NAME);
					Toast.makeText(getApplicationContext(), "Connect to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
					break;
				case Constant.MESSAGE_TOAST:
					//处理链接(发送)失败的消息
					Toast.makeText(getApplicationContext(), msg.getData().getString(Constant.TOAST),Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
			}
		}
	};
	
	public void setupChat() {
		Log.i(TAG, "setupChat()");
		//初始化对话进程
		mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.bluetooth_info);
		//初始化对话显示列表
		mConversationlv = (ListView) findViewById(R.id.bluetooth_device_lv);
		//设置对话显示列表源
		mConversationlv.setAdapter(mConversationArrayAdapter);
		//监听编辑框，用于处理按回车键发送消息	
		mOutet.setOnEditorActionListener(editorActionListener);
		mSendbtn.setOnClickListener(listener);
		
		//初始化BluetoothChatService并执行蓝牙连接
		mChatService = new BluetoothChatService(this,mHandler);
		//初始化将要发出的消息的字符串
		mOutStringBuffer = new StringBuffer("");
	}
	
	@Override
	public synchronized void onPause()
	{
		super.onPause();
		Log.i(TAG, "-- ON PAUSE --");
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		Log.e(TAG, "-- ON STOP --");
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		//Stop the Bluetooth chat services
		if(mChatService != null)
			mChatService.stop();
		Log.i(TAG, "-- ON DESTROY --");
	}
	
	private OnClickListener listener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			switch(v.getId())
			{
				case R.id.bluetooth_send_btn:
					//初始化文本框
//					TextView view = (TextView) findViewById(R.id.bluetooth_message_et);
					String message = mOutet.getText().toString();
					sendMessage(message);
					break;
			}
		}
	};
	
	private void sendMessage(String msg)
	{
		//检查是否处于连接状态
		if(mChatService.getState() != Constant.STATE_CONNECTED)
		{
			Toast.makeText(this, "You are not connected to a device.", Toast.LENGTH_SHORT).show();
			return;
		}
		//如果输入的消息不为空才发送,否则不发送
		if(msg.length() > 0)
		{
			//Get the message bytes and tell the BluetoothChatService to write
			byte[] send = msg.getBytes();
			mChatService.write(send);
			//Reset out string buffer to zero and clear the edit text field
			mOutStringBuffer.setLength(0);
			mOutet.setText(mOutStringBuffer);
		}
	}
	
	//The action listener for the EditText widget, to listen for the return key
	private OnEditorActionListener editorActionListener = new OnEditorActionListener()
	{
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
		{
			//按下回车键并且触发按键弹起事件时发送消息
			if(actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP)
			{
				String message = v.getText().toString();
				sendMessage(message);
			}
			Log.i(TAG, "END onEditorAction");
			return true;
		}
	};
	
	private void connectDevice(Intent data, boolean secure)
	{
		//Get the device MAC address
		String address = data.getExtras().getString(ScanBluetoothDeviceList.EXTRA_DEVICE_ADDRESS);
		//Get the BluetoothDevice object
		BluetoothDevice device = mbtAdapter.getRemoteDevice(address);
		//Attempt to connect to the device
		mChatService.connect(device);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		menu.add(0,1,0,"Open Bluetooth").setIcon(android.R.drawable.ic_menu_compass);
		menu.add(0,2,0,"Close Bluetooth").setIcon(android.R.drawable.ic_notification_clear_all);
		menu.add(0,3,0,"Make Discoverable").setIcon(android.R.drawable.ic_menu_view);
		menu.add(0,4,0,"Connect a Device").setIcon(android.R.drawable.ic_menu_search);
		menu.add(0,5,0,"Exit").setIcon(android.R.drawable.ic_lock_power_off);
		//返回值为true,表示 菜单可见，即显示菜单
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
//		mbtAdapter = BluetoothAdapter.getDefaultAdapter();
		switch(item.getItemId())
		{
			case 1:
				//启动蓝牙
				Log.i(TAG, "--- 启动蓝牙  ---");
				if(!mbtAdapter.isEnabled())
				{
					Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
				}
				else
				{
					Toast.makeText(bc, "蓝牙已启动", Toast.LENGTH_SHORT).show();
				}
				return true;
			case 2:
				Log.i(TAG, "--- 蓝牙已关闭 ---");
				menuTitletv.setText("");
				if(mbtAdapter.isEnabled())
				{
					mbtAdapter.disable();
				}
				return true;
			case 3:
				Log.i(TAG, "--- 设置可被检测到 ---");
				//蓝牙未启动,提示开启蓝牙
				if(!mbtAdapter.isEnabled())
				{
					Toast.makeText(bc, "请打开蓝牙", 1000).show();
				}
				else if(mbtAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE)
				{
					//请求可见状态
					Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
					//添加附加属性，可见状态的时间
					discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
					startActivity(discoverableIntent);
				}
				else
				{
					Toast.makeText(bc,"本机蓝牙可以被检测", Toast.LENGTH_SHORT).show();
				}
				return true;
			case 4:
				Log.i(TAG,"--- 搜索蓝牙设备 ---");
				//蓝牙未启动,提示开启蓝牙
				if(!mbtAdapter.isEnabled())
				{
					Toast.makeText(bc, "请打开蓝牙", 1000).show();
				}
				else
				{
					//启动DeviceListActivity查看蓝牙设备
					Intent serverIntent = new Intent(this, ScanBluetoothDeviceList.class);
					startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
				}
				return true;
			case 5:
				if(mbtAdapter.isEnabled())
				{
					mbtAdapter.disable();
				}
				finish();
				return true;
		}
		return false;
	}
	
	
}