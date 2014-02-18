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
// 获取所有按钮

Button buttonRead = (Button) this.findViewById(R.id.bt_read);
Button buttonSave = (Button) this.findViewById(R.id.bt_save);

// 为按钮添加事件
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
//        //使用下面的方法可以快速获取当前文件夹的位置，
//        //这样可以在后面追加路径从而避免使用绝对路径
//        //File filedir = this.getFilesDir();
//        Button button = (Button) this.findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() 
//        {
//			@Override
//			public void onClick(View v) 
//			{
//				//获取EditText中的内容
//				EditText filenameText = (EditText) findViewById(R.id.filename);
//				EditText contentText = (EditText) findViewById(R.id.filecontent);
//				String filename = filenameText.getText().toString();
//				String content = contentText.getText().toString();
//				try 
//				{
//					//使用通常文件保存方式,默认保存在data/data/包名/file/XXX里面
//					//fileService.save(filename, content);
//					
//					//判断sdcard是否存在于手机上而且没有写保护
//					//Android2.2版本以后sdcard的路径在mnt/sdcard，2.2之前在/sdcard
//					if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
//					{
//						//保存到SDCard中
//						fileService.saveToSDCard(filename, content);
//						//提示保存成功
//						Toast.makeText(MainActivity.this, R.string.success, 1).show();
//					}
//					else
//					{
//						//提示保存失败
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