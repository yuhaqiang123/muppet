package cn.bronzeware.core.ioc;

import cn.bronzeware.muppet.util.ArrayUtil;
import cn.bronzeware.util.reflect.ReflectUtil;

import java.util.List;

/**
 * Created by yuhaiqiang on 17/2/12.
 */
public class AbstractApplicationContext  implements ApplicationContext {


    private final BeanFactory beanFactory;

    @Override
    public Object[] getBeans() {
        return beanFactory.getBeans();
    }

    private String autoScanPackage = "";
    private BeanInitialize beanInitializor;

    public AbstractApplicationContext() {
        ApplicationConfig config = new StandardApplicationConfig();
        autoScanPackage = (String) config.getProperty(ApplicationConfig.AUTO_SCAN_PACKAGE_KEY);
        List<Class<?>> clazzList = ReflectUtil.getClasses(autoScanPackage);
        beanFactory = new BaseBeanFactory();
        beanInitializor = new AbstractBeanInitializator(this);
        List list = beanInitializor.initializeBeans(clazzList);
        for (Object object : list) {
            this.registerBean(object);
        }
        awareAndCapable();
        boot();
    }


    @Override
    public <T> T getBean(Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    @Override
    public <T> T getBean(String beanName, Class<T> clazz) {
        return beanFactory.getBean(beanName, clazz);
    }

    @Override
    public Object getBean(String beanName) {
        return beanFactory.getBean(beanName);
    }

    @Override
    public Object registerBean(String beanName, Object object) {
        return ((BaseBeanFactory) beanFactory).registerBean(beanName, object);
    }

    @Override
    public Object registerBean(Object object) {
        return ((BaseBeanFactory) beanFactory).registerBean(object);
    }

    protected void boot() {

    }

    protected void awareAndCapable() {

        Object[] objects = getBeans();
        for(Object object:objects){
            AwareInitialize awareInitialize = new AwareInitialize(this);
            awareInitialize.initialize(object);
        }
        for(Object object:objects){
            CapableInitialize capableInitialize = new CapableInitialize(this);
            capableInitialize.initialize(object);
        }

    }
}