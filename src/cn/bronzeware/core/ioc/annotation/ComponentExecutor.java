package cn.bronzeware.core.ioc.annotation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.LayoutStyle.ComponentPlacement;

import org.omg.CORBA.PRIVATE_MEMBER;

import com.sun.istack.internal.FinalArrayList;

import cn.bronzeware.core.ioc.ApplicationConfig;
import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.core.ioc.BeanInitializationException;
import cn.bronzeware.core.ioc.BeanMeta;
import cn.bronzeware.core.ioc.BeanMetaContext;
import cn.bronzeware.muppet.context.Context;
import cn.bronzeware.muppet.util.Utils;
import cn.bronzeware.muppet.util.log.Logger;

public class ComponentExecutor {

	private ApplicationContext applicationContext;

	public ComponentExecutor(ApplicationContext context) {
		this.applicationContext = context;
	}

	public Component execute(Class clazz) {
		Component component = (Component) clazz.getAnnotation(Component.class);
		if (Utils.notEmpty(component)) {
			// 如果Component 声明的子类型０
			if (!component.type().equals(Component.TYPE_DEFAULT) && !component.type().isAssignableFrom(clazz)) {
				throw new BeanInitializationException(String.format(
						"bean initialization error happened ,the bean which type is %s has inconrrect %s annotation ,"
								+ "the type property in %s must be the bean's parent type ",
						clazz.getName(), Component.class.getName(), Component.class.getName()));
			}
			return component;
		} else {
			return null;
		}
	}

	public List<Class<?>> execute(List<Class<?>> list) {
		List<Class<?>> results = new ArrayList<>();
		BeanMetaContext metas = applicationContext.getBean(BeanMetaContext.class);
		for (Class clazz : list) {
			Component component = this.execute(clazz);
			if (Utils.notEmpty(component)) {
				BeanMeta meta = new BeanMeta();
				meta.setAnnotation(component);
				String beanName = component.name().equals(Component.NAME_DEFAULT)? 
						(BeanMeta.DEFAULT_BEAN_NAME_PREFIX + clazz.getName())
						:component.name();
				meta.setBeanName(beanName);
				meta.setSocpe(component.scope());
				meta.setClazz(clazz);
				Class type = component.type().equals(Component.TYPE_DEFAULT) ?
						clazz:component.type();
				meta.setType(type);
				metas.setMeta(meta);
				
				results.add(clazz);
			}
		}
		return results;
	}

}
