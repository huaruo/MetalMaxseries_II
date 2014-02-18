package com.dana.startappII;

import com.dana.modulII.ListUpdateAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ListUpdate extends Activity
{
	//Debug
	private final static String TAG = "ListUpdate";
	private ListView listview;
	private TextView textview;
	private Button addbtn;
	
	private ListUpdateAdapter adapter;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//		//…Ë÷√»´∆¡
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.listupdate);
		listview = (ListView) findViewById(R.id.listupdate_lv);
		addbtn = (Button) findViewById(R.id.listupdate_add);
		addbtn.setOnClickListener(listener);
		
		adapter = new ListUpdateAdapter(this, mHandler);
		listview.setAdapter(adapter);
	}
	
	private OnClickListener listener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			switch(v.getId())
			{
				case R.id.listupdate_add:
					adapter.addListString("");
					adapter.notifyDataSetChanged();
					break;
				default:
					break;
			}
		}
	};
	
	private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
//			Bundle bundle = msg.getData();
			switch(msg.what)
			{
				case 0x99:
					adapter.notifyDataSetChanged();
					break;
				default:
					break;
			}
		}
	};
}