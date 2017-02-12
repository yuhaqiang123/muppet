package cn.bronzeware.core.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yuhaiqiang on 17/2/12.
 */
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
public @interface DefaultConstructor {

}

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@interface ConstructorParameter{

}
