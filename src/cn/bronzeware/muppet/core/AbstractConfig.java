package cn.bronzeware.muppet.core;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import cn.bronzeware.muppet.exceptions.ExcpMsg;

public abstract class AbstractConfig implements XMLConfig{

	private XMLConfigResource resource;
	private String xmlPath;
	
	public AbstractConfig(XMLConfigResource resource){
		this.xmlPath = resource.getXmlpath();
		this.resource = resource;
		setXMLConfigResource(resource);
		
	}
	
	public AbstractConfig(String xmlPath){
		this.xmlPath = xmlPath;
		config();
		  //使用工厂创建文件解析类  
		
	}

	
	private final void config(){

		DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();  
		Document document;
		DocumentBuilder builder;
		
			try {
				builder = dfactory.newDocumentBuilder();
				String classPath = Thread.class.getResource("/").getPath();
				
				/**
				 * URLDecoder.decode(classPath,"UTF-8")对中文路径进行转码
				 */
				document = builder.parse(new File(URLDecoder.decode(classPath,"UTF-8")+xmlPath));  
				XMLConfigResource resource = new XMLConfigResource();
				resource.setDocument(document);
				resource.setXmlpath(xmlPath);
				this.resource = resource;
				
			} catch (ParserConfigurationException e){
				throw new ResourceConfigException(ExcpMsg.CANNOT_CONFIG_FILES+xmlPath);
			}
			catch (SAXException e) {
				throw new ResourceConfigException(ExcpMsg.CANNOT_CONFIG_FILES+xmlPath);
			}catch (IOException e) {
				throw new ResourceConfigException(ExcpMsg.CANNOT_CONFIG_FILES+xmlPath);
			}
	}
	
	
	
	
	@Override
	public XMLConfigResource getXMLConfigResource() {
		return resource;
	}
	private  void setXMLConfigResource(XMLConfigResource resource){
		 this.resource = resource;
	}

}
