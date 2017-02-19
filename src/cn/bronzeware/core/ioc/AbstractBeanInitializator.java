package cn.bronzeware.core.ioc;

import cn.bronzeware.muppet.util.ArrayUtil;
import cn.bronzeware.muppet.util.log.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class AbstractBeanInitializator implements BeanInitialize {


	private ApplicationContext context = null;
	private Set<Class> toInitializateBean = new HashSet<Class>();
	public AbstractBeanInitializator(ApplicationContext context){
		this.context = context;
	}
	public <T> T initializeBean(Class<T> clazz){
		if(clazz.isInterface() == true){
			Logger.debugln(String.format("%s is interface ,unable to new a instance",clazz.getName()));
			return null;
			//throw new InitializeException(" the bean Class is interface ," + clazz.getName() + " initializing faild ");
		}
		Constructor[] constructors = clazz.getConstructors();
		T instance = null;
		Constructor<T> defaultConstructor = null;
		if(constructors.length != 0){
			for (Constructor constructor :constructors) {
				if(Objects.nonNull(constructor.getAnnotation(DefaultConstructor.class))){
					if(Objects.isNull(defaultConstructor)){
						defaultConstructor = constructor;
					}else{
						throw new InitializeException("only need one default constructor ,but " + clazz.getName() + " provides more");
					}
				}
			}
			if(Objects.isNull(defaultConstructor) && constructors.length > 1){
				throw new InitializeException("only need one default constructor ,but " + clazz.getName() + " provides more");
			}else{
				defaultConstructor = constructors[0];
			}
			Parameter[] parameters = defaultConstructor.getParameters();
			Object[] objects = new Object[parameters.length];
			for(int i = 0;i < parameters.length; i++){
				Parameter parameter = parameters[i];
				Class paramClazz = parameter.getType();
				try {
					objects[i] = context.getBean(paramClazz);
				}catch (SuchBeanNotFoundException e){
					//如果没有获取到就加载它
					if(toInitializateBean.contains(paramClazz)){
						String beanNames = ArrayUtil.getValues(toInitializateBean, " ,");
						throw new InitializeException("When bean initialization circular dependencies, please check the structure parameters of these beans 【" + beanNames + "】");
					}
					toInitializateBean.add(paramClazz);
					try {
						Object param = initializeBean(paramClazz);
						context.registerBean(param);
						objects[i] = param;
						toInitializateBean.remove(param);
					}catch (InitializeException e1){
						//加载失败抛出异常
						throw e1;
					}
				}
			}
			try {
				instance = defaultConstructor.newInstance(objects);
				return instance;
			}catch (Exception e){
				//会失败吗
				throw new InitializeException("initializing faild , the bean Class " + clazz + " initializing faild ");
			}
		}
		else {
			/**
			 * 没有构造函数时
			 */
			try{
				instance = clazz.newInstance();
				return instance;
			}catch (Exception e){
				throw new InitializeException("initializing faild , the bean Class " + clazz + " initializing faild ");
			}
		}

 }
	public List<Object>  initializeBeans(List<Class<?>> clazzList){
		List<Object> list = new ArrayList<Object>(clazzList.size());
		for(Class<?> clazz:clazzList){
			try{
				Object bean = initializeBean(clazz);
				if(Objects.nonNull(bean)) {
					list.add(bean);
				}
			}catch (InitializeException e){
				throw e;
			}
		}
		return list;
	}


 public static void main(String[]  args){


 }

 }