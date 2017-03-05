package cn.bronzeware.core.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {

	public boolean required() default true;
	
	enum KEY{NAME,TYPE};
	
	public static final String BEAN_NAME_DEFAULT = "";
	
	public String beanName() default BEAN_NAME_DEFAULT;
	
	public static final Class TYPE_DEFAULT = Class.class;
	
	public Class  type() default Class.class;
	
	
}
