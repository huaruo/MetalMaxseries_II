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
	//路径设置在SDK的某个文件夹下
//	private final static String sd_path = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator;
	//手机本地地址
//	private final static String private_path = context.getFilesDor().getPath() + File.separator;
	
	/**
	 * 创建文件夹
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
				//按照指定的路径创建文件夹
				dir.mkdirs();
			} catch(Exception e)
			{
				Log.i(TAG, e.getMessage());
				e.printStackTrace();
			}
			
		}
	}
	/**
	 * 创建文件
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
				//在指定的文件夹中创建文件
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
	 * 写文件
	 * @param filename
	 * @param str
	 */
	//向已创建的文件中写入数据
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
			//创建FileWriter对象，用来写入字符流
			bw = new BufferedWriter(fw);//将缓冲对文件输出
			String myreadline = datetime + "[]: " +str;
			
			bw.write(myreadline + "\r\n"); //写入文件
			bw.newLine();
			bw.flush();//刷新该流的缓冲
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
	 * 读文件
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
				Log.i(TAG, "文件不可读取");
				return "文件不可读取";
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
				//删除指定文件夹下的文件
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
	 * 将文件中指定内容的第一行替换为其它内容.
	 * @param filename
	 * @param oldStr 查找内容
	 * @param replaceStr 替换内容
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
			//保存该行前面的内容
			for(int  i = 1; temp != null && temp.equals(oldStr); i++)
			{
				buf = buf.append(temp);
				buf = buf.append(System.getProperty("line.separator"));
			}
			
			//将内容插入
			buf = buf.append(replaceStr);
			
			temp = br.readLine();
			Log.i("TAG", "temp: " + temp);
			//奥村该行后面的内容
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