package cn.bronzeware.util.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.sun.org.apache.xerces.internal.parsers.StandardParserConfiguration;
import com.sun.xml.internal.bind.api.impl.NameConverter.Standard;

import cn.bronzeware.muppet.util.ArrayUtil;
import cn.bronzeware.muppet.util.log.Logger;
import cn.bronzeware.test.StaticClass;
import cn.bronzeware.test.TestHashMap1;
import net.sf.cglib.core.ReflectUtils;

/**
 * 反射工具类
 * @author 于海强
 *
 */
public class ReflectUtil {

	private final static Map<String, Class> classMap = 
			new ConcurrentHashMap<>(30);
	private final static Map<Class<?>,Field[]> fieldsMap = 
			new ConcurrentHashMap();

	public static void main(String[] args){
		getClassPath();
	}

	public static String getClassPath(){
		return (Thread.class.getResource("/").getPath());
	}
	
	
	public static String getMethodFullName(Method method){
		Class clazz = method.getDeclaringClass();
		return String.format("%s.%s", clazz.getName(), method.getName());
	}
	
	
	public static Method[] getMethods(Class clazz, String methodName){
		Method[] methods = clazz.getDeclaredMethods();
		List<Method> list = new ArrayList();
		for(Method method:methods){
			if(method.getName().equals(methodName)){
				list.add(method);
			}
		}
		return list.toArray(new Method[list.size()]);
	}



	public static List<Class<?>> getClasses(String packageName){
		return ClassUtils.getClasses(packageName);
	}
	
	public static List<Class<?>> getClasses(String[] pkgNames){
		List<Class<?>> result = new ArrayList<>(100);
		for(String pkg:pkgNames){
			List<Class<?>> list = ClassUtils.getClasses(pkg);
			if(list == null){
				continue;
			}
			result.addAll(list);
		}
		return result;
	}


	/**
	 * 默认的代理实现,实现了{@link BindInvocationHandler}接口
	 */
	//private static BindInvocationHandler defaultProxy = new DefaultBindInvocationHandler();
	
	/**
	 * 
	 * {@link StandardBindInvocationHandler} 构造器需要一个{@link InvocationHandler}参数类型构造器，<br/><br/>
	 * 将请求委托给该构造参数，所以可以做一个简单的缓存，根据 在构造参数的类型，保存相应的{@link StandardBindInvocationHandler}类型实例<br/>
	 */
/*	private static Map<Class<? extends InvocationHandler> ,StandardBindInvocationHandler> stardardProxy = 
			new ConcurrentHashMap<>();
	*/
	
	/**
	 * 根据具体对象生成代理对象，代理加强采用默认的实现。
	 * 如果输入对象没有实现任何接口则采用cglib生成类的代理
	 * @param target 要代理的对象
	 * @return
	 */
	public static Object getProxy(Object target){
		return new DefaultBindInvocationHandler().bind(target);
	}
	
