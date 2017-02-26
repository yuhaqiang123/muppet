package cn.bronzeware.core.ioc.annotation;

import org.omg.CORBA.PRIVATE_MEMBER;

import cn.bronzeware.core.ioc.ApplicationConfig;
import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.core.ioc.BeanInitializationException;
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
			//如果Component 声明的子类型０
			if ( !component.type().isAssignableFrom(object.getClass())){
				throw new BeanInitializationException(
						String.format("bean initialization error happened ,the bean which type is %s has inconrrect %s annotation ,"
								+ "the type property in %s must be the bean's parent type ", clazz.getName(), Component.class.getName(), Component.class.getName()) );
			}
			return component;
		}
		else{
			return null;
		}
	}

}
