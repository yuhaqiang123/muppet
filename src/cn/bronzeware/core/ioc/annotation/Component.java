package cn.bronzeware.core.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {

	public static final String NAME_DEFAULT = "";
	public String name() default NAME_DEFAULT ;
	
	public static final Class TYPE_DEFAULT = Class.class;
	
	public Class type() default Class.class;
	
	public Scope scope() default Scope.singleton;
	
	enum Scope{
		singleton,
		prototype
	}
}
