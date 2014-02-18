package com.dana.modulII;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dana.startappII.R;

/**
* 按钮事件类
* @author Administrator
*
*/

public class FileButtonOnClickEvent implements OnClickListener {
// 通过activity获取其他控件
private Activity activity;
// 通过FileService读写文件
private FileService fileService;
// 打印信息用的标签

private static final String TAG = "FileButtonOnClickEvent";
public FileButtonOnClickEvent(Activity activity) {
this.fileService = new FileService(activity);
this.activity = activity;
}

@Override
public void onClick(View v) {
Button button = (Button) v;
switch (button.getId()) {
case R.id.bt_save:
// 获取文件名
EditText etFileNameS = (EditText) this.activity.findViewById(R.id.et_file_name);
String fileNameS = etFileNameS.getText().toString();
// 获取文件内容
EditText etFileConS = (EditText) this.activity.findViewById(R.id.et_file_content);

String fileContentS = etFileConS.getText().toString();
// 保存
try {

this.fileService.save(fileNameS, fileContentS);
// 在窗口中显示一个特效信息框
Toast.makeText(this.activity, R.string.file_save_success,Toast.LENGTH_LONG).show();
Log.i(TAG, "save file success!");
} catch (Exception e) {
Toast.makeText(this.activity, R.string.file_save_failed,
Toast.LENGTH_LONG).show();
Log.e(TAG, e.toString());
}

break;

case R.id.bt_read:

// 获取文件名
EditText etFileNameR = (EditText) this.activity.findViewById(R.id.et_file_name);

String fileNameR = etFileNameR.getText().toString();

// 读取文件
try {

String fielContentR = this.fileService.readFile(fileNameR);
EditText etFileConR = (EditText) this.activity.findViewById(R.id.et_file_content);
etFileConR.setText(fielContentR);
Log.i(TAG, "read file success!");

} catch (Exception e) {
Toast.makeText(this.activity, R.string.file_read_failed,
Toast.LENGTH_LONG).show();
Log.e(TAG, e.toString());
}
break;
default:
break;
}
}

}
