package cn.bronzeware.muppet.test;

import java.io.File;
import java.net.URLDecoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class TestResolveXML {

	
	public static void  main(String[] args) {
		try
		{
			DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
			Document document;
	        DocumentBuilder builder;
			
			builder = dfactory.newDocumentBuilder();
			String classPath = Thread.class.getResource("/").getPath();
			
			/**
			 * URLDecoder.decode(classPath,"UTF-8")对中文路径进行转码
			 */
			document = builder.parse(new File(URLDecoder.decode(classPath,"UTF-8")+"cn\\bronzeware\\muppet\\xml\\muppet.xml"));  
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
