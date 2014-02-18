package com.dana.modulII;

import java.util.ArrayList;
import java.util.List;

import com.dana.startappII.R;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;

public class ListUpdateAdapter extends BaseAdapter
{
	//Debug
	private final static String TAG = "ListUpdateAdapter";
	
	private Context context;
	private LayoutInflater inflater;
	private List<String> infoarr;
	private Handler handler;
	
	public ListUpdateAdapter(Context context, Handler handler)
	{
		super();
		this.context = context;
		this.handler = handler;
		inflater = LayoutInflater.from(context);
		infoarr = new ArrayList<String>();
		for(int i = 0; i<3; i++)
		{
			//listview初始化三个子项
			infoarr.add("");
		}
	}
		
		public int getCount()
		{
			return infoarr.size();
		}
		
		public Object getItem(int position)
		{
			return position;
		}
		
		public long getItemId(int position)
		{
			return position;
		}
		
		public List<String> addListString(String str)
		{
			infoarr.add(str);
			return infoarr;
		}
		
		public View getView(final int position, View v, ViewGroup parent)
		{
			if(v == null)
			{
				v = inflater.inflate(R.layout.listupdate_item, null);
			}
			final EditText edit = (EditText) v.findViewById(R.id.listupdate_item_et);
			edit.setText(infoarr.get(position)); //在重构adapter时不至于数据错乱
			Button del = (Button) v.findViewById(R.id.listupdate_item_del);
			edit.setOnFocusChangeListener(new OnFocusChangeListener()
			{
				@Override
				public void onFocusChange(View v, boolean hasFocus)
				{
					if(infoarr.size()>0)
					{
						infoarr.set(position, edit.getText().toString());
					}
				}
			});
			
			del.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					edit.requestFocus();//edit获得焦点，删除不会出错
					infoarr.remove(position);
					Message msg = handler.obtainMessage();
					msg.what = 0x99;
//					msg.obj = "";
//					Bundle bundle = new Bundle();
//					bundle.putBoolean("is_from_thread", true);
//					bundle.putString("track1", track1);
//					bundle.putString("track2", track2);
//					bundle.putString("track3", track3);
//					msg.setData(bundle);
					handler.sendMessage(msg);
				}
			});
			
			return v;
		}
		
}