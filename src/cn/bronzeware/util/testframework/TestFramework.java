package cn.bronzeware.util.testframework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.bronzeware.core.ioc.ApplicationConfig;
import cn.bronzeware.core.ioc.AutowiredApplicationContext;
import cn.bronzeware.core.ioc.StandardApplicationConfig;
import cn.bronzeware.muppet.util.ArrayUtil;
import cn.bronzeware.muppet.util.Utils;
import cn.bronzeware.util.reflect.ReflectUtil;

public class TestFramework {

	private Map<String,TestUnitMetaData> testUnits = new HashMap<>();

	private Map<Class, Object> map = null;
	
	private TestFrameworkApplicationContext testFrameworkApplicationContext;
	public TestFramework(TestFrameworkApplicationContext testFrameworkApplicationContext){
		this.testFrameworkApplicationContext = testFrameworkApplicationContext;
		testFrameworkApplicationContext.registerBean(TestFramework.class, this);
		map = testFrameworkApplicationContext.getTestUnit();
	}
	
	public Map<String, TestUnitMetaData> getTestUnits(){
		return testUnits;
	}
	
	public void invokeAll() {
		for(Map.Entry<Class, Object> entry:map.entrySet()){
			this.invoke(entry.getKey(), entry.getValue());
		}
		ArrayUtil.println(testUnits);
	}

	public void invoke(Class clazz, Object object) {
		Method[] methods = clazz.getDeclaredMethods();
		if (Utils.notEmpty(methods)) {
			for (Method method : methods) {
				Test test = method.getDeclaredAnnotation(Test.class);
				if (Utils.empty(test)) {
					continue;
				}
				TestUnitMetaData metaData = new TestUnitMetaData();
				try {
					metaData.setReturnType(method.getReturnType());
					metaData.setAnnotationTypes(method.getDeclaredAnnotations());
					metaData.setTargetClass(clazz);
					metaData.setTargetParams(new Object[] {});
					metaData.setTargetMethod(method);
					metaData.setTargetObject(object);
					testUnits.put(ReflectUtil.getMethodFullName(method), metaData);
					Object result = method.invoke(object, new Object[] {});
					metaData.setReturnValue(result);
				} catch (IllegalAccessException e) {
					throw new TestUnitInvokeException(String.format("不合法的访问状态"), e);
				} catch (IllegalArgumentException e) {
					throw new TestUnitInvokeException(String.format("参数不正确，目前不支持测试方法带参数"), e);
				} catch (InvocationTargetException e) {
					throw new TestUnitInvokeException(String.format("目标对象不正确"), e);
				}
			}
		}
	}

	public static void main(String[] args) {
		StandardApplicationConfig config = new StandardApplicationConfig();
		config.setProperty(ApplicationConfig.AUTO_SCAN_PACKAGE_KEY, 
				new String[]{"cn.bronzeware.muppet.test","cn.bronzeware.util.testframework"});
		TestFrameworkApplicationContext applicationContext = new TestFrameworkApplicationContext(config);
		TestFramework testFramework = new TestFramework(applicationContext);
		testFramework.invokeAll();
		//Set<Object> set = applicationContext.getTestUnit();
		//System.out.println(set.size());
	}
}
