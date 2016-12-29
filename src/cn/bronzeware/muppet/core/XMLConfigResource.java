package cn.bronzeware.muppet.core;

import org.w3c.dom.Document;

public class XMLConfigResource {

	private Document document;
	private String xmlpath;
	
	public XMLConfigResource(){
		
	}
	
	
	protected final void setXMLConfigResource(Document document,String xmlpath
			,Object... args){
		this.document = document;
		this.xmlpath = xmlpath;
	}
	
	

	public String getXmlpath() {
		return xmlpath;
	}

	public void setXmlpath(String xmlpath) {
		this.xmlpath = xmlpath;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}
	
}
