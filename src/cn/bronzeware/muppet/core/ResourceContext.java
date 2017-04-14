package cn.bronzeware.muppet.core;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.activation.DataSource;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.core.ioc.AutowiredApplicationContext;
import cn.bronzeware.muppet.context.ContextFactory;
import cn.bronzeware.muppet.datasource.DataSourceListener;
import cn.bronzeware.muppet.datasource.DataSourceUtil;
import cn.bronzeware.muppet.datasource.EntityPackage;
import cn.bronzeware.muppet.datasource.EntityPkgOnDataSourceConfig;
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
	private EntityPkgOnDataSourceConfig entityPkgOnDataSourceConfig;
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
		
		StandardXMLConfig config = new StandardXMLConfig(configFilePath, applicationContext);
		applicationContext.registerBean(StandardXMLConfig.class, config);
		
		
		/**
		 * 获取实体bean和数据scheme映射的信息
		 */
		resourceConfig = getEntityMappingDBXMLConfig(config);
		//注册进容器
		applicationContext.registerBean(StandardEntityMappingDBXMLConfig.class, resourceConfig);
		
		
		/**
		 * 获取数据源管理器
		 */
		dataSourceManager = getDataSourceListener(config);
		
		/**
		 * 检查数据源
		 */
		dataSourceManager.datasourceCheck();
		
		
		entityPkgOnDataSourceConfig = (EntityPkgOnDataSourceConfig)resourceConfig;
		List<EntityPackage> entityPackages = entityPkgOnDataSourceConfig.getEntityPackage();
		
		resourceLoader = new StandardResourceLoader();
		applicationContext.registerBean(StandardResourceLoader.class, resourceLoader);
		
		applicationContext.registerBean(Container.class, this.container);
		/**
		 * 是否根据数据源配置 建设数据库scheme
		 */
		isBuilded = resourceConfig.isBuilded();
		
		for(EntityPackage p:entityPackages){
			String dataSourceKey = p.getDataSourceKey();
			String pkgName = p.getPkgName();
			DataSourceUtil dataSourceUtil = dataSourceManager.getDataSourceUtil(dataSourceKey);
			DataBaseCheck check = new DataBaseCheck(applicationContext, dataSourceUtil);
			if(isBuilded){
				resolver = new StandardAnnoResolver(applicationContext);
			}else{
				resolver = new StandardDBCheckResolver(check, dataSourceManager.getDatasourceListener());
			}
			resourceBuild = new StandardResourceBuilder(applicationContext, check, dataSourceUtil);
			
			Map<String, Class<?>[]> map = resourceLoader.loadClass(new String[]{pkgName});
			resolveResource(map);
		}
		
		started();
		//RESOURCE_CONTEXT_INIT_POST
		listeners.event(EventType.RESOURCE_CONTEXT_INIT_POST, new Event(applicationContext, null));
	}
	
	private DataSourceManager getDataSourceListener(StandardXMLConfig config){
		applicationContext.registerBean(DataSourceListener.class, config.getDataSourceListener());
		dataSourceManager = new DataSourceManager(config.getDataSourceInfo(), applicationContext);
		return dataSourceManager;
	}
	
	private EntityMappingDBXMLConfig getEntityMappingDBXMLConfig(StandardXMLConfig config){
		resourceConfig = new StandardEntityMappingDBXMLConfig(config.getXMLConfigResource());
		return resourceConfig;
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
