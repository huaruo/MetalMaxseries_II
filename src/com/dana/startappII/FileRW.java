package com.dana.startappII;

import com.dana.modulII.FileButtonOnClickEvent;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class FileRW extends Activity {
/** Called when the activity is first created. */

@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.fileservice);
// ��ȡ���а�ť

Button buttonRead = (Button) this.findViewById(R.id.bt_read);
Button buttonSave = (Button) this.findViewById(R.id.bt_save);

// Ϊ��ť����¼�
FileButtonOnClickEvent fileBtOnClickEve = new FileButtonOnClickEvent(this);
buttonRead.setOnClickListener(fileBtOnClickEve);
buttonSave.setOnClickListener(fileBtOnClickEve);

}

}

//package com.hoo.file;
//import android.app.Activity;
//import android.os.Bundle;
//import android.os.Environment;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//public class MainActivity extends Activity 
//{
//	private static final String TAG = "MainActivity";
//    private FileService fileService;
//    
//    @Override
//    public void onCreate(Bundle savedInstanceState) 
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//     
//        fileService = new FileService(this);
//        //ʹ������ķ������Կ��ٻ�ȡ��ǰ�ļ��е�λ�ã�
//        //���������ں���׷��·���Ӷ�����ʹ�þ���·��
//        //File filedir = this.getFilesDir();
//        Button button = (Button) this.findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() 
//        {
//			@Override
//			public void onClick(View v) 
//			{
//				//��ȡEditText�е�����
//				EditText filenameText = (EditText) findViewById(R.id.filename);
//				EditText contentText = (EditText) findViewById(R.id.filecontent);
//				String filename = filenameText.getText().toString();
//				String content = contentText.getText().toString();
//				try 
//				{
//					//ʹ��ͨ���ļ����淽ʽ,Ĭ�ϱ�����data/data/����/file/XXX����
//					//fileService.save(filename, content);
//					
//					//�ж�sdcard�Ƿ�������ֻ��϶���û��д����
//					//Android2.2�汾�Ժ�sdcard��·����mnt/sdcard��2.2֮ǰ��/sdcard
//					if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
//					{
//						//���浽SDCard��
//						fileService.saveToSDCard(filename, content);
//						//��ʾ����ɹ�
//						Toast.makeText(MainActivity.this, R.string.success, 1).show();
//					}
//					else
//					{
//						//��ʾ����ʧ��
//						Toast.makeText(MainActivity.this, R.string.sdcarderror, 1).show();
//					}
//				} 
//				catch (Exception e)
//				{
//					Log.e(TAG, e.toString());
//					Toast.makeText(MainActivity.this, R.string.error, 1).show();
//				}
//			}
//		});
//    }
//}