	/**
	 * 对类进行代理，而不是针对具体接口，代理加强采用默认加强
	 * @param target 要被代理的类
	 * @return 返回代理类
	 */
	public static <T> T  getClassProxy(Class<T> target){
		try {
			return new DefaultBindInvocationHandler().getClassProxy(target.newInstance());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取代理对象，增强方式由用户指定，值得一说的是增强类型必须实现了 {@link BindInvocationHandler}
	 * 一般情况下不需要使用这个方法，因为我们已经提供了 {@link DefaultBindInvocationHandler}默认实现
	 * 如何你需要有其他的绑定请求，可以实现这个接口，然后调用这个方法，获取代理
	 * 
	 * @param target 目标对象
	 * @param handler 代理加强处理器
	 * @return
	 */
	public static Object getProxy(Object target,BindInvocationHandler handler){
		return handler.bind(target);
	}
	
	/**
	 * 获取代理对象 可以通过指定待代理的接口，获取实现指定接口的代理
	 * 但是如果被代理对象如果没实现这个接口，那么将抛出{@link IllegalArgumentException}异常
	 * @param target
	 * @param targetInterface
	 * @throws IllegalArgumentException
	 * @return
	 */
	public static <T> T getProxy(Object target,Class<T> targetInterface)
	{
		return new DefaultBindInvocationHandler().bind(target, targetInterface);
	}
	
	/**
	 * 获取代理对象，根据指定的接口，返回指定接口的代理对象，同时需要指定绑定操作的实现方式
	 * @param target 被 代理对象
	 * @param targetInterface 指定代理的接口
	 * @param handler 绑定代理加强处理器
	 * @throws IllegalArgumentException  如果target 没有实现 targetInterface接口 
	 * @return
	 */
	public static <T> T getProxy(Object target
			,Class<T> targetInterface 
			,BindInvocationHandler handler){
		return handler.bind(target, targetInterface);
	}
	
	/**
	 * 获取指定对象的代理，同时指定代理增强器
	 * @param target 被代理的对象
	 * @param invocationHandler 代理增强器
	 * @return 代理对象
	 */
	public static Object getProxy(Object target,InvocationHandler invocationHandler){
			StandardBindInvocationHandler handler = new StandardBindInvocationHandler(invocationHandler);
			return handler.bind(target);
	}
	
	/**
	 * 获取对类的代理，指定类型，指定代理加强器
	 * @param target  被代理的类
	 * @param invocationHandler 代理加强
	 * @return
	 */
	public static <T> T  getClassProxy(Class<T> target,InvocationHandler invocationHandler){
		
		try {
			StandardBindInvocationHandler handler = new StandardBindInvocationHandler(invocationHandler);
			return handler.getClassProxy(target.newInstance());
		} catch (InstantiationException e) {
			
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 指定被代理的对象，代理加强器，获取代理对象
	 * @param target 目标对象
	 * @param invocationHandler 代理加强对象
	 * @return
	 */
	public static <T> T  getClassProxy(T target,InvocationHandler invocationHandler){
		
			StandardBindInvocationHandler handler = new StandardBindInvocationHandler(invocationHandler);
			return handler.getClassProxy(target);
	}
	
	public static <T> T  getClassProxy(T target
			,InvocationHandler invocationHandler
			,Class[] constructorArgsClazzs
			,Object[] constructorArgsValues){
		
		StandardBindInvocationHandler handler = new StandardBindInvocationHandler(invocationHandler);
		return handler.getClassProxy(target,constructorArgsClazzs,constructorArgsValues);
	}
	
	
	
	public static <T> T  getClassProxy(Class<T> target
			,ProxyInvocationHandler proxyInvocationHandler
			,Class[] constructorArgsClazzs
			,Object[] constructorArgsValues){
		ExtendsionBindInvocationHandler handler = new ExtendsionBindInvocationHandler(proxyInvocationHandler);
		return handler.getClassProxy(target,constructorArgsClazzs,constructorArgsValues);
	}
	
	
	
	
	/**
	 * 根据指定的CLass对象实例化一个对象
	 * @param clazz
	 * @return
	 */
	public static <T> T getObject(Class<T> clazz){
		try {
			T object = clazz.newInstance();
			return object;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 优先使用线程上下文加载器加载类。
	 * @param name
	 * @return
	 */
	public static Class<?> getClassByContextLoader(String name){
		try {
			synchronized(classMap){
				if(classMap.containsKey(name)){
					return classMap.get(name);
				}
				ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
				ClassLoader selfClassLoader = ReflectUtil.class.getClassLoader();
				Class<?> clazz = null;
				
				if(contextClassLoader!=null&&contextClassLoader!=selfClassLoader){
					clazz = Class.forName(name,false,contextClassLoader);
				}else{
					clazz = Class.forName(name,false,selfClassLoader);
				}
				
				classMap.put(name, clazz);
				return clazz;
			}
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	
	
	/**
	 * 根据类名获取一个类
	 * @param name
	 * @return
	 */
	public static Class<?> getClass(String name){
		try {
			synchronized(classMap){
				if(classMap.containsKey(name)){
					return classMap.get(name);
				}
				Class<?> clazz = null;
				try{
					clazz = Class.forName(name);
				}catch (ClassNotFoundException e){
					ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
					if(Objects.nonNull(classLoader)){
						clazz = Class.forName(name, true, classLoader);
						//如果发生异常被外层异常捕获，返回null
					}else{
						return null;
					}
				}
				classMap.put(name, clazz);
				return clazz;
			}
		} catch (ClassNotFoundException e) {
			return null;
			//throw new ReflectException(e.getMessage());
		}
	}
	/**
	 * 基本数据类型向包装器类型转换
	 * @return
	 */

	public static Class<?> getClass(String name,boolean initialize,ClassLoader loader ){
		try {
			synchronized(classMap){
				if(classMap.containsKey(name)){
					return classMap.get(name);
				}
				
				Class<?> clazz = Class.forName(name, initialize, loader);
				classMap.put(name, clazz);
				return clazz;
			}
			
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	public static Field getField(Class<?> clazz,String fieldName){
		try {
			synchronized (fieldsMap) {
				Field[] fields = null;
				if(fieldsMap.containsKey(clazz))
				{
					fields = fieldsMap.get(clazz);
				}
				else{
					fields = clazz.getFields();
					fieldsMap.put(clazz,fields);
				}
				
				for(Field field:fields){
					if(field.getName().equals(fieldName)){
						return field;
					}
				}
				return null;
			}
			
		} catch (SecurityException e) {
			return null;
			//throw new ReflectException(e.getMessage());
		}
	}
	
	public static Field getFieldWithSuperClass(Class<?> clazz,String  fieldName){
		try {
			
			return clazz.getField(fieldName);
		} catch (NoSuchFieldException e){
			Class<?> superClazz = clazz.getSuperclass();
			if(superClazz!=null){
				try {
					return superClazz.getField(fieldName);
				} catch (NoSuchFieldException | SecurityException e1) {
					return null;
				}
			}
			return null;
		}
		catch (SecurityException e ) {
			return null;
		}
	}
		
	public static void setValue(Field field,Object dest,Object value){
		field.setAccessible(true);
		try {
			field.set(dest, value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new ReflectException(e.getMessage());
		}
	}
	
	public static Object getValue(Field field,Object dest){
		field.setAccessible(true);
		try {
			return field.get(dest);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new ReflectException(e.getMessage());
			//e.printStackTrace();
		}
	}
	
	
	
	public static boolean isNummic(Class<?> clazz){
		if(Number.class.isAssignableFrom(clazz)){
			return true;
		}
		else{
			if(clazz.isPrimitive()){
				Class<?> wraper = primitiveToWrapper(clazz);
				if(Number.class.isAssignableFrom(wraper)){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}
	}
	
	
	//@SuppressWarnings("hiding")
	public static Class<?> primitiveToWrapper(Class<?> type){
		if(!type.isPrimitive()){
			return null;
		}else{
			switch(type.getName()){
			case "int":
				return Integer.class;
			case "char":
				return Character.class;
			case "float":
				return Float.class;
			case "double":
				return Double.class;
			case "long":
				return Long.class;
			case "byte":
				return Byte.class;
			case "boolean":
				return Boolean.class;
			case "short":
				return Short.class;
			case "String":
				return String.class;
				default:
					return null;
			}
		}
	}
	
	public static Class<?> wrapperToPrimitive(Class<?> type){
		if(!Number.class.isAssignableFrom(type)){
			return null;
		}else{
			switch(type.getSimpleName()){
			case "Integer":
				return int.class;
			case "Float":
				return float.class;
			case "Double":
				return double.class;
			case "Long":
				return long.class;
			case "Boolean":
				return boolean.class;
			case "Character":
				return Character.class;
			case "String":
				return String.class;
			case "Byte":
				return byte.class;
			case "Short":
				return Short.class;
			default :
				return null;
			}
		}
	}
	

	private void setMethod(Object object,String fieldName,Object value){
//		Class clazz = object.getClass();
//	
//		Methodclazz.getMethods();
	}
	
	private   Object getMethod(Object object,String fieldName){
		Class clazz = object.getClass();
		Method[] methods = clazz.getMethods();
		for(Method method:methods){
			if(method.getName()
					.toLowerCase().equals(
							"get"+fieldName.toLowerCase())){
				try {
					return  method.invoke(object);
				} catch (IllegalAccessException e) {
					throw new ReflectException(e.getMessage());
				} catch (IllegalArgumentException e) {
					throw new ReflectException(e.getMessage());
				} catch (InvocationTargetException e) {
					throw new ReflectException(e.getMessage());
				}
				
			}
		}
		return  null;
	}
	
	
	
	
	
	
}
