package cn.bronzeware.util.testframework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.bronzeware.core.ioc.ApplicationConfig;
import cn.bronzeware.core.ioc.AutowiredApplicationContext;

public class TestFrameworkApplicationContext extends AutowiredApplicationContext{

	public TestFrameworkApplicationContext(ApplicationConfig config){
		super(config);
	}
	
	public TestFrameworkApplicationContext(){
		super();
	}
	
	@Override
	protected void beforeInitialize(){
		testClasses = new HashMap<>();
	}
	
	private  Map<Class, Object> testClasses;
	
	@Override
	protected void beforeRegister(Object key, Object value) {
		super.beforeRegister(key, value);
		Class clazz = value.getClass().getSuperclass();
		if(key instanceof Class){
			clazz = (Class)key;
		}
		
		
		Test test = (Test) clazz.getDeclaredAnnotation(Test.class);
		if(test != null){
			testClasses.put(clazz, value);
		}
	}
	public Map<Class, Object> getTestUnit(){
		return testClasses;
	}
}
