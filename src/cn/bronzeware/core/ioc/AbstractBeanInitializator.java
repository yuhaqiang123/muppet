package cn.bronzeware.core.ioc;

import cn.bronzeware.core.ioc.aop.InterceptorManager;
import cn.bronzeware.muppet.util.ArrayUtil;
import cn.bronzeware.muppet.util.log.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * BeanInitialize 负责实现初始化一个对象的功能，
 * 简单对象通过调用Class.newInstance()方法可以实例化，复杂的对象即没有提供默认构造方法的对象
 * 实例化需要构造入参，这时，将需要向ApplicationContext获取需要的实例，如果不存在那么就递归初始化该入参实例，如果 初始化失败，那么将报错
 * 
 * @since 1.5 2017年2月26日上午11:58:10
 * @author yuhaiqiang
 *
 */
public class AbstractBeanInitializator implements BeanInitialize {

	private ApplicationContext context = null;

	// 需要实例化的Bean，如果添加时发现该实例已经存在，那么将抛出异常。
	private Set<Class> toInitializateBean = new HashSet<Class>();

	private InterceptorManage interceptorManager = null;
	
	public AbstractBeanInitializator(ApplicationContext context) {
		this.context = context;
	}

	protected InterceptorManage getInterceptorManager(){
		if(interceptorManager == null){
			interceptorManager = context.getBean(InterceptorManager.class);
		}
		return interceptorManager;
	}
	
	/**
	 * 实例化一个bean.根据Class对象检查容器是否存在相应bean实例
	 */
	public <T> T initializeBean(Class<T> clazz) {
		// 如果是接口不做处理，返回null
		if (clazz.isInterface() == true) {
			Logger.debugln(String.format("%s is interface ,unable to new a instance", clazz.getName()));
			return null;
			// 不抛出异常
			// throw new InitializeException(" the bean Class is interface ," +
			// clazz.getName() + " initializing faild ");
		}
		T instance = null;
		//instance = context.containsBean(clazz);
		//if(instance != null){
			//return instance;
		//}
		Constructor[] constructors = clazz.getConstructors();
		Constructor<T> defaultConstructor = null;
		// 获取默认的构造方法
		if (constructors.length != 0) {
			for (Constructor constructor : constructors) {
				// 如果该构造方法声明了{@link DefaultConstructor }注解，那么将其视为默认构造器
				if (Objects.nonNull(constructor.getAnnotation(DefaultConstructor.class))) {
					// 默认构造器为空时，赋值。由于默认构造器只能存在一个，如果赋值时不为空，说明存在多个，抛出异常，
					if (Objects.isNull(defaultConstructor)) {
						defaultConstructor = constructor;
					} else {
						throw new InitializeException(
								"only need one default constructor ,but " + clazz.getName() + " provides more");
					}
				}
			}

			// 如果没有通过DefatultConstructor 声明默认构造器，并且个数多于一个，说明构造器过多
			if (Objects.isNull(defaultConstructor) && constructors.length > 1) {
				throw new InitializeException(
						"only need one default constructor ,but " + clazz.getName() + " provides more");
			} else {
				defaultConstructor = constructors[0];
			}

			/**
			 * 获取参数，并且从ApplicationContext中获取
			 */
			Parameter[] parameters = defaultConstructor.getParameters();
			Object[] objects = new Object[parameters.length];
			Class[] paramClazzs = new Class[parameters.length];
			for (int i = 0; i < parameters.length; i++) {
				Parameter parameter = parameters[i];
				Class paramClazz = parameter.getType();
				paramClazzs[i] = paramClazz;
				try {
					objects[i] = context.getBean(paramClazz);
				} catch (SuchBeanNotFoundException e) {
					// 如果没有获取到就加载它，如果toInitializateBean中存在，说明出现初始化参数循环依赖问题
					if (toInitializateBean.contains(paramClazz)) {
						String beanNames = ArrayUtil.getValues(toInitializateBean, " ,");
						throw new InitializeException(
								"When bean initialization circular dependencies, please check the structure parameters of these beans 【"
										+ beanNames + "】");
					}
					toInitializateBean.add(paramClazz);
					try {
						// 初始化这个bean,然后注册如果这个时候触发InitializeException，则报错
						Object param = initializeBean(paramClazz);
						context.registerBean(param);
						objects[i] = param;
						toInitializateBean.remove(param);
					} catch (InitializeException e1) {
						// 加载失败抛出异常
						throw e1;
					}
				}
			}
			
			// 准备好所有的参数，可以实例化了
			try {
				//instance = defaultConstructor.newInstance(objects);
				/**
				 * 根据默认构造方法生成对象实例后，需要InterceptorManager生成相关代理
				 * 这样便于容器对bean实现更强大的管理，例如Aop需要bean的代理支持，
				 * 代理逻辑的实现即由InterceptorManager查询客户端相关aop配置，生成相应代理增强类
				 */
				instance = (T)getInterceptorManager().intercept(clazz, paramClazzs, objects);
				//将代理类注册进ioc
				//this.context.registerBean(clazz, instance);
				return instance;
			} catch (Exception e) {
				// 会失败吗
				e.printStackTrace();
				throw new InitializeException("initializing faild , the bean Class " + clazz + " initializing faild " + e.getMessage());
			}
		} else {
			/**
			 * 没有构造函数时
			 */
			try {
				//instance = clazz.newInstance();
				
				instance = (T) interceptorManager.intercept(clazz,new Class[]{}, new Object[]{});
				//this.context.registerBean(clazz, instance);
				return instance;
			} catch (Exception e) {
				throw new InitializeException("initializing faild , the bean Class " + clazz + " initializing faild ");
			}
		}
	}

	
	

}