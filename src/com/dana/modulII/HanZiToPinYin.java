package com.dana.modulII;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class HanZiToPinYin
{
	/**
	 * 返回一个字的拼音
	 * @param hanzi
	 * @return
	 */
	public static String toPinYin(char hanzi)
	{
		HanyuPinyinOutputFormat hanyuPinyin = new HanyuPinyinOutputFormat();
		hanyuPinyin.setCaseType(HanyuPinyinCaseType.LOWERCASE);
	    hanyuPinyin.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
	    hanyuPinyin.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
	    String[] pinyinArray=null;
	    try {
	    	//是否在汉字范围内
	    	if(hanzi>=0x4e00 && hanzi<=0x9fa5){
	    		pinyinArray = PinyinHelper.toHanyuPinyinStringArray(hanzi, hanyuPinyin);
	    	}
	    } catch (BadHanyuPinyinOutputFormatCombination e) {
	    	e.printStackTrace();
	    }
	    //将汉字返回
		return pinyinArray[0];
	}
	
	/**
	 * 返回汉字的utf-16编码
	 * @param hanzi
	 * @return
	 */
	public static String toUtf16(String hanzi)
	{
		StringBuffer output = new StringBuffer();
		for(int i = 0; i<hanzi.length(); i++)
		{
			output.append("\\u" + Integer.toString(hanzi.charAt(i),16));
		}
		
		return output.toString();
	}
	
	/**
	 * 将utf16编码转成汉字
	 * @param utfStr
	 * @param splitStr utf16编码会有特殊字符加以分隔
	 * @return
	 */
	public static String utf16ToHanZi(String utfStr, String splitStr)
	{
		String[] strs;
		strs = utfStr.split(splitStr);
		StringBuilder sb = new StringBuilder();
		int temp;
		for(int i=1; i<strs.length; i++)
		{
			temp = Integer.parseInt(strs[i].substring(0,strs[i].length()-1),16);
			sb.append((char)temp);
		}
		return sb.toString();
	}
	
	/**
	 * asc编码转汉字
	 * @param ascStr
	 * @param splitStr 分隔符
	 * @return
	 */
	public static String asc2HanZi(String ascStr, String splitStr)
	{
		String[] strs;
		strs = ascStr.split(splitStr);
		String str ="";
		 for (int i = 0; i < strs.length; i++) {   
             str += (strs[i] + " "  
                     + (char) Integer.parseInt(strs[i]));   
         }  
		return str;
	}
	
	/**
	 * 汉字转asc
	 * @param hanziStr
	 * @return
	 */
	public static String hanzi2Asc(String hanziStr)
	{
		String str = "";
		char[] chars = hanziStr.toCharArray(); // 把字符中转换为字符数组   
		for (int i = 0; i < chars.length; i++) {// 输出结果   
		      
            str+= (" " + chars[i] + " " + (int) chars[i]);   
        }
		return str;
	}
	
	/**
     * UTF8编码的16进制
     * 将文件名中的汉字转为UTF8编码的串,以便下载时能正确显示另存的文件名. 
     * @param s 原文件名
     * @return 重新编码后的文件名
     */
    public static String toUtf8(String s) {
    	if (s == null || s.equals("")) {
    		return null;
    	}
    	StringBuffer sb = new StringBuffer();
    	try {
    		char c;
    		for (int i = 0; i < s.length(); i++) {
    			c = s.charAt(i);
    			if (c >= 0 && c <= 255) {
    				sb.append(c);
    			} else {
    				byte[] b;
    				b = Character.toString(c).getBytes("utf-8");
    				for (int j = 0; j < b.length; j++) {
    					int k = b[j];
    					if (k < 0)
    						k += 256;
    					sb.append("%" + Integer.toHexString(k).toUpperCase());
    				}
    			}
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return sb.toString();
    }
    /**
     * gbk汉字
     * @param gbkStr
     * @return
     */
    public static String gbk2HanZi(String gbkStr)
    {
    	String str ="";
    	byte[] buf = new byte[gbkStr.length()/2];
    	for(int i=0; i<buf.length; i++)
    	{
    		byte high = Byte.parseByte(gbkStr.substring(i*2,i*2+1),16);
    		byte low = Byte.parseByte(gbkStr.substring(i*2+1,i*2+2),16);
    		buf[i] = (byte)(high<<4|low);
    	}
    	try {
			str = new String(buf,"gbk");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return str;
    }
    
    /**
     * gbk汉字
     * @param str
     * @return
     */
    public static String stringToGbk(String str)

	{
    	str=str.toLowerCase();
		byte[] bytes=new byte[str.length()/2];			//定义字节数组，长度为字符串的一半。
		byte tempByte=0;					//临时变量。
		byte tempHigh=0;
		byte tempLow=0;
		for(int i=0,j=0;i<str.length();i+=2,j++)		//每循环处理2个字符，最后新城一个字节。
		{
			tempByte=(byte)(((int)str.charAt(i))&0xff);	//处理高位。
			if(tempByte>=48&&tempByte<=57)
			{
				tempHigh=(byte)((tempByte-48)<<4);
			}
			else if(tempByte>=97&&tempByte<=102)//'a'--'e' 
			{
				tempHigh=(byte)((tempByte-97+10)<<4);
			}
			tempByte=(byte)(((int)str.charAt(i+1))&0xff);	//处理低位。
			if(tempByte>=48&&tempByte<=57)
			{
				tempLow=(byte)(tempByte-48);
			}
			else if(tempByte>=97&&tempByte<=102)		//'a'--'e'
			{
				tempLow=(byte)(tempByte-97+10);		//'a'对应10.（或0xa.）
			}
			bytes[j]=(byte)(tempHigh|tempLow);		//通过‘或’加在一起。
		}
		String result = null;
		try {
			result = new String(bytes,"GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
    
    /**
     * 提取字符串中的汉字
     * @param str
     * @return
     */
    public static String getChinese(String str) {
    	String value = "";
    	Pattern p_pattern = Pattern.compile("([\u4e00-\u9fa5]+)");
    	Matcher m_matcher = p_pattern.matcher(str);
    	while (m_matcher.find()) 
    	{
    		value += m_matcher.group(0);
    	}
    	return value;
    }
    
    /**
     * 将  UTF-8格式的字符串转换为 GB2312编码格式的字符串：  
     * @param str
     * @return
     */
    public static String utf8Togb2312(String str)
    {
        StringBuffer sb =  new StringBuffer();  
        char c;
        for(int i=0; i<str.length(); i++)
        {  
             c = str.charAt(i);  

             switch(c) 
             {
                case '+':
                	sb.append(' ');
                	break;
                case '%':
                	try {
                		sb.append((char)Integer.parseInt(str.substring(i+1,i+3),16));
                	} catch (NumberFormatException e) {
                		throw new IllegalArgumentException();
                	}  
                	i += 2; 
                	break;  
                default: 
                	sb.append(c);  
                	break; 
             } 
        }  
        String result = sb.toString();  
        String res= null;  
         try {
             byte[] inputBytes = result.getBytes("8859_1"); 
             res=new String(inputBytes, "UTF-8");
         } catch  (Exception e){
        	// TODO Auto-generated catch block
 			e.printStackTrace(); 
         }  
         return res;  
    }  
    
    /**
     * 将 GB2312 编码格式的字符串转换为 UTF-8 格式的字符串
     * @param str
     * @return
     */
    public static String gb2312ToUtf8(String str)
    {
    	String urlEncode =  "" ;  
        try {
        	urlEncode = URLEncoder.encode (str,  "UTF-8" ); 
       }  catch(UnsupportedEncodingException e) {
           e.printStackTrace(); 
       } 
        return urlEncode;  
    }  
    
//    /**
//	 * Interprets the given byte array as UTF-8 and converts to UTF-16. The
//	 * {@link CharsRef} will be extended if it doesn't provide enough space to
//	 * hold the worst case of each byte becoming a UTF-16 codepoint.
//	 * <p>
//	 * NOTE: Full characters are read, even if this reads past the length passed
//	 * (and can result in an ArrayOutOfBoundsException if invalid UTF-8 is
//	 * passed). Explicit checks for valid UTF-8 are not performed.
//	 */
//	// TODO: broken if chars.offset != 0
//	public static void UTF8toUTF16(byte[] utf8, int offset, int length,
//			CharsRef chars) {
//		int out_offset = chars.offset = 0;
//		final char[] out = chars.chars = ArrayUtil.grow(chars.chars, length);
//		final int limit = offset + length;
//		while (offset < limit) {
//			int b = utf8[offset++] & 0xff;
//			if (b < 0xc0) {
//				assert b < 0x80;
//				out[out_offset++] = (char) b;
//			} else if (b < 0xe0) {
//				out[out_offset++] = (char) (((b & 0x1f) << 6) + (utf8[offset++] & 0x3f));
//			} else if (b < 0xf0) {
//				out[out_offset++] = (char) (((b & 0xf) << 12)
//						+ ((utf8[offset] & 0x3f) << 6) + (utf8[offset + 1] & 0x3f));
//				offset += 2;
//			} else {
//				assert b < 0xf8 : "b=" + b;
//				int ch = ((b & 0x7) << 18) + ((utf8[offset] & 0x3f) << 12)
//						+ ((utf8[offset + 1] & 0x3f) << 6)
//						+ (utf8[offset + 2] & 0x3f);
//				offset += 3;
//				if (ch < UNI_MAX_BMP) {
//					out[out_offset++] = (char) ch;
//				} else {
//					int chHalf = ch - 0x0010000;
//					out[out_offset++] = (char) ((chHalf >> 10) + 0xD800);
//					out[out_offset++] = (char) ((chHalf & HALF_MASK) + 0xDC00);
//				}
//			}
//		}
//		chars.length = out_offset - chars.offset;
//	}
//
// /** Encode characters from a char[] source, starting at
//   *  offset for length chars. After encoding, result.offset will always be 0.
//   */
// public static void UTF16toUTF8(final char[] source, final int offset, final int length, BytesRef result) {
//
//    int upto = 0;
//    int i = offset;
//    final int end = offset + length;
//    byte[] out = result.bytes;
//    // Pre-allocate for worst case 4-for-1
//    final int maxLen = length * 4;
//    if (out.length < maxLen)
//      out = result.bytes = new byte[maxLen];
//    result.offset = 0;
//
//    while(i < end) {
//      
//      final int code = (int) source[i++];
//
//      if (code < 0x80)
//        out[upto++] = (byte) code;
//      else if (code < 0x800) {
//        out[upto++] = (byte) (0xC0 | (code >> 6));
//        out[upto++] = (byte)(0x80 | (code & 0x3F));
//      } else if (code < 0xD800 || code > 0xDFFF) {
//        out[upto++] = (byte)(0xE0 | (code >> 12));
//        out[upto++] = (byte)(0x80 | ((code >> 6) & 0x3F));
//        out[upto++] = (byte)(0x80 | (code & 0x3F));
//      } else {
//        // surrogate pair
//        // confirm valid high surrogate
//        if (code < 0xDC00 && i < end) {
//          int utf32 = (int) source[i];
//          // confirm valid low surrogate and write pair
//          if (utf32 >= 0xDC00 && utf32 <= 0xDFFF) { 
//            utf32 = (code << 10) + utf32 + SURROGATE_OFFSET;
//            i++;
//            out[upto++] = (byte)(0xF0 | (utf32 >> 18));
//            out[upto++] = (byte)(0x80 | ((utf32 >> 12) & 0x3F));
//            out[upto++] = (byte)(0x80 | ((utf32 >> 6) & 0x3F));
//            out[upto++] = (byte)(0x80 | (utf32 & 0x3F));
//            continue;
//          }
//        }
//        // replace unpaired surrogate or out-of-order low surrogate
//        // with substitution character
//        out[upto++] = (byte) 0xEF;
//        out[upto++] = (byte) 0xBF;
//        out[upto++] = (byte) 0xBD;
//      }
//    }
//    //assert matches(source, offset, length, out, upto);
//    result.length = upto;
//  }
}