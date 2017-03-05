package cn.bronzeware.core.ioc;

import cn.bronzeware.core.ioc.annotation.Component;
import cn.bronzeware.core.ioc.annotation.ComponentExecutor;
import cn.bronzeware.muppet.util.ArrayUtil;
import cn.bronzeware.muppet.util.Utils;
import cn.bronzeware.muppet.util.log.Logger;
import cn.bronzeware.util.reflect.ReflectUtil;

import java.util.List;
import java.util.Objects;

/**
 * Created by yuhaiqiang on 17/2/12.
 */
abstract class AbstractApplicationContext implements ApplicationContext {

	/**
	 * beanFactory 存放具体的bean
	 */
	protected final BeanFactory beanFactory = new BaseBeanFactory();;

	@Override
	public Object[] getBeans() {
		return beanFactory.getBeans();
	}

	
	/**
	 * bean初始化工具
	 */
	protected final BeanInitialize beanInitializor  = new AbstractBeanInitializator(this);;

	/**
	 * 初始化实现 {@link Aware}接口的对象
	 */
	protected final AwareInitialize awareInitialize = new AwareInitialize(this);

	/**
	 * 初始化实现 {@link Capable}接口的类
	 */
	protected final CapableInitialize capableInitialize = new CapableInitialize(this);

	
	protected ApplicationConfig config = new StandardApplicationConfig();
	
	
	public AbstractApplicationContext() {
		
		
	}
	
	
	protected List initialieBeans(List<Class<?>> clazzList){
		List list = beanInitializor.initializeBeans(clazzList);
		return list;
	}

	@Override
	public <T> T getBean(Class<T> clazz) {
		T t = null;
		try {
			t = beanFactory.getBean(clazz);
		} catch (SuchBeanNotFoundException e) {
			Object object = null;
			try {
				object = beanInitializor.initializeBean(clazz);
			} catch (InitializeException e1) {
				Logger.error(e.getMessage());
				throw e1;
			}
			this.registerBean(object);
			t = this.getBean(clazz);
		}
		return t;
	}

	@Override
	public <T> T getBean(String beanName, Class<T> clazz) {
		T t = null;
		try {
			t = beanFactory.getBean(beanName, clazz);
		} catch (SuchBeanNotFoundException e) {
			// 没有获取到相应bean
			Object object = null;
			try {
				object = beanInitializor.initializeBean(clazz);
				// 如果按照clazz获取bean
			} catch (InitializeException e1) {
				// 根据name获取bean
				Class clazz1 = ReflectUtil.getClass(beanName);
				if (Objects.nonNull(clazz1)) {
					try {
						object = beanInitializor.initializeBean(clazz1);
						this.registerBean(object);
						t = this.getBean(beanName, clazz);

					} catch (InitializeException e2) {
						throw e;
					}
				} else {
					throw e;// 抛出最外面的异常
				}

			}

		}
		return t;
	}

	@Override
	public Object getBean(String beanName) {
		try {
			return beanFactory.getBean(beanName);
		} catch (SuchBeanNotFoundException e) {
			// 如果没有找到相应bean，那么就获取相应class，加载
			Class clazz = ReflectUtil.getClass(beanName);
			if (Objects.nonNull(clazz)) {
				return beanFactory.getBean(clazz);
			} else {
				// 如果获取不到相应bean，那么抛出异常
				throw new InitializeException(e.getMessage());
			}
		}
	}

	@Override
	public Object registerBean(String beanName, Object object) {
		return ((BaseBeanFactory) beanFactory).registerBean(beanName, object);
	}

	@Override
	public Object registerBean(Object object) {
			return ((BaseBeanFactory)this.beanFactory).registerBean(object);
	}

	@Override
	public Object registerBean(Class clazz, Object object) {
		return ((BaseBeanFactory) beanFactory).registerBean(clazz, object);
	}

	protected void awareAndCapable() {

		Object[] objects = getBeans();

		for (Object object : objects) {
			awareInitialize.initialize(object);
		}

		for (Object object : objects) {
			capableInitialize.initialize(object);
		}

	}

	protected void awareAndCapable(Object object) {

	}

	
}