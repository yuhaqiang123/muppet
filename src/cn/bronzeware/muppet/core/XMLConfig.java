package cn.bronzeware.muppet.core;

public interface XMLConfig {

	public XMLConfigResource getXMLConfigResource();
	public static final String XML_ROOT_KEY = "muppet";
	public static final String XML_LINKS = "#" + XML_ROOT_KEY+"#"+"links";
	public static final String XML_LINK = XML_LINKS + "#" +"link";
	
}
