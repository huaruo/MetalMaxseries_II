package com.dana.modulII;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

//Context.MODE_PRIVATE�������ݸ���ԭ����
//Context.MODE_APPEND��������׷�ӵ�ԭ���ݺ�
//Context.MODE_WORLD_READABLE����������Ӧ�ó����ȡ
//Context.MODE_WORLD_WRITEABLE����������Ӧ�ó���д�룬�Ḳ��ԭ���ݡ�
/**
 * �ļ�����������
 * ����Ǳ������ֻ���,Ĭ������/data/data/(����)/files����
 * ����Ǳ�����SD����,��Ĭ��·����/sdcard/����
 * Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)//�жϵ�ǰ�ֻ��Ƿ���SD��
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
	 * ��ȡ�ļ�������
	 * @param filename �ļ�����
	 * @return
	 * @throws Exception
	 */
	public String readFile(String filename) throws Exception
	{
		FileInputStream inStream = null;
		//���������
		if(filename.contains(File.separator))
			inStream = new FileInputStream(filename);
////		File file = new File(filename);
////		InputStream inStream = new FileInputStream(file);
		else
			inStream = context.openFileInput(filename); //���Ҫ�򿪴����/data/data/<package name>/filesĿ¼Ӧ��˽�е��ļ�������ʹ��Activity�ṩopenFileInput()����.�ڴ��ļ��������ʱʹ�õ�openFileOutput()�����ĵ�һ��������ָ���ļ����ƣ��� �ܰ���·���ָ���.
		//newһ��������
		byte[] buffer = new byte[1024];
		int len = 0;
		//ʹ��ByteArrayOutputStream�������������
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while( (len = inStream.read(buffer))!= -1)
		{
			//д������
			outStream.write(buffer, 0, len);
		}
		//�õ��ļ��Ķ���������
		byte[] data = outStream.toByteArray();
		//�ر���
		outStream.close();
		inStream.close();
		return new String(data);
	}
	/**
	 * ��Ĭ��˽�з�ʽ�����ļ�������SDCard��
	 * @param filename 
	 * @param content 
	 * @throws Exception
	 */
	public void saveToSDCard(String filename, String content) throws Exception
	{
		//ͨ��getExternalStorageDirectory������ȡSDCard���ļ�·��
		File file = new File(Environment.getExternalStorageDirectory(), filename);
		//��ȡ�����
		FileOutputStream outStream = new FileOutputStream(file);
		outStream.write(content.getBytes());
		outStream.close();
	}
	
	/**
	 * ��Ĭ��˽�з�ʽ�����ļ����ݣ�������ֻ��洢�ռ���
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
	 * ��׷�ӵķ�ʽ�����ļ�����
	 * @param filename �ļ�����
	 * @param content �ļ�����
	 * @throws Exception
	 */
	public void saveAppend(String filename, String content) throws Exception
	{	
		FileOutputStream outStream = context.openFileOutput(filename, Context.MODE_APPEND);
		outStream.write(content.getBytes());
		outStream.close();
	}
	
	/**
	 * ����������Ӧ�ôӸ��ļ��ж�ȡ���ݵķ�ʽ�����ļ�(Context.MODE_WORLD_READABLE)
	 * @param filename �ļ�����
	 * @param content �ļ�����
	 * @throws Exception
	 */
	public void saveReadable(String filename, String content) throws Exception
	{
		FileOutputStream outStream = context.openFileOutput(filename, Context.MODE_WORLD_READABLE);
		outStream.write(content.getBytes());
		outStream.close();
	}
	
	/**
	 * ����������Ӧ�������ļ�д�����ݵķ�ʽ�����ļ�
	 * @param filename �ļ�����
	 * @param content �ļ�����
	 * @throws Exception
	 */
	public void saveWriteable(String filename, String content) throws Exception
	{
		FileOutputStream outStream = context.openFileOutput(filename, Context.MODE_WORLD_WRITEABLE);
		outStream.write(content.getBytes());
		outStream.close();
	}
	
	/**
	 * ����������Ӧ�öԸ��ļ�����д�ķ�ʽ�����ļ�(MODE_WORLD_READABLE��MODE_WORLD_WRITEABLE)
	 * @param filename �ļ�����
	 * @param content �ļ�����
	 * @throws Exception
	 */
	public void saveRW(String filename, String content) throws Exception
	{
		FileOutputStream outStream = context.openFileOutput(filename,
				Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
		//Context.MODE_WORLD_READABLE(1) + Context.MODE_WORLD_WRITEABLE(2),��ʵ����3���
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