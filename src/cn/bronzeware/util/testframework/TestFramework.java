package cn.bronzeware.util.testframework;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import cn.bronzeware.core.ioc.ApplicationConfig;
import cn.bronzeware.core.ioc.AutowiredApplicationContext;
import cn.bronzeware.core.ioc.StandardApplicationConfig;
import cn.bronzeware.muppet.util.ArrayUtil;
import cn.bronzeware.muppet.util.FileUtil;
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

	
	public Map<String, Map<String, TestUnitMetaData>> getUnitMap(String path){
		File root =  new File(path);
		Map<String, Map<String, TestUnitMetaData>> map = new TreeMap<>(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				if(Long.parseLong(o1) < Long.parseLong(o2)){
					return 1;
				}else{
					return -1;
				}
			}
			
		});
		File[] files = root.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if(pathname.getAbsolutePath().indexOf("xml") > -1){
					String time = pathname.getName().substring(0, pathname.getName().indexOf("."));
					map.put(time, new XmlTestUnitStorage(pathname.getAbsolutePath()).resolve());
					return true;
				}
				if(pathname.getAbsolutePath().indexOf("serial") > -1){
					String time = pathname.getName().substring(0, pathname.getAbsolutePath().indexOf("."));
					map.put(time, new SerializationTestUnitStorage(pathname.getAbsolutePath()).resolve());
					return true;
				}
				return false;
			}
		});
		Map<String, Map<String, TestUnitMetaData>> resultMap = new HashMap<>();
		int i = 0;
		for(Map.Entry<String, Map<String, TestUnitMetaData>> entry:map.entrySet()){
			if(++i ==7){
				break;
			}
			resultMap.put(entry.getKey(), entry.getValue());
		}
		return resultMap;
	}
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		StandardApplicationConfig config = new StandardApplicationConfig();
		config.setProperty(ApplicationConfig.AUTO_SCAN_PACKAGE_KEY, 
				new String[]{"cn.bronzeware.muppet.test","cn.bronzeware.util.testframework"});
		TestFrameworkApplicationContext applicationContext = new TestFrameworkApplicationContext(config);
		TestFramework testFramework = new TestFramework(applicationContext);
		testFramework.invokeAll();
		String path = String.format(FileUtil.convertFilePathOnOs("bin/test/xml/%s.xml"), System.currentTimeMillis());
		//String path = String.format("bin/test/serialize/%s.serial", System.currentTimeMillis());
		TestUnitStorage storage = new XmlTestUnitStorage(path);
		//ArrayUtil.println(testFramework.getTestUnits());

		Map<String, Map<String, TestUnitMetaData>> oldMap = testFramework.getUnitMap("bin/test/xml");
		storage.store(testFramework.getTestUnits());
		HtmlTestUnitStorage html = new HtmlTestUnitStorage(FileUtil.convertFilePathOnOs("bin/test/html"));
		Map<String, File> fileMap = html.initialize(oldMap, testFramework.getTestUnits());
		System.out.println("time:" + (System.currentTimeMillis() - startTime));
	}
}
