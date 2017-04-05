package cn.bronzeware.util.testframework;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cn.bronzeware.muppet.util.FileUtil;
import cn.bronzeware.muppet.util.XMLUtil;
import cn.bronzeware.util.reflect.ReflectUtil;

public class XmlTestUnitStorage implements TestUnitStorage{

	private String path = null;
	
	public XmlTestUnitStorage(String path){
		this.path = path;
	}
	
	public Map<String, TestUnitMetaData> parse(Document document){
		Map<String, TestUnitMetaData> map = new HashMap<>();
		Node root = document.getChildNodes().item(0);
		NodeList nodes = root.getChildNodes();
		//ArrayUtil.println(XMLUtil.convertNodeList(nodes));
		for(Node node: XMLUtil.convertNodeList(nodes)){
			if(node.getNodeName().equals("unit")){
				TestUnitMetaData metaData = new TestUnitMetaData();
				NamedNodeMap namedNodeMap = node.getAttributes();
				for(Node n:XMLUtil.convertNodeList(namedNodeMap)){
					if(n.getNodeName().equals("class")){
						metaData.setTargetClass(ReflectUtil.getClass(n.getNodeValue()));
					}
					if(n.getNodeName().equals("method")){
						metaData.setMethodName(n.getNodeValue());;
					}
					
					if(n.getNodeName().equals("id")){
						metaData.setMethodName(n.getNodeValue());;
					}
				}
				NodeList nodeList = node.getChildNodes();
				for(Node n: XMLUtil.convertNodeList(nodeList)){
					if(n.getNodeName().equals("return")){
						NamedNodeMap namedNodeMap2 = n.getAttributes();
						List<Node> listNode = XMLUtil.convertNodeList(namedNodeMap2);
						for(Node n1:listNode){
							if(n1.getNodeName().equals("type")){
								metaData.setReturnType(ReflectUtil.getClass(n1.getNodeValue()));
							}
							if(n1.getNodeName().equals("value")){
								metaData.setReturnValue(n1.getNodeValue().equals("null") ? null:n1.getNodeValue());
							}
						}
					}
					
					if(n.getNodeName().equals("cmdout")){
						NamedNodeMap namedNodeMap2 = n.getAttributes();
						List<Node> listNode = XMLUtil.convertNodeList(namedNodeMap2);
						for(Node n1:listNode){
							if(n1.getNodeName().equals("value")){
								metaData.setCmdOutput(n1.getNodeValue().equals("null") ?null:n1.getNodeValue());
							}
						}
					}
					if(n.getNodeName().equals("cmderr")){
						NamedNodeMap namedNodeMap2 = n.getAttributes();
						List<Node> listNode = XMLUtil.convertNodeList(namedNodeMap2);
						for(Node n1:listNode){
							if(n1.getNodeName().equals("value")){
								metaData.setCmdErr(n1.getNodeValue().equals("null")? null:n1.getNodeValue());
							}
						}
					}
					
				}
				map.put(metaData.getMethodName(), metaData);
			}
		}
		return map;
		
	}
	
	@Override
	public Map<String, TestUnitMetaData> resolve(){
		Document document = XMLUtil.parseDoc(path);
		return parse(document);
	}
	
	
	public void initialize(Document document, Map<String, TestUnitMetaData> testUnits){
		Element element = document.createElement("Test");
		document.appendChild(element);
		for(Map.Entry<String, TestUnitMetaData> entry : testUnits.entrySet()){
			Element e = document.createElement("unit");
			TestUnitMetaData metaData = entry.getValue();
			e.setAttribute("id", ReflectUtil.getMethodFullName(metaData.getTargetMethod()));
			e.setAttribute("method", ReflectUtil.getMethodFullName(metaData.getTargetMethod()));
			e.setAttribute("class", metaData.getTargetClass().getName());
			
			Element returnDetail = document.createElement("return");
			returnDetail.setAttribute("value", metaData.getReturnValue() == null ? "null": metaData.getReturnValue().toString());
			returnDetail.setAttribute("type", metaData.getReturnType().getName());
			
			Element cmdOutDetail = document.createElement("cmdout");
			cmdOutDetail.setAttribute("value", metaData.getCmdOutput());
			
			Element cmdErrDetail = document.createElement("cmderr");
			cmdErrDetail.setAttribute("value", metaData.getCmdErr());
			
			e.appendChild(returnDetail);
			e.appendChild(cmdOutDetail);
			e.appendChild(cmdErrDetail);
			
			element.appendChild(e);
		}
	}
	
	
	@Override
	public Object store(Map<String, TestUnitMetaData> testUnits) {
		
		DocumentBuilderFactory documentBuilderFactory  =  DocumentBuilderFactory.newInstance();
		Document document = null;
		DocumentBuilder builder = null;
		try {
			builder = documentBuilderFactory.newDocumentBuilder();
			document = builder.newDocument();
			initialize(document, testUnits);
			TransformerFactory tf = TransformerFactory.newInstance();
			try {
	            Transformer transformer = tf.newTransformer();  
	            DOMSource source = new DOMSource(document);  
	            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	            transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
	            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	            File file = new File(path);
	            FileUtil.createFile(file);
	            PrintWriter pw = new PrintWriter(new FileOutputStream(file));
	            StreamResult result = new StreamResult(pw);
	            transformer.transform(source, result);     //关键转换  
	        } catch (Exception e) {  
	            e.printStackTrace();
	        }
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public static void main(String[] args){
		
	}

}
