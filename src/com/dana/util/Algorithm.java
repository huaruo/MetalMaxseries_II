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
	 * @��������: BCD��תΪ10���ƴ�(����������) 
	 * @�������: BCD�� 
	 * @������: 10���ƴ� 
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
	   * ��ָ���ַ���hexString����ÿ�����ַ��ָ�ת��Ϊ16������ʽ
	   * �磺"2B44EFD9" �C> byte[]{0x2B, 0��44, 0xEF, 0xD9}
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
	 * ��16������ʽ�ַ���������ַ���,�����������ַ����������ģ�
	 * @param bytes
	 * @return
	 */
	public static String hexStrToStr(String bytes)
	{
		String hexString="0123456789ABCDEF";
	ByteArrayOutputStream baos=new ByteArrayOutputStream(bytes.length()/2);
	// ��ÿ2λ16����������װ��һ���ֽ�
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
   * ���ֽ�����ת����16�����ַ��� 
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
	 * ���ַ��������16��������,�����������ַ����������ģ�
	 * @param str
	 * @return
	 */
	public static String stringToHexString(String str)
	{
		String hexString="0123456789ABCDEF";
	// ����Ĭ�ϱ����ȡ�ֽ�����
	byte[] bytes=str.getBytes();
	StringBuilder sb=new StringBuilder(bytes.length*2);
	// ���ֽ�������ÿ���ֽڲ���2λ16��������
	for(int i=0;i<bytes.length;i++)
	{
		sb.append(hexString.charAt((bytes[i]&0xf0)>>4));
		sb.append(hexString.charAt((bytes[i]&0x0f)>>0));
	}
	return sb.toString();
	}
  
  
  /** 
	 * MD5�����ַ��������ؼ��ܺ��16�����ַ��� 
	 * @param origin 
	 * @return 
	 */ 
	public static String MD5EncodeToHex(String origin) { 
	       return bytesToHexString(MD5Encode(origin)); 
	     }

	/** 
	 * MD5�����ַ��������ؼ��ܺ���ֽ����� 
	 * @param origin 
	 * @return 
	 */ 
	public static byte[] MD5Encode(String origin){ 
	    return MD5Encode(origin.getBytes()); 
	}

	/** 
	 * MD5�����ֽ����飬���ؼ��ܺ���ֽ����� 
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
	 * ���ַ�����GBK�����ʽת�ɺ���
	 * @param str
	 * @return
	 */
	public static String str2Gbk(String str)
	{
		str = str.toLowerCase();
		byte[] bytes = new byte[str.length() / 2]; // �����ֽ����飬����Ϊ�ַ�����һ�롣
		byte tempByte = 0; // ��ʱ������
		byte tempHigh = 0;
		byte tempLow = 0;
		int size = str.length();
		for (int i = 0, j = 0; i < size; i += 2, j++) // ÿѭ������2���ַ�������³�һ���ֽڡ�
		{
			tempByte = (byte) (((int) str.charAt(i)) & 0xff); // �����λ��
			if (tempByte >= 48 && tempByte <= 57)
			{
				tempHigh = (byte) ((tempByte - 48) << 4);
			}
			else if (tempByte >= 97 && tempByte <= 102)// 'a'--'e'
			{
				tempHigh = (byte) ((tempByte - 97 + 10) << 4);
			}
			tempByte = (byte) (((int) str.charAt(i + 1)) & 0xff); // �����λ��
			if (tempByte >= 48 && tempByte <= 57)
			{
				tempLow = (byte) (tempByte - 48);
			}
			else if (tempByte >= 97 && tempByte <= 102) // 'a'--'e'
			{
				tempLow = (byte) (tempByte - 97 + 10); // 'a'��Ӧ10.����0xa.��
			}
			bytes[j] = (byte) (tempHigh | tempLow); // ͨ�����򡯼���һ��
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
	 * �������ڲ�
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long dateDiff(String startDate, String endDate)
	{
		// ���մ���ĸ�ʽ����һ��simpledateformate����
		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
		long nd = 1000 * 24 * 60 * 60;// һ��ĺ�����
		long diff = 0;
		long day = -1;
		try
		{
			// �������ʱ��ĺ���ʱ�����
			diff = sd.parse(endDate).getTime() - sd.parse(startDate).getTime();
			day = diff / nd;// ����������
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}

		return day;
	}
	
	
	/**
	 * �ַ�����0
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
	 * ��ȡ��ǰʱ��
	 * yyyyMMddHHmmss
	 * @return
	 */
	public static String getTime()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
		String str = formatter.format(curDate);

		return str;
	}
	
	/**
	 * ��ȡ�汾��
	 * @return ��ǰӦ�õİ汾��
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
	 * �ַ�������ת�ַ���
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
	
		
	//���res�ļ����µ�ͼƬ
	public static Bitmap getResImage(Context c, String name) {

		ApplicationInfo appInfo = c.getApplicationInfo();

		int resID = c.getResources().getIdentifier(name, "drawable", appInfo.packageName);

		return BitmapFactory.decodeResource(c.getResources(), resID);

		}
	
	/**
	 * �����ַ������� 
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
				System.out.println(str + "λ������ĵ�"+(i+1)+"λ");
				return i;
			}
		}
		return -1;
	}
	
	
	/**
	 * ���ֽ�����ת��Ϊ���� 
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
	 * �ѿ����л�����ת�����ֽ����� 
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
	 * @��������: 10�����ַ���תΪBCD�� 
	 * @�������: 10�����ַ��� 
	 * @������: BCD�� 
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
	 * 10�����ֽ�תΪBCD�� 
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
	 * byte����תint
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
	 * �������ֽ�����
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
	   * ������ASCII�ַ��ϳ�һ���ֽڣ�
	   * �磺"EF"�C> 0xEF
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