package com.dana.modulII;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public final class FileOperation
{
	//Debug
	private final static String TAG = "FileOperation";
	//·��������SDK��ĳ���ļ�����
//	private final static String sd_path = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator;
	//�ֻ����ص�ַ
//	private final static String private_path = context.getFilesDor().getPath() + File.separator;
	
	/**
	 * �����ļ���
	 * @param filepath
	 * @throws IOException
	 */
	public static void CreateDir(String filepath) throws  IOException
	{
		File dir = new File(filepath);
		if(!dir.exists())
		{
			try
			{
				//����ָ����·�������ļ���
				dir.mkdirs();
			} catch(Exception e)
			{
				Log.i(TAG, e.getMessage());
				e.printStackTrace();
			}
			
		}
	}
	/**
	 * �����ļ�
	 * @param filename
	 * @throws IOException
	 */
	public static void CreateFile(String filename) throws IOException
	{
		File file = new File(filename);
		if(!file.exists())
		{
			try
			{
				//��ָ�����ļ����д����ļ�
				file.createNewFile();
			} catch(Exception e)
			{
				//TODO Auto-generated catch block
				Log.i(TAG, e.getMessage());
				e.printStackTrace();
			}
		}
//		else if(file.exists())
//		{
//			file.delete();
//		}
	}
	
	/**
	 * д�ļ�
	 * @param filename
	 * @param str
	 */
	//���Ѵ������ļ���д������
	public static void Write(String filename, String str)
	{
		FileWriter fw = null;
		BufferedWriter bw = null;
		String datetime = "";
		
		try {
			Date date = new Date();
			SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd" + " " + "HH:mm:ss");
			datetime = tempDate.format(date).toString();
			fw = new FileWriter(filename,true);
			//����FileWriter��������д���ַ���
			bw = new BufferedWriter(fw);//��������ļ����
			String myreadline = datetime + "[]: " +str;
			
			bw.write(myreadline + "\r\n"); //д���ļ�
			bw.newLine();
			bw.flush();//ˢ�¸����Ļ���
			bw.close();
			fw.close();
		} catch(FileNotFoundException e)
		{
			Log.i(TAG, e.getMessage());
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, e.getMessage());
			e.printStackTrace();
			try {
				bw.close();
				fw.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				Log.i(TAG, e1.getMessage());
				e1.printStackTrace();
			}
		}
	}
	/**
	 * ���ļ�
	 * @param filename
	 * @return
	 */
	public static String Read(String filename)
	{
		FileReader fr = null;
		BufferedReader br = null;
		StringBuffer sb = null;
		
		try
		{
			File file = new File(filename);
			if(!file.exists() || file.isDirectory())
			{
				Log.i(TAG, "�ļ����ɶ�ȡ");
				return "�ļ����ɶ�ȡ";
			}
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String temp = null;
			sb = new StringBuffer();
			temp = br.readLine();
			
			while(temp!=null)
			{
				sb.append(temp+"\n");
				temp=br.readLine();
			}
		} catch (FileNotFoundException e)
		{
			//TODO Auto-generated catch block
			e.printStackTrace();
		} catch(IOException e)
		{
			//TODO Auto_generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public static void Delete(String filename) throws IOException
	{
		File file = new File(filename);
		if(file.exists())
		{
			try
			{
				//ɾ��ָ���ļ����µ��ļ�
				file.delete();
			} catch(Exception e)
			{
				//TODO Auto-generated catch block
				Log.i(TAG, e.getMessage());
				e.printStackTrace();
			}
		}
		else
		{
			return;
		}
	}
	/**
	 * ���ļ���ָ�����ݵĵ�һ���滻Ϊ��������.
	 * @param filename
	 * @param oldStr ��������
	 * @param replaceStr �滻����
	 */
	public static void replaceTxtByStr(String  filename, String oldStr, String replaceStr)
	{
		String temp = "";
		try {
			File file = new File(filename);
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			StringBuffer buf = new StringBuffer();
			
			temp = br.readLine();
			Log.i("TAG", "temp: " + temp);
			//�������ǰ�������
			for(int  i = 1; temp != null && temp.equals(oldStr); i++)
			{
				buf = buf.append(temp);
				buf = buf.append(System.getProperty("line.separator"));
			}
			
			//�����ݲ���
			buf = buf.append(replaceStr);
			
			temp = br.readLine();
			Log.i("TAG", "temp: " + temp);
			//�´���к��������
			while (temp != null)
			{
				buf = buf.append(System.getProperty("line.separator"));
				buf = buf.append(temp);
			}
			
			br.close();
			FileOutputStream fos = new FileOutputStream(file);
			PrintWriter pw = new PrintWriter(fos);
			pw.write(buf.toString().toCharArray());
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}