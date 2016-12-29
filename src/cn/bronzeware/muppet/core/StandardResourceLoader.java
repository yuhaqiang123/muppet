package cn.bronzeware.muppet.core;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cn.bronzeware.util.reflect.ReflectUtil;

public class StandardResourceLoader implements ResourceLoad{

	private  String[] basePacket = {};
	
	/**
	 * 加载资源实体类时需要知道实体类所在的包在哪里，可以输入多组
	 * @param basePackets
	 */
	public StandardResourceLoader(String[] basePackets){
		this.basePacket = basePackets;
	}
	public StandardResourceLoader(){
		
	}
	/**
	 * 
	 * 对外接口，
	 * @param packetname,包名数组
	 */
	@Override
	public Map<String, Class<?>[]> loadClass(String[] packetname)
	 throws InitException{
		assert packetname!=null;
 		/**
		 * 实例化一个map这个map将会随着方法调用而传递
		 */
		Map<String, Class<?>[]> map = new HashMap<String, Class<?>[]>();
		for (int i = 0; i < packetname.length; i++) {
			//加载指定包下的实体类和子包
			loadClass(map,packetname[i]);
		}
		return map;
	}
	
		
	
	
	public static void main(String[] args) {

	/*	StandardResourceLoader resourceLoader = new 
				StandardResourceLoader(
						new String[]{"cn.bronzeware.muppet.entities"});
		*/
	}



	/**
	 * 	
	 *	加载指定包下面的所有包,每一个key都代表一个包，
	 * 	需要说明的是，这里的包并不仅仅代表是用户输入的包，而是类路径下
	 * 	实际的包的个数，因为用户输入的可能是父包，其下还有子文件夹也就是子包，这个子包也会
	 * 	被当做一个key被存储在map中
	 * 	相对应的Class[]数组是该包名下的所有的类
	 *  @author  
	*/
	private void loadClass(Map<String, Class<?>[]> map,String packetname)
	 throws InitException{
		try {
			Enumeration<URL> urls = this.getClass()
					.getClassLoader().
					getResources(packetname.replace(".", File.separator));
			while(urls!=null&&urls.hasMoreElements()){
				
				URL url = urls.nextElement();
				if(url!=null){
					String packetpath = url.getPath();
					File[] files = new File(URLDecoder.decode(packetpath)).listFiles(new FileFilter() {
						
						@Override
						public boolean accept(File file) {
							return (file.isFile()&&file.getName().endsWith(".class"))||file.isDirectory();
						}
					});
					loadPacketFiles(map, packetname, files);
					
				}
			}
		} catch (IOException e) {
			 
			throw new ResourceLoadException(e.getMessage());
		}
	}
	
	/**
	 * 加载文件列表中的所有的Class对象，如果这个文件属于目录，则递归加载
	 * 这个类，加载器优先使用上下文加载器，如果线程上下文加载器和本类的加载器不同，那么优先
	 * 使用上下文加载器。
	 * @param map 输入参数 ，方法返回后会将加载的Class对象存储在里面
	 * @param packetname  要加载的包，作为map中的key存储
	 * @param files  files是指要遍历的文件列表，这里面的文件可能是目录，需要进行判断
	 * 如果是目录则需要在加载这个子包
	 */
	private void loadPacketFiles(Map<String, Class<?>[]> map,String packetname,File[] files)
	{
		if(files!=null){
			Class<?>[] classArray = new Class<?>[files.length];
			int i = 0;
			for(File file:files){
				String filename = file.getName();
				
				/**
				 * 如果是文件，则加载
				 */
				if(file.isFile()){
					String className = null;
					try{
						className = filename.substring(0,filename.lastIndexOf('.'));
					}catch(Exception e){
						continue;
					}
					
					if(packetname!=null){
						className = packetname+"."+className;
				
						Class<?> clazz = ReflectUtil.getClassByContextLoader(className);
						classArray[i] = clazz;
						i++;
						
					}
				}else{
					
					/**
					 * 如果是目录那么则加载
					 */
					if(packetname!=null){
						 
						String subpatketname =packetname+"."+file.getName();
						loadClass(map, subpatketname);
					}
					
					
				}
			}
			
			Class<?>[] newClassArray = new Class<?>[i];
			System.arraycopy(classArray, 0,newClassArray, 0, i);
			map.put(packetname, classArray);
			
		}
	}




	

}
