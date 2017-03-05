package cn.bronzeware.core.ioc.test;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.core.ioc.ApplicationContextAware;
import cn.bronzeware.core.ioc.annotation.Component;
import cn.bronzeware.muppet.util.log.Logger;

/**
 * Created by yuhaiqiang on 17/2/12.
 */
@Component(name="C", type=ApplicationContextAware.class)
public class C implements ApplicationContextAware {
    public C(A a){
        Logger.println("C");
    }
    public void setApplicationContext(ApplicationContext app){
        B b = app.getBean(B.class);
        Logger.debugln(b);
    }
}
