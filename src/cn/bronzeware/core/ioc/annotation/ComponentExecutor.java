package cn.bronzeware.core.ioc.annotation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.omg.CORBA.PRIVATE_MEMBER;

import com.sun.istack.internal.FinalArrayList;

import cn.bronzeware.core.ioc.ApplicationConfig;
import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.core.ioc.BeanInitializationException;
import cn.bronzeware.muppet.util.Utils;
import cn.bronzeware.muppet.util.log.Logger;

public class ComponentExecutor {

	private ApplicationContext applicationContext;

	public ComponentExecutor(ApplicationContext context) {
		this.applicationContext = context;
	}

	public Component execute(Class clazz) {
		Component component = (Component)clazz.getAnnotation(Component.class);
		if (Utils.notEmpty(component)) {
			//如果Component 声明的子类型０
			if ( !component.type().equals(Component.TYPE_DEFAULT) && !component.type().isAssignableFrom(clazz)){
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
	
	public List<Class<?>> execute(List<Class<?>> list){
		List<Class<?>> results = new ArrayList<>();
		for(Class clazz:list){
			Component component = this.execute(clazz);
			if(Utils.notEmpty(component)){
				//Logger.println(clazz);
				results.add(clazz);
			}
		}
		return results;
	}

}
