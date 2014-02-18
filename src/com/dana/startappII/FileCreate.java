package com.dana.startappII;

import java.io.File;
import java.io.IOException;

import com.dana.modulII.FileOperation;
import com.dana.util.General;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class FileCreate extends Activity
{
	//Debug
	private final static String TAG = "FileCreate";
	
	private EditText pathet, contentet;
	private Button savebtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//����ȫ��
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fileoperation);
		
		pathet = (EditText) findViewById(R.id.file_path_et);
		contentet = (EditText) findViewById(R.id.file_content_et);
		savebtn = (Button) findViewById(R.id.file_save_btn);
		savebtn.setOnClickListener(listener);
	}
	
	private OnClickListener listener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			switch(v.getId())
			{
				case R.id.file_save_btn:
					String path = pathet.getText().toString();
					if(path.equals("")||path==null)
					{
						General.msgDialog(FileCreate.this, "��ʾ", "�������ļ���");
						break;
					}
					String content = contentet.getText().toString();
//					���ڵ����������string.split("[.]") �����
//					�������ߵ������� string.split("\\|")�����
//					�����Ǻŵ������� string.split("\\*")�����
//					����б�ߵ������� sring.split("\\\\")�����
//					���������ŵ������� sring.split("\\[\\]")�����
//					�������ǰ��\\ת�塣
					String[] temp = path.split(File.separator);
					int i,size;
					size = temp.length;
					String file_path = "";
					for(i=0;i<size-1;i++)
					{
						file_path+=File.separator;
						file_path+=temp[i];
					}
					String file_name = temp[size-1];
					String sd_path = "";
					//·��������SDK��ĳ���ļ�����
					if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) //��ȡSDCard��״̬���ֻ�װ��SDCard�����ҿ��Խ��ж�д
					{
						sd_path = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();//getAbsolutePath()����·���� getpath()���·��
					}
					else
					{
						sd_path = FileCreate.this.getFilesDir().getPath();
					}
					if(!file_path.equals(""))
						file_path = sd_path + File.separator+"POSDB" + file_path;
					else
						file_path = sd_path + File.separator+ "POSDB";
					file_name = file_path+File.separator+file_name+".txt";
				try {
					FileOperation.CreateDir(file_path);
					FileOperation.CreateFile(file_name);
					FileOperation.Write(file_name, content);
					String str =FileOperation.Read(file_name);
					Log.i(TAG, "Read: " + "\n" + str);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
				default:
					break;
			}
				
		}
	};
}