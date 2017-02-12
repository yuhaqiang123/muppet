package cn.bronzeware.core.ioc;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.core.ioc.Capable;
import cn.bronzeware.core.ioc.InitializeException;
import cn.bronzeware.core.ioc.SuchBeanNotFoundException;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

/**
 * Created by yuhaiqiang on 17/2/12.
 */
public class CapableInitialize {


    private static final String CAPABLE_NAME = "Capable";
    private ApplicationContext context;
    public CapableInitialize( ApplicationContext context){
        this.context = context;
    }
    public void initialize(Object instance ){
        Class clazz = instance.getClass();
        Class[] interfaces = clazz.getInterfaces();
        for(Class interfaceClazz:interfaces){
            if(Capable.class.isAssignableFrom(interfaceClazz)){
                String interfaceName = interfaceClazz.getName();
                String capableKey = interfaceName.substring(0,interfaceName.length() - CAPABLE_NAME.length());
                String methodName = "get" + capableKey;
                Method method = null;
                try{
                    method = clazz.getDeclaredMethod(methodName);
                }catch (NoSuchMethodException e){
                    throw new InitializeException("Initialize " + interfaceName + "  dependency, but did not find " + methodName + " method");
                }
                Parameter[] parameters = method.getParameters();
                if(Objects.nonNull(parameters) && parameters.length != 0){
                    throw new InitializeException("Initialize " + interfaceName + "  dependency, but did not find " + methodName + " method with none parameter");
                }
                try {
                    Object result = method.invoke(instance, null);
                    context.registerBean(result);
                }catch (Exception e){
                    //TODO
                }

            }
        }
    }
}
