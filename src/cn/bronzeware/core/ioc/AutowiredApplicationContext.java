package cn.bronzeware.core.ioc;

import java.util.List;

import cn.bronzeware.core.ioc.annotation.Component;
import cn.bronzeware.core.ioc.annotation.ComponentExecutor;
import cn.bronzeware.muppet.util.ArrayUtil;
import cn.bronzeware.muppet.util.Utils;
import cn.bronzeware.util.reflect.ReflectUtil;

public class AutowiredApplicationContext extends AbstractApplicationContext{
	
	private String autoScanPackage ;
	
	
	protected final ComponentExecutor componentExecutor = new ComponentExecutor(this);
	
	public AutowiredApplicationContext(){
		super();
		autoScanPackage = (String) config.getProperty(ApplicationConfig.AUTO_SCAN_PACKAGE_KEY);
		if(Utils.notEmpty(autoScanPackage)){
			List<Class<?>> clazzList = ReflectUtil.getClasses(autoScanPackage);
			clazzList = this.componentExecutor.execute(clazzList);
			this.registerBean(ComponentExecutor.class, componentExecutor);
			List list = initialieBeans(clazzList);
			for (Object object : list) {
				this.registerBean(object);//根据Component注解上的信息配置
			}
			this.registerBean(ApplicationContext.class, this);
			awareAndCapable();
		}else{
			throw new BeanInitializationException("bean initialization has error happend , can not found autowird configs, please check");
		}
	}
	
	

	@Override
	public Object registerBean(Object object) {
		Component component = this.componentExecutor.execute(object.getClass());
		if (Utils.notEmpty(component)) {
			Object result = null;
			if (!component.type().equals(Component.TYPE_DEFAULT)) {
				result = this.registerBean(component.type(), object);
			}
			if (!component.name().equals(Component.NAME_DEFAULT)) {
				result = this.registerBean(component.name(), object);
			}
			if(component.type().equals(Component.TYPE_DEFAULT) && component.name().equals(Component.NAME_DEFAULT)){
				result = super.registerBean(object);
			}
			return result;
		}
		else{
			return super.registerBean(object);
		}
	}
	
}
