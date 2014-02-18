package com.dana.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Algorithm
{
	/**
     * Sleep for a period of time.
     * @param secs the number of 100 milliseconds to sleep
     */
	public static void delay(int time)
	{
		try {
			Thread.sleep(time * 100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};
	
	/** 
	 * @函数功能: BCD码转为10进制串(阿拉伯数据) 
	 * @输入参数: BCD码 
	 * @输出结果: 10进制串 
	 */ 
	public static String bcd2Str(byte[] bytes){ 
	    StringBuffer temp=new StringBuffer(bytes.length*2);

	    for(int i=0;i<bytes.length;i++){ 
	     temp.append((byte)((bytes[i]& 0xf0)>>>4)); 
	     temp.append((byte)(bytes[i]& 0x0f)); 
	    } 
	    return temp.toString().substring(0,1).equalsIgnoreCase("0")?temp.toString().substring(1):temp.toString(); 
	}
	
	  /**
	   * 将指定字符串hexString，以每两个字符分割转换为16进制形式
	   * 如："2B44EFD9" –> byte[]{0x2B, 0×44, 0xEF, 0xD9}
	   * @param hex String
	   * @return byte[]
	   */
	public static byte[] hexStrToBytes(String hexString) {
	       if (hexString == null || hexString.equals("")) {
	           return null;
	       }
	       hexString = hexString.toUpperCase();
	       int length = hexString.length() / 2;
	       char[] hexChars = hexString.toCharArray();
	       byte[] result = new byte[length];
	       for (int i = 0; i < length; i++) {
	           int pos = i * 2;
	           result[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
	       }
	       return result;
	   }
	/**
	 * 将16进制形式字符串解码成字符串,适用于所有字符（包括中文）
	 * @param bytes
	 * @return
	 */
	public static String hexStrToStr(String bytes)
	{
		String hexString="0123456789ABCDEF";
	ByteArrayOutputStream baos=new ByteArrayOutputStream(bytes.length()/2);
	// 将每2位16进制整数组装成一个字节
	for(int i=0;i<bytes.length();i+=2)
	baos.write((hexString.indexOf(bytes.charAt(i))<<4 |hexString.indexOf(bytes.charAt(i+1))));
	return new String(baos.toByteArray());
	}
	  
 /**
  * Convert char to byte
  * @param c char
  * @return byte
  */
  private static byte charToByte(char c) {
  	byte b = (byte) "0123456789ABCDEF".indexOf(c); 
	    return b; 
 }
  
  /**
   * 把字节数组转换成16进制字符串 
   * @param bArray 
   * @return 
   */ 
  public static final String bytesToHexString(byte[] bArray) { 
   StringBuffer sb = new StringBuffer(bArray.length); 
   String temp; 
   int size = bArray.length;
   for (int i = 0; i < size; i++) { 
    temp = Integer.toHexString(0xFF & bArray[i]); 
    if (temp.length() < 2) 
     sb.append(0); 
     sb.append(temp.toUpperCase()); 
   } 
   return sb.toString(); 
  }
  
  /**
	 * 将字符串编码成16进制数字,适用于所有字符（包括中文）
	 * @param str
	 * @return
	 */
	public static String stringToHexString(String str)
	{
		String hexString="0123456789ABCDEF";
	// 根据默认编码获取字节数组
	byte[] bytes=str.getBytes();
	StringBuilder sb=new StringBuilder(bytes.length*2);
	// 将字节数组中每个字节拆解成2位16进制整数
	for(int i=0;i<bytes.length;i++)
	{
		sb.append(hexString.charAt((bytes[i]&0xf0)>>4));
		sb.append(hexString.charAt((bytes[i]&0x0f)>>0));
	}
	return sb.toString();
	}
  
  
  /** 
	 * MD5加密字符串，返回加密后的16进制字符串 
	 * @param origin 
	 * @return 
	 */ 
	public static String MD5EncodeToHex(String origin) { 
	       return bytesToHexString(MD5Encode(origin)); 
	     }

	/** 
	 * MD5加密字符串，返回加密后的字节数组 
	 * @param origin 
	 * @return 
	 */ 
	public static byte[] MD5Encode(String origin){ 
	    return MD5Encode(origin.getBytes()); 
	}

	/** 
	 * MD5加密字节数组，返回加密后的字节数组 
	 * @param bytes 
	 * @return 
	 */ 
	public static byte[] MD5Encode(byte[] bytes){ 
//		String MD5SEED = "d3776loqw21h77mmh675aas567xyusgKUL2H3DK4735O9861M6Q1ETGHB1C5ZK17K3P32VZR31WC77AC9E7TC43188PD3H9";
	    try { 
	    	// Create MD5 Hash
	    	MessageDigest digest = MessageDigest.getInstance("MD5");  
          digest.update(bytes);  
          byte[] messageDigest = digest.digest(); 
          return messageDigest;
	    } catch (NoSuchAlgorithmException e) { 
	     e.printStackTrace(); 
	     return new byte[0]; 
	    } 
	}
  
	/**
	 * 将字符串按GBK编码格式转成汉字
	 * @param str
	 * @return
	 */
	public static String str2Gbk(String str)
	{
		str = str.toLowerCase();
		byte[] bytes = new byte[str.length() / 2]; // 定义字节数组，长度为字符串的一半。
		byte tempByte = 0; // 临时变量。
		byte tempHigh = 0;
		byte tempLow = 0;
		int size = str.length();
		for (int i = 0, j = 0; i < size; i += 2, j++) // 每循环处理2个字符，最后新城一个字节。
		{
			tempByte = (byte) (((int) str.charAt(i)) & 0xff); // 处理高位。
			if (tempByte >= 48 && tempByte <= 57)
			{
				tempHigh = (byte) ((tempByte - 48) << 4);
			}
			else if (tempByte >= 97 && tempByte <= 102)// 'a'--'e'
			{
				tempHigh = (byte) ((tempByte - 97 + 10) << 4);
			}
			tempByte = (byte) (((int) str.charAt(i + 1)) & 0xff); // 处理低位。
			if (tempByte >= 48 && tempByte <= 57)
			{
				tempLow = (byte) (tempByte - 48);
			}
			else if (tempByte >= 97 && tempByte <= 102) // 'a'--'e'
			{
				tempLow = (byte) (tempByte - 97 + 10); // 'a'对应10.（或0xa.）
			}
			bytes[j] = (byte) (tempHigh | tempLow); // 通过‘或’加在一起。
		}
		String result = null;
		try
		{
			result = new String(bytes, "GBK");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return result;
	}
  
	/**
	 * 计算日期差
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long dateDiff(String startDate, String endDate)
	{
		// 按照传入的格式生成一个simpledateformate对象
		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
		long diff = 0;
		long day = -1;
		try
		{
			// 获得两个时间的毫秒时间差异
			diff = sd.parse(endDate).getTime() - sd.parse(startDate).getTime();
			day = diff / nd;// 计算差多少天
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}

		return day;
	}
	
	
	/**
	 * 字符串左补0
	 * @param str
	 * @param nLen
	 * @return
	 */
	public static String addZeroBeforeString(String str, int nLen)
	{
		int len = str.length();
		if (len < nLen)
		{
			while(len < nLen)
			{
				StringBuffer sb = new StringBuffer();
				sb.append("0").append(str);
				str = sb.toString();
				len = str.length();
			}
		}
		return str;
	}
	
	
	/**
	 * 获取当前时间
	 * yyyyMMddHHmmss
	 * @return
	 */
	public static String getTime()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);

		return str;
	}
	
	/**
	 * 获取版本号
	 * @return 当前应用的版本号
	 */
	public static String getVersion(Context c)
	{
		try
		{
			PackageManager manager = c.getPackageManager();
			PackageInfo info = manager.getPackageInfo(c.getPackageName(), 0);
			String version = info.versionName;
			return version;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "can not find version name";
		}
	}
	
	/**
	 * 字符串数组转字符串
	 * @param arrayStr
	 * @return
	 */
	public static String arraytoStr(String[] arrayStr)
	{
		int size = arrayStr.length;
		String str = "";
		for(int i=0; i<size; i++)
		{
			if(i<size-1)
				str+=arrayStr[i]+", ";
			else
				str+=arrayStr[i];
		}
		return str;
	}
	
		
	//获得res文件夹下的图片
	public static Bitmap getResImage(Context c, String name) {

		ApplicationInfo appInfo = c.getApplicationInfo();

		int resID = c.getResources().getIdentifier(name, "drawable", appInfo.packageName);

		return BitmapFactory.decodeResource(c.getResources(), resID);

		}
	
	/**
	 * 查找字符串数组 
	 * @param string[]  array, string str
	 */
	public static int strSearch(String[] array, String str)
	{
		int i, size;
		size = array.length;
		for(i=0;i<size; i++)
		{
			if(array[i].equals(str))
			{
				System.out.println(str + "位于数组的第"+(i+1)+"位");
				return i;
			}
		}
		return -1;
	}
	
	
	/**
	 * 把字节数组转换为对象 
	 * @param bytes 
	 * @return 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */ 
	public static final Object bytesToObject(byte[] bytes) throws IOException, ClassNotFoundException { 
	    ByteArrayInputStream in = new ByteArrayInputStream(bytes); 
	    ObjectInputStream oi = new ObjectInputStream(in); 
	    Object o = oi.readObject(); 
	    oi.close(); 
	    return o; 
	}
	
	/** 
	 * 把可序列化对象转换成字节数组 
	 * @param s 
	 * @return 
	 * @throws IOException 
	 */ 
	public static final byte[] objectToBytes(Serializable s) throws IOException { 
	    ByteArrayOutputStream out = new ByteArrayOutputStream(); 
	    ObjectOutputStream ot = new ObjectOutputStream(out); 
	    ot.writeObject(s); 
	    ot.flush(); 
	    ot.close(); 
	    return out.toByteArray(); 
	}
	
	public static final String objectToHexString(Serializable s) throws IOException{ 
	    return bytesToHexString(objectToBytes(s)); 
	}
	public static final Object hexStringToObject(String hex) throws IOException, ClassNotFoundException{ 
	    return bytesToObject(hexStrToBytes(hex)); 
	}
	
	/** 
	 * @函数功能: 10进制字符串转为BCD码 
	 * @输入参数: 10进制字符串 
	 * @输出结果: BCD码 
	 */ 
	public static byte[] ascToBcd(String str)
	{
		int asc_len = str.length();
		byte[] ascii = str.getBytes();
		byte[] bcd = new byte[(asc_len + 1) / 2];
		int j = 0;
		for (int i = 0; i < (asc_len + 1) / 2; i++)
		{
			bcd[i] = asc_to_bcd(ascii[j++]);
			bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
		}
		return bcd;
	}
	/**
	 * 10进制字节转为BCD码 
	 * @param asc
	 * @return
	 */
	public static byte asc_to_bcd(byte asc)
	{
		byte bcd;

		if ((asc >= '0') && (asc <= '9'))
			bcd = (byte) (asc - '0');
		else if ((asc >= 'A') && (asc <= 'F'))
			bcd = (byte) (asc - 'A' + 10);
		else if ((asc >= 'a') && (asc <= 'f'))
			bcd = (byte) (asc - 'a' + 10);
		else
			bcd = (byte) (asc - 48);

		return bcd;
	}
	/**
	 * byte数组转int
	 * @param intByte
	 * @return
	 */
	public static int bytesToInt(byte[] intByte) {
		int fromByte = 0;
		for (int i = 0; i < 2; i++)
		{
			int n = (intByte[i] < 0 ? (int)intByte[i] + 256 : (int)intByte[i]) << (8 * i);
			System.out.println(n);
			fromByte += n;
		}
		return fromByte;
	}
	
	/**
	 * 拷贝子字节数组
	 * @param src
	 * @param begin
	 * @param count
	 * @return
	 */
	public static byte[] subBytes(byte[] src, int begin, int count)
	{
		byte[] bs = new byte[count];
		for (int i = begin; i < begin + count; i++)
			bs[i - begin] = src[i];
		return bs;
	}
	
	
	  /**
	   * 将两个ASCII字符合成一个字节；
	   * 如："EF"–> 0xEF
	   * @param src0 byte
	   * @param src1 byte
	   * @return byte
	   */
	  public static byte uniteBytes(byte src0, byte src1) {
	    byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
	    _b0 = (byte)(_b0 << 4);
	    byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
	    byte ret = (byte)(_b0 ^ _b1);
	    return ret;
	  }
}