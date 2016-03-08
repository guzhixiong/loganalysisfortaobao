package com.wangcheng.dc.realtime;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public abstract class URLCategoryHelper { 
	private static final String CATEGORY_CONFIG_FILE = "/category.xml";
	private static Map<Pattern,String> urlPatterns = new HashMap<Pattern,String>();
	 
	public static void loadConfig() throws DocumentException{
		
		loadConfig(URLCategoryHelper.class.getResourceAsStream(CATEGORY_CONFIG_FILE));
	}

	public static void loadConfig(InputStream in) throws DocumentException{
		
		SAXReader reader = new SAXReader();
        Document   document = reader.read(in);  
        
        Iterator itemIterator = document.getRootElement().elementIterator("item");
        
        while(itemIterator.hasNext()){
        	Element itemElement = (Element) itemIterator.next();
        	String pattern = itemElement.attributeValue("pattern"); 
        	Pattern pattrn = Pattern.compile(pattern);
    		urlPatterns.put(pattrn,itemElement.getTextTrim());
        } 
	}
	public static String parse(String url){
		String category = null;
		
		for(Entry<Pattern,String> entry:urlPatterns.entrySet()){
			
			if(entry.getKey().matcher(url).find()){
				category = entry.getValue();
				break;
			}
		}  
		
		if(category==null){
			category = "店铺首页";
		}
		return category;
	}
	
	public static void main(String args[]) throws DocumentException{
		loadConfig();
		String category = URLCategoryHelper.parse("http://aa.taobao.com/a/product.html?uid=3432");
		System.out.println(category);
		category = URLCategoryHelper.parse("http://aa.taobao.com/a/home.html?uid=3432");
		System.out.println(category);
		category = URLCategoryHelper.parse("http://aa.taobao.com/a/category.html?uid=3432");
		System.out.println(category);
	}
}
