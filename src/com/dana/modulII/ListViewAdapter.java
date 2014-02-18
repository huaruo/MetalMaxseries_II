package com.dana.modulII;

import java.util.ArrayList;
import java.util.List;

import com.dana.startappII.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter
{
	//Debug
	private final static String TAG = "ListViewAdapter";
	
	private int count;
	private Context context;
	List<String> items = new ArrayList<String>();
	
	public ListViewAdapter(final Context context, final List<String> currentData)
	{
		super();
		this.items = currentData;
		this.count = currentData.size();
		this.context = context;
	}
	
	public void setCount(int countValue)
	{
		count = countValue;
	}
	
	@Override
	public int getCount()
	{
		return count;
	}
	
	@Override
	public Object getItem(int position)
	{
		return position;
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view = LayoutInflater.from(context).inflate(R.layout.listviewload_page_item, null);
		TextView tvContent = (TextView) view.findViewById(R.id.lvlpi_tv);
		tvContent.setText(items.get(position));
		return view;
	}
}