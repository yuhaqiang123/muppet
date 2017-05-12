package cn.bronzeware.core.ioc;

/**
 * <p>BeanFactory定义了获取bean的所有方式,通过自定义的beanName,bean的类型,已经通过混合获取bean
 * 通过这三种方式,你可以获取到已经注册进BeanFactory的所有bean.
 * <h3>当该bean没有被BeanFacory托管时,按定义的三种获取规则将无法返回bean实例给调用者.这时将抛出
 * {@link SuchBeanNotFoundException }异常.
 * <p>
 * 
 * @see {@link BaseBeanFactory} {@link ApplicationContext}
 * @throws SuchBeanNotFoundException
 * @author yuhaiqiang  email: yuhaiqiangvip@sina.com
 */
public interface BeanFactory {

	/**
	 * <p>推荐使用的获取 bean实例的方式,通过 该种方式获取bean实例,避免了对象的强制类型转换.
	 * <p>{@code getBean(BeanFactory.class)}
	 * @return clazz类型的bean实例.
	 * @throws SuchBeanNotFoundException 当容器没有托管该类型的实例时,抛出此异常
	 * @param clazz 类型
	 */
    <T> T getBean(Class<T> clazz);

    /**
     * <p>首先通过beanName去寻找相应bean实例,如果存在将其强转为 T 类型.此时会检查 获取的bean实例是否
     * 是T类型如果不匹配,那么将按照 {@link #getBean(Class)}去获取,如果依然获取不到将抛出 {@link SuchBeanNotFoundException}
     * <p>如果通过beanName获取不到响应的bean实例,那么将按照 {@link #getClass()}方式去获取bean实例.如果获取不到
     * 将抛出 {@link SuchBeanNotFoundException}
     * <h3>不推荐通过该方式获取bean实例
     * @throws SuchBeanNotFoundException 按照获取规则获取不到bean实例时
     * @param beanName 通过beanName获取
     * @param clazz  期待bean的类型
     * @return T类型 指定beanName的bean
     */
    <T> T getBean(String beanName, Class<T> clazz);

    /**
     * <p>通过指定beanName获取对应的bean.需要注意,通过这种方式,调用方无法知道bean的类型,需要一个
     * 强制类型转换
     * <h3>按照获取规则无法获取指定bean时,将抛出 {@link SuchBeanNotFoundException}
     * @throws SuchBeanNotFoundException
     * @param beanName 指定的beanName
     * @return 指定beanName的实例
     */
    Object getBean(String beanName);

    /**
     * 获取当前beanFactory托管的所有bean实例.
     * @return 返回对象实例数组.如果没有托管bean.那么将返回空数组
     */
    Object[] getBeans();
}


