package cn.bronzeware.muppet.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import cn.bronzeware.muppet.sql.SqlType;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Type {
	public SqlType type() ;
	public int length() default -1;	
	
}

