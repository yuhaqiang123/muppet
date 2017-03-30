package cn.bronzeware.core.ioc.test;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.core.ioc.ApplicationContextAware;
import cn.bronzeware.core.ioc.annotation.Component;
import cn.bronzeware.muppet.util.ArrayUtil;
import cn.bronzeware.muppet.util.log.Logger;

/**
 * Created by yuhaiqiang on 17/2/12.
 */
@TestAop
@Component(name="C")
public class C implements ApplicationContextAware {
	static int i = 0;
    public C(A a){
        Logger.println("C"+ ++i);
    }
    public void setApplicationContext(ApplicationContext app){
        B b = app.getBean(B.class);
        Logger.debugln(b);
    }
    
    public void test(){
    	System.out.println("test aop");
    }
}
