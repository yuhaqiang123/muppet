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
public class AbstractApplicationContext implements ApplicationContext {

	/**
	 * beanFactory 存放具体的bean
	 */
	private final BeanFactory beanFactory;

	@Override
	public Object[] getBeans() {
		return beanFactory.getBeans();
	}

	/**
	 * 自动扫描所在的包
	 */
	private String autoScanPackage = "";

	/**
	 * bean初始化工具
	 */
	private BeanInitialize beanInitializor;

	/**
	 * 初始化实现 {@link Aware}接口的对象
	 */
	private AwareInitialize awareInitialize = new AwareInitialize(this);

	/**
	 * 初始化实现 {@link Capable}接口的类
	 */
	private CapableInitialize capableInitialize = new CapableInitialize(this);

	private ComponentExecutor componentExecutor = new ComponentExecutor(this);

	public AbstractApplicationContext() {
		ApplicationConfig config = new StandardApplicationConfig();
		autoScanPackage = (String) config.getProperty(ApplicationConfig.AUTO_SCAN_PACKAGE_KEY);
		List<Class<?>> clazzList = ReflectUtil.getClasses(autoScanPackage);
		beanFactory = new BaseBeanFactory();
		this.registerBean(ComponentExecutor.class, componentExecutor);
		beanInitializor = new AbstractBeanInitializator(this);
		List list = beanInitializor.initializeBeans(clazzList);
		for (Object object : list) {
			this.registerBean(object);
		}
		this.registerBean(ApplicationContext.class, this);
		awareAndCapable();
		boot();
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
		Component component = this.componentExecutor.execute(object);
		if (Utils.notEmpty(component)) {
			Object result = null;
			if (!component.type().equals(Component.TYPE_DEFAULT)) {
				result = this.registerBean(component.type(), object);
			}
			if (!component.name().equals(Component.NAME_DEFAULT)) {
				result = this.registerBean(component.name(), object);
			}

			return result;
		}
		else{
			return ((BaseBeanFactory)this.beanFactory).registerBean(object);
		}
	}

	@Override
	public Object registerBean(Class clazz, Object object) {
		return ((BaseBeanFactory) beanFactory).registerBean(clazz, object);
	}

	protected void boot() {

	}

	protected void awareAndCapable(Object object) {

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
}