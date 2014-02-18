package com.dana.startappII;

import java.util.List;

import com.dana.modulII.CreateDataFactory;
import com.dana.modulII.ListViewAdapter;
import com.dana.util.Algorithm;
import com.dana.util.General;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ListViewLoading extends Activity
{
	//Debug
	private final static String TAG = "ListViewLoading";
	
	private ListView listView;
	private View loadingView; //加载视图的布局
	
	private List<String> currentData;//当前视图显示的数据
	private int currentPage = 1; //当前页，默认为1
	private int pageSize = 10; //每页显示十条信息
	private int last_item_position;//最后item的位置
	
	private ListViewAdapter myListViewAdapter;
	
	private boolean isLoading = false;//是否加载过，控制加载次数
	private Thread mThread = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//设置全屏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.listviewload);
		
		listView = (ListView) findViewById(R.id.lvl_lv);
		//加载视图布局
		loadingView = LayoutInflater.from(this).inflate(R.layout.listviewload_page, null);
		//创建当前视图要显示的数据
		currentData = CreateDataFactory.createUpdateData(currentPage, pageSize);
		//添加底部加载视图
		listView.addFooterView(loadingView);
		//初始化适配器
		myListViewAdapter = new ListViewAdapter(this, currentData);
		listView.setAdapter(myListViewAdapter);
		//监听下拉列表选中菜单
		listView.setOnItemClickListener(itemClickListener);
		//监听选项触屏事件
		listView.setOnTouchListener(touchListener);
		//监听选项焦点改变
		listView.setOnFocusChangeListener(focusChangeListener);
		//监听滚动条
		listView.setOnScrollListener(scrollListener);
	}
	
	private OnItemClickListener itemClickListener = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long id)//position The position of the view in the adapter.  id The row id of the item that was clicked.
		{
			General.msgDialog(ListViewLoading.this, "提示", "选中第"+(position+1)+"项: " + currentData.get(position));
		}
	};
	
	private OnTouchListener touchListener = new OnTouchListener()
	{
		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			return false;
		}
	};
	
	private OnFocusChangeListener focusChangeListener = new OnFocusChangeListener()
	{
		@Override
		public void onFocusChange(View v, boolean hasFocus)
		{
			v.setVisibility(View.VISIBLE);
		}
	};
	
	private OnScrollListener scrollListener = new OnScrollListener()
	{
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
		{
			last_item_position = firstVisibleItem + visibleItemCount -1;
			
			if(last_item_position == totalItemCount-2)
			{
				//这里控制当焦点落在某一个位置时，开始加载。
				//在第9个位置开始加载时，改为totalItemCount-1
				//则会在第10个位置开始加载
				System.out.println("开始加载..");
				Toast.makeText(ListViewLoading.this, "第"+currentPage+"页", Toast.LENGTH_SHORT).show();
				
				if(!isLoading)
				{
					mThread = new Thread(runn);
					mThread.start();
				}
			}
			//当ListView没有FooterView时，添加FooterView
			if(listView.getFooterViewsCount()==0)
			{
				listView.addFooterView(loadingView);
			}
		}
		
		public void onScrollStateChanged(AbsListView view, int scrollState)
		{
			
		}
	};
	
	private Runnable runn = new Runnable()
	{
		@Override
		public void run()
		{
			isLoading = true;
			//开启一个线程加载数据，否则会堵塞当前线程
			updateCurrentData();
			//发送一个消息，通知数据加载完成
			loadingHandler.sendEmptyMessage(0);
		}
	};
	
	private Handler loadingHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			//修改adapter count
			myListViewAdapter.setCount(currentPage*pageSize);
			//更新
			myListViewAdapter.notifyDataSetChanged();
			//消除MSG
			loadingHandler.removeMessages(0);
			//删除FooterView
			listView.removeFooterView(loadingView);
			//进入下一页,此时视图未加载.
			isLoading = false;
		}
	};
	
	//添加List元素
	private void updateCurrentData()
	{
		//模拟联接网络以及从网络中获取数据花费的时间
		Algorithm.delay(20);
		//更新一次，当前页加1
		currentPage++;
		//获取当前要更新的数据
		List<String> updateDataList = CreateDataFactory.createUpdateData(currentPage, pageSize);
		//需要更新的数据假如当前数据集合
		for (String itemData:updateDataList)
		{
			currentData.add(itemData);
		}
	}
}