package cn.bronzeware.muppet.listener;

import java.util.HashMap;
import java.util.Map;

import cn.bronzeware.core.ioc.ApplicationContext;

public class Event {

	private ApplicationContext applicationContext;
	
	private Object object = null;
	
	
	public  Event(ApplicationContext applicationContext, Object object){
		this.applicationContext = applicationContext;
		this.object = object;
	}
	
	public Object getEventParam(){
		return object;
	}
	
	
	public ApplicationContext getContext(){
		return applicationContext;
	}
}
