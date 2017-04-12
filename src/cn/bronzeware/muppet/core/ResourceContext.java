package cn.bronzeware.muppet.core;

import java.util.Map;
import java.util.Map.Entry;

import javax.activation.DataSource;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.core.ioc.AutowiredApplicationContext;
import cn.bronzeware.muppet.context.ContextFactory;
import cn.bronzeware.muppet.listener.Event;
import cn.bronzeware.muppet.listener.EventType;
import cn.bronzeware.muppet.listener.Listened;
import cn.bronzeware.muppet.listener.Listener;
import cn.bronzeware.muppet.listener.ListenerFactory;
import cn.bronzeware.muppet.listener.Listeners;
import cn.bronzeware.muppet.resource.Contained;
import cn.bronzeware.muppet.resource.Container;
import cn.bronzeware.muppet.resource.ResourceInfo;
import cn.bronzeware.muppet.resource.StandardContainer;
import cn.bronzeware.muppet.resource.TableInfo;

public class ResourceContext implements Contained,Listened{

	protected  ApplicationContext applicationContext = null;
	
	public Container<String, ResourceInfo> getContainer(){
		return this.container;
	}
	
	private  boolean isBooted = false;
	private  boolean isBuilded = true;

	private ContextFactory factory = null;
	private DataBaseCheck check = null;
	private EntityMappingDBXMLConfig resourceConfig ;
	private ResourceLoad resourceLoader;
	private StandardResourceBuilder resourceBuild =null;
	
	private Listeners listeners = null;
	private ListenerFactory listenerFactory = null;
	
	private DataSourceManager dataSourceManager;

	public ResourceContext(String configFilePath, ApplicationContext applicationContext) throws InitException{
		this.applicationContext = applicationContext;
		listenerFactory = new ListenerFactory(applicationContext, "cn.bronzeware.muppet.listener");
		listeners = listenerFactory.getListeners();
		init(configFilePath);
	}
	
	
	public ContextFactory getContextFactory(){
		if(factory==null){
			factory = new ContextFactory(this);
			return factory;
		}else{
			return factory;
		}
	}
	
	//public void setApplicationContext(ApplicationContext context = )
	
	private void init(String configFilePath) throws InitException{
		if(hasStarted()){
			throw new InitException() {
				
				@Override
				public String message() {
					
					return "muppet已经启动->";
				}
			};
		}
		//初始化前事件 RESOURCE_CONTEXT_INIT_PRE
		listeners.event(EventType.RESOURCE_CONTEXT_INIT_PRE, new Event(applicationContext, null));
		
		applicationContext.registerBean(ResourceContext.class, this);
		
		XMLConfig config = new StandardXMLConfig(configFilePath, applicationContext);
		applicationContext.registerBean(StandardXMLConfig.class, config);
		
		resourceConfig = new StandardEntityMappingDBXMLConfig(config.getXMLConfigResource());
		applicationContext.registerBean(StandardEntityMappingDBXMLConfig.class, resourceConfig);
		
		isBuilded = resourceConfig.isBuilded();
		
		dataSourceManager = applicationContext.getBean(DataSourceManager.class);
		dataSourceManager.datasourceCheck();
		
		check = new DataBaseCheck(applicationContext);
		applicationContext.registerBean(DataBaseCheck.class, check);
		
		resourceBuild = new StandardResourceBuilder(applicationContext);
		applicationContext.registerBean(StandardResourceBuilder.class , resourceBuild);
		
		if(isBuilded){
			resolver = new StandardAnnoResolver(applicationContext);
		}else{
			resolver = new StandardDBCheckResolver(check);
		}
		String[] basePackets = resourceConfig.getResourcePackageNames();
		resourceLoader = new StandardResourceLoader();
		applicationContext.registerBean(StandardResourceLoader.class, resourceLoader);
		
		applicationContext.registerBean(Container.class, this.container);
		Map<String, Class<?>[]> map = resourceLoader.loadClass(basePackets);
		resolveResource(map);
		
		started();
		//RESOURCE_CONTEXT_INIT_POST
		listeners.event(EventType.RESOURCE_CONTEXT_INIT_POST, new Event(applicationContext, null));
	}
	
	
	private boolean hasStarted(){
		return isBooted;
	}
	
	private void started(){
		isBooted = true;
		afterStart(applicationContext);
	}
	protected void afterStart(ApplicationContext applicationContext){
		
	}
	
	public static void main(String[] args){
		//new ResourceContext("muppet.xml");
	}
	
	
	
	private ResourceResolve resolver ;
	
	/**
	 * key 为Clazz的名字getName
	 */
	private Container<String,ResourceInfo> container = new StandardContainer();
	
	private void resolveResource(Map<String, Class<?>[]> map) throws InitException{
		if(map!=null&&map.size()>0){
			for(Entry<String, Class<?>[]> clazzs:map.entrySet())
			{
				Class<?>[] clazz = clazzs.getValue();
				String packetName = clazzs.getKey();
				try {
					for (int i = 0; i < clazz.length; i++) {
						if(clazz!=null){
								ResourceInfo resourceInfo = resolver.resolve(clazz[i]);
								if(resourceInfo==null){
									continue;
								}else{
									if(resourceInfo instanceof TableInfo)
									{
										TableInfo info = (TableInfo)resourceInfo;
										
										container.set(clazz[i].getName(),info);
										if(isBuilded){
											resourceBuild.build(info);
										}else{
											
										}
										
									}
								}
						}
					}
					
				} catch (ResourceResolveException e) {
					e.printStackTrace();
					throw e;
				}
				catch (BuildException e) {
					e.printStackTrace();
					throw e;
				}
			}
		}
	}


	
	
	@Override
	public void addListener(EventType type, Listener listener) {
		
		listeners.addListener(type, listener);
	}


}
