package cn.bronzeware.core.ioc.annotation;

import java.lang.reflect.Type;

public @interface Component {

	public static final String NAME_DEFAULT = "";
	public String name() default NAME_DEFAULT ;
	
	public static final Class TYPE_DEFAULT = Class.class;
	
	public Class type() default Class.class;
	
	
	
	
	
}
