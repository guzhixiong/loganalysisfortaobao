package com.wangcheng.dc.realtime;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public abstract class KeywordBaseURLCategoryHelper { 
	
	private static final Logger log = Logger.getLogger(KeywordBaseURLCategoryHelper.class); 
	
	private static final String CATEGORY_CONFIG_FILE = "/category.xml";
	private static Map<String,String> urlKeywords = new TreeMap<String,String>();
	
	private static Set<Entry<String,String>> urlKeywordEntrys = null;
	 
	public final static void loadConfig() throws DocumentException{
		
		loadConfig(URLCategoryHelper.class.getResourceAsStream(CATEGORY_CONFIG_FILE));
	}

	public final static void loadConfig(InputStream in) throws DocumentException{
		
		SAXReader reader = new SAXReader();
        Document   document = reader.read(in);  
        
        Iterator itemIterator = document.getRootElement().elementIterator("item");
        
        while(itemIterator.hasNext()){
        	Element itemElement = (Element) itemIterator.next();
        	String keyword = itemElement.attributeValue("keyword");  
        	urlKeywords.put(keyword,itemElement.getTextTrim());
        } 
        
        urlKeywordEntrys = urlKeywords.entrySet();
        
        log.info("load url keywords :"+urlKeywords);
	}
	public final static String parse(String url){
		String category = null;
		 
		for(Entry<String,String> entry:urlKeywordEntrys){
			
			if(url.indexOf(entry.getKey())>=0){
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
		KeywordBaseURLCategoryHelper.loadConfig();
		URLCategoryHelper.loadConfig();
		long begin = System.currentTimeMillis();
		
		for(int i=0;i<1;i++){
			System.out.println( KeywordBaseURLCategoryHelper.parse("http://mfsj1908.taobao.com/?search=y&scid=169729615&scname=0MLGt8nPytA%3D&checkedRange=true&queryType=cat"));
		}		
		
		long end = System.currentTimeMillis();
		
		//System.out.println("cost="+100000*1000/(end-begin)); 
		
		/*long begin2 = System.currentTimeMillis();
		
		for(int i=0;i<100000;i++){
			 URLCategoryHelper.parse("http://aa.taobao.com/a/product.html?uid=3432");
		}		
		
		long end2 = System.currentTimeMillis();
		
		System.out.println("cost="+100000*1000/(end2-begin2)); 
		
		System.out.println("vs="+(end2-begin2)/(end-begin)); */
	}
}
