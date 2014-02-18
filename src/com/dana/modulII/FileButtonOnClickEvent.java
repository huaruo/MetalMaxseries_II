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
* ��ť�¼���
* @author Administrator
*
*/

public class FileButtonOnClickEvent implements OnClickListener {
// ͨ��activity��ȡ�����ؼ�
private Activity activity;
// ͨ��FileService��д�ļ�
private FileService fileService;
// ��ӡ��Ϣ�õı�ǩ

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
// ��ȡ�ļ���
EditText etFileNameS = (EditText) this.activity.findViewById(R.id.et_file_name);
String fileNameS = etFileNameS.getText().toString();
// ��ȡ�ļ�����
EditText etFileConS = (EditText) this.activity.findViewById(R.id.et_file_content);

String fileContentS = etFileConS.getText().toString();
// ����
try {

this.fileService.save(fileNameS, fileContentS);
// �ڴ�������ʾһ����Ч��Ϣ��
Toast.makeText(this.activity, R.string.file_save_success,Toast.LENGTH_LONG).show();
Log.i(TAG, "save file success!");
} catch (Exception e) {
Toast.makeText(this.activity, R.string.file_save_failed,
Toast.LENGTH_LONG).show();
Log.e(TAG, e.toString());
}

break;

case R.id.bt_read:

// ��ȡ�ļ���
EditText etFileNameR = (EditText) this.activity.findViewById(R.id.et_file_name);

String fileNameR = etFileNameR.getText().toString();

// ��ȡ�ļ�
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
