package com.dana.modulII;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

//Context.MODE_PRIVATE：新内容覆盖原内容
//Context.MODE_APPEND：新内容追加到原内容后
//Context.MODE_WORLD_READABLE：允许其他应用程序读取
//Context.MODE_WORLD_WRITEABLE：允许其他应用程序写入，会覆盖原数据。
/**
 * 文件操作辅助类
 * 如果是保存在手机中,默认是在/data/data/(包名)/files下面
 * 如果是保存在SD卡中,则默认路径在/sdcard/下面
 * Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)//判断当前手机是否含有SD卡
 *
 */
public class FileService 
{
	private Context context;
	public FileService(Context context) 
	{
		this.context = context;
	}
	
	/**
	 * 读取文件的内容
	 * @param filename 文件名称
	 * @return
	 * @throws Exception
	 */
	public String readFile(String filename) throws Exception
	{
		FileInputStream inStream = null;
		//获得输入流
		if(filename.contains(File.separator))
			inStream = new FileInputStream(filename);
////		File file = new File(filename);
////		InputStream inStream = new FileInputStream(file);
		else
			inStream = context.openFileInput(filename); //如果要打开存放在/data/data/<package name>/files目录应用私有的文件，可以使用Activity提供openFileInput()方法.在打开文件的输出流时使用的openFileOutput()方法的第一参数用于指定文件名称，不 能包含路径分隔符.
		//new一个缓冲区
		byte[] buffer = new byte[1024];
		int len = 0;
		//使用ByteArrayOutputStream类来处理输出流
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while( (len = inStream.read(buffer))!= -1)
		{
			//写入数据
			outStream.write(buffer, 0, len);
		}
		//得到文件的二进制数据
		byte[] data = outStream.toByteArray();
		//关闭流
		outStream.close();
		inStream.close();
		return new String(data);
	}
	/**
	 * 以默认私有方式保存文件内容至SDCard中
	 * @param filename 
	 * @param content 
	 * @throws Exception
	 */
	public void saveToSDCard(String filename, String content) throws Exception
	{
		//通过getExternalStorageDirectory方法获取SDCard的文件路径
		File file = new File(Environment.getExternalStorageDirectory(), filename);
		//获取输出流
		FileOutputStream outStream = new FileOutputStream(file);
		outStream.write(content.getBytes());
		outStream.close();
	}
	
	/**
	 * 以默认私有方式保存文件内容，存放在手机存储空间中
	 * @param filename 
	 * @param content 
	 * @throws Exception
	 */
	public void save(String filename, String content) throws Exception
	{
		//
		FileOutputStream outStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
		outStream.write(content.getBytes());
		outStream.close();
	}
	
	/**
	 * 以追加的方式保存文件内容
	 * @param filename 文件名称
	 * @param content 文件内容
	 * @throws Exception
	 */
	public void saveAppend(String filename, String content) throws Exception
	{	
		FileOutputStream outStream = context.openFileOutput(filename, Context.MODE_APPEND);
		outStream.write(content.getBytes());
		outStream.close();
	}
	
	/**
	 * 以允许其他应用从该文件中读取内容的方式保存文件(Context.MODE_WORLD_READABLE)
	 * @param filename 文件名称
	 * @param content 文件内容
	 * @throws Exception
	 */
	public void saveReadable(String filename, String content) throws Exception
	{
		FileOutputStream outStream = context.openFileOutput(filename, Context.MODE_WORLD_READABLE);
		outStream.write(content.getBytes());
		outStream.close();
	}
	
	/**
	 * 以允许其他应用往该文件写入内容的方式保存文件
	 * @param filename 文件名称
	 * @param content 文件内容
	 * @throws Exception
	 */
	public void saveWriteable(String filename, String content) throws Exception
	{
		FileOutputStream outStream = context.openFileOutput(filename, Context.MODE_WORLD_WRITEABLE);
		outStream.write(content.getBytes());
		outStream.close();
	}
	
	/**
	 * 以允许其他应用对该文件读和写的方式保存文件(MODE_WORLD_READABLE与MODE_WORLD_WRITEABLE)
	 * @param filename 文件名称
	 * @param content 文件内容
	 * @throws Exception
	 */
	public void saveRW(String filename, String content) throws Exception
	{
		FileOutputStream outStream = context.openFileOutput(filename,
				Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
		//Context.MODE_WORLD_READABLE(1) + Context.MODE_WORLD_WRITEABLE(2),其实可用3替代
		outStream.write(content.getBytes());
		outStream.close();
	}
	
	public void savePRW(String filename, String content) throws Exception{  
		FileOutputStream outStream = context.openFileOutput(filename,   
		Context.MODE_WORLD_READABLE+ Context.MODE_WORLD_WRITEABLE+Context.MODE_APPEND);  
		outStream.write(content.getBytes());  
		outStream.close();  
		}  

}