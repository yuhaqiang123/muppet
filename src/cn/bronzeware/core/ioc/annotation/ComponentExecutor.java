package cn.bronzeware.core.ioc.annotation;

import org.omg.CORBA.PRIVATE_MEMBER;

import cn.bronzeware.core.ioc.ApplicationConfig;
import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.muppet.util.Utils;

public class ComponentExecutor {

	private ApplicationContext applicationContext;

	public ComponentExecutor(ApplicationContext context) {
		this.applicationContext = context;
	}

	public Component execute(Object object) {
		Class clazz = object.getClass();
		Component component = (Component)clazz.getAnnotation(Component.class);
		if (Utils.notEmpty(component)) {
			return component;
		}
		else{
			return null;
		}
	}

}
