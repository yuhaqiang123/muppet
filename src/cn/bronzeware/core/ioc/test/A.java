package cn.bronzeware.core.ioc.test;

import cn.bronzeware.core.ioc.annotation.Component;
import cn.bronzeware.core.ioc.annotation.Component.Scope;
import cn.bronzeware.muppet.util.ArrayUtil;
import cn.bronzeware.muppet.util.log.Logger;

/**
 * Created by yuhaiqiang on 17/2/12.
 */
@Component(scope=Scope.singleton)
public class A {
    public A(){
    	//ArrayUtil.println(Thread.currentThread().getStackTrace());
        Logger.debugln("A");
    }
}
