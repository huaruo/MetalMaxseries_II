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
	private View loadingView; //������ͼ�Ĳ���
	
	private List<String> currentData;//��ǰ��ͼ��ʾ������
	private int currentPage = 1; //��ǰҳ��Ĭ��Ϊ1
	private int pageSize = 10; //ÿҳ��ʾʮ����Ϣ
	private int last_item_position;//���item��λ��
	
	private ListViewAdapter myListViewAdapter;
	
	private boolean isLoading = false;//�Ƿ���ع������Ƽ��ش���
	private Thread mThread = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//����ȫ��
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.listviewload);
		
		listView = (ListView) findViewById(R.id.lvl_lv);
		//������ͼ����
		loadingView = LayoutInflater.from(this).inflate(R.layout.listviewload_page, null);
		//������ǰ��ͼҪ��ʾ������
		currentData = CreateDataFactory.createUpdateData(currentPage, pageSize);
		//��ӵײ�������ͼ
		listView.addFooterView(loadingView);
		//��ʼ��������
		myListViewAdapter = new ListViewAdapter(this, currentData);
		listView.setAdapter(myListViewAdapter);
		//���������б�ѡ�в˵�
		listView.setOnItemClickListener(itemClickListener);
		//����ѡ����¼�
		listView.setOnTouchListener(touchListener);
		//����ѡ���ı�
		listView.setOnFocusChangeListener(focusChangeListener);
		//����������
		listView.setOnScrollListener(scrollListener);
	}
	
	private OnItemClickListener itemClickListener = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long id)//position The position of the view in the adapter.  id The row id of the item that was clicked.
		{
			General.msgDialog(ListViewLoading.this, "��ʾ", "ѡ�е�"+(position+1)+"��: " + currentData.get(position));
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
				//������Ƶ���������ĳһ��λ��ʱ����ʼ���ء�
				//�ڵ�9��λ�ÿ�ʼ����ʱ����ΪtotalItemCount-1
				//����ڵ�10��λ�ÿ�ʼ����
				System.out.println("��ʼ����..");
				Toast.makeText(ListViewLoading.this, "��"+currentPage+"ҳ", Toast.LENGTH_SHORT).show();
				
				if(!isLoading)
				{
					mThread = new Thread(runn);
					mThread.start();
				}
			}
			//��ListViewû��FooterViewʱ�����FooterView
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
			//����һ���̼߳������ݣ�����������ǰ�߳�
			updateCurrentData();
			//����һ����Ϣ��֪ͨ���ݼ������
			loadingHandler.sendEmptyMessage(0);
		}
	};
	
	private Handler loadingHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			//�޸�adapter count
			myListViewAdapter.setCount(currentPage*pageSize);
			//����
			myListViewAdapter.notifyDataSetChanged();
			//����MSG
			loadingHandler.removeMessages(0);
			//ɾ��FooterView
			listView.removeFooterView(loadingView);
			//������һҳ,��ʱ��ͼδ����.
			isLoading = false;
		}
	};
	
	//���ListԪ��
	private void updateCurrentData()
	{
		//ģ�����������Լ��������л�ȡ���ݻ��ѵ�ʱ��
		Algorithm.delay(20);
		//����һ�Σ���ǰҳ��1
		currentPage++;
		//��ȡ��ǰҪ���µ�����
		List<String> updateDataList = CreateDataFactory.createUpdateData(currentPage, pageSize);
		//��Ҫ���µ����ݼ��統ǰ���ݼ���
		for (String itemData:updateDataList)
		{
			currentData.add(itemData);
		}
	}
}