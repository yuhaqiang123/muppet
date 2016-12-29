package cn.bronzeware.muppet.listener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.bronzeware.muppet.util.log.Logger;

public class ResourceContextInitPostListener implements Listener{

	@Override
	public EventType eventType() {
		
		return EventType.RESOURCE_CONTEXT_INIT_POST;
	}

	@Override
	public void event(EventType type, Event event) {
		Logger.println(ListenerLogMsg.RESOURCE_CONTEXT_INIT_POST);
	}

	public static void main(String [] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException{
		Class clazz = ResourceContextInitPostListener.class;
		Method method = clazz.getDeclaredMethod("event",new Class[]{EventType.class,Event.class});
		Object result = method.invoke(clazz.newInstance(),new Object[]{EventType.RESOURCE_CONTEXT_INIT_POST,new Event()});
		System.out.println(result);
	}
}
