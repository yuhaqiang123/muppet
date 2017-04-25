 package cn.bronzeware.util.testframework;

import java.util.LinkedList;
import java.util.List;

import cn.bronzeware.core.ioc.annotation.Component;
import cn.bronzeware.muppet.util.Utils;

public class TestAnnotationExecutor {

	public Test execute(Class clazz) {
		Test test = (Test)clazz.getAnnotation(Test.class);
		if(Utils.notEmpty(test)){
			return test;
		}
		else{
			return null;
		}
	}
	
	public List<Class<?>> execute(List<Class<?>> list) {
		List results = new LinkedList<>();
		for(Class<?> clazz : list){
			if(null != execute(clazz)){
				results.add(clazz);
			}
		}
		return results;
	}
	
	
}
