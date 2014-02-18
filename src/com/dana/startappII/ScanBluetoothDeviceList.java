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
	//蓝牙适配器
	private BluetoothAdapter mBtAdapter;
	//已配对的蓝牙设备
	private ArrayAdapter<String> mPairedDevicesAdapter;
	//未配对的蓝牙设备
	private ArrayAdapter<String> mNewDevicesAdapter;	
	
	private IntentFilter mfilter;
	
	private Button scanbtn;
	private ListView pairedDeviceslv, newDeviceslv;
	private TextView pairedDevicesTitle, newDevicesTitle;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//设置窗口
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.bluetooth_device_list);
		//Set result CANCELED in case the user backs out
		setResult(Activity.RESULT_CANCELED);
		
		scanbtn = (Button) findViewById(R.id.bluetooth_scan_btn);
		scanbtn.setOnClickListener(listener);
		
		pairedDevicesTitle = (TextView) findViewById(R.id.paired_devices_tv);
		newDevicesTitle = (TextView) findViewById(R.id.new_devices_tv);
		
		//初始化ArrayAdapter.已配对设备和新发现设备
		mPairedDevicesAdapter = new ArrayAdapter<String>(this,R.layout.bluetooth_info);
		mNewDevicesAdapter = new ArrayAdapter<String>(this, R.layout.bluetooth_info);
		
		//检测并设置已配对的设备列表
		pairedDeviceslv = (ListView) findViewById(R.id.paired_devices_lv);
		pairedDeviceslv.setAdapter(mPairedDevicesAdapter);
		pairedDeviceslv.setOnItemClickListener(itemClickListener);
		
		//检测并设置未配对的蓝牙设备列表
		newDeviceslv = (ListView) findViewById(R.id.new_devices_lv);
		newDeviceslv.setAdapter(mNewDevicesAdapter);
		newDeviceslv.setOnItemClickListener(itemClickListener);
		
		//当发现蓝牙设备时，需要注册一个广播
		mfilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, mfilter);
		
		//搜素设备完成时，需要注册一个广播
		mfilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(mReceiver, mfilter);
		
		//得到本地的蓝牙适配器
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		//得到已配对的设备
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
					//得到已经匹配的设备
					mPairedDevicesAdapter.clear();
					getPairedDevices();
					Log.i(TAG,"扫描周围设备");
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
		//如果有配对成功的设备添加到ArrayAdapter
		if(pairedDevices.size()>0)
		{
			Log.i(TAG,"添加已配对的设备");
			pairedDevicesTitle.setVisibility(View.VISIBLE);
			for(BluetoothDevice device:pairedDevices)
			{
				mPairedDevicesAdapter.add(device.getName() + "\n" + device.getAddress());
			}
		}
		//否则添加一个没有配对的字符串
		else
		{
//			String noDevices = getResources().getText(R.string.none_paired).toString();
			mPairedDevicesAdapter.add("No devices have been paired.");
		}
	}
	
	/**
	 * 搜索周围设备
	 */
	private void doDiscovery()
	{
		Log.i(TAG,"do Discovery");
		//设置显示进度条
		setProgressBarIndeterminateVisibility(true);
		//设置title为扫描状态
		setTitle("scanning for devices...");
		
		newDevicesTitle.setVisibility(View.VISIBLE);
		
		if(mBtAdapter.isDiscovering())
		{
			mBtAdapter.cancelDiscovery();
		}
		
		//搜索设备
		mBtAdapter.startDiscovery();
	}
	
	private OnItemClickListener itemClickListener = new OnItemClickListener()
	{
		public void onItemClick(AdapterView<?> parent, View v, int position, long id)
		{
			Log.i(TAG, "选中一个设备");
			//取消检测扫描发现设备的过程，释放资源
			if(mBtAdapter.isDiscovering())
				mBtAdapter.cancelDiscovery();
			
			//得到mac地址
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
				
				//创建一个包括Mac地址的Intent请求
				Intent intent = new Intent();
				intent.putExtra(EXTRA_DEVICE_ADDRESS, macAddress);
				
				//设置result并结束Activity
				setResult(Activity.RESULT_OK,intent);
				finish();
			}
		}
	};
	
	//注册广播，监听扫描蓝牙设备
	private final BroadcastReceiver mReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();
			//当发现一个设备时，
			if(BluetoothDevice.ACTION_FOUND.equals(action))
			{
				//从Intent得到蓝牙设备对象
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				//如果未配对，添加到设备列表
				if(device.getBondState() != BluetoothDevice.BOND_BONDED)
				{
					mNewDevicesAdapter.add(device.getName() + "\n" + device.getAddress());
				}
			}
			//扫描完成
			else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
			{
				//设置进度条不显示
				setProgressBarIndeterminateVisibility(false);
				setTitle("select a device to connect");
				//未发现蓝牙设备
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
		//注销注册的广播
		this.unregisterReceiver(mReceiver);
	}
}