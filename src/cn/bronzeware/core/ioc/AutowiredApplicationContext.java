package cn.bronzeware.core.ioc;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.bronzeware.core.ioc.annotation.AutowiredExecutor;
import cn.bronzeware.core.ioc.annotation.Component;
import cn.bronzeware.core.ioc.annotation.ComponentExecutor;
import cn.bronzeware.muppet.util.ArrayUtil;
import cn.bronzeware.muppet.util.Utils;
import cn.bronzeware.util.reflect.ReflectUtil;

public class AutowiredApplicationContext extends AbstractApplicationContext{
	
	private String autoScanPackage ;
	
	
	protected final ComponentExecutor componentExecutor = new ComponentExecutor(this);
	
	protected final AutowiredExecutor autowiredExecutor = new AutowiredExecutor(this);
	
	public AutowiredApplicationContext(){
		super();
		this.registerBean(ComponentExecutor.class, componentExecutor);
		this.registerBean(ApplicationContext.class, this);
		this.registerBean(BeanMetaContext.class, metas);
		//获取自动扫描包的目录
		autoScanPackage = (String) config.getProperty(ApplicationConfig.AUTO_SCAN_PACKAGE_KEY);
		if(Utils.notEmpty(autoScanPackage)){
			//获取包下所有的类
			List<Class<?>> clazzList = ReflectUtil.getClasses(autoScanPackage);
			//System.out.println(autoScanPackage);
			//ArrayUtil.println(clazzList);
			//获取所有添加Component注解的类
			clazzList = this.componentExecutor.execute(clazzList);
			//获取所有单例类
			//clazzList = this.singletonBeans(clazzList);
			configBeanClass(clazzList);
			//初始化所有 singleton Class实例
			List list = initializeBeans(clazzList);
			//ArrayUtil.println(list);
			
			//将componentExecutor和ApplicationContext放进去
			
			
			//自动装配
			autowireds(list);
			
			//解析aware和capable
			awareAndCapable();
			isRefresh = true;
			
		}else{
			throw new BeanInitializationException("bean initialization has error happend , can not found autowird configs, please check");
		}
	}
	
	protected List<Class<?>> singletonBeans(List<Class<?>> list){
		List<Class<?>> results = new ArrayList<>(list.size() * 9/10);
		for(Class clazz:list){
			BeanMeta meta = metas.getMeta(clazz);
			if(meta.getSocpe().equals(Component.Scope.singleton)){
				results.add(clazz);
			}
		}
		return results;
	}
	

	@Override
	public Object registerBean(Object object) {
		Component component = this.componentExecutor.execute(object.getClass());
		if (Utils.notEmpty(component)) {
			Object result = null;
			if (!component.type().equals(Component.TYPE_DEFAULT)) {
				result = this.registerBean(component.type(), object);
			}else{
				result = super.registerBean(object.getClass(), object);
			}
			
			if (!component.name().equals(Component.NAME_DEFAULT)) {
				result = this.registerBean(component.name(), object);
			}
			
			
			if(component.type().equals(Component.TYPE_DEFAULT) && component.name().equals(Component.NAME_DEFAULT)){
				result = super.registerBean(object);
			}
			return result;
		}
		else{
			return super.registerBean(object);
		}
	}
	
	protected void refreshBean(Object object){
		super.refreshBean(object);
		if(isRefresh){
			autowired(object);
		}
	}
	
	
	protected void autowired(Object object){
		Field[] fields = object.getClass().getDeclaredFields();
		for(Field field:fields){
			autowiredExecutor.execute(field, object);
		}
	}
	
	private void autowireds(List<Object> list){
		for(Object object:list){
			autowired(object);
		}
	}

	
}
