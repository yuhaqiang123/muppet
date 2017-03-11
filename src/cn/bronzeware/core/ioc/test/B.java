package cn.bronzeware.core.ioc.test;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.core.ioc.annotation.Autowired;
import cn.bronzeware.core.ioc.annotation.Component;
import cn.bronzeware.muppet.util.log.Logger;

/**
 * Created by yuhaiqiang on 17/2/12.
 */
@Component(name = "B",scope=Component.Scope.prototype)
public class B {
    public B(A a){
        Logger.println("BBB");
    }
    
    
    @Autowired
    public C c;

    
    @Autowired
    public A a;
    
    
    @Autowired
    public ApplicationContext applicationContext;


}
