package com.dana.startappII;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dana.modulII.HanZiToPinYin;
import com.dana.util.General;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class HanZiPinYin extends Activity
{
	//Debug
	private final static String TAG = "HanZiPinYin";
	private EditText hanziet;
	private TextView pinyintv;
	private Button readbtn, utf16btn, utf16tohanzibtn, utf8btn, asc2hanzibtn, hanzi2ascbtn, gbk2hanzibtn, gethanzibtn, utf8ToGb2312btn, gb2312ToUtf8btn;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hanzipinyin);
		hanziet = (EditText) findViewById(R.id.hzpy_hz_et);
		pinyintv = (TextView) findViewById(R.id.hzpy_py_tv);
		readbtn = (Button) findViewById(R.id.hzpy_read_btn);
		readbtn.setOnClickListener(listener);
		utf16btn = (Button) findViewById(R.id.hzpy_utf16_btn);
		utf16btn.setOnClickListener(listener);
		utf16tohanzibtn = (Button) findViewById(R.id.hzpy_utf16tohanzi_btn);
		utf16tohanzibtn.setOnClickListener(listener);
		utf8btn = (Button) findViewById(R.id.hzpy_utf8_btn);
		utf8btn.setOnClickListener(listener);
		asc2hanzibtn = (Button) findViewById(R.id.hzpy_asc2hanzi_btn);
		asc2hanzibtn.setOnClickListener(listener);
		hanzi2ascbtn = (Button) findViewById(R.id.hzpy_hanzi2asc_btn);
		hanzi2ascbtn.setOnClickListener(listener);
		gbk2hanzibtn = (Button) findViewById(R.id.hzpy_gbk2hanzi_btn);
		gbk2hanzibtn.setOnClickListener(listener);
		gethanzibtn = (Button) findViewById(R.id.hzpy_gethanzi_btn);
		gethanzibtn.setOnClickListener(listener);
		utf8ToGb2312btn = (Button) findViewById(R.id.hzpy_utf8togb2312_btn);
		utf8ToGb2312btn.setOnClickListener(listener);
		gb2312ToUtf8btn = (Button) findViewById(R.id.hzpy_gb2312toutf8_btn);
		gb2312ToUtf8btn.setOnClickListener(listener);
	}
	
	private OnClickListener listener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			String originalStr = "";
			String str = "";
			int i, size;
			originalStr = hanziet.getText().toString();
			Pattern p_str;
			Matcher m;
			String[] strs;
			pinyintv.setTextSize(24);
			pinyintv.setTextColor(getResources().getColor(R.color.white));
			switch(v.getId())
			{
				case R.id.hzpy_read_btn:
					if(originalStr==null || originalStr.equals(""))
					{
						General.msgDialog(HanZiPinYin.this, "提示", "请输入汉字");
						return;
					}
//					//汉字在   Unicode   里面有单独的几块区域，是中日韩（朝鲜）共享的。   
//					  以下两段   
//					  U+4e00   ~   U+9FB0   原来   GB2312   和   GBK   中的汉字   
//					  U+3400   ~   U+4DB6   包括   GB18030.2000   中那些增加的汉字  
					p_str = Pattern.compile("[\\u4e00-\\u9fa5]+");
					m = p_str.matcher(originalStr);
					if(!(m.find()&&m.group(0).equals(originalStr)))
					{//判断不是汉字
						General.msgDialog(HanZiPinYin.this, "提示", "请输入汉字");
						return;
					}
					size=originalStr.length();
					for(i=0; i<size; i++)
					{
						str += HanZiToPinYin.toPinYin(originalStr.charAt(i));
						str += " ";
					}
					pinyintv.setText(str);
					break;
				case R.id.hzpy_utf16_btn:
					if(originalStr==null || originalStr.equals(""))
					{
						General.msgDialog(HanZiPinYin.this, "提示", "请输入汉字");
						return;
					}
					p_str = Pattern.compile("[\\u4e00-\\u9fa5]+");
					m = p_str.matcher(originalStr);
					if(!(m.find()&&m.group(0).equals(originalStr)))
					{//判断不是汉字
						General.msgDialog(HanZiPinYin.this, "提示", "请输入汉字");
						return;
					}
					str = "\"" + originalStr + "\" 的utf16编码： ";
					str += HanZiToPinYin.toUtf16(originalStr);
					pinyintv.setText(str);
					break;
				case R.id.hzpy_utf16tohanzi_btn:
					originalStr = "&#x8fa3;&#x6912;&#x7c89;&#x6253;&#x591a;&#x5c11;&#x5206;"; 
					//originalStr = "&#23665;&#19996;&#20020;&#27778;&#19968;" //分割时用"&#"，向整数转换时用Integer.parseInt(xx,10);
					str = HanZiToPinYin.utf16ToHanZi(originalStr, "&#x");
					pinyintv.setText(originalStr + " = " + str);
					break;
				case R.id.hzpy_utf8_btn:
					if(originalStr==null || originalStr.equals(""))
					{
						General.msgDialog(HanZiPinYin.this, "提示", "请输入汉字");
						return;
					}
					p_str = Pattern.compile("[\\u4e00-\\u9fa5]+");
					m = p_str.matcher(originalStr);
					if(!(m.find()&&m.group(0).equals(originalStr)))
					{//判断不是汉字
						General.msgDialog(HanZiPinYin.this, "提示", "请输入汉字");
						return;
					}
					str = "\"" + originalStr + "\" 的utf8编码： ";
					str += HanZiToPinYin.toUtf8(originalStr);
					pinyintv.setText(str);
					break;
				case R.id.hzpy_asc2hanzi_btn:
					// ASCII转换为汉字字符串   
					originalStr = "22307 35806 24555 20048";// ASCII码     
		            str = HanZiToPinYin.asc2HanZi(originalStr, " ");
		            pinyintv.setText(str);
					break;
				case R.id.hzpy_hanzi2asc_btn:
					// 汉字字符串转换为ASCII码   
					originalStr = "新年快乐！";// 字符串 
					str = HanZiToPinYin.hanzi2Asc(originalStr);
		            pinyintv.setText(str);
		            break;
				case R.id.hzpy_gbk2hanzi_btn:
					originalStr = "c5fa";
					str = HanZiToPinYin.gbk2HanZi(originalStr);
					str += " : ";
					str += HanZiToPinYin.stringToGbk(originalStr);
					pinyintv.setText(str);
					break;
				case R.id.hzpy_gethanzi_btn:
					originalStr = "123abc这个中文cde123abc也要提取123ab";
					str = HanZiToPinYin.getChinese(originalStr);
					pinyintv.setText(str);
					break;
				case R.id.hzpy_utf8togb2312_btn:
					originalStr = "%E4%BD%A0";
					str = HanZiToPinYin.utf8Togb2312(originalStr);
					pinyintv.setText(str);
					break;
				case R.id.hzpy_gb2312toutf8_btn:
					originalStr = "你";
					str = HanZiToPinYin.gb2312ToUtf8(originalStr);
					pinyintv.setText(str);
					break;
				default:
					break;
			}
		}
	};
	
	
}