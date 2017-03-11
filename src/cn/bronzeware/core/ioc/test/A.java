package cn.bronzeware.core.ioc.test;

import cn.bronzeware.core.ioc.annotation.Component;
import cn.bronzeware.muppet.util.log.Logger;

/**
 * Created by yuhaiqiang on 17/2/12.
 */
@Component()
public class A {
    public A(){
        Logger.debugln("A");
    }
}
