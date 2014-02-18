package com.dana.startappII;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dana.startappII.R;

public class ScanBluetoothDeviceList extends Activity
{
	//Debug
	private static final String TAG = "ScanBluetoothDeviceList";
	//Return Intent Extra
	public static final String EXTRA_DEVICE_ADDRESS = "bluetooth_device_address";
	//����������
	private BluetoothAdapter mBtAdapter;
	//����Ե������豸
	private ArrayAdapter<String> mPairedDevicesAdapter;
	//δ��Ե������豸
	private ArrayAdapter<String> mNewDevicesAdapter;	
	
	private IntentFilter mfilter;
	
	private Button scanbtn;
	private ListView pairedDeviceslv, newDeviceslv;
	private TextView pairedDevicesTitle, newDevicesTitle;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//���ô���
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.bluetooth_device_list);
		//Set result CANCELED in case the user backs out
		setResult(Activity.RESULT_CANCELED);
		
		scanbtn = (Button) findViewById(R.id.bluetooth_scan_btn);
		scanbtn.setOnClickListener(listener);
		
		pairedDevicesTitle = (TextView) findViewById(R.id.paired_devices_tv);
		newDevicesTitle = (TextView) findViewById(R.id.new_devices_tv);
		
		//��ʼ��ArrayAdapter.������豸���·����豸
		mPairedDevicesAdapter = new ArrayAdapter<String>(this,R.layout.bluetooth_info);
		mNewDevicesAdapter = new ArrayAdapter<String>(this, R.layout.bluetooth_info);
		
		//��Ⲣ��������Ե��豸�б�
		pairedDeviceslv = (ListView) findViewById(R.id.paired_devices_lv);
		pairedDeviceslv.setAdapter(mPairedDevicesAdapter);
		pairedDeviceslv.setOnItemClickListener(itemClickListener);
		
		//��Ⲣ����δ��Ե������豸�б�
		newDeviceslv = (ListView) findViewById(R.id.new_devices_lv);
		newDeviceslv.setAdapter(mNewDevicesAdapter);
		newDeviceslv.setOnItemClickListener(itemClickListener);
		
		//�����������豸ʱ����Ҫע��һ���㲥
		mfilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, mfilter);
		
		//�����豸���ʱ����Ҫע��һ���㲥
		mfilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(mReceiver, mfilter);
		
		//�õ����ص�����������
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		//�õ�����Ե��豸
		getPairedDevices();
	}
	
	private OnClickListener listener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			switch(v.getId())
			{
				case R.id.bluetooth_scan_btn:
					//�õ��Ѿ�ƥ����豸
					mPairedDevicesAdapter.clear();
					getPairedDevices();
					Log.i(TAG,"ɨ����Χ�豸");
					doDiscovery();
					v.setVisibility(View.GONE);
					break;
				default:
					break;
			}
		}
	};
	
	private void getPairedDevices()
	{
		Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
		//�������Գɹ����豸��ӵ�ArrayAdapter
		if(pairedDevices.size()>0)
		{
			Log.i(TAG,"�������Ե��豸");
			pairedDevicesTitle.setVisibility(View.VISIBLE);
			for(BluetoothDevice device:pairedDevices)
			{
				mPairedDevicesAdapter.add(device.getName() + "\n" + device.getAddress());
			}
		}
		//�������һ��û����Ե��ַ���
		else
		{
//			String noDevices = getResources().getText(R.string.none_paired).toString();
			mPairedDevicesAdapter.add("No devices have been paired.");
		}
	}
	
	/**
	 * ������Χ�豸
	 */
	private void doDiscovery()
	{
		Log.i(TAG,"do Discovery");
		//������ʾ������
		setProgressBarIndeterminateVisibility(true);
		//����titleΪɨ��״̬
		setTitle("scanning for devices...");
		
		newDevicesTitle.setVisibility(View.VISIBLE);
		
		if(mBtAdapter.isDiscovering())
		{
			mBtAdapter.cancelDiscovery();
		}
		
		//�����豸
		mBtAdapter.startDiscovery();
	}
	
	private OnItemClickListener itemClickListener = new OnItemClickListener()
	{
		public void onItemClick(AdapterView<?> parent, View v, int position, long id)
		{
			Log.i(TAG, "ѡ��һ���豸");
			//ȡ�����ɨ�跢���豸�Ĺ��̣��ͷ���Դ
			if(mBtAdapter.isDiscovering())
				mBtAdapter.cancelDiscovery();
			
			//�õ�mac��ַ
			String macInfo = ((TextView)v).getText().toString();
			if(macInfo.equals("No devices have been paired."))
			{
				Toast.makeText(ScanBluetoothDeviceList.this, "It's an invalid device, Please check it.", Toast.LENGTH_SHORT).show();
				return;
			}
			else if(macInfo.equals("No devices found."))
			{
				Toast.makeText(ScanBluetoothDeviceList.this, "It's an invalid device, Please check.", Toast.LENGTH_SHORT).show();
				return;
			}
			else
			{
				String macAddress = macInfo.substring(macInfo.length()-17);
				Log.i(TAG, "MAC address: " + macAddress);
				
				//����һ������Mac��ַ��Intent����
				Intent intent = new Intent();
				intent.putExtra(EXTRA_DEVICE_ADDRESS, macAddress);
				
				//����result������Activity
				setResult(Activity.RESULT_OK,intent);
				finish();
			}
		}
	};
	
	//ע��㲥������ɨ�������豸
	private final BroadcastReceiver mReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();
			//������һ���豸ʱ��
			if(BluetoothDevice.ACTION_FOUND.equals(action))
			{
				//��Intent�õ������豸����
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				//���δ��ԣ���ӵ��豸�б�
				if(device.getBondState() != BluetoothDevice.BOND_BONDED)
				{
					mNewDevicesAdapter.add(device.getName() + "\n" + device.getAddress());
				}
			}
			//ɨ�����
			else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
			{
				//���ý���������ʾ
				setProgressBarIndeterminateVisibility(false);
				setTitle("select a device to connect");
				//δ���������豸
				if(mNewDevicesAdapter.getCount() == 0)
				{
					mNewDevicesAdapter.add("No devices found.");
				}
			}
		}
	};
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if(mBtAdapter != null)
		{
			mBtAdapter.cancelDiscovery();
		}
		//ע��ע��Ĺ㲥
		this.unregisterReceiver(mReceiver);
	}
}