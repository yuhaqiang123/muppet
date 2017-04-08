package cn.bronzeware.muppet.listener;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.core.ioc.aop.InterceptorConfigListener;
import cn.bronzeware.muppet.core.ResourceLoad;
import cn.bronzeware.muppet.core.StandardResourceLoader;
import cn.bronzeware.muppet.util.ArrayUtil;
import cn.bronzeware.muppet.util.log.Logger;
import cn.bronzeware.util.reflect.ReflectUtil;

public class ListenerFactory{

	private Listeners listeners = new Listeners();
	public String LISTENER_ROOT_PACKAGE= "cn.bronzeware.muppet.listener";
	private ResourceLoad standardResourceLoader = 
			new StandardResourceLoader();
	private ApplicationContext applicationContext;
	
	public ListenerFactory(ApplicationContext applicationContext, String rootPackage){
		this.applicationContext = applicationContext;
		this.LISTENER_ROOT_PACKAGE = rootPackage;
		loadListeners();
	}
	
	
	
	public Listeners getListeners(){
		return listeners;
	}
	
	protected void loadListeners(){
		Listener interceptor = new InterceptorConfigListener();
		listeners.addListener(interceptor.eventType(), interceptor);
	}
	
	
	public  void loadListener(){
		//Map<String,Class<?>[]> map = standardResourceLoader.loadClass(new String[]{LISTENER_ROOT_PACKAGE});
		//Class<?>[] rootlisteners = map.get(LISTENER_ROOT_PACKAGE);
		List<Class<?>> list = ReflectUtil.getClasses(LISTENER_ROOT_PACKAGE);
		Class<?>[] rootlisteners = list.toArray(new Class[list.size()]);
		if(rootlisteners==null){
			return;
		}else{
			for(Class clazz:rootlisteners){
				if(Listener.class.isAssignableFrom(clazz) && !Listener.class.equals(clazz)){
					Method method;
					try {
						method = clazz.getDeclaredMethod("eventType", null);
						Listener targetLisener = (Listener) applicationContext.getBean(clazz);
						EventType type = (EventType) method.invoke(targetLisener, null);
						listeners.addListener(type, targetLisener);
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
					
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}catch(UnSupportedEventTypeException e){
						
						///不支持的事件类型。一般不可能
						e.printStackTrace();
					}
					
				}else{
					continue;
				}
			}
		}
	
	}
}
