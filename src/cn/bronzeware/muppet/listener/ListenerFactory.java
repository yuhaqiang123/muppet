package cn.bronzeware.muppet.listener;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import cn.bronzeware.muppet.core.ResourceLoad;
import cn.bronzeware.muppet.core.StandardResourceLoader;
import cn.bronzeware.muppet.util.log.Logger;
import cn.bronzeware.util.reflect.ReflectUtil;

public class ListenerFactory{

	private static Listeners listeners = new Listeners();
	private static final String LISTENER_ROOT_PACKAGE= "cn.bronzeware.muppet.listener";
	private static ResourceLoad standardResourceLoader = 
			new StandardResourceLoader();
	static{
		loadListener();
	}
	
	public static Listeners getListeners(){
		return listeners;
	}
	
	
	private static void loadListener(){
		Map<String,Class<?>[]> map = standardResourceLoader.loadClass(new String[]{LISTENER_ROOT_PACKAGE});
		Class<?>[] rootlisteners = map.get(LISTENER_ROOT_PACKAGE);
		if(rootlisteners==null){
			return;
		}else{
			for(Class clazz:rootlisteners){
				if(Listener.class.isAssignableFrom(clazz)&&!Listener.class.equals(clazz)){
					Method method;
					try {
						method = clazz.getDeclaredMethod("eventType", null);
						InvocationHandler handler = new ListernInvocationHandler();
						Listener targetLisener = ReflectUtil.getClassProxy(clazz,handler);
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
