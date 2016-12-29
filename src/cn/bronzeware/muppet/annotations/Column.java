package cn.bronzeware.muppet.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.bronzeware.muppet.sql.SqlType;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

	public String columnname() default ""; 
	public Type type() default @Type(type=SqlType.DEFAULT);
	public boolean cannull() default true;
	public boolean index() default false;
	public boolean unique() default false;
	public String[] valuein() default {""};
	public String check() default "";
	public String defaultvalue() default "";
	
	/*public ColumnType ColumnType() default ColumnType.Type1;
	enum ColumnType{
		Type1
	}
	
	@interface Type{
		public SqlType type() ;
		public int length() default -1;	
	}*/
	
}
