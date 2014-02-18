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
	
	//Intent������루�������ӣ�����ɼ���
	private static final int REQUEST_CONNECT_DEVICE =1;
	private static final int REQUEST_ENABLE_BT = 2;
	
	//��Ҫ���ͳ�ȥ���ַ���
	private StringBuffer mOutStringBuffer;
	
	//��������������
	private BluetoothAdapter mbtAdapter = null;
	//�������Ķ���
	private BluetoothChatService mChatService = null;
	//���ӵ��豸������
	private String mConnectedDeviceName = null;
	//Array adapter for the conversation thread
	private ArrayAdapter<String> mConversationArrayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//���ô��ڲ���
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.bluetooth);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.custom_title);
		
		//�����Զ���title����
		menuTitletv=(TextView) findViewById(R.id.clt_menuTitle);
		menuTitletv.setVisibility(View.GONE);
		menuTitletv =(TextView) findViewById(R.id.clt_left_tv);
		menuTitletv.setVisibility(View.VISIBLE);
		menuTitletv.setText("Bluetooth Chat");
		menuTitletv =(TextView) findViewById(R.id.clt_right_tv);
		menuTitletv.setVisibility(View.VISIBLE);
		
		//��ʼ���༭��
		mOutet = (EditText) findViewById(R.id.bluetooth_message_et);
		//��ʼ�����Ͱ�ť��
		mSendbtn = (Button) findViewById(R.id.bluetooth_send_btn);
		
		//�õ�һ����������������
		mbtAdapter = BluetoothAdapter.getDefaultAdapter();
		//getDefaultAdapter()�������ڻ�ȡ��������������,Ȼ�����Ƿ�Ϊnull,���Ϊnull�����ʾû�������豸֧��
		if(mbtAdapter == null)
		{
			General.msgDialog(bc, "��ʾ", "Bluetooth is not available");
			finish();
			return;
		}		
	}
	//��onStart()���������ǽ���������Ƿ񱻴򿪣����û�д򿪣�������򿪣����������һЩ������Ϣ��׼������
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
			//�����ǰ״̬ΪSTATE_NONE,����Ҫ���������������
			if(mChatService.getState() == Constant.STATE_NONE)
			{
				//��ʼһ�������������
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
					//�����Ѿ��򿪣���������Ự
					setupChat();
				}
				else
				{
					//���ܴ�����
					Log.i(TAG,"Bluetooth is not enabled.");
					Toast.makeText(bc, "Bluetooth was not enabled. Leaving Bluetooth Chat.", Toast.LENGTH_SHORT).show();
				}
				break;
			case REQUEST_CONNECT_DEVICE:
				//��ScanBluetoothDeviceList�����豸����
				if(resultCode == Activity.RESULT_OK)
				{
					//��Intent�еõ��豸��MAC��ַ
					String address = data.getExtras().getString(ScanBluetoothDeviceList.EXTRA_DEVICE_ADDRESS);
					//�õ������豸����
					BluetoothDevice device= mbtAdapter.getRemoteDevice(address);
					try {
						if(device.getBondState() == BluetoothDevice.BOND_NONE)
						{
							Toast.makeText(bc, "Զ���豸���������������", Toast.LENGTH_LONG).show();
							//createBond
							boolean bondedStatus = ClsUtils.createBond(device.getClass(),device);
							Log.i(TAG, "���״̬: " + Boolean.toString(bondedStatus));
						}
						else if(device.getBondState() == BluetoothDevice.BOND_BONDED)
						{
							Toast.makeText(bc, device.getBondState() + " ...��������...", Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//��ʼ���豸��ִ����������
					mChatService = new BluetoothChatService(this, mHandler);
					//������������豸
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
							//����״̬Ϊ������
							menuTitletv.setText(R.string.title_connected_to);
							//����豸����
							menuTitletv.append(mConnectedDeviceName);
							if(mConversationArrayAdapter !=null)
								//���������¼
								mConversationArrayAdapter.clear();
							break;
						case Constant.STATE_CONNECTING:
							//������������
							menuTitletv.setText("connecting...");
							break;
						case Constant.STATE_LISTEN:
						case Constant.STATE_NONE:
							//���ڼ���״̬����û��׼��״̬������ʾû������
							menuTitletv.setText("not connected.");
							break;
						default:
							break;
					}
					break;
				case Constant.MESSAGE_WRITE:
					byte[] writeBuf = (byte[]) msg.obj;
					//���Լ�д��� ��ϢҲ��ʾ�������б���
					String writeMessage = new String(writeBuf);
					mConversationArrayAdapter.add("Me: " + writeMessage);
					break;
				case Constant.MESSAGE_READ:
					byte[] readBuf = (byte[]) msg.obj;
					//ȡ�����ݲ���ӵ�����Ի��б���
					String readMessage = new String(readBuf, 0, msg.arg1);
					mConversationArrayAdapter.add(mConnectedDeviceName + ": " + readMessage);
					break;
				case Constant.MESSAGE_DEVICE_NAME:
					//�������ӵ��豸����,����ʾһ��toast��ʾ
					mConnectedDeviceName = msg.getData().getString(Constant.DEVICE_NAME);
					Toast.makeText(getApplicationContext(), "Connect to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
					break;
				case Constant.MESSAGE_TOAST:
					//��������(����)ʧ�ܵ���Ϣ
					Toast.makeText(getApplicationContext(), msg.getData().getString(Constant.TOAST),Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
			}
		}
	};
	
	public void setupChat() {
		Log.i(TAG, "setupChat()");
		//��ʼ���Ի�����
		mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.bluetooth_info);
		//��ʼ���Ի���ʾ�б�
		mConversationlv = (ListView) findViewById(R.id.bluetooth_device_lv);
		//���öԻ���ʾ�б�Դ
		mConversationlv.setAdapter(mConversationArrayAdapter);
		//�����༭�����ڴ����س���������Ϣ	
		mOutet.setOnEditorActionListener(editorActionListener);
		mSendbtn.setOnClickListener(listener);
		
		//��ʼ��BluetoothChatService��ִ����������
		mChatService = new BluetoothChatService(this,mHandler);
		//��ʼ����Ҫ��������Ϣ���ַ���
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
					//��ʼ���ı���
//					TextView view = (TextView) findViewById(R.id.bluetooth_message_et);
					String message = mOutet.getText().toString();
					sendMessage(message);
					break;
			}
		}
	};
	
	private void sendMessage(String msg)
	{
		//����Ƿ�������״̬
		if(mChatService.getState() != Constant.STATE_CONNECTED)
		{
			Toast.makeText(this, "You are not connected to a device.", Toast.LENGTH_SHORT).show();
			return;
		}
		//����������Ϣ��Ϊ�ղŷ���,���򲻷���
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
			//���»س������Ҵ������������¼�ʱ������Ϣ
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
		//����ֵΪtrue,��ʾ �˵��ɼ�������ʾ�˵�
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
//		mbtAdapter = BluetoothAdapter.getDefaultAdapter();
		switch(item.getItemId())
		{
			case 1:
				//��������
				Log.i(TAG, "--- ��������  ---");
				if(!mbtAdapter.isEnabled())
				{
					Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
				}
				else
				{
					Toast.makeText(bc, "����������", Toast.LENGTH_SHORT).show();
				}
				return true;
			case 2:
				Log.i(TAG, "--- �����ѹر� ---");
				menuTitletv.setText("");
				if(mbtAdapter.isEnabled())
				{
					mbtAdapter.disable();
				}
				return true;
			case 3:
				Log.i(TAG, "--- ���ÿɱ���⵽ ---");
				//����δ����,��ʾ��������
				if(!mbtAdapter.isEnabled())
				{
					Toast.makeText(bc, "�������", 1000).show();
				}
				else if(mbtAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE)
				{
					//����ɼ�״̬
					Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
					//��Ӹ������ԣ��ɼ�״̬��ʱ��
					discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
					startActivity(discoverableIntent);
				}
				else
				{
					Toast.makeText(bc,"�����������Ա����", Toast.LENGTH_SHORT).show();
				}
				return true;
			case 4:
				Log.i(TAG,"--- ���������豸 ---");
				//����δ����,��ʾ��������
				if(!mbtAdapter.isEnabled())
				{
					Toast.makeText(bc, "�������", 1000).show();
				}
				else
				{
					//����DeviceListActivity�鿴�����豸
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