package com.dana.startappII;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dana.modulII.ExcelOperation;

public class ExcelFile extends Activity
{
	//Debug
	private final static String TAG = "ExcelFile";
	
	private Button createExcelbtn;
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.excelfile);
		
		createExcelbtn = (Button) findViewById(R.id.ef_create_btn);
		createExcelbtn.setOnClickListener(listener);
	}
	
	private OnClickListener listener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			
			String filename = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+"POSDB" + File.separator + "test.xls";
			String str = "";
			switch(v.getId())
			{
				case R.id.ef_create_btn:
				try {
					ExcelOperation.CreateExcelSheet(filename, "testbt", 0);
					ExcelOperation.WriteExcel(filename,"testbt", 0,0, "Пе");
					ExcelOperation.WriteExcelNumber(filename, "testbt", 0, 1, 3);
					str = ExcelOperation.ReadExcel(filename, "testbt", 0, 1);
					Log.i(TAG, str);
					str = ExcelOperation.getExcelSheet(filename, "testbt");
					Log.i(TAG, str);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.i(TAG, e.getMessage());
					e.printStackTrace();
				}				
					break;
				default:
					break;
			}
		}
	};
}