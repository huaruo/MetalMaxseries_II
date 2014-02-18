package com.dana.startappII;

import java.util.ArrayList;
import java.util.HashMap;

import com.dana.customII.CustomWindowActivity;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

public class Main extends CustomWindowActivity
{
	//Debug
	private static final String TAG = "Main";
	
	//控件
	private LinearLayout imgbtn1, imgbtn2;
	
	//定义网格菜单的图片文字
	private int[] menuImage = null;
	private String[] menuName = null;
	
	private Intent intent;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.i(TAG, "++ 创建视图 ++");
		
		//CustomWindowActivity时屏蔽
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
		Log.i(TAG,"自定义标题");

		//CustomWindowActivity时屏蔽
//		//将ImageView和TextView封装在LinearLayout内，设置点击属性。形成按钮
//		imgbtn1 = (LinearLayout) findViewById(R.id.clt_imgbtn1);
//		imgbtn1.setVisibility(View.VISIBLE);
//		imgbtn1.setClickable(true);
//		imgbtn1.setOnClickListener(listener);
		
		menuName = getResources().getStringArray(R.array.items_array);//获取字符串数组内容
		int size = menuName.length;
		menuImage = new int[size];
		for(int k=0; k<size; k++)
		{
			menuImage[k] = R.drawable.image;
		}
		
		//建立网格
		GridView gridView = (GridView) findViewById(R.id.main_gv);
		gridView.setVisibility(View.VISIBLE);
		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
		Log.i(TAG, "显示图样");
		
		for(int i=0; i<size; i++)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage",menuImage[i]);
			map.put("ItemText", menuName[i]);
			lstImageItem.add(map);
		}
		//初始化网格
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, lstImageItem,R.layout.grid_item,
			new String[]{"ItemImage", "ItemText"}, new int[]{R.id.gvitem_image,R.id.gvitem_tv});
		gridView.setAdapter(simpleAdapter);
		gridView.setOnItemClickListener(itemClickListener);
	}
	private OnClickListener listener = new OnClickListener()
	{
		public void onClick(View v)
		{
			switch(v.getId())
			{
				case R.id.clt_imgbtn1:
					finish();
					break;
				default:
					break;
			}
		}
	};
	
	private OnItemClickListener itemClickListener = new OnItemClickListener()
	{
		public void onItemClick(AdapterView<?> parent,
				View v, int position, long id)
		{
			Log.i(TAG, menuName[position]);
			switch(position)
			{
				case 0:
					intent = new Intent(Main.this,CanvasSurfaceView.class);
					startActivity(intent);
					break;
				case 1:
					intent = new Intent(Main.this, PaintSurfaceView.class);
					startActivity(intent);
					break;
				case 2:
					intent = new Intent(Main.this, BitmapSurfaceView.class);
					startActivity(intent);
					break;
				case 3:
					intent = new Intent(Main.this, ClipSurfaceView.class);
					startActivity(intent);
					break;
				case 4:
					intent = new Intent(Main.this, ListViewLoading.class);
					startActivity(intent);
					break;
				case 5:
					intent = new Intent(Main.this, WeiXinSimulation.class);
					startActivity(intent);
					break;
				case 6:
					intent = new Intent(Main.this, DragIcon.class);
					startActivity(intent);
					break;
				case 7:
					intent = new Intent(Main.this, ListUpdate.class);
					startActivity(intent);
					break;
				case 8:
					intent = new Intent(Main.this, BluetoothChat.class);
					startActivity(intent);
					break;
				case 9:
					intent = new Intent(Main.this, FileCreate.class);
					startActivity(intent);
					break;
				case 10:
					intent = new Intent(Main.this, FileRW.class);
					startActivity(intent);
					break;
				case 11:
					intent = new Intent(Main.this, WaveDiagram.class);
					startActivity(intent);
					break;
				case 12:
					intent = new Intent(Main.this, HanZiPinYin.class);
					startActivity(intent);
					break;
				case 13:
					intent = new Intent(Main.this, ExcelFile.class);
					startActivity(intent);
					break;
				default:
					break;
			}
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		menu.add(0,1,0,"启动").setIcon(R.drawable.image);
		menu.add(0,2,0,"退出").setIcon(R.drawable.image);
		//返回值为true,表示菜单可见，即显示菜单
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
		switch(item.getItemId())
		{
			case 1:
				new AlertDialog.Builder(this).setTitle("标题").setMessage("开始").show();
				break;
			case 2:
				//finish();
				System.exit(0);
				break;
			default:
				break;
		}
		return true;
	}
	
}