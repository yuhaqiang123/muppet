package cn.bronzeware.muppet.core;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;

public class XMLConfigResource {

	private String xmlpath;
	
	private Map<String, Object> map = new HashMap<>();
	
	public XMLConfigResource(){
		
	}
	
	
	public Object getProp(String key){
		return map.get(key);
	}
	
	public void setProp(String key,Object value){
		map.put(key, value);
	}
	
	
	protected final void setXMLConfigResource(Document document,String xmlpath
			,Object... args){
		
		this.xmlpath = xmlpath;
	}
	
	

	public String getXmlpath() {
		return xmlpath;
	}

	public void setXmlpath(String xmlpath) {
		this.xmlpath = xmlpath;
	}

	
}
