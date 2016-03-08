package com.wangcheng.dc.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class StringUtils {
	
	private static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		'a', 'b', 'c', 'd', 'e', 'f' };
	
	private static MessageDigest mdTemp = null;
	
	static{
		try {
			mdTemp = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static final String replaceAll(String text, String searchString,
			String replacement) {
		
		if(text==null){
			return replacement;
		}
		StringBuilder sb = new StringBuilder();
		int max = -1;
		int start = 0;
		int end = text.indexOf(searchString, start);
		if (end == -1) {
			return text;
		}
		int replLength = searchString.length();
		int increase = replacement.length() - replLength;
		increase = (increase < 0 ? 0 : increase);
		increase *= (max < 0 ? 16 : (max > 64 ? 64 : max));
		StringBuffer buf = new StringBuffer(text.length() + increase);
		while (end != -1) {
			buf.append(text.substring(start, end)).append(replacement);
			start = end + replLength;
			if (--max == 0) {
				break;
			}
			end = text.indexOf(searchString, start);
		}
		buf.append(text.substring(start));
		return buf.toString();
	}

	public static final String md5(String input) {
		 
		try {
			if(input==null){
				return null;
			}
			
			byte[] strTemp = input.getBytes();
		
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			
			return new String(str);
			
		} catch (Exception e) {
			return input;
		} 
	}

	public static void main(String args[]) throws UnsupportedEncodingException {
		String str = "分类页";
		System.out.println(URLEncoder.encode(str, "utf-8"));
		
		long begin = System.currentTimeMillis();
		
	//	for(int i=0;i<1000000;i++){
			System.out.println(md5("192.168.1.22Mozilla/4.0+[zh]+(compatible;+MSIE+7.0;+Windows)"));
			System.out.println(md5("192.168.1.160Mozilla/4.0+[zh]+(compatible;+MSIE+7.0;+Windows)"));
		//}
		
		
		long end = System.currentTimeMillis();
		
		//System.out.println("cost:"+1000000*1000/(end-begin));
	}
}
